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
class UpdateDSL extends Generators {
    
    def static void main(String[] args) {
        val update = new UpdateDSL();
        update.generateUpdateQuery();
        update.generateUpdateQueryImpl();
        update.generateUpdateSetFirstStep();
        update.generateUpdateImpl();
    }
    
    def generateUpdateQuery() {
        val out = new StringBuilder();
        
        out.append('''
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             */
            «generatedMethod»
            @Support({ DB2, H2, HANA, HSQLDB, INGRES, ORACLE, POSTGRES })
            <«TN(degree)»> void addValues(Row«degree»<«TN(degree)»> row, Row«degree»<«TN(degree)»> value);
        «ENDFOR»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             */
            «generatedMethod»
            @Support({ DB2, H2, HANA, HSQLDB, INGRES, ORACLE, POSTGRES })
            void addValues(RowN row, RowN value);
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             */
            «generatedMethod»
            @Support({ DB2, H2, HANA, HSQLDB, INGRES, ORACLE })
            <«TN(degree)»> void addValues(Row«degree»<«TN(degree)»> row, Select<? extends Record«degree»<«TN(degree)»>> select);
        «ENDFOR»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             */
            «generatedMethod»
            @Support({ DB2, H2, HANA, HSQLDB, INGRES, ORACLE })
            void addValues(RowN row, Select<?> select);
        ''');
         
        insert("org.jooq.UpdateQuery", out, "addValues");
    }
    
    def generateUpdateQueryImpl() {
        val out = new StringBuilder();
        
        out.append('''
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            «generatedMethod»
            @Override
            public final <«TN(degree)»> void addValues(Row«degree»<«TN(degree)»> row, Row«degree»<«TN(degree)»> value) {
                addValues0(row, value);
            }
        «ENDFOR»
        
            «generatedMethod»
            @Override
            public final void addValues(RowN row, RowN value) {
                addValues0(row, value);
            }
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            «generatedMethod»
            @Override
            public final <«TN(degree)»> void addValues(Row«degree»<«TN(degree)»> row, Select<? extends Record«degree»<«TN(degree)»>> select) {
                addValues0(row, select);
            }
        «ENDFOR»
        
            «generatedMethod»
            @Override
            public final void addValues(RowN row, Select<?> select) {
                addValues0(row, select);
            }
        ''');
         
        insert("org.jooq.impl.UpdateQueryImpl", out, "addValues");
    }
    
    def generateUpdateSetFirstStep() {
        val out = new StringBuilder();
        
        out.append('''
        «classHeader»
        package org.jooq;

        import static org.jooq.SQLDialect.DB2;
        import static org.jooq.SQLDialect.H2;
        import static org.jooq.SQLDialect.HANA;
        import static org.jooq.SQLDialect.HSQLDB;
        import static org.jooq.SQLDialect.INGRES;
        import static org.jooq.SQLDialect.ORACLE;
        import static org.jooq.SQLDialect.POSTGRES;
        import static org.jooq.SQLDialect.POSTGRES_9_5;
        
        import javax.annotation.Generated;
        
        /**
         * This type is used for the {@link Update}'s DSL API.
         * <p>
         * Example: <code><pre>
         * using(configuration)
         *       .update(table)
         *       .set(field1, value1)
         *       .set(field2, value2)
         *       .where(field1.greaterThan(100))
         *       .execute();
         * </pre></code>
         *
         * @author Lukas Eder
         */
        «generatedAnnotation»
        public interface UpdateSetFirstStep<R extends Record> extends UpdateSetStep<R> {
        «FOR degree : (1 .. Constants::MAX_ROW_DEGREE)»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             * <p>
             * This is emulated using a subquery for the <code>value</code>, where row
             * value expressions aren't supported.
             */
            @Support({ DB2, H2, HANA, HSQLDB, INGRES, ORACLE, POSTGRES })
            <«TN(degree)»> UpdateFromStep<R> set(Row«degree»<«TN(degree)»> row, Row«degree»<«TN(degree)»> value);
        «ENDFOR»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             * <p>
             * This is emulated using a subquery for the <code>value</code>, where row
             * value expressions aren't supported.
             */
            @Support({ DB2, H2, HANA, HSQLDB, INGRES, ORACLE, POSTGRES })
            UpdateFromStep<R> set(RowN row, RowN value);
        «FOR degree : (1 .. Constants::MAX_ROW_DEGREE)»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             */
            @Support({ DB2, H2, HANA, HSQLDB, INGRES, ORACLE, POSTGRES_9_5 })
            <«TN(degree)»> UpdateFromStep<R> set(Row«degree»<«TN(degree)»> row, Select<? extends Record«degree»<«TN(degree)»>> select);
        «ENDFOR»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             */
            @Support({ DB2, H2, HANA, HSQLDB, INGRES, ORACLE, POSTGRES_9_5 })
            UpdateFromStep<R> set(RowN row, Select<?> select);
        
        }
        ''');
         
        write("org.jooq.UpdateSetFirstStep", out);
    }
    
    def generateUpdateImpl() {
        val out = new StringBuilder();
        
        out.append('''
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            «generatedMethod»
            @Override
            public final <«TN(degree)»> UpdateFromStep<R> set(Row«degree»<«TN(degree)»> row, Row«degree»<«TN(degree)»> value) {
                getDelegate().addValues(row, value);
                return this;
            }
        «ENDFOR»
        
            «generatedMethod»
            @Override
            public final UpdateFromStep<R> set(RowN row, RowN value) {
                getDelegate().addValues(row, value);
                return this;
            }
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            «generatedMethod»
            @Override
            public final <«TN(degree)»> UpdateFromStep<R> set(Row«degree»<«TN(degree)»> row, Select<? extends Record«degree»<«TN(degree)»>> select) {
                getDelegate().addValues(row, select);
                return this;
            }
        «ENDFOR»
        
            «generatedMethod»
            @Override
            public final UpdateFromStep<R> set(RowN row, Select<?> select) {
                getDelegate().addValues(row, select);
                return this;
            }
        ''');
         
        insert("org.jooq.impl.UpdateImpl", out, "set");
    }
}