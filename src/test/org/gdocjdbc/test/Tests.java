package org.gdocjdbc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

public class Tests extends TestCase {

	/*
	 * !!!!!!!!!!!You need to set username and password here!!!!!!!!!!!!
	 * 
	 */
	String username = "";
	String password = "";
	
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

			
			Connection con = DriverManager.getConnection("jdbc:gdocjdbc", username, password);
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'");
			
			
	
			while (rs.next()) {
				String s = rs.getString("TABLE_NAME");
				String s2 = rs.getString("TABLE_TYPE");
				System.out.println("Name = " + s + " type=" + s2 + " rowcount: " + getRowCount(s, con));
				
				}

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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