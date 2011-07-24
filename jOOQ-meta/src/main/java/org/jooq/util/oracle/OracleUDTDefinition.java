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
package org.jooq.util.oracle;

import static org.jooq.util.oracle.sys.tables.AllTypeAttrs.ALL_TYPE_ATTRS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Record;
import org.jooq.util.AbstractUDTDefinition;
import org.jooq.util.AttributeDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.DefaultAttributeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.oracle.sys.tables.AllTypeAttrs;

public class OracleUDTDefinition extends AbstractUDTDefinition {

    public OracleUDTDefinition(Database database, String name, String comment) {
        super(database, name, comment);
    }

    @Override
    protected List<AttributeDefinition> getElements0() throws SQLException {
        List<AttributeDefinition> result = new ArrayList<AttributeDefinition>();

        for (Record record : create().select(
                AllTypeAttrs.ATTR_NAME,
                AllTypeAttrs.ATTR_NO,
                AllTypeAttrs.ATTR_TYPE_NAME,
                AllTypeAttrs.PRECISION,
                AllTypeAttrs.SCALE)
            .from(ALL_TYPE_ATTRS)
            .where(AllTypeAttrs.OWNER.equal(getSchemaName()))
            .and(AllTypeAttrs.TYPE_NAME.equal(getName()))
            .orderBy(AllTypeAttrs.ATTR_NO).fetch()) {

            DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(),
                record.getValue(AllTypeAttrs.ATTR_TYPE_NAME),
                record.getValueAsInteger(AllTypeAttrs.PRECISION, 0),
                record.getValueAsInteger(AllTypeAttrs.SCALE, 0));

            AttributeDefinition attribute = new DefaultAttributeDefinition(
                this,
                record.getValue(AllTypeAttrs.ATTR_NAME),
                record.getValueAsInteger(AllTypeAttrs.ATTR_NO),
                type);

            result.add(attribute);
        }

        return result;
    }
}
