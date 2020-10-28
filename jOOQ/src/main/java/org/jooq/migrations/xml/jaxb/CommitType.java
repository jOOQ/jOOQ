
package org.jooq.migrations.xml.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for CommitType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CommitType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="parents" type="{http://www.jooq.org/xsd/jooq-migrations-3.15.0.xsd}ParentsType" minOccurs="0"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="files" type="{http://www.jooq.org/xsd/jooq-migrations-3.15.0.xsd}FilesType" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CommitType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class CommitType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31500L;
    @XmlElement(required = true)
    protected String id;
    protected String message;
    @XmlElementWrapper(name = "parents")
    @XmlElement(name = "parent")
    protected List<ParentType> parents;
    @XmlElementWrapper(name = "files")
    @XmlElement(name = "file")
    protected List<FileType> files;

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public List<ParentType> getParents() {
        if (parents == null) {
            parents = new ArrayList<ParentType>();
        }
        return parents;
    }

    public void setParents(List<ParentType> parents) {
        this.parents = parents;
    }

    public List<FileType> getFiles() {
        if (files == null) {
            files = new ArrayList<FileType>();
        }
        return files;
    }

    public void setFiles(List<FileType> files) {
        this.files = files;
    }

    public CommitType withId(String value) {
        setId(value);
        return this;
    }

    public CommitType withMessage(String value) {
        setMessage(value);
        return this;
    }

    public CommitType withParents(ParentType... values) {
        if (values!= null) {
            for (ParentType value: values) {
                getParents().add(value);
            }
        }
        return this;
    }

    public CommitType withParents(Collection<ParentType> values) {
        if (values!= null) {
            getParents().addAll(values);
        }
        return this;
    }

    public CommitType withParents(List<ParentType> parents) {
        setParents(parents);
        return this;
    }

    public CommitType withFiles(FileType... values) {
        if (values!= null) {
            for (FileType value: values) {
                getFiles().add(value);
            }
        }
        return this;
    }

    public CommitType withFiles(Collection<FileType> values) {
        if (values!= null) {
            getFiles().addAll(values);
        }
        return this;
    }

    public CommitType withFiles(List<FileType> files) {
        setFiles(files);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("id", id);
        builder.append("message", message);
        builder.append("parents", "parent", parents);
        builder.append("files", "file", files);
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
        CommitType other = ((CommitType) that);
        if (id == null) {
            if (other.id!= null) {
                return false;
            }
        } else {
            if (!id.equals(other.id)) {
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
        if (parents == null) {
            if (other.parents!= null) {
                return false;
            }
        } else {
            if (!parents.equals(other.parents)) {
                return false;
            }
        }
        if (files == null) {
            if (other.files!= null) {
                return false;
            }
        } else {
            if (!files.equals(other.files)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((id == null)? 0 :id.hashCode()));
        result = ((prime*result)+((message == null)? 0 :message.hashCode()));
        result = ((prime*result)+((parents == null)? 0 :parents.hashCode()));
        result = ((prime*result)+((files == null)? 0 :files.hashCode()));
        return result;
    }

}
