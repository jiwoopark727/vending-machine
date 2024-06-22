package Operator.ItemInfo;

import javax.swing.JPanel;

import Main.Beverage;

public class BeverageLabel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Beverage beverage;
	
	private Option name;
	private Option price;
	private Option amount;
	
	public BeverageLabel(Beverage beverage) {
		
		this.beverage = beverage;
		
		String b_name = beverage.getType();
		String b_price = String.valueOf(beverage.getPrice());
		String b_amount = String.valueOf(beverage.getAmount());
		
		name = new Option(this, b_name, "수정");
		price = new Option(this, b_price, "수정");
		amount = new Option(this, b_amount, "보충");
		
		add(name);
		add(price);
		add(amount);
	}
	
	public void setName(String value) {
		beverage.setType(value);
	}

	public void setPrice(int value) {
		beverage.setPrice(value);
	}
	
	public int addAmount(int value) {
		int amount = beverage.getAmount();
		beverage.setAmount(amount + value);
		return amount + value;
	}
	
	public Option getNameOption() {
		return name;
	}

	public Option getPriceOption() {
		return price;
	}
	
	public Option getAmountOption() {
		return amount;
	}
	
}
