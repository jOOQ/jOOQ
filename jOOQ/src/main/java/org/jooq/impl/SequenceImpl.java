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

import static org.jooq.Clause.SEQUENCE;
import static org.jooq.Clause.SEQUENCE_REFERENCE;
import static org.jooq.SQLDialect.CUBRID;
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.Keywords.F_GEN_ID;
import static org.jooq.impl.Keywords.K_CURRENT_VALUE_FOR;
import static org.jooq.impl.Keywords.K_CURRVAL;
import static org.jooq.impl.Keywords.K_NEXTVAL;
import static org.jooq.impl.Keywords.K_NEXT_VALUE_FOR;
import static org.jooq.impl.Keywords.K_PREVIOUS_VALUE_FOR;
import static org.jooq.impl.Names.N_CURRVAL;
import static org.jooq.impl.Names.N_NEXTVAL;

import org.jooq.Catalog;
import org.jooq.Clause;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Keyword;
import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Sequence;
import org.jooq.exception.SQLDialectNotSupportedException;

/**
 * A common base class for sequences
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
@org.jooq.Internal
public class SequenceImpl<T extends Number> extends AbstractNamed implements Sequence<T> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 6224349401603636427L;
    private static final Clause[] CLAUSES          = { SEQUENCE, SEQUENCE_REFERENCE };

    private final boolean         nameIsPlainSQL;
    private final Schema          schema;
    private final DataType<T>     type;
    private final Field<T>        startWith;
    private final Field<T>        incrementBy;
    private final Field<T>        minValue;
    private final Field<T>        maxValue;
    private final boolean         cycle;
    private final Field<T>        cache;

    public SequenceImpl(String name, Schema schema, DataType<T> type) {
        this(name, schema, type, false);
    }

    SequenceImpl(String name, Schema schema, DataType<T> type, boolean nameIsPlainSQL) {
        this(DSL.name(name), schema, type, nameIsPlainSQL);
    }

    SequenceImpl(Name name, Schema schema, DataType<T> type, boolean nameIsPlainSQL) {
        this(name, schema, type, nameIsPlainSQL, null, null, null, null, false, null);
    }

    SequenceImpl(Name name, Schema schema, DataType<T> type, boolean nameIsPlainSQL,
        Field<T> startWith, Field<T> incrementBy, Field<T> minValue, Field<T> maxValue, boolean cycle, Field<T> cache) {
        super(qualify(schema, name), CommentImpl.NO_COMMENT);

        this.schema = schema;
        this.type = type;
        this.nameIsPlainSQL = nameIsPlainSQL;

        this.startWith = startWith;
        this.incrementBy = incrementBy;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.cycle = cycle;
        this.cache = cache;
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
    public final DataType<T> getDataType() {
        return type;
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
    public final Field<T> getMinValue() {
        return minValue;
    }

    @Override
    public final Field<T> getMaxValue() {
        return maxValue;
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
        return new SequenceFunction(SequenceMethod.CURRVAL);
    }

    @Override
    public final Field<T> nextval() {
        return new SequenceFunction(SequenceMethod.NEXTVAL);
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

    private class SequenceFunction extends AbstractField<T> {

        /**
         * Generated UID
         */
        private static final long    serialVersionUID = 2292275568395094887L;
        private final SequenceMethod method;

        SequenceFunction(SequenceMethod method) {
            super(method.name, type);

            this.method = method;
        }

        @Override
        public final void accept(Context<?> ctx) {
            Configuration configuration = ctx.configuration();
            SQLDialect family = configuration.family();

            switch (family) {












                case POSTGRES: {
                    ctx.visit(method.keyword).sql('(');
                    ctx.sql('\'').stringLiteral(true).visit(SequenceImpl.this).stringLiteral(false).sql('\'');
                    ctx.sql(')');
                    break;
                }




                case DERBY:
                case FIREBIRD:
                case H2:
                case HSQLDB:
                case MARIADB: {
                    if (method == SequenceMethod.NEXTVAL)
                        ctx.visit(K_NEXT_VALUE_FOR).sql(' ').visit(SequenceImpl.this);
                    else if (family == H2)
                        ctx.visit(SequenceImpl.this).sql('.').visit(method.keyword);
                    else if (family == HSQLDB)
                        ctx.visit(K_CURRENT_VALUE_FOR).sql(' ').visit(SequenceImpl.this);
                    else if (family == MARIADB)
                        ctx.visit(K_PREVIOUS_VALUE_FOR).sql(' ').visit(SequenceImpl.this);
                    else if (family == FIREBIRD)
                        ctx.visit(F_GEN_ID).sql('(').visit(SequenceImpl.this).sql(", 0)");














                    else {
                        throw new SQLDialectNotSupportedException("The sequence's current value functionality is not supported for the " + family + " dialect.");
                    }
                    break;
                }

                case CUBRID: {
                    ctx.visit(SequenceImpl.this).sql('.');

                    if (method == SequenceMethod.NEXTVAL)
                        ctx.visit(DSL.keyword("next_value"));
                    else
                        ctx.visit(DSL.keyword("current_value"));

                    break;
                }

                // Default is needed for hashCode() and toString()
                default: {
                    ctx.visit(SequenceImpl.this).sql('.').visit(method.keyword);
                    break;
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        Schema mappedSchema = Tools.getMappedSchema(ctx.configuration(), schema);

        if (mappedSchema != null && !"".equals(mappedSchema.getName()) && ctx.family() != CUBRID)
                ctx.visit(mappedSchema)
                   .sql('.');

        if (nameIsPlainSQL)
            ctx.sql(getName());
        else
            ctx.visit(getUnqualifiedName());
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }
}
