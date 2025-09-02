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
 * Apache-2.0 and offer limited warranties, support, maintenance, and commercial
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
import org.jooq.conf.ParamType;
import org.jooq.tools.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;



/**
 * The <code>XMLSERIALIZE</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class XMLSerialize<T>
extends
    AbstractField<T>
implements
    QOM.XMLSerialize<T>
{

    final boolean     content;
    final Field<XML>  value;
    final DataType<T> type;

    XMLSerialize(
        boolean content,
        Field<XML> value,
        DataType<T> type
    ) {
        super(
            N_XMLSERIALIZE,
            type
        );

        this.content = content;
        this.value = nullSafeNotNull(value, XML);
        this.type = type;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {







        ctx.visit(N_XMLSERIALIZE).sql('(');

        if (content)
            ctx.visit(K_CONTENT).sql(' ');




        else
            ctx.visit(K_DOCUMENT).sql(' ');

        ctx.visit(value).sql(' ').visit(K_AS).sql(' ')
           .sql(type.getCastTypeName(ctx.configuration()))
           .sql(')');
    }

















    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Boolean $arg1() {
        return content;
    }

    @Override
    public final Field<XML> $arg2() {
        return value;
    }

    @Override
    public final DataType<T> $arg3() {
        return type;
    }

    @Override
    public final QOM.XMLSerialize<T> $arg1(Boolean newValue) {
        return $constructor().apply(newValue, $arg2(), $arg3());
    }

    @Override
    public final QOM.XMLSerialize<T> $arg2(Field<XML> newValue) {
        return $constructor().apply($arg1(), newValue, $arg3());
    }

    @Override
    public final QOM.XMLSerialize<T> $arg3(DataType<T> newValue) {
        return $constructor().apply($arg1(), $arg2(), newValue);
    }

    @Override
    public final Function3<? super Boolean, ? super Field<XML>, ? super DataType<T>, ? extends QOM.XMLSerialize<T>> $constructor() {
        return (a1, a2, a3) -> new XMLSerialize<>(a1, a2, a3);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.XMLSerialize<?> o) {
            return
                $content() == o.$content() &&
                Objects.equals($value(), o.$value()) &&
                Objects.equals($type(), o.$type())
            ;
        }
        else
            return super.equals(that);
    }
}
