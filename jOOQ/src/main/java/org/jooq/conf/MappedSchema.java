







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
 * A schema mapping configuration.
 *
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

    private final static long serialVersionUID = 31100L;
    protected String input;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(RegexAdapter.class)
    protected Pattern inputExpression;
    protected String output;
    @XmlElementWrapper(name = "tables")
    @XmlElement(name = "table")
    protected List<MappedTable> tables;

    /**
     * The input schema name as defined in {@link org.jooq.Schema#getName()}
     * <p>
     * Either &lt;input/> or &lt;inputExpression/> must be provided
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
     * A regular expression matching the input schema name as defined in {@link org.jooq.Schema#getName()}
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
     * The output schema as it will be rendered in SQL.
     * <ul>
     * <li>When this is omitted, you can still apply table mapping.</li>
     * <li>When &lt;input/> is provided, &lt;output/> is a constant value.</li>
     * <li>When &lt;inputExpression/> is provided, &lt;output/> is a replacement expression</li>
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (input!= null) {
            sb.append("<input>");
            sb.append(input);
            sb.append("</input>");
        }
        if (inputExpression!= null) {
            sb.append("<inputExpression>");
            sb.append(inputExpression);
            sb.append("</inputExpression>");
        }
        if (output!= null) {
            sb.append("<output>");
            sb.append(output);
            sb.append("</output>");
        }
        if (tables!= null) {
            sb.append("<tables>");
            sb.append(tables);
            sb.append("</tables>");
        }
        return sb.toString();
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
        MappedSchema other = ((MappedSchema) that);
        if (input == null) {
            if (other.input!= null) {
                return false;
            }
        } else {
            if (!input.equals(other.input)) {
                return false;
            }
        }
        if (inputExpression == null) {
            if (other.inputExpression!= null) {
                return false;
            }
        } else {
            if (!inputExpression.equals(other.inputExpression)) {
                return false;
            }
        }
        if (output == null) {
            if (other.output!= null) {
                return false;
            }
        } else {
            if (!output.equals(other.output)) {
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((input == null)? 0 :input.hashCode()));
        result = ((prime*result)+((inputExpression == null)? 0 :inputExpression.hashCode()));
        result = ((prime*result)+((output == null)? 0 :output.hashCode()));
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        return result;
    }

}
