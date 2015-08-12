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


/**
 * @author Lukas Eder
 */
import org.jooq.Constants

class DSLContext extends Generators {
    
    def static void main(String[] args) {
        val ctx = new DSLContext();
        ctx.generateNewRecord();
        ctx.generateNewResult();
        ctx.generateSelect();
        ctx.generateSelectDistinct();
        ctx.generateInsert();
        ctx.generateMerge();
    }
    
    def generateNewRecord() {
        val outImpl = new StringBuilder();
        val outAPI = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            var fieldOrRow = "Row" + degree;
            
            if (degree == 1) {
                fieldOrRow = "Field";
            }
            
            outAPI.append('''
            
                /**
                 * Create a new empty {@link Record}.
                 * <p>
                 * The resulting record is attached to this {@link Configuration} by
                 * default. Use {@link Settings#isAttachRecords()} to override this
                 * behaviour.
                 *
                 * @return The new record
                 */
                «generatedMethod»
                <«TN(degree)»> Record«recTypeSuffix(degree)» newRecord(«Field_TN_fieldn(degree)»);
            ''');
            
            outImpl.append('''
            
                «generatedMethod»
                @Override
                public <«TN(degree)»> Record«recTypeSuffix(degree)» newRecord(«Field_TN_fieldn(degree)») {
                    return (Record«recTypeSuffixRaw(degree)») newRecord(new Field[] { «fieldn(degree)» });
                }
            ''');
        }

        insert("org.jooq.DSLContext", outAPI, "newRecord");
        insert("org.jooq.impl.DefaultDSLContext", outImpl, "newRecord");
    }
    
    def generateNewResult() {
        val outImpl = new StringBuilder();
        val outAPI = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            var fieldOrRow = "Row" + degree;
            
            if (degree == 1) {
                fieldOrRow = "Field";
            }
            
            outAPI.append('''
            
                /**
                 * Create a new empty {@link Result}.
                 * <p>
                 * The resulting result is attached to this {@link Configuration} by
                 * default. Use {@link Settings#isAttachRecords()} to override this
                 * behaviour.
                 *
                 * @return The new result
                 */
                «generatedMethod»
                <«TN(degree)»> Result<Record«recTypeSuffix(degree)»> newResult(«Field_TN_fieldn(degree)»);
            ''');
            
            outImpl.append('''
            
                «generatedMethod»
                @Override
                public <«TN(degree)»> Result<Record«recTypeSuffix(degree)»> newResult(«Field_TN_fieldn(degree)») {
                    return (Result) newResult(new Field[] { «fieldn(degree)» });
                }
            ''');
        }

        insert("org.jooq.DSLContext", outAPI, "newResult");
        insert("org.jooq.impl.DefaultDSLContext", outImpl, "newResult");
    }
    
    def generateSelect() {
        val outDSL = new StringBuilder();
        val outImpl = new StringBuilder();
        val outAPI = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            var fieldOrRow = "Row" + degree;
            
            if (degree == 1) {
                fieldOrRow = "Field";
            }
            
            outAPI.append('''
            
                /**
                 * Create a new DSL select statement.
                 * <p>
                 * This is the same as {@link #select(Field...)}, except that it
                 * declares additional record-level typesafety, which is needed by
                 * {@link «fieldOrRow»#in(Select)}, {@link «fieldOrRow»#equal(Select)} and other predicate
                 * building methods taking subselect arguments.
                 * <p>
                 * This creates an attached, renderable and executable <code>SELECT</code>
                 * statement from this {@link DSLContext}. If you don't need to render or
                 * execute this <code>SELECT</code> statement (e.g. because you want to
                 * create a subselect), consider using the static
                 * {@link DSL#select(«FOR d : (1..degree) SEPARATOR ', '»SelectField«ENDFOR»)} instead.
                 * <p>
                 * Example: <code><pre>
                 * using(configuration)
                 *       .select(«field1_field2_fieldn(degree)»)
                 *       .from(table1)
                 *       .join(table2).on(field1.equal(field2))
                 *       .where(field1.greaterThan(100))
                 *       .orderBy(field2);
                 * </pre></code>
                 *
                 * @see DSL#selectDistinct(SelectField...)
                 * @see #selectDistinct(SelectField...)
                 */
                «generatedMethod»
                @Support
                <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> select(«SelectField_TN_fieldn(degree)»);
            ''');
            
            outDSL.append('''
            
                /**
                 * Create a new DSL subselect statement.
                 * <p>
                 * This is the same as {@link #select(Field...)}, except that it declares
                 * additional record-level typesafety, which is needed by
                 * {@link «fieldOrRow»#in(Select)}, {@link «fieldOrRow»#equal(Select)} and other predicate
                 * building methods taking subselect arguments.
                 * <p>
                 * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
                 * creates an unattached, and thus not directly renderable or executable
                 * <code>SELECT</code> statement. You can use this statement in two ways:
                 * <ul>
                 * <li>As a subselect within another select</li>
                 * <li>As a statement, after attaching it using
                 * {@link Select#attach(org.jooq.Configuration)}</li>
                 * </ul>
                 * <p>
                 * Example: <code><pre>
                 * import static org.jooq.impl.DSL.*;
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
                 * @see DSLContext#select(SelectField...)
                 * @see #select(SelectField...)
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> select(«SelectField_TN_fieldn(degree)») {
                    return (SelectSelectStep) select(new SelectField[] { «fieldn(degree)» });
                }
            ''');
            
            outImpl.append('''
            
                «generatedMethod»
                @Override
                public <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> select(«SelectField_TN_fieldn(degree)») {
                    return (SelectSelectStep) select(new SelectField[] { «fieldn(degree)» });
                }
            ''');
        }

        insert("org.jooq.DSLContext", outAPI, "select");
        insert("org.jooq.impl.DefaultDSLContext", outImpl, "select");
        insert("org.jooq.impl.DSL", outDSL, "select");
    }
    
    def generateSelectDistinct() {
        val outDSL = new StringBuilder();
        val outImpl = new StringBuilder();
        val outAPI = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            var fieldOrRow = "Row" + degree;
            
            if (degree == 1) {
                fieldOrRow = "Field";
            }
            
            outAPI.append('''
            
                /**
                 * Create a new DSL select statement.
                 * <p>
                 * This is the same as {@link #selectDistinct(Field...)}, except that it
                 * declares additional record-level typesafety, which is needed by
                 * {@link «fieldOrRow»#in(Select)}, {@link «fieldOrRow»#equal(Select)} and other predicate
                 * building methods taking subselect arguments.
                 * <p>
                 * This creates an attached, renderable and executable <code>SELECT</code>
                 * statement from this {@link DSLContext}. If you don't need to render or
                 * execute this <code>SELECT</code> statement (e.g. because you want to
                 * create a subselect), consider using the static
                 * {@link DSL#selectDistinct(«FOR d : (1..degree) SEPARATOR ', '»SelectField«ENDFOR»)} instead.
                 * <p>
                 * Example: <code><pre>
                 * using(configuration)
                 *       .selectDistinct(«field1_field2_fieldn(degree)»)
                 *       .from(table1)
                 *       .join(table2).on(field1.equal(field2))
                 *       .where(field1.greaterThan(100))
                 *       .orderBy(field2);
                 * </pre></code>
                 *
                 * @see DSL#selectDistinct(SelectField...)
                 * @see #selectDistinct(SelectField...)
                 */
                «generatedMethod»
                @Support
                <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> selectDistinct(«SelectField_TN_fieldn(degree)»);
            ''');
            
            outDSL.append('''
            
                /**
                 * Create a new DSL subselect statement.
                 * <p>
                 * This is the same as {@link #selectDistinct(Field...)}, except that it
                 * declares additional record-level typesafety, which is needed by
                 * {@link «fieldOrRow»#in(Select)}, {@link «fieldOrRow»#equal(Select)} and other predicate
                 * building methods taking subselect arguments.
                 * <p>
                 * Unlike {@link Select} factory methods in the {@link DSLContext} API, this
                 * creates an unattached, and thus not directly renderable or executable
                 * <code>SELECT</code> statement. You can use this statement in two ways:
                 * <ul>
                 * <li>As a subselect within another select</li>
                 * <li>As a statement, after attaching it using
                 * {@link Select#attach(org.jooq.Configuration)}</li>
                 * </ul>
                 * <p>
                 * Example: <code><pre>
                 * import static org.jooq.impl.DSL.*;
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
                 * @see DSLContext#selectDistinct(SelectField...)
                 * @see #selectDistinct(SelectField...)
                 */
                «generatedMethod»
                @Support
                public static <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> selectDistinct(«SelectField_TN_fieldn(degree)») {
                    return (SelectSelectStep) selectDistinct(new SelectField[] { «fieldn(degree)» });
                }
            ''');
            
            outImpl.append('''
            
                «generatedMethod»
                @Override
                public <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> selectDistinct(«SelectField_TN_fieldn(degree)») {
                    return (SelectSelectStep) selectDistinct(new SelectField[] { «fieldn(degree)» });
                }
            ''');
        }

        insert("org.jooq.DSLContext", outAPI, "selectDistinct");
        insert("org.jooq.impl.DefaultDSLContext", outImpl, "selectDistinct");
        insert("org.jooq.impl.DSL", outDSL, "selectDistinct");
    }

    def generateInsert() {
        val outDSL = new StringBuilder();
        val outImpl = new StringBuilder();
        val outAPI = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            outAPI.append('''
            
                /**
                 * Create a new DSL insert statement.
                 * <p>
                 * Example: <code><pre>
                 * using(configuration)
                 *       .insertInto(table, «field1_field2_fieldn(degree)»)
                 *       .values(«XXX1_XXX2_XXXn(degree, "valueA")»)
                 *       .values(«XXX1_XXX2_XXXn(degree, "valueB")»)
                 *       .onDuplicateKeyUpdate()
                 *       .set(field1, value1)
                 *       .set(field2, value2)
                 *       .execute();
                 * </pre></code>
                 */
                «generatedMethod»
                @Support
                <R extends Record, «TN(degree)»> InsertValuesStep«degree»<R, «TN(degree)»> insertInto(Table<R> into, «Field_TN_fieldn(degree)»);
            ''');
            
            outDSL.append('''
            
                /**
                 * Create a new DSL insert statement.
                 * <p>
                 * Unlike {@link Insert} factory methods in the {@link DSLContext} API, this
                 * creates an unattached, and thus not directly renderable or executable
                 * <code>INSERT</code> statement.
                 * <p>
                 * Example: <code><pre>
                 * import static org.jooq.impl.DSL.*;
                 *
                 * // [...]
                 *
                 * insertInto(table, «field1_field2_fieldn(degree)»)
                 *   .values(«XXX1_XXX2_XXXn(degree, "valueA")»)
                 *   .values(«XXX1_XXX2_XXXn(degree, "valueB")»)
                 *   .onDuplicateKeyUpdate()
                 *   .set(field1, value1)
                 *   .set(field2, value2)
                 * </pre></code>
                 *
                 * @see DSLContext#insertInto(Table, «(1..degree).map[e | "Field"].join(", ")»)
                 */
                «generatedMethod»
                @Support
                public static <R extends Record, «TN(degree)»> InsertValuesStep«degree»<R, «TN(degree)»> insertInto(Table<R> into, «Field_TN_fieldn(degree)») {
                    return (InsertValuesStep«degree») insertInto(into, new Field[] { «fieldn(degree)» });
                }
            ''')

            outImpl.append('''
            
                «generatedMethod»
                @Override
                public <R extends Record, «TN(degree)»> InsertValuesStep«degree»<R, «TN(degree)»> insertInto(Table<R> into, «Field_TN_fieldn(degree)») {
                    return new InsertImpl(configuration(), into, Arrays.asList(new Field[] { «fieldn(degree)» }));
                }
            ''');
        }

        insert("org.jooq.DSLContext", outAPI, "insert");
        insert("org.jooq.impl.DefaultDSLContext", outImpl, "insert");
        insert("org.jooq.impl.DSL", outDSL, "insert");
    }

    def generateMerge() {
        val outDSL = new StringBuilder();
        val outImpl = new StringBuilder();
        val outAPI = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            outAPI.append('''
            
                /**
                 * Create a new DSL merge statement (H2-specific syntax).
                 * <p>
                 * This statement is available from DSL syntax only. It is known to be
                 * supported in some way by any of these dialects:
                 * <table border="1">
                 * <tr>
                 * <td>H2</td>
                 * <td>H2 natively supports this special syntax</td>
                 * <td><a href= "www.h2database.com/html/grammar.html#merge"
                 * >www.h2database.com/html/grammar.html#merge</a></td>
                 * </tr>
                 * <tr>
                 * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
                 * <td>These databases can emulate the H2-specific MERGE statement using a
                 * standard SQL MERGE statement, without restrictions</td>
                 * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
                 * </tr>
                 * </table>
                 */
                «generatedMethod»
                @Support({ CUBRID, DB2, H2, HANA, HSQLDB, INFORMIX, ORACLE, SQLSERVER, SYBASE })
                <R extends Record, «TN(degree)»> MergeKeyStep«degree»<R, «TN(degree)»> mergeInto(Table<R> table, «Field_TN_fieldn(degree)»);
            ''');
            
            outDSL.append('''
            
                /**
                 * Create a new DSL merge statement (H2-specific syntax).
                 * <p>
                 * Unlike {@link Merge} factory methods in the {@link DSLContext} API, this
                 * creates an unattached, and thus not directly renderable or executable
                 * <code>MERGE</code> statement.
                 * <p>
                 * This statement is available from DSL syntax only. It is known to be
                 * supported in some way by any of these dialects:
                 * <table border="1">
                 * <tr>
                 * <td>H2</td>
                 * <td>H2 natively supports this special syntax</td>
                 * <td><a href= "www.h2database.com/html/grammar.html#merge"
                 * >www.h2database.com/html/grammar.html#merge</a></td>
                 * </tr>
                 * <tr>
                 * <td>DB2, HSQLDB, Oracle, SQL Server, Sybase SQL Anywhere</td>
                 * <td>These databases can emulate the H2-specific MERGE statement using a
                 * standard SQL MERGE statement, without restrictions</td>
                 * <td>See {@link #mergeInto(Table)} for the standard MERGE statement</td>
                 * </tr>
                 * </table>
                 *
                 * @see DSLContext#mergeInto(Table, «(1..degree).map[e | "Field"].join(", ")»)
                 */
                «generatedMethod»
                @Support({ CUBRID, DB2, H2, HANA, HSQLDB, INFORMIX, ORACLE, SQLSERVER, SYBASE })
                public static <R extends Record, «TN(degree)»> MergeKeyStep«degree»<R, «TN(degree)»> mergeInto(Table<R> table, «Field_TN_fieldn(degree)») {
                	return using(new DefaultConfiguration()).mergeInto(table, «fieldn(degree)»);
                }
            ''');
            
            outImpl.append('''
            
                «generatedMethod»
                @Override
                public <R extends Record, «TN(degree)»> MergeKeyStep«degree»<R, «TN(degree)»> mergeInto(Table<R> table, «Field_TN_fieldn(degree)») {
                    return new MergeImpl(configuration(), table, Arrays.asList(«fieldn(degree)»));
                }
            ''');
        }

        insert("org.jooq.DSLContext", outAPI, "merge");
        insert("org.jooq.impl.DefaultDSLContext", outImpl, "merge");
        insert("org.jooq.impl.DSL", outDSL, "merge");
    }
}