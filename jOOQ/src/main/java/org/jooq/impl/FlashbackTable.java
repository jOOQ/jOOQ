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

import static org.jooq.impl.DSL.val;
import static org.jooq.impl.Keywords.K_AND;
import static org.jooq.impl.Keywords.K_AS_OF;
import static org.jooq.impl.Keywords.K_MAXVALUE;
import static org.jooq.impl.Keywords.K_MINVALUE;
import static org.jooq.impl.Keywords.K_SCN;
import static org.jooq.impl.Keywords.K_TIMESTAMP;
import static org.jooq.impl.Keywords.K_VERSIONS_BETWEEN;
import static org.jooq.impl.Names.N_FLASHBACK;

import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Keyword;
import org.jooq.Name;
// ...
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.Table;
// ...

/**
 * A flashback query clause implementation.
 *
 * @author Lukas Eder
 */
@Pro
final class FlashbackTable<R extends Record, T>
extends AbstractTable<R>
implements VersionsBetweenAndStep<R, T> {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = -7918219502110473521L;

    private final Table<R>      table;
    private final FlashbackType type;
    private final Field<?>      asOf;
    private final QueryPart     minvalue;
    private QueryPart           maxvalue;

    FlashbackTable(Table<R> table, Field<?> asOf, QueryPart minvalue, FlashbackType type) {
        super(table.getOptions(), N_FLASHBACK);

        this.table = table;
        this.asOf = asOf;
        this.minvalue = minvalue != null ? minvalue : K_MINVALUE;
        this.type = type;
    }

    private FlashbackTable(Table<R> table, FlashbackTable<R, T> o) {
        this(table, o.asOf, o.minvalue, o.type);

        this.maxvalue = o.maxvalue;
    }

    // ------------------------------------------------------------------------
    // XXX: Flashback API
    // ------------------------------------------------------------------------

    @Override
    public final Table<R> and(T value) {
        return and(val(value));
    }

    @Override
    public final Table<R> and(Field<? extends T> field) {
        maxvalue = field;
        return this;
    }

    @Override
    public final Table<R> andMaxvalue() {
        maxvalue = K_MAXVALUE;
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: Table API
    // ------------------------------------------------------------------------

    @Override
    public final Class<? extends R> getRecordType() {
        return table.getRecordType();
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#6303] jOOQ allows for the syntactically incorrect construction of aliased flashback table expressions
        Table<R> aliased = Tools.aliased(table);
        if (aliased != null) {
            Alias<?> alias = Tools.alias(table);

            ctx.visit(new Alias<>(
                new FlashbackTable<>(aliased, this),
                this, alias.alias, alias.fieldAliases, alias.wrapInParentheses
            ));
        }
        else {
            ctx.visit(table);

            if (asOf != null) {
                ctx.sql(' ')
                   .visit(K_AS_OF)
                   .sql(' ')
                   .visit(type.keyword())
                   .sql(' ')
                   .visit(asOf);
            }
            else {
                ctx.sql(' ')
                   .visit(K_VERSIONS_BETWEEN)
                   .sql(' ')
                   .visit(type.keyword())
                   .sql(' ')
                   .visit(minvalue)
                   .sql(' ')
                   .visit(K_AND)
                   .sql(' ')
                   .visit(maxvalue);
            }
        }
    }

    @Override
    public final boolean declaresTables() {
        return true;
    }

    @Override
    public final Table<R> as(Name alias) {
        return new TableAlias<>(this, alias, true);
    }

    @Override
    public final Table<R> as(Name alias, Name... fieldAliases) {
        return new TableAlias<>(this, alias, fieldAliases, true);
    }

    @Override
    final Fields<R> fields0() {
        return new Fields<>(table.fields());
    }

    /**
     * The flashback query clause type
     */
    @Pro
    enum FlashbackType {
        SCN, TIMESTAMP;

        Keyword keyword() {
            if (this == SCN)
                return K_SCN;
            else if (this == TIMESTAMP)
                return K_TIMESTAMP;
            else
                throw new IllegalStateException("Unsupported FlashbackType: " + this);
        }
    }
}
/* [/pro] */