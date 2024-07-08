package com.demo.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	//getConnConnection
	//singleton pattern
		private static Connection conn;
		
		public static Connection getConnConnection() {
			try {
			if(conn==null) {
				String url="jdbc:mysql://localhost:3306/testdb?useSSL=false";
				String username="root";
				String password="root";
				DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
				conn=DriverManager.getConnection(url,username,password);
		//		conn.setAutoCommit(false);
			}	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return conn;
		}
		
		public static void closeMyConnection() {
			
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
}
