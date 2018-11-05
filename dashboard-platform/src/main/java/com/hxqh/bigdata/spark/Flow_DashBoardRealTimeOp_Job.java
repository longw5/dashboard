package com.hxqh.bigdata.spark;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.hxqh.bigdata.common.Constants;
import com.hxqh.bigdata.conf.ConfigurationManager;
import com.hxqh.bigdata.domain.MsgParser;
import com.hxqh.bigdata.parse.Flow_InitParseRuleMapping_Job;

import kafka.serializer.StringDecoder;
import scala.Tuple2;

/**
 * 实时读取kafka数据，处理数据
 * @author wulong
 */
public class Flow_DashBoardRealTimeOp_Job {

	private static Logger logger = Logger.getLogger(Flow_DashBoardRealTimeOp_Job.class);

	public static void sparkOpKafkaS() {

		// 配置spark参数
		SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName(Constants.SPARK_APP_NAME);
		sparkConf.set("spark.streaming.blockInterval", "50");

		JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(5));

		// javaStreamingContext.checkpoint("hdfs://192.168.145.101:9000/streaming_checkpoint");

		// 配置kafka集群参数
		Map<String, String> kafkaParams = new HashMap<>(10);
		kafkaParams.put(Constants.KAFKA_METADATA_BROKER_LIST,
				ConfigurationManager.getProperty(Constants.KAFKA_METADATA_BROKER_LIST));

		// 构建topic set
		String kafkaTopics = ConfigurationManager.getProperty(Constants.KAFKA_TOPICS);
		String[] kafkaTopicsSplited = kafkaTopics.split(",");
		Set<String> topics = new HashSet<>();
		for (String kafkaTopic : kafkaTopicsSplited) {
			topics.add(kafkaTopic);
		}

		// 获取kafka流处理对象
		JavaPairInputDStream<String, String> kafkaDStream = KafkaUtils.createDirectStream(javaStreamingContext,
				String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);

		logger.info("获取到流对象...............");

		// 数据流处理
		kafkaSOpStep(kafkaDStream);

		javaStreamingContext.start();
		try {
			javaStreamingContext.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		javaStreamingContext.close();
	}

	@SuppressWarnings("serial")
	private static void kafkaSOpStep(JavaPairInputDStream<String, String> kafkaDStream) {

		Map<String, MsgParser> map = Flow_InitParseRuleMapping_Job.getMsgParser();
		
		kafkaDStream.map(new Function<Tuple2<String, String>, String>() {

			// 获取此时段内的流数据集
			public String call(Tuple2<String, String> v1) throws Exception {
				return v1._2();
			}
		}).foreachRDD(new VoidFunction<JavaRDD<String>>() {

			@Override
			public void call(JavaRDD<String> v) throws Exception {
				
				List<String> collect = v.collect();
				
				//增加指标运算
				//新建第一个job，统计指标，更新指标库，并进行数据存储
				//计算统计各种指标，读取hubble历史统计数据，并进行更新操作
				
				//按地区号，统计近7天区域交易量，交易排序，top10
				
				
				//按时间，统计近7天交易金额
				
				
				//按小时统计当日，交易笔数
				
				
				//当日总交易量，总交易笔数，年交易量，年交易金额
				
				
				//指标存储到hubble数据库
				
				
				//第二个job,交易数据存储到hive数据库中
				//交易数据存储到hive数据库
				//开启线程异步存储数据
				bdataStoreHive(map, collect.iterator());
			}

			private void bdataStoreHive(Map<String, MsgParser> map, Iterator<String> iterator) {
				try {
					while(null != iterator && iterator.hasNext()) {
						String next = iterator.next();
						
						Set<Entry<String, MsgParser>> entrySet = map.entrySet();
						for (Entry<String, MsgParser> entry : entrySet) {
							entry.getValue().setValue(next.substring(entry.getValue().getStart(), entry.getValue().getEnd()));
							if(entry.getValue().getLength()==entry.getValue().getValue().length()) {
								logger.info(entry.getValue());
								
								//持久化存储
								//存储到hive中
								
							}else {
								//数据报文异常，截取错误
							}
						}
						logger.info("存储到hive中.........");
						logger.info("============>处理下一条消息。");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		// kafka+sparkstreaming 实时解析，处理数据
		new Flow_DashBoardRealTimeOp_Job().sparkOpKafkaS();
	}

}
