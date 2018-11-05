package com.hxqh.bigdata.parse;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hxqh.bigdata.common.Constants;
import com.hxqh.bigdata.conf.ConfigurationManager;
import com.hxqh.bigdata.domain.MsgParser;
import com.hxqh.bigdata.util.HubbleConnUtils;

/**
 * 建立码表映射 只初始化一次
 * @author wulong
 */
public class Flow_InitParseRuleMapping_Job implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Map<String, MsgParser> MSG_PARSER = null;
	private static Logger logger = Logger.getLogger(Flow_InitParseRuleMapping_Job.class);

	private static Map<String, MsgParser> initParseMapping() {

		logger.info("初始化码表映射..................");

		if(MSG_PARSER!=null) {
			return MSG_PARSER;
		}else {
			MSG_PARSER = new HashMap<>();
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
	
			//预留过滤指标处理规则
//			Set<Object> quotas = new Flow_InputQuota_Job().getQuotas();
	
			try {
				conn = HubbleConnUtils.getConnection();
				logger.info("Hubble 数据库已连接..................");
				ps = CreateTabPs(conn, ps, Constants.SELECT_DATA_SQL);
				rs = ps.executeQuery();
	
				while (rs.next()) {
					String var_en_code = rs.getString("var_en_code");
					int var_length = rs.getInt("var_length");
					int var_start_at = rs.getInt("var_start_at");
					int var_end_at = rs.getInt("var_end_at");
					
					/*if(quotas.contains(var_en_code)) {
						//执行过滤操作
						MSG_PARSER.put(操作);
					}*/
					
					//目前解析全部报文
					MSG_PARSER.put(var_en_code.toUpperCase(),
							new MsgParser(var_en_code, var_length, var_start_at, var_end_at, null));
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				HubbleConnUtils.release(conn, ps, null);
			}
			logger.info("需要解析的指标及规则 : " + MSG_PARSER);
//			logger.info("需要解析的指标及规则个数 : " + MSG_PARSER.size());
			return MSG_PARSER;
		}
	}

	// 重新生成ps
	private static PreparedStatement CreateTabPs(Connection conn, PreparedStatement ps, String sql) {
		try {
			if (ps != null)
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
	
	public static Map<String, MsgParser> getMsgParser() {
		return initParseMapping();
	}
}
