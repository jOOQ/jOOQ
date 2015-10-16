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

package org.jooq.test;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.Settings;
import org.jooq.conf.SettingsTools;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;


/**
 * @author Lukas Eder
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MySQLTestSchemaMapping extends MySQLTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();

        create().query("use " + TAuthor().getSchema().getName() + getSchemaSuffix()).execute();
    }

    @Override
    protected String getSchemaSuffix() {
        return "2";
    }

    @Override
    protected DSLContext create0(Settings settings) {
        settings = (settings != null) ? settings : new Settings();
        RenderMapping mapping = SettingsTools.getRenderMapping(settings);
        List<MappedSchema> schemata = mapping.getSchemata();

        if (schemata.size() == 0) {
            schemata.add(new MappedSchema()
                .withInput(TAuthor().getSchema().getName())
                .withOutput(TAuthor().getSchema().getName() + getSchemaSuffix()));
        }
        else {
            schemata.get(0)
                .withInput(TAuthor().getSchema().getName())
                .withOutput(TAuthor().getSchema().getName() + getSchemaSuffix());
        }

        return super.create0(settings);
    }
}