
package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * The documentation configuration.
 * <p>
 * This feature is available in the commercial distribution only.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommentType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class CommentType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31500L;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String expression;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String message;
    @XmlElement(defaultValue = "false")
    protected Boolean deprecated = false;
    @XmlElement(defaultValue = "true")
    protected Boolean includeSchemaComment = true;

    /**
     * A regular expression matching all objects that should be commented.
     * 
     */
    public String getExpression() {
        return expression;
    }

    /**
     * A regular expression matching all objects that should be commented.
     * 
     */
    public void setExpression(String value) {
        this.expression = value;
    }

    /**
     * A comment that should be added to objects matched by this configuration.
     * 
     */
    public String getMessage() {
        return message;
    }

    /**
     * A comment that should be added to objects matched by this configuration.
     * 
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Whether the comment is a deprecation notice.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeprecated() {
        return deprecated;
    }

    /**
     * Sets the value of the deprecated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeprecated(Boolean value) {
        this.deprecated = value;
    }

    /**
     * Whether the schema comment (if available) should be included and prepended to the message.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeSchemaComment() {
        return includeSchemaComment;
    }

    /**
     * Sets the value of the includeSchemaComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeSchemaComment(Boolean value) {
        this.includeSchemaComment = value;
    }

    /**
     * A regular expression matching all objects that should be commented.
     * 
     */
    public CommentType withExpression(String value) {
        setExpression(value);
        return this;
    }

    /**
     * A comment that should be added to objects matched by this configuration.
     * 
     */
    public CommentType withMessage(String value) {
        setMessage(value);
        return this;
    }

    public CommentType withDeprecated(Boolean value) {
        setDeprecated(value);
        return this;
    }

    public CommentType withIncludeSchemaComment(Boolean value) {
        setIncludeSchemaComment(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("expression", expression);
        builder.append("message", message);
        builder.append("deprecated", deprecated);
        builder.append("includeSchemaComment", includeSchemaComment);
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
        CommentType other = ((CommentType) that);
        if (expression == null) {
            if (other.expression!= null) {
                return false;
            }
        } else {
            if (!expression.equals(other.expression)) {
                return false;
            }
        }
        if (message == null) {
            if (other.message!= null) {
                return false;
            }
        } else {
            if (!message.equals(other.message)) {
                return false;
            }
        }
        if (deprecated == null) {
            if (other.deprecated!= null) {
                return false;
            }
        } else {
            if (!deprecated.equals(other.deprecated)) {
                return false;
            }
        }
        if (includeSchemaComment == null) {
            if (other.includeSchemaComment!= null) {
                return false;
            }
        } else {
            if (!includeSchemaComment.equals(other.includeSchemaComment)) {
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
        result = ((prime*result)+((message == null)? 0 :message.hashCode()));
        result = ((prime*result)+((deprecated == null)? 0 :deprecated.hashCode()));
        result = ((prime*result)+((includeSchemaComment == null)? 0 :includeSchemaComment.hashCode()));
        return result;
    }

}
