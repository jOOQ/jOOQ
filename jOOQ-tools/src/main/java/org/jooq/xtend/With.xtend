/**
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

class With extends Generators {
    
    def static void main(String[] args) {
        val ctx = new With();
        ctx.generateSelect();
        ctx.generateSelectDistinct();
        ctx.generateInsert();
        ctx.generateMerge();
    }
    
    def generateSelect() {
        val outWithStep = new StringBuilder();
        val outWithImpl = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            var fieldOrRow = "Row" + degree;
            
            if (degree == 1) {
                fieldOrRow = "Field";
            }
            
            outWithStep.append('''
            
                /**
                 * Create a new DSL select statement.
                 * <p>
                 * This is the same as {@link #select(SelectField...)}, except that it
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
                 *       .with(name("t").fields(«XXX1_XXX2_XXXn(degree, "f")»).as(subselect))
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
                @Support({ DB2, FIREBIRD, HSQLDB, MYSQL_8_0, ORACLE, POSTGRES, SQLSERVER, SYBASE, VERTICA })
                <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> select(«SelectField_TN_fieldn(degree)»);
            ''');
            
            outWithImpl.append('''
            
                «generatedMethod»
                @Override
                public final <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> select(«SelectField_TN_fieldn(degree)») {
                    return (SelectSelectStep) select(new SelectField[] { «fieldn(degree)» });
                }
            ''');
        }

        insert("org.jooq.WithStep", outWithStep, "select");
        insert("org.jooq.impl.WithImpl", outWithImpl, "select");
    }
    
    def generateSelectDistinct() {
        val outWithStep = new StringBuilder();
        val outWithImpl = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            var fieldOrRow = "Row" + degree;
            
            if (degree == 1) {
                fieldOrRow = "Field";
            }
            
            outWithStep.append('''
            
                /**
                 * Create a new DSL select statement.
                 * <p>
                 * This is the same as {@link #selectDistinct(SelectField...)}, except that it
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
                 *       .with(name("t").fields(«XXX1_XXX2_XXXn(degree, "f")»).as(subselect))
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
                @Support({ DB2, FIREBIRD, HSQLDB, MYSQL_8_0, ORACLE, POSTGRES, SQLSERVER, SYBASE, VERTICA })
                <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> selectDistinct(«SelectField_TN_fieldn(degree)»);
            ''');
            
            outWithImpl.append('''
            
                «generatedMethod»
                @Override
                public final <«TN(degree)»> SelectSelectStep<Record«degree»<«TN(degree)»>> selectDistinct(«SelectField_TN_fieldn(degree)») {
                    return (SelectSelectStep) selectDistinct(new SelectField[] { «fieldn(degree)» });
                }
            ''');
        }

        insert("org.jooq.WithStep", outWithStep, "selectDistinct");
        insert("org.jooq.impl.WithImpl", outWithImpl, "selectDistinct");
    }

    def generateInsert() {
        val outWithStep = new StringBuilder();
        val outWithImpl = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            outWithStep.append('''
            
                /**
                 * Create a new DSL insert statement.
                 * <p>
                 * Example: <code><pre>
                 * using(configuration)
                 *       .with(name("t").fields(«XXX1_XXX2_XXXn(degree, "f")»).as(subselect))
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
                @Support({ POSTGRES, SQLSERVER })
                <R extends Record, «TN(degree)»> InsertValuesStep«degree»<R, «TN(degree)»> insertInto(Table<R> into, «Field_TN_fieldn(degree)»);
            ''');
            
            outWithImpl.append('''
            
                «generatedMethod»
                @Override
                public final <R extends Record, «TN(degree)»> InsertImpl insertInto(Table<R> into, «Field_TN_fieldn(degree)») {
                    return insertInto(into, Arrays.asList(«fieldn(degree)»));
                }
            ''');
        }

        insert("org.jooq.WithStep", outWithStep, "insert");
        insert("org.jooq.impl.WithImpl", outWithImpl, "insert");
    }

    def generateMerge() {
        val outWithStep = new StringBuilder();
        val outWithImpl = new StringBuilder();
        
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            outWithStep.append('''
            
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
                @Support({ SQLSERVER })
                <R extends Record, «TN(degree)»> MergeKeyStep«degree»<R, «TN(degree)»> mergeInto(Table<R> table, «Field_TN_fieldn(degree)»);
            ''');
            
            outWithImpl.append('''
            
                «generatedMethod»
                @Override
                public final <R extends Record, «TN(degree)»> MergeImpl mergeInto(Table<R> table, «Field_TN_fieldn(degree)») {
                    return mergeInto(table, Arrays.asList(«fieldn(degree)»));
                }
            ''');
        }

        insert("org.jooq.WithStep", outWithStep, "merge");
        insert("org.jooq.impl.WithImpl", outWithImpl, "merge");
    }
}