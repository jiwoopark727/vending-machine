package Operator.Sales;

import DataStructure.BinarySearchTree;
import DataStructure.Stack;
import DataStructure.TreeNode;

// SalesItem 을 날짜별로 묶음
public class GroupDate implements Comparable<GroupDate> {
	
	private String year_month;
	private BinarySearchTree<SalesItem> items;
	
	public GroupDate(String date) {
		
		String[] date_array = date.split("-");	// 2020-12-03
		year_month = String.format("%s-%s", date_array[0], date_array[1]);	// 년, 월 저장
		items = new BinarySearchTree<>();
	}
	
	public BinarySearchTree<SalesItem> getItems() {
		return items;
	}
	
	public String[][] toRow() {
		
		//int size = items.size();
		String[][] row = new String[][] {};
		
		//int i = 0;
		TreeNode<SalesItem> root = items.getRoot();
		
		Stack<TreeNode<SalesItem>> stack = new Stack<>();
		
		TreeNode<SalesItem> temp = root;
		
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
		SalesItem key = new SalesItem(date);
		SalesItem searched = items.find(key);

		// 찾아진게 없다면 삽입
		if (searched == null) {
			key.insertToSalesItems(type, price, amount);
			items.insert(key);
//			System.out.println("4. " + date + "추가");
		}
		// 찾아진게 있다면 안에 있는 트리에 삽입
		else {
//			System.out.println("5. 찾아진 것은 " + searched.getDate().toString());
			searched.insertToSalesItems(type, price, amount);
//			System.out.println("4. 기존에 있어서 " + date + " 에 " + type + " " + price + " " + amount + " 삽입");
		}
	}

	// 같은 달이면 삽입
	public void insert(String date, String type, String price, String amount) {
		SalesItem key = new SalesItem(date);
		SalesItem searched = null;
		
		TreeNode<SalesItem> root = items.getRoot();
		TreeNode<SalesItem> current = root;
		
		while (current != null) {
			// 현재 노드 == 찾는 값
//			System.out.println("groupdate. " + current.getData().getDate().toString() + " " + current.getData().isSameMonth(key));
			if (current.getData().isSameMonth(key)) {
				searched = current.getData();
				break;
			}
			// 현재 노드 > 찾는 값
			else if (current.getData().compareTo(key) > 0)
				current = current.getLeft();
			// 현재 노드 < 찾는 값
			else
				current = current.getRight();
		}

		// 찾아진게 없다면 삽입
		if (searched == null) {
			key.insertToSalesItems(type, price, amount);
			items.insert(key);
		}
		// 찾아진게 있다면 안에 있는 트리에 삽입
		else {
			searched.insertToSalesItems(type, price, amount);
		}
	}

	private int compare(GroupDate g) {
		return year_month.compareTo(g.year_month);
	}
	
	@Override
	public int compareTo(GroupDate g) {
		return compare(g);
	}
}
