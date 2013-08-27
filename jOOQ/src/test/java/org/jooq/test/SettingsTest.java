/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
 */
package org.jooq.test;

import static org.jooq.test.data.Table1.TABLE1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.MappedTable;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;
import org.jooq.impl.DSL;
import org.jooq.impl.SchemaImpl;
import org.jooq.impl.TableImpl;

import org.junit.Before;
import org.junit.Test;

/**
 * Some common tests related to {@link Settings}
 *
 * @author Lukas Eder
 */
public class SettingsTest {

    private Settings settings;

    @Before
    public void setUp() throws Exception {
        settings = SettingsTools.defaultSettings();
    }

    @Test
    public void testDefaultSettings() {
        Settings settings2 = SettingsTools.defaultSettings();
        settings.setAttachRecords(false);

        // Check that the above change to the default settings has no effect
        // on the clone
        assertTrue(settings2.isAttachRecords());
        assertTrue(SettingsTools.defaultSettings().isAttachRecords());
        assertFalse(settings.isAttachRecords());
    }

    @Test
    public void testRenderSchema() {
        Schema schema = new SchemaImpl("S");
        Table<?> table = new TableImpl<Record>("T", schema);

        DSLContext create0 = DSL.using(SQLDialect.ORACLE);
        assertEquals("\"S\".\"T\"", create0.render(table));

        DSLContext create1 = DSL.using(SQLDialect.ORACLE, new Settings().withRenderSchema(false));
        assertEquals("\"T\"", create1.render(table));

        DSLContext create2 = DSL.using(SQLDialect.ORACLE);
        create2.configuration().settings().setRenderSchema(false);
        assertEquals("\"T\"", create2.render(table));
    }

    @Test
    public void testRenderMapping() {
        DSLContext create1 = DSL.using(SQLDialect.ORACLE, new Settings().withRenderMapping(mapping()));
        assertEquals("\"TABLEX\"", create1.render(TABLE1));

        DSLContext create2 = DSL.using(SQLDialect.ORACLE);
        create2.configuration().settings().setRenderMapping(mapping());
        assertEquals("\"TABLEX\"", create2.render(TABLE1));
    }

    private RenderMapping mapping() {
        return new RenderMapping().withSchemata(
                   new MappedSchema().withInput("").withTables(
                       new MappedTable().withInput("TABLE1").withOutput("TABLEX")
                   )
               );
    }

    @Test
    public void testCloneable() {
        Settings settings1 = new Settings();
        Settings settings2 = SettingsTools.clone(settings1);

        assertEquals(settings1.isAttachRecords(), settings2.isAttachRecords());
    }
}
