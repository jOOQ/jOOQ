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
package org.jooq.codegen;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A cache for directories and their contents.
 *
 * @author Lukas Eder
 */
public final class Files {

    private final Map<File, String[]> lists;
    private final Set<File> mkdirs;

    public Files() {
        this.lists = new HashMap<>();
        this.mkdirs = new HashSet<>();
    }

    public final String[] list(File dir, FilenameFilter filter) {
        String[] list = lists.get(dir);

        if (list == null) {
            list = dir.list();
            lists.put(dir, list);
        }

        List<String> result = new ArrayList<>();
        for (String s : list)
            if (filter.accept(dir, s))
                result.add(s);

        return result.toArray(new String[0]);
    }

    public final void mkdirs(File dir) {
        if (mkdirs.add(dir))
            dir.mkdirs();
    }
}
