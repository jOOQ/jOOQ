
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
 * Synthetic key configuration.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyntheticKeysType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticKeysType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31400L;
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
    @XmlElement(name = "primaryKey")
    protected List<SyntheticForeignKeyType> foreignKeys;

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

    public SyntheticKeysType withIdentities(SyntheticIdentityType... values) {
        if (values!= null) {
            for (SyntheticIdentityType value: values) {
                getIdentities().add(value);
            }
        }
        return this;
    }

    public SyntheticKeysType withIdentities(Collection<SyntheticIdentityType> values) {
        if (values!= null) {
            getIdentities().addAll(values);
        }
        return this;
    }

    public SyntheticKeysType withIdentities(List<SyntheticIdentityType> identities) {
        setIdentities(identities);
        return this;
    }

    public SyntheticKeysType withPrimaryKeys(SyntheticPrimaryKeyType... values) {
        if (values!= null) {
            for (SyntheticPrimaryKeyType value: values) {
                getPrimaryKeys().add(value);
            }
        }
        return this;
    }

    public SyntheticKeysType withPrimaryKeys(Collection<SyntheticPrimaryKeyType> values) {
        if (values!= null) {
            getPrimaryKeys().addAll(values);
        }
        return this;
    }

    public SyntheticKeysType withPrimaryKeys(List<SyntheticPrimaryKeyType> primaryKeys) {
        setPrimaryKeys(primaryKeys);
        return this;
    }

    public SyntheticKeysType withUniqueKeys(SyntheticUniqueKeyType... values) {
        if (values!= null) {
            for (SyntheticUniqueKeyType value: values) {
                getUniqueKeys().add(value);
            }
        }
        return this;
    }

    public SyntheticKeysType withUniqueKeys(Collection<SyntheticUniqueKeyType> values) {
        if (values!= null) {
            getUniqueKeys().addAll(values);
        }
        return this;
    }

    public SyntheticKeysType withUniqueKeys(List<SyntheticUniqueKeyType> uniqueKeys) {
        setUniqueKeys(uniqueKeys);
        return this;
    }

    public SyntheticKeysType withForeignKeys(SyntheticForeignKeyType... values) {
        if (values!= null) {
            for (SyntheticForeignKeyType value: values) {
                getForeignKeys().add(value);
            }
        }
        return this;
    }

    public SyntheticKeysType withForeignKeys(Collection<SyntheticForeignKeyType> values) {
        if (values!= null) {
            getForeignKeys().addAll(values);
        }
        return this;
    }

    public SyntheticKeysType withForeignKeys(List<SyntheticForeignKeyType> foreignKeys) {
        setForeignKeys(foreignKeys);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("identities", "identity", identities);
        builder.append("primaryKeys", "primaryKey", primaryKeys);
        builder.append("uniqueKeys", "uniqueKey", uniqueKeys);
        builder.append("foreignKeys", "primaryKey", foreignKeys);
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
        SyntheticKeysType other = ((SyntheticKeysType) that);
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
        return result;
    }

}
