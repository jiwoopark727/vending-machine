package Operator.Sales;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import DataStructure.BinarySearchTree;
import DataStructure.Stack;
import DataStructure.TreeNode;
import Main.Beverage;
import Main.Vender;
import Operator.Operator;

public class SalesWindow extends JPanel {
	
	private static final long serialVersionUID = 1L;
	public static SalesWindow sales_window;
	
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JLabel date_label;
	private JComboBox<String> beverage_combo_box;
	
	private boolean is_group_date;
	
	private BinarySearchTree<GroupDate> sales_items_grouped_date;
	private BinarySearchTree<GroupBeverage> sales_items_grouped_beverage;

	public static SalesWindow create() {
		if (sales_window == null)
			sales_window = new SalesWindow();
		return sales_window;
	}
	
	private SalesWindow() {

		is_group_date = true;
		initDateGroupedItems();
		initBeverageGroupedItems();
		load();
		
		setBounds(100, 100, 402, 490);
		
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		contentPane = new JPanel();
		add(contentPane);
		contentPane.setLayout(null);
		
		JButton return_to_operator = new JButton("돌아가기");
		return_to_operator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleReturnToOperator();
			}
		});
		return_to_operator.setBounds(12, 10, 93, 23);
		contentPane.add(return_to_operator);
		
		date_label = createDateLabel();
		contentPane.add(date_label);
		
		JButton previous_month = new JButton("<");
		previous_month.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handlePrevious();
			}
		});
		previous_month.setBounds(117, 10, 41, 23);
		contentPane.add(previous_month);
		
		JButton next_month = new JButton(">");
		next_month.setBounds(225, 10, 41, 23);
		contentPane.add(next_month);
		next_month.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleNext();
			}
		});
		
		beverage_combo_box = createComboBox();
		beverage_combo_box.setBounds(269, 10, 107, 23);
		contentPane.add(beverage_combo_box);
		
		JTable table = createTableWithDate(date_label.getText());
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(12, 43, 366, 404);
		contentPane.add(scrollPane);
	}
	
	private void updateComboBox() {
		
		contentPane.remove(beverage_combo_box);
		
		beverage_combo_box = createComboBox();
		beverage_combo_box.setBounds(269, 10, 107, 23);
		
		contentPane.add(beverage_combo_box);
		
		revalidate();
		repaint();
	}
	
	private JLabel createDateLabel() {
		
		LocalDate current = LocalDate.now();
		
		int year = current.getYear();
		int month = current.getMonthValue();
		
		JLabel label = new JLabel(String.format("%d-%d", year, month));
		label.setFont(new Font("굴림", Font.PLAIN, 15));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(157, 12, 69, 19);
		
		return label;
	}
	
	private void initDateGroupedItems() {
		sales_items_grouped_date = new BinarySearchTree<>();
	}
	
	private void initBeverageGroupedItems() {
		sales_items_grouped_beverage = new BinarySearchTree<>();
	}
	
	private JComboBox<String> createComboBox() {
		
		TreeNode<GroupBeverage> root = sales_items_grouped_beverage.getRoot();
		
		Stack<TreeNode<GroupBeverage>> stack = new Stack<>();
		
		JComboBox<String> beverage_combo_box = new JComboBox<>();
		beverage_combo_box.addItem("");	// 공백 선택 시 모든 음료 표시
		
		TreeNode<GroupBeverage> temp = root;

		// 중위 순회
		while (true) {
			while (temp != null) {
				stack.push(temp);
				temp = temp.getLeft();
			}
			
			temp = stack.pop();
			if (temp == null) break;

			/* 실행부분 */
			
			String type = temp.getData().getType();
			beverage_combo_box.addItem(type);
			
			/*****************************/
			
			temp = temp.getRight();
		}
		
		beverage_combo_box.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				handleSelectBeverage();
			}
		});
		
		return beverage_combo_box;
	}
	
	private JTable createTableWithDate(String date) {
		
		String[] attributes = new String[]{"날짜", "품목", "단가", "개수", "매출"};
		String[][] contents = new String[][] {};
		
		GroupDate key = new GroupDate(date+"-01");
		GroupDate searched = sales_items_grouped_date.find(key);
		
		int sum = 0;

		if (searched != null) {
			
			TreeNode<SalesItem> root = searched.getItems().getRoot();
			
			Stack<TreeNode<SalesItem>> stack = new Stack<>();
			
			TreeNode<SalesItem> temp = root;


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

				// 날짜별로 테이블 만듬
				contents = MyDate.append(contents, temp.getData().toRow());
				
				/*****************************/
				
				temp = temp.getRight();
			}
			
		}
		
		contents = MyDate.append(contents, new String[][] {{
			null, null, null, null, String.valueOf(sum)
		}});
		
		JTable table = new JTable(contents, attributes);
		table.setEnabled(false);
		return table;
	}
	
	private JTable createTableWithBeverage(String type) {
		
		String[] attributes = new String[]{"날짜", "품목", "단가", "개수", "매출"};
		String[][] contents = new String[][] {};
		
		GroupBeverage key = new GroupBeverage(type);
		GroupBeverage searched = sales_items_grouped_beverage.find(key);
		
		int sum = 0;
		
		if (searched != null) {
			
			String date = date_label.getText();
			GroupDate _key = new GroupDate(date+"-01");
			GroupDate _searched = searched.getItems().find(_key);
			
			if (_searched != null) {
				
				TreeNode<SalesItem> root = _searched.getItems().getRoot();
				
				Stack<TreeNode<SalesItem>> stack = new Stack<>();
				
				TreeNode<SalesItem> temp = root;


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

					// 날짜별로 테이블 만듬
					contents = MyDate.append(contents, temp.getData().toRow());
					
					/*****************************/
					
					temp = temp.getRight();
				}
			}
		}
		
		contents = MyDate.append(contents, new String[][] {{
			null, null, null, null, String.valueOf(sum)
		}});
		
		JTable table = new JTable(contents, attributes);
		table.setEnabled(false);
		return table;
	}
	
	public void updateTable() {

		JTable table;
		if (is_group_date) {
			String date = date_label.getText();
			table = createTableWithDate(date);
		}
		else {
			String type = (String)beverage_combo_box.getSelectedItem();
			table = createTableWithBeverage(type);
		}
		
		scrollPane.setViewportView(table);
		
		revalidate();
		repaint();
	}
	
	private void setDateLabel(String date) {
		date_label.setText(date);
	}
	
	private String dateToString(LocalDate date) {
		
		String[] _date = date.toString().split("-");	// 2020-12-03
		
		return String.format("%s-%s", _date[0], _date[1]);
	}
	
	private void handleSelectBeverage() {
		
		String type = (String) beverage_combo_box.getSelectedItem();

		switch (type) {
		case "":	// 공백일 경우 모든 매출 표시
			setGroupDateMode(true);
			break;
		default:	// 나머지의 경우 type 에 맞는 음료들 매출만 표시
			setGroupDateMode(false);
			break;
		}
		
		updateTable();
	}
	
	private void setGroupDateMode(boolean value) {
		is_group_date = value;
	}
	
	private void handlePrevious() {
		
		String[] date = date_label.getText().split("-");	// 2024-12
		
		try {
			int year = Integer.parseInt(date[0]);
			int month = Integer.parseInt(date[1]);
			
			LocalDate _date = LocalDate.of(year, month, 1);
			_date = _date.minusMonths(1);
			
			String format = dateToString(_date);
			setDateLabel(format);
			
			updateTable();
		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	private void handleNext() {
		
		String[] date = date_label.getText().split("-");	// 2024-12
		
		try {
			int year = Integer.parseInt(date[0]);
			int month = Integer.parseInt(date[1]);
			
			LocalDate _date = LocalDate.of(year, month, 1);
			_date = _date.plusMonths(1);
			
			String format = dateToString(_date);
			setDateLabel(format);
			
			updateTable();
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	private void handleReturnToOperator() {
		JPanel main = Operator.operator;
		Vender.vender.setContentPane(main);
	}
	
	private void insertToGroupDate(String date, String type, String price, String amount) {
		GroupDate key = new GroupDate(date);	// 날짜별로 나눠야 되므로 date 을 넘겨줌
		GroupDate searched = sales_items_grouped_date.find(key);

		// 찾아진게 없다면 삽입
		if (searched == null) {
			key.insertToSalesItems(date, type, price, amount);
			sales_items_grouped_date.insert(key);
		}
		// 찾아진게 있다면 안에 있는 트리에 삽입
		else {
			searched.insertToSalesItems(date, type, price, amount);
		}
	}
	
	private void insertToGroupBeverage(String date, String type, String price, String amount) {
		GroupBeverage key = new GroupBeverage(type);	// ���Ằ�� ������ �ǹǷ� type �� �Ѱ���
		GroupBeverage searched = sales_items_grouped_beverage.find(key);

		// 찾아진게 없다면 삽입
		if (searched == null) {
			key.insertToSalesItems(date, type, price, amount);
			sales_items_grouped_beverage.insert(key);
		}
		// 찾아진게 있다면 안에 있는 트리에 삽입
		else {
			searched.insertToSalesItems(date, type, price, amount);
		}
	}
	
	public void save() {
		
		File file = new File(".\\src\\data\\sales.txt");
		
		try {
			FileWriter writer_f = new FileWriter(file, false);
			BufferedWriter writer;
			writer = new BufferedWriter(writer_f);
			
			TreeNode<GroupDate> root = sales_items_grouped_date.getRoot();
			Stack<TreeNode<GroupDate>> stack = new Stack<>();
			TreeNode<GroupDate> temp = root;

			// 중위 순회
			while (true) {
				while (temp != null) {
					stack.push(temp);
					temp = temp.getLeft();
				}
				
				temp = stack.pop();
				if (temp == null) break;

				/* 실행부분 */
				
				BinarySearchTree<SalesItem> items = temp.getData().getItems();

				TreeNode<SalesItem> _root = items.getRoot();
				Stack<TreeNode<SalesItem>> _stack = new Stack<>();
				TreeNode<SalesItem> _temp = _root;

				// 중위 순회
				while (true) {
					while (_temp != null) {
						_stack.push(_temp);
						_temp = _temp.getLeft();
					}
					
					_temp = _stack.pop();
					if (_temp == null) break;

					/* 실행부분 */
					
					String[][] item = _temp.getData().toRow();	// ��¥ ǰ�� �ܰ� ����
					for (int i=0; i<item.length; ++i) {
						// 마지막 항목인 매출은 빼고 추가
						String[] info = item[i];
						
						String date = info[0];
						String type = info[1];
						String price = info[2];
						String amount = info[3];
						
						writer.write(String.format("%s %s %s %s\n", date, type, price, amount));
					}
					
					/*****************************/
					
					_temp = _temp.getRight();
				}

				
				/*****************************/
				
				temp = temp.getRight();
			}
			
			writer.close();
			writer_f.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void load() {
		
		File file = new File(".\\src\\data\\sales.txt");
		
		try {
			FileReader reader_f = new FileReader(file);
			BufferedReader reader;
			reader = new BufferedReader(reader_f);
		
			String current = null;

			// 파일의 끝까지 검사
			while ((current = reader.readLine()) != null) {
				String[] token = current.split(" ");	// 날짜, 품목, 단가, 개수로 분리
				
				String date = token[0];
				String type = token[1];
				String price = token[2];
				String amount = token[3];
				
				insertToGroupDate(date, type, price, amount);
				insertToGroupBeverage(date, type, price, amount);
			}
			
			reader.close();
			reader_f.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void insertToSalesList(Beverage b) {
		
		String date = LocalDate.now().toString();
		String type = b.getType();
		String price = String.valueOf(b.getPrice());
		String amount = String.valueOf(1);
		
		insertToGroupDate(date, type, price, amount);
		insertToGroupBeverage(date, type, price, amount);
		
		updateComboBox();
	}
}
