
package org.jooq.util.xml.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * <p>Java class for Column complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Column"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="table_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="table_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="column_name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="data_type" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="character_maximum_length" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_precision" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="numeric_scale" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="domain_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="domain_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="domain_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="udt_catalog" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="udt_schema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="udt_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ordinal_position" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="identity_generation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="is_nullable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="column_default" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="readonly" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="is_generated" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="generation_expression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="generation_option" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Column", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Column implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31900L;
    @XmlElement(name = "table_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableCatalog;
    @XmlElement(name = "table_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableSchema;
    @XmlElement(name = "table_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String tableName;
    @XmlElement(name = "column_name", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String columnName;
    @XmlElement(name = "data_type", required = true)
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String dataType;
    @XmlElement(name = "character_maximum_length")
    protected Integer characterMaximumLength;
    @XmlElement(name = "numeric_precision")
    protected Integer numericPrecision;
    @XmlElement(name = "numeric_scale")
    protected Integer numericScale;
    @XmlElement(name = "domain_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String domainCatalog;
    @XmlElement(name = "domain_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String domainSchema;
    @XmlElement(name = "domain_name")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String domainName;
    @XmlElement(name = "udt_catalog")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtCatalog;
    @XmlElement(name = "udt_schema")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtSchema;
    @XmlElement(name = "udt_name")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String udtName;
    @XmlElement(name = "ordinal_position")
    protected Integer ordinalPosition;
    @XmlElement(name = "identity_generation")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String identityGeneration;
    @XmlElement(name = "is_nullable")
    protected Boolean isNullable;
    @XmlElement(name = "column_default")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String columnDefault;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String comment;
    protected Boolean readonly;
    @XmlElement(name = "is_generated")
    protected Boolean isGenerated;
    @XmlElement(name = "generation_expression")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String generationExpression;
    @XmlElement(name = "generation_option")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String generationOption;

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String value) {
        this.tableCatalog = value;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String value) {
        this.tableSchema = value;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String value) {
        this.tableName = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String value) {
        this.columnName = value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String value) {
        this.dataType = value;
    }

    public Integer getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(Integer value) {
        this.characterMaximumLength = value;
    }

    public Integer getNumericPrecision() {
        return numericPrecision;
    }

    public void setNumericPrecision(Integer value) {
        this.numericPrecision = value;
    }

    public Integer getNumericScale() {
        return numericScale;
    }

    public void setNumericScale(Integer value) {
        this.numericScale = value;
    }

    public String getDomainCatalog() {
        return domainCatalog;
    }

    public void setDomainCatalog(String value) {
        this.domainCatalog = value;
    }

    public String getDomainSchema() {
        return domainSchema;
    }

    public void setDomainSchema(String value) {
        this.domainSchema = value;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String value) {
        this.domainName = value;
    }

    public String getUdtCatalog() {
        return udtCatalog;
    }

    public void setUdtCatalog(String value) {
        this.udtCatalog = value;
    }

    public String getUdtSchema() {
        return udtSchema;
    }

    public void setUdtSchema(String value) {
        this.udtSchema = value;
    }

    public String getUdtName() {
        return udtName;
    }

    public void setUdtName(String value) {
        this.udtName = value;
    }

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Integer value) {
        this.ordinalPosition = value;
    }

    public String getIdentityGeneration() {
        return identityGeneration;
    }

    public void setIdentityGeneration(String value) {
        this.identityGeneration = value;
    }

    /**
     * Gets the value of the isNullable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNullable() {
        return isNullable;
    }

    /**
     * Sets the value of the isNullable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNullable(Boolean value) {
        this.isNullable = value;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String value) {
        this.columnDefault = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the readonly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReadonly() {
        return readonly;
    }

    /**
     * Sets the value of the readonly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReadonly(Boolean value) {
        this.readonly = value;
    }

    /**
     * Gets the value of the isGenerated property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsGenerated() {
        return isGenerated;
    }

    /**
     * Sets the value of the isGenerated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsGenerated(Boolean value) {
        this.isGenerated = value;
    }

    public String getGenerationExpression() {
        return generationExpression;
    }

    public void setGenerationExpression(String value) {
        this.generationExpression = value;
    }

    public String getGenerationOption() {
        return generationOption;
    }

    public void setGenerationOption(String value) {
        this.generationOption = value;
    }

    public Column withTableCatalog(String value) {
        setTableCatalog(value);
        return this;
    }

    public Column withTableSchema(String value) {
        setTableSchema(value);
        return this;
    }

    public Column withTableName(String value) {
        setTableName(value);
        return this;
    }

    public Column withColumnName(String value) {
        setColumnName(value);
        return this;
    }

    public Column withDataType(String value) {
        setDataType(value);
        return this;
    }

    public Column withCharacterMaximumLength(Integer value) {
        setCharacterMaximumLength(value);
        return this;
    }

    public Column withNumericPrecision(Integer value) {
        setNumericPrecision(value);
        return this;
    }

    public Column withNumericScale(Integer value) {
        setNumericScale(value);
        return this;
    }

    public Column withDomainCatalog(String value) {
        setDomainCatalog(value);
        return this;
    }

    public Column withDomainSchema(String value) {
        setDomainSchema(value);
        return this;
    }

    public Column withDomainName(String value) {
        setDomainName(value);
        return this;
    }

    public Column withUdtCatalog(String value) {
        setUdtCatalog(value);
        return this;
    }

    public Column withUdtSchema(String value) {
        setUdtSchema(value);
        return this;
    }

    public Column withUdtName(String value) {
        setUdtName(value);
        return this;
    }

    public Column withOrdinalPosition(Integer value) {
        setOrdinalPosition(value);
        return this;
    }

    public Column withIdentityGeneration(String value) {
        setIdentityGeneration(value);
        return this;
    }

    public Column withIsNullable(Boolean value) {
        setIsNullable(value);
        return this;
    }

    public Column withColumnDefault(String value) {
        setColumnDefault(value);
        return this;
    }

    public Column withComment(String value) {
        setComment(value);
        return this;
    }

    public Column withReadonly(Boolean value) {
        setReadonly(value);
        return this;
    }

    public Column withIsGenerated(Boolean value) {
        setIsGenerated(value);
        return this;
    }

    public Column withGenerationExpression(String value) {
        setGenerationExpression(value);
        return this;
    }

    public Column withGenerationOption(String value) {
        setGenerationOption(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("table_catalog", tableCatalog);
        builder.append("table_schema", tableSchema);
        builder.append("table_name", tableName);
        builder.append("column_name", columnName);
        builder.append("data_type", dataType);
        builder.append("character_maximum_length", characterMaximumLength);
        builder.append("numeric_precision", numericPrecision);
        builder.append("numeric_scale", numericScale);
        builder.append("domain_catalog", domainCatalog);
        builder.append("domain_schema", domainSchema);
        builder.append("domain_name", domainName);
        builder.append("udt_catalog", udtCatalog);
        builder.append("udt_schema", udtSchema);
        builder.append("udt_name", udtName);
        builder.append("ordinal_position", ordinalPosition);
        builder.append("identity_generation", identityGeneration);
        builder.append("is_nullable", isNullable);
        builder.append("column_default", columnDefault);
        builder.append("comment", comment);
        builder.append("readonly", readonly);
        builder.append("is_generated", isGenerated);
        builder.append("generation_expression", generationExpression);
        builder.append("generation_option", generationOption);
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
        Column other = ((Column) that);
        if (tableCatalog == null) {
            if (other.tableCatalog!= null) {
                return false;
            }
        } else {
            if (!tableCatalog.equals(other.tableCatalog)) {
                return false;
            }
        }
        if (tableSchema == null) {
            if (other.tableSchema!= null) {
                return false;
            }
        } else {
            if (!tableSchema.equals(other.tableSchema)) {
                return false;
            }
        }
        if (tableName == null) {
            if (other.tableName!= null) {
                return false;
            }
        } else {
            if (!tableName.equals(other.tableName)) {
                return false;
            }
        }
        if (columnName == null) {
            if (other.columnName!= null) {
                return false;
            }
        } else {
            if (!columnName.equals(other.columnName)) {
                return false;
            }
        }
        if (dataType == null) {
            if (other.dataType!= null) {
                return false;
            }
        } else {
            if (!dataType.equals(other.dataType)) {
                return false;
            }
        }
        if (characterMaximumLength == null) {
            if (other.characterMaximumLength!= null) {
                return false;
            }
        } else {
            if (!characterMaximumLength.equals(other.characterMaximumLength)) {
                return false;
            }
        }
        if (numericPrecision == null) {
            if (other.numericPrecision!= null) {
                return false;
            }
        } else {
            if (!numericPrecision.equals(other.numericPrecision)) {
                return false;
            }
        }
        if (numericScale == null) {
            if (other.numericScale!= null) {
                return false;
            }
        } else {
            if (!numericScale.equals(other.numericScale)) {
                return false;
            }
        }
        if (domainCatalog == null) {
            if (other.domainCatalog!= null) {
                return false;
            }
        } else {
            if (!domainCatalog.equals(other.domainCatalog)) {
                return false;
            }
        }
        if (domainSchema == null) {
            if (other.domainSchema!= null) {
                return false;
            }
        } else {
            if (!domainSchema.equals(other.domainSchema)) {
                return false;
            }
        }
        if (domainName == null) {
            if (other.domainName!= null) {
                return false;
            }
        } else {
            if (!domainName.equals(other.domainName)) {
                return false;
            }
        }
        if (udtCatalog == null) {
            if (other.udtCatalog!= null) {
                return false;
            }
        } else {
            if (!udtCatalog.equals(other.udtCatalog)) {
                return false;
            }
        }
        if (udtSchema == null) {
            if (other.udtSchema!= null) {
                return false;
            }
        } else {
            if (!udtSchema.equals(other.udtSchema)) {
                return false;
            }
        }
        if (udtName == null) {
            if (other.udtName!= null) {
                return false;
            }
        } else {
            if (!udtName.equals(other.udtName)) {
                return false;
            }
        }
        if (ordinalPosition == null) {
            if (other.ordinalPosition!= null) {
                return false;
            }
        } else {
            if (!ordinalPosition.equals(other.ordinalPosition)) {
                return false;
            }
        }
        if (identityGeneration == null) {
            if (other.identityGeneration!= null) {
                return false;
            }
        } else {
            if (!identityGeneration.equals(other.identityGeneration)) {
                return false;
            }
        }
        if (isNullable == null) {
            if (other.isNullable!= null) {
                return false;
            }
        } else {
            if (!isNullable.equals(other.isNullable)) {
                return false;
            }
        }
        if (columnDefault == null) {
            if (other.columnDefault!= null) {
                return false;
            }
        } else {
            if (!columnDefault.equals(other.columnDefault)) {
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
        if (readonly == null) {
            if (other.readonly!= null) {
                return false;
            }
        } else {
            if (!readonly.equals(other.readonly)) {
                return false;
            }
        }
        if (isGenerated == null) {
            if (other.isGenerated!= null) {
                return false;
            }
        } else {
            if (!isGenerated.equals(other.isGenerated)) {
                return false;
            }
        }
        if (generationExpression == null) {
            if (other.generationExpression!= null) {
                return false;
            }
        } else {
            if (!generationExpression.equals(other.generationExpression)) {
                return false;
            }
        }
        if (generationOption == null) {
            if (other.generationOption!= null) {
                return false;
            }
        } else {
            if (!generationOption.equals(other.generationOption)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((tableCatalog == null)? 0 :tableCatalog.hashCode()));
        result = ((prime*result)+((tableSchema == null)? 0 :tableSchema.hashCode()));
        result = ((prime*result)+((tableName == null)? 0 :tableName.hashCode()));
        result = ((prime*result)+((columnName == null)? 0 :columnName.hashCode()));
        result = ((prime*result)+((dataType == null)? 0 :dataType.hashCode()));
        result = ((prime*result)+((characterMaximumLength == null)? 0 :characterMaximumLength.hashCode()));
        result = ((prime*result)+((numericPrecision == null)? 0 :numericPrecision.hashCode()));
        result = ((prime*result)+((numericScale == null)? 0 :numericScale.hashCode()));
        result = ((prime*result)+((domainCatalog == null)? 0 :domainCatalog.hashCode()));
        result = ((prime*result)+((domainSchema == null)? 0 :domainSchema.hashCode()));
        result = ((prime*result)+((domainName == null)? 0 :domainName.hashCode()));
        result = ((prime*result)+((udtCatalog == null)? 0 :udtCatalog.hashCode()));
        result = ((prime*result)+((udtSchema == null)? 0 :udtSchema.hashCode()));
        result = ((prime*result)+((udtName == null)? 0 :udtName.hashCode()));
        result = ((prime*result)+((ordinalPosition == null)? 0 :ordinalPosition.hashCode()));
        result = ((prime*result)+((identityGeneration == null)? 0 :identityGeneration.hashCode()));
        result = ((prime*result)+((isNullable == null)? 0 :isNullable.hashCode()));
        result = ((prime*result)+((columnDefault == null)? 0 :columnDefault.hashCode()));
        result = ((prime*result)+((comment == null)? 0 :comment.hashCode()));
        result = ((prime*result)+((readonly == null)? 0 :readonly.hashCode()));
        result = ((prime*result)+((isGenerated == null)? 0 :isGenerated.hashCode()));
        result = ((prime*result)+((generationExpression == null)? 0 :generationExpression.hashCode()));
        result = ((prime*result)+((generationOption == null)? 0 :generationOption.hashCode()));
        return result;
    }

}
