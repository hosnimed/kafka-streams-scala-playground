package com.github.medhosni.kafkastreamsscalaplayground

import com.typesafe.scalalogging.StrictLogging
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serdes, Serializer}
import org.apache.kafka.streams.state.Stores
import org.apache.kafka.streams.{KeyValue, TestInputTopic, TestOutputTopic, Topology, TopologyTestDriver}


class MaxCalculatorProcessorSpec extends UnitSpec with StrictLogging{

  private val stringSerdes = new Serdes.StringSerde
  private val longSerdes = new Serdes.LongSerde
  private var driver:TopologyTestDriver = _
  private var inputTopic:TestInputTopic[String, Long] = _
  private var outputTopic:TestOutputTopic[String, Long] = _

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    val topology = new Topology()
    topology.addSource(TestConstants.sourceProcessor, TestConstants.inputTopic)
    topology.addProcessor(TestConstants.calculatorProcessor, MaxCalculatorProcessor.supplier, TestConstants.sourceProcessor)
    topology.addStateStore(
      Stores.keyValueStoreBuilder[String, Long](Stores.inMemoryKeyValueStore(TestConstants.calculatorStore), Serdes.String(), Serdes.Long().asInstanceOf[Serde[Long]]),
      TestConstants.calculatorProcessor
    )
    topology.addSink(TestConstants.sinkProcessor, TestConstants.outputTopic, TestConstants.calculatorProcessor)
    // print the topology
    logger.info("Topology created.")
    topology.describe()
    // setup test driver
    val testDriver = new TopologyTestDriver(topology, TestConstants.getStreamConf)
    driver = testDriver
    logger.info("Driver initialized")
    // setup test topics
    inputTopic = testDriver.createInputTopic[String, Long](TestConstants.inputTopic, stringSerdes.serializer, longSerdes.serializer().asInstanceOf[Serializer[Long]])
    outputTopic = testDriver.createOutputTopic[String, Long](TestConstants.outputTopic, stringSerdes.deserializer(), longSerdes.deserializer().asInstanceOf[Deserializer[Long]])
    // pre-populate store
    val calculatorStore = testDriver.getKeyValueStore[String, Long](TestConstants.calculatorStore)
    calculatorStore.put("kafka", 10L)
  }

  behavior of "CalculatorTopology"

  it should "not update store with smaller value" in {
    inputTopic.pipeInput("kafka", 9L)
    outputTopic.readKeyValue() should be(new KeyValue[String,Long]("kafka", 10L))
    outputTopic.isEmpty should be(true)
  }

  it should "ends with the max value" in {
    inputTopic.pipeInput("kafka", 12L)
    inputTopic.pipeInput("kafka", 11L)
    outputTopic.readKeyValue() should be(new KeyValue[String,Long]("kafka", 12L))
    outputTopic.isEmpty should be(true)
  }

  override def afterAll(): Unit = {
    driver.close()
    super.afterAll()
  }
}
