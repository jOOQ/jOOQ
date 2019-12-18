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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jooq.Version;
import org.jooq.Versions;

/**
 * @author Lukas Eder
 */
final class VersionsImpl implements Versions {

    final Version              root;
    final Map<String, Version> versions;

    VersionsImpl(Version root) {
        this.root = root;
        this.versions = new HashMap<>();

        add(root);
    }

    void add(Version version) {
        versions.put(version.id(), version);
    }

    void addAll(Collection<? extends Version> v) {
        for (Version version : v)
            add(version);
    }

    void remove(Version version) {
        versions.remove(version.id());
    }

    @Override
    public final Version root() {
        return root;
    }

    @Override
    public final Version get(String id) {
        return versions.get(id);
    }
}
