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
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...

import javax.annotation.Generated;

/**
 * The step in the {@link Constraint} construction DSL API that allows for
 * specifying the constraint type.
 * <p>
 * <h3>Referencing <code>XYZ*Step</code> types directly from client code</h3>
 * <p>
 * It is usually not recommended to reference any <code>XYZ*Step</code> types
 * directly from client code, or assign them to local variables. When writing
 * dynamic SQL, creating a statement's components dynamically, and passing them
 * to the DSL API statically is usually a better choice. See the manual's
 * section about dynamic SQL for details: <a href=
 * "https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql">https://www.jooq.org/doc/latest/manual/sql-building/dynamic-sql</a>.
 * <p>
 * Drawbacks of referencing the <code>XYZ*Step</code> types directly:
 * <ul>
 * <li>They're operating on mutable implementations (as of jOOQ 3.x)</li>
 * <li>They're less composable and not easy to get right when dynamic SQL gets
 * complex</li>
 * <li>They're less readable</li>
 * <li>They might have binary incompatible changes between minor releases</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface ConstraintTypeStep extends ConstraintFinalStep {

    /**
     * Create a <code>PRIMARY KEY</code> constraint.
     */
    @Support
    ConstraintFinalStep primaryKey(String... fields);

    /**
     * Create a <code>PRIMARY KEY</code> constraint.
     */
    @Support
    ConstraintFinalStep primaryKey(Name... fields);

    /**
     * Create a <code>PRIMARY KEY</code> constraint.
     */
    @Support
    ConstraintFinalStep primaryKey(Field<?>... fields);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    ConstraintForeignKeyReferencesStepN foreignKey(String... fields);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    ConstraintForeignKeyReferencesStepN foreignKey(Name... fields);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    ConstraintForeignKeyReferencesStepN foreignKey(Field<?>... fields);

 // [jooq-tools] START [foreignKey]

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1> ConstraintForeignKeyReferencesStep1<T1> foreignKey(Field<T1> field1);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2> ConstraintForeignKeyReferencesStep2<T1, T2> foreignKey(Field<T1> field1, Field<T2> field2);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3> ConstraintForeignKeyReferencesStep3<T1, T2, T3> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4> ConstraintForeignKeyReferencesStep4<T1, T2, T3, T4> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5> ConstraintForeignKeyReferencesStep5<T1, T2, T3, T4, T5> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6> ConstraintForeignKeyReferencesStep6<T1, T2, T3, T4, T5, T6> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7> ConstraintForeignKeyReferencesStep7<T1, T2, T3, T4, T5, T6, T7> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8> ConstraintForeignKeyReferencesStep8<T1, T2, T3, T4, T5, T6, T7, T8> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9> ConstraintForeignKeyReferencesStep9<T1, T2, T3, T4, T5, T6, T7, T8, T9> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> ConstraintForeignKeyReferencesStep10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> ConstraintForeignKeyReferencesStep11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> ConstraintForeignKeyReferencesStep12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> ConstraintForeignKeyReferencesStep13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> ConstraintForeignKeyReferencesStep14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ConstraintForeignKeyReferencesStep15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ConstraintForeignKeyReferencesStep16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ConstraintForeignKeyReferencesStep17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ConstraintForeignKeyReferencesStep18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ConstraintForeignKeyReferencesStep19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ConstraintForeignKeyReferencesStep20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ConstraintForeignKeyReferencesStep21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ConstraintForeignKeyReferencesStep22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep1<?> foreignKey(Name field1);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep2<?, ?> foreignKey(Name field1, Name field2);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep3<?, ?, ?> foreignKey(Name field1, Name field2, Name field3);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep4<?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep5<?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep6<?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep7<?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep8<?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep9<?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep14<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep15<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep16<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep17<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep18<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep19<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep20<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep21<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20, Name field21);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep22<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20, Name field21, Name field22);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep1<?> foreignKey(String field1);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep2<?, ?> foreignKey(String field1, String field2);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep3<?, ?, ?> foreignKey(String field1, String field2, String field3);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep4<?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep5<?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep6<?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep7<?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep8<?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep9<?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep14<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep15<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep16<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep17<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep18<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep19<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep20<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep21<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21);

    /**
     * Add a <code>FOREIGN KEY</code> clause to the <code>CONSTRAINT</code>.
     */
    @Generated("This method was generated using jOOQ-tools")
    @Support
    ConstraintForeignKeyReferencesStep22<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21, String field22);

// [jooq-tools] END [foreignKey]

    /**
     * Create a <code>UNIQUE</code> constraint.
     */
    @Support
    ConstraintFinalStep unique(String... fields);

    /**
     * Create a <code>UNIQUE</code> constraint.
     */
    @Support
    ConstraintFinalStep unique(Name... fields);

    /**
     * Create a <code>UNIQUE</code> constraint.
     */
    @Support
    ConstraintFinalStep unique(Field<?>... fields);

    /**
     * Create a <code>CHECK</code> constraint.
     */
    @Support({ CUBRID, DERBY, FIREBIRD, H2, HSQLDB, POSTGRES, SQLITE })
    ConstraintFinalStep check(Condition condition);
}
