package com.hxqh.bigdata.parse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hxqh.bigdata.common.Constants;
import com.hxqh.bigdata.conf.ConfigurationManager;
import com.hxqh.bigdata.util.HubbleConnUtils;

/**
 * 初始化解析报文码表
 * 只初始化一次
 * @author wulong
 */
public class Flow_InitParseRuleTab_Job implements Runnable{
	
	private static Logger logger = Logger.getLogger(Flow_InitParseRuleTab_Job.class);

	public static boolean initParseRule() {
		
		logger.info("初始化数据库码表配置..................");
		
		BufferedReader reader = readParseSchema();
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = HubbleConnUtils.getConnection();
			logger.info("Hubble 数据库已连接..................");
			ps = CreateTabPs(conn, ps, Constants.INIT_SQL);
			if(!ps.execute()) {
				logger.info("初始化数据库码表成功............");
				logger.info("执行建表操作..正在创建码表.......");
				ps = CreateTabPs(conn, ps, Constants.CREATE_TABLE_SQL);
				if(!ps.execute()) {
					logger.info("创建码表成功.");
					ps = CreateTabPs(conn, ps, Constants.INSERT_DATA_SQL);
					try {
						dataImportHubble(reader, ps);
					} catch (IOException e) {
						HubbleConnUtils.release(conn, ps, null);
						e.printStackTrace();
					}
					HubbleConnUtils.release(conn, ps, null);
					logger.info("码表初始化成功..........");
					return true;
				}else {
					HubbleConnUtils.release(conn, ps, null);
					logger.info("创建码表失败.程序退出");
					System.exit(0);
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally {
			HubbleConnUtils.release(conn, ps, null);
		}
		return false;
	}

	//码表数据导入到hubble
	private static void dataImportHubble(BufferedReader reader, PreparedStatement ps) throws IOException {
		String line = "";
		while ((line=reader.readLine())!=null) {
			String[] split = line.split("\\|");
			try {
				for (int i = 0; i < split.length; i++) {
						ps.setString(i+1, split[i]);
					}
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	//重新生成ps
	private static PreparedStatement CreateTabPs(Connection conn, PreparedStatement ps, String sql) {
		try {
			if(ps != null)
				ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ps = null;
		try {
			return conn.prepareStatement(ConfigurationManager.getProperty(sql));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ps;
	}

	//读取配置文件  解析规则
	private static BufferedReader readParseSchema() {
		return new BufferedReader(new InputStreamReader(Flow_InitParseRuleTab_Job.class.getClassLoader().getResourceAsStream("rule.properties")));
	}

	@Override
	public void run() {
		initParseRule();
	}
}
