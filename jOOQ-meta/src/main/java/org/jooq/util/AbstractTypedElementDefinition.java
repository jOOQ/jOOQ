/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under AGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 *
 * AGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it and/or
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.util;


import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DataType;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.DefaultDataType;
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
        DataTypeDefinition result = definedType;
        Database db = container.getDatabase();

        // [#976] Mapping DATE as TIMESTAMP
        if (db.dateAsTimestamp()) {
            DataType<?> dataType = null;

            try {
                dataType = DefaultDataType.getDataType(db.getDialect(), result.getType(), 0, 0);
            } catch (SQLDialectNotSupportedException ignore) {}

            if (dataType != null) {
                if (dataType.getSQLType() == Types.DATE) {
                    DataType<?> forcedDataType = DefaultDataType.getDataType(db.getDialect(), SQLDataType.TIMESTAMP.getTypeName(), 0, 0);
                    result = new DefaultDataTypeDefinition(db, child.getSchema(), forcedDataType.getTypeName());
                }
            }
        }

        // [#677] Forced types for matching regular expressions
        ForcedType forcedType = db.getConfiguredForcedType(child);
        if (forcedType != null) {
            log.debug("Forcing type", child + " into " + forcedType.getName());
            DataType<?> forcedDataType = null;

            String t = result.getType();
            int l = result.getLength();
            int p = result.getPrecision();
            int s = result.getScale();
            boolean n = result.isNullable();
            boolean d = result.isDefaulted();

            try {
                forcedDataType = DefaultDataType.getDataType(db.getDialect(), forcedType.getName(), p, s);
            } catch (SQLDialectNotSupportedException ignore) {}

            // [#677] SQLDataType matches are actual type-rewrites
            if (forcedDataType != null) {
                result = new DefaultDataTypeDefinition(db, child.getSchema(), forcedType.getName(), l, p, s, n, d);
            }

            // Other forced types are UDT's, enums, etc.
            else {
                result = new DefaultDataTypeDefinition(db, child.getSchema(), t, l, p, s, n, d, forcedType.getName());
            }
        }

        return result;
    }
}
