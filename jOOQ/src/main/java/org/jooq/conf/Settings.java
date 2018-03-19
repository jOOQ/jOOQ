







package org.jooq.conf;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Settings that influence the way jOOQ renders SQL code.
 *
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Settings", propOrder = {

})
@SuppressWarnings({
    "all"
})
public class Settings
    extends SettingsBase
    implements Serializable, Cloneable
{

    private final static long serialVersionUID = 31100L;
    @XmlElement(defaultValue = "true")
    protected Boolean renderCatalog = true;
    @XmlElement(defaultValue = "true")
    protected Boolean renderSchema = true;
    protected RenderMapping renderMapping;
    @XmlElement(defaultValue = "QUOTED")
    @XmlSchemaType(name = "string")
    protected RenderNameStyle renderNameStyle = RenderNameStyle.QUOTED;
    @XmlElement(defaultValue = "AS_IS")
    @XmlSchemaType(name = "string")
    protected RenderKeywordStyle renderKeywordStyle = RenderKeywordStyle.AS_IS;
    @XmlElement(defaultValue = "false")
    protected Boolean renderFormatted = false;
    protected RenderFormatting renderFormatting;
    @XmlElement(defaultValue = "false")
    protected Boolean renderScalarSubqueriesForStoredFunctions = false;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected BackslashEscaping backslashEscaping = BackslashEscaping.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected QuoteEscaping quoteEscaping = QuoteEscaping.DEFAULT;
    @XmlElement(defaultValue = "INDEXED")
    @XmlSchemaType(name = "string")
    protected ParamType paramType = ParamType.INDEXED;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected ParamCastMode paramCastMode = ParamCastMode.DEFAULT;
    @XmlElement(defaultValue = "PREPARED_STATEMENT")
    @XmlSchemaType(name = "string")
    protected StatementType statementType = StatementType.PREPARED_STATEMENT;
    @XmlElement(defaultValue = "true")
    protected Boolean executeLogging = true;
    @XmlElement(defaultValue = "false")
    protected Boolean executeWithOptimisticLocking = false;
    @XmlElement(defaultValue = "false")
    protected Boolean executeWithOptimisticLockingExcludeUnversioned = false;
    @XmlElement(defaultValue = "true")
    protected Boolean attachRecords = true;
    @XmlElement(defaultValue = "false")
    protected Boolean updatablePrimaryKeys = false;
    @XmlElement(defaultValue = "true")
    protected Boolean reflectionCaching = true;
    @XmlElement(defaultValue = "true")
    protected Boolean cacheRecordMappers = true;
    @XmlElement(defaultValue = "THROW_ALL")
    @XmlSchemaType(name = "string")
    protected ThrowExceptions throwExceptions = ThrowExceptions.THROW_ALL;
    @XmlElement(defaultValue = "true")
    protected Boolean fetchWarnings = true;
    @XmlElement(defaultValue = "0")
    protected Integer fetchServerOutputSize = 0;
    @XmlElement(defaultValue = "false")
    protected Boolean returnAllOnUpdatableRecord = false;
    @XmlElement(defaultValue = "true")
    protected Boolean returnRecordToPojo = true;
    @XmlElement(defaultValue = "true")
    protected Boolean mapJPAAnnotations = true;
    @XmlElement(defaultValue = "false")
    protected Boolean mapConstructorParameterNames = false;
    @XmlElement(defaultValue = "0")
    protected Integer queryTimeout = 0;
    @XmlElement(defaultValue = "0")
    protected Integer maxRows = 0;
    @XmlElement(defaultValue = "0")
    protected Integer fetchSize = 0;
    @XmlElement(defaultValue = "true")
    protected Boolean debugInfoOnStackTrace = true;
    @XmlElement(defaultValue = "false")
    protected Boolean inListPadding = false;
    @XmlElement(defaultValue = "2")
    protected Integer inListPadBase = 2;
    @XmlElement(defaultValue = ";")
    protected String delimiter = ";";
    @XmlElement(defaultValue = "LOG_DEBUG")
    @XmlSchemaType(name = "string")
    protected ExecuteWithoutWhere executeUpdateWithoutWhere = ExecuteWithoutWhere.LOG_DEBUG;
    @XmlElement(defaultValue = "LOG_DEBUG")
    @XmlSchemaType(name = "string")
    protected ExecuteWithoutWhere executeDeleteWithoutWhere = ExecuteWithoutWhere.LOG_DEBUG;
    @XmlElement(defaultValue = "IGNORE_ON_FAILURE")
    @XmlSchemaType(name = "string")
    protected ParseWithMetaLookups parseWithMetaLookups = ParseWithMetaLookups.IGNORE_ON_FAILURE;

    /**
     * Whether any catalog name should be rendered at all.
     * <p>
     * Use this for single-catalog environments, or when all objects are made
     * available using synonyms
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRenderCatalog() {
        return renderCatalog;
    }

    /**
     * Sets the value of the renderCatalog property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRenderCatalog(Boolean value) {
        this.renderCatalog = value;
    }

    /**
     * Whether any schema name should be rendered at all.
     * <p>
     * Setting this to false also implicitly sets "renderCatalog" to false.
     * <p>
     * Use this for single-schema environments, or when all objects are made
     * available using synonyms
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRenderSchema() {
        return renderSchema;
    }

    /**
     * Sets the value of the renderSchema property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRenderSchema(Boolean value) {
        this.renderSchema = value;
    }

    /**
     * Configure render mapping for runtime schema / table rewriting in
     * generated SQL.
     *
     * @return
     *     possible object is
     *     {@link RenderMapping }
     *
     */
    public RenderMapping getRenderMapping() {
        return renderMapping;
    }

    /**
     * Sets the value of the renderMapping property.
     *
     * @param value
     *     allowed object is
     *     {@link RenderMapping }
     *
     */
    public void setRenderMapping(RenderMapping value) {
        this.renderMapping = value;
    }

    /**
     * Whether rendered schema, table, column names, etc should be quoted
     * in rendered SQL, or transformed in any other way.
     * <p>
     * This is set to "QUOTED" by default for backwards-compatibility
     *
     * @return
     *     possible object is
     *     {@link RenderNameStyle }
     *
     */
    public RenderNameStyle getRenderNameStyle() {
        return renderNameStyle;
    }

    /**
     * Sets the value of the renderNameStyle property.
     *
     * @param value
     *     allowed object is
     *     {@link RenderNameStyle }
     *
     */
    public void setRenderNameStyle(RenderNameStyle value) {
        this.renderNameStyle = value;
    }

    /**
     * Whether SQL keywords should be rendered with upper or lower case.
     *
     * @return
     *     possible object is
     *     {@link RenderKeywordStyle }
     *
     */
    public RenderKeywordStyle getRenderKeywordStyle() {
        return renderKeywordStyle;
    }

    /**
     * Sets the value of the renderKeywordStyle property.
     *
     * @param value
     *     allowed object is
     *     {@link RenderKeywordStyle }
     *
     */
    public void setRenderKeywordStyle(RenderKeywordStyle value) {
        this.renderKeywordStyle = value;
    }

    /**
     * Whether rendered SQL should be pretty-printed.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRenderFormatted() {
        return renderFormatted;
    }

    /**
     * Sets the value of the renderFormatted property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRenderFormatted(Boolean value) {
        this.renderFormatted = value;
    }

    /**
     * All sorts of formatting flags / settings.
     *
     * @return
     *     possible object is
     *     {@link RenderFormatting }
     *
     */
    public RenderFormatting getRenderFormatting() {
        return renderFormatting;
    }

    /**
     * Sets the value of the renderFormatting property.
     *
     * @param value
     *     allowed object is
     *     {@link RenderFormatting }
     *
     */
    public void setRenderFormatting(RenderFormatting value) {
        this.renderFormatting = value;
    }

    /**
     * Whether stored function calls should be wrapped in scalar subqueries.
     * <p>
     * Oracle 11g (and potentially, other databases too) implements scalar subquery caching. With this flag
     * set to true, users can automatically profit from this feature in all SQL statements.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isRenderScalarSubqueriesForStoredFunctions() {
        return renderScalarSubqueriesForStoredFunctions;
    }

    /**
     * Sets the value of the renderScalarSubqueriesForStoredFunctions property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setRenderScalarSubqueriesForStoredFunctions(Boolean value) {
        this.renderScalarSubqueriesForStoredFunctions = value;
    }

    /**
     * Whether string literals should be escaped with backslash.
     *
     * @return
     *     possible object is
     *     {@link BackslashEscaping }
     *
     */
    public BackslashEscaping getBackslashEscaping() {
        return backslashEscaping;
    }

    /**
     * Sets the value of the backslashEscaping property.
     *
     * @param value
     *     allowed object is
     *     {@link BackslashEscaping }
     *
     */
    public void setBackslashEscaping(BackslashEscaping value) {
        this.backslashEscaping = value;
    }

    /**
     * Whether string literals should be escaped with backslash.
     *
     * @return
     *     possible object is
     *     {@link QuoteEscaping }
     *
     */
    public QuoteEscaping getQuoteEscaping() {
        return quoteEscaping;
    }

    /**
     * Sets the value of the quoteEscaping property.
     *
     * @param value
     *     allowed object is
     *     {@link QuoteEscaping }
     *
     */
    public void setQuoteEscaping(QuoteEscaping value) {
        this.quoteEscaping = value;
    }

    /**
     * Specify how bind variables are to be rendered.
     * <p>
     * Possibilities include:
     *
     * - question marks
     * - named parameters
     * - named or inlined parameters
     * - inlined parameters
     *
     * This value is overridden by statementType == STATIC_STATEMENT, in
     * case of which, this defaults to INLINED
     *
     * @return
     *     possible object is
     *     {@link ParamType }
     *
     */
    public ParamType getParamType() {
        return paramType;
    }

    /**
     * Sets the value of the paramType property.
     *
     * @param value
     *     allowed object is
     *     {@link ParamType }
     *
     */
    public void setParamType(ParamType value) {
        this.paramType = value;
    }

    /**
     * Whether rendered bind values should be cast to their respective type.
     *
     * @return
     *     possible object is
     *     {@link ParamCastMode }
     *
     */
    public ParamCastMode getParamCastMode() {
        return paramCastMode;
    }

    /**
     * Sets the value of the paramCastMode property.
     *
     * @param value
     *     allowed object is
     *     {@link ParamCastMode }
     *
     */
    public void setParamCastMode(ParamCastMode value) {
        this.paramCastMode = value;
    }

    /**
     * The type of statement that is to be executed.
     *
     * @return
     *     possible object is
     *     {@link StatementType }
     *
     */
    public StatementType getStatementType() {
        return statementType;
    }

    /**
     * Sets the value of the statementType property.
     *
     * @param value
     *     allowed object is
     *     {@link StatementType }
     *
     */
    public void setStatementType(StatementType value) {
        this.statementType = value;
    }

    /**
     * When set to true, this will add jOOQ's default logging ExecuteListeners.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isExecuteLogging() {
        return executeLogging;
    }

    /**
     * Sets the value of the executeLogging property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setExecuteLogging(Boolean value) {
        this.executeLogging = value;
    }

    /**
     * Whether store() and delete() methods should be executed with optimistic locking.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isExecuteWithOptimisticLocking() {
        return executeWithOptimisticLocking;
    }

    /**
     * Sets the value of the executeWithOptimisticLocking property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setExecuteWithOptimisticLocking(Boolean value) {
        this.executeWithOptimisticLocking = value;
    }

    /**
     * Whether store() and delete() methods should be executed with optimistic locking also on "unversioned" tables,
     * i.e. on tables that do not have a version and/or timestamp column.
     * <p>
     * This flag has no effect when "executeWithOptimisticLocking" is turned off.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isExecuteWithOptimisticLockingExcludeUnversioned() {
        return executeWithOptimisticLockingExcludeUnversioned;
    }

    /**
     * Sets the value of the executeWithOptimisticLockingExcludeUnversioned property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setExecuteWithOptimisticLockingExcludeUnversioned(Boolean value) {
        this.executeWithOptimisticLockingExcludeUnversioned = value;
    }

    /**
     * Whether fetched records should be attached to the fetching configuration.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isAttachRecords() {
        return attachRecords;
    }

    /**
     * Sets the value of the attachRecords property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setAttachRecords(Boolean value) {
        this.attachRecords = value;
    }

    /**
     * Whether primary key values are deemed to be "updatable" in jOOQ.
     * <p>
     * Setting this to "true" will allow for updating primary key values through
     * UpdatableRecord.store() and UpdatableRecord.update().
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isUpdatablePrimaryKeys() {
        return updatablePrimaryKeys;
    }

    /**
     * Sets the value of the updatablePrimaryKeys property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setUpdatablePrimaryKeys(Boolean value) {
        this.updatablePrimaryKeys = value;
    }

    /**
     * Whether reflection information should be cached in the configuration.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isReflectionCaching() {
        return reflectionCaching;
    }

    /**
     * Sets the value of the reflectionCaching property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setReflectionCaching(Boolean value) {
        this.reflectionCaching = value;
    }

    /**
     * Whether record mappers should be cached in the configuration.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isCacheRecordMappers() {
        return cacheRecordMappers;
    }

    /**
     * Sets the value of the cacheRecordMappers property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setCacheRecordMappers(Boolean value) {
        this.cacheRecordMappers = value;
    }

    /**
     * A strategy defining how exceptions from the database / JDBC driver should be propagated
     *
     * @return
     *     possible object is
     *     {@link ThrowExceptions }
     *
     */
    public ThrowExceptions getThrowExceptions() {
        return throwExceptions;
    }

    /**
     * Sets the value of the throwExceptions property.
     *
     * @param value
     *     allowed object is
     *     {@link ThrowExceptions }
     *
     */
    public void setThrowExceptions(ThrowExceptions value) {
        this.throwExceptions = value;
    }

    /**
     * Whether warnings should be fetched after each query execution.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isFetchWarnings() {
        return fetchWarnings;
    }

    /**
     * Sets the value of the fetchWarnings property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setFetchWarnings(Boolean value) {
        this.fetchWarnings = value;
    }

    /**
     * Whether server output should be fetched after each query execution.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getFetchServerOutputSize() {
        return fetchServerOutputSize;
    }

    /**
     * Sets the value of the fetchServerOutputSize property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setFetchServerOutputSize(Integer value) {
        this.fetchServerOutputSize = value;
    }

    /**
     * Whether calls to store(), insert() and update() should return all columns, not just identity columns.
     * <p>
     * Do note that only few databases support this feature. It is supported only in case the INSERT's or UPDATE's
     * RETURNING clause is fully supported, also for non-IDENTITY columns.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isReturnAllOnUpdatableRecord() {
        return returnAllOnUpdatableRecord;
    }

    /**
     * Sets the value of the returnAllOnUpdatableRecord property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setReturnAllOnUpdatableRecord(Boolean value) {
        this.returnAllOnUpdatableRecord = value;
    }

    /**
     * Whether calls to store(), insert(), update(), and delete() that are called on an UpdatableRecord
     * that is created from a POJO (e.g. in a DAO) should return all Record values to the POJO, including
     * IDENTITY values, and if <returnAllOnUpdatableRecord/> is active, also other values.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isReturnRecordToPojo() {
        return returnRecordToPojo;
    }

    /**
     * Sets the value of the returnRecordToPojo property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setReturnRecordToPojo(Boolean value) {
        this.returnRecordToPojo = value;
    }

    /**
     * Whether JPA annotations should be considered by the DefaultRecordMapper.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isMapJPAAnnotations() {
        return mapJPAAnnotations;
    }

    /**
     * Sets the value of the mapJPAAnnotations property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setMapJPAAnnotations(Boolean value) {
        this.mapJPAAnnotations = value;
    }

    /**
     * Whether constructor parameter names obtained via reflection in Java 8+ should be considered by the DefaultRecordMapper. This flag has no effect in Java 6 or 7.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isMapConstructorParameterNames() {
        return mapConstructorParameterNames;
    }

    /**
     * Sets the value of the mapConstructorParameterNames property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setMapConstructorParameterNames(Boolean value) {
        this.mapConstructorParameterNames = value;
    }

    /**
     * The default JDBC queryTimeout property that should be applied to all
     * jOOQ queries, for which no specific queryTimeout was specified.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getQueryTimeout() {
        return queryTimeout;
    }

    /**
     * Sets the value of the queryTimeout property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setQueryTimeout(Integer value) {
        this.queryTimeout = value;
    }

    /**
     * The default JDBC maxRows property that should be applied to all
     * jOOQ queries, for which no specific maxRows value was specified.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getMaxRows() {
        return maxRows;
    }

    /**
     * Sets the value of the maxRows property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setMaxRows(Integer value) {
        this.maxRows = value;
    }

    /**
     * The default JDBC fetchSize property that should be applied to all
     * jOOQ queries, for which no specific fetchSize value was specified.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getFetchSize() {
        return fetchSize;
    }

    /**
     * Sets the value of the fetchSize property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setFetchSize(Integer value) {
        this.fetchSize = value;
    }

    /**
     * [#5570] Whether exception stack traces should be enhanced with additional debug information.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isDebugInfoOnStackTrace() {
        return debugInfoOnStackTrace;
    }

    /**
     * Sets the value of the debugInfoOnStackTrace property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setDebugInfoOnStackTrace(Boolean value) {
        this.debugInfoOnStackTrace = value;
    }

    /**
     * [#5600] Whether IN lists in IN predicates should be padded to powers of inListPadBase (default 2).
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isInListPadding() {
        return inListPadding;
    }

    /**
     * Sets the value of the inListPadding property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setInListPadding(Boolean value) {
        this.inListPadding = value;
    }

    /**
     * [#7095] The base to use to calculate the powers of when applying in list padding.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getInListPadBase() {
        return inListPadBase;
    }

    /**
     * Sets the value of the inListPadBase property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setInListPadBase(Integer value) {
        this.inListPadBase = value;
    }

    /**
     * [#5826] The delimiter character to be used to delimit statements in batches.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Sets the value of the delimiter property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDelimiter(String value) {
        this.delimiter = value;
    }

    /**
     * [#6771] Specifies whether UPDATE statements are allowed to be executed lacking a WHERE clause. This has no effect on rendering the statements SQL string.
     *
     * @return
     *     possible object is
     *     {@link ExecuteWithoutWhere }
     *
     */
    public ExecuteWithoutWhere getExecuteUpdateWithoutWhere() {
        return executeUpdateWithoutWhere;
    }

    /**
     * Sets the value of the executeUpdateWithoutWhere property.
     *
     * @param value
     *     allowed object is
     *     {@link ExecuteWithoutWhere }
     *
     */
    public void setExecuteUpdateWithoutWhere(ExecuteWithoutWhere value) {
        this.executeUpdateWithoutWhere = value;
    }

    /**
     * [#6771] Specifies whether DELETE statements are allowed to be executed lacking a WHERE clause. This has no effect on rendering the statements SQL string.
     *
     * @return
     *     possible object is
     *     {@link ExecuteWithoutWhere }
     *
     */
    public ExecuteWithoutWhere getExecuteDeleteWithoutWhere() {
        return executeDeleteWithoutWhere;
    }

    /**
     * Sets the value of the executeDeleteWithoutWhere property.
     *
     * @param value
     *     allowed object is
     *     {@link ExecuteWithoutWhere }
     *
     */
    public void setExecuteDeleteWithoutWhere(ExecuteWithoutWhere value) {
        this.executeDeleteWithoutWhere = value;
    }

    /**
     * [#7163] Whether the parser should perform meta lookups in the Configuration's MetaProvider.
     *
     * @return
     *     possible object is
     *     {@link ParseWithMetaLookups }
     *
     */
    public ParseWithMetaLookups getParseWithMetaLookups() {
        return parseWithMetaLookups;
    }

    /**
     * Sets the value of the parseWithMetaLookups property.
     *
     * @param value
     *     allowed object is
     *     {@link ParseWithMetaLookups }
     *
     */
    public void setParseWithMetaLookups(ParseWithMetaLookups value) {
        this.parseWithMetaLookups = value;
    }

    public Settings withRenderCatalog(Boolean value) {
        setRenderCatalog(value);
        return this;
    }

    public Settings withRenderSchema(Boolean value) {
        setRenderSchema(value);
        return this;
    }

    public Settings withRenderMapping(RenderMapping value) {
        setRenderMapping(value);
        return this;
    }

    public Settings withRenderNameStyle(RenderNameStyle value) {
        setRenderNameStyle(value);
        return this;
    }

    public Settings withRenderKeywordStyle(RenderKeywordStyle value) {
        setRenderKeywordStyle(value);
        return this;
    }

    public Settings withRenderFormatted(Boolean value) {
        setRenderFormatted(value);
        return this;
    }

    public Settings withRenderFormatting(RenderFormatting value) {
        setRenderFormatting(value);
        return this;
    }

    public Settings withRenderScalarSubqueriesForStoredFunctions(Boolean value) {
        setRenderScalarSubqueriesForStoredFunctions(value);
        return this;
    }

    public Settings withBackslashEscaping(BackslashEscaping value) {
        setBackslashEscaping(value);
        return this;
    }

    public Settings withQuoteEscaping(QuoteEscaping value) {
        setQuoteEscaping(value);
        return this;
    }

    public Settings withParamType(ParamType value) {
        setParamType(value);
        return this;
    }

    public Settings withParamCastMode(ParamCastMode value) {
        setParamCastMode(value);
        return this;
    }

    public Settings withStatementType(StatementType value) {
        setStatementType(value);
        return this;
    }

    public Settings withExecuteLogging(Boolean value) {
        setExecuteLogging(value);
        return this;
    }

    public Settings withExecuteWithOptimisticLocking(Boolean value) {
        setExecuteWithOptimisticLocking(value);
        return this;
    }

    public Settings withExecuteWithOptimisticLockingExcludeUnversioned(Boolean value) {
        setExecuteWithOptimisticLockingExcludeUnversioned(value);
        return this;
    }

    public Settings withAttachRecords(Boolean value) {
        setAttachRecords(value);
        return this;
    }

    public Settings withUpdatablePrimaryKeys(Boolean value) {
        setUpdatablePrimaryKeys(value);
        return this;
    }

    public Settings withReflectionCaching(Boolean value) {
        setReflectionCaching(value);
        return this;
    }

    public Settings withCacheRecordMappers(Boolean value) {
        setCacheRecordMappers(value);
        return this;
    }

    public Settings withThrowExceptions(ThrowExceptions value) {
        setThrowExceptions(value);
        return this;
    }

    public Settings withFetchWarnings(Boolean value) {
        setFetchWarnings(value);
        return this;
    }

    public Settings withFetchServerOutputSize(Integer value) {
        setFetchServerOutputSize(value);
        return this;
    }

    public Settings withReturnAllOnUpdatableRecord(Boolean value) {
        setReturnAllOnUpdatableRecord(value);
        return this;
    }

    public Settings withReturnRecordToPojo(Boolean value) {
        setReturnRecordToPojo(value);
        return this;
    }

    public Settings withMapJPAAnnotations(Boolean value) {
        setMapJPAAnnotations(value);
        return this;
    }

    public Settings withMapConstructorParameterNames(Boolean value) {
        setMapConstructorParameterNames(value);
        return this;
    }

    public Settings withQueryTimeout(Integer value) {
        setQueryTimeout(value);
        return this;
    }

    public Settings withMaxRows(Integer value) {
        setMaxRows(value);
        return this;
    }

    public Settings withFetchSize(Integer value) {
        setFetchSize(value);
        return this;
    }

    public Settings withDebugInfoOnStackTrace(Boolean value) {
        setDebugInfoOnStackTrace(value);
        return this;
    }

    public Settings withInListPadding(Boolean value) {
        setInListPadding(value);
        return this;
    }

    public Settings withInListPadBase(Integer value) {
        setInListPadBase(value);
        return this;
    }

    public Settings withDelimiter(String value) {
        setDelimiter(value);
        return this;
    }

    public Settings withExecuteUpdateWithoutWhere(ExecuteWithoutWhere value) {
        setExecuteUpdateWithoutWhere(value);
        return this;
    }

    public Settings withExecuteDeleteWithoutWhere(ExecuteWithoutWhere value) {
        setExecuteDeleteWithoutWhere(value);
        return this;
    }

    public Settings withParseWithMetaLookups(ParseWithMetaLookups value) {
        setParseWithMetaLookups(value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (renderCatalog!= null) {
            sb.append("<renderCatalog>");
            sb.append(renderCatalog);
            sb.append("</renderCatalog>");
        }
        if (renderSchema!= null) {
            sb.append("<renderSchema>");
            sb.append(renderSchema);
            sb.append("</renderSchema>");
        }
        if (renderMapping!= null) {
            sb.append("<renderMapping>");
            sb.append(renderMapping);
            sb.append("</renderMapping>");
        }
        if (renderNameStyle!= null) {
            sb.append("<renderNameStyle>");
            sb.append(renderNameStyle);
            sb.append("</renderNameStyle>");
        }
        if (renderKeywordStyle!= null) {
            sb.append("<renderKeywordStyle>");
            sb.append(renderKeywordStyle);
            sb.append("</renderKeywordStyle>");
        }
        if (renderFormatted!= null) {
            sb.append("<renderFormatted>");
            sb.append(renderFormatted);
            sb.append("</renderFormatted>");
        }
        if (renderFormatting!= null) {
            sb.append("<renderFormatting>");
            sb.append(renderFormatting);
            sb.append("</renderFormatting>");
        }
        if (renderScalarSubqueriesForStoredFunctions!= null) {
            sb.append("<renderScalarSubqueriesForStoredFunctions>");
            sb.append(renderScalarSubqueriesForStoredFunctions);
            sb.append("</renderScalarSubqueriesForStoredFunctions>");
        }
        if (backslashEscaping!= null) {
            sb.append("<backslashEscaping>");
            sb.append(backslashEscaping);
            sb.append("</backslashEscaping>");
        }
        if (paramType!= null) {
            sb.append("<paramType>");
            sb.append(paramType);
            sb.append("</paramType>");
        }
        if (paramCastMode!= null) {
            sb.append("<paramCastMode>");
            sb.append(paramCastMode);
            sb.append("</paramCastMode>");
        }
        if (statementType!= null) {
            sb.append("<statementType>");
            sb.append(statementType);
            sb.append("</statementType>");
        }
        if (executeLogging!= null) {
            sb.append("<executeLogging>");
            sb.append(executeLogging);
            sb.append("</executeLogging>");
        }
        if (executeWithOptimisticLocking!= null) {
            sb.append("<executeWithOptimisticLocking>");
            sb.append(executeWithOptimisticLocking);
            sb.append("</executeWithOptimisticLocking>");
        }
        if (executeWithOptimisticLockingExcludeUnversioned!= null) {
            sb.append("<executeWithOptimisticLockingExcludeUnversioned>");
            sb.append(executeWithOptimisticLockingExcludeUnversioned);
            sb.append("</executeWithOptimisticLockingExcludeUnversioned>");
        }
        if (attachRecords!= null) {
            sb.append("<attachRecords>");
            sb.append(attachRecords);
            sb.append("</attachRecords>");
        }
        if (updatablePrimaryKeys!= null) {
            sb.append("<updatablePrimaryKeys>");
            sb.append(updatablePrimaryKeys);
            sb.append("</updatablePrimaryKeys>");
        }
        if (reflectionCaching!= null) {
            sb.append("<reflectionCaching>");
            sb.append(reflectionCaching);
            sb.append("</reflectionCaching>");
        }
        if (cacheRecordMappers!= null) {
            sb.append("<cacheRecordMappers>");
            sb.append(cacheRecordMappers);
            sb.append("</cacheRecordMappers>");
        }
        if (throwExceptions!= null) {
            sb.append("<throwExceptions>");
            sb.append(throwExceptions);
            sb.append("</throwExceptions>");
        }
        if (fetchWarnings!= null) {
            sb.append("<fetchWarnings>");
            sb.append(fetchWarnings);
            sb.append("</fetchWarnings>");
        }
        if (fetchServerOutputSize!= null) {
            sb.append("<fetchServerOutputSize>");
            sb.append(fetchServerOutputSize);
            sb.append("</fetchServerOutputSize>");
        }
        if (returnAllOnUpdatableRecord!= null) {
            sb.append("<returnAllOnUpdatableRecord>");
            sb.append(returnAllOnUpdatableRecord);
            sb.append("</returnAllOnUpdatableRecord>");
        }
        if (returnRecordToPojo!= null) {
            sb.append("<returnRecordToPojo>");
            sb.append(returnRecordToPojo);
            sb.append("</returnRecordToPojo>");
        }
        if (mapJPAAnnotations!= null) {
            sb.append("<mapJPAAnnotations>");
            sb.append(mapJPAAnnotations);
            sb.append("</mapJPAAnnotations>");
        }
        if (mapConstructorParameterNames!= null) {
            sb.append("<mapConstructorParameterNames>");
            sb.append(mapConstructorParameterNames);
            sb.append("</mapConstructorParameterNames>");
        }
        if (queryTimeout!= null) {
            sb.append("<queryTimeout>");
            sb.append(queryTimeout);
            sb.append("</queryTimeout>");
        }
        if (maxRows!= null) {
            sb.append("<maxRows>");
            sb.append(maxRows);
            sb.append("</maxRows>");
        }
        if (fetchSize!= null) {
            sb.append("<fetchSize>");
            sb.append(fetchSize);
            sb.append("</fetchSize>");
        }
        if (debugInfoOnStackTrace!= null) {
            sb.append("<debugInfoOnStackTrace>");
            sb.append(debugInfoOnStackTrace);
            sb.append("</debugInfoOnStackTrace>");
        }
        if (inListPadding!= null) {
            sb.append("<inListPadding>");
            sb.append(inListPadding);
            sb.append("</inListPadding>");
        }
        if (inListPadBase!= null) {
            sb.append("<inListPadBase>");
            sb.append(inListPadBase);
            sb.append("</inListPadBase>");
        }
        if (delimiter!= null) {
            sb.append("<delimiter>");
            sb.append(delimiter);
            sb.append("</delimiter>");
        }
        if (executeUpdateWithoutWhere!= null) {
            sb.append("<executeUpdateWithoutWhere>");
            sb.append(executeUpdateWithoutWhere);
            sb.append("</executeUpdateWithoutWhere>");
        }
        if (executeDeleteWithoutWhere!= null) {
            sb.append("<executeDeleteWithoutWhere>");
            sb.append(executeDeleteWithoutWhere);
            sb.append("</executeDeleteWithoutWhere>");
        }
        if (parseWithMetaLookups!= null) {
            sb.append("<parseWithMetaLookups>");
            sb.append(parseWithMetaLookups);
            sb.append("</parseWithMetaLookups>");
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
        Settings other = ((Settings) that);
        if (renderCatalog == null) {
            if (other.renderCatalog!= null) {
                return false;
            }
        } else {
            if (!renderCatalog.equals(other.renderCatalog)) {
                return false;
            }
        }
        if (renderSchema == null) {
            if (other.renderSchema!= null) {
                return false;
            }
        } else {
            if (!renderSchema.equals(other.renderSchema)) {
                return false;
            }
        }
        if (renderMapping == null) {
            if (other.renderMapping!= null) {
                return false;
            }
        } else {
            if (!renderMapping.equals(other.renderMapping)) {
                return false;
            }
        }
        if (renderNameStyle == null) {
            if (other.renderNameStyle!= null) {
                return false;
            }
        } else {
            if (!renderNameStyle.equals(other.renderNameStyle)) {
                return false;
            }
        }
        if (renderKeywordStyle == null) {
            if (other.renderKeywordStyle!= null) {
                return false;
            }
        } else {
            if (!renderKeywordStyle.equals(other.renderKeywordStyle)) {
                return false;
            }
        }
        if (renderFormatted == null) {
            if (other.renderFormatted!= null) {
                return false;
            }
        } else {
            if (!renderFormatted.equals(other.renderFormatted)) {
                return false;
            }
        }
        if (renderFormatting == null) {
            if (other.renderFormatting!= null) {
                return false;
            }
        } else {
            if (!renderFormatting.equals(other.renderFormatting)) {
                return false;
            }
        }
        if (renderScalarSubqueriesForStoredFunctions == null) {
            if (other.renderScalarSubqueriesForStoredFunctions!= null) {
                return false;
            }
        } else {
            if (!renderScalarSubqueriesForStoredFunctions.equals(other.renderScalarSubqueriesForStoredFunctions)) {
                return false;
            }
        }
        if (backslashEscaping == null) {
            if (other.backslashEscaping!= null) {
                return false;
            }
        } else {
            if (!backslashEscaping.equals(other.backslashEscaping)) {
                return false;
            }
        }
        if (paramType == null) {
            if (other.paramType!= null) {
                return false;
            }
        } else {
            if (!paramType.equals(other.paramType)) {
                return false;
            }
        }
        if (paramCastMode == null) {
            if (other.paramCastMode!= null) {
                return false;
            }
        } else {
            if (!paramCastMode.equals(other.paramCastMode)) {
                return false;
            }
        }
        if (statementType == null) {
            if (other.statementType!= null) {
                return false;
            }
        } else {
            if (!statementType.equals(other.statementType)) {
                return false;
            }
        }
        if (executeLogging == null) {
            if (other.executeLogging!= null) {
                return false;
            }
        } else {
            if (!executeLogging.equals(other.executeLogging)) {
                return false;
            }
        }
        if (executeWithOptimisticLocking == null) {
            if (other.executeWithOptimisticLocking!= null) {
                return false;
            }
        } else {
            if (!executeWithOptimisticLocking.equals(other.executeWithOptimisticLocking)) {
                return false;
            }
        }
        if (executeWithOptimisticLockingExcludeUnversioned == null) {
            if (other.executeWithOptimisticLockingExcludeUnversioned!= null) {
                return false;
            }
        } else {
            if (!executeWithOptimisticLockingExcludeUnversioned.equals(other.executeWithOptimisticLockingExcludeUnversioned)) {
                return false;
            }
        }
        if (attachRecords == null) {
            if (other.attachRecords!= null) {
                return false;
            }
        } else {
            if (!attachRecords.equals(other.attachRecords)) {
                return false;
            }
        }
        if (updatablePrimaryKeys == null) {
            if (other.updatablePrimaryKeys!= null) {
                return false;
            }
        } else {
            if (!updatablePrimaryKeys.equals(other.updatablePrimaryKeys)) {
                return false;
            }
        }
        if (reflectionCaching == null) {
            if (other.reflectionCaching!= null) {
                return false;
            }
        } else {
            if (!reflectionCaching.equals(other.reflectionCaching)) {
                return false;
            }
        }
        if (cacheRecordMappers == null) {
            if (other.cacheRecordMappers!= null) {
                return false;
            }
        } else {
            if (!cacheRecordMappers.equals(other.cacheRecordMappers)) {
                return false;
            }
        }
        if (throwExceptions == null) {
            if (other.throwExceptions!= null) {
                return false;
            }
        } else {
            if (!throwExceptions.equals(other.throwExceptions)) {
                return false;
            }
        }
        if (fetchWarnings == null) {
            if (other.fetchWarnings!= null) {
                return false;
            }
        } else {
            if (!fetchWarnings.equals(other.fetchWarnings)) {
                return false;
            }
        }
        if (fetchServerOutputSize == null) {
            if (other.fetchServerOutputSize!= null) {
                return false;
            }
        } else {
            if (!fetchServerOutputSize.equals(other.fetchServerOutputSize)) {
                return false;
            }
        }
        if (returnAllOnUpdatableRecord == null) {
            if (other.returnAllOnUpdatableRecord!= null) {
                return false;
            }
        } else {
            if (!returnAllOnUpdatableRecord.equals(other.returnAllOnUpdatableRecord)) {
                return false;
            }
        }
        if (returnRecordToPojo == null) {
            if (other.returnRecordToPojo!= null) {
                return false;
            }
        } else {
            if (!returnRecordToPojo.equals(other.returnRecordToPojo)) {
                return false;
            }
        }
        if (mapJPAAnnotations == null) {
            if (other.mapJPAAnnotations!= null) {
                return false;
            }
        } else {
            if (!mapJPAAnnotations.equals(other.mapJPAAnnotations)) {
                return false;
            }
        }
        if (mapConstructorParameterNames == null) {
            if (other.mapConstructorParameterNames!= null) {
                return false;
            }
        } else {
            if (!mapConstructorParameterNames.equals(other.mapConstructorParameterNames)) {
                return false;
            }
        }
        if (queryTimeout == null) {
            if (other.queryTimeout!= null) {
                return false;
            }
        } else {
            if (!queryTimeout.equals(other.queryTimeout)) {
                return false;
            }
        }
        if (maxRows == null) {
            if (other.maxRows!= null) {
                return false;
            }
        } else {
            if (!maxRows.equals(other.maxRows)) {
                return false;
            }
        }
        if (fetchSize == null) {
            if (other.fetchSize!= null) {
                return false;
            }
        } else {
            if (!fetchSize.equals(other.fetchSize)) {
                return false;
            }
        }
        if (debugInfoOnStackTrace == null) {
            if (other.debugInfoOnStackTrace!= null) {
                return false;
            }
        } else {
            if (!debugInfoOnStackTrace.equals(other.debugInfoOnStackTrace)) {
                return false;
            }
        }
        if (inListPadding == null) {
            if (other.inListPadding!= null) {
                return false;
            }
        } else {
            if (!inListPadding.equals(other.inListPadding)) {
                return false;
            }
        }
        if (inListPadBase == null) {
            if (other.inListPadBase!= null) {
                return false;
            }
        } else {
            if (!inListPadBase.equals(other.inListPadBase)) {
                return false;
            }
        }
        if (delimiter == null) {
            if (other.delimiter!= null) {
                return false;
            }
        } else {
            if (!delimiter.equals(other.delimiter)) {
                return false;
            }
        }
        if (executeUpdateWithoutWhere == null) {
            if (other.executeUpdateWithoutWhere!= null) {
                return false;
            }
        } else {
            if (!executeUpdateWithoutWhere.equals(other.executeUpdateWithoutWhere)) {
                return false;
            }
        }
        if (executeDeleteWithoutWhere == null) {
            if (other.executeDeleteWithoutWhere!= null) {
                return false;
            }
        } else {
            if (!executeDeleteWithoutWhere.equals(other.executeDeleteWithoutWhere)) {
                return false;
            }
        }
        if (parseWithMetaLookups == null) {
            if (other.parseWithMetaLookups!= null) {
                return false;
            }
        } else {
            if (!parseWithMetaLookups.equals(other.parseWithMetaLookups)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((renderCatalog == null)? 0 :renderCatalog.hashCode()));
        result = ((prime*result)+((renderSchema == null)? 0 :renderSchema.hashCode()));
        result = ((prime*result)+((renderMapping == null)? 0 :renderMapping.hashCode()));
        result = ((prime*result)+((renderNameStyle == null)? 0 :renderNameStyle.hashCode()));
        result = ((prime*result)+((renderKeywordStyle == null)? 0 :renderKeywordStyle.hashCode()));
        result = ((prime*result)+((renderFormatted == null)? 0 :renderFormatted.hashCode()));
        result = ((prime*result)+((renderFormatting == null)? 0 :renderFormatting.hashCode()));
        result = ((prime*result)+((renderScalarSubqueriesForStoredFunctions == null)? 0 :renderScalarSubqueriesForStoredFunctions.hashCode()));
        result = ((prime*result)+((backslashEscaping == null)? 0 :backslashEscaping.hashCode()));
        result = ((prime*result)+((paramType == null)? 0 :paramType.hashCode()));
        result = ((prime*result)+((paramCastMode == null)? 0 :paramCastMode.hashCode()));
        result = ((prime*result)+((statementType == null)? 0 :statementType.hashCode()));
        result = ((prime*result)+((executeLogging == null)? 0 :executeLogging.hashCode()));
        result = ((prime*result)+((executeWithOptimisticLocking == null)? 0 :executeWithOptimisticLocking.hashCode()));
        result = ((prime*result)+((executeWithOptimisticLockingExcludeUnversioned == null)? 0 :executeWithOptimisticLockingExcludeUnversioned.hashCode()));
        result = ((prime*result)+((attachRecords == null)? 0 :attachRecords.hashCode()));
        result = ((prime*result)+((updatablePrimaryKeys == null)? 0 :updatablePrimaryKeys.hashCode()));
        result = ((prime*result)+((reflectionCaching == null)? 0 :reflectionCaching.hashCode()));
        result = ((prime*result)+((cacheRecordMappers == null)? 0 :cacheRecordMappers.hashCode()));
        result = ((prime*result)+((throwExceptions == null)? 0 :throwExceptions.hashCode()));
        result = ((prime*result)+((fetchWarnings == null)? 0 :fetchWarnings.hashCode()));
        result = ((prime*result)+((fetchServerOutputSize == null)? 0 :fetchServerOutputSize.hashCode()));
        result = ((prime*result)+((returnAllOnUpdatableRecord == null)? 0 :returnAllOnUpdatableRecord.hashCode()));
        result = ((prime*result)+((returnRecordToPojo == null)? 0 :returnRecordToPojo.hashCode()));
        result = ((prime*result)+((mapJPAAnnotations == null)? 0 :mapJPAAnnotations.hashCode()));
        result = ((prime*result)+((mapConstructorParameterNames == null)? 0 :mapConstructorParameterNames.hashCode()));
        result = ((prime*result)+((queryTimeout == null)? 0 :queryTimeout.hashCode()));
        result = ((prime*result)+((maxRows == null)? 0 :maxRows.hashCode()));
        result = ((prime*result)+((fetchSize == null)? 0 :fetchSize.hashCode()));
        result = ((prime*result)+((debugInfoOnStackTrace == null)? 0 :debugInfoOnStackTrace.hashCode()));
        result = ((prime*result)+((inListPadding == null)? 0 :inListPadding.hashCode()));
        result = ((prime*result)+((inListPadBase == null)? 0 :inListPadBase.hashCode()));
        result = ((prime*result)+((delimiter == null)? 0 :delimiter.hashCode()));
        result = ((prime*result)+((executeUpdateWithoutWhere == null)? 0 :executeUpdateWithoutWhere.hashCode()));
        result = ((prime*result)+((executeDeleteWithoutWhere == null)? 0 :executeDeleteWithoutWhere.hashCode()));
        result = ((prime*result)+((parseWithMetaLookups == null)? 0 :parseWithMetaLookups.hashCode()));
        return result;
    }

}
