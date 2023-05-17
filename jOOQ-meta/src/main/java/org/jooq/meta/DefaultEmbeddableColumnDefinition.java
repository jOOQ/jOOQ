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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

/**
 * @author Lukas Eder
 */
public class DefaultEmbeddableColumnDefinition
    extends AbstractTypedElementDefinition<EmbeddableDefinition>
    implements EmbeddableColumnDefinition {

    private final ColumnDefinition referencingColumn;
    private DataTypeDefinition     mergedType;
    private final int              position;

    public DefaultEmbeddableColumnDefinition(EmbeddableDefinition container, String definingColumnName, ColumnDefinition referencingColumn, int position) {
        super(container, definingColumnName, position, referencingColumn.getDefinedType(), referencingColumn.getComment());

        this.referencingColumn = referencingColumn;
        this.mergedType = referencingColumn.getDefinedType();
        this.position = position;
    }

    @Override
    public final int getPosition() {
        return position;
    }

    @Override
    public final ColumnDefinition getReferencingColumn() {
        return referencingColumn;
    }

    private DataTypeDefinition merge(DataTypeDefinition original, DataTypeDefinition other) {
        if (!original.isNullable() && other.isNullable())
            return new DefaultDataTypeDefinition(
                original.getDatabase(),
                original.getSchema(),
                original.getType(),
                original.getLength(),
                original.getPrecision(),
                original.getScale(),
                original.isNullable() || other.isNullable(),
                original.isReadonly(),
                original.getGeneratedAlwaysAs(),
                original.getDefaultValue(),
                original.isIdentity(),
                original.getQualifiedUserType(),
                original.getGenerator(),
                original.getConverter(),
                original.getBinding(),
                original.getJavaType()
            );

        return original;
    }

    @Override
    public final void merge(EmbeddableColumnDefinition other) {
        this.mergedType = merge(mergedType, other.getType());
    }

    @Override
    public final int getReferencingColumnPosition() {
        return getReferencingColumn().getPosition();
    }

    @Override
    public DataTypeDefinition getType() {
        return merge(getReferencingColumn().getType(), mergedType);
    }

    @Override
    public DataTypeDefinition getType(JavaTypeResolver resolver) {
        return merge(getReferencingColumn().getType(resolver), mergedType);
    }

    @Override
    public DataTypeDefinition getDefinedType() {
        return merge(getReferencingColumn().getDefinedType(), mergedType);
    }

    @Override
    public String toString() {
        return super.toString() + " (referenced by " + getReferencingColumn() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        if (!(obj instanceof EmbeddableColumnDefinition))
            return false;

        EmbeddableColumnDefinition other = (EmbeddableColumnDefinition) obj;
        return getReferencingColumn().equals(other.getReferencingColumn());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
