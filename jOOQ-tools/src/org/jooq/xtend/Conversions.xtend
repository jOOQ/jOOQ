/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.xtend

import org.jooq.Constants

/**
 * @author Lukas Eder
 */
class Conversions extends Generators {
    
    def static void main(String[] args) {
        val conversions = new Conversions();
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
          implicit def asTuple«degree»[«TN(degree)»](r : Record«degree»[«TN(degree)»]): Tuple«degree»[«TN(degree)»] = r match {
            case null => null
            case _ => Tuple«degree»(«FOR d : (1..degree) SEPARATOR ', '»r.value«d»«ENDFOR»)
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
          implicit def asMapperFromTuple«degree»[«TN(degree)», E](f: Tuple«degree»[«TN(degree)»] => E): RecordMapper[Record«degree»[«TN(degree)»], E] = new RecordMapper[Record«degree»[«TN(degree)»], E] {
            def map(record: Record«degree»[«TN(degree)»]) = f(Tuple«degree»(«XXXn(degree, "record.value")»))
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