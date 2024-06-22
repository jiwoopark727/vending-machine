package Operator.Sales;

import java.time.LocalDate;

public class MyDate {
	
	public static LocalDate getDateOf(String date) {
		
		String[] date_array = date.split("-"); // 2024-06-06 형식으로 제공
		
		try {
			int year = Integer.parseInt(date_array[0]);
			int month = Integer.parseInt(date_array[1]);
			int day_of_month = Integer.parseInt(date_array[2]);
			
			return LocalDate.of(year, month, day_of_month);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
    public static String[][] append(String[][] a, String[][] b) {
    	String[][] result = new String[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
	
}
