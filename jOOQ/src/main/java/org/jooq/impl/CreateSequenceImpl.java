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

import static org.jooq.Clause.CREATE_SEQUENCE;
import static org.jooq.Clause.CREATE_SEQUENCE_SEQUENCE;
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
// ...
import static org.jooq.impl.Keywords.K_CACHE;
import static org.jooq.impl.Keywords.K_CREATE;
import static org.jooq.impl.Keywords.K_CYCLE;
import static org.jooq.impl.Keywords.K_IF_NOT_EXISTS;
import static org.jooq.impl.Keywords.K_INCREMENT_BY;
import static org.jooq.impl.Keywords.K_MAXVALUE;
import static org.jooq.impl.Keywords.K_MINVALUE;
import static org.jooq.impl.Keywords.K_NO;
import static org.jooq.impl.Keywords.K_SEQUENCE;
import static org.jooq.impl.Keywords.K_SERIAL;
import static org.jooq.impl.Keywords.K_START_WITH;

import java.util.EnumSet;

import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateSequenceFlagsStep;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.Sequence;

/**
 * @author Lukas Eder
 */
final class CreateSequenceImpl extends AbstractQuery implements

    // Cascading interface implementations for CREATE SEQUENCE behaviour
    CreateSequenceFlagsStep {

    /**
     * Generated UID
     */
    private static final long                serialVersionUID         = 8904572826501186329L;
    private static final Clause[]            CLAUSES                  = { CREATE_SEQUENCE };
    private static final EnumSet<SQLDialect> NO_SUPPORT_IF_NOT_EXISTS = EnumSet.of(DERBY, FIREBIRD);
    private static final EnumSet<SQLDialect> REQUIRES_START_WITH      = EnumSet.of(DERBY);
    private static final EnumSet<SQLDialect> NO_SUPPORT_CACHE         = EnumSet.of(DERBY, HSQLDB);

    private final Sequence<?>                sequence;
    private final boolean                    ifNotExists;
    private Field<?>                         startWith;
    private Field<?>                         incrementBy;
    private Field<?>                         minvalue;
    private boolean                          noMinvalue;
    private Field<?>                         maxvalue;
    private boolean                          noMaxvalue;
    private boolean                          cycle;
    private boolean                          noCycle;
    private Field<?>                         cache;
    private boolean                          noCache;

    CreateSequenceImpl(Configuration configuration, Sequence<?> sequence, boolean ifNotExists) {
        super(configuration);

        this.sequence = sequence;
        this.ifNotExists = ifNotExists;
    }

    // ------------------------------------------------------------------------
    // XXX: Sequence API
    // ------------------------------------------------------------------------

    @Override
    public final CreateSequenceImpl startWith(Number constant) {
        return startWith(DSL.val(constant));
    }

    @Override
    public final CreateSequenceImpl startWith(Field<? extends Number> constant) {
        this.startWith = constant;
        return this;
    }

    @Override
    public final CreateSequenceImpl incrementBy(Number constant) {
        return incrementBy(DSL.val(constant));
    }

    @Override
    public final CreateSequenceImpl incrementBy(Field<? extends Number> constant) {
        this.incrementBy = constant;
        return this;
    }

    @Override
    public final CreateSequenceImpl minvalue(Number constant) {
        return minvalue(DSL.val(constant));
    }

    @Override
    public final CreateSequenceImpl minvalue(Field<? extends Number> constant) {
        this.minvalue = constant;
        return this;
    }

    @Override
    public final CreateSequenceImpl noMinvalue() {
        this.noMinvalue = true;
        return this;
    }

    @Override
    public final CreateSequenceImpl maxvalue(Number constant) {
        return maxvalue(DSL.val(constant));
    }

    @Override
    public final CreateSequenceImpl maxvalue(Field<? extends Number> constant) {
        this.maxvalue = constant;
        return this;
    }

    @Override
    public final CreateSequenceImpl noMaxvalue() {
        this.noMaxvalue = true;
        return this;
    }

    @Override
    public final CreateSequenceImpl cycle() {
        this.cycle = true;
        return this;
    }

    @Override
    public final CreateSequenceImpl noCycle() {
        this.noCycle = true;
        return this;
    }

    @Override
    public final CreateSequenceImpl cache(Number constant) {
        return cache(DSL.val(constant));
    }

    @Override
    public final CreateSequenceImpl cache(Field<? extends Number> constant) {
        this.cache = constant;
        return this;
    }

    @Override
    public final CreateSequenceImpl noCache() {
        this.noCache = true;
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.family());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx)) {
            Tools.beginTryCatch(ctx, DDLStatementType.CREATE_SEQUENCE);
            accept0(ctx);
            Tools.endTryCatch(ctx, DDLStatementType.CREATE_SEQUENCE);
        }
        else {
            accept0(ctx);
        }
    }

    private final void accept0(Context<?> ctx) {
        ctx.start(CREATE_SEQUENCE_SEQUENCE)
           .visit(K_CREATE)
           .sql(' ')
           .visit(ctx.family() == CUBRID ? K_SERIAL : K_SEQUENCE)
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.visit(K_IF_NOT_EXISTS)
               .sql(' ');

        ctx.visit(sequence);

        // Some databases default to sequences starting with MIN_VALUE
        if (startWith == null && REQUIRES_START_WITH.contains(ctx.family()))
            ctx.sql(' ').visit(K_START_WITH).sql(" 1");
        else if (startWith != null)
            ctx.sql(' ').visit(K_START_WITH).sql(' ').visit(startWith);

        if (incrementBy != null)
            ctx.sql(' ').visit(K_INCREMENT_BY).sql(' ').visit(incrementBy);

        if (minvalue != null)
            ctx.sql(' ').visit(K_MINVALUE).sql(' ').visit(minvalue);
        else if (noMinvalue)
            ctx.sql(' ').visit(K_NO).sql(' ').visit(K_MINVALUE);

        if (maxvalue != null)
            ctx.sql(' ').visit(K_MAXVALUE).sql(' ').visit(maxvalue);
        else if (noMaxvalue)
            ctx.sql(' ').visit(K_NO).sql(' ').visit(K_MAXVALUE);

        if (cycle)
            ctx.sql(' ').visit(K_CYCLE);
        else if (noCycle)
            ctx.sql(' ').visit(K_NO).sql(' ').visit(K_CYCLE);

        if (!NO_SUPPORT_CACHE.contains(ctx.family()))
            if (cache != null)
                ctx.sql(' ').visit(K_CACHE).sql(' ').visit(cache);
            else if (noCache)
                ctx.sql(' ').visit(K_NO).sql(' ').visit(K_CACHE);

        ctx.end(CREATE_SEQUENCE_SEQUENCE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
