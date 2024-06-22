package Operator;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MoneyPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private int price;
	private int amount;
	
	private JLabel amount_label;
	
	public MoneyPanel(int price, int amount) {
		
		this.price = price;
		this.amount = amount;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		String price_s = String.valueOf(price);
		String amount_s = String.valueOf(amount);
		
		JLabel price_label = new JLabel(price_s);
		price_label.setFont(new Font("굴림", Font.PLAIN, 12));
		price_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		price_label.setHorizontalAlignment(SwingConstants.CENTER);
		add(price_label);
		
		amount_label = new JLabel(amount_s);
		amount_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		amount_label.setHorizontalAlignment(SwingConstants.CENTER);
		add(amount_label);
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
		update();
	}
	
	private void update() {
		String text = String.valueOf(amount);
		amount_label.setText(text);
	}
}
