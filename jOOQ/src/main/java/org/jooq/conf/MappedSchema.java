







package org.jooq.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java-Klasse für MappedSchema complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="MappedSchema"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="input" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="inputExpression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="output" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tables" type="{http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd}MappedTables" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MappedSchema", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MappedSchema
    extends SettingsBase
    implements Serializable, Cloneable
{

    private final static long serialVersionUID = 31000L;
    protected String input;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(RegexAdapter.class)
    protected Pattern inputExpression;
    protected String output;
    @XmlElementWrapper(name = "tables")
    @XmlElement(name = "table")
    protected List<MappedTable> tables;

    /**
     * Ruft den Wert der input-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInput() {
        return input;
    }

    /**
     * Legt den Wert der input-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInput(String value) {
        this.input = value;
    }

    /**
     * Ruft den Wert der inputExpression-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public Pattern getInputExpression() {
        return inputExpression;
    }

    /**
     * Legt den Wert der inputExpression-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInputExpression(Pattern value) {
        this.inputExpression = value;
    }

    /**
     * Ruft den Wert der output-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOutput() {
        return output;
    }

    /**
     * Legt den Wert der output-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOutput(String value) {
        this.output = value;
    }

    public List<MappedTable> getTables() {
        if (tables == null) {
            tables = new ArrayList<MappedTable>();
        }
        return tables;
    }

    public void setTables(List<MappedTable> tables) {
        this.tables = tables;
    }

    public MappedSchema withInput(String value) {
        setInput(value);
        return this;
    }

    public MappedSchema withInputExpression(Pattern value) {
        setInputExpression(value);
        return this;
    }

    public MappedSchema withOutput(String value) {
        setOutput(value);
        return this;
    }

    public MappedSchema withTables(MappedTable... values) {
        if (values!= null) {
            for (MappedTable value: values) {
                getTables().add(value);
            }
        }
        return this;
    }

    public MappedSchema withTables(Collection<MappedTable> values) {
        if (values!= null) {
            getTables().addAll(values);
        }
        return this;
    }

    public MappedSchema withTables(List<MappedTable> tables) {
        setTables(tables);
        return this;
    }

}
