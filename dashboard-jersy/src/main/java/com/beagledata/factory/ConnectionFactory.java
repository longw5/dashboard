package com.beagledata.factory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.beagledata.util.HubbleConnUtils;


/**
 * @author wl
 */
public class ConnectionFactory {

	public Connection getConnection(){
		
		Connection conn = null;
		try {
			conn = HubbleConnUtils.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public void release(Connection conn, PreparedStatement ps, ResultSet rs){
		HubbleConnUtils.release(conn, ps, rs);
	}
	
	public static void main(String[] args) throws SQLException {
		
		Connection connection = new ConnectionFactory().getConnection();
		
		System.out.println(connection.isClosed());
		
		
	}
	
}

