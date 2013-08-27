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
        import static org.jooq.impl.DSL.row;

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
            
            @Override
            public final Condition and(Field f) {
                if (maxValue == null) {
                    return and(row(f));
                }
                else {
                    return super.and(f);
                }
            }
            «FOR degree : (2..Constants::MAX_ROW_DEGREE)»

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
                RowN r = new RowImpl(Utils.fields(record.intoArray(), record.fields()));
                return and(r);
            }
            
            // ------------------------------------------------------------------------
            // XXX: QueryPart API
            // ------------------------------------------------------------------------
            
            @Override
            public final void bind(BindContext context) {
                delegate(context.configuration()).bind(context);
            }
        
            @Override
            public final void toSQL(RenderContext context) {
                delegate(context.configuration()).toSQL(context);
            }
        
            private final QueryPartInternal delegate(Configuration configuration) {
                // These casts are safe for RowImpl
                RowN r = (RowN) row;
                RowN min = (RowN) minValue;
                RowN max = (RowN) maxValue;
        
                // These dialects don't support the SYMMETRIC keyword at all
                if (symmetric && asList(ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, MYSQL, ORACLE, SQLITE, SQLSERVER, SYBASE).contains(configuration.dialect())) {
                    if (not) {
                        return (QueryPartInternal) r.notBetween(min, max).and(r.notBetween(max, min));
                    }
                    else {
                        return (QueryPartInternal) r.between(min, max).or(r.between(max, min));
                    }
                }
        
                // These dialects either don't support row value expressions, or they
                // Can't handle row value expressions with the BETWEEN predicate
                else if (row.size() > 1 && asList(CUBRID, DERBY, FIREBIRD, MYSQL, ORACLE, SQLITE, SQLSERVER, SYBASE).contains(configuration.dialect())) {
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