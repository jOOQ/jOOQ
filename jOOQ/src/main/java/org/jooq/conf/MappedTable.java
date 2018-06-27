







package org.jooq.conf;

import java.io.Serializable;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * A table mapping configuration.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MappedTable", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MappedTable
    extends SettingsBase
    implements Serializable, Cloneable
{

    private final static long serialVersionUID = 31008L;
    protected String input;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(RegexAdapter.class)
    protected Pattern inputExpression;
    @XmlElement(required = true)
    protected String output;

    /**
     * The input table as defined in {@link org.jooq.Table#getName()}
     * Either &lt;input/> or &lt;inputExpression/> must be provided.
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
     * Sets the value of the input property.
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
     * A regular expression matching the input table name as defined in {@link org.jooq.Table#getName()}
     * Either &lt;input/> or &lt;inputExpression/> must be provided
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
     * Sets the value of the inputExpression property.
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
     * The output table as it will be rendered in SQL.
     * <ul>
     * <li>When &lt;input/> is provided, &lt;output/> is a constant value.</li>
     * <li>When &lt;inputExpression/> is provided, &lt;output/> is a replacement expression.</li>
     * </ul>
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
     * Sets the value of the output property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOutput(String value) {
        this.output = value;
    }

    public MappedTable withInput(String value) {
        setInput(value);
        return this;
    }

    public MappedTable withInputExpression(Pattern value) {
        setInputExpression(value);
        return this;
    }

    public MappedTable withOutput(String value) {
        setOutput(value);
        return this;
    }

}
