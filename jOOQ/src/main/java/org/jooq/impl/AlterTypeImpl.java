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
import org.jooq.Record;
import org.jooq.conf.*;
import org.jooq.impl.*;
import org.jooq.tools.*;

import java.util.*;


/**
 * The <code>ALTER TYPE</code> statement.
 */
@SuppressWarnings({ "hiding", "rawtypes", "unchecked", "unused" })
final class AlterTypeImpl
extends
    AbstractDDLQuery
implements
    AlterTypeStep,
    AlterTypeRenameValueToStep,
    AlterTypeFinalStep
{

    private final Name          type;
    private       Name          renameTo;
    private       Schema        setSchema;
    private       Field<String> addValue;
    private       Field<String> renameValue;
    private       Field<String> renameValueTo;

    AlterTypeImpl(
        Configuration configuration,
        Name type
    ) {
        this(
            configuration,
            type,
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
        Name renameTo,
        Schema setSchema,
        Field<String> addValue,
        Field<String> renameValue,
        Field<String> renameValueTo
    ) {
        super(configuration);

        this.type = type;
        this.renameTo = renameTo;
        this.setSchema = setSchema;
        this.addValue = addValue;
        this.renameValue = renameValue;
        this.renameValueTo = renameValueTo;
    }

    final Name          $type()          { return type; }
    final Name          $renameTo()      { return renameTo; }
    final Schema        $setSchema()     { return setSchema; }
    final Field<String> $addValue()      { return addValue; }
    final Field<String> $renameValue()   { return renameValue; }
    final Field<String> $renameValueTo() { return renameValueTo; }

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



    @Override
    public final void accept(Context<?> ctx) {
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


}
