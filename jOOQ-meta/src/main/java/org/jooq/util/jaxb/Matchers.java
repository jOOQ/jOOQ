







package org.jooq.util.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Matchers complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Matchers"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="schemas" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatchersSchemasType" minOccurs="0"/&gt;
 *         &lt;element name="tables" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatchersTablesType" minOccurs="0"/&gt;
 *         &lt;element name="fields" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatchersFieldsType" minOccurs="0"/&gt;
 *         &lt;element name="routines" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatchersRoutinesType" minOccurs="0"/&gt;
 *         &lt;element name="sequences" type="{http://www.jooq.org/xsd/jooq-codegen-3.10.0.xsd}MatchersSequencesType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Matchers", propOrder = {
    "schemas",
    "tables",
    "fields",
    "routines",
    "sequences"
})
@SuppressWarnings({
    "all"
})
public class Matchers implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlElementWrapper(name = "schemas")
    @XmlElement(name = "schema")
    protected List<MatchersSchemaType> schemas;
    @XmlElementWrapper(name = "tables")
    @XmlElement(name = "table")
    protected List<MatchersTableType> tables;
    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    protected List<MatchersFieldType> fields;
    @XmlElementWrapper(name = "routines")
    @XmlElement(name = "routine")
    protected List<MatchersRoutineType> routines;
    @XmlElementWrapper(name = "sequences")
    @XmlElement(name = "sequence")
    protected List<MatchersSequenceType> sequences;

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

}
