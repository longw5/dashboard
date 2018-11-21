package com.hxqh.bigdata.spark;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import com.hxqh.bigdata.common.Constants;
import com.hxqh.bigdata.conf.ConfigurationManager;
import com.hxqh.bigdata.domain.MsgParser;
import com.hxqh.bigdata.parse.Flow_InitParseRuleMapping_Job;

import scala.Tuple2;

/**
 * 1 实时读取kafka数据，处理数据
 * 
 * @author wulong
 */
public class Flow_DashBoardRealTimeOp_Job {

	private static Logger logger = Logger.getLogger(Flow_DashBoardRealTimeOp_Job.class);

	public static void sparkOpKafkaS() {

		// 配置spark参数
		SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName(Constants.SPARK_APP_NAME);
//		SparkConf sparkConf = new SparkConf().setAppName(Constants.SPARK_APP_NAME);
		sparkConf.set("spark.streaming.blockInterval", "50");

		JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(5));

		javaStreamingContext.checkpoint("D://streaming_checkpoint");

		// 配置kafka集群参数
		Map<String, Object> kafkaParams = new HashMap<>(10);
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
		JavaInputDStream<ConsumerRecord<String, String>> kafkaDStream = org.apache.spark.streaming.kafka010.KafkaUtils
			.createDirectStream(javaStreamingContext, LocationStrategies.PreferConsistent(),
					ConsumerStrategies.Subscribe(topics, kafkaParams));
		
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
	private static void kafkaSOpStep(JavaInputDStream<ConsumerRecord<String, String>> kafkaDStream) {

		Map<String, MsgParser> map = Flow_InitParseRuleMapping_Job.getMsgParser();

		JavaDStream<Map<String,String>> kafkaStreamMap = kafkaDStream.map(new Function<ConsumerRecord<String,String>, Map<String, String>>() {

			@Override
			public Map<String, String> call(ConsumerRecord<String, String> record) throws Exception {
				
				Map<String, String> rtMap = new HashMap<>();
				
				String event = record.value();
				
				Set<Entry<String, MsgParser>> entrySet = map.entrySet();
				
				for (Entry<String, MsgParser> entry : entrySet) {
					entry.getValue()
							.setValue(event.substring(entry.getValue().getStart(), entry.getValue().getEnd()));
					if (entry.getValue().getLength() == entry.getValue().getValue().length()) {
						logger.info(entry.getValue());
						// 持久化存储
						rtMap.put(entry.getKey(), entry.getValue().getValue());
					} else {
						// 数据报文异常，截取错误
					}
				}
				return rtMap;
			}
		});
		
		kafkaStreamMap.mapToPair(new PairFunction<Map<String,String>, String, Integer>() {

			@Override
			public Tuple2<String, Integer> call(Map<String, String> map) throws Exception {
				
				int year = Calendar.getInstance().get(Calendar.YEAR);
				
				String day = map.get("DATA_ME").substring(0, 4);
				String hours = map.get("DATA_ME").substring(4, 8);
				return new Tuple2<String, Integer>(year+"_"+day+"_"+hours, 1);
			}
		}).reduceByKey(new Function2<Integer, Integer, Integer>() {
			
			@Override
			public Integer call(Integer v1, Integer v2) throws Exception {
				return v1+v2;
			}
		}).foreachRDD(new VoidFunction<JavaPairRDD<String,Integer>>() {

			@Override
			public void call(JavaPairRDD<String, Integer> t) throws Exception {
				
				t.collect().iterator().forEachRemaining(new Consumer<Tuple2<String, Integer>>() {

					@Override
					public void accept(Tuple2<String, Integer> t) {

						String key = t._1;
						
						String year = key.split("_")[0];
						String day = key.split("_")[1];
						String hours = key.split("_")[2];
						
						Integer value = t._2;
						//saveAsDb();
					}
				});
			}
		});
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		// kafka+sparkstreaming 实时解析，处理数据
		new Flow_DashBoardRealTimeOp_Job().sparkOpKafkaS();
	}

}
