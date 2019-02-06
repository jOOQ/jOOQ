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
import static org.jooq.conf.ParamType.INLINED;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.Keywords.K_DECLARE;
import static org.jooq.impl.Keywords.K_DEFAULT;

import org.jooq.Context;
// ...
import org.jooq.Field;
// ...
import org.jooq.Record1;
import org.jooq.Select;
import org.jooq.Statement;
// ...
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
@Pro
final class DeclarationImpl<T> extends AbstractStatement implements Declaration<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4976947749196983386L;
    final Variable<T>         variable;
    final Field<T>            value;

    DeclarationImpl(Variable<T> variable, Field<T> value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public final Statement set(T v) {
        return set(Tools.field(v, variable.getDataType()));
    }

    @Override
    public final Statement set(Field<T> v) {
        return new DeclarationImpl<T>(variable, v);
    }

    @Override
    public final Statement set(Select<? extends Record1<T>> v) {
        return set(field(v));
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {
            case H2:
                ctx.sql(BlockImpl.variableType(variable))
                   .sql(' ')
                   .sql(variable.getName());

                if (value instanceof ScalarSubquery) {
                    ctx.sql(" = null;")
                       .formatSeparator();

                    ctx.visit(variable.set(value));
                }
                else if (value != null)
                    ctx.sql(" = ").visit(value);
                else
                    ctx.sql(" = null");

                break;

            case POSTGRES:
            case ORACLE:
                ctx.visit(variable).sql(' ');
                Tools.toSQLDDLTypeDeclaration(ctx, variable.getDataType());

                if (value != null)
                    ctx.sql(" := ").visit(value);

                break;

            case DB2:
            case MARIADB:
            case MYSQL:
            case SQLSERVER:
            default:
                ctx.visit(K_DECLARE).sql(' ').visit(variable).sql(' ');
                Tools.toSQLDDLTypeDeclaration(ctx, variable.getDataType());

                ParamType previous = ctx.paramType();
                if (value != null)
                    if (ctx.family() == SQLSERVER)
                        ctx.sql(" = ").visit(value);
                    else
                        ctx.sql(' ').visit(K_DEFAULT).sql(' ').paramType(INLINED).visit(value).paramType(previous);

                break;
        }
    }
}

/* [/pro] */