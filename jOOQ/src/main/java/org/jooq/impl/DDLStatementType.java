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
package org.jooq.impl;

import org.jetbrains.annotations.*;


/**
 * @author Lukas Eder
 */
enum DDLStatementType {
    ALTER_DATABASE,
    ALTER_DOMAIN,
    ALTER_INDEX,
    ALTER_SCHEMA,
    ALTER_SEQUENCE,
    ALTER_TABLE,
    ALTER_VIEW,

    CREATE_DATABASE,
    CREATE_DOMAIN,
    CREATE_INDEX,
    CREATE_SCHEMA,
    CREATE_SEQUENCE,
    CREATE_TABLE,
    CREATE_VIEW,

    DROP_DATABASE,
    DROP_DOMAIN,
    DROP_INDEX,
    DROP_SCHEMA,
    DROP_SEQUENCE,
    DROP_TABLE,
    DROP_VIEW,
}
