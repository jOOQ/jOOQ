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
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.impl.QOM.*;
import org.jooq.tools.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * The <code>CREATE TYPE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unused" })
final class CreateTypeImpl
extends
    AbstractDDLQuery
implements
    QOM.CreateType,
    CreateTypeStep,
    CreateTypeFinalStep
{

    final Type<?>                                    type;
          QueryPartListView<? extends Field<String>> values;
          QueryPartListView<? extends Field<?>>      attributes;

    CreateTypeImpl(
        Configuration configuration,
        Type<?> type
    ) {
        this(
            configuration,
            type,
            null,
            null
        );
    }

    CreateTypeImpl(
        Configuration configuration,
        Type<?> type,
        Collection<? extends Field<String>> values,
        Collection<? extends Field<?>> attributes
    ) {
        super(configuration);

        this.type = type;
        this.values = new QueryPartList<>(values);
        this.attributes = new QueryPartList<>(attributes);
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final CreateTypeImpl asEnum(String... values) {
        return asEnum(Tools.fields(values));
    }

    @Override
    public final CreateTypeImpl asEnum(Field<String>... values) {
        return asEnum(Arrays.asList(values));
    }

    @Override
    public final CreateTypeImpl asEnum(Collection<? extends Field<String>> values) {
        this.values = new QueryPartList<>(values);
        return this;
    }

    @Override
    public final CreateTypeImpl asEnum() {
        return this;
    }

    @Override
    public final CreateTypeImpl as(Field<?>... attributes) {
        return as(Arrays.asList(attributes));
    }

    @Override
    public final CreateTypeImpl as(Collection<? extends Field<?>> attributes) {
        this.attributes = new QueryPartList<>(attributes);
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(K_CREATE).sql(' ');






        ctx.visit(K_TYPE).sql(' ')
           .visit(type).sql(' ');




        ctx.visit(K_AS).sql(' ');

        if (!values.isEmpty()) {
            ctx.visit(K_ENUM).sql(" (")
               .visit(values, ParamType.INLINED)
               .sql(')');
        }
        else {





            ctx.sql('(').visit(
                new QueryPartList<Field<?>>(attributes).map(f -> declare(f)),
                ParamType.INLINED
            ).sql(')');
        }
    }

    private static final <T> Field<T> declare(Field<T> f) {
        return CustomField.of(f.getUnqualifiedName(), f.getDataType(), c -> {
            c.visit(f.getUnqualifiedName());
            c.sql(' ');
            Tools.toSQLDDLTypeDeclarationForAddition(c, f.getDataType());
        });
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Type<?> $type() {
        return type;
    }

    @Override
    public final UnmodifiableList<? extends Field<String>> $values() {
        return QOM.unmodifiable(values);
    }

    @Override
    public final UnmodifiableList<? extends Field<?>> $attributes() {
        return QOM.unmodifiable(attributes);
    }

    @Override
    public final QOM.CreateType $type(Type<?> newValue) {
        return $constructor().apply(newValue, $values(), $attributes());
    }

    @Override
    public final QOM.CreateType $values(Collection<? extends Field<String>> newValue) {
        return $constructor().apply($type(), newValue, $attributes());
    }

    @Override
    public final QOM.CreateType $attributes(Collection<? extends Field<?>> newValue) {
        return $constructor().apply($type(), $values(), newValue);
    }

    public final Function3<? super Type<?>, ? super Collection<? extends Field<String>>, ? super Collection<? extends Field<?>>, ? extends QOM.CreateType> $constructor() {
        return (a1, a2, a3) -> new CreateTypeImpl(configuration(), a1, (Collection<? extends Field<String>>) a2, (Collection<? extends Field<?>>) a3);
    }

























}
