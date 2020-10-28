
package org.jooq.migrations.xml.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jooq.migrations.xml.jaxb package. 
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

    private final static QName _Migrations_QNAME = new QName("http://www.jooq.org/xsd/jooq-migrations-3.15.0.xsd", "migrations");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jooq.migrations.xml.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MigrationsType }
     * 
     */
    public MigrationsType createMigrationsType() {
        return new MigrationsType();
    }

    /**
     * Create an instance of {@link CommitType }
     * 
     */
    public CommitType createCommitType() {
        return new CommitType();
    }

    /**
     * Create an instance of {@link ParentType }
     * 
     */
    public ParentType createParentType() {
        return new ParentType();
    }

    /**
     * Create an instance of {@link FileType }
     * 
     */
    public FileType createFileType() {
        return new FileType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MigrationsType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link MigrationsType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.jooq.org/xsd/jooq-migrations-3.15.0.xsd", name = "migrations")
    public JAXBElement<MigrationsType> createMigrations(MigrationsType value) {
        return new JAXBElement<MigrationsType>(_Migrations_QNAME, MigrationsType.class, null, value);
    }

}
