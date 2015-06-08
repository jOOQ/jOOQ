/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.xtend

import org.jooq.Constants

/**
 * @author Lukas Eder
 */
class ScalaConversions extends Generators {
    
    def static void main(String[] args) {
        val conversions = new ScalaConversions();
        conversions.generateAsTuple();
        conversions.generateAsMapper();
        // conversions.generateAsHandler();
    }
    
    def generateAsTuple() {
        val out = new StringBuilder();
        
        out.append('''
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
          /**
           * Enrich any {@link org.jooq.Record«degree»} with the {@link Tuple«degree»} case class
           */
          implicit def asTuple«degree»[«TN(degree)»](r : Record«degree»[«TN(degree)»]): «IF degree == 1»Tuple«degree»[«TN(degree)»]«ELSE»(«TN(degree)»)«ENDIF» = r match {
            case null => null
            case _ => «IF degree == 1»Tuple«degree»«ENDIF»(«FOR d : (1..degree) SEPARATOR ', '»r.value«d»«ENDFOR»)
          }
        «ENDFOR»
        ''');
         
        insert("org.jooq.scala.Conversions", out, "tuples");
    }

    def generateAsMapper() {
        val out = new StringBuilder();
        
        out.append('''
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
          /**
           * Wrap a Scala <code>Tuple«degree» => E</code> function in a jOOQ <code>RecordMapper</code> type.
           */
          implicit def asMapperFromArgList«degree»[«TN(degree)», E](f: («TN(degree)») => E): RecordMapper[Record«degree»[«TN(degree)»], E] = new RecordMapper[Record«degree»[«TN(degree)»], E] {
            def map(record: Record«degree»[«TN(degree)»]) = f(«XXXn(degree, "record.value")»)
          }
        «ENDFOR»
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
          /**
           * Wrap a Scala <code>Tuple«degree» => E</code> function in a jOOQ <code>RecordMapper</code> type.
           */
          implicit def asMapperFromTuple«degree»[«TN(degree)», E](f: «IF degree == 1»Tuple«degree»[«TN(degree)»]«ELSE»((«TN(degree)»))«ENDIF» => E): RecordMapper[Record«degree»[«TN(degree)»], E] = new RecordMapper[Record«degree»[«TN(degree)»], E] {
            def map(record: Record«degree»[«TN(degree)»]) = f(«IF degree == 1»Tuple«degree»(«XXXn(degree, "record.value")»)«ELSE»(«XXXn(degree, "record.value")»)«ENDIF»)
          }
        «ENDFOR»
        ''');
         
        insert("org.jooq.scala.Conversions", out, "mapper");
    }

    def generateAsHandler() {
        val out = new StringBuilder();
        
        out.append('''
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
          /**
           * Wrap a Scala <code>Tuple«degree» => Unit</code> function in a jOOQ <code>RecordHandler</code> type.
           */
          implicit def asHandlerFromArgList«degree»[«TN(degree)»](f: («TN(degree)») => Unit): RecordHandler[Record«degree»[«TN(degree)»]] = new RecordHandler[Record«degree»[«TN(degree)»]] {
            def next(record: Record«degree»[«TN(degree)»]) = f(«XXXn(degree, "record.value")»)
          }
        «ENDFOR»
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
          /**
           * Wrap a Scala <code>Tuple«degree» => Unit</code> function in a jOOQ <code>RecordHandler</code> type.
           */
          implicit def asHandlerFromTuple«degree»[«TN(degree)», E](f: Tuple«degree»[«TN(degree)»] => E): RecordHandler[Record«degree»[«TN(degree)»]] = new RecordHandler[Record«degree»[«TN(degree)»]] {
            def next(record: Record«degree»[«TN(degree)»]) = f(Tuple«degree»(«XXXn(degree, "record.value")»))
          }
        «ENDFOR»
        ''');
         
        insert("org.jooq.scala.Conversions", out, "handler");
    }
}