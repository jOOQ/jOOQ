/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.impl;

import static org.jooq.impl.Tools.EMPTY_FIELD;
import static org.jooq.impl.Tools.fieldsByName;

import java.util.Collection;

import org.jooq.Condition;
import org.jooq.ConstraintTypeStep;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.impl.QOM.UEmpty;
import org.jooq.impl.QOM.UTransient;

/**
 * @author Lukas Eder
 */
final class ConstraintImpl
extends
    AbstractNamed
implements
    UEmpty,
    ConstraintTypeStep
{

    ConstraintImpl() {
        this(null);
    }

    ConstraintImpl(Name name) {
        super(name, null);
    }

    // ------------------------------------------------------------------------
    // XXX: Constraint API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(getQualifiedName());
    }

    @Override
    public final UniqueConstraintImpl unique(String... fields) {
        return unique(fieldsByName(fields));
    }

    @Override
    public final UniqueConstraintImpl unique(Name... fields) {
        return unique(fieldsByName(fields));
    }

    @Override
    public final UniqueConstraintImpl unique(Field<?>... fields) {
        return new UniqueConstraintImpl($name(), fields);
    }

    @Override
    public final UniqueConstraintImpl unique(Collection<? extends Field<?>> fields) {
        return unique(fields.toArray(EMPTY_FIELD));
    }

    @Override
    public final CheckConstraintImpl check(Condition condition) {
        return new CheckConstraintImpl($name(), condition);
    }

    @Override
    public final PrimaryKeyConstraintImpl primaryKey(String... fields) {
        return primaryKey(fieldsByName(fields));
    }

    @Override
    public final PrimaryKeyConstraintImpl primaryKey(Name... fields) {
        return primaryKey(fieldsByName(fields));
    }

    @Override
    public final PrimaryKeyConstraintImpl primaryKey(Field<?>... fields) {
        return new PrimaryKeyConstraintImpl($name(), fields);
    }

    @Override
    public final PrimaryKeyConstraintImpl primaryKey(Collection<? extends Field<?>> fields) {
        return primaryKey(fields.toArray(EMPTY_FIELD));
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String... fields) {
        return foreignKey(fieldsByName(fields));
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name... fields) {
        return foreignKey(fieldsByName(fields));
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field<?>... fields) {
        return new ForeignKeyConstraintImpl($name(), fields);
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Collection<? extends Field<?>> fields) {
        return foreignKey(fields.toArray(EMPTY_FIELD));
    }



    @Override
    public final <T1> ForeignKeyConstraintImpl foreignKey(Field<T1> field1) {
        return foreignKey(new Field[] { field1 });
    }

    @Override
    public final <T1, T2> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2) {
        return foreignKey(new Field[] { field1, field2 });
    }

    @Override
    public final <T1, T2, T3> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3) {
        return foreignKey(new Field[] { field1, field2, field3 });
    }

    @Override
    public final <T1, T2, T3, T4> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4) {
        return foreignKey(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    public final <T1, T2, T3, T4, T5> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForeignKeyConstraintImpl foreignKey(Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5, Field<T6> field6, Field<T7> field7, Field<T8> field8, Field<T9> field9, Field<T10> field10, Field<T11> field11, Field<T12> field12, Field<T13> field13, Field<T14> field14, Field<T15> field15, Field<T16> field16, Field<T17> field17, Field<T18> field18, Field<T19> field19, Field<T20> field20, Field<T21> field21, Field<T22> field22) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1) {
        return foreignKey(new Name[] { field1 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2) {
        return foreignKey(new Name[] { field1, field2 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3) {
        return foreignKey(new Name[] { field1, field2, field3 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4) {
        return foreignKey(new Name[] { field1, field2, field3, field4 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20, Name field21) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Name field1, Name field2, Name field3, Name field4, Name field5, Name field6, Name field7, Name field8, Name field9, Name field10, Name field11, Name field12, Name field13, Name field14, Name field15, Name field16, Name field17, Name field18, Name field19, Name field20, Name field21, Name field22) {
        return foreignKey(new Name[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1) {
        return foreignKey(new String[] { field1 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2) {
        return foreignKey(new String[] { field1, field2 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3) {
        return foreignKey(new String[] { field1, field2, field3 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4) {
        return foreignKey(new String[] { field1, field2, field3, field4 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(String field1, String field2, String field3, String field4, String field5, String field6, String field7, String field8, String field9, String field10, String field11, String field12, String field13, String field14, String field15, String field16, String field17, String field18, String field19, String field20, String field21, String field22) {
        return foreignKey(new String[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21, field22 });
    }



}
