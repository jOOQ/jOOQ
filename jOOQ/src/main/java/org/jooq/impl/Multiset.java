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

import static org.jooq.impl.DSL.array;
import static org.jooq.impl.DSL.jsonArray;
import static org.jooq.impl.DSL.jsonArrayAgg;
import static org.jooq.impl.DSL.jsonObject;
import static org.jooq.impl.DSL.jsonbArray;
import static org.jooq.impl.DSL.jsonbArrayAgg;
import static org.jooq.impl.DSL.jsonbObject;
import static org.jooq.impl.DSL.select;
import static org.jooq.impl.DSL.selectFrom;
import static org.jooq.impl.DSL.xmlagg;
import static org.jooq.impl.DSL.xmlelement;
import static org.jooq.impl.Keywords.K_MULTISET;
import static org.jooq.impl.Names.N_MULTISET;
import static org.jooq.impl.Names.N_RECORD;
import static org.jooq.impl.Names.N_RESULT;
import static org.jooq.impl.Tools.emulateMultiset;
import static org.jooq.impl.Tools.map;
import static org.jooq.impl.Tools.visitSubquery;

import org.jooq.Context;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Select;
import org.jooq.SelectField;
import org.jooq.Table;

/**
 * @author Lukas Eder
 */
final class Multiset<R extends Record> extends AbstractField<Result<R>> {

    final Select<R> select;

    @SuppressWarnings("unchecked")
    Multiset(Select<R> select) {
        super(N_MULTISET, new MultisetDataType<>((AbstractRow<R>) select.fieldsRow(), select.getRecordType()));

        this.select = select;
    }

    @Override
    public final void accept(Context<?> ctx) {
        switch (emulateMultiset(ctx.configuration())) {
            case JSON: {
                switch (ctx.family()) {










                    default:
                        // TODO: Re-apply derived table's ORDER BY clause as aggregate ORDER BY
                        Table<?> t = select.asTable("t");
                        visitSubquery(ctx, select(DSL.coalesce(jsonArrayAgg(jsonObject(t.fields())), jsonArray())).from(t), true);
                        break;
                }

                break;
            }

            case JSONB: {
                switch (ctx.family()) {










                    default:
                        // TODO: Re-apply derived table's ORDER BY clause as aggregate ORDER BY
                        Table<?> t = select.asTable("t");
                        visitSubquery(ctx, select(DSL.coalesce(jsonbArrayAgg(jsonbObject(t.fields())), jsonbArray())).from(t), true);
                        break;
                }

                break;
            }

            case XML: {
                switch (ctx.family()) {










                    default:
                        // TODO: Re-apply derived table's ORDER BY clause as aggregate ORDER BY
                        Table<?> t = select.asTable("t");
                        visitSubquery(ctx,
                            select(
                                xmlelement(N_RESULT,
                                    xmlagg(xmlelement(N_RECORD,
                                        map(t.fields(), f -> xmlelement(f.getUnqualifiedName(), f))
                                    ))
                                )
                            ).from(t),
                            true
                        );
                        break;
                }

                break;
            }

            case NATIVE:
                visitSubquery(ctx.visit(K_MULTISET), select, true);
                break;
        }
    }
}
