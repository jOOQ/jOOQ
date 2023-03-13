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
package org.jooq.impl;

// ...
// ...
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.DERBY;
// ...
import static org.jooq.SQLDialect.FIREBIRD;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
import static org.jooq.SQLDialect.IGNITE;
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
// ...
import static org.jooq.SQLDialect.SQLITE;
// ...
// ...
// ...
import static org.jooq.SQLDialect.TRINO;
// ...
import static org.jooq.SQLDialect.YUGABYTEDB;
import static org.jooq.conf.Transformation.WHEN_NEEDED;
import static org.jooq.impl.Tools.selectQueryImpl;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.util.Set;
import java.util.function.Predicate;

import org.jooq.Configuration;
import org.jooq.QueryPart;
import org.jooq.SQLDialect;
import org.jooq.conf.Transformation;

/**
 * Utilities related to SQL transformations.
 *
 * @author Lukas Eder
 */
final class Transformations {

    static final Set<SQLDialect> NO_SUPPORT_IN_LIMIT              = SQLDialect.supportedBy(MARIADB, MYSQL);
    static final Set<SQLDialect> SUPPORT_MISSING_TABLE_REFERENCES = SQLDialect.supportedBy();
    static final Set<SQLDialect> EMULATE_QUALIFY                  = SQLDialect.supportedBy(CUBRID, FIREBIRD, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB);
    static final Set<SQLDialect> EMULATE_ROWNUM                   = SQLDialect.supportedBy(CUBRID, DERBY, FIREBIRD, HSQLDB, IGNITE, MARIADB, MYSQL, POSTGRES, SQLITE, TRINO, YUGABYTEDB);
    static final Set<SQLDialect> EMULATE_GROUP_BY_COLUMN_INDEX    = SQLDialect.supportedBy(CUBRID, DERBY, H2, HSQLDB, IGNITE);
    static final Set<SQLDialect> NO_SUPPORT_CTE                   = SQLDialect.supportedUntil(CUBRID, DERBY);

    static final SelectQueryImpl<?> subqueryWithLimit(QueryPart source) {
        SelectQueryImpl<?> s;
        return (s = selectQueryImpl(source)) != null && s.getLimit().isApplicable() ? s : null;
    }

    static final boolean transformInConditionSubqueryWithLimitToDerivedTable(Configuration configuration) {
        return transform(
            configuration,
            "Settings.transformInConditionSubqueryWithLimitToDerivedTable",
            configuration.settings().getTransformInConditionSubqueryWithLimitToDerivedTable(),
            c -> NO_SUPPORT_IN_LIMIT.contains(c.dialect())
        );
    }

    static final boolean transformInlineCTE(Configuration configuration) {
        return transform(
            configuration,
            "Settings.transformInlineCTE",
            configuration.settings().getTransformInlineCTE(),
            c -> NO_SUPPORT_CTE.contains(c.dialect())
        );
    }

    static final boolean transformQualify(Configuration configuration) {
        return transform(
            configuration,
            "Settings.transformQualify",
            configuration.settings().getTransformQualify(),
            c -> EMULATE_QUALIFY.contains(c.dialect())
        );
    }

    static final boolean transformRownum(Configuration configuration) {
        return transform(
            configuration,
            "Settings.transformRownum",
            configuration.settings().getTransformRownum(),
            c -> EMULATE_ROWNUM.contains(c.dialect())
        );
    }

    static final boolean transformAppendMissingTableReferences(Configuration configuration) {
        return transform(
            configuration,
            "Settings.transformAppendMissingTableReferences",
            configuration.settings().getParseAppendMissingTableReferences(),
            c -> SUPPORT_MISSING_TABLE_REFERENCES.contains(c.settings().getParseDialect())
        );
    }

    static final boolean transformGroupByColumnIndex(Configuration configuration) {
        return transform(
            configuration,
            "Settings.transformGroupByColumnIndex",
            configuration.settings().getTransformGroupByColumnIndex(),
            c -> EMULATE_GROUP_BY_COLUMN_INDEX.contains(c.dialect())
        );
    }

    /**
     * Check whether a given SQL transformation needs to be applied.
     */
    static final boolean transform(
        Configuration configuration,
        String label,
        Transformation transformation,
        Predicate<? super Configuration> whenNeeded
    ) {
        boolean result;

        switch (defaultIfNull(transformation, WHEN_NEEDED)) {
            case NEVER:
                result = false;
                break;
            case ALWAYS:
                result = true;
                break;
            case WHEN_NEEDED:
                result = whenNeeded.test(configuration);
                break;
            default:
                throw new IllegalStateException("Transformation configuration not supported: " + transformation);
        }

        return result && configuration.requireCommercial(() -> "SQL transformation " + label + " required. SQL transformations are a commercial only feature. Please consider upgrading to the jOOQ Professional Edition or jOOQ Enterprise Edition.");
    }
}
