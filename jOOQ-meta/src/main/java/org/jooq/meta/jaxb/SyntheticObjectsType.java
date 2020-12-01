
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
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * Synthetic objects configuration.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyntheticObjectsType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticObjectsType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31500L;
    @XmlElementWrapper(name = "identities")
    @XmlElement(name = "identity")
    protected List<SyntheticIdentityType> identities;
    @XmlElementWrapper(name = "primaryKeys")
    @XmlElement(name = "primaryKey")
    protected List<SyntheticPrimaryKeyType> primaryKeys;
    @XmlElementWrapper(name = "uniqueKeys")
    @XmlElement(name = "uniqueKey")
    protected List<SyntheticUniqueKeyType> uniqueKeys;
    @XmlElementWrapper(name = "foreignKeys")
    @XmlElement(name = "foreignKey")
    protected List<SyntheticForeignKeyType> foreignKeys;
    @XmlElementWrapper(name = "views")
    @XmlElement(name = "view")
    protected List<SyntheticViewType> views;

    public List<SyntheticIdentityType> getIdentities() {
        if (identities == null) {
            identities = new ArrayList<SyntheticIdentityType>();
        }
        return identities;
    }

    public void setIdentities(List<SyntheticIdentityType> identities) {
        this.identities = identities;
    }

    public List<SyntheticPrimaryKeyType> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<SyntheticPrimaryKeyType>();
        }
        return primaryKeys;
    }

    public void setPrimaryKeys(List<SyntheticPrimaryKeyType> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<SyntheticUniqueKeyType> getUniqueKeys() {
        if (uniqueKeys == null) {
            uniqueKeys = new ArrayList<SyntheticUniqueKeyType>();
        }
        return uniqueKeys;
    }

    public void setUniqueKeys(List<SyntheticUniqueKeyType> uniqueKeys) {
        this.uniqueKeys = uniqueKeys;
    }

    public List<SyntheticForeignKeyType> getForeignKeys() {
        if (foreignKeys == null) {
            foreignKeys = new ArrayList<SyntheticForeignKeyType>();
        }
        return foreignKeys;
    }

    public void setForeignKeys(List<SyntheticForeignKeyType> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public List<SyntheticViewType> getViews() {
        if (views == null) {
            views = new ArrayList<SyntheticViewType>();
        }
        return views;
    }

    public void setViews(List<SyntheticViewType> views) {
        this.views = views;
    }

    public SyntheticObjectsType withIdentities(SyntheticIdentityType... values) {
        if (values!= null) {
            for (SyntheticIdentityType value: values) {
                getIdentities().add(value);
            }
        }
        return this;
    }

    public SyntheticObjectsType withIdentities(Collection<SyntheticIdentityType> values) {
        if (values!= null) {
            getIdentities().addAll(values);
        }
        return this;
    }

    public SyntheticObjectsType withIdentities(List<SyntheticIdentityType> identities) {
        setIdentities(identities);
        return this;
    }

    public SyntheticObjectsType withPrimaryKeys(SyntheticPrimaryKeyType... values) {
        if (values!= null) {
            for (SyntheticPrimaryKeyType value: values) {
                getPrimaryKeys().add(value);
            }
        }
        return this;
    }

    public SyntheticObjectsType withPrimaryKeys(Collection<SyntheticPrimaryKeyType> values) {
        if (values!= null) {
            getPrimaryKeys().addAll(values);
        }
        return this;
    }

    public SyntheticObjectsType withPrimaryKeys(List<SyntheticPrimaryKeyType> primaryKeys) {
        setPrimaryKeys(primaryKeys);
        return this;
    }

    public SyntheticObjectsType withUniqueKeys(SyntheticUniqueKeyType... values) {
        if (values!= null) {
            for (SyntheticUniqueKeyType value: values) {
                getUniqueKeys().add(value);
            }
        }
        return this;
    }

    public SyntheticObjectsType withUniqueKeys(Collection<SyntheticUniqueKeyType> values) {
        if (values!= null) {
            getUniqueKeys().addAll(values);
        }
        return this;
    }

    public SyntheticObjectsType withUniqueKeys(List<SyntheticUniqueKeyType> uniqueKeys) {
        setUniqueKeys(uniqueKeys);
        return this;
    }

    public SyntheticObjectsType withForeignKeys(SyntheticForeignKeyType... values) {
        if (values!= null) {
            for (SyntheticForeignKeyType value: values) {
                getForeignKeys().add(value);
            }
        }
        return this;
    }

    public SyntheticObjectsType withForeignKeys(Collection<SyntheticForeignKeyType> values) {
        if (values!= null) {
            getForeignKeys().addAll(values);
        }
        return this;
    }

    public SyntheticObjectsType withForeignKeys(List<SyntheticForeignKeyType> foreignKeys) {
        setForeignKeys(foreignKeys);
        return this;
    }

    public SyntheticObjectsType withViews(SyntheticViewType... values) {
        if (values!= null) {
            for (SyntheticViewType value: values) {
                getViews().add(value);
            }
        }
        return this;
    }

    public SyntheticObjectsType withViews(Collection<SyntheticViewType> values) {
        if (values!= null) {
            getViews().addAll(values);
        }
        return this;
    }

    public SyntheticObjectsType withViews(List<SyntheticViewType> views) {
        setViews(views);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("identities", "identity", identities);
        builder.append("primaryKeys", "primaryKey", primaryKeys);
        builder.append("uniqueKeys", "uniqueKey", uniqueKeys);
        builder.append("foreignKeys", "foreignKey", foreignKeys);
        builder.append("views", "view", views);
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
        SyntheticObjectsType other = ((SyntheticObjectsType) that);
        if (identities == null) {
            if (other.identities!= null) {
                return false;
            }
        } else {
            if (!identities.equals(other.identities)) {
                return false;
            }
        }
        if (primaryKeys == null) {
            if (other.primaryKeys!= null) {
                return false;
            }
        } else {
            if (!primaryKeys.equals(other.primaryKeys)) {
                return false;
            }
        }
        if (uniqueKeys == null) {
            if (other.uniqueKeys!= null) {
                return false;
            }
        } else {
            if (!uniqueKeys.equals(other.uniqueKeys)) {
                return false;
            }
        }
        if (foreignKeys == null) {
            if (other.foreignKeys!= null) {
                return false;
            }
        } else {
            if (!foreignKeys.equals(other.foreignKeys)) {
                return false;
            }
        }
        if (views == null) {
            if (other.views!= null) {
                return false;
            }
        } else {
            if (!views.equals(other.views)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((identities == null)? 0 :identities.hashCode()));
        result = ((prime*result)+((primaryKeys == null)? 0 :primaryKeys.hashCode()));
        result = ((prime*result)+((uniqueKeys == null)? 0 :uniqueKeys.hashCode()));
        result = ((prime*result)+((foreignKeys == null)? 0 :foreignKeys.hashCode()));
        result = ((prime*result)+((views == null)? 0 :views.hashCode()));
        return result;
    }

}
