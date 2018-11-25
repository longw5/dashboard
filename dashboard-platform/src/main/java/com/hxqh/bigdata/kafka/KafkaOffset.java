package com.hxqh.bigdata.kafka;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kafka.common.OffsetMetadataAndError;
import kafka.common.TopicAndPartition;
import kafka.javaapi.OffsetCommitResponse;
import kafka.javaapi.OffsetFetchRequest;
import kafka.javaapi.OffsetFetchResponse;
import kafka.network.BlockingChannel;

public class KafkaOffset {
	
	private static String group = "consumer_test";
	private static int correlationId = 0;
	private final static String clientId = "demoClientId";
 
	public void getOffset() {
		
		BlockingChannel channel = new BlockingChannel("192.168.145.101", 9092, BlockingChannel.UseDefaultBufferSize(), BlockingChannel.UseDefaultBufferSize(), 5000 /* read timeout in millis */);
		channel.connect();
		
		List<TopicAndPartition> partitions = Arrays.asList(0, 1, 2, 3, 4, 5).stream().map(i -> new TopicAndPartition("test", i)).collect(Collectors.toList());
			/* version */// version 1 and above fetch from Kafka, version 0 fetches from ZooKeeper，测试为0程序正常执行，为1程序执行没有任何效果
		OffsetFetchRequest fetchRequest = new OffsetFetchRequest(group, partitions, (short) 0, correlationId, clientId);
		/*try {
			channel.send(fetchRequest.underlying());
			OffsetFetchResponse fetchResponse = OffsetFetchResponse.readFrom(channel.receive().buffer());
			for (TopicAndPartition partition : partitions) {
				OffsetMetadataAndError result = fetchResponse.offsets().get(partition);
				short offsetFetchErrorCode = result.error();
				if (offsetFetchErrorCode == ErrorMapping.NotCoordinatorForConsumerCode()) {
					channel.disconnect();
					// Go to step 1 and retry the offset fetch
				} else {
					long offset = result.offset();
					System.out.println(String.format("offset->%s->%d", partition.partition(), offset));
				}
			}
		}finally {
			channel.disconnect();
		}*/
	}
 
	public void commitOffset() {
 
		BlockingChannel channel = new BlockingChannel("192.168.40.28", 9092, BlockingChannel.UseDefaultBufferSize(), BlockingChannel.UseDefaultBufferSize(), 5000 /* read timeout in millis */);
		channel.connect();
		/*try {
			long now = System.currentTimeMillis();
			Map<TopicAndPartition, OffsetAndMetadata> offsets = new LinkedHashMap<>();
			offsets.put(new TopicAndPartition("article_basic_info", 0), new OffsetAndMetadata(10L, "associated metadata", now));
			offsets.put(new TopicAndPartition("article_basic_info", 1), new OffsetAndMetadata(20L, "more metadata", now));
			OffsetCommitRequest commitRequest = new OffsetCommitRequest(group, offsets, correlationId++, clientId, (short) 0  version ); // version 1 and above commit to Kafka, version 0 commits to ZooKeeper
			channel.send(commitRequest.underlying());
			OffsetCommitResponse commitResponse = OffsetCommitResponse.readFrom(channel.receive().buffer());
			System.out.println(String.format("提交->%s", commitResponse.hasError()));
		} finally {
			channel.disconnect();
		}*/
	}
 
	public static void main(String[] args) {
		
		BlockingChannel channel = new BlockingChannel("192.168.145.101", 9092, BlockingChannel.UseDefaultBufferSize(), BlockingChannel.UseDefaultBufferSize(), 5000 /* read timeout in millis */);
		channel.connect();
		
		List<TopicAndPartition> partitions = Arrays.asList(0, 1, 2, 3, 4, 5).stream().map(i -> new TopicAndPartition("test", i)).collect(Collectors.toList());
		
		OffsetFetchRequest fetchRequest = new OffsetFetchRequest(group, partitions, (short) 0, correlationId, clientId);
		
		kafka.api.OffsetFetchRequest underlying = fetchRequest.underlying();
		
		channel.send(underlying);
		
		OffsetFetchResponse readFrom = OffsetFetchResponse.readFrom(channel.receive().payload());
		
		Map<TopicAndPartition, OffsetMetadataAndError> offsets = readFrom.offsets();
		
		Iterator<TopicAndPartition> iterator = partitions.iterator();
		
		while(iterator.hasNext()) {
			
			TopicAndPartition next = iterator.next();
			System.out.println(offsets.get(next));
		}
		
	}
}