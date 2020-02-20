
package org.jooq.conf;

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
 * The runtime schema and table mapping.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RenderMapping", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class RenderMapping
    extends SettingsBase
    implements Serializable, Cloneable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    protected String defaultCatalog;
    protected String defaultSchema;
    @XmlElementWrapper(name = "catalogs")
    @XmlElement(name = "catalog")
    protected List<MappedCatalog> catalogs;
    @XmlElementWrapper(name = "schemata")
    @XmlElement(name = "schema")
    protected List<MappedSchema> schemata;

    /**
     * The default catalog as defined in {@link org.jooq.Catalog#getName()}.
     * <p>
     * This catalog will be omitted in rendered SQL.
     * 
     */
    public String getDefaultCatalog() {
        return defaultCatalog;
    }

    /**
     * The default catalog as defined in {@link org.jooq.Catalog#getName()}.
     * <p>
     * This catalog will be omitted in rendered SQL.
     * 
     */
    public void setDefaultCatalog(String value) {
        this.defaultCatalog = value;
    }

    /**
     * The default schema as defined in {@link org.jooq.Schema#getName()}.
     * <p>
     * This schema will be omitted in rendered SQL.
     * 
     */
    public String getDefaultSchema() {
        return defaultSchema;
    }

    /**
     * The default schema as defined in {@link org.jooq.Schema#getName()}.
     * <p>
     * This schema will be omitted in rendered SQL.
     * 
     */
    public void setDefaultSchema(String value) {
        this.defaultSchema = value;
    }

    public List<MappedCatalog> getCatalogs() {
        if (catalogs == null) {
            catalogs = new ArrayList<MappedCatalog>();
        }
        return catalogs;
    }

    public void setCatalogs(List<MappedCatalog> catalogs) {
        this.catalogs = catalogs;
    }

    public List<MappedSchema> getSchemata() {
        if (schemata == null) {
            schemata = new ArrayList<MappedSchema>();
        }
        return schemata;
    }

    public void setSchemata(List<MappedSchema> schemata) {
        this.schemata = schemata;
    }

    /**
     * The default catalog as defined in {@link org.jooq.Catalog#getName()}.
     * <p>
     * This catalog will be omitted in rendered SQL.
     * 
     */
    public RenderMapping withDefaultCatalog(String value) {
        setDefaultCatalog(value);
        return this;
    }

    /**
     * The default schema as defined in {@link org.jooq.Schema#getName()}.
     * <p>
     * This schema will be omitted in rendered SQL.
     * 
     */
    public RenderMapping withDefaultSchema(String value) {
        setDefaultSchema(value);
        return this;
    }

    public RenderMapping withCatalogs(MappedCatalog... values) {
        if (values!= null) {
            for (MappedCatalog value: values) {
                getCatalogs().add(value);
            }
        }
        return this;
    }

    public RenderMapping withCatalogs(Collection<MappedCatalog> values) {
        if (values!= null) {
            getCatalogs().addAll(values);
        }
        return this;
    }

    public RenderMapping withCatalogs(List<MappedCatalog> catalogs) {
        setCatalogs(catalogs);
        return this;
    }

    public RenderMapping withSchemata(MappedSchema... values) {
        if (values!= null) {
            for (MappedSchema value: values) {
                getSchemata().add(value);
            }
        }
        return this;
    }

    public RenderMapping withSchemata(Collection<MappedSchema> values) {
        if (values!= null) {
            getSchemata().addAll(values);
        }
        return this;
    }

    public RenderMapping withSchemata(List<MappedSchema> schemata) {
        setSchemata(schemata);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("defaultCatalog", defaultCatalog);
        builder.append("defaultSchema", defaultSchema);
        builder.append("catalogs", "catalog", catalogs);
        builder.append("schemata", "schema", schemata);
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
        RenderMapping other = ((RenderMapping) that);
        if (defaultCatalog == null) {
            if (other.defaultCatalog!= null) {
                return false;
            }
        } else {
            if (!defaultCatalog.equals(other.defaultCatalog)) {
                return false;
            }
        }
        if (defaultSchema == null) {
            if (other.defaultSchema!= null) {
                return false;
            }
        } else {
            if (!defaultSchema.equals(other.defaultSchema)) {
                return false;
            }
        }
        if (catalogs == null) {
            if (other.catalogs!= null) {
                return false;
            }
        } else {
            if (!catalogs.equals(other.catalogs)) {
                return false;
            }
        }
        if (schemata == null) {
            if (other.schemata!= null) {
                return false;
            }
        } else {
            if (!schemata.equals(other.schemata)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((defaultCatalog == null)? 0 :defaultCatalog.hashCode()));
        result = ((prime*result)+((defaultSchema == null)? 0 :defaultSchema.hashCode()));
        result = ((prime*result)+((catalogs == null)? 0 :catalogs.hashCode()));
        result = ((prime*result)+((schemata == null)? 0 :schemata.hashCode()));
        return result;
    }

}
