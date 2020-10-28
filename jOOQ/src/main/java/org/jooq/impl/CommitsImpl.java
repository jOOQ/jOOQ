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
        List<File> files = new ArrayList<>();

        for (FileType file : commit.getFiles())
            files.add(Migrations.file(file.getPath(), file.getChange() == ChangeType.DELETE ? null : file.getContent(), file.getContentType()));

        return files;
    }

    @Override
    public final MigrationsType export() {
        MigrationsType result = new MigrationsType();
        List<CommitType> list = new ArrayList<>();

        for (Commit commit : this) {
            List<ParentType> parents = new ArrayList<>();
            List<FileType> files = new ArrayList<>();

            for (Commit parent : commit.parents())
                parents.add(new ParentType().withId(parent.id()));

            for (File file : commit.files())
                files.add(new FileType()
                    .withPath(file.path())
                    .withContent(file.content())
                    .withContentType(file.type())
                    .withChange(file.content() == null ? ChangeType.DELETE : ChangeType.MODIFY)
                );

            list.add(new CommitType()
                .withId(commit.id())
                .withMessage(commit.message())
                .withParents(parents)
                .withFiles(files)
            );
        }

        result.setCommits(list);
        return result;
    }

    @Override
    public String toString() {
        return "" + commits.values();
    }
}
