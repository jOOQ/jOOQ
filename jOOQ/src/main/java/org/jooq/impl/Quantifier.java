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
package org.jooq.impl;

import static org.jooq.impl.DSL.all;
import static org.jooq.impl.DSL.any;

import org.jooq.Keyword;
import org.jooq.QuantifiedSelect;
import org.jooq.Record;
import org.jooq.Select;

/**
 * A quantifier used for quantified comparison predicates
 *
 * @author Lukas Eder
 */
enum Quantifier {

    /**
     * The <code>ANY</code> quantifier
     */
    ANY("any"),

    /**
     * The <code>ALL</code> quantifier
     */
    ALL("all"),

    ;

    private final String  sql;
    private final Keyword keyword;

    private Quantifier(String sql) {
        this.sql = sql;
        this.keyword = DSL.keyword(sql);
    }

    public final String toSQL() {
        return sql;
    }

    public final Keyword toKeyword() {
        return keyword;
    }

    public <R extends Record> QuantifiedSelect<R> apply(Select<R> select) {
        switch (this) {
            case ANY:
                return any(select);
            case ALL:
                return all(select);
            default:
                throw new IllegalStateException();
        }
    }
}
