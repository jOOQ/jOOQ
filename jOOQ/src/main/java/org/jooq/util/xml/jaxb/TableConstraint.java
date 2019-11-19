
package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for TableConstraint complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TableConstraint"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="constraint_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="constraint_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="constraint_type" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}TableConstraintType"/&gt;
 *         &lt;element name="table_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TableConstraint", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class TableConstraint implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31300L;
    @XmlElement(name = "constraint_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintCatalog;
    @XmlElement(name = "constraint_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintSchema;
    @XmlElement(name = "constraint_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String constraintName;
    @XmlElement(name = "constraint_type", required = true)
    @XmlSchemaType(name = "string")
    protected TableConstraintType constraintType;
    @XmlElement(name = "table_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableCatalog;
    @XmlElement(name = "table_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableSchema;
    @XmlElement(name = "table_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableName;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;

    public String getConstraintCatalog() {
        return constraintCatalog;
    }

    public void setConstraintCatalog(String value) {
        this.constraintCatalog = value;
    }

    public String getConstraintSchema() {
        return constraintSchema;
    }

    public void setConstraintSchema(String value) {
        this.constraintSchema = value;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String value) {
        this.constraintName = value;
    }

    public TableConstraintType getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(TableConstraintType value) {
        this.constraintType = value;
    }

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String value) {
        this.tableCatalog = value;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String value) {
        this.tableSchema = value;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String value) {
        this.tableName = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        this.comment = value;
    }

    public TableConstraint withConstraintCatalog(String value) {
        setConstraintCatalog(value);
        return this;
    }

    public TableConstraint withConstraintSchema(String value) {
        setConstraintSchema(value);
        return this;
    }

    public TableConstraint withConstraintName(String value) {
        setConstraintName(value);
        return this;
    }

    public TableConstraint withConstraintType(TableConstraintType value) {
        setConstraintType(value);
        return this;
    }

    public TableConstraint withTableCatalog(String value) {
        setTableCatalog(value);
        return this;
    }

    public TableConstraint withTableSchema(String value) {
        setTableSchema(value);
        return this;
    }

    public TableConstraint withTableName(String value) {
        setTableName(value);
        return this;
    }

    public TableConstraint withComment(String value) {
        setComment(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("constraint_catalog", constraintCatalog);
        builder.append("constraint_schema", constraintSchema);
        builder.append("constraint_name", constraintName);
        builder.append("constraint_type", constraintType);
        builder.append("table_catalog", tableCatalog);
        builder.append("table_schema", tableSchema);
        builder.append("table_name", tableName);
        builder.append("comment", comment);
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
        TableConstraint other = ((TableConstraint) that);
        if (constraintCatalog == null) {
            if (other.constraintCatalog!= null) {
                return false;
            }
        } else {
            if (!constraintCatalog.equals(other.constraintCatalog)) {
                return false;
            }
        }
        if (constraintSchema == null) {
            if (other.constraintSchema!= null) {
                return false;
            }
        } else {
            if (!constraintSchema.equals(other.constraintSchema)) {
                return false;
            }
        }
        if (constraintName == null) {
            if (other.constraintName!= null) {
                return false;
            }
        } else {
            if (!constraintName.equals(other.constraintName)) {
                return false;
            }
        }
        if (constraintType == null) {
            if (other.constraintType!= null) {
                return false;
            }
        } else {
            if (!constraintType.equals(other.constraintType)) {
                return false;
            }
        }
        if (tableCatalog == null) {
            if (other.tableCatalog!= null) {
                return false;
            }
        } else {
            if (!tableCatalog.equals(other.tableCatalog)) {
                return false;
            }
        }
        if (tableSchema == null) {
            if (other.tableSchema!= null) {
                return false;
            }
        } else {
            if (!tableSchema.equals(other.tableSchema)) {
                return false;
            }
        }
        if (tableName == null) {
            if (other.tableName!= null) {
                return false;
            }
        } else {
            if (!tableName.equals(other.tableName)) {
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((constraintCatalog == null)? 0 :constraintCatalog.hashCode()));
        result = ((prime*result)+((constraintSchema == null)? 0 :constraintSchema.hashCode()));
        result = ((prime*result)+((constraintName == null)? 0 :constraintName.hashCode()));
        result = ((prime*result)+((constraintType == null)? 0 :constraintType.hashCode()));
        result = ((prime*result)+((tableCatalog == null)? 0 :tableCatalog.hashCode()));
        result = ((prime*result)+((tableSchema == null)? 0 :tableSchema.hashCode()));
        result = ((prime*result)+((tableName == null)? 0 :tableName.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        return result;
    }

}
