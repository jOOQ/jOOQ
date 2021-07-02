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

package org.jooq.impl;

import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.select;

import java.sql.ResultSetMetaData;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Select;

/**
 * @author Lukas Eder
 */
final class FetchCount extends AbstractResultQuery<Record1<Integer>> {

    private final Field<?>[]  count            = { count().as("c") };
    private final Select<?>   query;

    FetchCount(Configuration configuration, Select<?> query) {
        super(configuration);

        this.query = query;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(select(count).from(new AliasedSelect<>(query, true, true).as("t")));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Class<? extends Record1<Integer>> getRecordType0() {
        return (Class) RecordImpl1.class;
    }

    @Override
    public final Field<?>[] getFields() {
        return count;
    }
}
