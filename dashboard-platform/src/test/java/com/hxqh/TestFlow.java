package com.hxqh;

import org.apache.log4j.Logger;

import com.hxqh.bigdata.kafka.Flow_KafkaProdecer_Job;

public class TestFlow {

	private static Logger logger = Logger.getLogger(TestFlow.class);

	public static void main(String[] args) {
		
		logger.info("测试流程开始启动............");
		
		// 初始化数据解析码表
		logger.info("初始化数据码表............");
//		@SuppressWarnings("static-access")
//		boolean initParseRule = new Flow_InitParseRuleTab_Job().initParseRule();
//		logger.info("initParseRule : " + initParseRule);

		// 模拟kafka，生产消息数据
		// 启动kafka生产线程，模拟收发消息
		logger.info("模拟kafka生产数据............");
		try {
			new Thread(new Flow_KafkaProdecer_Job()).start();
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		// kafka+sparkstreaming 实时解析，处理数据
		logger.info("kafkaS实时处理数据............");
//		new Flow_DashBoardRealTimeOp_Job().sparkOpKafkaS();

		// 数据统计，指标提取
		
		
	}

}
