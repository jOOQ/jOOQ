/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.impl;

import static org.jooq.impl.DefaultExecuteContext.localConfiguration;

import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Row;
import org.jooq.Schema;
import org.jooq.UDT;
import org.jooq.UDTRecord;

/**
 * A record implementation for a record originating from a single UDT
 * <p>
 * This type is for JOOQ INTERNAL USE only. Do not reference directly
 *
 * @author Lukas Eder
 */
public class UDTRecordImpl<R extends UDTRecord<R>> extends AbstractRecord implements UDTRecord<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 5671315498175872799L;
    private final UDT<R>      udt;

    public UDTRecordImpl(UDT<R> udt) {
        super(udt.fields());

        this.udt = udt;
    }

    @Override
    public final UDT<R> getUDT() {
        return udt;
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Row valuesRow() {
        return new RowImpl(Utils.fields(intoArray(), fields.fields.fields()));
    }

    @Override
    public final String getSQLTypeName() throws SQLException {
        StringBuilder sb = new StringBuilder();

        // [#1693] This needs to return the fully qualified SQL type name, in
        // case the connected user is not the owner of the UDT
        Configuration configuration = localConfiguration();
        if (configuration != null) {
            Schema schema = Utils.getMappedSchema(configuration, getUDT().getSchema());

            if (schema != null) {
                sb.append(schema.getName());
                sb.append(".");
            }
        }

        sb.append(getUDT().getName());
        return sb.toString();
    }

    @Override
    public final void readSQL(SQLInput stream, String typeName) throws SQLException {
        Configuration configuration = localConfiguration();

        for (Field<?> field : getUDT().fields()) {
            setValue(configuration, stream, field);
        }
    }

    private final <T> void setValue(Configuration configuration, SQLInput stream, Field<T> field) throws SQLException {
        setValue(field, Utils.getFromSQLInput(configuration, stream, field));
    }

    @Override
    public final void writeSQL(SQLOutput stream) throws SQLException {
        for (Field<?> field : getUDT().fields()) {
            setValue(stream, field);
        }
    }

    private final <T> void setValue(SQLOutput stream, Field<T> field) throws SQLException {
        Utils.writeToSQLOutput(stream, field, getValue(field));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String separator = "";

        result.append(create().render(getUDT()));
        result.append("(");

        for (Object o : intoArray()) {
            result.append(separator);
            result.append(o);

            separator = ", ";
        }

        result.append(")");

        return result.toString();
    }
}
