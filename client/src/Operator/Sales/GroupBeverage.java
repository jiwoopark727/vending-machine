package Operator.Sales;

import DataStructure.BinarySearchTree;
import DataStructure.Stack;
import DataStructure.TreeNode;

// SalesItem 을 날짜별로 묶음
public class GroupBeverage implements Comparable<GroupBeverage> {
	
	//private static int ATTRIBUTE_LENGTH = 5;
	
	private String type;
	private BinarySearchTree<GroupDate> items;
	
	public GroupBeverage(String type) {
		
		this.type = type;
		items = new BinarySearchTree<>();
	}
	
	public BinarySearchTree<GroupDate> getItems() {
		return items;
	}
	
	public String[][] toRow() {
		
		//int size = items.size();
		String[][] row = new String[][] {};
		
		//int i = 0;
		TreeNode<GroupDate> root = items.getRoot();
		
		Stack<TreeNode<GroupDate>> stack = new Stack<>();
		
		TreeNode<GroupDate> temp = root;
		
		int sum = 0;

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
			String[][] _row = temp.getData().toRow();

			// 매출 계산
			for (int i=0; i<_row.length; ++i) {
				int sales = Integer.parseInt(_row[i][4]);
				sum += sales;
			}
			
			row = MyDate.append(row, _row);	// 두 배열 합치는 함수
			
			/*****************************/
			
			temp = temp.getRight();
		}
		
		row = MyDate.append(row, new String[][] {{
			null, null, null, null, String.valueOf(sum)
		}});
		
		return row;
	}

	public void insertToSalesItems(String date, String type, String price, String amount) {
		GroupDate key = new GroupDate(date);
		GroupDate searched = items.find(key);

		// 찾아진게 없다면 삽입
		if (searched == null) {
			key.insertToSalesItems(date, type, price, amount);
			items.insert(key);
		}
		// 찾아진게 있다면 안에 있는 트리에 삽입
		else {
			searched.insertToSalesItems(date, type, price, amount);
		}
	}
	
	public String getType() {
		return type;
	}
	
	private int compare(GroupBeverage g) {
		return type.compareTo(g.type);
	}
	
	@Override
	public int compareTo(GroupBeverage g) {
		return compare(g);
	}
}
