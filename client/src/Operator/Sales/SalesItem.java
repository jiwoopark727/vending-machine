package Operator.Sales;

import java.time.LocalDate;

import DataStructure.BinarySearchTree;
import DataStructure.Stack;
import DataStructure.TreeNode;

// 표에서 한 행이 될 객체
public class SalesItem implements Comparable<SalesItem> {

	private static int ATTRIBUTE_LENGTH = 5;
	
	private LocalDate sales_date;
	private BinarySearchTree<SalesItemNoDate> items;
	
	public SalesItem(String date) {
		
		sales_date = MyDate.getDateOf(date);
		items = new BinarySearchTree<SalesItemNoDate>();
	}

	public void insertToSalesItems(String type, String price, String amount) {
		
		try {
			
			SalesItemNoDate key = new SalesItemNoDate(type, price, amount);
			SalesItemNoDate searched = items.find(key);

			// 찾아진게 없다면 삽입
			if (searched == null) {
				items.insert(key);
//				System.out.println("1. 추가됨" + type + " 루트는 " + items.getRoot());
			}
			// 찾아진게 있다면 안에 있는 개수만 늘려줌
			else {
				int amount_i = Integer.parseInt(amount);
				searched.addAmount(amount_i);
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public String[][] toRow() {
		
		int size = items.size();
		String[][] row = new String[size][ATTRIBUTE_LENGTH];
		
		int i = 0;
		TreeNode<SalesItemNoDate> root = items.getRoot();
//		System.out.println("2. " + root);
		
		Stack<TreeNode<SalesItemNoDate>> stack = new Stack<>();
		
		TreeNode<SalesItemNoDate> temp = root;

		// 중위 순회
		while (true) {
			while (temp != null) {
				stack.push(temp);
				temp = temp.getLeft();
			}
			
			temp = stack.pop();
			if (temp == null) break;

			/* 실행부분 */

			// 날짜별로 테이블 만듬
			row[i++] = temp.getData().toRow(sales_date.toString());
			System.out.printf("%s %s %s %s\n", row[i-1][0], row[i-1][1], row[i-1][2], row[i-1][3]);
			
			/*****************************/
			
			temp = temp.getRight();
		}
		
		return row;
	}
	
	public BinarySearchTree<SalesItemNoDate> getItems() {
		return items;
	}
	
	public boolean isSameMonth(SalesItem item) {
		return sales_date.getMonthValue() == item.getDate().getMonthValue();
	}
	
	public LocalDate getDate() {
		return sales_date;
	}
	
	private int compare(SalesItem item) {
		LocalDate date = item.getDate();
		
		String sales_date_s = sales_date.toString();
		String date_s = date.toString();
		
		return sales_date_s.compareTo(date_s);
	}
	
	@Override
	public int compareTo(SalesItem s) {
		return compare(s);
	}
	
}
