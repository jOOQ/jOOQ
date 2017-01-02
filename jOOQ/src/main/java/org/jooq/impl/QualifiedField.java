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
package org.jooq.impl;

import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.Arrays;

import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.RenderContext;
import org.jooq.Table;
import org.jooq.TableField;

/**
 * A <code>QualifiedField</code> is a {@link Field} that always renders a field name
 * or alias as a literal using {@link RenderContext#literal(String)}
 *
 * @author Lukas Eder
 */
final class QualifiedField<T> extends AbstractField<T> implements TableField<Record, T> {

    /**
     * Generated UID
     */
    private static final long   serialVersionUID = 6937002867156868761L;

    private final Name          name;
    private final Table<Record> table;

    QualifiedField(Name name, DataType<T> type) {
        super(defaultIfNull(name.last(), ""), type);

        this.name = name;
        this.table = name.getName().length > 1
            ? DSL.table(DSL.name(Arrays.copyOf(name.getName(), name.getName().length - 1)))
            : null;
    }

    // ------------------------------------------------------------------------
    // Field API
    // ------------------------------------------------------------------------

    @Override
    public final void accept(Context<?> ctx) {
        ctx.visit(name);
    }

    @Override
    public final Table<Record> getTable() {
        return table;
    }
}
