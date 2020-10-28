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

import org.jooq.ContentType;
import org.jooq.File;

/**
 * @author Lukas Eder
 */
class FileImpl implements File {

    private final String      path;
    private final String      name;
    private final String      content;
    private final ContentType type;

    FileImpl(String path, String content, ContentType type) {
        this.path = path;
        this.content = content;
        this.type = type;
        this.name = path.substring(path.lastIndexOf('/') + 1);
    }

    @Override
    public final String path() {
        return path;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final String content() {
        return content;
    }

    @Override
    public final ContentType type() {
        return type;
    }

    @Override
    public String toString() {
        return "-- " + type + ": " + path + "\n" + content;
    }
}
