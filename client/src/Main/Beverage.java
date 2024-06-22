package Main;

import java.io.*;
import java.time.LocalDate;

import Operator.Sales.SalesWindow;

import static Main.Vender.writer;

public class Beverage {
	private String type;
	private int price;
	private int amount;
	private final String path;
	
	private static final int BEVERAGE_AMOUNT = 10;
	
	public Beverage(String path, String type, int price) {
		this(path, type, price, BEVERAGE_AMOUNT);
	}
	
	private Beverage(String path, String type, int price, int amount) {
		this.path = path;
		this.type = type;
		this.price= price;
		this.amount = amount;
	}

	// 재고 보충. 변경된 재고를 반환
	public int addItem(int amount) {
		this.amount += amount;
		return this.amount;
	}
	
	public boolean buy() {
		if (isExist()) {
			--amount;
			SalesWindow.sales_window.insertToSalesList(this);	// 매출 기록

			// 품절 시 파일 출력
			if (amount <= 0)
				saveSoldOut();
			return true;
		}
		return false;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int value) {
		amount = value;
	}

	// 재고가 없으면 false
	public boolean isExist() {
		boolean is_exist = amount > 0;
		return is_exist;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getPrice() {
		return price;
	}
	
	public String getPath() {
		return path;
	}
	
	private void saveSoldOut() {
		
		File file = new File(".\\src\\data\\soldout.txt");
		
		try {
			
			FileWriter fileWriter = new FileWriter(file, true);
			
			String date = LocalDate.now().toString();

			fileWriter.write(String.format("%s %s %d 재고 소진\n", date, type, price));
			
			/* 이 부분에 재고 소진이면 서버에 재고 소진 메시지 전송*/
			// 소켓에서 출력 스트림을 가져와서 메시지를 전송
			String message = String.format("%s %s %d 재고 소진", date, type, price);
			writer.println("soldOut");
			writer.println(message);  // 줄바꿈을 포함하여 메시지 전송
			writer.println("done");

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
