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

import static org.jooq.impl.Tools.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Commit;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.File;
import org.jooq.migrations.xml.jaxb.ChangeType;
import org.jooq.migrations.xml.jaxb.CommitType;
import org.jooq.migrations.xml.jaxb.FileType;
import org.jooq.migrations.xml.jaxb.MigrationsType;
import org.jooq.migrations.xml.jaxb.ParentType;

/**
 * @author Lukas Eder
 */
final class CommitsImpl implements Commits {

    final Configuration       configuration;
    final Commit              root;
    final Map<String, Commit> commits;

    CommitsImpl(Configuration configuration, Commit root) {
        this.configuration = configuration;
        this.commits = new LinkedHashMap<>();
        this.root = root;

        add(root);
    }

    @Override
    public void add(Commit commit) {
        commits.put(commit.id(), commit);
    }

    @Override
    public void addAll(Commit... c) {
        addAll(Arrays.asList(c));
    }

    @Override
    public void addAll(Collection<? extends Commit> c) {
        for (Commit commit : c)
            add(commit);
    }

    @Override
    public final Commit root() {
        return root;
    }

    @Override
    public final Commit get(String id) {
        return commits.get(id);
    }

    @Override
    public final Iterator<Commit> iterator() {
        return commits.values().iterator();
    }

    @Override
    public final Commits load(MigrationsType migrations) {
        Map<String, CommitType> map = new HashMap<>();

        for (CommitType commit : migrations.getCommits())
            map.put(commit.getId(), commit);

        for (CommitType commit : migrations.getCommits())
            load(map, commit);

        return this;
    }

    private final Commit load(Map<String, CommitType> map, CommitType commit) {
        Commit result = commits.get(commit.getId());

        if (result != null)
            return result;

        Commit p1 = root;
        Commit p2 = null;

        List<ParentType> parents = commit.getParents();
        int size = parents.size();
        if (size > 0) {
            CommitType c1 = map.get(parents.get(0).getId());

            if (c1 == null)
                throw new UnsupportedOperationException("Parent not found: " + parents.get(0).getId());

            p1 = load(map, c1);
            if (size == 2) {
                CommitType c2 = map.get(parents.get(1).getId());

                if (c2 == null)
                    throw new UnsupportedOperationException("Parent not found: " + parents.get(0).getId());

                p2 = load(map, c2);
            }
            else if (size > 2)
                throw new UnsupportedOperationException("Merging more than two parents not yet supported");
        }

        result = p2 == null
            ? p1.commit(commit.getId(), commit.getMessage(), files(commit))
            : p1.merge(commit.getId(), commit.getMessage(), p2, files(commit));

        commits.put(commit.getId(), result);
        return result;
    }

    private final List<File> files(CommitType commit) {
        return map(commit.getFiles(), f -> Migrations.file(f.getPath(), f.getChange() == ChangeType.DELETE ? null : f.getContent(), f.getContentType()));
    }

    @Override
    public final MigrationsType export() {
        return new MigrationsType().withCommits(map(this, commit -> new CommitType()
            .withId(commit.id())
            .withMessage(commit.message())
            .withParents(map(commit.parents(), parent -> new ParentType().withId(parent.id())))
            .withFiles(map(commit.files(), file -> new FileType()
                .withPath(file.path())
                .withContent(file.content())
                .withContentType(file.type())
                .withChange(file.content() == null ? ChangeType.DELETE : ChangeType.MODIFY)
            ))
        ));
    }

    @Override
    public String toString() {
        return "" + commits.values();
    }
}
