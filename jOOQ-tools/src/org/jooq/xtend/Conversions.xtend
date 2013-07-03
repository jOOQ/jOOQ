/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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