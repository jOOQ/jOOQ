/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */

package org.jooq.util.oracle;

import static org.jooq.impl.DSL.connectByRoot;
import static org.jooq.impl.DSL.falseCondition;
import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.prior;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectOne;
import static org.jooq.impl.DSL.substring;
import static org.jooq.impl.DSL.sysConnectByPath;
import static org.jooq.impl.DSL.val;
import static org.jooq.util.oracle.sys.Tables.ALL_COLL_TYPES;
import static org.jooq.util.oracle.sys.Tables.ALL_CONSTRAINTS;
import static org.jooq.util.oracle.sys.Tables.ALL_CONS_COLUMNS;
import static org.jooq.util.oracle.sys.Tables.ALL_DB_LINKS;
import static org.jooq.util.oracle.sys.Tables.ALL_MVIEW_COMMENTS;
import static org.jooq.util.oracle.sys.Tables.ALL_OBJECTS;
import static org.jooq.util.oracle.sys.Tables.ALL_PROCEDURES;
import static org.jooq.util.oracle.sys.Tables.ALL_QUEUES;
import static org.jooq.util.oracle.sys.Tables.ALL_QUEUE_TABLES;
import static org.jooq.util.oracle.sys.Tables.ALL_SEQUENCES;
import static org.jooq.util.oracle.sys.Tables.ALL_SYNONYMS;
import static org.jooq.util.oracle.sys.Tables.ALL_TAB_COMMENTS;
import static org.jooq.util.oracle.sys.Tables.ALL_TYPES;
import static org.jooq.util.oracle.sys.Tables.ALL_USERS;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.tools.JooqLogger;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultArrayDefinition;
import org.jooq.util.DefaultCheckConstraintDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;
import org.jooq.util.oracle.sys.tables.AllConstraints;
import org.jooq.util.oracle.sys.tables.AllObjects;
import org.jooq.util.oracle.sys.tables.AllSynonyms;

/**
 * @author Lukas Eder
 */
public class OracleDatabase extends AbstractDatabase {

    private static final JooqLogger                                      log = JooqLogger.getLogger(OracleDatabase.class);

    private List<OracleQueueDefinition>                                  queues;
    private transient Map<SchemaDefinition, List<OracleQueueDefinition>> queuesBySchema;
    private List<OracleLinkDefinition>                                   links;
    private Map<Name, Name>                                              synonyms;

    private static Boolean                                               is10g;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("P")) {
            SchemaDefinition schema = getSchema(record.getValue(ALL_CONS_COLUMNS.OWNER));
            String key = record.getValue(ALL_CONS_COLUMNS.CONSTRAINT_NAME);
            String tableName = record.getValue(ALL_CONS_COLUMNS.TABLE_NAME);
            String columnName = record.getValue(ALL_CONS_COLUMNS.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys("U")) {
            SchemaDefinition schema = getSchema(record.getValue(ALL_CONS_COLUMNS.OWNER));
            String key = record.getValue(ALL_CONS_COLUMNS.CONSTRAINT_NAME);
            String tableName = record.getValue(ALL_CONS_COLUMNS.TABLE_NAME);
            String columnName = record.getValue(ALL_CONS_COLUMNS.COLUMN_NAME);

            TableDefinition table = getTable(schema, tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    private Result<Record4<String, String, String, String>> fetchKeys(String constraintType) {
        return create().select(
                ALL_CONS_COLUMNS.OWNER,
                ALL_CONS_COLUMNS.CONSTRAINT_NAME,
                ALL_CONS_COLUMNS.TABLE_NAME,
                ALL_CONS_COLUMNS.COLUMN_NAME)
            .from(ALL_CONS_COLUMNS
                .join(ALL_CONSTRAINTS)
                .on(ALL_CONS_COLUMNS.OWNER.equal(ALL_CONSTRAINTS.OWNER))
                .and(ALL_CONS_COLUMNS.CONSTRAINT_NAME.equal(ALL_CONSTRAINTS.CONSTRAINT_NAME)))
            .where(ALL_CONSTRAINTS.CONSTRAINT_TYPE.equal(constraintType)
                .and(ALL_CONSTRAINTS.CONSTRAINT_NAME.notLike("BIN$%"))
                .and(ALL_CONS_COLUMNS.OWNER.upper().in(getInputSchemata())))
            .orderBy(
                ALL_CONS_COLUMNS.OWNER,
                ALL_CONS_COLUMNS.CONSTRAINT_NAME,
                ALL_CONS_COLUMNS.POSITION)
            .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadForeignKeys(DefaultRelations relations) throws SQLException {
        for (Record record : create().select(
                    ALL_CONS_COLUMNS.OWNER,
                    ALL_CONS_COLUMNS.CONSTRAINT_NAME,
                    ALL_CONS_COLUMNS.TABLE_NAME,
                    ALL_CONS_COLUMNS.COLUMN_NAME,
                    ALL_CONSTRAINTS.R_CONSTRAINT_NAME,
                    ALL_CONSTRAINTS.R_OWNER)
                .from(ALL_CONSTRAINTS
                    .join(ALL_CONS_COLUMNS)
                    .on(ALL_CONSTRAINTS.OWNER.equal(ALL_CONS_COLUMNS.OWNER))
                    .and(ALL_CONSTRAINTS.TABLE_NAME.equal(ALL_CONS_COLUMNS.TABLE_NAME))
                    .and(ALL_CONSTRAINTS.CONSTRAINT_NAME.equal(ALL_CONS_COLUMNS.CONSTRAINT_NAME)))
                .where(ALL_CONSTRAINTS.CONSTRAINT_TYPE.equal("R"))
                .and(ALL_CONSTRAINTS.OWNER.upper().in(getInputSchemata()))
                .orderBy(
                    ALL_CONS_COLUMNS.OWNER,
                    ALL_CONS_COLUMNS.TABLE_NAME,
                    ALL_CONS_COLUMNS.CONSTRAINT_NAME,
                    ALL_CONS_COLUMNS.POSITION)
                .fetch()) {

            SchemaDefinition foreignKeySchema = getSchema(record.getValue(ALL_CONS_COLUMNS.OWNER));
            SchemaDefinition uniqueKeySchema = getSchema(record.getValue(ALL_CONSTRAINTS.R_OWNER));

            String foreignKeyName = record.getValue(ALL_CONS_COLUMNS.CONSTRAINT_NAME);
            String foreignKeyTableName = record.getValue(ALL_CONS_COLUMNS.TABLE_NAME);
            String foreignKeyColumnName = record.getValue(ALL_CONS_COLUMNS.COLUMN_NAME);
            String uniqueKeyName = record.getValue(ALL_CONSTRAINTS.R_CONSTRAINT_NAME);

            TableDefinition referencingTable = getTable(foreignKeySchema, foreignKeyTableName);
            if (referencingTable != null) {
                ColumnDefinition column = referencingTable.getColumn(foreignKeyColumnName);
                relations.addForeignKey(foreignKeyName, uniqueKeyName, column, uniqueKeySchema);
            }
        }
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations relations) throws SQLException {
        AllConstraints ac = ALL_CONSTRAINTS.as("ac");

        for (Record record : create()
                .select(
                    ac.OWNER,
                    ac.TABLE_NAME,
                    ac.CONSTRAINT_NAME,
                    ac.SEARCH_CONDITION
                )
                .from(ac)
                .where(ac.CONSTRAINT_TYPE.eq("C"))
                .and(ac.OWNER.upper().in(getInputSchemata()))
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ac.OWNER));
            TableDefinition table = getTable(schema, record.getValue(ac.TABLE_NAME));

            if (table != null) {
                relations.addCheckConstraint(table, new DefaultCheckConstraintDefinition(
                    schema,
                    table,
                    record.getValue(ac.CONSTRAINT_NAME),
                    record.getValue(ac.SEARCH_CONDITION)
                ));
            }
        }
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();

        for (String name : create()
                .selectDistinct(ALL_USERS.USERNAME)
                .from(ALL_USERS)
                .fetch(ALL_USERS.USERNAME)) {

            result.add(new SchemaDefinition(this, name, ""));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create().select(
                    ALL_SEQUENCES.SEQUENCE_OWNER,
                    ALL_SEQUENCES.SEQUENCE_NAME,
                    ALL_SEQUENCES.MAX_VALUE.nvl(BigDecimal.valueOf(Long.MAX_VALUE)))
                .from(ALL_SEQUENCES)
                .where(ALL_SEQUENCES.SEQUENCE_OWNER.upper().in(getInputSchemata()))
                .orderBy(
                    ALL_SEQUENCES.SEQUENCE_OWNER,
                    ALL_SEQUENCES.SEQUENCE_NAME)
                .fetch()) {


            SchemaDefinition schema = getSchema(record.getValue(ALL_SEQUENCES.SEQUENCE_OWNER));
            BigInteger value = record.getValue(ALL_SEQUENCES.MAX_VALUE.nvl(BigDecimal.valueOf(Long.MAX_VALUE)), BigInteger.class);
            DataTypeDefinition type = getDataTypeForMAX_VAL(schema, value);

            result.add(new DefaultSequenceDefinition(
                schema,
                record.getValue(ALL_SEQUENCES.SEQUENCE_NAME),
                type));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create()
                .select()
                .from(
                     select(
                        ALL_TAB_COMMENTS.OWNER,
                        ALL_TAB_COMMENTS.TABLE_NAME,
                        ALL_TAB_COMMENTS.COMMENTS)
                    .from(ALL_TAB_COMMENTS)
                    .where(ALL_TAB_COMMENTS.OWNER.upper().in(getInputSchemata()))
                    .and(ALL_TAB_COMMENTS.TABLE_NAME.notLike("%$%"))
                .unionAll(
                    is10g()

                    ?    select(
                            ALL_MVIEW_COMMENTS.OWNER,
                            ALL_MVIEW_COMMENTS.MVIEW_NAME,
                            ALL_MVIEW_COMMENTS.COMMENTS)
                        .from(ALL_MVIEW_COMMENTS)
                        .where(ALL_MVIEW_COMMENTS.OWNER.upper().in(getInputSchemata()))
                        .and(ALL_MVIEW_COMMENTS.MVIEW_NAME.notLike("%$%"))

                    :    select(val(""), val(""), val(""))
                        .where(falseCondition())))
                .orderBy(1, 2)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_TAB_COMMENTS.OWNER));
            String name = record.getValue(ALL_TAB_COMMENTS.TABLE_NAME);
            String comment = record.getValue(ALL_TAB_COMMENTS.COMMENTS);

            OracleTableDefinition table = new OracleTableDefinition(schema, name, comment);
            result.add(table);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();

        for (Record record : create().selectDistinct(
                ALL_TYPES.OWNER,
                ALL_TYPES.TYPE_NAME)
            .from(ALL_TYPES)
            .where(ALL_TYPES.OWNER.upper().in(getInputSchemata()))
            .and(ALL_TYPES.TYPECODE.in("OBJECT", "XMLTYPE"))
            .orderBy(
                ALL_TYPES.OWNER,
                ALL_TYPES.TYPE_NAME)
            .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_TYPES.OWNER));
            String name = record.getValue(ALL_TYPES.TYPE_NAME);

            result.add(new OracleUDTDefinition(schema, name, null));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> arrays = new ArrayList<ArrayDefinition>();

        for (Record record : create().select(
                ALL_COLL_TYPES.OWNER,
                ALL_COLL_TYPES.TYPE_NAME,
                ALL_COLL_TYPES.ELEM_TYPE_OWNER,
                ALL_COLL_TYPES.ELEM_TYPE_NAME,
                ALL_COLL_TYPES.LENGTH.nvl(BigDecimal.ZERO),
                ALL_COLL_TYPES.PRECISION.nvl(BigDecimal.ZERO),
                ALL_COLL_TYPES.SCALE.nvl(BigDecimal.ZERO))
            .from(ALL_COLL_TYPES)
            .where(ALL_COLL_TYPES.OWNER.upper().in(getInputSchemata()))
            .and(ALL_COLL_TYPES.COLL_TYPE.in("VARYING ARRAY", "TABLE"))
            .orderBy(
                ALL_COLL_TYPES.OWNER,
                ALL_COLL_TYPES.TYPE_NAME)
            .fetch()) {

            // [#3552] Check if the reported type is really a synonym for another type
            TypeInfo info = getTypeInfo(
                getSchema(record.getValue(ALL_COLL_TYPES.OWNER)),
                record.getValue(ALL_COLL_TYPES.ELEM_TYPE_OWNER),
                record.getValue(ALL_COLL_TYPES.ELEM_TYPE_NAME));

            SchemaDefinition schema = getSchema(record.getValue(ALL_COLL_TYPES.OWNER));

            String name = record.getValue(ALL_COLL_TYPES.TYPE_NAME);
            String dataType = record.getValue(ALL_COLL_TYPES.ELEM_TYPE_NAME);

            int length = record.getValue(ALL_COLL_TYPES.LENGTH.nvl(BigDecimal.ZERO)).intValue();
            int precision = record.getValue(ALL_COLL_TYPES.PRECISION.nvl(BigDecimal.ZERO)).intValue();
            int scale = record.getValue(ALL_COLL_TYPES.SCALE.nvl(BigDecimal.ZERO)).intValue();

            DefaultDataTypeDefinition type = new DefaultDataTypeDefinition(this, info.schema, dataType, length, precision, scale, null, null, info.name);
            DefaultArrayDefinition array = new DefaultArrayDefinition(schema, name, type);

            arrays.add(array);
        }

        return arrays;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();

        for (Record record : create().select(
                    ALL_OBJECTS.OWNER,
                    ALL_OBJECTS.OBJECT_NAME,
                    ALL_OBJECTS.OBJECT_ID,
                    ALL_PROCEDURES.AGGREGATE)
                .from(ALL_OBJECTS)
                .leftOuterJoin(ALL_PROCEDURES)
                    .on(ALL_OBJECTS.OWNER.equal(ALL_PROCEDURES.OWNER))
                // [#4224] ALL_PROCEDURES.OBJECT_ID didn't exist in 10g
                //         The join predicate doesn't seem to be required, as
                //         Procedures are unique by (OWNER, OBJECT_NAME)
                //  .and(ALL_OBJECTS.OBJECT_ID.equal(ALL_PROCEDURES.OBJECT_ID))
                    .and(ALL_OBJECTS.OBJECT_NAME.equal(ALL_PROCEDURES.OBJECT_NAME))
                .where(ALL_OBJECTS.OWNER.upper().in(getInputSchemata())
                    .and(ALL_OBJECTS.OBJECT_TYPE.in("FUNCTION", "PROCEDURE")))
                .orderBy(
                    ALL_OBJECTS.OWNER,
                    ALL_OBJECTS.OBJECT_NAME,
                    ALL_OBJECTS.OBJECT_ID)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_OBJECTS.OWNER));
            String objectName = record.getValue(ALL_OBJECTS.OBJECT_NAME);
            BigDecimal objectId = record.getValue(ALL_OBJECTS.OBJECT_ID);
            boolean aggregate = record.getValue(ALL_PROCEDURES.AGGREGATE, boolean.class);

            result.add(new OracleRoutineDefinition(schema, null, objectName, "", objectId, null, aggregate));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();

        for (Record record : create().select(
                    ALL_OBJECTS.OWNER,
        		    ALL_OBJECTS.OBJECT_NAME,
        		    ALL_OBJECTS.OBJECT_ID)
                .from(ALL_OBJECTS)
                .where(ALL_OBJECTS.OWNER.upper().in(getInputSchemata())
                .and(ALL_OBJECTS.OBJECT_TYPE.equal("PACKAGE")))
                .orderBy(
                    ALL_OBJECTS.OWNER,
                    ALL_OBJECTS.OBJECT_NAME,
                    ALL_OBJECTS.OBJECT_ID)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_OBJECTS.OWNER));
            String name = record.getValue(ALL_OBJECTS.OBJECT_NAME);

            result.add(new OraclePackageDefinition(schema, name, ""));
        }

        return result;
    }

    protected List<OracleQueueDefinition> getQueues0() {
        List<OracleQueueDefinition> result = new ArrayList<OracleQueueDefinition>();

        for (Record record : create().select(
                    ALL_QUEUES.OWNER,
                    ALL_QUEUES.NAME,
                    ALL_QUEUES.USER_COMMENT,
                    ALL_QUEUE_TABLES.OBJECT_TYPE
                )
                .from(ALL_QUEUES)
                .join(ALL_QUEUE_TABLES)
                .on(ALL_QUEUES.OWNER.eq(ALL_QUEUE_TABLES.OWNER))
                .and(ALL_QUEUES.QUEUE_TABLE.eq(ALL_QUEUE_TABLES.QUEUE_TABLE))
                .where(ALL_QUEUES.OWNER.upper().in(getInputSchemata()))
                .and(ALL_QUEUE_TABLES.TYPE.eq("OBJECT"))
                .orderBy(
                    ALL_QUEUES.OWNER,
                    ALL_QUEUES.NAME)
                .fetch()) {

            SchemaDefinition schema = getSchema(record.getValue(ALL_QUEUES.OWNER));
            String name = record.getValue(ALL_QUEUES.NAME);
            String comment = record.getValue(ALL_QUEUES.USER_COMMENT);
            String objectType = record.getValue(ALL_QUEUE_TABLES.OBJECT_TYPE);
            UDTDefinition udt = null;

            if (objectType != null && objectType.contains(".")) {
                String[] split = objectType.split("\\.");
                SchemaDefinition udtSchema = getSchema(split[0]);
                udt = getUDT(udtSchema, split[1], true);
            }
            else {
                log.info("Invalid UDT", objectType);
            }

            result.add(new OracleQueueDefinition(schema, name, comment, udt));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.ORACLE);
    }

    boolean is10g() {
        if (is10g == null) {

            // [#2864] The ALL_MVIEW_COMMENTS view was introduced in Oracle 10g
            try {
                create().selectCount()
                        .from(ALL_MVIEW_COMMENTS)
                        .fetch();

                is10g = true;
            }
            catch (DataAccessException e) {
                is10g = false;
            }
        }

        return is10g;
    }

    // --------------------------------------------------------------------------------------------
    // Oracle-specific API
    // --------------------------------------------------------------------------------------------

    public final List<OracleLinkDefinition> getLinks(SchemaDefinition schema) {
        if (links == null) {
            links = new ArrayList<OracleLinkDefinition>();

            for (Record2<String, String> link : create()
                .select(
                    ALL_DB_LINKS.OWNER,
                    ALL_DB_LINKS.DB_LINK
                )
                .from(ALL_DB_LINKS)
                .orderBy(
                    ALL_DB_LINKS.OWNER,
                    ALL_DB_LINKS.DB_LINK
                )) {

                // Check if the db link is really available to this connection
                // It might not be, for a variety of reasons
                try {
                    create().selectOne()
                            .from(ALL_DB_LINKS.at(link.value2()))
                            .where(falseCondition())
                            .fetch();

                    SchemaDefinition linkSchema = getSchema(link.value1());
                    if (linkSchema != null) {
                        links.add(new OracleLinkDefinition(schema, link.value2()));
                    }
                }
                catch (DataAccessException e) {
                    log.info("Error while accessing dblink", e.getMessage());
                }
            }
        }

        return filterSchema(links, schema);
    }

    public final List<OracleQueueDefinition> getQueues(SchemaDefinition schema) {
        if (queues == null) {
            queues = new ArrayList<OracleQueueDefinition>();

            try {
                List<OracleQueueDefinition> u = getQueues0();

                queues = filterExcludeInclude(u);
                log.info("Queues fetched", fetchedSize(u, queues));
            } catch (Exception e) {
                log.error("Error while fetching queues", e);
            }
        }

        if (queuesBySchema == null) {
            queuesBySchema = new LinkedHashMap<SchemaDefinition, List<OracleQueueDefinition>>();
        }

        return filterSchema(queues, schema, queuesBySchema);
    }

    public final OracleQueueDefinition getQueue(SchemaDefinition schema, String name) {
        return getQueue(schema, name, false);
    }

    public final OracleQueueDefinition getQueue(SchemaDefinition schema, String name, boolean ignoreCase) {
        return getDefinition(getQueues(schema), name, ignoreCase);
    }

    final Name getSynonym(Name object) {
        if (synonyms == null) {
            synonyms = new LinkedHashMap<Name, Name>();

            AllObjects o = ALL_OBJECTS;
            AllSynonyms s1 = ALL_SYNONYMS;
            AllSynonyms s2 = ALL_SYNONYMS.as("s2");
            AllSynonyms s3 = ALL_SYNONYMS.as("s3");

            Field<String> dot = inline(".");
            String arr = " <- ";

            for (Record5<String, String, String, String, String> record : create()
                    .select(
                        s3.OWNER,
                        s3.SYNONYM_NAME,
                        connectByRoot(s3.TABLE_OWNER).as(s3.TABLE_OWNER),
                        connectByRoot(s3.TABLE_NAME).as(s3.TABLE_NAME),
                        substring(sysConnectByPath(s3.TABLE_OWNER.concat(dot).concat(s3.TABLE_NAME), arr).concat(inline(arr)).concat(s3.OWNER).concat(dot).concat(s3.SYNONYM_NAME), inline(5)))
                    .from(
                        select()
                        .from(
                            select(s1.OWNER, s1.SYNONYM_NAME, s1.TABLE_OWNER, s1.TABLE_NAME)
                            .from(s1)

                            // If a PUBLIC SYNONYM is referenced, unfortunately, it doesn't generate
                            // TABLE_OWNER = 'PUBLIC' in the ALL_SYNONYMS table. This simple trick will
                            // duplicate all SYNONYMs as they might potentially be PUBLIC
                            .union(
                            select(s1.OWNER, s1.SYNONYM_NAME, inline("PUBLIC"), s1.TABLE_NAME)
                            .from(s1))
                            .asTable("s2")
                        )

                        // Avoid dangling self-references from invalid SYNONYMs
                        .where(row(s2.OWNER, s2.SYNONYM_NAME).ne(s2.TABLE_OWNER, s2.TABLE_NAME))
                        .asTable("s3")
                    )
                    .connectBy(s3.TABLE_OWNER.eq(prior(s3.OWNER)))
                    .and(s3.TABLE_NAME.eq(prior(s3.SYNONYM_NAME)))

                    // Only consider SYNONYM hierarchies that end in a non-SYNONYM object from our
                    // input schemata
                    .startWith(DSL.exists(
                        selectOne()
                        .from(o)
                        .where(s3.TABLE_OWNER.eq(o.OWNER))
                        .and(s3.TABLE_NAME.eq(o.OBJECT_NAME))
                        .and(o.OBJECT_TYPE.ne(inline("SYNONYM")))
                        .and(o.OWNER.in(getInputSchemata()))
                    ))
                    .fetch()) {

                synonyms.put(name(record.value1(), record.value2()), name(record.value3(), record.value4()));
            }
        }

        return synonyms.get(object);
    }

    /**
     * Information associated with a type.
     */
    class TypeInfo {
        SchemaDefinition schema;
        String name;
    }

    /**
     * Resolve a type synonym.
     */
    TypeInfo getTypeInfo(SchemaDefinition typeSchema, String typeOwner, String typeName) {
        TypeInfo result = new TypeInfo();

        // [#3711] Check if the reported type is really a synonym for another type
        if (typeOwner != null) {
            Name synonym = getSynonym(name(typeOwner, typeName));

            if (synonym != null) {
                log.info("Applying synonym", DSL.name(typeOwner, typeName) + " is synonym for " + synonym);

                typeOwner = synonym.getName()[0];
                typeName = synonym.getName()[1];
            }
        }

        if (typeOwner != null)
            typeSchema = getSchema(typeOwner);

        result.name = typeName;
        result.schema = typeSchema;

        return result;
    }
}
