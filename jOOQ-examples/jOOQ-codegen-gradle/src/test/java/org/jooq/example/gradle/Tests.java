/*
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
package org.jooq.example.gradle;

import static org.jooq.example.gradle.db.information_schema.Tables.*;

import org.junit.Assert;
import org.junit.Test;

import org.jooq.impl.DSL;

import java.util.Arrays;

/**
 * Tests verifying that the gradle build worked correctly.
 *
 * @author Lukas Eder
 */
public class Tests {

    @Test
    public void testInformationSchema() throws Exception{
        Class.forName("org.h2.Driver");

        Assert.assertEquals(
            Arrays.asList("COLUMNS", "TABLES"),
            DSL.using("jdbc:h2:~/test-gradle", "sa", "")
               .select(TABLES.TABLE_NAME)
               .from(TABLES)
               .where(TABLES.TABLE_NAME.in("COLUMNS", "TABLES"))
               .and(TABLES.TABLE_SCHEMA.eq("INFORMATION_SCHEMA"))
               .fetch(TABLES.TABLE_NAME)
        );
    }
}
