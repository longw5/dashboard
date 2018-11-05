package com.beagledata.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beagledata.factory.ConnectionFactory;

/**
 * @author wl
 */
public class DashBoardRTDAO extends ConnectionFactory {

	private static DashBoardRTDAO instance;
	
	
	public static DashBoardRTDAO getInstance(){
		if(instance == null)
			instance = new DashBoardRTDAO();
		return instance;
	}
	
	public Map<String, Object> listRt(){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		Map<String, Object> map = new HashMap<>();
		List<Map<String, String>> list = new ArrayList<>();
		
		conn = getConnection();
		try {
			ps = conn.prepareStatement("select * from datagram_parse_rule");
			rs = ps.executeQuery();
			while(rs.next()){
				
				Map<String, String> record = new HashMap<>();
				
				record.put("var_trans", rs.getString(1));
				record.put("var_id", rs.getString(2));
				record.put("var_cn_name", rs.getString(3));
				record.put("var_en_code", rs.getString(4));
				record.put("var_length", rs.getString(5));
				record.put("var_start_at", rs.getString(6));
				record.put("var_end_at", rs.getString(7));
				list.add(record);
			}
			map.put("success", "0");
			map.put("record", list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			release(conn, ps, rs);
		}
		return map;
	}
	
	public static void main(String[] args) {
		
		Map<String, Object> listRt = DashBoardRTDAO.getInstance().listRt();
		System.out.println(listRt);
	}
	
}
