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
class WithAsSteps extends Generators {
    
    def static void main(String[] args) {
        val steps = new WithAsSteps();
        
        steps.generateWithAsSteps();
        steps.generateWithSteps();
        steps.generateWithImpl();
        
        steps.generateDerivedColumnLists();
        steps.generateDerivedColumnListImpl();
        steps.generateNameFields();
    }
    
    def generateWithAsSteps() {
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            out.append('''
            «classHeader»
            package org.jooq;
                        
            import static org.jooq.SQLDialect.DB2;
            import static org.jooq.SQLDialect.FIREBIRD;
            import static org.jooq.SQLDialect.HSQLDB;
            import static org.jooq.SQLDialect.ORACLE;
            import static org.jooq.SQLDialect.POSTGRES;
            import static org.jooq.SQLDialect.SQLSERVER;
            import static org.jooq.SQLDialect.SYBASE;
            import static org.jooq.SQLDialect.VERTICA;
            
            import javax.annotation.Generated;
            
            /**
             * This type is part of the jOOQ DSL to create {@link Select}, {@link Insert},
             * {@link Update}, {@link Delete}, {@link Merge} statements prefixed with a
             * <code>WITH</code> clause and with {@link CommonTableExpression}s.
             * <p>
             * Example:
             * <code><pre>
             * DSL.with("table", "col1", "col2")
             *    .as(
             *        select(one(), two())
             *    )
             *    .select()
             *    .from("table")
             * </pre></code>
             *
             * @author Lukas Eder
             */
            «generatedAnnotation»
            public interface WithAsStep«degree» {
            
                /**
                 * Associate a subselect with a common table expression's table and column names.
                 */
                @Support({ DB2, FIREBIRD, HSQLDB, ORACLE, POSTGRES, SQLSERVER, SYBASE, VERTICA })
                WithStep as(Select<? extends Record«degree»<«(1 .. degree).map(["?"]).join(", ")»>> select);
            }
            ''');
             
            write("org.jooq.WithAsStep" + degree, out);
        }
    }

    def generateWithSteps() {
        val out = new StringBuilder();
        
        for (degree : (1 .. Constants::MAX_ROW_DEGREE)) {
            out.append('''
            
                /**
                 * Add another common table expression to the <code>WITH</code> clause.
                 */
                «generatedMethod»
                @Support({ DB2, FIREBIRD, H2, HSQLDB, ORACLE, POSTGRES, SQLSERVER, SYBASE, VERTICA })
                WithAsStep«degree» with(String alias, «XXXn(degree, "String fieldAlias")»);
            ''');
        }

        insert("org.jooq.WithStep", out, "with");
    }

    def generateWithImpl() {
        val outImplements = new StringBuilder();
        val outWithMethod = new StringBuilder();
        
        for (degree : (1 .. Constants::MAX_ROW_DEGREE)) {
            outImplements.append('''    WithAsStep«degree»,
            ''');
        }
        
        for (degree : (1 .. Constants::MAX_ROW_DEGREE)) {
            outWithMethod.append('''
            
                @Override
                public final WithAsStep«degree» with(String a, «XXXn(degree, "String fieldAlias")») {
                    this.alias = a;
                    this.fieldAliases = new String[] { «XXXn(degree, "fieldAlias")» };
            
                    return this;
                }
            ''');
        }

        insert("org.jooq.impl.WithImpl", outImplements, "implements-with-as-step");
        insert("org.jooq.impl.WithImpl", outWithMethod, "with");
    }
    
    def generateDerivedColumnLists() {
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            out.append('''
            «classHeader»
            package org.jooq;

            import javax.annotation.Generated;

            /**
             * A <code>DerivedColumnList</code> is a name of a table expression with
             * optional derived column list.
             * <p>
             * An example of a correlation name with derived column list is:
             * <code>table(column1, column2)</code>
             *
             * @author Lukas Eder
             */
            «generatedAnnotation»
            public interface DerivedColumnList«degree» extends QueryPart {
            
                /**
                 * Specify a subselect to refer to by the <code>DerivedColumnList</code> to
                 * form a common table expression.
                 */
                <R extends Record«degree»<«(1 .. degree).map(["?"]).join(", ")»>> CommonTableExpression<R> as(Select<R> select);
            
            }
            ''');
             
            write("org.jooq.DerivedColumnList" + degree, out);
        }
    }
    
    def generateDerivedColumnListImpl() {
        val outImpl = new StringBuilder();
        
        for (degree : (1 .. Constants::MAX_ROW_DEGREE)) {
            outImpl.append('''    DerivedColumnList«degree»,
            ''');
        }

        insert("org.jooq.impl.DerivedColumnListImpl", outImpl, "implements-derived-column-list");
    }
    
    def generateNameFields() {
        val outAPI = new StringBuilder();
        val outImpl = new StringBuilder();
        
        for (degree : (1 .. Constants::MAX_ROW_DEGREE)) {
            outAPI.append('''
            
                /**
                 * Add a list of fields to this name to make this name a
                 * {@link DerivedColumnList}.
                 * <p>
                 * The <code>DerivedColumnList</code> can then be used along with a
                 * subselect to form a {@link CommonTableExpression} to be used with
                 * <code>WITH</code> clauses.
                 */
                «generatedMethod»
                @Support({ DB2, FIREBIRD, H2, HSQLDB, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                DerivedColumnList«degree» fields(«XXXn(degree, "String fieldName")»);
            ''');
        }
        
        for (degree : (1 .. Constants::MAX_ROW_DEGREE)) {
            outImpl.append('''
            
                «generatedMethod»
                @Override
                public final DerivedColumnListImpl fields(«XXXn(degree, "String fieldName")») {
                    return fields(new String[] { «XXXn(degree, "fieldName")» });
                }
            ''');
        }

        insert("org.jooq.Name", outAPI, "fields");
        insert("org.jooq.impl.NameImpl", outImpl, "fields");
    }
}