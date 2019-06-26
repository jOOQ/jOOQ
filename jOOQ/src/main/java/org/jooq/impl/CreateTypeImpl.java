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

import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.Keywords.K_CREATE;
import static org.jooq.impl.Keywords.K_ENUM;
import static org.jooq.impl.Keywords.K_TYPE;
import static org.jooq.impl.SQLDataType.VARCHAR;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.CreateTypeFinalStep;
import org.jooq.CreateTypeStep;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class CreateTypeImpl extends AbstractRowCountQuery implements

    // Cascading interface implementations for CREATE TYPE behaviour
    CreateTypeStep,
    CreateTypeFinalStep {

    private static final long serialVersionUID = -5018375056147329888L;
    private final Name        type;
    private QueryPartList<?>  values;

    CreateTypeImpl(Configuration configuration, Name type) {
        super(configuration);

        this.type = type;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final CreateTypeFinalStep asEnum() {
        return asEnum(Collections.emptyList());
    }

    @Override
    public final CreateTypeFinalStep asEnum(String... v) {
        return asEnum(Tools.inline(v));
    }


    @SafeVarargs

    @Override
    public final CreateTypeFinalStep asEnum(Field<String>... v) {
        return asEnum(Arrays.asList(v));
    }

    @Override
    public final CreateTypeFinalStep asEnum(Collection<?> v) {
        values = new QueryPartList<Field<String>>(Tools.fields(v, VARCHAR));
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ParamType previous = ctx.paramType();

        ctx.visit(K_CREATE).sql(' ').visit(K_TYPE).sql(' ')
           .visit(type).sql(' ')
           .visit(K_AS).sql(' ').visit(K_ENUM).sql(" (")
           .paramType(ParamType.INLINED)
           .visit(values)
           .sql(')')
           .paramType(previous);
    }
}
