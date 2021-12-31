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

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.Internal.*;
import static org.jooq.impl.Keywords.*;
import static org.jooq.impl.Names.*;
import static org.jooq.impl.SQLDataType.*;
import static org.jooq.impl.Tools.*;
import static org.jooq.impl.Tools.BooleanDataKey.*;
import static org.jooq.impl.Tools.DataExtendedKey.*;
import static org.jooq.impl.Tools.DataKey.*;
import static org.jooq.SQLDialect.*;

import org.jooq.*;
import org.jooq.Function1;
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>XMLFOREST</code> statement.
 */
@SuppressWarnings({ "rawtypes", "unused" })
final class XMLForest
extends
    AbstractField<XML>
implements
    QOM.XMLForest
{

    final QueryPartListView<? extends Field<?>> fields;

    XMLForest(
        Collection<? extends Field<?>> fields
    ) {
        super(
            N_XMLFOREST,
            allNotNull(XML)
        );

        this.fields = new QueryPartList<>(fields);
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {





        ctx.data(DATA_AS_REQUIRED, true,
            c -> c.visit(N_XMLFOREST).sql('(')
                  .declareFields(true, x -> x.visit(new SelectFieldList<>(fields)))
                  .sql(')')
        );
    }














    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final UnmodifiableList<? extends Field<?>> $fields() {
        return QOM.unmodifiable(fields);
    }

    @Override
    public final QOM.XMLForest $fields(UnmodifiableList<? extends Field<?>> newValue) {
        return constructor().apply(newValue);
    }

    public final Function1<? super UnmodifiableList<? extends Field<?>>, ? extends QOM.XMLForest> constructor() {
        return (a1) -> new XMLForest((Collection<? extends Field<?>>) a1);
    }






















    // -------------------------------------------------------------------------
    // XXX: The Object API
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object that) {
        if (that instanceof QOM.XMLForest) { QOM.XMLForest o = (QOM.XMLForest) that;
            return
                StringUtils.equals($fields(), o.$fields())
            ;
        }
        else
            return super.equals(that);
    }
}
