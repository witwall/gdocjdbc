package org.gdocjdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.TestCase;

public class Tests extends TestCase {

	/*
	 * !!!!!!!!!!!You need to set username and password here!!!!!!!!!!!!
	 * 
	 */
	String username = "blah@example.com";
	String password = "mypassword";
	
	public void testGoogleDocJDBC() {
		try {
			//Driver driver = 
			try {
				Class.forName("org.gdocjdbc.jdbcDriver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			//Connection con = 
			
			
			while(true) {
				Properties dbProps = new Properties();
				dbProps.setProperty("user", username);
				dbProps.setProperty("password", password);
				dbProps.setProperty("cacheExpire", "10000");
				getResults(DriverManager.getConnection("jdbc:gdocjdbc", dbProps));
				Thread.sleep(1000 * 10);
			}

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
	}
	
	public void testCreateTables() {
		
		
		try {
			//Driver driver = 
			try {
				Class.forName("org.gdocjdbc.jdbcDriver").newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			//Connection con = 
			
			
		
				Properties dbProps = new Properties();
				dbProps.setProperty("user", username);
				dbProps.setProperty("password", password);
				dbProps.setProperty("cacheExpire", "10000");
				getResults(DriverManager.getConnection("jdbc:gdocjdbc", dbProps));
				
				

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	private static void getResults(Connection con) throws SQLException {
		Statement stmt = con.createStatement();
		//ResultSet rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'");
		ResultSet rs = stmt.executeQuery("SELECT * FROM simpsonscharacters_Sheet1 where character='Homer Simpson'");
		

		while (rs.next()) {
//			String s = rs.getString("TABLE_NAME");
//			String s2 = rs.getString("TABLE_TYPE");
			String s = rs.getString("VOICEDBY");
			String s2 = rs.getString("FIRSTAPPEARANCE");
			//System.out.println("Name = " + s + " type=" + s2 + " rowcount: " + getRowCount(s, con));
			System.out.println("VOICEDBY = " + s + " AND FIRSTAPPEARANCE=" + s2);
			
			}
	}
	
	
	public void testbreak() {
		String myString = "pickles.test@gmail.com";
		String myString2 = myString.toLowerCase();
	}
	
	private Integer getRowCount(String tablename, Connection con) throws SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT count(*) as MYCOUNT FROM " + tablename);
		Integer count = null;
		
		while (rs.next()) {
			count = rs.getInt("MYCOUNT");
		}
		
		return count;
	}
	
}
