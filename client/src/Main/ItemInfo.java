package Main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import DataStructure.LinkedList;

import static Main.Vender.fileWriter;
import static Main.Vender.fileReader3;
import static Main.Vender.writer;

public class ItemInfo {
	
	public static final int PRODUCT_IMAGE_WIDTH = 64;
	public static final int PRODUCT_IMAGE_HEIGHT = 64;
	
	private final Vender vender;
	private final JPanel parent;	// 자판기 Panel
	private final Beverage beverage;
	
	private JPanel info_panel;		// 아이템 Panel
	
	private JLabel item_name;
	private JLabel item_image;
	private JLabel item_price;
	private JButton item_buy_btn;
	
	public ItemInfo(Vender vender, JPanel parent, Beverage beverage) {
		this.vender = vender;
		this.parent = parent;
		this.beverage = beverage;
		init();
	}

	/* 자판기 창 라벨 설정 */
	private void init() {
		createInfoPanel();
		parent.add(info_panel);
	}

	/* 현재 정보 반영하여 아이템 다시 표시 */
	public void update() {
		parent.remove(info_panel);
		createInfoPanel();
		parent.add(info_panel);
	}
	
	public static void updateAll(LinkedList<ItemInfo> infoes) {
		int size = infoes.size();
		Vender vender = null;
		for (int i=0; i<size; ++i) {
			ItemInfo info = infoes.get(i);
			info.update();
			vender = info.vender;
		}
		if (vender != null) {
			vender.revalidate();
			vender.repaint();			
		}
	}
	
	private JPanel createInfoPanel() {
		info_panel = new JPanel();
		info_panel.setLayout(new BoxLayout(info_panel, BoxLayout.Y_AXIS));	// 요소들 세로 정렬하기 위함

		// 음료 이름
		item_name = getNameLabel(beverage);

		// 음료 이미지 (입력 금액 충족 시 이미지 border 생성)
		item_image = getImageLabel(beverage);

		// 음료 가격
		item_price = getPriceLabel(beverage);

		// 구매 버튼 (품절 표시 기능 포함)
        try {
            item_buy_btn = getBuyButton(beverage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JComponent[] components = new JComponent[] {
			item_name, item_image, item_price, item_buy_btn	
		};
		
		for (JComponent component : components) {
			component.setAlignmentX(Component.CENTER_ALIGNMENT);
			info_panel.add(component);
		}
		
		return info_panel;
	}


	/* 음료 정보 받아 종류 라벨 생성 */
	private JLabel getNameLabel(Beverage b) {
		JLabel item_name = new JLabel();
		item_name.setText(b.getType());
		item_name.setHorizontalAlignment(SwingConstants.CENTER);
		item_name.setFont(Vender.DEFAUlT_FONT);
		return item_name;
	}

	/* 음료 정보 받아 이미지 생성 */
	private JLabel getImageLabel(Beverage b) {
		JLabel item_image = new JLabel();
		
		if (beverage.isExist() && vender.canBuy(beverage)) {
			LineBorder border = new LineBorder(Color.red, 1, true);
			item_image.setBorder(border);
		}
		
		ImageIcon icon = getImageIcon(b.getPath());
		item_image.setIcon(icon);
		return item_image;
	}

	/* 음료 정보 받아 가격 라벨 생성 */
	private JLabel getPriceLabel(Beverage b) {
		JLabel item_price = new JLabel();
		String price_text = String.valueOf(b.getPrice()) + "원";
		item_price.setText(price_text);
		item_price.setHorizontalAlignment(SwingConstants.CENTER);
		item_price.setFont(Vender.DEFAUlT_FONT);
		return item_price;
	}

	/* 음료 정보 받아 구매 버튼 생성 */
	private JButton getBuyButton(Beverage b) throws IOException{
		JButton item_buy_btn = new JButton();
		
		String text;
		if (beverage.isExist())
			text = "구매";
		else
			text = "품절";
		item_buy_btn.setText(text);
		
		item_buy_btn.setHorizontalAlignment(SwingConstants.CENTER);
		item_buy_btn.setFont(Vender.DEFAUlT_FONT);
		
		item_buy_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					fileWriter.write("음료판매 " + beverage.getType() + "\n");
					fileWriter.flush();

					//update.txt 파일  보내기
					writer.println("update");

					String line = fileReader3.readLine();
					writer.println(line);

					writer.println("done");

					//System.out.printf("%s: %d원\n", b.getType(), b.getPrice());
					handleBuyButton();
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		});

		return item_buy_btn;
	}
	
	private void handleBuyButton() {
		if (beverage.isExist() && vender.canBuy(beverage)) {
			beverage.buy();
			vender.buy(beverage.getPrice());
			//System.out.printf("%s 가 판매됨 %d개 남음\n", beverage.getType(), beverage.getAmount());
			updateAll(vender.getItemInfoes());
		}
	}

	/* 경로로부터 ImageIcon 을 가져옴. */
	private ImageIcon getImageIcon(String path) {
		path = String.format("../images/%s", path);
		URL url = this.getClass().getResource(path);
		
		ImageIcon icon = new ImageIcon(url);
		Image img = icon.getImage();
		
		img = img.getScaledInstance(PRODUCT_IMAGE_WIDTH, PRODUCT_IMAGE_HEIGHT, Image.SCALE_SMOOTH);	// 칸에 맞춰 이미지 사이즈 조절
		icon = new ImageIcon(img);
		
		return icon;
	}
}
