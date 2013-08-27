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
        
        import org.jooq.api.annotation.State;
        import org.jooq.api.annotation.Transition;

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
        @State
        public interface UpdateSetFirstStep<R extends Record> extends UpdateSetStep<R> {
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             * <p>
             * This is simulated using a subquery for the <code>value</code>, where row
             * value expressions aren't supported.
             */
            @Support({ DB2, H2, HSQLDB, INGRES, ORACLE, POSTGRES })
            @Transition(
                name = "SET",
                args = {
                	"Row",
                	"Row"
            	}
            )
            <«TN(degree)»> UpdateWhereStep<R> set(Row«degree»<«TN(degree)»> row, Row«degree»<«TN(degree)»> value);
        «ENDFOR»
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        
            /**
             * Specify a multi-column set clause for the <code>UPDATE</code> statement.
             */
            @Support({ DB2, H2, HSQLDB, INGRES, ORACLE })
            @Transition(
                name = "SET",
                args = {
                	"Row",
                	"Select"
            	}
            )
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