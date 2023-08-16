
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
    protected MatcherRule keyIdentifier;
    protected MatcherRule pathMethodName;
    protected MatcherRule pathMethodNameInverse;
    protected MatcherRule pathMethodNameManyToMany;

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
     * This rule influences the naming of the generated key literal in the Keys class.
     * 
     */
    public MatcherRule getKeyIdentifier() {
        return keyIdentifier;
    }

    /**
     * This rule influences the naming of the generated key literal in the Keys class.
     * 
     */
    public void setKeyIdentifier(MatcherRule value) {
        this.keyIdentifier = value;
    }

    /**
     * This rule influences the naming of the generated to-one path join methods.
     * 
     */
    public MatcherRule getPathMethodName() {
        return pathMethodName;
    }

    /**
     * This rule influences the naming of the generated to-one path join methods.
     * 
     */
    public void setPathMethodName(MatcherRule value) {
        this.pathMethodName = value;
    }

    /**
     * This rule influences the naming of the generated to-many path join methods.
     * 
     */
    public MatcherRule getPathMethodNameInverse() {
        return pathMethodNameInverse;
    }

    /**
     * This rule influences the naming of the generated to-many path join methods.
     * 
     */
    public void setPathMethodNameInverse(MatcherRule value) {
        this.pathMethodNameInverse = value;
    }

    /**
     * This rule influences the naming of the generated many-to-many path join methods.
     * 
     */
    public MatcherRule getPathMethodNameManyToMany() {
        return pathMethodNameManyToMany;
    }

    /**
     * This rule influences the naming of the generated many-to-many path join methods.
     * 
     */
    public void setPathMethodNameManyToMany(MatcherRule value) {
        this.pathMethodNameManyToMany = value;
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
     * This rule influences the naming of the generated key literal in the Keys class.
     * 
     */
    public MatchersForeignKeyType withKeyIdentifier(MatcherRule value) {
        setKeyIdentifier(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated to-one path join methods.
     * 
     */
    public MatchersForeignKeyType withPathMethodName(MatcherRule value) {
        setPathMethodName(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated to-many path join methods.
     * 
     */
    public MatchersForeignKeyType withPathMethodNameInverse(MatcherRule value) {
        setPathMethodNameInverse(value);
        return this;
    }

    /**
     * This rule influences the naming of the generated many-to-many path join methods.
     * 
     */
    public MatchersForeignKeyType withPathMethodNameManyToMany(MatcherRule value) {
        setPathMethodNameManyToMany(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("keyIdentifier", keyIdentifier);
        builder.append("pathMethodName", pathMethodName);
        builder.append("pathMethodNameInverse", pathMethodNameInverse);
        builder.append("pathMethodNameManyToMany", pathMethodNameManyToMany);
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
        if (keyIdentifier == null) {
            if (other.keyIdentifier!= null) {
                return false;
            }
        } else {
            if (!keyIdentifier.equals(other.keyIdentifier)) {
                return false;
            }
        }
        if (pathMethodName == null) {
            if (other.pathMethodName!= null) {
                return false;
            }
        } else {
            if (!pathMethodName.equals(other.pathMethodName)) {
                return false;
            }
        }
        if (pathMethodNameInverse == null) {
            if (other.pathMethodNameInverse!= null) {
                return false;
            }
        } else {
            if (!pathMethodNameInverse.equals(other.pathMethodNameInverse)) {
                return false;
            }
        }
        if (pathMethodNameManyToMany == null) {
            if (other.pathMethodNameManyToMany!= null) {
                return false;
            }
        } else {
            if (!pathMethodNameManyToMany.equals(other.pathMethodNameManyToMany)) {
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
        result = ((prime*result)+((keyIdentifier == null)? 0 :keyIdentifier.hashCode()));
        result = ((prime*result)+((pathMethodName == null)? 0 :pathMethodName.hashCode()));
        result = ((prime*result)+((pathMethodNameInverse == null)? 0 :pathMethodNameInverse.hashCode()));
        result = ((prime*result)+((pathMethodNameManyToMany == null)? 0 :pathMethodNameManyToMany.hashCode()));
        return result;
    }

}
