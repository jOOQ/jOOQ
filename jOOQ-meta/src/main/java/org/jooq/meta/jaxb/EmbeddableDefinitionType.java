
package org.jooq.meta.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * An embeddable type declaration
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmbeddableDefinitionType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class EmbeddableDefinitionType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31500L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String catalog;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String schema;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String referencingName;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String referencingComment;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tables;
    @XmlElement(defaultValue = "false")
    protected Boolean replacesFields = false;
    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    protected List<EmbeddableField> fields;

    /**
     * The defining catalog of the embeddable type, or the catalog of the first matched table if left empty.
     * 
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * The defining catalog of the embeddable type, or the catalog of the first matched table if left empty.
     * 
     */
    public void setCatalog(String value) {
        this.catalog = value;
    }

    /**
     * The defining schema of the embeddable type, or the schema of the first matched table if left empty.
     * 
     */
    public String getSchema() {
        return schema;
    }

    /**
     * The defining schema of the embeddable type, or the schema of the first matched table if left empty.
     * 
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * The defining name of the embeddable type.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The defining name of the embeddable type.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * The defining comment on the embeddable type.
     * 
     */
    public String getComment() {
        return comment;
    }

    /**
     * The defining comment on the embeddable type.
     * 
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * The referencing name of the embeddable type, defaulting to the defining name.
     * 
     */
    public String getReferencingName() {
        return referencingName;
    }

    /**
     * The referencing name of the embeddable type, defaulting to the defining name.
     * 
     */
    public void setReferencingName(String value) {
        this.referencingName = value;
    }

    /**
     * The referencing comment on the embeddable type, defaulting to the defining comment.
     * 
     */
    public String getReferencingComment() {
        return referencingComment;
    }

    /**
     * The referencing comment on the embeddable type, defaulting to the defining comment.
     * 
     */
    public void setReferencingComment(String value) {
        this.referencingComment = value;
    }

    /**
     * A regular expression matching the tables to which to apply the embeddable definition.
     * 
     */
    public String getTables() {
        return tables;
    }

    /**
     * A regular expression matching the tables to which to apply the embeddable definition.
     * 
     */
    public void setTables(String value) {
        this.tables = value;
    }

    /**
     * Specify that the embeddable field replaces its underlying fields in code generation output, and when working with asterisks.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReplacesFields() {
        return replacesFields;
    }

    /**
     * Sets the value of the replacesFields property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReplacesFields(Boolean value) {
        this.replacesFields = value;
    }

    public List<EmbeddableField> getFields() {
        if (fields == null) {
            fields = new ArrayList<EmbeddableField>();
        }
        return fields;
    }

    public void setFields(List<EmbeddableField> fields) {
        this.fields = fields;
    }

    /**
     * The defining catalog of the embeddable type, or the catalog of the first matched table if left empty.
     * 
     */
    public EmbeddableDefinitionType withCatalog(String value) {
        setCatalog(value);
        return this;
    }

    /**
     * The defining schema of the embeddable type, or the schema of the first matched table if left empty.
     * 
     */
    public EmbeddableDefinitionType withSchema(String value) {
        setSchema(value);
        return this;
    }

    /**
     * The defining name of the embeddable type.
     * 
     */
    public EmbeddableDefinitionType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * The defining comment on the embeddable type.
     * 
     */
    public EmbeddableDefinitionType withComment(String value) {
        setComment(value);
        return this;
    }

    /**
     * The referencing name of the embeddable type, defaulting to the defining name.
     * 
     */
    public EmbeddableDefinitionType withReferencingName(String value) {
        setReferencingName(value);
        return this;
    }

    /**
     * The referencing comment on the embeddable type, defaulting to the defining comment.
     * 
     */
    public EmbeddableDefinitionType withReferencingComment(String value) {
        setReferencingComment(value);
        return this;
    }

    /**
     * A regular expression matching the tables to which to apply the embeddable definition.
     * 
     */
    public EmbeddableDefinitionType withTables(String value) {
        setTables(value);
        return this;
    }

    public EmbeddableDefinitionType withReplacesFields(Boolean value) {
        setReplacesFields(value);
        return this;
    }

    public EmbeddableDefinitionType withFields(EmbeddableField... values) {
        if (values!= null) {
            for (EmbeddableField value: values) {
                getFields().add(value);
            }
        }
        return this;
    }

    public EmbeddableDefinitionType withFields(Collection<EmbeddableField> values) {
        if (values!= null) {
            getFields().addAll(values);
        }
        return this;
    }

    public EmbeddableDefinitionType withFields(List<EmbeddableField> fields) {
        setFields(fields);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("catalog", catalog);
        builder.append("schema", schema);
        builder.append("name", name);
        builder.append("comment", comment);
        builder.append("referencingName", referencingName);
        builder.append("referencingComment", referencingComment);
        builder.append("tables", tables);
        builder.append("replacesFields", replacesFields);
        builder.append("fields", "field", fields);
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
        EmbeddableDefinitionType other = ((EmbeddableDefinitionType) that);
        if (catalog == null) {
            if (other.catalog!= null) {
                return false;
            }
        } else {
            if (!catalog.equals(other.catalog)) {
                return false;
            }
        }
        if (schema == null) {
            if (other.schema!= null) {
                return false;
            }
        } else {
            if (!schema.equals(other.schema)) {
                return false;
            }
        }
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
                return false;
            }
        }
        if (comment == null) {
            if (other.comment!= null) {
                return false;
            }
        } else {
            if (!comment.equals(other.comment)) {
                return false;
            }
        }
        if (referencingName == null) {
            if (other.referencingName!= null) {
                return false;
            }
        } else {
            if (!referencingName.equals(other.referencingName)) {
                return false;
            }
        }
        if (referencingComment == null) {
            if (other.referencingComment!= null) {
                return false;
            }
        } else {
            if (!referencingComment.equals(other.referencingComment)) {
                return false;
            }
        }
        if (tables == null) {
            if (other.tables!= null) {
                return false;
            }
        } else {
            if (!tables.equals(other.tables)) {
                return false;
            }
        }
        if (replacesFields == null) {
            if (other.replacesFields!= null) {
                return false;
            }
        } else {
            if (!replacesFields.equals(other.replacesFields)) {
                return false;
            }
        }
        if (fields == null) {
            if (other.fields!= null) {
                return false;
            }
        } else {
            if (!fields.equals(other.fields)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((catalog == null)? 0 :catalog.hashCode()));
        result = ((prime*result)+((schema == null)? 0 :schema.hashCode()));
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        result = ((prime*result)+((referencingName == null)? 0 :referencingName.hashCode()));
        result = ((prime*result)+((referencingComment == null)? 0 :referencingComment.hashCode()));
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        result = ((prime*result)+((replacesFields == null)? 0 :replacesFields.hashCode()));
        result = ((prime*result)+((fields == null)? 0 :fields.hashCode()));
        return result;
    }

}
