package com.github.medhosni.kafkastreamsscalaplayground

import java.time.Duration

import com.typesafe.scalalogging.StrictLogging
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.processor.{Processor, ProcessorContext, ProcessorSupplier, PunctuationType}
import org.apache.kafka.streams.state.KeyValueStore

class MaxCalculatorProcessor extends Processor[String, Long] with StrictLogging {
  var kvStore:KeyValueStore[String, Long] = _

  def init(context: ProcessorContext): Unit = {
    kvStore = context.getStateStore("CalculatorStore").asInstanceOf[KeyValueStore[String, Long]]
    context.schedule(Duration.ofSeconds(1), PunctuationType.STREAM_TIME, _ => {
      kvStore.all().forEachRemaining{ kv: KeyValue[String, Long] => {
        val (word:String, count:Long) = (kv.key, kv.value)
        logger.info(s"($word, $count) forwarded.")
        context.forward(word, count)
      }}
    })
    context.commit()
  }

  def process(key: String, count: Long): Unit = {
      Option(kvStore.get(key)) match {
        case Some(value) => kvStore.put(key, math.max(value, count))
        case None => kvStore.put(key, count)
      }
  }

  def close(): Unit = {
    logger.info("Max Calculator Processor Closed.")
  }

}

object MaxCalculatorProcessor {
  def supplier = new CalculatorSupplier
  class CalculatorSupplier extends ProcessorSupplier[String, Long]{
    def get(): Processor[String, Long] = new MaxCalculatorProcessor
  }

}
