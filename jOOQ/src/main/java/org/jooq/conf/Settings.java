//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren.
// Generiert: 2017.04.24 um 10:35:14 AM CEST
//


package org.jooq.conf;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Settings complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="Settings"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element name="renderCatalog" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="renderSchema" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="renderMapping" type="{http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd}RenderMapping" minOccurs="0"/&gt;
 *         &lt;element name="renderNameStyle" type="{http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd}RenderNameStyle" minOccurs="0"/&gt;
 *         &lt;element name="renderKeywordStyle" type="{http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd}RenderKeywordStyle" minOccurs="0"/&gt;
 *         &lt;element name="renderFormatted" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="renderScalarSubqueriesForStoredFunctions" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="backslashEscaping" type="{http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd}BackslashEscaping" minOccurs="0"/&gt;
 *         &lt;element name="paramType" type="{http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd}ParamType" minOccurs="0"/&gt;
 *         &lt;element name="paramCastMode" type="{http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd}ParamCastMode" minOccurs="0"/&gt;
 *         &lt;element name="statementType" type="{http://www.jooq.org/xsd/jooq-runtime-3.9.0.xsd}StatementType" minOccurs="0"/&gt;
 *         &lt;element name="executeLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="executeWithOptimisticLocking" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="executeWithOptimisticLockingExcludeUnversioned" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="attachRecords" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="updatablePrimaryKeys" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="reflectionCaching" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="fetchWarnings" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="returnAllOnUpdatableRecord" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="returnRecordToPojo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="mapJPAAnnotations" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="queryTimeout" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="maxRows" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="fetchSize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="debugInfoOnStackTrace" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="inListPadding" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="delimiter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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

    private final static long serialVersionUID = 31000L;
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
    @XmlElement(defaultValue = "false")
    protected Boolean renderScalarSubqueriesForStoredFunctions = false;
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
    protected Boolean fetchWarnings = true;
    @XmlElement(defaultValue = "false")
    protected Boolean returnAllOnUpdatableRecord = false;
    @XmlElement(defaultValue = "true")
    protected Boolean returnRecordToPojo = true;
    @XmlElement(defaultValue = "true")
    protected Boolean mapJPAAnnotations = true;
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
    @XmlElement(defaultValue = ";")
    protected String delimiter = ";";

    /**
     * Ruft den Wert der renderCatalog-Eigenschaft ab.
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
     * Legt den Wert der renderCatalog-Eigenschaft fest.
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
     * Ruft den Wert der renderSchema-Eigenschaft ab.
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
     * Legt den Wert der renderSchema-Eigenschaft fest.
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
     * Ruft den Wert der renderMapping-Eigenschaft ab.
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
     * Legt den Wert der renderMapping-Eigenschaft fest.
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
     * Ruft den Wert der renderNameStyle-Eigenschaft ab.
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
     * Legt den Wert der renderNameStyle-Eigenschaft fest.
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
     * Ruft den Wert der renderKeywordStyle-Eigenschaft ab.
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
     * Legt den Wert der renderKeywordStyle-Eigenschaft fest.
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
     * Ruft den Wert der renderFormatted-Eigenschaft ab.
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
     * Legt den Wert der renderFormatted-Eigenschaft fest.
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
     * Ruft den Wert der renderScalarSubqueriesForStoredFunctions-Eigenschaft ab.
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
     * Legt den Wert der renderScalarSubqueriesForStoredFunctions-Eigenschaft fest.
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
     * Ruft den Wert der backslashEscaping-Eigenschaft ab.
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
     * Legt den Wert der backslashEscaping-Eigenschaft fest.
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
     * Ruft den Wert der paramType-Eigenschaft ab.
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
     * Legt den Wert der paramType-Eigenschaft fest.
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
     * Ruft den Wert der paramCastMode-Eigenschaft ab.
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
     * Legt den Wert der paramCastMode-Eigenschaft fest.
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
     * Ruft den Wert der statementType-Eigenschaft ab.
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
     * Legt den Wert der statementType-Eigenschaft fest.
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
     * Ruft den Wert der executeLogging-Eigenschaft ab.
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
     * Legt den Wert der executeLogging-Eigenschaft fest.
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
     * Ruft den Wert der executeWithOptimisticLocking-Eigenschaft ab.
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
     * Legt den Wert der executeWithOptimisticLocking-Eigenschaft fest.
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
     * Ruft den Wert der executeWithOptimisticLockingExcludeUnversioned-Eigenschaft ab.
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
     * Legt den Wert der executeWithOptimisticLockingExcludeUnversioned-Eigenschaft fest.
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
     * Ruft den Wert der attachRecords-Eigenschaft ab.
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
     * Legt den Wert der attachRecords-Eigenschaft fest.
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
     * Ruft den Wert der updatablePrimaryKeys-Eigenschaft ab.
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
     * Legt den Wert der updatablePrimaryKeys-Eigenschaft fest.
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
     * Ruft den Wert der reflectionCaching-Eigenschaft ab.
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
     * Legt den Wert der reflectionCaching-Eigenschaft fest.
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
     * Ruft den Wert der fetchWarnings-Eigenschaft ab.
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
     * Legt den Wert der fetchWarnings-Eigenschaft fest.
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
     * Ruft den Wert der returnAllOnUpdatableRecord-Eigenschaft ab.
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
     * Legt den Wert der returnAllOnUpdatableRecord-Eigenschaft fest.
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
     * Ruft den Wert der returnRecordToPojo-Eigenschaft ab.
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
     * Legt den Wert der returnRecordToPojo-Eigenschaft fest.
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
     * Ruft den Wert der mapJPAAnnotations-Eigenschaft ab.
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
     * Legt den Wert der mapJPAAnnotations-Eigenschaft fest.
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
     * Ruft den Wert der queryTimeout-Eigenschaft ab.
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
     * Legt den Wert der queryTimeout-Eigenschaft fest.
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
     * Ruft den Wert der maxRows-Eigenschaft ab.
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
     * Legt den Wert der maxRows-Eigenschaft fest.
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
     * Ruft den Wert der fetchSize-Eigenschaft ab.
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
     * Legt den Wert der fetchSize-Eigenschaft fest.
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
     * Ruft den Wert der debugInfoOnStackTrace-Eigenschaft ab.
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
     * Legt den Wert der debugInfoOnStackTrace-Eigenschaft fest.
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
     * Ruft den Wert der inListPadding-Eigenschaft ab.
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
     * Legt den Wert der inListPadding-Eigenschaft fest.
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
     * Ruft den Wert der delimiter-Eigenschaft ab.
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
     * Legt den Wert der delimiter-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDelimiter(String value) {
        this.delimiter = value;
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

    public Settings withRenderScalarSubqueriesForStoredFunctions(Boolean value) {
        setRenderScalarSubqueriesForStoredFunctions(value);
        return this;
    }

    public Settings withBackslashEscaping(BackslashEscaping value) {
        setBackslashEscaping(value);
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

    public Settings withFetchWarnings(Boolean value) {
        setFetchWarnings(value);
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

    public Settings withDelimiter(String value) {
        setDelimiter(value);
        return this;
    }

}
