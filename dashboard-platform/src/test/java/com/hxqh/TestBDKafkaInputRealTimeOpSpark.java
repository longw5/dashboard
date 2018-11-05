package com.hxqh;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import kafka.serializer.StringDecoder;
import scala.Tuple2;

/**
 * @author wulong
 */
public class TestBDKafkaInputRealTimeOpSpark {

	public static void main(String[] args) {
		
		//配置spark参数
		SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName(Constants.SPARK_APP_NAME);
		sparkConf.set("spark.streaming.blockInterval", "50");

		JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(5));

		//javaStreamingContext.checkpoint("hdfs://192.168.145.101:9000/streaming_checkpoint");

		//配置kafka集群参数
		Map<String, String> kafkaParams = new HashMap<>(10);
		kafkaParams.put(Constants.KAFKA_METADATA_BROKER_LIST, ConfigurationManager.getProperty(Constants.KAFKA_METADATA_BROKER_LIST));

		// 构建topic set
		String kafkaTopics = ConfigurationManager.getProperty(Constants.KAFKA_TOPICS);
		String[] kafkaTopicsSplited = kafkaTopics.split(",");
		Set<String> topics = new HashSet<>();
		for (String kafkaTopic : kafkaTopicsSplited) {
			topics.add(kafkaTopic);
		}
		
		//获取kafka流处理对象
		JavaPairInputDStream<String, String> kafkaDStream = KafkaUtils.createDirectStream(javaStreamingContext,
				String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);

		System.out.println("获取到流对象...............");
		
		//数据流处理
		kafkaDStream.map(new Function<Tuple2<String, String>, String>() {

			private static final long serialVersionUID = 1L;

			//获取此时段内的流数据集
			public String call(Tuple2<String, String> v1) throws Exception {
				return v1._2();
			}
		}).foreachRDD(new VoidFunction<JavaRDD<String>>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(JavaRDD<String> v) throws Exception {
				
				//此时段内的数据集
				List<String> collect = v.collect();
				long count = v.count();
				System.out.println("数据条数："+count);
				System.out.println("数据:"+collect.get(1).substring(1, 2));
			}
		});

		javaStreamingContext.start();
		try {
			javaStreamingContext.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		javaStreamingContext.close();
	}

}
