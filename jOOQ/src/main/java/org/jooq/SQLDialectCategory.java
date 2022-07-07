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

import static java.util.Collections.unmodifiableSet;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A category for {@link SQLDialect}.
 * <p>
 * Some {@link SQLDialect} families share a common category, as they are all
 * derived from a common ancestor. Such categories can help define behaviour
 * that is specific to <em>all</em> members of the category. For example,
 * <em>all</em> {@link SQLDialect#POSTGRES} related dialects support casts of
 * the form <code>IDENTIFIER::TYPE</code>.
 *
 * @author Lukas Eder
 */
public enum SQLDialectCategory {

    /**
     * The default SQL dialect category.
     * <p>
     * This dialect is chosen in the absence of a more explicit category.
     */
    OTHER,

    // -------------------------------------------------------------------------
    // SQL dialects for free usage
    // -------------------------------------------------------------------------

    /**
     * The MySQL dialect category.
     */
    MYSQL,

    /**
     * The PostgreSQL dialect category.
     */
    POSTGRES,

    // -------------------------------------------------------------------------
    // SQL dialects for commercial usage
    // -------------------------------------------------------------------------











    ;

    private EnumSet<SQLDialect> dialects;
    private EnumSet<SQLDialect> families;

    /**
     * Get all {@link SQLDialect} values belonging to this category.
     */
    public final Set<SQLDialect> dialects() {
        if (dialects == null)
            dialects = filter(SQLDialect.values());

        return unmodifiableSet(dialects);
    }

    /**
     * Get all {@link SQLDialect} families belonging to this category.
     */
    public final Set<SQLDialect> families() {
        if (families == null)
            families = filter(SQLDialect.families());

        return unmodifiableSet(families);
    }

    private final EnumSet<SQLDialect> filter(SQLDialect[] values) {
        EnumSet<SQLDialect> set = EnumSet.noneOf(SQLDialect.class);

        for (SQLDialect family : values)
            if (family.category() == this)
                set.add(family);

        return set;
    }
}
