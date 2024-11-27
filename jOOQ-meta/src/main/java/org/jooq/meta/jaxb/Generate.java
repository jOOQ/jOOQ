
package org.jooq.meta.jaxb;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.util.jaxb.tools.StringAdapter;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


/**
 * Options strictly related to generated code.
 * 
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Generate", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Generate implements Serializable, XMLAppendable
{

    private final static long serialVersionUID = 32000L;
    @XmlElement(defaultValue = "true")
    protected Boolean indexes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean relations = true;
    @XmlElement(defaultValue = "true")
    protected Boolean sequenceFlags = true;
    @XmlElement(defaultValue = "true")
    protected Boolean udtPaths = true;
    @XmlElement(defaultValue = "true")
    protected Boolean implicitJoinPathsToOne = true;
    @XmlElement(defaultValue = "true")
    protected Boolean implicitJoinPathsToMany = true;
    @XmlElement(defaultValue = "true")
    protected Boolean implicitJoinPathsManyToMany = true;
    @XmlElement(defaultValue = "true")
    protected Boolean implicitJoinPathTableSubtypes = true;
    @XmlElement(defaultValue = "false")
    protected Boolean implicitJoinPathUnusedConstructors = false;
    @XmlElement(defaultValue = "true")
    protected Boolean implicitJoinPathsUseTableNameForUnambiguousFKs = true;
    @XmlElement(defaultValue = "true")
    protected Boolean implicitJoinPathsAsKotlinProperties = true;
    @XmlElement(defaultValue = "true")
    protected Boolean deprecated = true;
    @XmlElement(defaultValue = "true")
    protected Boolean deprecationOnUnknownTypes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean instanceFields = true;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected VisibilityModifier visibilityModifier = VisibilityModifier.DEFAULT;
    @XmlElement(defaultValue = "false")
    protected Boolean generatedAnnotation = false;
    @XmlElement(defaultValue = "DETECT_FROM_JDK")
    @XmlSchemaType(name = "string")
    protected GeneratedAnnotationType generatedAnnotationType = GeneratedAnnotationType.DETECT_FROM_JDK;
    @XmlElement(defaultValue = "false")
    protected Boolean generatedAnnotationDate = false;
    @XmlElement(defaultValue = "true")
    protected Boolean generatedAnnotationJooqVersion = true;
    @XmlElement(defaultValue = "false")
    protected Boolean nonnullAnnotation = false;
    @XmlElement(defaultValue = "javax.annotation.Nonnull")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String nonnullAnnotationType = "javax.annotation.Nonnull";
    @XmlElement(defaultValue = "false")
    protected Boolean nullableAnnotation = false;
    @XmlElement(defaultValue = "false")
    protected Boolean nullableAnnotationOnWriteOnlyNullableTypes = false;
    @XmlElement(defaultValue = "javax.annotation.Nullable")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String nullableAnnotationType = "javax.annotation.Nullable";
    @XmlElement(defaultValue = "false")
    protected Boolean constructorPropertiesAnnotation = false;
    protected Boolean constructorPropertiesAnnotationOnPojos;
    protected Boolean constructorPropertiesAnnotationOnRecords;
    @XmlElement(defaultValue = "true")
    protected Boolean routines = true;
    @XmlElement(defaultValue = "true")
    protected Boolean sequences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean triggers = true;
    @XmlElement(defaultValue = "true")
    protected Boolean synonyms = true;
    @XmlElement(defaultValue = "true")
    protected Boolean udts = true;
    @XmlElement(defaultValue = "true")
    protected Boolean queues = true;
    @XmlElement(defaultValue = "true")
    protected Boolean links = true;
    @XmlElement(defaultValue = "true")
    protected Boolean keys = true;
    @XmlElement(defaultValue = "true")
    protected Boolean tables = true;
    @XmlElement(defaultValue = "true")
    protected Boolean embeddables = true;
    @XmlElement(defaultValue = "true")
    protected Boolean records = true;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordsIncludes;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String recordsExcludes;
    @XmlElement(defaultValue = "false")
    protected Boolean recordsImplementingRecordN = false;
    @XmlElement(defaultValue = "false")
    protected Boolean enumsAsScalaSealedTraits = false;
    @XmlElement(defaultValue = "true")
    protected Boolean enumsAsScalaEnums = true;
    @XmlElement(defaultValue = "false")
    protected Boolean pojos = false;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojosIncludes;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojosExcludes;
    @XmlElement(defaultValue = "true")
    protected Boolean pojosEqualsAndHashCode = true;
    @XmlElement(defaultValue = "true")
    protected Boolean pojosEqualsAndHashCodeIncludePrimaryKeyOnly = true;
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojosEqualsAndHashCodeColumnIncludeExpression = "";
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String pojosEqualsAndHashCodeColumnExcludeExpression = "";
    @XmlElement(defaultValue = "true")
    protected Boolean pojosToString = true;
    @XmlElement(defaultValue = "false")
    protected Boolean pojosAsJavaRecordClasses = false;
    @XmlElement(defaultValue = "true")
    protected Boolean pojosAsScalaCaseClasses = true;
    @XmlElement(defaultValue = "true")
    protected Boolean pojosAsKotlinDataClasses = true;
    @XmlElement(defaultValue = "false")
    protected Boolean immutablePojos = false;
    @XmlElement(defaultValue = "true")
    protected Boolean serializablePojos = true;
    @XmlElement(defaultValue = "false")
    protected Boolean interfaces = false;
    @XmlElement(defaultValue = "false")
    protected Boolean immutableInterfaces = false;
    @XmlElement(defaultValue = "true")
    protected Boolean serializableInterfaces = true;
    @XmlElement(defaultValue = "false")
    protected Boolean daos = false;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String daosIncludes;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String daosExcludes;
    @XmlElement(defaultValue = "true")
    protected Boolean jooqVersionReference = true;
    @XmlElement(defaultValue = "false")
    protected Boolean jpaAnnotations = false;
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String jpaVersion;
    @XmlElement(defaultValue = "false")
    protected Boolean validationAnnotations = false;
    @XmlElement(defaultValue = "false")
    protected Boolean springAnnotations = false;
    @XmlElement(defaultValue = "false")
    protected Boolean springDao = false;
    @XmlElement(defaultValue = "true")
    protected Boolean kotlinSetterJvmNameAnnotationsOnIsPrefix = true;
    @XmlElement(defaultValue = "false")
    protected Boolean kotlinNotNullPojoAttributes = false;
    @XmlElement(defaultValue = "false")
    protected Boolean kotlinNotNullRecordAttributes = false;
    @XmlElement(defaultValue = "false")
    protected Boolean kotlinNotNullInterfaceAttributes = false;
    @XmlElement(defaultValue = "true")
    protected Boolean kotlinDefaultedNullablePojoAttributes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean kotlinDefaultedNullableRecordAttributes = true;
    @XmlElement(defaultValue = "false")
    protected Boolean globalObjectNames = false;
    @XmlElement(defaultValue = "true")
    protected Boolean globalObjectReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalCatalogReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalSchemaReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalDomainReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalTriggerReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalSynonymReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalTableReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalSequenceReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalUDTReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalRoutineReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalQueueReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalLinkReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalKeyReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean globalIndexReferences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean defaultCatalog = true;
    @XmlElement(defaultValue = "true")
    protected Boolean defaultSchema = true;
    @XmlElement(defaultValue = "true")
    protected Boolean javadoc = true;
    @XmlElement(defaultValue = "true")
    protected Boolean comments = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnCatalogs = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnSchemas = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnTables = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnColumns = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnEmbeddables = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnUDTs = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnAttributes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnPackages = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnRoutines = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnParameters = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnSequences = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnDomains = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnLinks = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnQueues = true;
    @XmlElement(defaultValue = "true")
    protected Boolean commentsOnKeys = true;
    @XmlElement(defaultValue = "true")
    protected Boolean sources = true;
    @XmlElement(defaultValue = "true")
    protected Boolean sourcesOnViews = true;
    @XmlElement(defaultValue = "false")
    protected Boolean fluentSetters = false;
    @XmlElement(defaultValue = "false")
    protected Boolean javaBeansGettersAndSetters = false;
    @XmlElement(defaultValue = "false")
    protected Boolean varargSetters = false;
    @XmlElement(defaultValue = "CONSTANT")
    @XmlSchemaType(name = "string")
    protected GeneratedSerialVersionUID generatedSerialVersionUID = GeneratedSerialVersionUID.CONSTANT;
    @XmlElement(defaultValue = "500")
    protected Integer maxMembersPerInitialiser = 500;
    @XmlElement(defaultValue = "")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String fullyQualifiedTypes = "";
    @XmlElement(defaultValue = "false")
    protected Boolean emptyCatalogs = false;
    @XmlElement(defaultValue = "false")
    protected Boolean emptySchemas = false;
    @XmlElement(defaultValue = "true")
    protected Boolean javaTimeTypes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean spatialTypes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean xmlTypes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean jsonTypes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean intervalTypes = true;
    @XmlElement(defaultValue = "true")
    protected Boolean decfloatTypes = true;
    @XmlElement(defaultValue = "\\n")
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String newline = "\\n";
    @XmlJavaTypeAdapter(StringAdapter.class)
    protected String indentation;
    @XmlElement(defaultValue = "80")
    protected Integer printMarginForBlockComment = 80;
    @XmlElement(defaultValue = "DETECT_FROM_JDK")
    @XmlSchemaType(name = "string")
    protected GeneratedTextBlocks textBlocks = GeneratedTextBlocks.DETECT_FROM_JDK;
    @XmlElement(defaultValue = "true")
    protected Boolean whereMethodOverrides = true;
    @XmlElement(defaultValue = "true")
    protected Boolean renameMethodOverrides = true;
    @XmlElement(defaultValue = "true")
    protected Boolean asMethodOverrides = true;
    @XmlElement(defaultValue = "false")
    protected Boolean hiddenColumnsInRecords = false;
    @XmlElement(defaultValue = "false")
    protected Boolean hiddenColumnsInPojos = false;
    @XmlElement(defaultValue = "false")
    protected Boolean hiddenColumnsInInterfaces = false;

    /**
     * Generate index information.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIndexes() {
        return indexes;
    }

    /**
     * Generate index information.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIndexes(Boolean value) {
        this.indexes = value;
    }

    /**
     * Primary key / foreign key relations should be generated and used.
     * This is a prerequisite for various advanced features
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRelations() {
        return relations;
    }

    /**
     * Primary key / foreign key relations should be generated and used.
     * This is a prerequisite for various advanced features
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRelations(Boolean value) {
        this.relations = value;
    }

    /**
     * Sequence flags should be generated and used.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSequenceFlags() {
        return sequenceFlags;
    }

    /**
     * Sequence flags should be generated and used.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSequenceFlags(Boolean value) {
        this.sequenceFlags = value;
    }

    /**
     * Generate UDT path expressions on tables and on UDTs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUdtPaths() {
        return udtPaths;
    }

    /**
     * Generate UDT path expressions on tables and on UDTs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUdtPaths(Boolean value) {
        this.udtPaths = value;
    }

    /**
     * Generate implicit join path constructors on generated tables for outgoing foreign key relationships (to-one relationships)
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isImplicitJoinPathsToOne() {
        return implicitJoinPathsToOne;
    }

    /**
     * Generate implicit join path constructors on generated tables for outgoing foreign key relationships (to-one relationships)
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setImplicitJoinPathsToOne(Boolean value) {
        this.implicitJoinPathsToOne = value;
    }

    /**
     * Generate implicit join path constructors on generated tables for incoming foreign key relationships (to-many relationships)
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isImplicitJoinPathsToMany() {
        return implicitJoinPathsToMany;
    }

    /**
     * Generate implicit join path constructors on generated tables for incoming foreign key relationships (to-many relationships)
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setImplicitJoinPathsToMany(Boolean value) {
        this.implicitJoinPathsToMany = value;
    }

    /**
     * Generate implicit join path constructors on generated tables for many-to-many relationships. This turns off implicitly, if either of the other path generations are turned off.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isImplicitJoinPathsManyToMany() {
        return implicitJoinPathsManyToMany;
    }

    /**
     * Generate implicit join path constructors on generated tables for many-to-many relationships. This turns off implicitly, if either of the other path generations are turned off.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setImplicitJoinPathsManyToMany(Boolean value) {
        this.implicitJoinPathsManyToMany = value;
    }

    /**
     * Generate implicit join path table subtypes implementing {@link org.jooq.Path} for increased JOIN convenience.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isImplicitJoinPathTableSubtypes() {
        return implicitJoinPathTableSubtypes;
    }

    /**
     * Generate implicit join path table subtypes implementing {@link org.jooq.Path} for increased JOIN convenience.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setImplicitJoinPathTableSubtypes(Boolean value) {
        this.implicitJoinPathTableSubtypes = value;
    }

    /**
     * Generate implicit join path constructors also if there isn't any outgoing or incoming foreign key relationship.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isImplicitJoinPathUnusedConstructors() {
        return implicitJoinPathUnusedConstructors;
    }

    /**
     * Generate implicit join path constructors also if there isn't any outgoing or incoming foreign key relationship.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setImplicitJoinPathUnusedConstructors(Boolean value) {
        this.implicitJoinPathUnusedConstructors = value;
    }

    /**
     * Whether names of unambiguous {@link org.jooq.meta.ForeignKeyDefinition} should be based
     * on the referenced {@link org.jooq.meta.TableDefinition}.
     * <p>
     * When a child table has only one {@link org.jooq.meta.ForeignKeyDefinition} towards a
     * parent table, then that path is "unambiguous." In that case, some
     * {@link GeneratorStrategy} implementations may choose to use the parent
     * table's {@link org.jooq.meta.TableDefinition} for implementations of
     * {@link org.jooq.codegen.GeneratorStrategy#getJavaMethodName(Definition)}, instead of the
     * {@link org.jooq.meta.ForeignKeyDefinition}, e.g. for implicit join paths.
     * <p>
     * This flag allows for turning off this default behaviour.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isImplicitJoinPathsUseTableNameForUnambiguousFKs() {
        return implicitJoinPathsUseTableNameForUnambiguousFKs;
    }

    /**
     * Whether names of unambiguous {@link org.jooq.meta.ForeignKeyDefinition} should be based
     * on the referenced {@link org.jooq.meta.TableDefinition}.
     * <p>
     * When a child table has only one {@link org.jooq.meta.ForeignKeyDefinition} towards a
     * parent table, then that path is "unambiguous." In that case, some
     * {@link GeneratorStrategy} implementations may choose to use the parent
     * table's {@link org.jooq.meta.TableDefinition} for implementations of
     * {@link org.jooq.codegen.GeneratorStrategy#getJavaMethodName(Definition)}, instead of the
     * {@link org.jooq.meta.ForeignKeyDefinition}, e.g. for implicit join paths.
     * <p>
     * This flag allows for turning off this default behaviour.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setImplicitJoinPathsUseTableNameForUnambiguousFKs(Boolean value) {
        this.implicitJoinPathsUseTableNameForUnambiguousFKs = value;
    }

    /**
     * Whether implicit join path constructors should be offered as properties in Kotlin.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isImplicitJoinPathsAsKotlinProperties() {
        return implicitJoinPathsAsKotlinProperties;
    }

    /**
     * Whether implicit join path constructors should be offered as properties in Kotlin.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setImplicitJoinPathsAsKotlinProperties(Boolean value) {
        this.implicitJoinPathsAsKotlinProperties = value;
    }

    /**
     * Generate deprecated code for backwards compatibility
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeprecated() {
        return deprecated;
    }

    /**
     * Generate deprecated code for backwards compatibility
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeprecated(Boolean value) {
        this.deprecated = value;
    }

    /**
     * Generate deprecation annotations on references to unknown data types.
     * This helps to identify columns, attributes, and parameters, which may not be usable through
     * jOOQ API, without adding custom data type bindings to them.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDeprecationOnUnknownTypes() {
        return deprecationOnUnknownTypes;
    }

    /**
     * Generate deprecation annotations on references to unknown data types.
     * This helps to identify columns, attributes, and parameters, which may not be usable through
     * jOOQ API, without adding custom data type bindings to them.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeprecationOnUnknownTypes(Boolean value) {
        this.deprecationOnUnknownTypes = value;
    }

    /**
     * @deprecated
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isInstanceFields() {
        return instanceFields;
    }

    /**
     * @deprecated
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setInstanceFields(Boolean value) {
        this.instanceFields = value;
    }

    /**
     * The visibility modifier to be used with generated code.
     * 
     */
    public VisibilityModifier getVisibilityModifier() {
        return visibilityModifier;
    }

    /**
     * The visibility modifier to be used with generated code.
     * 
     */
    public void setVisibilityModifier(VisibilityModifier value) {
        this.visibilityModifier = value;
    }

    /**
     * Use the {@link org.jooq.Generated} annotation specified by {@link #getGeneratedAnnotationType()} in generated code.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGeneratedAnnotation() {
        return generatedAnnotation;
    }

    /**
     * Use the {@link org.jooq.Generated} annotation specified by {@link #getGeneratedAnnotationType()} in generated code.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGeneratedAnnotation(Boolean value) {
        this.generatedAnnotation = value;
    }

    /**
     * Specify the type of {@link org.jooq.Generated} annotation to use in generated code.
     * 
     */
    public GeneratedAnnotationType getGeneratedAnnotationType() {
        return generatedAnnotationType;
    }

    /**
     * Specify the type of {@link org.jooq.Generated} annotation to use in generated code.
     * 
     */
    public void setGeneratedAnnotationType(GeneratedAnnotationType value) {
        this.generatedAnnotationType = value;
    }

    /**
     * Whether the {@link org.jooq.Generated} annotation specified by {@link #getGeneratedAnnotationType()} should include the <code>date</code> attribute.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGeneratedAnnotationDate() {
        return generatedAnnotationDate;
    }

    /**
     * Whether the {@link org.jooq.Generated} annotation specified by {@link #getGeneratedAnnotationType()} should include the <code>date</code> attribute.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGeneratedAnnotationDate(Boolean value) {
        this.generatedAnnotationDate = value;
    }

    /**
     * Whether the {@link org.jooq.Generated} annotation specified by {@link #getGeneratedAnnotationType()} should include the jOOQ version.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGeneratedAnnotationJooqVersion() {
        return generatedAnnotationJooqVersion;
    }

    /**
     * Whether the {@link org.jooq.Generated} annotation specified by {@link #getGeneratedAnnotationType()} should include the jOOQ version.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGeneratedAnnotationJooqVersion(Boolean value) {
        this.generatedAnnotationJooqVersion = value;
    }

    /**
     * Whether non-nullable items should be annotated with the annotation type specified in {@link #nonnullAnnotationType}. In SQL and by consequence in jOOQ, non-nullability cannot be guaranteed statically. There may still be some cases (e.g. after unions, outer joins, etc.) where a normally non-null value turns out to be null!
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNonnullAnnotation() {
        return nonnullAnnotation;
    }

    /**
     * Whether non-nullable items should be annotated with the annotation type specified in {@link #nonnullAnnotationType}. In SQL and by consequence in jOOQ, non-nullability cannot be guaranteed statically. There may still be some cases (e.g. after unions, outer joins, etc.) where a normally non-null value turns out to be null!
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNonnullAnnotation(Boolean value) {
        this.nonnullAnnotation = value;
    }

    /**
     * Specify the qualified annotation name for all non-nullable items in generated code, defaulting to the JSR-305 {@link javax.annotation.Nonnull} type.
     * 
     */
    public String getNonnullAnnotationType() {
        return nonnullAnnotationType;
    }

    /**
     * Specify the qualified annotation name for all non-nullable items in generated code, defaulting to the JSR-305 {@link javax.annotation.Nonnull} type.
     * 
     */
    public void setNonnullAnnotationType(String value) {
        this.nonnullAnnotationType = value;
    }

    /**
     * Whether nullable items should be annotated with the annotation type specified in {@link #nullableAnnotationType}. Unlike {@link #nonnullAnnotation}, nullability can be guaranteed as in SQL, and by consequence in jOOQ, every column expression can be made nullable using some SQL operation.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNullableAnnotation() {
        return nullableAnnotation;
    }

    /**
     * Whether nullable items should be annotated with the annotation type specified in {@link #nullableAnnotationType}. Unlike {@link #nonnullAnnotation}, nullability can be guaranteed as in SQL, and by consequence in jOOQ, every column expression can be made nullable using some SQL operation.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNullableAnnotation(Boolean value) {
        this.nullableAnnotation = value;
    }

    /**
     * Whether write-only (e.g. defaulted, non-null) nullable items should be annotated with the annotation type specified in {@link #nullableAnnotationType}. Unlike {@link #nonnullAnnotation}, nullability can be guaranteed as in SQL, and by consequence in jOOQ, every column expression can be made nullable using some SQL operation.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNullableAnnotationOnWriteOnlyNullableTypes() {
        return nullableAnnotationOnWriteOnlyNullableTypes;
    }

    /**
     * Whether write-only (e.g. defaulted, non-null) nullable items should be annotated with the annotation type specified in {@link #nullableAnnotationType}. Unlike {@link #nonnullAnnotation}, nullability can be guaranteed as in SQL, and by consequence in jOOQ, every column expression can be made nullable using some SQL operation.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNullableAnnotationOnWriteOnlyNullableTypes(Boolean value) {
        this.nullableAnnotationOnWriteOnlyNullableTypes = value;
    }

    /**
     * Specify the qualified annotation name for all nullable items in generated code, defaulting to the JSR-305 {@link javax.annotation.Nullable} type.
     * 
     */
    public String getNullableAnnotationType() {
        return nullableAnnotationType;
    }

    /**
     * Specify the qualified annotation name for all nullable items in generated code, defaulting to the JSR-305 {@link javax.annotation.Nullable} type.
     * 
     */
    public void setNullableAnnotationType(String value) {
        this.nullableAnnotationType = value;
    }

    /**
     * Generate the {@link java.beans.ConstructorProperties} annotation on generated POJOs and/or records.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isConstructorPropertiesAnnotation() {
        return constructorPropertiesAnnotation;
    }

    /**
     * Generate the {@link java.beans.ConstructorProperties} annotation on generated POJOs and/or records.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setConstructorPropertiesAnnotation(Boolean value) {
        this.constructorPropertiesAnnotation = value;
    }

    /**
     * Generate the {@link java.beans.ConstructorProperties} annotation on generated POJOs (defaults to <code>true</code> if {@link #constructorPropertiesAnnotation} is active).
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isConstructorPropertiesAnnotationOnPojos() {
        return constructorPropertiesAnnotationOnPojos;
    }

    /**
     * Generate the {@link java.beans.ConstructorProperties} annotation on generated POJOs (defaults to <code>true</code> if {@link #constructorPropertiesAnnotation} is active).
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setConstructorPropertiesAnnotationOnPojos(Boolean value) {
        this.constructorPropertiesAnnotationOnPojos = value;
    }

    /**
     * Generate the {@link java.beans.ConstructorProperties} annotation on generated records (defaults to <code>true</code> if {@link #constructorPropertiesAnnotation} is active).
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isConstructorPropertiesAnnotationOnRecords() {
        return constructorPropertiesAnnotationOnRecords;
    }

    /**
     * Generate the {@link java.beans.ConstructorProperties} annotation on generated records (defaults to <code>true</code> if {@link #constructorPropertiesAnnotation} is active).
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setConstructorPropertiesAnnotationOnRecords(Boolean value) {
        this.constructorPropertiesAnnotationOnRecords = value;
    }

    /**
     * Generate Routine classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRoutines() {
        return routines;
    }

    /**
     * Generate Routine classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRoutines(Boolean value) {
        this.routines = value;
    }

    /**
     * Generate Sequence classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSequences() {
        return sequences;
    }

    /**
     * Generate Sequence classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSequences(Boolean value) {
        this.sequences = value;
    }

    /**
     * Generate Trigger classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTriggers() {
        return triggers;
    }

    /**
     * Generate Trigger classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTriggers(Boolean value) {
        this.triggers = value;
    }

    /**
     * Generate Synonym classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSynonyms() {
        return synonyms;
    }

    /**
     * Generate Synonym classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSynonyms(Boolean value) {
        this.synonyms = value;
    }

    /**
     * Generate UDT classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUdts() {
        return udts;
    }

    /**
     * Generate UDT classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUdts(Boolean value) {
        this.udts = value;
    }

    /**
     * Generate Queue classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isQueues() {
        return queues;
    }

    /**
     * Generate Queue classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setQueues(Boolean value) {
        this.queues = value;
    }

    /**
     * Generate database Link classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLinks() {
        return links;
    }

    /**
     * Generate database Link classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLinks(Boolean value) {
        this.links = value;
    }

    /**
     * Generate Key classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKeys() {
        return keys;
    }

    /**
     * Generate Key classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKeys(Boolean value) {
        this.keys = value;
    }

    /**
     * Generate Table classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTables() {
        return tables;
    }

    /**
     * Generate Table classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTables(Boolean value) {
        this.tables = value;
    }

    /**
     * Generate embeddable classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEmbeddables() {
        return embeddables;
    }

    /**
     * Generate embeddable classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEmbeddables(Boolean value) {
        this.embeddables = value;
    }

    /**
     * Generate TableRecord classes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRecords() {
        return records;
    }

    /**
     * Generate TableRecord classes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRecords(Boolean value) {
        this.records = value;
    }

    /**
     * All the object identifiers for which to generate records, by default, all of them.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public String getRecordsIncludes() {
        return recordsIncludes;
    }

    /**
     * All the object identifiers for which to generate records, by default, all of them.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public void setRecordsIncludes(String value) {
        this.recordsIncludes = value;
    }

    /**
     * All the object identifiers for which not to generate records.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public String getRecordsExcludes() {
        return recordsExcludes;
    }

    /**
     * All the object identifiers for which not to generate records.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public void setRecordsExcludes(String value) {
        this.recordsExcludes = value;
    }

    /**
     * Generate TableRecord classes that implement Record[N] super types
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRecordsImplementingRecordN() {
        return recordsImplementingRecordN;
    }

    /**
     * Generate TableRecord classes that implement Record[N] super types
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRecordsImplementingRecordN(Boolean value) {
        this.recordsImplementingRecordN = value;
    }

    /**
     * @deprecated Activate the legacy Scala sealed trait enum emulation
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public Boolean isEnumsAsScalaSealedTraits() {
        return enumsAsScalaSealedTraits;
    }

    /**
     * @deprecated Activate the legacy Scala sealed trait enum emulation
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    @Deprecated
    public void setEnumsAsScalaSealedTraits(Boolean value) {
        this.enumsAsScalaSealedTraits = value;
    }

    /**
     * Generate enums as Scala 3.0 enums (if Scala 3 is supported).
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEnumsAsScalaEnums() {
        return enumsAsScalaEnums;
    }

    /**
     * Generate enums as Scala 3.0 enums (if Scala 3 is supported).
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnumsAsScalaEnums(Boolean value) {
        this.enumsAsScalaEnums = value;
    }

    /**
     * Generate POJOs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPojos() {
        return pojos;
    }

    /**
     * Generate POJOs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPojos(Boolean value) {
        this.pojos = value;
    }

    /**
     * All the object identifiers for which to generate POJOs, by default, all of them.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public String getPojosIncludes() {
        return pojosIncludes;
    }

    /**
     * All the object identifiers for which to generate POJOs, by default, all of them.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public void setPojosIncludes(String value) {
        this.pojosIncludes = value;
    }

    /**
     * All the object identifiers for which not to generate POJOs.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public String getPojosExcludes() {
        return pojosExcludes;
    }

    /**
     * All the object identifiers for which not to generate POJOs.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public void setPojosExcludes(String value) {
        this.pojosExcludes = value;
    }

    /**
     * Generate basic equals() and hashCode() methods in POJOs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPojosEqualsAndHashCode() {
        return pojosEqualsAndHashCode;
    }

    /**
     * Generate basic equals() and hashCode() methods in POJOs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPojosEqualsAndHashCode(Boolean value) {
        this.pojosEqualsAndHashCode = value;
    }

    /**
     * Include primary key columns only in generated equals() and hashCode() methods in POJOs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPojosEqualsAndHashCodeIncludePrimaryKeyOnly() {
        return pojosEqualsAndHashCodeIncludePrimaryKeyOnly;
    }

    /**
     * Include primary key columns only in generated equals() and hashCode() methods in POJOs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPojosEqualsAndHashCodeIncludePrimaryKeyOnly(Boolean value) {
        this.pojosEqualsAndHashCodeIncludePrimaryKeyOnly = value;
    }

    /**
     * A regular expression matching columns for inclusion in generated equals() and hashCode() methods in POJOs.
     * 
     */
    public String getPojosEqualsAndHashCodeColumnIncludeExpression() {
        return pojosEqualsAndHashCodeColumnIncludeExpression;
    }

    /**
     * A regular expression matching columns for inclusion in generated equals() and hashCode() methods in POJOs.
     * 
     */
    public void setPojosEqualsAndHashCodeColumnIncludeExpression(String value) {
        this.pojosEqualsAndHashCodeColumnIncludeExpression = value;
    }

    /**
     * A regular expression matching columns for exclusion in generated equals() and hashCode() methods in POJOs.
     * 
     */
    public String getPojosEqualsAndHashCodeColumnExcludeExpression() {
        return pojosEqualsAndHashCodeColumnExcludeExpression;
    }

    /**
     * A regular expression matching columns for exclusion in generated equals() and hashCode() methods in POJOs.
     * 
     */
    public void setPojosEqualsAndHashCodeColumnExcludeExpression(String value) {
        this.pojosEqualsAndHashCodeColumnExcludeExpression = value;
    }

    /**
     * Generate basic toString() methods in POJOs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPojosToString() {
        return pojosToString;
    }

    /**
     * Generate basic toString() methods in POJOs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPojosToString(Boolean value) {
        this.pojosToString = value;
    }

    /**
     * Generate POJOs as records, when using the JavaGenerator.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPojosAsJavaRecordClasses() {
        return pojosAsJavaRecordClasses;
    }

    /**
     * Generate POJOs as records, when using the JavaGenerator.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPojosAsJavaRecordClasses(Boolean value) {
        this.pojosAsJavaRecordClasses = value;
    }

    /**
     * Generate POJOs as case classes, when using the ScalaGenerator or Scala3Generator.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPojosAsScalaCaseClasses() {
        return pojosAsScalaCaseClasses;
    }

    /**
     * Generate POJOs as case classes, when using the ScalaGenerator or Scala3Generator.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPojosAsScalaCaseClasses(Boolean value) {
        this.pojosAsScalaCaseClasses = value;
    }

    /**
     * Generate POJOs as data classes, when using the KotlinGenerator.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPojosAsKotlinDataClasses() {
        return pojosAsKotlinDataClasses;
    }

    /**
     * Generate POJOs as data classes, when using the KotlinGenerator.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPojosAsKotlinDataClasses(Boolean value) {
        this.pojosAsKotlinDataClasses = value;
    }

    /**
     * Generate immutable POJOs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isImmutablePojos() {
        return immutablePojos;
    }

    /**
     * Generate immutable POJOs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setImmutablePojos(Boolean value) {
        this.immutablePojos = value;
    }

    /**
     * Generate serializable POJOs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSerializablePojos() {
        return serializablePojos;
    }

    /**
     * Generate serializable POJOs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSerializablePojos(Boolean value) {
        this.serializablePojos = value;
    }

    /**
     * Generated interfaces to be implemented by records and/or POJOs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInterfaces() {
        return interfaces;
    }

    /**
     * Generated interfaces to be implemented by records and/or POJOs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInterfaces(Boolean value) {
        this.interfaces = value;
    }

    /**
     * Generate immutable interfaces.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isImmutableInterfaces() {
        return immutableInterfaces;
    }

    /**
     * Generate immutable interfaces.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setImmutableInterfaces(Boolean value) {
        this.immutableInterfaces = value;
    }

    /**
     * Generate serializable interfaces.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSerializableInterfaces() {
        return serializableInterfaces;
    }

    /**
     * Generate serializable interfaces.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSerializableInterfaces(Boolean value) {
        this.serializableInterfaces = value;
    }

    /**
     * Generate DAOs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDaos() {
        return daos;
    }

    /**
     * Generate DAOs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDaos(Boolean value) {
        this.daos = value;
    }

    /**
     * All the object identifiers for which to generate DAOs, by default, all of them.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public String getDaosIncludes() {
        return daosIncludes;
    }

    /**
     * All the object identifiers for which to generate DAOs, by default, all of them.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public void setDaosIncludes(String value) {
        this.daosIncludes = value;
    }

    /**
     * All the object identifiers for which not to generate DAOs.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public String getDaosExcludes() {
        return daosExcludes;
    }

    /**
     * All the object identifiers for which not to generate DAOs.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public void setDaosExcludes(String value) {
        this.daosExcludes = value;
    }

    /**
     * Generate references to the most up to date minor release in {@link org.jooq.Constants} to produce compilation errors if an outdated runtime library is being used.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJooqVersionReference() {
        return jooqVersionReference;
    }

    /**
     * Generate references to the most up to date minor release in {@link org.jooq.Constants} to produce compilation errors if an outdated runtime library is being used.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJooqVersionReference(Boolean value) {
        this.jooqVersionReference = value;
    }

    /**
     * Annotate POJOs and Records with JPA annotations.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJpaAnnotations() {
        return jpaAnnotations;
    }

    /**
     * Annotate POJOs and Records with JPA annotations.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJpaAnnotations(Boolean value) {
        this.jpaAnnotations = value;
    }

    /**
     * Version of JPA specification is to be used to generate version-specific annotations. If it is omitted, the latest version is used by default.
     * 
     */
    public String getJpaVersion() {
        return jpaVersion;
    }

    /**
     * Version of JPA specification is to be used to generate version-specific annotations. If it is omitted, the latest version is used by default.
     * 
     */
    public void setJpaVersion(String value) {
        this.jpaVersion = value;
    }

    /**
     * Annotate POJOs and Records with JSR-303 validation annotations
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isValidationAnnotations() {
        return validationAnnotations;
    }

    /**
     * Annotate POJOs and Records with JSR-303 validation annotations
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValidationAnnotations(Boolean value) {
        this.validationAnnotations = value;
    }

    /**
     * Annotate DAOs with useful spring annotations such as @Repository or @Autowired.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSpringAnnotations() {
        return springAnnotations;
    }

    /**
     * Annotate DAOs with useful spring annotations such as @Repository or @Autowired.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSpringAnnotations(Boolean value) {
        this.springAnnotations = value;
    }

    /**
     * Generate an AbstractSpringDAOImpl as a base class for other DAO classes, containing @Transactional annotations, etc.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSpringDao() {
        return springDao;
    }

    /**
     * Generate an AbstractSpringDAOImpl as a base class for other DAO classes, containing @Transactional annotations, etc.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSpringDao(Boolean value) {
        this.springDao = value;
    }

    /**
     * Workaround for Kotlin generating <code>setX()</code> setters instead of <code>setIsX()</code> in byte code for mutable properties called <code>isX</code>.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKotlinSetterJvmNameAnnotationsOnIsPrefix() {
        return kotlinSetterJvmNameAnnotationsOnIsPrefix;
    }

    /**
     * Workaround for Kotlin generating <code>setX()</code> setters instead of <code>setIsX()</code> in byte code for mutable properties called <code>isX</code>.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKotlinSetterJvmNameAnnotationsOnIsPrefix(Boolean value) {
        this.kotlinSetterJvmNameAnnotationsOnIsPrefix = value;
    }

    /**
     * Generate non-nullable types on POJO attributes, where column is not null.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKotlinNotNullPojoAttributes() {
        return kotlinNotNullPojoAttributes;
    }

    /**
     * Generate non-nullable types on POJO attributes, where column is not null.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKotlinNotNullPojoAttributes(Boolean value) {
        this.kotlinNotNullPojoAttributes = value;
    }

    /**
     * Generate non-nullable types on Record attributes, where column is not null.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKotlinNotNullRecordAttributes() {
        return kotlinNotNullRecordAttributes;
    }

    /**
     * Generate non-nullable types on Record attributes, where column is not null.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKotlinNotNullRecordAttributes(Boolean value) {
        this.kotlinNotNullRecordAttributes = value;
    }

    /**
     * Generate non-nullable types on interface attributes, where column is not null.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKotlinNotNullInterfaceAttributes() {
        return kotlinNotNullInterfaceAttributes;
    }

    /**
     * Generate non-nullable types on interface attributes, where column is not null.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKotlinNotNullInterfaceAttributes(Boolean value) {
        this.kotlinNotNullInterfaceAttributes = value;
    }

    /**
     * Generate defaulted nullable POJO attributes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKotlinDefaultedNullablePojoAttributes() {
        return kotlinDefaultedNullablePojoAttributes;
    }

    /**
     * Generate defaulted nullable POJO attributes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKotlinDefaultedNullablePojoAttributes(Boolean value) {
        this.kotlinDefaultedNullablePojoAttributes = value;
    }

    /**
     * Generate defaulted nullable Record attributes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKotlinDefaultedNullableRecordAttributes() {
        return kotlinDefaultedNullableRecordAttributes;
    }

    /**
     * Generate defaulted nullable Record attributes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKotlinDefaultedNullableRecordAttributes(Boolean value) {
        this.kotlinDefaultedNullableRecordAttributes = value;
    }

    /**
     * Turn on generation of all global object names.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalObjectNames() {
        return globalObjectNames;
    }

    /**
     * Turn on generation of all global object names.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalObjectNames(Boolean value) {
        this.globalObjectNames = value;
    }

    /**
     * Turn off generation of all global object references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalObjectReferences() {
        return globalObjectReferences;
    }

    /**
     * Turn off generation of all global object references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalObjectReferences(Boolean value) {
        this.globalObjectReferences = value;
    }

    /**
     * Turn off generation of global catalog references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalCatalogReferences() {
        return globalCatalogReferences;
    }

    /**
     * Turn off generation of global catalog references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalCatalogReferences(Boolean value) {
        this.globalCatalogReferences = value;
    }

    /**
     * Turn off generation of global schema references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalSchemaReferences() {
        return globalSchemaReferences;
    }

    /**
     * Turn off generation of global schema references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalSchemaReferences(Boolean value) {
        this.globalSchemaReferences = value;
    }

    /**
     * Turn off generation of global domain references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalDomainReferences() {
        return globalDomainReferences;
    }

    /**
     * Turn off generation of global domain references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalDomainReferences(Boolean value) {
        this.globalDomainReferences = value;
    }

    /**
     * Turn off generation of global trigger references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalTriggerReferences() {
        return globalTriggerReferences;
    }

    /**
     * Turn off generation of global trigger references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalTriggerReferences(Boolean value) {
        this.globalTriggerReferences = value;
    }

    /**
     * Turn off generation of global synonym references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalSynonymReferences() {
        return globalSynonymReferences;
    }

    /**
     * Turn off generation of global synonym references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalSynonymReferences(Boolean value) {
        this.globalSynonymReferences = value;
    }

    /**
     * Turn off generation of global table references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalTableReferences() {
        return globalTableReferences;
    }

    /**
     * Turn off generation of global table references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalTableReferences(Boolean value) {
        this.globalTableReferences = value;
    }

    /**
     * Turn off generation of global sequence references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalSequenceReferences() {
        return globalSequenceReferences;
    }

    /**
     * Turn off generation of global sequence references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalSequenceReferences(Boolean value) {
        this.globalSequenceReferences = value;
    }

    /**
     * Turn off generation of global UDT references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalUDTReferences() {
        return globalUDTReferences;
    }

    /**
     * Turn off generation of global UDT references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalUDTReferences(Boolean value) {
        this.globalUDTReferences = value;
    }

    /**
     * Turn off generation of global routine references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalRoutineReferences() {
        return globalRoutineReferences;
    }

    /**
     * Turn off generation of global routine references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalRoutineReferences(Boolean value) {
        this.globalRoutineReferences = value;
    }

    /**
     * Turn off generation of global queue references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalQueueReferences() {
        return globalQueueReferences;
    }

    /**
     * Turn off generation of global queue references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalQueueReferences(Boolean value) {
        this.globalQueueReferences = value;
    }

    /**
     * Turn off generation of global database link references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalLinkReferences() {
        return globalLinkReferences;
    }

    /**
     * Turn off generation of global database link references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalLinkReferences(Boolean value) {
        this.globalLinkReferences = value;
    }

    /**
     * Turn off generation of global key references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalKeyReferences() {
        return globalKeyReferences;
    }

    /**
     * Turn off generation of global key references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalKeyReferences(Boolean value) {
        this.globalKeyReferences = value;
    }

    /**
     * Turn off generation of global index references.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGlobalIndexReferences() {
        return globalIndexReferences;
    }

    /**
     * Turn off generation of global index references.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGlobalIndexReferences(Boolean value) {
        this.globalIndexReferences = value;
    }

    /**
     * Turn off generation of a <code>DefaultCatalog</code> object, which is generated by default in the absence of catalogs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDefaultCatalog() {
        return defaultCatalog;
    }

    /**
     * Turn off generation of a <code>DefaultCatalog</code> object, which is generated by default in the absence of catalogs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDefaultCatalog(Boolean value) {
        this.defaultCatalog = value;
    }

    /**
     * Turn off generation of a <code>DefaultSchema</code> object, which is generated by default in the absence of schemas.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDefaultSchema() {
        return defaultSchema;
    }

    /**
     * Turn off generation of a <code>DefaultSchema</code> object, which is generated by default in the absence of schemas.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDefaultSchema(Boolean value) {
        this.defaultSchema = value;
    }

    /**
     * Turn off generation of Javadoc on all objects.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJavadoc() {
        return javadoc;
    }

    /**
     * Turn off generation of Javadoc on all objects.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJavadoc(Boolean value) {
        this.javadoc = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all objects.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isComments() {
        return comments;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all objects.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setComments(Boolean value) {
        this.comments = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all catalogs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnCatalogs() {
        return commentsOnCatalogs;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all catalogs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnCatalogs(Boolean value) {
        this.commentsOnCatalogs = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all schemas.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnSchemas() {
        return commentsOnSchemas;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all schemas.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnSchemas(Boolean value) {
        this.commentsOnSchemas = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all tables.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnTables() {
        return commentsOnTables;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all tables.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnTables(Boolean value) {
        this.commentsOnTables = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all columns.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnColumns() {
        return commentsOnColumns;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all columns.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnColumns(Boolean value) {
        this.commentsOnColumns = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all embeddables.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnEmbeddables() {
        return commentsOnEmbeddables;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all embeddables.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnEmbeddables(Boolean value) {
        this.commentsOnEmbeddables = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all UDTs.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnUDTs() {
        return commentsOnUDTs;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all UDTs.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnUDTs(Boolean value) {
        this.commentsOnUDTs = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all attributes.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnAttributes() {
        return commentsOnAttributes;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all attributes.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnAttributes(Boolean value) {
        this.commentsOnAttributes = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all packages.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnPackages() {
        return commentsOnPackages;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all packages.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnPackages(Boolean value) {
        this.commentsOnPackages = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all routines.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnRoutines() {
        return commentsOnRoutines;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all routines.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnRoutines(Boolean value) {
        this.commentsOnRoutines = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all parameters.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnParameters() {
        return commentsOnParameters;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all parameters.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnParameters(Boolean value) {
        this.commentsOnParameters = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all sequences.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnSequences() {
        return commentsOnSequences;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all sequences.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnSequences(Boolean value) {
        this.commentsOnSequences = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all domains.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnDomains() {
        return commentsOnDomains;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all domains.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnDomains(Boolean value) {
        this.commentsOnDomains = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all links.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnLinks() {
        return commentsOnLinks;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all links.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnLinks(Boolean value) {
        this.commentsOnLinks = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all queues.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnQueues() {
        return commentsOnQueues;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all queues.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnQueues(Boolean value) {
        this.commentsOnQueues = value;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all keys.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommentsOnKeys() {
        return commentsOnKeys;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all keys.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommentsOnKeys(Boolean value) {
        this.commentsOnKeys = value;
    }

    /**
     * Turn off generation of all source code on all object types.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSources() {
        return sources;
    }

    /**
     * Turn off generation of all source code on all object types.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSources(Boolean value) {
        this.sources = value;
    }

    /**
     * Turn off generation of all source code on all views.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSourcesOnViews() {
        return sourcesOnViews;
    }

    /**
     * Turn off generation of all source code on all views.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSourcesOnViews(Boolean value) {
        this.sourcesOnViews = value;
    }

    /**
     * Generate fluent setters in records, POJOs, interfaces.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFluentSetters() {
        return fluentSetters;
    }

    /**
     * Generate fluent setters in records, POJOs, interfaces.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFluentSetters(Boolean value) {
        this.fluentSetters = value;
    }

    /**
     * Modify DefaultGeneratorStrategy behaviour to generate getters and setters in JavaBeans style in records, POJOs, interfaces.
     * <p>
     * If this flag is set to false, then:
     * <p>
     * <ul>
     * <li>Column name   : X_INDEX</li>
     * <li>Attribute name: xIndex</li>
     * <li>Getter name   : getXIndex()</li>
     * <li>Setter name   : setXIndex()</li>
     * </ul>
     * <p>
     * If this flag is set to true, then:
     * <ul>
     * <li>Getter name   : getxIndex()</li>
     * <li>Setter name   : setxIndex()</li>
     * </ul>
     * <p>
     * Custom GeneratorStrategy implementations are unaffected
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJavaBeansGettersAndSetters() {
        return javaBeansGettersAndSetters;
    }

    /**
     * Modify DefaultGeneratorStrategy behaviour to generate getters and setters in JavaBeans style in records, POJOs, interfaces.
     * <p>
     * If this flag is set to false, then:
     * <p>
     * <ul>
     * <li>Column name   : X_INDEX</li>
     * <li>Attribute name: xIndex</li>
     * <li>Getter name   : getXIndex()</li>
     * <li>Setter name   : setXIndex()</li>
     * </ul>
     * <p>
     * If this flag is set to true, then:
     * <ul>
     * <li>Getter name   : getxIndex()</li>
     * <li>Setter name   : setxIndex()</li>
     * </ul>
     * <p>
     * Custom GeneratorStrategy implementations are unaffected
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJavaBeansGettersAndSetters(Boolean value) {
        this.javaBeansGettersAndSetters = value;
    }

    /**
     * Generate varargs setters for array types for convenience.
     * <p>
     * This may lead to compilation warnings in current Java versions.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVarargSetters() {
        return varargSetters;
    }

    /**
     * Generate varargs setters for array types for convenience.
     * <p>
     * This may lead to compilation warnings in current Java versions.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVarargSetters(Boolean value) {
        this.varargSetters = value;
    }

    /**
     * The serial version UID to be generated in all files.
     * 
     */
    public GeneratedSerialVersionUID getGeneratedSerialVersionUID() {
        return generatedSerialVersionUID;
    }

    /**
     * The serial version UID to be generated in all files.
     * 
     */
    public void setGeneratedSerialVersionUID(GeneratedSerialVersionUID value) {
        this.generatedSerialVersionUID = value;
    }

    /**
     * The maximum number of members per initialiser, to prevent reaching the 64kb byte code per method limit in generated code.
     * 
     */
    public Integer getMaxMembersPerInitialiser() {
        return maxMembersPerInitialiser;
    }

    /**
     * The maximum number of members per initialiser, to prevent reaching the 64kb byte code per method limit in generated code.
     * 
     */
    public void setMaxMembersPerInitialiser(Integer value) {
        this.maxMembersPerInitialiser = value;
    }

    /**
     * A regular expression matching all the types in generated code that should be fully qualified.
     * <p>
     * This can be useful if you have a database object that generates a String
     * class, and you want to avoid naming clashes with the java.lang package
     * by specifying <code>java\.lang\..*</code>
     * 
     */
    public String getFullyQualifiedTypes() {
        return fullyQualifiedTypes;
    }

    /**
     * A regular expression matching all the types in generated code that should be fully qualified.
     * <p>
     * This can be useful if you have a database object that generates a String
     * class, and you want to avoid naming clashes with the java.lang package
     * by specifying <code>java\.lang\..*</code>
     * 
     */
    public void setFullyQualifiedTypes(String value) {
        this.fullyQualifiedTypes = value;
    }

    /**
     * Whether empty catalogs (e.g. empty because of <excludes/> configurations) should still be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEmptyCatalogs() {
        return emptyCatalogs;
    }

    /**
     * Whether empty catalogs (e.g. empty because of <excludes/> configurations) should still be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEmptyCatalogs(Boolean value) {
        this.emptyCatalogs = value;
    }

    /**
     * Whether empty schemas (e.g. empty because of <excludes/> configurations) should still be generated.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEmptySchemas() {
        return emptySchemas;
    }

    /**
     * Whether empty schemas (e.g. empty because of <excludes/> configurations) should still be generated.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEmptySchemas(Boolean value) {
        this.emptySchemas = value;
    }

    /**
     * A flag indicating whether Java 8's java.time types should be used by the
     * source code generator, rather than JDBC's java.sql types.
     * <p>
     * This flag is ignored in the commercial Java 6 distribution of jOOQ 3.9+ 
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJavaTimeTypes() {
        return javaTimeTypes;
    }

    /**
     * A flag indicating whether Java 8's java.time types should be used by the
     * source code generator, rather than JDBC's java.sql types.
     * <p>
     * This flag is ignored in the commercial Java 6 distribution of jOOQ 3.9+ 
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJavaTimeTypes(Boolean value) {
        this.javaTimeTypes = value;
    }

    /**
     * A flag indicating whether the spatial type support should be enabled.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSpatialTypes() {
        return spatialTypes;
    }

    /**
     * A flag indicating whether the spatial type support should be enabled.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSpatialTypes(Boolean value) {
        this.spatialTypes = value;
    }

    /**
     * A flag indicating whether the XML type support should be enabled.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isXmlTypes() {
        return xmlTypes;
    }

    /**
     * A flag indicating whether the XML type support should be enabled.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setXmlTypes(Boolean value) {
        this.xmlTypes = value;
    }

    /**
     * A flag indicating whether the JSON type support should be enabled.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJsonTypes() {
        return jsonTypes;
    }

    /**
     * A flag indicating whether the JSON type support should be enabled.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJsonTypes(Boolean value) {
        this.jsonTypes = value;
    }

    /**
     * A flag indicating whether the INTERVAL type support should be enabled.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIntervalTypes() {
        return intervalTypes;
    }

    /**
     * A flag indicating whether the INTERVAL type support should be enabled.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIntervalTypes(Boolean value) {
        this.intervalTypes = value;
    }

    /**
     * A flag indicating whether the DECFLOAT type support should be enabled.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDecfloatTypes() {
        return decfloatTypes;
    }

    /**
     * A flag indicating whether the DECFLOAT type support should be enabled.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDecfloatTypes(Boolean value) {
        this.decfloatTypes = value;
    }

    /**
     * The newline characters to be used in generated code. Whitespace characters can be used, e.g. \n, \r\n
     * 
     */
    public String getNewline() {
        return newline;
    }

    /**
     * The newline characters to be used in generated code. Whitespace characters can be used, e.g. \n, \r\n
     * 
     */
    public void setNewline(String value) {
        this.newline = value;
    }

    /**
     * The indentation characters to be used in generated code. If unspecified, an idiomatic default indentation of the language will be used (4 spaces in Java, 2 spaces in Scala). Whitespace characters can be used, e.g. \t
     * 
     */
    public String getIndentation() {
        return indentation;
    }

    /**
     * The indentation characters to be used in generated code. If unspecified, an idiomatic default indentation of the language will be used (4 spaces in Java, 2 spaces in Scala). Whitespace characters can be used, e.g. \t
     * 
     */
    public void setIndentation(String value) {
        this.indentation = value;
    }

    /**
     * The print margin to apply to generated Javadoc and other block comments, for automatic line wrapping. The feature is turned off if the print margin is <code>0</code>.
     * 
     */
    public Integer getPrintMarginForBlockComment() {
        return printMarginForBlockComment;
    }

    /**
     * The print margin to apply to generated Javadoc and other block comments, for automatic line wrapping. The feature is turned off if the print margin is <code>0</code>.
     * 
     */
    public void setPrintMarginForBlockComment(Integer value) {
        this.printMarginForBlockComment = value;
    }

    /**
     * Whether to generate String in text block format.
     * 
     */
    public GeneratedTextBlocks getTextBlocks() {
        return textBlocks;
    }

    /**
     * Whether to generate String in text block format.
     * 
     */
    public void setTextBlocks(GeneratedTextBlocks value) {
        this.textBlocks = value;
    }

    /**
     * Whether to generate overrides for {@link org.jooq.Table#where(org.jooq.Condition)} and related overloads.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWhereMethodOverrides() {
        return whereMethodOverrides;
    }

    /**
     * Whether to generate overrides for {@link org.jooq.Table#where(org.jooq.Condition)} and related overloads.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWhereMethodOverrides(Boolean value) {
        this.whereMethodOverrides = value;
    }

    /**
     * Whether to generate overrides (see <a href="https://github.com/jOOQ/jOOQ/issues/13937">https://github.com/jOOQ/jOOQ/issues/13937</a>) for {@link org.jooq.Table#rename(org.jooq.Name)} and related overloads.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRenameMethodOverrides() {
        return renameMethodOverrides;
    }

    /**
     * Whether to generate overrides (see <a href="https://github.com/jOOQ/jOOQ/issues/13937">https://github.com/jOOQ/jOOQ/issues/13937</a>) for {@link org.jooq.Table#rename(org.jooq.Name)} and related overloads.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRenameMethodOverrides(Boolean value) {
        this.renameMethodOverrides = value;
    }

    /**
     * Whether to generate overrides for {@link org.jooq.Table#as(org.jooq.Name)} and related overloads.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAsMethodOverrides() {
        return asMethodOverrides;
    }

    /**
     * Whether to generate overrides for {@link org.jooq.Table#as(org.jooq.Name)} and related overloads.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAsMethodOverrides(Boolean value) {
        this.asMethodOverrides = value;
    }

    /**
     * Whether hidden columns should be generated in records.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHiddenColumnsInRecords() {
        return hiddenColumnsInRecords;
    }

    /**
     * Whether hidden columns should be generated in records.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHiddenColumnsInRecords(Boolean value) {
        this.hiddenColumnsInRecords = value;
    }

    /**
     * Whether hidden columns should be generated in POJOs.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHiddenColumnsInPojos() {
        return hiddenColumnsInPojos;
    }

    /**
     * Whether hidden columns should be generated in POJOs.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHiddenColumnsInPojos(Boolean value) {
        this.hiddenColumnsInPojos = value;
    }

    /**
     * Whether hidden columns should be generated in interfaces.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHiddenColumnsInInterfaces() {
        return hiddenColumnsInInterfaces;
    }

    /**
     * Whether hidden columns should be generated in interfaces.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHiddenColumnsInInterfaces(Boolean value) {
        this.hiddenColumnsInInterfaces = value;
    }

    /**
     * Generate index information.
     * 
     */
    public Generate withIndexes(Boolean value) {
        setIndexes(value);
        return this;
    }

    /**
     * Primary key / foreign key relations should be generated and used.
     * This is a prerequisite for various advanced features
     * 
     */
    public Generate withRelations(Boolean value) {
        setRelations(value);
        return this;
    }

    /**
     * Sequence flags should be generated and used.
     * 
     */
    public Generate withSequenceFlags(Boolean value) {
        setSequenceFlags(value);
        return this;
    }

    /**
     * Generate UDT path expressions on tables and on UDTs.
     * 
     */
    public Generate withUdtPaths(Boolean value) {
        setUdtPaths(value);
        return this;
    }

    /**
     * Generate implicit join path constructors on generated tables for outgoing foreign key relationships (to-one relationships)
     * 
     */
    public Generate withImplicitJoinPathsToOne(Boolean value) {
        setImplicitJoinPathsToOne(value);
        return this;
    }

    /**
     * Generate implicit join path constructors on generated tables for incoming foreign key relationships (to-many relationships)
     * 
     */
    public Generate withImplicitJoinPathsToMany(Boolean value) {
        setImplicitJoinPathsToMany(value);
        return this;
    }

    /**
     * Generate implicit join path constructors on generated tables for many-to-many relationships. This turns off implicitly, if either of the other path generations are turned off.
     * 
     */
    public Generate withImplicitJoinPathsManyToMany(Boolean value) {
        setImplicitJoinPathsManyToMany(value);
        return this;
    }

    /**
     * Generate implicit join path table subtypes implementing {@link org.jooq.Path} for increased JOIN convenience.
     * 
     */
    public Generate withImplicitJoinPathTableSubtypes(Boolean value) {
        setImplicitJoinPathTableSubtypes(value);
        return this;
    }

    /**
     * Generate implicit join path constructors also if there isn't any outgoing or incoming foreign key relationship.
     * 
     */
    public Generate withImplicitJoinPathUnusedConstructors(Boolean value) {
        setImplicitJoinPathUnusedConstructors(value);
        return this;
    }

    /**
     * Whether names of unambiguous {@link org.jooq.meta.ForeignKeyDefinition} should be based
     * on the referenced {@link org.jooq.meta.TableDefinition}.
     * <p>
     * When a child table has only one {@link org.jooq.meta.ForeignKeyDefinition} towards a
     * parent table, then that path is "unambiguous." In that case, some
     * {@link GeneratorStrategy} implementations may choose to use the parent
     * table's {@link org.jooq.meta.TableDefinition} for implementations of
     * {@link org.jooq.codegen.GeneratorStrategy#getJavaMethodName(Definition)}, instead of the
     * {@link org.jooq.meta.ForeignKeyDefinition}, e.g. for implicit join paths.
     * <p>
     * This flag allows for turning off this default behaviour.
     * 
     */
    public Generate withImplicitJoinPathsUseTableNameForUnambiguousFKs(Boolean value) {
        setImplicitJoinPathsUseTableNameForUnambiguousFKs(value);
        return this;
    }

    /**
     * Whether implicit join path constructors should be offered as properties in Kotlin.
     * 
     */
    public Generate withImplicitJoinPathsAsKotlinProperties(Boolean value) {
        setImplicitJoinPathsAsKotlinProperties(value);
        return this;
    }

    /**
     * Generate deprecated code for backwards compatibility
     * 
     */
    public Generate withDeprecated(Boolean value) {
        setDeprecated(value);
        return this;
    }

    /**
     * Generate deprecation annotations on references to unknown data types.
     * This helps to identify columns, attributes, and parameters, which may not be usable through
     * jOOQ API, without adding custom data type bindings to them.
     * 
     */
    public Generate withDeprecationOnUnknownTypes(Boolean value) {
        setDeprecationOnUnknownTypes(value);
        return this;
    }

    /**
     * @deprecated
     * 
     */
    public Generate withInstanceFields(Boolean value) {
        setInstanceFields(value);
        return this;
    }

    /**
     * The visibility modifier to be used with generated code.
     * 
     */
    public Generate withVisibilityModifier(VisibilityModifier value) {
        setVisibilityModifier(value);
        return this;
    }

    /**
     * Use the {@link org.jooq.Generated} annotation specified by {@link #getGeneratedAnnotationType()} in generated code.
     * 
     */
    public Generate withGeneratedAnnotation(Boolean value) {
        setGeneratedAnnotation(value);
        return this;
    }

    /**
     * Specify the type of {@link org.jooq.Generated} annotation to use in generated code.
     * 
     */
    public Generate withGeneratedAnnotationType(GeneratedAnnotationType value) {
        setGeneratedAnnotationType(value);
        return this;
    }

    /**
     * Whether the {@link org.jooq.Generated} annotation specified by {@link #getGeneratedAnnotationType()} should include the <code>date</code> attribute.
     * 
     */
    public Generate withGeneratedAnnotationDate(Boolean value) {
        setGeneratedAnnotationDate(value);
        return this;
    }

    /**
     * Whether the {@link org.jooq.Generated} annotation specified by {@link #getGeneratedAnnotationType()} should include the jOOQ version.
     * 
     */
    public Generate withGeneratedAnnotationJooqVersion(Boolean value) {
        setGeneratedAnnotationJooqVersion(value);
        return this;
    }

    /**
     * Whether non-nullable items should be annotated with the annotation type specified in {@link #nonnullAnnotationType}. In SQL and by consequence in jOOQ, non-nullability cannot be guaranteed statically. There may still be some cases (e.g. after unions, outer joins, etc.) where a normally non-null value turns out to be null!
     * 
     */
    public Generate withNonnullAnnotation(Boolean value) {
        setNonnullAnnotation(value);
        return this;
    }

    /**
     * Specify the qualified annotation name for all non-nullable items in generated code, defaulting to the JSR-305 {@link javax.annotation.Nonnull} type.
     * 
     */
    public Generate withNonnullAnnotationType(String value) {
        setNonnullAnnotationType(value);
        return this;
    }

    /**
     * Whether nullable items should be annotated with the annotation type specified in {@link #nullableAnnotationType}. Unlike {@link #nonnullAnnotation}, nullability can be guaranteed as in SQL, and by consequence in jOOQ, every column expression can be made nullable using some SQL operation.
     * 
     */
    public Generate withNullableAnnotation(Boolean value) {
        setNullableAnnotation(value);
        return this;
    }

    /**
     * Whether write-only (e.g. defaulted, non-null) nullable items should be annotated with the annotation type specified in {@link #nullableAnnotationType}. Unlike {@link #nonnullAnnotation}, nullability can be guaranteed as in SQL, and by consequence in jOOQ, every column expression can be made nullable using some SQL operation.
     * 
     */
    public Generate withNullableAnnotationOnWriteOnlyNullableTypes(Boolean value) {
        setNullableAnnotationOnWriteOnlyNullableTypes(value);
        return this;
    }

    /**
     * Specify the qualified annotation name for all nullable items in generated code, defaulting to the JSR-305 {@link javax.annotation.Nullable} type.
     * 
     */
    public Generate withNullableAnnotationType(String value) {
        setNullableAnnotationType(value);
        return this;
    }

    /**
     * Generate the {@link java.beans.ConstructorProperties} annotation on generated POJOs and/or records.
     * 
     */
    public Generate withConstructorPropertiesAnnotation(Boolean value) {
        setConstructorPropertiesAnnotation(value);
        return this;
    }

    /**
     * Generate the {@link java.beans.ConstructorProperties} annotation on generated POJOs (defaults to <code>true</code> if {@link #constructorPropertiesAnnotation} is active).
     * 
     */
    public Generate withConstructorPropertiesAnnotationOnPojos(Boolean value) {
        setConstructorPropertiesAnnotationOnPojos(value);
        return this;
    }

    /**
     * Generate the {@link java.beans.ConstructorProperties} annotation on generated records (defaults to <code>true</code> if {@link #constructorPropertiesAnnotation} is active).
     * 
     */
    public Generate withConstructorPropertiesAnnotationOnRecords(Boolean value) {
        setConstructorPropertiesAnnotationOnRecords(value);
        return this;
    }

    /**
     * Generate Routine classes.
     * 
     */
    public Generate withRoutines(Boolean value) {
        setRoutines(value);
        return this;
    }

    /**
     * Generate Sequence classes.
     * 
     */
    public Generate withSequences(Boolean value) {
        setSequences(value);
        return this;
    }

    /**
     * Generate Trigger classes.
     * 
     */
    public Generate withTriggers(Boolean value) {
        setTriggers(value);
        return this;
    }

    /**
     * Generate Synonym classes.
     * 
     */
    public Generate withSynonyms(Boolean value) {
        setSynonyms(value);
        return this;
    }

    /**
     * Generate UDT classes.
     * 
     */
    public Generate withUdts(Boolean value) {
        setUdts(value);
        return this;
    }

    /**
     * Generate Queue classes.
     * 
     */
    public Generate withQueues(Boolean value) {
        setQueues(value);
        return this;
    }

    /**
     * Generate database Link classes.
     * 
     */
    public Generate withLinks(Boolean value) {
        setLinks(value);
        return this;
    }

    /**
     * Generate Key classes.
     * 
     */
    public Generate withKeys(Boolean value) {
        setKeys(value);
        return this;
    }

    /**
     * Generate Table classes.
     * 
     */
    public Generate withTables(Boolean value) {
        setTables(value);
        return this;
    }

    /**
     * Generate embeddable classes.
     * 
     */
    public Generate withEmbeddables(Boolean value) {
        setEmbeddables(value);
        return this;
    }

    /**
     * Generate TableRecord classes.
     * 
     */
    public Generate withRecords(Boolean value) {
        setRecords(value);
        return this;
    }

    /**
     * All the object identifiers for which to generate records, by default, all of them.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public Generate withRecordsIncludes(String value) {
        setRecordsIncludes(value);
        return this;
    }

    /**
     * All the object identifiers for which not to generate records.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public Generate withRecordsExcludes(String value) {
        setRecordsExcludes(value);
        return this;
    }

    /**
     * Generate TableRecord classes that implement Record[N] super types
     * 
     */
    public Generate withRecordsImplementingRecordN(Boolean value) {
        setRecordsImplementingRecordN(value);
        return this;
    }

    /**
     * @deprecated Activate the legacy Scala sealed trait enum emulation
     * 
     */
    public Generate withEnumsAsScalaSealedTraits(Boolean value) {
        setEnumsAsScalaSealedTraits(value);
        return this;
    }

    /**
     * Generate enums as Scala 3.0 enums (if Scala 3 is supported).
     * 
     */
    public Generate withEnumsAsScalaEnums(Boolean value) {
        setEnumsAsScalaEnums(value);
        return this;
    }

    /**
     * Generate POJOs.
     * 
     */
    public Generate withPojos(Boolean value) {
        setPojos(value);
        return this;
    }

    /**
     * All the object identifiers for which to generate POJOs, by default, all of them.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public Generate withPojosIncludes(String value) {
        setPojosIncludes(value);
        return this;
    }

    /**
     * All the object identifiers for which not to generate POJOs.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public Generate withPojosExcludes(String value) {
        setPojosExcludes(value);
        return this;
    }

    /**
     * Generate basic equals() and hashCode() methods in POJOs.
     * 
     */
    public Generate withPojosEqualsAndHashCode(Boolean value) {
        setPojosEqualsAndHashCode(value);
        return this;
    }

    /**
     * Include primary key columns only in generated equals() and hashCode() methods in POJOs.
     * 
     */
    public Generate withPojosEqualsAndHashCodeIncludePrimaryKeyOnly(Boolean value) {
        setPojosEqualsAndHashCodeIncludePrimaryKeyOnly(value);
        return this;
    }

    /**
     * A regular expression matching columns for inclusion in generated equals() and hashCode() methods in POJOs.
     * 
     */
    public Generate withPojosEqualsAndHashCodeColumnIncludeExpression(String value) {
        setPojosEqualsAndHashCodeColumnIncludeExpression(value);
        return this;
    }

    /**
     * A regular expression matching columns for exclusion in generated equals() and hashCode() methods in POJOs.
     * 
     */
    public Generate withPojosEqualsAndHashCodeColumnExcludeExpression(String value) {
        setPojosEqualsAndHashCodeColumnExcludeExpression(value);
        return this;
    }

    /**
     * Generate basic toString() methods in POJOs.
     * 
     */
    public Generate withPojosToString(Boolean value) {
        setPojosToString(value);
        return this;
    }

    /**
     * Generate POJOs as records, when using the JavaGenerator.
     * 
     */
    public Generate withPojosAsJavaRecordClasses(Boolean value) {
        setPojosAsJavaRecordClasses(value);
        return this;
    }

    /**
     * Generate POJOs as case classes, when using the ScalaGenerator or Scala3Generator.
     * 
     */
    public Generate withPojosAsScalaCaseClasses(Boolean value) {
        setPojosAsScalaCaseClasses(value);
        return this;
    }

    /**
     * Generate POJOs as data classes, when using the KotlinGenerator.
     * 
     */
    public Generate withPojosAsKotlinDataClasses(Boolean value) {
        setPojosAsKotlinDataClasses(value);
        return this;
    }

    /**
     * Generate immutable POJOs.
     * 
     */
    public Generate withImmutablePojos(Boolean value) {
        setImmutablePojos(value);
        return this;
    }

    /**
     * Generate serializable POJOs.
     * 
     */
    public Generate withSerializablePojos(Boolean value) {
        setSerializablePojos(value);
        return this;
    }

    /**
     * Generated interfaces to be implemented by records and/or POJOs.
     * 
     */
    public Generate withInterfaces(Boolean value) {
        setInterfaces(value);
        return this;
    }

    /**
     * Generate immutable interfaces.
     * 
     */
    public Generate withImmutableInterfaces(Boolean value) {
        setImmutableInterfaces(value);
        return this;
    }

    /**
     * Generate serializable interfaces.
     * 
     */
    public Generate withSerializableInterfaces(Boolean value) {
        setSerializableInterfaces(value);
        return this;
    }

    /**
     * Generate DAOs.
     * 
     */
    public Generate withDaos(Boolean value) {
        setDaos(value);
        return this;
    }

    /**
     * All the object identifiers for which to generate DAOs, by default, all of them.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public Generate withDaosIncludes(String value) {
        setDaosIncludes(value);
        return this;
    }

    /**
     * All the object identifiers for which not to generate DAOs.
     * <p>
     * This is a Java regular expression. Use the pipe to separate several expressions.
     * Watch out for case-sensitivity. Depending on your database, this might be
     * important!
     * <p>
     * You can create case-insensitive regular expressions
     * using this syntax: <code>(?i:expr)</code>
     * <p>
     * Whitespace is ignored and comments are possible unless overridden in {@link #getRegexFlags()}.
     * 
     */
    public Generate withDaosExcludes(String value) {
        setDaosExcludes(value);
        return this;
    }

    /**
     * Generate references to the most up to date minor release in {@link org.jooq.Constants} to produce compilation errors if an outdated runtime library is being used.
     * 
     */
    public Generate withJooqVersionReference(Boolean value) {
        setJooqVersionReference(value);
        return this;
    }

    /**
     * Annotate POJOs and Records with JPA annotations.
     * 
     */
    public Generate withJpaAnnotations(Boolean value) {
        setJpaAnnotations(value);
        return this;
    }

    /**
     * Version of JPA specification is to be used to generate version-specific annotations. If it is omitted, the latest version is used by default.
     * 
     */
    public Generate withJpaVersion(String value) {
        setJpaVersion(value);
        return this;
    }

    /**
     * Annotate POJOs and Records with JSR-303 validation annotations
     * 
     */
    public Generate withValidationAnnotations(Boolean value) {
        setValidationAnnotations(value);
        return this;
    }

    /**
     * Annotate DAOs with useful spring annotations such as @Repository or @Autowired.
     * 
     */
    public Generate withSpringAnnotations(Boolean value) {
        setSpringAnnotations(value);
        return this;
    }

    /**
     * Generate an AbstractSpringDAOImpl as a base class for other DAO classes, containing @Transactional annotations, etc.
     * 
     */
    public Generate withSpringDao(Boolean value) {
        setSpringDao(value);
        return this;
    }

    /**
     * Workaround for Kotlin generating <code>setX()</code> setters instead of <code>setIsX()</code> in byte code for mutable properties called <code>isX</code>.
     * 
     */
    public Generate withKotlinSetterJvmNameAnnotationsOnIsPrefix(Boolean value) {
        setKotlinSetterJvmNameAnnotationsOnIsPrefix(value);
        return this;
    }

    /**
     * Generate non-nullable types on POJO attributes, where column is not null.
     * 
     */
    public Generate withKotlinNotNullPojoAttributes(Boolean value) {
        setKotlinNotNullPojoAttributes(value);
        return this;
    }

    /**
     * Generate non-nullable types on Record attributes, where column is not null.
     * 
     */
    public Generate withKotlinNotNullRecordAttributes(Boolean value) {
        setKotlinNotNullRecordAttributes(value);
        return this;
    }

    /**
     * Generate non-nullable types on interface attributes, where column is not null.
     * 
     */
    public Generate withKotlinNotNullInterfaceAttributes(Boolean value) {
        setKotlinNotNullInterfaceAttributes(value);
        return this;
    }

    /**
     * Generate defaulted nullable POJO attributes.
     * 
     */
    public Generate withKotlinDefaultedNullablePojoAttributes(Boolean value) {
        setKotlinDefaultedNullablePojoAttributes(value);
        return this;
    }

    /**
     * Generate defaulted nullable Record attributes.
     * 
     */
    public Generate withKotlinDefaultedNullableRecordAttributes(Boolean value) {
        setKotlinDefaultedNullableRecordAttributes(value);
        return this;
    }

    /**
     * Turn on generation of all global object names.
     * 
     */
    public Generate withGlobalObjectNames(Boolean value) {
        setGlobalObjectNames(value);
        return this;
    }

    /**
     * Turn off generation of all global object references.
     * 
     */
    public Generate withGlobalObjectReferences(Boolean value) {
        setGlobalObjectReferences(value);
        return this;
    }

    /**
     * Turn off generation of global catalog references.
     * 
     */
    public Generate withGlobalCatalogReferences(Boolean value) {
        setGlobalCatalogReferences(value);
        return this;
    }

    /**
     * Turn off generation of global schema references.
     * 
     */
    public Generate withGlobalSchemaReferences(Boolean value) {
        setGlobalSchemaReferences(value);
        return this;
    }

    /**
     * Turn off generation of global domain references.
     * 
     */
    public Generate withGlobalDomainReferences(Boolean value) {
        setGlobalDomainReferences(value);
        return this;
    }

    /**
     * Turn off generation of global trigger references.
     * 
     */
    public Generate withGlobalTriggerReferences(Boolean value) {
        setGlobalTriggerReferences(value);
        return this;
    }

    /**
     * Turn off generation of global synonym references.
     * 
     */
    public Generate withGlobalSynonymReferences(Boolean value) {
        setGlobalSynonymReferences(value);
        return this;
    }

    /**
     * Turn off generation of global table references.
     * 
     */
    public Generate withGlobalTableReferences(Boolean value) {
        setGlobalTableReferences(value);
        return this;
    }

    /**
     * Turn off generation of global sequence references.
     * 
     */
    public Generate withGlobalSequenceReferences(Boolean value) {
        setGlobalSequenceReferences(value);
        return this;
    }

    /**
     * Turn off generation of global UDT references.
     * 
     */
    public Generate withGlobalUDTReferences(Boolean value) {
        setGlobalUDTReferences(value);
        return this;
    }

    /**
     * Turn off generation of global routine references.
     * 
     */
    public Generate withGlobalRoutineReferences(Boolean value) {
        setGlobalRoutineReferences(value);
        return this;
    }

    /**
     * Turn off generation of global queue references.
     * 
     */
    public Generate withGlobalQueueReferences(Boolean value) {
        setGlobalQueueReferences(value);
        return this;
    }

    /**
     * Turn off generation of global database link references.
     * 
     */
    public Generate withGlobalLinkReferences(Boolean value) {
        setGlobalLinkReferences(value);
        return this;
    }

    /**
     * Turn off generation of global key references.
     * 
     */
    public Generate withGlobalKeyReferences(Boolean value) {
        setGlobalKeyReferences(value);
        return this;
    }

    /**
     * Turn off generation of global index references.
     * 
     */
    public Generate withGlobalIndexReferences(Boolean value) {
        setGlobalIndexReferences(value);
        return this;
    }

    /**
     * Turn off generation of a <code>DefaultCatalog</code> object, which is generated by default in the absence of catalogs.
     * 
     */
    public Generate withDefaultCatalog(Boolean value) {
        setDefaultCatalog(value);
        return this;
    }

    /**
     * Turn off generation of a <code>DefaultSchema</code> object, which is generated by default in the absence of schemas.
     * 
     */
    public Generate withDefaultSchema(Boolean value) {
        setDefaultSchema(value);
        return this;
    }

    /**
     * Turn off generation of Javadoc on all objects.
     * 
     */
    public Generate withJavadoc(Boolean value) {
        setJavadoc(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all objects.
     * 
     */
    public Generate withComments(Boolean value) {
        setComments(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all catalogs.
     * 
     */
    public Generate withCommentsOnCatalogs(Boolean value) {
        setCommentsOnCatalogs(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all schemas.
     * 
     */
    public Generate withCommentsOnSchemas(Boolean value) {
        setCommentsOnSchemas(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all tables.
     * 
     */
    public Generate withCommentsOnTables(Boolean value) {
        setCommentsOnTables(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all columns.
     * 
     */
    public Generate withCommentsOnColumns(Boolean value) {
        setCommentsOnColumns(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all embeddables.
     * 
     */
    public Generate withCommentsOnEmbeddables(Boolean value) {
        setCommentsOnEmbeddables(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all UDTs.
     * 
     */
    public Generate withCommentsOnUDTs(Boolean value) {
        setCommentsOnUDTs(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all attributes.
     * 
     */
    public Generate withCommentsOnAttributes(Boolean value) {
        setCommentsOnAttributes(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all packages.
     * 
     */
    public Generate withCommentsOnPackages(Boolean value) {
        setCommentsOnPackages(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all routines.
     * 
     */
    public Generate withCommentsOnRoutines(Boolean value) {
        setCommentsOnRoutines(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all parameters.
     * 
     */
    public Generate withCommentsOnParameters(Boolean value) {
        setCommentsOnParameters(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all sequences.
     * 
     */
    public Generate withCommentsOnSequences(Boolean value) {
        setCommentsOnSequences(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all domains.
     * 
     */
    public Generate withCommentsOnDomains(Boolean value) {
        setCommentsOnDomains(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all links.
     * 
     */
    public Generate withCommentsOnLinks(Boolean value) {
        setCommentsOnLinks(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all queues.
     * 
     */
    public Generate withCommentsOnQueues(Boolean value) {
        setCommentsOnQueues(value);
        return this;
    }

    /**
     * Turn off generation of all SQL comments as Javadoc on all keys.
     * 
     */
    public Generate withCommentsOnKeys(Boolean value) {
        setCommentsOnKeys(value);
        return this;
    }

    /**
     * Turn off generation of all source code on all object types.
     * 
     */
    public Generate withSources(Boolean value) {
        setSources(value);
        return this;
    }

    /**
     * Turn off generation of all source code on all views.
     * 
     */
    public Generate withSourcesOnViews(Boolean value) {
        setSourcesOnViews(value);
        return this;
    }

    /**
     * Generate fluent setters in records, POJOs, interfaces.
     * 
     */
    public Generate withFluentSetters(Boolean value) {
        setFluentSetters(value);
        return this;
    }

    /**
     * Modify DefaultGeneratorStrategy behaviour to generate getters and setters in JavaBeans style in records, POJOs, interfaces.
     * <p>
     * If this flag is set to false, then:
     * <p>
     * <ul>
     * <li>Column name   : X_INDEX</li>
     * <li>Attribute name: xIndex</li>
     * <li>Getter name   : getXIndex()</li>
     * <li>Setter name   : setXIndex()</li>
     * </ul>
     * <p>
     * If this flag is set to true, then:
     * <ul>
     * <li>Getter name   : getxIndex()</li>
     * <li>Setter name   : setxIndex()</li>
     * </ul>
     * <p>
     * Custom GeneratorStrategy implementations are unaffected
     * 
     */
    public Generate withJavaBeansGettersAndSetters(Boolean value) {
        setJavaBeansGettersAndSetters(value);
        return this;
    }

    /**
     * Generate varargs setters for array types for convenience.
     * <p>
     * This may lead to compilation warnings in current Java versions.
     * 
     */
    public Generate withVarargSetters(Boolean value) {
        setVarargSetters(value);
        return this;
    }

    /**
     * The serial version UID to be generated in all files.
     * 
     */
    public Generate withGeneratedSerialVersionUID(GeneratedSerialVersionUID value) {
        setGeneratedSerialVersionUID(value);
        return this;
    }

    /**
     * The maximum number of members per initialiser, to prevent reaching the 64kb byte code per method limit in generated code.
     * 
     */
    public Generate withMaxMembersPerInitialiser(Integer value) {
        setMaxMembersPerInitialiser(value);
        return this;
    }

    /**
     * A regular expression matching all the types in generated code that should be fully qualified.
     * <p>
     * This can be useful if you have a database object that generates a String
     * class, and you want to avoid naming clashes with the java.lang package
     * by specifying <code>java\.lang\..*</code>
     * 
     */
    public Generate withFullyQualifiedTypes(String value) {
        setFullyQualifiedTypes(value);
        return this;
    }

    /**
     * Whether empty catalogs (e.g. empty because of <excludes/> configurations) should still be generated.
     * 
     */
    public Generate withEmptyCatalogs(Boolean value) {
        setEmptyCatalogs(value);
        return this;
    }

    /**
     * Whether empty schemas (e.g. empty because of <excludes/> configurations) should still be generated.
     * 
     */
    public Generate withEmptySchemas(Boolean value) {
        setEmptySchemas(value);
        return this;
    }

    /**
     * A flag indicating whether Java 8's java.time types should be used by the
     * source code generator, rather than JDBC's java.sql types.
     * <p>
     * This flag is ignored in the commercial Java 6 distribution of jOOQ 3.9+ 
     * 
     */
    public Generate withJavaTimeTypes(Boolean value) {
        setJavaTimeTypes(value);
        return this;
    }

    /**
     * A flag indicating whether the spatial type support should be enabled.
     * 
     */
    public Generate withSpatialTypes(Boolean value) {
        setSpatialTypes(value);
        return this;
    }

    /**
     * A flag indicating whether the XML type support should be enabled.
     * 
     */
    public Generate withXmlTypes(Boolean value) {
        setXmlTypes(value);
        return this;
    }

    /**
     * A flag indicating whether the JSON type support should be enabled.
     * 
     */
    public Generate withJsonTypes(Boolean value) {
        setJsonTypes(value);
        return this;
    }

    /**
     * A flag indicating whether the INTERVAL type support should be enabled.
     * 
     */
    public Generate withIntervalTypes(Boolean value) {
        setIntervalTypes(value);
        return this;
    }

    /**
     * A flag indicating whether the DECFLOAT type support should be enabled.
     * 
     */
    public Generate withDecfloatTypes(Boolean value) {
        setDecfloatTypes(value);
        return this;
    }

    /**
     * The newline characters to be used in generated code. Whitespace characters can be used, e.g. \n, \r\n
     * 
     */
    public Generate withNewline(String value) {
        setNewline(value);
        return this;
    }

    /**
     * The indentation characters to be used in generated code. If unspecified, an idiomatic default indentation of the language will be used (4 spaces in Java, 2 spaces in Scala). Whitespace characters can be used, e.g. \t
     * 
     */
    public Generate withIndentation(String value) {
        setIndentation(value);
        return this;
    }

    /**
     * The print margin to apply to generated Javadoc and other block comments, for automatic line wrapping. The feature is turned off if the print margin is <code>0</code>.
     * 
     */
    public Generate withPrintMarginForBlockComment(Integer value) {
        setPrintMarginForBlockComment(value);
        return this;
    }

    /**
     * Whether to generate String in text block format.
     * 
     */
    public Generate withTextBlocks(GeneratedTextBlocks value) {
        setTextBlocks(value);
        return this;
    }

    /**
     * Whether to generate overrides for {@link org.jooq.Table#where(org.jooq.Condition)} and related overloads.
     * 
     */
    public Generate withWhereMethodOverrides(Boolean value) {
        setWhereMethodOverrides(value);
        return this;
    }

    /**
     * Whether to generate overrides (see <a href="https://github.com/jOOQ/jOOQ/issues/13937">https://github.com/jOOQ/jOOQ/issues/13937</a>) for {@link org.jooq.Table#rename(org.jooq.Name)} and related overloads.
     * 
     */
    public Generate withRenameMethodOverrides(Boolean value) {
        setRenameMethodOverrides(value);
        return this;
    }

    /**
     * Whether to generate overrides for {@link org.jooq.Table#as(org.jooq.Name)} and related overloads.
     * 
     */
    public Generate withAsMethodOverrides(Boolean value) {
        setAsMethodOverrides(value);
        return this;
    }

    /**
     * Whether hidden columns should be generated in records.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Generate withHiddenColumnsInRecords(Boolean value) {
        setHiddenColumnsInRecords(value);
        return this;
    }

    /**
     * Whether hidden columns should be generated in POJOs.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Generate withHiddenColumnsInPojos(Boolean value) {
        setHiddenColumnsInPojos(value);
        return this;
    }

    /**
     * Whether hidden columns should be generated in interfaces.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Generate withHiddenColumnsInInterfaces(Boolean value) {
        setHiddenColumnsInInterfaces(value);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("indexes", indexes);
        builder.append("relations", relations);
        builder.append("sequenceFlags", sequenceFlags);
        builder.append("udtPaths", udtPaths);
        builder.append("implicitJoinPathsToOne", implicitJoinPathsToOne);
        builder.append("implicitJoinPathsToMany", implicitJoinPathsToMany);
        builder.append("implicitJoinPathsManyToMany", implicitJoinPathsManyToMany);
        builder.append("implicitJoinPathTableSubtypes", implicitJoinPathTableSubtypes);
        builder.append("implicitJoinPathUnusedConstructors", implicitJoinPathUnusedConstructors);
        builder.append("implicitJoinPathsUseTableNameForUnambiguousFKs", implicitJoinPathsUseTableNameForUnambiguousFKs);
        builder.append("implicitJoinPathsAsKotlinProperties", implicitJoinPathsAsKotlinProperties);
        builder.append("deprecated", deprecated);
        builder.append("deprecationOnUnknownTypes", deprecationOnUnknownTypes);
        builder.append("instanceFields", instanceFields);
        builder.append("visibilityModifier", visibilityModifier);
        builder.append("generatedAnnotation", generatedAnnotation);
        builder.append("generatedAnnotationType", generatedAnnotationType);
        builder.append("generatedAnnotationDate", generatedAnnotationDate);
        builder.append("generatedAnnotationJooqVersion", generatedAnnotationJooqVersion);
        builder.append("nonnullAnnotation", nonnullAnnotation);
        builder.append("nonnullAnnotationType", nonnullAnnotationType);
        builder.append("nullableAnnotation", nullableAnnotation);
        builder.append("nullableAnnotationOnWriteOnlyNullableTypes", nullableAnnotationOnWriteOnlyNullableTypes);
        builder.append("nullableAnnotationType", nullableAnnotationType);
        builder.append("constructorPropertiesAnnotation", constructorPropertiesAnnotation);
        builder.append("constructorPropertiesAnnotationOnPojos", constructorPropertiesAnnotationOnPojos);
        builder.append("constructorPropertiesAnnotationOnRecords", constructorPropertiesAnnotationOnRecords);
        builder.append("routines", routines);
        builder.append("sequences", sequences);
        builder.append("triggers", triggers);
        builder.append("synonyms", synonyms);
        builder.append("udts", udts);
        builder.append("queues", queues);
        builder.append("links", links);
        builder.append("keys", keys);
        builder.append("tables", tables);
        builder.append("embeddables", embeddables);
        builder.append("records", records);
        builder.append("recordsIncludes", recordsIncludes);
        builder.append("recordsExcludes", recordsExcludes);
        builder.append("recordsImplementingRecordN", recordsImplementingRecordN);
        builder.append("enumsAsScalaSealedTraits", enumsAsScalaSealedTraits);
        builder.append("enumsAsScalaEnums", enumsAsScalaEnums);
        builder.append("pojos", pojos);
        builder.append("pojosIncludes", pojosIncludes);
        builder.append("pojosExcludes", pojosExcludes);
        builder.append("pojosEqualsAndHashCode", pojosEqualsAndHashCode);
        builder.append("pojosEqualsAndHashCodeIncludePrimaryKeyOnly", pojosEqualsAndHashCodeIncludePrimaryKeyOnly);
        builder.append("pojosEqualsAndHashCodeColumnIncludeExpression", pojosEqualsAndHashCodeColumnIncludeExpression);
        builder.append("pojosEqualsAndHashCodeColumnExcludeExpression", pojosEqualsAndHashCodeColumnExcludeExpression);
        builder.append("pojosToString", pojosToString);
        builder.append("pojosAsJavaRecordClasses", pojosAsJavaRecordClasses);
        builder.append("pojosAsScalaCaseClasses", pojosAsScalaCaseClasses);
        builder.append("pojosAsKotlinDataClasses", pojosAsKotlinDataClasses);
        builder.append("immutablePojos", immutablePojos);
        builder.append("serializablePojos", serializablePojos);
        builder.append("interfaces", interfaces);
        builder.append("immutableInterfaces", immutableInterfaces);
        builder.append("serializableInterfaces", serializableInterfaces);
        builder.append("daos", daos);
        builder.append("daosIncludes", daosIncludes);
        builder.append("daosExcludes", daosExcludes);
        builder.append("jooqVersionReference", jooqVersionReference);
        builder.append("jpaAnnotations", jpaAnnotations);
        builder.append("jpaVersion", jpaVersion);
        builder.append("validationAnnotations", validationAnnotations);
        builder.append("springAnnotations", springAnnotations);
        builder.append("springDao", springDao);
        builder.append("kotlinSetterJvmNameAnnotationsOnIsPrefix", kotlinSetterJvmNameAnnotationsOnIsPrefix);
        builder.append("kotlinNotNullPojoAttributes", kotlinNotNullPojoAttributes);
        builder.append("kotlinNotNullRecordAttributes", kotlinNotNullRecordAttributes);
        builder.append("kotlinNotNullInterfaceAttributes", kotlinNotNullInterfaceAttributes);
        builder.append("kotlinDefaultedNullablePojoAttributes", kotlinDefaultedNullablePojoAttributes);
        builder.append("kotlinDefaultedNullableRecordAttributes", kotlinDefaultedNullableRecordAttributes);
        builder.append("globalObjectNames", globalObjectNames);
        builder.append("globalObjectReferences", globalObjectReferences);
        builder.append("globalCatalogReferences", globalCatalogReferences);
        builder.append("globalSchemaReferences", globalSchemaReferences);
        builder.append("globalDomainReferences", globalDomainReferences);
        builder.append("globalTriggerReferences", globalTriggerReferences);
        builder.append("globalSynonymReferences", globalSynonymReferences);
        builder.append("globalTableReferences", globalTableReferences);
        builder.append("globalSequenceReferences", globalSequenceReferences);
        builder.append("globalUDTReferences", globalUDTReferences);
        builder.append("globalRoutineReferences", globalRoutineReferences);
        builder.append("globalQueueReferences", globalQueueReferences);
        builder.append("globalLinkReferences", globalLinkReferences);
        builder.append("globalKeyReferences", globalKeyReferences);
        builder.append("globalIndexReferences", globalIndexReferences);
        builder.append("defaultCatalog", defaultCatalog);
        builder.append("defaultSchema", defaultSchema);
        builder.append("javadoc", javadoc);
        builder.append("comments", comments);
        builder.append("commentsOnCatalogs", commentsOnCatalogs);
        builder.append("commentsOnSchemas", commentsOnSchemas);
        builder.append("commentsOnTables", commentsOnTables);
        builder.append("commentsOnColumns", commentsOnColumns);
        builder.append("commentsOnEmbeddables", commentsOnEmbeddables);
        builder.append("commentsOnUDTs", commentsOnUDTs);
        builder.append("commentsOnAttributes", commentsOnAttributes);
        builder.append("commentsOnPackages", commentsOnPackages);
        builder.append("commentsOnRoutines", commentsOnRoutines);
        builder.append("commentsOnParameters", commentsOnParameters);
        builder.append("commentsOnSequences", commentsOnSequences);
        builder.append("commentsOnDomains", commentsOnDomains);
        builder.append("commentsOnLinks", commentsOnLinks);
        builder.append("commentsOnQueues", commentsOnQueues);
        builder.append("commentsOnKeys", commentsOnKeys);
        builder.append("sources", sources);
        builder.append("sourcesOnViews", sourcesOnViews);
        builder.append("fluentSetters", fluentSetters);
        builder.append("javaBeansGettersAndSetters", javaBeansGettersAndSetters);
        builder.append("varargSetters", varargSetters);
        builder.append("generatedSerialVersionUID", generatedSerialVersionUID);
        builder.append("maxMembersPerInitialiser", maxMembersPerInitialiser);
        builder.append("fullyQualifiedTypes", fullyQualifiedTypes);
        builder.append("emptyCatalogs", emptyCatalogs);
        builder.append("emptySchemas", emptySchemas);
        builder.append("javaTimeTypes", javaTimeTypes);
        builder.append("spatialTypes", spatialTypes);
        builder.append("xmlTypes", xmlTypes);
        builder.append("jsonTypes", jsonTypes);
        builder.append("intervalTypes", intervalTypes);
        builder.append("decfloatTypes", decfloatTypes);
        builder.append("newline", newline);
        builder.append("indentation", indentation);
        builder.append("printMarginForBlockComment", printMarginForBlockComment);
        builder.append("textBlocks", textBlocks);
        builder.append("whereMethodOverrides", whereMethodOverrides);
        builder.append("renameMethodOverrides", renameMethodOverrides);
        builder.append("asMethodOverrides", asMethodOverrides);
        builder.append("hiddenColumnsInRecords", hiddenColumnsInRecords);
        builder.append("hiddenColumnsInPojos", hiddenColumnsInPojos);
        builder.append("hiddenColumnsInInterfaces", hiddenColumnsInInterfaces);
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
        Generate other = ((Generate) that);
        if (indexes == null) {
            if (other.indexes!= null) {
                return false;
            }
        } else {
            if (!indexes.equals(other.indexes)) {
                return false;
            }
        }
        if (relations == null) {
            if (other.relations!= null) {
                return false;
            }
        } else {
            if (!relations.equals(other.relations)) {
                return false;
            }
        }
        if (sequenceFlags == null) {
            if (other.sequenceFlags!= null) {
                return false;
            }
        } else {
            if (!sequenceFlags.equals(other.sequenceFlags)) {
                return false;
            }
        }
        if (udtPaths == null) {
            if (other.udtPaths!= null) {
                return false;
            }
        } else {
            if (!udtPaths.equals(other.udtPaths)) {
                return false;
            }
        }
        if (implicitJoinPathsToOne == null) {
            if (other.implicitJoinPathsToOne!= null) {
                return false;
            }
        } else {
            if (!implicitJoinPathsToOne.equals(other.implicitJoinPathsToOne)) {
                return false;
            }
        }
        if (implicitJoinPathsToMany == null) {
            if (other.implicitJoinPathsToMany!= null) {
                return false;
            }
        } else {
            if (!implicitJoinPathsToMany.equals(other.implicitJoinPathsToMany)) {
                return false;
            }
        }
        if (implicitJoinPathsManyToMany == null) {
            if (other.implicitJoinPathsManyToMany!= null) {
                return false;
            }
        } else {
            if (!implicitJoinPathsManyToMany.equals(other.implicitJoinPathsManyToMany)) {
                return false;
            }
        }
        if (implicitJoinPathTableSubtypes == null) {
            if (other.implicitJoinPathTableSubtypes!= null) {
                return false;
            }
        } else {
            if (!implicitJoinPathTableSubtypes.equals(other.implicitJoinPathTableSubtypes)) {
                return false;
            }
        }
        if (implicitJoinPathUnusedConstructors == null) {
            if (other.implicitJoinPathUnusedConstructors!= null) {
                return false;
            }
        } else {
            if (!implicitJoinPathUnusedConstructors.equals(other.implicitJoinPathUnusedConstructors)) {
                return false;
            }
        }
        if (implicitJoinPathsUseTableNameForUnambiguousFKs == null) {
            if (other.implicitJoinPathsUseTableNameForUnambiguousFKs!= null) {
                return false;
            }
        } else {
            if (!implicitJoinPathsUseTableNameForUnambiguousFKs.equals(other.implicitJoinPathsUseTableNameForUnambiguousFKs)) {
                return false;
            }
        }
        if (implicitJoinPathsAsKotlinProperties == null) {
            if (other.implicitJoinPathsAsKotlinProperties!= null) {
                return false;
            }
        } else {
            if (!implicitJoinPathsAsKotlinProperties.equals(other.implicitJoinPathsAsKotlinProperties)) {
                return false;
            }
        }
        if (deprecated == null) {
            if (other.deprecated!= null) {
                return false;
            }
        } else {
            if (!deprecated.equals(other.deprecated)) {
                return false;
            }
        }
        if (deprecationOnUnknownTypes == null) {
            if (other.deprecationOnUnknownTypes!= null) {
                return false;
            }
        } else {
            if (!deprecationOnUnknownTypes.equals(other.deprecationOnUnknownTypes)) {
                return false;
            }
        }
        if (instanceFields == null) {
            if (other.instanceFields!= null) {
                return false;
            }
        } else {
            if (!instanceFields.equals(other.instanceFields)) {
                return false;
            }
        }
        if (visibilityModifier == null) {
            if (other.visibilityModifier!= null) {
                return false;
            }
        } else {
            if (!visibilityModifier.equals(other.visibilityModifier)) {
                return false;
            }
        }
        if (generatedAnnotation == null) {
            if (other.generatedAnnotation!= null) {
                return false;
            }
        } else {
            if (!generatedAnnotation.equals(other.generatedAnnotation)) {
                return false;
            }
        }
        if (generatedAnnotationType == null) {
            if (other.generatedAnnotationType!= null) {
                return false;
            }
        } else {
            if (!generatedAnnotationType.equals(other.generatedAnnotationType)) {
                return false;
            }
        }
        if (generatedAnnotationDate == null) {
            if (other.generatedAnnotationDate!= null) {
                return false;
            }
        } else {
            if (!generatedAnnotationDate.equals(other.generatedAnnotationDate)) {
                return false;
            }
        }
        if (generatedAnnotationJooqVersion == null) {
            if (other.generatedAnnotationJooqVersion!= null) {
                return false;
            }
        } else {
            if (!generatedAnnotationJooqVersion.equals(other.generatedAnnotationJooqVersion)) {
                return false;
            }
        }
        if (nonnullAnnotation == null) {
            if (other.nonnullAnnotation!= null) {
                return false;
            }
        } else {
            if (!nonnullAnnotation.equals(other.nonnullAnnotation)) {
                return false;
            }
        }
        if (nonnullAnnotationType == null) {
            if (other.nonnullAnnotationType!= null) {
                return false;
            }
        } else {
            if (!nonnullAnnotationType.equals(other.nonnullAnnotationType)) {
                return false;
            }
        }
        if (nullableAnnotation == null) {
            if (other.nullableAnnotation!= null) {
                return false;
            }
        } else {
            if (!nullableAnnotation.equals(other.nullableAnnotation)) {
                return false;
            }
        }
        if (nullableAnnotationOnWriteOnlyNullableTypes == null) {
            if (other.nullableAnnotationOnWriteOnlyNullableTypes!= null) {
                return false;
            }
        } else {
            if (!nullableAnnotationOnWriteOnlyNullableTypes.equals(other.nullableAnnotationOnWriteOnlyNullableTypes)) {
                return false;
            }
        }
        if (nullableAnnotationType == null) {
            if (other.nullableAnnotationType!= null) {
                return false;
            }
        } else {
            if (!nullableAnnotationType.equals(other.nullableAnnotationType)) {
                return false;
            }
        }
        if (constructorPropertiesAnnotation == null) {
            if (other.constructorPropertiesAnnotation!= null) {
                return false;
            }
        } else {
            if (!constructorPropertiesAnnotation.equals(other.constructorPropertiesAnnotation)) {
                return false;
            }
        }
        if (constructorPropertiesAnnotationOnPojos == null) {
            if (other.constructorPropertiesAnnotationOnPojos!= null) {
                return false;
            }
        } else {
            if (!constructorPropertiesAnnotationOnPojos.equals(other.constructorPropertiesAnnotationOnPojos)) {
                return false;
            }
        }
        if (constructorPropertiesAnnotationOnRecords == null) {
            if (other.constructorPropertiesAnnotationOnRecords!= null) {
                return false;
            }
        } else {
            if (!constructorPropertiesAnnotationOnRecords.equals(other.constructorPropertiesAnnotationOnRecords)) {
                return false;
            }
        }
        if (routines == null) {
            if (other.routines!= null) {
                return false;
            }
        } else {
            if (!routines.equals(other.routines)) {
                return false;
            }
        }
        if (sequences == null) {
            if (other.sequences!= null) {
                return false;
            }
        } else {
            if (!sequences.equals(other.sequences)) {
                return false;
            }
        }
        if (triggers == null) {
            if (other.triggers!= null) {
                return false;
            }
        } else {
            if (!triggers.equals(other.triggers)) {
                return false;
            }
        }
        if (synonyms == null) {
            if (other.synonyms!= null) {
                return false;
            }
        } else {
            if (!synonyms.equals(other.synonyms)) {
                return false;
            }
        }
        if (udts == null) {
            if (other.udts!= null) {
                return false;
            }
        } else {
            if (!udts.equals(other.udts)) {
                return false;
            }
        }
        if (queues == null) {
            if (other.queues!= null) {
                return false;
            }
        } else {
            if (!queues.equals(other.queues)) {
                return false;
            }
        }
        if (links == null) {
            if (other.links!= null) {
                return false;
            }
        } else {
            if (!links.equals(other.links)) {
                return false;
            }
        }
        if (keys == null) {
            if (other.keys!= null) {
                return false;
            }
        } else {
            if (!keys.equals(other.keys)) {
                return false;
            }
        }
        if (tables == null) {
            if (other.tables!= null) {
                return false;
            }
        } else {
            if (!tables.equals(other.tables)) {
                return false;
            }
        }
        if (embeddables == null) {
            if (other.embeddables!= null) {
                return false;
            }
        } else {
            if (!embeddables.equals(other.embeddables)) {
                return false;
            }
        }
        if (records == null) {
            if (other.records!= null) {
                return false;
            }
        } else {
            if (!records.equals(other.records)) {
                return false;
            }
        }
        if (recordsIncludes == null) {
            if (other.recordsIncludes!= null) {
                return false;
            }
        } else {
            if (!recordsIncludes.equals(other.recordsIncludes)) {
                return false;
            }
        }
        if (recordsExcludes == null) {
            if (other.recordsExcludes!= null) {
                return false;
            }
        } else {
            if (!recordsExcludes.equals(other.recordsExcludes)) {
                return false;
            }
        }
        if (recordsImplementingRecordN == null) {
            if (other.recordsImplementingRecordN!= null) {
                return false;
            }
        } else {
            if (!recordsImplementingRecordN.equals(other.recordsImplementingRecordN)) {
                return false;
            }
        }
        if (enumsAsScalaSealedTraits == null) {
            if (other.enumsAsScalaSealedTraits!= null) {
                return false;
            }
        } else {
            if (!enumsAsScalaSealedTraits.equals(other.enumsAsScalaSealedTraits)) {
                return false;
            }
        }
        if (enumsAsScalaEnums == null) {
            if (other.enumsAsScalaEnums!= null) {
                return false;
            }
        } else {
            if (!enumsAsScalaEnums.equals(other.enumsAsScalaEnums)) {
                return false;
            }
        }
        if (pojos == null) {
            if (other.pojos!= null) {
                return false;
            }
        } else {
            if (!pojos.equals(other.pojos)) {
                return false;
            }
        }
        if (pojosIncludes == null) {
            if (other.pojosIncludes!= null) {
                return false;
            }
        } else {
            if (!pojosIncludes.equals(other.pojosIncludes)) {
                return false;
            }
        }
        if (pojosExcludes == null) {
            if (other.pojosExcludes!= null) {
                return false;
            }
        } else {
            if (!pojosExcludes.equals(other.pojosExcludes)) {
                return false;
            }
        }
        if (pojosEqualsAndHashCode == null) {
            if (other.pojosEqualsAndHashCode!= null) {
                return false;
            }
        } else {
            if (!pojosEqualsAndHashCode.equals(other.pojosEqualsAndHashCode)) {
                return false;
            }
        }
        if (pojosEqualsAndHashCodeIncludePrimaryKeyOnly == null) {
            if (other.pojosEqualsAndHashCodeIncludePrimaryKeyOnly!= null) {
                return false;
            }
        } else {
            if (!pojosEqualsAndHashCodeIncludePrimaryKeyOnly.equals(other.pojosEqualsAndHashCodeIncludePrimaryKeyOnly)) {
                return false;
            }
        }
        if (pojosEqualsAndHashCodeColumnIncludeExpression == null) {
            if (other.pojosEqualsAndHashCodeColumnIncludeExpression!= null) {
                return false;
            }
        } else {
            if (!pojosEqualsAndHashCodeColumnIncludeExpression.equals(other.pojosEqualsAndHashCodeColumnIncludeExpression)) {
                return false;
            }
        }
        if (pojosEqualsAndHashCodeColumnExcludeExpression == null) {
            if (other.pojosEqualsAndHashCodeColumnExcludeExpression!= null) {
                return false;
            }
        } else {
            if (!pojosEqualsAndHashCodeColumnExcludeExpression.equals(other.pojosEqualsAndHashCodeColumnExcludeExpression)) {
                return false;
            }
        }
        if (pojosToString == null) {
            if (other.pojosToString!= null) {
                return false;
            }
        } else {
            if (!pojosToString.equals(other.pojosToString)) {
                return false;
            }
        }
        if (pojosAsJavaRecordClasses == null) {
            if (other.pojosAsJavaRecordClasses!= null) {
                return false;
            }
        } else {
            if (!pojosAsJavaRecordClasses.equals(other.pojosAsJavaRecordClasses)) {
                return false;
            }
        }
        if (pojosAsScalaCaseClasses == null) {
            if (other.pojosAsScalaCaseClasses!= null) {
                return false;
            }
        } else {
            if (!pojosAsScalaCaseClasses.equals(other.pojosAsScalaCaseClasses)) {
                return false;
            }
        }
        if (pojosAsKotlinDataClasses == null) {
            if (other.pojosAsKotlinDataClasses!= null) {
                return false;
            }
        } else {
            if (!pojosAsKotlinDataClasses.equals(other.pojosAsKotlinDataClasses)) {
                return false;
            }
        }
        if (immutablePojos == null) {
            if (other.immutablePojos!= null) {
                return false;
            }
        } else {
            if (!immutablePojos.equals(other.immutablePojos)) {
                return false;
            }
        }
        if (serializablePojos == null) {
            if (other.serializablePojos!= null) {
                return false;
            }
        } else {
            if (!serializablePojos.equals(other.serializablePojos)) {
                return false;
            }
        }
        if (interfaces == null) {
            if (other.interfaces!= null) {
                return false;
            }
        } else {
            if (!interfaces.equals(other.interfaces)) {
                return false;
            }
        }
        if (immutableInterfaces == null) {
            if (other.immutableInterfaces!= null) {
                return false;
            }
        } else {
            if (!immutableInterfaces.equals(other.immutableInterfaces)) {
                return false;
            }
        }
        if (serializableInterfaces == null) {
            if (other.serializableInterfaces!= null) {
                return false;
            }
        } else {
            if (!serializableInterfaces.equals(other.serializableInterfaces)) {
                return false;
            }
        }
        if (daos == null) {
            if (other.daos!= null) {
                return false;
            }
        } else {
            if (!daos.equals(other.daos)) {
                return false;
            }
        }
        if (daosIncludes == null) {
            if (other.daosIncludes!= null) {
                return false;
            }
        } else {
            if (!daosIncludes.equals(other.daosIncludes)) {
                return false;
            }
        }
        if (daosExcludes == null) {
            if (other.daosExcludes!= null) {
                return false;
            }
        } else {
            if (!daosExcludes.equals(other.daosExcludes)) {
                return false;
            }
        }
        if (jooqVersionReference == null) {
            if (other.jooqVersionReference!= null) {
                return false;
            }
        } else {
            if (!jooqVersionReference.equals(other.jooqVersionReference)) {
                return false;
            }
        }
        if (jpaAnnotations == null) {
            if (other.jpaAnnotations!= null) {
                return false;
            }
        } else {
            if (!jpaAnnotations.equals(other.jpaAnnotations)) {
                return false;
            }
        }
        if (jpaVersion == null) {
            if (other.jpaVersion!= null) {
                return false;
            }
        } else {
            if (!jpaVersion.equals(other.jpaVersion)) {
                return false;
            }
        }
        if (validationAnnotations == null) {
            if (other.validationAnnotations!= null) {
                return false;
            }
        } else {
            if (!validationAnnotations.equals(other.validationAnnotations)) {
                return false;
            }
        }
        if (springAnnotations == null) {
            if (other.springAnnotations!= null) {
                return false;
            }
        } else {
            if (!springAnnotations.equals(other.springAnnotations)) {
                return false;
            }
        }
        if (springDao == null) {
            if (other.springDao!= null) {
                return false;
            }
        } else {
            if (!springDao.equals(other.springDao)) {
                return false;
            }
        }
        if (kotlinSetterJvmNameAnnotationsOnIsPrefix == null) {
            if (other.kotlinSetterJvmNameAnnotationsOnIsPrefix!= null) {
                return false;
            }
        } else {
            if (!kotlinSetterJvmNameAnnotationsOnIsPrefix.equals(other.kotlinSetterJvmNameAnnotationsOnIsPrefix)) {
                return false;
            }
        }
        if (kotlinNotNullPojoAttributes == null) {
            if (other.kotlinNotNullPojoAttributes!= null) {
                return false;
            }
        } else {
            if (!kotlinNotNullPojoAttributes.equals(other.kotlinNotNullPojoAttributes)) {
                return false;
            }
        }
        if (kotlinNotNullRecordAttributes == null) {
            if (other.kotlinNotNullRecordAttributes!= null) {
                return false;
            }
        } else {
            if (!kotlinNotNullRecordAttributes.equals(other.kotlinNotNullRecordAttributes)) {
                return false;
            }
        }
        if (kotlinNotNullInterfaceAttributes == null) {
            if (other.kotlinNotNullInterfaceAttributes!= null) {
                return false;
            }
        } else {
            if (!kotlinNotNullInterfaceAttributes.equals(other.kotlinNotNullInterfaceAttributes)) {
                return false;
            }
        }
        if (kotlinDefaultedNullablePojoAttributes == null) {
            if (other.kotlinDefaultedNullablePojoAttributes!= null) {
                return false;
            }
        } else {
            if (!kotlinDefaultedNullablePojoAttributes.equals(other.kotlinDefaultedNullablePojoAttributes)) {
                return false;
            }
        }
        if (kotlinDefaultedNullableRecordAttributes == null) {
            if (other.kotlinDefaultedNullableRecordAttributes!= null) {
                return false;
            }
        } else {
            if (!kotlinDefaultedNullableRecordAttributes.equals(other.kotlinDefaultedNullableRecordAttributes)) {
                return false;
            }
        }
        if (globalObjectNames == null) {
            if (other.globalObjectNames!= null) {
                return false;
            }
        } else {
            if (!globalObjectNames.equals(other.globalObjectNames)) {
                return false;
            }
        }
        if (globalObjectReferences == null) {
            if (other.globalObjectReferences!= null) {
                return false;
            }
        } else {
            if (!globalObjectReferences.equals(other.globalObjectReferences)) {
                return false;
            }
        }
        if (globalCatalogReferences == null) {
            if (other.globalCatalogReferences!= null) {
                return false;
            }
        } else {
            if (!globalCatalogReferences.equals(other.globalCatalogReferences)) {
                return false;
            }
        }
        if (globalSchemaReferences == null) {
            if (other.globalSchemaReferences!= null) {
                return false;
            }
        } else {
            if (!globalSchemaReferences.equals(other.globalSchemaReferences)) {
                return false;
            }
        }
        if (globalDomainReferences == null) {
            if (other.globalDomainReferences!= null) {
                return false;
            }
        } else {
            if (!globalDomainReferences.equals(other.globalDomainReferences)) {
                return false;
            }
        }
        if (globalTriggerReferences == null) {
            if (other.globalTriggerReferences!= null) {
                return false;
            }
        } else {
            if (!globalTriggerReferences.equals(other.globalTriggerReferences)) {
                return false;
            }
        }
        if (globalSynonymReferences == null) {
            if (other.globalSynonymReferences!= null) {
                return false;
            }
        } else {
            if (!globalSynonymReferences.equals(other.globalSynonymReferences)) {
                return false;
            }
        }
        if (globalTableReferences == null) {
            if (other.globalTableReferences!= null) {
                return false;
            }
        } else {
            if (!globalTableReferences.equals(other.globalTableReferences)) {
                return false;
            }
        }
        if (globalSequenceReferences == null) {
            if (other.globalSequenceReferences!= null) {
                return false;
            }
        } else {
            if (!globalSequenceReferences.equals(other.globalSequenceReferences)) {
                return false;
            }
        }
        if (globalUDTReferences == null) {
            if (other.globalUDTReferences!= null) {
                return false;
            }
        } else {
            if (!globalUDTReferences.equals(other.globalUDTReferences)) {
                return false;
            }
        }
        if (globalRoutineReferences == null) {
            if (other.globalRoutineReferences!= null) {
                return false;
            }
        } else {
            if (!globalRoutineReferences.equals(other.globalRoutineReferences)) {
                return false;
            }
        }
        if (globalQueueReferences == null) {
            if (other.globalQueueReferences!= null) {
                return false;
            }
        } else {
            if (!globalQueueReferences.equals(other.globalQueueReferences)) {
                return false;
            }
        }
        if (globalLinkReferences == null) {
            if (other.globalLinkReferences!= null) {
                return false;
            }
        } else {
            if (!globalLinkReferences.equals(other.globalLinkReferences)) {
                return false;
            }
        }
        if (globalKeyReferences == null) {
            if (other.globalKeyReferences!= null) {
                return false;
            }
        } else {
            if (!globalKeyReferences.equals(other.globalKeyReferences)) {
                return false;
            }
        }
        if (globalIndexReferences == null) {
            if (other.globalIndexReferences!= null) {
                return false;
            }
        } else {
            if (!globalIndexReferences.equals(other.globalIndexReferences)) {
                return false;
            }
        }
        if (defaultCatalog == null) {
            if (other.defaultCatalog!= null) {
                return false;
            }
        } else {
            if (!defaultCatalog.equals(other.defaultCatalog)) {
                return false;
            }
        }
        if (defaultSchema == null) {
            if (other.defaultSchema!= null) {
                return false;
            }
        } else {
            if (!defaultSchema.equals(other.defaultSchema)) {
                return false;
            }
        }
        if (javadoc == null) {
            if (other.javadoc!= null) {
                return false;
            }
        } else {
            if (!javadoc.equals(other.javadoc)) {
                return false;
            }
        }
        if (comments == null) {
            if (other.comments!= null) {
                return false;
            }
        } else {
            if (!comments.equals(other.comments)) {
                return false;
            }
        }
        if (commentsOnCatalogs == null) {
            if (other.commentsOnCatalogs!= null) {
                return false;
            }
        } else {
            if (!commentsOnCatalogs.equals(other.commentsOnCatalogs)) {
                return false;
            }
        }
        if (commentsOnSchemas == null) {
            if (other.commentsOnSchemas!= null) {
                return false;
            }
        } else {
            if (!commentsOnSchemas.equals(other.commentsOnSchemas)) {
                return false;
            }
        }
        if (commentsOnTables == null) {
            if (other.commentsOnTables!= null) {
                return false;
            }
        } else {
            if (!commentsOnTables.equals(other.commentsOnTables)) {
                return false;
            }
        }
        if (commentsOnColumns == null) {
            if (other.commentsOnColumns!= null) {
                return false;
            }
        } else {
            if (!commentsOnColumns.equals(other.commentsOnColumns)) {
                return false;
            }
        }
        if (commentsOnEmbeddables == null) {
            if (other.commentsOnEmbeddables!= null) {
                return false;
            }
        } else {
            if (!commentsOnEmbeddables.equals(other.commentsOnEmbeddables)) {
                return false;
            }
        }
        if (commentsOnUDTs == null) {
            if (other.commentsOnUDTs!= null) {
                return false;
            }
        } else {
            if (!commentsOnUDTs.equals(other.commentsOnUDTs)) {
                return false;
            }
        }
        if (commentsOnAttributes == null) {
            if (other.commentsOnAttributes!= null) {
                return false;
            }
        } else {
            if (!commentsOnAttributes.equals(other.commentsOnAttributes)) {
                return false;
            }
        }
        if (commentsOnPackages == null) {
            if (other.commentsOnPackages!= null) {
                return false;
            }
        } else {
            if (!commentsOnPackages.equals(other.commentsOnPackages)) {
                return false;
            }
        }
        if (commentsOnRoutines == null) {
            if (other.commentsOnRoutines!= null) {
                return false;
            }
        } else {
            if (!commentsOnRoutines.equals(other.commentsOnRoutines)) {
                return false;
            }
        }
        if (commentsOnParameters == null) {
            if (other.commentsOnParameters!= null) {
                return false;
            }
        } else {
            if (!commentsOnParameters.equals(other.commentsOnParameters)) {
                return false;
            }
        }
        if (commentsOnSequences == null) {
            if (other.commentsOnSequences!= null) {
                return false;
            }
        } else {
            if (!commentsOnSequences.equals(other.commentsOnSequences)) {
                return false;
            }
        }
        if (commentsOnDomains == null) {
            if (other.commentsOnDomains!= null) {
                return false;
            }
        } else {
            if (!commentsOnDomains.equals(other.commentsOnDomains)) {
                return false;
            }
        }
        if (commentsOnLinks == null) {
            if (other.commentsOnLinks!= null) {
                return false;
            }
        } else {
            if (!commentsOnLinks.equals(other.commentsOnLinks)) {
                return false;
            }
        }
        if (commentsOnQueues == null) {
            if (other.commentsOnQueues!= null) {
                return false;
            }
        } else {
            if (!commentsOnQueues.equals(other.commentsOnQueues)) {
                return false;
            }
        }
        if (commentsOnKeys == null) {
            if (other.commentsOnKeys!= null) {
                return false;
            }
        } else {
            if (!commentsOnKeys.equals(other.commentsOnKeys)) {
                return false;
            }
        }
        if (sources == null) {
            if (other.sources!= null) {
                return false;
            }
        } else {
            if (!sources.equals(other.sources)) {
                return false;
            }
        }
        if (sourcesOnViews == null) {
            if (other.sourcesOnViews!= null) {
                return false;
            }
        } else {
            if (!sourcesOnViews.equals(other.sourcesOnViews)) {
                return false;
            }
        }
        if (fluentSetters == null) {
            if (other.fluentSetters!= null) {
                return false;
            }
        } else {
            if (!fluentSetters.equals(other.fluentSetters)) {
                return false;
            }
        }
        if (javaBeansGettersAndSetters == null) {
            if (other.javaBeansGettersAndSetters!= null) {
                return false;
            }
        } else {
            if (!javaBeansGettersAndSetters.equals(other.javaBeansGettersAndSetters)) {
                return false;
            }
        }
        if (varargSetters == null) {
            if (other.varargSetters!= null) {
                return false;
            }
        } else {
            if (!varargSetters.equals(other.varargSetters)) {
                return false;
            }
        }
        if (generatedSerialVersionUID == null) {
            if (other.generatedSerialVersionUID!= null) {
                return false;
            }
        } else {
            if (!generatedSerialVersionUID.equals(other.generatedSerialVersionUID)) {
                return false;
            }
        }
        if (maxMembersPerInitialiser == null) {
            if (other.maxMembersPerInitialiser!= null) {
                return false;
            }
        } else {
            if (!maxMembersPerInitialiser.equals(other.maxMembersPerInitialiser)) {
                return false;
            }
        }
        if (fullyQualifiedTypes == null) {
            if (other.fullyQualifiedTypes!= null) {
                return false;
            }
        } else {
            if (!fullyQualifiedTypes.equals(other.fullyQualifiedTypes)) {
                return false;
            }
        }
        if (emptyCatalogs == null) {
            if (other.emptyCatalogs!= null) {
                return false;
            }
        } else {
            if (!emptyCatalogs.equals(other.emptyCatalogs)) {
                return false;
            }
        }
        if (emptySchemas == null) {
            if (other.emptySchemas!= null) {
                return false;
            }
        } else {
            if (!emptySchemas.equals(other.emptySchemas)) {
                return false;
            }
        }
        if (javaTimeTypes == null) {
            if (other.javaTimeTypes!= null) {
                return false;
            }
        } else {
            if (!javaTimeTypes.equals(other.javaTimeTypes)) {
                return false;
            }
        }
        if (spatialTypes == null) {
            if (other.spatialTypes!= null) {
                return false;
            }
        } else {
            if (!spatialTypes.equals(other.spatialTypes)) {
                return false;
            }
        }
        if (xmlTypes == null) {
            if (other.xmlTypes!= null) {
                return false;
            }
        } else {
            if (!xmlTypes.equals(other.xmlTypes)) {
                return false;
            }
        }
        if (jsonTypes == null) {
            if (other.jsonTypes!= null) {
                return false;
            }
        } else {
            if (!jsonTypes.equals(other.jsonTypes)) {
                return false;
            }
        }
        if (intervalTypes == null) {
            if (other.intervalTypes!= null) {
                return false;
            }
        } else {
            if (!intervalTypes.equals(other.intervalTypes)) {
                return false;
            }
        }
        if (decfloatTypes == null) {
            if (other.decfloatTypes!= null) {
                return false;
            }
        } else {
            if (!decfloatTypes.equals(other.decfloatTypes)) {
                return false;
            }
        }
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
        if (printMarginForBlockComment == null) {
            if (other.printMarginForBlockComment!= null) {
                return false;
            }
        } else {
            if (!printMarginForBlockComment.equals(other.printMarginForBlockComment)) {
                return false;
            }
        }
        if (textBlocks == null) {
            if (other.textBlocks!= null) {
                return false;
            }
        } else {
            if (!textBlocks.equals(other.textBlocks)) {
                return false;
            }
        }
        if (whereMethodOverrides == null) {
            if (other.whereMethodOverrides!= null) {
                return false;
            }
        } else {
            if (!whereMethodOverrides.equals(other.whereMethodOverrides)) {
                return false;
            }
        }
        if (renameMethodOverrides == null) {
            if (other.renameMethodOverrides!= null) {
                return false;
            }
        } else {
            if (!renameMethodOverrides.equals(other.renameMethodOverrides)) {
                return false;
            }
        }
        if (asMethodOverrides == null) {
            if (other.asMethodOverrides!= null) {
                return false;
            }
        } else {
            if (!asMethodOverrides.equals(other.asMethodOverrides)) {
                return false;
            }
        }
        if (hiddenColumnsInRecords == null) {
            if (other.hiddenColumnsInRecords!= null) {
                return false;
            }
        } else {
            if (!hiddenColumnsInRecords.equals(other.hiddenColumnsInRecords)) {
                return false;
            }
        }
        if (hiddenColumnsInPojos == null) {
            if (other.hiddenColumnsInPojos!= null) {
                return false;
            }
        } else {
            if (!hiddenColumnsInPojos.equals(other.hiddenColumnsInPojos)) {
                return false;
            }
        }
        if (hiddenColumnsInInterfaces == null) {
            if (other.hiddenColumnsInInterfaces!= null) {
                return false;
            }
        } else {
            if (!hiddenColumnsInInterfaces.equals(other.hiddenColumnsInInterfaces)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((indexes == null)? 0 :indexes.hashCode()));
        result = ((prime*result)+((relations == null)? 0 :relations.hashCode()));
        result = ((prime*result)+((sequenceFlags == null)? 0 :sequenceFlags.hashCode()));
        result = ((prime*result)+((udtPaths == null)? 0 :udtPaths.hashCode()));
        result = ((prime*result)+((implicitJoinPathsToOne == null)? 0 :implicitJoinPathsToOne.hashCode()));
        result = ((prime*result)+((implicitJoinPathsToMany == null)? 0 :implicitJoinPathsToMany.hashCode()));
        result = ((prime*result)+((implicitJoinPathsManyToMany == null)? 0 :implicitJoinPathsManyToMany.hashCode()));
        result = ((prime*result)+((implicitJoinPathTableSubtypes == null)? 0 :implicitJoinPathTableSubtypes.hashCode()));
        result = ((prime*result)+((implicitJoinPathUnusedConstructors == null)? 0 :implicitJoinPathUnusedConstructors.hashCode()));
        result = ((prime*result)+((implicitJoinPathsUseTableNameForUnambiguousFKs == null)? 0 :implicitJoinPathsUseTableNameForUnambiguousFKs.hashCode()));
        result = ((prime*result)+((implicitJoinPathsAsKotlinProperties == null)? 0 :implicitJoinPathsAsKotlinProperties.hashCode()));
        result = ((prime*result)+((deprecated == null)? 0 :deprecated.hashCode()));
        result = ((prime*result)+((deprecationOnUnknownTypes == null)? 0 :deprecationOnUnknownTypes.hashCode()));
        result = ((prime*result)+((instanceFields == null)? 0 :instanceFields.hashCode()));
        result = ((prime*result)+((visibilityModifier == null)? 0 :visibilityModifier.hashCode()));
        result = ((prime*result)+((generatedAnnotation == null)? 0 :generatedAnnotation.hashCode()));
        result = ((prime*result)+((generatedAnnotationType == null)? 0 :generatedAnnotationType.hashCode()));
        result = ((prime*result)+((generatedAnnotationDate == null)? 0 :generatedAnnotationDate.hashCode()));
        result = ((prime*result)+((generatedAnnotationJooqVersion == null)? 0 :generatedAnnotationJooqVersion.hashCode()));
        result = ((prime*result)+((nonnullAnnotation == null)? 0 :nonnullAnnotation.hashCode()));
        result = ((prime*result)+((nonnullAnnotationType == null)? 0 :nonnullAnnotationType.hashCode()));
        result = ((prime*result)+((nullableAnnotation == null)? 0 :nullableAnnotation.hashCode()));
        result = ((prime*result)+((nullableAnnotationOnWriteOnlyNullableTypes == null)? 0 :nullableAnnotationOnWriteOnlyNullableTypes.hashCode()));
        result = ((prime*result)+((nullableAnnotationType == null)? 0 :nullableAnnotationType.hashCode()));
        result = ((prime*result)+((constructorPropertiesAnnotation == null)? 0 :constructorPropertiesAnnotation.hashCode()));
        result = ((prime*result)+((constructorPropertiesAnnotationOnPojos == null)? 0 :constructorPropertiesAnnotationOnPojos.hashCode()));
        result = ((prime*result)+((constructorPropertiesAnnotationOnRecords == null)? 0 :constructorPropertiesAnnotationOnRecords.hashCode()));
        result = ((prime*result)+((routines == null)? 0 :routines.hashCode()));
        result = ((prime*result)+((sequences == null)? 0 :sequences.hashCode()));
        result = ((prime*result)+((triggers == null)? 0 :triggers.hashCode()));
        result = ((prime*result)+((synonyms == null)? 0 :synonyms.hashCode()));
        result = ((prime*result)+((udts == null)? 0 :udts.hashCode()));
        result = ((prime*result)+((queues == null)? 0 :queues.hashCode()));
        result = ((prime*result)+((links == null)? 0 :links.hashCode()));
        result = ((prime*result)+((keys == null)? 0 :keys.hashCode()));
        result = ((prime*result)+((tables == null)? 0 :tables.hashCode()));
        result = ((prime*result)+((embeddables == null)? 0 :embeddables.hashCode()));
        result = ((prime*result)+((records == null)? 0 :records.hashCode()));
        result = ((prime*result)+((recordsIncludes == null)? 0 :recordsIncludes.hashCode()));
        result = ((prime*result)+((recordsExcludes == null)? 0 :recordsExcludes.hashCode()));
        result = ((prime*result)+((recordsImplementingRecordN == null)? 0 :recordsImplementingRecordN.hashCode()));
        result = ((prime*result)+((enumsAsScalaSealedTraits == null)? 0 :enumsAsScalaSealedTraits.hashCode()));
        result = ((prime*result)+((enumsAsScalaEnums == null)? 0 :enumsAsScalaEnums.hashCode()));
        result = ((prime*result)+((pojos == null)? 0 :pojos.hashCode()));
        result = ((prime*result)+((pojosIncludes == null)? 0 :pojosIncludes.hashCode()));
        result = ((prime*result)+((pojosExcludes == null)? 0 :pojosExcludes.hashCode()));
        result = ((prime*result)+((pojosEqualsAndHashCode == null)? 0 :pojosEqualsAndHashCode.hashCode()));
        result = ((prime*result)+((pojosEqualsAndHashCodeIncludePrimaryKeyOnly == null)? 0 :pojosEqualsAndHashCodeIncludePrimaryKeyOnly.hashCode()));
        result = ((prime*result)+((pojosEqualsAndHashCodeColumnIncludeExpression == null)? 0 :pojosEqualsAndHashCodeColumnIncludeExpression.hashCode()));
        result = ((prime*result)+((pojosEqualsAndHashCodeColumnExcludeExpression == null)? 0 :pojosEqualsAndHashCodeColumnExcludeExpression.hashCode()));
        result = ((prime*result)+((pojosToString == null)? 0 :pojosToString.hashCode()));
        result = ((prime*result)+((pojosAsJavaRecordClasses == null)? 0 :pojosAsJavaRecordClasses.hashCode()));
        result = ((prime*result)+((pojosAsScalaCaseClasses == null)? 0 :pojosAsScalaCaseClasses.hashCode()));
        result = ((prime*result)+((pojosAsKotlinDataClasses == null)? 0 :pojosAsKotlinDataClasses.hashCode()));
        result = ((prime*result)+((immutablePojos == null)? 0 :immutablePojos.hashCode()));
        result = ((prime*result)+((serializablePojos == null)? 0 :serializablePojos.hashCode()));
        result = ((prime*result)+((interfaces == null)? 0 :interfaces.hashCode()));
        result = ((prime*result)+((immutableInterfaces == null)? 0 :immutableInterfaces.hashCode()));
        result = ((prime*result)+((serializableInterfaces == null)? 0 :serializableInterfaces.hashCode()));
        result = ((prime*result)+((daos == null)? 0 :daos.hashCode()));
        result = ((prime*result)+((daosIncludes == null)? 0 :daosIncludes.hashCode()));
        result = ((prime*result)+((daosExcludes == null)? 0 :daosExcludes.hashCode()));
        result = ((prime*result)+((jooqVersionReference == null)? 0 :jooqVersionReference.hashCode()));
        result = ((prime*result)+((jpaAnnotations == null)? 0 :jpaAnnotations.hashCode()));
        result = ((prime*result)+((jpaVersion == null)? 0 :jpaVersion.hashCode()));
        result = ((prime*result)+((validationAnnotations == null)? 0 :validationAnnotations.hashCode()));
        result = ((prime*result)+((springAnnotations == null)? 0 :springAnnotations.hashCode()));
        result = ((prime*result)+((springDao == null)? 0 :springDao.hashCode()));
        result = ((prime*result)+((kotlinSetterJvmNameAnnotationsOnIsPrefix == null)? 0 :kotlinSetterJvmNameAnnotationsOnIsPrefix.hashCode()));
        result = ((prime*result)+((kotlinNotNullPojoAttributes == null)? 0 :kotlinNotNullPojoAttributes.hashCode()));
        result = ((prime*result)+((kotlinNotNullRecordAttributes == null)? 0 :kotlinNotNullRecordAttributes.hashCode()));
        result = ((prime*result)+((kotlinNotNullInterfaceAttributes == null)? 0 :kotlinNotNullInterfaceAttributes.hashCode()));
        result = ((prime*result)+((kotlinDefaultedNullablePojoAttributes == null)? 0 :kotlinDefaultedNullablePojoAttributes.hashCode()));
        result = ((prime*result)+((kotlinDefaultedNullableRecordAttributes == null)? 0 :kotlinDefaultedNullableRecordAttributes.hashCode()));
        result = ((prime*result)+((globalObjectNames == null)? 0 :globalObjectNames.hashCode()));
        result = ((prime*result)+((globalObjectReferences == null)? 0 :globalObjectReferences.hashCode()));
        result = ((prime*result)+((globalCatalogReferences == null)? 0 :globalCatalogReferences.hashCode()));
        result = ((prime*result)+((globalSchemaReferences == null)? 0 :globalSchemaReferences.hashCode()));
        result = ((prime*result)+((globalDomainReferences == null)? 0 :globalDomainReferences.hashCode()));
        result = ((prime*result)+((globalTriggerReferences == null)? 0 :globalTriggerReferences.hashCode()));
        result = ((prime*result)+((globalSynonymReferences == null)? 0 :globalSynonymReferences.hashCode()));
        result = ((prime*result)+((globalTableReferences == null)? 0 :globalTableReferences.hashCode()));
        result = ((prime*result)+((globalSequenceReferences == null)? 0 :globalSequenceReferences.hashCode()));
        result = ((prime*result)+((globalUDTReferences == null)? 0 :globalUDTReferences.hashCode()));
        result = ((prime*result)+((globalRoutineReferences == null)? 0 :globalRoutineReferences.hashCode()));
        result = ((prime*result)+((globalQueueReferences == null)? 0 :globalQueueReferences.hashCode()));
        result = ((prime*result)+((globalLinkReferences == null)? 0 :globalLinkReferences.hashCode()));
        result = ((prime*result)+((globalKeyReferences == null)? 0 :globalKeyReferences.hashCode()));
        result = ((prime*result)+((globalIndexReferences == null)? 0 :globalIndexReferences.hashCode()));
        result = ((prime*result)+((defaultCatalog == null)? 0 :defaultCatalog.hashCode()));
        result = ((prime*result)+((defaultSchema == null)? 0 :defaultSchema.hashCode()));
        result = ((prime*result)+((javadoc == null)? 0 :javadoc.hashCode()));
        result = ((prime*result)+((comments == null)? 0 :comments.hashCode()));
        result = ((prime*result)+((commentsOnCatalogs == null)? 0 :commentsOnCatalogs.hashCode()));
        result = ((prime*result)+((commentsOnSchemas == null)? 0 :commentsOnSchemas.hashCode()));
        result = ((prime*result)+((commentsOnTables == null)? 0 :commentsOnTables.hashCode()));
        result = ((prime*result)+((commentsOnColumns == null)? 0 :commentsOnColumns.hashCode()));
        result = ((prime*result)+((commentsOnEmbeddables == null)? 0 :commentsOnEmbeddables.hashCode()));
        result = ((prime*result)+((commentsOnUDTs == null)? 0 :commentsOnUDTs.hashCode()));
        result = ((prime*result)+((commentsOnAttributes == null)? 0 :commentsOnAttributes.hashCode()));
        result = ((prime*result)+((commentsOnPackages == null)? 0 :commentsOnPackages.hashCode()));
        result = ((prime*result)+((commentsOnRoutines == null)? 0 :commentsOnRoutines.hashCode()));
        result = ((prime*result)+((commentsOnParameters == null)? 0 :commentsOnParameters.hashCode()));
        result = ((prime*result)+((commentsOnSequences == null)? 0 :commentsOnSequences.hashCode()));
        result = ((prime*result)+((commentsOnDomains == null)? 0 :commentsOnDomains.hashCode()));
        result = ((prime*result)+((commentsOnLinks == null)? 0 :commentsOnLinks.hashCode()));
        result = ((prime*result)+((commentsOnQueues == null)? 0 :commentsOnQueues.hashCode()));
        result = ((prime*result)+((commentsOnKeys == null)? 0 :commentsOnKeys.hashCode()));
        result = ((prime*result)+((sources == null)? 0 :sources.hashCode()));
        result = ((prime*result)+((sourcesOnViews == null)? 0 :sourcesOnViews.hashCode()));
        result = ((prime*result)+((fluentSetters == null)? 0 :fluentSetters.hashCode()));
        result = ((prime*result)+((javaBeansGettersAndSetters == null)? 0 :javaBeansGettersAndSetters.hashCode()));
        result = ((prime*result)+((varargSetters == null)? 0 :varargSetters.hashCode()));
        result = ((prime*result)+((generatedSerialVersionUID == null)? 0 :generatedSerialVersionUID.hashCode()));
        result = ((prime*result)+((maxMembersPerInitialiser == null)? 0 :maxMembersPerInitialiser.hashCode()));
        result = ((prime*result)+((fullyQualifiedTypes == null)? 0 :fullyQualifiedTypes.hashCode()));
        result = ((prime*result)+((emptyCatalogs == null)? 0 :emptyCatalogs.hashCode()));
        result = ((prime*result)+((emptySchemas == null)? 0 :emptySchemas.hashCode()));
        result = ((prime*result)+((javaTimeTypes == null)? 0 :javaTimeTypes.hashCode()));
        result = ((prime*result)+((spatialTypes == null)? 0 :spatialTypes.hashCode()));
        result = ((prime*result)+((xmlTypes == null)? 0 :xmlTypes.hashCode()));
        result = ((prime*result)+((jsonTypes == null)? 0 :jsonTypes.hashCode()));
        result = ((prime*result)+((intervalTypes == null)? 0 :intervalTypes.hashCode()));
        result = ((prime*result)+((decfloatTypes == null)? 0 :decfloatTypes.hashCode()));
        result = ((prime*result)+((newline == null)? 0 :newline.hashCode()));
        result = ((prime*result)+((indentation == null)? 0 :indentation.hashCode()));
        result = ((prime*result)+((printMarginForBlockComment == null)? 0 :printMarginForBlockComment.hashCode()));
        result = ((prime*result)+((textBlocks == null)? 0 :textBlocks.hashCode()));
        result = ((prime*result)+((whereMethodOverrides == null)? 0 :whereMethodOverrides.hashCode()));
        result = ((prime*result)+((renameMethodOverrides == null)? 0 :renameMethodOverrides.hashCode()));
        result = ((prime*result)+((asMethodOverrides == null)? 0 :asMethodOverrides.hashCode()));
        result = ((prime*result)+((hiddenColumnsInRecords == null)? 0 :hiddenColumnsInRecords.hashCode()));
        result = ((prime*result)+((hiddenColumnsInPojos == null)? 0 :hiddenColumnsInPojos.hashCode()));
        result = ((prime*result)+((hiddenColumnsInInterfaces == null)? 0 :hiddenColumnsInInterfaces.hashCode()));
        return result;
    }

}
