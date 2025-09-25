
package org.jooq.conf;

import java.io.Serializable;
import java.util.regex.Pattern;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * A udt mapping configuration.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MappedUDT", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class MappedUDT
    extends SettingsBase
    implements Serializable, Cloneable, MappedSchemaObject, XMLAppendable
{

    private final static long serialVersionUID = 32008L;
    protected String input;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(RegexAdapter.class)
    protected Pattern inputExpression;
    @XmlElement(required = true)
    protected String output;

    /**
     * The input UDT as defined in {@link org.jooq.UDT#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided.
     * 
     */
    public String getInput() {
        return input;
    }

    /**
     * The input UDT as defined in {@link org.jooq.UDT#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided.
     * 
     */
    public void setInput(String value) {
        this.input = value;
    }

    /**
     * A regular expression matching the input UDT name as defined in {@link org.jooq.UDT#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public Pattern getInputExpression() {
        return inputExpression;
    }

    /**
     * A regular expression matching the input UDT name as defined in {@link org.jooq.UDT#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public void setInputExpression(Pattern value) {
        this.inputExpression = value;
    }

    /**
     * The output UDT as it will be rendered in SQL.
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
     * The output UDT as it will be rendered in SQL.
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
     * The input UDT as defined in {@link org.jooq.UDT#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided.
     * 
     */
    public MappedUDT withInput(String value) {
        setInput(value);
        return this;
    }

    /**
     * A regular expression matching the input UDT name as defined in {@link org.jooq.UDT#getName()}
     * <p>
     * Either &lt;input/&gt; or &lt;inputExpression/&gt; must be provided
     * 
     */
    public MappedUDT withInputExpression(Pattern value) {
        setInputExpression(value);
        return this;
    }

    /**
     * The output UDT as it will be rendered in SQL.
     * <ul>
     * <li>When &lt;input/&gt; is provided, &lt;output/&gt; is a constant value.</li>
     * <li>When &lt;inputExpression/&gt; is provided, &lt;output/&gt; is a replacement expression.</li>
     * </ul>
     * 
     */
    public MappedUDT withOutput(String value) {
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
        MappedUDT other = ((MappedUDT) that);
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
