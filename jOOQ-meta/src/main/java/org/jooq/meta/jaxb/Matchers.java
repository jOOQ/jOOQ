
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
 * Declarative naming strategy configuration.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Matchers", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Matchers implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32001L;
    @XmlElementWrapper(name = "catalogs")
    @XmlElement(name = "catalog")
    protected List<MatchersCatalogType> catalogs;
    @XmlElementWrapper(name = "schemas")
    @XmlElement(name = "schema")
    protected List<MatchersSchemaType> schemas;
    @XmlElementWrapper(name = "tables")
    @XmlElement(name = "table")
    protected List<MatchersTableType> tables;
    @XmlElementWrapper(name = "indexes")
    @XmlElement(name = "index")
    protected List<MatchersIndexType> indexes;
    @XmlElementWrapper(name = "primaryKeys")
    @XmlElement(name = "primaryKey")
    protected List<MatchersPrimaryKeyType> primaryKeys;
    @XmlElementWrapper(name = "uniqueKeys")
    @XmlElement(name = "uniqueKey")
    protected List<MatchersUniqueKeyType> uniqueKeys;
    @XmlElementWrapper(name = "foreignKeys")
    @XmlElement(name = "foreignKey")
    protected List<MatchersForeignKeyType> foreignKeys;
    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    protected List<MatchersFieldType> fields;
    @XmlElementWrapper(name = "routines")
    @XmlElement(name = "routine")
    protected List<MatchersRoutineType> routines;
    @XmlElementWrapper(name = "sequences")
    @XmlElement(name = "sequence")
    protected List<MatchersSequenceType> sequences;
    @XmlElementWrapper(name = "enums")
    @XmlElement(name = "enum")
    protected List<MatchersEnumType> enums;
    @XmlElementWrapper(name = "embeddables")
    @XmlElement(name = "embeddable")
    protected List<MatchersEmbeddableType> embeddables;
    @XmlElementWrapper(name = "udts")
    @XmlElement(name = "udt")
    protected List<MatchersUDTType> udts;
    @XmlElementWrapper(name = "attributes")
    @XmlElement(name = "attribute")
    protected List<MatchersAttributeType> attributes;

    public List<MatchersCatalogType> getCatalogs() {
        if (catalogs == null) {
            catalogs = new ArrayList<MatchersCatalogType>();
        }
        return catalogs;
    }

    public void setCatalogs(List<MatchersCatalogType> catalogs) {
        this.catalogs = catalogs;
    }

    public List<MatchersSchemaType> getSchemas() {
        if (schemas == null) {
            schemas = new ArrayList<MatchersSchemaType>();
        }
        return schemas;
    }

    public void setSchemas(List<MatchersSchemaType> schemas) {
        this.schemas = schemas;
    }

    public List<MatchersTableType> getTables() {
        if (tables == null) {
            tables = new ArrayList<MatchersTableType>();
        }
        return tables;
    }

    public void setTables(List<MatchersTableType> tables) {
        this.tables = tables;
    }

    public List<MatchersIndexType> getIndexes() {
        if (indexes == null) {
            indexes = new ArrayList<MatchersIndexType>();
        }
        return indexes;
    }

    public void setIndexes(List<MatchersIndexType> indexes) {
        this.indexes = indexes;
    }

    public List<MatchersPrimaryKeyType> getPrimaryKeys() {
        if (primaryKeys == null) {
            primaryKeys = new ArrayList<MatchersPrimaryKeyType>();
        }
        return primaryKeys;
    }

    public void setPrimaryKeys(List<MatchersPrimaryKeyType> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    public List<MatchersUniqueKeyType> getUniqueKeys() {
        if (uniqueKeys == null) {
            uniqueKeys = new ArrayList<MatchersUniqueKeyType>();
        }
        return uniqueKeys;
    }

    public void setUniqueKeys(List<MatchersUniqueKeyType> uniqueKeys) {
        this.uniqueKeys = uniqueKeys;
    }

    public List<MatchersForeignKeyType> getForeignKeys() {
        if (foreignKeys == null) {
            foreignKeys = new ArrayList<MatchersForeignKeyType>();
        }
        return foreignKeys;
    }

    public void setForeignKeys(List<MatchersForeignKeyType> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public List<MatchersFieldType> getFields() {
        if (fields == null) {
            fields = new ArrayList<MatchersFieldType>();
        }
        return fields;
    }

    public void setFields(List<MatchersFieldType> fields) {
        this.fields = fields;
    }

    public List<MatchersRoutineType> getRoutines() {
        if (routines == null) {
            routines = new ArrayList<MatchersRoutineType>();
        }
        return routines;
    }

    public void setRoutines(List<MatchersRoutineType> routines) {
        this.routines = routines;
    }

    public List<MatchersSequenceType> getSequences() {
        if (sequences == null) {
            sequences = new ArrayList<MatchersSequenceType>();
        }
        return sequences;
    }

    public void setSequences(List<MatchersSequenceType> sequences) {
        this.sequences = sequences;
    }

    public List<MatchersEnumType> getEnums() {
        if (enums == null) {
            enums = new ArrayList<MatchersEnumType>();
        }
        return enums;
    }

    public void setEnums(List<MatchersEnumType> enums) {
        this.enums = enums;
    }

    public List<MatchersEmbeddableType> getEmbeddables() {
        if (embeddables == null) {
            embeddables = new ArrayList<MatchersEmbeddableType>();
        }
        return embeddables;
    }

    public void setEmbeddables(List<MatchersEmbeddableType> embeddables) {
        this.embeddables = embeddables;
    }

    public List<MatchersUDTType> getUdts() {
        if (udts == null) {
            udts = new ArrayList<MatchersUDTType>();
        }
        return udts;
    }

    public void setUdts(List<MatchersUDTType> udts) {
        this.udts = udts;
    }

    public List<MatchersAttributeType> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<MatchersAttributeType>();
        }
        return attributes;
    }

    public void setAttributes(List<MatchersAttributeType> attributes) {
        this.attributes = attributes;
    }

    public Matchers withCatalogs(MatchersCatalogType... values) {
        if (values!= null) {
            for (MatchersCatalogType value: values) {
                getCatalogs().add(value);
            }
        }
        return this;
    }

    public Matchers withCatalogs(Collection<MatchersCatalogType> values) {
        if (values!= null) {
            getCatalogs().addAll(values);
        }
        return this;
    }

    public Matchers withCatalogs(List<MatchersCatalogType> catalogs) {
        setCatalogs(catalogs);
        return this;
    }

    public Matchers withSchemas(MatchersSchemaType... values) {
        if (values!= null) {
            for (MatchersSchemaType value: values) {
                getSchemas().add(value);
            }
        }
        return this;
    }

    public Matchers withSchemas(Collection<MatchersSchemaType> values) {
        if (values!= null) {
            getSchemas().addAll(values);
        }
        return this;
    }

    public Matchers withSchemas(List<MatchersSchemaType> schemas) {
        setSchemas(schemas);
        return this;
    }

    public Matchers withTables(MatchersTableType... values) {
        if (values!= null) {
            for (MatchersTableType value: values) {
                getTables().add(value);
            }
        }
        return this;
    }

    public Matchers withTables(Collection<MatchersTableType> values) {
        if (values!= null) {
            getTables().addAll(values);
        }
        return this;
    }

    public Matchers withTables(List<MatchersTableType> tables) {
        setTables(tables);
        return this;
    }

    public Matchers withIndexes(MatchersIndexType... values) {
        if (values!= null) {
            for (MatchersIndexType value: values) {
                getIndexes().add(value);
            }
        }
        return this;
    }

    public Matchers withIndexes(Collection<MatchersIndexType> values) {
        if (values!= null) {
            getIndexes().addAll(values);
        }
        return this;
    }

    public Matchers withIndexes(List<MatchersIndexType> indexes) {
        setIndexes(indexes);
        return this;
    }

    public Matchers withPrimaryKeys(MatchersPrimaryKeyType... values) {
        if (values!= null) {
            for (MatchersPrimaryKeyType value: values) {
                getPrimaryKeys().add(value);
            }
        }
        return this;
    }

    public Matchers withPrimaryKeys(Collection<MatchersPrimaryKeyType> values) {
        if (values!= null) {
            getPrimaryKeys().addAll(values);
        }
        return this;
    }

    public Matchers withPrimaryKeys(List<MatchersPrimaryKeyType> primaryKeys) {
        setPrimaryKeys(primaryKeys);
        return this;
    }

    public Matchers withUniqueKeys(MatchersUniqueKeyType... values) {
        if (values!= null) {
            for (MatchersUniqueKeyType value: values) {
                getUniqueKeys().add(value);
            }
        }
        return this;
    }

    public Matchers withUniqueKeys(Collection<MatchersUniqueKeyType> values) {
        if (values!= null) {
            getUniqueKeys().addAll(values);
        }
        return this;
    }

    public Matchers withUniqueKeys(List<MatchersUniqueKeyType> uniqueKeys) {
        setUniqueKeys(uniqueKeys);
        return this;
    }

    public Matchers withForeignKeys(MatchersForeignKeyType... values) {
        if (values!= null) {
            for (MatchersForeignKeyType value: values) {
                getForeignKeys().add(value);
            }
        }
        return this;
    }

    public Matchers withForeignKeys(Collection<MatchersForeignKeyType> values) {
        if (values!= null) {
            getForeignKeys().addAll(values);
        }
        return this;
    }

    public Matchers withForeignKeys(List<MatchersForeignKeyType> foreignKeys) {
        setForeignKeys(foreignKeys);
        return this;
    }

    public Matchers withFields(MatchersFieldType... values) {
        if (values!= null) {
            for (MatchersFieldType value: values) {
                getFields().add(value);
            }
        }
        return this;
    }

    public Matchers withFields(Collection<MatchersFieldType> values) {
        if (values!= null) {
            getFields().addAll(values);
        }
        return this;
    }

    public Matchers withFields(List<MatchersFieldType> fields) {
        setFields(fields);
        return this;
    }

    public Matchers withRoutines(MatchersRoutineType... values) {
        if (values!= null) {
            for (MatchersRoutineType value: values) {
                getRoutines().add(value);
            }
        }
        return this;
    }

    public Matchers withRoutines(Collection<MatchersRoutineType> values) {
        if (values!= null) {
            getRoutines().addAll(values);
        }
        return this;
    }

    public Matchers withRoutines(List<MatchersRoutineType> routines) {
        setRoutines(routines);
        return this;
    }

    public Matchers withSequences(MatchersSequenceType... values) {
        if (values!= null) {
            for (MatchersSequenceType value: values) {
                getSequences().add(value);
            }
        }
        return this;
    }

    public Matchers withSequences(Collection<MatchersSequenceType> values) {
        if (values!= null) {
            getSequences().addAll(values);
        }
        return this;
    }

    public Matchers withSequences(List<MatchersSequenceType> sequences) {
        setSequences(sequences);
        return this;
    }

    public Matchers withEnums(MatchersEnumType... values) {
        if (values!= null) {
            for (MatchersEnumType value: values) {
                getEnums().add(value);
            }
        }
        return this;
    }

    public Matchers withEnums(Collection<MatchersEnumType> values) {
        if (values!= null) {
            getEnums().addAll(values);
        }
        return this;
    }

    public Matchers withEnums(List<MatchersEnumType> enums) {
        setEnums(enums);
        return this;
    }

    public Matchers withEmbeddables(MatchersEmbeddableType... values) {
        if (values!= null) {
            for (MatchersEmbeddableType value: values) {
                getEmbeddables().add(value);
            }
        }
        return this;
    }

    public Matchers withEmbeddables(Collection<MatchersEmbeddableType> values) {
        if (values!= null) {
            getEmbeddables().addAll(values);
        }
        return this;
    }

    public Matchers withEmbeddables(List<MatchersEmbeddableType> embeddables) {
        setEmbeddables(embeddables);
        return this;
    }

    public Matchers withUdts(MatchersUDTType... values) {
        if (values!= null) {
            for (MatchersUDTType value: values) {
                getUdts().add(value);
            }
        }
        return this;
    }

    public Matchers withUdts(Collection<MatchersUDTType> values) {
        if (values!= null) {
            getUdts().addAll(values);
        }
        return this;
    }

    public Matchers withUdts(List<MatchersUDTType> udts) {
        setUdts(udts);
        return this;
    }

    public Matchers withAttributes(MatchersAttributeType... values) {
        if (values!= null) {
            for (MatchersAttributeType value: values) {
                getAttributes().add(value);
            }
        }
        return this;
    }

    public Matchers withAttributes(Collection<MatchersAttributeType> values) {
        if (values!= null) {
            getAttributes().addAll(values);
        }
        return this;
    }

    public Matchers withAttributes(List<MatchersAttributeType> attributes) {
        setAttributes(attributes);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("catalogs", "catalog", catalogs);
        builder.append("schemas", "schema", schemas);
        builder.append("tables", "table", tables);
        builder.append("indexes", "index", indexes);
        builder.append("primaryKeys", "primaryKey", primaryKeys);
        builder.append("uniqueKeys", "uniqueKey", uniqueKeys);
        builder.append("foreignKeys", "foreignKey", foreignKeys);
        builder.append("fields", "field", fields);
        builder.append("routines", "routine", routines);
        builder.append("sequences", "sequence", sequences);
        builder.append("enums", "enum", enums);
        builder.append("embeddables", "embeddable", embeddables);
        builder.append("udts", "udt", udts);
        builder.append("attributes", "attribute", attributes);
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
        Matchers other = ((Matchers) that);
        if ((catalogs == null)||catalogs.isEmpty()) {
            if ((other.catalogs!= null)&&(!other.catalogs.isEmpty())) {
                return false;
            }
        } else {
            if (!catalogs.equals(other.catalogs)) {
                return false;
            }
        }
        if ((schemas == null)||schemas.isEmpty()) {
            if ((other.schemas!= null)&&(!other.schemas.isEmpty())) {
                return false;
            }
        } else {
            if (!schemas.equals(other.schemas)) {
                return false;
            }
        }
        if ((tables == null)||tables.isEmpty()) {
            if ((other.tables!= null)&&(!other.tables.isEmpty())) {
                return false;
            }
        } else {
            if (!tables.equals(other.tables)) {
                return false;
            }
        }
        if ((indexes == null)||indexes.isEmpty()) {
            if ((other.indexes!= null)&&(!other.indexes.isEmpty())) {
                return false;
            }
        } else {
            if (!indexes.equals(other.indexes)) {
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
        if ((fields == null)||fields.isEmpty()) {
            if ((other.fields!= null)&&(!other.fields.isEmpty())) {
                return false;
            }
        } else {
            if (!fields.equals(other.fields)) {
                return false;
            }
        }
        if ((routines == null)||routines.isEmpty()) {
            if ((other.routines!= null)&&(!other.routines.isEmpty())) {
                return false;
            }
        } else {
            if (!routines.equals(other.routines)) {
                return false;
            }
        }
        if ((sequences == null)||sequences.isEmpty()) {
            if ((other.sequences!= null)&&(!other.sequences.isEmpty())) {
                return false;
            }
        } else {
            if (!sequences.equals(other.sequences)) {
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
        if ((embeddables == null)||embeddables.isEmpty()) {
            if ((other.embeddables!= null)&&(!other.embeddables.isEmpty())) {
                return false;
            }
        } else {
            if (!embeddables.equals(other.embeddables)) {
                return false;
            }
        }
        if ((udts == null)||udts.isEmpty()) {
            if ((other.udts!= null)&&(!other.udts.isEmpty())) {
                return false;
            }
        } else {
            if (!udts.equals(other.udts)) {
                return false;
            }
        }
        if ((attributes == null)||attributes.isEmpty()) {
            if ((other.attributes!= null)&&(!other.attributes.isEmpty())) {
                return false;
            }
        } else {
            if (!attributes.equals(other.attributes)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+(((catalogs == null)||catalogs.isEmpty())? 0 :catalogs.hashCode()));
        result = ((prime*result)+(((schemas == null)||schemas.isEmpty())? 0 :schemas.hashCode()));
        result = ((prime*result)+(((tables == null)||tables.isEmpty())? 0 :tables.hashCode()));
        result = ((prime*result)+(((indexes == null)||indexes.isEmpty())? 0 :indexes.hashCode()));
        result = ((prime*result)+(((primaryKeys == null)||primaryKeys.isEmpty())? 0 :primaryKeys.hashCode()));
        result = ((prime*result)+(((uniqueKeys == null)||uniqueKeys.isEmpty())? 0 :uniqueKeys.hashCode()));
        result = ((prime*result)+(((foreignKeys == null)||foreignKeys.isEmpty())? 0 :foreignKeys.hashCode()));
        result = ((prime*result)+(((fields == null)||fields.isEmpty())? 0 :fields.hashCode()));
        result = ((prime*result)+(((routines == null)||routines.isEmpty())? 0 :routines.hashCode()));
        result = ((prime*result)+(((sequences == null)||sequences.isEmpty())? 0 :sequences.hashCode()));
        result = ((prime*result)+(((enums == null)||enums.isEmpty())? 0 :enums.hashCode()));
        result = ((prime*result)+(((embeddables == null)||embeddables.isEmpty())? 0 :embeddables.hashCode()));
        result = ((prime*result)+(((udts == null)||udts.isEmpty())? 0 :udts.hashCode()));
        result = ((prime*result)+(((attributes == null)||attributes.isEmpty())? 0 :attributes.hashCode()));
        return result;
    }

}
