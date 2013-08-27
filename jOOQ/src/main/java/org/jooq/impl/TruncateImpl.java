/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.Clause.TRUNCATE;
import static org.jooq.Clause.TRUNCATE_TRUNCATE;

import org.jooq.BindContext;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TruncateCascadeStep;
import org.jooq.TruncateFinalStep;
import org.jooq.TruncateIdentityStep;

/**
 * @author Lukas Eder
 */
class TruncateImpl<R extends Record> extends AbstractQuery implements

    // Cascading interface implementations for Truncate behaviour
    TruncateIdentityStep<R> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { TRUNCATE };

    private final Table<R>    table;
    private Boolean           cascade;
    private Boolean           restartIdentity;

    public TruncateImpl(Configuration configuration, Table<R> table) {
        super(configuration);

        this.table = table;
    }

    @Override
    public final TruncateFinalStep<R> cascade() {
        cascade = true;
        return this;
    }

    @Override
    public final TruncateFinalStep<R> restrict() {
        cascade = false;
        return this;
    }

    @Override
    public final TruncateCascadeStep<R> restartIdentity() {
        restartIdentity = true;
        return this;
    }

    @Override
    public final TruncateCascadeStep<R> continueIdentity() {
        restartIdentity = false;
        return this;
    }

    @Override
    public final void toSQL(RenderContext context) {
        switch (context.configuration().dialect()) {

            // These dialects don't implement the TRUNCATE statement
            case FIREBIRD:
            case INGRES:
            case SQLITE: {
                context.visit(create(context).delete(table));
                break;
            }

            // All other dialects do
            default: {
                context.start(TRUNCATE_TRUNCATE)
                       .keyword("truncate table").sql(" ")
                       .visit(table);

                if (context.configuration().dialect() == SQLDialect.DB2) {
                    context.sql(" ").keyword("immediate");
                }

                if (restartIdentity != null) {
                    context.formatSeparator()
                           .keyword(restartIdentity ? "restart identity" : "continue identity");
                }

                if (cascade != null) {
                    context.formatSeparator()
                           .keyword(cascade ? "cascade" : "restrict");
                }

                context.end(TRUNCATE_TRUNCATE);
                break;
            }
        }
    }

    @Override
    public final void bind(BindContext context) {
        context.visit(table);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
