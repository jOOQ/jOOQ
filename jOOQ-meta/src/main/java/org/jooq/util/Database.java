/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.jooq.util;

import java.sql.Connection;
import java.util.List;

import org.jooq.SQLDialect;
import org.jooq.impl.Factory;
import org.jooq.util.jaxb.CustomType;
import org.jooq.util.jaxb.EnumType;
import org.jooq.util.jaxb.ForcedType;
import org.jooq.util.jaxb.MasterDataTable;
import org.jooq.util.jaxb.Schema;

/**
 * A general database model.
 *
 * @author Lukas Eder
 */
public interface Database {

    /**
     * The schemata generated from this database
     */
    List<SchemaDefinition> getSchemata();

    /**
     * Get a schema defined in this database by name
     */
    SchemaDefinition getSchema(String name);

    /**
     * Retrieve the schema's primary key / foreign key relations
     */
    Relations getRelations();

    /**
     * The sequences contained in this database
     */
    List<SequenceDefinition> getSequences(SchemaDefinition schema);

    /**
     * The tables contained in this database
     */
    List<TableDefinition> getTables(SchemaDefinition schema);

    /**
     * Get a table in this database by name
     */
    TableDefinition getTable(SchemaDefinition schema, String name);

    /**
     * Get a table in this database by name
     */
    TableDefinition getTable(SchemaDefinition schema, String name, boolean ignoreCase);

    /**
     * The master data tables contained in this database (for schema
     * {@link #getSchema(String)})
     */
    List<MasterDataTableDefinition> getMasterDataTables(SchemaDefinition schema);

    /**
     * Get a master data table in this database by name
     */
    MasterDataTableDefinition getMasterDataTable(SchemaDefinition schema, String name);

    /**
     * Get a master data table in this database by name
     */
    MasterDataTableDefinition getMasterDataTable(SchemaDefinition schema, String name, boolean ignoreCase);

    /**
     * The enum UDTs defined in this database
     */
    List<EnumDefinition> getEnums(SchemaDefinition schema);

    /**
     * Get an enum UDT defined in this database by name
     */
    EnumDefinition getEnum(SchemaDefinition schema, String name);

    /**
     * Get an enum UDT defined in this database by name
     */
    EnumDefinition getEnum(SchemaDefinition schema, String name, boolean ignoreCase);

    /**
     * The UDTs defined in this database
     */
    List<UDTDefinition> getUDTs(SchemaDefinition schema);

    /**
     * Get a UDT defined in this database by name
     */
    UDTDefinition getUDT(SchemaDefinition schema, String name);

    /**
     * Get a UDT defined in this database by name
     */
    UDTDefinition getUDT(SchemaDefinition schema, String name, boolean ignoreCase);

    /**
     * The Arrays defined in this database
     */
    List<ArrayDefinition> getArrays(SchemaDefinition schema);

    /**
     * Get a ARRAY defined in this database by name
     */
    ArrayDefinition getArray(SchemaDefinition schema, String name);

    /**
     * Get a ARRAY defined in this database by name
     */
    ArrayDefinition getArray(SchemaDefinition schema, String name, boolean ignoreCase);

    /**
     * The stored routines (procedures and functions) contained in this database
     */
    List<RoutineDefinition> getRoutines(SchemaDefinition schema);

    /**
     * The packages contained in this database
     */
    List<PackageDefinition> getPackages(SchemaDefinition schema);

    /**
     * Initialise a connection to this database
     */
    void setConnection(Connection connection);

    /**
     * The database connection
     */
    Connection getConnection();

    /**
     * The input schemata are the schemata that jooq-meta is reading data from
     */
    List<String> getInputSchemata();

    /**
     * The output schema is the schema used by jooq-codegen in class names
     *
     * @deprecated - 2.0.5 - This will be implemented in each {@link Definition#getOutputName()}
     */
    @Deprecated
    String getOutputSchema(String inputSchema);

    /**
     * The input and output schemata
     */
    void setConfiguredSchemata(List<Schema> schemata);

    /**
     * Only database objects matching any of these regular expressions will be
     * generated.
     */
    void setIncludes(String[] includes);

    /**
     * Only database objects matching any of these regular expressions will be
     * generated.
     */
    String[] getIncludes();

    /**
     * Database objects matching any of these table names will be generated as
     * master data tables.
     */
    void setConfiguredMasterDataTables(List<MasterDataTable> tables);

    /**
     * Database objects matching any of these table names will be generated as
     * master data tables.
     */
    List<MasterDataTable> getConfiguredMasterDataTables();

    /**
     * Database objects matching any of these field names will be generated as
     * custom types
     */
    void setConfiguredCustomTypes(List<CustomType> types);

    /**
     * Database objects matching any of these field names will be generated as
     * custom types
     */
    List<CustomType> getConfiguredCustomTypes();

    /**
     * Get a specific configured custom type by its name
     */
    CustomType getConfiguredCustomType(String name);

    /**
     * Database objects matching any of these field names will be generated as
     * enum types
     */
    void setConfiguredEnumTypes(List<EnumType> types);

    /**
     * Database objects matching any of these field names will be generated as
     * enum types
     */
    List<EnumType> getConfiguredEnumTypes();

    /**
     * Database objects matching any of these field names will be generated as
     * forced types
     */
    void setConfiguredForcedTypes(List<ForcedType> types);

    /**
     * Database objects matching any of these field names will be generated as
     * forced types
     */
    List<ForcedType> getConfiguredForcedTypes();

    /**
     * Get the configured forced type object for any given {@link Definition},
     * or <code>null</code> if no {@link ForcedType} matches the definition.
     */
    ForcedType getConfiguredForcedType(Definition definition);

    /**
     * Database objects matching any of these regular expressions will not be
     * generated.
     */
    void setExcludes(String[] excludes);

    /**
     * Database objects matching any of these regular expressions will not be
     * generated.
     */
    String[] getExcludes();

    /**
     * Get the dialect for this database
     */
    SQLDialect getDialect();

    /**
     * Create the factory for this database
     */
    Factory create();

    /**
     * Check whether a type is an array type
     */
    boolean isArrayType(String dataType);

    /**
     * Whether this database supports unsigned types
     */
    void setSupportsUnsignedTypes(boolean supportsUnsignedTypes);

    /**
     * Whether this database supports unsigned types
     */
    boolean supportsUnsignedTypes();

    /**
     * Whether DATE columns should be treated as TIMESTAMP columns
     */
    void setDateAsTimestamp(boolean dateAsTimestamp);

    /**
     * Whether DATE columns should be treated as TIMESTAMP columns
     */
    boolean dateAsTimestamp();
}
