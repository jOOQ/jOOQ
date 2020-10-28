
package org.jooq.migrations.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.ContentType;
import org.jooq.migrations.xml.ContentTypeAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for FileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FileType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="path" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="contentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="change" type="{http://www.jooq.org/xsd/jooq-migrations-3.15.0.xsd}ChangeType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FileType", propOrder = {
    "path",
    "content",
    "contentType",
    "change"
})
@SuppressWarnings({
    "all"
})
public class FileType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31500L;
    @XmlElement(required = true)
    protected String path;
    protected String content;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(ContentTypeAdapter.class)
    protected ContentType contentType;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected ChangeType change;

    public String getPath() {
        return path;
    }

    public void setPath(String value) {
        this.path = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType value) {
        this.contentType = value;
    }

    public ChangeType getChange() {
        return change;
    }

    public void setChange(ChangeType value) {
        this.change = value;
    }

    public FileType withPath(String value) {
        setPath(value);
        return this;
    }

    public FileType withContent(String value) {
        setContent(value);
        return this;
    }

    public FileType withContentType(ContentType value) {
        setContentType(value);
        return this;
    }

    public FileType withChange(ChangeType value) {
        setChange(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("path", path);
        builder.append("content", content);
        builder.append("contentType", contentType);
        builder.append("change", change);
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
        FileType other = ((FileType) that);
        if (path == null) {
            if (other.path!= null) {
                return false;
            }
        } else {
            if (!path.equals(other.path)) {
                return false;
            }
        }
        if (content == null) {
            if (other.content!= null) {
                return false;
            }
        } else {
            if (!content.equals(other.content)) {
                return false;
            }
        }
        if (contentType == null) {
            if (other.contentType!= null) {
                return false;
            }
        } else {
            if (!contentType.equals(other.contentType)) {
                return false;
            }
        }
        if (change == null) {
            if (other.change!= null) {
                return false;
            }
        } else {
            if (!change.equals(other.change)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((path == null)? 0 :path.hashCode()));
        result = ((prime*result)+((content == null)? 0 :content.hashCode()));
        result = ((prime*result)+((contentType == null)? 0 :contentType.hashCode()));
        result = ((prime*result)+((change == null)? 0 :change.hashCode()));
        return result;
    }

}
