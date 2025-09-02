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
 * The <code>BINARY CONCAT</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
final class BinaryConcat
extends
    AbstractField<byte[]>
implements
    QOM.BinaryConcat
{

    final Field<byte[]> bytes1;
    final Field<byte[]> bytes2;

    BinaryConcat(
        Field<byte[]> bytes1,
        Field<byte[]> bytes2
    ) {
        super(
            N_BINARY_CONCAT,
            allNotNull(VARBINARY, bytes1, bytes2)
        );

        this.bytes1 = nullSafeNotNull(bytes1, VARBINARY);
        this.bytes2 = nullSafeNotNull(bytes2, VARBINARY);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        switch (ctx.family()) {






            default:
                ctx.sql('(').visit(bytes1).sql(" || ").visit(bytes2).sql(')');
                break;
        }
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Field<byte[]> $arg1() {
        return bytes1;
    }

    @Override
    public final Field<byte[]> $arg2() {
        return bytes2;
    }

    @Override
    public final QOM.BinaryConcat $arg1(Field<byte[]> newValue) {
        return $constructor().apply(newValue, $arg2());
    }

    @Override
    public final QOM.BinaryConcat $arg2(Field<byte[]> newValue) {
        return $constructor().apply($arg1(), newValue);
    }

    @Override
    public final Function2<? super Field<byte[]>, ? super Field<byte[]>, ? extends QOM.BinaryConcat> $constructor() {
        return (a1, a2) -> new BinaryConcat(a1, a2);
    }

    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.BinaryConcat o) {
            return
                Objects.equals($bytes1(), o.$bytes1()) &&
                Objects.equals($bytes2(), o.$bytes2())
            ;
        }
        else
            return super.equals(that);
    }
}
