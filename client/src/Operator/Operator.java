package Operator;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import DataStructure.LinkedList;
import Main.Vender;
import Money.Money;
import Operator.ItemInfo.ItemCustomWindow;
import Operator.Sales.SalesWindow;

public class Operator extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JPanel money_panel;
	private LinkedList<MoneyPanel> money_panels;
	
	public static Operator operator; 
	
	public static Operator create() {
		if (operator == null)
			operator = new Operator();
		return operator;
	}
	
	private Operator() {
		
		setBounds(100, 100, 402, 490);
		
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panel_1 = new JPanel();
		add(panel_1);
		panel_1.setLayout(null);
		
		money_panel = new JPanel();
		money_panel.setBounds(12, 245, 368, 72);
		panel_1.add(money_panel);
		
		initMoneyPanel(Vender.vender.getMoney());
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(12, 351, 368, 39);
		panel_1.add(panel_6);
		
		JButton btnNewButton_2 = new JButton("수금");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleGetMoney();
			}
		});
		panel_6.add(btnNewButton_2);
		
		JButton button_1 = new JButton("보충");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAddMoney();
			}
		});
		panel_6.add(button_1);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(12, 87, 368, 74);
		panel_1.add(panel_4);
		
		JLabel lblNewLabel = new JLabel("자판기 관리자");
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 20));
		panel_4.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel panel_3 = new JPanel();
		panel_4.add(panel_3);
		
		JButton btnNewButton = new JButton("일별/월별 매출산출");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSales();
			}
		});
		panel_3.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("물건 커스터마이징");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleCustomInfo();
			}
		});
		panel_3.add(btnNewButton_1);
		
		JButton return_to_vender = new JButton("돌아가기");
		return_to_vender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleReturnToVender();
			}
		});
		return_to_vender.setBounds(12, 10, 93, 23);
		panel_1.add(return_to_vender);
		
		JButton button = new JButton("비밀번호 변경");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleChangePassword();
			}
		});
		button.setBounds(117, 10, 125, 23);
		panel_1.add(button);
	}
	
	private String getNewPassword(Password pw) {

		String input;
		boolean canChange = true;
		do {
			String msg = canChange ? "새로운 비밀번호를 입력해주세요." : "특수문자 및 숫자가 하나 이상 포함된 8자리 이상을 입력해주세요.";
			input = JOptionPane.showInputDialog(msg);
			if (input == null) return null;
		} while (!(canChange = pw.canChange(input)));
		
		return input;
	}

	// 일별/월별 매출 버튼 클릭 시 이벤트
	private void handleSales() {
		SalesWindow.sales_window.updateTable();
		Vender.vender.setContentPane(SalesWindow.sales_window);
	}

	// 물건 커스터마이징 버튼 클릭 시 이벤트
	private void handleCustomInfo() {
		ItemCustomWindow window = new ItemCustomWindow();
		Vender.vender.setContentPane(window);
	}

	// 비밀번호 변경 버튼 클릭 시 이벤트
	private void handleChangePassword() {
		Password pw = Vender.vender.getPassword();
		String new_password = getNewPassword(pw);
		if (new_password == null) return;
		pw.change(new_password);
	}

	// 돌아가기 버튼 클릭 시 이벤트
	private void handleReturnToVender() {
		
		JPanel main = Vender.vender.getContentPane();
		Vender.vender.setContentPane(main);
	}

	// 수금 버튼 클릭 시 이벤트
	private void handleGetMoney() {
		
		LinkedList<Money> money = Vender.vender.getMoney();
		
		for (int i=0, size=money.size(); i<size; ++i) {
			
			Money m = money.get(i);
			
			int amount = m.getAmount();
			
			if (amount > Vender.DEFAULT_CHANGE_AMOUNT)
				amount = Vender.DEFAULT_CHANGE_AMOUNT;
		
			m.setAmount(amount);
		}
		
		updateMoneyPanel(money);
	}

	// 보충 버튼 클릭 시 이벤트
	private void handleAddMoney() {
		
		LinkedList<Money> money = Vender.vender.getMoney();
		int amount = Vender.DEFAULT_CHANGE_AMOUNT;
		
		for (int i=0, size=money.size(); i<size; ++i) {
			
			Money m = money.get(i);
			
			if (m.getAmount() < amount)
				m.setAmount(amount);
		}
		
		updateMoneyPanel(money);
		Vender.vender.displayChangeMoney(false);	// 거스름돈 부족 표시 제거
	}
	
	public void updateMoneyPanel(LinkedList<Money> money) {
		
		for (int i=0, size=money_panels.size(); i<size; ++i) {
			MoneyPanel mp = money_panels.get(i);
			
			int price = mp.getPrice();
			int amount = Money.getMoneyAmount(money, price);
			
			mp.setAmount(amount);
		}
	}
	
	private void initMoneyPanel(LinkedList<Money> money) {
		
		money_panels = new LinkedList<>();
		
		for (int i=0, size=money.size(); i<size; ++i) {
			Money m = money.get(i);
			
			int price = m.getPrice();
			int amount = m.getAmount();
			
			MoneyPanel mp = new MoneyPanel(price, amount);
			money_panels.add(mp);
			money_panel.add(mp);
		}
	}
}
