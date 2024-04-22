
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
 * Declarative naming strategy configuration for UDT names.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersUDTType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersUDTType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31908L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule udtClass;
    protected MatcherRule udtIdentifier;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtImplements;
    protected MatcherRule pathClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pathExtends;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pathImplements;
    protected MatcherRule recordClass;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordExtends;
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
     * This table matcher applies to all unqualified or qualified UDT names matched by this expression. If left empty, this matcher applies to all UDTs.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This table matcher applies to all unqualified or qualified UDT names matched by this expression. If left empty, this matcher applies to all UDTs.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDT} object.
     * 
     */
    public MatcherRule getUdtClass() {
        return udtClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDT} object.
     * 
     */
    public void setUdtClass(MatcherRule value) {
        this.udtClass = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDT} identifier.
     * 
     */
    public MatcherRule getUdtIdentifier() {
        return udtIdentifier;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDT} identifier.
     * 
     */
    public void setUdtIdentifier(MatcherRule value) {
        this.udtIdentifier = value;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.UDT} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.UDT} does, so to minimise
     * unexpected behaviour, custom table super classes should extend {@link org.jooq.impl.UDTImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public String getUdtExtends() {
        return udtExtends;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.UDT} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.UDT} does, so to minimise
     * unexpected behaviour, custom table super classes should extend {@link org.jooq.impl.UDTImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public void setUdtExtends(String value) {
        this.udtExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.UDT} should implement.
     * 
     */
    public String getUdtImplements() {
        return udtImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.UDT} should implement.
     * 
     */
    public void setUdtImplements(String value) {
        this.udtImplements = value;
    }

    /**
     * This rule influences the naming of the generated UDT path object.
     * 
     */
    public MatcherRule getPathClass() {
        return pathClass;
    }

    /**
     * This rule influences the naming of the generated UDT path object.
     * 
     */
    public void setPathClass(MatcherRule value) {
        this.pathClass = value;
    }

    /**
     * This string provides a super class that a generated UDT path object should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.UDTRecord} does, so to minimise
     * unexpected behaviour, custom UDT record super classes should extend {@link org.jooq.impl.UDTPathTableFieldImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public String getPathExtends() {
        return pathExtends;
    }

    /**
     * This string provides a super class that a generated UDT path object should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.UDTRecord} does, so to minimise
     * unexpected behaviour, custom UDT record super classes should extend {@link org.jooq.impl.UDTPathTableFieldImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public void setPathExtends(String value) {
        this.pathExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated UDT path object should implement.
     * 
     */
    public String getPathImplements() {
        return pathImplements;
    }

    /**
     * This string provides additional interfaces that a generated UDT path object should implement.
     * 
     */
    public void setPathImplements(String value) {
        this.pathImplements = value;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDTRecord} object.
     * 
     */
    public MatcherRule getRecordClass() {
        return recordClass;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDTRecord} object.
     * 
     */
    public void setRecordClass(MatcherRule value) {
        this.recordClass = value;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.UDTRecord} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.UDTRecord} does, so to minimise
     * unexpected behaviour, custom UDT record super classes should extend {@link org.jooq.impl.UDTRecordImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public String getRecordExtends() {
        return recordExtends;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.UDTRecord} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.UDTRecord} does, so to minimise
     * unexpected behaviour, custom UDT record super classes should extend {@link org.jooq.impl.UDTRecordImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public void setRecordExtends(String value) {
        this.recordExtends = value;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.UDTRecord} should implement.
     * 
     */
    public String getRecordImplements() {
        return recordImplements;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.UDTRecord} should implement.
     * 
     */
    public void setRecordImplements(String value) {
        this.recordImplements = value;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.UDTRecord} and/or the POJO.
     * 
     */
    public MatcherRule getInterfaceClass() {
        return interfaceClass;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.UDTRecord} and/or the POJO.
     * 
     */
    public void setInterfaceClass(MatcherRule value) {
        this.interfaceClass = value;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.UDTRecord} and/or POJO) should implement.
     * 
     */
    public String getInterfaceImplements() {
        return interfaceImplements;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.UDTRecord} and/or POJO) should implement.
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
     * This table matcher applies to all unqualified or qualified UDT names matched by this expression. If left empty, this matcher applies to all UDTs.
     * 
     */
    public MatchersUDTType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDT} object.
     * 
     */
    public MatchersUDTType withUdtClass(MatcherRule value) {
        setUdtClass(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDT} identifier.
     * 
     */
    public MatchersUDTType withUdtIdentifier(MatcherRule value) {
        setUdtIdentifier(value);
        return this;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.UDT} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.UDT} does, so to minimise
     * unexpected behaviour, custom table super classes should extend {@link org.jooq.impl.UDTImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public MatchersUDTType withUdtExtends(String value) {
        setUdtExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.UDT} should implement.
     * 
     */
    public MatchersUDTType withUdtImplements(String value) {
        setUdtImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated UDT path object.
     * 
     */
    public MatchersUDTType withPathClass(MatcherRule value) {
        setPathClass(value);
        return this;
    }

    /**
     * This string provides a super class that a generated UDT path object should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.UDTRecord} does, so to minimise
     * unexpected behaviour, custom UDT record super classes should extend {@link org.jooq.impl.UDTPathTableFieldImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public MatchersUDTType withPathExtends(String value) {
        setPathExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated UDT path object should implement.
     * 
     */
    public MatchersUDTType withPathImplements(String value) {
        setPathImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated {@link org.jooq.UDTRecord} object.
     * 
     */
    public MatchersUDTType withRecordClass(MatcherRule value) {
        setRecordClass(value);
        return this;
    }

    /**
     * This string provides a super class that a generated {@link org.jooq.UDTRecord} should extend.
     * <p>        
     * jOOQ internals make a few assumptions about what a {@link org.jooq.UDTRecord} does, so to minimise
     * unexpected behaviour, custom UDT record super classes should extend {@link org.jooq.impl.UDTRecordImpl}
     * and follow its (undocumented!) assumptions (e.g. constructors, etc.). Use this at your own risk.
     * 
     */
    public MatchersUDTType withRecordExtends(String value) {
        setRecordExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated {@link org.jooq.UDTRecord} should implement.
     * 
     */
    public MatchersUDTType withRecordImplements(String value) {
        setRecordImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated interface implemented by the {@link org.jooq.UDTRecord} and/or the POJO.
     * 
     */
    public MatchersUDTType withInterfaceClass(MatcherRule value) {
        setInterfaceClass(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated interface (which is implemented by the {@link org.jooq.UDTRecord} and/or POJO) should implement.
     * 
     */
    public MatchersUDTType withInterfaceImplements(String value) {
        setInterfaceImplements(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated POJOs object.
     * 
     */
    public MatchersUDTType withPojoClass(MatcherRule value) {
        setPojoClass(value);
        return this;
    }

    /**
     * This string provides a super class that a generated POJO should extend.
     * 
     */
    public MatchersUDTType withPojoExtends(String value) {
        setPojoExtends(value);
        return this;
    }

    /**
     * This string provides additional interfaces that a generated POJO should implement.
     * 
     */
    public MatchersUDTType withPojoImplements(String value) {
        setPojoImplements(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("udtClass", udtClass);
        builder.append("udtIdentifier", udtIdentifier);
        builder.append("udtExtends", udtExtends);
        builder.append("udtImplements", udtImplements);
        builder.append("pathClass", pathClass);
        builder.append("pathExtends", pathExtends);
        builder.append("pathImplements", pathImplements);
        builder.append("recordClass", recordClass);
        builder.append("recordExtends", recordExtends);
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
        MatchersUDTType other = ((MatchersUDTType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (udtClass == null) {
            if (other.udtClass!= null) {
                return false;
            }
        } else {
            if (!udtClass.equals(other.udtClass)) {
                return false;
            }
        }
        if (udtIdentifier == null) {
            if (other.udtIdentifier!= null) {
                return false;
            }
        } else {
            if (!udtIdentifier.equals(other.udtIdentifier)) {
                return false;
            }
        }
        if (udtExtends == null) {
            if (other.udtExtends!= null) {
                return false;
            }
        } else {
            if (!udtExtends.equals(other.udtExtends)) {
                return false;
            }
        }
        if (udtImplements == null) {
            if (other.udtImplements!= null) {
                return false;
            }
        } else {
            if (!udtImplements.equals(other.udtImplements)) {
                return false;
            }
        }
        if (pathClass == null) {
            if (other.pathClass!= null) {
                return false;
            }
        } else {
            if (!pathClass.equals(other.pathClass)) {
                return false;
            }
        }
        if (pathExtends == null) {
            if (other.pathExtends!= null) {
                return false;
            }
        } else {
            if (!pathExtends.equals(other.pathExtends)) {
                return false;
            }
        }
        if (pathImplements == null) {
            if (other.pathImplements!= null) {
                return false;
            }
        } else {
            if (!pathImplements.equals(other.pathImplements)) {
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
        if (recordExtends == null) {
            if (other.recordExtends!= null) {
                return false;
            }
        } else {
            if (!recordExtends.equals(other.recordExtends)) {
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
        result = ((prime*result)+((udtClass == null)? 0 :udtClass.hashCode()));
        result = ((prime*result)+((udtIdentifier == null)? 0 :udtIdentifier.hashCode()));
        result = ((prime*result)+((udtExtends == null)? 0 :udtExtends.hashCode()));
        result = ((prime*result)+((udtImplements == null)? 0 :udtImplements.hashCode()));
        result = ((prime*result)+((pathClass == null)? 0 :pathClass.hashCode()));
        result = ((prime*result)+((pathExtends == null)? 0 :pathExtends.hashCode()));
        result = ((prime*result)+((pathImplements == null)? 0 :pathImplements.hashCode()));
        result = ((prime*result)+((recordClass == null)? 0 :recordClass.hashCode()));
        result = ((prime*result)+((recordExtends == null)? 0 :recordExtends.hashCode()));
        result = ((prime*result)+((recordImplements == null)? 0 :recordImplements.hashCode()));
        result = ((prime*result)+((interfaceClass == null)? 0 :interfaceClass.hashCode()));
        result = ((prime*result)+((interfaceImplements == null)? 0 :interfaceImplements.hashCode()));
        result = ((prime*result)+((pojoClass == null)? 0 :pojoClass.hashCode()));
        result = ((prime*result)+((pojoExtends == null)? 0 :pojoExtends.hashCode()));
        result = ((prime*result)+((pojoImplements == null)? 0 :pojoImplements.hashCode()));
        return result;
    }

}
