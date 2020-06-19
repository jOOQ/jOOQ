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

import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.schema;
import static org.jooq.impl.Keywords.K_ADD;
import static org.jooq.impl.Keywords.K_ALTER;
import static org.jooq.impl.Keywords.K_RENAME;
import static org.jooq.impl.Keywords.K_RENAME_TO;
import static org.jooq.impl.Keywords.K_SCHEMA;
import static org.jooq.impl.Keywords.K_SET;
import static org.jooq.impl.Keywords.K_TO;
import static org.jooq.impl.Keywords.K_TYPE;
import static org.jooq.impl.Keywords.K_VALUE;

import org.jooq.AlterTypeFinalStep;
import org.jooq.AlterTypeRenameValueToStep;
import org.jooq.AlterTypeStep;
import org.jooq.Configuration;
import org.jooq.Context;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.conf.ParamType;

/**
 * @author Lukas Eder
 */
final class AlterTypeImpl extends AbstractRowCountQuery implements

    // Cascading interface implementations for CREATE TYPE behaviour
    AlterTypeStep,
    AlterTypeRenameValueToStep,
    AlterTypeFinalStep {

    private static final long serialVersionUID = -5018375056147329888L;
    private final Name        type;
    private Name              renameTo;
    private Schema            setSchema;
    private Field<String>     addValue;
    private Field<String>     renameValueFrom;
    private Field<String>     renameValueTo;

    AlterTypeImpl(Configuration configuration, Name type) {
        super(configuration);

        this.type = type;
    }

    // ------------------------------------------------------------------------
    // XXX: DSL API
    // ------------------------------------------------------------------------

    @Override
    public final AlterTypeImpl renameTo(String newName) {
        return renameTo(DSL.name(newName));
    }

    @Override
    public final AlterTypeImpl renameTo(Name newName) {
        this.renameTo = newName;
        return this;
    }

    @Override
    public final AlterTypeImpl setSchema(String newSchema) {
        return setSchema(name(newSchema));
    }

    @Override
    public final AlterTypeImpl setSchema(Name newSchema) {
        return setSchema(schema(newSchema));
    }

    @Override
    public final AlterTypeImpl setSchema(Schema newSchema) {
        this.setSchema = newSchema;
        return this;
    }

    @Override
    public final AlterTypeImpl addValue(String newEnumValue) {
        return addValue(Tools.field(newEnumValue));
    }

    @Override
    public final AlterTypeImpl addValue(Field<String> newEnumValue) {
        this.addValue = newEnumValue;
        return this;
    }

    @Override
    public final AlterTypeImpl renameValue(String existingEnumValue) {
        return renameValue(Tools.field(existingEnumValue));
    }

    @Override
    public final AlterTypeImpl renameValue(Field<String> existingEnumValue) {
        this.renameValueFrom = existingEnumValue;
        return this;
    }

    @Override
    public final AlterTypeImpl to(String newEnumValue) {
        return to(Tools.field(newEnumValue));
    }

    @Override
    public final AlterTypeImpl to(Field<String> newEnumValue) {
        this.renameValueTo = newEnumValue;
        return this;
    }

    // ------------------------------------------------------------------------
    // XXX: QueryPart API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ParamType previous = ctx.paramType();
        boolean qualified = ctx.qualify();

        ctx.visit(K_ALTER).sql(' ').visit(K_TYPE).sql(' ')
           .visit(type).sql(' ');

        if (renameTo != null)
            ctx.visit(K_RENAME_TO).sql(' ').qualify(false).visit(renameTo).qualify(qualified);
        else if (setSchema != null)
            ctx.visit(K_SET).sql(' ').visit(K_SCHEMA).sql(' ').visit(setSchema);
        else if (addValue != null)
            ctx.visit(K_ADD).sql(' ').visit(K_VALUE).sql(' ').visit(addValue);
        else if (renameValueFrom != null)
            ctx.visit(K_RENAME).sql(' ').visit(K_VALUE).sql(' ').visit(renameValueFrom).sql(' ').visit(K_TO).sql(' ').visit(renameValueTo);

        ctx.paramType(previous);
    }
}
