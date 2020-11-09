
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
 * Declarative naming strategy configuration for embeddable names.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersEmbeddableType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersEmbeddableType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31500L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule recordClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordImplements;
    protected MatcherRule interfaceClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String interfaceImplements;
    protected MatcherRule pojoClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojoExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojoImplements;

    /**
     * This table matcher applies to all unqualified or qualified embeddable names matched by this expression. If left empty, this matcher applies to all embeddables.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This table matcher applies to all unqualified or qualified embeddable names matched by this expression. If left empty, this matcher applies to all embeddables.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.EmbeddableRecord} object.
     * 
     */
    public MatcherRule getRecordClass() {
        return recordClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.EmbeddableRecord} object.
     * 
     */
    public void setRecordClass(MatcherRule value) {
        this.recordClass = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.EmbeddableRecord} should implement.
     * 
     */
    public String getRecordImplements() {
        return recordImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.EmbeddableRecord} should implement.
     * 
     */
    public void setRecordImplements(String value) {
        this.recordImplements = value;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.EmbeddableRecord} and/or the POJO.
     * 
     */
    public MatcherRule getInterfaceClass() {
        return interfaceClass;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.EmbeddableRecord} and/or the POJO.
     * 
     */
    public void setInterfaceClass(MatcherRule value) {
        this.interfaceClass = value;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.EmbeddableRecord} and/or POJO) should implement.
     * 
     */
    public String getInterfaceImplements() {
        return interfaceImplements;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.EmbeddableRecord} and/or POJO) should implement.
     * 
     */
    public void setInterfaceImplements(String value) {
        this.interfaceImplements = value;
    }

    /**
     * This rule influences the naming of the generated POJOs object.
     * 
     */
    public MatcherRule getPojoClass() {
        return pojoClass;
    }

    /**
     * This rule influences the naming of the generated POJOs object.
     * 
     */
    public void setPojoClass(MatcherRule value) {
        this.pojoClass = value;
    }

    /**
     * This string provides a super class that a generated POJO should extend.
     * 
     */
    public String getPojoExtends() {
        return pojoExtends;
    }

    /**
     * This string provides a super class that a generated POJO should extend.
     * 
     */
    public void setPojoExtends(String value) {
        this.pojoExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated POJO should implement.
     * 
     */
    public String getPojoImplements() {
        return pojoImplements;
    }

    /**
     * This string provides additional interfaces that a generated POJO should implement.
     * 
     */
    public void setPojoImplements(String value) {
        this.pojoImplements = value;
    }

    /**
     * This table matcher applies to all unqualified or qualified embeddable names matched by this expression. If left empty, this matcher applies to all embeddables.
     * 
     */
    public MatchersEmbeddableType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.EmbeddableRecord} object.
     * 
     */
    public MatchersEmbeddableType withRecordClass(MatcherRule value) {
        setRecordClass(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.EmbeddableRecord} should implement.
     * 
     */
    public MatchersEmbeddableType withRecordImplements(String value) {
        setRecordImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.EmbeddableRecord} and/or the POJO.
     * 
     */
    public MatchersEmbeddableType withInterfaceClass(MatcherRule value) {
        setInterfaceClass(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.EmbeddableRecord} and/or POJO) should implement.
     * 
     */
    public MatchersEmbeddableType withInterfaceImplements(String value) {
        setInterfaceImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated POJOs object.
     * 
     */
    public MatchersEmbeddableType withPojoClass(MatcherRule value) {
        setPojoClass(value);
        return this;
    }

    /**
     * This string provides a super class that a generated POJO should extend.
     * 
     */
    public MatchersEmbeddableType withPojoExtends(String value) {
        setPojoExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated POJO should implement.
     * 
     */
    public MatchersEmbeddableType withPojoImplements(String value) {
        setPojoImplements(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("recordClass", recordClass);
        builder.append("recordImplements", recordImplements);
        builder.append("interfaceClass", interfaceClass);
        builder.append("interfaceImplements", interfaceImplements);
        builder.append("pojoClass", pojoClass);
        builder.append("pojoExtends", pojoExtends);
        builder.append("pojoImplements", pojoImplements);
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
        MatchersEmbeddableType other = ((MatchersEmbeddableType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (recordClass == null) {
            if (other.recordClass!= null) {
                return false;
            }
        } else {
            if (!recordClass.equals(other.recordClass)) {
                return false;
            }
        }
        if (recordImplements == null) {
            if (other.recordImplements!= null) {
                return false;
            }
        } else {
            if (!recordImplements.equals(other.recordImplements)) {
                return false;
            }
        }
        if (interfaceClass == null) {
            if (other.interfaceClass!= null) {
                return false;
            }
        } else {
            if (!interfaceClass.equals(other.interfaceClass)) {
                return false;
            }
        }
        if (interfaceImplements == null) {
            if (other.interfaceImplements!= null) {
                return false;
            }
        } else {
            if (!interfaceImplements.equals(other.interfaceImplements)) {
                return false;
            }
        }
        if (pojoClass == null) {
            if (other.pojoClass!= null) {
                return false;
            }
        } else {
            if (!pojoClass.equals(other.pojoClass)) {
                return false;
            }
        }
        if (pojoExtends == null) {
            if (other.pojoExtends!= null) {
                return false;
            }
        } else {
            if (!pojoExtends.equals(other.pojoExtends)) {
                return false;
            }
        }
        if (pojoImplements == null) {
            if (other.pojoImplements!= null) {
                return false;
            }
        } else {
            if (!pojoImplements.equals(other.pojoImplements)) {
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
        result = ((prime*result)+((recordClass == null)? 0 :recordClass.hashCode()));
        result = ((prime*result)+((recordImplements == null)? 0 :recordImplements.hashCode()));
        result = ((prime*result)+((interfaceClass == null)? 0 :interfaceClass.hashCode()));
        result = ((prime*result)+((interfaceImplements == null)? 0 :interfaceImplements.hashCode()));
        result = ((prime*result)+((pojoClass == null)? 0 :pojoClass.hashCode()));
        result = ((prime*result)+((pojoExtends == null)? 0 :pojoExtends.hashCode()));
        result = ((prime*result)+((pojoImplements == null)? 0 :pojoImplements.hashCode()));
        return result;
    }

}
