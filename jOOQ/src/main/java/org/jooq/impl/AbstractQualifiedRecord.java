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

import static org.jooq.impl.DefaultExecuteContext.localScope;
import static org.jooq.impl.Tools.fieldsArray;
import static org.jooq.impl.Tools.getMappedUDTName;
import static org.jooq.impl.Tools.row0;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import org.jooq.Converter;
import org.jooq.Field;
import org.jooq.QualifiedRecord;
import org.jooq.RecordQualifier;
import org.jooq.Row;
import org.jooq.Scope;

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

    @SuppressWarnings("unchecked")
    @Override
    public final <T> R with(Field<T> field, T value) {
        return (R) super.with(field, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T, U> R with(Field<T> field, U value, Converter<? extends T, ? super U> converter) {
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

        // [#1693] This needs to return the fully qualified SQL type name, in
        // case the connected user is not the owner of the UDT
        return getMappedUDTName(localScope(), this);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void readSQL(SQLInput stream, String typeName) throws SQLException {
        Scope scope = localScope();
        Field<?>[] f = getQualifier().fields();

        for (int i = 0; i < f.length; i++) {
            Field field = f[i];
            DefaultBindingGetSQLInputContext out = new DefaultBindingGetSQLInputContext(scope.configuration(), scope.data(), stream);
            field.getBinding().get(out);
            set(i, field, out.value());
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void writeSQL(SQLOutput stream) throws SQLException {
        Scope scope = localScope();
        Field<?>[] f = getQualifier().fields();

        for (int i = 0; i < f.length; i++)
            f[i].getBinding().set(new DefaultBindingSetSQLOutputContext(scope.configuration(), scope.data(), stream, get(i)));
    }
}
