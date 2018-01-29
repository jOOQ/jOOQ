







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


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
public class ForcedType implements Serializable
{

    private final static long serialVersionUID = 31100L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String userType;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String converter;
    protected Boolean enumConverter;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String binding;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expressions;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String types;
    @XmlElement(defaultValue = "ALL")
    @XmlSchemaType(name = "string")
    protected Nullability nullability = Nullability.ALL;

    /**
     * The name (in {@link org.jooq.impl.SQLDataType}) to force any matches to
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
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
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Sets the value of the userType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUserType(String value) {
        this.userType = value;
    }

    /**
     * A converter implementation for the {@link #getUserType()}.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getConverter() {
        return converter;
    }

    /**
     * Sets the value of the converter property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
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
     * A {@link org.jooq.Binding} implementation for the custom type.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBinding() {
        return binding;
    }

    /**
     * Sets the value of the binding property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBinding(String value) {
        this.binding = value;
    }

    /**
     * A Java regular expression matching columns, parameters, attributes,
     * etc to be forced to have this type. If provided, both "expressions" and
     * "types" must match.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getExpression() {
        return expression;
    }

    /**
     * Sets the value of the expression property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * The same as expression. This is kept for backwards compatibility reasons.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getExpressions() {
        return expressions;
    }

    /**
     * Sets the value of the expressions property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setExpressions(String value) {
        this.expressions = value;
    }

    /**
     * A Java regular expression matching data types to be forced to have this
     * type. If provided, both "expression" and "types" must match.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTypes() {
        return types;
    }

    /**
     * Sets the value of the types property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTypes(String value) {
        this.types = value;
    }

    /**
     * Whether this forced type should apply to nullable / non-nullable / all columns
     *
     * @return
     *     possible object is
     *     {@link Nullability }
     *
     */
    public Nullability getNullability() {
        return nullability;
    }

    /**
     * Sets the value of the nullability property.
     *
     * @param value
     *     allowed object is
     *     {@link Nullability }
     *
     */
    public void setNullability(Nullability value) {
        this.nullability = value;
    }

    public ForcedType withName(String value) {
        setName(value);
        return this;
    }

    public ForcedType withUserType(String value) {
        setUserType(value);
        return this;
    }

    public ForcedType withConverter(String value) {
        setConverter(value);
        return this;
    }

    public ForcedType withEnumConverter(Boolean value) {
        setEnumConverter(value);
        return this;
    }

    public ForcedType withBinding(String value) {
        setBinding(value);
        return this;
    }

    public ForcedType withExpression(String value) {
        setExpression(value);
        return this;
    }

    public ForcedType withExpressions(String value) {
        setExpressions(value);
        return this;
    }

    public ForcedType withTypes(String value) {
        setTypes(value);
        return this;
    }

    public ForcedType withNullability(Nullability value) {
        setNullability(value);
        return this;
    }

}
