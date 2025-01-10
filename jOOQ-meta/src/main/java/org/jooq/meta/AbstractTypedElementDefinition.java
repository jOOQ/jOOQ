/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.jooq.tools.Convert.convert;
import static org.jooq.tools.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ...
import org.jooq.Converter;
import org.jooq.DataType;
import org.jooq.GeneratorStatementType;
import org.jooq.Name;
import org.jooq.exception.SQLDialectNotSupportedException;
// ...
import org.jooq.impl.AutoConverter;
import org.jooq.impl.DateAsTimestampBinding;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.EnumConverter;
import org.jooq.impl.QOM.GenerationOption;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.XMLtoJAXBConverter;
import org.jooq.meta.jaxb.CustomType;
import org.jooq.meta.jaxb.ForcedType;
import org.jooq.meta.jaxb.LambdaConverter;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

public abstract class AbstractTypedElementDefinition<T extends Definition>
extends
    AbstractContainerElementDefinition<T>
implements
    TypedElementDefinition<T>
{

    private static final JooqLogger         log                            = JooqLogger.getLogger(AbstractTypedElementDefinition.class);
    private static final Pattern            LENGTH_PRECISION_SCALE_PATTERN = Pattern.compile("[\\w\\s]+(?:\\(\\s*?(\\d+)\\s*?\\)|\\(\\s*?(\\d+)\\s*?,\\s*?(\\d+)\\s*?\\))");

    private final DataTypeDefinition        definedType;
    private transient DataTypeDefinition    type;
    private Map<Object, DataTypeDefinition> resolvedType;

    public AbstractTypedElementDefinition(T container, String name, int position, DataTypeDefinition definedType, String comment) {
        this(container, name, position, definedType, comment, null);
    }

    public AbstractTypedElementDefinition(T container, String name, int position, DataTypeDefinition definedType, String comment, String overload) {
        super(container, name, position, comment, overload);

        this.definedType = definedType;
        this.resolvedType = new HashMap<>();
    }

    @Override
    public DataTypeDefinition getType() {
        if (type == null)
            type = mapDefinedType(container, this, definedType, new DefaultJavaTypeResolver());

        return type;
    }

    @Override
    public DataTypeDefinition getType(JavaTypeResolver resolver) {
        Object key = resolver.cacheKey();

        if (key == null)
            return mapDefinedType(container, this, definedType, resolver);
        else
            return resolvedType.computeIfAbsent(key, k -> mapDefinedType(container, this, definedType, resolver));
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
        else if (db.getForceIntegerTypesOnZeroScaleDecimals())
            return DefaultDataType.getDataType(db.getDialect(), t, p, s);

        DataType<?> result = DefaultDataType.getDataType(db.getDialect(), t);
        if (result.getType() == BigDecimal.class && s == 0)
            DefaultDataType.getDataType(db.getDialect(), BigInteger.class);

        return result;
    }

    public static final DataTypeDefinition mapDefinedType(Definition container, Definition child, DataTypeDefinition definedType, JavaTypeResolver resolver) {
        if (resolver == null)
            return mapDefinedType0(container, child, definedType, resolver);
        else
            return resolver.resolveDefinedType(() -> mapDefinedType0(container, child, definedType, resolver));
    }

    static final DataTypeDefinition mapDefinedType0(
        Definition container,
        Definition child,
        DataTypeDefinition definedType,
        JavaTypeResolver resolver
    ) {
        if (resolver == null)
            resolver = new DefaultJavaTypeResolver();

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
                        result.isNullable(), result.getDefaultValue(), result.isIdentity(), (Name) null, null,
                        binding, null
                    );
                }
            }
        }

        // [#677] Forced types for matching regular expressions
        ForcedType forcedType = db.getConfiguredForcedType(child, definedType);

        if (forcedType != null) {
            String uType = forcedType.getUserType();
            String name = forcedType.getName();
            String generator = forcedType.getGenerator();
            String converter = null;
            String binding = result.getBinding();







            boolean n = result.isNullable();
            String d = result.getDefaultValue();
            boolean i = result.isIdentity();
            boolean h = result.isHidden();
            boolean r = result.isReadonly();
            String g = result.getGeneratedAlwaysAs();





            int l = 0;
            int p = 0;
            int s = 0;

            CustomType customType = customType(db, forcedType);

            // [#2486] Allow users to override length, precision, and scale
            if (name != null) {
                DataType<?> forcedDataType = null;

                Matcher matcher = LENGTH_PRECISION_SCALE_PATTERN.matcher(name);
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
                    forcedDataType = getDataType(db, name, p, s);

                    // [#677] SQLDataType matches are actual type-rewrites
                    if (forcedDataType != null)
                        result = new DefaultDataTypeDefinition(db, child.getSchema(), name, l, p, s, n, h, r, g, d, i, (Name) null, generator, converter, binding, null);

                }
                catch (SQLDialectNotSupportedException e) {

                    if (!db.getConfiguredCustomTypes().isEmpty()) {
                        if (customType != null)
                            db.markUsed(forcedType);

                        // [#7373] [#10944] Refer to <customType/> only if someone is still using the feature
                        else if (!TRUE.equals(forcedType.isIgnoreUnused()))
                            log.warn("Bad configuration for <forcedType/> " + forcedType.getName() + ". No matching <customType/> found, and no matching SQLDataType found: " + forcedType);
                    }
                }
            }

            if (customType != null) {

                // [#7373] [#10944] Historically, configured custom types could have a userType declaration in their names
                //                  This is no longer documented, but should be maintained, still
                uType = (!StringUtils.isBlank(customType.getType()))
                    ? customType.getType()
                    : customType.getName();
                name = customType.getName();

                if (generator == null)
                    generator = customType.getGenerator();









                // [#13791] AutoConverters profit from simplified configuration
                if (TRUE.equals(customType.isAutoConverter()) ||
                    AutoConverter.class.getName().equals(customType.getConverter())) {
                    String tType = tType(db, resolver, result);
                    converter = resolver.constructorCall(AutoConverter.class.getName() + "<" + resolver.ref(tType) + ", " + resolver.ref(uType) + ">") + "(" + resolver.classLiteral(tType) + ", " + resolver.classLiteral(uType) + ")";
                }

                // [#5877] [#6567] ... so do EnumConverters
                else if (TRUE.equals(customType.isEnumConverter()) ||
                    EnumConverter.class.getName().equals(customType.getConverter())) {
                    String tType = tType(db, resolver, result);
                    converter = resolver.constructorCall(EnumConverter.class.getName() + "<" + resolver.ref(tType) + ", " + resolver.ref(uType) + ">") + "(" + resolver.classLiteral(tType) + ", " + resolver.classLiteral(uType) + ")";
                }

                // [#13607] ... so do XML and JSON converters
                else if (TRUE.equals(customType.isXmlConverter()) ||
                    XMLtoJAXBConverter.class.getName().equals(customType.getConverter())) {
                    converter = resolver.constructorCall(XMLtoJAXBConverter.class.getName() + "<" + resolver.ref(uType) + ">") + "(" + resolver.classLiteral(uType) + ")";
                }
                else if (TRUE.equals(customType.isJsonConverter())) {
                    if (tType(db, resolver, result).endsWith("JSONB"))
                        converter = resolver.constructorCall("org.jooq.jackson.extensions.converters.JSONBtoJacksonConverter<" + resolver.ref(uType) + ">") + "(" + resolver.classLiteral(uType) + ")";
                    else
                        converter = resolver.constructorCall("org.jooq.jackson.extensions.converters.JSONtoJacksonConverter<" + resolver.ref(uType) + ">") + "(" + resolver.classLiteral(uType) + ")";
                }
                else if ("org.jooq.jackson.extensions.converters.JSONtoJacksonConverter".equals(customType.getConverter())) {
                    converter = resolver.constructorCall("org.jooq.jackson.extensions.converters.JSONtoJacksonConverter<" + resolver.ref(uType) + ">") + "(" + resolver.classLiteral(uType) + ")";
                }
                else if ("org.jooq.jackson.extensions.converters.JSONBtoJacksonConverter".equals(customType.getConverter())) {
                    converter = resolver.constructorCall("org.jooq.jackson.extensions.converters.JSONBtoJacksonConverter<" + resolver.ref(uType) + ">") + "(" + resolver.classLiteral(uType) + ")";
                }

                else if (customType.getLambdaConverter() != null) {
                    LambdaConverter c = customType.getLambdaConverter();
                    String tType = tType(db, resolver, result);
                    converter = resolver.ref(Converter.class) + ".of" + (!FALSE.equals(c.isNullable()) ? "Nullable" : "") + "(" + resolver.classLiteral(tType) + ", " + resolver.classLiteral(uType) + ", " + c.getFrom() + ", " + c.getTo() + ")";
                }
                else if (!StringUtils.isBlank(customType.getConverter())) {
                    if (TRUE.equals(customType.isGenericConverter())) {
                        String tType = tType(db, resolver, result);
                        converter = resolver.constructorCall(customType.getConverter() + "<" + resolver.ref(tType) + ", " + resolver.ref(uType) + ">") + "(" + resolver.classLiteral(tType) + ", " + resolver.classLiteral(uType) + ")";
                    }
                    else
                        converter = customType.getConverter();
                }

                if (!StringUtils.isBlank(customType.getBinding())) {
                    if (TRUE.equals(customType.isGenericBinding())) {
                        String tType = tType(db, resolver, result);
                        binding = resolver.constructorCall(customType.getBinding() + "<" + resolver.ref(tType) + ", " + resolver.ref(uType) + ">") + "(" + resolver.classLiteral(tType) + ", " + resolver.classLiteral(uType) + ")";
                    }
                    else
                        binding = customType.getBinding();
                }
            }














            if (name != null || uType != null) {
                db.markUsed(forcedType);
                log.info("Forcing type", child + " to " + forcedType);

                if (customType != null) {
                    l = result.getLength();
                    p = result.getPrecision();
                    s = result.getScale();
                    String t = result.getType();
                    Name u = result.getQualifiedUserType();
                    result = new DefaultDataTypeDefinition(db, definedType.getSchema(), t, l, p, s, n, h, r, g, d, i, u, generator, converter, binding, uType);
                }
            }

            if (generator != null) {
                db.markUsed(forcedType);
                ((DefaultDataTypeDefinition) result).generator(generator);







            }







        }

        return result;
    }

    private static final String tType(Database db, JavaTypeResolver resolver, DataTypeDefinition definedType) {
        if (resolver != null)
            return resolver.resolve(definedType);

        try {
            return getDataType(db, definedType.getType(), definedType.getPrecision(), definedType.getScale())
                .getType()
                .getName();
        }
        catch (SQLDialectNotSupportedException ignore) {
            return Object.class.getName();
        }
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
                .withGenericBinding(forcedType.isGenericBinding())
                .withAutoConverter(forcedType.isAutoConverter())
                .withGenericConverter(forcedType.isGenericConverter())
                .withEnumConverter(forcedType.isEnumConverter())
                .withXmlConverter(forcedType.isXmlConverter())
                .withJsonConverter(forcedType.isJsonConverter())
                .withLambdaConverter(forcedType.getLambdaConverter())
                .withVisibilityModifier(forcedType.getVisibilityModifier())
                .withHidden(forcedType.isHidden())
                .withGenerator(forcedType.getGenerator())
                .withAuditInsertTimestamp(forcedType.isAuditInsertTimestamp())
                .withAuditInsertUser(forcedType.isAuditInsertUser())
                .withAuditUpdateTimestamp(forcedType.isAuditUpdateTimestamp())
                .withAuditUpdateUser(forcedType.isAuditUpdateUser())
                .withConverter(forcedType.getConverter())
                .withName(name)
                .withType(forcedType.getUserType());
        }

        return null;
    }

    @Override
    public final DomainDefinition getDomain() {
        return getDatabase().getDomain(getSchema(), getDefinedType().getQualifiedUserType());
    }
}
