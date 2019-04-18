
package org.jooq.conf;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * All sorts of formatting flags / settings.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RenderFormatting", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class RenderFormatting
    extends SettingsBase
    implements Serializable, Cloneable
{

    private final static long serialVersionUID = 31200L;
    @XmlElement(defaultValue = "\n")
    protected String newline = "\n";
    @XmlElement(defaultValue = "  ")
    protected String indentation = "  ";
    @XmlElement(defaultValue = "80")
    protected Integer printMargin = 80;

    /**
     * The character to be used for line breaks.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNewline() {
        return newline;
    }

    /**
     * Sets the value of the newline property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNewline(String value) {
        this.newline = value;
    }

    /**
     * The characters to be used for indentation.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIndentation() {
        return indentation;
    }

    /**
     * Sets the value of the indentation property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIndentation(String value) {
        this.indentation = value;
    }

    /**
     * The print margin after which (some) formatted elements will break lines.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getPrintMargin() {
        return printMargin;
    }

    /**
     * Sets the value of the printMargin property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setPrintMargin(Integer value) {
        this.printMargin = value;
    }

    public RenderFormatting withNewline(String value) {
        setNewline(value);
        return this;
    }

    public RenderFormatting withIndentation(String value) {
        setIndentation(value);
        return this;
    }

    public RenderFormatting withPrintMargin(Integer value) {
        setPrintMargin(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ((newline!= null)&&(!"".equals(newline))) {
            sb.append("<newline>");
            sb.append(newline);
            sb.append("</newline>");
        }
        if ((indentation!= null)&&(!"".equals(indentation))) {
            sb.append("<indentation>");
            sb.append(indentation);
            sb.append("</indentation>");
        }
        if (printMargin!= null) {
            sb.append("<printMargin>");
            sb.append(printMargin);
            sb.append("</printMargin>");
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
        RenderFormatting other = ((RenderFormatting) that);
        if (newline == null) {
            if (other.newline!= null) {
                return false;
            }
        } else {
            if (!newline.equals(other.newline)) {
                return false;
            }
        }
        if (indentation == null) {
            if (other.indentation!= null) {
                return false;
            }
        } else {
            if (!indentation.equals(other.indentation)) {
                return false;
            }
        }
        if (printMargin == null) {
            if (other.printMargin!= null) {
                return false;
            }
        } else {
            if (!printMargin.equals(other.printMargin)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((newline == null)? 0 :newline.hashCode()));
        result = ((prime*result)+((indentation == null)? 0 :indentation.hashCode()));
        result = ((prime*result)+((printMargin == null)? 0 :printMargin.hashCode()));
        return result;
    }

}
