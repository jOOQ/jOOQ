
package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


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
public class MatchersSchemaType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
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

    /**
     * Sets the value of the expression property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public MatchersSchemaType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * Sets the value of the schemaClass property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public MatchersSchemaType withSchemaClass(MatcherRule value) {
        setSchemaClass(value);
        return this;
    }

    /**
     * Sets the value of the schemaIdentifier property.
     *
     * @param value
     *     allowed object is
     *     {@link MatcherRule }
     *
     */
    public MatchersSchemaType withSchemaIdentifier(MatcherRule value) {
        setSchemaIdentifier(value);
        return this;
    }

    /**
     * Sets the value of the schemaImplements property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public MatchersSchemaType withSchemaImplements(String value) {
        setSchemaImplements(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("schemaClass", schemaClass);
        builder.append("schemaIdentifier", schemaIdentifier);
        builder.append("schemaImplements", schemaImplements);
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
        MatchersSchemaType other = ((MatchersSchemaType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (schemaClass == null) {
            if (other.schemaClass!= null) {
                return false;
            }
        } else {
            if (!schemaClass.equals(other.schemaClass)) {
                return false;
            }
        }
        if (schemaIdentifier == null) {
            if (other.schemaIdentifier!= null) {
                return false;
            }
        } else {
            if (!schemaIdentifier.equals(other.schemaIdentifier)) {
                return false;
            }
        }
        if (schemaImplements == null) {
            if (other.schemaImplements!= null) {
                return false;
            }
        } else {
            if (!schemaImplements.equals(other.schemaImplements)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((expression == null)? 0 :expression.hashCode()));
        result = ((prime*result)+((schemaClass == null)? 0 :schemaClass.hashCode()));
        result = ((prime*result)+((schemaIdentifier == null)? 0 :schemaIdentifier.hashCode()));
        result = ((prime*result)+((schemaImplements == null)? 0 :schemaImplements.hashCode()));
        return result;
    }

}
