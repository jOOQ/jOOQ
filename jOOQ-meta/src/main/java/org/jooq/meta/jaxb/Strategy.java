
package org.jooq.meta.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * Definitions of custom naming strategies (declarative or programmatic) to define how generated Java objects should be named.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Strategy", propOrder = {
    "name",
    "matchers"
})
@SuppressWarnings({
    "all"
})
public class Strategy implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlElement(defaultValue = "org.jooq.codegen.DefaultGeneratorStrategy")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name = "org.jooq.codegen.DefaultGeneratorStrategy";
    protected Matchers matchers;

    /**
     * The class used to provide a naming strategy for generated source code. You may override this with your custom naming strategy. This cannot be combined with a matcher configuration.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * The matcher strategy configuration used when applying an XML-based strategy. This cannot be combined with a named strategy configuration.
     *
     * @return
     *     possible object is
     *     {@link Matchers }
     *
     */
    public Matchers getMatchers() {
        return matchers;
    }

    /**
     * Sets the value of the matchers property.
     *
     * @param value
     *     allowed object is
     *     {@link Matchers }
     *
     */
    public void setMatchers(Matchers value) {
        this.matchers = value;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public Strategy withName(String value) {
        setName(value);
        return this;
    }

    /**
     * Sets the value of the matchers property.
     *
     * @param value
     *     allowed object is
     *     {@link Matchers }
     *
     */
    public Strategy withMatchers(Matchers value) {
        setMatchers(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("matchers", matchers);
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
        Strategy other = ((Strategy) that);
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
                return false;
            }
        }
        if (matchers == null) {
            if (other.matchers!= null) {
                return false;
            }
        } else {
            if (!matchers.equals(other.matchers)) {
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
        result = ((prime*result)+((matchers == null)? 0 :matchers.hashCode()));
        return result;
    }

}
