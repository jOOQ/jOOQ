
package org.jooq.meta.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * A forced type declaration
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ForcedType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class ForcedType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31700L;
    @XmlElement(defaultValue = "0")
    protected Integer priority = 0;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String userType;
    @XmlSchemaType(name = "string")
    protected VisibilityModifier visibilityModifier;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String generator;
    protected Boolean auditInsertTimestamp;
    protected Boolean auditInsertUser;
    protected Boolean auditUpdateTimestamp;
    protected Boolean auditUpdateUser;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String converter;
    protected Boolean enumConverter;
    protected LambdaConverter lambdaConverter;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String binding;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String excludeExpression;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String includeExpression;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expressions;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String sql;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String excludeTypes;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String includeTypes;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String types;
    @XmlElement(defaultValue = "ALL")
    @XmlSchemaType(name = "string")
    protected Nullability nullability = Nullability.ALL;
    @XmlElement(defaultValue = "ALL")
    @XmlSchemaType(name = "string")
    protected ForcedTypeObjectType objectType = ForcedTypeObjectType.ALL;

    /**
     * The priority among forced types in which to apply this one. Forced types of equal priority will be applied in the order in which they're added to the forced types list (e.g. the Maven lexical XML order)
     * 
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * The priority among forced types in which to apply this one. Forced types of equal priority will be applied in the order in which they're added to the forced types list (e.g. the Maven lexical XML order)
     * 
     */
    public void setPriority(Integer value) {
        this.priority = value;
    }

    /**
     * The name (in {@link org.jooq.impl.SQLDataType}) to force any matches to
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The name (in {@link org.jooq.impl.SQLDataType}) to force any matches to
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * The type of the user type - e.g. java.time.LocalDateTime.
     * <p>
     * If provided, {@link #getName()} will be ignored, and either {@link #getConverter()}
     * or {@link #getBinding()} is required
     * 
     */
    public String getUserType() {
        return userType;
    }

    /**
     * The type of the user type - e.g. java.time.LocalDateTime.
     * <p>
     * If provided, {@link #getName()} will be ignored, and either {@link #getConverter()}
     * or {@link #getBinding()} is required
     * 
     */
    public void setUserType(String value) {
        this.userType = value;
    }

    /**
     * The visibility modifier to be used in generated code for the column that is matched by this forced type, if applicable.
     * <p>
     * This has no effect on matched objects that are not columns.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public VisibilityModifier getVisibilityModifier() {
        return visibilityModifier;
    }

    /**
     * The visibility modifier to be used in generated code for the column that is matched by this forced type, if applicable.
     * <p>
     * This has no effect on matched objects that are not columns.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setVisibilityModifier(VisibilityModifier value) {
        this.visibilityModifier = value;
    }

    /**
     * A {@link org.jooq.Generator} implementation used for client-side computed columns.
     * <p>
     * This has no effect on matched objects that are not columns.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public String getGenerator() {
        return generator;
    }

    /**
     * A {@link org.jooq.Generator} implementation used for client-side computed columns.
     * <p>
     * This has no effect on matched objects that are not columns.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setGenerator(String value) {
        this.generator = value;
    }

    /**
     * Whether this column acts as an audit {@link org.jooq.GeneratorStatementType#INSERT} timestamp.
     * <p>
     * This flag produces a {@link #generator} configuration, so it cannot be combined with a custom generator. The different audit flags are mutually exclusive.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAuditInsertTimestamp() {
        return auditInsertTimestamp;
    }

    /**
     * Sets the value of the auditInsertTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAuditInsertTimestamp(Boolean value) {
        this.auditInsertTimestamp = value;
    }

    /**
     * Whether this column acts as an audit {@link org.jooq.GeneratorStatementType#INSERT} timestamp.
     * <p>
     * This flag produces a {@link #generator} configuration, so it cannot be combined with a custom generator. The different audit flags are mutually exclusive.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAuditInsertUser() {
        return auditInsertUser;
    }

    /**
     * Sets the value of the auditInsertUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAuditInsertUser(Boolean value) {
        this.auditInsertUser = value;
    }

    /**
     * Whether this column acts as an audit {@link org.jooq.GeneratorStatementType#UPDATE} timestamp.
     * <p>
     * This flag produces a {@link #generator} configuration, so it cannot be combined with a custom generator. The different audit flags are mutually exclusive.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAuditUpdateTimestamp() {
        return auditUpdateTimestamp;
    }

    /**
     * Sets the value of the auditUpdateTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAuditUpdateTimestamp(Boolean value) {
        this.auditUpdateTimestamp = value;
    }

    /**
     * Whether this column acts as an audit {@link org.jooq.GeneratorStatementType#UPDATE} timestamp.
     * <p>
     * This flag produces a {@link #generator} configuration, so it cannot be combined with a custom generator. The different audit flags are mutually exclusive.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAuditUpdateUser() {
        return auditUpdateUser;
    }

    /**
     * Sets the value of the auditUpdateUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAuditUpdateUser(Boolean value) {
        this.auditUpdateUser = value;
    }

    /**
     * A converter implementation for the {@link #getUserType()}.
     * 
     */
    public String getConverter() {
        return converter;
    }

    /**
     * A converter implementation for the {@link #getUserType()}.
     * 
     */
    public void setConverter(String value) {
        this.converter = value;
    }

    /**
     * Whether the converter is an {@link org.jooq.impl.EnumConverter}.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEnumConverter() {
        return enumConverter;
    }

    /**
     * Sets the value of the enumConverter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnumConverter(Boolean value) {
        this.enumConverter = value;
    }

    /**
     * A lambda converter implementation for the {@link #getUserType()}.
     * 
     */
    public LambdaConverter getLambdaConverter() {
        return lambdaConverter;
    }

    /**
     * A lambda converter implementation for the {@link #getUserType()}.
     * 
     */
    public void setLambdaConverter(LambdaConverter value) {
        this.lambdaConverter = value;
    }

    /**
     * A {@link org.jooq.Binding} implementation for the custom type.
     * 
     */
    public String getBinding() {
        return binding;
    }

    /**
     * A {@link org.jooq.Binding} implementation for the custom type.
     * 
     */
    public void setBinding(String value) {
        this.binding = value;
    }

    /**
     * A Java regular expression matching columns, parameters, attributes,
     * etc. which must not have this type. Excludes match before includes, i.e.
     * excludes have a higher priority.
     * 
     */
    public String getExcludeExpression() {
        return excludeExpression;
    }

    /**
     * A Java regular expression matching columns, parameters, attributes,
     * etc. which must not have this type. Excludes match before includes, i.e.
     * excludes have a higher priority.
     * 
     */
    public void setExcludeExpression(String value) {
        this.excludeExpression = value;
    }

    /**
     * A Java regular expression matching columns, parameters, attributes,
     * etc. to be forced to have this type. If provided, both "includeExpression" and
     * "includeTypes" must match.
     * 
     */
    public String getIncludeExpression() {
        return includeExpression;
    }

    /**
     * A Java regular expression matching columns, parameters, attributes,
     * etc. to be forced to have this type. If provided, both "includeExpression" and
     * "includeTypes" must match.
     * 
     */
    public void setIncludeExpression(String value) {
        this.includeExpression = value;
    }

    /**
     * The same as {@link #getIncludeExpression()}. This is kept for backwards compatibility reasons.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * The same as {@link #getIncludeExpression()}. This is kept for backwards compatibility reasons.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * The same as {@link #getIncludeExpression()}. This is kept for backwards compatibility reasons.
     * 
     */
    public String getExpressions() {
        return expressions;
    }

    /**
     * The same as {@link #getIncludeExpression()}. This is kept for backwards compatibility reasons.
     * 
     */
    public void setExpressions(String value) {
        this.expressions = value;
    }

    /**
     * A SQL statement that produces a table with one column containing the matched qualified or unqualified column names.
     * 
     */
    public String getSql() {
        return sql;
    }

    /**
     * A SQL statement that produces a table with one column containing the matched qualified or unqualified column names.
     * 
     */
    public void setSql(String value) {
        this.sql = value;
    }

    /**
     * A Java regular expression matching data types
     * which must not have this type. Excludes match before includes, i.e.
     * excludes have a higher priority.
     * 
     */
    public String getExcludeTypes() {
        return excludeTypes;
    }

    /**
     * A Java regular expression matching data types
     * which must not have this type. Excludes match before includes, i.e.
     * excludes have a higher priority.
     * 
     */
    public void setExcludeTypes(String value) {
        this.excludeTypes = value;
    }

    /**
     * A Java regular expression matching data types to be forced to have this
     * type. If provided, both "includeExpression" and "includeTypes" must match.
     * 
     */
    public String getIncludeTypes() {
        return includeTypes;
    }

    /**
     * A Java regular expression matching data types to be forced to have this
     * type. If provided, both "includeExpression" and "includeTypes" must match.
     * 
     */
    public void setIncludeTypes(String value) {
        this.includeTypes = value;
    }

    /**
     * The same as {@link #getIncludeTypes()}. This is kept for backwards compatibility reasons.
     * 
     */
    public String getTypes() {
        return types;
    }

    /**
     * The same as {@link #getIncludeTypes()}. This is kept for backwards compatibility reasons.
     * 
     */
    public void setTypes(String value) {
        this.types = value;
    }

    /**
     * Whether this forced type should apply to nullable / non-nullable / all columns
     * 
     */
    public Nullability getNullability() {
        return nullability;
    }

    /**
     * Whether this forced type should apply to nullable / non-nullable / all columns
     * 
     */
    public void setNullability(Nullability value) {
        this.nullability = value;
    }

    /**
     * Whether this forced type should apply to all object types, or only to specific ones
     * 
     */
    public ForcedTypeObjectType getObjectType() {
        return objectType;
    }

    /**
     * Whether this forced type should apply to all object types, or only to specific ones
     * 
     */
    public void setObjectType(ForcedTypeObjectType value) {
        this.objectType = value;
    }

    /**
     * The priority among forced types in which to apply this one. Forced types of equal priority will be applied in the order in which they're added to the forced types list (e.g. the Maven lexical XML order)
     * 
     */
    public ForcedType withPriority(Integer value) {
        setPriority(value);
        return this;
    }

    /**
     * The name (in {@link org.jooq.impl.SQLDataType}) to force any matches to
     * 
     */
    public ForcedType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * The type of the user type - e.g. java.time.LocalDateTime.
     * <p>
     * If provided, {@link #getName()} will be ignored, and either {@link #getConverter()}
     * or {@link #getBinding()} is required
     * 
     */
    public ForcedType withUserType(String value) {
        setUserType(value);
        return this;
    }

    /**
     * The visibility modifier to be used in generated code for the column that is matched by this forced type, if applicable.
     * <p>
     * This has no effect on matched objects that are not columns.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public ForcedType withVisibilityModifier(VisibilityModifier value) {
        setVisibilityModifier(value);
        return this;
    }

    /**
     * A {@link org.jooq.Generator} implementation used for client-side computed columns.
     * <p>
     * This has no effect on matched objects that are not columns.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public ForcedType withGenerator(String value) {
        setGenerator(value);
        return this;
    }

    public ForcedType withAuditInsertTimestamp(Boolean value) {
        setAuditInsertTimestamp(value);
        return this;
    }

    public ForcedType withAuditInsertUser(Boolean value) {
        setAuditInsertUser(value);
        return this;
    }

    public ForcedType withAuditUpdateTimestamp(Boolean value) {
        setAuditUpdateTimestamp(value);
        return this;
    }

    public ForcedType withAuditUpdateUser(Boolean value) {
        setAuditUpdateUser(value);
        return this;
    }

    /**
     * A converter implementation for the {@link #getUserType()}.
     * 
     */
    public ForcedType withConverter(String value) {
        setConverter(value);
        return this;
    }

    public ForcedType withEnumConverter(Boolean value) {
        setEnumConverter(value);
        return this;
    }

    /**
     * A lambda converter implementation for the {@link #getUserType()}.
     * 
     */
    public ForcedType withLambdaConverter(LambdaConverter value) {
        setLambdaConverter(value);
        return this;
    }

    /**
     * A {@link org.jooq.Binding} implementation for the custom type.
     * 
     */
    public ForcedType withBinding(String value) {
        setBinding(value);
        return this;
    }

    /**
     * A Java regular expression matching columns, parameters, attributes,
     * etc. which must not have this type. Excludes match before includes, i.e.
     * excludes have a higher priority.
     * 
     */
    public ForcedType withExcludeExpression(String value) {
        setExcludeExpression(value);
        return this;
    }

    /**
     * A Java regular expression matching columns, parameters, attributes,
     * etc. to be forced to have this type. If provided, both "includeExpression" and
     * "includeTypes" must match.
     * 
     */
    public ForcedType withIncludeExpression(String value) {
        setIncludeExpression(value);
        return this;
    }

    /**
     * The same as {@link #getIncludeExpression()}. This is kept for backwards compatibility reasons.
     * 
     */
    public ForcedType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * The same as {@link #getIncludeExpression()}. This is kept for backwards compatibility reasons.
     * 
     */
    public ForcedType withExpressions(String value) {
        setExpressions(value);
        return this;
    }

    /**
     * A SQL statement that produces a table with one column containing the matched qualified or unqualified column names.
     * 
     */
    public ForcedType withSql(String value) {
        setSql(value);
        return this;
    }

    /**
     * A Java regular expression matching data types
     * which must not have this type. Excludes match before includes, i.e.
     * excludes have a higher priority.
     * 
     */
    public ForcedType withExcludeTypes(String value) {
        setExcludeTypes(value);
        return this;
    }

    /**
     * A Java regular expression matching data types to be forced to have this
     * type. If provided, both "includeExpression" and "includeTypes" must match.
     * 
     */
    public ForcedType withIncludeTypes(String value) {
        setIncludeTypes(value);
        return this;
    }

    /**
     * The same as {@link #getIncludeTypes()}. This is kept for backwards compatibility reasons.
     * 
     */
    public ForcedType withTypes(String value) {
        setTypes(value);
        return this;
    }

    /**
     * Whether this forced type should apply to nullable / non-nullable / all columns
     * 
     */
    public ForcedType withNullability(Nullability value) {
        setNullability(value);
        return this;
    }

    /**
     * Whether this forced type should apply to all object types, or only to specific ones
     * 
     */
    public ForcedType withObjectType(ForcedTypeObjectType value) {
        setObjectType(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("priority", priority);
        builder.append("name", name);
        builder.append("userType", userType);
        builder.append("visibilityModifier", visibilityModifier);
        builder.append("generator", generator);
        builder.append("auditInsertTimestamp", auditInsertTimestamp);
        builder.append("auditInsertUser", auditInsertUser);
        builder.append("auditUpdateTimestamp", auditUpdateTimestamp);
        builder.append("auditUpdateUser", auditUpdateUser);
        builder.append("converter", converter);
        builder.append("enumConverter", enumConverter);
        builder.append("lambdaConverter", lambdaConverter);
        builder.append("binding", binding);
        builder.append("excludeExpression", excludeExpression);
        builder.append("includeExpression", includeExpression);
        builder.append("expression", expression);
        builder.append("expressions", expressions);
        builder.append("sql", sql);
        builder.append("excludeTypes", excludeTypes);
        builder.append("includeTypes", includeTypes);
        builder.append("types", types);
        builder.append("nullability", nullability);
        builder.append("objectType", objectType);
    }

    @Override
    public String toString() {
        XMLBuilder builder = XMLBuilder.nonFormatting();
        appendTo(builder);
        return builder.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass()!= that.getClass()) {
            return false;
        }
        ForcedType other = ((ForcedType) that);
        if (priority == null) {
            if (other.priority!= null) {
                return false;
            }
        } else {
            if (!priority.equals(other.priority)) {
                return false;
            }
        }
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
                return false;
            }
        }
        if (userType == null) {
            if (other.userType!= null) {
                return false;
            }
        } else {
            if (!userType.equals(other.userType)) {
                return false;
            }
        }
        if (visibilityModifier == null) {
            if (other.visibilityModifier!= null) {
                return false;
            }
        } else {
            if (!visibilityModifier.equals(other.visibilityModifier)) {
                return false;
            }
        }
        if (generator == null) {
            if (other.generator!= null) {
                return false;
            }
        } else {
            if (!generator.equals(other.generator)) {
                return false;
            }
        }
        if (auditInsertTimestamp == null) {
            if (other.auditInsertTimestamp!= null) {
                return false;
            }
        } else {
            if (!auditInsertTimestamp.equals(other.auditInsertTimestamp)) {
                return false;
            }
        }
        if (auditInsertUser == null) {
            if (other.auditInsertUser!= null) {
                return false;
            }
        } else {
            if (!auditInsertUser.equals(other.auditInsertUser)) {
                return false;
            }
        }
        if (auditUpdateTimestamp == null) {
            if (other.auditUpdateTimestamp!= null) {
                return false;
            }
        } else {
            if (!auditUpdateTimestamp.equals(other.auditUpdateTimestamp)) {
                return false;
            }
        }
        if (auditUpdateUser == null) {
            if (other.auditUpdateUser!= null) {
                return false;
            }
        } else {
            if (!auditUpdateUser.equals(other.auditUpdateUser)) {
                return false;
            }
        }
        if (converter == null) {
            if (other.converter!= null) {
                return false;
            }
        } else {
            if (!converter.equals(other.converter)) {
                return false;
            }
        }
        if (enumConverter == null) {
            if (other.enumConverter!= null) {
                return false;
            }
        } else {
            if (!enumConverter.equals(other.enumConverter)) {
                return false;
            }
        }
        if (lambdaConverter == null) {
            if (other.lambdaConverter!= null) {
                return false;
            }
        } else {
            if (!lambdaConverter.equals(other.lambdaConverter)) {
                return false;
            }
        }
        if (binding == null) {
            if (other.binding!= null) {
                return false;
            }
        } else {
            if (!binding.equals(other.binding)) {
                return false;
            }
        }
        if (excludeExpression == null) {
            if (other.excludeExpression!= null) {
                return false;
            }
        } else {
            if (!excludeExpression.equals(other.excludeExpression)) {
                return false;
            }
        }
        if (includeExpression == null) {
            if (other.includeExpression!= null) {
                return false;
            }
        } else {
            if (!includeExpression.equals(other.includeExpression)) {
                return false;
            }
        }
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (expressions == null) {
            if (other.expressions!= null) {
                return false;
            }
        } else {
            if (!expressions.equals(other.expressions)) {
                return false;
            }
        }
        if (sql == null) {
            if (other.sql!= null) {
                return false;
            }
        } else {
            if (!sql.equals(other.sql)) {
                return false;
            }
        }
        if (excludeTypes == null) {
            if (other.excludeTypes!= null) {
                return false;
            }
        } else {
            if (!excludeTypes.equals(other.excludeTypes)) {
                return false;
            }
        }
        if (includeTypes == null) {
            if (other.includeTypes!= null) {
                return false;
            }
        } else {
            if (!includeTypes.equals(other.includeTypes)) {
                return false;
            }
        }
        if (types == null) {
            if (other.types!= null) {
                return false;
            }
        } else {
            if (!types.equals(other.types)) {
                return false;
            }
        }
        if (nullability == null) {
            if (other.nullability!= null) {
                return false;
            }
        } else {
            if (!nullability.equals(other.nullability)) {
                return false;
            }
        }
        if (objectType == null) {
            if (other.objectType!= null) {
                return false;
            }
        } else {
            if (!objectType.equals(other.objectType)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((priority == null)? 0 :priority.hashCode()));
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((userType == null)? 0 :userType.hashCode()));
        result = ((prime*result)+((visibilityModifier == null)? 0 :visibilityModifier.hashCode()));
        result = ((prime*result)+((generator == null)? 0 :generator.hashCode()));
        result = ((prime*result)+((auditInsertTimestamp == null)? 0 :auditInsertTimestamp.hashCode()));
        result = ((prime*result)+((auditInsertUser == null)? 0 :auditInsertUser.hashCode()));
        result = ((prime*result)+((auditUpdateTimestamp == null)? 0 :auditUpdateTimestamp.hashCode()));
        result = ((prime*result)+((auditUpdateUser == null)? 0 :auditUpdateUser.hashCode()));
        result = ((prime*result)+((converter == null)? 0 :converter.hashCode()));
        result = ((prime*result)+((enumConverter == null)? 0 :enumConverter.hashCode()));
        result = ((prime*result)+((lambdaConverter == null)? 0 :lambdaConverter.hashCode()));
        result = ((prime*result)+((binding == null)? 0 :binding.hashCode()));
        result = ((prime*result)+((excludeExpression == null)? 0 :excludeExpression.hashCode()));
        result = ((prime*result)+((includeExpression == null)? 0 :includeExpression.hashCode()));
        result = ((prime*result)+((expression == null)? 0 :expression.hashCode()));
        result = ((prime*result)+((expressions == null)? 0 :expressions.hashCode()));
        result = ((prime*result)+((sql == null)? 0 :sql.hashCode()));
        result = ((prime*result)+((excludeTypes == null)? 0 :excludeTypes.hashCode()));
        result = ((prime*result)+((includeTypes == null)? 0 :includeTypes.hashCode()));
        result = ((prime*result)+((types == null)? 0 :types.hashCode()));
        result = ((prime*result)+((nullability == null)? 0 :nullability.hashCode()));
        result = ((prime*result)+((objectType == null)? 0 :objectType.hashCode()));
        return result;
    }

}
