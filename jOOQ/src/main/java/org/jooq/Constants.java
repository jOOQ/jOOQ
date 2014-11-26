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
package org.jooq;

/**
 * Some publicly available constants used in jOOQ
 *
 * @author Lukas Eder
 */
public final class Constants {

    /**
     * The latest jOOQ minor version.
     */
    public static final String MINOR_VERSION  = "3.6";

    /**
     * The latest jOOQ version.
     * <p>
     * This is the same as {@link #MINOR_VERSION}, but it may include patch
     * version suffixes.
     */
    public static final String VERSION        = "3.6.0";

    /**
     * The latest jOOQ full version.
     * <p>
     * This is the same as {@link #VERSION}, but it may include release
     * candidate and other suffixes.
     */
    public static final String FULL_VERSION   = "3.6.0";

    /**
     * The current jooq-runtime XSD file name.
     */
    public static final String XSD_RUNTIME    = "jooq-runtime-3.5.0.xsd";

    /**
     * The current jooq-runtime XML namespace
     */
    public static final String NS_RUNTIME     = "http://www.jooq.org/xsd/" + XSD_RUNTIME;

    /**
     * The current jooq-meta XSD file name.
     */
    public static final String XSD_META       = "jooq-meta-3.5.0.xsd";

    /**
     * The current jooq-meta XML namespace.
     */
    public static final String NS_META        = "http://www.jooq.org/xsd/" + XSD_META;

    /**
     * The current jooq-codegen XSD file name.
     */
    public static final String XSD_CODEGEN    = "jooq-codegen-3.5.0.xsd";

    /**
     * The current jooq-codegen XML namespace.
     */
    public static final String NS_CODEGEN     = "http://www.jooq.org/xsd/" + XSD_CODEGEN;

    /**
     * The maximum degree of {@link Row} and {@link Record} subtypes
     */
    public static final int    MAX_ROW_DEGREE = 22;

    /**
     * No further instances
     */
    private Constants() {}
}
