
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
 * Declarative naming strategy configuration for foreign key names.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MatchersForeignKeyType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MatchersForeignKeyType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31900L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    protected MatcherRule methodName;
    protected MatcherRule methodNameInverse;
    protected MatcherRule methodNameManyToMany;

    /**
     * This table matcher applies to all unqualified or qualified table names matched by this expression. If left empty, this matcher applies to all tables.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * This table matcher applies to all unqualified or qualified table names matched by this expression. If left empty, this matcher applies to all tables.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * This rule influences the naming of the generated to-one path join methods.
     * 
     */
    public MatcherRule getMethodName() {
        return methodName;
    }

    /**
     * This rule influences the naming of the generated to-one path join methods.
     * 
     */
    public void setMethodName(MatcherRule value) {
        this.methodName = value;
    }

    /**
     * This rule influences the naming of the generated to-many path join methods.
     * 
     */
    public MatcherRule getMethodNameInverse() {
        return methodNameInverse;
    }

    /**
     * This rule influences the naming of the generated to-many path join methods.
     * 
     */
    public void setMethodNameInverse(MatcherRule value) {
        this.methodNameInverse = value;
    }

    /**
     * This rule influences the naming of the generated many-to-many path join methods.
     * 
     */
    public MatcherRule getMethodNameManyToMany() {
        return methodNameManyToMany;
    }

    /**
     * This rule influences the naming of the generated many-to-many path join methods.
     * 
     */
    public void setMethodNameManyToMany(MatcherRule value) {
        this.methodNameManyToMany = value;
    }

    /**
     * This table matcher applies to all unqualified or qualified table names matched by this expression. If left empty, this matcher applies to all tables.
     * 
     */
    public MatchersForeignKeyType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated to-one path join methods.
     * 
     */
    public MatchersForeignKeyType withMethodName(MatcherRule value) {
        setMethodName(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated to-many path join methods.
     * 
     */
    public MatchersForeignKeyType withMethodNameInverse(MatcherRule value) {
        setMethodNameInverse(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated many-to-many path join methods.
     * 
     */
    public MatchersForeignKeyType withMethodNameManyToMany(MatcherRule value) {
        setMethodNameManyToMany(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("methodName", methodName);
        builder.append("methodNameInverse", methodNameInverse);
        builder.append("methodNameManyToMany", methodNameManyToMany);
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
        MatchersForeignKeyType other = ((MatchersForeignKeyType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (methodName == null) {
            if (other.methodName!= null) {
                return false;
            }
        } else {
            if (!methodName.equals(other.methodName)) {
                return false;
            }
        }
        if (methodNameInverse == null) {
            if (other.methodNameInverse!= null) {
                return false;
            }
        } else {
            if (!methodNameInverse.equals(other.methodNameInverse)) {
                return false;
            }
        }
        if (methodNameManyToMany == null) {
            if (other.methodNameManyToMany!= null) {
                return false;
            }
        } else {
            if (!methodNameManyToMany.equals(other.methodNameManyToMany)) {
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
        result = ((prime*result)+((methodName == null)? 0 :methodName.hashCode()));
        result = ((prime*result)+((methodNameInverse == null)? 0 :methodNameInverse.hashCode()));
        result = ((prime*result)+((methodNameManyToMany == null)? 0 :methodNameManyToMany.hashCode()));
        return result;
    }

}
