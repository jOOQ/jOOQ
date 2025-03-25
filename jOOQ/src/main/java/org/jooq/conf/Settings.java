
package org.jooq.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jooq.SQLDialect;
import org.jooq.util.jaxb.tools.XMLAppendable;
import org.jooq.util.jaxb.tools.XMLBuilder;


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
    implements Serializable, Cloneable, XMLAppendable
{

    private final static long serialVersionUID = 31800L;
    @XmlElement(defaultValue = "true")
    protected Boolean forceIntegerTypesOnZeroScaleDecimals = true;
    @XmlElement(defaultValue = "true")
    protected Boolean renderCatalog = true;
    @XmlElement(defaultValue = "true")
    protected Boolean renderSchema = true;
    @XmlElement(defaultValue = "ALWAYS")
    @XmlSchemaType(name = "string")
    protected RenderTable renderTable = RenderTable.ALWAYS;
    protected RenderMapping renderMapping;
    @XmlElement(defaultValue = "EXPLICIT_DEFAULT_QUOTED")
    @XmlSchemaType(name = "string")
    protected RenderQuotedNames renderQuotedNames = RenderQuotedNames.EXPLICIT_DEFAULT_QUOTED;
    @XmlElement(defaultValue = "AS_IS")
    @XmlSchemaType(name = "string")
    protected RenderNameCase renderNameCase = RenderNameCase.AS_IS;
    @XmlElement(defaultValue = "QUOTED")
    @XmlSchemaType(name = "string")
    protected RenderNameStyle renderNameStyle = RenderNameStyle.QUOTED;
    @XmlElement(defaultValue = ":")
    protected String renderNamedParamPrefix = ":";
    @XmlElement(defaultValue = "AS_IS")
    @XmlSchemaType(name = "string")
    protected RenderKeywordCase renderKeywordCase = RenderKeywordCase.AS_IS;
    @XmlElement(defaultValue = "AS_IS")
    @XmlSchemaType(name = "string")
    protected RenderKeywordStyle renderKeywordStyle = RenderKeywordStyle.AS_IS;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(LocaleAdapter.class)
    protected Locale renderLocale;
    @XmlElement(defaultValue = "false")
    protected Boolean renderFormatted = false;
    protected RenderFormatting renderFormatting;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected RenderOptionalKeyword renderOptionalAssociativityParentheses = RenderOptionalKeyword.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected RenderOptionalKeyword renderOptionalAsKeywordForTableAliases = RenderOptionalKeyword.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected RenderOptionalKeyword renderOptionalAsKeywordForFieldAliases = RenderOptionalKeyword.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected RenderOptionalKeyword renderOptionalInnerKeyword = RenderOptionalKeyword.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected RenderOptionalKeyword renderOptionalOuterKeyword = RenderOptionalKeyword.DEFAULT;
    @XmlElement(defaultValue = "OFF")
    @XmlSchemaType(name = "string")
    protected RenderImplicitWindowRange renderImplicitWindowRange = RenderImplicitWindowRange.OFF;
    @XmlElement(defaultValue = "false")
    protected Boolean renderScalarSubqueriesForStoredFunctions = false;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected RenderImplicitJoinType renderImplicitJoinType = RenderImplicitJoinType.DEFAULT;
    @XmlElement(defaultValue = "IMPLICIT_NULL")
    @XmlSchemaType(name = "string")
    protected RenderDefaultNullability renderDefaultNullability = RenderDefaultNullability.IMPLICIT_NULL;
    @XmlElement(defaultValue = "false")
    protected Boolean renderCoalesceToEmptyStringInConcat = false;
    @XmlElement(defaultValue = "true")
    protected Boolean renderOrderByRownumberForEmulatedPagination = true;
    @XmlElement(defaultValue = "true")
    protected Boolean renderOutputForSQLServerReturningClause = true;
    @XmlElement(defaultValue = "true")
    protected Boolean renderGroupConcatMaxLenSessionVariable = true;
    @XmlElement(defaultValue = "false")
    protected Boolean renderParenthesisAroundSetOperationQueries = false;
    @XmlElement(defaultValue = "true")
    protected Boolean renderVariablesInDerivedTablesForEmulations = true;
    @XmlElement(defaultValue = "true")
    protected Boolean renderRowConditionForSeekClause = true;
    @XmlElement(defaultValue = "false")
    protected Boolean renderRedundantConditionForSeekClause = false;
    @XmlElement(defaultValue = "false")
    protected Boolean renderPlainSQLTemplatesAsRaw = false;
    @XmlElement(defaultValue = ".")
    protected String namePathSeparator = ".";
    @XmlElement(defaultValue = "false")
    protected Boolean bindOffsetDateTimeType = false;
    @XmlElement(defaultValue = "false")
    protected Boolean bindOffsetTimeType = false;
    @XmlElement(defaultValue = "true")
    protected Boolean fetchTriggerValuesAfterSQLServerOutput = true;
    @XmlElement(defaultValue = "WHEN_RESULT_REQUESTED")
    @XmlSchemaType(name = "string")
    protected FetchIntermediateResult fetchIntermediateResult = FetchIntermediateResult.WHEN_RESULT_REQUESTED;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsDuplicateStatements = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsDuplicateStatementsUsingTransformPatterns = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsMissingWasNullCall = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsRepeatedStatements = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsConsecutiveAggregation = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsConcatenationInPredicate = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsPossiblyWrongExpression = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsTooManyColumnsFetched = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsTooManyRowsFetched = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsUnnecessaryWasNullCall = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsPatterns = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsTrivialCondition = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsNullCondition = true;
    @XmlElement(defaultValue = "false")
    protected Boolean transformPatterns = false;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsLogging = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsUnnecessaryDistinct = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsUnnecessaryScalarSubquery = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsUnnecessaryInnerJoin = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsUnnecessaryGroupByExpressions = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsUnnecessaryOrderByExpressions = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsUnnecessaryExistsSubqueryClauses = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsCountConstant = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsTrim = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNotAnd = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNotOr = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNotNot = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNotComparison = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNotNotDistinct = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsDistinctFromNull = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNormaliseAssociativeOps = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNormaliseInListSingleElementToComparison = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNormaliseFieldCompareValue = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNormaliseCoalesceToNvl = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsOrEqToIn = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsAndNeToNotIn = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsMergeOrComparison = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsMergeAndComparison = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsMergeInLists = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsMergeRangePredicates = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsMergeBetweenSymmetricPredicates = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsCaseSearchedToCaseSimple = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsCaseElseNull = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsUnreachableCaseClauses = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsUnreachableDecodeClauses = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsCaseDistinctToDecode = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsCaseMergeWhenWhen = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsCaseMergeWhenElse = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsCaseToCaseAbbreviation = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsSimplifyCaseAbbreviation = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsFlattenCaseAbbreviation = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsFlattenDecode = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsFlattenCase = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsTrivialCaseAbbreviation = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsTrivialPredicates = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsTrivialBitwiseOperations = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsBitSet = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsBitGet = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsScalarSubqueryCountAsteriskGtZero = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsScalarSubqueryCountExpressionGtZero = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsEmptyScalarSubquery = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNegNeg = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsBitNotBitNot = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsBitNotBitNand = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsBitNotBitNor = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsBitNotBitXNor = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsNullOnNullInput = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsIdempotentFunctionRepetition = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsArithmeticComparisons = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsArithmeticExpressions = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsTrigonometricFunctions = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsLogarithmicFunctions = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsHyperbolicFunctions = true;
    @XmlElement(defaultValue = "true")
    protected Boolean transformPatternsInverseHyperbolicFunctions = true;
    @XmlElement(defaultValue = "false")
    protected Boolean transformInlineBindValuesForFieldComparisons = false;
    @XmlElement(defaultValue = "false")
    protected Boolean transformAnsiJoinToTableLists = false;
    @XmlElement(defaultValue = "WHEN_NEEDED")
    @XmlSchemaType(name = "string")
    protected Transformation transformInConditionSubqueryWithLimitToDerivedTable = Transformation.WHEN_NEEDED;
    @XmlElement(defaultValue = "WHEN_NEEDED")
    @XmlSchemaType(name = "string")
    protected Transformation transformQualify = Transformation.WHEN_NEEDED;
    @XmlElement(defaultValue = "false")
    protected Boolean transformTableListsToAnsiJoin = false;
    @XmlElement(defaultValue = "NEVER")
    @XmlSchemaType(name = "string")
    protected Transformation transformRownum = Transformation.NEVER;
    @XmlElement(defaultValue = "NEVER")
    @XmlSchemaType(name = "string")
    protected TransformUnneededArithmeticExpressions transformUnneededArithmeticExpressions = TransformUnneededArithmeticExpressions.NEVER;
    @XmlElement(defaultValue = "WHEN_NEEDED")
    @XmlSchemaType(name = "string")
    protected Transformation transformGroupByColumnIndex = Transformation.WHEN_NEEDED;
    @XmlElement(defaultValue = "WHEN_NEEDED")
    @XmlSchemaType(name = "string")
    protected Transformation transformInlineCTE = Transformation.WHEN_NEEDED;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected BackslashEscaping backslashEscaping = BackslashEscaping.DEFAULT;
    @XmlElement(defaultValue = "INDEXED")
    @XmlSchemaType(name = "string")
    protected ParamType paramType = ParamType.INDEXED;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected ParamCastMode paramCastMode = ParamCastMode.DEFAULT;
    @XmlElement(defaultValue = "PREPARED_STATEMENT")
    @XmlSchemaType(name = "string")
    protected StatementType statementType = StatementType.PREPARED_STATEMENT;
    @XmlElement(defaultValue = "0")
    protected Integer inlineThreshold = 0;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InvocationOrder transactionListenerStartInvocationOrder = InvocationOrder.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InvocationOrder transactionListenerEndInvocationOrder = InvocationOrder.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InvocationOrder migrationListenerStartInvocationOrder = InvocationOrder.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InvocationOrder migrationListenerEndInvocationOrder = InvocationOrder.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InvocationOrder visitListenerStartInvocationOrder = InvocationOrder.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InvocationOrder visitListenerEndInvocationOrder = InvocationOrder.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InvocationOrder recordListenerStartInvocationOrder = InvocationOrder.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InvocationOrder recordListenerEndInvocationOrder = InvocationOrder.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InvocationOrder executeListenerStartInvocationOrder = InvocationOrder.DEFAULT;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InvocationOrder executeListenerEndInvocationOrder = InvocationOrder.DEFAULT;
    @XmlElement(defaultValue = "true")
    protected Boolean executeLogging = true;
    @XmlElement(defaultValue = "true")
    protected Boolean executeLoggingSQLExceptions = true;
    @XmlElement(defaultValue = "true")
    protected Boolean diagnosticsLogging = true;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected DiagnosticsConnection diagnosticsConnection = DiagnosticsConnection.DEFAULT;
    @XmlElement(defaultValue = "true")
    protected Boolean updateRecordVersion = true;
    @XmlElement(defaultValue = "true")
    protected Boolean updateRecordTimestamp = true;
    @XmlElement(defaultValue = "false")
    protected Boolean executeWithOptimisticLocking = false;
    @XmlElement(defaultValue = "false")
    protected Boolean executeWithOptimisticLockingExcludeUnversioned = false;
    @XmlElement(defaultValue = "true")
    protected Boolean attachRecords = true;
    @XmlElement(defaultValue = "true")
    protected Boolean insertUnchangedRecords = true;
    @XmlElement(defaultValue = "NEVER")
    @XmlSchemaType(name = "string")
    protected UpdateUnchangedRecords updateUnchangedRecords = UpdateUnchangedRecords.NEVER;
    @XmlElement(defaultValue = "false")
    protected Boolean updatablePrimaryKeys = false;
    @XmlElement(defaultValue = "true")
    protected Boolean reflectionCaching = true;
    @XmlElement(defaultValue = "true")
    protected Boolean cacheRecordMappers = true;
    @XmlElement(defaultValue = "true")
    protected Boolean cacheParsingConnection = true;
    @XmlElement(defaultValue = "8192")
    protected Integer cacheParsingConnectionLRUCacheSize = 8192;
    @XmlElement(defaultValue = "true")
    protected Boolean cachePreparedStatementInLoader = true;
    @XmlElement(defaultValue = "THROW_ALL")
    @XmlSchemaType(name = "string")
    protected ThrowExceptions throwExceptions = ThrowExceptions.THROW_ALL;
    @XmlElement(defaultValue = "true")
    protected Boolean fetchWarnings = true;
    @XmlElement(defaultValue = "0")
    protected Integer fetchServerOutputSize = 0;
    @XmlElement(defaultValue = "true")
    protected Boolean returnIdentityOnUpdatableRecord = true;
    @XmlElement(defaultValue = "false")
    protected Boolean returnDefaultOnUpdatableRecord = false;
    @XmlElement(defaultValue = "false")
    protected Boolean returnComputedOnUpdatableRecord = false;
    @XmlElement(defaultValue = "false")
    protected Boolean returnAllOnUpdatableRecord = false;
    @XmlElement(defaultValue = "true")
    protected Boolean returnRecordToPojo = true;
    @XmlElement(defaultValue = "true")
    protected Boolean mapJPAAnnotations = true;
    @XmlElement(defaultValue = "false")
    protected Boolean mapRecordComponentParameterNames = false;
    @XmlElement(defaultValue = "true")
    protected Boolean mapConstructorPropertiesParameterNames = true;
    @XmlElement(defaultValue = "false")
    protected Boolean mapConstructorParameterNames = false;
    @XmlElement(defaultValue = "true")
    protected Boolean mapConstructorParameterNamesInKotlin = true;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected QueryPoolable queryPoolable = QueryPoolable.DEFAULT;
    @XmlElement(defaultValue = "0")
    protected Integer queryTimeout = 0;
    @XmlElement(defaultValue = "0")
    protected Integer maxRows = 0;
    @XmlElement(defaultValue = "0")
    protected Integer fetchSize = 0;
    @XmlElement(defaultValue = "2147483647")
    protected Integer batchSize = 2147483647;
    @XmlElement(defaultValue = "true")
    protected Boolean debugInfoOnStackTrace = true;
    @XmlElement(defaultValue = "false")
    protected Boolean inListPadding = false;
    @XmlElement(defaultValue = "2")
    protected Integer inListPadBase = 2;
    @XmlElement(defaultValue = ";")
    protected String delimiter = ";";
    @XmlElement(defaultValue = "false")
    protected Boolean emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly = false;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected NestedCollectionEmulation emulateMultiset = NestedCollectionEmulation.DEFAULT;
    @XmlElement(defaultValue = "false")
    protected Boolean emulateComputedColumns = false;
    @XmlElement(defaultValue = "LOG_DEBUG")
    @XmlSchemaType(name = "string")
    protected ExecuteWithoutWhere executeUpdateWithoutWhere = ExecuteWithoutWhere.LOG_DEBUG;
    @XmlElement(defaultValue = "LOG_DEBUG")
    @XmlSchemaType(name = "string")
    protected ExecuteWithoutWhere executeDeleteWithoutWhere = ExecuteWithoutWhere.LOG_DEBUG;
    @XmlElement(type = String.class, defaultValue = "DEFAULT")
    @XmlJavaTypeAdapter(SQLDialectAdapter.class)
    protected SQLDialect interpreterDialect;
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected InterpreterNameLookupCaseSensitivity interpreterNameLookupCaseSensitivity = InterpreterNameLookupCaseSensitivity.DEFAULT;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(LocaleAdapter.class)
    protected Locale interpreterLocale;
    @XmlElement(defaultValue = "false")
    protected Boolean interpreterDelayForeignKeyDeclarations = false;
    @XmlElement(defaultValue = "false")
    protected Boolean metaIncludeSystemIndexes = false;
    @XmlElement(defaultValue = "false")
    protected Boolean metaIncludeSystemSequences = false;
    @XmlElement(defaultValue = "false")
    protected Boolean migrationAllowsUndo = false;
    @XmlElement(defaultValue = "false")
    protected Boolean migrationRevertUntracked = false;
    @XmlElement(defaultValue = "false")
    protected Boolean migrationAutoBaseline = false;
    @XmlElement(defaultValue = "true")
    protected Boolean migrationAutoValidation = true;
    @XmlElement(defaultValue = "true")
    protected Boolean migrationIgnoreDefaultTimestampPrecisionDiffs = true;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(LocaleAdapter.class)
    protected Locale locale;
    @XmlElement(type = String.class, defaultValue = "DEFAULT")
    @XmlJavaTypeAdapter(SQLDialectAdapter.class)
    protected SQLDialect parseDialect;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(LocaleAdapter.class)
    protected Locale parseLocale;
    @XmlElement(defaultValue = "YYYY-MM-DD")
    protected String parseDateFormat = "YYYY-MM-DD";
    @XmlElement(defaultValue = "YYYY-MM-DD HH24:MI:SS.FF")
    protected String parseTimestampFormat = "YYYY-MM-DD HH24:MI:SS.FF";
    @XmlElement(defaultValue = ":")
    protected String parseNamedParamPrefix = ":";
    @XmlElement(defaultValue = "DEFAULT")
    @XmlSchemaType(name = "string")
    protected ParseNameCase parseNameCase = ParseNameCase.DEFAULT;
    @XmlElement(defaultValue = "OFF")
    @XmlSchemaType(name = "string")
    protected ParseWithMetaLookups parseWithMetaLookups = ParseWithMetaLookups.OFF;
    @XmlElement(defaultValue = "WHEN_NEEDED")
    @XmlSchemaType(name = "string")
    protected Transformation parseAppendMissingTableReferences = Transformation.WHEN_NEEDED;
    @XmlElement(defaultValue = "false")
    protected Boolean parseSetCommands = false;
    @XmlElement(defaultValue = "IGNORE")
    @XmlSchemaType(name = "string")
    protected ParseUnsupportedSyntax parseUnsupportedSyntax = ParseUnsupportedSyntax.IGNORE;
    @XmlElement(defaultValue = "FAIL")
    @XmlSchemaType(name = "string")
    protected ParseUnknownFunctions parseUnknownFunctions = ParseUnknownFunctions.FAIL;
    @XmlElement(defaultValue = "false")
    protected Boolean parseIgnoreCommercialOnlyFeatures = false;
    @XmlElement(defaultValue = "false")
    protected Boolean parseIgnoreComments = false;
    @XmlElement(defaultValue = "[jooq ignore start]")
    protected String parseIgnoreCommentStart = "[jooq ignore start]";
    @XmlElement(defaultValue = "[jooq ignore stop]")
    protected String parseIgnoreCommentStop = "[jooq ignore stop]";
    @XmlElement(defaultValue = "false")
    protected Boolean parseRetainCommentsBetweenQueries = false;
    @XmlElement(defaultValue = "true")
    protected Boolean parseMetaDefaultExpressions = true;
    @XmlElement(defaultValue = "IGNORE")
    @XmlSchemaType(name = "string")
    protected WriteIfReadonly readonlyTableRecordInsert = WriteIfReadonly.IGNORE;
    @XmlElement(defaultValue = "IGNORE")
    @XmlSchemaType(name = "string")
    protected WriteIfReadonly readonlyUpdatableRecordUpdate = WriteIfReadonly.IGNORE;
    @XmlElement(defaultValue = "IGNORE")
    @XmlSchemaType(name = "string")
    protected WriteIfReadonly readonlyInsert = WriteIfReadonly.IGNORE;
    @XmlElement(defaultValue = "IGNORE")
    @XmlSchemaType(name = "string")
    protected WriteIfReadonly readonlyUpdate = WriteIfReadonly.IGNORE;
    @XmlElement(defaultValue = "true")
    protected Boolean applyWorkaroundFor7962 = true;
    @XmlElementWrapper(name = "interpreterSearchPath")
    @XmlElement(name = "schema")
    protected List<InterpreterSearchSchema> interpreterSearchPath;
    @XmlElementWrapper(name = "migrationSchemata")
    @XmlElement(name = "schema")
    protected List<MigrationSchema> migrationSchemata;
    @XmlElementWrapper(name = "parseSearchPath")
    @XmlElement(name = "schema")
    protected List<ParseSearchSchema> parseSearchPath;

    /**
     * Historically, zero-scale decimal types are generated as their most appropriate, corresponding integer type (e.g. NUMBER(2, 0) and less: Byte). The same behaviour is replicated in the {@link org.jooq.Meta} API. This flag allows for turning off this feature.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isForceIntegerTypesOnZeroScaleDecimals() {
        return forceIntegerTypesOnZeroScaleDecimals;
    }

    /**
     * Historically, zero-scale decimal types are generated as their most appropriate, corresponding integer type (e.g. NUMBER(2, 0) and less: Byte). The same behaviour is replicated in the {@link org.jooq.Meta} API. This flag allows for turning off this feature.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setForceIntegerTypesOnZeroScaleDecimals(Boolean value) {
        this.forceIntegerTypesOnZeroScaleDecimals = value;
    }

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
     * Whether any catalog name should be rendered at all.
     * <p>
     * Use this for single-catalog environments, or when all objects are made
     * available using synonyms
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
     * Whether any schema name should be rendered at all.
     * <p>
     * Setting this to false also implicitly sets "renderCatalog" to false.
     * <p>
     * Use this for single-schema environments, or when all objects are made
     * available using synonyms
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
     * Whether any table name qualification should be rendered at all on columns.
     * <p>
     * Setting when tables aren't rendered, then implicitly, schemas and catalogs aren't rendered either.
     * <p>
     * The following values are available:
     * <ul>
     * <li>{@link RenderTable#ALWAYS}: The default, which should always be preferred. Columns are always qualified with their tables, where possible.</li>
     * <li>{@link RenderTable#WHEN_MULTIPLE_TABLES}: The simplest option to reduce generated query verbosity, avoiding table qualification only in queries with a single table in the <code>FROM</code> clause.</li>
     * <li>{@link RenderTable#WHEN_AMBIGUOUS_COLUMNS}: A much more expensive to compute option that checks the <code>FROM</code> clause for ambiguous column names, in case of which columns are qualified.</li>
     * <li>{@link RenderTable#NEVER}: Always turn off table qualification.</li>
     * </ul>
     * <p>
     * Use this when verbosity of rendered SQL is a problem.
     * 
     */
    public RenderTable getRenderTable() {
        return renderTable;
    }

    /**
     * Whether any table name qualification should be rendered at all on columns.
     * <p>
     * Setting when tables aren't rendered, then implicitly, schemas and catalogs aren't rendered either.
     * <p>
     * The following values are available:
     * <ul>
     * <li>{@link RenderTable#ALWAYS}: The default, which should always be preferred. Columns are always qualified with their tables, where possible.</li>
     * <li>{@link RenderTable#WHEN_MULTIPLE_TABLES}: The simplest option to reduce generated query verbosity, avoiding table qualification only in queries with a single table in the <code>FROM</code> clause.</li>
     * <li>{@link RenderTable#WHEN_AMBIGUOUS_COLUMNS}: A much more expensive to compute option that checks the <code>FROM</code> clause for ambiguous column names, in case of which columns are qualified.</li>
     * <li>{@link RenderTable#NEVER}: Always turn off table qualification.</li>
     * </ul>
     * <p>
     * Use this when verbosity of rendered SQL is a problem.
     * 
     */
    public void setRenderTable(RenderTable value) {
        this.renderTable = value;
    }

    /**
     * Configure render mapping for runtime schema / table rewriting in
     * generated SQL.
     * 
     */
    public RenderMapping getRenderMapping() {
        return renderMapping;
    }

    /**
     * Configure render mapping for runtime schema / table rewriting in
     * generated SQL.
     * 
     */
    public void setRenderMapping(RenderMapping value) {
        this.renderMapping = value;
    }

    /**
     * Whether rendered schema, table, column names, etc should be quoted.
     * <p>
     * This only affects names created through {@link org.jooq.impl.DSL#name(String)} methods (including those that are implicitly created through this method), not {@link org.jooq.impl.DSL#quotedName(String)} or {@link org.jooq.impl.DSL#unquotedName(String)}, whose behaviour cannot be overridden.
     * <p>
     * This setting does not affect any plain SQL usage.
     * 
     */
    public RenderQuotedNames getRenderQuotedNames() {
        return renderQuotedNames;
    }

    /**
     * Whether rendered schema, table, column names, etc should be quoted.
     * <p>
     * This only affects names created through {@link org.jooq.impl.DSL#name(String)} methods (including those that are implicitly created through this method), not {@link org.jooq.impl.DSL#quotedName(String)} or {@link org.jooq.impl.DSL#unquotedName(String)}, whose behaviour cannot be overridden.
     * <p>
     * This setting does not affect any plain SQL usage.
     * 
     */
    public void setRenderQuotedNames(RenderQuotedNames value) {
        this.renderQuotedNames = value;
    }

    /**
     * Whether the case of {@link org.jooq.Name} references should be modified in any way.
     * <p>
     * Names are modified irrespective of the {@link #getRenderQuotedNames()} setting.
     * <p>
     * This setting does not affect any plain SQL usage.
     * 
     */
    public RenderNameCase getRenderNameCase() {
        return renderNameCase;
    }

    /**
     * Whether the case of {@link org.jooq.Name} references should be modified in any way.
     * <p>
     * Names are modified irrespective of the {@link #getRenderQuotedNames()} setting.
     * <p>
     * This setting does not affect any plain SQL usage.
     * 
     */
    public void setRenderNameCase(RenderNameCase value) {
        this.renderNameCase = value;
    }

    /**
     * Whether rendered schema, table, column names, etc should be quoted
     * in rendered SQL, or transformed in any other way.
     * <p>
     * This is set to "QUOTED" by default for backwards-compatibility.
     * <p>
     * @deprecated - 3.12.0 - [#5909] - Use {@link RenderQuotedNames} and {@link RenderNameCase} instead.
     * 
     */
    @Deprecated
    public RenderNameStyle getRenderNameStyle() {
        return renderNameStyle;
    }

    /**
     * Whether rendered schema, table, column names, etc should be quoted
     * in rendered SQL, or transformed in any other way.
     * <p>
     * This is set to "QUOTED" by default for backwards-compatibility.
     * <p>
     * @deprecated - 3.12.0 - [#5909] - Use {@link RenderQuotedNames} and {@link RenderNameCase} instead.
     * 
     */
    @Deprecated
    public void setRenderNameStyle(RenderNameStyle value) {
        this.renderNameStyle = value;
    }

    /**
     * The prefix to use for named parameters in generated SQL.
     * <p>
     * Named parameter syntax defaults to <code>:name</code> (such as supported by Oracle, JPA, Spring), but 
     * vendor specific parameters may look differently. This flag can be used to determine the prefix to be
     * used by named parameters, such as <code>@</code> for SQL Server's <code>@name</code> or <code>$</code>
     * for PostgreSQL's <code>$name</code>, when generating SQL.
     * <p>
     * "Named indexed" parameters can be obtained in the same way by specifingy {@code ParamType#NAMED} and not
     * providing a name to parameters, resulting in <code>:1</code> or <code>@1</code> or <code>$1</code>, etc.
     * 
     */
    public String getRenderNamedParamPrefix() {
        return renderNamedParamPrefix;
    }

    /**
     * The prefix to use for named parameters in generated SQL.
     * <p>
     * Named parameter syntax defaults to <code>:name</code> (such as supported by Oracle, JPA, Spring), but 
     * vendor specific parameters may look differently. This flag can be used to determine the prefix to be
     * used by named parameters, such as <code>@</code> for SQL Server's <code>@name</code> or <code>$</code>
     * for PostgreSQL's <code>$name</code>, when generating SQL.
     * <p>
     * "Named indexed" parameters can be obtained in the same way by specifingy {@code ParamType#NAMED} and not
     * providing a name to parameters, resulting in <code>:1</code> or <code>@1</code> or <code>$1</code>, etc.
     * 
     */
    public void setRenderNamedParamPrefix(String value) {
        this.renderNamedParamPrefix = value;
    }

    /**
     * Whether the case of {@link org.jooq.Keyword} references should be modified in any way.
     * 
     */
    public RenderKeywordCase getRenderKeywordCase() {
        return renderKeywordCase;
    }

    /**
     * Whether the case of {@link org.jooq.Keyword} references should be modified in any way.
     * 
     */
    public void setRenderKeywordCase(RenderKeywordCase value) {
        this.renderKeywordCase = value;
    }

    /**
     * Whether the case of {@link org.jooq.Keyword} references should be modified in any way.
     * <p>
     * @deprecated - 3.12.0 - [#5909] - Use {@link RenderKeywordCase} instead.
     * 
     */
    @Deprecated
    public RenderKeywordStyle getRenderKeywordStyle() {
        return renderKeywordStyle;
    }

    /**
     * Whether the case of {@link org.jooq.Keyword} references should be modified in any way.
     * <p>
     * @deprecated - 3.12.0 - [#5909] - Use {@link RenderKeywordCase} instead.
     * 
     */
    @Deprecated
    public void setRenderKeywordStyle(RenderKeywordStyle value) {
        this.renderKeywordStyle = value;
    }

    /**
     * The Locale to be used with any render locale dependent logic (as e.g. transforming names to lower / uppper case), defaulting to {@link #getLocale()}.
     * 
     */
    public Locale getRenderLocale() {
        return renderLocale;
    }

    /**
     * The Locale to be used with any render locale dependent logic (as e.g. transforming names to lower / uppper case), defaulting to {@link #getLocale()}.
     * 
     */
    public void setRenderLocale(Locale value) {
        this.renderLocale = value;
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
     * Whether rendered SQL should be pretty-printed.
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
     */
    public RenderFormatting getRenderFormatting() {
        return renderFormatting;
    }

    /**
     * All sorts of formatting flags / settings.
     * 
     */
    public void setRenderFormatting(RenderFormatting value) {
        this.renderFormatting = value;
    }

    /**
     * Whether to render optional parentheses to make associativity explicit, e.g. <code>((a + b) + c)</code> instead of (a + b + c).
     * 
     */
    public RenderOptionalKeyword getRenderOptionalAssociativityParentheses() {
        return renderOptionalAssociativityParentheses;
    }

    /**
     * Whether to render optional parentheses to make associativity explicit, e.g. <code>((a + b) + c)</code> instead of (a + b + c).
     * 
     */
    public void setRenderOptionalAssociativityParentheses(RenderOptionalKeyword value) {
        this.renderOptionalAssociativityParentheses = value;
    }

    /**
     * Whether to render the optional <code>AS</code> keyword in table aliases, if it is optional in the output dialect. This is ignored if the keyword is not supported (e.g. in Oracle)
     * 
     */
    public RenderOptionalKeyword getRenderOptionalAsKeywordForTableAliases() {
        return renderOptionalAsKeywordForTableAliases;
    }

    /**
     * Whether to render the optional <code>AS</code> keyword in table aliases, if it is optional in the output dialect. This is ignored if the keyword is not supported (e.g. in Oracle)
     * 
     */
    public void setRenderOptionalAsKeywordForTableAliases(RenderOptionalKeyword value) {
        this.renderOptionalAsKeywordForTableAliases = value;
    }

    /**
     * Whether to render the optional <code>AS</code> keyword in table aliases, if it is optional in the output dialect.
     * 
     */
    public RenderOptionalKeyword getRenderOptionalAsKeywordForFieldAliases() {
        return renderOptionalAsKeywordForFieldAliases;
    }

    /**
     * Whether to render the optional <code>AS</code> keyword in table aliases, if it is optional in the output dialect.
     * 
     */
    public void setRenderOptionalAsKeywordForFieldAliases(RenderOptionalKeyword value) {
        this.renderOptionalAsKeywordForFieldAliases = value;
    }

    /**
     * Whether to render the optional <code>INNER</code> keyword in <code>INNER JOIN</code>, if it is optional in the output dialect.
     * 
     */
    public RenderOptionalKeyword getRenderOptionalInnerKeyword() {
        return renderOptionalInnerKeyword;
    }

    /**
     * Whether to render the optional <code>INNER</code> keyword in <code>INNER JOIN</code>, if it is optional in the output dialect.
     * 
     */
    public void setRenderOptionalInnerKeyword(RenderOptionalKeyword value) {
        this.renderOptionalInnerKeyword = value;
    }

    /**
     * Whether to render the optional <code>OUTER</code> keyword in <code>OUTER JOIN</code>, if it is optional in the output dialect.
     * 
     */
    public RenderOptionalKeyword getRenderOptionalOuterKeyword() {
        return renderOptionalOuterKeyword;
    }

    /**
     * Whether to render the optional <code>OUTER</code> keyword in <code>OUTER JOIN</code>, if it is optional in the output dialect.
     * 
     */
    public void setRenderOptionalOuterKeyword(RenderOptionalKeyword value) {
        this.renderOptionalOuterKeyword = value;
    }

    /**
     * Whether to render an explicit window <code>RANGE</code> clause when an implicit clause is applied.
     * 
     */
    public RenderImplicitWindowRange getRenderImplicitWindowRange() {
        return renderImplicitWindowRange;
    }

    /**
     * Whether to render an explicit window <code>RANGE</code> clause when an implicit clause is applied.
     * 
     */
    public void setRenderImplicitWindowRange(RenderImplicitWindowRange value) {
        this.renderImplicitWindowRange = value;
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
     * Whether stored function calls should be wrapped in scalar subqueries.
     * <p>
     * Oracle 11g (and potentially, other databases too) implements scalar subquery caching. With this flag
     * set to true, users can automatically profit from this feature in all SQL statements.
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
     * The join type to be generated by implicit joins.
     * 
     */
    public RenderImplicitJoinType getRenderImplicitJoinType() {
        return renderImplicitJoinType;
    }

    /**
     * The join type to be generated by implicit joins.
     * 
     */
    public void setRenderImplicitJoinType(RenderImplicitJoinType value) {
        this.renderImplicitJoinType = value;
    }

    /**
     * Whether the {@link org.jooq.Nullability#DEFAULT} nullablity should be rendered in generated DDL, and how it should be rendered.
     * 
     */
    public RenderDefaultNullability getRenderDefaultNullability() {
        return renderDefaultNullability;
    }

    /**
     * Whether the {@link org.jooq.Nullability#DEFAULT} nullablity should be rendered in generated DDL, and how it should be rendered.
     * 
     */
    public void setRenderDefaultNullability(RenderDefaultNullability value) {
        this.renderDefaultNullability = value;
    }

    /**
     * Whether stored function calls should be wrapped in scalar subqueries.
     * <p>
     * Oracle 11g (and potentially, other databases too) implements scalar subquery caching. With this flag
     * set to true, users can automatically profit from this feature in all SQL statements.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRenderCoalesceToEmptyStringInConcat() {
        return renderCoalesceToEmptyStringInConcat;
    }

    /**
     * Whether stored function calls should be wrapped in scalar subqueries.
     * <p>
     * Oracle 11g (and potentially, other databases too) implements scalar subquery caching. With this flag
     * set to true, users can automatically profit from this feature in all SQL statements.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRenderCoalesceToEmptyStringInConcat(Boolean value) {
        this.renderCoalesceToEmptyStringInConcat = value;
    }

    /**
     * Whether an additional <code>ORDER BY rn</code> clause should be rendered on emulated paginated queries.
     * <p>
     * Older databases did not support OFFSET .. FETCH pagination, so jOOQ emulates it using derived
     * tables and <code>ROWNUM</code> (Oracle 11g and older) or <code>ROW_NUMBER()</code> (e.g. DB2, 
     * SQL Server, etc.) filtering. While these subqueries are ordered, the ordering is not 
     * <em>guaranteed</em> to be stable in the outer most queries. It may be stable (and e.g. in Oracle, 
     * it mostly is, if queries are not parallel, or joined to other queries, etc.), so the excess 
     * <code>ORDER BY</code> clause may add some additional performance overhead. This setting forces 
     * jOOQ to not generate the additional <code>ORDER BY</code> clause.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/7609">https://github.com/jOOQ/jOOQ/issues/7609</a>.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRenderOrderByRownumberForEmulatedPagination() {
        return renderOrderByRownumberForEmulatedPagination;
    }

    /**
     * Whether an additional <code>ORDER BY rn</code> clause should be rendered on emulated paginated queries.
     * <p>
     * Older databases did not support OFFSET .. FETCH pagination, so jOOQ emulates it using derived
     * tables and <code>ROWNUM</code> (Oracle 11g and older) or <code>ROW_NUMBER()</code> (e.g. DB2, 
     * SQL Server, etc.) filtering. While these subqueries are ordered, the ordering is not 
     * <em>guaranteed</em> to be stable in the outer most queries. It may be stable (and e.g. in Oracle, 
     * it mostly is, if queries are not parallel, or joined to other queries, etc.), so the excess 
     * <code>ORDER BY</code> clause may add some additional performance overhead. This setting forces 
     * jOOQ to not generate the additional <code>ORDER BY</code> clause.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/7609">https://github.com/jOOQ/jOOQ/issues/7609</a>.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRenderOrderByRownumberForEmulatedPagination(Boolean value) {
        this.renderOrderByRownumberForEmulatedPagination = value;
    }

    /**
     * Whether the jOOQ <code>RETURNING</code> clause should map to SQL Server's <code>OUTPUT</code> clause.
     * <p>
     * SQL Server supports an <code>OUTPUT</code> clause in most DML statements, whose behaviour
     * is almost identical to <code>RETURNING</code> in Firebird, Oracle, PostgreSQL. Users who
     * want to prevent jOOQ from rendering this <code>OUTPUT</code> clause can deactivate this flag
     * to revert to jOOQ calling {@code java.sql.Statement#getGeneratedKeys()} instead, which
     * is only supported for single row inserts.
     * <p>
     * This <code>OUTPUT</code> clause does not support fetching trigger generated values. In order
     * to fetch trigger generated values, {@link #fetchTriggerValuesAfterSQLServerOutput} needs to
     * be enabled as well.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/4498">https://github.com/jOOQ/jOOQ/issues/4498</a>.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRenderOutputForSQLServerReturningClause() {
        return renderOutputForSQLServerReturningClause;
    }

    /**
     * Whether the jOOQ <code>RETURNING</code> clause should map to SQL Server's <code>OUTPUT</code> clause.
     * <p>
     * SQL Server supports an <code>OUTPUT</code> clause in most DML statements, whose behaviour
     * is almost identical to <code>RETURNING</code> in Firebird, Oracle, PostgreSQL. Users who
     * want to prevent jOOQ from rendering this <code>OUTPUT</code> clause can deactivate this flag
     * to revert to jOOQ calling {@code java.sql.Statement#getGeneratedKeys()} instead, which
     * is only supported for single row inserts.
     * <p>
     * This <code>OUTPUT</code> clause does not support fetching trigger generated values. In order
     * to fetch trigger generated values, {@link #fetchTriggerValuesAfterSQLServerOutput} needs to
     * be enabled as well.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/4498">https://github.com/jOOQ/jOOQ/issues/4498</a>.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRenderOutputForSQLServerReturningClause(Boolean value) {
        this.renderOutputForSQLServerReturningClause = value;
    }

    /**
     * Whether the jOOQ <code>GROUP_CONCAT</code> function should be overflow-protected by setting the <code>@@group_concat_max_len</code> session variable in MySQL style database systems.
     * <p>
     * MySQL truncates <code>GROUP_CONCAT</code> results after a certain length, which may be way
     * too small for jOOQ's usage, especially when using the <code>MULTISET</code> emulation. By
     * default, jOOQ sets a session variable to the highest possible value prior to executing a
     * query containing <code>GROUP_CONCAT</code>. This flag can be used to opt out of this.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/12092">https://github.com/jOOQ/jOOQ/issues/12092</a>.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRenderGroupConcatMaxLenSessionVariable() {
        return renderGroupConcatMaxLenSessionVariable;
    }

    /**
     * Whether the jOOQ <code>GROUP_CONCAT</code> function should be overflow-protected by setting the <code>@@group_concat_max_len</code> session variable in MySQL style database systems.
     * <p>
     * MySQL truncates <code>GROUP_CONCAT</code> results after a certain length, which may be way
     * too small for jOOQ's usage, especially when using the <code>MULTISET</code> emulation. By
     * default, jOOQ sets a session variable to the highest possible value prior to executing a
     * query containing <code>GROUP_CONCAT</code>. This flag can be used to opt out of this.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/12092">https://github.com/jOOQ/jOOQ/issues/12092</a>.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRenderGroupConcatMaxLenSessionVariable(Boolean value) {
        this.renderGroupConcatMaxLenSessionVariable = value;
    }

    /**
     * Whether queries combined with set operators (e.g. UNION and UNION ALL) should always be surrounded by a parenthesis pair.
     * <p>
     * By default (i.e. when this setting is set to <code>false</code> jOOQ will only render parenthesis pairs around queries combined with set operators when required.
     * This is for example the case when set operators are nested, when non-associative operators like EXCEPT are used, or when the queries are rendered as derived tables.
     * <p>
     * When this setting is set to <code>true</code> the queries combined with set operators will always be surrounded by a parenthesis pair.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/3676">https://github.com/jOOQ/jOOQ/issues/3676</a> and <a href="https://github.com/jOOQ/jOOQ/issues/9751">https://github.com/jOOQ/jOOQ/issues/9751</a>.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRenderParenthesisAroundSetOperationQueries() {
        return renderParenthesisAroundSetOperationQueries;
    }

    /**
     * Whether queries combined with set operators (e.g. UNION and UNION ALL) should always be surrounded by a parenthesis pair.
     * <p>
     * By default (i.e. when this setting is set to <code>false</code> jOOQ will only render parenthesis pairs around queries combined with set operators when required.
     * This is for example the case when set operators are nested, when non-associative operators like EXCEPT are used, or when the queries are rendered as derived tables.
     * <p>
     * When this setting is set to <code>true</code> the queries combined with set operators will always be surrounded by a parenthesis pair.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/3676">https://github.com/jOOQ/jOOQ/issues/3676</a> and <a href="https://github.com/jOOQ/jOOQ/issues/9751">https://github.com/jOOQ/jOOQ/issues/9751</a>.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRenderParenthesisAroundSetOperationQueries(Boolean value) {
        this.renderParenthesisAroundSetOperationQueries = value;
    }

    /**
     * Whether emulations that require repeating expressions should render variables for those expressions in derived tables.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/14065">https://github.com/jOOQ/jOOQ/issues/14065</a>.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRenderVariablesInDerivedTablesForEmulations() {
        return renderVariablesInDerivedTablesForEmulations;
    }

    /**
     * Whether emulations that require repeating expressions should render variables for those expressions in derived tables.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/14065">https://github.com/jOOQ/jOOQ/issues/14065</a>.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRenderVariablesInDerivedTablesForEmulations(Boolean value) {
        this.renderVariablesInDerivedTablesForEmulations = value;
    }

    /**
     * Whether a <code>(a, b) < (:a, :b)</code> row predicate should be rendered for the <code>SEEK</code> clause.
     * <p>
     * Some RDBMS may support <code>(a, b) < (:a, :b)</code> row predicate syntax, which is very convenient for <code>SEEK</code> clause implementations, but fail to optimise this predicate as could be expected.
     * This flag allows for expanding the predicate to the much more verbose, but equivalent <code>(a < :a) OR (a = :a AND b < :b)</code>. Dialects without native support for row predicates aren't affected
     * by this flag.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRenderRowConditionForSeekClause() {
        return renderRowConditionForSeekClause;
    }

    /**
     * Whether a <code>(a, b) < (:a, :b)</code> row predicate should be rendered for the <code>SEEK</code> clause.
     * <p>
     * Some RDBMS may support <code>(a, b) < (:a, :b)</code> row predicate syntax, which is very convenient for <code>SEEK</code> clause implementations, but fail to optimise this predicate as could be expected.
     * This flag allows for expanding the predicate to the much more verbose, but equivalent <code>(a < :a) OR (a = :a AND b < :b)</code>. Dialects without native support for row predicates aren't affected
     * by this flag.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRenderRowConditionForSeekClause(Boolean value) {
        this.renderRowConditionForSeekClause = value;
    }

    /**
     * Whether a redundant <code>(a <= :a)</code> predicate should be rendered for a <code>(a, b) < (:a, :b)</code> predicate for the <code>SEEK</code> clause.
     * <p>
     * Some RDBMS may not be able to properly optimise <code>(a, b) < ('a', 'b')</code> or <code>(a < 'a') OR (a = 'a' AND b < 'b')</code>, and choose an appropriate index. By adding an additional redundant predicate,
     * jOOQ may help the optimiser, e.g. <code>(a <= :a) AND (a, b) < ('a', 'b')</code> or <code>(a <= :a) AND ((a < 'a') OR (a = 'a' AND b < 'b'))</code>
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRenderRedundantConditionForSeekClause() {
        return renderRedundantConditionForSeekClause;
    }

    /**
     * Whether a redundant <code>(a <= :a)</code> predicate should be rendered for a <code>(a, b) < (:a, :b)</code> predicate for the <code>SEEK</code> clause.
     * <p>
     * Some RDBMS may not be able to properly optimise <code>(a, b) < ('a', 'b')</code> or <code>(a < 'a') OR (a = 'a' AND b < 'b')</code>, and choose an appropriate index. By adding an additional redundant predicate,
     * jOOQ may help the optimiser, e.g. <code>(a <= :a) AND (a, b) < ('a', 'b')</code> or <code>(a <= :a) AND ((a < 'a') OR (a = 'a' AND b < 'b'))</code>
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRenderRedundantConditionForSeekClause(Boolean value) {
        this.renderRedundantConditionForSeekClause = value;
    }

    /**
     * Whether plain SQL templates ({@link org.jooq.SQL}) are rendered as raw string content.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRenderPlainSQLTemplatesAsRaw() {
        return renderPlainSQLTemplatesAsRaw;
    }

    /**
     * Whether plain SQL templates ({@link org.jooq.SQL}) are rendered as raw string content.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRenderPlainSQLTemplatesAsRaw(Boolean value) {
        this.renderPlainSQLTemplatesAsRaw = value;
    }

    /**
     * The character(s) to be used as a separator in paths encoded in a {@link Name}
     * <p>
     * A few hierarchical mapping features work with paths encoded in names (specifically field aliases), such as the reflective mapping of nested values when aliasing fields as:
     * <p>
     * <pre><code>
     * SELECT 
     *   a.first_name AS "book.author.firstName"
     *   a.last_name AS "book.author.lastName"
     * FROM ...
     * </code></pre>
     * <p>
     * Not all dialects support "." in identifiers. This setting allows for specifying an alternative String to use as separator, e.g. "__".
     * 
     */
    public String getNamePathSeparator() {
        return namePathSeparator;
    }

    /**
     * The character(s) to be used as a separator in paths encoded in a {@link Name}
     * <p>
     * A few hierarchical mapping features work with paths encoded in names (specifically field aliases), such as the reflective mapping of nested values when aliasing fields as:
     * <p>
     * <pre><code>
     * SELECT 
     *   a.first_name AS "book.author.firstName"
     *   a.last_name AS "book.author.lastName"
     * FROM ...
     * </code></pre>
     * <p>
     * Not all dialects support "." in identifiers. This setting allows for specifying an alternative String to use as separator, e.g. "__".
     * 
     */
    public void setNamePathSeparator(String value) {
        this.namePathSeparator = value;
    }

    /**
     * Whether the <code>java.time</code> (JSR 310) type {@link java.time.OffsetDateTime} should be bound natively to JDBC.
     * <p>
     * Historically, jOOQ encoded the <code>java.time</code> types as strings to offer better compatibility with older JDBC drivers. By now, most drivers should support the <code>java.time</code> types. Using them may produce better performance both on the server and on the client side.
     * <p>
     * This flag allows for reverting to pre-jOOQ 3.14 behaviour, where the default is to bind these types natively.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/9902">https://github.com/jOOQ/jOOQ/issues/9902</a>.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBindOffsetDateTimeType() {
        return bindOffsetDateTimeType;
    }

    /**
     * Whether the <code>java.time</code> (JSR 310) type {@link java.time.OffsetDateTime} should be bound natively to JDBC.
     * <p>
     * Historically, jOOQ encoded the <code>java.time</code> types as strings to offer better compatibility with older JDBC drivers. By now, most drivers should support the <code>java.time</code> types. Using them may produce better performance both on the server and on the client side.
     * <p>
     * This flag allows for reverting to pre-jOOQ 3.14 behaviour, where the default is to bind these types natively.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/9902">https://github.com/jOOQ/jOOQ/issues/9902</a>.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBindOffsetDateTimeType(Boolean value) {
        this.bindOffsetDateTimeType = value;
    }

    /**
     * Whether the <code>java.time</code> (JSR 310) type {@link java.time.OffsetTime} should be bound natively to JDBC.
     * <p>
     * Historically, jOOQ encoded the <code>java.time</code> types as strings to offer better compatibility with older JDBC drivers. By now, most drivers should support the <code>java.time</code> types. Using them may produce better performance both on the server and on the client side.
     * <p>
     * This flag allows for reverting to pre-jOOQ 3.14 behaviour, where the default is to bind these types natively.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/9902">https://github.com/jOOQ/jOOQ/issues/9902</a>.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBindOffsetTimeType() {
        return bindOffsetTimeType;
    }

    /**
     * Whether the <code>java.time</code> (JSR 310) type {@link java.time.OffsetTime} should be bound natively to JDBC.
     * <p>
     * Historically, jOOQ encoded the <code>java.time</code> types as strings to offer better compatibility with older JDBC drivers. By now, most drivers should support the <code>java.time</code> types. Using them may produce better performance both on the server and on the client side.
     * <p>
     * This flag allows for reverting to pre-jOOQ 3.14 behaviour, where the default is to bind these types natively.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/9902">https://github.com/jOOQ/jOOQ/issues/9902</a>.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBindOffsetTimeType(Boolean value) {
        this.bindOffsetTimeType = value;
    }

    /**
     * Fetch trigger values after SQL Server <code>OUTPUT</code> clause.
     * <p>
     * SQL Server <code>OUTPUT</code> statements do not support fetching trigger generated values.
     * This is a limitation of the {@link #renderOutputForSQLServerReturningClause}. An additional
     * <code>MERGE</code> statement can run a second query if (and only if) the primary key has been
     * included in the <code>OUTPUT</code> clause.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/4498">https://github.com/jOOQ/jOOQ/issues/4498</a>.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFetchTriggerValuesAfterSQLServerOutput() {
        return fetchTriggerValuesAfterSQLServerOutput;
    }

    /**
     * Fetch trigger values after SQL Server <code>OUTPUT</code> clause.
     * <p>
     * SQL Server <code>OUTPUT</code> statements do not support fetching trigger generated values.
     * This is a limitation of the {@link #renderOutputForSQLServerReturningClause}. An additional
     * <code>MERGE</code> statement can run a second query if (and only if) the primary key has been
     * included in the <code>OUTPUT</code> clause.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/4498">https://github.com/jOOQ/jOOQ/issues/4498</a>.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFetchTriggerValuesAfterSQLServerOutput(Boolean value) {
        this.fetchTriggerValuesAfterSQLServerOutput = value;
    }

    /**
     * Whether to fetch data into intermediate {@link org.jooq.Result} instances.
     * <p>
     * By default, a {@link org.jooq.ResultQuery} produces no intermediate {@link org.jooq.Result} 
     * instances if they are not explicitly requested by the caller, e.g. by calling
     * {@link org.jooq.ResultQuery#fetch()}, or in the presence of {@link org.jooq.ExecuteListener}
     * instances, which may require access to {@link org.jooq.ExecuteContext#result()}.
     * This default behaviour helps avoid unnecessary allocations of possibly large data structures.
     * <p>
     * Using this flag, fetching of intermediate results can be turned off even when execute listeners
     * are present, or turned on even if they're absent.
     * 
     */
    public FetchIntermediateResult getFetchIntermediateResult() {
        return fetchIntermediateResult;
    }

    /**
     * Whether to fetch data into intermediate {@link org.jooq.Result} instances.
     * <p>
     * By default, a {@link org.jooq.ResultQuery} produces no intermediate {@link org.jooq.Result} 
     * instances if they are not explicitly requested by the caller, e.g. by calling
     * {@link org.jooq.ResultQuery#fetch()}, or in the presence of {@link org.jooq.ExecuteListener}
     * instances, which may require access to {@link org.jooq.ExecuteContext#result()}.
     * This default behaviour helps avoid unnecessary allocations of possibly large data structures.
     * <p>
     * Using this flag, fetching of intermediate results can be turned off even when execute listeners
     * are present, or turned on even if they're absent.
     * 
     */
    public void setFetchIntermediateResult(FetchIntermediateResult value) {
        this.fetchIntermediateResult = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#duplicateStatements(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsDuplicateStatements() {
        return diagnosticsDuplicateStatements;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#duplicateStatements(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsDuplicateStatements(Boolean value) {
        this.diagnosticsDuplicateStatements = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#duplicateStatements(org.jooq.DiagnosticsContext)} diagnostic with the {@link #transformPatterns} feature activated.
     * <p>
     * When transforming patterns, many more complex, duplicate SQL statements can be recognised than if simply
     * parsing and re-rendering the statement. This flag turns on all transformation patterns, independently of their
     * individual settings.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsDuplicateStatementsUsingTransformPatterns() {
        return diagnosticsDuplicateStatementsUsingTransformPatterns;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#duplicateStatements(org.jooq.DiagnosticsContext)} diagnostic with the {@link #transformPatterns} feature activated.
     * <p>
     * When transforming patterns, many more complex, duplicate SQL statements can be recognised than if simply
     * parsing and re-rendering the statement. This flag turns on all transformation patterns, independently of their
     * individual settings.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsDuplicateStatementsUsingTransformPatterns(Boolean value) {
        this.diagnosticsDuplicateStatementsUsingTransformPatterns = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#missingWasNullCall(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsMissingWasNullCall() {
        return diagnosticsMissingWasNullCall;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#missingWasNullCall(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsMissingWasNullCall(Boolean value) {
        this.diagnosticsMissingWasNullCall = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#repeatedStatements(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsRepeatedStatements() {
        return diagnosticsRepeatedStatements;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#repeatedStatements(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsRepeatedStatements(Boolean value) {
        this.diagnosticsRepeatedStatements = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#consecutiveAggregation(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsConsecutiveAggregation() {
        return diagnosticsConsecutiveAggregation;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#consecutiveAggregation(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsConsecutiveAggregation(Boolean value) {
        this.diagnosticsConsecutiveAggregation = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#concatenationInPredicate(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsConcatenationInPredicate() {
        return diagnosticsConcatenationInPredicate;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#concatenationInPredicate(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsConcatenationInPredicate(Boolean value) {
        this.diagnosticsConcatenationInPredicate = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#possiblyWrongExpression(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsPossiblyWrongExpression() {
        return diagnosticsPossiblyWrongExpression;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#possiblyWrongExpression(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsPossiblyWrongExpression(Boolean value) {
        this.diagnosticsPossiblyWrongExpression = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#tooManyColumnsFetched(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsTooManyColumnsFetched() {
        return diagnosticsTooManyColumnsFetched;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#tooManyColumnsFetched(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsTooManyColumnsFetched(Boolean value) {
        this.diagnosticsTooManyColumnsFetched = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#tooManyRowsFetched(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsTooManyRowsFetched() {
        return diagnosticsTooManyRowsFetched;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#tooManyRowsFetched(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsTooManyRowsFetched(Boolean value) {
        this.diagnosticsTooManyRowsFetched = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#unnecessaryWasNullCall(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsUnnecessaryWasNullCall() {
        return diagnosticsUnnecessaryWasNullCall;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#unnecessaryWasNullCall(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsUnnecessaryWasNullCall(Boolean value) {
        this.diagnosticsUnnecessaryWasNullCall = value;
    }

    /**
     * Whether to run the various pattern transformation diagnostics.
     * <p>
     * {@link #transformPatterns} allows for applying numerous pattern transformations, which can be turned on separately when running
     * diagnostics. This flag overrides the {@link #transformPatterns} flag in the diagnostics context. Individual pattern flags
     * still allow to enable / disable the pattern for diagnostics. 
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsPatterns() {
        return diagnosticsPatterns;
    }

    /**
     * Whether to run the various pattern transformation diagnostics.
     * <p>
     * {@link #transformPatterns} allows for applying numerous pattern transformations, which can be turned on separately when running
     * diagnostics. This flag overrides the {@link #transformPatterns} flag in the diagnostics context. Individual pattern flags
     * still allow to enable / disable the pattern for diagnostics. 
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsPatterns(Boolean value) {
        this.diagnosticsPatterns = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#trivialCondition(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsTrivialCondition() {
        return diagnosticsTrivialCondition;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#trivialCondition(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsTrivialCondition(Boolean value) {
        this.diagnosticsTrivialCondition = value;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#nullConditoin(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsNullCondition() {
        return diagnosticsNullCondition;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#nullConditoin(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsNullCondition(Boolean value) {
        this.diagnosticsNullCondition = value;
    }

    /**
     * Transform various syntax patterns to better versions, if possible.
     * <p>
     * This flag enables the pattern transformation feature, which consists of several sub-flags that are
     * all prefixed with "transformPatterns", e.g. {@link #transformPatternsTrim}. While the sub-flags default
     * to being enabled, and can be disabled on an individual basis, the global feature itself is disabled by
     * default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatterns() {
        return transformPatterns;
    }

    /**
     * Transform various syntax patterns to better versions, if possible.
     * <p>
     * This flag enables the pattern transformation feature, which consists of several sub-flags that are
     * all prefixed with "transformPatterns", e.g. {@link #transformPatternsTrim}. While the sub-flags default
     * to being enabled, and can be disabled on an individual basis, the global feature itself is disabled by
     * default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatterns(Boolean value) {
        this.transformPatterns = value;
    }

    /**
     * Activate debug logging of the {@link #transformPatterns} feature.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsLogging() {
        return transformPatternsLogging;
    }

    /**
     * Activate debug logging of the {@link #transformPatterns} feature.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsLogging(Boolean value) {
        this.transformPatternsLogging = value;
    }

    /**
     * Transform <code>SELECT DISTINCT a, b FROM t GROUP BY a, b</code> to <code>SELECT a, b FROM t GROUP BY a, b</code>.
     * <p>
     * The <code>GROUP BY</code> clause already removes duplicates, so if the <code>DISTINCT</code> clause
     * contains at least all the columns from <code>GROUP BY</code> then it can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsUnnecessaryDistinct() {
        return transformPatternsUnnecessaryDistinct;
    }

    /**
     * Transform <code>SELECT DISTINCT a, b FROM t GROUP BY a, b</code> to <code>SELECT a, b FROM t GROUP BY a, b</code>.
     * <p>
     * The <code>GROUP BY</code> clause already removes duplicates, so if the <code>DISTINCT</code> clause
     * contains at least all the columns from <code>GROUP BY</code> then it can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsUnnecessaryDistinct(Boolean value) {
        this.transformPatternsUnnecessaryDistinct = value;
    }

    /**
     * Transform <code>SELECT (SELECT 1)</code> to <code>SELECT 1</code>.
     * <p>
     * Scalar subqueries that don't have any content other than a <code>SELECT</code> clause are unnecessary
     * and can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsUnnecessaryScalarSubquery() {
        return transformPatternsUnnecessaryScalarSubquery;
    }

    /**
     * Transform <code>SELECT (SELECT 1)</code> to <code>SELECT 1</code>.
     * <p>
     * Scalar subqueries that don't have any content other than a <code>SELECT</code> clause are unnecessary
     * and can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsUnnecessaryScalarSubquery(Boolean value) {
        this.transformPatternsUnnecessaryScalarSubquery = value;
    }

    /**
     * Transform <code>SELECT * FROM t INNER JOIN u ON TRUE</code> to <code>SELECT * FROM t CROSS JOIN u</code>.
     * <p>
     * Some <code>INNER JOIN</code> expressions can be proven to be unnecessary.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsUnnecessaryInnerJoin() {
        return transformPatternsUnnecessaryInnerJoin;
    }

    /**
     * Transform <code>SELECT * FROM t INNER JOIN u ON TRUE</code> to <code>SELECT * FROM t CROSS JOIN u</code>.
     * <p>
     * Some <code>INNER JOIN</code> expressions can be proven to be unnecessary.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsUnnecessaryInnerJoin(Boolean value) {
        this.transformPatternsUnnecessaryInnerJoin = value;
    }

    /**
     * Transform <code>SELECT a, b FROM t GROUP BY a, a, b</code> to <code>SELECT a, b FROM t GROUP BY a, b</code>.
     * <p>
     * Duplicate <code>GROUP BY</code> expressions can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsUnnecessaryGroupByExpressions() {
        return transformPatternsUnnecessaryGroupByExpressions;
    }

    /**
     * Transform <code>SELECT a, b FROM t GROUP BY a, a, b</code> to <code>SELECT a, b FROM t GROUP BY a, b</code>.
     * <p>
     * Duplicate <code>GROUP BY</code> expressions can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsUnnecessaryGroupByExpressions(Boolean value) {
        this.transformPatternsUnnecessaryGroupByExpressions = value;
    }

    /**
     * Transform <code>SELECT a, b FROM t ORDER BY a, a, b</code> to <code>SELECT a, b FROM t ORDER BY a, b</code>.
     * <p>
     * Duplicate <code>ORDER BY</code> expressions can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsUnnecessaryOrderByExpressions() {
        return transformPatternsUnnecessaryOrderByExpressions;
    }

    /**
     * Transform <code>SELECT a, b FROM t ORDER BY a, a, b</code> to <code>SELECT a, b FROM t ORDER BY a, b</code>.
     * <p>
     * Duplicate <code>ORDER BY</code> expressions can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsUnnecessaryOrderByExpressions(Boolean value) {
        this.transformPatternsUnnecessaryOrderByExpressions = value;
    }

    /**
     * Transform <code>[ NOT ] EXISTS (SELECT DISTINCT a, b FROM t ORDER BY c LIMIT d)</code> to <code>[ NOT ] EXISTS (SELECT 1 FROM t)</code>.
     * <p>
     * In <code>EXISTS</code> subqueries, quite a few <code>SELECT</code> clauses are meaningless, and can
     * thus be removed. These include:
     * <ul>
     * <li><code>SELECT</code> (any projection can be ignored)</li>
     * <li><code>DISTINCT</code></li>
     * <li><code>ORDER BY</code></li>
     * <li><code>LIMIT</code> (except <code>LIMIT 0</code>, in case of which {@link #transformPatternsTrivialPredicates} applies).</li>
     * </ul>
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsUnnecessaryExistsSubqueryClauses() {
        return transformPatternsUnnecessaryExistsSubqueryClauses;
    }

    /**
     * Transform <code>[ NOT ] EXISTS (SELECT DISTINCT a, b FROM t ORDER BY c LIMIT d)</code> to <code>[ NOT ] EXISTS (SELECT 1 FROM t)</code>.
     * <p>
     * In <code>EXISTS</code> subqueries, quite a few <code>SELECT</code> clauses are meaningless, and can
     * thus be removed. These include:
     * <ul>
     * <li><code>SELECT</code> (any projection can be ignored)</li>
     * <li><code>DISTINCT</code></li>
     * <li><code>ORDER BY</code></li>
     * <li><code>LIMIT</code> (except <code>LIMIT 0</code>, in case of which {@link #transformPatternsTrivialPredicates} applies).</li>
     * </ul>
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsUnnecessaryExistsSubqueryClauses(Boolean value) {
        this.transformPatternsUnnecessaryExistsSubqueryClauses = value;
    }

    /**
     * Transform <code>COUNT(1)</code> or any other <code>COUNT(const)</code> to <code>COUNT(*)</code>.
     * <p>
     * There is no benefit to counting a constant expression. In fact, in some RDBMS, it might even be slightly
     * slower, at least in benchmarks.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsCountConstant() {
        return transformPatternsCountConstant;
    }

    /**
     * Transform <code>COUNT(1)</code> or any other <code>COUNT(const)</code> to <code>COUNT(*)</code>.
     * <p>
     * There is no benefit to counting a constant expression. In fact, in some RDBMS, it might even be slightly
     * slower, at least in benchmarks.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsCountConstant(Boolean value) {
        this.transformPatternsCountConstant = value;
    }

    /**
     * Transform <code>LTRIM(RTRIM(x))</code> or <code>RTRIM(LTRIM(x))</code> to <code>TRIM(x)</code>.
     * <p>
     * Historically, a few dialects did not implement <code>TRIM(x)</code> or <code>TRIM(BOTH FROM x)</code>,
     * so users worked around this by wrapping <code>LTRIM()</code> and <code>RTRIM()</code> with each other.
     * Maintaining this is usually undesirable, so this transformation helps remove the unwanted wrapping.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsTrim() {
        return transformPatternsTrim;
    }

    /**
     * Transform <code>LTRIM(RTRIM(x))</code> or <code>RTRIM(LTRIM(x))</code> to <code>TRIM(x)</code>.
     * <p>
     * Historically, a few dialects did not implement <code>TRIM(x)</code> or <code>TRIM(BOTH FROM x)</code>,
     * so users worked around this by wrapping <code>LTRIM()</code> and <code>RTRIM()</code> with each other.
     * Maintaining this is usually undesirable, so this transformation helps remove the unwanted wrapping.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsTrim(Boolean value) {
        this.transformPatternsTrim = value;
    }

    /**
     * Transform <code>NOT(p AND q)</code> to <code>NOT(p) OR NOT(q)</code>.
     * <p>
     * This transformation normalises a predicate using De Morgan's rules.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNotAnd() {
        return transformPatternsNotAnd;
    }

    /**
     * Transform <code>NOT(p AND q)</code> to <code>NOT(p) OR NOT(q)</code>.
     * <p>
     * This transformation normalises a predicate using De Morgan's rules.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNotAnd(Boolean value) {
        this.transformPatternsNotAnd = value;
    }

    /**
     * Transform <code>NOT(p OR q)</code> to <code>NOT(p) AND NOT(q)</code>.
     * <p>
     * This transformation normalises a predicate using De Morgan's rules.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNotOr() {
        return transformPatternsNotOr;
    }

    /**
     * Transform <code>NOT(p OR q)</code> to <code>NOT(p) AND NOT(q)</code>.
     * <p>
     * This transformation normalises a predicate using De Morgan's rules.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNotOr(Boolean value) {
        this.transformPatternsNotOr = value;
    }

    /**
     * Transform <code>NOT(NOT(x))</code> to <code>x</code>.
     * <p>
     * This transformation removes a redundant logic negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNotNot() {
        return transformPatternsNotNot;
    }

    /**
     * Transform <code>NOT(NOT(x))</code> to <code>x</code>.
     * <p>
     * This transformation removes a redundant logic negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNotNot(Boolean value) {
        this.transformPatternsNotNot = value;
    }

    /**
     * Transform <code>NOT (a != b)</code> to <code>a = b</code>, and similar comparisons.
     * <p>
     * This transformation removes a redundant logical negation from the <code>DISTINCT</code> predicate.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNotComparison() {
        return transformPatternsNotComparison;
    }

    /**
     * Transform <code>NOT (a != b)</code> to <code>a = b</code>, and similar comparisons.
     * <p>
     * This transformation removes a redundant logical negation from the <code>DISTINCT</code> predicate.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNotComparison(Boolean value) {
        this.transformPatternsNotComparison = value;
    }

    /**
     * Transform <code>NOT (a IS NOT DISTINCT FROM b)</code> to <code>a IS DISTINCT FROM b</code>.
     * <p>
     * This transformation removes a redundant logical negation from the <code>DISTINCT</code> predicate.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNotNotDistinct() {
        return transformPatternsNotNotDistinct;
    }

    /**
     * Transform <code>NOT (a IS NOT DISTINCT FROM b)</code> to <code>a IS DISTINCT FROM b</code>.
     * <p>
     * This transformation removes a redundant logical negation from the <code>DISTINCT</code> predicate.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNotNotDistinct(Boolean value) {
        this.transformPatternsNotNotDistinct = value;
    }

    /**
     * Transform <code>a IS [ NOT ] DISTINCT FROM NULL</code> to <code>a IS [ NOT ] NULL</code>.
     * <p>
     * This simplifies the much more verbose <code>DISTINCT</code> predicate.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsDistinctFromNull() {
        return transformPatternsDistinctFromNull;
    }

    /**
     * Transform <code>a IS [ NOT ] DISTINCT FROM NULL</code> to <code>a IS [ NOT ] NULL</code>.
     * <p>
     * This simplifies the much more verbose <code>DISTINCT</code> predicate.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsDistinctFromNull(Boolean value) {
        this.transformPatternsDistinctFromNull = value;
    }

    /**
     * Transform <code>(a + b) + (c + d)</code> to <code>((a + b) + c) + d</code>.
     * <p>
     * This transformation turns trees into lists, which greatly simplifies other tree traversal transformations.
     * Some of those other transformations currently rely on this flag to be active.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNormaliseAssociativeOps() {
        return transformPatternsNormaliseAssociativeOps;
    }

    /**
     * Transform <code>(a + b) + (c + d)</code> to <code>((a + b) + c) + d</code>.
     * <p>
     * This transformation turns trees into lists, which greatly simplifies other tree traversal transformations.
     * Some of those other transformations currently rely on this flag to be active.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNormaliseAssociativeOps(Boolean value) {
        this.transformPatternsNormaliseAssociativeOps = value;
    }

    /**
     * Transform <code>x IN (a)</code> to <code>x = a</code> and <code>x NOT IN (a)</code> to <code>x != a</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNormaliseInListSingleElementToComparison() {
        return transformPatternsNormaliseInListSingleElementToComparison;
    }

    /**
     * Transform <code>x IN (a)</code> to <code>x = a</code> and <code>x NOT IN (a)</code> to <code>x != a</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNormaliseInListSingleElementToComparison(Boolean value) {
        this.transformPatternsNormaliseInListSingleElementToComparison = value;
    }

    /**
     * Transform <code>1 = a</code> to <code>a = 1</code>.
     * <p>
     * This transformation inverses {@link TableField} [op] {@link org.jooq.impl.QOM.Val} comparisons, if they're not in that order.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNormaliseFieldCompareValue() {
        return transformPatternsNormaliseFieldCompareValue;
    }

    /**
     * Transform <code>1 = a</code> to <code>a = 1</code>.
     * <p>
     * This transformation inverses {@link TableField} [op] {@link org.jooq.impl.QOM.Val} comparisons, if they're not in that order.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNormaliseFieldCompareValue(Boolean value) {
        this.transformPatternsNormaliseFieldCompareValue = value;
    }

    /**
     * Transform 2 argument <code>COALESCE(a, b)</code> to <code>NVL(a, b)</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNormaliseCoalesceToNvl() {
        return transformPatternsNormaliseCoalesceToNvl;
    }

    /**
     * Transform 2 argument <code>COALESCE(a, b)</code> to <code>NVL(a, b)</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNormaliseCoalesceToNvl(Boolean value) {
        this.transformPatternsNormaliseCoalesceToNvl = value;
    }

    /**
     * Transform <code>x = c1 OR x = c2</code> to <code>x IN (c1, c2)</code>.
     * <p>
     * This transformation simplifies verbose <code>OR</code> predicates into simpler <code>IN</code> predicates.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsOrEqToIn() {
        return transformPatternsOrEqToIn;
    }

    /**
     * Transform <code>x = c1 OR x = c2</code> to <code>x IN (c1, c2)</code>.
     * <p>
     * This transformation simplifies verbose <code>OR</code> predicates into simpler <code>IN</code> predicates.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsOrEqToIn(Boolean value) {
        this.transformPatternsOrEqToIn = value;
    }

    /**
     * Transform <code>x != c1 AND x != c2</code> to <code>x NOT IN (c1, c2)</code>.
     * <p>
     * This transformation simplifies verbose <code>AND</code> predicates into simpler <code>NOT IN</code> predicates.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsAndNeToNotIn() {
        return transformPatternsAndNeToNotIn;
    }

    /**
     * Transform <code>x != c1 AND x != c2</code> to <code>x NOT IN (c1, c2)</code>.
     * <p>
     * This transformation simplifies verbose <code>AND</code> predicates into simpler <code>NOT IN</code> predicates.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsAndNeToNotIn(Boolean value) {
        this.transformPatternsAndNeToNotIn = value;
    }

    /**
     * Transform <code>x = a OR x > a</code> to <code>x >= a</code>.
     * <p>
     * This transformation merges multiple <code>OR</code> connected comparisons to a single comparison using a simpler operator.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsMergeOrComparison() {
        return transformPatternsMergeOrComparison;
    }

    /**
     * Transform <code>x = a OR x > a</code> to <code>x >= a</code>.
     * <p>
     * This transformation merges multiple <code>OR</code> connected comparisons to a single comparison using a simpler operator.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsMergeOrComparison(Boolean value) {
        this.transformPatternsMergeOrComparison = value;
    }

    /**
     * Transform <code>x >= a AND x <= a</code> to <code>x = a</code>.
     * <p>
     * This transformation merges multiple <code>AND</code> connected comparisons to a single comparison using a simpler operator.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsMergeAndComparison() {
        return transformPatternsMergeAndComparison;
    }

    /**
     * Transform <code>x >= a AND x <= a</code> to <code>x = a</code>.
     * <p>
     * This transformation merges multiple <code>AND</code> connected comparisons to a single comparison using a simpler operator.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsMergeAndComparison(Boolean value) {
        this.transformPatternsMergeAndComparison = value;
    }

    /**
     * Transform <code>x IN (a, b, c) AND x IN (b, c, d)</code> to <code>x IN (b, c)</code>.
     * <p>
     * This transformation merges multiple <code>OR</code> connected comparisons to a single comparison using a simpler operator.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsMergeInLists() {
        return transformPatternsMergeInLists;
    }

    /**
     * Transform <code>x IN (a, b, c) AND x IN (b, c, d)</code> to <code>x IN (b, c)</code>.
     * <p>
     * This transformation merges multiple <code>OR</code> connected comparisons to a single comparison using a simpler operator.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsMergeInLists(Boolean value) {
        this.transformPatternsMergeInLists = value;
    }

    /**
     * Transform <code>x >= a AND x <= b</code> to <code>x BETWEEN a AND b</code>.
     * <p>
     * This transformation merges multiple <code>AND</code> connected range predicates to a single comparison using <code>BETWEEN</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsMergeRangePredicates() {
        return transformPatternsMergeRangePredicates;
    }

    /**
     * Transform <code>x >= a AND x <= b</code> to <code>x BETWEEN a AND b</code>.
     * <p>
     * This transformation merges multiple <code>AND</code> connected range predicates to a single comparison using <code>BETWEEN</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsMergeRangePredicates(Boolean value) {
        this.transformPatternsMergeRangePredicates = value;
    }

    /**
     * Transform <code>x BETWEEN a AND b OR x BETWEEN b AND a</code> to <code>x BETWEEN SYMMETRIC a AND b</code>.
     * <p>
     * This transformation merges multiple <code>OR</code> connected <code>BETWEEN</code> predicates to a single comparison using <code>BETWEEN SYMMETRIC</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsMergeBetweenSymmetricPredicates() {
        return transformPatternsMergeBetweenSymmetricPredicates;
    }

    /**
     * Transform <code>x BETWEEN a AND b OR x BETWEEN b AND a</code> to <code>x BETWEEN SYMMETRIC a AND b</code>.
     * <p>
     * This transformation merges multiple <code>OR</code> connected <code>BETWEEN</code> predicates to a single comparison using <code>BETWEEN SYMMETRIC</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsMergeBetweenSymmetricPredicates(Boolean value) {
        this.transformPatternsMergeBetweenSymmetricPredicates = value;
    }

    /**
     * Transform a searched <code>CASE WHEN x = .. WHEN x = ..</code> to a simple <code>CASE x WHEN … WHEN …</code> expression.
     * <p>
     * When a searched <code>CASE</code> expression always compares the same column to a value, then it can be simplified, possibly
     * unlocking further transformations that are available only to the simple <code>CASE</code> expression.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsCaseSearchedToCaseSimple() {
        return transformPatternsCaseSearchedToCaseSimple;
    }

    /**
     * Transform a searched <code>CASE WHEN x = .. WHEN x = ..</code> to a simple <code>CASE x WHEN … WHEN …</code> expression.
     * <p>
     * When a searched <code>CASE</code> expression always compares the same column to a value, then it can be simplified, possibly
     * unlocking further transformations that are available only to the simple <code>CASE</code> expression.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsCaseSearchedToCaseSimple(Boolean value) {
        this.transformPatternsCaseSearchedToCaseSimple = value;
    }

    /**
     * Transform <code>CASE … ELSE NULL</code> removing the <code>ELSE</code> clause.
     * <p>
     * <code>CASE WHEN x THEN y ELSE NULL END</code> is equivalent to <code>CASE WHEN x THEN y END</code>.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsCaseElseNull() {
        return transformPatternsCaseElseNull;
    }

    /**
     * Transform <code>CASE … ELSE NULL</code> removing the <code>ELSE</code> clause.
     * <p>
     * <code>CASE WHEN x THEN y ELSE NULL END</code> is equivalent to <code>CASE WHEN x THEN y END</code>.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsCaseElseNull(Boolean value) {
        this.transformPatternsCaseElseNull = value;
    }

    /**
     * Transform <code>CASE</code> by removing unreachable clauses.
     * <p>
     * Case clauses can be proven to be unreachable, and thus removed:
     * <ul>
     * <li><code>CASE WHEN p THEN 1 WHEN TRUE THEN 2 WHEN q … ELSE … END</code> is equivalent to <code>CASE WHEN p THEN 1 ELSE 2 END</code></li>
     * <li><code>CASE WHEN p THEN 1 WHEN FALSE THEN 2 WHEN q .. ELSE .. END</code> is equivalent to <code>CASE WHEN p THEN 1 WHEN q … ELSE … END</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsUnreachableCaseClauses() {
        return transformPatternsUnreachableCaseClauses;
    }

    /**
     * Transform <code>CASE</code> by removing unreachable clauses.
     * <p>
     * Case clauses can be proven to be unreachable, and thus removed:
     * <ul>
     * <li><code>CASE WHEN p THEN 1 WHEN TRUE THEN 2 WHEN q … ELSE … END</code> is equivalent to <code>CASE WHEN p THEN 1 ELSE 2 END</code></li>
     * <li><code>CASE WHEN p THEN 1 WHEN FALSE THEN 2 WHEN q .. ELSE .. END</code> is equivalent to <code>CASE WHEN p THEN 1 WHEN q … ELSE … END</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsUnreachableCaseClauses(Boolean value) {
        this.transformPatternsUnreachableCaseClauses = value;
    }

    /**
     * Transform <code>DECODE</code> by removing unreachable clauses.
     * <p>
     * DECODE clauses can be proven to be unreachable, and thus removed:
     * <ul>
     * <li><code>DECODE(a, b, 1, c, 2, b, 3)</code> is equivalent to <code>DECODE(a, b, 1, c, 2)</code></li>
     * <li><code>DECODE(a, b, 1, c, 2, b, 3, 4)</code> is equivalent to <code>DECODE(a, b, 1, c, 2, 4)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsUnreachableDecodeClauses() {
        return transformPatternsUnreachableDecodeClauses;
    }

    /**
     * Transform <code>DECODE</code> by removing unreachable clauses.
     * <p>
     * DECODE clauses can be proven to be unreachable, and thus removed:
     * <ul>
     * <li><code>DECODE(a, b, 1, c, 2, b, 3)</code> is equivalent to <code>DECODE(a, b, 1, c, 2)</code></li>
     * <li><code>DECODE(a, b, 1, c, 2, b, 3, 4)</code> is equivalent to <code>DECODE(a, b, 1, c, 2, 4)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsUnreachableDecodeClauses(Boolean value) {
        this.transformPatternsUnreachableDecodeClauses = value;
    }

    /**
     * Transform <code>CASE WHEN a IS NOT DISTINCT FROM b …</code> to an equivalent <code>DECODE</code> function.
     * <p>
     * When all <code>WHEN</code> clauses of a <code>CASE</code> expression use the <code>DISTINCT</code> predicate, then the
     * <code>CASE</code> expression can be transformed into a <code>DECODE</code> function call:
     * <ul>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 END</code> is equivalent to <code>DECODE(a, b, 1)</code></li>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 ELSE 2 END</code> is equivalent to <code>DECODE(a, b, 1, 2)</code></li>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 WHEN a IS NOT DISTINCT FROM c THEN 2 END</code> is equivalent to <code>DECODE(a, b, 1, c, 2)</code></li>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 WHEN a IS NOT DISTINCT FROM c THEN 2 ELSE 3 END</code> is equivalent to <code>DECODE(a, b, 1, c, 2, 3)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsCaseDistinctToDecode() {
        return transformPatternsCaseDistinctToDecode;
    }

    /**
     * Transform <code>CASE WHEN a IS NOT DISTINCT FROM b …</code> to an equivalent <code>DECODE</code> function.
     * <p>
     * When all <code>WHEN</code> clauses of a <code>CASE</code> expression use the <code>DISTINCT</code> predicate, then the
     * <code>CASE</code> expression can be transformed into a <code>DECODE</code> function call:
     * <ul>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 END</code> is equivalent to <code>DECODE(a, b, 1)</code></li>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 ELSE 2 END</code> is equivalent to <code>DECODE(a, b, 1, 2)</code></li>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 WHEN a IS NOT DISTINCT FROM c THEN 2 END</code> is equivalent to <code>DECODE(a, b, 1, c, 2)</code></li>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 WHEN a IS NOT DISTINCT FROM c THEN 2 ELSE 3 END</code> is equivalent to <code>DECODE(a, b, 1, c, 2, 3)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsCaseDistinctToDecode(Boolean value) {
        this.transformPatternsCaseDistinctToDecode = value;
    }

    /**
     * Transform <code>CASE WHEN a THEN x WHEN b THEN x END</code> to <code>CASE WHEN a OR b THEN x END</code>.
     * <p>
     * Two consecutive <code>WHEN</code> clauses can be merged, if their respective <code>THEN</code> clause is identical.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsCaseMergeWhenWhen() {
        return transformPatternsCaseMergeWhenWhen;
    }

    /**
     * Transform <code>CASE WHEN a THEN x WHEN b THEN x END</code> to <code>CASE WHEN a OR b THEN x END</code>.
     * <p>
     * Two consecutive <code>WHEN</code> clauses can be merged, if their respective <code>THEN</code> clause is identical.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsCaseMergeWhenWhen(Boolean value) {
        this.transformPatternsCaseMergeWhenWhen = value;
    }

    /**
     * Transform <code>CASE WHEN a THEN x WHEN b THEN y ELSE y END</code> to <code>CASE WHEN a THEN x ELSE y END</code>.
     * <p>
     * The ultimate <code>WHEN</code> clause can be merged with the <code>ELSE</code>, if their respective result is identical.
     * If the <code>WHEN</code> clause is the only <code>WHEN</code> clause, then the entire <code>CASE</code> expression can
     * be replaced by the <code>ELSE</code> clause content.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsCaseMergeWhenElse() {
        return transformPatternsCaseMergeWhenElse;
    }

    /**
     * Transform <code>CASE WHEN a THEN x WHEN b THEN y ELSE y END</code> to <code>CASE WHEN a THEN x ELSE y END</code>.
     * <p>
     * The ultimate <code>WHEN</code> clause can be merged with the <code>ELSE</code>, if their respective result is identical.
     * If the <code>WHEN</code> clause is the only <code>WHEN</code> clause, then the entire <code>CASE</code> expression can
     * be replaced by the <code>ELSE</code> clause content.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsCaseMergeWhenElse(Boolean value) {
        this.transformPatternsCaseMergeWhenElse = value;
    }

    /**
     * Transform <code>CASE</code> expressions to their respective abbreviations.
     * <p>
     * Some <code>CASE</code> expressions have a shorter abbreviated form, such as <code>COALESCE()</code> or <code>NULLIF()</code>.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsCaseToCaseAbbreviation() {
        return transformPatternsCaseToCaseAbbreviation;
    }

    /**
     * Transform <code>CASE</code> expressions to their respective abbreviations.
     * <p>
     * Some <code>CASE</code> expressions have a shorter abbreviated form, such as <code>COALESCE()</code> or <code>NULLIF()</code>.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsCaseToCaseAbbreviation(Boolean value) {
        this.transformPatternsCaseToCaseAbbreviation = value;
    }

    /**
     * Transform complex predicates into simpler <code>CASE</code> abbreviations.
     * <p>
     * Some predicates can be simplified into case abbreviations, such as, for example
     * <ul>
     * <li><code>a IS NULL OR COALESCE(a = b, FALSE)</code> to <code>NULLIF(a, b) IS NULL</code></li>
     * <li><code>a IS NOT NULL AND COALESCE(a != b, TRUE)</code> to <code>NULLIF(a, b) IS NOT NULL</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsSimplifyCaseAbbreviation() {
        return transformPatternsSimplifyCaseAbbreviation;
    }

    /**
     * Transform complex predicates into simpler <code>CASE</code> abbreviations.
     * <p>
     * Some predicates can be simplified into case abbreviations, such as, for example
     * <ul>
     * <li><code>a IS NULL OR COALESCE(a = b, FALSE)</code> to <code>NULLIF(a, b) IS NULL</code></li>
     * <li><code>a IS NOT NULL AND COALESCE(a != b, TRUE)</code> to <code>NULLIF(a, b) IS NOT NULL</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsSimplifyCaseAbbreviation(Boolean value) {
        this.transformPatternsSimplifyCaseAbbreviation = value;
    }

    /**
     * Flatten nested <code>CASE</code> abbreviations such as <code>NVL</code> or <code>CASE</code>.
     * <p>
     * Nested <code>CASE</code> abbreviations can be flattened, as such:
     * <ul>
     * <li><code>NVL(NVL(a, b), c)</code> to <code>COALESCE(a, b, c)</code></li>
     * <li><code>COALESCE(a, ..., COALESCE(b, ..., c), ..., d)</code> to <code>COALESCE(a, …, b, …, c, ..., d)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsFlattenCaseAbbreviation() {
        return transformPatternsFlattenCaseAbbreviation;
    }

    /**
     * Flatten nested <code>CASE</code> abbreviations such as <code>NVL</code> or <code>CASE</code>.
     * <p>
     * Nested <code>CASE</code> abbreviations can be flattened, as such:
     * <ul>
     * <li><code>NVL(NVL(a, b), c)</code> to <code>COALESCE(a, b, c)</code></li>
     * <li><code>COALESCE(a, ..., COALESCE(b, ..., c), ..., d)</code> to <code>COALESCE(a, …, b, …, c, ..., d)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsFlattenCaseAbbreviation(Boolean value) {
        this.transformPatternsFlattenCaseAbbreviation = value;
    }

    /**
     * Flatten nested <code>DECODE</code> functions.
     * <p>
     * Nested <code>DECODE</code> functions can be flattened, as such:
     * <ul>
     * <li><code>DECODE(a, b, c, DECODE(a, d, e))</code> to <code>DECODE(a, b, c, d, e)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsFlattenDecode() {
        return transformPatternsFlattenDecode;
    }

    /**
     * Flatten nested <code>DECODE</code> functions.
     * <p>
     * Nested <code>DECODE</code> functions can be flattened, as such:
     * <ul>
     * <li><code>DECODE(a, b, c, DECODE(a, d, e))</code> to <code>DECODE(a, b, c, d, e)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsFlattenDecode(Boolean value) {
        this.transformPatternsFlattenDecode = value;
    }

    /**
     * Transform <code>CASE … ELSE CASE …</code> by flattening the nested <code>CASE</code>.
     * <p>
     * <code>CASE WHEN a THEN b ELSE CASE WHEN c THEN d END END</code> is equivalent to <code>CASE WHEN a THEN b WHEN c THEN d END</code>.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsFlattenCase() {
        return transformPatternsFlattenCase;
    }

    /**
     * Transform <code>CASE … ELSE CASE …</code> by flattening the nested <code>CASE</code>.
     * <p>
     * <code>CASE WHEN a THEN b ELSE CASE WHEN c THEN d END END</code> is equivalent to <code>CASE WHEN a THEN b WHEN c THEN d END</code>.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsFlattenCase(Boolean value) {
        this.transformPatternsFlattenCase = value;
    }

    /**
     * Transform trivial case abbreviations like <code>NVL(NULL, a)</code> to <code>a</code>.
     * <p>
     * This transformation removes any trivial case abbreviations, such as <code>NVL()</code>,
     * <code>COALESCE()</code>, <code>NULLIF()</code>, etc.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsTrivialCaseAbbreviation() {
        return transformPatternsTrivialCaseAbbreviation;
    }

    /**
     * Transform trivial case abbreviations like <code>NVL(NULL, a)</code> to <code>a</code>.
     * <p>
     * This transformation removes any trivial case abbreviations, such as <code>NVL()</code>,
     * <code>COALESCE()</code>, <code>NULLIF()</code>, etc.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsTrivialCaseAbbreviation(Boolean value) {
        this.transformPatternsTrivialCaseAbbreviation = value;
    }

    /**
     * Transform trivial predicates like <code>1 = 1</code> to <code>TRUE</code>.
     * <p>
     * This transformation removes any trivial predicates.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsTrivialPredicates() {
        return transformPatternsTrivialPredicates;
    }

    /**
     * Transform trivial predicates like <code>1 = 1</code> to <code>TRUE</code>.
     * <p>
     * This transformation removes any trivial predicates.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsTrivialPredicates(Boolean value) {
        this.transformPatternsTrivialPredicates = value;
    }

    /**
     * Transform trivial bitwise comparisons like <code>BIT_OR(a, 0)</code> to <code>a</code>.
     * <p>
     * This transformation removes any trivial predicates.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsTrivialBitwiseOperations() {
        return transformPatternsTrivialBitwiseOperations;
    }

    /**
     * Transform trivial bitwise comparisons like <code>BIT_OR(a, 0)</code> to <code>a</code>.
     * <p>
     * This transformation removes any trivial predicates.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsTrivialBitwiseOperations(Boolean value) {
        this.transformPatternsTrivialBitwiseOperations = value;
    }

    /**
     * Transform bitwise operations to an equivalent <code>BIT_SET(a, b)</code> or <code>BIT_SET(a, b, c)</code> expression.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsBitSet() {
        return transformPatternsBitSet;
    }

    /**
     * Transform bitwise operations to an equivalent <code>BIT_SET(a, b)</code> or <code>BIT_SET(a, b, c)</code> expression.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsBitSet(Boolean value) {
        this.transformPatternsBitSet = value;
    }

    /**
     * Transform bitwise operations to an equivalent <code>BIT_GET(a, b)</code> expression.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsBitGet() {
        return transformPatternsBitGet;
    }

    /**
     * Transform bitwise operations to an equivalent <code>BIT_GET(a, b)</code> expression.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsBitGet(Boolean value) {
        this.transformPatternsBitGet = value;
    }

    /**
     * Transform predicates comparing scalar subqueries with a count <code>(SELECT COUNT(*) …) > 0</code> to equivalent <code>EXISTS (SELECT 1 …)</code>.
     * <p>
     * Scalar subqueries that count rows and whose count is compared to 0 can be transformed into equivalent, but likely cheaper to execute EXISTS queries.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsScalarSubqueryCountAsteriskGtZero() {
        return transformPatternsScalarSubqueryCountAsteriskGtZero;
    }

    /**
     * Transform predicates comparing scalar subqueries with a count <code>(SELECT COUNT(*) …) > 0</code> to equivalent <code>EXISTS (SELECT 1 …)</code>.
     * <p>
     * Scalar subqueries that count rows and whose count is compared to 0 can be transformed into equivalent, but likely cheaper to execute EXISTS queries.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsScalarSubqueryCountAsteriskGtZero(Boolean value) {
        this.transformPatternsScalarSubqueryCountAsteriskGtZero = value;
    }

    /**
     * Transform predicates comparing scalar subqueries with a count <code>(SELECT COUNT(expr) …) > 0</code> to equivalent <code>EXISTS (SELECT 1 … WHERE expr IS NOT NULL)</code>.
     * <p>
     * Scalar subqueries that count non-null expressions and whose count is compared to 0 can be transformed into equivalent, but likely cheaper to execute EXISTS queries.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsScalarSubqueryCountExpressionGtZero() {
        return transformPatternsScalarSubqueryCountExpressionGtZero;
    }

    /**
     * Transform predicates comparing scalar subqueries with a count <code>(SELECT COUNT(expr) …) > 0</code> to equivalent <code>EXISTS (SELECT 1 … WHERE expr IS NOT NULL)</code>.
     * <p>
     * Scalar subqueries that count non-null expressions and whose count is compared to 0 can be transformed into equivalent, but likely cheaper to execute EXISTS queries.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsScalarSubqueryCountExpressionGtZero(Boolean value) {
        this.transformPatternsScalarSubqueryCountExpressionGtZero = value;
    }

    /**
     * Transform empty scalar subqueries like <code>(SELECT 1 WHERE FALSE)</code> to <code>NULL</code>.
     * <p>
     * Scalar subqueries that are guaranteed to produce no results can be replaced by a <code>NULL</code> value.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsEmptyScalarSubquery() {
        return transformPatternsEmptyScalarSubquery;
    }

    /**
     * Transform empty scalar subqueries like <code>(SELECT 1 WHERE FALSE)</code> to <code>NULL</code>.
     * <p>
     * Scalar subqueries that are guaranteed to produce no results can be replaced by a <code>NULL</code> value.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsEmptyScalarSubquery(Boolean value) {
        this.transformPatternsEmptyScalarSubquery = value;
    }

    /**
     * Transform <code>-(-(x))</code> to <code>x</code>
     * <p>
     * This transformation removes a redundant arithmetic negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNegNeg() {
        return transformPatternsNegNeg;
    }

    /**
     * Transform <code>-(-(x))</code> to <code>x</code>
     * <p>
     * This transformation removes a redundant arithmetic negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNegNeg(Boolean value) {
        this.transformPatternsNegNeg = value;
    }

    /**
     * Transform <code>~(~(x))</code> to <code>x</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsBitNotBitNot() {
        return transformPatternsBitNotBitNot;
    }

    /**
     * Transform <code>~(~(x))</code> to <code>x</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsBitNotBitNot(Boolean value) {
        this.transformPatternsBitNotBitNot = value;
    }

    /**
     * Transform <code>~(bitnand(x, y))</code> to <code>bitand(x, y)</code> and <code>~(bitand(x, y)</code> to <code>bitnand(x, y)</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsBitNotBitNand() {
        return transformPatternsBitNotBitNand;
    }

    /**
     * Transform <code>~(bitnand(x, y))</code> to <code>bitand(x, y)</code> and <code>~(bitand(x, y)</code> to <code>bitnand(x, y)</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsBitNotBitNand(Boolean value) {
        this.transformPatternsBitNotBitNand = value;
    }

    /**
     * Transform <code>~(bitnor(x, y))</code> to <code>bitor(x, y)</code> and <code>~(bitor(x, y)</code> to <code>bitnor(x, y)</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsBitNotBitNor() {
        return transformPatternsBitNotBitNor;
    }

    /**
     * Transform <code>~(bitnor(x, y))</code> to <code>bitor(x, y)</code> and <code>~(bitor(x, y)</code> to <code>bitnor(x, y)</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsBitNotBitNor(Boolean value) {
        this.transformPatternsBitNotBitNor = value;
    }

    /**
     * Transform <code>~(bitxnor(x, y))</code> to <code>bitxor(x, y)</code> and <code>~(bitxor(x, y)</code> to <code>bitxnor(x, y)</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsBitNotBitXNor() {
        return transformPatternsBitNotBitXNor;
    }

    /**
     * Transform <code>~(bitxnor(x, y))</code> to <code>bitxor(x, y)</code> and <code>~(bitxor(x, y)</code> to <code>bitxnor(x, y)</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsBitNotBitXNor(Boolean value) {
        this.transformPatternsBitNotBitXNor = value;
    }

    /**
     * Any {org.jooq.impl.QOM.UReturnsNullOnNullInput} function or expression with <code>NULL</code> arguments can be replaced by <code>NULL</code>.
     * <p>
     * There are many built-in SQL functions and operators with a <code>RETURNS NULL ON NULL INPUT</code> property, e.g.
     * <ul>
     * <li><code>ABS(NULL)</code></li>
     * <li><code>MOD(NULL, 1)</code></li>
     * <li><code>NULL + 1</code></li>
     * </ul>
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsNullOnNullInput() {
        return transformPatternsNullOnNullInput;
    }

    /**
     * Any {org.jooq.impl.QOM.UReturnsNullOnNullInput} function or expression with <code>NULL</code> arguments can be replaced by <code>NULL</code>.
     * <p>
     * There are many built-in SQL functions and operators with a <code>RETURNS NULL ON NULL INPUT</code> property, e.g.
     * <ul>
     * <li><code>ABS(NULL)</code></li>
     * <li><code>MOD(NULL, 1)</code></li>
     * <li><code>NULL + 1</code></li>
     * </ul>
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsNullOnNullInput(Boolean value) {
        this.transformPatternsNullOnNullInput = value;
    }

    /**
     * Transform all repetitions of idempotent functions, such as <code>UPPER(UPPER(s))</code> to <code>UPPER(s)</code>.
     * <p>
     * Idempotent functions that are covered so far, include:
     * <ul>
     * <li><code>LTRIM(LTRIM(s))</code> to <code>LTRIM(s)</code></li>
     * <li><code>LTRIM(TRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>RTRIM(RTRIM(s))</code> to <code>RTRIM(s)</code></li>
     * <li><code>RTRIM(TRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>TRIM(LTRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>TRIM(RTRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>UPPER(UPPER(s))</code> to <code>UPPER(s)</code></li>
     * <li><code>LOWER(LOWER(s))</code> to <code>LOWER(s)</code></li>
     * </ul>
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsIdempotentFunctionRepetition() {
        return transformPatternsIdempotentFunctionRepetition;
    }

    /**
     * Transform all repetitions of idempotent functions, such as <code>UPPER(UPPER(s))</code> to <code>UPPER(s)</code>.
     * <p>
     * Idempotent functions that are covered so far, include:
     * <ul>
     * <li><code>LTRIM(LTRIM(s))</code> to <code>LTRIM(s)</code></li>
     * <li><code>LTRIM(TRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>RTRIM(RTRIM(s))</code> to <code>RTRIM(s)</code></li>
     * <li><code>RTRIM(TRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>TRIM(LTRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>TRIM(RTRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>UPPER(UPPER(s))</code> to <code>UPPER(s)</code></li>
     * <li><code>LOWER(LOWER(s))</code> to <code>LOWER(s)</code></li>
     * </ul>
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsIdempotentFunctionRepetition(Boolean value) {
        this.transformPatternsIdempotentFunctionRepetition = value;
    }

    /**
     * Transform <code>a + 1 = 2</code> to <code>a = 2 - 1</code>, and other transformations.
     * <p>
     * It is usually best to compare single columns with constants or expressions to
     * encourage index usage. While function based indexes are possible in some RDBMS,
     * ordinary indexes are more reusable and should be preferred.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsArithmeticComparisons() {
        return transformPatternsArithmeticComparisons;
    }

    /**
     * Transform <code>a + 1 = 2</code> to <code>a = 2 - 1</code>, and other transformations.
     * <p>
     * It is usually best to compare single columns with constants or expressions to
     * encourage index usage. While function based indexes are possible in some RDBMS,
     * ordinary indexes are more reusable and should be preferred.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsArithmeticComparisons(Boolean value) {
        this.transformPatternsArithmeticComparisons = value;
    }

    /**
     * Transform <code>1 / y * x</code> to <code>x / y</code>, and other transformations.
     * <p>
     * This transformation simplifies arithmetic expressions.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsArithmeticExpressions() {
        return transformPatternsArithmeticExpressions;
    }

    /**
     * Transform <code>1 / y * x</code> to <code>x / y</code>, and other transformations.
     * <p>
     * This transformation simplifies arithmetic expressions.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsArithmeticExpressions(Boolean value) {
        this.transformPatternsArithmeticExpressions = value;
    }

    /**
     * Transform <code>SIN(x) / COS(x)</code> to <code>TAN(x)</code>, and other transformations.
     * <p>
     * This transformation turns expanded trignonometric function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsTrigonometricFunctions() {
        return transformPatternsTrigonometricFunctions;
    }

    /**
     * Transform <code>SIN(x) / COS(x)</code> to <code>TAN(x)</code>, and other transformations.
     * <p>
     * This transformation turns expanded trignonometric function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsTrigonometricFunctions(Boolean value) {
        this.transformPatternsTrigonometricFunctions = value;
    }

    /**
     * Transform <code>LN(value) / LN(base)</code> to <code>LOG(base, value)</code>, and other transformations.
     * <p>
     * This transformation turns expanded logarithmic function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsLogarithmicFunctions() {
        return transformPatternsLogarithmicFunctions;
    }

    /**
     * Transform <code>LN(value) / LN(base)</code> to <code>LOG(base, value)</code>, and other transformations.
     * <p>
     * This transformation turns expanded logarithmic function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsLogarithmicFunctions(Boolean value) {
        this.transformPatternsLogarithmicFunctions = value;
    }

    /**
     * Transform <code>(EXP(x) - EXP(-x)) / 2</code> to <code>SINH(x)</code>, and other transformations.
     * <p>
     * This transformation turns expanded hyperbolic function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsHyperbolicFunctions() {
        return transformPatternsHyperbolicFunctions;
    }

    /**
     * Transform <code>(EXP(x) - EXP(-x)) / 2</code> to <code>SINH(x)</code>, and other transformations.
     * <p>
     * This transformation turns expanded hyperbolic function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsHyperbolicFunctions(Boolean value) {
        this.transformPatternsHyperbolicFunctions = value;
    }

    /**
     * Transform <code>LN(x + SQRT(SQUARE(x) + 1))</code> to <code>ASINH(x)</code>, and other transformations.
     * <p>
     * This transformation turns expanded inverse hyperbolic function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformPatternsInverseHyperbolicFunctions() {
        return transformPatternsInverseHyperbolicFunctions;
    }

    /**
     * Transform <code>LN(x + SQRT(SQUARE(x) + 1))</code> to <code>ASINH(x)</code>, and other transformations.
     * <p>
     * This transformation turns expanded inverse hyperbolic function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformPatternsInverseHyperbolicFunctions(Boolean value) {
        this.transformPatternsInverseHyperbolicFunctions = value;
    }

    /**
     * Transform {@link org.jooq.impl.QOM.CompareCondition} and a few other types of condition to inline their bind values, in case they match
     *          
     * <p>
     * Historically, prior to ANSI join syntax, joins were implemented by listing tables in 
     * the FROM clause and providing join predicates in the WHERE clause, possibly using vendor specific
     * operators like <code>(+)</code> (Oracle, DB2) or <code>*=</code> (SQL Server) for outer join
     * support. For backwards compatibility with older RDBMS versions, ANSI joins in jOOQ code may be
     * converted to equivalent table lists in generated SQL using this flag.
     * <p>
     * This flag has a limited implementation that supports inner joins (in most cases) and outer joins
     * (only for simple comparison predicates).
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformInlineBindValuesForFieldComparisons() {
        return transformInlineBindValuesForFieldComparisons;
    }

    /**
     * Transform {@link org.jooq.impl.QOM.CompareCondition} and a few other types of condition to inline their bind values, in case they match
     *          
     * <p>
     * Historically, prior to ANSI join syntax, joins were implemented by listing tables in 
     * the FROM clause and providing join predicates in the WHERE clause, possibly using vendor specific
     * operators like <code>(+)</code> (Oracle, DB2) or <code>*=</code> (SQL Server) for outer join
     * support. For backwards compatibility with older RDBMS versions, ANSI joins in jOOQ code may be
     * converted to equivalent table lists in generated SQL using this flag.
     * <p>
     * This flag has a limited implementation that supports inner joins (in most cases) and outer joins
     * (only for simple comparison predicates).
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformInlineBindValuesForFieldComparisons(Boolean value) {
        this.transformInlineBindValuesForFieldComparisons = value;
    }

    /**
     * Transform ANSI join to table lists if possible.
     * <p>
     * Historically, prior to ANSI join syntax, joins were implemented by listing tables in 
     * the FROM clause and providing join predicates in the WHERE clause, possibly using vendor specific
     * operators like <code>(+)</code> (Oracle, DB2) or <code>*=</code> (SQL Server) for outer join
     * support. For backwards compatibility with older RDBMS versions, ANSI joins in jOOQ code may be
     * converted to equivalent table lists in generated SQL using this flag.
     * <p>
     * This flag has a limited implementation that supports inner joins (in most cases) and outer joins
     * (only for simple comparison predicates).
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformAnsiJoinToTableLists() {
        return transformAnsiJoinToTableLists;
    }

    /**
     * Transform ANSI join to table lists if possible.
     * <p>
     * Historically, prior to ANSI join syntax, joins were implemented by listing tables in 
     * the FROM clause and providing join predicates in the WHERE clause, possibly using vendor specific
     * operators like <code>(+)</code> (Oracle, DB2) or <code>*=</code> (SQL Server) for outer join
     * support. For backwards compatibility with older RDBMS versions, ANSI joins in jOOQ code may be
     * converted to equivalent table lists in generated SQL using this flag.
     * <p>
     * This flag has a limited implementation that supports inner joins (in most cases) and outer joins
     * (only for simple comparison predicates).
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformAnsiJoinToTableLists(Boolean value) {
        this.transformAnsiJoinToTableLists = value;
    }

    /**
     * Transform a subquery from an IN condition with LIMIT to an equivalent derived table.
     * <p>
     * This transformation works around a known MySQL limitation "ERROR 1235 (42000): This version of MySQL doesn't yet support 'LIMIT & IN/ALL/ANY/SOME subquery'"
     * <p>
     * This feature is available in the commercial distribution only.
     * <p>
     * @deprecated - 3.18.0 - [#14634] - The configuration of this transformation is deprecated. It will no longer be commercially available only, but apply also to the jOOQ Open Source Edition, when required.
     *               
     * 
     */
    @Deprecated
    public Transformation getTransformInConditionSubqueryWithLimitToDerivedTable() {
        return transformInConditionSubqueryWithLimitToDerivedTable;
    }

    /**
     * Transform a subquery from an IN condition with LIMIT to an equivalent derived table.
     * <p>
     * This transformation works around a known MySQL limitation "ERROR 1235 (42000): This version of MySQL doesn't yet support 'LIMIT & IN/ALL/ANY/SOME subquery'"
     * <p>
     * This feature is available in the commercial distribution only.
     * <p>
     * @deprecated - 3.18.0 - [#14634] - The configuration of this transformation is deprecated. It will no longer be commercially available only, but apply also to the jOOQ Open Source Edition, when required.
     *               
     * 
     */
    @Deprecated
    public void setTransformInConditionSubqueryWithLimitToDerivedTable(Transformation value) {
        this.transformInConditionSubqueryWithLimitToDerivedTable = value;
    }

    /**
     * Transform the <code>QUALIFY</code> clause to an equivalent derived table to filter on window functions.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Transformation getTransformQualify() {
        return transformQualify;
    }

    /**
     * Transform the <code>QUALIFY</code> clause to an equivalent derived table to filter on window functions.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setTransformQualify(Transformation value) {
        this.transformQualify = value;
    }

    /**
     * Transform table lists to ANSI join if possible.
     * <p>
     * (Very) historically, prior to ANSI join syntax, joins were implemented by listing tables in 
     * the FROM clause and providing join predicates in the WHERE clause, possibly using vendor specific
     * operators like <code>(+)</code> (Oracle, DB2) or <code>*=</code> (SQL Server) for outer join
     * support. Migrating such join syntax is tedious. The jOOQ parser can parse the old syntax and
     * this flag enables the transformation to ANSI join syntax.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTransformTableListsToAnsiJoin() {
        return transformTableListsToAnsiJoin;
    }

    /**
     * Transform table lists to ANSI join if possible.
     * <p>
     * (Very) historically, prior to ANSI join syntax, joins were implemented by listing tables in 
     * the FROM clause and providing join predicates in the WHERE clause, possibly using vendor specific
     * operators like <code>(+)</code> (Oracle, DB2) or <code>*=</code> (SQL Server) for outer join
     * support. Migrating such join syntax is tedious. The jOOQ parser can parse the old syntax and
     * this flag enables the transformation to ANSI join syntax.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTransformTableListsToAnsiJoin(Boolean value) {
        this.transformTableListsToAnsiJoin = value;
    }

    /**
     * Transform <code>ROWNUM</code> expressions to corresponding <code>LIMIT</code> clauses or <code>ROW_NUMBER()</code> expressions.
     * <p>
     * In Oracle 11g and less, <code>ROWNUM</code> filtering was the most popular way to paginate. This pseudo
     * column is not supported in other RDBMS, and should be replaced in Oracle 12c by the FETCH clause or
     * <code>ROW_NUMBER() OVER ()</code> filtering. This transformation allows for replacing such a filter by
     * equivalent SQL, if possible.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Transformation getTransformRownum() {
        return transformRownum;
    }

    /**
     * Transform <code>ROWNUM</code> expressions to corresponding <code>LIMIT</code> clauses or <code>ROW_NUMBER()</code> expressions.
     * <p>
     * In Oracle 11g and less, <code>ROWNUM</code> filtering was the most popular way to paginate. This pseudo
     * column is not supported in other RDBMS, and should be replaced in Oracle 12c by the FETCH clause or
     * <code>ROW_NUMBER() OVER ()</code> filtering. This transformation allows for replacing such a filter by
     * equivalent SQL, if possible.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setTransformRownum(Transformation value) {
        this.transformRownum = value;
    }

    /**
     * Transform arithmetic expressions on literals and bind variables.
     * <p>
     * Arithmetic expressions may be implemented by the user, or arise from emulations from within jOOQ.
     * Expressions on literals and bind variables could be evaluated in the client prior to generating SQL.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public TransformUnneededArithmeticExpressions getTransformUnneededArithmeticExpressions() {
        return transformUnneededArithmeticExpressions;
    }

    /**
     * Transform arithmetic expressions on literals and bind variables.
     * <p>
     * Arithmetic expressions may be implemented by the user, or arise from emulations from within jOOQ.
     * Expressions on literals and bind variables could be evaluated in the client prior to generating SQL.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setTransformUnneededArithmeticExpressions(TransformUnneededArithmeticExpressions value) {
        this.transformUnneededArithmeticExpressions = value;
    }

    /**
     * Transform <code>GROUP BY [column index]</code> clauses by substituting the column index.
     * <p>
     * Not all dialects support grouping by column index, which is a convenient but also a bit confusing feature of
     * some dialects. jOOQ can transform the syntax into an equivalent syntax where the referenced <code>SELECT</code>
     * expression is duplicated into the <code>GROUP BY</code> clause.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Transformation getTransformGroupByColumnIndex() {
        return transformGroupByColumnIndex;
    }

    /**
     * Transform <code>GROUP BY [column index]</code> clauses by substituting the column index.
     * <p>
     * Not all dialects support grouping by column index, which is a convenient but also a bit confusing feature of
     * some dialects. jOOQ can transform the syntax into an equivalent syntax where the referenced <code>SELECT</code>
     * expression is duplicated into the <code>GROUP BY</code> clause.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setTransformGroupByColumnIndex(Transformation value) {
        this.transformGroupByColumnIndex = value;
    }

    /**
     * Transform Common Table Expressions (CTE) by inlining their <code>WITH</code> clause definition to wherever they're referenced.
     * <p>
     * Non-recursive CTE are just syntax sugar for inline views (derived tables). When they're not supported natively,
     * jOOQ can simply inline their definition to wherever they're referenced.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Transformation getTransformInlineCTE() {
        return transformInlineCTE;
    }

    /**
     * Transform Common Table Expressions (CTE) by inlining their <code>WITH</code> clause definition to wherever they're referenced.
     * <p>
     * Non-recursive CTE are just syntax sugar for inline views (derived tables). When they're not supported natively,
     * jOOQ can simply inline their definition to wherever they're referenced.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setTransformInlineCTE(Transformation value) {
        this.transformInlineCTE = value;
    }

    /**
     * Whether string literals should be escaped with backslash.
     * 
     */
    public BackslashEscaping getBackslashEscaping() {
        return backslashEscaping;
    }

    /**
     * Whether string literals should be escaped with backslash.
     * 
     */
    public void setBackslashEscaping(BackslashEscaping value) {
        this.backslashEscaping = value;
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
     */
    public ParamType getParamType() {
        return paramType;
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
     */
    public void setParamType(ParamType value) {
        this.paramType = value;
    }

    /**
     * Whether rendered bind values should be cast to their respective type.
     * 
     */
    public ParamCastMode getParamCastMode() {
        return paramCastMode;
    }

    /**
     * Whether rendered bind values should be cast to their respective type.
     * 
     */
    public void setParamCastMode(ParamCastMode value) {
        this.paramCastMode = value;
    }

    /**
     * The type of statement that is to be executed.
     * 
     */
    public StatementType getStatementType() {
        return statementType;
    }

    /**
     * The type of statement that is to be executed.
     * 
     */
    public void setStatementType(StatementType value) {
        this.statementType = value;
    }

    /**
     * The maximum number of allowed bind variables before inlining all values where <code>0</code> uses the dialect defaults: <ul>
     * <li>{@link org.jooq.SQLDialect#ACCESS} : 768</li>
     * <li>{@link org.jooq.SQLDialect#ASE} : 2000</li>
     * <li>{@link org.jooq.SQLDialect#INGRES} : 1024</li>
     * <li>{@link org.jooq.SQLDialect#ORACLE} : 32767</li>
     * <li>{@link org.jooq.SQLDialect#POSTGRES} : 32767</li>
     * <li>{@link org.jooq.SQLDialect#SQLITE} : 999</li>
     * <li>{@link org.jooq.SQLDialect#SQLSERVER} : 2100</li>
     * </ul>
     * 
     */
    public Integer getInlineThreshold() {
        return inlineThreshold;
    }

    /**
     * The maximum number of allowed bind variables before inlining all values where <code>0</code> uses the dialect defaults: <ul>
     * <li>{@link org.jooq.SQLDialect#ACCESS} : 768</li>
     * <li>{@link org.jooq.SQLDialect#ASE} : 2000</li>
     * <li>{@link org.jooq.SQLDialect#INGRES} : 1024</li>
     * <li>{@link org.jooq.SQLDialect#ORACLE} : 32767</li>
     * <li>{@link org.jooq.SQLDialect#POSTGRES} : 32767</li>
     * <li>{@link org.jooq.SQLDialect#SQLITE} : 999</li>
     * <li>{@link org.jooq.SQLDialect#SQLSERVER} : 2100</li>
     * </ul>
     * 
     */
    public void setInlineThreshold(Integer value) {
        this.inlineThreshold = value;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.TransactionListener}s.
     * 
     */
    public InvocationOrder getTransactionListenerStartInvocationOrder() {
        return transactionListenerStartInvocationOrder;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.TransactionListener}s.
     * 
     */
    public void setTransactionListenerStartInvocationOrder(InvocationOrder value) {
        this.transactionListenerStartInvocationOrder = value;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.TransactionListener}s.
     * 
     */
    public InvocationOrder getTransactionListenerEndInvocationOrder() {
        return transactionListenerEndInvocationOrder;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.TransactionListener}s.
     * 
     */
    public void setTransactionListenerEndInvocationOrder(InvocationOrder value) {
        this.transactionListenerEndInvocationOrder = value;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.MigrationListener}s.
     * 
     */
    public InvocationOrder getMigrationListenerStartInvocationOrder() {
        return migrationListenerStartInvocationOrder;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.MigrationListener}s.
     * 
     */
    public void setMigrationListenerStartInvocationOrder(InvocationOrder value) {
        this.migrationListenerStartInvocationOrder = value;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.MigrationListener}s.
     * 
     */
    public InvocationOrder getMigrationListenerEndInvocationOrder() {
        return migrationListenerEndInvocationOrder;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.MigrationListener}s.
     * 
     */
    public void setMigrationListenerEndInvocationOrder(InvocationOrder value) {
        this.migrationListenerEndInvocationOrder = value;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.VisitListener}s.
     * 
     */
    public InvocationOrder getVisitListenerStartInvocationOrder() {
        return visitListenerStartInvocationOrder;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.VisitListener}s.
     * 
     */
    public void setVisitListenerStartInvocationOrder(InvocationOrder value) {
        this.visitListenerStartInvocationOrder = value;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.VisitListener}s.
     * 
     */
    public InvocationOrder getVisitListenerEndInvocationOrder() {
        return visitListenerEndInvocationOrder;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.VisitListener}s.
     * 
     */
    public void setVisitListenerEndInvocationOrder(InvocationOrder value) {
        this.visitListenerEndInvocationOrder = value;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.RecordListener}s.
     * 
     */
    public InvocationOrder getRecordListenerStartInvocationOrder() {
        return recordListenerStartInvocationOrder;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.RecordListener}s.
     * 
     */
    public void setRecordListenerStartInvocationOrder(InvocationOrder value) {
        this.recordListenerStartInvocationOrder = value;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.RecordListener}s.
     * 
     */
    public InvocationOrder getRecordListenerEndInvocationOrder() {
        return recordListenerEndInvocationOrder;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.RecordListener}s.
     * 
     */
    public void setRecordListenerEndInvocationOrder(InvocationOrder value) {
        this.recordListenerEndInvocationOrder = value;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.ExecuteListener}s.
     * 
     */
    public InvocationOrder getExecuteListenerStartInvocationOrder() {
        return executeListenerStartInvocationOrder;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.ExecuteListener}s.
     * 
     */
    public void setExecuteListenerStartInvocationOrder(InvocationOrder value) {
        this.executeListenerStartInvocationOrder = value;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.ExecuteListener}s.
     * 
     */
    public InvocationOrder getExecuteListenerEndInvocationOrder() {
        return executeListenerEndInvocationOrder;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.ExecuteListener}s.
     * 
     */
    public void setExecuteListenerEndInvocationOrder(InvocationOrder value) {
        this.executeListenerEndInvocationOrder = value;
    }

    /**
     * When set to true, this will add jOOQ's default {@link org.jooq.tools.LoggerListener} for debug logging. This is meant for use in development only.
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
     * When set to true, this will add jOOQ's default {@link org.jooq.tools.LoggerListener} for debug logging. This is meant for use in development only.
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
     * [#14420] Whether constraint violations and other {@link java.sql.SQLException} should produce additional log information about the column name and data causing the problem. Unlike {@link #executeLogging}, this is meant for use in production as well as development. This feature is available only in commercial distributions.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isExecuteLoggingSQLExceptions() {
        return executeLoggingSQLExceptions;
    }

    /**
     * [#14420] Whether constraint violations and other {@link java.sql.SQLException} should produce additional log information about the column name and data causing the problem. Unlike {@link #executeLogging}, this is meant for use in production as well as development. This feature is available only in commercial distributions.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExecuteLoggingSQLExceptions(Boolean value) {
        this.executeLoggingSQLExceptions = value;
    }

    /**
     * When set to true, this will add jOOQ's default logging DiagnosticsListeners.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiagnosticsLogging() {
        return diagnosticsLogging;
    }

    /**
     * When set to true, this will add jOOQ's default logging DiagnosticsListeners.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiagnosticsLogging(Boolean value) {
        this.diagnosticsLogging = value;
    }

    /**
     * Whether to activate the DiagnosticsConnection, explicit by <code>DEFAULT</code>, implicit if <code>ON</code>, or turned <code>OFF</code> entirely.
     * 
     */
    public DiagnosticsConnection getDiagnosticsConnection() {
        return diagnosticsConnection;
    }

    /**
     * Whether to activate the DiagnosticsConnection, explicit by <code>DEFAULT</code>, implicit if <code>ON</code>, or turned <code>OFF</code> entirely.
     * 
     */
    public void setDiagnosticsConnection(DiagnosticsConnection value) {
        this.diagnosticsConnection = value;
    }

    /**
     * Whether store(), insert(), and update() methods should update the record version prior to the operation, for use with {@link #executeWithOptimisticLocking}.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUpdateRecordVersion() {
        return updateRecordVersion;
    }

    /**
     * Whether store(), insert(), and update() methods should update the record version prior to the operation, for use with {@link #executeWithOptimisticLocking}.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUpdateRecordVersion(Boolean value) {
        this.updateRecordVersion = value;
    }

    /**
     * Whether store(), insert(), and update() methods should update the record timestamp prior to the operation, for use with {@link #executeWithOptimisticLocking}.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUpdateRecordTimestamp() {
        return updateRecordTimestamp;
    }

    /**
     * Whether store(), insert(), and update() methods should update the record timestamp prior to the operation, for use with {@link #executeWithOptimisticLocking}.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUpdateRecordTimestamp(Boolean value) {
        this.updateRecordTimestamp = value;
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
     * Whether store() and delete() methods should be executed with optimistic locking.
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
     * Whether store() and delete() methods should be executed with optimistic locking also on "unversioned" tables,
     * i.e. on tables that do not have a version and/or timestamp column.
     * <p>
     * This flag has no effect when "executeWithOptimisticLocking" is turned off.
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
     * Whether fetched records should be attached to the fetching configuration.
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
     * Whether {@link org.jooq.TableRecord#insert()} calls should be executed if the record is unchanged. This also affects the <code>INSERT</code> part of {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#merge()} calls.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInsertUnchangedRecords() {
        return insertUnchangedRecords;
    }

    /**
     * Whether {@link org.jooq.TableRecord#insert()} calls should be executed if the record is unchanged. This also affects the <code>INSERT</code> part of {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#merge()} calls.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInsertUnchangedRecords(Boolean value) {
        this.insertUnchangedRecords = value;
    }

    /**
     * Whether {@link org.jooq.UpdatableRecord#update()} calls should be executed if the record is unchanged. This also affects the <code>UPDATE</code> part of {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#merge()} calls.
     * 
     */
    public UpdateUnchangedRecords getUpdateUnchangedRecords() {
        return updateUnchangedRecords;
    }

    /**
     * Whether {@link org.jooq.UpdatableRecord#update()} calls should be executed if the record is unchanged. This also affects the <code>UPDATE</code> part of {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#merge()} calls.
     * 
     */
    public void setUpdateUnchangedRecords(UpdateUnchangedRecords value) {
        this.updateUnchangedRecords = value;
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
     * Whether primary key values are deemed to be "updatable" in jOOQ.
     * <p>
     * Setting this to "true" will allow for updating primary key values through
     * UpdatableRecord.store() and UpdatableRecord.update().
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
     * Whether reflection information should be cached in the configuration.
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
     * Whether record mappers should be cached in the configuration.
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
     * Whether parsing connection translations should be cached in the configuration.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCacheParsingConnection() {
        return cacheParsingConnection;
    }

    /**
     * Whether parsing connection translations should be cached in the configuration.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCacheParsingConnection(Boolean value) {
        this.cacheParsingConnection = value;
    }

    /**
     * The default implementation of the ParsingConnection cache's LRU cache size.
     * 
     */
    public Integer getCacheParsingConnectionLRUCacheSize() {
        return cacheParsingConnectionLRUCacheSize;
    }

    /**
     * The default implementation of the ParsingConnection cache's LRU cache size.
     * 
     */
    public void setCacheParsingConnectionLRUCacheSize(Integer value) {
        this.cacheParsingConnectionLRUCacheSize = value;
    }

    /**
     * Whether JDBC {@link java.sql.PreparedStatement} instances should be cached in loader API.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCachePreparedStatementInLoader() {
        return cachePreparedStatementInLoader;
    }

    /**
     * Whether JDBC {@link java.sql.PreparedStatement} instances should be cached in loader API.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCachePreparedStatementInLoader(Boolean value) {
        this.cachePreparedStatementInLoader = value;
    }

    /**
     * A strategy defining how exceptions from the database / JDBC driver should be propagated
     * 
     */
    public ThrowExceptions getThrowExceptions() {
        return throwExceptions;
    }

    /**
     * A strategy defining how exceptions from the database / JDBC driver should be propagated
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
     * Whether warnings should be fetched after each query execution.
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
     */
    public Integer getFetchServerOutputSize() {
        return fetchServerOutputSize;
    }

    /**
     * Whether server output should be fetched after each query execution.
     * 
     */
    public void setFetchServerOutputSize(Integer value) {
        this.fetchServerOutputSize = value;
    }

    /**
     * Whether calls to store(), insert() and update() should return the identity column.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReturnIdentityOnUpdatableRecord() {
        return returnIdentityOnUpdatableRecord;
    }

    /**
     * Whether calls to store(), insert() and update() should return the identity column.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReturnIdentityOnUpdatableRecord(Boolean value) {
        this.returnIdentityOnUpdatableRecord = value;
    }

    /**
     * Whether calls to store(), insert() and update() should return values for columns that are {@link org.jooq.DataType#defaulted()}.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReturnDefaultOnUpdatableRecord() {
        return returnDefaultOnUpdatableRecord;
    }

    /**
     * Whether calls to store(), insert() and update() should return values for columns that are {@link org.jooq.DataType#defaulted()}.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReturnDefaultOnUpdatableRecord(Boolean value) {
        this.returnDefaultOnUpdatableRecord = value;
    }

    /**
     * Whether calls to store(), insert() and update() should return values for columns that are {@link org.jooq.DataType#computed()}.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReturnComputedOnUpdatableRecord() {
        return returnComputedOnUpdatableRecord;
    }

    /**
     * Whether calls to store(), insert() and update() should return values for columns that are {@link org.jooq.DataType#computed()}.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReturnComputedOnUpdatableRecord(Boolean value) {
        this.returnComputedOnUpdatableRecord = value;
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
     * Whether calls to store(), insert() and update() should return all columns, not just identity columns.
     * <p>
     * Do note that only few databases support this feature. It is supported only in case the INSERT's or UPDATE's
     * RETURNING clause is fully supported, also for non-IDENTITY columns.
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
     * IDENTITY values, and if {@link #returnAllOnUpdatableRecord} is active, also other values.
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
     * Whether calls to store(), insert(), update(), and delete() that are called on an UpdatableRecord
     * that is created from a POJO (e.g. in a DAO) should return all Record values to the POJO, including
     * IDENTITY values, and if {@link #returnAllOnUpdatableRecord} is active, also other values.
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
     * Whether JPA annotations should be considered by the DefaultRecordMapper.
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
     * Whether constructor parameter names obtained from the {@link java.lang.Record} component names should be considered by the DefaultRecordMapper.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMapRecordComponentParameterNames() {
        return mapRecordComponentParameterNames;
    }

    /**
     * Whether constructor parameter names obtained from the {@link java.lang.Record} component names should be considered by the DefaultRecordMapper.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMapRecordComponentParameterNames(Boolean value) {
        this.mapRecordComponentParameterNames = value;
    }

    /**
     * Whether constructor parameter names obtained from the {@link java.beans.ConstructorProperties} annotation should be considered by the DefaultRecordMapper.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMapConstructorPropertiesParameterNames() {
        return mapConstructorPropertiesParameterNames;
    }

    /**
     * Whether constructor parameter names obtained from the {@link java.beans.ConstructorProperties} annotation should be considered by the DefaultRecordMapper.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMapConstructorPropertiesParameterNames(Boolean value) {
        this.mapConstructorPropertiesParameterNames = value;
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
     * Whether constructor parameter names obtained via reflection in Java 8+ should be considered by the DefaultRecordMapper. This flag has no effect in Java 6 or 7.
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
     * Whether constructor parameter names obtained via reflection in Kotlin should be considered by the DefaultRecordMapper. This flag has no effect in Java.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMapConstructorParameterNamesInKotlin() {
        return mapConstructorParameterNamesInKotlin;
    }

    /**
     * Whether constructor parameter names obtained via reflection in Kotlin should be considered by the DefaultRecordMapper. This flag has no effect in Java.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMapConstructorParameterNamesInKotlin(Boolean value) {
        this.mapConstructorParameterNamesInKotlin = value;
    }

    /**
     * The default JDBC poolable property that should be applied to all
     * jOOQ queries, for which no specific poolable flag was specified.
     * 
     */
    public QueryPoolable getQueryPoolable() {
        return queryPoolable;
    }

    /**
     * The default JDBC poolable property that should be applied to all
     * jOOQ queries, for which no specific poolable flag was specified.
     * 
     */
    public void setQueryPoolable(QueryPoolable value) {
        this.queryPoolable = value;
    }

    /**
     * The default JDBC queryTimeout property that should be applied to all
     * jOOQ queries, for which no specific queryTimeout was specified.
     * 
     */
    public Integer getQueryTimeout() {
        return queryTimeout;
    }

    /**
     * The default JDBC queryTimeout property that should be applied to all
     * jOOQ queries, for which no specific queryTimeout was specified.
     * 
     */
    public void setQueryTimeout(Integer value) {
        this.queryTimeout = value;
    }

    /**
     * The default JDBC maxRows property that should be applied to all
     * jOOQ queries, for which no specific maxRows value was specified.
     * 
     */
    public Integer getMaxRows() {
        return maxRows;
    }

    /**
     * The default JDBC maxRows property that should be applied to all
     * jOOQ queries, for which no specific maxRows value was specified.
     * 
     */
    public void setMaxRows(Integer value) {
        this.maxRows = value;
    }

    /**
     * The default JDBC fetchSize property that should be applied to all
     * jOOQ queries, for which no specific fetchSize value was specified.
     * 
     */
    public Integer getFetchSize() {
        return fetchSize;
    }

    /**
     * The default JDBC fetchSize property that should be applied to all
     * jOOQ queries, for which no specific fetchSize value was specified.
     * 
     */
    public void setFetchSize(Integer value) {
        this.fetchSize = value;
    }

    /**
     * A property specifying a batch size that should be applied to all automatically created {@link org.jooq.tools.jdbc.BatchedConnection} instances.
     * 
     */
    public Integer getBatchSize() {
        return batchSize;
    }

    /**
     * A property specifying a batch size that should be applied to all automatically created {@link org.jooq.tools.jdbc.BatchedConnection} instances.
     * 
     */
    public void setBatchSize(Integer value) {
        this.batchSize = value;
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
     * [#5570] Whether exception stack traces should be enhanced with additional debug information.
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
     * [#5600] Whether IN lists in IN predicates should be padded to powers of inListPadBase (default 2).
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
     */
    public Integer getInListPadBase() {
        return inListPadBase;
    }

    /**
     * [#7095] The base to use to calculate the powers of when applying in list padding.
     * 
     */
    public void setInListPadBase(Integer value) {
        this.inListPadBase = value;
    }

    /**
     * [#5826] The delimiter character to be used to delimit statements in batches.
     * 
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * [#5826] The delimiter character to be used to delimit statements in batches.
     * 
     */
    public void setDelimiter(String value) {
        this.delimiter = value;
    }

    /**
     * [#6462] Use only the primary key to emulate MySQL's INSERT .. ON DUPLICATE KEY UPDATE statement. In MySQL, the statement considers all unique keys for duplicates to apply an update rather than an insert. Earlier versions of jOOQ considered only the PRIMARY KEY. This flag can be turned on to maintain backwards compatibility.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEmulateOnDuplicateKeyUpdateOnPrimaryKeyOnly() {
        return emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly;
    }

    /**
     * [#6462] Use only the primary key to emulate MySQL's INSERT .. ON DUPLICATE KEY UPDATE statement. In MySQL, the statement considers all unique keys for duplicates to apply an update rather than an insert. Earlier versions of jOOQ considered only the PRIMARY KEY. This flag can be turned on to maintain backwards compatibility.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEmulateOnDuplicateKeyUpdateOnPrimaryKeyOnly(Boolean value) {
        this.emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly = value;
    }

    /**
     * [#3884] How <code>MULTISET</code> support should be emulated.
     * 
     */
    public NestedCollectionEmulation getEmulateMultiset() {
        return emulateMultiset;
    }

    /**
     * [#3884] How <code>MULTISET</code> support should be emulated.
     * 
     */
    public void setEmulateMultiset(NestedCollectionEmulation value) {
        this.emulateMultiset = value;
    }

    /**
     * [#13418] Whether computed columns should be emulated in the client.
     * <p>
     * This can be useful if a schema was generated using a dialect that supports computed columns, but it is
     * deployed on an RDBMS that does not.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEmulateComputedColumns() {
        return emulateComputedColumns;
    }

    /**
     * [#13418] Whether computed columns should be emulated in the client.
     * <p>
     * This can be useful if a schema was generated using a dialect that supports computed columns, but it is
     * deployed on an RDBMS that does not.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEmulateComputedColumns(Boolean value) {
        this.emulateComputedColumns = value;
    }

    /**
     * [#6771] Specifies whether UPDATE statements are allowed to be executed lacking a WHERE clause. This has no effect on rendering the statements SQL string.
     * 
     */
    public ExecuteWithoutWhere getExecuteUpdateWithoutWhere() {
        return executeUpdateWithoutWhere;
    }

    /**
     * [#6771] Specifies whether UPDATE statements are allowed to be executed lacking a WHERE clause. This has no effect on rendering the statements SQL string.
     * 
     */
    public void setExecuteUpdateWithoutWhere(ExecuteWithoutWhere value) {
        this.executeUpdateWithoutWhere = value;
    }

    /**
     * [#6771] Specifies whether DELETE statements are allowed to be executed lacking a WHERE clause. This has no effect on rendering the statements SQL string.
     * 
     */
    public ExecuteWithoutWhere getExecuteDeleteWithoutWhere() {
        return executeDeleteWithoutWhere;
    }

    /**
     * [#6771] Specifies whether DELETE statements are allowed to be executed lacking a WHERE clause. This has no effect on rendering the statements SQL string.
     * 
     */
    public void setExecuteDeleteWithoutWhere(ExecuteWithoutWhere value) {
        this.executeDeleteWithoutWhere = value;
    }

    /**
     * [#7337] The dialect that should be used to interpret SQL DDL statements. {@link SQLDialect#DEFAULT} means that jOOQ interprets the SQL itself. Any other dialect (if supported) will be interpreted on an actual JDBC connection.
     * 
     */
    public SQLDialect getInterpreterDialect() {
        return interpreterDialect;
    }

    /**
     * [#7337] The dialect that should be used to interpret SQL DDL statements. {@link SQLDialect#DEFAULT} means that jOOQ interprets the SQL itself. Any other dialect (if supported) will be interpreted on an actual JDBC connection.
     * 
     */
    public void setInterpreterDialect(SQLDialect value) {
        this.interpreterDialect = value;
    }

    /**
     * [#9633] The case sensitivity of identifiers used when interpreting SQL DDL statements.
     * 
     */
    public InterpreterNameLookupCaseSensitivity getInterpreterNameLookupCaseSensitivity() {
        return interpreterNameLookupCaseSensitivity;
    }

    /**
     * [#9633] The case sensitivity of identifiers used when interpreting SQL DDL statements.
     * 
     */
    public void setInterpreterNameLookupCaseSensitivity(InterpreterNameLookupCaseSensitivity value) {
        this.interpreterNameLookupCaseSensitivity = value;
    }

    /**
     * The Locale to be used with any interpreter locale dependent logic, defaulting to {@link #getLocale()}.
     * 
     */
    public Locale getInterpreterLocale() {
        return interpreterLocale;
    }

    /**
     * The Locale to be used with any interpreter locale dependent logic, defaulting to {@link #getLocale()}.
     * 
     */
    public void setInterpreterLocale(Locale value) {
        this.interpreterLocale = value;
    }

    /**
     * Using this flag, the interpreter will be able to delay the addition of foreign key declarations until the end of the interpretation run.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInterpreterDelayForeignKeyDeclarations() {
        return interpreterDelayForeignKeyDeclarations;
    }

    /**
     * Using this flag, the interpreter will be able to delay the addition of foreign key declarations until the end of the interpretation run.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInterpreterDelayForeignKeyDeclarations(Boolean value) {
        this.interpreterDelayForeignKeyDeclarations = value;
    }

    /**
     * The {@link org.jooq.Meta} implementation that is backed by {@link java.sql.DatabaseMetaData} does not produce system generated indexes on constraints, by default.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMetaIncludeSystemIndexes() {
        return metaIncludeSystemIndexes;
    }

    /**
     * The {@link org.jooq.Meta} implementation that is backed by {@link java.sql.DatabaseMetaData} does not produce system generated indexes on constraints, by default.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMetaIncludeSystemIndexes(Boolean value) {
        this.metaIncludeSystemIndexes = value;
    }

    /**
     * The {@link org.jooq.Meta} implementation that is backed by {@link java.sql.DatabaseMetaData} does not produce system generated sequences, by default.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMetaIncludeSystemSequences() {
        return metaIncludeSystemSequences;
    }

    /**
     * The {@link org.jooq.Meta} implementation that is backed by {@link java.sql.DatabaseMetaData} does not produce system generated sequences, by default.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMetaIncludeSystemSequences(Boolean value) {
        this.metaIncludeSystemSequences = value;
    }

    /**
     * Whether migrations are allowed to be executed in inverse order.<p><strong>This is a potentially destructive feature, which should not be turned on in production</strong>. It is useful mostly to quickly switch between branches in a development environment. This feature is available only in commercial distributions.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMigrationAllowsUndo() {
        return migrationAllowsUndo;
    }

    /**
     * Whether migrations are allowed to be executed in inverse order.<p><strong>This is a potentially destructive feature, which should not be turned on in production</strong>. It is useful mostly to quickly switch between branches in a development environment. This feature is available only in commercial distributions.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMigrationAllowsUndo(Boolean value) {
        this.migrationAllowsUndo = value;
    }

    /**
     * Whether migrations revert any untracked changes in the schemas that are being migrated.<p><strong>This is a potentially destructive feature, which should not be turned on in production</strong>. It is useful mostly to quickly revert any elements created in a development environment. This feature is available only in commercial distributions.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMigrationRevertUntracked() {
        return migrationRevertUntracked;
    }

    /**
     * Whether migrations revert any untracked changes in the schemas that are being migrated.<p><strong>This is a potentially destructive feature, which should not be turned on in production</strong>. It is useful mostly to quickly revert any elements created in a development environment. This feature is available only in commercial distributions.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMigrationRevertUntracked(Boolean value) {
        this.migrationRevertUntracked = value;
    }

    /**
     * Whether to automatically existing schemas that are not yet managed by jOOQ Migrations.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMigrationAutoBaseline() {
        return migrationAutoBaseline;
    }

    /**
     * Whether to automatically existing schemas that are not yet managed by jOOQ Migrations.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMigrationAutoBaseline(Boolean value) {
        this.migrationAutoBaseline = value;
    }

    /**
     * Whether a migration automatically runs a validation first.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMigrationAutoValidation() {
        return migrationAutoValidation;
    }

    /**
     * Whether a migration automatically runs a validation first.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMigrationAutoValidation(Boolean value) {
        this.migrationAutoValidation = value;
    }

    /**
     * Various <code>migrateTo()</code> methods (e.g. {@link org.jooq.Meta#migrateTo(org.jooq.Meta)}) ignore the difference between <code>TIMESTAMP</code> and <code>TIMESTAMP(6)</code>, if 6 is the default precision for timestamps on the configured dialect.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMigrationIgnoreDefaultTimestampPrecisionDiffs() {
        return migrationIgnoreDefaultTimestampPrecisionDiffs;
    }

    /**
     * Various <code>migrateTo()</code> methods (e.g. {@link org.jooq.Meta#migrateTo(org.jooq.Meta)}) ignore the difference between <code>TIMESTAMP</code> and <code>TIMESTAMP(6)</code>, if 6 is the default precision for timestamps on the configured dialect.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMigrationIgnoreDefaultTimestampPrecisionDiffs(Boolean value) {
        this.migrationIgnoreDefaultTimestampPrecisionDiffs = value;
    }

    /**
     * The Locale to be used with any locale dependent logic if there is not a more specific locale available. More specific locales include e.g. {@link #getRenderLocale()}, {@link #getParseLocale()}, or {@link #getInterpreterLocale()}.
     * 
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * The Locale to be used with any locale dependent logic if there is not a more specific locale available. More specific locales include e.g. {@link #getRenderLocale()}, {@link #getParseLocale()}, or {@link #getInterpreterLocale()}.
     * 
     */
    public void setLocale(Locale value) {
        this.locale = value;
    }

    /**
     * [#7337] The input dialect that should be chosen to disambiguate ambiguous SQL syntax.
     * 
     */
    public SQLDialect getParseDialect() {
        return parseDialect;
    }

    /**
     * [#7337] The input dialect that should be chosen to disambiguate ambiguous SQL syntax.
     * 
     */
    public void setParseDialect(SQLDialect value) {
        this.parseDialect = value;
    }

    /**
     * The Locale to be used with any parser locale dependent logic, defaulting to {@link #getLocale()}.
     * 
     */
    public Locale getParseLocale() {
        return parseLocale;
    }

    /**
     * The Locale to be used with any parser locale dependent logic, defaulting to {@link #getLocale()}.
     * 
     */
    public void setParseLocale(Locale value) {
        this.parseLocale = value;
    }

    /**
     * The date format to use when parsing functions whose behaviour depends on some session date format, such as NLS_DATE_FORMAT in Oracle
     * 
     */
    public String getParseDateFormat() {
        return parseDateFormat;
    }

    /**
     * The date format to use when parsing functions whose behaviour depends on some session date format, such as NLS_DATE_FORMAT in Oracle
     * 
     */
    public void setParseDateFormat(String value) {
        this.parseDateFormat = value;
    }

    /**
     * The timestamp format to use when parsing functions whose behaviour depends on some session date format, such as NLS_TIMESTAMP_FORMAT in Oracle
     * 
     */
    public String getParseTimestampFormat() {
        return parseTimestampFormat;
    }

    /**
     * The timestamp format to use when parsing functions whose behaviour depends on some session date format, such as NLS_TIMESTAMP_FORMAT in Oracle
     * 
     */
    public void setParseTimestampFormat(String value) {
        this.parseTimestampFormat = value;
    }

    /**
     * The prefix to use for named parameters in parsed SQL.
     * <p>
     * Named parameter syntax defaults to <code>:name</code> (such as supported by Oracle, JPA, Spring), but 
     * vendor specific parameters may look differently. This flag can be used to determine the prefix to be
     * used by named parameters, such as <code>@</code> for SQL Server's <code>@name</code> or <code>$</code>
     * for PostgreSQL's <code>$name</code> when parsing SQL.
     * <p>
     * "Named indexed" parameters can be obtained in the same way by specifingy {@code ParamType#NAMED} and not
     * providing a name to parameters, resulting in <code>:1</code> or <code>@1</code> or <code>$1</code>, etc.
     * 
     */
    public String getParseNamedParamPrefix() {
        return parseNamedParamPrefix;
    }

    /**
     * The prefix to use for named parameters in parsed SQL.
     * <p>
     * Named parameter syntax defaults to <code>:name</code> (such as supported by Oracle, JPA, Spring), but 
     * vendor specific parameters may look differently. This flag can be used to determine the prefix to be
     * used by named parameters, such as <code>@</code> for SQL Server's <code>@name</code> or <code>$</code>
     * for PostgreSQL's <code>$name</code> when parsing SQL.
     * <p>
     * "Named indexed" parameters can be obtained in the same way by specifingy {@code ParamType#NAMED} and not
     * providing a name to parameters, resulting in <code>:1</code> or <code>@1</code> or <code>$1</code>, etc.
     * 
     */
    public void setParseNamedParamPrefix(String value) {
        this.parseNamedParamPrefix = value;
    }

    /**
     * [#7337] The default name case for parsed identifiers.
     * 
     */
    public ParseNameCase getParseNameCase() {
        return parseNameCase;
    }

    /**
     * [#7337] The default name case for parsed identifiers.
     * 
     */
    public void setParseNameCase(ParseNameCase value) {
        this.parseNameCase = value;
    }

    /**
     * [#7163] Whether the parser should perform meta lookups in the Configuration's MetaProvider.
     * 
     */
    public ParseWithMetaLookups getParseWithMetaLookups() {
        return parseWithMetaLookups;
    }

    /**
     * [#7163] Whether the parser should perform meta lookups in the Configuration's MetaProvider.
     * 
     */
    public void setParseWithMetaLookups(ParseWithMetaLookups value) {
        this.parseWithMetaLookups = value;
    }

    /**
     * Transform the parsed SQL to append missing table references to the query's <code>FROM</code> or <code>USING</code> clause, if applicable.
     * <p>
     * Teradata (and possibly others) allow for referencing tables that are not listed in the <code>FROM</code>
     * clause, such as <code>SELECT t.* FROM t WHERE t.i = u.i</code>. This transformation is executed in the
     * parser, to produce <code>SELECT t.* FROM t, u WHERE t.i = u.i</code>, instead. By default, it is active
     * when the input dialect supports this syntax.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Transformation getParseAppendMissingTableReferences() {
        return parseAppendMissingTableReferences;
    }

    /**
     * Transform the parsed SQL to append missing table references to the query's <code>FROM</code> or <code>USING</code> clause, if applicable.
     * <p>
     * Teradata (and possibly others) allow for referencing tables that are not listed in the <code>FROM</code>
     * clause, such as <code>SELECT t.* FROM t WHERE t.i = u.i</code>. This transformation is executed in the
     * parser, to produce <code>SELECT t.* FROM t, u WHERE t.i = u.i</code>, instead. By default, it is active
     * when the input dialect supports this syntax.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public void setParseAppendMissingTableReferences(Transformation value) {
        this.parseAppendMissingTableReferences = value;
    }

    /**
     * [#9780] Whether commands of the type <code>SET key = value</code> should be parsed rather than ignored.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isParseSetCommands() {
        return parseSetCommands;
    }

    /**
     * [#9780] Whether commands of the type <code>SET key = value</code> should be parsed rather than ignored.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setParseSetCommands(Boolean value) {
        this.parseSetCommands = value;
    }

    /**
     * [#5917] Whether the parser should accept unsupported (but known) syntax.
     * 
     */
    public ParseUnsupportedSyntax getParseUnsupportedSyntax() {
        return parseUnsupportedSyntax;
    }

    /**
     * [#5917] Whether the parser should accept unsupported (but known) syntax.
     * 
     */
    public void setParseUnsupportedSyntax(ParseUnsupportedSyntax value) {
        this.parseUnsupportedSyntax = value;
    }

    /**
     * [#7344] Whether the parser should accept unknown functions.
     * 
     */
    public ParseUnknownFunctions getParseUnknownFunctions() {
        return parseUnknownFunctions;
    }

    /**
     * [#7344] Whether the parser should accept unknown functions.
     * 
     */
    public void setParseUnknownFunctions(ParseUnknownFunctions value) {
        this.parseUnknownFunctions = value;
    }

    /**
     * [#13109] Whether the parser of the jOOQ Open Source Edition should ignore commercial only features, rather than failing.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isParseIgnoreCommercialOnlyFeatures() {
        return parseIgnoreCommercialOnlyFeatures;
    }

    /**
     * [#13109] Whether the parser of the jOOQ Open Source Edition should ignore commercial only features, rather than failing.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setParseIgnoreCommercialOnlyFeatures(Boolean value) {
        this.parseIgnoreCommercialOnlyFeatures = value;
    }

    /**
     * [#8325] Whether the parser should ignore content between ignore comment tokens.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isParseIgnoreComments() {
        return parseIgnoreComments;
    }

    /**
     * [#8325] Whether the parser should ignore content between ignore comment tokens.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setParseIgnoreComments(Boolean value) {
        this.parseIgnoreComments = value;
    }

    /**
     * [#8325] The ignore comment start token
     * 
     */
    public String getParseIgnoreCommentStart() {
        return parseIgnoreCommentStart;
    }

    /**
     * [#8325] The ignore comment start token
     * 
     */
    public void setParseIgnoreCommentStart(String value) {
        this.parseIgnoreCommentStart = value;
    }

    /**
     * [#8325] The ignore comment stop token
     * 
     */
    public String getParseIgnoreCommentStop() {
        return parseIgnoreCommentStop;
    }

    /**
     * [#8325] The ignore comment stop token
     * 
     */
    public void setParseIgnoreCommentStop(String value) {
        this.parseIgnoreCommentStop = value;
    }

    /**
     * [#12538] Whether the parser should retain comments and whitespace between queries when parsing multiple queries through {@link org.jooq.Parser#parse(String)}.
     * <p>
     * jOOQ's query object model doesn't have a way to represent comments
     * or other whitespace, and as such, the parser simply skips them by default.
     * However, it may be desirable to retain comments before or in between top
     * level queries, when parsing multiple such queries in a script. Comments
     * inside of queries (including procedural statements) are still not supported.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isParseRetainCommentsBetweenQueries() {
        return parseRetainCommentsBetweenQueries;
    }

    /**
     * [#12538] Whether the parser should retain comments and whitespace between queries when parsing multiple queries through {@link org.jooq.Parser#parse(String)}.
     * <p>
     * jOOQ's query object model doesn't have a way to represent comments
     * or other whitespace, and as such, the parser simply skips them by default.
     * However, it may be desirable to retain comments before or in between top
     * level queries, when parsing multiple such queries in a script. Comments
     * inside of queries (including procedural statements) are still not supported.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setParseRetainCommentsBetweenQueries(Boolean value) {
        this.parseRetainCommentsBetweenQueries = value;
    }

    /**
     * [#8469] Whether to parse default expressions retrieved from {@link java.sql.DatabaseMetaData}.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isParseMetaDefaultExpressions() {
        return parseMetaDefaultExpressions;
    }

    /**
     * [#8469] Whether to parse default expressions retrieved from {@link java.sql.DatabaseMetaData}.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setParseMetaDefaultExpressions(Boolean value) {
        this.parseMetaDefaultExpressions = value;
    }

    /**
     * [#9864] The behaviour when trying to insert into readonly columns using {@link org.jooq.TableRecord#insert()}.
     * 
     */
    public WriteIfReadonly getReadonlyTableRecordInsert() {
        return readonlyTableRecordInsert;
    }

    /**
     * [#9864] The behaviour when trying to insert into readonly columns using {@link org.jooq.TableRecord#insert()}.
     * 
     */
    public void setReadonlyTableRecordInsert(WriteIfReadonly value) {
        this.readonlyTableRecordInsert = value;
    }

    /**
     * [#9864] The behaviour when trying to update a readonly column using {@link org.jooq.UpdatableRecord#update()}.
     * 
     */
    public WriteIfReadonly getReadonlyUpdatableRecordUpdate() {
        return readonlyUpdatableRecordUpdate;
    }

    /**
     * [#9864] The behaviour when trying to update a readonly column using {@link org.jooq.UpdatableRecord#update()}.
     * 
     */
    public void setReadonlyUpdatableRecordUpdate(WriteIfReadonly value) {
        this.readonlyUpdatableRecordUpdate = value;
    }

    /**
     * [#9864] The behaviour when trying to insert into readonly columns using {@link org.jooq.Insert} statements, or the insert clause of a {@link org.jooq.Merge} statement.
     * 
     */
    public WriteIfReadonly getReadonlyInsert() {
        return readonlyInsert;
    }

    /**
     * [#9864] The behaviour when trying to insert into readonly columns using {@link org.jooq.Insert} statements, or the insert clause of a {@link org.jooq.Merge} statement.
     * 
     */
    public void setReadonlyInsert(WriteIfReadonly value) {
        this.readonlyInsert = value;
    }

    /**
     * [#9864] The behaviour when trying to update a readonly column using {@link org.jooq.Update} statements, or the update clause of a {@link org.jooq.Merge} statement.
     * 
     */
    public WriteIfReadonly getReadonlyUpdate() {
        return readonlyUpdate;
    }

    /**
     * [#9864] The behaviour when trying to update a readonly column using {@link org.jooq.Update} statements, or the update clause of a {@link org.jooq.Merge} statement.
     * 
     */
    public void setReadonlyUpdate(WriteIfReadonly value) {
        this.readonlyUpdate = value;
    }

    /**
     * [#7963] Apply workaround for ORA-04043 when inserting into Oracle tables with qualified, quoted identifiers, and fetching generated keys
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isApplyWorkaroundFor7962() {
        return applyWorkaroundFor7962;
    }

    /**
     * [#7963] Apply workaround for ORA-04043 when inserting into Oracle tables with qualified, quoted identifiers, and fetching generated keys
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setApplyWorkaroundFor7962(Boolean value) {
        this.applyWorkaroundFor7962 = value;
    }

    public List<InterpreterSearchSchema> getInterpreterSearchPath() {
        if (interpreterSearchPath == null) {
            interpreterSearchPath = new ArrayList<InterpreterSearchSchema>();
        }
        return interpreterSearchPath;
    }

    public void setInterpreterSearchPath(List<InterpreterSearchSchema> interpreterSearchPath) {
        this.interpreterSearchPath = interpreterSearchPath;
    }

    public List<MigrationSchema> getMigrationSchemata() {
        if (migrationSchemata == null) {
            migrationSchemata = new ArrayList<MigrationSchema>();
        }
        return migrationSchemata;
    }

    public void setMigrationSchemata(List<MigrationSchema> migrationSchemata) {
        this.migrationSchemata = migrationSchemata;
    }

    public List<ParseSearchSchema> getParseSearchPath() {
        if (parseSearchPath == null) {
            parseSearchPath = new ArrayList<ParseSearchSchema>();
        }
        return parseSearchPath;
    }

    public void setParseSearchPath(List<ParseSearchSchema> parseSearchPath) {
        this.parseSearchPath = parseSearchPath;
    }

    /**
     * Historically, zero-scale decimal types are generated as their most appropriate, corresponding integer type (e.g. NUMBER(2, 0) and less: Byte). The same behaviour is replicated in the {@link org.jooq.Meta} API. This flag allows for turning off this feature.
     * 
     */
    public Settings withForceIntegerTypesOnZeroScaleDecimals(Boolean value) {
        setForceIntegerTypesOnZeroScaleDecimals(value);
        return this;
    }

    /**
     * Whether any catalog name should be rendered at all.
     * <p>
     * Use this for single-catalog environments, or when all objects are made
     * available using synonyms
     * 
     */
    public Settings withRenderCatalog(Boolean value) {
        setRenderCatalog(value);
        return this;
    }

    /**
     * Whether any schema name should be rendered at all.
     * <p>
     * Setting this to false also implicitly sets "renderCatalog" to false.
     * <p>
     * Use this for single-schema environments, or when all objects are made
     * available using synonyms
     * 
     */
    public Settings withRenderSchema(Boolean value) {
        setRenderSchema(value);
        return this;
    }

    /**
     * Whether any table name qualification should be rendered at all on columns.
     * <p>
     * Setting when tables aren't rendered, then implicitly, schemas and catalogs aren't rendered either.
     * <p>
     * The following values are available:
     * <ul>
     * <li>{@link RenderTable#ALWAYS}: The default, which should always be preferred. Columns are always qualified with their tables, where possible.</li>
     * <li>{@link RenderTable#WHEN_MULTIPLE_TABLES}: The simplest option to reduce generated query verbosity, avoiding table qualification only in queries with a single table in the <code>FROM</code> clause.</li>
     * <li>{@link RenderTable#WHEN_AMBIGUOUS_COLUMNS}: A much more expensive to compute option that checks the <code>FROM</code> clause for ambiguous column names, in case of which columns are qualified.</li>
     * <li>{@link RenderTable#NEVER}: Always turn off table qualification.</li>
     * </ul>
     * <p>
     * Use this when verbosity of rendered SQL is a problem.
     * 
     */
    public Settings withRenderTable(RenderTable value) {
        setRenderTable(value);
        return this;
    }

    /**
     * Configure render mapping for runtime schema / table rewriting in
     * generated SQL.
     * 
     */
    public Settings withRenderMapping(RenderMapping value) {
        setRenderMapping(value);
        return this;
    }

    /**
     * Whether rendered schema, table, column names, etc should be quoted.
     * <p>
     * This only affects names created through {@link org.jooq.impl.DSL#name(String)} methods (including those that are implicitly created through this method), not {@link org.jooq.impl.DSL#quotedName(String)} or {@link org.jooq.impl.DSL#unquotedName(String)}, whose behaviour cannot be overridden.
     * <p>
     * This setting does not affect any plain SQL usage.
     * 
     */
    public Settings withRenderQuotedNames(RenderQuotedNames value) {
        setRenderQuotedNames(value);
        return this;
    }

    /**
     * Whether the case of {@link org.jooq.Name} references should be modified in any way.
     * <p>
     * Names are modified irrespective of the {@link #getRenderQuotedNames()} setting.
     * <p>
     * This setting does not affect any plain SQL usage.
     * 
     */
    public Settings withRenderNameCase(RenderNameCase value) {
        setRenderNameCase(value);
        return this;
    }

    /**
     * Whether rendered schema, table, column names, etc should be quoted
     * in rendered SQL, or transformed in any other way.
     * <p>
     * This is set to "QUOTED" by default for backwards-compatibility.
     * <p>
     * @deprecated - 3.12.0 - [#5909] - Use {@link RenderQuotedNames} and {@link RenderNameCase} instead.
     * 
     */
    @Deprecated
    public Settings withRenderNameStyle(RenderNameStyle value) {
        setRenderNameStyle(value);
        return this;
    }

    /**
     * The prefix to use for named parameters in generated SQL.
     * <p>
     * Named parameter syntax defaults to <code>:name</code> (such as supported by Oracle, JPA, Spring), but 
     * vendor specific parameters may look differently. This flag can be used to determine the prefix to be
     * used by named parameters, such as <code>@</code> for SQL Server's <code>@name</code> or <code>$</code>
     * for PostgreSQL's <code>$name</code>, when generating SQL.
     * <p>
     * "Named indexed" parameters can be obtained in the same way by specifingy {@code ParamType#NAMED} and not
     * providing a name to parameters, resulting in <code>:1</code> or <code>@1</code> or <code>$1</code>, etc.
     * 
     */
    public Settings withRenderNamedParamPrefix(String value) {
        setRenderNamedParamPrefix(value);
        return this;
    }

    /**
     * Whether the case of {@link org.jooq.Keyword} references should be modified in any way.
     * 
     */
    public Settings withRenderKeywordCase(RenderKeywordCase value) {
        setRenderKeywordCase(value);
        return this;
    }

    /**
     * Whether the case of {@link org.jooq.Keyword} references should be modified in any way.
     * <p>
     * @deprecated - 3.12.0 - [#5909] - Use {@link RenderKeywordCase} instead.
     * 
     */
    @Deprecated
    public Settings withRenderKeywordStyle(RenderKeywordStyle value) {
        setRenderKeywordStyle(value);
        return this;
    }

    /**
     * The Locale to be used with any render locale dependent logic (as e.g. transforming names to lower / uppper case), defaulting to {@link #getLocale()}.
     * 
     */
    public Settings withRenderLocale(Locale value) {
        setRenderLocale(value);
        return this;
    }

    /**
     * Whether rendered SQL should be pretty-printed.
     * 
     */
    public Settings withRenderFormatted(Boolean value) {
        setRenderFormatted(value);
        return this;
    }

    /**
     * All sorts of formatting flags / settings.
     * 
     */
    public Settings withRenderFormatting(RenderFormatting value) {
        setRenderFormatting(value);
        return this;
    }

    /**
     * Whether to render optional parentheses to make associativity explicit, e.g. <code>((a + b) + c)</code> instead of (a + b + c).
     * 
     */
    public Settings withRenderOptionalAssociativityParentheses(RenderOptionalKeyword value) {
        setRenderOptionalAssociativityParentheses(value);
        return this;
    }

    /**
     * Whether to render the optional <code>AS</code> keyword in table aliases, if it is optional in the output dialect. This is ignored if the keyword is not supported (e.g. in Oracle)
     * 
     */
    public Settings withRenderOptionalAsKeywordForTableAliases(RenderOptionalKeyword value) {
        setRenderOptionalAsKeywordForTableAliases(value);
        return this;
    }

    /**
     * Whether to render the optional <code>AS</code> keyword in table aliases, if it is optional in the output dialect.
     * 
     */
    public Settings withRenderOptionalAsKeywordForFieldAliases(RenderOptionalKeyword value) {
        setRenderOptionalAsKeywordForFieldAliases(value);
        return this;
    }

    /**
     * Whether to render the optional <code>INNER</code> keyword in <code>INNER JOIN</code>, if it is optional in the output dialect.
     * 
     */
    public Settings withRenderOptionalInnerKeyword(RenderOptionalKeyword value) {
        setRenderOptionalInnerKeyword(value);
        return this;
    }

    /**
     * Whether to render the optional <code>OUTER</code> keyword in <code>OUTER JOIN</code>, if it is optional in the output dialect.
     * 
     */
    public Settings withRenderOptionalOuterKeyword(RenderOptionalKeyword value) {
        setRenderOptionalOuterKeyword(value);
        return this;
    }

    /**
     * Whether to render an explicit window <code>RANGE</code> clause when an implicit clause is applied.
     * 
     */
    public Settings withRenderImplicitWindowRange(RenderImplicitWindowRange value) {
        setRenderImplicitWindowRange(value);
        return this;
    }

    /**
     * Whether stored function calls should be wrapped in scalar subqueries.
     * <p>
     * Oracle 11g (and potentially, other databases too) implements scalar subquery caching. With this flag
     * set to true, users can automatically profit from this feature in all SQL statements.
     * 
     */
    public Settings withRenderScalarSubqueriesForStoredFunctions(Boolean value) {
        setRenderScalarSubqueriesForStoredFunctions(value);
        return this;
    }

    /**
     * The join type to be generated by implicit joins.
     * 
     */
    public Settings withRenderImplicitJoinType(RenderImplicitJoinType value) {
        setRenderImplicitJoinType(value);
        return this;
    }

    /**
     * Whether the {@link org.jooq.Nullability#DEFAULT} nullablity should be rendered in generated DDL, and how it should be rendered.
     * 
     */
    public Settings withRenderDefaultNullability(RenderDefaultNullability value) {
        setRenderDefaultNullability(value);
        return this;
    }

    /**
     * Whether stored function calls should be wrapped in scalar subqueries.
     * <p>
     * Oracle 11g (and potentially, other databases too) implements scalar subquery caching. With this flag
     * set to true, users can automatically profit from this feature in all SQL statements.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withRenderCoalesceToEmptyStringInConcat(Boolean value) {
        setRenderCoalesceToEmptyStringInConcat(value);
        return this;
    }

    /**
     * Whether an additional <code>ORDER BY rn</code> clause should be rendered on emulated paginated queries.
     * <p>
     * Older databases did not support OFFSET .. FETCH pagination, so jOOQ emulates it using derived
     * tables and <code>ROWNUM</code> (Oracle 11g and older) or <code>ROW_NUMBER()</code> (e.g. DB2, 
     * SQL Server, etc.) filtering. While these subqueries are ordered, the ordering is not 
     * <em>guaranteed</em> to be stable in the outer most queries. It may be stable (and e.g. in Oracle, 
     * it mostly is, if queries are not parallel, or joined to other queries, etc.), so the excess 
     * <code>ORDER BY</code> clause may add some additional performance overhead. This setting forces 
     * jOOQ to not generate the additional <code>ORDER BY</code> clause.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/7609">https://github.com/jOOQ/jOOQ/issues/7609</a>.
     * 
     */
    public Settings withRenderOrderByRownumberForEmulatedPagination(Boolean value) {
        setRenderOrderByRownumberForEmulatedPagination(value);
        return this;
    }

    /**
     * Whether the jOOQ <code>RETURNING</code> clause should map to SQL Server's <code>OUTPUT</code> clause.
     * <p>
     * SQL Server supports an <code>OUTPUT</code> clause in most DML statements, whose behaviour
     * is almost identical to <code>RETURNING</code> in Firebird, Oracle, PostgreSQL. Users who
     * want to prevent jOOQ from rendering this <code>OUTPUT</code> clause can deactivate this flag
     * to revert to jOOQ calling {@code java.sql.Statement#getGeneratedKeys()} instead, which
     * is only supported for single row inserts.
     * <p>
     * This <code>OUTPUT</code> clause does not support fetching trigger generated values. In order
     * to fetch trigger generated values, {@link #fetchTriggerValuesAfterSQLServerOutput} needs to
     * be enabled as well.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/4498">https://github.com/jOOQ/jOOQ/issues/4498</a>.
     * 
     */
    public Settings withRenderOutputForSQLServerReturningClause(Boolean value) {
        setRenderOutputForSQLServerReturningClause(value);
        return this;
    }

    /**
     * Whether the jOOQ <code>GROUP_CONCAT</code> function should be overflow-protected by setting the <code>@@group_concat_max_len</code> session variable in MySQL style database systems.
     * <p>
     * MySQL truncates <code>GROUP_CONCAT</code> results after a certain length, which may be way
     * too small for jOOQ's usage, especially when using the <code>MULTISET</code> emulation. By
     * default, jOOQ sets a session variable to the highest possible value prior to executing a
     * query containing <code>GROUP_CONCAT</code>. This flag can be used to opt out of this.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/12092">https://github.com/jOOQ/jOOQ/issues/12092</a>.
     * 
     */
    public Settings withRenderGroupConcatMaxLenSessionVariable(Boolean value) {
        setRenderGroupConcatMaxLenSessionVariable(value);
        return this;
    }

    /**
     * Whether queries combined with set operators (e.g. UNION and UNION ALL) should always be surrounded by a parenthesis pair.
     * <p>
     * By default (i.e. when this setting is set to <code>false</code> jOOQ will only render parenthesis pairs around queries combined with set operators when required.
     * This is for example the case when set operators are nested, when non-associative operators like EXCEPT are used, or when the queries are rendered as derived tables.
     * <p>
     * When this setting is set to <code>true</code> the queries combined with set operators will always be surrounded by a parenthesis pair.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/3676">https://github.com/jOOQ/jOOQ/issues/3676</a> and <a href="https://github.com/jOOQ/jOOQ/issues/9751">https://github.com/jOOQ/jOOQ/issues/9751</a>.
     * 
     */
    public Settings withRenderParenthesisAroundSetOperationQueries(Boolean value) {
        setRenderParenthesisAroundSetOperationQueries(value);
        return this;
    }

    /**
     * Whether emulations that require repeating expressions should render variables for those expressions in derived tables.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/14065">https://github.com/jOOQ/jOOQ/issues/14065</a>.
     * 
     */
    public Settings withRenderVariablesInDerivedTablesForEmulations(Boolean value) {
        setRenderVariablesInDerivedTablesForEmulations(value);
        return this;
    }

    /**
     * Whether a <code>(a, b) < (:a, :b)</code> row predicate should be rendered for the <code>SEEK</code> clause.
     * <p>
     * Some RDBMS may support <code>(a, b) < (:a, :b)</code> row predicate syntax, which is very convenient for <code>SEEK</code> clause implementations, but fail to optimise this predicate as could be expected.
     * This flag allows for expanding the predicate to the much more verbose, but equivalent <code>(a < :a) OR (a = :a AND b < :b)</code>. Dialects without native support for row predicates aren't affected
     * by this flag.
     * 
     */
    public Settings withRenderRowConditionForSeekClause(Boolean value) {
        setRenderRowConditionForSeekClause(value);
        return this;
    }

    /**
     * Whether a redundant <code>(a <= :a)</code> predicate should be rendered for a <code>(a, b) < (:a, :b)</code> predicate for the <code>SEEK</code> clause.
     * <p>
     * Some RDBMS may not be able to properly optimise <code>(a, b) < ('a', 'b')</code> or <code>(a < 'a') OR (a = 'a' AND b < 'b')</code>, and choose an appropriate index. By adding an additional redundant predicate,
     * jOOQ may help the optimiser, e.g. <code>(a <= :a) AND (a, b) < ('a', 'b')</code> or <code>(a <= :a) AND ((a < 'a') OR (a = 'a' AND b < 'b'))</code>
     * 
     */
    public Settings withRenderRedundantConditionForSeekClause(Boolean value) {
        setRenderRedundantConditionForSeekClause(value);
        return this;
    }

    /**
     * Whether plain SQL templates ({@link org.jooq.SQL}) are rendered as raw string content.
     * 
     */
    public Settings withRenderPlainSQLTemplatesAsRaw(Boolean value) {
        setRenderPlainSQLTemplatesAsRaw(value);
        return this;
    }

    /**
     * The character(s) to be used as a separator in paths encoded in a {@link Name}
     * <p>
     * A few hierarchical mapping features work with paths encoded in names (specifically field aliases), such as the reflective mapping of nested values when aliasing fields as:
     * <p>
     * <pre><code>
     * SELECT 
     *   a.first_name AS "book.author.firstName"
     *   a.last_name AS "book.author.lastName"
     * FROM ...
     * </code></pre>
     * <p>
     * Not all dialects support "." in identifiers. This setting allows for specifying an alternative String to use as separator, e.g. "__".
     * 
     */
    public Settings withNamePathSeparator(String value) {
        setNamePathSeparator(value);
        return this;
    }

    /**
     * Whether the <code>java.time</code> (JSR 310) type {@link java.time.OffsetDateTime} should be bound natively to JDBC.
     * <p>
     * Historically, jOOQ encoded the <code>java.time</code> types as strings to offer better compatibility with older JDBC drivers. By now, most drivers should support the <code>java.time</code> types. Using them may produce better performance both on the server and on the client side.
     * <p>
     * This flag allows for reverting to pre-jOOQ 3.14 behaviour, where the default is to bind these types natively.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/9902">https://github.com/jOOQ/jOOQ/issues/9902</a>.
     * 
     */
    public Settings withBindOffsetDateTimeType(Boolean value) {
        setBindOffsetDateTimeType(value);
        return this;
    }

    /**
     * Whether the <code>java.time</code> (JSR 310) type {@link java.time.OffsetTime} should be bound natively to JDBC.
     * <p>
     * Historically, jOOQ encoded the <code>java.time</code> types as strings to offer better compatibility with older JDBC drivers. By now, most drivers should support the <code>java.time</code> types. Using them may produce better performance both on the server and on the client side.
     * <p>
     * This flag allows for reverting to pre-jOOQ 3.14 behaviour, where the default is to bind these types natively.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/9902">https://github.com/jOOQ/jOOQ/issues/9902</a>.
     * 
     */
    public Settings withBindOffsetTimeType(Boolean value) {
        setBindOffsetTimeType(value);
        return this;
    }

    /**
     * Fetch trigger values after SQL Server <code>OUTPUT</code> clause.
     * <p>
     * SQL Server <code>OUTPUT</code> statements do not support fetching trigger generated values.
     * This is a limitation of the {@link #renderOutputForSQLServerReturningClause}. An additional
     * <code>MERGE</code> statement can run a second query if (and only if) the primary key has been
     * included in the <code>OUTPUT</code> clause.
     * <p>
     * For details, see <a href="https://github.com/jOOQ/jOOQ/issues/4498">https://github.com/jOOQ/jOOQ/issues/4498</a>.
     * 
     */
    public Settings withFetchTriggerValuesAfterSQLServerOutput(Boolean value) {
        setFetchTriggerValuesAfterSQLServerOutput(value);
        return this;
    }

    /**
     * Whether to fetch data into intermediate {@link org.jooq.Result} instances.
     * <p>
     * By default, a {@link org.jooq.ResultQuery} produces no intermediate {@link org.jooq.Result} 
     * instances if they are not explicitly requested by the caller, e.g. by calling
     * {@link org.jooq.ResultQuery#fetch()}, or in the presence of {@link org.jooq.ExecuteListener}
     * instances, which may require access to {@link org.jooq.ExecuteContext#result()}.
     * This default behaviour helps avoid unnecessary allocations of possibly large data structures.
     * <p>
     * Using this flag, fetching of intermediate results can be turned off even when execute listeners
     * are present, or turned on even if they're absent.
     * 
     */
    public Settings withFetchIntermediateResult(FetchIntermediateResult value) {
        setFetchIntermediateResult(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#duplicateStatements(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     */
    public Settings withDiagnosticsDuplicateStatements(Boolean value) {
        setDiagnosticsDuplicateStatements(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#duplicateStatements(org.jooq.DiagnosticsContext)} diagnostic with the {@link #transformPatterns} feature activated.
     * <p>
     * When transforming patterns, many more complex, duplicate SQL statements can be recognised than if simply
     * parsing and re-rendering the statement. This flag turns on all transformation patterns, independently of their
     * individual settings.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withDiagnosticsDuplicateStatementsUsingTransformPatterns(Boolean value) {
        setDiagnosticsDuplicateStatementsUsingTransformPatterns(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#missingWasNullCall(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     */
    public Settings withDiagnosticsMissingWasNullCall(Boolean value) {
        setDiagnosticsMissingWasNullCall(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#repeatedStatements(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     */
    public Settings withDiagnosticsRepeatedStatements(Boolean value) {
        setDiagnosticsRepeatedStatements(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#consecutiveAggregation(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withDiagnosticsConsecutiveAggregation(Boolean value) {
        setDiagnosticsConsecutiveAggregation(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#concatenationInPredicate(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withDiagnosticsConcatenationInPredicate(Boolean value) {
        setDiagnosticsConcatenationInPredicate(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#possiblyWrongExpression(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withDiagnosticsPossiblyWrongExpression(Boolean value) {
        setDiagnosticsPossiblyWrongExpression(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#tooManyColumnsFetched(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     */
    public Settings withDiagnosticsTooManyColumnsFetched(Boolean value) {
        setDiagnosticsTooManyColumnsFetched(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#tooManyRowsFetched(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     */
    public Settings withDiagnosticsTooManyRowsFetched(Boolean value) {
        setDiagnosticsTooManyRowsFetched(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#unnecessaryWasNullCall(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * 
     */
    public Settings withDiagnosticsUnnecessaryWasNullCall(Boolean value) {
        setDiagnosticsUnnecessaryWasNullCall(value);
        return this;
    }

    /**
     * Whether to run the various pattern transformation diagnostics.
     * <p>
     * {@link #transformPatterns} allows for applying numerous pattern transformations, which can be turned on separately when running
     * diagnostics. This flag overrides the {@link #transformPatterns} flag in the diagnostics context. Individual pattern flags
     * still allow to enable / disable the pattern for diagnostics. 
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withDiagnosticsPatterns(Boolean value) {
        setDiagnosticsPatterns(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#trivialCondition(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withDiagnosticsTrivialCondition(Boolean value) {
        setDiagnosticsTrivialCondition(value);
        return this;
    }

    /**
     * Whether to run the {@link org.jooq.DiagnosticsListener#nullConditoin(org.jooq.DiagnosticsContext)} diagnostic.
     * <p>
     * Diagnostics are turned off if no {@link org.jooq.Configuration#diagnosticsListenerProviders()} are configured.
     * Once configured, this diagnostic is turned on by default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withDiagnosticsNullCondition(Boolean value) {
        setDiagnosticsNullCondition(value);
        return this;
    }

    /**
     * Transform various syntax patterns to better versions, if possible.
     * <p>
     * This flag enables the pattern transformation feature, which consists of several sub-flags that are
     * all prefixed with "transformPatterns", e.g. {@link #transformPatternsTrim}. While the sub-flags default
     * to being enabled, and can be disabled on an individual basis, the global feature itself is disabled by
     * default.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatterns(Boolean value) {
        setTransformPatterns(value);
        return this;
    }

    /**
     * Activate debug logging of the {@link #transformPatterns} feature.
     * 
     */
    public Settings withTransformPatternsLogging(Boolean value) {
        setTransformPatternsLogging(value);
        return this;
    }

    /**
     * Transform <code>SELECT DISTINCT a, b FROM t GROUP BY a, b</code> to <code>SELECT a, b FROM t GROUP BY a, b</code>.
     * <p>
     * The <code>GROUP BY</code> clause already removes duplicates, so if the <code>DISTINCT</code> clause
     * contains at least all the columns from <code>GROUP BY</code> then it can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsUnnecessaryDistinct(Boolean value) {
        setTransformPatternsUnnecessaryDistinct(value);
        return this;
    }

    /**
     * Transform <code>SELECT (SELECT 1)</code> to <code>SELECT 1</code>.
     * <p>
     * Scalar subqueries that don't have any content other than a <code>SELECT</code> clause are unnecessary
     * and can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsUnnecessaryScalarSubquery(Boolean value) {
        setTransformPatternsUnnecessaryScalarSubquery(value);
        return this;
    }

    /**
     * Transform <code>SELECT * FROM t INNER JOIN u ON TRUE</code> to <code>SELECT * FROM t CROSS JOIN u</code>.
     * <p>
     * Some <code>INNER JOIN</code> expressions can be proven to be unnecessary.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsUnnecessaryInnerJoin(Boolean value) {
        setTransformPatternsUnnecessaryInnerJoin(value);
        return this;
    }

    /**
     * Transform <code>SELECT a, b FROM t GROUP BY a, a, b</code> to <code>SELECT a, b FROM t GROUP BY a, b</code>.
     * <p>
     * Duplicate <code>GROUP BY</code> expressions can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsUnnecessaryGroupByExpressions(Boolean value) {
        setTransformPatternsUnnecessaryGroupByExpressions(value);
        return this;
    }

    /**
     * Transform <code>SELECT a, b FROM t ORDER BY a, a, b</code> to <code>SELECT a, b FROM t ORDER BY a, b</code>.
     * <p>
     * Duplicate <code>ORDER BY</code> expressions can be removed.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsUnnecessaryOrderByExpressions(Boolean value) {
        setTransformPatternsUnnecessaryOrderByExpressions(value);
        return this;
    }

    /**
     * Transform <code>[ NOT ] EXISTS (SELECT DISTINCT a, b FROM t ORDER BY c LIMIT d)</code> to <code>[ NOT ] EXISTS (SELECT 1 FROM t)</code>.
     * <p>
     * In <code>EXISTS</code> subqueries, quite a few <code>SELECT</code> clauses are meaningless, and can
     * thus be removed. These include:
     * <ul>
     * <li><code>SELECT</code> (any projection can be ignored)</li>
     * <li><code>DISTINCT</code></li>
     * <li><code>ORDER BY</code></li>
     * <li><code>LIMIT</code> (except <code>LIMIT 0</code>, in case of which {@link #transformPatternsTrivialPredicates} applies).</li>
     * </ul>
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsUnnecessaryExistsSubqueryClauses(Boolean value) {
        setTransformPatternsUnnecessaryExistsSubqueryClauses(value);
        return this;
    }

    /**
     * Transform <code>COUNT(1)</code> or any other <code>COUNT(const)</code> to <code>COUNT(*)</code>.
     * <p>
     * There is no benefit to counting a constant expression. In fact, in some RDBMS, it might even be slightly
     * slower, at least in benchmarks.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsCountConstant(Boolean value) {
        setTransformPatternsCountConstant(value);
        return this;
    }

    /**
     * Transform <code>LTRIM(RTRIM(x))</code> or <code>RTRIM(LTRIM(x))</code> to <code>TRIM(x)</code>.
     * <p>
     * Historically, a few dialects did not implement <code>TRIM(x)</code> or <code>TRIM(BOTH FROM x)</code>,
     * so users worked around this by wrapping <code>LTRIM()</code> and <code>RTRIM()</code> with each other.
     * Maintaining this is usually undesirable, so this transformation helps remove the unwanted wrapping.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsTrim(Boolean value) {
        setTransformPatternsTrim(value);
        return this;
    }

    /**
     * Transform <code>NOT(p AND q)</code> to <code>NOT(p) OR NOT(q)</code>.
     * <p>
     * This transformation normalises a predicate using De Morgan's rules.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNotAnd(Boolean value) {
        setTransformPatternsNotAnd(value);
        return this;
    }

    /**
     * Transform <code>NOT(p OR q)</code> to <code>NOT(p) AND NOT(q)</code>.
     * <p>
     * This transformation normalises a predicate using De Morgan's rules.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNotOr(Boolean value) {
        setTransformPatternsNotOr(value);
        return this;
    }

    /**
     * Transform <code>NOT(NOT(x))</code> to <code>x</code>.
     * <p>
     * This transformation removes a redundant logic negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNotNot(Boolean value) {
        setTransformPatternsNotNot(value);
        return this;
    }

    /**
     * Transform <code>NOT (a != b)</code> to <code>a = b</code>, and similar comparisons.
     * <p>
     * This transformation removes a redundant logical negation from the <code>DISTINCT</code> predicate.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNotComparison(Boolean value) {
        setTransformPatternsNotComparison(value);
        return this;
    }

    /**
     * Transform <code>NOT (a IS NOT DISTINCT FROM b)</code> to <code>a IS DISTINCT FROM b</code>.
     * <p>
     * This transformation removes a redundant logical negation from the <code>DISTINCT</code> predicate.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNotNotDistinct(Boolean value) {
        setTransformPatternsNotNotDistinct(value);
        return this;
    }

    /**
     * Transform <code>a IS [ NOT ] DISTINCT FROM NULL</code> to <code>a IS [ NOT ] NULL</code>.
     * <p>
     * This simplifies the much more verbose <code>DISTINCT</code> predicate.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsDistinctFromNull(Boolean value) {
        setTransformPatternsDistinctFromNull(value);
        return this;
    }

    /**
     * Transform <code>(a + b) + (c + d)</code> to <code>((a + b) + c) + d</code>.
     * <p>
     * This transformation turns trees into lists, which greatly simplifies other tree traversal transformations.
     * Some of those other transformations currently rely on this flag to be active.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNormaliseAssociativeOps(Boolean value) {
        setTransformPatternsNormaliseAssociativeOps(value);
        return this;
    }

    /**
     * Transform <code>x IN (a)</code> to <code>x = a</code> and <code>x NOT IN (a)</code> to <code>x != a</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNormaliseInListSingleElementToComparison(Boolean value) {
        setTransformPatternsNormaliseInListSingleElementToComparison(value);
        return this;
    }

    /**
     * Transform <code>1 = a</code> to <code>a = 1</code>.
     * <p>
     * This transformation inverses {@link TableField} [op] {@link org.jooq.impl.QOM.Val} comparisons, if they're not in that order.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNormaliseFieldCompareValue(Boolean value) {
        setTransformPatternsNormaliseFieldCompareValue(value);
        return this;
    }

    /**
     * Transform 2 argument <code>COALESCE(a, b)</code> to <code>NVL(a, b)</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNormaliseCoalesceToNvl(Boolean value) {
        setTransformPatternsNormaliseCoalesceToNvl(value);
        return this;
    }

    /**
     * Transform <code>x = c1 OR x = c2</code> to <code>x IN (c1, c2)</code>.
     * <p>
     * This transformation simplifies verbose <code>OR</code> predicates into simpler <code>IN</code> predicates.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsOrEqToIn(Boolean value) {
        setTransformPatternsOrEqToIn(value);
        return this;
    }

    /**
     * Transform <code>x != c1 AND x != c2</code> to <code>x NOT IN (c1, c2)</code>.
     * <p>
     * This transformation simplifies verbose <code>AND</code> predicates into simpler <code>NOT IN</code> predicates.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsAndNeToNotIn(Boolean value) {
        setTransformPatternsAndNeToNotIn(value);
        return this;
    }

    /**
     * Transform <code>x = a OR x > a</code> to <code>x >= a</code>.
     * <p>
     * This transformation merges multiple <code>OR</code> connected comparisons to a single comparison using a simpler operator.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsMergeOrComparison(Boolean value) {
        setTransformPatternsMergeOrComparison(value);
        return this;
    }

    /**
     * Transform <code>x >= a AND x <= a</code> to <code>x = a</code>.
     * <p>
     * This transformation merges multiple <code>AND</code> connected comparisons to a single comparison using a simpler operator.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsMergeAndComparison(Boolean value) {
        setTransformPatternsMergeAndComparison(value);
        return this;
    }

    /**
     * Transform <code>x IN (a, b, c) AND x IN (b, c, d)</code> to <code>x IN (b, c)</code>.
     * <p>
     * This transformation merges multiple <code>OR</code> connected comparisons to a single comparison using a simpler operator.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsMergeInLists(Boolean value) {
        setTransformPatternsMergeInLists(value);
        return this;
    }

    /**
     * Transform <code>x >= a AND x <= b</code> to <code>x BETWEEN a AND b</code>.
     * <p>
     * This transformation merges multiple <code>AND</code> connected range predicates to a single comparison using <code>BETWEEN</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsMergeRangePredicates(Boolean value) {
        setTransformPatternsMergeRangePredicates(value);
        return this;
    }

    /**
     * Transform <code>x BETWEEN a AND b OR x BETWEEN b AND a</code> to <code>x BETWEEN SYMMETRIC a AND b</code>.
     * <p>
     * This transformation merges multiple <code>OR</code> connected <code>BETWEEN</code> predicates to a single comparison using <code>BETWEEN SYMMETRIC</code>.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsMergeBetweenSymmetricPredicates(Boolean value) {
        setTransformPatternsMergeBetweenSymmetricPredicates(value);
        return this;
    }

    /**
     * Transform a searched <code>CASE WHEN x = .. WHEN x = ..</code> to a simple <code>CASE x WHEN … WHEN …</code> expression.
     * <p>
     * When a searched <code>CASE</code> expression always compares the same column to a value, then it can be simplified, possibly
     * unlocking further transformations that are available only to the simple <code>CASE</code> expression.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsCaseSearchedToCaseSimple(Boolean value) {
        setTransformPatternsCaseSearchedToCaseSimple(value);
        return this;
    }

    /**
     * Transform <code>CASE … ELSE NULL</code> removing the <code>ELSE</code> clause.
     * <p>
     * <code>CASE WHEN x THEN y ELSE NULL END</code> is equivalent to <code>CASE WHEN x THEN y END</code>.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsCaseElseNull(Boolean value) {
        setTransformPatternsCaseElseNull(value);
        return this;
    }

    /**
     * Transform <code>CASE</code> by removing unreachable clauses.
     * <p>
     * Case clauses can be proven to be unreachable, and thus removed:
     * <ul>
     * <li><code>CASE WHEN p THEN 1 WHEN TRUE THEN 2 WHEN q … ELSE … END</code> is equivalent to <code>CASE WHEN p THEN 1 ELSE 2 END</code></li>
     * <li><code>CASE WHEN p THEN 1 WHEN FALSE THEN 2 WHEN q .. ELSE .. END</code> is equivalent to <code>CASE WHEN p THEN 1 WHEN q … ELSE … END</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsUnreachableCaseClauses(Boolean value) {
        setTransformPatternsUnreachableCaseClauses(value);
        return this;
    }

    /**
     * Transform <code>DECODE</code> by removing unreachable clauses.
     * <p>
     * DECODE clauses can be proven to be unreachable, and thus removed:
     * <ul>
     * <li><code>DECODE(a, b, 1, c, 2, b, 3)</code> is equivalent to <code>DECODE(a, b, 1, c, 2)</code></li>
     * <li><code>DECODE(a, b, 1, c, 2, b, 3, 4)</code> is equivalent to <code>DECODE(a, b, 1, c, 2, 4)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsUnreachableDecodeClauses(Boolean value) {
        setTransformPatternsUnreachableDecodeClauses(value);
        return this;
    }

    /**
     * Transform <code>CASE WHEN a IS NOT DISTINCT FROM b …</code> to an equivalent <code>DECODE</code> function.
     * <p>
     * When all <code>WHEN</code> clauses of a <code>CASE</code> expression use the <code>DISTINCT</code> predicate, then the
     * <code>CASE</code> expression can be transformed into a <code>DECODE</code> function call:
     * <ul>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 END</code> is equivalent to <code>DECODE(a, b, 1)</code></li>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 ELSE 2 END</code> is equivalent to <code>DECODE(a, b, 1, 2)</code></li>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 WHEN a IS NOT DISTINCT FROM c THEN 2 END</code> is equivalent to <code>DECODE(a, b, 1, c, 2)</code></li>
     * <li><code>CASE WHEN a IS NOT DISTINCT FROM b THEN 1 WHEN a IS NOT DISTINCT FROM c THEN 2 ELSE 3 END</code> is equivalent to <code>DECODE(a, b, 1, c, 2, 3)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsCaseDistinctToDecode(Boolean value) {
        setTransformPatternsCaseDistinctToDecode(value);
        return this;
    }

    /**
     * Transform <code>CASE WHEN a THEN x WHEN b THEN x END</code> to <code>CASE WHEN a OR b THEN x END</code>.
     * <p>
     * Two consecutive <code>WHEN</code> clauses can be merged, if their respective <code>THEN</code> clause is identical.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsCaseMergeWhenWhen(Boolean value) {
        setTransformPatternsCaseMergeWhenWhen(value);
        return this;
    }

    /**
     * Transform <code>CASE WHEN a THEN x WHEN b THEN y ELSE y END</code> to <code>CASE WHEN a THEN x ELSE y END</code>.
     * <p>
     * The ultimate <code>WHEN</code> clause can be merged with the <code>ELSE</code>, if their respective result is identical.
     * If the <code>WHEN</code> clause is the only <code>WHEN</code> clause, then the entire <code>CASE</code> expression can
     * be replaced by the <code>ELSE</code> clause content.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsCaseMergeWhenElse(Boolean value) {
        setTransformPatternsCaseMergeWhenElse(value);
        return this;
    }

    /**
     * Transform <code>CASE</code> expressions to their respective abbreviations.
     * <p>
     * Some <code>CASE</code> expressions have a shorter abbreviated form, such as <code>COALESCE()</code> or <code>NULLIF()</code>.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsCaseToCaseAbbreviation(Boolean value) {
        setTransformPatternsCaseToCaseAbbreviation(value);
        return this;
    }

    /**
     * Transform complex predicates into simpler <code>CASE</code> abbreviations.
     * <p>
     * Some predicates can be simplified into case abbreviations, such as, for example
     * <ul>
     * <li><code>a IS NULL OR COALESCE(a = b, FALSE)</code> to <code>NULLIF(a, b) IS NULL</code></li>
     * <li><code>a IS NOT NULL AND COALESCE(a != b, TRUE)</code> to <code>NULLIF(a, b) IS NOT NULL</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsSimplifyCaseAbbreviation(Boolean value) {
        setTransformPatternsSimplifyCaseAbbreviation(value);
        return this;
    }

    /**
     * Flatten nested <code>CASE</code> abbreviations such as <code>NVL</code> or <code>CASE</code>.
     * <p>
     * Nested <code>CASE</code> abbreviations can be flattened, as such:
     * <ul>
     * <li><code>NVL(NVL(a, b), c)</code> to <code>COALESCE(a, b, c)</code></li>
     * <li><code>COALESCE(a, ..., COALESCE(b, ..., c), ..., d)</code> to <code>COALESCE(a, …, b, …, c, ..., d)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsFlattenCaseAbbreviation(Boolean value) {
        setTransformPatternsFlattenCaseAbbreviation(value);
        return this;
    }

    /**
     * Flatten nested <code>DECODE</code> functions.
     * <p>
     * Nested <code>DECODE</code> functions can be flattened, as such:
     * <ul>
     * <li><code>DECODE(a, b, c, DECODE(a, d, e))</code> to <code>DECODE(a, b, c, d, e)</code></li>
     * </ul>
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsFlattenDecode(Boolean value) {
        setTransformPatternsFlattenDecode(value);
        return this;
    }

    /**
     * Transform <code>CASE … ELSE CASE …</code> by flattening the nested <code>CASE</code>.
     * <p>
     * <code>CASE WHEN a THEN b ELSE CASE WHEN c THEN d END END</code> is equivalent to <code>CASE WHEN a THEN b WHEN c THEN d END</code>.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsFlattenCase(Boolean value) {
        setTransformPatternsFlattenCase(value);
        return this;
    }

    /**
     * Transform trivial case abbreviations like <code>NVL(NULL, a)</code> to <code>a</code>.
     * <p>
     * This transformation removes any trivial case abbreviations, such as <code>NVL()</code>,
     * <code>COALESCE()</code>, <code>NULLIF()</code>, etc.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsTrivialCaseAbbreviation(Boolean value) {
        setTransformPatternsTrivialCaseAbbreviation(value);
        return this;
    }

    /**
     * Transform trivial predicates like <code>1 = 1</code> to <code>TRUE</code>.
     * <p>
     * This transformation removes any trivial predicates.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsTrivialPredicates(Boolean value) {
        setTransformPatternsTrivialPredicates(value);
        return this;
    }

    /**
     * Transform trivial bitwise comparisons like <code>BIT_OR(a, 0)</code> to <code>a</code>.
     * <p>
     * This transformation removes any trivial predicates.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsTrivialBitwiseOperations(Boolean value) {
        setTransformPatternsTrivialBitwiseOperations(value);
        return this;
    }

    /**
     * Transform bitwise operations to an equivalent <code>BIT_SET(a, b)</code> or <code>BIT_SET(a, b, c)</code> expression.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsBitSet(Boolean value) {
        setTransformPatternsBitSet(value);
        return this;
    }

    /**
     * Transform bitwise operations to an equivalent <code>BIT_GET(a, b)</code> expression.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsBitGet(Boolean value) {
        setTransformPatternsBitGet(value);
        return this;
    }

    /**
     * Transform predicates comparing scalar subqueries with a count <code>(SELECT COUNT(*) …) > 0</code> to equivalent <code>EXISTS (SELECT 1 …)</code>.
     * <p>
     * Scalar subqueries that count rows and whose count is compared to 0 can be transformed into equivalent, but likely cheaper to execute EXISTS queries.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsScalarSubqueryCountAsteriskGtZero(Boolean value) {
        setTransformPatternsScalarSubqueryCountAsteriskGtZero(value);
        return this;
    }

    /**
     * Transform predicates comparing scalar subqueries with a count <code>(SELECT COUNT(expr) …) > 0</code> to equivalent <code>EXISTS (SELECT 1 … WHERE expr IS NOT NULL)</code>.
     * <p>
     * Scalar subqueries that count non-null expressions and whose count is compared to 0 can be transformed into equivalent, but likely cheaper to execute EXISTS queries.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsScalarSubqueryCountExpressionGtZero(Boolean value) {
        setTransformPatternsScalarSubqueryCountExpressionGtZero(value);
        return this;
    }

    /**
     * Transform empty scalar subqueries like <code>(SELECT 1 WHERE FALSE)</code> to <code>NULL</code>.
     * <p>
     * Scalar subqueries that are guaranteed to produce no results can be replaced by a <code>NULL</code> value.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsEmptyScalarSubquery(Boolean value) {
        setTransformPatternsEmptyScalarSubquery(value);
        return this;
    }

    /**
     * Transform <code>-(-(x))</code> to <code>x</code>
     * <p>
     * This transformation removes a redundant arithmetic negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNegNeg(Boolean value) {
        setTransformPatternsNegNeg(value);
        return this;
    }

    /**
     * Transform <code>~(~(x))</code> to <code>x</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsBitNotBitNot(Boolean value) {
        setTransformPatternsBitNotBitNot(value);
        return this;
    }

    /**
     * Transform <code>~(bitnand(x, y))</code> to <code>bitand(x, y)</code> and <code>~(bitand(x, y)</code> to <code>bitnand(x, y)</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsBitNotBitNand(Boolean value) {
        setTransformPatternsBitNotBitNand(value);
        return this;
    }

    /**
     * Transform <code>~(bitnor(x, y))</code> to <code>bitor(x, y)</code> and <code>~(bitor(x, y)</code> to <code>bitnor(x, y)</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsBitNotBitNor(Boolean value) {
        setTransformPatternsBitNotBitNor(value);
        return this;
    }

    /**
     * Transform <code>~(bitxnor(x, y))</code> to <code>bitxor(x, y)</code> and <code>~(bitxor(x, y)</code> to <code>bitxnor(x, y)</code>.
     * <p>
     * This transformation removes a redundant bitwise negation.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsBitNotBitXNor(Boolean value) {
        setTransformPatternsBitNotBitXNor(value);
        return this;
    }

    /**
     * Any {org.jooq.impl.QOM.UReturnsNullOnNullInput} function or expression with <code>NULL</code> arguments can be replaced by <code>NULL</code>.
     * <p>
     * There are many built-in SQL functions and operators with a <code>RETURNS NULL ON NULL INPUT</code> property, e.g.
     * <ul>
     * <li><code>ABS(NULL)</code></li>
     * <li><code>MOD(NULL, 1)</code></li>
     * <li><code>NULL + 1</code></li>
     * </ul>
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsNullOnNullInput(Boolean value) {
        setTransformPatternsNullOnNullInput(value);
        return this;
    }

    /**
     * Transform all repetitions of idempotent functions, such as <code>UPPER(UPPER(s))</code> to <code>UPPER(s)</code>.
     * <p>
     * Idempotent functions that are covered so far, include:
     * <ul>
     * <li><code>LTRIM(LTRIM(s))</code> to <code>LTRIM(s)</code></li>
     * <li><code>LTRIM(TRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>RTRIM(RTRIM(s))</code> to <code>RTRIM(s)</code></li>
     * <li><code>RTRIM(TRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>TRIM(LTRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>TRIM(RTRIM(s))</code> to <code>TRIM(s)</code></li>
     * <li><code>UPPER(UPPER(s))</code> to <code>UPPER(s)</code></li>
     * <li><code>LOWER(LOWER(s))</code> to <code>LOWER(s)</code></li>
     * </ul>
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsIdempotentFunctionRepetition(Boolean value) {
        setTransformPatternsIdempotentFunctionRepetition(value);
        return this;
    }

    /**
     * Transform <code>a + 1 = 2</code> to <code>a = 2 - 1</code>, and other transformations.
     * <p>
     * It is usually best to compare single columns with constants or expressions to
     * encourage index usage. While function based indexes are possible in some RDBMS,
     * ordinary indexes are more reusable and should be preferred.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsArithmeticComparisons(Boolean value) {
        setTransformPatternsArithmeticComparisons(value);
        return this;
    }

    /**
     * Transform <code>1 / y * x</code> to <code>x / y</code>, and other transformations.
     * <p>
     * This transformation simplifies arithmetic expressions.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsArithmeticExpressions(Boolean value) {
        setTransformPatternsArithmeticExpressions(value);
        return this;
    }

    /**
     * Transform <code>SIN(x) / COS(x)</code> to <code>TAN(x)</code>, and other transformations.
     * <p>
     * This transformation turns expanded trignonometric function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsTrigonometricFunctions(Boolean value) {
        setTransformPatternsTrigonometricFunctions(value);
        return this;
    }

    /**
     * Transform <code>LN(value) / LN(base)</code> to <code>LOG(base, value)</code>, and other transformations.
     * <p>
     * This transformation turns expanded logarithmic function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsLogarithmicFunctions(Boolean value) {
        setTransformPatternsLogarithmicFunctions(value);
        return this;
    }

    /**
     * Transform <code>(EXP(x) - EXP(-x)) / 2</code> to <code>SINH(x)</code>, and other transformations.
     * <p>
     * This transformation turns expanded hyperbolic function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsHyperbolicFunctions(Boolean value) {
        setTransformPatternsHyperbolicFunctions(value);
        return this;
    }

    /**
     * Transform <code>LN(x + SQRT(SQUARE(x) + 1))</code> to <code>ASINH(x)</code>, and other transformations.
     * <p>
     * This transformation turns expanded inverse hyperbolic function definitions into their shorter equivalents.
     * <p>
     * To enable this feature, {@link #transformPatterns} must be enabled as well.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformPatternsInverseHyperbolicFunctions(Boolean value) {
        setTransformPatternsInverseHyperbolicFunctions(value);
        return this;
    }

    /**
     * Transform {@link org.jooq.impl.QOM.CompareCondition} and a few other types of condition to inline their bind values, in case they match
     *          
     * <p>
     * Historically, prior to ANSI join syntax, joins were implemented by listing tables in 
     * the FROM clause and providing join predicates in the WHERE clause, possibly using vendor specific
     * operators like <code>(+)</code> (Oracle, DB2) or <code>*=</code> (SQL Server) for outer join
     * support. For backwards compatibility with older RDBMS versions, ANSI joins in jOOQ code may be
     * converted to equivalent table lists in generated SQL using this flag.
     * <p>
     * This flag has a limited implementation that supports inner joins (in most cases) and outer joins
     * (only for simple comparison predicates).
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformInlineBindValuesForFieldComparisons(Boolean value) {
        setTransformInlineBindValuesForFieldComparisons(value);
        return this;
    }

    /**
     * Transform ANSI join to table lists if possible.
     * <p>
     * Historically, prior to ANSI join syntax, joins were implemented by listing tables in 
     * the FROM clause and providing join predicates in the WHERE clause, possibly using vendor specific
     * operators like <code>(+)</code> (Oracle, DB2) or <code>*=</code> (SQL Server) for outer join
     * support. For backwards compatibility with older RDBMS versions, ANSI joins in jOOQ code may be
     * converted to equivalent table lists in generated SQL using this flag.
     * <p>
     * This flag has a limited implementation that supports inner joins (in most cases) and outer joins
     * (only for simple comparison predicates).
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformAnsiJoinToTableLists(Boolean value) {
        setTransformAnsiJoinToTableLists(value);
        return this;
    }

    /**
     * Transform a subquery from an IN condition with LIMIT to an equivalent derived table.
     * <p>
     * This transformation works around a known MySQL limitation "ERROR 1235 (42000): This version of MySQL doesn't yet support 'LIMIT & IN/ALL/ANY/SOME subquery'"
     * <p>
     * This feature is available in the commercial distribution only.
     * <p>
     * @deprecated - 3.18.0 - [#14634] - The configuration of this transformation is deprecated. It will no longer be commercially available only, but apply also to the jOOQ Open Source Edition, when required.
     *               
     * 
     */
    @Deprecated
    public Settings withTransformInConditionSubqueryWithLimitToDerivedTable(Transformation value) {
        setTransformInConditionSubqueryWithLimitToDerivedTable(value);
        return this;
    }

    /**
     * Transform the <code>QUALIFY</code> clause to an equivalent derived table to filter on window functions.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformQualify(Transformation value) {
        setTransformQualify(value);
        return this;
    }

    /**
     * Transform table lists to ANSI join if possible.
     * <p>
     * (Very) historically, prior to ANSI join syntax, joins were implemented by listing tables in 
     * the FROM clause and providing join predicates in the WHERE clause, possibly using vendor specific
     * operators like <code>(+)</code> (Oracle, DB2) or <code>*=</code> (SQL Server) for outer join
     * support. Migrating such join syntax is tedious. The jOOQ parser can parse the old syntax and
     * this flag enables the transformation to ANSI join syntax.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformTableListsToAnsiJoin(Boolean value) {
        setTransformTableListsToAnsiJoin(value);
        return this;
    }

    /**
     * Transform <code>ROWNUM</code> expressions to corresponding <code>LIMIT</code> clauses or <code>ROW_NUMBER()</code> expressions.
     * <p>
     * In Oracle 11g and less, <code>ROWNUM</code> filtering was the most popular way to paginate. This pseudo
     * column is not supported in other RDBMS, and should be replaced in Oracle 12c by the FETCH clause or
     * <code>ROW_NUMBER() OVER ()</code> filtering. This transformation allows for replacing such a filter by
     * equivalent SQL, if possible.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformRownum(Transformation value) {
        setTransformRownum(value);
        return this;
    }

    /**
     * Transform arithmetic expressions on literals and bind variables.
     * <p>
     * Arithmetic expressions may be implemented by the user, or arise from emulations from within jOOQ.
     * Expressions on literals and bind variables could be evaluated in the client prior to generating SQL.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformUnneededArithmeticExpressions(TransformUnneededArithmeticExpressions value) {
        setTransformUnneededArithmeticExpressions(value);
        return this;
    }

    /**
     * Transform <code>GROUP BY [column index]</code> clauses by substituting the column index.
     * <p>
     * Not all dialects support grouping by column index, which is a convenient but also a bit confusing feature of
     * some dialects. jOOQ can transform the syntax into an equivalent syntax where the referenced <code>SELECT</code>
     * expression is duplicated into the <code>GROUP BY</code> clause.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformGroupByColumnIndex(Transformation value) {
        setTransformGroupByColumnIndex(value);
        return this;
    }

    /**
     * Transform Common Table Expressions (CTE) by inlining their <code>WITH</code> clause definition to wherever they're referenced.
     * <p>
     * Non-recursive CTE are just syntax sugar for inline views (derived tables). When they're not supported natively,
     * jOOQ can simply inline their definition to wherever they're referenced.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withTransformInlineCTE(Transformation value) {
        setTransformInlineCTE(value);
        return this;
    }

    /**
     * Whether string literals should be escaped with backslash.
     * 
     */
    public Settings withBackslashEscaping(BackslashEscaping value) {
        setBackslashEscaping(value);
        return this;
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
     */
    public Settings withParamType(ParamType value) {
        setParamType(value);
        return this;
    }

    /**
     * Whether rendered bind values should be cast to their respective type.
     * 
     */
    public Settings withParamCastMode(ParamCastMode value) {
        setParamCastMode(value);
        return this;
    }

    /**
     * The type of statement that is to be executed.
     * 
     */
    public Settings withStatementType(StatementType value) {
        setStatementType(value);
        return this;
    }

    /**
     * The maximum number of allowed bind variables before inlining all values where <code>0</code> uses the dialect defaults: <ul>
     * <li>{@link org.jooq.SQLDialect#ACCESS} : 768</li>
     * <li>{@link org.jooq.SQLDialect#ASE} : 2000</li>
     * <li>{@link org.jooq.SQLDialect#INGRES} : 1024</li>
     * <li>{@link org.jooq.SQLDialect#ORACLE} : 32767</li>
     * <li>{@link org.jooq.SQLDialect#POSTGRES} : 32767</li>
     * <li>{@link org.jooq.SQLDialect#SQLITE} : 999</li>
     * <li>{@link org.jooq.SQLDialect#SQLSERVER} : 2100</li>
     * </ul>
     * 
     */
    public Settings withInlineThreshold(Integer value) {
        setInlineThreshold(value);
        return this;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.TransactionListener}s.
     * 
     */
    public Settings withTransactionListenerStartInvocationOrder(InvocationOrder value) {
        setTransactionListenerStartInvocationOrder(value);
        return this;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.TransactionListener}s.
     * 
     */
    public Settings withTransactionListenerEndInvocationOrder(InvocationOrder value) {
        setTransactionListenerEndInvocationOrder(value);
        return this;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.MigrationListener}s.
     * 
     */
    public Settings withMigrationListenerStartInvocationOrder(InvocationOrder value) {
        setMigrationListenerStartInvocationOrder(value);
        return this;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.MigrationListener}s.
     * 
     */
    public Settings withMigrationListenerEndInvocationOrder(InvocationOrder value) {
        setMigrationListenerEndInvocationOrder(value);
        return this;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.VisitListener}s.
     * 
     */
    public Settings withVisitListenerStartInvocationOrder(InvocationOrder value) {
        setVisitListenerStartInvocationOrder(value);
        return this;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.VisitListener}s.
     * 
     */
    public Settings withVisitListenerEndInvocationOrder(InvocationOrder value) {
        setVisitListenerEndInvocationOrder(value);
        return this;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.RecordListener}s.
     * 
     */
    public Settings withRecordListenerStartInvocationOrder(InvocationOrder value) {
        setRecordListenerStartInvocationOrder(value);
        return this;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.RecordListener}s.
     * 
     */
    public Settings withRecordListenerEndInvocationOrder(InvocationOrder value) {
        setRecordListenerEndInvocationOrder(value);
        return this;
    }

    /**
     * The order of invocation for [action]start() methods registered {@link org.jooq.ExecuteListener}s.
     * 
     */
    public Settings withExecuteListenerStartInvocationOrder(InvocationOrder value) {
        setExecuteListenerStartInvocationOrder(value);
        return this;
    }

    /**
     * The order of invocation for [action]end() methods registered {@link org.jooq.ExecuteListener}s.
     * 
     */
    public Settings withExecuteListenerEndInvocationOrder(InvocationOrder value) {
        setExecuteListenerEndInvocationOrder(value);
        return this;
    }

    /**
     * When set to true, this will add jOOQ's default {@link org.jooq.tools.LoggerListener} for debug logging. This is meant for use in development only.
     * 
     */
    public Settings withExecuteLogging(Boolean value) {
        setExecuteLogging(value);
        return this;
    }

    /**
     * [#14420] Whether constraint violations and other {@link java.sql.SQLException} should produce additional log information about the column name and data causing the problem. Unlike {@link #executeLogging}, this is meant for use in production as well as development. This feature is available only in commercial distributions.
     * 
     */
    public Settings withExecuteLoggingSQLExceptions(Boolean value) {
        setExecuteLoggingSQLExceptions(value);
        return this;
    }

    /**
     * When set to true, this will add jOOQ's default logging DiagnosticsListeners.
     * 
     */
    public Settings withDiagnosticsLogging(Boolean value) {
        setDiagnosticsLogging(value);
        return this;
    }

    /**
     * Whether to activate the DiagnosticsConnection, explicit by <code>DEFAULT</code>, implicit if <code>ON</code>, or turned <code>OFF</code> entirely.
     * 
     */
    public Settings withDiagnosticsConnection(DiagnosticsConnection value) {
        setDiagnosticsConnection(value);
        return this;
    }

    /**
     * Whether store(), insert(), and update() methods should update the record version prior to the operation, for use with {@link #executeWithOptimisticLocking}.
     * 
     */
    public Settings withUpdateRecordVersion(Boolean value) {
        setUpdateRecordVersion(value);
        return this;
    }

    /**
     * Whether store(), insert(), and update() methods should update the record timestamp prior to the operation, for use with {@link #executeWithOptimisticLocking}.
     * 
     */
    public Settings withUpdateRecordTimestamp(Boolean value) {
        setUpdateRecordTimestamp(value);
        return this;
    }

    /**
     * Whether store() and delete() methods should be executed with optimistic locking.
     * 
     */
    public Settings withExecuteWithOptimisticLocking(Boolean value) {
        setExecuteWithOptimisticLocking(value);
        return this;
    }

    /**
     * Whether store() and delete() methods should be executed with optimistic locking also on "unversioned" tables,
     * i.e. on tables that do not have a version and/or timestamp column.
     * <p>
     * This flag has no effect when "executeWithOptimisticLocking" is turned off.
     * 
     */
    public Settings withExecuteWithOptimisticLockingExcludeUnversioned(Boolean value) {
        setExecuteWithOptimisticLockingExcludeUnversioned(value);
        return this;
    }

    /**
     * Whether fetched records should be attached to the fetching configuration.
     * 
     */
    public Settings withAttachRecords(Boolean value) {
        setAttachRecords(value);
        return this;
    }

    /**
     * Whether {@link org.jooq.TableRecord#insert()} calls should be executed if the record is unchanged. This also affects the <code>INSERT</code> part of {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#merge()} calls.
     * 
     */
    public Settings withInsertUnchangedRecords(Boolean value) {
        setInsertUnchangedRecords(value);
        return this;
    }

    /**
     * Whether {@link org.jooq.UpdatableRecord#update()} calls should be executed if the record is unchanged. This also affects the <code>UPDATE</code> part of {@link org.jooq.UpdatableRecord#store()} and {@link org.jooq.UpdatableRecord#merge()} calls.
     * 
     */
    public Settings withUpdateUnchangedRecords(UpdateUnchangedRecords value) {
        setUpdateUnchangedRecords(value);
        return this;
    }

    /**
     * Whether primary key values are deemed to be "updatable" in jOOQ.
     * <p>
     * Setting this to "true" will allow for updating primary key values through
     * UpdatableRecord.store() and UpdatableRecord.update().
     * 
     */
    public Settings withUpdatablePrimaryKeys(Boolean value) {
        setUpdatablePrimaryKeys(value);
        return this;
    }

    /**
     * Whether reflection information should be cached in the configuration.
     * 
     */
    public Settings withReflectionCaching(Boolean value) {
        setReflectionCaching(value);
        return this;
    }

    /**
     * Whether record mappers should be cached in the configuration.
     * 
     */
    public Settings withCacheRecordMappers(Boolean value) {
        setCacheRecordMappers(value);
        return this;
    }

    /**
     * Whether parsing connection translations should be cached in the configuration.
     * 
     */
    public Settings withCacheParsingConnection(Boolean value) {
        setCacheParsingConnection(value);
        return this;
    }

    /**
     * The default implementation of the ParsingConnection cache's LRU cache size.
     * 
     */
    public Settings withCacheParsingConnectionLRUCacheSize(Integer value) {
        setCacheParsingConnectionLRUCacheSize(value);
        return this;
    }

    /**
     * Whether JDBC {@link java.sql.PreparedStatement} instances should be cached in loader API.
     * 
     */
    public Settings withCachePreparedStatementInLoader(Boolean value) {
        setCachePreparedStatementInLoader(value);
        return this;
    }

    /**
     * A strategy defining how exceptions from the database / JDBC driver should be propagated
     * 
     */
    public Settings withThrowExceptions(ThrowExceptions value) {
        setThrowExceptions(value);
        return this;
    }

    /**
     * Whether warnings should be fetched after each query execution.
     * 
     */
    public Settings withFetchWarnings(Boolean value) {
        setFetchWarnings(value);
        return this;
    }

    /**
     * Whether server output should be fetched after each query execution.
     * 
     */
    public Settings withFetchServerOutputSize(Integer value) {
        setFetchServerOutputSize(value);
        return this;
    }

    /**
     * Whether calls to store(), insert() and update() should return the identity column.
     * 
     */
    public Settings withReturnIdentityOnUpdatableRecord(Boolean value) {
        setReturnIdentityOnUpdatableRecord(value);
        return this;
    }

    /**
     * Whether calls to store(), insert() and update() should return values for columns that are {@link org.jooq.DataType#defaulted()}.
     * 
     */
    public Settings withReturnDefaultOnUpdatableRecord(Boolean value) {
        setReturnDefaultOnUpdatableRecord(value);
        return this;
    }

    /**
     * Whether calls to store(), insert() and update() should return values for columns that are {@link org.jooq.DataType#computed()}.
     * 
     */
    public Settings withReturnComputedOnUpdatableRecord(Boolean value) {
        setReturnComputedOnUpdatableRecord(value);
        return this;
    }

    /**
     * Whether calls to store(), insert() and update() should return all columns, not just identity columns.
     * <p>
     * Do note that only few databases support this feature. It is supported only in case the INSERT's or UPDATE's
     * RETURNING clause is fully supported, also for non-IDENTITY columns.
     * 
     */
    public Settings withReturnAllOnUpdatableRecord(Boolean value) {
        setReturnAllOnUpdatableRecord(value);
        return this;
    }

    /**
     * Whether calls to store(), insert(), update(), and delete() that are called on an UpdatableRecord
     * that is created from a POJO (e.g. in a DAO) should return all Record values to the POJO, including
     * IDENTITY values, and if {@link #returnAllOnUpdatableRecord} is active, also other values.
     * 
     */
    public Settings withReturnRecordToPojo(Boolean value) {
        setReturnRecordToPojo(value);
        return this;
    }

    /**
     * Whether JPA annotations should be considered by the DefaultRecordMapper.
     * 
     */
    public Settings withMapJPAAnnotations(Boolean value) {
        setMapJPAAnnotations(value);
        return this;
    }

    /**
     * Whether constructor parameter names obtained from the {@link java.lang.Record} component names should be considered by the DefaultRecordMapper.
     * 
     */
    public Settings withMapRecordComponentParameterNames(Boolean value) {
        setMapRecordComponentParameterNames(value);
        return this;
    }

    /**
     * Whether constructor parameter names obtained from the {@link java.beans.ConstructorProperties} annotation should be considered by the DefaultRecordMapper.
     * 
     */
    public Settings withMapConstructorPropertiesParameterNames(Boolean value) {
        setMapConstructorPropertiesParameterNames(value);
        return this;
    }

    /**
     * Whether constructor parameter names obtained via reflection in Java 8+ should be considered by the DefaultRecordMapper. This flag has no effect in Java 6 or 7.
     * 
     */
    public Settings withMapConstructorParameterNames(Boolean value) {
        setMapConstructorParameterNames(value);
        return this;
    }

    /**
     * Whether constructor parameter names obtained via reflection in Kotlin should be considered by the DefaultRecordMapper. This flag has no effect in Java.
     * 
     */
    public Settings withMapConstructorParameterNamesInKotlin(Boolean value) {
        setMapConstructorParameterNamesInKotlin(value);
        return this;
    }

    /**
     * The default JDBC poolable property that should be applied to all
     * jOOQ queries, for which no specific poolable flag was specified.
     * 
     */
    public Settings withQueryPoolable(QueryPoolable value) {
        setQueryPoolable(value);
        return this;
    }

    /**
     * The default JDBC queryTimeout property that should be applied to all
     * jOOQ queries, for which no specific queryTimeout was specified.
     * 
     */
    public Settings withQueryTimeout(Integer value) {
        setQueryTimeout(value);
        return this;
    }

    /**
     * The default JDBC maxRows property that should be applied to all
     * jOOQ queries, for which no specific maxRows value was specified.
     * 
     */
    public Settings withMaxRows(Integer value) {
        setMaxRows(value);
        return this;
    }

    /**
     * The default JDBC fetchSize property that should be applied to all
     * jOOQ queries, for which no specific fetchSize value was specified.
     * 
     */
    public Settings withFetchSize(Integer value) {
        setFetchSize(value);
        return this;
    }

    /**
     * A property specifying a batch size that should be applied to all automatically created {@link org.jooq.tools.jdbc.BatchedConnection} instances.
     * 
     */
    public Settings withBatchSize(Integer value) {
        setBatchSize(value);
        return this;
    }

    /**
     * [#5570] Whether exception stack traces should be enhanced with additional debug information.
     * 
     */
    public Settings withDebugInfoOnStackTrace(Boolean value) {
        setDebugInfoOnStackTrace(value);
        return this;
    }

    /**
     * [#5600] Whether IN lists in IN predicates should be padded to powers of inListPadBase (default 2).
     * 
     */
    public Settings withInListPadding(Boolean value) {
        setInListPadding(value);
        return this;
    }

    /**
     * [#7095] The base to use to calculate the powers of when applying in list padding.
     * 
     */
    public Settings withInListPadBase(Integer value) {
        setInListPadBase(value);
        return this;
    }

    /**
     * [#5826] The delimiter character to be used to delimit statements in batches.
     * 
     */
    public Settings withDelimiter(String value) {
        setDelimiter(value);
        return this;
    }

    /**
     * [#6462] Use only the primary key to emulate MySQL's INSERT .. ON DUPLICATE KEY UPDATE statement. In MySQL, the statement considers all unique keys for duplicates to apply an update rather than an insert. Earlier versions of jOOQ considered only the PRIMARY KEY. This flag can be turned on to maintain backwards compatibility.
     * 
     */
    public Settings withEmulateOnDuplicateKeyUpdateOnPrimaryKeyOnly(Boolean value) {
        setEmulateOnDuplicateKeyUpdateOnPrimaryKeyOnly(value);
        return this;
    }

    /**
     * [#3884] How <code>MULTISET</code> support should be emulated.
     * 
     */
    public Settings withEmulateMultiset(NestedCollectionEmulation value) {
        setEmulateMultiset(value);
        return this;
    }

    /**
     * [#13418] Whether computed columns should be emulated in the client.
     * <p>
     * This can be useful if a schema was generated using a dialect that supports computed columns, but it is
     * deployed on an RDBMS that does not.
     * 
     */
    public Settings withEmulateComputedColumns(Boolean value) {
        setEmulateComputedColumns(value);
        return this;
    }

    /**
     * [#6771] Specifies whether UPDATE statements are allowed to be executed lacking a WHERE clause. This has no effect on rendering the statements SQL string.
     * 
     */
    public Settings withExecuteUpdateWithoutWhere(ExecuteWithoutWhere value) {
        setExecuteUpdateWithoutWhere(value);
        return this;
    }

    /**
     * [#6771] Specifies whether DELETE statements are allowed to be executed lacking a WHERE clause. This has no effect on rendering the statements SQL string.
     * 
     */
    public Settings withExecuteDeleteWithoutWhere(ExecuteWithoutWhere value) {
        setExecuteDeleteWithoutWhere(value);
        return this;
    }

    /**
     * [#7337] The dialect that should be used to interpret SQL DDL statements. {@link SQLDialect#DEFAULT} means that jOOQ interprets the SQL itself. Any other dialect (if supported) will be interpreted on an actual JDBC connection.
     * 
     */
    public Settings withInterpreterDialect(SQLDialect value) {
        setInterpreterDialect(value);
        return this;
    }

    /**
     * [#9633] The case sensitivity of identifiers used when interpreting SQL DDL statements.
     * 
     */
    public Settings withInterpreterNameLookupCaseSensitivity(InterpreterNameLookupCaseSensitivity value) {
        setInterpreterNameLookupCaseSensitivity(value);
        return this;
    }

    /**
     * The Locale to be used with any interpreter locale dependent logic, defaulting to {@link #getLocale()}.
     * 
     */
    public Settings withInterpreterLocale(Locale value) {
        setInterpreterLocale(value);
        return this;
    }

    /**
     * Using this flag, the interpreter will be able to delay the addition of foreign key declarations until the end of the interpretation run.
     * 
     */
    public Settings withInterpreterDelayForeignKeyDeclarations(Boolean value) {
        setInterpreterDelayForeignKeyDeclarations(value);
        return this;
    }

    /**
     * The {@link org.jooq.Meta} implementation that is backed by {@link java.sql.DatabaseMetaData} does not produce system generated indexes on constraints, by default.
     * 
     */
    public Settings withMetaIncludeSystemIndexes(Boolean value) {
        setMetaIncludeSystemIndexes(value);
        return this;
    }

    /**
     * The {@link org.jooq.Meta} implementation that is backed by {@link java.sql.DatabaseMetaData} does not produce system generated sequences, by default.
     * 
     */
    public Settings withMetaIncludeSystemSequences(Boolean value) {
        setMetaIncludeSystemSequences(value);
        return this;
    }

    /**
     * Whether migrations are allowed to be executed in inverse order.<p><strong>This is a potentially destructive feature, which should not be turned on in production</strong>. It is useful mostly to quickly switch between branches in a development environment. This feature is available only in commercial distributions.
     * 
     */
    public Settings withMigrationAllowsUndo(Boolean value) {
        setMigrationAllowsUndo(value);
        return this;
    }

    /**
     * Whether migrations revert any untracked changes in the schemas that are being migrated.<p><strong>This is a potentially destructive feature, which should not be turned on in production</strong>. It is useful mostly to quickly revert any elements created in a development environment. This feature is available only in commercial distributions.
     * 
     */
    public Settings withMigrationRevertUntracked(Boolean value) {
        setMigrationRevertUntracked(value);
        return this;
    }

    /**
     * Whether to automatically existing schemas that are not yet managed by jOOQ Migrations.
     * 
     */
    public Settings withMigrationAutoBaseline(Boolean value) {
        setMigrationAutoBaseline(value);
        return this;
    }

    /**
     * Whether a migration automatically runs a validation first.
     * 
     */
    public Settings withMigrationAutoValidation(Boolean value) {
        setMigrationAutoValidation(value);
        return this;
    }

    /**
     * Various <code>migrateTo()</code> methods (e.g. {@link org.jooq.Meta#migrateTo(org.jooq.Meta)}) ignore the difference between <code>TIMESTAMP</code> and <code>TIMESTAMP(6)</code>, if 6 is the default precision for timestamps on the configured dialect.
     * 
     */
    public Settings withMigrationIgnoreDefaultTimestampPrecisionDiffs(Boolean value) {
        setMigrationIgnoreDefaultTimestampPrecisionDiffs(value);
        return this;
    }

    /**
     * The Locale to be used with any locale dependent logic if there is not a more specific locale available. More specific locales include e.g. {@link #getRenderLocale()}, {@link #getParseLocale()}, or {@link #getInterpreterLocale()}.
     * 
     */
    public Settings withLocale(Locale value) {
        setLocale(value);
        return this;
    }

    /**
     * [#7337] The input dialect that should be chosen to disambiguate ambiguous SQL syntax.
     * 
     */
    public Settings withParseDialect(SQLDialect value) {
        setParseDialect(value);
        return this;
    }

    /**
     * The Locale to be used with any parser locale dependent logic, defaulting to {@link #getLocale()}.
     * 
     */
    public Settings withParseLocale(Locale value) {
        setParseLocale(value);
        return this;
    }

    /**
     * The date format to use when parsing functions whose behaviour depends on some session date format, such as NLS_DATE_FORMAT in Oracle
     * 
     */
    public Settings withParseDateFormat(String value) {
        setParseDateFormat(value);
        return this;
    }

    /**
     * The timestamp format to use when parsing functions whose behaviour depends on some session date format, such as NLS_TIMESTAMP_FORMAT in Oracle
     * 
     */
    public Settings withParseTimestampFormat(String value) {
        setParseTimestampFormat(value);
        return this;
    }

    /**
     * The prefix to use for named parameters in parsed SQL.
     * <p>
     * Named parameter syntax defaults to <code>:name</code> (such as supported by Oracle, JPA, Spring), but 
     * vendor specific parameters may look differently. This flag can be used to determine the prefix to be
     * used by named parameters, such as <code>@</code> for SQL Server's <code>@name</code> or <code>$</code>
     * for PostgreSQL's <code>$name</code> when parsing SQL.
     * <p>
     * "Named indexed" parameters can be obtained in the same way by specifingy {@code ParamType#NAMED} and not
     * providing a name to parameters, resulting in <code>:1</code> or <code>@1</code> or <code>$1</code>, etc.
     * 
     */
    public Settings withParseNamedParamPrefix(String value) {
        setParseNamedParamPrefix(value);
        return this;
    }

    /**
     * [#7337] The default name case for parsed identifiers.
     * 
     */
    public Settings withParseNameCase(ParseNameCase value) {
        setParseNameCase(value);
        return this;
    }

    /**
     * [#7163] Whether the parser should perform meta lookups in the Configuration's MetaProvider.
     * 
     */
    public Settings withParseWithMetaLookups(ParseWithMetaLookups value) {
        setParseWithMetaLookups(value);
        return this;
    }

    /**
     * Transform the parsed SQL to append missing table references to the query's <code>FROM</code> or <code>USING</code> clause, if applicable.
     * <p>
     * Teradata (and possibly others) allow for referencing tables that are not listed in the <code>FROM</code>
     * clause, such as <code>SELECT t.* FROM t WHERE t.i = u.i</code>. This transformation is executed in the
     * parser, to produce <code>SELECT t.* FROM t, u WHERE t.i = u.i</code>, instead. By default, it is active
     * when the input dialect supports this syntax.
     * <p>
     * This feature is available in the commercial distribution only.
     * 
     */
    public Settings withParseAppendMissingTableReferences(Transformation value) {
        setParseAppendMissingTableReferences(value);
        return this;
    }

    /**
     * [#9780] Whether commands of the type <code>SET key = value</code> should be parsed rather than ignored.
     * 
     */
    public Settings withParseSetCommands(Boolean value) {
        setParseSetCommands(value);
        return this;
    }

    /**
     * [#5917] Whether the parser should accept unsupported (but known) syntax.
     * 
     */
    public Settings withParseUnsupportedSyntax(ParseUnsupportedSyntax value) {
        setParseUnsupportedSyntax(value);
        return this;
    }

    /**
     * [#7344] Whether the parser should accept unknown functions.
     * 
     */
    public Settings withParseUnknownFunctions(ParseUnknownFunctions value) {
        setParseUnknownFunctions(value);
        return this;
    }

    /**
     * [#13109] Whether the parser of the jOOQ Open Source Edition should ignore commercial only features, rather than failing.
     * 
     */
    public Settings withParseIgnoreCommercialOnlyFeatures(Boolean value) {
        setParseIgnoreCommercialOnlyFeatures(value);
        return this;
    }

    /**
     * [#8325] Whether the parser should ignore content between ignore comment tokens.
     * 
     */
    public Settings withParseIgnoreComments(Boolean value) {
        setParseIgnoreComments(value);
        return this;
    }

    /**
     * [#8325] The ignore comment start token
     * 
     */
    public Settings withParseIgnoreCommentStart(String value) {
        setParseIgnoreCommentStart(value);
        return this;
    }

    /**
     * [#8325] The ignore comment stop token
     * 
     */
    public Settings withParseIgnoreCommentStop(String value) {
        setParseIgnoreCommentStop(value);
        return this;
    }

    /**
     * [#12538] Whether the parser should retain comments and whitespace between queries when parsing multiple queries through {@link org.jooq.Parser#parse(String)}.
     * <p>
     * jOOQ's query object model doesn't have a way to represent comments
     * or other whitespace, and as such, the parser simply skips them by default.
     * However, it may be desirable to retain comments before or in between top
     * level queries, when parsing multiple such queries in a script. Comments
     * inside of queries (including procedural statements) are still not supported.
     * 
     */
    public Settings withParseRetainCommentsBetweenQueries(Boolean value) {
        setParseRetainCommentsBetweenQueries(value);
        return this;
    }

    /**
     * [#8469] Whether to parse default expressions retrieved from {@link java.sql.DatabaseMetaData}.
     * 
     */
    public Settings withParseMetaDefaultExpressions(Boolean value) {
        setParseMetaDefaultExpressions(value);
        return this;
    }

    /**
     * [#9864] The behaviour when trying to insert into readonly columns using {@link org.jooq.TableRecord#insert()}.
     * 
     */
    public Settings withReadonlyTableRecordInsert(WriteIfReadonly value) {
        setReadonlyTableRecordInsert(value);
        return this;
    }

    /**
     * [#9864] The behaviour when trying to update a readonly column using {@link org.jooq.UpdatableRecord#update()}.
     * 
     */
    public Settings withReadonlyUpdatableRecordUpdate(WriteIfReadonly value) {
        setReadonlyUpdatableRecordUpdate(value);
        return this;
    }

    /**
     * [#9864] The behaviour when trying to insert into readonly columns using {@link org.jooq.Insert} statements, or the insert clause of a {@link org.jooq.Merge} statement.
     * 
     */
    public Settings withReadonlyInsert(WriteIfReadonly value) {
        setReadonlyInsert(value);
        return this;
    }

    /**
     * [#9864] The behaviour when trying to update a readonly column using {@link org.jooq.Update} statements, or the update clause of a {@link org.jooq.Merge} statement.
     * 
     */
    public Settings withReadonlyUpdate(WriteIfReadonly value) {
        setReadonlyUpdate(value);
        return this;
    }

    /**
     * [#7963] Apply workaround for ORA-04043 when inserting into Oracle tables with qualified, quoted identifiers, and fetching generated keys
     * 
     */
    public Settings withApplyWorkaroundFor7962(Boolean value) {
        setApplyWorkaroundFor7962(value);
        return this;
    }

    public Settings withInterpreterSearchPath(InterpreterSearchSchema... values) {
        if (values!= null) {
            for (InterpreterSearchSchema value: values) {
                getInterpreterSearchPath().add(value);
            }
        }
        return this;
    }

    public Settings withInterpreterSearchPath(Collection<InterpreterSearchSchema> values) {
        if (values!= null) {
            getInterpreterSearchPath().addAll(values);
        }
        return this;
    }

    public Settings withInterpreterSearchPath(List<InterpreterSearchSchema> interpreterSearchPath) {
        setInterpreterSearchPath(interpreterSearchPath);
        return this;
    }

    public Settings withMigrationSchemata(MigrationSchema... values) {
        if (values!= null) {
            for (MigrationSchema value: values) {
                getMigrationSchemata().add(value);
            }
        }
        return this;
    }

    public Settings withMigrationSchemata(Collection<MigrationSchema> values) {
        if (values!= null) {
            getMigrationSchemata().addAll(values);
        }
        return this;
    }

    public Settings withMigrationSchemata(List<MigrationSchema> migrationSchemata) {
        setMigrationSchemata(migrationSchemata);
        return this;
    }

    public Settings withParseSearchPath(ParseSearchSchema... values) {
        if (values!= null) {
            for (ParseSearchSchema value: values) {
                getParseSearchPath().add(value);
            }
        }
        return this;
    }

    public Settings withParseSearchPath(Collection<ParseSearchSchema> values) {
        if (values!= null) {
            getParseSearchPath().addAll(values);
        }
        return this;
    }

    public Settings withParseSearchPath(List<ParseSearchSchema> parseSearchPath) {
        setParseSearchPath(parseSearchPath);
        return this;
    }

    @Override
    public final void appendTo(XMLBuilder builder) {
        builder.append("forceIntegerTypesOnZeroScaleDecimals", forceIntegerTypesOnZeroScaleDecimals);
        builder.append("renderCatalog", renderCatalog);
        builder.append("renderSchema", renderSchema);
        builder.append("renderTable", renderTable);
        builder.append("renderMapping", renderMapping);
        builder.append("renderQuotedNames", renderQuotedNames);
        builder.append("renderNameCase", renderNameCase);
        builder.append("renderNameStyle", renderNameStyle);
        builder.append("renderNamedParamPrefix", renderNamedParamPrefix);
        builder.append("renderKeywordCase", renderKeywordCase);
        builder.append("renderKeywordStyle", renderKeywordStyle);
        builder.append("renderLocale", renderLocale);
        builder.append("renderFormatted", renderFormatted);
        builder.append("renderFormatting", renderFormatting);
        builder.append("renderOptionalAssociativityParentheses", renderOptionalAssociativityParentheses);
        builder.append("renderOptionalAsKeywordForTableAliases", renderOptionalAsKeywordForTableAliases);
        builder.append("renderOptionalAsKeywordForFieldAliases", renderOptionalAsKeywordForFieldAliases);
        builder.append("renderOptionalInnerKeyword", renderOptionalInnerKeyword);
        builder.append("renderOptionalOuterKeyword", renderOptionalOuterKeyword);
        builder.append("renderImplicitWindowRange", renderImplicitWindowRange);
        builder.append("renderScalarSubqueriesForStoredFunctions", renderScalarSubqueriesForStoredFunctions);
        builder.append("renderImplicitJoinType", renderImplicitJoinType);
        builder.append("renderDefaultNullability", renderDefaultNullability);
        builder.append("renderCoalesceToEmptyStringInConcat", renderCoalesceToEmptyStringInConcat);
        builder.append("renderOrderByRownumberForEmulatedPagination", renderOrderByRownumberForEmulatedPagination);
        builder.append("renderOutputForSQLServerReturningClause", renderOutputForSQLServerReturningClause);
        builder.append("renderGroupConcatMaxLenSessionVariable", renderGroupConcatMaxLenSessionVariable);
        builder.append("renderParenthesisAroundSetOperationQueries", renderParenthesisAroundSetOperationQueries);
        builder.append("renderVariablesInDerivedTablesForEmulations", renderVariablesInDerivedTablesForEmulations);
        builder.append("renderRowConditionForSeekClause", renderRowConditionForSeekClause);
        builder.append("renderRedundantConditionForSeekClause", renderRedundantConditionForSeekClause);
        builder.append("renderPlainSQLTemplatesAsRaw", renderPlainSQLTemplatesAsRaw);
        builder.append("namePathSeparator", namePathSeparator);
        builder.append("bindOffsetDateTimeType", bindOffsetDateTimeType);
        builder.append("bindOffsetTimeType", bindOffsetTimeType);
        builder.append("fetchTriggerValuesAfterSQLServerOutput", fetchTriggerValuesAfterSQLServerOutput);
        builder.append("fetchIntermediateResult", fetchIntermediateResult);
        builder.append("diagnosticsDuplicateStatements", diagnosticsDuplicateStatements);
        builder.append("diagnosticsDuplicateStatementsUsingTransformPatterns", diagnosticsDuplicateStatementsUsingTransformPatterns);
        builder.append("diagnosticsMissingWasNullCall", diagnosticsMissingWasNullCall);
        builder.append("diagnosticsRepeatedStatements", diagnosticsRepeatedStatements);
        builder.append("diagnosticsConsecutiveAggregation", diagnosticsConsecutiveAggregation);
        builder.append("diagnosticsConcatenationInPredicate", diagnosticsConcatenationInPredicate);
        builder.append("diagnosticsPossiblyWrongExpression", diagnosticsPossiblyWrongExpression);
        builder.append("diagnosticsTooManyColumnsFetched", diagnosticsTooManyColumnsFetched);
        builder.append("diagnosticsTooManyRowsFetched", diagnosticsTooManyRowsFetched);
        builder.append("diagnosticsUnnecessaryWasNullCall", diagnosticsUnnecessaryWasNullCall);
        builder.append("diagnosticsPatterns", diagnosticsPatterns);
        builder.append("diagnosticsTrivialCondition", diagnosticsTrivialCondition);
        builder.append("diagnosticsNullCondition", diagnosticsNullCondition);
        builder.append("transformPatterns", transformPatterns);
        builder.append("transformPatternsLogging", transformPatternsLogging);
        builder.append("transformPatternsUnnecessaryDistinct", transformPatternsUnnecessaryDistinct);
        builder.append("transformPatternsUnnecessaryScalarSubquery", transformPatternsUnnecessaryScalarSubquery);
        builder.append("transformPatternsUnnecessaryInnerJoin", transformPatternsUnnecessaryInnerJoin);
        builder.append("transformPatternsUnnecessaryGroupByExpressions", transformPatternsUnnecessaryGroupByExpressions);
        builder.append("transformPatternsUnnecessaryOrderByExpressions", transformPatternsUnnecessaryOrderByExpressions);
        builder.append("transformPatternsUnnecessaryExistsSubqueryClauses", transformPatternsUnnecessaryExistsSubqueryClauses);
        builder.append("transformPatternsCountConstant", transformPatternsCountConstant);
        builder.append("transformPatternsTrim", transformPatternsTrim);
        builder.append("transformPatternsNotAnd", transformPatternsNotAnd);
        builder.append("transformPatternsNotOr", transformPatternsNotOr);
        builder.append("transformPatternsNotNot", transformPatternsNotNot);
        builder.append("transformPatternsNotComparison", transformPatternsNotComparison);
        builder.append("transformPatternsNotNotDistinct", transformPatternsNotNotDistinct);
        builder.append("transformPatternsDistinctFromNull", transformPatternsDistinctFromNull);
        builder.append("transformPatternsNormaliseAssociativeOps", transformPatternsNormaliseAssociativeOps);
        builder.append("transformPatternsNormaliseInListSingleElementToComparison", transformPatternsNormaliseInListSingleElementToComparison);
        builder.append("transformPatternsNormaliseFieldCompareValue", transformPatternsNormaliseFieldCompareValue);
        builder.append("transformPatternsNormaliseCoalesceToNvl", transformPatternsNormaliseCoalesceToNvl);
        builder.append("transformPatternsOrEqToIn", transformPatternsOrEqToIn);
        builder.append("transformPatternsAndNeToNotIn", transformPatternsAndNeToNotIn);
        builder.append("transformPatternsMergeOrComparison", transformPatternsMergeOrComparison);
        builder.append("transformPatternsMergeAndComparison", transformPatternsMergeAndComparison);
        builder.append("transformPatternsMergeInLists", transformPatternsMergeInLists);
        builder.append("transformPatternsMergeRangePredicates", transformPatternsMergeRangePredicates);
        builder.append("transformPatternsMergeBetweenSymmetricPredicates", transformPatternsMergeBetweenSymmetricPredicates);
        builder.append("transformPatternsCaseSearchedToCaseSimple", transformPatternsCaseSearchedToCaseSimple);
        builder.append("transformPatternsCaseElseNull", transformPatternsCaseElseNull);
        builder.append("transformPatternsUnreachableCaseClauses", transformPatternsUnreachableCaseClauses);
        builder.append("transformPatternsUnreachableDecodeClauses", transformPatternsUnreachableDecodeClauses);
        builder.append("transformPatternsCaseDistinctToDecode", transformPatternsCaseDistinctToDecode);
        builder.append("transformPatternsCaseMergeWhenWhen", transformPatternsCaseMergeWhenWhen);
        builder.append("transformPatternsCaseMergeWhenElse", transformPatternsCaseMergeWhenElse);
        builder.append("transformPatternsCaseToCaseAbbreviation", transformPatternsCaseToCaseAbbreviation);
        builder.append("transformPatternsSimplifyCaseAbbreviation", transformPatternsSimplifyCaseAbbreviation);
        builder.append("transformPatternsFlattenCaseAbbreviation", transformPatternsFlattenCaseAbbreviation);
        builder.append("transformPatternsFlattenDecode", transformPatternsFlattenDecode);
        builder.append("transformPatternsFlattenCase", transformPatternsFlattenCase);
        builder.append("transformPatternsTrivialCaseAbbreviation", transformPatternsTrivialCaseAbbreviation);
        builder.append("transformPatternsTrivialPredicates", transformPatternsTrivialPredicates);
        builder.append("transformPatternsTrivialBitwiseOperations", transformPatternsTrivialBitwiseOperations);
        builder.append("transformPatternsBitSet", transformPatternsBitSet);
        builder.append("transformPatternsBitGet", transformPatternsBitGet);
        builder.append("transformPatternsScalarSubqueryCountAsteriskGtZero", transformPatternsScalarSubqueryCountAsteriskGtZero);
        builder.append("transformPatternsScalarSubqueryCountExpressionGtZero", transformPatternsScalarSubqueryCountExpressionGtZero);
        builder.append("transformPatternsEmptyScalarSubquery", transformPatternsEmptyScalarSubquery);
        builder.append("transformPatternsNegNeg", transformPatternsNegNeg);
        builder.append("transformPatternsBitNotBitNot", transformPatternsBitNotBitNot);
        builder.append("transformPatternsBitNotBitNand", transformPatternsBitNotBitNand);
        builder.append("transformPatternsBitNotBitNor", transformPatternsBitNotBitNor);
        builder.append("transformPatternsBitNotBitXNor", transformPatternsBitNotBitXNor);
        builder.append("transformPatternsNullOnNullInput", transformPatternsNullOnNullInput);
        builder.append("transformPatternsIdempotentFunctionRepetition", transformPatternsIdempotentFunctionRepetition);
        builder.append("transformPatternsArithmeticComparisons", transformPatternsArithmeticComparisons);
        builder.append("transformPatternsArithmeticExpressions", transformPatternsArithmeticExpressions);
        builder.append("transformPatternsTrigonometricFunctions", transformPatternsTrigonometricFunctions);
        builder.append("transformPatternsLogarithmicFunctions", transformPatternsLogarithmicFunctions);
        builder.append("transformPatternsHyperbolicFunctions", transformPatternsHyperbolicFunctions);
        builder.append("transformPatternsInverseHyperbolicFunctions", transformPatternsInverseHyperbolicFunctions);
        builder.append("transformInlineBindValuesForFieldComparisons", transformInlineBindValuesForFieldComparisons);
        builder.append("transformAnsiJoinToTableLists", transformAnsiJoinToTableLists);
        builder.append("transformInConditionSubqueryWithLimitToDerivedTable", transformInConditionSubqueryWithLimitToDerivedTable);
        builder.append("transformQualify", transformQualify);
        builder.append("transformTableListsToAnsiJoin", transformTableListsToAnsiJoin);
        builder.append("transformRownum", transformRownum);
        builder.append("transformUnneededArithmeticExpressions", transformUnneededArithmeticExpressions);
        builder.append("transformGroupByColumnIndex", transformGroupByColumnIndex);
        builder.append("transformInlineCTE", transformInlineCTE);
        builder.append("backslashEscaping", backslashEscaping);
        builder.append("paramType", paramType);
        builder.append("paramCastMode", paramCastMode);
        builder.append("statementType", statementType);
        builder.append("inlineThreshold", inlineThreshold);
        builder.append("transactionListenerStartInvocationOrder", transactionListenerStartInvocationOrder);
        builder.append("transactionListenerEndInvocationOrder", transactionListenerEndInvocationOrder);
        builder.append("migrationListenerStartInvocationOrder", migrationListenerStartInvocationOrder);
        builder.append("migrationListenerEndInvocationOrder", migrationListenerEndInvocationOrder);
        builder.append("visitListenerStartInvocationOrder", visitListenerStartInvocationOrder);
        builder.append("visitListenerEndInvocationOrder", visitListenerEndInvocationOrder);
        builder.append("recordListenerStartInvocationOrder", recordListenerStartInvocationOrder);
        builder.append("recordListenerEndInvocationOrder", recordListenerEndInvocationOrder);
        builder.append("executeListenerStartInvocationOrder", executeListenerStartInvocationOrder);
        builder.append("executeListenerEndInvocationOrder", executeListenerEndInvocationOrder);
        builder.append("executeLogging", executeLogging);
        builder.append("executeLoggingSQLExceptions", executeLoggingSQLExceptions);
        builder.append("diagnosticsLogging", diagnosticsLogging);
        builder.append("diagnosticsConnection", diagnosticsConnection);
        builder.append("updateRecordVersion", updateRecordVersion);
        builder.append("updateRecordTimestamp", updateRecordTimestamp);
        builder.append("executeWithOptimisticLocking", executeWithOptimisticLocking);
        builder.append("executeWithOptimisticLockingExcludeUnversioned", executeWithOptimisticLockingExcludeUnversioned);
        builder.append("attachRecords", attachRecords);
        builder.append("insertUnchangedRecords", insertUnchangedRecords);
        builder.append("updateUnchangedRecords", updateUnchangedRecords);
        builder.append("updatablePrimaryKeys", updatablePrimaryKeys);
        builder.append("reflectionCaching", reflectionCaching);
        builder.append("cacheRecordMappers", cacheRecordMappers);
        builder.append("cacheParsingConnection", cacheParsingConnection);
        builder.append("cacheParsingConnectionLRUCacheSize", cacheParsingConnectionLRUCacheSize);
        builder.append("cachePreparedStatementInLoader", cachePreparedStatementInLoader);
        builder.append("throwExceptions", throwExceptions);
        builder.append("fetchWarnings", fetchWarnings);
        builder.append("fetchServerOutputSize", fetchServerOutputSize);
        builder.append("returnIdentityOnUpdatableRecord", returnIdentityOnUpdatableRecord);
        builder.append("returnDefaultOnUpdatableRecord", returnDefaultOnUpdatableRecord);
        builder.append("returnComputedOnUpdatableRecord", returnComputedOnUpdatableRecord);
        builder.append("returnAllOnUpdatableRecord", returnAllOnUpdatableRecord);
        builder.append("returnRecordToPojo", returnRecordToPojo);
        builder.append("mapJPAAnnotations", mapJPAAnnotations);
        builder.append("mapRecordComponentParameterNames", mapRecordComponentParameterNames);
        builder.append("mapConstructorPropertiesParameterNames", mapConstructorPropertiesParameterNames);
        builder.append("mapConstructorParameterNames", mapConstructorParameterNames);
        builder.append("mapConstructorParameterNamesInKotlin", mapConstructorParameterNamesInKotlin);
        builder.append("queryPoolable", queryPoolable);
        builder.append("queryTimeout", queryTimeout);
        builder.append("maxRows", maxRows);
        builder.append("fetchSize", fetchSize);
        builder.append("batchSize", batchSize);
        builder.append("debugInfoOnStackTrace", debugInfoOnStackTrace);
        builder.append("inListPadding", inListPadding);
        builder.append("inListPadBase", inListPadBase);
        builder.append("delimiter", delimiter);
        builder.append("emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly", emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly);
        builder.append("emulateMultiset", emulateMultiset);
        builder.append("emulateComputedColumns", emulateComputedColumns);
        builder.append("executeUpdateWithoutWhere", executeUpdateWithoutWhere);
        builder.append("executeDeleteWithoutWhere", executeDeleteWithoutWhere);
        builder.append("interpreterDialect", interpreterDialect);
        builder.append("interpreterNameLookupCaseSensitivity", interpreterNameLookupCaseSensitivity);
        builder.append("interpreterLocale", interpreterLocale);
        builder.append("interpreterDelayForeignKeyDeclarations", interpreterDelayForeignKeyDeclarations);
        builder.append("metaIncludeSystemIndexes", metaIncludeSystemIndexes);
        builder.append("metaIncludeSystemSequences", metaIncludeSystemSequences);
        builder.append("migrationAllowsUndo", migrationAllowsUndo);
        builder.append("migrationRevertUntracked", migrationRevertUntracked);
        builder.append("migrationAutoBaseline", migrationAutoBaseline);
        builder.append("migrationAutoValidation", migrationAutoValidation);
        builder.append("migrationIgnoreDefaultTimestampPrecisionDiffs", migrationIgnoreDefaultTimestampPrecisionDiffs);
        builder.append("locale", locale);
        builder.append("parseDialect", parseDialect);
        builder.append("parseLocale", parseLocale);
        builder.append("parseDateFormat", parseDateFormat);
        builder.append("parseTimestampFormat", parseTimestampFormat);
        builder.append("parseNamedParamPrefix", parseNamedParamPrefix);
        builder.append("parseNameCase", parseNameCase);
        builder.append("parseWithMetaLookups", parseWithMetaLookups);
        builder.append("parseAppendMissingTableReferences", parseAppendMissingTableReferences);
        builder.append("parseSetCommands", parseSetCommands);
        builder.append("parseUnsupportedSyntax", parseUnsupportedSyntax);
        builder.append("parseUnknownFunctions", parseUnknownFunctions);
        builder.append("parseIgnoreCommercialOnlyFeatures", parseIgnoreCommercialOnlyFeatures);
        builder.append("parseIgnoreComments", parseIgnoreComments);
        builder.append("parseIgnoreCommentStart", parseIgnoreCommentStart);
        builder.append("parseIgnoreCommentStop", parseIgnoreCommentStop);
        builder.append("parseRetainCommentsBetweenQueries", parseRetainCommentsBetweenQueries);
        builder.append("parseMetaDefaultExpressions", parseMetaDefaultExpressions);
        builder.append("readonlyTableRecordInsert", readonlyTableRecordInsert);
        builder.append("readonlyUpdatableRecordUpdate", readonlyUpdatableRecordUpdate);
        builder.append("readonlyInsert", readonlyInsert);
        builder.append("readonlyUpdate", readonlyUpdate);
        builder.append("applyWorkaroundFor7962", applyWorkaroundFor7962);
        builder.append("interpreterSearchPath", "schema", interpreterSearchPath);
        builder.append("migrationSchemata", "schema", migrationSchemata);
        builder.append("parseSearchPath", "schema", parseSearchPath);
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
        Settings other = ((Settings) that);
        if (forceIntegerTypesOnZeroScaleDecimals == null) {
            if (other.forceIntegerTypesOnZeroScaleDecimals!= null) {
                return false;
            }
        } else {
            if (!forceIntegerTypesOnZeroScaleDecimals.equals(other.forceIntegerTypesOnZeroScaleDecimals)) {
                return false;
            }
        }
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
        if (renderTable == null) {
            if (other.renderTable!= null) {
                return false;
            }
        } else {
            if (!renderTable.equals(other.renderTable)) {
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
        if (renderQuotedNames == null) {
            if (other.renderQuotedNames!= null) {
                return false;
            }
        } else {
            if (!renderQuotedNames.equals(other.renderQuotedNames)) {
                return false;
            }
        }
        if (renderNameCase == null) {
            if (other.renderNameCase!= null) {
                return false;
            }
        } else {
            if (!renderNameCase.equals(other.renderNameCase)) {
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
        if (renderNamedParamPrefix == null) {
            if (other.renderNamedParamPrefix!= null) {
                return false;
            }
        } else {
            if (!renderNamedParamPrefix.equals(other.renderNamedParamPrefix)) {
                return false;
            }
        }
        if (renderKeywordCase == null) {
            if (other.renderKeywordCase!= null) {
                return false;
            }
        } else {
            if (!renderKeywordCase.equals(other.renderKeywordCase)) {
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
        if (renderLocale == null) {
            if (other.renderLocale!= null) {
                return false;
            }
        } else {
            if (!renderLocale.equals(other.renderLocale)) {
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
        if (renderOptionalAssociativityParentheses == null) {
            if (other.renderOptionalAssociativityParentheses!= null) {
                return false;
            }
        } else {
            if (!renderOptionalAssociativityParentheses.equals(other.renderOptionalAssociativityParentheses)) {
                return false;
            }
        }
        if (renderOptionalAsKeywordForTableAliases == null) {
            if (other.renderOptionalAsKeywordForTableAliases!= null) {
                return false;
            }
        } else {
            if (!renderOptionalAsKeywordForTableAliases.equals(other.renderOptionalAsKeywordForTableAliases)) {
                return false;
            }
        }
        if (renderOptionalAsKeywordForFieldAliases == null) {
            if (other.renderOptionalAsKeywordForFieldAliases!= null) {
                return false;
            }
        } else {
            if (!renderOptionalAsKeywordForFieldAliases.equals(other.renderOptionalAsKeywordForFieldAliases)) {
                return false;
            }
        }
        if (renderOptionalInnerKeyword == null) {
            if (other.renderOptionalInnerKeyword!= null) {
                return false;
            }
        } else {
            if (!renderOptionalInnerKeyword.equals(other.renderOptionalInnerKeyword)) {
                return false;
            }
        }
        if (renderOptionalOuterKeyword == null) {
            if (other.renderOptionalOuterKeyword!= null) {
                return false;
            }
        } else {
            if (!renderOptionalOuterKeyword.equals(other.renderOptionalOuterKeyword)) {
                return false;
            }
        }
        if (renderImplicitWindowRange == null) {
            if (other.renderImplicitWindowRange!= null) {
                return false;
            }
        } else {
            if (!renderImplicitWindowRange.equals(other.renderImplicitWindowRange)) {
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
        if (renderImplicitJoinType == null) {
            if (other.renderImplicitJoinType!= null) {
                return false;
            }
        } else {
            if (!renderImplicitJoinType.equals(other.renderImplicitJoinType)) {
                return false;
            }
        }
        if (renderDefaultNullability == null) {
            if (other.renderDefaultNullability!= null) {
                return false;
            }
        } else {
            if (!renderDefaultNullability.equals(other.renderDefaultNullability)) {
                return false;
            }
        }
        if (renderCoalesceToEmptyStringInConcat == null) {
            if (other.renderCoalesceToEmptyStringInConcat!= null) {
                return false;
            }
        } else {
            if (!renderCoalesceToEmptyStringInConcat.equals(other.renderCoalesceToEmptyStringInConcat)) {
                return false;
            }
        }
        if (renderOrderByRownumberForEmulatedPagination == null) {
            if (other.renderOrderByRownumberForEmulatedPagination!= null) {
                return false;
            }
        } else {
            if (!renderOrderByRownumberForEmulatedPagination.equals(other.renderOrderByRownumberForEmulatedPagination)) {
                return false;
            }
        }
        if (renderOutputForSQLServerReturningClause == null) {
            if (other.renderOutputForSQLServerReturningClause!= null) {
                return false;
            }
        } else {
            if (!renderOutputForSQLServerReturningClause.equals(other.renderOutputForSQLServerReturningClause)) {
                return false;
            }
        }
        if (renderGroupConcatMaxLenSessionVariable == null) {
            if (other.renderGroupConcatMaxLenSessionVariable!= null) {
                return false;
            }
        } else {
            if (!renderGroupConcatMaxLenSessionVariable.equals(other.renderGroupConcatMaxLenSessionVariable)) {
                return false;
            }
        }
        if (renderParenthesisAroundSetOperationQueries == null) {
            if (other.renderParenthesisAroundSetOperationQueries!= null) {
                return false;
            }
        } else {
            if (!renderParenthesisAroundSetOperationQueries.equals(other.renderParenthesisAroundSetOperationQueries)) {
                return false;
            }
        }
        if (renderVariablesInDerivedTablesForEmulations == null) {
            if (other.renderVariablesInDerivedTablesForEmulations!= null) {
                return false;
            }
        } else {
            if (!renderVariablesInDerivedTablesForEmulations.equals(other.renderVariablesInDerivedTablesForEmulations)) {
                return false;
            }
        }
        if (renderRowConditionForSeekClause == null) {
            if (other.renderRowConditionForSeekClause!= null) {
                return false;
            }
        } else {
            if (!renderRowConditionForSeekClause.equals(other.renderRowConditionForSeekClause)) {
                return false;
            }
        }
        if (renderRedundantConditionForSeekClause == null) {
            if (other.renderRedundantConditionForSeekClause!= null) {
                return false;
            }
        } else {
            if (!renderRedundantConditionForSeekClause.equals(other.renderRedundantConditionForSeekClause)) {
                return false;
            }
        }
        if (renderPlainSQLTemplatesAsRaw == null) {
            if (other.renderPlainSQLTemplatesAsRaw!= null) {
                return false;
            }
        } else {
            if (!renderPlainSQLTemplatesAsRaw.equals(other.renderPlainSQLTemplatesAsRaw)) {
                return false;
            }
        }
        if (namePathSeparator == null) {
            if (other.namePathSeparator!= null) {
                return false;
            }
        } else {
            if (!namePathSeparator.equals(other.namePathSeparator)) {
                return false;
            }
        }
        if (bindOffsetDateTimeType == null) {
            if (other.bindOffsetDateTimeType!= null) {
                return false;
            }
        } else {
            if (!bindOffsetDateTimeType.equals(other.bindOffsetDateTimeType)) {
                return false;
            }
        }
        if (bindOffsetTimeType == null) {
            if (other.bindOffsetTimeType!= null) {
                return false;
            }
        } else {
            if (!bindOffsetTimeType.equals(other.bindOffsetTimeType)) {
                return false;
            }
        }
        if (fetchTriggerValuesAfterSQLServerOutput == null) {
            if (other.fetchTriggerValuesAfterSQLServerOutput!= null) {
                return false;
            }
        } else {
            if (!fetchTriggerValuesAfterSQLServerOutput.equals(other.fetchTriggerValuesAfterSQLServerOutput)) {
                return false;
            }
        }
        if (fetchIntermediateResult == null) {
            if (other.fetchIntermediateResult!= null) {
                return false;
            }
        } else {
            if (!fetchIntermediateResult.equals(other.fetchIntermediateResult)) {
                return false;
            }
        }
        if (diagnosticsDuplicateStatements == null) {
            if (other.diagnosticsDuplicateStatements!= null) {
                return false;
            }
        } else {
            if (!diagnosticsDuplicateStatements.equals(other.diagnosticsDuplicateStatements)) {
                return false;
            }
        }
        if (diagnosticsDuplicateStatementsUsingTransformPatterns == null) {
            if (other.diagnosticsDuplicateStatementsUsingTransformPatterns!= null) {
                return false;
            }
        } else {
            if (!diagnosticsDuplicateStatementsUsingTransformPatterns.equals(other.diagnosticsDuplicateStatementsUsingTransformPatterns)) {
                return false;
            }
        }
        if (diagnosticsMissingWasNullCall == null) {
            if (other.diagnosticsMissingWasNullCall!= null) {
                return false;
            }
        } else {
            if (!diagnosticsMissingWasNullCall.equals(other.diagnosticsMissingWasNullCall)) {
                return false;
            }
        }
        if (diagnosticsRepeatedStatements == null) {
            if (other.diagnosticsRepeatedStatements!= null) {
                return false;
            }
        } else {
            if (!diagnosticsRepeatedStatements.equals(other.diagnosticsRepeatedStatements)) {
                return false;
            }
        }
        if (diagnosticsConsecutiveAggregation == null) {
            if (other.diagnosticsConsecutiveAggregation!= null) {
                return false;
            }
        } else {
            if (!diagnosticsConsecutiveAggregation.equals(other.diagnosticsConsecutiveAggregation)) {
                return false;
            }
        }
        if (diagnosticsConcatenationInPredicate == null) {
            if (other.diagnosticsConcatenationInPredicate!= null) {
                return false;
            }
        } else {
            if (!diagnosticsConcatenationInPredicate.equals(other.diagnosticsConcatenationInPredicate)) {
                return false;
            }
        }
        if (diagnosticsPossiblyWrongExpression == null) {
            if (other.diagnosticsPossiblyWrongExpression!= null) {
                return false;
            }
        } else {
            if (!diagnosticsPossiblyWrongExpression.equals(other.diagnosticsPossiblyWrongExpression)) {
                return false;
            }
        }
        if (diagnosticsTooManyColumnsFetched == null) {
            if (other.diagnosticsTooManyColumnsFetched!= null) {
                return false;
            }
        } else {
            if (!diagnosticsTooManyColumnsFetched.equals(other.diagnosticsTooManyColumnsFetched)) {
                return false;
            }
        }
        if (diagnosticsTooManyRowsFetched == null) {
            if (other.diagnosticsTooManyRowsFetched!= null) {
                return false;
            }
        } else {
            if (!diagnosticsTooManyRowsFetched.equals(other.diagnosticsTooManyRowsFetched)) {
                return false;
            }
        }
        if (diagnosticsUnnecessaryWasNullCall == null) {
            if (other.diagnosticsUnnecessaryWasNullCall!= null) {
                return false;
            }
        } else {
            if (!diagnosticsUnnecessaryWasNullCall.equals(other.diagnosticsUnnecessaryWasNullCall)) {
                return false;
            }
        }
        if (diagnosticsPatterns == null) {
            if (other.diagnosticsPatterns!= null) {
                return false;
            }
        } else {
            if (!diagnosticsPatterns.equals(other.diagnosticsPatterns)) {
                return false;
            }
        }
        if (diagnosticsTrivialCondition == null) {
            if (other.diagnosticsTrivialCondition!= null) {
                return false;
            }
        } else {
            if (!diagnosticsTrivialCondition.equals(other.diagnosticsTrivialCondition)) {
                return false;
            }
        }
        if (diagnosticsNullCondition == null) {
            if (other.diagnosticsNullCondition!= null) {
                return false;
            }
        } else {
            if (!diagnosticsNullCondition.equals(other.diagnosticsNullCondition)) {
                return false;
            }
        }
        if (transformPatterns == null) {
            if (other.transformPatterns!= null) {
                return false;
            }
        } else {
            if (!transformPatterns.equals(other.transformPatterns)) {
                return false;
            }
        }
        if (transformPatternsLogging == null) {
            if (other.transformPatternsLogging!= null) {
                return false;
            }
        } else {
            if (!transformPatternsLogging.equals(other.transformPatternsLogging)) {
                return false;
            }
        }
        if (transformPatternsUnnecessaryDistinct == null) {
            if (other.transformPatternsUnnecessaryDistinct!= null) {
                return false;
            }
        } else {
            if (!transformPatternsUnnecessaryDistinct.equals(other.transformPatternsUnnecessaryDistinct)) {
                return false;
            }
        }
        if (transformPatternsUnnecessaryScalarSubquery == null) {
            if (other.transformPatternsUnnecessaryScalarSubquery!= null) {
                return false;
            }
        } else {
            if (!transformPatternsUnnecessaryScalarSubquery.equals(other.transformPatternsUnnecessaryScalarSubquery)) {
                return false;
            }
        }
        if (transformPatternsUnnecessaryInnerJoin == null) {
            if (other.transformPatternsUnnecessaryInnerJoin!= null) {
                return false;
            }
        } else {
            if (!transformPatternsUnnecessaryInnerJoin.equals(other.transformPatternsUnnecessaryInnerJoin)) {
                return false;
            }
        }
        if (transformPatternsUnnecessaryGroupByExpressions == null) {
            if (other.transformPatternsUnnecessaryGroupByExpressions!= null) {
                return false;
            }
        } else {
            if (!transformPatternsUnnecessaryGroupByExpressions.equals(other.transformPatternsUnnecessaryGroupByExpressions)) {
                return false;
            }
        }
        if (transformPatternsUnnecessaryOrderByExpressions == null) {
            if (other.transformPatternsUnnecessaryOrderByExpressions!= null) {
                return false;
            }
        } else {
            if (!transformPatternsUnnecessaryOrderByExpressions.equals(other.transformPatternsUnnecessaryOrderByExpressions)) {
                return false;
            }
        }
        if (transformPatternsUnnecessaryExistsSubqueryClauses == null) {
            if (other.transformPatternsUnnecessaryExistsSubqueryClauses!= null) {
                return false;
            }
        } else {
            if (!transformPatternsUnnecessaryExistsSubqueryClauses.equals(other.transformPatternsUnnecessaryExistsSubqueryClauses)) {
                return false;
            }
        }
        if (transformPatternsCountConstant == null) {
            if (other.transformPatternsCountConstant!= null) {
                return false;
            }
        } else {
            if (!transformPatternsCountConstant.equals(other.transformPatternsCountConstant)) {
                return false;
            }
        }
        if (transformPatternsTrim == null) {
            if (other.transformPatternsTrim!= null) {
                return false;
            }
        } else {
            if (!transformPatternsTrim.equals(other.transformPatternsTrim)) {
                return false;
            }
        }
        if (transformPatternsNotAnd == null) {
            if (other.transformPatternsNotAnd!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNotAnd.equals(other.transformPatternsNotAnd)) {
                return false;
            }
        }
        if (transformPatternsNotOr == null) {
            if (other.transformPatternsNotOr!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNotOr.equals(other.transformPatternsNotOr)) {
                return false;
            }
        }
        if (transformPatternsNotNot == null) {
            if (other.transformPatternsNotNot!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNotNot.equals(other.transformPatternsNotNot)) {
                return false;
            }
        }
        if (transformPatternsNotComparison == null) {
            if (other.transformPatternsNotComparison!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNotComparison.equals(other.transformPatternsNotComparison)) {
                return false;
            }
        }
        if (transformPatternsNotNotDistinct == null) {
            if (other.transformPatternsNotNotDistinct!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNotNotDistinct.equals(other.transformPatternsNotNotDistinct)) {
                return false;
            }
        }
        if (transformPatternsDistinctFromNull == null) {
            if (other.transformPatternsDistinctFromNull!= null) {
                return false;
            }
        } else {
            if (!transformPatternsDistinctFromNull.equals(other.transformPatternsDistinctFromNull)) {
                return false;
            }
        }
        if (transformPatternsNormaliseAssociativeOps == null) {
            if (other.transformPatternsNormaliseAssociativeOps!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNormaliseAssociativeOps.equals(other.transformPatternsNormaliseAssociativeOps)) {
                return false;
            }
        }
        if (transformPatternsNormaliseInListSingleElementToComparison == null) {
            if (other.transformPatternsNormaliseInListSingleElementToComparison!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNormaliseInListSingleElementToComparison.equals(other.transformPatternsNormaliseInListSingleElementToComparison)) {
                return false;
            }
        }
        if (transformPatternsNormaliseFieldCompareValue == null) {
            if (other.transformPatternsNormaliseFieldCompareValue!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNormaliseFieldCompareValue.equals(other.transformPatternsNormaliseFieldCompareValue)) {
                return false;
            }
        }
        if (transformPatternsNormaliseCoalesceToNvl == null) {
            if (other.transformPatternsNormaliseCoalesceToNvl!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNormaliseCoalesceToNvl.equals(other.transformPatternsNormaliseCoalesceToNvl)) {
                return false;
            }
        }
        if (transformPatternsOrEqToIn == null) {
            if (other.transformPatternsOrEqToIn!= null) {
                return false;
            }
        } else {
            if (!transformPatternsOrEqToIn.equals(other.transformPatternsOrEqToIn)) {
                return false;
            }
        }
        if (transformPatternsAndNeToNotIn == null) {
            if (other.transformPatternsAndNeToNotIn!= null) {
                return false;
            }
        } else {
            if (!transformPatternsAndNeToNotIn.equals(other.transformPatternsAndNeToNotIn)) {
                return false;
            }
        }
        if (transformPatternsMergeOrComparison == null) {
            if (other.transformPatternsMergeOrComparison!= null) {
                return false;
            }
        } else {
            if (!transformPatternsMergeOrComparison.equals(other.transformPatternsMergeOrComparison)) {
                return false;
            }
        }
        if (transformPatternsMergeAndComparison == null) {
            if (other.transformPatternsMergeAndComparison!= null) {
                return false;
            }
        } else {
            if (!transformPatternsMergeAndComparison.equals(other.transformPatternsMergeAndComparison)) {
                return false;
            }
        }
        if (transformPatternsMergeInLists == null) {
            if (other.transformPatternsMergeInLists!= null) {
                return false;
            }
        } else {
            if (!transformPatternsMergeInLists.equals(other.transformPatternsMergeInLists)) {
                return false;
            }
        }
        if (transformPatternsMergeRangePredicates == null) {
            if (other.transformPatternsMergeRangePredicates!= null) {
                return false;
            }
        } else {
            if (!transformPatternsMergeRangePredicates.equals(other.transformPatternsMergeRangePredicates)) {
                return false;
            }
        }
        if (transformPatternsMergeBetweenSymmetricPredicates == null) {
            if (other.transformPatternsMergeBetweenSymmetricPredicates!= null) {
                return false;
            }
        } else {
            if (!transformPatternsMergeBetweenSymmetricPredicates.equals(other.transformPatternsMergeBetweenSymmetricPredicates)) {
                return false;
            }
        }
        if (transformPatternsCaseSearchedToCaseSimple == null) {
            if (other.transformPatternsCaseSearchedToCaseSimple!= null) {
                return false;
            }
        } else {
            if (!transformPatternsCaseSearchedToCaseSimple.equals(other.transformPatternsCaseSearchedToCaseSimple)) {
                return false;
            }
        }
        if (transformPatternsCaseElseNull == null) {
            if (other.transformPatternsCaseElseNull!= null) {
                return false;
            }
        } else {
            if (!transformPatternsCaseElseNull.equals(other.transformPatternsCaseElseNull)) {
                return false;
            }
        }
        if (transformPatternsUnreachableCaseClauses == null) {
            if (other.transformPatternsUnreachableCaseClauses!= null) {
                return false;
            }
        } else {
            if (!transformPatternsUnreachableCaseClauses.equals(other.transformPatternsUnreachableCaseClauses)) {
                return false;
            }
        }
        if (transformPatternsUnreachableDecodeClauses == null) {
            if (other.transformPatternsUnreachableDecodeClauses!= null) {
                return false;
            }
        } else {
            if (!transformPatternsUnreachableDecodeClauses.equals(other.transformPatternsUnreachableDecodeClauses)) {
                return false;
            }
        }
        if (transformPatternsCaseDistinctToDecode == null) {
            if (other.transformPatternsCaseDistinctToDecode!= null) {
                return false;
            }
        } else {
            if (!transformPatternsCaseDistinctToDecode.equals(other.transformPatternsCaseDistinctToDecode)) {
                return false;
            }
        }
        if (transformPatternsCaseMergeWhenWhen == null) {
            if (other.transformPatternsCaseMergeWhenWhen!= null) {
                return false;
            }
        } else {
            if (!transformPatternsCaseMergeWhenWhen.equals(other.transformPatternsCaseMergeWhenWhen)) {
                return false;
            }
        }
        if (transformPatternsCaseMergeWhenElse == null) {
            if (other.transformPatternsCaseMergeWhenElse!= null) {
                return false;
            }
        } else {
            if (!transformPatternsCaseMergeWhenElse.equals(other.transformPatternsCaseMergeWhenElse)) {
                return false;
            }
        }
        if (transformPatternsCaseToCaseAbbreviation == null) {
            if (other.transformPatternsCaseToCaseAbbreviation!= null) {
                return false;
            }
        } else {
            if (!transformPatternsCaseToCaseAbbreviation.equals(other.transformPatternsCaseToCaseAbbreviation)) {
                return false;
            }
        }
        if (transformPatternsSimplifyCaseAbbreviation == null) {
            if (other.transformPatternsSimplifyCaseAbbreviation!= null) {
                return false;
            }
        } else {
            if (!transformPatternsSimplifyCaseAbbreviation.equals(other.transformPatternsSimplifyCaseAbbreviation)) {
                return false;
            }
        }
        if (transformPatternsFlattenCaseAbbreviation == null) {
            if (other.transformPatternsFlattenCaseAbbreviation!= null) {
                return false;
            }
        } else {
            if (!transformPatternsFlattenCaseAbbreviation.equals(other.transformPatternsFlattenCaseAbbreviation)) {
                return false;
            }
        }
        if (transformPatternsFlattenDecode == null) {
            if (other.transformPatternsFlattenDecode!= null) {
                return false;
            }
        } else {
            if (!transformPatternsFlattenDecode.equals(other.transformPatternsFlattenDecode)) {
                return false;
            }
        }
        if (transformPatternsFlattenCase == null) {
            if (other.transformPatternsFlattenCase!= null) {
                return false;
            }
        } else {
            if (!transformPatternsFlattenCase.equals(other.transformPatternsFlattenCase)) {
                return false;
            }
        }
        if (transformPatternsTrivialCaseAbbreviation == null) {
            if (other.transformPatternsTrivialCaseAbbreviation!= null) {
                return false;
            }
        } else {
            if (!transformPatternsTrivialCaseAbbreviation.equals(other.transformPatternsTrivialCaseAbbreviation)) {
                return false;
            }
        }
        if (transformPatternsTrivialPredicates == null) {
            if (other.transformPatternsTrivialPredicates!= null) {
                return false;
            }
        } else {
            if (!transformPatternsTrivialPredicates.equals(other.transformPatternsTrivialPredicates)) {
                return false;
            }
        }
        if (transformPatternsTrivialBitwiseOperations == null) {
            if (other.transformPatternsTrivialBitwiseOperations!= null) {
                return false;
            }
        } else {
            if (!transformPatternsTrivialBitwiseOperations.equals(other.transformPatternsTrivialBitwiseOperations)) {
                return false;
            }
        }
        if (transformPatternsBitSet == null) {
            if (other.transformPatternsBitSet!= null) {
                return false;
            }
        } else {
            if (!transformPatternsBitSet.equals(other.transformPatternsBitSet)) {
                return false;
            }
        }
        if (transformPatternsBitGet == null) {
            if (other.transformPatternsBitGet!= null) {
                return false;
            }
        } else {
            if (!transformPatternsBitGet.equals(other.transformPatternsBitGet)) {
                return false;
            }
        }
        if (transformPatternsScalarSubqueryCountAsteriskGtZero == null) {
            if (other.transformPatternsScalarSubqueryCountAsteriskGtZero!= null) {
                return false;
            }
        } else {
            if (!transformPatternsScalarSubqueryCountAsteriskGtZero.equals(other.transformPatternsScalarSubqueryCountAsteriskGtZero)) {
                return false;
            }
        }
        if (transformPatternsScalarSubqueryCountExpressionGtZero == null) {
            if (other.transformPatternsScalarSubqueryCountExpressionGtZero!= null) {
                return false;
            }
        } else {
            if (!transformPatternsScalarSubqueryCountExpressionGtZero.equals(other.transformPatternsScalarSubqueryCountExpressionGtZero)) {
                return false;
            }
        }
        if (transformPatternsEmptyScalarSubquery == null) {
            if (other.transformPatternsEmptyScalarSubquery!= null) {
                return false;
            }
        } else {
            if (!transformPatternsEmptyScalarSubquery.equals(other.transformPatternsEmptyScalarSubquery)) {
                return false;
            }
        }
        if (transformPatternsNegNeg == null) {
            if (other.transformPatternsNegNeg!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNegNeg.equals(other.transformPatternsNegNeg)) {
                return false;
            }
        }
        if (transformPatternsBitNotBitNot == null) {
            if (other.transformPatternsBitNotBitNot!= null) {
                return false;
            }
        } else {
            if (!transformPatternsBitNotBitNot.equals(other.transformPatternsBitNotBitNot)) {
                return false;
            }
        }
        if (transformPatternsBitNotBitNand == null) {
            if (other.transformPatternsBitNotBitNand!= null) {
                return false;
            }
        } else {
            if (!transformPatternsBitNotBitNand.equals(other.transformPatternsBitNotBitNand)) {
                return false;
            }
        }
        if (transformPatternsBitNotBitNor == null) {
            if (other.transformPatternsBitNotBitNor!= null) {
                return false;
            }
        } else {
            if (!transformPatternsBitNotBitNor.equals(other.transformPatternsBitNotBitNor)) {
                return false;
            }
        }
        if (transformPatternsBitNotBitXNor == null) {
            if (other.transformPatternsBitNotBitXNor!= null) {
                return false;
            }
        } else {
            if (!transformPatternsBitNotBitXNor.equals(other.transformPatternsBitNotBitXNor)) {
                return false;
            }
        }
        if (transformPatternsNullOnNullInput == null) {
            if (other.transformPatternsNullOnNullInput!= null) {
                return false;
            }
        } else {
            if (!transformPatternsNullOnNullInput.equals(other.transformPatternsNullOnNullInput)) {
                return false;
            }
        }
        if (transformPatternsIdempotentFunctionRepetition == null) {
            if (other.transformPatternsIdempotentFunctionRepetition!= null) {
                return false;
            }
        } else {
            if (!transformPatternsIdempotentFunctionRepetition.equals(other.transformPatternsIdempotentFunctionRepetition)) {
                return false;
            }
        }
        if (transformPatternsArithmeticComparisons == null) {
            if (other.transformPatternsArithmeticComparisons!= null) {
                return false;
            }
        } else {
            if (!transformPatternsArithmeticComparisons.equals(other.transformPatternsArithmeticComparisons)) {
                return false;
            }
        }
        if (transformPatternsArithmeticExpressions == null) {
            if (other.transformPatternsArithmeticExpressions!= null) {
                return false;
            }
        } else {
            if (!transformPatternsArithmeticExpressions.equals(other.transformPatternsArithmeticExpressions)) {
                return false;
            }
        }
        if (transformPatternsTrigonometricFunctions == null) {
            if (other.transformPatternsTrigonometricFunctions!= null) {
                return false;
            }
        } else {
            if (!transformPatternsTrigonometricFunctions.equals(other.transformPatternsTrigonometricFunctions)) {
                return false;
            }
        }
        if (transformPatternsLogarithmicFunctions == null) {
            if (other.transformPatternsLogarithmicFunctions!= null) {
                return false;
            }
        } else {
            if (!transformPatternsLogarithmicFunctions.equals(other.transformPatternsLogarithmicFunctions)) {
                return false;
            }
        }
        if (transformPatternsHyperbolicFunctions == null) {
            if (other.transformPatternsHyperbolicFunctions!= null) {
                return false;
            }
        } else {
            if (!transformPatternsHyperbolicFunctions.equals(other.transformPatternsHyperbolicFunctions)) {
                return false;
            }
        }
        if (transformPatternsInverseHyperbolicFunctions == null) {
            if (other.transformPatternsInverseHyperbolicFunctions!= null) {
                return false;
            }
        } else {
            if (!transformPatternsInverseHyperbolicFunctions.equals(other.transformPatternsInverseHyperbolicFunctions)) {
                return false;
            }
        }
        if (transformInlineBindValuesForFieldComparisons == null) {
            if (other.transformInlineBindValuesForFieldComparisons!= null) {
                return false;
            }
        } else {
            if (!transformInlineBindValuesForFieldComparisons.equals(other.transformInlineBindValuesForFieldComparisons)) {
                return false;
            }
        }
        if (transformAnsiJoinToTableLists == null) {
            if (other.transformAnsiJoinToTableLists!= null) {
                return false;
            }
        } else {
            if (!transformAnsiJoinToTableLists.equals(other.transformAnsiJoinToTableLists)) {
                return false;
            }
        }
        if (transformInConditionSubqueryWithLimitToDerivedTable == null) {
            if (other.transformInConditionSubqueryWithLimitToDerivedTable!= null) {
                return false;
            }
        } else {
            if (!transformInConditionSubqueryWithLimitToDerivedTable.equals(other.transformInConditionSubqueryWithLimitToDerivedTable)) {
                return false;
            }
        }
        if (transformQualify == null) {
            if (other.transformQualify!= null) {
                return false;
            }
        } else {
            if (!transformQualify.equals(other.transformQualify)) {
                return false;
            }
        }
        if (transformTableListsToAnsiJoin == null) {
            if (other.transformTableListsToAnsiJoin!= null) {
                return false;
            }
        } else {
            if (!transformTableListsToAnsiJoin.equals(other.transformTableListsToAnsiJoin)) {
                return false;
            }
        }
        if (transformRownum == null) {
            if (other.transformRownum!= null) {
                return false;
            }
        } else {
            if (!transformRownum.equals(other.transformRownum)) {
                return false;
            }
        }
        if (transformUnneededArithmeticExpressions == null) {
            if (other.transformUnneededArithmeticExpressions!= null) {
                return false;
            }
        } else {
            if (!transformUnneededArithmeticExpressions.equals(other.transformUnneededArithmeticExpressions)) {
                return false;
            }
        }
        if (transformGroupByColumnIndex == null) {
            if (other.transformGroupByColumnIndex!= null) {
                return false;
            }
        } else {
            if (!transformGroupByColumnIndex.equals(other.transformGroupByColumnIndex)) {
                return false;
            }
        }
        if (transformInlineCTE == null) {
            if (other.transformInlineCTE!= null) {
                return false;
            }
        } else {
            if (!transformInlineCTE.equals(other.transformInlineCTE)) {
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
        if (inlineThreshold == null) {
            if (other.inlineThreshold!= null) {
                return false;
            }
        } else {
            if (!inlineThreshold.equals(other.inlineThreshold)) {
                return false;
            }
        }
        if (transactionListenerStartInvocationOrder == null) {
            if (other.transactionListenerStartInvocationOrder!= null) {
                return false;
            }
        } else {
            if (!transactionListenerStartInvocationOrder.equals(other.transactionListenerStartInvocationOrder)) {
                return false;
            }
        }
        if (transactionListenerEndInvocationOrder == null) {
            if (other.transactionListenerEndInvocationOrder!= null) {
                return false;
            }
        } else {
            if (!transactionListenerEndInvocationOrder.equals(other.transactionListenerEndInvocationOrder)) {
                return false;
            }
        }
        if (migrationListenerStartInvocationOrder == null) {
            if (other.migrationListenerStartInvocationOrder!= null) {
                return false;
            }
        } else {
            if (!migrationListenerStartInvocationOrder.equals(other.migrationListenerStartInvocationOrder)) {
                return false;
            }
        }
        if (migrationListenerEndInvocationOrder == null) {
            if (other.migrationListenerEndInvocationOrder!= null) {
                return false;
            }
        } else {
            if (!migrationListenerEndInvocationOrder.equals(other.migrationListenerEndInvocationOrder)) {
                return false;
            }
        }
        if (visitListenerStartInvocationOrder == null) {
            if (other.visitListenerStartInvocationOrder!= null) {
                return false;
            }
        } else {
            if (!visitListenerStartInvocationOrder.equals(other.visitListenerStartInvocationOrder)) {
                return false;
            }
        }
        if (visitListenerEndInvocationOrder == null) {
            if (other.visitListenerEndInvocationOrder!= null) {
                return false;
            }
        } else {
            if (!visitListenerEndInvocationOrder.equals(other.visitListenerEndInvocationOrder)) {
                return false;
            }
        }
        if (recordListenerStartInvocationOrder == null) {
            if (other.recordListenerStartInvocationOrder!= null) {
                return false;
            }
        } else {
            if (!recordListenerStartInvocationOrder.equals(other.recordListenerStartInvocationOrder)) {
                return false;
            }
        }
        if (recordListenerEndInvocationOrder == null) {
            if (other.recordListenerEndInvocationOrder!= null) {
                return false;
            }
        } else {
            if (!recordListenerEndInvocationOrder.equals(other.recordListenerEndInvocationOrder)) {
                return false;
            }
        }
        if (executeListenerStartInvocationOrder == null) {
            if (other.executeListenerStartInvocationOrder!= null) {
                return false;
            }
        } else {
            if (!executeListenerStartInvocationOrder.equals(other.executeListenerStartInvocationOrder)) {
                return false;
            }
        }
        if (executeListenerEndInvocationOrder == null) {
            if (other.executeListenerEndInvocationOrder!= null) {
                return false;
            }
        } else {
            if (!executeListenerEndInvocationOrder.equals(other.executeListenerEndInvocationOrder)) {
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
        if (executeLoggingSQLExceptions == null) {
            if (other.executeLoggingSQLExceptions!= null) {
                return false;
            }
        } else {
            if (!executeLoggingSQLExceptions.equals(other.executeLoggingSQLExceptions)) {
                return false;
            }
        }
        if (diagnosticsLogging == null) {
            if (other.diagnosticsLogging!= null) {
                return false;
            }
        } else {
            if (!diagnosticsLogging.equals(other.diagnosticsLogging)) {
                return false;
            }
        }
        if (diagnosticsConnection == null) {
            if (other.diagnosticsConnection!= null) {
                return false;
            }
        } else {
            if (!diagnosticsConnection.equals(other.diagnosticsConnection)) {
                return false;
            }
        }
        if (updateRecordVersion == null) {
            if (other.updateRecordVersion!= null) {
                return false;
            }
        } else {
            if (!updateRecordVersion.equals(other.updateRecordVersion)) {
                return false;
            }
        }
        if (updateRecordTimestamp == null) {
            if (other.updateRecordTimestamp!= null) {
                return false;
            }
        } else {
            if (!updateRecordTimestamp.equals(other.updateRecordTimestamp)) {
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
        if (insertUnchangedRecords == null) {
            if (other.insertUnchangedRecords!= null) {
                return false;
            }
        } else {
            if (!insertUnchangedRecords.equals(other.insertUnchangedRecords)) {
                return false;
            }
        }
        if (updateUnchangedRecords == null) {
            if (other.updateUnchangedRecords!= null) {
                return false;
            }
        } else {
            if (!updateUnchangedRecords.equals(other.updateUnchangedRecords)) {
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
        if (cacheParsingConnection == null) {
            if (other.cacheParsingConnection!= null) {
                return false;
            }
        } else {
            if (!cacheParsingConnection.equals(other.cacheParsingConnection)) {
                return false;
            }
        }
        if (cacheParsingConnectionLRUCacheSize == null) {
            if (other.cacheParsingConnectionLRUCacheSize!= null) {
                return false;
            }
        } else {
            if (!cacheParsingConnectionLRUCacheSize.equals(other.cacheParsingConnectionLRUCacheSize)) {
                return false;
            }
        }
        if (cachePreparedStatementInLoader == null) {
            if (other.cachePreparedStatementInLoader!= null) {
                return false;
            }
        } else {
            if (!cachePreparedStatementInLoader.equals(other.cachePreparedStatementInLoader)) {
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
        if (returnIdentityOnUpdatableRecord == null) {
            if (other.returnIdentityOnUpdatableRecord!= null) {
                return false;
            }
        } else {
            if (!returnIdentityOnUpdatableRecord.equals(other.returnIdentityOnUpdatableRecord)) {
                return false;
            }
        }
        if (returnDefaultOnUpdatableRecord == null) {
            if (other.returnDefaultOnUpdatableRecord!= null) {
                return false;
            }
        } else {
            if (!returnDefaultOnUpdatableRecord.equals(other.returnDefaultOnUpdatableRecord)) {
                return false;
            }
        }
        if (returnComputedOnUpdatableRecord == null) {
            if (other.returnComputedOnUpdatableRecord!= null) {
                return false;
            }
        } else {
            if (!returnComputedOnUpdatableRecord.equals(other.returnComputedOnUpdatableRecord)) {
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
        if (mapRecordComponentParameterNames == null) {
            if (other.mapRecordComponentParameterNames!= null) {
                return false;
            }
        } else {
            if (!mapRecordComponentParameterNames.equals(other.mapRecordComponentParameterNames)) {
                return false;
            }
        }
        if (mapConstructorPropertiesParameterNames == null) {
            if (other.mapConstructorPropertiesParameterNames!= null) {
                return false;
            }
        } else {
            if (!mapConstructorPropertiesParameterNames.equals(other.mapConstructorPropertiesParameterNames)) {
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
        if (mapConstructorParameterNamesInKotlin == null) {
            if (other.mapConstructorParameterNamesInKotlin!= null) {
                return false;
            }
        } else {
            if (!mapConstructorParameterNamesInKotlin.equals(other.mapConstructorParameterNamesInKotlin)) {
                return false;
            }
        }
        if (queryPoolable == null) {
            if (other.queryPoolable!= null) {
                return false;
            }
        } else {
            if (!queryPoolable.equals(other.queryPoolable)) {
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
        if (batchSize == null) {
            if (other.batchSize!= null) {
                return false;
            }
        } else {
            if (!batchSize.equals(other.batchSize)) {
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
        if (emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly == null) {
            if (other.emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly!= null) {
                return false;
            }
        } else {
            if (!emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly.equals(other.emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly)) {
                return false;
            }
        }
        if (emulateMultiset == null) {
            if (other.emulateMultiset!= null) {
                return false;
            }
        } else {
            if (!emulateMultiset.equals(other.emulateMultiset)) {
                return false;
            }
        }
        if (emulateComputedColumns == null) {
            if (other.emulateComputedColumns!= null) {
                return false;
            }
        } else {
            if (!emulateComputedColumns.equals(other.emulateComputedColumns)) {
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
        if (interpreterDialect == null) {
            if (other.interpreterDialect!= null) {
                return false;
            }
        } else {
            if (!interpreterDialect.equals(other.interpreterDialect)) {
                return false;
            }
        }
        if (interpreterNameLookupCaseSensitivity == null) {
            if (other.interpreterNameLookupCaseSensitivity!= null) {
                return false;
            }
        } else {
            if (!interpreterNameLookupCaseSensitivity.equals(other.interpreterNameLookupCaseSensitivity)) {
                return false;
            }
        }
        if (interpreterLocale == null) {
            if (other.interpreterLocale!= null) {
                return false;
            }
        } else {
            if (!interpreterLocale.equals(other.interpreterLocale)) {
                return false;
            }
        }
        if (interpreterDelayForeignKeyDeclarations == null) {
            if (other.interpreterDelayForeignKeyDeclarations!= null) {
                return false;
            }
        } else {
            if (!interpreterDelayForeignKeyDeclarations.equals(other.interpreterDelayForeignKeyDeclarations)) {
                return false;
            }
        }
        if (metaIncludeSystemIndexes == null) {
            if (other.metaIncludeSystemIndexes!= null) {
                return false;
            }
        } else {
            if (!metaIncludeSystemIndexes.equals(other.metaIncludeSystemIndexes)) {
                return false;
            }
        }
        if (metaIncludeSystemSequences == null) {
            if (other.metaIncludeSystemSequences!= null) {
                return false;
            }
        } else {
            if (!metaIncludeSystemSequences.equals(other.metaIncludeSystemSequences)) {
                return false;
            }
        }
        if (migrationAllowsUndo == null) {
            if (other.migrationAllowsUndo!= null) {
                return false;
            }
        } else {
            if (!migrationAllowsUndo.equals(other.migrationAllowsUndo)) {
                return false;
            }
        }
        if (migrationRevertUntracked == null) {
            if (other.migrationRevertUntracked!= null) {
                return false;
            }
        } else {
            if (!migrationRevertUntracked.equals(other.migrationRevertUntracked)) {
                return false;
            }
        }
        if (migrationAutoBaseline == null) {
            if (other.migrationAutoBaseline!= null) {
                return false;
            }
        } else {
            if (!migrationAutoBaseline.equals(other.migrationAutoBaseline)) {
                return false;
            }
        }
        if (migrationAutoValidation == null) {
            if (other.migrationAutoValidation!= null) {
                return false;
            }
        } else {
            if (!migrationAutoValidation.equals(other.migrationAutoValidation)) {
                return false;
            }
        }
        if (migrationIgnoreDefaultTimestampPrecisionDiffs == null) {
            if (other.migrationIgnoreDefaultTimestampPrecisionDiffs!= null) {
                return false;
            }
        } else {
            if (!migrationIgnoreDefaultTimestampPrecisionDiffs.equals(other.migrationIgnoreDefaultTimestampPrecisionDiffs)) {
                return false;
            }
        }
        if (locale == null) {
            if (other.locale!= null) {
                return false;
            }
        } else {
            if (!locale.equals(other.locale)) {
                return false;
            }
        }
        if (parseDialect == null) {
            if (other.parseDialect!= null) {
                return false;
            }
        } else {
            if (!parseDialect.equals(other.parseDialect)) {
                return false;
            }
        }
        if (parseLocale == null) {
            if (other.parseLocale!= null) {
                return false;
            }
        } else {
            if (!parseLocale.equals(other.parseLocale)) {
                return false;
            }
        }
        if (parseDateFormat == null) {
            if (other.parseDateFormat!= null) {
                return false;
            }
        } else {
            if (!parseDateFormat.equals(other.parseDateFormat)) {
                return false;
            }
        }
        if (parseTimestampFormat == null) {
            if (other.parseTimestampFormat!= null) {
                return false;
            }
        } else {
            if (!parseTimestampFormat.equals(other.parseTimestampFormat)) {
                return false;
            }
        }
        if (parseNamedParamPrefix == null) {
            if (other.parseNamedParamPrefix!= null) {
                return false;
            }
        } else {
            if (!parseNamedParamPrefix.equals(other.parseNamedParamPrefix)) {
                return false;
            }
        }
        if (parseNameCase == null) {
            if (other.parseNameCase!= null) {
                return false;
            }
        } else {
            if (!parseNameCase.equals(other.parseNameCase)) {
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
        if (parseAppendMissingTableReferences == null) {
            if (other.parseAppendMissingTableReferences!= null) {
                return false;
            }
        } else {
            if (!parseAppendMissingTableReferences.equals(other.parseAppendMissingTableReferences)) {
                return false;
            }
        }
        if (parseSetCommands == null) {
            if (other.parseSetCommands!= null) {
                return false;
            }
        } else {
            if (!parseSetCommands.equals(other.parseSetCommands)) {
                return false;
            }
        }
        if (parseUnsupportedSyntax == null) {
            if (other.parseUnsupportedSyntax!= null) {
                return false;
            }
        } else {
            if (!parseUnsupportedSyntax.equals(other.parseUnsupportedSyntax)) {
                return false;
            }
        }
        if (parseUnknownFunctions == null) {
            if (other.parseUnknownFunctions!= null) {
                return false;
            }
        } else {
            if (!parseUnknownFunctions.equals(other.parseUnknownFunctions)) {
                return false;
            }
        }
        if (parseIgnoreCommercialOnlyFeatures == null) {
            if (other.parseIgnoreCommercialOnlyFeatures!= null) {
                return false;
            }
        } else {
            if (!parseIgnoreCommercialOnlyFeatures.equals(other.parseIgnoreCommercialOnlyFeatures)) {
                return false;
            }
        }
        if (parseIgnoreComments == null) {
            if (other.parseIgnoreComments!= null) {
                return false;
            }
        } else {
            if (!parseIgnoreComments.equals(other.parseIgnoreComments)) {
                return false;
            }
        }
        if (parseIgnoreCommentStart == null) {
            if (other.parseIgnoreCommentStart!= null) {
                return false;
            }
        } else {
            if (!parseIgnoreCommentStart.equals(other.parseIgnoreCommentStart)) {
                return false;
            }
        }
        if (parseIgnoreCommentStop == null) {
            if (other.parseIgnoreCommentStop!= null) {
                return false;
            }
        } else {
            if (!parseIgnoreCommentStop.equals(other.parseIgnoreCommentStop)) {
                return false;
            }
        }
        if (parseRetainCommentsBetweenQueries == null) {
            if (other.parseRetainCommentsBetweenQueries!= null) {
                return false;
            }
        } else {
            if (!parseRetainCommentsBetweenQueries.equals(other.parseRetainCommentsBetweenQueries)) {
                return false;
            }
        }
        if (parseMetaDefaultExpressions == null) {
            if (other.parseMetaDefaultExpressions!= null) {
                return false;
            }
        } else {
            if (!parseMetaDefaultExpressions.equals(other.parseMetaDefaultExpressions)) {
                return false;
            }
        }
        if (readonlyTableRecordInsert == null) {
            if (other.readonlyTableRecordInsert!= null) {
                return false;
            }
        } else {
            if (!readonlyTableRecordInsert.equals(other.readonlyTableRecordInsert)) {
                return false;
            }
        }
        if (readonlyUpdatableRecordUpdate == null) {
            if (other.readonlyUpdatableRecordUpdate!= null) {
                return false;
            }
        } else {
            if (!readonlyUpdatableRecordUpdate.equals(other.readonlyUpdatableRecordUpdate)) {
                return false;
            }
        }
        if (readonlyInsert == null) {
            if (other.readonlyInsert!= null) {
                return false;
            }
        } else {
            if (!readonlyInsert.equals(other.readonlyInsert)) {
                return false;
            }
        }
        if (readonlyUpdate == null) {
            if (other.readonlyUpdate!= null) {
                return false;
            }
        } else {
            if (!readonlyUpdate.equals(other.readonlyUpdate)) {
                return false;
            }
        }
        if (applyWorkaroundFor7962 == null) {
            if (other.applyWorkaroundFor7962 != null) {
                return false;
            }
        } else {
            if (!applyWorkaroundFor7962 .equals(other.applyWorkaroundFor7962)) {
                return false;
            }
        }
        if ((interpreterSearchPath == null)||interpreterSearchPath.isEmpty()) {
            if ((other.interpreterSearchPath!= null)&&(!other.interpreterSearchPath.isEmpty())) {
                return false;
            }
        } else {
            if (!interpreterSearchPath.equals(other.interpreterSearchPath)) {
                return false;
            }
        }
        if ((migrationSchemata == null)||migrationSchemata.isEmpty()) {
            if ((other.migrationSchemata!= null)&&(!other.migrationSchemata.isEmpty())) {
                return false;
            }
        } else {
            if (!migrationSchemata.equals(other.migrationSchemata)) {
                return false;
            }
        }
        if ((parseSearchPath == null)||parseSearchPath.isEmpty()) {
            if ((other.parseSearchPath!= null)&&(!other.parseSearchPath.isEmpty())) {
                return false;
            }
        } else {
            if (!parseSearchPath.equals(other.parseSearchPath)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = ((prime*result)+((forceIntegerTypesOnZeroScaleDecimals == null)? 0 :forceIntegerTypesOnZeroScaleDecimals.hashCode()));
        result = ((prime*result)+((renderCatalog == null)? 0 :renderCatalog.hashCode()));
        result = ((prime*result)+((renderSchema == null)? 0 :renderSchema.hashCode()));
        result = ((prime*result)+((renderTable == null)? 0 :renderTable.hashCode()));
        result = ((prime*result)+((renderMapping == null)? 0 :renderMapping.hashCode()));
        result = ((prime*result)+((renderQuotedNames == null)? 0 :renderQuotedNames.hashCode()));
        result = ((prime*result)+((renderNameCase == null)? 0 :renderNameCase.hashCode()));
        result = ((prime*result)+((renderNameStyle == null)? 0 :renderNameStyle.hashCode()));
        result = ((prime*result)+((renderNamedParamPrefix == null)? 0 :renderNamedParamPrefix.hashCode()));
        result = ((prime*result)+((renderKeywordCase == null)? 0 :renderKeywordCase.hashCode()));
        result = ((prime*result)+((renderKeywordStyle == null)? 0 :renderKeywordStyle.hashCode()));
        result = ((prime*result)+((renderLocale == null)? 0 :renderLocale.hashCode()));
        result = ((prime*result)+((renderFormatted == null)? 0 :renderFormatted.hashCode()));
        result = ((prime*result)+((renderFormatting == null)? 0 :renderFormatting.hashCode()));
        result = ((prime*result)+((renderOptionalAssociativityParentheses == null)? 0 :renderOptionalAssociativityParentheses.hashCode()));
        result = ((prime*result)+((renderOptionalAsKeywordForTableAliases == null)? 0 :renderOptionalAsKeywordForTableAliases.hashCode()));
        result = ((prime*result)+((renderOptionalAsKeywordForFieldAliases == null)? 0 :renderOptionalAsKeywordForFieldAliases.hashCode()));
        result = ((prime*result)+((renderOptionalInnerKeyword == null)? 0 :renderOptionalInnerKeyword.hashCode()));
        result = ((prime*result)+((renderOptionalOuterKeyword == null)? 0 :renderOptionalOuterKeyword.hashCode()));
        result = ((prime*result)+((renderImplicitWindowRange == null)? 0 :renderImplicitWindowRange.hashCode()));
        result = ((prime*result)+((renderScalarSubqueriesForStoredFunctions == null)? 0 :renderScalarSubqueriesForStoredFunctions.hashCode()));
        result = ((prime*result)+((renderImplicitJoinType == null)? 0 :renderImplicitJoinType.hashCode()));
        result = ((prime*result)+((renderDefaultNullability == null)? 0 :renderDefaultNullability.hashCode()));
        result = ((prime*result)+((renderCoalesceToEmptyStringInConcat == null)? 0 :renderCoalesceToEmptyStringInConcat.hashCode()));
        result = ((prime*result)+((renderOrderByRownumberForEmulatedPagination == null)? 0 :renderOrderByRownumberForEmulatedPagination.hashCode()));
        result = ((prime*result)+((renderOutputForSQLServerReturningClause == null)? 0 :renderOutputForSQLServerReturningClause.hashCode()));
        result = ((prime*result)+((renderGroupConcatMaxLenSessionVariable == null)? 0 :renderGroupConcatMaxLenSessionVariable.hashCode()));
        result = ((prime*result)+((renderParenthesisAroundSetOperationQueries == null)? 0 :renderParenthesisAroundSetOperationQueries.hashCode()));
        result = ((prime*result)+((renderVariablesInDerivedTablesForEmulations == null)? 0 :renderVariablesInDerivedTablesForEmulations.hashCode()));
        result = ((prime*result)+((renderRowConditionForSeekClause == null)? 0 :renderRowConditionForSeekClause.hashCode()));
        result = ((prime*result)+((renderRedundantConditionForSeekClause == null)? 0 :renderRedundantConditionForSeekClause.hashCode()));
        result = ((prime*result)+((renderPlainSQLTemplatesAsRaw == null)? 0 :renderPlainSQLTemplatesAsRaw.hashCode()));
        result = ((prime*result)+((namePathSeparator == null)? 0 :namePathSeparator.hashCode()));
        result = ((prime*result)+((bindOffsetDateTimeType == null)? 0 :bindOffsetDateTimeType.hashCode()));
        result = ((prime*result)+((bindOffsetTimeType == null)? 0 :bindOffsetTimeType.hashCode()));
        result = ((prime*result)+((fetchTriggerValuesAfterSQLServerOutput == null)? 0 :fetchTriggerValuesAfterSQLServerOutput.hashCode()));
        result = ((prime*result)+((fetchIntermediateResult == null)? 0 :fetchIntermediateResult.hashCode()));
        result = ((prime*result)+((diagnosticsDuplicateStatements == null)? 0 :diagnosticsDuplicateStatements.hashCode()));
        result = ((prime*result)+((diagnosticsDuplicateStatementsUsingTransformPatterns == null)? 0 :diagnosticsDuplicateStatementsUsingTransformPatterns.hashCode()));
        result = ((prime*result)+((diagnosticsMissingWasNullCall == null)? 0 :diagnosticsMissingWasNullCall.hashCode()));
        result = ((prime*result)+((diagnosticsRepeatedStatements == null)? 0 :diagnosticsRepeatedStatements.hashCode()));
        result = ((prime*result)+((diagnosticsConsecutiveAggregation == null)? 0 :diagnosticsConsecutiveAggregation.hashCode()));
        result = ((prime*result)+((diagnosticsConcatenationInPredicate == null)? 0 :diagnosticsConcatenationInPredicate.hashCode()));
        result = ((prime*result)+((diagnosticsPossiblyWrongExpression == null)? 0 :diagnosticsPossiblyWrongExpression.hashCode()));
        result = ((prime*result)+((diagnosticsTooManyColumnsFetched == null)? 0 :diagnosticsTooManyColumnsFetched.hashCode()));
        result = ((prime*result)+((diagnosticsTooManyRowsFetched == null)? 0 :diagnosticsTooManyRowsFetched.hashCode()));
        result = ((prime*result)+((diagnosticsUnnecessaryWasNullCall == null)? 0 :diagnosticsUnnecessaryWasNullCall.hashCode()));
        result = ((prime*result)+((diagnosticsPatterns == null)? 0 :diagnosticsPatterns.hashCode()));
        result = ((prime*result)+((diagnosticsTrivialCondition == null)? 0 :diagnosticsTrivialCondition.hashCode()));
        result = ((prime*result)+((diagnosticsNullCondition == null)? 0 :diagnosticsNullCondition.hashCode()));
        result = ((prime*result)+((transformPatterns == null)? 0 :transformPatterns.hashCode()));
        result = ((prime*result)+((transformPatternsLogging == null)? 0 :transformPatternsLogging.hashCode()));
        result = ((prime*result)+((transformPatternsUnnecessaryDistinct == null)? 0 :transformPatternsUnnecessaryDistinct.hashCode()));
        result = ((prime*result)+((transformPatternsUnnecessaryScalarSubquery == null)? 0 :transformPatternsUnnecessaryScalarSubquery.hashCode()));
        result = ((prime*result)+((transformPatternsUnnecessaryInnerJoin == null)? 0 :transformPatternsUnnecessaryInnerJoin.hashCode()));
        result = ((prime*result)+((transformPatternsUnnecessaryGroupByExpressions == null)? 0 :transformPatternsUnnecessaryGroupByExpressions.hashCode()));
        result = ((prime*result)+((transformPatternsUnnecessaryOrderByExpressions == null)? 0 :transformPatternsUnnecessaryOrderByExpressions.hashCode()));
        result = ((prime*result)+((transformPatternsUnnecessaryExistsSubqueryClauses == null)? 0 :transformPatternsUnnecessaryExistsSubqueryClauses.hashCode()));
        result = ((prime*result)+((transformPatternsCountConstant == null)? 0 :transformPatternsCountConstant.hashCode()));
        result = ((prime*result)+((transformPatternsTrim == null)? 0 :transformPatternsTrim.hashCode()));
        result = ((prime*result)+((transformPatternsNotAnd == null)? 0 :transformPatternsNotAnd.hashCode()));
        result = ((prime*result)+((transformPatternsNotOr == null)? 0 :transformPatternsNotOr.hashCode()));
        result = ((prime*result)+((transformPatternsNotNot == null)? 0 :transformPatternsNotNot.hashCode()));
        result = ((prime*result)+((transformPatternsNotComparison == null)? 0 :transformPatternsNotComparison.hashCode()));
        result = ((prime*result)+((transformPatternsNotNotDistinct == null)? 0 :transformPatternsNotNotDistinct.hashCode()));
        result = ((prime*result)+((transformPatternsDistinctFromNull == null)? 0 :transformPatternsDistinctFromNull.hashCode()));
        result = ((prime*result)+((transformPatternsNormaliseAssociativeOps == null)? 0 :transformPatternsNormaliseAssociativeOps.hashCode()));
        result = ((prime*result)+((transformPatternsNormaliseInListSingleElementToComparison == null)? 0 :transformPatternsNormaliseInListSingleElementToComparison.hashCode()));
        result = ((prime*result)+((transformPatternsNormaliseFieldCompareValue == null)? 0 :transformPatternsNormaliseFieldCompareValue.hashCode()));
        result = ((prime*result)+((transformPatternsNormaliseCoalesceToNvl == null)? 0 :transformPatternsNormaliseCoalesceToNvl.hashCode()));
        result = ((prime*result)+((transformPatternsOrEqToIn == null)? 0 :transformPatternsOrEqToIn.hashCode()));
        result = ((prime*result)+((transformPatternsAndNeToNotIn == null)? 0 :transformPatternsAndNeToNotIn.hashCode()));
        result = ((prime*result)+((transformPatternsMergeOrComparison == null)? 0 :transformPatternsMergeOrComparison.hashCode()));
        result = ((prime*result)+((transformPatternsMergeAndComparison == null)? 0 :transformPatternsMergeAndComparison.hashCode()));
        result = ((prime*result)+((transformPatternsMergeInLists == null)? 0 :transformPatternsMergeInLists.hashCode()));
        result = ((prime*result)+((transformPatternsMergeRangePredicates == null)? 0 :transformPatternsMergeRangePredicates.hashCode()));
        result = ((prime*result)+((transformPatternsMergeBetweenSymmetricPredicates == null)? 0 :transformPatternsMergeBetweenSymmetricPredicates.hashCode()));
        result = ((prime*result)+((transformPatternsCaseSearchedToCaseSimple == null)? 0 :transformPatternsCaseSearchedToCaseSimple.hashCode()));
        result = ((prime*result)+((transformPatternsCaseElseNull == null)? 0 :transformPatternsCaseElseNull.hashCode()));
        result = ((prime*result)+((transformPatternsUnreachableCaseClauses == null)? 0 :transformPatternsUnreachableCaseClauses.hashCode()));
        result = ((prime*result)+((transformPatternsUnreachableDecodeClauses == null)? 0 :transformPatternsUnreachableDecodeClauses.hashCode()));
        result = ((prime*result)+((transformPatternsCaseDistinctToDecode == null)? 0 :transformPatternsCaseDistinctToDecode.hashCode()));
        result = ((prime*result)+((transformPatternsCaseMergeWhenWhen == null)? 0 :transformPatternsCaseMergeWhenWhen.hashCode()));
        result = ((prime*result)+((transformPatternsCaseMergeWhenElse == null)? 0 :transformPatternsCaseMergeWhenElse.hashCode()));
        result = ((prime*result)+((transformPatternsCaseToCaseAbbreviation == null)? 0 :transformPatternsCaseToCaseAbbreviation.hashCode()));
        result = ((prime*result)+((transformPatternsSimplifyCaseAbbreviation == null)? 0 :transformPatternsSimplifyCaseAbbreviation.hashCode()));
        result = ((prime*result)+((transformPatternsFlattenCaseAbbreviation == null)? 0 :transformPatternsFlattenCaseAbbreviation.hashCode()));
        result = ((prime*result)+((transformPatternsFlattenDecode == null)? 0 :transformPatternsFlattenDecode.hashCode()));
        result = ((prime*result)+((transformPatternsFlattenCase == null)? 0 :transformPatternsFlattenCase.hashCode()));
        result = ((prime*result)+((transformPatternsTrivialCaseAbbreviation == null)? 0 :transformPatternsTrivialCaseAbbreviation.hashCode()));
        result = ((prime*result)+((transformPatternsTrivialPredicates == null)? 0 :transformPatternsTrivialPredicates.hashCode()));
        result = ((prime*result)+((transformPatternsTrivialBitwiseOperations == null)? 0 :transformPatternsTrivialBitwiseOperations.hashCode()));
        result = ((prime*result)+((transformPatternsBitSet == null)? 0 :transformPatternsBitSet.hashCode()));
        result = ((prime*result)+((transformPatternsBitGet == null)? 0 :transformPatternsBitGet.hashCode()));
        result = ((prime*result)+((transformPatternsScalarSubqueryCountAsteriskGtZero == null)? 0 :transformPatternsScalarSubqueryCountAsteriskGtZero.hashCode()));
        result = ((prime*result)+((transformPatternsScalarSubqueryCountExpressionGtZero == null)? 0 :transformPatternsScalarSubqueryCountExpressionGtZero.hashCode()));
        result = ((prime*result)+((transformPatternsEmptyScalarSubquery == null)? 0 :transformPatternsEmptyScalarSubquery.hashCode()));
        result = ((prime*result)+((transformPatternsNegNeg == null)? 0 :transformPatternsNegNeg.hashCode()));
        result = ((prime*result)+((transformPatternsBitNotBitNot == null)? 0 :transformPatternsBitNotBitNot.hashCode()));
        result = ((prime*result)+((transformPatternsBitNotBitNand == null)? 0 :transformPatternsBitNotBitNand.hashCode()));
        result = ((prime*result)+((transformPatternsBitNotBitNor == null)? 0 :transformPatternsBitNotBitNor.hashCode()));
        result = ((prime*result)+((transformPatternsBitNotBitXNor == null)? 0 :transformPatternsBitNotBitXNor.hashCode()));
        result = ((prime*result)+((transformPatternsNullOnNullInput == null)? 0 :transformPatternsNullOnNullInput.hashCode()));
        result = ((prime*result)+((transformPatternsIdempotentFunctionRepetition == null)? 0 :transformPatternsIdempotentFunctionRepetition.hashCode()));
        result = ((prime*result)+((transformPatternsArithmeticComparisons == null)? 0 :transformPatternsArithmeticComparisons.hashCode()));
        result = ((prime*result)+((transformPatternsArithmeticExpressions == null)? 0 :transformPatternsArithmeticExpressions.hashCode()));
        result = ((prime*result)+((transformPatternsTrigonometricFunctions == null)? 0 :transformPatternsTrigonometricFunctions.hashCode()));
        result = ((prime*result)+((transformPatternsLogarithmicFunctions == null)? 0 :transformPatternsLogarithmicFunctions.hashCode()));
        result = ((prime*result)+((transformPatternsHyperbolicFunctions == null)? 0 :transformPatternsHyperbolicFunctions.hashCode()));
        result = ((prime*result)+((transformPatternsInverseHyperbolicFunctions == null)? 0 :transformPatternsInverseHyperbolicFunctions.hashCode()));
        result = ((prime*result)+((transformInlineBindValuesForFieldComparisons == null)? 0 :transformInlineBindValuesForFieldComparisons.hashCode()));
        result = ((prime*result)+((transformAnsiJoinToTableLists == null)? 0 :transformAnsiJoinToTableLists.hashCode()));
        result = ((prime*result)+((transformInConditionSubqueryWithLimitToDerivedTable == null)? 0 :transformInConditionSubqueryWithLimitToDerivedTable.hashCode()));
        result = ((prime*result)+((transformQualify == null)? 0 :transformQualify.hashCode()));
        result = ((prime*result)+((transformTableListsToAnsiJoin == null)? 0 :transformTableListsToAnsiJoin.hashCode()));
        result = ((prime*result)+((transformRownum == null)? 0 :transformRownum.hashCode()));
        result = ((prime*result)+((transformUnneededArithmeticExpressions == null)? 0 :transformUnneededArithmeticExpressions.hashCode()));
        result = ((prime*result)+((transformGroupByColumnIndex == null)? 0 :transformGroupByColumnIndex.hashCode()));
        result = ((prime*result)+((transformInlineCTE == null)? 0 :transformInlineCTE.hashCode()));
        result = ((prime*result)+((backslashEscaping == null)? 0 :backslashEscaping.hashCode()));
        result = ((prime*result)+((paramType == null)? 0 :paramType.hashCode()));
        result = ((prime*result)+((paramCastMode == null)? 0 :paramCastMode.hashCode()));
        result = ((prime*result)+((statementType == null)? 0 :statementType.hashCode()));
        result = ((prime*result)+((inlineThreshold == null)? 0 :inlineThreshold.hashCode()));
        result = ((prime*result)+((transactionListenerStartInvocationOrder == null)? 0 :transactionListenerStartInvocationOrder.hashCode()));
        result = ((prime*result)+((transactionListenerEndInvocationOrder == null)? 0 :transactionListenerEndInvocationOrder.hashCode()));
        result = ((prime*result)+((migrationListenerStartInvocationOrder == null)? 0 :migrationListenerStartInvocationOrder.hashCode()));
        result = ((prime*result)+((migrationListenerEndInvocationOrder == null)? 0 :migrationListenerEndInvocationOrder.hashCode()));
        result = ((prime*result)+((visitListenerStartInvocationOrder == null)? 0 :visitListenerStartInvocationOrder.hashCode()));
        result = ((prime*result)+((visitListenerEndInvocationOrder == null)? 0 :visitListenerEndInvocationOrder.hashCode()));
        result = ((prime*result)+((recordListenerStartInvocationOrder == null)? 0 :recordListenerStartInvocationOrder.hashCode()));
        result = ((prime*result)+((recordListenerEndInvocationOrder == null)? 0 :recordListenerEndInvocationOrder.hashCode()));
        result = ((prime*result)+((executeListenerStartInvocationOrder == null)? 0 :executeListenerStartInvocationOrder.hashCode()));
        result = ((prime*result)+((executeListenerEndInvocationOrder == null)? 0 :executeListenerEndInvocationOrder.hashCode()));
        result = ((prime*result)+((executeLogging == null)? 0 :executeLogging.hashCode()));
        result = ((prime*result)+((executeLoggingSQLExceptions == null)? 0 :executeLoggingSQLExceptions.hashCode()));
        result = ((prime*result)+((diagnosticsLogging == null)? 0 :diagnosticsLogging.hashCode()));
        result = ((prime*result)+((diagnosticsConnection == null)? 0 :diagnosticsConnection.hashCode()));
        result = ((prime*result)+((updateRecordVersion == null)? 0 :updateRecordVersion.hashCode()));
        result = ((prime*result)+((updateRecordTimestamp == null)? 0 :updateRecordTimestamp.hashCode()));
        result = ((prime*result)+((executeWithOptimisticLocking == null)? 0 :executeWithOptimisticLocking.hashCode()));
        result = ((prime*result)+((executeWithOptimisticLockingExcludeUnversioned == null)? 0 :executeWithOptimisticLockingExcludeUnversioned.hashCode()));
        result = ((prime*result)+((attachRecords == null)? 0 :attachRecords.hashCode()));
        result = ((prime*result)+((insertUnchangedRecords == null)? 0 :insertUnchangedRecords.hashCode()));
        result = ((prime*result)+((updateUnchangedRecords == null)? 0 :updateUnchangedRecords.hashCode()));
        result = ((prime*result)+((updatablePrimaryKeys == null)? 0 :updatablePrimaryKeys.hashCode()));
        result = ((prime*result)+((reflectionCaching == null)? 0 :reflectionCaching.hashCode()));
        result = ((prime*result)+((cacheRecordMappers == null)? 0 :cacheRecordMappers.hashCode()));
        result = ((prime*result)+((cacheParsingConnection == null)? 0 :cacheParsingConnection.hashCode()));
        result = ((prime*result)+((cacheParsingConnectionLRUCacheSize == null)? 0 :cacheParsingConnectionLRUCacheSize.hashCode()));
        result = ((prime*result)+((cachePreparedStatementInLoader == null)? 0 :cachePreparedStatementInLoader.hashCode()));
        result = ((prime*result)+((throwExceptions == null)? 0 :throwExceptions.hashCode()));
        result = ((prime*result)+((fetchWarnings == null)? 0 :fetchWarnings.hashCode()));
        result = ((prime*result)+((fetchServerOutputSize == null)? 0 :fetchServerOutputSize.hashCode()));
        result = ((prime*result)+((returnIdentityOnUpdatableRecord == null)? 0 :returnIdentityOnUpdatableRecord.hashCode()));
        result = ((prime*result)+((returnDefaultOnUpdatableRecord == null)? 0 :returnDefaultOnUpdatableRecord.hashCode()));
        result = ((prime*result)+((returnComputedOnUpdatableRecord == null)? 0 :returnComputedOnUpdatableRecord.hashCode()));
        result = ((prime*result)+((returnAllOnUpdatableRecord == null)? 0 :returnAllOnUpdatableRecord.hashCode()));
        result = ((prime*result)+((returnRecordToPojo == null)? 0 :returnRecordToPojo.hashCode()));
        result = ((prime*result)+((mapJPAAnnotations == null)? 0 :mapJPAAnnotations.hashCode()));
        result = ((prime*result)+((mapRecordComponentParameterNames == null)? 0 :mapRecordComponentParameterNames.hashCode()));
        result = ((prime*result)+((mapConstructorPropertiesParameterNames == null)? 0 :mapConstructorPropertiesParameterNames.hashCode()));
        result = ((prime*result)+((mapConstructorParameterNames == null)? 0 :mapConstructorParameterNames.hashCode()));
        result = ((prime*result)+((mapConstructorParameterNamesInKotlin == null)? 0 :mapConstructorParameterNamesInKotlin.hashCode()));
        result = ((prime*result)+((queryPoolable == null)? 0 :queryPoolable.hashCode()));
        result = ((prime*result)+((queryTimeout == null)? 0 :queryTimeout.hashCode()));
        result = ((prime*result)+((maxRows == null)? 0 :maxRows.hashCode()));
        result = ((prime*result)+((fetchSize == null)? 0 :fetchSize.hashCode()));
        result = ((prime*result)+((batchSize == null)? 0 :batchSize.hashCode()));
        result = ((prime*result)+((debugInfoOnStackTrace == null)? 0 :debugInfoOnStackTrace.hashCode()));
        result = ((prime*result)+((inListPadding == null)? 0 :inListPadding.hashCode()));
        result = ((prime*result)+((inListPadBase == null)? 0 :inListPadBase.hashCode()));
        result = ((prime*result)+((delimiter == null)? 0 :delimiter.hashCode()));
        result = ((prime*result)+((emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly == null)? 0 :emulateOnDuplicateKeyUpdateOnPrimaryKeyOnly.hashCode()));
        result = ((prime*result)+((emulateMultiset == null)? 0 :emulateMultiset.hashCode()));
        result = ((prime*result)+((emulateComputedColumns == null)? 0 :emulateComputedColumns.hashCode()));
        result = ((prime*result)+((executeUpdateWithoutWhere == null)? 0 :executeUpdateWithoutWhere.hashCode()));
        result = ((prime*result)+((executeDeleteWithoutWhere == null)? 0 :executeDeleteWithoutWhere.hashCode()));
        result = ((prime*result)+((interpreterDialect == null)? 0 :interpreterDialect.hashCode()));
        result = ((prime*result)+((interpreterNameLookupCaseSensitivity == null)? 0 :interpreterNameLookupCaseSensitivity.hashCode()));
        result = ((prime*result)+((interpreterLocale == null)? 0 :interpreterLocale.hashCode()));
        result = ((prime*result)+((interpreterDelayForeignKeyDeclarations == null)? 0 :interpreterDelayForeignKeyDeclarations.hashCode()));
        result = ((prime*result)+((metaIncludeSystemIndexes == null)? 0 :metaIncludeSystemIndexes.hashCode()));
        result = ((prime*result)+((metaIncludeSystemSequences == null)? 0 :metaIncludeSystemSequences.hashCode()));
        result = ((prime*result)+((migrationAllowsUndo == null)? 0 :migrationAllowsUndo.hashCode()));
        result = ((prime*result)+((migrationRevertUntracked == null)? 0 :migrationRevertUntracked.hashCode()));
        result = ((prime*result)+((migrationAutoBaseline == null)? 0 :migrationAutoBaseline.hashCode()));
        result = ((prime*result)+((migrationAutoValidation == null)? 0 :migrationAutoValidation.hashCode()));
        result = ((prime*result)+((migrationIgnoreDefaultTimestampPrecisionDiffs == null)? 0 :migrationIgnoreDefaultTimestampPrecisionDiffs.hashCode()));
        result = ((prime*result)+((locale == null)? 0 :locale.hashCode()));
        result = ((prime*result)+((parseDialect == null)? 0 :parseDialect.hashCode()));
        result = ((prime*result)+((parseLocale == null)? 0 :parseLocale.hashCode()));
        result = ((prime*result)+((parseDateFormat == null)? 0 :parseDateFormat.hashCode()));
        result = ((prime*result)+((parseTimestampFormat == null)? 0 :parseTimestampFormat.hashCode()));
        result = ((prime*result)+((parseNamedParamPrefix == null)? 0 :parseNamedParamPrefix.hashCode()));
        result = ((prime*result)+((parseNameCase == null)? 0 :parseNameCase.hashCode()));
        result = ((prime*result)+((parseWithMetaLookups == null)? 0 :parseWithMetaLookups.hashCode()));
        result = ((prime*result)+((parseAppendMissingTableReferences == null)? 0 :parseAppendMissingTableReferences.hashCode()));
        result = ((prime*result)+((parseSetCommands == null)? 0 :parseSetCommands.hashCode()));
        result = ((prime*result)+((parseUnsupportedSyntax == null)? 0 :parseUnsupportedSyntax.hashCode()));
        result = ((prime*result)+((parseUnknownFunctions == null)? 0 :parseUnknownFunctions.hashCode()));
        result = ((prime*result)+((parseIgnoreCommercialOnlyFeatures == null)? 0 :parseIgnoreCommercialOnlyFeatures.hashCode()));
        result = ((prime*result)+((parseIgnoreComments == null)? 0 :parseIgnoreComments.hashCode()));
        result = ((prime*result)+((parseIgnoreCommentStart == null)? 0 :parseIgnoreCommentStart.hashCode()));
        result = ((prime*result)+((parseIgnoreCommentStop == null)? 0 :parseIgnoreCommentStop.hashCode()));
        result = ((prime*result)+((parseRetainCommentsBetweenQueries == null)? 0 :parseRetainCommentsBetweenQueries.hashCode()));
        result = ((prime*result)+((parseMetaDefaultExpressions == null)? 0 :parseMetaDefaultExpressions.hashCode()));
        result = ((prime*result)+((readonlyTableRecordInsert == null)? 0 :readonlyTableRecordInsert.hashCode()));
        result = ((prime*result)+((readonlyUpdatableRecordUpdate == null)? 0 :readonlyUpdatableRecordUpdate.hashCode()));
        result = ((prime*result)+((readonlyInsert == null)? 0 :readonlyInsert.hashCode()));
        result = ((prime*result)+((readonlyUpdate == null)? 0 :readonlyUpdate.hashCode()));
        result = ((prime*result)+((applyWorkaroundFor7962 == null)? 0 :applyWorkaroundFor7962 .hashCode()));
        result = ((prime*result)+(((interpreterSearchPath == null)||interpreterSearchPath.isEmpty())? 0 :interpreterSearchPath.hashCode()));
        result = ((prime*result)+(((migrationSchemata == null)||migrationSchemata.isEmpty())? 0 :migrationSchemata.hashCode()));
        result = ((prime*result)+(((parseSearchPath == null)||parseSearchPath.isEmpty())? 0 :parseSearchPath.hashCode()));
        return result;
    }

}
