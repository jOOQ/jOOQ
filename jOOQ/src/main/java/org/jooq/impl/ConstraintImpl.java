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

/**
 * @author Lukas Eder
 */
@SuppressWarnings("rawtypes")
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
    public final ForeignKeyConstraintImpl foreignKey(Field field1) {
        return foreignKey(new Field[] { field1 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2) {
        return foreignKey(new Field[] { field1, field2 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3) {
        return foreignKey(new Field[] { field1, field2, field3 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4) {
        return foreignKey(new Field[] { field1, field2, field3, field4 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21) {
        return foreignKey(new Field[] { field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14, field15, field16, field17, field18, field19, field20, field21 });
    }

    @Override
    public final ForeignKeyConstraintImpl foreignKey(Field field1, Field field2, Field field3, Field field4, Field field5, Field field6, Field field7, Field field8, Field field9, Field field10, Field field11, Field field12, Field field13, Field field14, Field field15, Field field16, Field field17, Field field18, Field field19, Field field20, Field field21, Field field22) {
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
