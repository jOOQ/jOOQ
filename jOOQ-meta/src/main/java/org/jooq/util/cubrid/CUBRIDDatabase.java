/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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

package org.jooq.util.cubrid;

import static org.jooq.impl.DSL.concat;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.val;
import static org.jooq.tools.StringUtils.defaultIfNull;
import static org.jooq.util.cubrid.dba.Tables.DB_CLASS;
import static org.jooq.util.cubrid.dba.Tables.DB_INDEX;
import static org.jooq.util.cubrid.dba.Tables.DB_INDEX_KEY;
import static org.jooq.util.cubrid.dba.Tables.DB_SERIAL;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.ConnectionRunnable;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.CatalogDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.DefaultEnumDefinition;
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

/**
 * @author Lukas Eder
 */
public class CUBRIDDatabase extends AbstractDatabase {

    @Override
    protected void loadPrimaryKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(DB_INDEX.IS_UNIQUE.isTrue().and(DB_INDEX.IS_PRIMARY_KEY.isFalse()))) {
            String key = record.get("constraint_name", String.class);
            String tableName = record.get(DB_CLASS.CLASS_NAME);
            String columnName = record.get(DB_INDEX_KEY.KEY_ATTR_NAME);

            TableDefinition table = getTable(getSchemata().get(0), tableName);
            if (table != null) {
                relations.addUniqueKey(key, table.getColumn(columnName));
            }
        }
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations relations) throws SQLException {
        for (Record record : fetchKeys(DB_INDEX.IS_PRIMARY_KEY.isTrue())) {
            String key = record.get("constraint_name", String.class);
            String tableName = record.get(DB_CLASS.CLASS_NAME);
            String columnName = record.get(DB_INDEX_KEY.KEY_ATTR_NAME);

            TableDefinition table = getTable(getSchemata().get(0), tableName);
            if (table != null) {
                relations.addPrimaryKey(key, table.getColumn(columnName));
            }
        }
    }

    private Result<Record3<String, String, String>> fetchKeys(Condition condition) {
        return
        create().select(
                    concat(DB_CLASS.CLASS_NAME, val("__"), DB_INDEX.INDEX_NAME).as("constraint_name"),
                    DB_INDEX_KEY.KEY_ATTR_NAME,
                    DB_CLASS.CLASS_NAME)
                .from(DB_INDEX)
                .join(DB_CLASS).on(DB_INDEX.CLASS_NAME.equal(DB_CLASS.CLASS_NAME))
                .join(DB_INDEX_KEY).on(
                    DB_INDEX_KEY.INDEX_NAME.equal(DB_INDEX.INDEX_NAME).and(
                    DB_INDEX_KEY.CLASS_NAME.equal(DB_INDEX.CLASS_NAME)))
                .where(condition)
                .orderBy(
                    DB_INDEX.INDEX_NAME.asc())
                .fetch();
    }

    @Override
    protected void loadForeignKeys(final DefaultRelations relations) throws SQLException {
        create().connection(new ConnectionRunnable() {
            @Override
            public void run(Connection connection) throws SQLException {
                DatabaseMetaData meta = connection.getMetaData();

                for (String table : create()
                        .selectDistinct(DB_INDEX.CLASS_NAME)
                        .from(DB_INDEX)
                        .where(DB_INDEX.IS_FOREIGN_KEY.isTrue())
                        .fetch(DB_INDEX.CLASS_NAME)) {

                    for (Record record : create().fetch(meta.getImportedKeys(null, null, table))) {
                        String foreignKeyName =
                            record.get("FKTABLE_NAME", String.class) +
                            "__" +
                            record.get("FK_NAME", String.class);
                        String foreignKeyTableName = record.get("FKTABLE_NAME", String.class);
                        String foreignKeyColumnName = record.get("FKCOLUMN_NAME", String.class);
                        String uniqueKeyName =
                            record.get("PKTABLE_NAME", String.class) +
                            "__" +
                            record.get("PK_NAME", String.class);

                        TableDefinition referencingTable = getTable(getSchemata().get(0), foreignKeyTableName);
                        if (referencingTable != null) {
                            ColumnDefinition column = referencingTable.getColumn(foreignKeyColumnName);
                            relations.addForeignKey(foreignKeyName, uniqueKeyName, column, getSchemata().get(0));
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {
        // Currently not supported
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        List<CatalogDefinition> result = new ArrayList<CatalogDefinition>();
        result.add(new CatalogDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        List<SchemaDefinition> result = new ArrayList<SchemaDefinition>();
        result.add(new SchemaDefinition(this, "", ""));
        return result;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        List<SequenceDefinition> result = new ArrayList<SequenceDefinition>();

        for (Record record : create()
                .select(
                    DB_SERIAL.NAME,
                    DB_SERIAL.MAX_VAL)
                .from(DB_SERIAL)
                .fetch()) {

            BigInteger value = defaultIfNull(record.get(DB_SERIAL.MAX_VAL, BigInteger.class), BigInteger.valueOf(Long.MAX_VALUE));
            DataTypeDefinition type = getDataTypeForMAX_VAL(getSchemata().get(0), value);

            result.add(new DefaultSequenceDefinition(
                getSchemata().get(0),
                record.get(DB_SERIAL.NAME),
                type));
        }

        return result;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        List<TableDefinition> result = new ArrayList<TableDefinition>();

        for (Record record : create()
                .select(
                    DB_CLASS.CLASS_NAME)
                .from(DB_CLASS)
                .orderBy(
                    DB_CLASS.CLASS_NAME.asc())
                .fetch()) {

            String name = record.get(DB_CLASS.CLASS_NAME);

            CUBRIDTableDefinition table = new CUBRIDTableDefinition(getSchemata().get(0), name, null);
            result.add(table);
        }

        return result;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        List<EnumDefinition> result = new ArrayList<EnumDefinition>();

        for (TableDefinition tableDefinition : getTables(getSchemata().get(0))) {
            for (Record record : create().fetch("SHOW COLUMNS FROM {0} WHERE TYPE LIKE 'ENUM(%)'", field(name(tableDefinition.getInputName())))) {
                String table = tableDefinition.getInputName();
                String column = record.get("Field", String.class);
                String columnType = record.get("Type", String.class);
                String name = table + "_" + column;

                ColumnDefinition columnDefinition = tableDefinition.getColumn(column);

                // [#1137] Avoid generating enum classes for enum types that
                // are explicitly forced to another type
                if (getConfiguredForcedType(columnDefinition) == null) {
                    DefaultEnumDefinition definition = new DefaultEnumDefinition(getSchemata().get(0), name, "");
                    for (String string : columnType.replaceAll("ENUM\\(|\\)", "").split(",")) {
                        definition.addLiteral(string.trim().replaceAll("'", ""));
                    }

                    result.add(definition);
                }
            }
        }

        return result;
    }

    @Override
    protected List<DomainDefinition> getDomains0() throws SQLException {
        List<DomainDefinition> result = new ArrayList<DomainDefinition>();
        return result;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        List<UDTDefinition> result = new ArrayList<UDTDefinition>();
        return result;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        List<ArrayDefinition> result = new ArrayList<ArrayDefinition>();
        return result;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        List<RoutineDefinition> result = new ArrayList<RoutineDefinition>();
        return result;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        List<PackageDefinition> result = new ArrayList<PackageDefinition>();
        return result;
    }

    @Override
    protected DSLContext create0() {
        return DSL.using(getConnection(), SQLDialect.CUBRID);
    }
}
