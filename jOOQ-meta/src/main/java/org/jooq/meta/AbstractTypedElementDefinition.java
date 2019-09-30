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


import static org.jooq.tools.Convert.convert;
import static org.jooq.tools.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jooq.DataType;
import org.jooq.Name;
import org.jooq.exception.SQLDialectNotSupportedException;
import org.jooq.impl.DateAsTimestampBinding;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.EnumConverter;
import org.jooq.impl.SQLDataType;
import org.jooq.meta.jaxb.CustomType;
import org.jooq.meta.jaxb.ForcedType;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

public abstract class AbstractTypedElementDefinition<T extends Definition>
    extends AbstractDefinition
    implements TypedElementDefinition<T> {

    private static final JooqLogger      log                            = JooqLogger.getLogger(AbstractTypedElementDefinition.class);
    private static final Pattern         LENGTH_PRECISION_SCALE_PATTERN = Pattern.compile("[\\w\\s]+(?:\\(\\s*?(\\d+)\\s*?\\)|\\(\\s*?(\\d+)\\s*?,\\s*?(\\d+)\\s*?\\))");

    private final T                      container;
    private final DataTypeDefinition     definedType;
    private transient DataTypeDefinition type;
    private transient DataTypeDefinition resolvedType;

    public AbstractTypedElementDefinition(T container, String name, int position, DataTypeDefinition definedType, String comment) {
        super(container.getDatabase(),
              container.getSchema(),
              protectName(container, name, position),
              comment);

        this.container = container;
        this.definedType = definedType;
    }

    private static final String protectName(Definition container, String name, int position) {
        if (name == null) {

            // [#6654] Specific error messages per type
            if (container instanceof TableDefinition)
                log.info("Missing name", "Table " + container + " holds a column without a name at position " + position);
            else if (container instanceof UDTDefinition)
                log.info("Missing name", "UDT " + container + " holds an attribute without a name at position " + position);
            else if (container instanceof IndexDefinition)
                log.info("Missing name", "Index " + container + " holds a column without a name at position " + position);
            else if (container instanceof RoutineDefinition)
                log.info("Missing name", "Routine " + container + " holds a parameter without a name at position " + position);
            else
                log.info("Missing name", "Object " + container + " holds an element without a name at position " + position);

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
        List<Definition> result = new ArrayList<>();

        result.addAll(getContainer().getDefinitionPath());
        result.add(this);

        return result;
    }

    @Override
    public DataTypeDefinition getType() {
        if (type == null) {
            type = mapDefinedType(container, this, definedType, null);
        }

        return type;
    }

    @Override
    public DataTypeDefinition getType(JavaTypeResolver resolver) {
        if (resolvedType == null) {
            resolvedType = mapDefinedType(container, this, definedType, resolver);
        }

        return resolvedType;
    }

    @Override
    public DataTypeDefinition getDefinedType() {
        return definedType;
    }

    public static final DataType<?> getDataType(Database db, String t, int p, int s) {


        // [#8493] Synthetic SQLDataType aliases used by the code generator only
        if ("OFFSETDATETIME".equalsIgnoreCase(t))
            return SQLDataType.OFFSETDATETIME.precision(p);
        else if ("OFFSETTIME".equalsIgnoreCase(t))
            return SQLDataType.OFFSETTIME.precision(p);
        else if ("LOCALDATE".equalsIgnoreCase(t))
            return SQLDataType.LOCALDATE;
        else if ("LOCALDATETIME".equalsIgnoreCase(t))
            return SQLDataType.LOCALDATETIME.precision(p);
        else if ("LOCALTIME".equalsIgnoreCase(t))
            return SQLDataType.LOCALTIME.precision(p);
        else

        if (db.getForceIntegerTypesOnZeroScaleDecimals())
            return DefaultDataType.getDataType(db.getDialect(), t, p, s);

        DataType<?> result = DefaultDataType.getDataType(db.getDialect(), t);
        if (result.getType() == BigDecimal.class && s == 0)
            DefaultDataType.getDataType(db.getDialect(), BigInteger.class);

        return result;
    }

    @SuppressWarnings("deprecation")
    public static final DataTypeDefinition mapDefinedType(Definition container, Definition child, DataTypeDefinition definedType, JavaTypeResolver resolver) {
        DataTypeDefinition result = definedType;
        Database db = container.getDatabase();

        log.debug("Type mapping", child + " with type " + definedType.getType());

        // [#976] Mapping DATE as TIMESTAMP
        if (db.dateAsTimestamp()) {
            DataType<?> dataType = null;

            try {
                dataType = getDataType(db, result.getType(), 0, 0);
            } catch (SQLDialectNotSupportedException ignore) {}

            if (dataType != null) {

                // [#5239] [#5762] [#6453] Don't rely on getSQLType()
                if (SQLDataType.DATE.equals(dataType.getSQLDataType())) {
                    DataType<?> forcedDataType = getDataType(db, SQLDataType.TIMESTAMP.getTypeName(), 0, 0);
                    String binding = DateAsTimestampBinding.class.getName();


                    if (db.javaTimeTypes())
                        binding = org.jooq.impl.LocalDateAsLocalDateTimeBinding.class.getName();


                    result = new DefaultDataTypeDefinition(
                        db,
                        child.getSchema(),
                        forcedDataType.getTypeName(),
                        0, 0, 0,
                        result.isNullable(), result.getDefaultValue(), (Name) null, null,
                        binding
                    );
                }
            }
        }

        // [#677] Forced types for matching regular expressions
        ForcedType forcedType = db.getConfiguredForcedType(child, definedType);
        if (forcedType != null) {
            String uType = forcedType.getName();
            String converter = null;
            String binding = result.getBinding();

            CustomType customType = customType(db, forcedType);
            if (customType != null) {
                uType = (!StringUtils.isBlank(customType.getType()))
                    ? customType.getType()
                    : customType.getName();

                // [#5877] [#6567] EnumConverters profit from simplified configuration
                if (Boolean.TRUE.equals(customType.isEnumConverter()) ||
                    EnumConverter.class.getName().equals(customType.getConverter())) {

                    String tType = Object.class.getName();

                    if (resolver != null)
                        tType = resolver.resolve(definedType);
                    else
                        try {
                            tType = getDataType(db, definedType.getType(), definedType.getPrecision(), definedType.getScale())
                                .getType()
                                .getName();
                        }
                        catch (SQLDialectNotSupportedException ignore) {}

                    converter = "new " + EnumConverter.class.getName() + "<" + tType + ", " + uType + ">(" + tType + ".class, " + uType + ".class)";
                }
                else if (!StringUtils.isBlank(customType.getConverter())) {
                    converter = customType.getConverter();
                }

                if (!StringUtils.isBlank(customType.getBinding()))
                    binding = customType.getBinding();
            }

            if (uType != null) {
                log.info("Forcing type", child + " to " + forcedType);

                DataType<?> forcedDataType = null;

                boolean n = result.isNullable();
                String d = result.getDefaultValue();

                int l = 0;
                int p = 0;
                int s = 0;

                // [#2486] Allow users to override length, precision, and scale
                Matcher matcher = LENGTH_PRECISION_SCALE_PATTERN.matcher(uType);
                if (matcher.find()) {
                    if (!isEmpty(matcher.group(1))) {
                        l = p = convert(matcher.group(1), int.class);
                    }
                    else {
                        p = convert(matcher.group(2), int.class);
                        s = convert(matcher.group(3), int.class);
                    }
                }

                try {
                    forcedDataType = getDataType(db, uType, p, s);
                } catch (SQLDialectNotSupportedException ignore) {}

                // [#677] SQLDataType matches are actual type-rewrites
                if (forcedDataType != null) {

                    // [#3704] When <forcedType/> matches a custom type AND a data type rewrite, the rewrite was usually accidental.
                    if (customType != null)
                        log.warn("Custom type conflict", child + " has custom type " + customType + " forced by " + forcedType + " but a data type rewrite applies");

                    result = new DefaultDataTypeDefinition(db, child.getSchema(), uType, l, p, s, n, d, (Name) null, converter, binding);
                }

                // Other forced types are UDT's, enums, etc.
                else if (customType != null) {
                    l = result.getLength();
                    p = result.getPrecision();
                    s = result.getScale();
                    String t = result.getType();
                    Name u = result.getQualifiedUserType();
                    result = new DefaultDataTypeDefinition(db, definedType.getSchema(), t, l, p, s, n, d, u, converter, binding, uType);
                }

                // [#4597] If we don't have a type-rewrite (forcedDataType) or a
                //         matching customType, the user probably malconfigured
                //         their <forcedTypes/> or <customTypes/>
                else {
                    log.warn("Bad configuration for <forcedType/> " + forcedType.getName() + ". No matching <customType/> found, and no matching SQLDataType found: " + forcedType);
                }
            }
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    public static final CustomType customType(Database db, ForcedType forcedType) {
        String name = forcedType.getName();

        // [#4598] Legacy use-case where a <forcedType/> referes to a <customType/>
        //         element by name.
        if (StringUtils.isBlank(forcedType.getUserType())) {
            if (name != null)
                for (CustomType type : db.getConfiguredCustomTypes())
                    if (name.equals(type.getName()))
                        return type;
        }

        // [#4598] New default use-case where <forcedType/> embeds <customType/>
        //         information for convenience.
        else {
            return new CustomType()
                .withBinding(forcedType.getBinding())
                .withEnumConverter(forcedType.isEnumConverter())
                .withConverter(forcedType.getConverter())
                .withName(name)
                .withType(forcedType.getUserType());
        }

        return null;
    }
}
