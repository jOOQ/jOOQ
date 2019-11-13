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
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
import static org.jooq.SQLDialect.FIREBIRD;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.POSTGRES;
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

import java.util.Set;

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
final class CreateSequenceImpl extends AbstractRowCountQuery implements

    // Cascading interface implementations for CREATE SEQUENCE behaviour
    CreateSequenceFlagsStep {

    /**
     * Generated UID
     */
    private static final long                serialVersionUID         = 8904572826501186329L;
    private static final Clause[]            CLAUSES                  = { CREATE_SEQUENCE };
    private static final Set<SQLDialect>     NO_SUPPORT_IF_NOT_EXISTS = SQLDialect.supportedBy(DERBY, FIREBIRD);
    private static final Set<SQLDialect>     REQUIRES_START_WITH      = SQLDialect.supportedBy(DERBY);
    private static final Set<SQLDialect>     NO_SUPPORT_CACHE         = SQLDialect.supportedBy(DERBY, FIREBIRD, HSQLDB);
    private static final Set<SQLDialect>     NO_SEPARATOR             = SQLDialect.supportedBy(CUBRID, MARIADB);
    private static final Set<SQLDialect>     OMIT_NO_CACHE            = SQLDialect.supportedBy(FIREBIRD, POSTGRES);
    private static final Set<SQLDialect>     OMIT_NO_CYCLE            = SQLDialect.supportedBy(FIREBIRD);
    private static final Set<SQLDialect>     OMIT_NO_MINVALUE         = SQLDialect.supportedBy(FIREBIRD);
    private static final Set<SQLDialect>     OMIT_NO_MAXVALUE         = SQLDialect.supportedBy(FIREBIRD);

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

    final Sequence<?> $sequence()    { return sequence; }
    final boolean     $ifNotExists() { return ifNotExists; }
    final Field<?>    $startWith()   { return startWith; }
    final Field<?>    $incrementBy() { return incrementBy; }
    final Field<?>    $minvalue()    { return minvalue; }
    final boolean     $noMinvalue()  { return noMinvalue; }
    final Field<?>    $maxvalue()    { return maxvalue; }
    final boolean     $noMaxvalue()  { return noMaxvalue; }
    final boolean     $cycle()       { return cycle; }
    final boolean     $noCycle()     { return noCycle; }
    final Field<?>    $cache()       { return cache; }
    final boolean     $noCache()     { return noCache; }

    // ------------------------------------------------------------------------
    // XXX: Sequence API
    // ------------------------------------------------------------------------

    @Override
    public final CreateSequenceImpl startWith(Number value) {
        return startWith(Tools.field(value, sequence.getDataType()));
    }

    @Override
    public final CreateSequenceImpl startWith(Field<? extends Number> value) {
        this.startWith = value;
        return this;
    }

    @Override
    public final CreateSequenceImpl incrementBy(Number value) {
        return incrementBy(Tools.field(value, sequence.getDataType()));
    }

    @Override
    public final CreateSequenceImpl incrementBy(Field<? extends Number> value) {
        this.incrementBy = value;
        return this;
    }

    @Override
    public final CreateSequenceImpl minvalue(Number value) {
        return minvalue(Tools.field(value, sequence.getDataType()));
    }

    @Override
    public final CreateSequenceImpl minvalue(Field<? extends Number> value) {
        this.minvalue = value;
        return this;
    }

    @Override
    public final CreateSequenceImpl noMinvalue() {
        this.noMinvalue = true;
        return this;
    }

    @Override
    public final CreateSequenceImpl maxvalue(Number value) {
        return maxvalue(Tools.field(value, sequence.getDataType()));
    }

    @Override
    public final CreateSequenceImpl maxvalue(Field<? extends Number> value) {
        this.maxvalue = value;
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
    public final CreateSequenceImpl cache(Number value) {
        return cache(Tools.field(value, sequence.getDataType()));
    }

    @Override
    public final CreateSequenceImpl cache(Field<? extends Number> value) {
        this.cache = value;
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
        SQLDialect family = ctx.family();

        ctx.start(CREATE_SEQUENCE_SEQUENCE)
           .visit(K_CREATE)
           .sql(' ')
           .visit(family == CUBRID ? K_SERIAL : K_SEQUENCE)
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.visit(K_IF_NOT_EXISTS)
               .sql(' ');

        ctx.visit(sequence);
        String noSeparator = NO_SEPARATOR.contains(family) ? "" : " ";

        // Some databases default to sequences starting with MIN_VALUE
        if (startWith == null && REQUIRES_START_WITH.contains(family))
            ctx.sql(' ').visit(K_START_WITH).sql(" 1");
        else if (startWith != null)
            ctx.sql(' ').visit(K_START_WITH).sql(' ').visit(startWith);

        if (incrementBy != null)
            ctx.sql(' ').visit(K_INCREMENT_BY).sql(' ').visit(incrementBy);

        if (minvalue != null)
            ctx.sql(' ').visit(K_MINVALUE).sql(' ').visit(minvalue);
        else if (noMinvalue && !OMIT_NO_MINVALUE.contains(family))
            ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_MINVALUE);

        if (maxvalue != null)
            ctx.sql(' ').visit(K_MAXVALUE).sql(' ').visit(maxvalue);
        else if (noMaxvalue && !OMIT_NO_MAXVALUE.contains(family))
            ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_MAXVALUE);

        if (cycle)
            ctx.sql(' ').visit(K_CYCLE);
        else if (noCycle && !OMIT_NO_CYCLE.contains(family))
            ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_CYCLE);

        if (!NO_SUPPORT_CACHE.contains(family))
            if (cache != null)
                ctx.sql(' ').visit(K_CACHE).sql(' ').visit(cache);
            else if (noCache && !OMIT_NO_CACHE.contains(family))
                ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_CACHE);

        ctx.end(CREATE_SEQUENCE_SEQUENCE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
