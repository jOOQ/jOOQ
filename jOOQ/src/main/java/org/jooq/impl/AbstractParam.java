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
 */
package org.jooq.impl;

import static org.jooq.Clause.FIELD;
import static org.jooq.Clause.FIELD_VALUE;
import static org.jooq.conf.ParamType.INDEXED;
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.conf.ParamType.NAMED;
import static org.jooq.conf.ParamType.NAMED_OR_INLINED;

// ...
import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Param;
import org.jooq.ParamMode;
import org.jooq.UDTRecord;
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

/**
 * A base implementation for {@link Param}
 *
 * @author Lukas Eder
 */
abstract class AbstractParam<T> extends AbstractField<T> implements Param<T> {

    /**
     * Generated UID
     */
    private static final long     serialVersionUID = 1311856649676227970L;
    private static final Clause[] CLAUSES          = { FIELD, FIELD_VALUE };

    private final String      paramName;
    T                         value;
    private boolean           inline;

    AbstractParam(T value, DataType<T> type) {
        this(value, type, null);
    }

    AbstractParam(T value, DataType<T> type, String paramName) {
        super(name(value, paramName), type);

        this.paramName = paramName;
        this.value = value;
    }

    /**
     * A utility method that generates a field name.
     * <p>
     * <ul>
     * <li>If <code>paramName != null</code>, take <code>paramName</code></li>
     * <li>Otherwise, take the string value of <code>value</code></li>
     * </ul>
     */
    private static String name(Object value, String paramName) {
        return paramName != null
             ? paramName

             // [#3707] Protect value.toString call for certain jOOQ types.
             : value instanceof UDTRecord
             ? ((UDTRecord<?>) value).getUDT().getName()





             : String.valueOf(value);
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

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
    public final void setValue(T value) {
        setConverted(value);
    }

    @Override
    public final void setConverted(Object value) {
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
    public final void setInline(boolean inline) {
        this.inline = inline;
    }

    @Override
    public final boolean isInline() {
        return inline;
    }

    final boolean isInline(Context<?> context) {
        return isInline()
            || (context.paramType() == INLINED)
            || (context.paramType() == NAMED_OR_INLINED && StringUtils.isBlank(paramName));
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
}
