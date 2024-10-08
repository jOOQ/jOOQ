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
import static org.jooq.impl.DefaultExecuteContext.localExecuteContext;
import static org.jooq.impl.Tools.fieldsArray;
import static org.jooq.impl.Tools.getMappedUDTName;
import static org.jooq.impl.Tools.row0;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import org.jooq.Converter;
import org.jooq.ExecuteContext;
import org.jooq.Field;
import org.jooq.QualifiedRecord;
import org.jooq.RecordQualifier;
import org.jooq.Row;

/**
 * @author Lukas Eder
 */
abstract class AbstractQualifiedRecord<R extends QualifiedRecord<R>> extends AbstractRecord implements QualifiedRecord<R> {

    private final RecordQualifier<R> qualifier;

    public AbstractQualifiedRecord(RecordQualifier<R> qualifier) {
        super((AbstractRow) qualifier.fieldsRow());

        this.qualifier = qualifier;
    }

    // -------------------------------------------------------------------------
    // XXX: QualifiedRecord API
    // -------------------------------------------------------------------------

    @Override
    public final RecordQualifier<R> getQualifier() {
        return qualifier;
    }

    // [#12180] scalac 3 requires overriding this method to work around an interoperability regression
    @SuppressWarnings("unchecked")
    @Override
    public /* non-final */ <T> R with(Field<T> field, T value) {
        return (R) super.with(field, value);
    }

    // [#12180] scalac 3 requires overriding this method to work around an interoperability regression
    @SuppressWarnings("unchecked")
    @Override
    public /* non-final */ <T, U> R with(Field<T> field, U value, Converter<? extends T, ? super U> converter) {
        return (R) super.with(field, value, converter);
    }

    /*
     * Subclasses may override this method
     */
    @Override
    public Row fieldsRow() {
        return fields;
    }

    /*
     * Subclasses may override this method
     */
    @Override
    public Row valuesRow() {
        return row0(fieldsArray(intoArray(), fields.fields.fields()));
    }

    // -------------------------------------------------------------------------
    // XXX: SQLData API
    // -------------------------------------------------------------------------

    @Override
    public final String getSQLTypeName() throws SQLException {
        ExecuteContext ctx = localExecuteContext();

        // [#1693] This needs to return the fully qualified SQL type name, in
        // case the connected user is not the owner of the UDT
        String result = getMappedUDTName(ctx, this);










        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void readSQL(SQLInput stream, String typeName) throws SQLException {
        ExecuteContext ctx = localExecuteContext();
        Field<?>[] f = getQualifier().fields();

        for (int i = 0; i < f.length; i++) {
            Field field = f[i];
            DefaultBindingGetSQLInputContext out = new DefaultBindingGetSQLInputContext(ctx, stream);
            field.getBinding().get(out);
            set(i, field, out.value());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void writeSQL(SQLOutput stream) throws SQLException {
        ExecuteContext ctx = localExecuteContext();
        Field<?>[] f = getQualifier().fields();

        for (int i = 0; i < f.length; i++)
            f[i].getBinding().set(new DefaultBindingSetSQLOutputContext(ctx, stream, get(i)));
    }
}
