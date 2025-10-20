
package org.jooq.meta.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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

    private final static long serialVersionUID = 31835L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule schemaClass;
    protected MatcherRule schemaIdentifier;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schemaExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schemaImplements;

    /**
     * This schema matcher applies to all unqualified or qualified schema names matched by this expression. If left empty, this matcher applies to all schemas.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This schema matcher applies to all unqualified or qualified schema names matched by this expression. If left empty, this matcher applies to all schemas.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Schema} object.
     * 
     */
    public MatcherRule getSchemaClass() {
        return schemaClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Schema} object.
     * 
     */
    public void setSchemaClass(MatcherRule value) {
        this.schemaClass = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Schema} identifier.
     * 
     */
    public MatcherRule getSchemaIdentifier() {
        return schemaIdentifier;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Schema} identifier.
     * 
     */
    public void setSchemaIdentifier(MatcherRule value) {
        this.schemaIdentifier = value;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Schema} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.Schema} does, so to minimise
     * unexpected behaviour, custom schema super classes should extend {@link org.jooq.impl.SchemaImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public String getSchemaExtends() {
        return schemaExtends;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Schema} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.Schema} does, so to minimise
     * unexpected behaviour, custom schema super classes should extend {@link org.jooq.impl.SchemaImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public void setSchemaExtends(String value) {
        this.schemaExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Schema} should implement.
     * 
     */
    public String getSchemaImplements() {
        return schemaImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Schema} should implement.
     * 
     */
    public void setSchemaImplements(String value) {
        this.schemaImplements = value;
    }

    /**
     * This schema matcher applies to all unqualified or qualified schema names matched by this expression. If left empty, this matcher applies to all schemas.
     * 
     */
    public MatchersSchemaType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Schema} object.
     * 
     */
    public MatchersSchemaType withSchemaClass(MatcherRule value) {
        setSchemaClass(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Schema} identifier.
     * 
     */
    public MatchersSchemaType withSchemaIdentifier(MatcherRule value) {
        setSchemaIdentifier(value);
        return this;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Schema} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.Schema} does, so to minimise
     * unexpected behaviour, custom schema super classes should extend {@link org.jooq.impl.SchemaImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public MatchersSchemaType withSchemaExtends(String value) {
        setSchemaExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Schema} should implement.
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
        builder.append("schemaExtends", schemaExtends);
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
        if (schemaExtends == null) {
            if (other.schemaExtends!= null) {
                return false;
            }
        } else {
            if (!schemaExtends.equals(other.schemaExtends)) {
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
        result = ((prime*result)+((schemaExtends == null)? 0 :schemaExtends.hashCode()));
        result = ((prime*result)+((schemaImplements == null)? 0 :schemaImplements.hashCode()));
        return result;
    }

}
