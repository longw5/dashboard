package com.hxqh.bigdata.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import com.hxqh.bigdata.util.ConnPool;

import scala.util.Random;

public class HubbleInsertTask implements Runnable {

	public static ConnPool connPool = new ConnPool();

	@Override
	public void run() {

		String midid = UUID.randomUUID().toString().replaceAll("-", "");
		int cusid = new Random().nextInt(100000000);

		String sql = "insert into middleman_relation values ('"+cusid+"','"+midid+"')";
		System.out.println("insert sql:"+sql);
		Connection conn;
		try {
			conn = connPool.getConnection();
			int executeUpdate = conn.createStatement().executeUpdate(sql);
			System.out.println(executeUpdate);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
