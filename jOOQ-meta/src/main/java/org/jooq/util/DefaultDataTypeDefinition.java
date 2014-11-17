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


import static org.jooq.impl.DefaultDataType.normalise;

import org.jooq.SQLDialect;
// ...


/**
 * @author Lukas Eder
 */
public class DefaultDataTypeDefinition implements DataTypeDefinition {

    private final Database         database;
    private final SchemaDefinition schema;
    private final String           typeName;
    private final String           udtName;
    private final String           converter;
    private final String           binding;
    private final boolean          nullable;
    private final boolean          defaulted;
    private final int              length;
    private final int              precision;
    private final int              scale;

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName) {
        this(database, schema, typeName, null, null, null, null, null, null);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultable, typeName, null);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable, String udtName) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultable, udtName, null);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable, String udtName, String converter) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultable, udtName, converter, null);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable, String udtName, String converter, String binding) {
        this.database = database;
        this.schema = schema;

        // [#3420] Some databases report NULL as a data type, e.g. Oracle for (some) AQ tables
        this.typeName = typeName == null ? "OTHER" : typeName;
        this.udtName = udtName;
        this.converter = converter;
        this.binding = binding;

        // Some dialects do not distinguish between length and precision...
        if (length != null && precision != null && length.intValue() != 0 && precision.intValue() != 0) {

            // [#650] TODO Use the central type registry to find the right
            // data type instead of pattern matching
            if (this.typeName.toLowerCase().matches(".*?(char|text|lob|xml|graphic|string).*?")) {
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
    public final String getConverter() {
        return converter;
    }

    @Override
    public final String getBinding() {
        return binding;
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
        /* [pro] xx
        xxxxxx xxxxxxxxxxxxxxxxxxxxxxx x
            xxxx xxxxxxx x
                xxxxxx xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                    xx xxxxxxxxx xx x
                    xx xxxxx xx xxx
            x
        x

        xx [/pro] */
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
