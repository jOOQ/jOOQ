package org.jooq.util.vertabelo;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.tools.JooqLogger;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.CatalogDefinition;
import org.jooq.util.CatalogVersionProvider;
import org.jooq.util.CheckConstraintDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.Definition;
import org.jooq.util.DomainDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.ForeignKeyDefinition;
import org.jooq.util.IdentityDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.Relations;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SchemaVersionProvider;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.UniqueKeyDefinition;
import org.jooq.util.jaxb.CustomType;
import org.jooq.util.jaxb.EnumType;
import org.jooq.util.jaxb.ForcedType;
import org.jooq.util.jaxb.RegexFlag;
import org.jooq.util.jaxb.Schema;
import org.jooq.util.vertabelo.v2_2.VertabeloDatabase_v2_2;
import org.jooq.util.vertabelo.v2_3.VertabeloDatabase_v2_3;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Base class for classes called by end user.
 * 
 * 
 * @author Rafał Strzaliński
 * @author Michał Kołodziejski
 *
 */
public abstract class BaseVertabeloDatabase implements Database {

	private static final JooqLogger log = JooqLogger.getLogger(BaseVertabeloDatabase.class);
	
	protected Properties properties;
	protected Database database;

	abstract void readXML();

	String getVersion(String xml) {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			
			Document doc = dbBuilder.parse(is);
				        Node root = doc.getElementsByTagName("DatabaseModel").item(0);

	        Node attr = root.getAttributes().getNamedItem("VersionId");
	        return attr.getNodeValue();
	        
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new RuntimeException("Error while parsing Vertabelo XML file.",e);
		}

	}

	void setVertabeloXML(String xml) {

		String version = this.getVersion(xml);
	
		log.info("Detected Vertabelo XML version: ", version);
		
		if("2.1".equals(version) || "2.2".equals(version)) {
			this.database = new VertabeloDatabase_v2_2(xml);
		} else if ("2.3".equals(version)) {
			this.database = new VertabeloDatabase_v2_3(xml);
		} else {
			throw new RuntimeException("Unsupported Vertabelo XML format. Version: " + version);
		}
	}

	// delegate section 

	public int hashCode() {
		return database.hashCode();
	}

	public boolean equals(Object obj) {
		return database.equals(obj);
	}

	public final SQLDialect getDialect() {
		return database.getDialect();
	}

	public final void setConnection(Connection connection) {
		database.setConnection(connection);
	}

	public final Connection getConnection() {
		return database.getConnection();
	}

	public final DSLContext create() {
		return database.create();
	}

	public String toString() {
		return database.toString();
	}

	public final boolean exists(org.jooq.Table<?> table) {
		return database.exists(table);
	}

	public final boolean existAll(org.jooq.Table<?>... t) {
		return database.existAll(t);
	}

	public final List<CatalogDefinition> getCatalogs() {
		return database.getCatalogs();
	}

	public final CatalogDefinition getCatalog(String inputName) {
		return database.getCatalog(inputName);
	}

	public final List<SchemaDefinition> getSchemata() {
		return database.getSchemata();
	}

	public final List<SchemaDefinition> getSchemata(CatalogDefinition catalog) {
		return database.getSchemata(catalog);
	}

	public final SchemaDefinition getSchema(String inputName) {
		return database.getSchema(inputName);
	}

	public final List<String> getInputSchemata() {
		return database.getInputSchemata();
	}

	public String getOutputSchema(String inputSchema) {
		return database.getOutputSchema(inputSchema);
	}

	public final void setConfiguredSchemata(List<Schema> schemata) {
		database.setConfiguredSchemata(schemata);
	}

	public final void setProperties(Properties properties) {
		this.properties = properties;

		// Properties should be passed via constructor.

		// This is a kind of hack.
		// We rely here on fact that setProperties is called just after
		// constructor.

		if (this.database == null) {
			readXML();
		}

		database.setProperties(properties);
	}

	public final List<Filter> getFilters() {
		return database.getFilters();
	}

	public final void addFilter(Filter filter) {
		database.addFilter(filter);
	}

	public final void setExcludes(String[] excludes) {
		database.setExcludes(excludes);
	}

	public final String[] getExcludes() {
		return database.getExcludes();
	}

	public final void setIncludes(String[] includes) {
		database.setIncludes(includes);
	}

	public final String[] getIncludes() {
		return database.getIncludes();
	}

	public final void setIncludeExcludeColumns(boolean includeExcludeColumns) {
		database.setIncludeExcludeColumns(includeExcludeColumns);
	}

	public final boolean getIncludeExcludeColumns() {
		return database.getIncludeExcludeColumns();
	}

	public final void setRegexFlags(List<RegexFlag> regexFlags) {
		database.setRegexFlags(regexFlags);
	}

	public final List<RegexFlag> getRegexFlags() {
		return database.getRegexFlags();
	}

	public void setRecordVersionFields(String[] recordVersionFields) {
		database.setRecordVersionFields(recordVersionFields);
	}

	public String[] getRecordVersionFields() {
		return database.getRecordVersionFields();
	}

	public void setRecordTimestampFields(String[] recordTimestampFields) {
		database.setRecordTimestampFields(recordTimestampFields);
	}

	public String[] getRecordTimestampFields() {
		return database.getRecordTimestampFields();
	}

	public void setSyntheticPrimaryKeys(String[] syntheticPrimaryKeys) {
		database.setSyntheticPrimaryKeys(syntheticPrimaryKeys);
	}

	public String[] getSyntheticPrimaryKeys() {
		return database.getSyntheticPrimaryKeys();
	}

	public void setOverridePrimaryKeys(String[] overridePrimaryKeys) {
		database.setOverridePrimaryKeys(overridePrimaryKeys);
	}

	public String[] getOverridePrimaryKeys() {
		return database.getOverridePrimaryKeys();
	}

	public final void setConfiguredEnumTypes(List<EnumType> configuredEnumTypes) {
		database.setConfiguredEnumTypes(configuredEnumTypes);
	}

	public final List<EnumType> getConfiguredEnumTypes() {
		return database.getConfiguredEnumTypes();
	}

	public final void setConfiguredCustomTypes(List<CustomType> configuredCustomTypes) {
		database.setConfiguredCustomTypes(configuredCustomTypes);
	}

	public final List<CustomType> getConfiguredCustomTypes() {
		return database.getConfiguredCustomTypes();
	}

	public final CustomType getConfiguredCustomType(String typeName) {
		return database.getConfiguredCustomType(typeName);
	}

	public final void setConfiguredForcedTypes(List<ForcedType> configuredForcedTypes) {
		database.setConfiguredForcedTypes(configuredForcedTypes);
	}

	public final List<ForcedType> getConfiguredForcedTypes() {
		return database.getConfiguredForcedTypes();
	}

	public final SchemaVersionProvider getSchemaVersionProvider() {
		return database.getSchemaVersionProvider();
	}

	public final void setSchemaVersionProvider(SchemaVersionProvider schemaVersionProvider) {
		database.setSchemaVersionProvider(schemaVersionProvider);
	}

	public final CatalogVersionProvider getCatalogVersionProvider() {
		return database.getCatalogVersionProvider();
	}

	public final void setCatalogVersionProvider(CatalogVersionProvider catalogVersionProvider) {
		database.setCatalogVersionProvider(catalogVersionProvider);
	}

	public final void setSupportsUnsignedTypes(boolean supportsUnsignedTypes) {
		database.setSupportsUnsignedTypes(supportsUnsignedTypes);
	}

	public final boolean supportsUnsignedTypes() {
		return database.supportsUnsignedTypes();
	}

	public final void setIgnoreProcedureReturnValues(boolean ignoreProcedureReturnValues) {
		database.setIgnoreProcedureReturnValues(ignoreProcedureReturnValues);
	}

	public final boolean ignoreProcedureReturnValues() {
		return database.ignoreProcedureReturnValues();
	}

	public final void setDateAsTimestamp(boolean dateAsTimestamp) {
		database.setDateAsTimestamp(dateAsTimestamp);
	}

	public final boolean dateAsTimestamp() {
		return database.dateAsTimestamp();
	}

	public final void setIncludeRelations(boolean includeRelations) {
		database.setIncludeRelations(includeRelations);
	}

	public final boolean includeRelations() {
		return database.includeRelations();
	}

	public final void setTableValuedFunctions(boolean tableValuedFunctions) {
		database.setTableValuedFunctions(tableValuedFunctions);
	}

	public final boolean tableValuedFunctions() {
		return database.tableValuedFunctions();
	}

	public final List<SequenceDefinition> getSequences(SchemaDefinition schema) {
		return database.getSequences(schema);
	}

	public final List<IdentityDefinition> getIdentities(SchemaDefinition schema) {
		return database.getIdentities(schema);
	}

	public final List<UniqueKeyDefinition> getUniqueKeys(SchemaDefinition schema) {
		return database.getUniqueKeys(schema);
	}

	public final List<ForeignKeyDefinition> getForeignKeys(SchemaDefinition schema) {
		return database.getForeignKeys(schema);
	}

	public final List<CheckConstraintDefinition> getCheckConstraints(SchemaDefinition schema) {
		return database.getCheckConstraints(schema);
	}

	public final List<TableDefinition> getTables(SchemaDefinition schema) {
		return database.getTables(schema);
	}

	public final TableDefinition getTable(SchemaDefinition schema, String name) {
		return database.getTable(schema, name);
	}

	public final TableDefinition getTable(SchemaDefinition schema, String name, boolean ignoreCase) {
		return database.getTable(schema, name, ignoreCase);
	}

	public final List<EnumDefinition> getEnums(SchemaDefinition schema) {
		return database.getEnums(schema);
	}

	public final ForcedType getConfiguredForcedType(Definition definition) {
		return database.getConfiguredForcedType(definition);
	}

	public final ForcedType getConfiguredForcedType(Definition definition, DataTypeDefinition definedType) {
		return database.getConfiguredForcedType(definition, definedType);
	}

	public final EnumDefinition getEnum(SchemaDefinition schema, String name) {
		return database.getEnum(schema, name);
	}

	public final EnumDefinition getEnum(SchemaDefinition schema, String name, boolean ignoreCase) {
		return database.getEnum(schema, name, ignoreCase);
	}

	public final List<DomainDefinition> getDomains(SchemaDefinition schema) {
		return database.getDomains(schema);
	}

	public final DomainDefinition getDomain(SchemaDefinition schema, String name) {
		return database.getDomain(schema, name);
	}

	public final DomainDefinition getDomain(SchemaDefinition schema, String name, boolean ignoreCase) {
		return database.getDomain(schema, name, ignoreCase);
	}

	public final List<ArrayDefinition> getArrays(SchemaDefinition schema) {
		return database.getArrays(schema);
	}

	public final ArrayDefinition getArray(SchemaDefinition schema, String name) {
		return database.getArray(schema, name);
	}

	public final ArrayDefinition getArray(SchemaDefinition schema, String name, boolean ignoreCase) {
		return database.getArray(schema, name, ignoreCase);
	}

	public final List<UDTDefinition> getUDTs(SchemaDefinition schema) {
		return database.getUDTs(schema);
	}

	public final UDTDefinition getUDT(SchemaDefinition schema, String name) {
		return database.getUDT(schema, name);
	}

	public final UDTDefinition getUDT(SchemaDefinition schema, String name, boolean ignoreCase) {
		return database.getUDT(schema, name, ignoreCase);
	}

	public final Relations getRelations() {
		return database.getRelations();
	}

	public final List<RoutineDefinition> getRoutines(SchemaDefinition schema) {
		return database.getRoutines(schema);
	}

	public final List<PackageDefinition> getPackages(SchemaDefinition schema) {
		return database.getPackages(schema);
	}

	public final <T extends Definition> List<T> filterExcludeInclude(List<T> definitions) {
		return database.filterExcludeInclude(definitions);
	}

	public final boolean isArrayType(String dataType) {
		return database.isArrayType(dataType);
	}
}
