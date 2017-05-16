







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java class for Target complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Target"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="packageName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="directory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="encoding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Target", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Target implements Serializable
{

    private final static long serialVersionUID = 31000L;
    @XmlElement(defaultValue = "org.jooq.generated")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String packageName = "org.jooq.generated";
    @XmlElement(defaultValue = "target/generated-sources/jooq")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String directory = "target/generated-sources/jooq";
    @XmlElement(defaultValue = "UTF-8")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String encoding = "UTF-8";

    /**
     * Gets the value of the packageName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sets the value of the packageName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPackageName(String value) {
        this.packageName = value;
    }

    /**
     * Gets the value of the directory property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets the value of the directory property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDirectory(String value) {
        this.directory = value;
    }

    /**
     * Gets the value of the encoding property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the value of the encoding property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEncoding(String value) {
        this.encoding = value;
    }

    public Target withPackageName(String value) {
        setPackageName(value);
        return this;
    }

    public Target withDirectory(String value) {
        setDirectory(value);
        return this;
    }

    public Target withEncoding(String value) {
        setEncoding(value);
        return this;
    }

}
