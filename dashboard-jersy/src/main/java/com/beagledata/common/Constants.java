package com.beagledata.common;

/**
 * 常量接口
 */
public interface Constants {

    /**
     * hubble 数据库相关的常量
     */
    String JDBC_DATASOURCE_SIZE = "jdbc.datasource.size";
    String JDBC_DRIVER = "jdbc.hubble.driver";
    String JDBC_URL = "jdbc.hubble.url";
    String JDBC_USER = "jdbc.hubble.username";
    String JDBC_PASSWORD = "jdbc.hubble.password";

    /**
     * 任务名称：银联数据实时大屏展示数据接口
     */
    String SPARK_APP_NAME = "CIB Dashboard System";
    
    /**
     * 初始化码表
     */
    String INIT_SQL = "dashboard.parse.rule.init";
	String CREATE_TABLE_SQL = "dashboard.parse.rule.create";
	String INSERT_DATA_SQL = "dashboard.parse.rule.import";
	String SELECT_DATA_SQL = "dashboard.parse.rule.select";
}
