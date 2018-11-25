package com.hxqh.bigdata.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PoolTest {

    /**
     * 测试数据库连接池
     * @param args
     */
    @SuppressWarnings("all")
    public static void main(String[] args) {
        JdbcUtil util = new JdbcUtil();
        try {
            Connection conn = util.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("select * from datagram_parse_rule");
            while(rs.next()) {
            	System.out.println(rs.getString(1));
            }
            
            if(conn != null){
                System.out.println("我得到了一个连接");
            }
            util.CloseConnection(conn, null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}