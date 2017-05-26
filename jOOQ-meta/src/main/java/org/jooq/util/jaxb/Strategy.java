







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


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
public class Strategy implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlElement(defaultValue = "org.jooq.util.DefaultGeneratorStrategy")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name = "org.jooq.util.DefaultGeneratorStrategy";
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

    public Strategy withName(String value) {
        setName(value);
        return this;
    }

    public Strategy withMatchers(Matchers value) {
        setMatchers(value);
        return this;
    }

}
