
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
 * <p>Java class for SyntheticReadonlyRowidType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticReadonlyRowidType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tables" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyntheticReadonlyRowidType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticReadonlyRowidType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31800L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tables;

    /**
     * The optional ROWID column name.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The optional ROWID column name.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic ROWID.
     * 
     */
    public String getTables() {
        return tables;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic ROWID.
     * 
     */
    public void setTables(String value) {
        this.tables = value;
    }

    /**
     * The optional ROWID column name.
     * 
     */
    public SyntheticReadonlyRowidType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic ROWID.
     * 
     */
    public SyntheticReadonlyRowidType withTables(String value) {
        setTables(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("tables", tables);
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
        SyntheticReadonlyRowidType other = ((SyntheticReadonlyRowidType) that);
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((name == null)? 0 :name.hashCode()));
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        return result;
    }

}
