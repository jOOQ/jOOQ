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

import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_VALUE;
import static org.jooq.SQLDialect.*;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.ParamType.NAMED_OR_INLINED;

import java.util.Arrays;
import java.util.Set;

// ...
// ...
// ...
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Name;
import org.jooq.Param;
import org.jooq.ParamMode;
import org.jooq.QualifiedRecord;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.impl.DefaultBinding.InternalBinding;
import org.jooq.impl.QOM.NotYetImplementedException;
import org.jooq.tools.StringUtils;

/**
 * A base implementation for {@link Param}
 *
 * @author Lukas Eder
 */
abstract class AbstractParam<T> extends AbstractParamX<T> implements SimpleQueryPart {

    private static Set<SQLDialect> NO_SUPPORT_ARRAY_BINDS    = SQLDialect.supportedBy(TRINO);
    private static Set<SQLDialect> NO_SUPPORT_INTERVAL_BINDS = SQLDialect.supportedBy(TRINO);
    private static final Clause[]  CLAUSES                   = { FIELD, FIELD_VALUE };

    private final String           paramName;
    T                              value;
    private boolean                inline;

    AbstractParam(T value, DataType<T> type) {
        this(value, type, null);
    }

    AbstractParam(T value, DataType<T> type, String paramName) {
        super(name(value, paramName), type);

        this.paramName = paramName;
        this.value = value;
    }

    @Override
    final boolean isPossiblyNullable() {
        return !inline || value == null;
    }

    /**
     * A utility method that generates a field name.
     * <p>
     * <ul>
     * <li>If <code>paramName != null</code>, take <code>paramName</code></li>
     * <li>Otherwise, take the string value of <code>value</code></li>
     * </ul>
     */
    static final Name name(Object value, String paramName) {
        return new LazyName(() -> DSL.name(
               paramName != null
             ? paramName

             // [#3707] Protect value.toString call for certain jOOQ types.
             : value instanceof QualifiedRecord<?> q
             ? q.getQualifier().getName()





             : name(value)
        ));
    }

    private final static String name(Object value) {

        // [#13392] [##14131] The generated name of an array value shouldn't
        //          depend on the identity of the value, but on the value itself
        if (value instanceof byte[] b) {
            return "b_" + Internal.hash0(Arrays.hashCode(Arrays.copyOf(b, 16)));
        }
        else if (value instanceof Object[] o) {
            return "a_" + Internal.hash0(Arrays.hashCode(Arrays.copyOf(o, 16)));
        }
        else
            return String.valueOf(value);
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    final boolean parenthesised(Context<?> ctx) {

        // [#13581] User defined Binding implementations can generate any type
        //          of SQL, including SQL that isn't parenthesised.
        // [#8681]  Negative values (e.g. "-1") can't be considered parenthesised
        //          just like inline(1).neg() is not parenthesised
        return getBinding() instanceof InternalBinding && positive(value);
    }

    private static boolean positive(Object value) {
        return value instanceof Number n ? n.doubleValue() >= 0 : false;
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return CLAUSES;
    }

    @Override
    public final boolean generatesCast() {
        return true;
    }

    // ------------------------------------------------------------------------
    // XXX: Param API
    // ------------------------------------------------------------------------

    @Override
    final void setConverted0(Object value) {
        this.value = getDataType().convert(value);
    }

    @Override
    public final T getValue() {
        return value;
    }

    @Override
    public final String getParamName() {
        return paramName;
    }

    @Override
    final void setInline0(boolean inline) {
        this.inline = inline;
    }

    @Override
    public final boolean isInline() {
        return inline;
    }

    final boolean isInline(Context<?> ctx) {
        return isInline()
            || (ctx.paramType() == INLINED)
            || (ctx.paramType() == NAMED_OR_INLINED && StringUtils.isBlank(paramName))
            // [#10153] [#11485] Some dialects support ARRAY types only as inline values
            || NO_SUPPORT_ARRAY_BINDS.contains(ctx.dialect()) && getDataType().isArray()
            || NO_SUPPORT_INTERVAL_BINDS.contains(ctx.dialect()) && getDataType().isInterval()
        ;
    }

    @Override
    public final ParamType getParamType() {
        return inline
             ? INLINED
             : StringUtils.isBlank(paramName)
             ? INDEXED
             : NAMED
             ;
    }

    @Override
    public final ParamMode getParamMode() {
        return ParamMode.IN;
    }

    @Override
    public void accept(Context<?> ctx) {}

    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final T $value() {
        return value;
    }

    @Override
    public final boolean $inline() {
        return inline;
    }

    @Override
    public /* non-final */ Param<T> $value(T newValue) {
        // TODO [#12425] Only Val implements this, so far.
        throw new NotYetImplementedException();
    }

    @Override
    public /* non-final */ Param<T> $inline(boolean inline) {
        // TODO [#12425] Only Val implements this, so far.
        throw new NotYetImplementedException();
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (that instanceof Param<?> p) {
            Object thatValue = p.getValue();

            if (value == null)
                return thatValue == null;
            else if (value instanceof byte[] && thatValue instanceof byte[])
                return Arrays.equals((byte[]) value, (byte[]) thatValue);
            else if (value instanceof Object[] && thatValue instanceof Object[])
                return Arrays.equals((Object[]) value, (Object[]) thatValue);
            else
                return value.equals(thatValue);
        }
        else
            return false;
    }

    @Override
    public int hashCode() {
        return value == null
            ? 0
            : value instanceof byte[] a
            ? Arrays.hashCode(a)
            : value instanceof Object[] a
            ? Arrays.hashCode(a)
            : value.hashCode();
    }
}
