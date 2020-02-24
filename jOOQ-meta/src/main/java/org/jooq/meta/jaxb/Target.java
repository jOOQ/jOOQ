
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
 * Options to define where the generated code should be located.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Target", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Target implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 31200L;
    @XmlElement(defaultValue = "org.jooq.generated")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String packageName = "org.jooq.generated";
    @XmlElement(defaultValue = "target/generated-sources/jooq")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String directory = "target/generated-sources/jooq";
    @XmlElement(defaultValue = "UTF-8")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String encoding = "UTF-8";
    @XmlElement(defaultValue = "true")
    protected Boolean clean = true;

    /**
     * The destination package of your generated classes (within the destination directory)
     * <p>
     * jOOQ may append the schema name to this package if generating multiple schemas,
     * e.g. org.jooq.generated.schema1, org.jooq.generated.schema2
     * 
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * The destination package of your generated classes (within the destination directory)
     * <p>
     * jOOQ may append the schema name to this package if generating multiple schemas,
     * e.g. org.jooq.generated.schema1, org.jooq.generated.schema2
     * 
     */
    public void setPackageName(String value) {
        this.packageName = value;
    }

    /**
     * The destination directory of your generated classes
     * 
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * The destination directory of your generated classes
     * 
     */
    public void setDirectory(String value) {
        this.directory = value;
    }

    /**
     * The file encoding to be used with all output files.
     * 
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * The file encoding to be used with all output files.
     * 
     */
    public void setEncoding(String value) {
        this.encoding = value;
    }

    /**
     * Whether the target package should be cleaned to contain only generated code after a generation run.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isClean() {
        return clean;
    }

    /**
     * Sets the value of the clean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setClean(Boolean value) {
        this.clean = value;
    }

    /**
     * The destination package of your generated classes (within the destination directory)
     * <p>
     * jOOQ may append the schema name to this package if generating multiple schemas,
     * e.g. org.jooq.generated.schema1, org.jooq.generated.schema2
     * 
     */
    public Target withPackageName(String value) {
        setPackageName(value);
        return this;
    }

    /**
     * The destination directory of your generated classes
     * 
     */
    public Target withDirectory(String value) {
        setDirectory(value);
        return this;
    }

    /**
     * The file encoding to be used with all output files.
     * 
     */
    public Target withEncoding(String value) {
        setEncoding(value);
        return this;
    }

    public Target withClean(Boolean value) {
        setClean(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("packageName", packageName);
        builder.append("directory", directory);
        builder.append("encoding", encoding);
        builder.append("clean", clean);
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
        Target other = ((Target) that);
        if (packageName == null) {
            if (other.packageName!= null) {
                return false;
            }
        } else {
            if (!packageName.equals(other.packageName)) {
                return false;
            }
        }
        if (directory == null) {
            if (other.directory!= null) {
                return false;
            }
        } else {
            if (!directory.equals(other.directory)) {
                return false;
            }
        }
        if (encoding == null) {
            if (other.encoding!= null) {
                return false;
            }
        } else {
            if (!encoding.equals(other.encoding)) {
                return false;
            }
        }
        if (clean == null) {
            if (other.clean!= null) {
                return false;
            }
        } else {
            if (!clean.equals(other.clean)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((packageName == null)? 0 :packageName.hashCode()));
        result = ((prime*result)+((directory == null)? 0 :directory.hashCode()));
        result = ((prime*result)+((encoding == null)? 0 :encoding.hashCode()));
        result = ((prime*result)+((clean == null)? 0 :clean.hashCode()));
        return result;
    }

}
