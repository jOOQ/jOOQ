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

fun Matchers.schemas(block: MutableList<MatchersSchemaType>.() -> Unit) {
    block(schemas)
}

@JvmName("mutableListMatchersSchemaType")
fun MutableList<MatchersSchemaType>.schema(block: MatchersSchemaType.() -> Unit) {
    val e = MatchersSchemaType()
    block(e)
    add(e)
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

fun Matchers.foreignKeys(block: MutableList<MatchersForeignKeyType>.() -> Unit) {
    block(foreignKeys)
}

@JvmName("mutableListMatchersForeignKeyType")
fun MutableList<MatchersForeignKeyType>.foreignKey(block: MatchersForeignKeyType.() -> Unit) {
    val e = MatchersForeignKeyType()
    block(e)
    add(e)
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

fun Matchers.routines(block: MutableList<MatchersRoutineType>.() -> Unit) {
    block(routines)
}

@JvmName("mutableListMatchersRoutineType")
fun MutableList<MatchersRoutineType>.routine(block: MatchersRoutineType.() -> Unit) {
    val e = MatchersRoutineType()
    block(e)
    add(e)
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

fun Matchers.enums(block: MutableList<MatchersEnumType>.() -> Unit) {
    block(enums)
}

@JvmName("mutableListMatchersEnumType")
fun MutableList<MatchersEnumType>.enum(block: MatchersEnumType.() -> Unit) {
    val e = MatchersEnumType()
    block(e)
    add(e)
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

fun SyntheticObjectsType.enums(block: MutableList<SyntheticEnumType>.() -> Unit) {
    block(enums)
}

@JvmName("mutableListSyntheticEnumType")
fun MutableList<SyntheticEnumType>.enum(block: SyntheticEnumType.() -> Unit) {
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
