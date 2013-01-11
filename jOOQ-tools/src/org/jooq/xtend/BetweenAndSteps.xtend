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
class BetweenAndSteps extends Generators {
    
    def static void main(String[] args) {
        val steps = new BetweenAndSteps();
        steps.generateBetweenAndSteps();
        steps.generateBetweenAndStepsImpl();
    }
    
    def generateBetweenAndSteps() {
        for (degree : (1..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            out.append('''
            «classHeader»
            package org.jooq;

            import org.jooq.Support;

            import javax.annotation.Generated;
            
            /**
             * An intermediate DSL type for the construction of a <code>BETWEEN</code>
             * predicate.
             *
             * @author Lukas Eder
             */
            «generatedAnnotation»
            public interface BetweenAndStep«degree»<«TN(degree)»> {
            
                /**
                 * Create a condition to check this field against some bounds
                 */
                @Support
                Condition and(«Field_TN_XXXn(degree, "maxValue")»);
            
                /**
                 * Create a condition to check this field against some bounds
                 */
                @Support
                Condition and(«TN_XXXn(degree, "maxValue")»);

                /**
                 * Create a condition to check this field against some bounds
                 */
                @Support
                Condition and(Row«degree»<«TN(degree)»> maxValue);
            
                /**
                 * Create a condition to check this field against some bounds
                 */
                @Support
                Condition and(Record«degree»<«TN(degree)»> maxValue);

            }
            ''');
             
            write("org.jooq.BetweenAndStep" + degree, out);
        }
    }

    def generateBetweenAndStepsImpl() {
        val out = new StringBuilder();
        
        out.append('''
        «classHeader»
        package org.jooq.impl;
        
        import static java.util.Arrays.asList;
        import static org.jooq.impl.Factory.row;
        import static org.jooq.impl.Factory.vals;
        import static org.jooq.SQLDialect.ASE;
        import static org.jooq.SQLDialect.CUBRID;
        import static org.jooq.SQLDialect.DB2;
        import static org.jooq.SQLDialect.DERBY;
        import static org.jooq.SQLDialect.FIREBIRD;
        import static org.jooq.SQLDialect.H2;
        import static org.jooq.SQLDialect.MYSQL;
        import static org.jooq.SQLDialect.ORACLE;
        import static org.jooq.SQLDialect.SQLITE;
        import static org.jooq.SQLDialect.SQLSERVER;
        import static org.jooq.SQLDialect.SYBASE;

        import javax.annotation.Generated;
        
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        import org.jooq.BetweenAndStep«degree»;
        «ENDFOR»
        import org.jooq.BetweenAndStepN;
        import org.jooq.BindContext;
        import org.jooq.Condition;
        import org.jooq.Configuration;
        import org.jooq.Field;
        import org.jooq.QueryPartInternal;
        import org.jooq.Record;
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        import org.jooq.Record«degree»;
        «ENDFOR»
        import org.jooq.RenderContext;
        import org.jooq.Row;
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        import org.jooq.Row«degree»;
        «ENDFOR»
        import org.jooq.RowN;
        
        /**
         * @author Lukas Eder
         */
        «generatedAnnotation»
        @SuppressWarnings({ "rawtypes", "unchecked" })
        class RowBetweenCondition<«TN(Constants::MAX_ROW_DEGREE)»> extends AbstractCondition
        implements
        
            // This BetweenAndStep implementation implements all types. Type-safety is
            // being checked through the type-safe API. No need for further checks here
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
            BetweenAndStep«degree»<«TN(degree)»>,
            «ENDFOR»
            BetweenAndStepN {

            private static final long serialVersionUID = -4666251100802237878L;
        
            private final boolean     symmetric;
            private final boolean     not;
            private final Row         row;
            private final Row         minValue;
            private Row               maxValue;
        
            RowBetweenCondition(Row row, Row minValue, boolean not, boolean symmetric) {
                this.row = row;
                this.minValue = minValue;
                this.not = not;
                this.symmetric = symmetric;
            }

            // ------------------------------------------------------------------------
            // XXX: BetweenAndStep API
            // ------------------------------------------------------------------------
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition and(«Field_TN_tn(degree)») {
                return and(row(«tn(degree)»));
            }
            «ENDFOR»

            @Override
            public final Condition and(Field<?>... fields) {
                return and(row(fields));
            }
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition and(«TN_tn(degree)») {
                return and(row(«tn(degree)»));
            }
            «ENDFOR»

            @Override
            public final Condition and(Object... values) {
                return and(row(values));
            }
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition and(Row«degree»<«TN(degree)»> r) {
                this.maxValue = r;
                return this;
            }
            «ENDFOR»

            @Override
            public final Condition and(RowN r) {
                this.maxValue = r;
                return this;
            }
            «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

            @Override
            public final Condition and(Record«degree»<«TN(degree)»> record) {
                return and(record.valuesRow());
            }
            «ENDFOR»

            @Override
            public final Condition and(Record record) {
                RowN r = new RowImpl(vals(record.intoArray(), record.fields()));
                return and(r);
            }
            
            // ------------------------------------------------------------------------
            // XXX: QueryPart API
            // ------------------------------------------------------------------------
            
            @Override
            public final void bind(BindContext context) {
                delegate(context).bind(context);
            }
        
            @Override
            public final void toSQL(RenderContext context) {
                delegate(context).toSQL(context);
            }
        
            private final QueryPartInternal delegate(Configuration configuration) {
                // These casts are safe for RowImpl
                RowN r = (RowN) row;
                RowN min = (RowN) minValue;
                RowN max = (RowN) maxValue;
        
                // These dialects don't support the SYMMETRIC keyword at all
                if (symmetric && asList(ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, MYSQL, ORACLE, SQLITE, SQLSERVER, SYBASE).contains(configuration.getDialect())) {
                    if (not) {
                        return (QueryPartInternal) r.notBetween(min, max).and(r.notBetween(max, min));
                    }
                    else {
                        return (QueryPartInternal) r.between(min, max).or(r.between(max, min));
                    }
                }
        
                // These dialects either don't support row value expressions, or they
                // Can't handle row value expressions with the BETWEEN predicate
                else if (row.getDegree() > 1 && asList(CUBRID, DERBY, FIREBIRD, MYSQL, ORACLE, SQLITE, SQLSERVER, SYBASE).contains(configuration.getDialect())) {
                    Condition result = r.ge(min).and(r.le(max));
        
                    if (not) {
                        result = result.not();
                    }
        
                    return (QueryPartInternal) result;
                }
                else {
                    return new Native();
                }
            }
        
            private class Native extends AbstractQueryPart {
        
                /**
                 * Generated UID
                 */
                private static final long serialVersionUID = 2915703568738921575L;
        
                @Override
                public final void toSQL(RenderContext context) {
                    context.sql(row)
                           .keyword(not ? " not" : "")
                           .keyword(" between ")
                           .keyword(symmetric ? "symmetric " : "")
                           .sql(minValue)
                           .keyword(" and ")
                           .sql(maxValue);
                }
        
                @Override
                public final void bind(BindContext context) {
                    context.bind(row).bind(minValue).bind(maxValue);
                }
            }
        }
        ''');
        
        write("org.jooq.impl.RowBetweenCondition", out);
    }
}