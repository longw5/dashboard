package com.beagledata.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.beagledata.common.Constants;
import com.beagledata.conf.ConfigurationManager;

public class HubbleConnUtils {
	
	private static final String driver = ConfigurationManager.getProperty(Constants.JDBC_DRIVER);
	private static final String url = ConfigurationManager.getProperty(Constants.JDBC_URL);
	private static final String username = ConfigurationManager.getProperty(Constants.JDBC_USER);
	private static final String password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD);
	
	private HubbleConnUtils(){}
	
	static{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		Connection connection = null;
		java.util.Properties info = new java.util.Properties();
		info.put("user", username);
		info.put("password", password);
		try {
			connection = DriverManager.getConnection(url, info);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public static void release(Connection conn,Statement st,ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(st != null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
		Connection conn = HubbleConnUtils.getConnection();
		
		try {
			System.out.println(conn.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
