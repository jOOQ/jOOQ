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
package org.jooq.meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.jooq.DSLContext;
import org.jooq.Result;

/**
 * A cache for {@link Pattern} instances.
 *
 * @author Lukas Eder
 */
final class Statements {

    private DSLContext                   ctx;
    private final Map<String, Result<?>> sqlCache;
    private final Map<String, Set<?>>    sqlCacheSingleColumnSet;

    Statements() {
        this.sqlCache = new HashMap<>();
        this.sqlCacheSingleColumnSet = new HashMap<>();
    }

    final void dslContext(DSLContext c) {
        this.ctx = c;
    }

    final Result<?> fetch(String sql) {
        Result<?> result = sqlCache.get(sql);

        if (result == null)
            sqlCache.put(sql, result = ctx.fetch(sql));

        return result;
    }

    final Set<?> fetchSet(String sql) {
        Set<?> result = sqlCacheSingleColumnSet.get(sql);

        if (result == null)
            sqlCacheSingleColumnSet.put(sql, result = fetch(sql).intoSet(0));

        return result;
    }
}
