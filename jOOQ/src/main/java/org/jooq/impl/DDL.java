/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

import static org.jooq.impl.DSL.constraint;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Constraint;
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
public class DDL {

    private final DSLContext ctx;

    public DDL(DSLContext ctx) {
        this.ctx = ctx;
    }

    public Queries queries(Table<?> table) {
        List<Constraint> constraints = new ArrayList<Constraint>();

        for (UniqueKey<?> key : table.getKeys())
            if (key.isPrimary())
                constraints.add(constraint(key.getName()).primaryKey(key.getFieldsArray()));
            else
                constraints.add(constraint(key.getName()).unique(key.getFieldsArray()));

        for (ForeignKey<?, ?> key : table.getReferences())
            constraints.add(constraint(key.getName()).foreignKey(key.getFieldsArray()).references(key.getKey().getTable(), key.getKey().getFieldsArray()));

        return DSL.queries(
            ctx.createTable(table)
               .columns(table.fields())
               .constraints(constraints)
        );
    }

    public Queries queries(Schema schema) {
        List<Query> queries = new ArrayList<Query>();

        for (Table<?> table : schema.getTables()) {
            List<Constraint> constraints = new ArrayList<Constraint>();

            for (UniqueKey<?> key : table.getKeys())
                if (key.isPrimary())
                    constraints.add(constraint(key.getName()).primaryKey(key.getFieldsArray()));
                else
                    constraints.add(constraint(key.getName()).unique(key.getFieldsArray()));

            queries.add(
                ctx.createTable(table)
                   .columns(table.fields())
                   .constraints(constraints)
            );
        }

        for (Table<?> table : schema.getTables())
            for (ForeignKey<?, ?> key : table.getReferences())
                queries.add(ctx.alterTable(table).add(constraint(key.getName()).foreignKey(key.getFieldsArray()).references(key.getKey().getTable(), key.getKey().getFieldsArray())));

        return DSL.queries(queries);
    }
}
