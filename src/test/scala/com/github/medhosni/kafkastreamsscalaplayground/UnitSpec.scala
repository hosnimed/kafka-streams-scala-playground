package com.github.medhosni.kafkastreamsscalaplayground

import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, FunSpec, Inspectors, Matchers}

abstract class UnitSpec 
  extends FlatSpec
  with Matchers 
  with BeforeAndAfterAll
  with BeforeAndAfterEach
  with TypeCheckedTripleEquals 
  with Inspectors


