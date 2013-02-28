/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.liquibase.test;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.jooq.liquibase.Adapters.table;
import static org.jooq.maven.example.h2.Tables.T_BOOK;
import static org.jooq.maven.example.h2.Tables.V_LIBRARY;

import java.sql.Connection;
import java.sql.DriverManager;

import liquibase.database.core.H2Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.DatabaseSnapshotGeneratorFactory;

import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.Executor;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AdaptersTest {

    private static Connection       connection;
    private static H2Database       database;
    private static DatabaseSnapshot snapshot;
    private static Executor         create;

    @BeforeClass
    public static void before() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:~/liquibase-test", "sa", "");
        database = new H2Database();
        database.setConnection(new JdbcConnection(connection));
        snapshot = DatabaseSnapshotGeneratorFactory.getInstance().createSnapshot(database, "PUBLIC", null);
        create = new Executor(connection, SQLDialect.H2);
    }

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testTable() {
        Table<?> table = table(snapshot.getTable("T_BOOK"));

        testEqualTables(T_BOOK, table);
    }

    @Test
    public void testView() {
        Table<?> table = table(snapshot.getView("V_LIBRARY"));

        testEqualTables(V_LIBRARY, table);
    }

    /**
     * A couple of tests, checking if tables are really equal
     */
    private void testEqualTables(Table<?> t1, Table<?> t2) {
        assertEquals(t1.getName(), t2.getName());
        assertEquals(asList(t1.fields()), asList(t2.fields()));
        assertEquals(asList(t1.fieldsRow().types()), asList(t2.fieldsRow().types()));

        assertEquals(
            create.select().from(t1).orderBy(1).fetch(),
            create.select().from(t2).orderBy(1).fetch());
    }
}
