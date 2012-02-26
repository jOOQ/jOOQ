/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
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
 * . Neither the name "jOOQ" nor the names of its contributors may be
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
package org.jooq.util.db2;

import static org.jooq.util.db2.syscat.tables.Attributes.ATTRIBUTES;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractUDTDefinition;
import org.jooq.util.AttributeDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultAttributeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.db2.syscat.tables.Attributes;

/**
 * DB2 UDT definition
 *
 * @author Espen Stromsnes
 */
public class DB2UDTDefinition extends AbstractUDTDefinition {

    public DB2UDTDefinition(SchemaDefinition schema, String name, String comment) {
        super(schema, name, comment);
    }

    @Override
    protected List<AttributeDefinition> getElements0() throws SQLException {
        List<AttributeDefinition> result = new ArrayList<AttributeDefinition>();

        for (Record record : create().select(
                Attributes.ATTR_NAME,
                Attributes.ORDINAL,
                Attributes.ATTR_TYPENAME,
                Attributes.LENGTH,
                Attributes.SCALE)
            .from(ATTRIBUTES)
            .where(Attributes.TYPESCHEMA.equal(getSchema().getName()))
            .and(Attributes.TYPENAME.equal(getName()))
            .orderBy(Attributes.ORDINAL).fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(
                getDatabase(),
                getSchema(),
                record.getValue(Attributes.ATTR_TYPENAME),
                record.getValue(Attributes.LENGTH),
                record.getValue(Attributes.SCALE));

            AttributeDefinition column = new DefaultAttributeDefinition(
                this,
                record.getValue(Attributes.ATTR_NAME),
                record.getValue(Attributes.ORDINAL),
                type);

            result.add(column);
        }

        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() {
        return Collections.emptyList();
    }
}
