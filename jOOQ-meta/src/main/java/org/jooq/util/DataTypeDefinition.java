/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
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
package org.jooq.util;


/**
 * A definition for a data type object.
 *
 * @author Lukas Eder
 */
public interface DataTypeDefinition {

    /**
     * The dialect-specific column type.
     */
    String getType();

    /**
     * The converter type that is applied to this data type, or
     * <code>null</code>, if no such converter type is configured.
     */
    String getConverter();

    /**
     * The binding type that is applied to this data type, or
     * <code>null</code>, if no such binding type is configured.
     */
    String getBinding();

    /**
     * The type's length.
     */
    int getLength();

    /**
     * The type's precision.
     */
    int getPrecision();

    /**
     * The type's scale.
     */
    int getScale();

    /**
     * The user type, if applicable.
     */
    String getUserType();

    /**
     * Whether this data type is nullable.
     */
    boolean isNullable();

    /**
     * Whether this data type is defaultable.
     */
    boolean isDefaulted();

    /**
     * Whether this data type represents a udt.
     */
    boolean isUDT();

    /**
     * Whether this data type is a NUMBER type without precision and scale.
     */
    boolean isGenericNumberType();

    /**
     * The underlying database.
     */
    Database getDatabase();

    /**
     * The underlying schema.
     */
    SchemaDefinition getSchema();

}
