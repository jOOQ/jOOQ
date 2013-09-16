/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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

        DSLContext create0 = DSL.using(SQLDialect.POSTGRES);
        assertEquals("\"S\".\"T\"", create0.render(table));

        DSLContext create1 = DSL.using(SQLDialect.POSTGRES, new Settings().withRenderSchema(false));
        assertEquals("\"T\"", create1.render(table));

        DSLContext create2 = DSL.using(SQLDialect.POSTGRES);
        create2.configuration().settings().setRenderSchema(false);
        assertEquals("\"T\"", create2.render(table));
    }

    @Test
    public void testRenderMapping() {
        DSLContext create1 = DSL.using(SQLDialect.POSTGRES, new Settings().withRenderMapping(mapping()));
        assertEquals("\"TABLEX\"", create1.render(TABLE1));

        DSLContext create2 = DSL.using(SQLDialect.POSTGRES);
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
