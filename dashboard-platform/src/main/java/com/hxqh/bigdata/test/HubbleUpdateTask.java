package com.hxqh.bigdata.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import com.hxqh.bigdata.util.ConnPool;
import scala.util.Random;

public class HubbleUpdateTask implements Runnable {

	private ConnPool connPool;
	private List<String> list;
	
	public HubbleUpdateTask(ConnPool connPool) {
		this.connPool = connPool;
	}

	public HubbleUpdateTask(ConnPool connPool, List<String> list) {
		this.connPool = connPool;
		this.list = list;
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
			conn.createStatement().executeUpdate(sql);
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
