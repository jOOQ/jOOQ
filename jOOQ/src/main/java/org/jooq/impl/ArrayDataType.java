/*
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.impl;

import org.jooq.Configuration;
import org.jooq.DataType;

/**
 * A wrapper for anonymous array data types
 *
 * @author Lukas Eder
 */
final class ArrayDataType<T> extends DefaultDataType<T[]> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 7883229760246533448L;

    private final DataType<T> elementType;

    public ArrayDataType(DataType<T> elementType) {
        super(null, elementType.getArrayType(), elementType.getTypeName(), elementType.getCastTypeName());

        this.elementType = elementType;
    }

    @Override
    public final String getTypeName(Configuration configuration) {
        String typeName = elementType.getTypeName(configuration);
        return getArrayType(configuration, typeName);
    }

    @Override
    public final String getCastTypeName(Configuration configuration) {
        String castTypeName = elementType.getCastTypeName(configuration);
        return getArrayType(configuration, castTypeName);
    }

    private static String getArrayType(Configuration configuration, String dataType) {
        switch (configuration.family()) {
            case HSQLDB:
                return dataType + " array";
            case POSTGRES:
                return dataType + "[]";
            case H2:
                return "array";

            // Default implementation is needed for hash-codes and toString()
            default:
                return dataType + "[]";
        }
    }
}
