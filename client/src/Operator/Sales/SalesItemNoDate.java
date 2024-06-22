package Operator.Sales;

public class SalesItemNoDate implements Comparable<SalesItemNoDate> {

	private static int ATTRIBUTE_LENGTH = 5;
	
	private String type;
	private int price;
	private int amount;
	
	public SalesItemNoDate(String type, String price, String amount) {
		this(type, Integer.parseInt(price), Integer.parseInt(amount));
	}
	
	public SalesItemNoDate(String type, int price, int amount) {
		this.type = type;
		this.price = price;
		this.amount = amount;
	}
	
	public void addAmount(int value) {
		amount += value;
	}
	
	public String[] toRow(String date) {
		String[] row = new String[ATTRIBUTE_LENGTH];
		
		row[0] = date;
		row[1] = type;
		row[2] = String.valueOf(price);				// 단가
		row[3] = String.valueOf(amount);			// 개수
		row[4] = String.valueOf(price * amount);	// 매출
		
		return row;
	}
	
	public String getType() {
		return type;
	}
	
	private int compare(SalesItemNoDate item) {
		return type.compareTo(item.type);
	}
	
	@Override
	public int compareTo(SalesItemNoDate s) {
		return compare(s);
	}
}
