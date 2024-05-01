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
import org.jooq.conf.*;
import org.jooq.impl.QOM.CycleOption;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;


/**
 * The <code>CREATE SEQUENCE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class CreateSequenceImpl<T extends Number>
extends
    AbstractDDLQuery
implements
    QOM.CreateSequence<T>,
    CreateSequenceAsStep<T>,
    CreateSequenceFlagsStep<T>,
    CreateSequenceFinalStep
{

    final Sequence<?> sequence;
    final boolean     ifNotExists;
          DataType<T> dataType;
          Field<T>    startWith;
          Field<T>    incrementBy;
          Field<T>    minvalue;
          boolean     noMinvalue;
          Field<T>    maxvalue;
          boolean     noMaxvalue;
          CycleOption cycle;
          Field<T>    cache;
          boolean     noCache;

    CreateSequenceImpl(
        Configuration configuration,
        Sequence<?> sequence,
        boolean ifNotExists
    ) {
        this(
            configuration,
            sequence,
            ifNotExists,
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

    CreateSequenceImpl(
        Configuration configuration,
        Sequence<?> sequence,
        boolean ifNotExists,
        DataType<T> dataType,
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
        this.ifNotExists = ifNotExists;
        this.dataType = dataType;
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
    public final <T extends Number> CreateSequenceImpl<T> as(Class<T> dataType) {
        return as(DefaultDataType.getDataType(null, dataType));
    }

    @Override
    public final <T extends Number> CreateSequenceImpl<T> as(DataType<T> dataType) {
        this.dataType = (DataType) dataType;
        return (CreateSequenceImpl) this;
    }

    @Override
    public final CreateSequenceImpl<T> startWith(T startWith) {
        return startWith(Tools.field(startWith, (DataType<T>) sequence.getDataType()));
    }

    @Override
    public final CreateSequenceImpl<T> startWith(Field<T> startWith) {
        this.startWith = startWith;
        return this;
    }

    @Override
    public final CreateSequenceImpl<T> incrementBy(T incrementBy) {
        return incrementBy(Tools.field(incrementBy, (DataType<T>) sequence.getDataType()));
    }

    @Override
    public final CreateSequenceImpl<T> incrementBy(Field<T> incrementBy) {
        this.incrementBy = incrementBy;
        return this;
    }

    @Override
    public final CreateSequenceImpl<T> minvalue(T minvalue) {
        return minvalue(Tools.field(minvalue, (DataType<T>) sequence.getDataType()));
    }

    @Override
    public final CreateSequenceImpl<T> minvalue(Field<T> minvalue) {
        this.minvalue = minvalue;
        return this;
    }

    @Override
    public final CreateSequenceImpl<T> noMinvalue() {
        this.noMinvalue = true;
        return this;
    }

    @Override
    public final CreateSequenceImpl<T> maxvalue(T maxvalue) {
        return maxvalue(Tools.field(maxvalue, (DataType<T>) sequence.getDataType()));
    }

    @Override
    public final CreateSequenceImpl<T> maxvalue(Field<T> maxvalue) {
        this.maxvalue = maxvalue;
        return this;
    }

    @Override
    public final CreateSequenceImpl<T> noMaxvalue() {
        this.noMaxvalue = true;
        return this;
    }

    @Override
    public final CreateSequenceImpl<T> cycle() {
        this.cycle = CycleOption.CYCLE;
        return this;
    }

    @Override
    public final CreateSequenceImpl<T> noCycle() {
        this.cycle = CycleOption.NO_CYCLE;
        return this;
    }

    @Override
    public final CreateSequenceImpl<T> cache(T cache) {
        return cache(Tools.field(cache, (DataType<T>) sequence.getDataType()));
    }

    @Override
    public final CreateSequenceImpl<T> cache(Field<T> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public final CreateSequenceImpl<T> noCache() {
        this.noCache = true;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Clause[] CLAUSES                    = { Clause.CREATE_SEQUENCE };
    static final Set<SQLDialect>  NO_SUPPORT_IF_NOT_EXISTS   = SQLDialect.supportedUntil(DERBY, FIREBIRD);
    static final Set<SQLDialect>  REQUIRES_START_WITH        = SQLDialect.supportedBy(DERBY);
    static final Set<SQLDialect>  NO_SUPPORT_CACHE           = SQLDialect.supportedBy(DERBY, FIREBIRD, HSQLDB);
    static final Set<SQLDialect>  NO_SUPPORT_AS              = SQLDialect.supportedBy(CUBRID, DUCKDB, FIREBIRD, IGNITE, MARIADB, MYSQL, SQLITE, TRINO);
    static final Set<SQLDialect>  NO_SEPARATOR               = SQLDialect.supportedBy(CUBRID, MARIADB);
    static final Set<SQLDialect>  OMIT_NO_CACHE              = SQLDialect.supportedBy(DUCKDB, FIREBIRD, POSTGRES, YUGABYTEDB);
    static final Set<SQLDialect>  OMIT_NO_CYCLE              = SQLDialect.supportedBy(FIREBIRD);
    static final Set<SQLDialect>  OMIT_NO_MINVALUE           = SQLDialect.supportedBy(FIREBIRD);
    static final Set<SQLDialect>  OMIT_NO_MAXVALUE           = SQLDialect.supportedBy(FIREBIRD);






    private final boolean supportsIfNotExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_NOT_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifNotExists && !supportsIfNotExists(ctx))
            tryCatch(ctx, DDLStatementType.CREATE_SEQUENCE, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        ctx.start(Clause.CREATE_SEQUENCE_SEQUENCE)
           .visit(K_CREATE)
           .sql(' ')
           .visit(ctx.family() == CUBRID ? K_SERIAL : K_SEQUENCE)
           .sql(' ');

        if (ifNotExists && supportsIfNotExists(ctx))
            ctx.visit(K_IF_NOT_EXISTS)
               .sql(' ');






        ctx.visit(sequence);

        String noSeparator = NO_SEPARATOR.contains(ctx.dialect()) ? "" : " ";

        if (dataType != null && !NO_SUPPORT_AS.contains(ctx.dialect())) {
            ctx.sql(' ').visit(K_AS).sql(' ');
            toSQLDDLTypeDeclaration(ctx, dataType);
        }

        // Some databases default to sequences starting with MIN_VALUE
        if (startWith == null && REQUIRES_START_WITH.contains(ctx.dialect()))
            ctx.sql(' ').visit(K_START_WITH).sql(" 1");
        else if (startWith != null)
            ctx.sql(' ').visit(K_START_WITH).sql(' ').visit(startWith);

        if (incrementBy != null)
            ctx.sql(' ').visit(K_INCREMENT_BY).sql(' ').visit(incrementBy);

        if (minvalue != null)
            ctx.sql(' ').visit(K_MINVALUE).sql(' ').visit(minvalue);
        else if (noMinvalue && !OMIT_NO_MINVALUE.contains(ctx.dialect()))
            ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_MINVALUE);

        if (maxvalue != null)
            ctx.sql(' ').visit(K_MAXVALUE).sql(' ').visit(maxvalue);
        else if (noMaxvalue && !OMIT_NO_MAXVALUE.contains(ctx.dialect()))
            ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_MAXVALUE);

        if (cycle == CycleOption.CYCLE)
            ctx.sql(' ').visit(K_CYCLE);
        else if (cycle == CycleOption.NO_CYCLE && !OMIT_NO_CYCLE.contains(ctx.dialect()))
            ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_CYCLE);

        if (!NO_SUPPORT_CACHE.contains(ctx.dialect()))
            if (cache != null)
                ctx.sql(' ').visit(K_CACHE).sql(' ').visit(cache);
            else if (noCache && !OMIT_NO_CACHE.contains(ctx.dialect()))
                ctx.sql(' ').visit(K_NO).sql(noSeparator).visit(K_CACHE);

        ctx.end(Clause.CREATE_SEQUENCE_SEQUENCE);
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Sequence<?> $sequence() {
        return sequence;
    }

    @Override
    public final boolean $ifNotExists() {
        return ifNotExists;
    }

    @Override
    public final DataType<T> $dataType() {
        return dataType;
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
    public final QOM.CreateSequence<T> $sequence(Sequence<?> newValue) {
        return $constructor().apply(newValue, $ifNotExists(), $dataType(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $ifNotExists(boolean newValue) {
        return $constructor().apply($sequence(), newValue, $dataType(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $dataType(DataType<T> newValue) {
        return $constructor().apply($sequence(), $ifNotExists(), newValue, $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $startWith(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifNotExists(), $dataType(), newValue, $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $incrementBy(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifNotExists(), $dataType(), $startWith(), newValue, $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $minvalue(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifNotExists(), $dataType(), $startWith(), $incrementBy(), newValue, $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $noMinvalue(boolean newValue) {
        return $constructor().apply($sequence(), $ifNotExists(), $dataType(), $startWith(), $incrementBy(), $minvalue(), newValue, $maxvalue(), $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $maxvalue(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifNotExists(), $dataType(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), newValue, $noMaxvalue(), $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $noMaxvalue(boolean newValue) {
        return $constructor().apply($sequence(), $ifNotExists(), $dataType(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), newValue, $cycle(), $cache(), $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $cycle(CycleOption newValue) {
        return $constructor().apply($sequence(), $ifNotExists(), $dataType(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), newValue, $cache(), $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $cache(Field<T> newValue) {
        return $constructor().apply($sequence(), $ifNotExists(), $dataType(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), newValue, $noCache());
    }

    @Override
    public final QOM.CreateSequence<T> $noCache(boolean newValue) {
        return $constructor().apply($sequence(), $ifNotExists(), $dataType(), $startWith(), $incrementBy(), $minvalue(), $noMinvalue(), $maxvalue(), $noMaxvalue(), $cycle(), $cache(), newValue);
    }

    public final Function12<? super Sequence<?>, ? super Boolean, ? super DataType<T>, ? super Field<T>, ? super Field<T>, ? super Field<T>, ? super Boolean, ? super Field<T>, ? super Boolean, ? super CycleOption, ? super Field<T>, ? super Boolean, ? extends QOM.CreateSequence<T>> $constructor() {
        return (a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12) -> new CreateSequenceImpl(configuration(), a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
    }






































}
