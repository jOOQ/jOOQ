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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
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

import static org.jooq.Clause.SEQUENCE;
import static org.jooq.Clause.SEQUENCE_REFERENCE;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.impl.DSL.function;
import static org.jooq.impl.DSL.generateSeries;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DefaultMetaProvider.meta;
import static org.jooq.impl.Keywords.K_CURRENT_VALUE_FOR;
import static org.jooq.impl.Keywords.K_CURRVAL;
import static org.jooq.impl.Keywords.K_NEXTVAL;
import static org.jooq.impl.Keywords.K_NEXT_VALUE_FOR;
import static org.jooq.impl.Keywords.K_PREVIOUS_VALUE_FOR;
import static org.jooq.impl.Keywords.K_SEQUENCE;
import static org.jooq.impl.Names.N_CURRVAL;
import static org.jooq.impl.Names.N_GENERATE_SERIES;
import static org.jooq.impl.Names.N_GEN_ID;
import static org.jooq.impl.Names.N_GET_NEXT_SEQUENCE_VALUE;
import static org.jooq.impl.Names.N_NEXTVAL;

import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.Comment;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Keyword;
import org.jooq.Name;
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Sequence;
import org.jooq.impl.QOM.UNotYetImplemented;

/**
 * A common base class for sequences
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class SequenceImpl<T extends Number>
extends
    AbstractTypedNamed<T>
implements
    Sequence<T>,
    UNotYetImplemented
{

    private static final Clause[]     CLAUSES          = { SEQUENCE, SEQUENCE_REFERENCE };

    private final Schema              schema;
    private final Field<T>            startWith;
    private final Field<T>            incrementBy;
    private final Field<T>            minvalue;
    private final Field<T>            maxvalue;
    private final boolean             cycle;
    private final Field<T>            cache;
    private final SequenceFunction<T> currval;
    private final SequenceFunction<T> nextval;

    SequenceImpl(Name name, Schema schema, Comment comment, DataType<T> type) {
        this(name, schema, comment, type, null, null, null, null, false, null);
    }

    SequenceImpl(
        Name name,
        Schema schema,
        Comment comment,
        DataType<T> type,
        Field<T> startWith,
        Field<T> incrementBy,
        Field<T> minvalue,
        Field<T> maxvalue,
        boolean cycle,
        Field<T> cache
    ) {
        super(qualify(schema, name), comment, type);

        this.schema = schema;
        this.startWith = startWith;
        this.incrementBy = incrementBy;
        this.minvalue = minvalue;
        this.maxvalue = maxvalue;
        this.cycle = cycle;
        this.cache = cache;
        this.currval = new SequenceFunction<>(SequenceMethod.CURRVAL, this);
        this.nextval = new SequenceFunction<>(SequenceMethod.NEXTVAL, this);
    }

    @Override
    public final Catalog getCatalog() {
        return getSchema() == null ? null : getSchema().getCatalog();
    }

    @Override
    public final Schema getSchema() {
        return schema;
    }

    @Override
    public final Field<T> getStartWith() {
        return startWith;
    }

    @Override
    public final Field<T> getIncrementBy() {
        return incrementBy;
    }

    @Override
    public final Field<T> getMinvalue() {
        return minvalue;
    }

    @Override
    public final Field<T> getMaxvalue() {
        return maxvalue;
    }

    @Override
    public final boolean getCycle() {
        return cycle;
    }

    @Override
    public final Field<T> getCache() {
        return cache;
    }

    @Override
    public final Field<T> currval() {
        return currval;
    }

    @Override
    public final Field<T> nextval() {
        return nextval;
    }

    @Override
    public final Select<Record1<T>> nextvals(int size) {
        return DSL.select(nextval().as(N_NEXTVAL)).from(generateSeries(1, size).as(N_GENERATE_SERIES));
    }

    private enum SequenceMethod {
        CURRVAL(K_CURRVAL, N_CURRVAL),
        NEXTVAL(K_NEXTVAL, N_NEXTVAL);

        final Keyword keyword;
        final Name name;

        private SequenceMethod(Keyword keyword, Name name) {
            this.keyword = keyword;
            this.name = name;
        }
    }

    private static class SequenceFunction<T extends Number> extends AbstractField<T> implements UNotYetImplemented {
        private final SequenceMethod  method;
        private final SequenceImpl<T> sequence;

        SequenceFunction(SequenceMethod method, SequenceImpl<T> sequence) {
            super(method.name, sequence.getDataType());

            this.method = method;
            this.sequence = sequence;
        }

        @Override
        public final void accept(Context<?> ctx) {
            Configuration configuration = ctx.configuration();
            SQLDialect family = configuration.family();

            switch (family) {




















                case DUCKDB:
                case POSTGRES:
                case YUGABYTEDB:
                    ctx.visit(method.keyword).sql('(');
                    ctx.sql('\'').stringLiteral(true).visit(sequence).stringLiteral(false).sql('\'');
                    ctx.sql(')');
                    break;

                case CUBRID:
                    ctx.visit(sequence).sql('.');

                    if (method == SequenceMethod.NEXTVAL)
                        ctx.visit(DSL.keyword("next_value"));
                    else
                        ctx.visit(DSL.keyword("current_value"));

                    break;


                case DERBY:
                case FIREBIRD:
                case H2:
                case HSQLDB:
                case MARIADB:
                    if (method == SequenceMethod.NEXTVAL)
                        ctx.visit(K_NEXT_VALUE_FOR).sql(' ').visit(sequence);




                    else if (family == HSQLDB || family == H2)
                        ctx.visit(K_CURRENT_VALUE_FOR).sql(' ').visit(sequence);
                    else if (family == MARIADB)
                        ctx.visit(K_PREVIOUS_VALUE_FOR).sql(' ').visit(sequence);
                    else if (family == FIREBIRD)
                        ctx.visit(N_GEN_ID).sql('(').visit(sequence).sql(", 0)");













                    else
                        ctx.visit(sequence).sql('.').visit(method.keyword);

                    break;

                // Default is needed for hashCode() and toString()
                default:
                    ctx.visit(sequence).sql('.').visit(method.keyword);
                    break;
            }
        }

        // -------------------------------------------------------------------------
        // The Object API
        // -------------------------------------------------------------------------

        @Override
        public boolean equals(Object that) {
            if (that instanceof SequenceFunction<?> s)
                return method == s.method && sequence.equals(s.sequence);
            else
                return super.equals(that);
        }
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {













        QualifiedImpl.acceptMappedSchemaPrefix(ctx, getSchema());

        ctx.visit(getUnqualifiedName());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Schema $schema() {
        return schema;
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof SequenceImpl<?> s)
            return getQualifiedName().equals(s.getQualifiedName());
        else
            return super.equals(that);
    }
}
