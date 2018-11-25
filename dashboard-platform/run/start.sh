#!/bin/bash
/opt/spark/bin/spark-submit \
--class com.hxqh.bigdata.spark.Flow_DashBoardRealTimeOp_Job \
--jars /home/hadoop/dashboard.jar
--total-executor-cores 5 \
--executor-memory 10G \
--driver-class-path /home/hadoop/lib/*.jar