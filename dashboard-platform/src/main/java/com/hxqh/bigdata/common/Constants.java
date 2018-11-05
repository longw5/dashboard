package com.hxqh.bigdata.common;

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
     * 项目配置相关的常量
     */
    String SPARK_LOCAL = "spark.local";
    String SPARK_LOCAL_TASKID_SESSION = "spark.local.taskid.session";
    String SPARK_LOCAL_TASKID_PAGE = "spark.local.taskid.page";
    String SPARK_LOCAL_TASKID_PRODUCT = "spark.local.taskid.product";
    String SPARK_LOCAL_TASKID_AREA = "spark.local.taskid.area";
    /**
     * Spark作业相关的常量
     */
    String SPARK_APP_NAME_SESSION = "UserVisitSessionAnalysis";
    
    /**
     * kafka配置参数
     */
    String KAFKA_METADATA_BROKER_LIST = "metadata.broker.list";
	String KAFKA_KEY_SERIALIZER_CLASS = "kafka.serializer.StringEncoder";
	String KAFKA_SERIALIZER_CLASS = "kafka.serializer.StringEncoder";
	String KAFKA_REQUEST_REQUIRED_ACKS = "kafka_request_required_acks";
	String KAFKA_COMPRESSION_CODEC = "kafka_compression_codec";
	String KAFKA_MESSAGE_SEND_MAX_RETRIES = "kafka_message_send_max_retries";
	String KAFKA_RECONNECT_BACKOFF_MS = "kafka_reconnect_backoff_ms";
	String KAFKA_REQUEST_TIMEOUT_MS = "kafka_request_timeout_ms";
	String KAFKA_PRODUCER_TYPE = "kafka_producer_type";
	String KAFKA_QUEUE_BUFFERING_MAX_MS = "kafka_queue_buffering_max_ms";
	String KAFKA_QUEUE_BUFFERING_MAX_MESSAGES = "kafka_queue_buffering_max_messages";
	String KAFKA_QUEUE_ENQUEUE_TIMEOUT_MS = "kafka_queue_enqueue_timeout_ms";
	String KAFKA_BATCH_NUM_MESSAGES = "kafka_batch_num_messages";
	String KAFKA_SEND_BUFFER_BYTES = "kafka_send_buffer_bytes";
	String KAFKA_TOPICS = "kafka.topics";
	String KAFKA_TOPICS_KEY = "kafka.topics.key";
	String KAFKA_TOPICS_MSG = "kafka.topics.msg";

    /**
     * 初始化码表
     */
    String INIT_SQL = "dashboard.parse.rule.init";
	String CREATE_TABLE_SQL = "dashboard.parse.rule.create";
	String INSERT_DATA_SQL = "dashboard.parse.rule.import";
	String SELECT_DATA_SQL = "dashboard.parse.rule.select";
}
