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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jooq.File;
import org.jooq.Files;
import org.jooq.Version;

/**
 * @author Lukas Eder
 */
final class FilesImpl implements Files {

    private final Version    from;
    private final Version    to;
    private final List<File> files;


    FilesImpl(Version from, Version to, Collection<? extends File> f) {
        this.from = from;
        this.to = to;
        this.files = new ArrayList<>(f);
    }

    @Override
    public final Iterator<File> iterator() {
        return files.iterator();
    }

    @Override
    public final Version from() {
        return from;
    }

    @Override
    public final Version to() {
        return to;
    }

    @Override
    public String toString() {
        return "" + files;
    }
}
