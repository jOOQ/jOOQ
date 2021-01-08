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

import static java.util.Collections.emptyList;

import java.util.Collections;

import org.jooq.Commit;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.ContentType;
import org.jooq.File;
import org.jooq.Internal;

import org.jetbrains.annotations.NotNull;

/**
 * The public API entry point for jOOQ migrations.
 */
@Internal
public final class Migrations {

    @Internal
    @NotNull
    public static final Commits commits(Configuration configuration) {
        return new CommitsImpl(configuration, new CommitImpl(configuration, "init", "init", emptyList(), emptyList()));
    }

    @Internal
    @NotNull
    public static final File file(String path, String content, ContentType type) {
        return new FileImpl(path, content, type);
    }
}
