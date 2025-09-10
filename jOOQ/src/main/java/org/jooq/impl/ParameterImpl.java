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

// ...
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.impl.Keywords.K_DEFAULT;
import static org.jooq.impl.Keywords.K_IN;
import static org.jooq.impl.Keywords.K_INOUT;
import static org.jooq.impl.Keywords.K_OUT;
import static org.jooq.impl.SQLDataType.CLOB;

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
import org.jooq.impl.QOM.UEmpty;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A common base class for stored procedure parameters
 *
 * @author Lukas Eder
 */
final class ParameterImpl<T> extends AbstractField<T> implements Parameter<T>, UEmpty, TypedReference<T> {

    private final ParamMode   paramMode;
    private final boolean     isUnnamed;








    ParameterImpl(ParamMode paramMode, Name name, DataType<T> type) {
        this(paramMode, name, type, name == null || name.empty());
    }

    /**
     * @deprecated - [#11327] - 3.15.0 - Do not reuse this constructor
     */
    @Deprecated
    ParameterImpl(ParamMode paramMode, Name name, DataType<T> type, boolean isUnnamed) {
        super(name, type);

        this.paramMode = paramMode;
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
        return getDataType().defaulted();
    }

    @Override
    public final boolean isUnnamed() {
        return isUnnamed;
    }


































}
