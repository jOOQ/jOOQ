
package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="catalogs" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}Catalogs" minOccurs="0"/&gt;
 *         &lt;element name="schemata" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}Schemata" minOccurs="0"/&gt;
 *         &lt;element name="sequences" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}Sequences" minOccurs="0"/&gt;
 *         &lt;element name="tables" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}Tables" minOccurs="0"/&gt;
 *         &lt;element name="columns" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}Columns" minOccurs="0"/&gt;
 *         &lt;element name="table_constraints" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}TableConstraints" minOccurs="0"/&gt;
 *         &lt;element name="key_column_usages" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}KeyColumnUsages" minOccurs="0"/&gt;
 *         &lt;element name="referential_constraints" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}ReferentialConstraints" minOccurs="0"/&gt;
 *         &lt;element name="indexes" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}Indexes" minOccurs="0"/&gt;
 *         &lt;element name="index_column_usages" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}IndexColumnUsages" minOccurs="0"/&gt;
 *         &lt;element name="routines" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}Routines" minOccurs="0"/&gt;
 *         &lt;element name="parameters" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}Parameters" minOccurs="0"/&gt;
 *         &lt;element name="element_types" type="{http://www.jooq.org/xsd/jooq-meta-3.12.0.xsd}ElementTypes" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "information_schema")
@SuppressWarnings({
    "all"
})
public class InformationSchema implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlElementWrapper(name = "catalogs")
    @XmlElement(name = "catalog")
    protected List<Catalog> catalogs;
    @XmlElementWrapper(name = "schemata")
    @XmlElement(name = "schema")
    protected List<Schema> schemata;
    @XmlElementWrapper(name = "sequences")
    @XmlElement(name = "sequence")
    protected List<Sequence> sequences;
    @XmlElementWrapper(name = "tables")
    @XmlElement(name = "table")
    protected List<Table> tables;
    @XmlElementWrapper(name = "columns")
    @XmlElement(name = "column")
    protected List<Column> columns;
    @XmlElementWrapper(name = "table_constraints")
    @XmlElement(name = "table_constraint")
    protected List<TableConstraint> tableConstraints;
    @XmlElementWrapper(name = "key_column_usages")
    @XmlElement(name = "key_column_usage")
    protected List<KeyColumnUsage> keyColumnUsages;
    @XmlElementWrapper(name = "referential_constraints")
    @XmlElement(name = "referential_constraint")
    protected List<ReferentialConstraint> referentialConstraints;
    @XmlElementWrapper(name = "indexes")
    @XmlElement(name = "index")
    protected List<Index> indexes;
    @XmlElementWrapper(name = "index_column_usages")
    @XmlElement(name = "index_column_usage")
    protected List<IndexColumnUsage> indexColumnUsages;
    @XmlElementWrapper(name = "routines")
    @XmlElement(name = "routine")
    protected List<Routine> routines;
    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "parameter")
    protected List<Parameter> parameters;
    @XmlElementWrapper(name = "element_types")
    @XmlElement(name = "element_type")
    protected List<ElementType> elementTypes;

    public List<Catalog> getCatalogs() {
        if (catalogs == null) {
            catalogs = new ArrayList<Catalog>();
        }
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    public List<Schema> getSchemata() {
        if (schemata == null) {
            schemata = new ArrayList<Schema>();
        }
        return schemata;
    }

    public void setSchemata(List<Schema> schemata) {
        this.schemata = schemata;
    }

    public List<Sequence> getSequences() {
        if (sequences == null) {
            sequences = new ArrayList<Sequence>();
        }
        return sequences;
    }

    public void setSequences(List<Sequence> sequences) {
        this.sequences = sequences;
    }

    public List<Table> getTables() {
        if (tables == null) {
            tables = new ArrayList<Table>();
        }
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<Column>();
        }
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<TableConstraint> getTableConstraints() {
        if (tableConstraints == null) {
            tableConstraints = new ArrayList<TableConstraint>();
        }
        return tableConstraints;
    }

    public void setTableConstraints(List<TableConstraint> tableConstraints) {
        this.tableConstraints = tableConstraints;
    }

    public List<KeyColumnUsage> getKeyColumnUsages() {
        if (keyColumnUsages == null) {
            keyColumnUsages = new ArrayList<KeyColumnUsage>();
        }
        return keyColumnUsages;
    }

    public void setKeyColumnUsages(List<KeyColumnUsage> keyColumnUsages) {
        this.keyColumnUsages = keyColumnUsages;
    }

    public List<ReferentialConstraint> getReferentialConstraints() {
        if (referentialConstraints == null) {
            referentialConstraints = new ArrayList<ReferentialConstraint>();
        }
        return referentialConstraints;
    }

    public void setReferentialConstraints(List<ReferentialConstraint> referentialConstraints) {
        this.referentialConstraints = referentialConstraints;
    }

    public List<Index> getIndexes() {
        if (indexes == null) {
            indexes = new ArrayList<Index>();
        }
        return indexes;
    }

    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
    }

    public List<IndexColumnUsage> getIndexColumnUsages() {
        if (indexColumnUsages == null) {
            indexColumnUsages = new ArrayList<IndexColumnUsage>();
        }
        return indexColumnUsages;
    }

    public void setIndexColumnUsages(List<IndexColumnUsage> indexColumnUsages) {
        this.indexColumnUsages = indexColumnUsages;
    }

    public List<Routine> getRoutines() {
        if (routines == null) {
            routines = new ArrayList<Routine>();
        }
        return routines;
    }

    public void setRoutines(List<Routine> routines) {
        this.routines = routines;
    }

    public List<Parameter> getParameters() {
        if (parameters == null) {
            parameters = new ArrayList<Parameter>();
        }
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<ElementType> getElementTypes() {
        if (elementTypes == null) {
            elementTypes = new ArrayList<ElementType>();
        }
        return elementTypes;
    }

    public void setElementTypes(List<ElementType> elementTypes) {
        this.elementTypes = elementTypes;
    }

    public InformationSchema withCatalogs(Catalog... values) {
        if (values!= null) {
            for (Catalog value: values) {
                getCatalogs().add(value);
            }
        }
        return this;
    }

    public InformationSchema withCatalogs(Collection<Catalog> values) {
        if (values!= null) {
            getCatalogs().addAll(values);
        }
        return this;
    }

    public InformationSchema withCatalogs(List<Catalog> catalogs) {
        setCatalogs(catalogs);
        return this;
    }

    public InformationSchema withSchemata(Schema... values) {
        if (values!= null) {
            for (Schema value: values) {
                getSchemata().add(value);
            }
        }
        return this;
    }

    public InformationSchema withSchemata(Collection<Schema> values) {
        if (values!= null) {
            getSchemata().addAll(values);
        }
        return this;
    }

    public InformationSchema withSchemata(List<Schema> schemata) {
        setSchemata(schemata);
        return this;
    }

    public InformationSchema withSequences(Sequence... values) {
        if (values!= null) {
            for (Sequence value: values) {
                getSequences().add(value);
            }
        }
        return this;
    }

    public InformationSchema withSequences(Collection<Sequence> values) {
        if (values!= null) {
            getSequences().addAll(values);
        }
        return this;
    }

    public InformationSchema withSequences(List<Sequence> sequences) {
        setSequences(sequences);
        return this;
    }

    public InformationSchema withTables(Table... values) {
        if (values!= null) {
            for (Table value: values) {
                getTables().add(value);
            }
        }
        return this;
    }

    public InformationSchema withTables(Collection<Table> values) {
        if (values!= null) {
            getTables().addAll(values);
        }
        return this;
    }

    public InformationSchema withTables(List<Table> tables) {
        setTables(tables);
        return this;
    }

    public InformationSchema withColumns(Column... values) {
        if (values!= null) {
            for (Column value: values) {
                getColumns().add(value);
            }
        }
        return this;
    }

    public InformationSchema withColumns(Collection<Column> values) {
        if (values!= null) {
            getColumns().addAll(values);
        }
        return this;
    }

    public InformationSchema withColumns(List<Column> columns) {
        setColumns(columns);
        return this;
    }

    public InformationSchema withTableConstraints(TableConstraint... values) {
        if (values!= null) {
            for (TableConstraint value: values) {
                getTableConstraints().add(value);
            }
        }
        return this;
    }

    public InformationSchema withTableConstraints(Collection<TableConstraint> values) {
        if (values!= null) {
            getTableConstraints().addAll(values);
        }
        return this;
    }

    public InformationSchema withTableConstraints(List<TableConstraint> tableConstraints) {
        setTableConstraints(tableConstraints);
        return this;
    }

    public InformationSchema withKeyColumnUsages(KeyColumnUsage... values) {
        if (values!= null) {
            for (KeyColumnUsage value: values) {
                getKeyColumnUsages().add(value);
            }
        }
        return this;
    }

    public InformationSchema withKeyColumnUsages(Collection<KeyColumnUsage> values) {
        if (values!= null) {
            getKeyColumnUsages().addAll(values);
        }
        return this;
    }

    public InformationSchema withKeyColumnUsages(List<KeyColumnUsage> keyColumnUsages) {
        setKeyColumnUsages(keyColumnUsages);
        return this;
    }

    public InformationSchema withReferentialConstraints(ReferentialConstraint... values) {
        if (values!= null) {
            for (ReferentialConstraint value: values) {
                getReferentialConstraints().add(value);
            }
        }
        return this;
    }

    public InformationSchema withReferentialConstraints(Collection<ReferentialConstraint> values) {
        if (values!= null) {
            getReferentialConstraints().addAll(values);
        }
        return this;
    }

    public InformationSchema withReferentialConstraints(List<ReferentialConstraint> referentialConstraints) {
        setReferentialConstraints(referentialConstraints);
        return this;
    }

    public InformationSchema withIndexes(Index... values) {
        if (values!= null) {
            for (Index value: values) {
                getIndexes().add(value);
            }
        }
        return this;
    }

    public InformationSchema withIndexes(Collection<Index> values) {
        if (values!= null) {
            getIndexes().addAll(values);
        }
        return this;
    }

    public InformationSchema withIndexes(List<Index> indexes) {
        setIndexes(indexes);
        return this;
    }

    public InformationSchema withIndexColumnUsages(IndexColumnUsage... values) {
        if (values!= null) {
            for (IndexColumnUsage value: values) {
                getIndexColumnUsages().add(value);
            }
        }
        return this;
    }

    public InformationSchema withIndexColumnUsages(Collection<IndexColumnUsage> values) {
        if (values!= null) {
            getIndexColumnUsages().addAll(values);
        }
        return this;
    }

    public InformationSchema withIndexColumnUsages(List<IndexColumnUsage> indexColumnUsages) {
        setIndexColumnUsages(indexColumnUsages);
        return this;
    }

    public InformationSchema withRoutines(Routine... values) {
        if (values!= null) {
            for (Routine value: values) {
                getRoutines().add(value);
            }
        }
        return this;
    }

    public InformationSchema withRoutines(Collection<Routine> values) {
        if (values!= null) {
            getRoutines().addAll(values);
        }
        return this;
    }

    public InformationSchema withRoutines(List<Routine> routines) {
        setRoutines(routines);
        return this;
    }

    public InformationSchema withParameters(Parameter... values) {
        if (values!= null) {
            for (Parameter value: values) {
                getParameters().add(value);
            }
        }
        return this;
    }

    public InformationSchema withParameters(Collection<Parameter> values) {
        if (values!= null) {
            getParameters().addAll(values);
        }
        return this;
    }

    public InformationSchema withParameters(List<Parameter> parameters) {
        setParameters(parameters);
        return this;
    }

    public InformationSchema withElementTypes(ElementType... values) {
        if (values!= null) {
            for (ElementType value: values) {
                getElementTypes().add(value);
            }
        }
        return this;
    }

    public InformationSchema withElementTypes(Collection<ElementType> values) {
        if (values!= null) {
            getElementTypes().addAll(values);
        }
        return this;
    }

    public InformationSchema withElementTypes(List<ElementType> elementTypes) {
        setElementTypes(elementTypes);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("catalogs", "catalog", catalogs);
        builder.append("schemata", "schema", schemata);
        builder.append("sequences", "sequence", sequences);
        builder.append("tables", "table", tables);
        builder.append("columns", "column", columns);
        builder.append("table_constraints", "table_constraint", tableConstraints);
        builder.append("key_column_usages", "key_column_usage", keyColumnUsages);
        builder.append("referential_constraints", "referential_constraint", referentialConstraints);
        builder.append("indexes", "index", indexes);
        builder.append("index_column_usages", "index_column_usage", indexColumnUsages);
        builder.append("routines", "routine", routines);
        builder.append("parameters", "parameter", parameters);
        builder.append("element_types", "element_type", elementTypes);
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
        InformationSchema other = ((InformationSchema) that);
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
        if (sequences == null) {
            if (other.sequences!= null) {
                return false;
            }
        } else {
            if (!sequences.equals(other.sequences)) {
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
        if (columns == null) {
            if (other.columns!= null) {
                return false;
            }
        } else {
            if (!columns.equals(other.columns)) {
                return false;
            }
        }
        if (tableConstraints == null) {
            if (other.tableConstraints!= null) {
                return false;
            }
        } else {
            if (!tableConstraints.equals(other.tableConstraints)) {
                return false;
            }
        }
        if (keyColumnUsages == null) {
            if (other.keyColumnUsages!= null) {
                return false;
            }
        } else {
            if (!keyColumnUsages.equals(other.keyColumnUsages)) {
                return false;
            }
        }
        if (referentialConstraints == null) {
            if (other.referentialConstraints!= null) {
                return false;
            }
        } else {
            if (!referentialConstraints.equals(other.referentialConstraints)) {
                return false;
            }
        }
        if (indexes == null) {
            if (other.indexes!= null) {
                return false;
            }
        } else {
            if (!indexes.equals(other.indexes)) {
                return false;
            }
        }
        if (indexColumnUsages == null) {
            if (other.indexColumnUsages!= null) {
                return false;
            }
        } else {
            if (!indexColumnUsages.equals(other.indexColumnUsages)) {
                return false;
            }
        }
        if (routines == null) {
            if (other.routines!= null) {
                return false;
            }
        } else {
            if (!routines.equals(other.routines)) {
                return false;
            }
        }
        if (parameters == null) {
            if (other.parameters!= null) {
                return false;
            }
        } else {
            if (!parameters.equals(other.parameters)) {
                return false;
            }
        }
        if (elementTypes == null) {
            if (other.elementTypes!= null) {
                return false;
            }
        } else {
            if (!elementTypes.equals(other.elementTypes)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((catalogs == null)? 0 :catalogs.hashCode()));
        result = ((prime*result)+((schemata == null)? 0 :schemata.hashCode()));
        result = ((prime*result)+((sequences == null)? 0 :sequences.hashCode()));
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        result = ((prime*result)+((columns == null)? 0 :columns.hashCode()));
        result = ((prime*result)+((tableConstraints == null)? 0 :tableConstraints.hashCode()));
        result = ((prime*result)+((keyColumnUsages == null)? 0 :keyColumnUsages.hashCode()));
        result = ((prime*result)+((referentialConstraints == null)? 0 :referentialConstraints.hashCode()));
        result = ((prime*result)+((indexes == null)? 0 :indexes.hashCode()));
        result = ((prime*result)+((indexColumnUsages == null)? 0 :indexColumnUsages.hashCode()));
        result = ((prime*result)+((routines == null)? 0 :routines.hashCode()));
        result = ((prime*result)+((parameters == null)? 0 :parameters.hashCode()));
        result = ((prime*result)+((elementTypes == null)? 0 :elementTypes.hashCode()));
        return result;
    }

}
