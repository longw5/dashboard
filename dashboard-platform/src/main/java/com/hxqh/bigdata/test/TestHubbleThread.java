package com.hxqh.bigdata.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hxqh.bigdata.util.ConnPool;

public class TestHubbleThread {

	public static HubbleUpdateTask updatetask = new HubbleUpdateTask();
	public static HubbleInsertTask inserttask = new HubbleInsertTask();

	public static void main2(String[] args) throws SQLException {

		ConnPool connPool = new ConnPool();
		Connection conn = connPool.getConnection();
		PreparedStatement ps = conn.prepareStatement("show tables");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {

			String string = rs.getString(1);
			System.out.println(string);
		}
	}

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
}
