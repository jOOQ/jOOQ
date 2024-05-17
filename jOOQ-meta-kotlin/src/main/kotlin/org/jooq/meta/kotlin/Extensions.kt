package org.jooq.meta.kotlin

import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target

// ----------------------------------------------------------------------------
// Extensions to the jOOQ-meta Configuration object hierarchy
// ----------------------------------------------------------------------------

fun configuration(block: Configuration.() -> Unit): Configuration {
    val c = Configuration()
    block(c)
    return c
}

// [jooq-tools] START [jooq-meta]

fun Configuration.jdbc(block: Jdbc.() -> Unit) {
    if (jdbc == null)
        jdbc = Jdbc()

    block(jdbc)
}

fun Jdbc.properties(block: MutableList<Property>.() -> Unit) {
    block(properties)
}

@JvmName("mutableListProperty")
fun MutableList<Property>.property(block: Property.() -> Unit) {
    val e = Property()
    block(e)
    add(e)
}

fun Configuration.generator(block: Generator.() -> Unit) {
    if (generator == null)
        generator = Generator()

    block(generator)
}

fun Generator.strategy(block: Strategy.() -> Unit) {
    if (strategy == null)
        strategy = Strategy()

    block(strategy)
}

fun Strategy.matchers(block: Matchers.() -> Unit) {
    if (matchers == null)
        matchers = Matchers()

    block(matchers)
}

fun Matchers.catalogs(block: MutableList<MatchersCatalogType>.() -> Unit) {
    block(catalogs)
}

@JvmName("mutableListMatchersCatalogType")
fun MutableList<MatchersCatalogType>.catalog(block: MatchersCatalogType.() -> Unit) {
    val e = MatchersCatalogType()
    block(e)
    add(e)
}

fun MatchersCatalogType.catalogClass(block: MatcherRule.() -> Unit) {
    if (catalogClass == null)
        catalogClass = MatcherRule()

    block(catalogClass)
}

fun MatchersCatalogType.catalogIdentifier(block: MatcherRule.() -> Unit) {
    if (catalogIdentifier == null)
        catalogIdentifier = MatcherRule()

    block(catalogIdentifier)
}

fun Matchers.schemas(block: MutableList<MatchersSchemaType>.() -> Unit) {
    block(schemas)
}

@JvmName("mutableListMatchersSchemaType")
fun MutableList<MatchersSchemaType>.schema(block: MatchersSchemaType.() -> Unit) {
    val e = MatchersSchemaType()
    block(e)
    add(e)
}

fun MatchersSchemaType.schemaClass(block: MatcherRule.() -> Unit) {
    if (schemaClass == null)
        schemaClass = MatcherRule()

    block(schemaClass)
}

fun MatchersSchemaType.schemaIdentifier(block: MatcherRule.() -> Unit) {
    if (schemaIdentifier == null)
        schemaIdentifier = MatcherRule()

    block(schemaIdentifier)
}

fun Matchers.tables(block: MutableList<MatchersTableType>.() -> Unit) {
    block(tables)
}

@JvmName("mutableListMatchersTableType")
fun MutableList<MatchersTableType>.table(block: MatchersTableType.() -> Unit) {
    val e = MatchersTableType()
    block(e)
    add(e)
}

fun MatchersTableType.tableClass(block: MatcherRule.() -> Unit) {
    if (tableClass == null)
        tableClass = MatcherRule()

    block(tableClass)
}

fun MatchersTableType.tableIdentifier(block: MatcherRule.() -> Unit) {
    if (tableIdentifier == null)
        tableIdentifier = MatcherRule()

    block(tableIdentifier)
}

fun MatchersTableType.pathClass(block: MatcherRule.() -> Unit) {
    if (pathClass == null)
        pathClass = MatcherRule()

    block(pathClass)
}

fun MatchersTableType.pathExtends(block: MatcherRule.() -> Unit) {
    if (pathExtends == null)
        pathExtends = MatcherRule()

    block(pathExtends)
}

fun MatchersTableType.pathImplements(block: MatcherRule.() -> Unit) {
    if (pathImplements == null)
        pathImplements = MatcherRule()

    block(pathImplements)
}

fun MatchersTableType.recordClass(block: MatcherRule.() -> Unit) {
    if (recordClass == null)
        recordClass = MatcherRule()

    block(recordClass)
}

fun MatchersTableType.interfaceClass(block: MatcherRule.() -> Unit) {
    if (interfaceClass == null)
        interfaceClass = MatcherRule()

    block(interfaceClass)
}

fun MatchersTableType.daoClass(block: MatcherRule.() -> Unit) {
    if (daoClass == null)
        daoClass = MatcherRule()

    block(daoClass)
}

fun MatchersTableType.pojoClass(block: MatcherRule.() -> Unit) {
    if (pojoClass == null)
        pojoClass = MatcherRule()

    block(pojoClass)
}

fun Matchers.indexes(block: MutableList<MatchersIndexType>.() -> Unit) {
    block(indexes)
}

@JvmName("mutableListMatchersIndexType")
fun MutableList<MatchersIndexType>.index(block: MatchersIndexType.() -> Unit) {
    val e = MatchersIndexType()
    block(e)
    add(e)
}

fun MatchersIndexType.keyIdentifier(block: MatcherRule.() -> Unit) {
    if (keyIdentifier == null)
        keyIdentifier = MatcherRule()

    block(keyIdentifier)
}

fun Matchers.primaryKeys(block: MutableList<MatchersPrimaryKeyType>.() -> Unit) {
    block(primaryKeys)
}

@JvmName("mutableListMatchersPrimaryKeyType")
fun MutableList<MatchersPrimaryKeyType>.primaryKey(block: MatchersPrimaryKeyType.() -> Unit) {
    val e = MatchersPrimaryKeyType()
    block(e)
    add(e)
}

fun MatchersPrimaryKeyType.keyIdentifier(block: MatcherRule.() -> Unit) {
    if (keyIdentifier == null)
        keyIdentifier = MatcherRule()

    block(keyIdentifier)
}

fun Matchers.uniqueKeys(block: MutableList<MatchersUniqueKeyType>.() -> Unit) {
    block(uniqueKeys)
}

@JvmName("mutableListMatchersUniqueKeyType")
fun MutableList<MatchersUniqueKeyType>.uniqueKey(block: MatchersUniqueKeyType.() -> Unit) {
    val e = MatchersUniqueKeyType()
    block(e)
    add(e)
}

fun MatchersUniqueKeyType.keyIdentifier(block: MatcherRule.() -> Unit) {
    if (keyIdentifier == null)
        keyIdentifier = MatcherRule()

    block(keyIdentifier)
}

fun Matchers.foreignKeys(block: MutableList<MatchersForeignKeyType>.() -> Unit) {
    block(foreignKeys)
}

@JvmName("mutableListMatchersForeignKeyType")
fun MutableList<MatchersForeignKeyType>.foreignKey(block: MatchersForeignKeyType.() -> Unit) {
    val e = MatchersForeignKeyType()
    block(e)
    add(e)
}

fun MatchersForeignKeyType.keyIdentifier(block: MatcherRule.() -> Unit) {
    if (keyIdentifier == null)
        keyIdentifier = MatcherRule()

    block(keyIdentifier)
}

fun MatchersForeignKeyType.pathMethodName(block: MatcherRule.() -> Unit) {
    if (pathMethodName == null)
        pathMethodName = MatcherRule()

    block(pathMethodName)
}

fun MatchersForeignKeyType.pathMethodNameInverse(block: MatcherRule.() -> Unit) {
    if (pathMethodNameInverse == null)
        pathMethodNameInverse = MatcherRule()

    block(pathMethodNameInverse)
}

fun MatchersForeignKeyType.pathMethodNameManyToMany(block: MatcherRule.() -> Unit) {
    if (pathMethodNameManyToMany == null)
        pathMethodNameManyToMany = MatcherRule()

    block(pathMethodNameManyToMany)
}

fun Matchers.fields(block: MutableList<MatchersFieldType>.() -> Unit) {
    block(fields)
}

@JvmName("mutableListMatchersFieldType")
fun MutableList<MatchersFieldType>.field(block: MatchersFieldType.() -> Unit) {
    val e = MatchersFieldType()
    block(e)
    add(e)
}

fun MatchersFieldType.fieldIdentifier(block: MatcherRule.() -> Unit) {
    if (fieldIdentifier == null)
        fieldIdentifier = MatcherRule()

    block(fieldIdentifier)
}

fun MatchersFieldType.fieldMember(block: MatcherRule.() -> Unit) {
    if (fieldMember == null)
        fieldMember = MatcherRule()

    block(fieldMember)
}

fun MatchersFieldType.fieldSetter(block: MatcherRule.() -> Unit) {
    if (fieldSetter == null)
        fieldSetter = MatcherRule()

    block(fieldSetter)
}

fun MatchersFieldType.fieldGetter(block: MatcherRule.() -> Unit) {
    if (fieldGetter == null)
        fieldGetter = MatcherRule()

    block(fieldGetter)
}

fun MatchersFieldType.daoMember(block: MatcherRule.() -> Unit) {
    if (daoMember == null)
        daoMember = MatcherRule()

    block(daoMember)
}

fun Matchers.routines(block: MutableList<MatchersRoutineType>.() -> Unit) {
    block(routines)
}

@JvmName("mutableListMatchersRoutineType")
fun MutableList<MatchersRoutineType>.routine(block: MatchersRoutineType.() -> Unit) {
    val e = MatchersRoutineType()
    block(e)
    add(e)
}

fun MatchersRoutineType.routineClass(block: MatcherRule.() -> Unit) {
    if (routineClass == null)
        routineClass = MatcherRule()

    block(routineClass)
}

fun MatchersRoutineType.routineMethod(block: MatcherRule.() -> Unit) {
    if (routineMethod == null)
        routineMethod = MatcherRule()

    block(routineMethod)
}

fun Matchers.sequences(block: MutableList<MatchersSequenceType>.() -> Unit) {
    block(sequences)
}

@JvmName("mutableListMatchersSequenceType")
fun MutableList<MatchersSequenceType>.sequence(block: MatchersSequenceType.() -> Unit) {
    val e = MatchersSequenceType()
    block(e)
    add(e)
}

fun MatchersSequenceType.sequenceIdentifier(block: MatcherRule.() -> Unit) {
    if (sequenceIdentifier == null)
        sequenceIdentifier = MatcherRule()

    block(sequenceIdentifier)
}

fun Matchers.enums(block: MutableList<MatchersEnumType>.() -> Unit) {
    block(enums)
}

@JvmName("mutableListMatchersEnumType")
fun MutableList<MatchersEnumType>.enum_(block: MatchersEnumType.() -> Unit) {
    val e = MatchersEnumType()
    block(e)
    add(e)
}

fun MatchersEnumType.enumClass(block: MatcherRule.() -> Unit) {
    if (enumClass == null)
        enumClass = MatcherRule()

    block(enumClass)
}

fun MatchersEnumType.enumLiteral(block: MatcherRule.() -> Unit) {
    if (enumLiteral == null)
        enumLiteral = MatcherRule()

    block(enumLiteral)
}

fun Matchers.embeddables(block: MutableList<MatchersEmbeddableType>.() -> Unit) {
    block(embeddables)
}

@JvmName("mutableListMatchersEmbeddableType")
fun MutableList<MatchersEmbeddableType>.embeddable(block: MatchersEmbeddableType.() -> Unit) {
    val e = MatchersEmbeddableType()
    block(e)
    add(e)
}

fun MatchersEmbeddableType.recordClass(block: MatcherRule.() -> Unit) {
    if (recordClass == null)
        recordClass = MatcherRule()

    block(recordClass)
}

fun MatchersEmbeddableType.interfaceClass(block: MatcherRule.() -> Unit) {
    if (interfaceClass == null)
        interfaceClass = MatcherRule()

    block(interfaceClass)
}

fun MatchersEmbeddableType.pojoClass(block: MatcherRule.() -> Unit) {
    if (pojoClass == null)
        pojoClass = MatcherRule()

    block(pojoClass)
}

fun Matchers.udts(block: MutableList<MatchersUDTType>.() -> Unit) {
    block(udts)
}

@JvmName("mutableListMatchersUDTType")
fun MutableList<MatchersUDTType>.udt(block: MatchersUDTType.() -> Unit) {
    val e = MatchersUDTType()
    block(e)
    add(e)
}

fun MatchersUDTType.udtClass(block: MatcherRule.() -> Unit) {
    if (udtClass == null)
        udtClass = MatcherRule()

    block(udtClass)
}

fun MatchersUDTType.udtIdentifier(block: MatcherRule.() -> Unit) {
    if (udtIdentifier == null)
        udtIdentifier = MatcherRule()

    block(udtIdentifier)
}

fun MatchersUDTType.pathClass(block: MatcherRule.() -> Unit) {
    if (pathClass == null)
        pathClass = MatcherRule()

    block(pathClass)
}

fun MatchersUDTType.recordClass(block: MatcherRule.() -> Unit) {
    if (recordClass == null)
        recordClass = MatcherRule()

    block(recordClass)
}

fun MatchersUDTType.interfaceClass(block: MatcherRule.() -> Unit) {
    if (interfaceClass == null)
        interfaceClass = MatcherRule()

    block(interfaceClass)
}

fun MatchersUDTType.pojoClass(block: MatcherRule.() -> Unit) {
    if (pojoClass == null)
        pojoClass = MatcherRule()

    block(pojoClass)
}

fun Matchers.attributes(block: MutableList<MatchersAttributeType>.() -> Unit) {
    block(attributes)
}

@JvmName("mutableListMatchersAttributeType")
fun MutableList<MatchersAttributeType>.attribute(block: MatchersAttributeType.() -> Unit) {
    val e = MatchersAttributeType()
    block(e)
    add(e)
}

fun MatchersAttributeType.attributeIdentifier(block: MatcherRule.() -> Unit) {
    if (attributeIdentifier == null)
        attributeIdentifier = MatcherRule()

    block(attributeIdentifier)
}

fun MatchersAttributeType.attributeMember(block: MatcherRule.() -> Unit) {
    if (attributeMember == null)
        attributeMember = MatcherRule()

    block(attributeMember)
}

fun MatchersAttributeType.attributeSetter(block: MatcherRule.() -> Unit) {
    if (attributeSetter == null)
        attributeSetter = MatcherRule()

    block(attributeSetter)
}

fun MatchersAttributeType.attributeGetter(block: MatcherRule.() -> Unit) {
    if (attributeGetter == null)
        attributeGetter = MatcherRule()

    block(attributeGetter)
}

fun Generator.database(block: Database.() -> Unit) {
    if (database == null)
        database = Database()

    block(database)
}

fun Database.syntheticObjects(block: SyntheticObjectsType.() -> Unit) {
    if (syntheticObjects == null)
        syntheticObjects = SyntheticObjectsType()

    block(syntheticObjects)
}

fun SyntheticObjectsType.readonlyColumns(block: MutableList<SyntheticReadonlyColumnType>.() -> Unit) {
    block(readonlyColumns)
}

@JvmName("mutableListSyntheticReadonlyColumnType")
fun MutableList<SyntheticReadonlyColumnType>.readonlyColumn(block: SyntheticReadonlyColumnType.() -> Unit) {
    val e = SyntheticReadonlyColumnType()
    block(e)
    add(e)
}

fun SyntheticObjectsType.readonlyRowids(block: MutableList<SyntheticReadonlyRowidType>.() -> Unit) {
    block(readonlyRowids)
}

@JvmName("mutableListSyntheticReadonlyRowidType")
fun MutableList<SyntheticReadonlyRowidType>.readonlyRowid(block: SyntheticReadonlyRowidType.() -> Unit) {
    val e = SyntheticReadonlyRowidType()
    block(e)
    add(e)
}

fun SyntheticObjectsType.columns(block: MutableList<SyntheticColumnType>.() -> Unit) {
    block(columns)
}

@JvmName("mutableListSyntheticColumnType")
fun MutableList<SyntheticColumnType>.column(block: SyntheticColumnType.() -> Unit) {
    val e = SyntheticColumnType()
    block(e)
    add(e)
}

fun SyntheticObjectsType.identities(block: MutableList<SyntheticIdentityType>.() -> Unit) {
    block(identities)
}

@JvmName("mutableListSyntheticIdentityType")
fun MutableList<SyntheticIdentityType>.identity(block: SyntheticIdentityType.() -> Unit) {
    val e = SyntheticIdentityType()
    block(e)
    add(e)
}

fun SyntheticObjectsType.defaults(block: MutableList<SyntheticDefaultType>.() -> Unit) {
    block(defaults)
}

@JvmName("mutableListSyntheticDefaultType")
fun MutableList<SyntheticDefaultType>.default_(block: SyntheticDefaultType.() -> Unit) {
    val e = SyntheticDefaultType()
    block(e)
    add(e)
}

fun SyntheticObjectsType.enums(block: MutableList<SyntheticEnumType>.() -> Unit) {
    block(enums)
}

@JvmName("mutableListSyntheticEnumType")
fun MutableList<SyntheticEnumType>.enum_(block: SyntheticEnumType.() -> Unit) {
    val e = SyntheticEnumType()
    block(e)
    add(e)
}

fun SyntheticObjectsType.primaryKeys(block: MutableList<SyntheticPrimaryKeyType>.() -> Unit) {
    block(primaryKeys)
}

@JvmName("mutableListSyntheticPrimaryKeyType")
fun MutableList<SyntheticPrimaryKeyType>.primaryKey(block: SyntheticPrimaryKeyType.() -> Unit) {
    val e = SyntheticPrimaryKeyType()
    block(e)
    add(e)
}

fun SyntheticObjectsType.uniqueKeys(block: MutableList<SyntheticUniqueKeyType>.() -> Unit) {
    block(uniqueKeys)
}

@JvmName("mutableListSyntheticUniqueKeyType")
fun MutableList<SyntheticUniqueKeyType>.uniqueKey(block: SyntheticUniqueKeyType.() -> Unit) {
    val e = SyntheticUniqueKeyType()
    block(e)
    add(e)
}

fun SyntheticObjectsType.foreignKeys(block: MutableList<SyntheticForeignKeyType>.() -> Unit) {
    block(foreignKeys)
}

@JvmName("mutableListSyntheticForeignKeyType")
fun MutableList<SyntheticForeignKeyType>.foreignKey(block: SyntheticForeignKeyType.() -> Unit) {
    val e = SyntheticForeignKeyType()
    block(e)
    add(e)
}

fun SyntheticObjectsType.views(block: MutableList<SyntheticViewType>.() -> Unit) {
    block(views)
}

@JvmName("mutableListSyntheticViewType")
fun MutableList<SyntheticViewType>.view(block: SyntheticViewType.() -> Unit) {
    val e = SyntheticViewType()
    block(e)
    add(e)
}

fun SyntheticObjectsType.daos(block: MutableList<SyntheticDaoType>.() -> Unit) {
    block(daos)
}

@JvmName("mutableListSyntheticDaoType")
fun MutableList<SyntheticDaoType>.dao(block: SyntheticDaoType.() -> Unit) {
    val e = SyntheticDaoType()
    block(e)
    add(e)
}

fun SyntheticDaoType.methods(block: MutableList<SyntheticDaoMethodType>.() -> Unit) {
    block(methods)
}

@JvmName("mutableListSyntheticDaoMethodType")
fun MutableList<SyntheticDaoMethodType>.method(block: SyntheticDaoMethodType.() -> Unit) {
    val e = SyntheticDaoMethodType()
    block(e)
    add(e)
}

fun Database.properties(block: MutableList<Property>.() -> Unit) {
    block(properties)
}

fun Database.comments(block: MutableList<CommentType>.() -> Unit) {
    block(comments)
}

@JvmName("mutableListCommentType")
fun MutableList<CommentType>.comment(block: CommentType.() -> Unit) {
    val e = CommentType()
    block(e)
    add(e)
}

fun Database.catalogs(block: MutableList<CatalogMappingType>.() -> Unit) {
    block(catalogs)
}

@JvmName("mutableListCatalogMappingType")
fun MutableList<CatalogMappingType>.catalog(block: CatalogMappingType.() -> Unit) {
    val e = CatalogMappingType()
    block(e)
    add(e)
}

fun CatalogMappingType.schemata(block: MutableList<SchemaMappingType>.() -> Unit) {
    block(schemata)
}

@JvmName("mutableListSchemaMappingType")
fun MutableList<SchemaMappingType>.schema(block: SchemaMappingType.() -> Unit) {
    val e = SchemaMappingType()
    block(e)
    add(e)
}

fun Database.schemata(block: MutableList<SchemaMappingType>.() -> Unit) {
    block(schemata)
}

fun Database.embeddables(block: MutableList<EmbeddableDefinitionType>.() -> Unit) {
    block(embeddables)
}

@JvmName("mutableListEmbeddableDefinitionType")
fun MutableList<EmbeddableDefinitionType>.embeddable(block: EmbeddableDefinitionType.() -> Unit) {
    val e = EmbeddableDefinitionType()
    block(e)
    add(e)
}

fun EmbeddableDefinitionType.fields(block: MutableList<EmbeddableField>.() -> Unit) {
    block(fields)
}

@JvmName("mutableListEmbeddableField")
fun MutableList<EmbeddableField>.field(block: EmbeddableField.() -> Unit) {
    val e = EmbeddableField()
    block(e)
    add(e)
}

fun Database.customTypes(block: MutableList<CustomType>.() -> Unit) {
    block(customTypes)
}

@JvmName("mutableListCustomType")
fun MutableList<CustomType>.customType(block: CustomType.() -> Unit) {
    val e = CustomType()
    block(e)
    add(e)
}

fun CustomType.lambdaConverter(block: LambdaConverter.() -> Unit) {
    if (lambdaConverter == null)
        lambdaConverter = LambdaConverter()

    block(lambdaConverter)
}

fun Database.enumTypes(block: MutableList<EnumType>.() -> Unit) {
    block(enumTypes)
}

@JvmName("mutableListEnumType")
fun MutableList<EnumType>.enumType(block: EnumType.() -> Unit) {
    val e = EnumType()
    block(e)
    add(e)
}

fun Database.forcedTypes(block: MutableList<ForcedType>.() -> Unit) {
    block(forcedTypes)
}

@JvmName("mutableListForcedType")
fun MutableList<ForcedType>.forcedType(block: ForcedType.() -> Unit) {
    val e = ForcedType()
    block(e)
    add(e)
}

fun ForcedType.lambdaConverter(block: LambdaConverter.() -> Unit) {
    if (lambdaConverter == null)
        lambdaConverter = LambdaConverter()

    block(lambdaConverter)
}

fun Generator.generate(block: Generate.() -> Unit) {
    if (generate == null)
        generate = Generate()

    block(generate)
}

fun Generator.target(block: Target.() -> Unit) {
    if (target == null)
        target = Target()

    block(target)
}

// [jooq-tools] END [jooq-meta]
