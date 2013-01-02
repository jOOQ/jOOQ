/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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


/**
 * @author Lukas Eder
 */
import org.jooq.Constants

class Factory extends Generators {
    
    def static void main(String[] args) {
        val factory = new Factory();
        factory.generateSelect();
        factory.generateSelectDistinct();
        factory.generateRowValue();
        factory.generateRowField();
        factory.generateValues();
    }
    
    def generateSelect() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            var fieldOrRow = "Row" + degree;
            
            if (degree == 1) {
                fieldOrRow = "Field";
            }
            
            out.append('''
            
                /**
                 * Create a new DSL subselect statement.
                 * <p>
                 * This is the same as {@link #select(Field...)}, except that it declares
                 * additional record-level typesafety, which is needed by
                 * {@link «fieldOrRow»#in(Select)}, {@link «fieldOrRow»#equal(Select)} and other predicate
                 * building methods taking subselect arguments.
                 * <p>
                 * Unlike {@link Select} factory methods in the {@link Executor} API, this
                 * creates an unattached, and thus not directly renderable or executable
                 * <code>SELECT</code> statement. You can use this statement in two ways:
                 * <ul>
                 * <li>As a subselect within another select</li>
                 * <li>As a statement, after attaching it using
                 * {@link Select#attach(org.jooq.Configuration)}</li>
                 * </ul>
                 * <p>
                 * Example: <code><pre>
                 * import static org.jooq.impl.Factory.*;
                 *
                 * // [...]
                 *
                 * select(«field1_field2_fieldn(degree)»)
                 *  .from(table1)
                 *  .join(table2).on(field1.equal(field2))
                 *  .where(field1.greaterThan(100))
                 *  .orderBy(field2);
                 * </pre></code>
                 *
                 * @see Executor#select(Field...)
                 * @see #select(Field...)
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> select(«Field_TN_fieldn(degree)») {
                    return (SelectSelectStep) select(new Field[] { «fieldn(degree)» });
                }
            ''');
        }

        insert("org.jooq.impl.Factory", out, "select");
    }
    
    def generateSelectDistinct() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            var fieldOrRow = "Row" + degree;
            
            if (degree == 1) {
                fieldOrRow = "Field";
            }
            
            out.append('''
            
                /**
                 * Create a new DSL subselect statement.
                 * <p>
                 * This is the same as {@link #selectDistinct(Field...)}, except that it
                 * declares additional record-level typesafety, which is needed by
                 * {@link «fieldOrRow»#in(Select)}, {@link «fieldOrRow»#equal(Select)} and other predicate
                 * building methods taking subselect arguments.
                 * <p>
                 * Unlike {@link Select} factory methods in the {@link Executor} API, this
                 * creates an unattached, and thus not directly renderable or executable
                 * <code>SELECT</code> statement. You can use this statement in two ways:
                 * <ul>
                 * <li>As a subselect within another select</li>
                 * <li>As a statement, after attaching it using
                 * {@link Select#attach(org.jooq.Configuration)}</li>
                 * </ul>
                 * <p>
                 * Example: <code><pre>
                 * import static org.jooq.impl.Factory.*;
                 *
                 * // [...]
                 *
                 * selectDistinct(«field1_field2_fieldn(degree)»)
                 *  .from(table1)
                 *  .join(table2).on(field1.equal(field2))
                 *  .where(field1.greaterThan(100))
                 *  .orderBy(field2);
                 * </pre></code>
                 *
                 * @see Executor#selectDistinct(Field...)
                 * @see #selectDistinct(Field...)
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> selectDistinct(«Field_TN_fieldn(degree)») {
                    return (SelectSelectStep) selectDistinct(new Field[] { «fieldn(degree)» });
                }
            ''');
        }

        insert("org.jooq.impl.Factory", out, "selectDistinct");
    }
    
    def generateRowValue() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Create a row value expression of degree <code>«degree»</code>
                 * <p>
                 * Note: Not all databases support row value expressions, but many row value
                 * expression operations can be simulated on all databases. See relevant row
                 * value expression method Javadocs for details.
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> Row«degree»<«TN(degree)»> row(«TN_tn(degree)») {
                    return row(«val_tn(degree)»);
                }
            ''');
        }

        insert("org.jooq.impl.Factory", out, "row-value");
    }
    
    def generateRowField() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Create a row value expression of degree <code>«degree»</code>
                 * <p>
                 * Note: Not all databases support row value expressions, but many row value
                 * expression operations can be simulated on all databases. See relevant row
                 * value expression method Javadocs for details.
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> Row«degree»<«TN(degree)»> row(«Field_TN_tn(degree)») {
                    return new RowImpl(«tn(degree)»);
                }
            ''');
        }

        insert("org.jooq.impl.Factory", out, "row-field");
    }
    
    def generateValues() {
        val out = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Create a <code>VALUES()</code> expression of degree <code>«degree»</code>
                 * <p>
                 * The <code>VALUES()</code> constructor is a tool supported by some
                 * databases to allow for constructing tables from constant values.
                 * <p>
                 * If a database doesn't support the <code>VALUES()</code> constructor, it
                 * can be simulated using <code>SELECT .. UNION ALL ..</code>. The following
                 * expressions are equivalent:
                 * <p>
                 * <pre><code>
                 * -- Using VALUES() constructor
                 * VALUES(«field1_field2_fieldn(degree)»),
                 *       («field1_field2_fieldn(degree)»),
                 *       («field1_field2_fieldn(degree)»)
                 *
                 * -- Using UNION ALL
                 * SELECT «field1_field2_fieldn(degree)» UNION ALL
                 * SELECT «field1_field2_fieldn(degree)» UNION ALL
                 * SELECT «field1_field2_fieldn(degree)»
                 * </code></pre>
                 */
                «generatedMethod»
                @Support
                static <«TN(degree)»> Table<Record«degree»<«TN(degree)»>> values(Row«degree»<«TN(degree)»>... rows) {
                    return new Values<Record«degree»<«TN(degree)»>>(rows);
                }
            ''');
        }

        insert("org.jooq.impl.Factory", out, "values");
    }
}