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

// ...
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.one;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.Names.N_GENERATE_SERIES;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.Record1;
import org.jooq.TableOptions;

/**
 * @author Lukas Eder
 */
final class GenerateSeries extends AbstractTable<Record1<Integer>> {

    /**
     * Generated UID
     */
    private static final long    serialVersionUID = 2385574114457239818L;

    private final Field<Integer> from;
    private final Field<Integer> to;
    private final Field<Integer> step;

    GenerateSeries(Field<Integer> from, Field<Integer> to) {
        this(from, to, null);
    }

    GenerateSeries(Field<Integer> from, Field<Integer> to, Field<Integer> step) {
        super(TableOptions.expression(), N_GENERATE_SERIES);

        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(delegate(ctx.configuration()));
    }

    @Override // Avoid AbstractTable implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    private final QueryPart delegate(Configuration configuration) {
        switch (configuration.family()) {























            case POSTGRES:
            default:
                if (step == null)
                    return table("{generate_series}({0}, {1})", from, to);
                else
                    return table("{generate_series}({0}, {1}, {2})", from, to, step);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Class<? extends Record1<Integer>> getRecordType() {
        return (Class) RecordImpl1.class;
    }

    @Override
    final Fields<Record1<Integer>> fields0() {
        return new Fields<>(DSL.field(name("generate_series"), Integer.class));
    }
}
