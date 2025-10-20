
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
 * Declarative naming strategy configuration for catalog names.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersCatalogType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersCatalogType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31835L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule catalogClass;
    protected MatcherRule catalogIdentifier;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String catalogExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String catalogImplements;

    /**
     * This catalog matcher applies to all unqualified or qualified catalog names matched by this expression. If left empty, this matcher applies to all catalogs.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This catalog matcher applies to all unqualified or qualified catalog names matched by this expression. If left empty, this matcher applies to all catalogs.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Catalog} object.
     * 
     */
    public MatcherRule getCatalogClass() {
        return catalogClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Catalog} object.
     * 
     */
    public void setCatalogClass(MatcherRule value) {
        this.catalogClass = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Catalog} identifier.
     * 
     */
    public MatcherRule getCatalogIdentifier() {
        return catalogIdentifier;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Catalog} identifier.
     * 
     */
    public void setCatalogIdentifier(MatcherRule value) {
        this.catalogIdentifier = value;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Catalog} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.Catalog} does, so to minimise
     * unexpected behaviour, custom catalog super classes should extend {@link org.jooq.impl.CatalogImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public String getCatalogExtends() {
        return catalogExtends;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Catalog} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.Catalog} does, so to minimise
     * unexpected behaviour, custom catalog super classes should extend {@link org.jooq.impl.CatalogImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public void setCatalogExtends(String value) {
        this.catalogExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Catalog} should implement.
     * 
     */
    public String getCatalogImplements() {
        return catalogImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Catalog} should implement.
     * 
     */
    public void setCatalogImplements(String value) {
        this.catalogImplements = value;
    }

    /**
     * This catalog matcher applies to all unqualified or qualified catalog names matched by this expression. If left empty, this matcher applies to all catalogs.
     * 
     */
    public MatchersCatalogType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Catalog} object.
     * 
     */
    public MatchersCatalogType withCatalogClass(MatcherRule value) {
        setCatalogClass(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.Catalog} identifier.
     * 
     */
    public MatchersCatalogType withCatalogIdentifier(MatcherRule value) {
        setCatalogIdentifier(value);
        return this;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.Catalog} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.Catalog} does, so to minimise
     * unexpected behaviour, custom catalog super classes should extend {@link org.jooq.impl.CatalogImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public MatchersCatalogType withCatalogExtends(String value) {
        setCatalogExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.Catalog} should implement.
     * 
     */
    public MatchersCatalogType withCatalogImplements(String value) {
        setCatalogImplements(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("catalogClass", catalogClass);
        builder.append("catalogIdentifier", catalogIdentifier);
        builder.append("catalogExtends", catalogExtends);
        builder.append("catalogImplements", catalogImplements);
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
        MatchersCatalogType other = ((MatchersCatalogType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (catalogClass == null) {
            if (other.catalogClass!= null) {
                return false;
            }
        } else {
            if (!catalogClass.equals(other.catalogClass)) {
                return false;
            }
        }
        if (catalogIdentifier == null) {
            if (other.catalogIdentifier!= null) {
                return false;
            }
        } else {
            if (!catalogIdentifier.equals(other.catalogIdentifier)) {
                return false;
            }
        }
        if (catalogExtends == null) {
            if (other.catalogExtends!= null) {
                return false;
            }
        } else {
            if (!catalogExtends.equals(other.catalogExtends)) {
                return false;
            }
        }
        if (catalogImplements == null) {
            if (other.catalogImplements!= null) {
                return false;
            }
        } else {
            if (!catalogImplements.equals(other.catalogImplements)) {
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
        result = ((prime*result)+((catalogClass == null)? 0 :catalogClass.hashCode()));
        result = ((prime*result)+((catalogIdentifier == null)? 0 :catalogIdentifier.hashCode()));
        result = ((prime*result)+((catalogExtends == null)? 0 :catalogExtends.hashCode()));
        result = ((prime*result)+((catalogImplements == null)? 0 :catalogImplements.hashCode()));
        return result;
    }

}
