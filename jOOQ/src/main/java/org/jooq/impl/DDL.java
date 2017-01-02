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
 */
package org.jooq.impl;

import static org.jooq.DDLFlag.FOREIGN_KEY;
import static org.jooq.DDLFlag.PRIMARY_KEY;
import static org.jooq.DDLFlag.SCHEMA;
import static org.jooq.DDLFlag.TABLE;
import static org.jooq.DDLFlag.UNIQUE;
import static org.jooq.impl.DSL.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Constraint;
import org.jooq.DDLFlag;
import org.jooq.DSLContext;
import org.jooq.ForeignKey;
import org.jooq.Queries;
import org.jooq.Query;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.UniqueKey;

/**
 * @author Lukas Eder
 */
final class DDL {

    private final DSLContext       ctx;
    private final EnumSet<DDLFlag> flags;

    DDL(DSLContext ctx, DDLFlag... flags) {
        this.ctx = ctx;
        this.flags = EnumSet.noneOf(DDLFlag.class);

        for (DDLFlag flag : flags)
            this.flags.add(flag);
    }

    final Queries queries(Table<?> table) {
        List<Constraint> constraints = new ArrayList<Constraint>();

        if (flags.contains(TABLE)) {
            if (flags.contains(PRIMARY_KEY))
                for (UniqueKey<?> key : table.getKeys())
                    if (key.isPrimary())
                        constraints.add(constraint(key.getName()).primaryKey(key.getFieldsArray()));

            if (flags.contains(UNIQUE))
                for (UniqueKey<?> key : table.getKeys())
                    if (!key.isPrimary())
                        constraints.add(constraint(key.getName()).unique(key.getFieldsArray()));

            if (flags.contains(FOREIGN_KEY))
                for (ForeignKey<?, ?> key : table.getReferences())
                    constraints.add(constraint(key.getName()).foreignKey(key.getFieldsArray()).references(key.getKey().getTable(), key.getKey().getFieldsArray()));
        }

        return DSL.queries(
            ctx.createTable(table)
               .columns(table.fields())
               .constraints(constraints)
        );
    }

    final Queries queries(Schema schema) {
        List<Query> queries = new ArrayList<Query>();

        if (flags.contains(SCHEMA))
            queries.add(ctx.createSchema(schema.getName()));

        if (flags.contains(TABLE)) {
            for (Table<?> table : schema.getTables()) {
                List<Constraint> constraints = new ArrayList<Constraint>();

                if (flags.contains(PRIMARY_KEY))
                    for (UniqueKey<?> key : table.getKeys())
                        if (key.isPrimary())
                            constraints.add(constraint(key.getName()).primaryKey(key.getFieldsArray()));

                if (flags.contains(UNIQUE))
                    for (UniqueKey<?> key : table.getKeys())
                        if (!key.isPrimary())
                            constraints.add(constraint(key.getName()).unique(key.getFieldsArray()));

                queries.add(
                    ctx.createTable(table)
                       .columns(table.fields())
                       .constraints(constraints)
                );
            }

            if (flags.contains(FOREIGN_KEY))
                for (Table<?> table : schema.getTables())
                    for (ForeignKey<?, ?> key : table.getReferences())
                        queries.add(ctx.alterTable(table).add(constraint(key.getName()).foreignKey(key.getFieldsArray()).references(key.getKey().getTable(), key.getKey().getFieldsArray())));
        }

        return DSL.queries(queries);
    }

    final Queries queries(Catalog catalog) {
        List<Query> queries = new ArrayList<Query>();

        for (Schema schema : catalog.getSchemas())
            queries.addAll(Arrays.asList(queries(schema).queries()));

        return DSL.queries(queries);
    }
}
