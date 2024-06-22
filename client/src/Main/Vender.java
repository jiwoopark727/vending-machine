package Main;

import Operator.Password;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import DataStructure.LinkedList;
import Money.Money;
import Operator.Operator;
import Operator.Sales.SalesSaveThread;
import Operator.Sales.SalesWindow;

import java.net.*;

public class Vender extends JFrame {

	public static Vender vender = null;

	public static PrintWriter writer;
	public static BufferedReader fileReader3;
	public static FileWriter fileWriter;

	/* 상수 */
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_CHANGE_AMOUNT = 10;

	private static final int INPUT_BILL_LIMIT = 5000;
	private static final int INPUT_FULL_LIMIT = 7000;

	private static final String DEFAULT_PASSWORD = "00000000";

	public static final Font DEFAUlT_FONT = new Font("굴림", Font.PLAIN, 12);

	public static Socket socket;

	/* 사용자 */
	private User user;

	private LinkedList<Beverage> beverages;
	private LinkedList<Money> money;
	private LinkedList<Money> inputed;

	/* GUI 컴포넌트 */
	private JPanel contentPane;
	private JLabel inputed_money_label;
	private LinkedList<JButton> input_money_buttons;
	private LinkedList<JLabel> input_money_labels;
	private LinkedList<ItemInfo> item_info_components;
	private JPanel input_money_pannel;
	private JLabel can_change_label;

	/* 자판기 관리자 비밀번호 */
	private Password password;


	private static void create() {
		if (vender == null)
			vender = new Vender();
	}

	private Vender() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(12, 12, 426, 32);
		contentPane.add(panel);

		JLabel lblNewLabel = new JLabel("자판기");
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		input_money_pannel = new JPanel();
		input_money_pannel.setBounds(12, 450, 426, 66);
		contentPane.add(input_money_pannel);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(327, 300, 111, 66);
		contentPane.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.Y_AXIS));

		JLabel lblNewLabel_1 = new JLabel("\uC785\uB825\uB41C \uAE08\uC561");
		lblNewLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_2.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);

		inputed_money_label = new JLabel("0\uC6D0");
		inputed_money_label.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_2.add(inputed_money_label);
		inputed_money_label.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnNewButton = new JButton("\uBC18\uD658");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				writer.println("데이터 변경된걸 여따가 넣으면돼 대신 String 이나 int 이런걸로 객체는 안돼");
				returnMoney();
				displayInputedMoney();
			}
		});
		btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_2.add(btnNewButton);

		JPanel item_display_panel = new JPanel();
		item_display_panel.setBounds(12, 85, 426, 180);
		contentPane.add(item_display_panel);

		// 유저 초기화
		initUser();

		// 음료 목록 초기화
		initBeverages();

		// 아이템 정보 담을 리스트 초기화
		initItemInfoComponent(item_display_panel);

		JPanel can_change_panel = new JPanel();
		can_change_panel.setBounds(327, 380, 111, 32);
		contentPane.add(can_change_panel);

		can_change_label = new JLabel("");
		can_change_label.setHorizontalAlignment(SwingConstants.CENTER);
		can_change_panel.add(can_change_label);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(327, 420, 111, 38);
		contentPane.add(panel_1);

		JButton btnNewButton_1 = new JButton("\uAD00\uB9AC\uC790");
		panel_1.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleOperatorButton();
			}
		});

		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(label);

		password = new Password(DEFAULT_PASSWORD);

		// 거스름돈 초기화
		initMoney();

		// 입력 금액 초기화
		initInputedMoney();

		// 금액 입력 버튼 생성
		createInputMoneyButtons();

		createInputMoneyLabels();

		// 돈 입력 버튼 이벤트 추가
		initInputMoneyButtons();

		// 자판기 GUI 표시
		setVisible(true);
	}

	private void initUser() {
		user = new User();
	}

	/* 아이템 정보에 추가될 라벨, 버튼의 컴포넌트를 담을 리스트 초기화 */
	private void initItemInfoComponent(JPanel item_display_panel) {
		item_info_components = new LinkedList<>();
		for (int i=0; i<beverages.size(); ++i) {
			Beverage b = beverages.get(i);
			ItemInfo info = new ItemInfo(this, item_display_panel, b);
			item_info_components.add(info);
		}
	}

	public LinkedList<Money> getInputedMoney() {
		return inputed;
	}

	public LinkedList<Money> getMoney() {
		return money;
	}

	public void updateItemInfoComponent() {
		ItemInfo.updateAll(item_info_components);
	}

	/* 입력 금액 출력 */
	public void displayInputedMoney() {
		int has = Money.getFullPrice(inputed);
		String result = String.format("%,3d원", has);

		inputed_money_label.setText(result);
		System.out.printf("display: %s\n", result);
	}

	private boolean addInputMoney(int value) {
		int bill = Money.getBillPrice(inputed);
		int full = Money.getFullPrice(inputed);

		System.out.printf("%d %d %d\n", full+value, bill, value);
		if (full + value > INPUT_FULL_LIMIT
				|| (bill >= INPUT_BILL_LIMIT
					&& value >= 1000))
			return false;

		Money.addMoney(inputed, value);
		return true;
	}

	private void displayUserMoney() {
		int size = input_money_labels.size();
		LinkedList<Money> money = user.getMoney();
		for (int i=0; i<size; ++i) {
			JLabel label = input_money_labels.get(i);
			int amount = Money.getMoneyAmount(money, Money.money_unit[i]);
			label.setText(String.valueOf(amount));
		}
	}

	public void displayChangeMoney(boolean value) {
		//int money = Money.getFullPrice(money);

		if (value) {
			can_change_label.setText("거스름돈 부족");
		} else {
			can_change_label.setText("");
		}
	}

	/* 금액 입력 버튼 클릭 시 이벤트 */
	private void handleInputMoney(int value) {
		if (user.hasMoney(value) && addInputMoney(value)) {
			user.takeMoney(value);
			displayInputedMoney();
			displayUserMoney();
			updateItemInfoComponent();
		}
		else
			System.out.println("추가할 수 없음");
	}

	private void returnMoney() {
		for (;inputed.size() > 0;) {
			Money m = inputed.remove(0);
			if (m.getAmount() <= 0) continue;
			System.out.printf("인출 %d %d\n", m.getPrice(), m.getAmount());

			LinkedList<Money> user_money = user.getMoney();
			Money.addMoney(user_money, m);

			displayUserMoney();
		}
		updateItemInfoComponent();
		initInputedMoney();
	}

	////////////////////////////////////////////////////

	/* 음료 재고 초기화(default) */

	private void initBeverages() {

		beverages = new LinkedList<>();

		String[] paths = new String[] {
			"물.jpg", "커피.jpg", "이온음료.jpg", "고급커피.jpg", "탄산음료.jpg", "특화음료.jpg"
		};

		File file = new File(".\\src\\data\\beverages.txt");

		// 저장된 음료 정보가 있다면 그 정보로 불러옴
		if (file.exists()) {

			int i = 0;

			try {
				FileReader reader_f = new FileReader(file);
				BufferedReader reader;
				reader = new BufferedReader(reader_f);

				String current = null;

				// 파일의 끝까지 검사
				while ((current = reader.readLine()) != null) {
					String[] token = current.split(" ");	// 품목, 단가, 개수로 분리

					String type = token[0];
					int price = Integer.parseInt(token[1]);
					int amount = Integer.parseInt(token[2]);

					Beverage b = new Beverage(paths[i], type, price);
					b.setAmount(amount);

					beverages.add(b);
					++i;
				}

				reader.close();
				reader_f.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

		// 음료 정보가 없다면 기본값으로 초기화
		} else {

			String[] types = new String[] {
					"물", "커피", "이온음료", "고급커피", "탄산음료", "특화음료"
			};
			Integer[] prices = new Integer[] {
					450, 500, 550, 700, 750, 800
			};
			for (int i=0; i<types.length; ++i) {
				Beverage b = new Beverage(paths[i], types[i], prices[i]);
				beverages.add(b);
			}

		}

	}

	/* 음료 정보 저장 */
	public void saveBeverages() {

		File file = new File(".\\src\\data\\beverages.txt");

		try {
			FileWriter writer_f = new FileWriter(file, false);
			BufferedWriter writer;
			writer = new BufferedWriter(writer_f);

			/* 실행부분 */

			for (int i=0, size=beverages.size(); i<size; ++i) {

				Beverage b = beverages.get(i);

				// 이름 가격 재고
				String type = b.getType();
				int price = b.getPrice();
				int amount = b.getAmount();

				writer.write(String.format("%s %d %d\n", type, price, amount));
			}

			writer.close();
			writer_f.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/* 거스름돈 초기화(default) */
	private void initMoney() {
		money = new LinkedList<>();
		Money.initMoneyList(money, DEFAULT_CHANGE_AMOUNT);

		System.out.printf("자판기는 총 %,3d원을 가지고 있습니다\n", Money.getFullPrice(money));
	}

	/* 입력 금액 초기화 */
	private void initInputedMoney() {
		inputed = new LinkedList<>();
		Money.initMoneyList(inputed, 0);

		System.out.printf("입력금액 총 %,3d원을 가지고 있습니다\n", Money.getFullPrice(inputed));
	}

	/* 금액 입력 버튼 생성 */
	private void createInputMoneyButtons() {

		if (input_money_buttons == null)
			input_money_buttons = new LinkedList<>();

		for (int b : Money.money_unit) {
			JButton btn = new JButton(String.valueOf(b));
			input_money_buttons.add(btn);
		}
	}

	/* 금액 입력 버튼 밑 사용자 보유 개수 라벨 생성 */
	private void createInputMoneyLabels() {

		if (input_money_labels == null)
			input_money_labels = new LinkedList<>();

		LinkedList<Money> money = user.getMoney();

		for (int unit : Money.money_unit) {
			int amount = Money.getMoneyAmount(money, unit);
			JLabel label = new JLabel(String.valueOf(amount));
			input_money_labels.add(label);
		}
	}

	public LinkedList<ItemInfo> getItemInfoes() {
		return item_info_components;
	}

	public LinkedList<Beverage> getBeverages() {
		return beverages;
	}

	public void buy(int price) {
		int change = Money.getFullPrice(inputed) - price;
		System.out.println("거슴름돈은" + change);

		LinkedList<Money> price_money = Money.subMoney(inputed, price);

		System.out.printf("[1] money: %d\n", Money.getFullPrice(money));

		if (Money.getFullPrice(price_money) != price) {
			Money.addChange(money, price);
		}
		else
			Money.addMoney(money, price_money);		// 음료를 샀으므로 자판기가 돈을 먹음

		System.out.printf("[2] money: %d\n", Money.getFullPrice(money));
		Money.subMoney(money, change);	// 거스름돈을 자판기에서 뺌, money 에 값 추가하고 빼는 것보다 가격에서 거스름돈 뺀 값을 빼는게 효율적
		System.out.printf("[3] money: %d\n", Money.getFullPrice(money));

		initInputedMoney();
		Money.addChange(inputed, change);	// 입력금액에 거스름돈 넣어줌
		displayInputedMoney();
	}

	public boolean canBuy(Beverage b) {
		if (inputed == null) return false;
		int full = Money.getFullPrice(inputed);			// 입력된 금액이 충족하는지 체크
		return full >= b.getPrice() && canChange(b);
	}

	public boolean canChange(Beverage b) {

		boolean can = false;
		do {
			if (money == null) break;

			int full = Money.getFullPrice(inputed);		// 입력된 금액이 충족하는지 체크
			if (full < b.getPrice()) break;

			int full_change = Money.getFullPrice(money) + b.getPrice();	// 자판기에 거스름돈 있는지 체크

			if (full_change < (full - b.getPrice())) break;

			can = true;
			break;
		} while(true);

		displayChangeMoney(!can);
		return can;
	}

	/* 금액 입력 버튼 이벤트 추가 */
	private void initInputMoneyButtons() {
		if (input_money_buttons != null) {

			for (int i=0; i<input_money_buttons.size(); ++i) {
				JButton btn = input_money_buttons.get(i);

				btn.addActionListener(new ActionListener() {
					final int money = Integer.parseInt(btn.getText());

					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println(money);
						handleInputMoney(money);
					}
				});

				btn.setAlignmentX(Component.CENTER_ALIGNMENT);
				JLabel label = input_money_labels.get(i);
				label.setAlignmentX(Component.CENTER_ALIGNMENT);

				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				panel.add(btn);
				panel.add(input_money_labels.get(i));
				input_money_pannel.add(panel);
			}

		}
	}

	/////////////////////////////////////////////

	private void handleOperatorButton() {

		boolean isCorrect = true;
		do {
			int result = showPasswordInput(isCorrect);
			if (result < 0)
				isCorrect = false;
			else if (result == 0)
				return;
		}
		while (!isCorrect);

		Operator operator = Operator.create();
		operator.updateMoneyPanel(money);	// 자판기 소지 금액 업데이트
		setContentPane(operator);
	}

	private int showPasswordInput(boolean isCorrect) {
		JPanel panel = new JPanel();

		String msg = isCorrect ?  "관리자 비밀번호를 입력해주세요." : "올바른 비밀번호를 입력해주세요.";
		JLabel label = new JLabel(msg);
		JPasswordField pass = new JPasswordField(10);
		panel.add(label);
		panel.add(pass);
		String[] options = new String[]{"확인", "취소"};
		int option = JOptionPane.showOptionDialog(null, panel, "관리자 비밀번호 입력",
		                            JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
		                            null, options, options[1]);

		if(option == 0) // 확인버튼 눌렀을 때
		{
		       String password = new String(pass.getPassword());

		       if (this.password.equals(password))
		    	   return 1;
		       else
		    	   return -1;
		}

		return 0;
	}

	public Password getPassword() {
		return password;
	}

	/////////////////////////////////////////////

	private void updateScreen() {
		revalidate();
		repaint();
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	@Override
	public void setContentPane(Container contentPane) {
		super.setContentPane(contentPane);
		updateScreen();
	}


	public static void main(String[] args) throws IOException {
		String hostname = "192.168.25.180";
		int port = 8000;
		String filePath1 = "src/data/update.txt"; // 전송할 파일의 경로
		String filePath2 = "src/data/beverages.txt"; // 전송할 파일의 경로
		fileReader3 = new BufferedReader(new FileReader(filePath1));
		fileWriter = new FileWriter("src/data/update.txt");

		try {
			socket = new Socket(hostname, port);
			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);

			/** NOTE
			 * 서버에 텍스트 파일 데이터 전송
			 * 서버는 받은 데이터를 클라랑 동일하게 세팅
			 * 변경사항이 생기면 서버랑 클라 둘 다 업데이트 하기 (파일)
			 * 		(이때 클라에서 서버로 소켓 통신)
			 * 	음료 판매, 재고?, 매출, 비밀번호
			 */

			// 파일 읽기
			//update.txt 파일  보내기
			String line;
			writer.println("update");
			while ((line = fileReader3.readLine()) != null) {
				writer.println(line);
			}

			writer.println("done");


			//beverages.txt파일 보내기
			BufferedReader fileReader2 = new BufferedReader(new FileReader(filePath2));
			writer.println("beverage");
			String line2;
			while ((line2 = fileReader2.readLine()) != null) {
				writer.println(line2);
			}

			writer.println("done");

			System.out.println("File sent to the server.");
		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
		}


		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					SalesWindow.create();	// 자판기 관리자 매출산출 창 생성(먼저 생성해두어야 자판기의 매출이 반영됨)
					Vender.create();		// 자판기 창 생성

					Thread sales_save_thread = new SalesSaveThread(10000);		// 10초마다 매출 정보 저장
					sales_save_thread.start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

