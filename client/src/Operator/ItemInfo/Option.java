package Operator.ItemInfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Main.Vender;

import static Main.Vender.writer;
import static Main.Vender.fileWriter;

public class Option extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private BeverageLabel parent;
	
	private String option;
	private String button_text;
	
	private JLabel label;
	private JButton button;
	
	//private boolean inputing;
	
	private JTextField text_field;
	
	public Option(BeverageLabel parent, String option, String button_text) {
		
		this.parent = parent;
		
		//inputing = false;
		this.option = option;
		this.button_text = button_text;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		label = new JLabel(option);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setAlignmentX(0.5f);
		add(label);
		
		this.button = new JButton(button_text);
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleInput(option);
			}
		});
		this.button.setAlignmentX(0.5f);
		add(this.button);
	}
	
	private void setInputing(boolean value) {
		//inputing = value;
	}

	// 수정, 보충 버튼 눌렀을 시 이벤트
	private void handleInput(String oldName) {
		setInputing(true);
		
		removeAll();
		
		text_field = createTextfield();
		add(text_field);

		button = createButton("확인");
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                try {
                    handleModify(oldName);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
		});
		add(button);
		
		updateScreen();
	}

	//확인 버튼 눌렀을시 이벤트
	private void handleModify(String oldName) throws IOException {
		setInputing(false);

		String inputed = text_field.getText();
		String changed = "";

		//update.txt에 음료이름변경 항목으로 추가
			System.out.println(oldName);
			String newName = inputed;
			String result = "음료이름변경 " + oldName + " -> " + newName;
			System.out.println(result);
			fileWriter.write(result);
			fileWriter.flush();

		// 소켓에서 출력 스트림을 가져와서 메시지를 전송
		writer.println("changeName");
		writer.println(result);
		writer.println("done");

		try {
			if (parent.getNameOption() == this) {
				parent.setName(inputed);
				changed = inputed;
			} else if (parent.getPriceOption() == this) {
				int price = Integer.parseInt(inputed);
				parent.setPrice(price);
				changed = inputed;
			} else if (parent.getAmountOption() == this) {
				int amount = Integer.parseInt(inputed);
				int changed_amount = parent.addAmount(amount);
				changed = String.valueOf(changed_amount);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		option = changed;    // 바뀐 값으로 변경

		removeAll();

		label = createLabel(option);
		add(label);

		button = createButton(button_text);
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleInput(option);
			}
		});
		add(button);

		Vender.vender.saveBeverages();
		updateScreen();
		Vender.vender.updateItemInfoComponent();    // 수정된 사항 아이템 진열대에 반영
	}

	private void updateScreen() {
		revalidate();
		repaint();
	}

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setAlignmentX(0.5f);
		return label;
	}

	private JTextField createTextfield() {
		JTextField text_field = new JTextField();
		text_field.setHorizontalAlignment(SwingConstants.CENTER);
		text_field.setAlignmentX(0.5f);
		return text_field;
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setAlignmentX(0.5f);
		return button;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
		setLabel(option);
	}

	public JLabel getLabel() {
		return label;
	}

	private void setLabel(String label) {
		this.label.setText(label);
	}

	public JButton getButton() {
		return button;
	}
}