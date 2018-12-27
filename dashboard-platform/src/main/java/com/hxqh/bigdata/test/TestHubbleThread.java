package com.hxqh.bigdata.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hxqh.bigdata.util.ConnPool;

public class TestHubbleThread {

	private static List<String> list = new ArrayList<>();
	private static ConnPool connPool = ConnPool.newInstance(50);
	private static HubbleUpdateTask updatetask = new HubbleUpdateTask(connPool, list);
	private static HubbleInsertTask inserttask = new HubbleInsertTask(connPool);

	static{
		list.add("10081");
		list.add("10323");
		list.add("10237");
		list.add("10774");
		list.add("10245");
		list.add("10236");
		list.add("10987");
		list.add("10321");
		list.add("10723");
		list.add("10819");
		list.add("10077");
		list.add("10180");
		list.add("10108");
		list.add("10149");
		list.add("10660");
		list.add("10960");
		list.add("10360");
	}
	
	/**
	 * 测试并发
	 * @param args
	 */
	public static void main(String[] args) {

		ExecutorService executorService = Executors.newFixedThreadPool(20);
		
		for (int i = 0; i < 20; i++) {
			if (i % 2 == 0) {
				executorService.execute(updatetask);
			} else {
				executorService.execute(inserttask);
			}
		}
		executorService.shutdown();
	}

	/**
	 * 测试连接
	 * @param args
	 * @throws SQLException
	 */
	public static void main2(String[] args) throws SQLException {

		ConnPool connPool = ConnPool.newInstance(10);
		Connection conn = connPool.getConnection();
		PreparedStatement ps = conn.prepareStatement("show tables");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			String string = rs.getString(1);
			System.out.println(string);
		}
	}
}
