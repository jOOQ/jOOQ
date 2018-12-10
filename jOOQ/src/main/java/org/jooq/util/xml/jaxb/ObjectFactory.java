







package org.jooq.util.xml.jaxb;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.jooq.util.xml.jaxb package.
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jooq.util.xml.jaxb
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InformationSchema }
     *
     */
    public InformationSchema createInformationSchema() {
        return new InformationSchema();
    }

    /**
     * Create an instance of {@link Catalog }
     *
     */
    public Catalog createCatalog() {
        return new Catalog();
    }

    /**
     * Create an instance of {@link Schema }
     *
     */
    public Schema createSchema() {
        return new Schema();
    }

    /**
     * Create an instance of {@link Sequence }
     *
     */
    public Sequence createSequence() {
        return new Sequence();
    }

    /**
     * Create an instance of {@link Table }
     *
     */
    public Table createTable() {
        return new Table();
    }

    /**
     * Create an instance of {@link Column }
     *
     */
    public Column createColumn() {
        return new Column();
    }

    /**
     * Create an instance of {@link TableConstraint }
     *
     */
    public TableConstraint createTableConstraint() {
        return new TableConstraint();
    }

    /**
     * Create an instance of {@link KeyColumnUsage }
     *
     */
    public KeyColumnUsage createKeyColumnUsage() {
        return new KeyColumnUsage();
    }

    /**
     * Create an instance of {@link ReferentialConstraint }
     *
     */
    public ReferentialConstraint createReferentialConstraint() {
        return new ReferentialConstraint();
    }

    /**
     * Create an instance of {@link Index }
     *
     */
    public Index createIndex() {
        return new Index();
    }

    /**
     * Create an instance of {@link IndexColumnUsage }
     *
     */
    public IndexColumnUsage createIndexColumnUsage() {
        return new IndexColumnUsage();
    }

    /**
     * Create an instance of {@link Routine }
     *
     */
    public Routine createRoutine() {
        return new Routine();
    }

    /**
     * Create an instance of {@link Parameter }
     *
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link ElementType }
     *
     */
    public ElementType createElementType() {
        return new ElementType();
    }

}
