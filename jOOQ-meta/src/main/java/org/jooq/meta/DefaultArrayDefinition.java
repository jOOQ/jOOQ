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

public class DefaultArrayDefinition extends AbstractDefinition implements ArrayDefinition {

    private final DataTypeDefinition     definedType;
    private final DataTypeDefinition     definedIndexType;
    private transient DataTypeDefinition type;
    private transient DataTypeDefinition indexType;
    private transient DataTypeDefinition resolvedType;
    private transient DataTypeDefinition resolvedIndexType;

    public DefaultArrayDefinition(SchemaDefinition schema, String name, DataTypeDefinition type) {
        this(schema, null, name, type);
    }

    public DefaultArrayDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, DataTypeDefinition type) {
        this(schema, pkg, name, type, null);
    }

    public DefaultArrayDefinition(SchemaDefinition schema, PackageDefinition pkg, String name, DataTypeDefinition type, DataTypeDefinition indexType) {
        super(schema.getDatabase(), schema, pkg, name, "", null);

        this.definedType = type;
        this.definedIndexType = indexType;
    }

    @Override
    public DataTypeDefinition getElementType() {
        if (type == null)
            type = AbstractTypedElementDefinition.mapDefinedType(this, this, definedType, null);

        return type;
    }

    @Override
    public DataTypeDefinition getElementType(JavaTypeResolver resolver) {
        if (resolvedType == null)
            resolvedType = AbstractTypedElementDefinition.mapDefinedType(this, this, definedType, resolver);

        return resolvedType;
    }

    @Override
    public DataTypeDefinition getIndexType() {
        if (indexType == null && definedIndexType != null)
            indexType = AbstractTypedElementDefinition.mapDefinedType(this, this, definedIndexType, null);

        return indexType;
    }

    @Override
    public DataTypeDefinition getIndexType(JavaTypeResolver resolver) {
        if (resolvedIndexType == null && definedIndexType != null)
            resolvedIndexType = AbstractTypedElementDefinition.mapDefinedType(this, this, definedIndexType, resolver);

        return resolvedIndexType;
    }
}
