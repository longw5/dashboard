package com.hxqh;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.hxqh.bigdata.util.HubbleConnUtils;

public class TestHubble {

	//测试hubble数据库的批量入库
	@SuppressWarnings("unused")
	public static void main3(String[] args) {
		
		Connection conn = HubbleConnUtils.getConnection();

		try {
			System.out.println("数据库连接:" + (!conn.isClosed()));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			DatabaseMetaData metaData = conn.getMetaData();
			boolean supportsBatchUpdates = metaData.supportsBatchUpdates();
			System.out.println(supportsBatchUpdates);
			
			Statement statement = conn.createStatement();
			for (int i = 0; i < 10; i++) {
				String sql = "insert into test(id, name) values('"+ new Random().nextInt(100000000)+ "', '张三')";
				statement.addBatch(sql);
			}
			int[] executeBatch = statement.executeBatch();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//测试hubble数据库的数据单条插入功能
	@SuppressWarnings({ "unused", "rawtypes" })
	public static void main2(String[] args) {

		Connection conn = HubbleConnUtils.getConnection();

		try {
			System.out.println("数据库连接:" + (!conn.isClosed()));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Map> list = new ArrayList<>();

		String sql = "insert into test(id, name) values(?, ?)";
		try {
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, new Random().nextInt(100000000)+"");
			ps.setString(2, "张三");
			
			int executeUpdate = ps.executeUpdate();

			System.out.println(executeUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//测试hubble数据库的简单查询
	@SuppressWarnings({ "rawtypes", "unused" })
	public static void main(String[] args) {

		Connection conn = HubbleConnUtils.getConnection();

		try {
			System.out.println(conn.isClosed());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Map> list = new ArrayList<>();
		String sql = "select * from datagram_parse_rule";
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				String id = rs.getString("var_trans");
				String name = rs.getString("var_id");
				System.out.println(id + ":" + name);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
