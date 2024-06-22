package Operator.ItemInfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import DataStructure.LinkedList;
import Main.Beverage;
import Main.Vender;
import Operator.Operator;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

public class ItemCustomWindow extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public ItemCustomWindow() {
		setBounds(100, 100, 402, 490);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(12, 43, 378, 437);
		add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JButton btnNewButton_1 = new JButton("돌아가기");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleReturnToVender();
			}
		});
		btnNewButton_1.setBounds(12, 10, 97, 23);
		add(btnNewButton_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(105, 10, 197, 34);
		add(panel_1);
		panel_1.setLayout(new GridLayout(0, 3, 0, 0));
		
		JLabel lblNewLabel = new JLabel("이름");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("가격");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("재고");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel_2);
		
		LinkedList<Beverage> beverages = Vender.vender.getBeverages();
		for (int i=0, size=beverages.size(); i<size; ++i) {
			Beverage b = beverages.get(i);
			
			BeverageLabel label = new BeverageLabel(b);
			System.out.println();
			panel.add(label);
		}
	}

	// 돌아가기 버튼 클릭 시 이벤트
	private void handleReturnToVender() {
		
		JPanel main = Operator.operator;
		Vender.vender.setContentPane(main);
	}
}
