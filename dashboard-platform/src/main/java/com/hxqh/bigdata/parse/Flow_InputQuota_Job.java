package com.hxqh.bigdata.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.Set;

import com.hxqh.bigdata.conf.ConfigurationManager;

/**
 * 
 * 提取指标可配置话操作，读取提取指标配置文件
 * @author wulong
 *
 */
public class Flow_InputQuota_Job implements Serializable{

	private static final long serialVersionUID = 1L;

	//导入需要提取的指标文件
	@SuppressWarnings("unused")
	public Set<Object> getQuotas() {
		Properties prop = new Properties();
		InputStream in = ConfigurationManager.class.getClassLoader().getResourceAsStream("quota.properties");
        try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.keySet();
	}
}
