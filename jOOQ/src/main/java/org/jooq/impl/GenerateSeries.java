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

import static org.jooq.SQLDialect.*;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.inline;
// ...
import static org.jooq.impl.DSL.one;
// ...
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.unnest;
import static org.jooq.impl.DSL.unquotedName;
import static org.jooq.impl.DSL.withRecursive;
import static org.jooq.impl.Internal.iadd;
import static org.jooq.impl.Internal.idiv;
import static org.jooq.impl.Internal.imul;
import static org.jooq.impl.Internal.isub;
import static org.jooq.impl.Keywords.K_TABLE;
import static org.jooq.impl.Names.N_GENERATE_ARRAY;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.Names.N_SYSTEM_RANGE;
import static org.jooq.impl.Names.N_UNNEST;
import static org.jooq.impl.SQLDataType.INTEGER;
import static org.jooq.impl.Tools.visitSubquery;

import java.util.Set;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
// ...
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableOptions;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class GenerateSeries extends AbstractTable<Record1<Integer>> implements AutoAliasTable<Record1<Integer>> {
    private static final Set<SQLDialect> EMULATE_WITH_RECURSIVE = SQLDialect.supportedBy(FIREBIRD, HSQLDB, MARIADB, MYSQL, SQLITE);
    private static final Set<SQLDialect> EMULATE_SYSTEM_RANGE   = SQLDialect.supportedBy(H2);











    private final Field<Integer>         from;
    private final Field<Integer>         to;
    private final Field<Integer>         step;
    private final Name                   name;

    GenerateSeries(Field<Integer> from, Field<Integer> to) {
        this(from, to, null);
    }

    GenerateSeries(Field<Integer> from, Field<Integer> to, Field<Integer> step) {
        this(from, to, step, N_GENERATE_SERIES);
    }

    GenerateSeries(Field<Integer> from, Field<Integer> to, Field<Integer> step, Name name) {
        super(TableOptions.expression(), name);

        this.from = from;
        this.to = to;
        this.step = step;
        this.name = name;
    }

    @Override
    public Table<Record1<Integer>> as(Name alias) {
        return new TableAlias<>(new GenerateSeries(from, to, step, alias), alias);
    }

    @Override
    public Table<Record1<Integer>> as(Name alias, Name... fieldAliases) {
        return new TableAlias<>(new GenerateSeries(from, to, step, alias), alias, fieldAliases);
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (EMULATE_WITH_RECURSIVE.contains(ctx.dialect())) {
            Field<Integer> f = DSL.field(name, INTEGER);
            visitSubquery(
                ctx,
                withRecursive(name, name)
                    .as(select(from).unionAll(select(iadd(f, step == null ? inline(1) : step)).from(name).where(f.lt(to))))
                    .select(f).from(name)
            );
        }
        else if (EMULATE_SYSTEM_RANGE.contains(ctx.dialect())) {
            if (step == null) {
                ctx.visit(N_SYSTEM_RANGE).sql('(').visit(from).sql(", ").visit(to).sql(')');
            }

            // Work around https://github.com/h2database/h2database/issues/3046
            else {
                ctx.visit(N_SYSTEM_RANGE).sql('(').visit(from).sql(", ").visit(to).sql(", ");
                ctx.paramType(INLINED, c -> c.visit(step));
                ctx.sql(')');
            }
        }





































        else {
            if (step == null)
                ctx.visit(N_GENERATE_SERIES).sql('(').visit(from).sql(", ").visit(to).sql(')');
            else
                ctx.visit(N_GENERATE_SERIES).sql('(').visit(from).sql(", ").visit(to).sql(", ").visit(step).sql(')');
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Class<? extends Record1<Integer>> getRecordType() {
        return (Class) RecordImpl1.class;
    }

    @Override
    final FieldsImpl<Record1<Integer>> fields0() {
        return new FieldsImpl<>(DSL.field(DSL.name(name, N_GENERATE_SERIES), Integer.class));
    }

    @Override // Avoid AbstractTable implementation
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final Table<Record1<Integer>> autoAlias(Context<?> ctx) {
        if (EMULATE_WITH_RECURSIVE.contains(ctx.dialect()))
            return as(name);
        else if (EMULATE_SYSTEM_RANGE.contains(ctx.dialect()))
            return as(name, name);








        else
            return null;
    }
}
