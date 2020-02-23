package com.github.medhosni.kafkastreamsscalaplayground

import java.util.Properties

import org.apache.kafka.common.serialization.{Serde, Serdes, StringDeserializer, StringSerializer}
import org.apache.kafka.streams.StreamsConfig.{APPLICATION_ID_CONFIG, BOOTSTRAP_SERVERS_CONFIG, DEFAULT_KEY_SERDE_CLASS_CONFIG, DEFAULT_VALUE_SERDE_CLASS_CONFIG}

object TestConstants {
  val calculatorStore = "CalculatorStore"


  def getStreamConf = {
    val p = new Properties()
    p.put(APPLICATION_ID_CONFIG, "TestApp")
    p.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    p.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass.getName)
    p
  }

  val stringSer = new StringSerializer
  val stringDe = new StringDeserializer
  val stringSerde: Serde[String] = Serdes.String()

  val sourceProcessor = "sourceProcessor"
  val inputTopic = "inputtopic"
  val calculatorProcessor = "calculatorProcessor"
  val sinkProcessor = "sinkProcessor"
  val outputTopic = "outputtopic"

}

