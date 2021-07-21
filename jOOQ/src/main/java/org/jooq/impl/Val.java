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

import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.joining;
// ...
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.ParamType.NAMED_OR_INLINED;
import static org.jooq.impl.DSL.sql;
import static org.jooq.impl.QueryPartListView.wrap;
import static org.jooq.impl.RowField.acceptMultisetContent;
import static org.jooq.impl.SQLDataType.OTHER;
import static org.jooq.impl.SQLDataType.VARCHAR;
import static org.jooq.impl.Tools.embeddedFields;
import static org.jooq.impl.Tools.findAny;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.row0;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_LIST_ALREADY_INDENTED;
import static org.jooq.impl.Tools.BooleanDataKey.DATA_MULTISET_CONTENT;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONB;
// ...
import org.jooq.RenderContext;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.types.DayToSecond;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
import org.jooq.types.YearToMonth;

/**
 * @author Lukas Eder
 */
final class Val<T> extends AbstractParam<T> {

    private static final JooqLogger                          log              = JooqLogger.getLogger(Val.class);
    private static final ConcurrentHashMap<Class<?>, Object> legacyWarnings   = new ConcurrentHashMap<>();

    Val(T value, DataType<T> type) {
        super(value, type(value, type));
    }

    Val(T value, DataType<T> type, String paramName) {
        super(value, type(value, type), paramName);
    }

    private static final <T> DataType<T> type(T value, DataType<T> type) {
        return value == null ? type.null_() : type.notNull();
    }

    // ------------------------------------------------------------------------
    // XXX: Field API
    // ------------------------------------------------------------------------

    /**
     * [#10438] Convert this bind value to a new type.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    final <U> Field<U> convertTo(DataType<U> type) {

        // [#10438] A user defined data type could was not provided explicitly,
        //          when wrapping a bind value in DSL::val or DSL::inline
        if (getDataType() instanceof DataTypeProxy) {

            // [#9492] Maintain legacy static type registry behaviour for now
            if (((DataTypeProxy<?>) getDataType()).type() instanceof LegacyConvertedDataType && type == SQLDataType.OTHER) {
                type = (DataType) ((DataTypeProxy<?>) getDataType()).type();

                if (legacyWarnings.size() < 8 && legacyWarnings.put(type.getType(), "") == null)
                    log.warn("Deprecation", "User-defined, converted data type " + type.getType() + " was registered statically, which will be unsupported in the future, see https://github.com/jOOQ/jOOQ/issues/9492. Please use explicit data types in generated code, or e.g. with DSL.val(Object, DataType), or DSL.inline(Object, DataType).", new SQLWarning("Static type registry usage"));
            }

            return convertTo0(type);
        }

        // [#10438] A data type conversion between built in data types was made
        else if (type instanceof ConvertedDataType)
            return convertTo0(type);

        // [#11061] Infer bind value data types if they could not be defined eagerly, mostly from the parser.
        //          Cannot use convertTo0() here as long as Param.setValue() is possible (mutable Params)
        else if (OTHER.equals(getDataType()))
            return new ConvertedVal<>(this, type);
        else
            return (Val) this;
    }

    final Val<T> copy(Object newValue) {
        Val<T> w = new Val<>(getDataType().convert(newValue), getDataType(), getParamName());
        w.setInline0(isInline());
        return w;
    }

    final <U> Val<U> convertTo0(DataType<U> type) {
        Val<U> w = new Val<>(type.convert(getValue()), type, getParamName());
        w.setInline0(isInline());
        return w;
    }

    @Override
    public void accept(Context<?> ctx) {
        if (getDataType().isEmbeddable()) {

            // [#12237] If a RowField is nested somewhere in MULTISET, we must apply
            //          the MULTISET emulation as well, here
            if (TRUE.equals(ctx.data(DATA_MULTISET_CONTENT)))
                acceptMultisetContent(ctx, row0(embeddedFields(this)), this, this::acceptDefaultEmbeddable);
            else
                acceptDefaultEmbeddable(ctx);
        }
        else if (ctx instanceof RenderContext) {
            ParamType paramType = ctx.paramType();

            if (isInline(ctx))
                ctx.paramType(INLINED);








            try {
                getBinding().sql(new DefaultBindingSQLContext<>(ctx.configuration(), ctx.data(), (RenderContext) ctx, value, getBindVariable(ctx)));
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while generating SQL for Binding", e);
            }

            ctx.paramType(paramType);
        }

        else {

            // [#1302] Bind value only if it was not explicitly forced to be inlined
            if (!isInline(ctx))
                ctx.bindValue(value, this);
        }
    }

    private void acceptDefaultEmbeddable(Context<?> ctx) {
        ctx.data(DATA_LIST_ALREADY_INDENTED, true, c -> c.visit(wrap(embeddedFields(this))));
    }


























































































































    /**
     * Get a bind variable, depending on value of
     * {@link RenderContext#namedParams()}
     */
    final String getBindVariable(Context<?> ctx) {
        if (ctx.paramType() == NAMED || ctx.paramType() == NAMED_OR_INLINED) {
            int index = ctx.nextIndex();
            String prefix = defaultIfNull(ctx.settings().getRenderNamedParamPrefix(), ":");

            if (StringUtils.isBlank(getParamName()))
                return prefix + index;
            else
                return prefix + getParamName();
        }
        else {
            return "?";
        }
    }
}
