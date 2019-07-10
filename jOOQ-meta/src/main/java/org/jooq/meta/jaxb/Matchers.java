
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
 * Declarative naming strategy configuration.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Matchers", propOrder = {
    "schemas",
    "tables",
    "fields",
    "routines",
    "sequences",
    "enums"
})
@SuppressWarnings({
    "all"
})
public class Matchers implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
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
    @XmlElementWrapper(name = "enums")
    @XmlElement(name = "enum")
    protected List<MatchersEnumType> enums;

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

    public List<MatchersEnumType> getEnums() {
        if (enums == null) {
            enums = new ArrayList<MatchersEnumType>();
        }
        return enums;
    }

    public void setEnums(List<MatchersEnumType> enums) {
        this.enums = enums;
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

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("schemas", "schema", schemas);
        builder.append("tables", "table", tables);
        builder.append("fields", "field", fields);
        builder.append("routines", "routine", routines);
        builder.append("sequences", "sequence", sequences);
        builder.append("enums", "enum", enums);
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
        if (schemas == null) {
            if (other.schemas!= null) {
                return false;
            }
        } else {
            if (!schemas.equals(other.schemas)) {
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
        if (fields == null) {
            if (other.fields!= null) {
                return false;
            }
        } else {
            if (!fields.equals(other.fields)) {
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
        if (sequences == null) {
            if (other.sequences!= null) {
                return false;
            }
        } else {
            if (!sequences.equals(other.sequences)) {
                return false;
            }
        }
        if (enums == null) {
            if (other.enums!= null) {
                return false;
            }
        } else {
            if (!enums.equals(other.enums)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((schemas == null)? 0 :schemas.hashCode()));
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        result = ((prime*result)+((fields == null)? 0 :fields.hashCode()));
        result = ((prime*result)+((routines == null)? 0 :routines.hashCode()));
        result = ((prime*result)+((sequences == null)? 0 :sequences.hashCode()));
        result = ((prime*result)+((enums == null)? 0 :enums.hashCode()));
        return result;
    }

}
