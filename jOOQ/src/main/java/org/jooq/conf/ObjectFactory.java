







package org.jooq.conf;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.jooq.conf package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Settings_QNAME = new QName("http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd", "settings");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jooq.conf
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Settings }
     *
     */
    public Settings createSettings() {
        return new Settings();
    }

    /**
     * Create an instance of {@link RenderMapping }
     *
     */
    public RenderMapping createRenderMapping() {
        return new RenderMapping();
    }

    /**
     * Create an instance of {@link MappedSchema }
     *
     */
    public MappedSchema createMappedSchema() {
        return new MappedSchema();
    }

    /**
     * Create an instance of {@link MappedTable }
     *
     */
    public MappedTable createMappedTable() {
        return new MappedTable();
    }

    /**
     * Create an instance of {@link RenderFormatting }
     *
     */
    public RenderFormatting createRenderFormatting() {
        return new RenderFormatting();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Settings }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd", name = "settings")
    public JAXBElement<Settings> createSettings(Settings value) {
        return new JAXBElement<Settings>(_Settings_QNAME, Settings.class, null, value);
    }

}
