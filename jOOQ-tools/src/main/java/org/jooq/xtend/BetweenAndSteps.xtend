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
        import static org.jooq.Clause.CONDITION;
        import static org.jooq.Clause.CONDITION_BETWEEN;
        import static org.jooq.Clause.CONDITION_BETWEEN_SYMMETRIC;
        import static org.jooq.Clause.CONDITION_NOT_BETWEEN;
        import static org.jooq.Clause.CONDITION_NOT_BETWEEN_SYMMETRIC;
        import static org.jooq.SQLDialect.ACCESS;
        import static org.jooq.SQLDialect.ASE;
        import static org.jooq.SQLDialect.CUBRID;
        import static org.jooq.SQLDialect.DB2;
        import static org.jooq.SQLDialect.DERBY;
        import static org.jooq.SQLDialect.FIREBIRD;
        import static org.jooq.SQLDialect.H2;
        import static org.jooq.SQLDialect.HANA;
        import static org.jooq.SQLDialect.INFORMIX;
        import static org.jooq.SQLDialect.INGRES;
        import static org.jooq.SQLDialect.MARIADB;
        import static org.jooq.SQLDialect.MYSQL;
        import static org.jooq.SQLDialect.ORACLE;
        import static org.jooq.SQLDialect.REDSHIFT;
        import static org.jooq.SQLDialect.SQLITE;
        import static org.jooq.SQLDialect.SQLSERVER;
        import static org.jooq.SQLDialect.SYBASE;
        import static org.jooq.SQLDialect.VERTICA;
        import static org.jooq.impl.DSL.row;

        import javax.annotation.Generated;
        
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»
        import org.jooq.BetweenAndStep«degree»;
        «ENDFOR»
        import org.jooq.BetweenAndStepN;
        import org.jooq.BindContext;
        import org.jooq.Clause;
        import org.jooq.Condition;
        import org.jooq.Configuration;
        import org.jooq.Context;
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

            private static final long     serialVersionUID              = -4666251100802237878L;
            private static final Clause[] CLAUSES_BETWEEN               = { CONDITION, CONDITION_BETWEEN };
            private static final Clause[] CLAUSES_BETWEEN_SYMMETRIC     = { CONDITION, CONDITION_BETWEEN_SYMMETRIC };
            private static final Clause[] CLAUSES_NOT_BETWEEN           = { CONDITION, CONDITION_NOT_BETWEEN };
            private static final Clause[] CLAUSES_NOT_BETWEEN_SYMMETRIC = { CONDITION, CONDITION_NOT_BETWEEN_SYMMETRIC };
        
            private final boolean         symmetric;
            private final boolean         not;
            private final Row             row;
            private final Row             minValue;
            private Row                   maxValue;
        
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
                RowN r = new RowImpl(Tools.fields(record.intoArray(), record.fields()));
                return and(r);
            }

            // ------------------------------------------------------------------------
            // XXX: QueryPart API
            // ------------------------------------------------------------------------

            @Override
            public final void accept(Context<?> ctx) {
                ctx.visit(delegate(ctx.configuration()));
            }
        
            @Override
            public final Clause[] clauses(Context<?> ctx) {
                return null;
            }
            
            private final QueryPartInternal delegate(Configuration configuration) {
                // These casts are safe for RowImpl
                RowN r = (RowN) row;
                RowN min = (RowN) minValue;
                RowN max = (RowN) maxValue;
        
                // These dialects don't support the SYMMETRIC keyword at all
                if (symmetric && asList(ACCESS, ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HANA, INFORMIX, INGRES, MARIADB, MYSQL, ORACLE, REDSHIFT, SQLITE, SQLSERVER, SYBASE, VERTICA).contains(configuration.family())) {
                    return not
                        ? (QueryPartInternal) r.notBetween(min, max).and(r.notBetween(max, min))
                        : (QueryPartInternal) r.between(min, max).or(r.between(max, min));
                }
        
                // These dialects either don't support row value expressions, or they
                // Can't handle row value expressions with the BETWEEN predicate
                else if (row.size() > 1 && asList(ACCESS, CUBRID, DERBY, FIREBIRD, HANA, INFORMIX, INGRES, MARIADB, MYSQL, ORACLE, SQLITE, SQLSERVER, SYBASE).contains(configuration.family())) {
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
                public final void accept(Context<?> context) {
                                   context.visit(row);
                    if (not)       context.sql(" ").keyword("not");
                                   context.sql(" ").keyword("between");
                    if (symmetric) context.sql(" ").keyword("symmetric");
                                   context.sql(" ").visit(minValue);
                                   context.sql(" ").keyword("and");
                                   context.sql(" ").visit(maxValue);
                }

                @Override
                public final Clause[] clauses(Context<?> ctx) {
                    return not ? symmetric ? CLAUSES_NOT_BETWEEN_SYMMETRIC
                                           : CLAUSES_NOT_BETWEEN
                               : symmetric ? CLAUSES_BETWEEN_SYMMETRIC
                                           : CLAUSES_BETWEEN;
                }
            }
        }
        ''');
        
        write("org.jooq.impl.RowBetweenCondition", out);
    }
}