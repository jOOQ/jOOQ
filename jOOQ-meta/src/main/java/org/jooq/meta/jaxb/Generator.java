
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
public class Generator implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31400L;
    @XmlElement(defaultValue = "org.jooq.codegen.DefaultGenerator")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String name = "org.jooq.codegen.DefaultGenerator";
    protected Strategy strategy;
    protected Database database;
    protected Generate generate;
    protected Target target;

    /**
     * The class used to generate source code. This can be overridden with a custom code generator implementation.
     * 
     */
    public String getName() {
        return name;
    }

    /**
     * The class used to generate source code. This can be overridden with a custom code generator implementation.
     * 
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Definitions of custom naming strategies (declarative or programmatic) to define how generated Java objects should be named.
     * 
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Definitions of custom naming strategies (declarative or programmatic) to define how generated Java objects should be named.
     * 
     */
    public void setStrategy(Strategy value) {
        this.strategy = value;
    }

    /**
     * Configuration of the database meta data source.
     * 
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * Configuration of the database meta data source.
     * 
     */
    public void setDatabase(Database value) {
        this.database = value;
    }

    /**
     * Options strictly related to generated code.
     * 
     */
    public Generate getGenerate() {
        return generate;
    }

    /**
     * Options strictly related to generated code.
     * 
     */
    public void setGenerate(Generate value) {
        this.generate = value;
    }

    /**
     * Options to define where the generated code should be located.
     * 
     */
    public Target getTarget() {
        return target;
    }

    /**
     * Options to define where the generated code should be located.
     * 
     */
    public void setTarget(Target value) {
        this.target = value;
    }

    /**
     * The class used to generate source code. This can be overridden with a custom code generator implementation.
     * 
     */
    public Generator withName(String value) {
        setName(value);
        return this;
    }

    /**
     * Definitions of custom naming strategies (declarative or programmatic) to define how generated Java objects should be named.
     * 
     */
    public Generator withStrategy(Strategy value) {
        setStrategy(value);
        return this;
    }

    /**
     * Configuration of the database meta data source.
     * 
     */
    public Generator withDatabase(Database value) {
        setDatabase(value);
        return this;
    }

    /**
     * Options strictly related to generated code.
     * 
     */
    public Generator withGenerate(Generate value) {
        setGenerate(value);
        return this;
    }

    /**
     * Options to define where the generated code should be located.
     * 
     */
    public Generator withTarget(Target value) {
        setTarget(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("name", name);
        builder.append("strategy", strategy);
        builder.append("database", database);
        builder.append("generate", generate);
        builder.append("target", target);
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
