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
 *
 *
 *
 */
package org.jooq.meta;


import static java.util.Arrays.asList;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DefaultDataType.normalise;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.impl.QOM.GenerationLocation;
import org.jooq.impl.QOM.GenerationOption;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.types.UByte;
import org.jooq.types.UInteger;
import org.jooq.types.ULong;
import org.jooq.types.UShort;
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
    private String                 generator;
    private final String           converter;
    private final String           binding;
    private final boolean          nullable;
    private boolean                readonly;
    private String                 generatedAlwaysAs;
    private GenerationOption       generationOption;
    private XMLTypeDefinition      xmlTypeDefinition;
    private boolean                identity;
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
     * @deprecated - [#330] - 3.9.0 - Use {@link #DefaultDataTypeDefinition(Database, SchemaDefinition, String, Number, Number, Number, Boolean, String, Name, String, String, String)} instead.
     */
    @Deprecated
    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, String userType, String converter, String binding, String javaType) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue, name(userType), converter, binding, javaType);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, Name userType, String converter, String binding, String javaType) {
        this(database, schema, typeName, length, precision, scale, nullable, defaultValue, false, userType, converter, binding, javaType);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, String defaultValue, boolean isIdentity, Name userType, String converter, String binding, String javaType) {
        this(database, schema, typeName, length, precision, scale, nullable, false, null, defaultValue, isIdentity, userType, converter, binding, javaType);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, boolean readonly, String generatedAlwaysAs, String defaultValue, boolean identity, Name userType, String converter, String binding, String javaType) {
        this(database, schema, typeName, length, precision, scale, nullable, readonly, generatedAlwaysAs, defaultValue, identity, userType, null, converter, binding, javaType);
    }

    public DefaultDataTypeDefinition(Database database, SchemaDefinition schema, String typeName, Number length, Number precision, Number scale, Boolean nullable, boolean readonly, String generatedAlwaysAs, String defaultValue, boolean identity, Name userType, String generator, String converter, String binding, String javaType) {
        this.database = database;
        this.schema = schema;

        // [#3420] Some databases report NULL as a data type, e.g. Oracle for (some) AQ tables
        this.type = typeName == null ? "OTHER" : typeName;
        this.userType = userType;
        this.javaType = javaType;
        this.generator = generator;
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
        this.readonly = readonly;
        this.generatedAlwaysAs = generatedAlwaysAs;
        this.defaultValue = defaultValue;
        this.identity = identity;

        if (generator != null)
            database.create().configuration().commercial(() -> "Client side computed columns are a commercial only feature. Please consider upgrading to the jOOQ Professional Edition or jOOQ Enterprise Edition.");
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

    public final DefaultDataTypeDefinition readonly(boolean r) {
        this.readonly = r;
        return this;
    }

    @Override
    public final boolean isReadonly() {
        return readonly;
    }

    @Override
    public final boolean isComputed() {
        return getGeneratedAlwaysAs() != null;
    }

    @Override
    public final String getGeneratedAlwaysAs() {
        return generatedAlwaysAs;
    }

    private static final JooqLogger logGeneratedAlwaysAs = JooqLogger.getLogger(DefaultDataTypeDefinition.class, "generateAlwaysAs", 1);

    public final DefaultDataTypeDefinition generatedAlwaysAs(String g) {
        if (g != null && !database.create().configuration().commercial())
            logGeneratedAlwaysAs.info("Computed columns", "Computed columns are a commercial only jOOQ feature. If you wish to profit from this feature, please upgrade to the jOOQ Professional Edition");

        this.generatedAlwaysAs = g;
        return this;
    }

    @Override
    public GenerationOption getGenerationOption() {
        return generationOption;
    }

    @Override
    public XMLTypeDefinition getXMLTypeDefinition() {
        return xmlTypeDefinition;
    }

    @Override
    public GenerationLocation getGenerationLocation() {
        return generator == null ? GenerationLocation.SERVER : GenerationLocation.CLIENT;
    }

    private static final JooqLogger logGenerator = JooqLogger.getLogger(DefaultDataTypeDefinition.class, "logGenerator", 1);

    public final DefaultDataTypeDefinition generator(String g) {
        if (g != null && !database.create().configuration().commercial())
            logGenerator.info("Computed columns", "Client side computed columns are a commercial only jOOQ feature. If you wish to profit from this feature, please upgrade to the jOOQ Professional Edition");

        this.generator = g;
        return this;
    }

    public final DefaultDataTypeDefinition generationOption(GenerationOption go) {
        this.generationOption = go;
        return this;
    }

    public final DefaultDataTypeDefinition xmlTypeDefinition(XMLTypeDefinition x) {
        this.xmlTypeDefinition = x;
        return this;
    }

    public final DefaultDataTypeDefinition identity(boolean i) {
        this.identity = i;
        return this;
    }

    @Override
    public final boolean isIdentity() {
        return identity;
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
    public final boolean isUDTArray() {
        return isArray() && getDatabase().getArray(schema, userType).getElementType(new DefaultJavaTypeResolver()).isUDT();
    }

    @Override
    public final String getType() {
        return type;
    }

    @Override
    public final String getGenerator() {
        return generator;
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
    public final String getJavaType(JavaTypeResolver resolver) {
        if (resolver == null)
            return getJavaType();
        else
            return resolver.resolve(this);
    }

    @Override
    public final boolean isGenericNumberType() {

























        return false;
    }

    @Override
    public List<String> getMatchNames() {
        Set<String> result = new LinkedHashSet<>();
        result.add(getType());

        if (getLength() != 0)
            result.add(getType() + "(" + getLength() + ")");
        if (getScale() == 0)
            result.add(getType() + "(" + getPrecision() + ")");

        result.add(getType() + "(" + getPrecision() + "," + getScale() + ")");
        result.add(getType() + "(" + getPrecision() + ", " + getScale() + ")");

        // [#5872] We should match user-defined types as well, in case of which the type might be reported
        //         as USER-DEFINED (in PostgreSQL)
        if (!StringUtils.isBlank(getUserType())) {
            result.add(getUserType());
            result.add(getQualifiedUserType().unquotedName().toString());
        }

        return new ArrayList<>(result);
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
