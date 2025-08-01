/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

package org.jooq.meta.yugabytedb;

import static org.jooq.impl.DSL.inline;
import static org.jooq.impl.DSL.unquotedName;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record6;
import org.jooq.ResultQuery;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.meta.postgres.PostgresDatabase;

/**
 * @author Lukas Eder
 */
public class YugabyteDBDatabase extends PostgresDatabase {

    boolean connectionInitialised;

    @Override
    protected DSLContext create0() {
        DSLContext ctx = DSL.using(getConnection(), SQLDialect.YUGABYTEDB);

        if (!connectionInitialised && getConnection() != null) {
            connectionInitialised = true;

            // Work around YugabyteDB's distributed catalog, which performs very poorly when using nested loops
            // https://github.com/yugabyte/yugabyte-db/issues/9938
            ctx.set(unquotedName("enable_nestloop"), inline(false)).execute();
        }

        return ctx;
    }

    @Override
    protected Field<String> attgenerated(Field<String> attgenerated) {

        // [#18701] YugabyteDB doesn't support the ATTGENERATED property
        return DSL.inline("s");
    }
}
