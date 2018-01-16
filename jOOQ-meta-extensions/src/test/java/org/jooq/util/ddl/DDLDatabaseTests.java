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
package org.jooq.util.ddl;

import static java.util.Comparator.comparing;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.jooq.util.Definition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.jaxb.Schema;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


/**
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DDLDatabaseTests {

    public DDLDatabase database(String scripts) {
        DDLDatabase database = new DDLDatabase();
        Properties properties = new Properties();
        properties.setProperty("scripts", scripts);
        database.setProperties(properties);
        return database;
    }

    @Test
    public void testQualifiedTables() {
        DDLDatabase database = database("/qualified-tables.sql");
        database.setConfiguredSchemata(Arrays.asList(new Schema().withInputSchema("S1"), new Schema().withInputSchema("S2"), new Schema().withInputSchema("s3"), new Schema().withInputSchema("S4")));

        List<SchemaDefinition> schemata = database.getSchemata();
        schemata.sort(comparing(Definition::getName, String::compareToIgnoreCase));
        assertEquals(4, schemata.size());
        SchemaDefinition s1 = schemata.get(0);
        SchemaDefinition s2 = schemata.get(1);
        SchemaDefinition s3 = schemata.get(2);
        SchemaDefinition s4 = schemata.get(3);

        assertEquals("S1", s1.getInputName());
        assertEquals("S2", s2.getInputName());
        assertEquals("s3", s3.getInputName());
        assertEquals("S4", s4.getInputName());

        List<TableDefinition> s1Tables = database.getTables(s1);
        assertEquals(3, s1Tables.size());
        TableDefinition s1t = s1Tables.get(0);
        TableDefinition s1u = s1Tables.get(1);
        TableDefinition s1v = s1Tables.get(2);

        assertEquals("T", s1t.getInputName());
        assertEquals("U", s1u.getInputName());
        assertEquals("V", s1v.getInputName());
        assertEquals(1, s1t.getColumns().size());
        assertEquals(1, s1u.getColumns().size());
        assertEquals(1, s1v.getColumns().size());
        assertEquals("A", s1t.getColumns().get(0).getInputName());
        assertEquals("B", s1u.getColumns().get(0).getInputName());
        assertEquals("C", s1v.getColumns().get(0).getInputName());

        List<TableDefinition> s2Tables = database.getTables(s2);
        assertEquals(1, s2Tables.size());
        TableDefinition s2t = s2Tables.get(0);
        assertEquals("T", s2t.getInputName());
        assertEquals(1, s2t.getColumns().size());
        assertEquals("A", s2t.getColumns().get(0).getInputName());

        List<TableDefinition> s3Tables = database.getTables(s3);
        assertEquals(1, s3Tables.size());
        TableDefinition s3t = s3Tables.get(0);
        assertEquals("T", s3t.getInputName());
        assertEquals(1, s2t.getColumns().size());
        assertEquals("A", s2t.getColumns().get(0).getInputName());

        List<TableDefinition> s4Tables = database.getTables(s4);
        assertEquals(1, s4Tables.size());
        TableDefinition s4t = s4Tables.get(0);
        assertEquals("T", s4t.getInputName());
        assertEquals(1, s2t.getColumns().size());
        assertEquals("A", s2t.getColumns().get(0).getInputName());
    }
}

