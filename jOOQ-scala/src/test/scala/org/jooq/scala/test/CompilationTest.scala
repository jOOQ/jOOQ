/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.scala.test

import collection.JavaConversions._
import org.scalatest.FunSuite
import org.jooq._
import org.jooq.impl._
import org.jooq.impl.DSL._
import org.jooq.scala.example.h2.Tables._
import org.jooq.scala.Conversions._
import java.sql.DriverManager
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.util.ArrayList
import org.jooq.scala.test.pojo.BookCaseWithConstructorProperties
import org.jooq.scala.test.pojo.BookCase

/**
 * Keeping the Scala compiler happy without any compilation regressions is tricky.
 * <p>
 * This test aims for using common idioms to ensure that Scala usage of jOOQ API keeps compiling.
 *
 * @author Lukas Eder
 */
@RunWith(classOf[JUnitRunner])
class CompilationTest extends FunSuite {

  /* [pro] xx
  xxxxxxxxxxxxxxxxxx x
    xxx x x xxxxxxxxxxxxx
    xxx xx x xxxxxxxxxxxxxxxxxxxx xxx xxxxxxxxxxx
    xxx xx x xxxxxxxxxxx xxx xxxxxxxxxxxxxxxxxxxx
  x
  xx [/pro] */
}
