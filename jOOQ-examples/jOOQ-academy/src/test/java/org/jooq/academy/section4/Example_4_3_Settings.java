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
package org.jooq.academy.section4;

import static java.lang.System.out;
import static org.jooq.SQLDialect.H2;
import static org.jooq.example.db.h2.Tables.AUTHOR;
import static org.jooq.impl.DSL.using;

import org.jooq.Select;
import org.jooq.academy.tools.Tools;
import org.jooq.conf.MappedSchema;
import org.jooq.conf.MappedTable;
import org.jooq.conf.RenderKeywordStyle;
import org.jooq.conf.RenderMapping;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import org.junit.Test;

public class Example_4_3_Settings {

    @Test
    public void run() {
        Select<?> select =
        DSL.select()
           .from(AUTHOR)
           .where(AUTHOR.ID.eq(3));

        Tools.title("A couple of settings at work - Formatting");
        out.println(using(H2, new Settings().withRenderFormatted(false)).render(select));
        out.println(using(H2, new Settings().withRenderFormatted(true)).render(select));

        Tools.title("A couple of settings at work - Schema");
        out.println(using(H2, new Settings().withRenderSchema(false)).render(select));
        out.println(using(H2, new Settings().withRenderSchema(true)).render(select));

        Tools.title("A couple of settings at work - Name style");
        out.println(using(H2, new Settings().withRenderNameStyle(RenderNameStyle.AS_IS)).render(select));
        out.println(using(H2, new Settings().withRenderNameStyle(RenderNameStyle.UPPER)).render(select));
        out.println(using(H2, new Settings().withRenderNameStyle(RenderNameStyle.LOWER)).render(select));
        out.println(using(H2, new Settings().withRenderNameStyle(RenderNameStyle.QUOTED)).render(select));

        Tools.title("A couple of settings at work - Keyword style");
        out.println(using(H2, new Settings().withRenderKeywordStyle(RenderKeywordStyle.UPPER)).render(select));
        out.println(using(H2, new Settings().withRenderKeywordStyle(RenderKeywordStyle.LOWER)).render(select));

        Tools.title("A couple of settings at work - Mapping");
        out.println(using(H2, new Settings()
            .withRenderMapping(new RenderMapping()
                .withSchemata(new MappedSchema()
                    .withInput("PUBLIC")
                    .withOutput("test")
                    .withTables(new MappedTable()
                        .withInput("AUTHOR")
                        .withOutput("test-author"))
                )
            )).render(select));

    }
}
