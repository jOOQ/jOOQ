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
        
        import java.util.Collection;
        
        import javax.annotation.Generated;

        /**
         * This type is used for the {@link Merge}'s DSL API.
         * <p>
         * Example: <code><pre>
         * Factory create = new Factory();
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
            MergeNotMatchedValuesStepN<R> whenNotMatchedThenInsert(Field<?>... fields);
        
            /**
             * Add the <code>WHEN MATCHED THEN UPDATE</code> clause to the
             * <code>MERGE</code> statement
             */
            @Support({ CUBRID, DB2, HSQLDB, ORACLE, SQLSERVER, SYBASE })
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
             * Factory create = new Factory();
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
             * Factory create = new Factory();
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
            
            import org.jooq.impl.Executor;
            
            /**
             * This type is used for the H2-specific variant of the {@link Merge}'s DSL API.
             * <p>
             * Example: <code><pre>
             * Factory create = new Factory();
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
                 * {@link Executor#mergeInto(Table, «(1..degree).join(', ', [e | 'Field'])»)}
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