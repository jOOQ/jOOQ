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
    public static final String MINOR_VERSION  = "3.13";

    /**
     * The latest jOOQ version.
     * <p>
     * This is the same as {@link #MINOR_VERSION}, but it may include patch
     * version suffixes.
     */
    public static final String VERSION        = "3.13.1-SNAPSHOT";

    /**
     * The latest jOOQ full version.
     * <p>
     * This is the same as {@link #VERSION}, but it may include release
     * candidate and other suffixes.
     */
    public static final String FULL_VERSION   = "3.13.1-SNAPSHOT";

    /**
     * The current jooq-runtime XSD file name.
     */
    public static final String XSD_RUNTIME    = "jooq-runtime-3.13.0.xsd";

    /**
     * The current jooq-runtime XML namespace.
     */
    public static final String NS_RUNTIME     = "http://www.jooq.org/xsd/" + XSD_RUNTIME;

    /**
     * The current jooq-runtime XSD classpath location.
     */
    public static final String CP_RUNTIME     = "/xsd/" + XSD_RUNTIME;

    /**
     * The current jooq-export XSD file name.
     */
    public static final String XSD_EXPORT     = "jooq-export-3.10.0.xsd";

    /**
     * The current jooq-export XML namespace.
     */
    public static final String NS_EXPORT      = "http://www.jooq.org/xsd/" + XSD_EXPORT;

    /**
     * The current jooq-export XSD classpath location.
     */
    public static final String CP_EXPORT      = "/xsd/" + XSD_EXPORT;

    /**
     * The current jooq-meta XSD file name.
     */
    public static final String XSD_META       = "jooq-meta-3.12.0.xsd";

    /**
     * The current jooq-meta XML namespace.
     */
    public static final String NS_META        = "http://www.jooq.org/xsd/" + XSD_META;

    /**
     * The current jooq-meta XSD classpath location.
     */
    public static final String CP_META        = "/xsd/" + XSD_META;

    /**
     * The current jooq-codegen XSD file name.
     */
    public static final String XSD_CODEGEN    = "jooq-codegen-3.13.0.xsd";

    /**
     * The current jooq-codegen XML namespace.
     */
    public static final String NS_CODEGEN     = "http://www.jooq.org/xsd/" + XSD_CODEGEN;

    /**
     * The current jooq-codegen XSD classpath location.
     */
    public static final String CP_CODEGEN     = "/xsd/" + XSD_CODEGEN;



    /**
     * The maximum degree of {@link Row} and {@link Record} subtypes
     */
    public static final int    MAX_ROW_DEGREE = 22;



    /**
     * No further instances
     */
    private Constants() {}
}
