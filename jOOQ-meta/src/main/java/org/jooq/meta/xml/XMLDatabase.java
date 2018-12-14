/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package org.jooq.meta.xml;

import static org.jooq.impl.DSL.name;
import static org.jooq.tools.StringUtils.defaultIfBlank;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.isBlank;
import static org.jooq.util.xml.jaxb.TableConstraintType.PRIMARY_KEY;
import static org.jooq.util.xml.jaxb.TableConstraintType.UNIQUE;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.JAXB;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jooq.Constants;
import org.jooq.DSLContext;
import org.jooq.Name;
import org.jooq.SQLDialect;
import org.jooq.SortOrder;
import org.jooq.conf.MiniJAXB;
import org.jooq.exception.ExceptionTools;
import org.jooq.impl.DSL;
import org.jooq.meta.AbstractDatabase;
import org.jooq.meta.AbstractIndexDefinition;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.DataTypeDefinition;
import org.jooq.meta.DefaultDataTypeDefinition;
import org.jooq.meta.DefaultIndexColumnDefinition;
import org.jooq.meta.DefaultRelations;
import org.jooq.meta.DefaultSequenceDefinition;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.IndexColumnDefinition;
import org.jooq.meta.IndexDefinition;
import org.jooq.meta.PackageDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UDTDefinition;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.util.xml.jaxb.Index;
import org.jooq.util.xml.jaxb.IndexColumnUsage;
import org.jooq.util.xml.jaxb.InformationSchema;
import org.jooq.util.xml.jaxb.KeyColumnUsage;
import org.jooq.util.xml.jaxb.ReferentialConstraint;
import org.jooq.util.xml.jaxb.Routine;
import org.jooq.util.xml.jaxb.Schema;
import org.jooq.util.xml.jaxb.Sequence;
import org.jooq.util.xml.jaxb.Table;
import org.jooq.util.xml.jaxb.TableConstraint;
import org.jooq.util.xml.jaxb.TableConstraintType;

/**
 * The XML Database.
 *
 * @author Lukas Eder
 */
public class XMLDatabase extends AbstractDatabase {

    private static final JooqLogger log        = JooqLogger.getLogger(XMLDatabase.class);

    InformationSchema               info;

    private InformationSchema info() {
        if (info == null) {

            // [#8115] Support old property name style for backwards compatibility reasons
            String xml = getProperties().getProperty("xmlFile", getProperties().getProperty("xml-file"));
            String xsl = getProperties().getProperty("xslFile", getProperties().getProperty("xsl-file"));

            if (xml == null)
                throw new RuntimeException("Must provide an xmlFile property");

            log.info("Using XML file", xml);

            try {
                String content;

                if (StringUtils.isBlank(xsl)) {
                    RandomAccessFile f = null;

                    try {
                        URL url = XMLDatabase.class.getResource(xml);
                        File file = url != null ? new File(url.toURI()) : new File(xml);

                        f = new RandomAccessFile(file, "r");
                        byte[] bytes = new byte[(int) f.length()];
                        f.readFully(bytes);

                        // [#7414] Default to reading UTF-8
                        content = new String(bytes, "UTF-8");

                        // [#7414] Alternatively, read the encoding from the XML file
                        try {
                            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader( new StringReader(content) );
                            String encoding = reader.getCharacterEncodingScheme();

                            // Returned encoding can be null in the presence of a BOM
                            // See https://stackoverflow.com/a/27147259/521799
                            if (encoding != null && !"UTF-8".equals(encoding))
                                content = new String(bytes, encoding);
                        }
                        catch (XMLStreamException e) {
                            log.warn("Could not open XML Stream: " + e.getMessage());
                        }
                        catch (UnsupportedEncodingException e) {
                            log.warn("Unsupported encoding: " + e.getMessage());
                        }
                    }
                    finally {
                        if (f != null) {
                            try {
                                f.close();
                            }
                            catch (Exception ignore) {}
                        }
                    }
                }
                else {
                    InputStream xmlIs = null;
                    InputStream xslIs = null;

                    try {
                        log.info("Using XSL file", xsl);

                        xmlIs = XMLDatabase.class.getResourceAsStream(xml);
                        if (xmlIs == null)
                            xmlIs = new FileInputStream(xml);

                        xslIs = XMLDatabase.class.getResourceAsStream(xsl);
                        if (xslIs == null)
                            xslIs = new FileInputStream(xsl);

                        StringWriter writer = new StringWriter();
                        TransformerFactory factory = TransformerFactory.newInstance();
                        Transformer transformer = factory.newTransformer(new StreamSource(xslIs));

                        transformer.transform(new StreamSource(xmlIs), new StreamResult(writer));
                        content = writer.getBuffer().toString();
                    }
                    catch (TransformerException e) {
                        throw new RuntimeException("Error while transforming XML file " + xml + " with XSL file " + xsl, e);
                    }
                    finally {
                        if (xmlIs != null) {
                            try {
                                xmlIs.close();
                            }
                            catch (Exception ignore) {}
                        }
                        if (xslIs != null) {
                            try {
                                xslIs.close();
                            }
                            catch (Exception ignore) {}
                        }
                    }
                }

                // TODO [#1201] Add better error handling here
                content = content.replaceAll(
                    "<(\\w+:)?information_schema xmlns(:\\w+)?=\"http://www.jooq.org/xsd/jooq-meta-\\d+\\.\\d+\\.\\d+.xsd\">",
                    "<$1information_schema xmlns$2=\"" + Constants.NS_META + "\">");

                content = content.replace(
                    "<information_schema>",
                    "<information_schema xmlns=\"" + Constants.NS_META + "\">");

                // [#7579] [#8044] Workaround for obscure JAXB bug on JDK 9+
                content = MiniJAXB.jaxbNamespaceBugWorkaround(content, new InformationSchema());

                try {
                    info = JAXB.unmarshal(new StringReader(content), InformationSchema.class);
                }
                catch (Throwable t) {
                    if (ExceptionTools.getCause(t, ClassNotFoundException.class) != null ||
                        ExceptionTools.getCause(t, Error.class) != null) {

                        info = MiniJAXB.unmarshal(content, InformationSchema.class);
                    }
                    else
                        throw t;
                }
            }
            catch (Exception e) {
                throw new RuntimeException("Error while opening files " + xml + " or " + xsl, e);
            }
        }

        return info;
    }

    @Override
    protected DSLContext create0() {
        SQLDialect dialect = SQLDialect.DEFAULT;

        try {
            dialect = SQLDialect.valueOf(getProperties().getProperty("dialect"));
        }
        catch (Exception ignore) {}

        // [#6493] Data types are better discovered from the family, not the dialect. This affects the XMLDatabase,
        //         for instance. Other databases are currently not affected by the family / dialect distinction
        return DSL.using(dialect.family());
    }

    @Override
    protected List<IndexDefinition> getIndexes0() throws SQLException {
        List<IndexDefinition> result = new ArrayList<IndexDefinition>();

        final Map<Name, SortedSet<IndexColumnUsage>> indexColumnUsage = new HashMap<Name, SortedSet<IndexColumnUsage>>();
        for (IndexColumnUsage ic : info().getIndexColumnUsages()) {
            Name name = name(ic.getIndexCatalog(), ic.getIndexSchema(), ic.getTableName(), ic.getIndexName());

            SortedSet<IndexColumnUsage> list = indexColumnUsage.get(name);
            if (list == null) {
                list = new TreeSet<IndexColumnUsage>(new Comparator<IndexColumnUsage>() {
                    @Override
                    public int compare(IndexColumnUsage o1, IndexColumnUsage o2) {
                        int r = 0;

                        r = defaultIfNull(o1.getIndexCatalog(), "").compareTo(defaultIfNull(o2.getIndexCatalog(), ""));
                        if (r != 0)
                            return r;

                        r = defaultIfNull(o1.getIndexSchema(), "").compareTo(defaultIfNull(o2.getIndexSchema(), ""));
                        if (r != 0)
                            return r;

                        r = defaultIfNull(o1.getTableName(), "").compareTo(defaultIfNull(o2.getTableName(), ""));
                        if (r != 0)
                            return r;

                        r = defaultIfNull(o1.getIndexName(), "").compareTo(defaultIfNull(o2.getIndexName(), ""));
                        if (r != 0)
                            return r;

                        return Integer.valueOf(o1.getOrdinalPosition()).compareTo(o2.getOrdinalPosition());
                    }
                });
                indexColumnUsage.put(name, list);
            }

            list.add(ic);
        }

        indexLoop:
        for (Index i : info().getIndexes()) {
            if (getInputSchemata().contains(i.getTableSchema())) {
                final SchemaDefinition schema = getSchema(i.getTableSchema());
                final TableDefinition table = getTable(schema, i.getTableName());

                if (table == null)
                    continue indexLoop;

                final Name name = name(i.getIndexCatalog(), i.getIndexSchema(), i.getTableName(), i.getIndexName());

                IndexDefinition index = new AbstractIndexDefinition(schema, i.getIndexName(), table, Boolean.TRUE.equals(i.isIsUnique()), i.getComment()) {
                    private final List<IndexColumnDefinition> indexColumns;

                    {
                        indexColumns = new ArrayList<IndexColumnDefinition>();
                        SortedSet<IndexColumnUsage> list = indexColumnUsage.get(name);

                        if (list != null)
                            for (IndexColumnUsage ic : list) {
                                ColumnDefinition column = table.getColumn(ic.getColumnName());

                                if (column != null)
                                    indexColumns.add(new DefaultIndexColumnDefinition(
                                        this,
                                        column,
                                        Boolean.TRUE.equals(ic.isIsDescending()) ? SortOrder.DESC : SortOrder.ASC,
                                        ic.getOrdinalPosition()
                                    ));
                                else
                                    log.error(String.format("Column %s not found in table %s.", ic.getColumnName(), table));
                            }
                        else
                            log.error(String.format("No columns found for index %s.", name));
                    }

                    @Override
                    protected List<IndexColumnDefinition> getIndexColumns0() {
                        return indexColumns;
                    }
                };

                result.add(index);
            }
        }

        return result;
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) {
        for (KeyColumnUsage usage : keyColumnUsage(PRIMARY_KEY)) {
            SchemaDefinition schema = getSchema(usage.getConstraintSchema());
            String key = usage.getConstraintName();
            String tableName = usage.getTableName();
            String columnName = usage.getColumnName();

            TableDefinition table = getTable(schema, tableName);
            if (table != null)
                relations.addPrimaryKey(key, table, table.getColumn(columnName));
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) {
        for (KeyColumnUsage usage : keyColumnUsage(UNIQUE)) {
            SchemaDefinition schema = getSchema(usage.getConstraintSchema());
            String key = usage.getConstraintName();
            String tableName = usage.getTableName();
            String columnName = usage.getColumnName();

            TableDefinition table = getTable(schema, tableName);
            if (table != null)
                relations.addUniqueKey(key, table, table.getColumn(columnName));
        }
    }

    private List<KeyColumnUsage> keyColumnUsage(TableConstraintType constraintType) {
        List<KeyColumnUsage> result = new ArrayList<KeyColumnUsage>();

        for (TableConstraint constraint : info().getTableConstraints())
            if (constraintType == constraint.getConstraintType()
                    && getInputSchemata().contains(constraint.getConstraintSchema()))
                for (KeyColumnUsage usage : info().getKeyColumnUsages())
                    if (    StringUtils.equals(defaultIfNull(constraint.getConstraintCatalog(), ""), defaultIfNull(usage.getConstraintCatalog(), ""))
                         && StringUtils.equals(defaultIfNull(constraint.getConstraintSchema(), ""), defaultIfNull(usage.getConstraintSchema(), ""))
                         && StringUtils.equals(defaultIfNull(constraint.getConstraintName(), ""), defaultIfNull(usage.getConstraintName(), "")))

                        result.add(usage);

        Collections.sort(result, new Comparator<KeyColumnUsage>() {
            @Override
            public int compare(KeyColumnUsage o1, KeyColumnUsage o2) {
                int r = 0;

                r = defaultIfNull(o1.getConstraintCatalog(), "").compareTo(defaultIfNull(o2.getConstraintCatalog(), ""));
                if (r != 0)
                    return r;

                r = defaultIfNull(o1.getConstraintSchema(), "").compareTo(defaultIfNull(o2.getConstraintSchema(), ""));
                if (r != 0)
                    return r;

                r = defaultIfNull(o1.getConstraintName(), "").compareTo(defaultIfNull(o2.getConstraintName(), ""));
                if (r != 0)
                    return r;

                return Integer.valueOf(o1.getOrdinalPosition()).compareTo(o2.getOrdinalPosition());
            }
        });

        return result;
    }

    @Override
    protected void loadForeignKeys(DefaultRelations relations) {
        for (ReferentialConstraint fk : info().getReferentialConstraints()) {
            if (getInputSchemata().contains(fk.getConstraintSchema())) {
                for (KeyColumnUsage usage : info().getKeyColumnUsages()) {
                    if (    StringUtils.equals(defaultIfNull(fk.getConstraintCatalog(), ""), defaultIfNull(usage.getConstraintCatalog(), ""))
                         && StringUtils.equals(defaultIfNull(fk.getConstraintSchema(), ""), defaultIfNull(usage.getConstraintSchema(), ""))
                         && StringUtils.equals(defaultIfNull(fk.getConstraintName(), ""), defaultIfNull(usage.getConstraintName(), ""))) {

                        SchemaDefinition foreignKeySchema = getSchema(fk.getConstraintSchema());
                        SchemaDefinition uniqueKeySchema = getSchema(fk.getUniqueConstraintSchema());

                        String foreignKey = usage.getConstraintName();
                        String foreignKeyTableName = usage.getTableName();
                        String foreignKeyColumn = usage.getColumnName();
                        String uniqueKey = fk.getUniqueConstraintName();
                        String uniqueKeyTableName = null;

                        for (TableConstraint uk : info().getTableConstraints()) {
                            if (    StringUtils.equals(defaultIfNull(fk.getUniqueConstraintCatalog(), ""), defaultIfNull(uk.getConstraintCatalog(), ""))
                                 && StringUtils.equals(defaultIfNull(fk.getUniqueConstraintSchema(), ""), defaultIfNull(uk.getConstraintSchema(), ""))
                                 && StringUtils.equals(defaultIfNull(fk.getUniqueConstraintName(), ""), defaultIfNull(uk.getConstraintName(), ""))) {
                                uniqueKeyTableName = uk.getTableName();
                                break;
                            }
                        }

                        TableDefinition foreignKeyTable = getTable(foreignKeySchema, foreignKeyTableName);
                        TableDefinition uniqueKeyTable = getTable(uniqueKeySchema, uniqueKeyTableName);

                        if (foreignKeyTable != null && uniqueKeyTable != null)
                            relations.addForeignKey(
                                foreignKey,
                                foreignKeyTable,
                                foreignKeyTable.getColumn(foreignKeyColumn),
                                uniqueKey,
                                uniqueKeyTable
                            );
                    }
                }
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) {
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        List<CatalogDefinition> result = new ArrayList<CatalogDefinition>();
        result.add(new CatalogDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (Schema schema : info().getSchemata()) {
            result.add(new SchemaDefinition(this, schema.getSchemaName(), schema.getComment()));
        }

        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Sequence sequence : info().getSequences()) {
            if (getInputSchemata().contains(sequence.getSequenceSchema())) {
                SchemaDefinition schema = getSchema(sequence.getSequenceSchema());

                DataTypeDefinition type = new DefaultDataTypeDefinition(
                    this,
                    schema,
                    sequence.getDataType(),
                    sequence.getCharacterMaximumLength(),
                    sequence.getNumericPrecision(),
                    sequence.getNumericScale(),
                    false,
                    (String) null
                );

                result.add(new DefaultSequenceDefinition(schema, sequence.getSequenceName(), type, sequence.getComment()));
            }
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Table table : info().getTables()) {
            if (getInputSchemata().contains(table.getTableSchema())) {
                SchemaDefinition schema = getSchema(table.getTableSchema());

                result.add(new XMLTableDefinition(schema, info(), table, table.getComment()));
            }
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
        return result;
    }

    @Override
    protected List<DomainDefinition> getDomains0() throws SQLException {
        List<DomainDefinition> result = new ArrayList<DomainDefinition>();
        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();
        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Routine routine : info().getRoutines()) {
            if (isBlank(routine.getSpecificPackage()) && isBlank(routine.getRoutinePackage())) {
                String schemaName = defaultIfBlank(routine.getSpecificSchema(), routine.getRoutineSchema());

                if (getInputSchemata().contains(schemaName)) {
                    SchemaDefinition schema = getSchema(schemaName);

                    result.add(new XMLRoutineDefinition(schema, null, info(), routine, routine.getComment()));
                }
            }
        }

        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();

        Set<String> packages = new HashSet<String>();
        for (Routine routine : info().getRoutines()) {
            String schemaName = defaultIfBlank(routine.getSpecificSchema(), routine.getRoutineSchema());

            if (getInputSchemata().contains(schemaName)) {
                SchemaDefinition schema = getSchema(schemaName);
                String packageName = defaultIfBlank(routine.getSpecificPackage(), routine.getRoutinePackage());

                if (!isBlank(packageName) && packages.add(packageName)) {
                    result.add(new XMLPackageDefinition(schema, info(), packageName));
                }
            }
        }

        return result;
    }

    static int unbox(Integer i) {
        return i == null ? 0 : i.intValue();
    }

    static long unbox(Long l) {
        return l == null ? 0L : l.longValue();
    }
}
