
package org.jooq.conf;

import java.io.Serializable;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


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
    implements Serializable, Cloneable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    protected String input;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(RegexAdapter.class)
    protected Pattern inputExpression;
    @XmlElement(required = true)
    protected String output;

    /**
     * The input table as defined in {@link org.jooq.Table#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided.
     * 
     */
    public String getInput() {
        return input;
    }

    /**
     * The input table as defined in {@link org.jooq.Table#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided.
     * 
     */
    public void setInput(String value) {
        this.input = value;
    }

    /**
     * A regular expression matching the input table name as defined in {@link org.jooq.Table#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public Pattern getInputExpression() {
        return inputExpression;
    }

    /**
     * A regular expression matching the input table name as defined in {@link org.jooq.Table#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public void setInputExpression(Pattern value) {
        this.inputExpression = value;
    }

    /**
     * The output table as it will be rendered in SQL.
     * <ul>
     * <li>When &lt;input/&gt; is provided, &lt;output/&gt; is a constant value.</li>
     * <li>When &lt;inputExpression/&gt; is provided, &lt;output/&gt; is a replacement expression.</li>
     * </ul>
     * 
     */
    public String getOutput() {
        return output;
    }

    /**
     * The output table as it will be rendered in SQL.
     * <ul>
     * <li>When &lt;input/&gt; is provided, &lt;output/&gt; is a constant value.</li>
     * <li>When &lt;inputExpression/&gt; is provided, &lt;output/&gt; is a replacement expression.</li>
     * </ul>
     * 
     */
    public void setOutput(String value) {
        this.output = value;
    }

    /**
     * The input table as defined in {@link org.jooq.Table#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided.
     * 
     */
    public MappedTable withInput(String value) {
        setInput(value);
        return this;
    }

    /**
     * A regular expression matching the input table name as defined in {@link org.jooq.Table#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public MappedTable withInputExpression(Pattern value) {
        setInputExpression(value);
        return this;
    }

    /**
     * The output table as it will be rendered in SQL.
     * <ul>
     * <li>When &lt;input/&gt; is provided, &lt;output/&gt; is a constant value.</li>
     * <li>When &lt;inputExpression/&gt; is provided, &lt;output/&gt; is a replacement expression.</li>
     * </ul>
     * 
     */
    public MappedTable withOutput(String value) {
        setOutput(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("input", input);
        builder.append("inputExpression", inputExpression);
        builder.append("output", output);
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
        MappedTable other = ((MappedTable) that);
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((input == null)? 0 :input.hashCode()));
        result = ((prime*result)+((inputExpression == null)? 0 :inputExpression.pattern().hashCode()));
        result = ((prime*result)+((output == null)? 0 :output.hashCode()));
        return result;
    }

}
