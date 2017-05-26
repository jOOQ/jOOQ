







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * Declarative naming strategy configuration for schema names.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersSchemaType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersSchemaType implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule schemaClass;
    protected MatcherRule schemaIdentifier;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schemaImplements;

    /**
     * This schema matcher applies to all unqualified or qualified schema names matched by this expression. If left empty, this matcher applies to all schemas.
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
     * This rule influences the naming of the generated {@link org.jooq.Schema} object.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getSchemaClass() {
        return schemaClass;
    }

    /**
     * Sets the value of the schemaClass property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setSchemaClass(MatcherRule value) {
        this.schemaClass = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Schema} identifier.
     *
     * @return
     *     possible object is
     *     {@link MatcherRule }
     *
     */
    public MatcherRule getSchemaIdentifier() {
        return schemaIdentifier;
    }

    /**
     * Sets the value of the schemaIdentifier property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public void setSchemaIdentifier(MatcherRule value) {
        this.schemaIdentifier = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Schema} should implement.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSchemaImplements() {
        return schemaImplements;
    }

    /**
     * Sets the value of the schemaImplements property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSchemaImplements(String value) {
        this.schemaImplements = value;
    }

    public MatchersSchemaType withExpression(String value) {
        setExpression(value);
        return this;
    }

    public MatchersSchemaType withSchemaClass(MatcherRule value) {
        setSchemaClass(value);
        return this;
    }

    public MatchersSchemaType withSchemaIdentifier(MatcherRule value) {
        setSchemaIdentifier(value);
        return this;
    }

    public MatchersSchemaType withSchemaImplements(String value) {
        setSchemaImplements(value);
        return this;
    }

}
