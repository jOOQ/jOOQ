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
class Rows extends Generators {

    def static void main(String[] args) {
        val rows = new Rows();
        rows.generateRowClasses();
        rows.generateRowImpl();
    }

    def generateRowClasses() {
        for (degree : (0..Constants::MAX_ROW_DEGREE)) {
            val out = new StringBuilder();
            
            val typeSuffix = typeSuffix(degree)
            val typeSuffixRaw = typeSuffixRaw(degree)
            val recTypeSuffix = recTypeSuffix(degree)
            val recTypeSuffixRaw = recTypeSuffixRaw(degree)

            out.append('''
            «classHeader»
            package org.jooq;

            import static org.jooq.SQLDialect.ASE;
            import static org.jooq.SQLDialect.CUBRID;
            import static org.jooq.SQLDialect.DB2;
            import static org.jooq.SQLDialect.DERBY;
            import static org.jooq.SQLDialect.FIREBIRD;
            import static org.jooq.SQLDialect.H2;
            import static org.jooq.SQLDialect.HANA;
            import static org.jooq.SQLDialect.HSQLDB;
            import static org.jooq.SQLDialect.INGRES;
            import static org.jooq.SQLDialect.MARIADB;
            import static org.jooq.SQLDialect.MYSQL;
            import static org.jooq.SQLDialect.ORACLE;
            import static org.jooq.SQLDialect.POSTGRES;
            import static org.jooq.SQLDialect.SQLSERVER;
            import static org.jooq.SQLDialect.SYBASE;

            import org.jooq.Comparator;
            import org.jooq.impl.DSL;

            import java.util.Collection;

            import javax.annotation.Generated;

            /**
             * A model type for a row value expression with degree <code>«IF degree > 0»«degree»«ELSE»N > «Constants::MAX_ROW_DEGREE»«ENDIF»</code>.
             * <p>
             * Note: Not all databases support row value expressions, but many row value
             * expression operations can be emulated on all databases. See relevant row
             * value expression method Javadocs for details.
             *
             * @author Lukas Eder
             */
            «generatedAnnotation»
            public interface Row«typeSuffix» extends Row {
            «IF degree > 0»
            
                // ------------------------------------------------------------------------
                // Field accessors
                // ------------------------------------------------------------------------
                «FOR d : (1..degree)»

                /**
                 * Get the «first(d)» field.
                 */
                Field<T«d»> field«d»();
                «ENDFOR»
            «ENDIF»
            
                // ------------------------------------------------------------------------
                // Generic comparison predicates
                // ------------------------------------------------------------------------
                
                /**
                 * Compare this row value expression with another row value expression
                 * using a dynamic comparator.
                 * <p>
                 * See the explicit comparison methods for details. Note, not all
                 * {@link Comparator} types are supported
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 * @see #notEqual(Row«typeSuffixRaw»)
                 * @see #lessThan(Row«typeSuffixRaw»)
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition compare(Comparator comparator, Row«typeSuffix» row);
            
                /**
                 * Compare this row value expression with a record
                 * using a dynamic comparator.
                 * <p>
                 * See the explicit comparison methods for details. Note, not all
                 * {@link Comparator} types are supported
                 *
                 * @see #equal(Record«recTypeSuffixRaw»)
                 * @see #notEqual(Record«recTypeSuffixRaw»)
                 * @see #lessThan(Record«recTypeSuffixRaw»)
                 * @see #lessOrEqual(Record«recTypeSuffixRaw»)
                 * @see #greaterThan(Record«recTypeSuffixRaw»)
                 * @see #greaterOrEqual(Record«recTypeSuffixRaw»)
                 */
                @Support
                Condition compare(Comparator comparator, Record«recTypeSuffix» record);
            
                /**
                 * Compare this row value expression with another row value expression
                 * using a dynamic comparator.
                 * <p>
                 * See the explicit comparison methods for details. Note, not all
                 * {@link Comparator} types are supported
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 * @see #notEqual(Row«typeSuffixRaw»)
                 * @see #lessThan(Row«typeSuffixRaw»)
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition compare(Comparator comparator, «TN_tn(degree)»);
            
                /**
                 * Compare this row value expression with another row value expression
                 * using a dynamic comparator.
                 * <p>
                 * See the explicit comparison methods for details. Note, not all
                 * {@link Comparator} types are supported
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 * @see #notEqual(Row«typeSuffixRaw»)
                 * @see #lessThan(Row«typeSuffixRaw»)
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition compare(Comparator comparator, «Field_TN_tn(degree)»);
            
                /**
                 * Compare this row value expression with a subselect
                 * using a dynamic comparator.
                 * <p>
                 * See the explicit comparison methods for details. Note, not all
                 * {@link Comparator} types are supported
                 *
                 * @see #equal(Select)
                 * @see #notEqual(Select)
                 * @see #lessThan(Select)
                 * @see #lessOrEqual(Select)
                 * @see #greaterThan(Select)
                 * @see #greaterOrEqual(Select)
                 */
                @Support
                Condition compare(Comparator comparator, Select<? extends Record«recTypeSuffix»> select);
            
                /**
                 * Compare this row value expression with a subselect
                 * using a dynamic comparator.
                 * <p>
                 * See the explicit comparison methods for details. Note, not all
                 * {@link Comparator} types are supported
                 *
                 * @see #equal(Select)
                 * @see #notEqual(Select)
                 * @see #lessThan(Select)
                 * @see #lessOrEqual(Select)
                 * @see #greaterThan(Select)
                 * @see #greaterOrEqual(Select)
                 */
                @Support
                Condition compare(Comparator comparator, QuantifiedSelect<? extends Record«recTypeSuffix»> select);
            
                // ------------------------------------------------------------------------
                // Equal / Not equal comparison predicates
                // ------------------------------------------------------------------------

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 * <p>
                 * Row equality comparison predicates can be emulated in those databases
                 * that do not support such predicates natively:
                 * <code>(A, B) = (1, 2)</code> is equivalent to
                 * <code>A = 1 AND B = 2</code>
                 */
                @Support
                Condition equal(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition equal(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition equal(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition equal(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition equal(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for equality.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HANA, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition equal(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition eq(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition eq(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition eq(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition eq(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for equality.
                 *
                 * @see #equal(Row«typeSuffixRaw»)
                 */
                @Support
                Condition eq(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for equality.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HANA, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition eq(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * non-equality.
                 * <p>
                 * Row non-equality comparison predicates can be emulated in those
                 * databases that do not support such predicates natively:
                 * <code>(A, B) &lt;> (1, 2)</code> is equivalent to
                 * <code>NOT(A = 1 AND B = 2)</code>
                 */
                @Support
                Condition notEqual(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for non-equality
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition notEqual(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for.
                 * non-equality
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition notEqual(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition notEqual(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition notEqual(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for non-equality.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HANA, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition notEqual(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ne(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ne(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ne(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ne(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for non-equality.
                 *
                 * @see #notEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ne(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for non-equality.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HANA, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition ne(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                // ------------------------------------------------------------------------
                // Ordering comparison predicates
                // ------------------------------------------------------------------------

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 * <p>
                 * Row order comparison predicates can be emulated in those
                 * databases that do not support such predicates natively:
                 * <code>(A, B, C) &lt; (1, 2, 3)</code> is equivalent to
                 * <code>A &lt; 1 OR (A = 1 AND B &lt; 2) OR (A = 1 AND B = 2 AND C &lt; 3)</code>
                 */
                @Support
                Condition lessThan(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessThan(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessThan(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessThan(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessThan(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition lessThan(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lt(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lt(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lt(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lt(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #lessThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lt(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition lt(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 * <p>
                 * Row order comparison predicates can be emulated in those
                 * databases that do not support such predicates natively:
                 * <code>(A, B) &lt;= (1, 2)</code> is equivalent to
                 * <code>A &lt; 1 OR (A = 1 AND B &lt; 2) OR (A = 1 AND B = 2)</code>
                 */
                @Support
                Condition lessOrEqual(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessOrEqual(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessOrEqual(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessOrEqual(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition lessOrEqual(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition lessOrEqual(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition le(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition le(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition le(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition le(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #lessOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition le(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition le(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 * <p>
                 * Row order comparison predicates can be emulated in those
                 * databases that do not support such predicates natively:
                 * <code>(A, B, C) > (1, 2, 3)</code> is equivalent to
                 * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2 AND C > 3)</code>
                 */
                @Support
                Condition greaterThan(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterThan(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterThan(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterThan(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterThan(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition greaterThan(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition gt(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition gt(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition gt(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition gt(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #greaterThan(Row«typeSuffixRaw»)
                 */
                @Support
                Condition gt(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition gt(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 * <p>
                 * Row order comparison predicates can be emulated in those
                 * databases that do not support such predicates natively:
                 * <code>(A, B) >= (1, 2)</code> is equivalent to
                 * <code>A > 1 OR (A = 1 AND B > 2) OR (A = 1 AND B = 2)</code>
                 */
                @Support
                Condition greaterOrEqual(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterOrEqual(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterOrEqual(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterOrEqual(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition greaterOrEqual(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition greaterOrEqual(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ge(Row«typeSuffix» row);

                /**
                 * Compare this row value expression with a record for order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ge(Record«recTypeSuffix» record);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ge(«TN_tn(degree)»);

                /**
                 * Compare this row value expression with another row value expression for
                 * order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ge(«Field_TN_tn(degree)»);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see #greaterOrEqual(Row«typeSuffixRaw»)
                 */
                @Support
                Condition ge(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a subselect for order.
                 *
                 * @see DSL#all(Field)
                 * @see DSL#all(Select)
                 * @see DSL#all(Object...)
                 * @see DSL#any(Field)
                 * @see DSL#any(Select)
                 * @see DSL#any(Object...)
                 */
                @Support({ ASE, CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, INGRES, MARIADB, MYSQL, ORACLE, POSTGRES, SQLSERVER, SYBASE })
                Condition ge(QuantifiedSelect<? extends Record«recTypeSuffix»> select);

                // ------------------------------------------------------------------------
                // [NOT] BETWEEN predicates
                // ------------------------------------------------------------------------

                /**
                 * Check if this row value expression is within a range of two other row
                 * value expressions.
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» between(«TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is within a range of two other row
                 * value expressions.
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» between(«Field_TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is within a range of two other row
                 * value expressions.
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» between(Row«typeSuffix» minValue);

                /**
                 * Check if this row value expression is within a range of two records.
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» between(Record«recTypeSuffix» minValue);

                /**
                 * Check if this row value expression is within a range of two other row
                 * value expressions.
                 * <p>
                 * This is the same as calling <code>between(minValue).and(maxValue)</code>
                 * <p>
                 * The expression <code>A BETWEEN B AND C</code> is equivalent to the
                 * expression <code>A >= B AND A &lt;= C</code> for those SQL dialects that do
                 * not properly support the <code>BETWEEN</code> predicate for row value
                 * expressions
                 */
                @Support
                Condition between(Row«typeSuffix» minValue,
                                  Row«typeSuffix» maxValue);

                /**
                 * Check if this row value expression is within a range of two records.
                 * <p>
                 * This is the same as calling <code>between(minValue).and(maxValue)</code>
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                Condition between(Record«recTypeSuffix» minValue,
                                  Record«recTypeSuffix» maxValue);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #betweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» betweenSymmetric(«TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #betweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» betweenSymmetric(«Field_TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #betweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» betweenSymmetric(Row«typeSuffix» minValue);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * records.
                 *
                 * @see #betweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» betweenSymmetric(Record«recTypeSuffix» minValue);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * other row value expressions.
                 * <p>
                 * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
                 * <p>
                 * The expression <code>A BETWEEN SYMMETRIC B AND C</code> is equivalent to
                 * the expression <code>(A >= B AND A &lt;= C) OR (A >= C AND A &lt;= B)</code>
                 * for those SQL dialects that do not properly support the
                 * <code>BETWEEN</code> predicate for row value expressions
                 */
                @Support
                Condition betweenSymmetric(Row«typeSuffix» minValue,
                                           Row«typeSuffix» maxValue);

                /**
                 * Check if this row value expression is within a symmetric range of two
                 * records.
                 * <p>
                 * This is the same as calling <code>betweenSymmetric(minValue).and(maxValue)</code>
                 *
                 * @see #betweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                Condition betweenSymmetric(Record«recTypeSuffix» minValue,
                                           Record«recTypeSuffix» maxValue);

                /**
                 * Check if this row value expression is not within a range of two other
                 * row value expressions.
                 *
                 * @see #between(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetween(«TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is not within a range of two other
                 * row value expressions.
                 *
                 * @see #notBetween(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetween(«Field_TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is not within a range of two other
                 * row value expressions.
                 *
                 * @see #notBetween(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetween(Row«typeSuffix» minValue);

                /**
                 * Check if this row value expression is within a range of two records.
                 *
                 * @see #notBetween(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetween(Record«recTypeSuffix» minValue);

                /**
                 * Check if this row value expression is not within a range of two other
                 * row value expressions.
                 * <p>
                 * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
                 * <p>
                 * The expression <code>A NOT BETWEEN B AND C</code> is equivalent to the
                 * expression <code>A &lt; B OR A > C</code> for those SQL dialects that do
                 * not properly support the <code>BETWEEN</code> predicate for row value
                 * expressions
                 */
                @Support
                Condition notBetween(Row«typeSuffix» minValue,
                                     Row«typeSuffix» maxValue);

                /**
                 * Check if this row value expression is within a range of two records.
                 * <p>
                 * This is the same as calling <code>notBetween(minValue).and(maxValue)</code>
                 *
                 * @see #notBetween(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                Condition notBetween(Record«recTypeSuffix» minValue,
                                     Record«recTypeSuffix» maxValue);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #notBetweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetweenSymmetric(«TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #notBetweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetweenSymmetric(«Field_TN_XXXn(degree, "minValue")»);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * other row value expressions.
                 *
                 * @see #notBetweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetweenSymmetric(Row«typeSuffix» minValue);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * records.
                 *
                 * @see #notBetweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                BetweenAndStep«typeSuffix» notBetweenSymmetric(Record«recTypeSuffix» minValue);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * other row value expressions.
                 * <p>
                 * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
                 * <p>
                 * The expression <code>A NOT BETWEEN SYMMETRIC B AND C</code> is equivalent
                 * to the expression <code>(A &lt; B OR A > C) AND (A &lt; C OR A > B)</code> for
                 * those SQL dialects that do not properly support the <code>BETWEEN</code>
                 * predicate for row value expressions
                 */
                @Support
                Condition notBetweenSymmetric(Row«typeSuffix» minValue,
                                              Row«typeSuffix» maxValue);

                /**
                 * Check if this row value expression is not within a symmetric range of two
                 * records.
                 * <p>
                 * This is the same as calling <code>notBetweenSymmetric(minValue).and(maxValue)</code>
                 *
                 * @see #notBetweenSymmetric(Row«typeSuffixRaw», Row«typeSuffixRaw»)
                 */
                @Support
                Condition notBetweenSymmetric(Record«recTypeSuffix» minValue,
                                              Record«recTypeSuffix» maxValue);

                // ------------------------------------------------------------------------
                // [NOT] DISTINCT predicates
                // ------------------------------------------------------------------------

«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * distinctness.
«««                 */
«««                @Support
«««                Condition isDistinctFrom(Row«typeSuffix» row);
«««
«««                /**
«««                 * Compare this row value expression with a record for distinctness.
«««                 *
«««                 * @see #isDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isDistinctFrom(Record«recTypeSuffix» record);
«««
«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * distinctness.
«««                 *
«««                 * @see #isDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isDistinctFrom(«TN_tn(degree)»);
«««
«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * distinctness.
«««                 *
«««                 * @see #isDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isDistinctFrom(«Field_TN_tn(degree)»);
«««
«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * non-distinctness.
«««                 */
«««                @Support
«««                Condition isNotDistinctFrom(Row«typeSuffix» row);
«««
«««                /**
«««                 * Compare this row value expression with a record for non-distinctness.
«««                 *
«««                 * @see #isNotDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isNotDistinctFrom(Record«recTypeSuffix» record);
«««
«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * non-distinctness.
«««                 *
«««                 * @see #isNotDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isNotDistinctFrom(«TN_tn(degree)»);
«««
«««                /**
«««                 * Compare this row value expression with another row value expression for
«««                 * non-distinctness.
«««                 *
«««                 * @see #isNotDistinctFrom(Row«typeSuffixRaw»)
«««                 */
«««                @Support
«««                Condition isNotDistinctFrom(«Field_TN_tn(degree)»);

                // ------------------------------------------------------------------------
                // [NOT] IN predicates
                // ------------------------------------------------------------------------

                /**
                 * Compare this row value expression with a set of row value expressions for
                 * equality.
                 * <p>
                 * Row IN predicates can be emulated in those databases that do not support
                 * such predicates natively: <code>(A, B) IN ((1, 2), (3, 4))</code> is
                 * equivalent to <code>((A, B) = (1, 2)) OR ((A, B) = (3, 4))</code>, which
                 * is equivalent to <code>(A = 1 AND B = 2) OR (A = 3 AND B = 4)</code>
                 */
                @Support
                Condition in(Collection<? extends Row«typeSuffix»> rows);

                /**
                 * Compare this row value expression with a set of records for
                 * equality.
                 * <p>
                 * Row IN predicates can be emulated in those databases that do not support
                 * such predicates natively: <code>(A, B) IN ((1, 2), (3, 4))</code> is
                 * equivalent to <code>((A, B) = (1, 2)) OR ((A, B) = (3, 4))</code>, which
                 * is equivalent to <code>(A = 1 AND B = 2) OR (A = 3 AND B = 4)</code>
                 */
                @Support
                Condition in(Result<? extends Record«recTypeSuffix»> result);

                /**
                 * Compare this row value expression with a set of row value expressions for
                 * equality.
                 *
                 * @see #in(Collection)
                 */
                @Support
                Condition in(Row«typeSuffix»... rows);

                /**
                 * Compare this row value expression with a set of records for equality.
                 *
                 * @see #in(Collection)
                 */
                @Support
                Condition in(Record«recTypeSuffix»... record);

                /**
                 * Compare this row value expression with a subselect for equality.
                 *
                 * @see #in(Collection)
                 */
                @Support
                Condition in(Select<? extends Record«recTypeSuffix»> select);

                /**
                 * Compare this row value expression with a set of row value expressions for
                 * equality.
                 * <p>
                 * Row NOT IN predicates can be emulated in those databases that do not
                 * support such predicates natively:
                 * <code>(A, B) NOT IN ((1, 2), (3, 4))</code> is equivalent to
                 * <code>NOT(((A, B) = (1, 2)) OR ((A, B) = (3, 4)))</code>, which is
                 * equivalent to <code>NOT((A = 1 AND B = 2) OR (A = 3 AND B = 4))</code>
                 */
                @Support
                Condition notIn(Collection<? extends Row«typeSuffix»> rows);

                /**
                 * Compare this row value expression with a set of records for
                 * equality.
                 * <p>
                 * Row NOT IN predicates can be emulated in those databases that do not
                 * support such predicates natively:
                 * <code>(A, B) NOT IN ((1, 2), (3, 4))</code> is equivalent to
                 * <code>NOT(((A, B) = (1, 2)) OR ((A, B) = (3, 4)))</code>, which is
                 * equivalent to <code>NOT((A = 1 AND B = 2) OR (A = 3 AND B = 4))</code>
                 */
                @Support
                Condition notIn(Result<? extends Record«recTypeSuffix»> result);

                /**
                 * Compare this row value expression with a set of row value expressions for
                 * equality.
                 *
                 * @see #notIn(Collection)
                 */
                @Support
                Condition notIn(Row«typeSuffix»... rows);

                /**
                 * Compare this row value expression with a set of records for non-equality.
                 *
                 * @see #notIn(Collection)
                 */
                @Support
                Condition notIn(Record«recTypeSuffix»... record);

                /**
                 * Compare this row value expression with a subselect for non-equality.
                 *
                 * @see #notIn(Collection)
                 */
                @Support
                Condition notIn(Select<? extends Record«recTypeSuffix»> select);

                «IF degree == 2»
                // ------------------------------------------------------------------------
                // Row2-specific OVERLAPS predicate
                // ------------------------------------------------------------------------

                /**
                 * Check if this row value expression overlaps another row value expression.
                 * <p>
                 * The SQL standard specifies a temporal <code>OVERLAPS</code> predicate,
                 * which comes in two flavours:
                 * <ul>
                 * <li><code>(DATE, DATE) OVERLAPS (DATE, DATE)</code></li>
                 * <li><code>(DATE, INTERVAL) OVERLAPS (DATE, INTERVAL)</code></li>
                 * </ul>
                 * <p>
                 * jOOQ also supports arbitrary 2-degree row value expression comparisons,
                 * by emulating them as such <code><pre>
                 * -- This predicate
                 * (A, B) OVERLAPS (C, D)
                 *
                 * -- can be emulated as such
                 * (C &lt;= B) AND (A &lt;= D)
                 * </pre></code>
                 */
                @Support
                Condition overlaps(T1 t1, T2 t2);

                /**
                 * Check if this row value expression overlaps another row value expression.
                 * <p>
                 * The SQL standard specifies a temporal <code>OVERLAPS</code> predicate,
                 * which comes in two flavours:
                 * <ul>
                 * <li><code>(DATE, DATE) OVERLAPS (DATE, DATE)</code></li>
                 * <li><code>(DATE, INTERVAL) OVERLAPS (DATE, INTERVAL)</code></li>
                 * </ul>
                 * <p>
                 * jOOQ also supports arbitrary 2-degree row value expression comparisons,
                 * by emulating them as such <code><pre>
                 * -- This predicate
                 * (A, B) OVERLAPS (C, D)
                 *
                 * -- can be emulated as such
                 * (C &lt;= B) AND (A &lt;= D)
                 * </pre></code>
                 */
                @Support
                Condition overlaps(Field<T1> t1, Field<T2> t2);

                /**
                 * Check if this row value expression overlaps another row value expression.
                 * <p>
                 * The SQL standard specifies a temporal <code>OVERLAPS</code> predicate,
                 * which comes in two flavours:
                 * <ul>
                 * <li><code>(DATE, DATE) OVERLAPS (DATE, DATE)</code></li>
                 * <li><code>(DATE, INTERVAL) OVERLAPS (DATE, INTERVAL)</code></li>
                 * </ul>
                 * <p>
                 * jOOQ also supports arbitrary 2-degree row value expression comparisons,
                 * by emulating them as such <code><pre>
                 * -- This predicate
                 * (A, B) OVERLAPS (C, D)
                 *
                 * -- can be emulated as such
                 * (C &lt;= B) AND (A &lt;= D)
                 * </pre></code>
                 */
                @Support
                Condition overlaps(Row2<T1, T2> row);

                «ENDIF»
            }
            ''');

            write("org.jooq.Row" + degreeOrN(degree), out);
        }
    }

    def generateRowImpl() {
        val out = new StringBuilder();

        out.append('''
    «classHeader»
    package org.jooq.impl;

    import static org.jooq.Clause.FIELD_ROW;
    import static org.jooq.SQLDialect.INFORMIX;
    import static org.jooq.impl.DSL.row;

    import java.util.Arrays;
    import java.util.Collection;

    import javax.annotation.Generated;

    «FOR degree : (0..Constants::MAX_ROW_DEGREE)»
    import org.jooq.BetweenAndStep«typeSuffixRaw(degree)»;
    «ENDFOR»
    import org.jooq.Clause;
    import org.jooq.Comparator;
    import org.jooq.Condition;
    import org.jooq.Context;
    import org.jooq.DataType;
    import org.jooq.Field;
    import org.jooq.Name;
    import org.jooq.QuantifiedSelect;
    «FOR degree : (0..Constants::MAX_ROW_DEGREE)»
    import org.jooq.Record«recTypeSuffixRaw(degree)»;
    «ENDFOR»
    import org.jooq.Row;
    «FOR degree : (0..Constants::MAX_ROW_DEGREE)»
    import org.jooq.Row«typeSuffixRaw(degree)»;
    «ENDFOR»
    import org.jooq.Result;
    import org.jooq.Select;

    /**
     * @author Lukas Eder
     */
    «generatedAnnotation»
    @SuppressWarnings({ "rawtypes", "unchecked" })
    class RowImpl<«TN(Constants::MAX_ROW_DEGREE)»> extends AbstractQueryPart
    implements

        // This row implementation implements all row types. Type-safety is
        // being checked through the type-safe API. No need for further checks here
        «FOR degree : (0..Constants::MAX_ROW_DEGREE) SEPARATOR ','»
        Row«typeSuffix(degree)»
        «ENDFOR»
    {

        /**
         * Generated UID
         */
        private static final long     serialVersionUID = -929427349071556318L;
        private static final Clause[] CLAUSES          = { FIELD_ROW };
    
        final Fields                  fields;

        RowImpl(Field<?>... fields) {
            this(new Fields(fields));
        }

        RowImpl(Collection<? extends Field<?>> fields) {
            this(new Fields(fields));
        }

        RowImpl(Fields fields) {
            super();

            this.fields = fields;
        }

        // ------------------------------------------------------------------------
        // XXX: QueryPart API
        // ------------------------------------------------------------------------

        @Override
        public final void accept(Context<?> context) {

            /* [pro] */
            if (context.family() == INFORMIX)
                context.keyword("row").sql(" ");

            /* [/pro] */
            context.sql("(");

            String separator = "";
            for (Field<?> field : fields.fields) {
                context.sql(separator);
                context.visit(field);

                separator = ", ";
            }

            context.sql(")");
        }
    
        @Override
        public final Clause[] clauses(Context<?> ctx) {
            return CLAUSES;
        }

        // ------------------------------------------------------------------------
        // XXX: Row accessor API
        // ------------------------------------------------------------------------

        @Override
        public final int size() {
            return fields.size();
        }

        @Override
        public final <T> Field<T> field(Field<T> field) {
            return fields.field(field);
        }

        @Override
        public final Field<?> field(String name) {
            return fields.field(name);
        }

        @Override
        public final <T> Field<T> field(String name, Class<T> type) {
            return fields.field(name, type);
        }

        @Override
        public final <T> Field<T> field(String name, DataType<T> dataType) {
            return fields.field(name, dataType);
        }

        @Override
        public final Field<?> field(Name name) {
            return fields.field(name);
        }

        @Override
        public final <T> Field<T> field(Name name, Class<T> type) {
            return fields.field(name, type);
        }

        @Override
        public final <T> Field<T> field(Name name, DataType<T> dataType) {
            return fields.field(name, dataType);
        }

        @Override
        public final Field<?> field(int index) {
            return fields.field(index);
        }

        @Override
        public final <T> Field<T> field(int index, Class<T> type) {
            return fields.field(index, type);
        }

        @Override
        public final <T> Field<T> field(int index, DataType<T> dataType) {
            return fields.field(index, dataType);
        }

        @Override
        public final Field<?>[] fields() {
            return fields.fields();
        }
    
        @Override
        public final Field<?>[] fields(Field<?>... f) {
            return fields.fields(f);
        }
    
        @Override
        public final Field<?>[] fields(String... fieldNames) {
            return fields.fields(fieldNames);
        }

        @Override
        public final Field<?>[] fields(Name... fieldNames) {
            return fields.fields(fieldNames);
        }
    
        @Override
        public final Field<?>[] fields(int... fieldIndexes) {
            return fields.fields(fieldIndexes);
        }

        @Override
        public final int indexOf(Field<?> field) {
            return fields.indexOf(field);
        }

        @Override
        public final int indexOf(String fieldName) {
            return fields.indexOf(fieldName);
        }

        @Override
        public final int indexOf(Name fieldName) {
            return fields.indexOf(fieldName);
        }

        @Override
        public final Class<?>[] types() {
            return fields.types();
        }
    
        @Override
        public final Class<?> type(int fieldIndex) {
            return fields.type(fieldIndex);
        }
    
        @Override
        public final Class<?> type(String fieldName) {
            return fields.type(fieldName);
        }
    
        @Override
        public final Class<?> type(Name fieldName) {
            return fields.type(fieldName);
        }
    
        @Override
        public final DataType<?>[] dataTypes() {
            return fields.dataTypes();
        }
    
        @Override
        public final DataType<?> dataType(int fieldIndex) {
            return fields.dataType(fieldIndex);
        }
    
        @Override
        public final DataType<?> dataType(String fieldName) {
            return fields.dataType(fieldName);
        }
    
        @Override
        public final DataType<?> dataType(Name fieldName) {
            return fields.dataType(fieldName);
        }
        «FOR degree : (1..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Field<T«degree»> field«degree»() {
            return fields.field(«degree - 1»);
        }
        «ENDFOR»

        // ------------------------------------------------------------------------
        // [NOT] NULL predicates
        // ------------------------------------------------------------------------

        @Override
        public final Condition isNull() {
            return new RowIsNull(this, true);
        }

        @Override
        public final Condition isNotNull() {
            return new RowIsNull(this, false);
        }

        // ------------------------------------------------------------------------
        // Generic comparison predicates
        // ------------------------------------------------------------------------
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition compare(Comparator comparator, Row«typeSuffix(degree)» row) {
            return new RowCondition(this, row, comparator);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition compare(Comparator comparator, Record«recTypeSuffix(degree)» record) {
            return new RowCondition(this, record.valuesRow(), comparator);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition compare(Comparator comparator, «TN_tn(degree)») {
            return compare(comparator, row(«tn(degree)»));
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition compare(Comparator comparator, «Field_TN_tn(degree)») {
            return compare(comparator, row(«tn(degree)»));
        }
        «ENDFOR»

        @Override
        public final Condition compare(Comparator comparator, Select select) {
            return new RowSubqueryCondition(this, select, comparator);
        }

        @Override
        public final Condition compare(Comparator comparator, QuantifiedSelect select) {
            return new RowSubqueryCondition(this, select, comparator);
        }

        // ------------------------------------------------------------------------
        // Equal / Not equal comparison predicates
        // ------------------------------------------------------------------------
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition equal(Row«typeSuffix(degree)» row) {
            return compare(Comparator.EQUALS, row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition equal(Record«recTypeSuffix(degree)» record) {
            return compare(Comparator.EQUALS, record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition equal(«TN_tn(degree)») {
            return compare(Comparator.EQUALS, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition equal(«Field_TN_tn(degree)») {
            return compare(Comparator.EQUALS, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition eq(Row«typeSuffix(degree)» row) {
            return equal(row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition eq(Record«recTypeSuffix(degree)» record) {
            return equal(record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition eq(«TN_tn(degree)») {
            return equal(«tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition eq(«Field_TN_tn(degree)») {
            return equal(«tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition notEqual(Row«typeSuffix(degree)» row) {
            return compare(Comparator.NOT_EQUALS, row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition notEqual(Record«recTypeSuffix(degree)» record) {
            return compare(Comparator.NOT_EQUALS, record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition notEqual(«TN_tn(degree)») {
            return compare(Comparator.NOT_EQUALS, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition notEqual(«Field_TN_tn(degree)») {
            return compare(Comparator.NOT_EQUALS, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition ne(Row«typeSuffix(degree)» row) {
            return notEqual(row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition ne(Record«recTypeSuffix(degree)» record) {
            return notEqual(record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition ne(«TN_tn(degree)») {
            return notEqual(«tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition ne(«Field_TN_tn(degree)») {
            return notEqual(«tn(degree)»);
        }
        «ENDFOR»

        // ------------------------------------------------------------------------
        // Ordering comparison predicates
        // ------------------------------------------------------------------------
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lessThan(Row«typeSuffix(degree)» row) {
            return compare(Comparator.LESS, row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lessThan(Record«recTypeSuffix(degree)» record) {
            return compare(Comparator.LESS, record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lessThan(«TN_tn(degree)») {
            return compare(Comparator.LESS, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lessThan(«Field_TN_tn(degree)») {
            return compare(Comparator.LESS, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lt(Row«typeSuffix(degree)» row) {
            return lessThan(row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lt(Record«recTypeSuffix(degree)» record) {
            return lessThan(record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lt(«TN_tn(degree)») {
            return lessThan(«tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lt(«Field_TN_tn(degree)») {
            return lessThan(«tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lessOrEqual(Row«typeSuffix(degree)» row) {
            return compare(Comparator.LESS_OR_EQUAL, row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lessOrEqual(Record«recTypeSuffix(degree)» record) {
            return compare(Comparator.LESS_OR_EQUAL, record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lessOrEqual(«TN_tn(degree)») {
            return compare(Comparator.LESS_OR_EQUAL, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition lessOrEqual(«Field_TN_tn(degree)») {
            return compare(Comparator.LESS_OR_EQUAL, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition le(Row«typeSuffix(degree)» row) {
            return lessOrEqual(row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition le(Record«recTypeSuffix(degree)» record) {
            return lessOrEqual(record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition le(«TN_tn(degree)») {
            return lessOrEqual(«tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition le(«Field_TN_tn(degree)») {
            return lessOrEqual(«tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition greaterThan(Row«typeSuffix(degree)» row) {
            return compare(Comparator.GREATER, row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition greaterThan(Record«recTypeSuffix(degree)» record) {
            return compare(Comparator.GREATER, record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition greaterThan(«TN_tn(degree)») {
            return compare(Comparator.GREATER, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition greaterThan(«Field_TN_tn(degree)») {
            return compare(Comparator.GREATER, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition gt(Row«typeSuffix(degree)» row) {
            return greaterThan(row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition gt(Record«recTypeSuffix(degree)» record) {
            return greaterThan(record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition gt(«TN_tn(degree)») {
            return greaterThan(«tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition gt(«Field_TN_tn(degree)») {
            return greaterThan(«tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition greaterOrEqual(Row«typeSuffix(degree)» row) {
            return compare(Comparator.GREATER_OR_EQUAL, row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition greaterOrEqual(Record«recTypeSuffix(degree)» record) {
            return compare(Comparator.GREATER_OR_EQUAL, record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition greaterOrEqual(«TN_tn(degree)») {
            return compare(Comparator.GREATER_OR_EQUAL, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition greaterOrEqual(«Field_TN_tn(degree)») {
            return compare(Comparator.GREATER_OR_EQUAL, «tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition ge(Row«typeSuffix(degree)» row) {
            return greaterOrEqual(row);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition ge(Record«recTypeSuffix(degree)» record) {
            return greaterOrEqual(record);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition ge(«TN_tn(degree)») {
            return greaterOrEqual(«tn(degree)»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition ge(«Field_TN_tn(degree)») {
            return greaterOrEqual(«tn(degree)»);
        }
        «ENDFOR»

        // ------------------------------------------------------------------------
        // [NOT] BETWEEN predicates
        // ------------------------------------------------------------------------
        «FOR keyword : newArrayList("between", "betweenSymmetric", "notBetween", "notBetweenSymmetric")»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final BetweenAndStep«typeSuffix(degree)» «keyword»(«TN_tn(degree)») {
            return «keyword»(row(«tn(degree)»));
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final BetweenAndStep«typeSuffix(degree)» «keyword»(«Field_TN_tn(degree)») {
            return «keyword»(row(«tn(degree)»));
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final BetweenAndStep«typeSuffix(degree)» «keyword»(Row«typeSuffix(degree)» row) {
            return new RowBetweenCondition(this, row, «keyword.startsWith("not")», «keyword.endsWith("Symmetric")»);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final BetweenAndStep«typeSuffix(degree)» «keyword»(Record«recTypeSuffix(degree)» record) {
            return «keyword»(record.valuesRow());
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition «keyword»(Row«typeSuffix(degree)» minValue, Row«typeSuffix(degree)» maxValue) {
            return «keyword»(minValue).and(maxValue);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition «keyword»(Record«recTypeSuffix(degree)» minValue, Record«recTypeSuffix(degree)» maxValue) {
            return «keyword»(minValue).and(maxValue);
        }
        «ENDFOR»
        «ENDFOR»

        // ------------------------------------------------------------------------
        // [NOT] DISTINCT predicates
        // ------------------------------------------------------------------------

        // ------------------------------------------------------------------------
        // [NOT] IN predicates
        // ------------------------------------------------------------------------
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition in(Row«typeSuffix(degree)»... rows) {
            return in(Arrays.asList(rows));
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition in(Record«recTypeSuffix(degree)»... records) {
            Row«typeSuffix(degree)»[] rows = new Row«typeSuffixRaw(degree)»[records.length];

            for (int i = 0; i < records.length; i++) {
                rows[i] = «IF degree == 0»(RowN) «ENDIF»records[i].valuesRow();
            }

            return in(rows);
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition notIn(Row«typeSuffix(degree)»... rows) {
            return notIn(Arrays.asList(rows));
        }
        «ENDFOR»
        «FOR degree : (0..Constants::MAX_ROW_DEGREE)»

        @Override
        public final Condition notIn(Record«recTypeSuffix(degree)»... records) {
            Row«typeSuffix(degree)»[] rows = new Row«typeSuffixRaw(degree)»[records.length];

            for (int i = 0; i < records.length; i++) {
                rows[i] = «IF degree == 0»(RowN) «ENDIF»records[i].valuesRow();
            }

            return notIn(rows);
        }
        «ENDFOR»

        @Override
        public final Condition in(Collection rows) {
            QueryPartList<Row> list = new QueryPartList<Row>(rows);
            return new RowInCondition(this, list, Comparator.IN);
        }

        @Override
        public final Condition in(Result result) {
            QueryPartList<Row> list = new QueryPartList<Row>(Utils.rows(result));
            return new RowInCondition(this, list, Comparator.IN);
        }

        @Override
        public final Condition notIn(Collection rows) {
            QueryPartList<Row> list = new QueryPartList<Row>(rows);
            return new RowInCondition(this, list, Comparator.NOT_IN);
        }

        @Override
        public final Condition notIn(Result result) {
            QueryPartList<Row> list = new QueryPartList<Row>(Utils.rows(result));
            return new RowInCondition(this, list, Comparator.NOT_IN);
        }

        // ------------------------------------------------------------------------
        // Predicates involving subqueries
        // ------------------------------------------------------------------------

        @Override
        public final Condition equal(Select select) {
            return compare(Comparator.EQUALS, select);
        }

        @Override
        public final Condition equal(QuantifiedSelect select) {
            return compare(Comparator.EQUALS, select);
        }

        @Override
        public final Condition eq(Select select) {
            return equal(select);
        }

        @Override
        public final Condition eq(QuantifiedSelect select) {
            return equal(select);
        }

        @Override
        public final Condition notEqual(Select select) {
            return compare(Comparator.NOT_EQUALS, select);
        }

        @Override
        public final Condition notEqual(QuantifiedSelect select) {
            return compare(Comparator.NOT_EQUALS, select);
        }

        @Override
        public final Condition ne(Select select) {
            return notEqual(select);
        }

        @Override
        public final Condition ne(QuantifiedSelect select) {
            return notEqual(select);
        }

        @Override
        public final Condition greaterThan(Select select) {
            return compare(Comparator.GREATER, select);
        }

        @Override
        public final Condition greaterThan(QuantifiedSelect select) {
            return compare(Comparator.GREATER, select);
        }

        @Override
        public final Condition gt(Select select) {
            return greaterThan(select);
        }

        @Override
        public final Condition gt(QuantifiedSelect select) {
            return greaterThan(select);
        }

        @Override
        public final Condition greaterOrEqual(Select select) {
            return compare(Comparator.GREATER_OR_EQUAL, select);
        }

        @Override
        public final Condition greaterOrEqual(QuantifiedSelect select) {
            return compare(Comparator.GREATER_OR_EQUAL, select);
        }

        @Override
        public final Condition ge(Select select) {
            return greaterOrEqual(select);
        }

        @Override
        public final Condition ge(QuantifiedSelect select) {
            return greaterOrEqual(select);
        }

        @Override
        public final Condition lessThan(Select select) {
            return compare(Comparator.LESS, select);
        }

        @Override
        public final Condition lessThan(QuantifiedSelect select) {
            return compare(Comparator.LESS, select);
        }

        @Override
        public final Condition lt(Select select) {
            return lessThan(select);
        }

        @Override
        public final Condition lt(QuantifiedSelect select) {
            return lessThan(select);
        }

        @Override
        public final Condition lessOrEqual(Select select) {
            return compare(Comparator.LESS_OR_EQUAL, select);
        }

        @Override
        public final Condition lessOrEqual(QuantifiedSelect select) {
            return compare(Comparator.LESS_OR_EQUAL, select);
        }

        @Override
        public final Condition le(Select select) {
            return lessOrEqual(select);
        }

        @Override
        public final Condition le(QuantifiedSelect select) {
            return lessOrEqual(select);
        }

        @Override
        public final Condition in(Select select) {
            return compare(Comparator.IN, select);
        }

        @Override
        public final Condition notIn(Select select) {
            return compare(Comparator.NOT_IN, select);
        }

        // ------------------------------------------------------------------------
        // XXX: Row2 API
        // ------------------------------------------------------------------------

        @Override
        public final Condition overlaps(T1 t1, T2 t2) {
            return overlaps(row(t1, t2));
        }

        @Override
        public final Condition overlaps(Field<T1> t1, Field<T2> t2) {
            return overlaps(row(t1, t2));
        }

        @Override
        public final Condition overlaps(Row2<T1, T2> row) {
            return new RowOverlapsCondition(this, row);
        }
    }
        ''');

        write("org.jooq.impl.RowImpl", out);
    }
}
