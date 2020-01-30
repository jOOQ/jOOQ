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
package org.jooq.meta.tools;

import java.io.File;
import java.util.Comparator;

final class FlywayFileComparator implements Comparator<File> {

    public static final FlywayFileComparator INSTANCE = new FlywayFileComparator();

    @Override
    public final int compare(File o1, File o2) {
        String s1 = o1 == null ? null : o1.getName();
        String s2 = o2 == null ? null : o2.getName();

        if (s1 != null && s2 != null) {
            int i1 = s1.indexOf("__");
            int i2 = s2.indexOf("__");

            if (i1 > 1 && i2 > 1) {
                FlywayVersion v1 = FlywayVersion.fromVersion(s1.substring(1, i1));
                FlywayVersion v2 = FlywayVersion.fromVersion(s2.substring(1, i2));

                return v1.compareTo(v2);
            }

            // [#9498] Emulate "repeatable" migrations, see https://flywaydb.org/documentation/migrations#overview
            else if (i1 > 1)
                return -1;
            else if (i2 > 1)
                return 1;
        }

        // In case we're not comparing two Flyway files, fall back to default comparator logic
        return FileComparator.INSTANCE.compare(o1, o2);
    }
}
