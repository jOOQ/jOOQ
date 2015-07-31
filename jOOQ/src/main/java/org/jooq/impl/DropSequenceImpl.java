/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
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
package org.jooq.impl;

import static java.util.Arrays.asList;
import static org.jooq.Clause.DROP_SEQUENCE;
import static org.jooq.Clause.DROP_SEQUENCE_SEQUENCE;
import static org.jooq.SQLDialect.ACCESS;
import static org.jooq.SQLDialect.ASE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DB2;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.ORACLE;
import static org.jooq.SQLDialect.SQLSERVER;
import static org.jooq.SQLDialect.SYBASE;
import static org.jooq.impl.DropStatementType.SEQUENCE;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DropSequenceFinalStep;
import org.jooq.Sequence;

/**
 * @author Lukas Eder
 */
class DropSequenceImpl extends AbstractQuery implements

    // Cascading interface implementations for DROP SEQUENCE behaviour
    DropSequenceFinalStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { DROP_SEQUENCE };

    private final Sequence<?>     sequence;
    private final boolean         ifExists;

    DropSequenceImpl(Configuration configuration, Sequence<?> sequence) {
        this(configuration, sequence, false);
    }

    DropSequenceImpl(Configuration configuration, Sequence<?> sequence, boolean ifExists) {
        super(configuration);

        this.sequence = sequence;
        this.ifExists = ifExists;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfExists(Context<?> ctx) {
        return !asList(ACCESS, ASE, DB2, DERBY, FIREBIRD, ORACLE, SQLSERVER, SYBASE).contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx)) {
            Utils.executeImmediateBegin(ctx, SEQUENCE);
            accept0(ctx);
            Utils.executeImmediateEnd(ctx, SEQUENCE);
        }
        else {
            accept0(ctx);
        }
    }

    private void accept0(Context<?> ctx) {
        ctx.start(DROP_SEQUENCE_SEQUENCE)
           .keyword("drop")
           .sql(' ')
           .keyword(ctx.family() == CUBRID ? "serial" : "sequence")
           .sql(' ');

        if (ifExists && supportsIfExists(ctx))
            ctx.keyword("if exists").sql(' ');

        ctx.visit(sequence);

        if (ctx.family() == DERBY)
            ctx.sql(' ').keyword("restrict");

        ctx.end(DROP_SEQUENCE_SEQUENCE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
