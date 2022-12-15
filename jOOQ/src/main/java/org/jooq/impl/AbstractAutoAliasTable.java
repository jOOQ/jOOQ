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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import org.jooq.Context;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableOptions;

/**
 * A base implementation for {@link AutoAlias} and {@link Table}.
 *
 * @author Lukas Eder
 */
abstract class AbstractAutoAliasTable<R extends Record>
extends
    AbstractTable<R>
implements
    AutoAlias<Table<R>>
{

    final Name   alias;
    final Name[] fieldAliases;

    AbstractAutoAliasTable(Name alias) {
        this(alias, null);
    }

    AbstractAutoAliasTable(Name alias, Name[] fieldAliases) {
        super(TableOptions.expression(), alias != null ? alias : DSL.name("t"));

        this.alias = alias;
        this.fieldAliases = fieldAliases;
    }

    abstract AbstractAutoAliasTable<R> construct(Name newAlias, Name[] newFieldAliases);

    // -------------------------------------------------------------------------
    // XXX: Table API
    // -------------------------------------------------------------------------

    @Override
    public final boolean declaresTables() {

        // Always true, because unnested tables are always aliased
        return true;
    }

    @Override
    public final Table<R> autoAlias(Context<?> ctx, Table<R> t) {

        // TODO [#5799] Possibly, add dialect specific behaviour?
        return t.as(alias, fieldAliases);
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final Table<R> as(Name as) {
        return new TableAlias<>(construct(as, null), as, fieldAliases);
    }

    @Override
    public final Table<R> as(Name as, Name... fields) {
        return new TableAlias<>(construct(as, fields), as, fields);
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Table<R> $aliased() {
        return construct(alias, null);
    }

    @Override
    public final Name $alias() {
        return alias;
    }
}
