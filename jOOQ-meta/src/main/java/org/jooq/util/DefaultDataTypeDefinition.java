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
 */
package org.jooq.util;


import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DefaultDataType.normalise;

import org.jooq.Name;
import org.jooq.SQLDialect;
// ...


/**
 * @author Lukas Eder
 */
public class DefaultDataTypeDefinition implements DataTypeDefinition {

    private final Database         database;
    private final SchemaDefinition schema;
    private final String           type;
    private final Name             userType;
    private final String           javaType;
    private final String           converter;
    private final String           binding;
    private final boolean          nullable;
    private final String           defaultValue;
    private final int              length;
    private final int              precision;
    private final int              scale;

    private static final String defaultValue(Boolean defaultable) {
        return defaultable != null && defaultable ? "NULL" : null;
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName) {
        this(database, schema, typeName, null, null, null, null, (String) null, (Name) null);
    }

    /**
     * @deprecated - [#4841] - 3.8.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String)} instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultable, typeName, null);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue, typeName, null);
    }

    /**
     * @deprecated - [#4841] - 3.8.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String, String)} instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable, String userType) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultable, userType, null);
    }

    /**
     * @deprecated - [#330] - 3.9.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String, Name)}  instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, String userType) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue, name(userType));
    }

    /**
     * @deprecated - [#330] - 3.9.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String, Name)}  instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultValue, Name userType) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue(defaultValue), userType);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, Name userType) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue, userType, null);
    }

    /**
     * @deprecated - [#4841] - 3.8.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String, String, String)} instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable, String userType, String converter) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultable, userType, converter, null);
    }

    /**
     * @deprecated - [#330] - 3.9.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String, Name, String)}  instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, String userType, String converter) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue, name(userType), converter);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, Name userType, String converter) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue, userType, converter, null);
    }

    /**
     * @deprecated - [#4841] - 3.8.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String, String, String, String)} instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable, String userType, String converter, String binding) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultable, userType, converter, binding, null);
    }

    /**
     * @deprecated - [#330] - 3.9.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String, Name, String, String)}  instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, String userType, String converter, String binding) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue, name(userType), converter, binding, null);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, Name userType, String converter, String binding) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue, userType, converter, binding, null);
    }

    /**
     * @deprecated - [#4841] - 3.8.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String, String, String, String, String)} instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, Boolean defaultable, String userType, String converter, String binding, String javaType) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue(defaultable), userType, converter, binding, javaType);
    }

    /**
     * @deprecated - [#330] - 3.9.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String, Name, String, String, String)}  instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, String userType, String converter, String binding, String javaType) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue, name(userType), converter, binding, javaType);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, Name userType, String converter, String binding, String javaType) {
        this.database = database;
        this.schema = schema;

        // [#3420] Some databases report NULL as a data type, e.g. Oracle for (some) AQ tables
        this.type = typeName == null ? "OTHER" : typeName;
        this.userType = userType;
        this.javaType = javaType;
        this.converter = converter;
        this.binding = binding;

        // Some dialects do not distinguish between length and precision...
        if (length != null && precision != null && length.intValue() != 0 && precision.intValue() != 0) {

            // [#650] TODO Use the central type registry to find the right
            // data type instead of pattern matching
            if (this.type.toLowerCase().matches(".*?(char|text|lob|xml|graphic|string).*?")) {
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
        this.defaultValue = defaultValue;
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
        return getDefaultValue() != null;
    }

    @Override
    public final String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public final boolean isUDT() {
        if (userType == null)
            return false;

        return getDatabase().getUDT(schema, userType) != null;
    }

    @Override
    public final boolean isArray() {
        if (userType == null)
            return false;

        return getDatabase().getArray(schema, userType) != null;
    }

    @Override
    public final String getType() {
        return type;
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
        return userType != null ? userType.last() : null;
    }

    @Override
    public final Name getQualifiedUserType() {
        return userType;
    }

    @Override
    public final String getJavaType() {
        return javaType;
    }

    @Override
    public final boolean isGenericNumberType() {










        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((userType == null) ? 0 : userType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj instanceof DefaultDataTypeDefinition) {
            DefaultDataTypeDefinition other = (DefaultDataTypeDefinition) obj;

            if (!normalise(type).equals(normalise(other.type)))
                return false;

            if (userType == null && other.userType == null)
                return true;

            if (userType == null || other.userType == null)
                return false;

            return normalise(userType.last()).equals(normalise(other.userType.last()));
        }

        return false;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("DataType [ t=");
        sb.append(type);
        sb.append("; p=");
        sb.append(precision);
        sb.append("; s=");
        sb.append(scale);
        sb.append("; u=");
        sb.append(userType);
        sb.append("; j=");
        sb.append(javaType);
        sb.append(" ]");

        return sb.toString();
    }
}
