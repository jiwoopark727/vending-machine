package Operator.Sales;

public class SalesSaveThread extends Thread {
	
	private long wait;
	
	public SalesSaveThread(long wait) {
		this.wait = wait;
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(wait);	// wait 밀리초마다 매출 정보 저장
				
				SalesWindow.sales_window.save();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
