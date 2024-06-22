package Main;

import DataStructure.LinkedList;
import Money.Money;

public class User {

	private LinkedList<Money> money;

	public User() {
		money = new LinkedList<>();
		Money.initMoneyList(money, 5);
	}

	public LinkedList<Money> getMoney() {
		return money;
	}

	public boolean hasMoney(int value) {
		int size = money.size();
		for (int i=0; i<size; ++i) {
			Money m = money.get(i);
			System.out.printf("%d %d\n", m.getPrice(), m.getAmount());
			if (m.getPrice() == value && m.getAmount() >= 1) {
				return true;
			}
		}
		return false;
	}

	public void takeMoney(int value) {
		int size = money.size();
		for (int i=0; i<size; ++i) {
			Money m = money.get(i);
			if (m.getPrice() == value) {
				m.buy();
				return;
			}
		}
	}
}