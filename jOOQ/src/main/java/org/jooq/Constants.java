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
package org.jooq;


/**
 * Some publicly available constants used in jOOQ
 *
 * @author Lukas Eder
 */
public final class Constants {

    /**
     * The Maven groupId used for this edition / distribution
     */
    public static final String GROUP_ID       = "org.jooq";

    /**
     * The latest jOOQ minor version.
     */
    public static final String MINOR_VERSION  = "3.19";

    /**
     * The latest jOOQ version.
     * <p>
     * This is the same as {@link #MINOR_VERSION}, but it may include patch
     * version suffixes.
     */
    public static final String VERSION        = "3.19.0-SNAPSHOT";

    /**
     * The latest jOOQ full version.
     * <p>
     * This is the same as {@link #VERSION}, but it may include release
     * candidate and other suffixes.
     */
    public static final String FULL_VERSION   = "3.19.0-SNAPSHOT";

    /**
     * The build date of this version.
     */
    public static final String BUILD_DATE     = "0000-00-00 00:00:00";

    /**
     * The current jooq-runtime XSD file name.
     */
    public static final String XSD_RUNTIME    = "jooq-runtime-3.19.0.xsd";

    /**
     * The current jooq-runtime XML namespace.
     */
    public static final String NS_RUNTIME     = "http://www.jooq.org/xsd/" + XSD_RUNTIME;

    /**
     * The current jooq-runtime XSD classpath location.
     */
    public static final String CP_RUNTIME     = "/org/jooq/xsd/" + XSD_RUNTIME;

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
    public static final String CP_EXPORT      = "/org/jooq/xsd/" + XSD_EXPORT;

    /**
     * The current jooq-meta XSD file name.
     */
    public static final String XSD_META       = "jooq-meta-3.19.0.xsd";

    /**
     * The current jooq-meta XML namespace.
     */
    public static final String NS_META        = "http://www.jooq.org/xsd/" + XSD_META;

    /**
     * The current jooq-meta XSD classpath location.
     */
    public static final String CP_META        = "/org/jooq/xsd/" + XSD_META;

    /**
     * The current jooq-migrations XSD file name.
     */
    public static final String XSD_MIGRATIONS = "jooq-migrations-3.19.0.xsd";

    /**
     * The current jooq-migrations XML namespace.
     */
    public static final String NS_MIGRATIONS  = "http://www.jooq.org/xsd/" + XSD_META;

    /**
     * The current jooq-migrations XSD classpath location.
     */
    public static final String CP_MIGRATIONS  = "/org/jooq/xsd/" + XSD_META;

    /**
     * The current jooq-codegen XSD file name.
     */
    public static final String XSD_CODEGEN    = "jooq-codegen-3.19.0.xsd";

    /**
     * The current jooq-codegen XML namespace.
     */
    public static final String NS_CODEGEN     = "http://www.jooq.org/xsd/" + XSD_CODEGEN;

    /**
     * The current jooq-codegen XSD classpath location.
     */
    public static final String CP_CODEGEN     = "/org/jooq/meta/xsd/" + XSD_CODEGEN;



    /**
     * The maximum degree of {@link org.jooq.Row} and {@link org.jooq.Record} subtypes
     */
    public static final int    MAX_ROW_DEGREE = 22;



    /**
     * The minor release 3.15.
     */
    public static final String VERSION_3_15   = "3.15";

    /**
     * The minor release 3.16.
     */
    public static final String VERSION_3_16   = "3.16";

    /**
     * The minor release 3.17.
     */
    public static final String VERSION_3_17   = "3.17";

    /**
     * The minor release 3.18.
     */
    public static final String VERSION_3_18   = "3.18";

    /**
     * The minor release 3.19.
     */
    public static final String VERSION_3_19   = "3.19";

    /**
     * No further instances
     */
    private Constants() {}
}
