package Money;

import DataStructure.LinkedList;

public abstract class Money implements Cloneable {

	public static final int[] money_unit = {
			10, 50, 100, 500, 1000
	};

	private final int price;
	private int amount;

	public Money(int price, int amount) {
		this.price = price;
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int value) {
		amount = value;
	}

	public void buy() {
		--amount;
	}

	public int getPrice() {
		return price;
	}

	public int getFullPrice() {
		return amount * price;
	}

	public static int getFullPrice(LinkedList<Money> money) {
		int size = money.size();
		int sum = 0;

		for (int i=0; i<size; ++i)
			sum += money.get(i).getFullPrice();

		return sum;
	}

	public static int getBillPrice(LinkedList<Money> money) {
		int size = money.size();
		int sum = 0;

		for (int i=0; i<size; ++i) {
			Money m = money.get(i);
			if (m instanceof Bill)
				sum += m.getFullPrice();
		}

		return sum;
	}

	public static int getMoneyAmount(LinkedList<Money> money, int price) {
		int size = money.size();

		for (int i=0; i<size; ++i) {
			Money m = money.get(i);
			if (m.getPrice() == price)
				return m.amount;
		}

		return 0;
	}

	public static void addMoney(LinkedList<Money> money, int value) {
		int size = money.size();

		for (int i=0; i<size; ++i) {
			Money m = money.get(i);
			if (m.price == value) {
				++m.amount;
				break;
			}
		}
	}

	public static void addMoney(LinkedList<Money> money, Money value) {
		int size = money.size();

		for (int i=0; i<size; ++i) {
			Money m = money.get(i);
			if (m.price == value.price) {
				m.amount += value.amount;
				break;
			}
		}
	}

	public static void addMoney(LinkedList<Money> money, LinkedList<Money> value) {
		int size = money.size();
		int value_size = value.size();

		for (int i=0; i<size; ++i) {
			boolean b = false;
			Money m = money.get(i);

			for (int j=0; j<value_size; ++j) {
				Money value_m = value.get(j);

				if (m.price == value_m.price) {
					m.amount += value_m.amount;
					b = true;
					break;
				}
			}

			if (b) break;
		}
	}

	public static LinkedList<Money> subMoney(LinkedList<Money> money, int value) {
		// 자판기의 money 에서 빼올거고(거스름돈), inputed 에서도 빼올거

		LinkedList<Money> removed = new LinkedList<>();

		do {
			int size = money.size();
			// 화폐는 오름차순으로 정렬되어 있기 때문에 뒤에서 부터 보면서 빼는게 효율적
			for (int i=size-1; i>=0; --i) {
				Money m = money.get(i);

				int price = m.price;
				int amount = m.amount;

				// 가져갈 값을 Money 의 가격으로 나눠서 그게 개수보다 작거나 같으면 개수를 빼면되고
				// 개수보다 크다면 Money 는 없애버리고 새로운 Money 를 찾으러 간다
				int sub = value/price;
				if (sub <= amount) {
					amount -= sub;
					value = 0;
				}
				else {
					sub = amount;
					amount = 0;
					value = value - amount * price;
				}

				removed.add(Money.createMoneyWithType(price, sub));
				m.setAmount(amount);

				if (value == 0) break; // 다 뺏었음
				else continue;
			}
		} while (value > 0);


		return removed;
	}

	/*
	 * 금액을 1000부터 내림차순으로 나눠서 몇번 나눠지는지(몫) 으로 Money 생성하고
	 * 몫 * 금액을 value 에서 빼준다! 그리고 나머지가 있는지 검사해서 있으면 반복하고
	 * 나머지가 없다면 그대로 거스름돈 반환!
	 */
	public static LinkedList<Money> addChange(LinkedList<Money> money, int value) {
		int prices[] = { 1000, 500, 100, 50, 10 };

		int mok;
		int namoji;

		for (int i=0; i<prices.length; ++i) {
			int price = prices[i];
			if (value < price) continue;

			mok = value / price;
			namoji = value % price;

			for (int j=0; j<mok; ++j)
				Money.addMoney(money, price);

			value -= mok * price;

			if (namoji == 0) break;
		}

		return money;
	}

	public static void initMoneyList(LinkedList<Money> list, int amount) {
		if (list != null) {
			for (int i=0; i<money_unit.length; ++i) {
				Money m = Money.createMoneyWithType(money_unit[i], amount);
				list.add(m);
			}
		}
	}

	private static Money createMoneyWithType(int price, int amount) {
		Money m;
		if (price >= 1000)
			m = new Bill(price, amount);
		else
			m = new Coin(price, amount);
		return m;
	}

	@Override
	public Money clone() {
		try {
			return (Money) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}