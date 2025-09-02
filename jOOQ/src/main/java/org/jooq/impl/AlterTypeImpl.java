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
 * The <code>ALTER TYPE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class AlterTypeImpl
extends
    AbstractDDLQuery
implements
    QOM.AlterType,
    AlterTypeStep,
    AlterTypeRenameValueToStep,
    AlterTypeFinalStep
{

    final Name          type;
    final boolean       ifExists;
          Name          renameTo;
          Schema        setSchema;
          Field<String> addValue;
          Field<String> renameValue;
          Field<String> renameValueTo;

    AlterTypeImpl(
        Configuration configuration,
        Name type,
        boolean ifExists
    ) {
        this(
            configuration,
            type,
            ifExists,
            null,
            null,
            null,
            null,
            null
        );
    }

    AlterTypeImpl(
        Configuration configuration,
        Name type,
        boolean ifExists,
        Name renameTo,
        Schema setSchema,
        Field<String> addValue,
        Field<String> renameValue,
        Field<String> renameValueTo
    ) {
        super(configuration);

        this.type = type;
        this.ifExists = ifExists;
        this.renameTo = renameTo;
        this.setSchema = setSchema;
        this.addValue = addValue;
        this.renameValue = renameValue;
        this.renameValueTo = renameValueTo;
    }

    // -------------------------------------------------------------------------
    // XXX: DSL API
    // -------------------------------------------------------------------------

    @Override
    public final AlterTypeImpl renameTo(String renameTo) {
        return renameTo(DSL.name(renameTo));
    }

    @Override
    public final AlterTypeImpl renameTo(Name renameTo) {
        this.renameTo = renameTo;
        return this;
    }

    @Override
    public final AlterTypeImpl setSchema(String setSchema) {
        return setSchema(DSL.schema(DSL.name(setSchema)));
    }

    @Override
    public final AlterTypeImpl setSchema(Name setSchema) {
        return setSchema(DSL.schema(setSchema));
    }

    @Override
    public final AlterTypeImpl setSchema(Schema setSchema) {
        this.setSchema = setSchema;
        return this;
    }

    @Override
    public final AlterTypeImpl addValue(String addValue) {
        return addValue(Tools.field(addValue));
    }

    @Override
    public final AlterTypeImpl addValue(Field<String> addValue) {
        this.addValue = addValue;
        return this;
    }

    @Override
    public final AlterTypeImpl renameValue(String renameValue) {
        return renameValue(Tools.field(renameValue));
    }

    @Override
    public final AlterTypeImpl renameValue(Field<String> renameValue) {
        this.renameValue = renameValue;
        return this;
    }

    @Override
    public final AlterTypeImpl to(String renameValueTo) {
        return to(Tools.field(renameValueTo));
    }

    @Override
    public final AlterTypeImpl to(Field<String> renameValueTo) {
        this.renameValueTo = renameValueTo;
        return this;
    }

    // -------------------------------------------------------------------------
    // XXX: QueryPart API
    // -------------------------------------------------------------------------



    private static final Set<SQLDialect> NO_SUPPORT_IF_EXISTS = SQLDialect.supportedUntil(POSTGRES, YUGABYTEDB);

    private final boolean supportsIfExists(Context<?> ctx) {
        return !NO_SUPPORT_IF_EXISTS.contains(ctx.dialect());
    }

    @Override
    public final void accept(Context<?> ctx) {
        if (ifExists && !supportsIfExists(ctx))
            tryCatch(ctx, DDLStatementType.ALTER_TYPE, c -> accept0(c));
        else
            accept0(ctx);
    }

    private final void accept0(Context<?> ctx) {
        ctx.visit(K_ALTER).sql(' ').visit(K_TYPE).sql(' ')
           .visit(type).sql(' ');

        if (renameTo != null)
            ctx.visit(K_RENAME_TO).sql(' ').qualify(false, c -> c.visit(renameTo));
        else if (setSchema != null)
            ctx.visit(K_SET).sql(' ').visit(K_SCHEMA).sql(' ').visit(setSchema);
        else if (addValue != null)
            ctx.visit(K_ADD).sql(' ').visit(K_VALUE).sql(' ').visit(addValue);
        else if (renameValue != null)
            ctx.visit(K_RENAME).sql(' ').visit(K_VALUE).sql(' ').visit(renameValue).sql(' ').visit(K_TO).sql(' ').visit(renameValueTo);
    }



    // -------------------------------------------------------------------------
    // XXX: Query Object Model
    // -------------------------------------------------------------------------

    @Override
    public final Name $type() {
        return type;
    }

    @Override
    public final boolean $ifExists() {
        return ifExists;
    }

    @Override
    public final Name $renameTo() {
        return renameTo;
    }

    @Override
    public final Schema $setSchema() {
        return setSchema;
    }

    @Override
    public final Field<String> $addValue() {
        return addValue;
    }

    @Override
    public final Field<String> $renameValue() {
        return renameValue;
    }

    @Override
    public final Field<String> $renameValueTo() {
        return renameValueTo;
    }

    @Override
    public final QOM.AlterType $type(Name newValue) {
        return $constructor().apply(newValue, $ifExists(), $renameTo(), $setSchema(), $addValue(), $renameValue(), $renameValueTo());
    }

    @Override
    public final QOM.AlterType $ifExists(boolean newValue) {
        return $constructor().apply($type(), newValue, $renameTo(), $setSchema(), $addValue(), $renameValue(), $renameValueTo());
    }

    @Override
    public final QOM.AlterType $renameTo(Name newValue) {
        return $constructor().apply($type(), $ifExists(), newValue, $setSchema(), $addValue(), $renameValue(), $renameValueTo());
    }

    @Override
    public final QOM.AlterType $setSchema(Schema newValue) {
        return $constructor().apply($type(), $ifExists(), $renameTo(), newValue, $addValue(), $renameValue(), $renameValueTo());
    }

    @Override
    public final QOM.AlterType $addValue(Field<String> newValue) {
        return $constructor().apply($type(), $ifExists(), $renameTo(), $setSchema(), newValue, $renameValue(), $renameValueTo());
    }

    @Override
    public final QOM.AlterType $renameValue(Field<String> newValue) {
        return $constructor().apply($type(), $ifExists(), $renameTo(), $setSchema(), $addValue(), newValue, $renameValueTo());
    }

    @Override
    public final QOM.AlterType $renameValueTo(Field<String> newValue) {
        return $constructor().apply($type(), $ifExists(), $renameTo(), $setSchema(), $addValue(), $renameValue(), newValue);
    }

    public final Function7<? super Name, ? super Boolean, ? super Name, ? super Schema, ? super Field<String>, ? super Field<String>, ? super Field<String>, ? extends QOM.AlterType> $constructor() {
        return (a1, a2, a3, a4, a5, a6, a7) -> new AlterTypeImpl(configuration(), a1, a2, a3, a4, a5, a6, a7);
    }
































}
