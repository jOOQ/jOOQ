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
import static org.jooq.Clause.CREATE_SEQUENCE;
import static org.jooq.Clause.CREATE_SEQUENCE_SEQUENCE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.SQLSERVER;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateSequenceFinalStep;
import org.jooq.Sequence;

/**
 * @author Lukas Eder
 */
class CreateSequenceImpl extends AbstractQuery implements

    // Cascading interface implementations for CREATE SEQUENCE behaviour
    CreateSequenceFinalStep {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 8904572826501186329L;
    private static final Clause[] CLAUSES          = { CREATE_SEQUENCE };

    private final Sequence<?>     sequence;

    CreateSequenceImpl(Configuration configuration, Sequence<?> sequence) {
        super(configuration);

        this.sequence = sequence;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.start(CREATE_SEQUENCE_SEQUENCE)
           .keyword("create")
           .sql(' ')
           .keyword(ctx.family() == CUBRID ? "serial" : "sequence")
           .sql(' ')
           .visit(sequence);

        // Some databases default to sequences starting with MIN_VALUE
        if (asList(DERBY, SQLSERVER).contains(ctx.family()))
            ctx.keyword("start with").sql(" 1");

        ctx.end(CREATE_SEQUENCE_SEQUENCE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
