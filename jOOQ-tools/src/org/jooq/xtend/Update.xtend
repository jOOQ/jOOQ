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

import org.jooq.Constants

/**
 * @author Lukas Eder
 */
class Update extends Generators {
    
    def static void main(String[] args) {
        val update = new Update();
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
            @Support({ DB2, H2, HSQLDB, INGRES, ORACLE, POSTGRES })
            <«TN(degree)»> void addValues(Row«degree»<«TN(degree)»> row, Row«degree»<«TN(degree)»> value);
        «ENDFOR»
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             */
            «generatedMethod»
            @Support({ DB2, H2, HSQLDB, INGRES, ORACLE })
            <«TN(degree)»> void addValues(Row«degree»<«TN(degree)»> row, Select<? extends Record«degree»<«TN(degree)»>> select);
        «ENDFOR»
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
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            «generatedMethod»
            @Override
            public final <«TN(degree)»> void addValues(Row«degree»<«TN(degree)»> row, Select<? extends Record«degree»<«TN(degree)»>> select) {
                addValues0(row, select);
            }
        «ENDFOR»
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
        import static org.jooq.SQLDialect.HSQLDB;
        import static org.jooq.SQLDialect.INGRES;
        import static org.jooq.SQLDialect.ORACLE;
        import static org.jooq.SQLDialect.POSTGRES;
        
        import javax.annotation.Generated;

        /**
         * This type is used for the {@link Update}'s DSL API.
         * <p>
         * Example: <code><pre>
         * Executor create = new Executor(connection, dialect);
         *
         * create.update(table)
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
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             * <p>
             * This is simulated using a subquery for the <code>value</code>, where row
             * value expressions aren't supported.
             */
            @Support({ DB2, H2, HSQLDB, INGRES, ORACLE, POSTGRES })
            <«TN(degree)»> UpdateWhereStep<R> set(Row«degree»<«TN(degree)»> row, Row«degree»<«TN(degree)»> value);
        «ENDFOR»
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             */
            @Support({ DB2, H2, HSQLDB, INGRES, ORACLE })
            <«TN(degree)»> UpdateWhereStep<R> set(Row«degree»<«TN(degree)»> row, Select<? extends Record«degree»<«TN(degree)»>> select);
        «ENDFOR»
        
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
            public final <«TN(degree)»> UpdateWhereStep<R> set(Row«degree»<«TN(degree)»> row, Row«degree»<«TN(degree)»> value) {
                getDelegate().addValues(row, value);
                return this;
            }
        «ENDFOR»
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            «generatedMethod»
            @Override
            public final <«TN(degree)»> UpdateWhereStep<R> set(Row«degree»<«TN(degree)»> row, Select<? extends Record«degree»<«TN(degree)»>> select) {
                getDelegate().addValues(row, select);
                return this;
            }
        «ENDFOR»
        ''');
         
        insert("org.jooq.impl.UpdateImpl", out, "set");
    }
}