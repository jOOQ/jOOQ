
package org.jooq.meta.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlType;
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

    private final static long serialVersionUID = 31928L;
    @XmlElementWrapper(name = "readonlyColumns")
    @XmlElement(name = "readonlyColumn")
    protected List<SyntheticReadonlyColumnType> readonlyColumns;
    @XmlElementWrapper(name = "readonlyRowids")
    @XmlElement(name = "readonlyRowid")
    protected List<SyntheticReadonlyRowidType> readonlyRowids;
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    protected List<SyntheticColumnType> columns;
    @XmlElementWrapper(name = "identities")
    @XmlElement(name = "identity")
    protected List<SyntheticIdentityType> identities;
    @XmlElementWrapper(name = "enums")
    @XmlElement(name = "enum")
    protected List<SyntheticEnumType> enums;
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
    @XmlElementWrapper(name = "daos")
    @XmlElement(name = "view")
    protected List<SyntheticDaoType> daos;

    public List<SyntheticReadonlyColumnType> getReadonlyColumns() {
        if (readonlyColumns == null) {
            readonlyColumns = new ArrayList<SyntheticReadonlyColumnType>();
        }
        return readonlyColumns;
    }

    public void setReadonlyColumns(List<SyntheticReadonlyColumnType> readonlyColumns) {
        this.readonlyColumns = readonlyColumns;
    }

    public List<SyntheticReadonlyRowidType> getReadonlyRowids() {
        if (readonlyRowids == null) {
            readonlyRowids = new ArrayList<SyntheticReadonlyRowidType>();
        }
        return readonlyRowids;
    }

    public void setReadonlyRowids(List<SyntheticReadonlyRowidType> readonlyRowids) {
        this.readonlyRowids = readonlyRowids;
    }

    public List<SyntheticColumnType> getColumns() {
        if (columns == null) {
            columns = new ArrayList<SyntheticColumnType>();
        }
        return columns;
    }

    public void setColumns(List<SyntheticColumnType> columns) {
        this.columns = columns;
    }

    public List<SyntheticIdentityType> getIdentities() {
        if (identities == null) {
            identities = new ArrayList<SyntheticIdentityType>();
        }
        return identities;
    }

    public void setIdentities(List<SyntheticIdentityType> identities) {
        this.identities = identities;
    }

    public List<SyntheticEnumType> getEnums() {
        if (enums == null) {
            enums = new ArrayList<SyntheticEnumType>();
        }
        return enums;
    }

    public void setEnums(List<SyntheticEnumType> enums) {
        this.enums = enums;
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

    public List<SyntheticDaoType> getDaos() {
        if (daos == null) {
            daos = new ArrayList<SyntheticDaoType>();
        }
        return daos;
    }

    public void setDaos(List<SyntheticDaoType> daos) {
        this.daos = daos;
    }

    public SyntheticObjectsType withReadonlyColumns(SyntheticReadonlyColumnType... values) {
        if (values!= null) {
            for (SyntheticReadonlyColumnType value: values) {
                getReadonlyColumns().add(value);
            }
        }
        return this;
    }

    public SyntheticObjectsType withReadonlyColumns(Collection<SyntheticReadonlyColumnType> values) {
        if (values!= null) {
            getReadonlyColumns().addAll(values);
        }
        return this;
    }

    public SyntheticObjectsType withReadonlyColumns(List<SyntheticReadonlyColumnType> readonlyColumns) {
        setReadonlyColumns(readonlyColumns);
        return this;
    }

    public SyntheticObjectsType withReadonlyRowids(SyntheticReadonlyRowidType... values) {
        if (values!= null) {
            for (SyntheticReadonlyRowidType value: values) {
                getReadonlyRowids().add(value);
            }
        }
        return this;
    }

    public SyntheticObjectsType withReadonlyRowids(Collection<SyntheticReadonlyRowidType> values) {
        if (values!= null) {
            getReadonlyRowids().addAll(values);
        }
        return this;
    }

    public SyntheticObjectsType withReadonlyRowids(List<SyntheticReadonlyRowidType> readonlyRowids) {
        setReadonlyRowids(readonlyRowids);
        return this;
    }

    public SyntheticObjectsType withColumns(SyntheticColumnType... values) {
        if (values!= null) {
            for (SyntheticColumnType value: values) {
                getColumns().add(value);
            }
        }
        return this;
    }

    public SyntheticObjectsType withColumns(Collection<SyntheticColumnType> values) {
        if (values!= null) {
            getColumns().addAll(values);
        }
        return this;
    }

    public SyntheticObjectsType withColumns(List<SyntheticColumnType> columns) {
        setColumns(columns);
        return this;
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

    public SyntheticObjectsType withEnums(SyntheticEnumType... values) {
        if (values!= null) {
            for (SyntheticEnumType value: values) {
                getEnums().add(value);
            }
        }
        return this;
    }

    public SyntheticObjectsType withEnums(Collection<SyntheticEnumType> values) {
        if (values!= null) {
            getEnums().addAll(values);
        }
        return this;
    }

    public SyntheticObjectsType withEnums(List<SyntheticEnumType> enums) {
        setEnums(enums);
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

    public SyntheticObjectsType withDaos(SyntheticDaoType... values) {
        if (values!= null) {
            for (SyntheticDaoType value: values) {
                getDaos().add(value);
            }
        }
        return this;
    }

    public SyntheticObjectsType withDaos(Collection<SyntheticDaoType> values) {
        if (values!= null) {
            getDaos().addAll(values);
        }
        return this;
    }

    public SyntheticObjectsType withDaos(List<SyntheticDaoType> daos) {
        setDaos(daos);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("readonlyColumns", "readonlyColumn", readonlyColumns);
        builder.append("readonlyRowids", "readonlyRowid", readonlyRowids);
        builder.append("columns", "column", columns);
        builder.append("identities", "identity", identities);
        builder.append("enums", "enum", enums);
        builder.append("primaryKeys", "primaryKey", primaryKeys);
        builder.append("uniqueKeys", "uniqueKey", uniqueKeys);
        builder.append("foreignKeys", "foreignKey", foreignKeys);
        builder.append("views", "view", views);
        builder.append("daos", "view", daos);
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
        if ((readonlyColumns == null)||readonlyColumns.isEmpty()) {
            if ((other.readonlyColumns!= null)&&(!other.readonlyColumns.isEmpty())) {
                return false;
            }
        } else {
            if (!readonlyColumns.equals(other.readonlyColumns)) {
                return false;
            }
        }
        if ((readonlyRowids == null)||readonlyRowids.isEmpty()) {
            if ((other.readonlyRowids!= null)&&(!other.readonlyRowids.isEmpty())) {
                return false;
            }
        } else {
            if (!readonlyRowids.equals(other.readonlyRowids)) {
                return false;
            }
        }
        if ((columns == null)||columns.isEmpty()) {
            if ((other.columns!= null)&&(!other.columns.isEmpty())) {
                return false;
            }
        } else {
            if (!columns.equals(other.columns)) {
                return false;
            }
        }
        if ((identities == null)||identities.isEmpty()) {
            if ((other.identities!= null)&&(!other.identities.isEmpty())) {
                return false;
            }
        } else {
            if (!identities.equals(other.identities)) {
                return false;
            }
        }
        if ((enums == null)||enums.isEmpty()) {
            if ((other.enums!= null)&&(!other.enums.isEmpty())) {
                return false;
            }
        } else {
            if (!enums.equals(other.enums)) {
                return false;
            }
        }
        if ((primaryKeys == null)||primaryKeys.isEmpty()) {
            if ((other.primaryKeys!= null)&&(!other.primaryKeys.isEmpty())) {
                return false;
            }
        } else {
            if (!primaryKeys.equals(other.primaryKeys)) {
                return false;
            }
        }
        if ((uniqueKeys == null)||uniqueKeys.isEmpty()) {
            if ((other.uniqueKeys!= null)&&(!other.uniqueKeys.isEmpty())) {
                return false;
            }
        } else {
            if (!uniqueKeys.equals(other.uniqueKeys)) {
                return false;
            }
        }
        if ((foreignKeys == null)||foreignKeys.isEmpty()) {
            if ((other.foreignKeys!= null)&&(!other.foreignKeys.isEmpty())) {
                return false;
            }
        } else {
            if (!foreignKeys.equals(other.foreignKeys)) {
                return false;
            }
        }
        if ((views == null)||views.isEmpty()) {
            if ((other.views!= null)&&(!other.views.isEmpty())) {
                return false;
            }
        } else {
            if (!views.equals(other.views)) {
                return false;
            }
        }
        if ((daos == null)||daos.isEmpty()) {
            if ((other.daos!= null)&&(!other.daos.isEmpty())) {
                return false;
            }
        } else {
            if (!daos.equals(other.daos)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+(((readonlyColumns == null)||readonlyColumns.isEmpty())? 0 :readonlyColumns.hashCode()));
        result = ((prime*result)+(((readonlyRowids == null)||readonlyRowids.isEmpty())? 0 :readonlyRowids.hashCode()));
        result = ((prime*result)+(((columns == null)||columns.isEmpty())? 0 :columns.hashCode()));
        result = ((prime*result)+(((identities == null)||identities.isEmpty())? 0 :identities.hashCode()));
        result = ((prime*result)+(((enums == null)||enums.isEmpty())? 0 :enums.hashCode()));
        result = ((prime*result)+(((primaryKeys == null)||primaryKeys.isEmpty())? 0 :primaryKeys.hashCode()));
        result = ((prime*result)+(((uniqueKeys == null)||uniqueKeys.isEmpty())? 0 :uniqueKeys.hashCode()));
        result = ((prime*result)+(((foreignKeys == null)||foreignKeys.isEmpty())? 0 :foreignKeys.hashCode()));
        result = ((prime*result)+(((views == null)||views.isEmpty())? 0 :views.hashCode()));
        result = ((prime*result)+(((daos == null)||daos.isEmpty())? 0 :daos.hashCode()));
        return result;
    }

}
