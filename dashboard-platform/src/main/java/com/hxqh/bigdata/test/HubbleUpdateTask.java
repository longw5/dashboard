package com.hxqh.bigdata.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hxqh.bigdata.util.ConnPool;

import scala.util.Random;

public class HubbleUpdateTask implements Runnable {

	public static ConnPool connPool = new ConnPool();
	public static List<String> list = new ArrayList<>();

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
	
	@Override
	public void run() {

		String midid = UUID.randomUUID().toString().replaceAll("-", "");
		String cusid = list.get(new Random().nextInt(list.size()-1));

		String sql = "update customer set middleman='" + midid + "' where cus_id='" + cusid + "'";
		System.out.println("update sql:"+sql);
		Connection conn;
		try {
			conn = connPool.getConnection();
			int executeUpdate = conn.createStatement().executeUpdate(sql);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
