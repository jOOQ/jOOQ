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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.ExtendedDataKey.*;
import static org.jooq.impl.Tools.SimpleDataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.ParamType;
import org.jooq.impl.QOM.CycleOption;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>ALTER SEQUENCE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class AlterSequenceImpl<T extends Number>
extends
    AbstractDDLQuery
implements
    QOM.AlterSequence<T>,
    AlterSequenceStep<T>,
    AlterSequenceFlagsStep<T>,
    AlterSequenceFinalStep
{

    final Sequence<T> sequence;
    final boolean     ifExists;
          Sequence<?> renameTo;
          boolean     restart;
          Field<T>    restartWith;
          Field<T>    startWith;
          Field<T>    incrementBy;
          Field<T>    minvalue;
          boolean     noMinvalue;
          Field<T>    maxvalue;
          boolean     noMaxvalue;
          CycleOption cycle;
          Field<T>    cache;
          boolean     noCache;

    AlterSequenceImpl(
        Configuration configuration,
        Sequence<T> sequence,
        boolean ifExists
    ) {
        this(
            configuration,
            sequence,
            ifExists,
            null,
            false,
            null,
            null,
            null,
            null,
            false,
            null,
            false,
            null,
            null,
            false
        );
    }

    AlterSequenceImpl(
        Configuration configuration,
        Sequence<T> sequence,
        boolean ifExists,
        Sequence<?> renameTo,
        boolean restart,
        Field<T> restartWith,
        Field<T> startWith,
        Field<T> incrementBy,
        Field<T> minvalue,
        boolean noMinvalue,
        Field<T> maxvalue,
        boolean noMaxvalue,
        CycleOption cycle,
        Field<T> cache,
        boolean noCache
    ) {
        super(configuration);

        this.sequence = sequence;
        this.ifExists = ifExists;
        this.renameTo = renameTo;
        this.restart = restart;
        this.restartWith = restartWith;
        this.startWith = startWith;
        this.incrementBy = incrementBy;
        this.minvalue = minvalue;
        this.noMinvalue = noMinvalue;
        this.maxvalue = maxvalue;
        this.noMaxvalue = noMaxvalue;
        this.cycle = cycle;
        this.cache = cache;
        this.noCache = noCache;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final AlterSequenceImpl<T> renameTo(String renameTo) {
        return renameTo(DSL.sequence(DSL.name(renameTo)));
    }

    @Override
    public final AlterSequenceImpl<T> renameTo(Name renameTo) {
        return renameTo(DSL.sequence(renameTo));
    }

    @Override
    public final AlterSequenceImpl<T> renameTo(Sequence<?> renameTo) {
        this.renameTo = renameTo;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> restart() {
        this.restart = true;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> restartWith(T restartWith) {
        return restartWith(Tools.field(restartWith));
    }

    @Override
    public final AlterSequenceImpl<T> restartWith(Field<T> restartWith) {
        this.restartWith = restartWith;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> startWith(T startWith) {
        return startWith(Tools.field(startWith));
    }

    @Override
    public final AlterSequenceImpl<T> startWith(Field<T> startWith) {
        this.startWith = startWith;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> incrementBy(T incrementBy) {
        return incrementBy(Tools.field(incrementBy));
    }

    @Override
    public final AlterSequenceImpl<T> incrementBy(Field<T> incrementBy) {
        this.incrementBy = incrementBy;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> minvalue(T minvalue) {
        return minvalue(Tools.field(minvalue));
    }

    @Override
    public final AlterSequenceImpl<T> minvalue(Field<T> minvalue) {
        this.minvalue = minvalue;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> noMinvalue() {
        this.noMinvalue = true;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> maxvalue(T maxvalue) {
        return maxvalue(Tools.field(maxvalue));
    }

    @Override
    public final AlterSequenceImpl<T> maxvalue(Field<T> maxvalue) {
        this.maxvalue = maxvalue;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> noMaxvalue() {
        this.noMaxvalue = true;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> cycle() {
        this.cycle = CycleOption.CYCLE;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> noCycle() {
        this.cycle = CycleOption.NO_CYCLE;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> cache(T cache) {
        return cache(Tools.field(cache));
    }

    @Override
    public final AlterSequenceImpl<T> cache(Field<T> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public final AlterSequenceImpl<T> noCache() {
        this.noCache = true;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[]        CLAUSES              = { Clause.ALTER_SEQUENCE };
    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD);
    private static final Set<SQLDialect> NO_SEPARATOR         = SQLDialect.supportedBy(CUBRID, MARIADB);
    private static final Set<SQLDialect> NO_SUPPORT_CACHE     = SQLDialect.supportedBy(DERBY, FIREBIRD, HSQLDB);
    private static final Set<SQLDialect> EMULATE_NO_CACHE     = SQLDialect.supportedBy(POSTGRES, YUGABYTEDB);





    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.ALTER_SEQUENCE, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        switch (ctx.family()) {











            case MARIADB:
                if (renameTo != null)
                    acceptRenameTable(ctx);
                else
                    accept1(ctx);

                break;

            default:
                accept1(ctx);
                break;
        }
    }

    private final void acceptRenameTable(Context<?> ctx) {
        boolean qualify = ctx.qualify();

        ctx.start(Clause.ALTER_SEQUENCE_SEQUENCE)
           .start(Clause.ALTER_SEQUENCE_RENAME)
           .visit(K_ALTER_TABLE)
           .sql(' ')
           .visit(sequence)
           .sql(' ')
           .visit(K_RENAME_TO)
           .sql(' ')
           .qualify(false, c -> c.visit(renameTo))
           .end(Clause.ALTER_SEQUENCE_RENAME)
           .end(Clause.ALTER_SEQUENCE_SEQUENCE);
    }


















    private final void accept1(Context<?> ctx) {
        ctx.start(Clause.ALTER_SEQUENCE_SEQUENCE)
           .visit(K_ALTER)
           .sql(' ')
           .visit(ctx.family() == CUBRID ? K_SERIAL : K_SEQUENCE);

        if (ifExists && supportsIfExists(ctx))
            ctx.sql(' ').visit(K_IF_EXISTS);






        ctx.sql(' ').visit(sequence);

        ctx.end(Clause.ALTER_SEQUENCE_SEQUENCE);

        if (renameTo != null) {
            ctx.start(Clause.ALTER_SEQUENCE_RENAME)
               .sql(' ').visit(K_RENAME_TO)
               .sql(' ')
               .qualify(false, c -> c.visit(renameTo))
               .end(Clause.ALTER_SEQUENCE_RENAME);
        }
        else {
            ctx.start(Clause.ALTER_SEQUENCE_RESTART);

            String noSeparator = NO_SEPARATOR.contains(ctx.dialect()) ? "" : " ";

            if (incrementBy != null) {
                ctx.sql(' ').visit(K_INCREMENT_BY)
                   .sql(' ').visit(incrementBy);
            }

            if (minvalue != null)
                ctx.sql(' ').visit(K_MINVALUE).sql(' ').visit(minvalue);
            else if (noMinvalue)
                ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_MINVALUE);

            if (maxvalue != null)
                ctx.sql(' ').visit(K_MAXVALUE).sql(' ').visit(maxvalue);
            else if (noMaxvalue)
                ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_MAXVALUE);

            if (startWith != null) {
                ctx.sql(' ').visit(K_START_WITH)
                   .sql(' ').visit(startWith);
            }

            if (restart) {










                    ctx.sql(' ').visit(K_RESTART);
            }
            else if (restartWith != null) {
                if (ctx.family() == CUBRID)
                    ctx.sql(' ').visit(K_START_WITH)
                       .sql(' ').visit(restartWith);
                else
                    ctx.sql(' ').visit(K_RESTART_WITH)
                       .sql(' ').visit(restartWith);
            }

            if (!NO_SUPPORT_CACHE.contains(ctx.dialect()))
                if (cache != null)
                    ctx.sql(' ').visit(K_CACHE).sql(' ').visit(cache);
                else if (noCache)
                    if (EMULATE_NO_CACHE.contains(ctx.dialect()))
                        ctx.sql(' ').visit(K_CACHE).sql(' ').sql(1);
                    else
                        ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_CACHE);

            if (cycle == CycleOption.CYCLE)
                ctx.sql(' ').visit(K_CYCLE);
            else if (cycle == CycleOption.NO_CYCLE)
                ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_CYCLE);

            ctx.end(Clause.ALTER_SEQUENCE_RESTART);
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Sequence<T> $sequence() {
        return sequence;
    }

    @Override
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final Sequence<?> $renameTo() {
        return renameTo;
    }

    @Override
    public final boolean $restart() {
        return restart;
    }

    @Override
    public final Field<T> $restartWith() {
        return restartWith;
    }

    @Override
    public final Field<T> $startWith() {
        return startWith;
    }

    @Override
    public final Field<T> $incrementBy() {
        return incrementBy;
    }

    @Override
    public final Field<T> $minvalue() {
        return minvalue;
    }

    @Override
    public final boolean $noMinvalue() {
        return noMinvalue;
    }

    @Override
    public final Field<T> $maxvalue() {
        return maxvalue;
    }

    @Override
    public final boolean $noMaxvalue() {
        return noMaxvalue;
    }

    @Override
    public final CycleOption $cycle() {
        return cycle;
    }

    @Override
    public final Field<T> $cache() {
        return cache;
    }

    @Override
    public final boolean $noCache() {
        return noCache;
    }

    @Override
    public final QOM.AlterSequence<T> $sequence(Sequence<T> newValue) {
        return $constructor().apply(newValue, $ifExists(), $renameTo(), $restart(), $restartWith(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $ifExists(boolean newValue) {
        return $constructor().apply($sequence(), newValue, $renameTo(), $restart(), $restartWith(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $renameTo(Sequence<?> newValue) {
        return $constructor().apply($sequence(), $ifExists(), newValue, $restart(), $restartWith(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $restart(boolean newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), newValue, $restartWith(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $restartWith(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), $restart(), newValue, $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $startWith(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), $restart(), $restartWith(), newValue, $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $incrementBy(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), $restart(), $restartWith(), $startWith(), newValue, $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $minvalue(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), $restart(), $restartWith(), $startWith(), $incrementBy(), newValue, $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $noMinvalue(boolean newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), $restart(), $restartWith(), $startWith(), $incrementBy(), $minvalue(), newValue, $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $maxvalue(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), $restart(), $restartWith(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), newValue, $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $noMaxvalue(boolean newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), $restart(), $restartWith(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), newValue, $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $cycle(CycleOption newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), $restart(), $restartWith(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), newValue, $cache(), $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $cache(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), $restart(), $restartWith(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), newValue, $noCache());
    }

    @Override
    public final QOM.AlterSequence<T> $noCache(boolean newValue) {
        return $constructor().apply($sequence(), $ifExists(), $renameTo(), $restart(), $restartWith(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), newValue);
    }

    public final Function14<? super Sequence<T>, ? super Boolean, ? super Sequence<?>, ? super Boolean, ? super Field<T>, ? super Field<T>, ? super Field<T>, ? super Field<T>, ? super Boolean, ? super Field<T>, ? super Boolean, ? super CycleOption, ? super Field<T>, ? super Boolean, ? extends QOM.AlterSequence<T>> $constructor() {
        return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14) -> new AlterSequenceImpl(configuration(), a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
    }









































}
