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


import static org.jooq.impl.DefaultDataType.normalise;

import org.jooq.SQLDialect;
import org.jooq.util.oracle.OracleDataType;


/**
 * @author Lukas Eder
 */
public class DefaultDataTypeDefinition implements DataTypeDefinition {

    private final Database         database;
    private final SchemaDefinition schema;
    private final String           typeName;
    private final String           udtName;
    private final boolean          nullable;
    private final boolean          defaulted;
    private final int              length;
    private final int              precision;
    private final int              scale;

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName) {
        this(database, schema, typeName, null, null, null, null, null);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultable, typeName);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable, String udtName) {
        this.database = database;
        this.schema = schema;
        this.typeName = typeName;
        this.udtName = udtName;

        // Some dialects do not distinguish between length and precision...
        if (length != null && precision != null && length.intValue() != 0 && precision.intValue() != 0) {

            // [#650] TODO Use the central type registry to find the right
            // data type instead of pattern matching
            if (typeName.toLowerCase().matches(".*?(char|text|lob|xml|graphic|string).*?")) {
                precision = null;
                scale = null;
            }
            else {
                length = null;
            }
        }

        this.length = length == null ? 0 : length.intValue();
        this.precision = precision == null ? 0 : precision.intValue();
        this.scale = scale == null ? 0 : scale.intValue();
        this.nullable = nullable == null ? true : nullable.booleanValue();
        this.defaulted = defaultable == null ? false : defaultable.booleanValue();
    }

    @Override
    public final Database getDatabase() {
        return database;
    }

    @Override
    public final SchemaDefinition getSchema() {
        return schema;
    }

    private final SQLDialect getDialect() {
        return getDatabase().getDialect();
    }

    @Override
    public final boolean isNullable() {
        return nullable;
    }

    @Override
    public final boolean isDefaulted() {
        return defaulted;
    }

    @Override
    public final boolean isUDT() {
        return getDatabase().getUDT(schema, udtName) != null;
    }

    @Override
    public final String getType() {
        return typeName;
    }

    @Override
    public final int getLength() {
        return length;
    }

    @Override
    public final int getPrecision() {
        return precision;
    }

    @Override
    public final int getScale() {
        return scale;
    }

    @Override
    public final String getUserType() {
        return udtName;
    }

    @Override
    public final boolean isGenericNumberType() {
        switch (getDialect().family()) {
            case ORACLE: {
                return (OracleDataType.NUMBER.getTypeName().equalsIgnoreCase(typeName)
                    && precision == 0
                    && scale == 0);
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
        result = prime * result + ((udtName == null) ? 0 : udtName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof DefaultDataTypeDefinition) {
            DefaultDataTypeDefinition other = (DefaultDataTypeDefinition) obj;

            if (normalise(typeName).equals(normalise(other.typeName)) &&
                normalise(udtName).equals(normalise(other.udtName))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("DataType [ t=");
        sb.append(typeName);
        sb.append("; p=");
        sb.append(precision);
        sb.append("; s=");
        sb.append(scale);
        sb.append("; u=");
        sb.append(udtName);
        sb.append(" ]");

        return sb.toString();
    }
}
