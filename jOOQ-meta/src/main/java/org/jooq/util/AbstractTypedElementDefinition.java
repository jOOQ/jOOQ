/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.util;

import static org.jooq.impl.FieldTypeHelper.getDialectDataType;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DataType;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.JooqLogger;
import org.jooq.util.jaxb.ForcedType;

abstract class AbstractTypedElementDefinition<T extends Definition>
    extends AbstractDefinition
    implements TypedElementDefinition<T> {

    private static final JooqLogger      log = JooqLogger.getLogger(AbstractTypedElementDefinition.class);

    private final T                      container;
    private final DataTypeDefinition     definedType;
    private transient DataTypeDefinition type;

    public AbstractTypedElementDefinition(T container, String name, int position, DataTypeDefinition definedType, String comment) {
        super(container.getDatabase(),
              container.getSchema(),
              protectName(container.getName(), name, position),
              comment);

        this.container = container;
        this.definedType = definedType;
    }

    private static String protectName(String table, String name, int position) {
        if (name == null) {
            log.warn("Missing name", "Object " + table + " holds a column without a name at position " + position);
            return "_" + position;
        }

        return name;
    }

    @Override
    public final T getContainer() {
        return container;
    }

    @Override
    public List<Definition> getDefinitionPath() {
        List<Definition> result = new ArrayList<Definition>();

        result.addAll(getContainer().getDefinitionPath());
        result.add(this);

        return result;
    }

    @Override
    public DataTypeDefinition getType() {
        if (type == null) {
            type = mapDefinedType(container, this, definedType);
        }

        return type;
    }

    static DataTypeDefinition mapDefinedType(Definition container, Definition child, DataTypeDefinition definedType) {
        DataTypeDefinition result = null;
        Database db = container.getDatabase();

        // [#677] Forced types for matching regular expressions
        ForcedType forcedType = db.getConfiguredForcedType(child);
        if (forcedType != null) {
            log.debug("Forcing type", child + " into " + forcedType.getName());
            DataType<?> forcedDataType = null;

            String t = definedType.getType();
            int l = definedType.getLength();
            int p = definedType.getPrecision();
            int s = definedType.getScale();

            try {
                forcedDataType = getDialectDataType(db.getDialect(), forcedType.getName(), p, s);
            } catch (SQLDialectNotSupportedException ignore) {}

            // [#677] SQLDataType matches are actual type-rewrites
            if (forcedDataType != null) {
                result = new DefaultDataTypeDefinition(db, child.getSchema(), forcedType.getName(), l, p, s);
            }

            // Other forced types are UDT's, enums, etc.
            else {
                result = new DefaultDataTypeDefinition(db, child.getSchema(), t, l, p, s, forcedType.getName());
            }
        }

        // [#976] Mapping DATE as TIMESTAMP
        if (db.dateAsTimestamp()) {
            DataType<?> dataType = null;

            try {
                dataType = getDialectDataType(db.getDialect(), definedType.getType(), 0, 0);
            } catch (SQLDialectNotSupportedException ignore) {}

            if (dataType != null) {
                if (dataType.getSQLType() == Types.DATE) {
                    DataType<?> forcedDataType = getDialectDataType(db.getDialect(), SQLDataType.TIMESTAMP.getTypeName(), 0, 0);
                    result = new DefaultDataTypeDefinition(db, child.getSchema(), forcedDataType.getTypeName(), 0, 0, 0);
                }
            }
        }

        // If not yet set, use the default defined type
        if (result == null) {
            result = definedType;
        }

        return result;
    }
}
