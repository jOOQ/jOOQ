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
package org.jooq.meta;

import org.jooq.SortOrder;

/**
 * @author Lukas Eder
 */
public class DefaultIndexColumnDefinition
    extends AbstractTypedElementDefinition<IndexDefinition>
    implements IndexColumnDefinition {

    private final ColumnDefinition column;
    private final SortOrder        sortOrder;
    private final int              position;

    public DefaultIndexColumnDefinition(IndexDefinition container, ColumnDefinition column, SortOrder sortOrder, int position) {
        super(container, column.getInputName(), position, column.getDefinedType(), "");

        this.column = column;
        this.sortOrder = sortOrder;
        this.position = position;
    }

    @Override
    public DataTypeDefinition getType() {
        return getColumn().getType();
    }

    @Override
    public DataTypeDefinition getType(JavaTypeResolver resolver) {
        return getColumn().getType(resolver);
    }

    @Override
    public DataTypeDefinition getDefinedType() {
        return getColumn().getDefinedType();
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public SortOrder getSortOrder() {
        return sortOrder;
    }

    @Override
    public ColumnDefinition getColumn() {
        return column;
    }
}
