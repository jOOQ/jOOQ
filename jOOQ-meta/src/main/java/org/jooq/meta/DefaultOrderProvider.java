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

import java.util.Comparator;

/**
 * A default order provider that allows for comparing arbitrary definitions.
 * <p>
 * jOOQ's code generator, by default, implements ordering of objects in a
 * reasonable, predictable way, according to these rules:
 * <p>
 * <ul>
 * <li><strong>Alphabetic when irrelevant</strong>:<br/>
 * When the order of objects within a parent is irrelevant, they are ordered
 * alphabetically. Such objects include:
 * <ul>
 * <li>{@link SchemaDefinition} within {@link CatalogDefinition}</li>
 * <li>{@link ArrayDefinition} within {@link SchemaDefinition}</li>
 * <li>{@link DomainDefinition} within {@link SchemaDefinition}</li>
 * <li>{@link EnumDefinition} within {@link SchemaDefinition}</li>
 * <li>{@link PackageDefinition} within {@link SchemaDefinition}</li>
 * <li>{@link RoutineDefinition} within {@link SchemaDefinition}</li>
 * <li>{@link UDTDefinition} within {@link SchemaDefinition}</li>
 * <li>{@link TableDefinition} within {@link SchemaDefinition}</li>
 * <li>{@link ConstraintDefinition} within {@link TableDefinition}</li>
 * <li>{@link IndexDefinition} within {@link TableDefinition}</li>
 * <li>{@link SequenceDefinition} within {@link SchemaDefinition}</li>
 * </ul>
 * </li>
 * <li><strong>In given order when relevant</strong>:<br/>
 * When the order of objects within a parent is relevant, and provided by the
 * database vendor, then that order is used. Such objects include:
 * <ul>
 * <li>{@link AttributeDefinition} within {@link UDTDefinition}</li>
 * <li>{@link ColumnDefinition} within {@link TableDefinition}</li>
 * <li>{@link IndexColumnDefinition} within {@link IndexDefinition}</li>
 * <li>{@link ParameterDefinition} within {@link RoutineDefinition}</li>
 * </ul>
 * </li></li>
 * </ul>
 *
 * @author Lukas Eder
 */
public class DefaultOrderProvider implements Comparator<Definition> {

    @Override
    public int compare(Definition o1, Definition o2) {
        if (o1 instanceof ColumnDefinition && o2 instanceof ColumnDefinition)
            return compare0((ColumnDefinition) o1, (ColumnDefinition) o2);
        else if (o1 instanceof EmbeddableColumnDefinition && o2 instanceof EmbeddableColumnDefinition)
            return compare0((EmbeddableColumnDefinition) o1, (EmbeddableColumnDefinition) o2);
        else if (o1 instanceof AttributeDefinition && o2 instanceof AttributeDefinition)
            return compare0((AttributeDefinition) o1, (AttributeDefinition) o2);
        else if (o1 instanceof IndexColumnDefinition && o2 instanceof IndexColumnDefinition)
            return compare0((IndexColumnDefinition) o1, (IndexColumnDefinition) o2);
        else if (o1 instanceof ParameterDefinition && o2 instanceof ParameterDefinition)
            return compare0((ParameterDefinition) o1, (ParameterDefinition) o2);
        else
            return o1.getQualifiedInputName().compareTo(o2.getQualifiedInputName());
    }

    private int compare0(PositionedDefinition i1, PositionedDefinition i2) {
        return Integer.valueOf(i1.getPosition()).compareTo(i2.getPosition());
    }
}
