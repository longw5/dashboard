package com.hxqh.bigdata.spark;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.hadoop.io.MD5Hash;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.CanCommitOffsets;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.HasOffsetRanges;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.kafka010.OffsetRange;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hxqh.bigdata.common.Constants;
import com.hxqh.bigdata.conf.ConfigurationManager;
import com.hxqh.bigdata.domain.MsgParser;
import com.hxqh.bigdata.kafka.Kafka_Prodecer;
import com.hxqh.bigdata.parse.Flow_InitParseRuleMapping_Job;
import com.hxqh.bigdata.util.redis;

import scala.Tuple2;

/**
 * 1 实时读取kafka数据，处理数据
 * 
 * @author wulong
 */
public class KafkaStreamingOpTask {

	private static Logger logger = Logger.getLogger(KafkaStreamingOpTask.class);

	public static void sparkOpKafkaS() {

		// 配置spark参数
		SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName(Constants.SPARK_APP_NAME);
//		SparkConf sparkConf = new SparkConf().setAppName(Constants.SPARK_APP_NAME);
		sparkConf.set("spark.streaming.blockInterval", "50");

		JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(5));

//		javaStreamingContext.checkpoint("D://streaming_checkpoint");

		// 配置kafka集群参数
		Map<String, Object> kafkaParams = new HashMap<>(10);
		kafkaParams.put(Constants.KAFKA_METADATA_BROKER_LIST,
				ConfigurationManager.getProperty(Constants.KAFKA_METADATA_BROKER_LIST));
		kafkaParams.put("bootstrap.servers", ConfigurationManager.getProperty(Constants.KAFKA_METADATA_BROKER_LIST));
		kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		//读取消息  early lastest none
		kafkaParams.put("auto.offset.reset", "latest ");
		//一次最大拉取消息
		kafkaParams.put("max.poll.records", "200");	
		//是否正常提交
		kafkaParams.put("enable.auto.commit", "false");
		//消费者线程组
		kafkaParams.put("group.id", "consumer_test");

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
		if(kafkaSOpStep(kafkaDStream)) {
			
			kafkaDStream.foreachRDD(new VoidFunction<JavaRDD<ConsumerRecord<String,String>>>() {
	
				@Override
				public void call(JavaRDD<ConsumerRecord<String, String>> t) throws Exception {
					//获取偏移量
					OffsetRange[] offsetRanges = ((HasOffsetRanges)(t.rdd())).offsetRanges();
					//手动提交offset
					((CanCommitOffsets)kafkaDStream.inputDStream()).commitAsync(offsetRanges);
				}
			});
		}
		
		javaStreamingContext.start();
		try {
			javaStreamingContext.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		javaStreamingContext.close();
	}

	@SuppressWarnings("serial")
	private static boolean kafkaSOpStep(JavaInputDStream<ConsumerRecord<String, String>> kafkaDStream) {

		//获取解析器
		Map<String, MsgParser> map = Flow_InitParseRuleMapping_Job.getMsgParser();

		//报文解析，提取，数据判断
		JavaDStream<Map<String,String>> kafkaStreamMap = kafkaDStream.map(new Function<ConsumerRecord<String,String>, Map<String, String>>() {

			@Override
			public Map<String, String> call(ConsumerRecord<String, String> record) throws Exception {
				
				Map<String, String> rtMap = new HashMap<>();
				String event = record.value();
				Set<Entry<String, MsgParser>> entrySet = map.entrySet();
				
				//为了手动更新offset中， 数据已入库但是没有手动提交offset的问题，
				//解决重复消费message的问题，
				MD5Hash digest = MD5Hash.digest(event);
				//快速存入redis中
				if(redis.get(digest)==null) {
					redis.put(digest);
					//根据不同的报文去解析
					for (Entry<String, MsgParser> entry : entrySet) {
						entry.getValue()
								.setValue(event.substring(entry.getValue().getStart(), entry.getValue().getEnd()));
						if (entry.getValue().getLength() == entry.getValue().getValue().length()) {
							logger.info(entry.getValue());
							// 持久化存储
							rtMap.put(entry.getKey(), entry.getValue().getValue());
						} else {
							// 数据报文异常，截取错误
							Kafka_Prodecer.sendMassage("error", UUID.randomUUID().toString(), event);
						}
					}
				}else {
					//重复数据，不进行处理
				}
				return rtMap;
			}
		});
		
		//具体业务处理
		JsonArray jsonArray = new JsonArray();
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
			public void call(JavaPairRDD<String, Integer> args) throws Exception {
				
				args.collect().iterator().forEachRemaining(new Consumer<Tuple2<String, Integer>>() {

					@Override
					public void accept(Tuple2<String, Integer> t) {
						
						JsonObject jsonObject = new JsonObject();
						String year = t._1.split("_")[0];
						String day = t._1.split("_")[1];
						String hours = t._1.split("_")[2];
						
						jsonObject.addProperty("year", year);
						jsonObject.addProperty("day", day);
						jsonObject.addProperty("hours", hours);
						jsonArray.add(jsonObject);
					}
				});
				
			}
		});
		Kafka_Prodecer.sendMassage("test", UUID.randomUUID().toString(), jsonArray.toString());
		return true;
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		// kafka+sparkstreaming 实时解析，处理数据
		new KafkaStreamingOpTask().sparkOpKafkaS();
	}

}
