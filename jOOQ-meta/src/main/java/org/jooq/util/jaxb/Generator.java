







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * Configuration that affects the way code is being generated.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Generator", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Generator implements Serializable
{

    private final static long serialVersionUID = 31100L;
    @XmlElement(defaultValue = "org.jooq.util.DefaultGenerator")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name = "org.jooq.util.DefaultGenerator";
    protected Strategy strategy;
    protected Database database;
    protected Generate generate;
    protected Target target;

    /**
     * The class used to generate source code. This can be overridden with a custom code generator implementation.
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
     * Definitions of custom naming strategies (declarative or programmatic) to define how generated Java objects should be named.
     *
     * @return
     *     possible object is
     *     {@link Strategy }
     *
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Sets the value of the strategy property.
     *
     * @param value
     *     allowed object is
     *     {@link Strategy }
     *
     */
    public void setStrategy(Strategy value) {
        this.strategy = value;
    }

    /**
     * Configuration of the database meta data source.
     *
     * @return
     *     possible object is
     *     {@link Database }
     *
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * Sets the value of the database property.
     *
     * @param value
     *     allowed object is
     *     {@link Database }
     *
     */
    public void setDatabase(Database value) {
        this.database = value;
    }

    /**
     * Options strictly related to generated code.
     *
     * @return
     *     possible object is
     *     {@link Generate }
     *
     */
    public Generate getGenerate() {
        return generate;
    }

    /**
     * Sets the value of the generate property.
     *
     * @param value
     *     allowed object is
     *     {@link Generate }
     *
     */
    public void setGenerate(Generate value) {
        this.generate = value;
    }

    /**
     * Options to define where the generated code should be located.
     *
     * @return
     *     possible object is
     *     {@link Target }
     *
     */
    public Target getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     *
     * @param value
     *     allowed object is
     *     {@link Target }
     *
     */
    public void setTarget(Target value) {
        this.target = value;
    }

    public Generator withName(String value) {
        setName(value);
        return this;
    }

    public Generator withStrategy(Strategy value) {
        setStrategy(value);
        return this;
    }

    public Generator withDatabase(Database value) {
        setDatabase(value);
        return this;
    }

    public Generator withGenerate(Generate value) {
        setGenerate(value);
        return this;
    }

    public Generator withTarget(Target value) {
        setTarget(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (name!= null) {
            sb.append("<name>");
            sb.append(name);
            sb.append("</name>");
        }
        if (strategy!= null) {
            sb.append("<strategy>");
            sb.append(strategy);
            sb.append("</strategy>");
        }
        if (database!= null) {
            sb.append("<database>");
            sb.append(database);
            sb.append("</database>");
        }
        if (generate!= null) {
            sb.append("<generate>");
            sb.append(generate);
            sb.append("</generate>");
        }
        if (target!= null) {
            sb.append("<target>");
            sb.append(target);
            sb.append("</target>");
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
        Generator other = ((Generator) that);
        if (name == null) {
            if (other.name!= null) {
                return false;
            }
        } else {
            if (!name.equals(other.name)) {
                return false;
            }
        }
        if (strategy == null) {
            if (other.strategy!= null) {
                return false;
            }
        } else {
            if (!strategy.equals(other.strategy)) {
                return false;
            }
        }
        if (database == null) {
            if (other.database!= null) {
                return false;
            }
        } else {
            if (!database.equals(other.database)) {
                return false;
            }
        }
        if (generate == null) {
            if (other.generate!= null) {
                return false;
            }
        } else {
            if (!generate.equals(other.generate)) {
                return false;
            }
        }
        if (target == null) {
            if (other.target!= null) {
                return false;
            }
        } else {
            if (!target.equals(other.target)) {
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
        result = ((prime*result)+((strategy == null)? 0 :strategy.hashCode()));
        result = ((prime*result)+((database == null)? 0 :database.hashCode()));
        result = ((prime*result)+((generate == null)? 0 :generate.hashCode()));
        result = ((prime*result)+((target == null)? 0 :target.hashCode()));
        return result;
    }

}
