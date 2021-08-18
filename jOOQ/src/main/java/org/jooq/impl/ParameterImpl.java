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

// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.impl.Keywords.K_IN;
import static org.jooq.impl.Keywords.K_INOUT;
import static org.jooq.impl.Keywords.K_OUT;

import java.util.Set;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.ParamMode;
import org.jooq.Parameter;
// ...
import org.jooq.Record1;
import org.jooq.SQLDialect;
import org.jooq.Select;
import org.jooq.Statement;
// ...

/**
 * A common base class for stored procedure parameters
 *
 * @author Lukas Eder
 */
final class ParameterImpl<T> extends AbstractField<T> implements Parameter<T> {

    private final ParamMode   paramMode;
    private final boolean     isDefaulted;
    private final boolean     isUnnamed;








    ParameterImpl(ParamMode paramMode, Name name, DataType<T> type) {
        this(paramMode, name, type, type.defaulted(), name == null || name.empty());
    }

    /**
     * @deprecated - [#11327] - 3.15.0 - Do not reuse this constructor
     */
    @Deprecated
    ParameterImpl(ParamMode paramMode, Name name, DataType<T> type, boolean isDefaulted, boolean isUnnamed) {
        super(name, type);

        this.paramMode = paramMode;
        this.isDefaulted = isDefaulted;
        this.isUnnamed = isUnnamed;




    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------










    @Override
    public final void accept(Context<?> ctx) {








































        ctx.visit(getUnqualifiedName());
    }

    // -------------------------------------------------------------------------
    // XXX: Parameter API
    // -------------------------------------------------------------------------

    @Override
    public final ParamMode getParamMode() {
        return paramMode;
    }

    @Override
    public final boolean isDefaulted() {
        return isDefaulted;
    }

    @Override
    public final boolean isUnnamed() {
        return isUnnamed;
    }


































}
