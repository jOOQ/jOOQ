/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
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
        ForcedType forcedType = db.getConfiguredForcedType(child, definedType);
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
