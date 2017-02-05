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
 */

package org.jooq.util.xml;

import static org.jooq.tools.StringUtils.defaultIfBlank;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.tools.StringUtils.isBlank;
import static org.jooq.util.xml.jaxb.TableConstraintType.PRIMARY_KEY;
import static org.jooq.util.xml.jaxb.TableConstraintType.UNIQUE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXB;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.CatalogDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.DomainDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
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

    /**
     * The property name for the XML file
     */
    public static final String      P_XML_FILE = "xml-file";

    /**
     * The property name for the XSL file that pre-processes the XML file
     */
    public static final String      P_XSL_FILE = "xsl-file";

    /**
     * The property name for the dialect name
     */
    public static final String      P_DIALECT  = "dialect";

    InformationSchema               info;

    private InformationSchema info() {
        if (info == null) {
            String xml = getProperties().getProperty(P_XML_FILE);
            String xsl = getProperties().getProperty(P_XSL_FILE);

            InputStream xmlIs = null;
            InputStream xslIs = null;

            log.info("Using XML file", xml);

            try {
                xmlIs = XMLDatabase.class.getResourceAsStream(xml);
                if (xmlIs == null)
                    xmlIs = new FileInputStream(xml);

                if (StringUtils.isBlank(xsl)) {
                    info = JAXB.unmarshal(new File(xml), InformationSchema.class);
                }
                else {
                    log.info("Using XSL file", xsl);

                    xslIs = XMLDatabase.class.getResourceAsStream(xsl);
                    if (xslIs == null)
                        xslIs = new FileInputStream(xsl);

                    try {
                        StringWriter writer = new StringWriter();
                        TransformerFactory factory = TransformerFactory.newInstance();
                        Transformer transformer = factory.newTransformer(new StreamSource(xslIs));

                        transformer.transform(new StreamSource(xmlIs), new StreamResult(writer));
                        info = JAXB.unmarshal(new StringReader(writer.getBuffer().toString()), InformationSchema.class);
                    }
                    catch (TransformerException e) {
                        throw new RuntimeException("Error while transforming XML file " + xml + " with XSL file " + xsl, e);
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException("Error while opening files " + xml + " or " + xsl, e);
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

        return info;
    }

    @Override
    protected DSLContext create0() {
        SQLDialect dialect = SQLDialect.DEFAULT;

        try {
            dialect = SQLDialect.valueOf(getProperties().getProperty(P_DIALECT));
        }
        catch (Exception ignore) {}

        return DSL.using(dialect);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) {
        for (KeyColumnUsage usage : keyColumnUsage(PRIMARY_KEY)) {
            SchemaDefinition schema = getSchema(usage.getConstraintSchema());
            String key = usage.getConstraintName();
            String tableName = usage.getTableName();
            String columnName = usage.getColumnName();

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
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
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    private List<KeyColumnUsage> keyColumnUsage(TableConstraintType constraintType) {
        List<KeyColumnUsage> result = new ArrayList<KeyColumnUsage>();

        for (TableConstraint constraint : info().getTableConstraints()) {
            if (constraintType == constraint.getConstraintType()
                    && getInputSchemata().contains(constraint.getConstraintSchema())) {

                for (KeyColumnUsage usage : info().getKeyColumnUsages()) {
                    if (    StringUtils.equals(constraint.getConstraintCatalog(), usage.getConstraintCatalog())
                         && StringUtils.equals(constraint.getConstraintSchema(), usage.getConstraintSchema())
                         && StringUtils.equals(constraint.getConstraintName(), usage.getConstraintName())) {

                        result.add(usage);
                    }
                }
            }
        }

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
        for (ReferentialConstraint constraint : info().getReferentialConstraints()) {
            if (getInputSchemata().contains(constraint.getConstraintSchema())) {

                for (KeyColumnUsage usage : info().getKeyColumnUsages()) {
                    if (    StringUtils.equals(constraint.getConstraintCatalog(), usage.getConstraintCatalog())
                         && StringUtils.equals(constraint.getConstraintSchema(), usage.getConstraintSchema())
                         && StringUtils.equals(constraint.getConstraintName(), usage.getConstraintName())) {

                        SchemaDefinition foreignKeySchema = getSchema(constraint.getConstraintSchema());
                        SchemaDefinition uniqueKeySchema = getSchema(constraint.getUniqueConstraintSchema());

                        String foreignKey = usage.getConstraintName();
                        String foreignKeyTable = usage.getTableName();
                        String foreignKeyColumn = usage.getColumnName();
                        String uniqueKey = constraint.getUniqueConstraintName();

                        TableDefinition referencingTable = getTable(foreignKeySchema, foreignKeyTable);

                        if (referencingTable != null) {
                            ColumnDefinition referencingColumn = referencingTable.getColumn(foreignKeyColumn);
                            relations.addForeignKey(foreignKey, uniqueKey, referencingColumn, uniqueKeySchema);
                        }
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
            result.add(new SchemaDefinition(this, schema.getSchemaName(), null));
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

                result.add(new DefaultSequenceDefinition(schema, sequence.getSequenceName(), type));
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

                result.add(new XMLTableDefinition(schema, info(), table));
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

                    result.add(new XMLRoutineDefinition(schema, null, info(), routine));
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
