/*
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
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq;

// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.FIREBIRD_3_0;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
import static org.jooq.SQLDialect.MYSQL_8_0;
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
// ...
// ...

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.Generated;

import org.jooq.conf.Settings;

/**
 * A SQL identifier <code>QueryPart</code>.
 * <p>
 * A <code>Name</code> is a {@link QueryPart} that renders a SQL identifier
 * according to the settings specified in {@link Settings#getRenderNameStyle()}.
 *
 * @author Lukas Eder
 */
public interface Name extends QueryPart {

    /**
     * Get the first segment of the qualified name (usually a {@link Catalog} or {@link Schema} name).
     */
    String first();

    /**
     * Get the last segment of the qualified name (usually a {@link Table}, {@link Field}, or {@link Parameter} name).
     */
    String last();

    /**
     * Whether this is a qualified name.
     * <p>
     * This is <code>true</code> as soon as {@link #getName()} has a length of more than <code>1</code>.
     */
    boolean qualified();

    /**
     * This name's qualifier (if it is {@link #qualified()}), or <code>null</code>.
     */
    Name qualifier();

    /**
     * This name, unqualified.
     */
    Name unqualifiedName();

    /**
     * This name, quoted.
     */
    Name quotedName();

    /**
     * This name, unquoted.
     */
    Name unquotedName();

    /**
     * Get the individual, unqualified name parts of this name.
     */
    Name[] parts();

    /**
     * Appends <code>name</code> to this name.
     */
    Name append(String name);

    /**
     * Appends <code>name</code> (all of its {@link #parts()}) to this name.
     */
    Name append(Name name);

    /**
     * The qualified name of this SQL identifier.
     */
    String[] getName();

    /**
     * Create an empty {@link WindowDefinition} from this name.
     *
     * @see #as(WindowSpecification)
     */
    @Support({ CUBRID, FIREBIRD_3_0, MARIADB, MYSQL_8_0, POSTGRES })
    WindowDefinition as();

    /**
     * Create a {@link WindowDefinition} from this name.
     * <p>
     * This creates a window definition that can be
     * <ul>
     * <li>declared in the <code>WINDOW</code> clause (see
     * {@link SelectWindowStep#window(WindowDefinition...)}</li>
     * <li>referenced from the <code>OVER</code> clause (see
     * {@link AggregateFunction#over(WindowDefinition)}</li>
     * </ul>
     */
    @Support({ CUBRID, FIREBIRD_3_0, MARIADB, MYSQL_8_0, POSTGRES })
    WindowDefinition as(WindowSpecification window);

    /**
     * Specify a subselect to refer to by the <code>Name</code> to form a common
     * table expression.
     * <p>
     * Column names are implicitly inherited from the <code>SELECT</code>
     * statement.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    <R extends Record> CommonTableExpression<R> as(Select<R> select);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList fields(String... fieldNames);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList fields(Name... fieldNames);


    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     * <p>
     * This works in a similar way as {@link #fields(String...)}, except
     * that all column names are produced by a function that receives the CTE's
     * {@link Select} columns and their column indexes as input.
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    DerivedColumnList fields(Function<? super Field<?>, ? extends String> fieldNameFunction);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     * <p>
     * This works in a similar way as {@link #fields(String...)}, except
     * that all column names are produced by a function that receives the CTE's
     * {@link Select} columns as input.
     */
    @Support({ FIREBIRD, H2, HSQLDB, POSTGRES })
    DerivedColumnList fields(BiFunction<? super Field<?>, ? super Integer, ? extends String> fieldNameFunction);


    // [jooq-tools] START [fields]

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList1 fields(String fieldName1);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList2 fields(String fieldName1, String fieldName2);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList3 fields(String fieldName1, String fieldName2, String fieldName3);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList4 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList5 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList6 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList7 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList8 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList9 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList10 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList11 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList12 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList13 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList14 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList15 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList16 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList17 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList18 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList19 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList20 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19, String fieldName20);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList21 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19, String fieldName20, String fieldName21);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList22 fields(String fieldName1, String fieldName2, String fieldName3, String fieldName4, String fieldName5, String fieldName6, String fieldName7, String fieldName8, String fieldName9, String fieldName10, String fieldName11, String fieldName12, String fieldName13, String fieldName14, String fieldName15, String fieldName16, String fieldName17, String fieldName18, String fieldName19, String fieldName20, String fieldName21, String fieldName22);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList1 fields(Name fieldName1);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList2 fields(Name fieldName1, Name fieldName2);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList3 fields(Name fieldName1, Name fieldName2, Name fieldName3);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList4 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList5 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList6 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList7 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList8 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList9 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList10 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList11 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList12 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList13 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList14 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList15 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList16 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList17 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList18 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList19 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList20 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19, Name fieldName20);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList21 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19, Name fieldName20, Name fieldName21);

    /**
     * Add a list of fields to this name to make this name a
     * {@link DerivedColumnList}.
     * <p>
     * The <code>DerivedColumnList</code> can then be used along with a
     * subselect to form a {@link CommonTableExpression} to be used with
     * <code>WITH</code> clauses.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support({ FIREBIRD, H2, HSQLDB, MARIADB, MYSQL_8_0, POSTGRES })
    DerivedColumnList22 fields(Name fieldName1, Name fieldName2, Name fieldName3, Name fieldName4, Name fieldName5, Name fieldName6, Name fieldName7, Name fieldName8, Name fieldName9, Name fieldName10, Name fieldName11, Name fieldName12, Name fieldName13, Name fieldName14, Name fieldName15, Name fieldName16, Name fieldName17, Name fieldName18, Name fieldName19, Name fieldName20, Name fieldName21, Name fieldName22);

// [jooq-tools] END [fields]

    /**
     * {@inheritDoc}
     */
    @Override
    boolean equals(Object other);

    /**
     * Compare this name with another one ignoring case.
     */
    boolean equalsIgnoreCase(Name other);
}
