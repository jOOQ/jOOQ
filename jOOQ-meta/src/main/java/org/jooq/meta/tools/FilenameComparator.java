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

import java.math.BigInteger;
import java.util.Comparator;
import java.util.regex.Pattern;

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
public final class FilenameComparator implements Comparator<String> {

    // Idea taken from here: https://codereview.stackexchange.com/a/37217/5314
    private static final Pattern           NUMBERS  = Pattern.compile("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
    public static final FilenameComparator INSTANCE = new FilenameComparator();

    @Override
    public final int compare(String o1, String o2) {
        if (o1 == null || o2 == null)
            return o1 == null ? o2 == null ? 0 : -1 : 1;

        String[] split1 = NUMBERS.split(o1);
        String[] split2 = NUMBERS.split(o2);

        for (int i = 0; i < Math.min(split1.length, split2.length); i++) {
            char c1 = split1[i].charAt(0);
            char c2 = split2[i].charAt(0);
            int cmp = 0;

            if (c1 >= '0' && c1 <= '9' && c2 >= 0 && c2 <= '9')
                cmp = new BigInteger(split1[i]).compareTo(new BigInteger(split2[i]));

            if (cmp == 0)
                cmp = split1[i].compareTo(split2[i]);

            if (cmp != 0)
                return cmp;
        }

        return split1.length - split2.length;
    }
}
