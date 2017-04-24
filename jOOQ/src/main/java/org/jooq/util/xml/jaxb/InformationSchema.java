//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2017.04.24 um 10:36:23 AM CEST
//


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


/**
 * <p>Java-Klasse für anonymous complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="schemata" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}Schemata" minOccurs="0"/&gt;
 *         &lt;element name="sequences" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}Sequences" minOccurs="0"/&gt;
 *         &lt;element name="tables" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}Tables" minOccurs="0"/&gt;
 *         &lt;element name="columns" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}Columns" minOccurs="0"/&gt;
 *         &lt;element name="table_constraints" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}TableConstraints" minOccurs="0"/&gt;
 *         &lt;element name="key_column_usages" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}KeyColumnUsages" minOccurs="0"/&gt;
 *         &lt;element name="referential_constraints" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}ReferentialConstraints" minOccurs="0"/&gt;
 *         &lt;element name="routines" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}Routines" minOccurs="0"/&gt;
 *         &lt;element name="parameters" type="{http://www.jooq.org/xsd/jooq-meta-3.10.0.xsd}Parameters" minOccurs="0"/&gt;
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
public class InformationSchema implements Serializable
{

    private final static long serialVersionUID = 31000L;
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
    @XmlElementWrapper(name = "routines")
    @XmlElement(name = "routine")
    protected List<Routine> routines;
    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "parameter")
    protected List<Parameter> parameters;

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

}
