package com.hxqh.bigdata.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取连接的工具类
 * @author wds
 *
 */
public class JdbcUtil {

    //数据库连接池
    private static ConnPool  connPool = new ConnPool();

    /**
     * 从池中获取一个连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException{
        return connPool.getConnection();
    }

    /**
     * 关闭连接
     * @param conn
     * @param st
     * @param rs
     * @throws SQLException 
     */
    public static void CloseConnection(Connection conn, Statement st, ResultSet rs) throws SQLException{

        // 关闭存储查询结果的ResultSet对象
        if(rs != null){
                rs.close();
        }

        //关闭Statement对象
        if(st != null){
                st.close();
        }

        //关闭连接
        if(conn != null){
                conn.close();
        }
    }
    
    /**
     * orm映射
     * 查询单条数据
     * @param sql
     * @param className
     * @return
     */
    public static Map<String, String> querySingle(String sql, String className){
    	
    	Map<String, String> map = new HashMap<>();
    	Connection conn = null;
    	Statement st = null;
    	ResultSet rs = null;
    	Class<?> class1;
    	
    	try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()) {
				try {
					class1 = Class.forName(className);
					Field[] declaredFields = class1.getDeclaredFields();
					for (int i = 0; i < declaredFields.length; i++) {
						String field = declaredFields[i].getName();
						String value = rs.getString(field);
						map.put(field, value);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
    	} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			HubbleConnUtils.release(conn, st, rs);
		}
    	return map;
    }
    
    
    /**
     * orm映射
     * @param sql
     * @param className
     * @return
     */
    public static List<Map<String, String>> query(String sql, String className){
    	
    	List<Map<String, String>> list = null;
    	Connection conn = null;
    	Statement st = null;
    	ResultSet rs = null;
    	Class<?> class1;
    	
    	try {
			conn = JdbcUtil.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()) {
				list = new ArrayList<>();
				Map<String, String> map = null;
				try {
					class1 = Class.forName(className);
					map = new HashMap<>();
					Field[] declaredFields = class1.getDeclaredFields();
					for (int i = 0; i < declaredFields.length; i++) {
						String field = declaredFields[i].getName();
						String value = rs.getString(field);
						map.put(field, value);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				list.add(map);
			}
    	} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			HubbleConnUtils.release(conn, st, rs);
		}
    	return list;
    }

}