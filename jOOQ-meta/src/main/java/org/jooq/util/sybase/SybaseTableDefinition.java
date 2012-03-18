/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name of the "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.util.sybase;

import static org.jooq.util.sybase.sys.Tables.SYSDOMAIN;
import static org.jooq.util.sybase.sys.Tables.SYSTAB;
import static org.jooq.util.sybase.sys.Tables.SYSTABCOL;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.SchemaDefinition;
/**
 * Sybase table definition
 *
 * @author Espen Stromsnes
 */
public class SybaseTableDefinition extends AbstractTableDefinition {

    public SybaseTableDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    public List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        for (Record record : create().select(
                SYSTABCOL.COLUMN_NAME,
                SYSTABCOL.COLUMN_ID,
                SYSDOMAIN.DOMAIN_NAME,
                SYSTABCOL.NULLS,
                SYSTABCOL.WIDTH,
                SYSTABCOL.SCALE,
                SYSTABCOL.DEFAULT)
             .from(SYSTABCOL)
             .join(SYSTAB)
             .on(SYSTABCOL.TABLE_ID.equal(SYSTAB.TABLE_ID))
             .join(SYSDOMAIN)
             .on(SYSTABCOL.DOMAIN_ID.equal(SYSDOMAIN.DOMAIN_ID))
             .where(SYSTAB.TABLE_NAME.equal(getName()))
             .orderBy(SYSTABCOL.COLUMN_ID)
             .fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(SYSDOMAIN.DOMAIN_NAME),
                record.getValue(SYSTABCOL.WIDTH),
                record.getValue(SYSTABCOL.WIDTH),
                record.getValue(SYSTABCOL.SCALE));

            ColumnDefinition column = new DefaultColumnDefinition(
            	getDatabase().getTable(getSchema(), getName()),
                record.getValue(SYSTABCOL.COLUMN_NAME),
                record.getValue(SYSTABCOL.COLUMN_ID),
                type,
                record.getValue(SYSTABCOL.NULLS, boolean.class),
                "autoincrement".equalsIgnoreCase(record.getValue(SYSTABCOL.DEFAULT)),
                null);

            result.add(column);
        }

        return result;
    }
}
