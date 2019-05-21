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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * A configuration type for use with the various {@link DSLContext#ddl(Catalog)}
 * methods.
 *
 * @author Lukas Eder
 */
public final class DDLExportConfiguration {

    final EnumSet<DDLFlag> flags;

    public DDLExportConfiguration() {
        this(
            EnumSet.allOf(DDLFlag.class)
        );
    }

    private DDLExportConfiguration(
        Set<DDLFlag> flags
    ) {
        this.flags = EnumSet.copyOf(flags);
    }

    public final Set<DDLFlag> flags() {
        return Collections.unmodifiableSet(flags);
    }

    public final DDLExportConfiguration flags(DDLFlag... newFlags) {
        return flags(Arrays.asList(newFlags));
    }

    public final DDLExportConfiguration flags(Collection<DDLFlag> newFlags) {
        return new DDLExportConfiguration(EnumSet.copyOf(newFlags));
    }
}
