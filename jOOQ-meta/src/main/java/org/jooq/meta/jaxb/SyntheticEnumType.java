
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
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for SyntheticEnumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SyntheticEnumType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tables" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fields" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="literals" type="{http://www.jooq.org/xsd/jooq-codegen-3.20.1.xsd}SyntheticEnumLiteralsType" minOccurs="0"/&gt;
 *         &lt;element name="literalSql" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="literalsFromColumnContent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="literalsFromCheckConstraints" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ignoreUnused" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyntheticEnumType", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class SyntheticEnumType implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32001L;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tables;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String fields;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String literalSql;
    protected Boolean literalsFromColumnContent;
    protected Boolean literalsFromCheckConstraints;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;
    @XmlElement(defaultValue = "false")
    protected Boolean ignoreUnused = false;
    @XmlElementWrapper(name = "literals")
    @XmlElement(name = "literal")
    protected List<String> literals;

    /**
     * The optional enum name. If provided, then literals must match for each table/field match (though matches are optional). If omitted, then the name is generated based on each table/field match.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The optional enum name. If provided, then literals must match for each table/field match (though matches are optional). If omitted, then the name is generated based on each table/field match.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic enum type.
     * 
     */
    public String getTables() {
        return tables;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic enum type.
     * 
     */
    public void setTables(String value) {
        this.tables = value;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic enum type.
     * 
     */
    public String getFields() {
        return fields;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic enum type.
     * 
     */
    public void setFields(String value) {
        this.fields = value;
    }

    /**
     * A SQL query producing the literals.
     * 
     */
    public String getLiteralSql() {
        return literalSql;
    }

    /**
     * A SQL query producing the literals.
     * 
     */
    public void setLiteralSql(String value) {
        this.literalSql = value;
    }

    /**
     * The matched column's content defines the literals (this is convenience for {@link #literalSql} being <code>SELECT DISTINCT matched_column FROM matched_table</code>).
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLiteralsFromColumnContent() {
        return literalsFromColumnContent;
    }

    /**
     * The matched column's content defines the literals (this is convenience for {@link #literalSql} being <code>SELECT DISTINCT matched_column FROM matched_table</code>).
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLiteralsFromColumnContent(Boolean value) {
        this.literalsFromColumnContent = value;
    }

    /**
     * The list of literals is parsed from the applicable <code>CHECK</code> constraints for the matched column, if possible.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLiteralsFromCheckConstraints() {
        return literalsFromCheckConstraints;
    }

    /**
     * The list of literals is parsed from the applicable <code>CHECK</code> constraints for the matched column, if possible.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLiteralsFromCheckConstraints(Boolean value) {
        this.literalsFromCheckConstraints = value;
    }

    /**
     * The enum comment.
     * 
     */
    public String getComment() {
        return comment;
    }

    /**
     * The enum comment.
     * 
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIgnoreUnused() {
        return ignoreUnused;
    }

    /**
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIgnoreUnused(Boolean value) {
        this.ignoreUnused = value;
    }

    public List<String> getLiterals() {
        if (literals == null) {
            literals = new ArrayList<String>();
        }
        return literals;
    }

    public void setLiterals(List<String> literals) {
        this.literals = literals;
    }

    /**
     * The optional enum name. If provided, then literals must match for each table/field match (though matches are optional). If omitted, then the name is generated based on each table/field match.
     * 
     */
    public SyntheticEnumType withName(String value) {
        setName(value);
        return this;
    }

    /**
     * A regular expression matching all tables on which to apply this synthetic enum type.
     * 
     */
    public SyntheticEnumType withTables(String value) {
        setTables(value);
        return this;
    }

    /**
     * A regular expression matching all fields on which to apply this synthetic enum type.
     * 
     */
    public SyntheticEnumType withFields(String value) {
        setFields(value);
        return this;
    }

    /**
     * A SQL query producing the literals.
     * 
     */
    public SyntheticEnumType withLiteralSql(String value) {
        setLiteralSql(value);
        return this;
    }

    /**
     * The matched column's content defines the literals (this is convenience for {@link #literalSql} being <code>SELECT DISTINCT matched_column FROM matched_table</code>).
     * 
     */
    public SyntheticEnumType withLiteralsFromColumnContent(Boolean value) {
        setLiteralsFromColumnContent(value);
        return this;
    }

    /**
     * The list of literals is parsed from the applicable <code>CHECK</code> constraints for the matched column, if possible.
     * 
     */
    public SyntheticEnumType withLiteralsFromCheckConstraints(Boolean value) {
        setLiteralsFromCheckConstraints(value);
        return this;
    }

    /**
     * The enum comment.
     * 
     */
    public SyntheticEnumType withComment(String value) {
        setComment(value);
        return this;
    }

    /**
     * Set this flag to true if no warning should be logged if this object was not used by a code generation run.
     * 
     */
    public SyntheticEnumType withIgnoreUnused(Boolean value) {
        setIgnoreUnused(value);
        return this;
    }

    public SyntheticEnumType withLiterals(String... values) {
        if (values!= null) {
            for (String value: values) {
                getLiterals().add(value);
            }
        }
        return this;
    }

    public SyntheticEnumType withLiterals(Collection<String> values) {
        if (values!= null) {
            getLiterals().addAll(values);
        }
        return this;
    }

    public SyntheticEnumType withLiterals(List<String> literals) {
        setLiterals(literals);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("tables", tables);
        builder.append("fields", fields);
        builder.append("literalSql", literalSql);
        builder.append("literalsFromColumnContent", literalsFromColumnContent);
        builder.append("literalsFromCheckConstraints", literalsFromCheckConstraints);
        builder.append("comment", comment);
        builder.append("ignoreUnused", ignoreUnused);
        builder.append("literals", "literal", literals);
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
        SyntheticEnumType other = ((SyntheticEnumType) that);
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
        if (fields == null) {
            if (other.fields!= null) {
                return false;
            }
        } else {
            if (!fields.equals(other.fields)) {
                return false;
            }
        }
        if (literalSql == null) {
            if (other.literalSql!= null) {
                return false;
            }
        } else {
            if (!literalSql.equals(other.literalSql)) {
                return false;
            }
        }
        if (literalsFromColumnContent == null) {
            if (other.literalsFromColumnContent!= null) {
                return false;
            }
        } else {
            if (!literalsFromColumnContent.equals(other.literalsFromColumnContent)) {
                return false;
            }
        }
        if (literalsFromCheckConstraints == null) {
            if (other.literalsFromCheckConstraints!= null) {
                return false;
            }
        } else {
            if (!literalsFromCheckConstraints.equals(other.literalsFromCheckConstraints)) {
                return false;
            }
        }
        if (comment == null) {
            if (other.comment!= null) {
                return false;
            }
        } else {
            if (!comment.equals(other.comment)) {
                return false;
            }
        }
        if (ignoreUnused == null) {
            if (other.ignoreUnused!= null) {
                return false;
            }
        } else {
            if (!ignoreUnused.equals(other.ignoreUnused)) {
                return false;
            }
        }
        if (literals == null) {
            if (other.literals!= null) {
                return false;
            }
        } else {
            if (!literals.equals(other.literals)) {
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
        result = ((prime*result)+((fields == null)? 0 :fields.hashCode()));
        result = ((prime*result)+((literalSql == null)? 0 :literalSql.hashCode()));
        result = ((prime*result)+((literalsFromColumnContent == null)? 0 :literalsFromColumnContent.hashCode()));
        result = ((prime*result)+((literalsFromCheckConstraints == null)? 0 :literalsFromCheckConstraints.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        result = ((prime*result)+((ignoreUnused == null)? 0 :ignoreUnused.hashCode()));
        result = ((prime*result)+((literals == null)? 0 :literals.hashCode()));
        return result;
    }

}
