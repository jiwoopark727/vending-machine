package Operator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class Password {

	private static final int LENGTH = 8;
	private static final int SECRET_KEY = 113;	// 부호화/복호화에 쓰일 키
	
	private String password;
	
	public Password(String password) {
		if (!load())
			this.password = password;
	}
	
	public void change(String new_password) {
		
		if (canChange(new_password)) {
			password = new_password;
			save();
		}
	}
	
	public boolean equals(String value) {
		return password.equals(value);
	}
	
	public boolean canChange(String new_password) {
		if (new_password.length() < LENGTH)
			return false;
		System.out.printf("%b %b\n", checkNumber(new_password), checkSpecial(new_password));
		if (!checkNumber(new_password))
			return false;
		if (!checkSpecial(new_password))
			return false;
		return true;
	}
	
	private void save() {
		
		File file = new File(".\\src\\data\\pw.dat");
		
		try {
			
			FileWriter writer = new FileWriter(file);	
			writer.write(encrypt());
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean load() {
		
		File file = new File(".\\src\\data\\pw.dat");

		if (file.exists()) {
			try {
			
				FileReader reader_f = new FileReader(file);
				BufferedReader reader = new BufferedReader(reader_f);
				
				String pw = reader.readLine();
				password = decrypt(pw);
				
				System.out.println("비밀번호: "+password);
				
				reader.close();
				reader_f.close();

				return true;
				
			} catch (FileNotFoundException e) {
				System.out.println("비밀번호: "+password);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("비밀번호: "+password);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private String encrypt() {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<password.length(); ++i) {
			
			char c = password.charAt(i);
			
			builder.append((char)((int)c^SECRET_KEY));
		}
		
		return builder.toString();
	}
	
	private String decrypt(String password) {
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<password.length(); ++i) {
			
			char c = password.charAt(i);
			
			builder.append((char)((int)c^SECRET_KEY));
		}

		return builder.toString();
	}
	
	private boolean checkNumber(String value) {
		Pattern pattern = Pattern.compile("[0-9]");
		return pattern.matcher(value).find();
	}
	
	private boolean checkSpecial(String value) {
		Pattern pattern = Pattern.compile("[~!@#$%^&*()_+|<>?:{}]");
		return pattern.matcher(value).find();
	}
}
