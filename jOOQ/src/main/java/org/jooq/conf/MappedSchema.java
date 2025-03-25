
package org.jooq.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


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
    implements Serializable, Cloneable, XMLAppendable
{

    private final static long serialVersionUID = 31900L;
    protected String input;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(RegexAdapter.class)
    protected Pattern inputExpression;
    protected String output;
    @XmlElementWrapper(name = "tables")
    @XmlElement(name = "table")
    protected List<MappedTable> tables;
    @XmlElementWrapper(name = "udts")
    @XmlElement(name = "udt")
    protected List<MappedUDT> udts;

    /**
     * The input schema name as defined in {@link org.jooq.Schema#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public String getInput() {
        return input;
    }

    /**
     * The input schema name as defined in {@link org.jooq.Schema#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public void setInput(String value) {
        this.input = value;
    }

    /**
     * A regular expression matching the input schema name as defined in {@link org.jooq.Schema#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public Pattern getInputExpression() {
        return inputExpression;
    }

    /**
     * A regular expression matching the input schema name as defined in {@link org.jooq.Schema#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public void setInputExpression(Pattern value) {
        this.inputExpression = value;
    }

    /**
     * The output schema as it will be rendered in SQL.
     * <ul>
     * <li>When this is omitted, you can still apply table mapping.</li>
     * <li>When &lt;input/&gt; is provided, &lt;output/&gt; is a constant value.</li>
     * <li>When &lt;inputExpression/&gt; is provided, &lt;output/&gt; is a replacement expression</li>
     * </ul>
     * 
     */
    public String getOutput() {
        return output;
    }

    /**
     * The output schema as it will be rendered in SQL.
     * <ul>
     * <li>When this is omitted, you can still apply table mapping.</li>
     * <li>When &lt;input/&gt; is provided, &lt;output/&gt; is a constant value.</li>
     * <li>When &lt;inputExpression/&gt; is provided, &lt;output/&gt; is a replacement expression</li>
     * </ul>
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

    public List<MappedUDT> getUdts() {
        if (udts == null) {
            udts = new ArrayList<MappedUDT>();
        }
        return udts;
    }

    public void setUdts(List<MappedUDT> udts) {
        this.udts = udts;
    }

    /**
     * The input schema name as defined in {@link org.jooq.Schema#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public MappedSchema withInput(String value) {
        setInput(value);
        return this;
    }

    /**
     * A regular expression matching the input schema name as defined in {@link org.jooq.Schema#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public MappedSchema withInputExpression(Pattern value) {
        setInputExpression(value);
        return this;
    }

    /**
     * The output schema as it will be rendered in SQL.
     * <ul>
     * <li>When this is omitted, you can still apply table mapping.</li>
     * <li>When &lt;input/&gt; is provided, &lt;output/&gt; is a constant value.</li>
     * <li>When &lt;inputExpression/&gt; is provided, &lt;output/&gt; is a replacement expression</li>
     * </ul>
     * 
     */
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

    public MappedSchema withUdts(MappedUDT... values) {
        if (values!= null) {
            for (MappedUDT value: values) {
                getUdts().add(value);
            }
        }
        return this;
    }

    public MappedSchema withUdts(Collection<MappedUDT> values) {
        if (values!= null) {
            getUdts().addAll(values);
        }
        return this;
    }

    public MappedSchema withUdts(List<MappedUDT> udts) {
        setUdts(udts);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("input", input);
        builder.append("inputExpression", inputExpression);
        builder.append("output", output);
        builder.append("tables", "table", tables);
        builder.append("udts", "udt", udts);
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
            if (!inputExpression.pattern().equals(other.inputExpression.pattern())) {
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
        if ((tables == null)||tables.isEmpty()) {
            if ((other.tables!= null)&&(!other.tables.isEmpty())) {
                return false;
            }
        } else {
            if (!tables.equals(other.tables)) {
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((input == null)? 0 :input.hashCode()));
        result = ((prime*result)+((inputExpression == null)? 0 :inputExpression.pattern().hashCode()));
        result = ((prime*result)+((output == null)? 0 :output.hashCode()));
        result = ((prime*result)+(((tables == null)||tables.isEmpty())? 0 :tables.hashCode()));
        result = ((prime*result)+(((udts == null)||udts.isEmpty())? 0 :udts.hashCode()));
        return result;
    }

}
