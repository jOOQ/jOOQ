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

/**
 * A comparator that compares file names semantically, just like the Windows
 * Explorer.
 * <p>
 * <strong>Desired semantic ordering:</strong>
 *
 * <pre>
 * version-1
 * version-2
 * version-10
 * </pre>
 * <p>
 * <strong>Undesired, lexicographic ordering:</strong>
 *
 * <pre>
 * version-1
 * version-10
 * version-2
 * </pre>
 *
 * @author Lukas Eder
 */
public final class FileComparator implements Comparator<File> {

    public static final FileComparator INSTANCE = new FileComparator();

    @Override
    public final int compare(File o1, File o2) {
        String s1 = o1 == null ? null : o1.getName();
        String s2 = o2 == null ? null : o2.getName();

        return FilenameComparator.INSTANCE.compare(s1, s2);
    }
}
