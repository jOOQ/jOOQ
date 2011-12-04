/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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
import java.util.Properties;

import org.jooq.SQLDialect;
import org.jooq.impl.Factory;

/**
 * A general database model.
 *
 * @author Lukas Eder
 */
public interface Database {

    /**
     * The schema generated from this database
     */
    SchemaDefinition getSchema();

    /**
     * Retrieve the schema's primary key / foreign key relations
     */
    Relations getRelations();

    /**
     * The sequences contained in this database (for schema {@link #getSchema()}
     */
    List<SequenceDefinition> getSequences();

    /**
     * The tables contained in this database (for schema {@link #getSchema()})
     */
    List<TableDefinition> getTables();

    /**
     * Get a table in this database by name
     */
    TableDefinition getTable(String name);

    /**
     * The master data tables contained in this database (for schema
     * {@link #getSchema()})
     */
    List<MasterDataTableDefinition> getMasterDataTables();

    /**
     * Get a master data table in this database by name
     */
    MasterDataTableDefinition getMasterDataTable(String name);

    /**
     * The enum UDTs defined in this database
     */
    List<EnumDefinition> getEnums();

    /**
     * Get an enum UDT defined in this database by name
     */
    EnumDefinition getEnum(String name);

    /**
     * The UDTs defined in this database
     */
    List<UDTDefinition> getUDTs();

    /**
     * Get a UDT defined in this database by name
     */
    UDTDefinition getUDT(String name);

    /**
     * The Arrays defined in this database
     */
    List<ArrayDefinition> getArrays();

    /**
     * Get a ARRAY defined in this database by name
     */
    ArrayDefinition getArray(String name);

    /**
     * The stored routines (procedures and functions) contained in this database
     * (for schema {@link #getSchema()})
     */
    List<RoutineDefinition> getRoutines();

    /**
     * The packages contained in this database (for schema {@link #getSchema()})
     */
    List<PackageDefinition> getPackages();

    /**
     * Initialise a connection to this database
     */
    void setConnection(Connection connection);

    /**
     * The database connection
     */
    Connection getConnection();

    /**
     * The input schema is the schema that jooq-meta is reading data from
     */
    void setInputSchema(String schema);

    /**
     * The input schema is the schema that jooq-meta is reading data from
     */
    String getInputSchema();

    /**
     * The output schema is the schema used by jooq-codegen in class names
     */
    void setOutputSchema(String schema);

    /**
     * The output schema is the schema used by jooq-codegen in class names
     */
    String getOutputSchema();

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
     * Database objects matching any of these regular expressions will be
     * generated as master data tables.
     */
    void setMasterDataTableNames(String[] masterDataTables);

    /**
     * Database objects matching any of these regular expressions will be
     * generated as master data tables.
     */
    String[] getMasterDataTableNames();

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
     * Set the configuration properties to this database
     */
    void setProperties(Properties properties);

    /**
     * Get a configuration property from this database
     */
    String getProperty(String property);

    /**
     * Get a configuration property from this database
     */
    List<String> getPropertyNames();

    /**
     * Check whether a type is an array type
     */
    boolean isArrayType(String dataType);

    /**
     * Whether this database supports unsigned types
     */
    boolean supportsUnsignedTypes();
}
