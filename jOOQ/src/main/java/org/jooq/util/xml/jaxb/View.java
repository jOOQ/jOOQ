
package org.jooq.util.xml.jaxb;

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
 * <p>Java class for View complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="View"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="table_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="view_definition" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "View", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class View implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31300L;
    @XmlElement(name = "table_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableCatalog;
    @XmlElement(name = "table_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableSchema;
    @XmlElement(name = "table_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableName;
    @XmlElement(name = "view_definition", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String viewDefinition;

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

    public String getViewDefinition() {
        return viewDefinition;
    }

    public void setViewDefinition(String value) {
        this.viewDefinition = value;
    }

    public View withTableCatalog(String value) {
        setTableCatalog(value);
        return this;
    }

    public View withTableSchema(String value) {
        setTableSchema(value);
        return this;
    }

    public View withTableName(String value) {
        setTableName(value);
        return this;
    }

    public View withViewDefinition(String value) {
        setViewDefinition(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("table_catalog", tableCatalog);
        builder.append("table_schema", tableSchema);
        builder.append("table_name", tableName);
        builder.append("view_definition", viewDefinition);
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
        View other = ((View) that);
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
        if (viewDefinition == null) {
            if (other.viewDefinition!= null) {
                return false;
            }
        } else {
            if (!viewDefinition.equals(other.viewDefinition)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((tableCatalog == null)? 0 :tableCatalog.hashCode()));
        result = ((prime*result)+((tableSchema == null)? 0 :tableSchema.hashCode()));
        result = ((prime*result)+((tableName == null)? 0 :tableName.hashCode()));
        result = ((prime*result)+((viewDefinition == null)? 0 :viewDefinition.hashCode()));
        return result;
    }

}
