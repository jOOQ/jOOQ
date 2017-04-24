







package org.jooq.util.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;


/**
 * <p>Java-Klasse für Target complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der packageName-Eigenschaft ab.
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
     * Legt den Wert der packageName-Eigenschaft fest.
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
     * Ruft den Wert der directory-Eigenschaft ab.
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
     * Legt den Wert der directory-Eigenschaft fest.
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
     * Ruft den Wert der encoding-Eigenschaft ab.
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
     * Legt den Wert der encoding-Eigenschaft fest.
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
