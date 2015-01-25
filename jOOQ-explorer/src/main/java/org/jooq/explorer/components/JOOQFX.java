/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.explorer.components;

import java.sql.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.explorer.components.field.CDateField;
import org.jooq.explorer.components.field.CField;
import org.jooq.explorer.components.field.CFieldLabel;
import org.jooq.explorer.components.field.CForeignKeyField;
import org.jooq.explorer.components.field.COtherField;
import org.jooq.explorer.components.field.CVarcharField;

/**
 * @author Lukas Eder
 */
public class JOOQFX {

    public static <T> CField<T> field(DSLContext ctx, Field<T> field) {
        return field(ctx, field, r -> {});
    }

    @SuppressWarnings("unchecked")
    public static <T> CField<T> field(DSLContext ctx, Field<T> field, Consumer<? super CField<T>> consumer) {
        return JOOQFX.create(
            () -> {
                Class<T> type = field.getType();
                CField<?> result = null;

                if (field instanceof TableField) {
                    Table<?> table = ((TableField) field).getTable();

                    fkLoop:
                    for (ForeignKey key : table.getReferences()) {
                        TableField[] fields = key.getFieldsArray();

                        for (int i = 0; i < fields.length; i++) {
                            if (fields[i].equals(field)) {
                                result = new CForeignKeyField(ctx, field, key);

                                break fkLoop;
                            }
                        }
                    }
                }

                if (result == null) {
                    if (type == String.class) {
                        result = new CVarcharField((Field<String>) field);
                    }
                    else if (type == Date.class) {
                        result = new CDateField((Field<Date>) field);
                    }
                    else {
                        result = new COtherField((Field<Object>) field);
                    }
                }

                return (CField<T>) result;
            },
            consumer
        );
    }

    public static CFieldLabel label(DSLContext ctx, Field<?> field) {
        return label(ctx, field, r -> {});
    }

    public static CFieldLabel label(DSLContext ctx, Field<?> field, Consumer<? super CFieldLabel> consumer) {
        return create(() -> new CFieldLabel(field), consumer);
    }

    private static <R> R create(Supplier<R> creator, Consumer<? super R> finisher) {
        R result = creator.get();
        finisher.accept(result);
        return result;
    }
}
