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
package org.jooq.explorer.components.field;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.selectFrom;

import java.util.Map;
import java.util.Objects;

import javafx.scene.control.ComboBox;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Result;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

/**
 * @author Lukas Eder
 */
@SuppressWarnings("restriction")
public class CForeignKeyField<T> extends CField<T> {

    private ComboBox<String> input;
    private Map<String, T>   values1;
    private Map<T, String>   values2;

    public CForeignKeyField(DSLContext ctx, Field<T> field, ForeignKey<?, ?> key) {
        super(field);

        if (key.getKey().getTable().getName().equals("REGIONS")) {
            Result<?> result = ctx.fetch(selectFrom(key.getKey().getTable()));

            values1 = (Map) result.intoMap(field("REGION_NAME"), field("REGION_ID"));
            values2 = (Map) result.intoMap(field("REGION_ID"), field("REGION_NAME"));
        }
        else {
            T[] options = (T[]) ctx.fetchValues(key.getKey().getFieldsArray()[0]).toArray();

            values1 = Seq.of(options).distinct().map(v -> Tuple.tuple(Objects.toString(v), v)).toMap(Tuple2::v1, Tuple2::v2);
            values2 = Seq.of(options).distinct().map(v -> Tuple.tuple(Objects.toString(v), v)).toMap(Tuple2::v2, Tuple2::v1);

        }

        input = new ComboBox<>();
        input.getItems().addAll(values2.values());
        getChildren().addAll(input);
    }

    @Override
    public final T getValue() {
        return values1.get(input.getValue());
    }

    @Override
    public final void setValue(T value) {
        input.setValue(values2.get(value));
    }
}
