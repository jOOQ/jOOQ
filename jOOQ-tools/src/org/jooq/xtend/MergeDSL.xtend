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

class MergeDSL extends Generators {
    
    def static void main(String[] args) {
        val merge = new MergeDSL();
        merge.generateMergeNotMatchedStep();
        merge.generateMergeNotMatchedValuesStep();
        merge.generateMergeKeyStep();
        merge.generateMergeValuesStep();
        merge.generateMergeImplImplements();
        merge.generateMergeImplValues();
        merge.generateMergeImplWhenNotMatchedThenInsert();
    }
    
    def generateMergeNotMatchedStep() {
        val out = new StringBuilder();
        
        out.append('''
        «classHeader»
        package org.jooq;
        
        import static org.jooq.SQLDialect.CUBRID;
        import static org.jooq.SQLDialect.DB2;
        import static org.jooq.SQLDialect.HSQLDB;
        import static org.jooq.SQLDialect.ORACLE;
        import static org.jooq.SQLDialect.SQLSERVER;
        import static org.jooq.SQLDialect.SYBASE;
        
        import org.jooq.api.annotation.State;
        import org.jooq.api.annotation.Transition;
        
        import java.util.Collection;
        
        import javax.annotation.Generated;

        /**
         * This type is used for the {@link Merge}'s DSL API.
         * <p>
         * Example: <code><pre>
         * DSLContext create = DSL.using(configuration);
         *
         * create.mergeInto(table)
         *       .using(select)
         *       .on(condition)
         *       .whenMatchedThenUpdate()
         *       .set(field1, value1)
         *       .set(field2, value2)
         *       .whenNotMatchedThenInsert(field1, field2)
         *       .values(value1, value2)
         *       .execute();
         * </pre></code>
         *
         * @author Lukas Eder
         */
        «generatedAnnotation»
        @State
        public interface MergeNotMatchedStep<R extends Record> extends MergeFinalStep<R> {
        
            /**
             * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
             * <code>MERGE</code> statement.
             * <p>
             * Unlike the {@link #whenNotMatchedThenInsert(Field...)} and
             * {@link #whenNotMatchedThenInsert(Collection)} methods, this will give
             * access to a MySQL-like API allowing for
             * <code>INSERT SET a = x, b = y</code> syntax.
             */
            @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
            @Transition(
                name = "WHEN NOT MATCHED THEN INSERT"
            )
            MergeNotMatchedSetStep<R> whenNotMatchedThenInsert();
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

            /**
             * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
             * <code>MERGE</code> statement
             */
            @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
            <«TN(degree)»> MergeNotMatchedValuesStep«degree»<R, «TN(degree)»> whenNotMatchedThenInsert(«Field_TN_fieldn(degree)»);
            «ENDFOR»
        
            /**
             * Add the <code>WHEN NOT MATCHED THEN INSERT</code> clause to the
             * <code>MERGE</code> statement
             */
            @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
            @Transition(
                name = "WHEN NOT MATCHED THEN INSERT",
                args = "Field+"
            )
            MergeNotMatchedValuesStepN<R> whenNotMatchedThenInsert(Field<?>... fields);
        
            /**
             * Add the <code>WHEN MATCHED THEN UPDATE</code> clause to the
             * <code>MERGE</code> statement
             */
            @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
            @Transition(
                name = "WHEN NOT MATCHED THEN INSERT",
                args = "Field+"
            )
            MergeNotMatchedValuesStepN<R> whenNotMatchedThenInsert(Collection<? extends Field<?>> fields);
        }
        ''');
         
        write("org.jooq.MergeNotMatchedStep", out);
    }
    
    def generateMergeNotMatchedValuesStep() {
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            out.append('''
            «classHeader»
            package org.jooq;
            
            import static org.jooq.SQLDialect.CUBRID;
            import static org.jooq.SQLDialect.DB2;
            import static org.jooq.SQLDialect.HSQLDB;
            import static org.jooq.SQLDialect.ORACLE;
            import static org.jooq.SQLDialect.SQLSERVER;
            import static org.jooq.SQLDialect.SYBASE;
            
            import java.util.Collection;

            import javax.annotation.Generated;
            
            /**
             * This type is used for the {@link Merge}'s DSL API.
             * <p>
             * Example: <code><pre>
             * DSLContext create = DSL.using(configuration);
             *
             * create.mergeInto(table)
             *       .using(select)
             *       .on(condition)
             *       .whenMatchedThenUpdate()
             *       .set(field1, value1)
             *       .set(field2, value2)
             *       .whenNotMatchedThenInsert(field1, field2)
             *       .values(value1, value2)
             *       .execute();
             * </pre></code>
             *
             * @author Lukas Eder
             */
            «generatedAnnotation»
            public interface MergeNotMatchedValuesStep«degree»<R extends Record, «TN(degree)»> {
            
                /**
                 * Set <code>VALUES</code> for <code>INSERT</code> in the <code>MERGE</code>
                 * statement's <code>WHEN NOT MATCHED THEN INSERT</code> clause.
                 */
                @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
                MergeNotMatchedWhereStep<R> values(«TN_XXXn(degree, "value")»);
            
                /**
                 * Set <code>VALUES</code> for <code>INSERT</code> in the <code>MERGE</code>
                 * statement's <code>WHEN NOT MATCHED THEN INSERT</code> clause.
                 */
                @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
                MergeNotMatchedWhereStep<R> values(«Field_TN_XXXn(degree, "value")»);
            
                /**
                 * Set <code>VALUES</code> for <code>INSERT</code> in the <code>MERGE</code>
                 * statement's <code>WHEN NOT MATCHED THEN INSERT</code> clause.
                 */
                @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
                MergeNotMatchedWhereStep<R> values(Collection<?> values);
            }
            ''');
             
            write("org.jooq.MergeNotMatchedValuesStep" + degree, out);
        }
    }
    
    def generateMergeKeyStep() {
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            out.append('''
            «classHeader»
            package org.jooq;
            
            import static org.jooq.SQLDialect.CUBRID;
            import static org.jooq.SQLDialect.DB2;
            import static org.jooq.SQLDialect.H2;
            import static org.jooq.SQLDialect.HSQLDB;
            import static org.jooq.SQLDialect.ORACLE;
            import static org.jooq.SQLDialect.SQLSERVER;
            import static org.jooq.SQLDialect.SYBASE;
            
            import java.util.Collection;
            
            import javax.annotation.Generated;
            
            /**
             * This type is used for the H2-specific variant of the {@link Merge}'s DSL API.
             * <p>
             * Example: <code><pre>
             * DSLContext create = DSL.using(configuration);
             *
             * create.mergeInto(table, «field1_field2_fieldn(degree)»)
             *       .key(id)
             *       .values(«XXX1_XXX2_XXXn(degree, "value")»)
             *       .execute();
             * </pre></code>
             *
             * @author Lukas Eder
             */
            «generatedAnnotation»
            public interface MergeKeyStep«degree»<R extends Record, «TN(degree)»> extends MergeValuesStep«degree»<R, «TN(degree)»> {
            
                /**
                 * Specify an optional <code>KEY</code> clause.
                 * <p>
                 * Use this optional clause in order to override using the underlying
                 * <code>PRIMARY KEY</code>.
                 */
                @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
                MergeValuesStep«degree»<R, «TN(degree)»> key(Field<?>... keys);
            
                /**
                 * Specify an optional <code>KEY</code> clause.
                 * <p>
                 * Use this optional clause in order to override using the underlying
                 * <code>PRIMARY KEY</code>.
                 */
                @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
                MergeValuesStep«degree»<R, «TN(degree)»> key(Collection<? extends Field<?>> keys);
            }
            ''');
             
            write("org.jooq.MergeKeyStep" + degree, out);
        }
    }
    
    def generateMergeValuesStep() {
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            out.append('''
            «classHeader»
            package org.jooq;
            
            import static org.jooq.SQLDialect.CUBRID;
            import static org.jooq.SQLDialect.DB2;
            import static org.jooq.SQLDialect.H2;
            import static org.jooq.SQLDialect.HSQLDB;
            import static org.jooq.SQLDialect.ORACLE;
            import static org.jooq.SQLDialect.SQLSERVER;
            import static org.jooq.SQLDialect.SYBASE;
            
            import java.util.Collection;
            
            import javax.annotation.Generated;
            
            /**
             * This type is used for the H2-specific variant of the {@link Merge}'s DSL API.
             * <p>
             * Example: <code><pre>
             * using(configuration)
             *       .mergeInto(table, «field1_field2_fieldn(degree)»)
             *       .key(id)
             *       .values(«XXX1_XXX2_XXXn(degree, "value")»)
             *       .execute();
             * </pre></code>
             *
             * @author Lukas Eder
             */
            «generatedAnnotation»
            public interface MergeValuesStep«degree»<R extends Record, «TN(degree)»> {
            
                /**
                 * Specify a <code>VALUES</code> clause
                 */
                @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
                Merge<R> values(«TN_XXXn(degree, "value")»);
            
                /**
                 * Specify a <code>VALUES</code> clause
                 */
                @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
                Merge<R> values(«Field_TN_XXXn(degree, "value")»);
            
                /**
                 * Specify a <code>VALUES</code> clause
                 */
                @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
                Merge<R> values(Collection<?> values);
            
                /**
                 * Use a <code>SELECT</code> statement as the source of values for the
                 * <code>MERGE</code> statement
                 * <p>
                 * This variant of the <code>MERGE .. SELECT</code> statement expects a
                 * select returning exactly as many fields as specified previously in the
                 * <code>INTO</code> clause:
                 * {@link DSLContext#mergeInto(Table, «(1..degree).join(', ', [e | 'Field'])»)}
                 */
                @Support({ CUBRID, DB2, H2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
                Merge<R> select(Select<? extends Record«degree»<«TN(degree)»>> select);
            }
            ''');
             
            write("org.jooq.MergeValuesStep" + degree, out);
        }
    }
    
    def generateMergeImplImplements() {
        val outKeyStep = new StringBuilder();
        val outNotMatchedValuesStep = new StringBuilder();
        
        outKeyStep.append('''
        
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
            MergeKeyStep«degree»<R, «TN(degree)»>,
            «ENDFOR»
        ''')
        
        outNotMatchedValuesStep.append('''
        
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
            MergeNotMatchedValuesStep«degree»<R, «TN(degree)»>,
            «ENDFOR»
        ''')

        insert("org.jooq.impl.MergeImpl", outKeyStep, "implementsKeyStep");
        insert("org.jooq.impl.MergeImpl", outNotMatchedValuesStep, "implementsNotMatchedValuesStep");
    }
    
    def generateMergeImplValues() {
        val out = new StringBuilder();
        
        out.append('''
        
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
            @Override
            public final MergeImpl values(«TN_XXXn(degree, "value")») {
                return values(new Object[] { «XXXn(degree, "value")» });
            }

            «ENDFOR»
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

            @Override
            public final MergeImpl values(«Field_TN_XXXn(degree, "value")») {
                return values(new Field[] { «XXXn(degree, "value")» });
            }
            «ENDFOR»
        ''')

        insert("org.jooq.impl.MergeImpl", out, "values");
    }
    
    def generateMergeImplWhenNotMatchedThenInsert() {
        val out = new StringBuilder();
        
        out.append('''
        
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
            @Override
            @SuppressWarnings("hiding")
            public final <«TN(degree)»> MergeImpl whenNotMatchedThenInsert(«Field_TN_fieldn(degree)») {
                return whenNotMatchedThenInsert(new Field[] { «fieldn(degree)» });
            }

            «ENDFOR»
        ''')

        insert("org.jooq.impl.MergeImpl", out, "whenNotMatchedThenInsert");
    }
}