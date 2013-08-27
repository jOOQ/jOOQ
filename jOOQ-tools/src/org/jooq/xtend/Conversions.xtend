/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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