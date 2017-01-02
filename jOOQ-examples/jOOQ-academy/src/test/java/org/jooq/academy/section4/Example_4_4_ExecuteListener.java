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
 */
package org.jooq.academy.section4;

import static org.jooq.academy.tools.Tools.connection;
import static org.jooq.example.db.h2.Tables.AUTHOR;

import org.jooq.ExecuteListener;
import org.jooq.SQLDialect;
import org.jooq.academy.tools.Tools;
import org.jooq.impl.CallbackExecuteListener;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultConnectionProvider;
import org.jooq.impl.DefaultExecuteListenerProvider;

import org.junit.Test;

public class Example_4_4_ExecuteListener {

    @Test
    public void run() {
        Tools.title("Displaying execution time using a custom ExecuteListener");

        ExecuteListener listener = new CallbackExecuteListener()
            .onStart(ctx -> {

                // Register the start time to the current context
                ctx.data("time", System.nanoTime());
            })
            .onEnd(ctx -> {
                // Extract the start time from the current context
                Long time = (Long) ctx.data("time");

                System.out.println("Execution time : " + ((System.nanoTime() - time) / 1000 / 1000.0) + "ms. Query : " + ctx.sql());
            });

        DSL.using(new DefaultConfiguration()
               .set(SQLDialect.H2)
               .set(new DefaultConnectionProvider(connection()))
               .set(new DefaultExecuteListenerProvider(listener))
           )
           .select(AUTHOR.ID)
           .from(AUTHOR)
           .fetch();
    }
}
