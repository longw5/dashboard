package com.hxqh.bigdata.kafka;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.hxqh.bigdata.common.Constants;
import com.hxqh.bigdata.conf.ConfigurationManager;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import scala.util.Random;

/**
 * 模拟kafka生产数据
 * @author wulong
 */
public class Flow_KafkaProdecer_Job implements Runnable{

	private static Producer<String, String> producer;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Producer<String, String> getProducer() {
		if (producer == null) {
			synchronized (Flow_KafkaProdecer_Job.class) {
				Properties pro = new Properties();
				pro.setProperty("metadata.broker.list", ConfigurationManager.getProperty(Constants.KAFKA_METADATA_BROKER_LIST));
				pro.setProperty("request.required.acks", ConfigurationManager.getProperty(Constants.KAFKA_REQUEST_REQUIRED_ACKS));
				pro.setProperty("producer.type", ConfigurationManager.getProperty(Constants.KAFKA_PRODUCER_TYPE));
				pro.setProperty("serializer.class", ConfigurationManager.getProperty(Constants.KAFKA_SERIALIZER_CLASS));
				pro.setProperty("request.timeout.ms", ConfigurationManager.getProperty(Constants.KAFKA_REQUEST_TIMEOUT_MS));
				pro.setProperty("key.serializer.class", ConfigurationManager.getProperty(Constants.KAFKA_KEY_SERIALIZER_CLASS));
				pro.setProperty("compression.codec", ConfigurationManager.getProperty(Constants.KAFKA_COMPRESSION_CODEC));
				pro.setProperty("message.send.max.retries", ConfigurationManager.getProperty(Constants.KAFKA_MESSAGE_SEND_MAX_RETRIES));
				pro.setProperty("retry.backoff.ms", ConfigurationManager.getProperty(Constants.KAFKA_RECONNECT_BACKOFF_MS));
				ProducerConfig config = new ProducerConfig(pro);
				producer = new Producer(config);
			}
		}
		return producer;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sendMassage(String topic, String key, String message) {
		Producer producer = getProducer();
		KeyedMessage km = new KeyedMessage(topic, key, message);
		producer.send(km);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sendListMassage(String topic, Map<String, String> msg) {
		Producer producer = getProducer();
		List list = new ArrayList();
		for (Iterator localIterator = msg.keySet().iterator(); localIterator.hasNext();) {
			String k = (String) localIterator.next();

			KeyedMessage km = new KeyedMessage(topic, k, (String) msg.get(k));
			list.add(km);
		}
		producer.send(list);
	}

	@Override
	public void run() {
		for (int i = 0; i < 10000; i++) {
			try {
				Thread.sleep(new Random().nextInt(3000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sendMassage(ConfigurationManager.getProperty(Constants.KAFKA_TOPICS), ConfigurationManager.getProperty(Constants.KAFKA_TOPICS_KEY), ConfigurationManager.getProperty(Constants.KAFKA_TOPICS_MSG));
		}
	}
}
