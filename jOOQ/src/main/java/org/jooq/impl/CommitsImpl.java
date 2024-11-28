/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.jooq.ContentType.INCREMENT;
import static org.jooq.ContentType.SCRIPT;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.isEmpty;
import static org.jooq.impl.Tools.map;
import static org.jooq.tools.StringUtils.defaultIfNull;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jooq.Commit;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.ContentType;
import org.jooq.File;
import org.jooq.FilePattern;
import org.jooq.Migrations;
import org.jooq.Source;
import org.jooq.Tag;
import org.jooq.conf.MigrationDefaultContentType;
import org.jooq.exception.DataMigrationVerificationException;
import org.jooq.migrations.xml.jaxb.ChangeType;
import org.jooq.migrations.xml.jaxb.CommitType;
import org.jooq.migrations.xml.jaxb.FileType;
import org.jooq.migrations.xml.jaxb.MigrationsType;
import org.jooq.migrations.xml.jaxb.ParentType;
import org.jooq.migrations.xml.jaxb.TagType;
import org.jooq.tools.JooqLogger;
import org.jooq.util.jaxb.tools.MiniJAXB;

/**
 * @author Lukas Eder
 */
final class CommitsImpl implements Commits {

    private static final JooqLogger log = JooqLogger.getLogger(CommitsImpl.class);
    final Configuration             configuration;
    final Migrations                migrations;
    final Commit                    root;
    final Map<String, Commit>       commitsById;
    final Map<String, Commit>       commitsByTag;

    CommitsImpl(Configuration configuration, Commit root) {
        this.configuration = configuration;
        this.migrations = configuration.dsl().migrations();
        this.commitsById = new LinkedHashMap<>();
        this.commitsByTag = new LinkedHashMap<>();
        this.root = root;

        add(root);
    }

    @Override
    public final Commits add(Commit commit) {
        if (root != commit.root())
            throw new DataMigrationVerificationException("A Commits graph must contain a single graph whose commits all share the same root.");

        Commit duplicate;

        if ((duplicate = commitsById.get(commit.id())) != null)
            throw new DataMigrationVerificationException("Duplicate commit ID already present on commit: " + duplicate);

        for (Tag tag : commit.tags())
            if ((duplicate = commitsByTag.get(tag.id())) != null)
                throw new DataMigrationVerificationException("Duplicate tag " + tag + " already present on commit: " + duplicate);

        commitsById.put(commit.id(), commit);

        for (Tag tag : commit.tags())
            commitsByTag.put(tag.id(), commit);

        if (log.isDebugEnabled())
            log.debug("Commit added", commit);

        return this;
    }

    @Override
    public final Commits addAll(Commit... c) {
        return addAll(asList(c));
    }

    @Override
    public final Commits addAll(Collection<? extends Commit> c) {
        for (Commit commit : c)
            add(commit);

        return this;
    }

    @Override
    public final Commit root() {
        return root;
    }

    @Override
    public final Commit current() {
        return new MigrationImpl(configuration, root).currentCommit(false);
    }

    @Override
    public final Commit latest() {
        Map<String, Commit> commits = new HashMap<>(commitsById);

        for (Entry<String, Commit> e : commitsById.entrySet())
            for (Commit parent : e.getValue().parents())
                commits.remove(parent.id());

        if (commits.size() == 1)
            return commits.values().iterator().next();
        else
            throw new DataMigrationVerificationException("No latest commit available. There are " + commits.size() + " unmerged branches.");
    }

    @Override
    public final Commit get(String id) {
        Commit result = commitsById.get(id);
        return result != null ? result : commitsByTag.get(id);
    }

    @Override
    public final Iterator<Commit> iterator() {
        return unmodifiableCollection(commitsById.values()).iterator();
    }

    // [#9506] TODO: Formalise this decoding, and make it part of the public API
    final class FileData {
        final FilePattern   pattern;
        final Source        source;
        final String        path;
        final String        id;
        final String        message;
        final String        author;
        final List<TagType> tags;
        final List<String>  parents;
        final ContentType   contentType;

        FileData(FilePattern pattern, Source source) {
            this.pattern = pattern;
            this.source = source;

            // [#9506] TODO: Other naming schemes?
            String name = source.name();

            if (name == null)
                throw new DataMigrationVerificationException("Cannot work with unnamed sources: " + source);

            String basename = name.replace(".sql", "");

            // [#9506] File name encoding basedir/[encoding].sql where [encoding] can contain:
            //         - id/schemas/path.sql
            //         - id/increment/[path and message].sql
            //         - id/[path and message].sql
            //         - id.sql
            java.io.File p1 = new java.io.File(pattern.path(source.file())).getParentFile();
            java.io.File p2 = p1 != null ? p1.getParentFile() : null;

            this.contentType = p2 == null
                ? defaultContentType()
                : contentType(p1.getName());
            this.path = name;

            this.id = p1 == null
                ? basename
                : p2 == null
                ? p1.getName()
                : p2.getName();

            java.io.File meta = new java.io.File(source.file().getParent(), basename + ".xml");
            CommitType commit = null;
            if (meta.exists())
                commit = MiniJAXB.unmarshal(meta, CommitType.class);

            this.message =
                  commit != null && commit.getMessage() != null
                ? commit.getMessage()
                : p2 != null || p1 != null
                ? basename
                : null;
            this.author =
                  commit != null
                ? commit.getAuthor()
                : null;

            this.tags = new ArrayList<>();
            this.parents = new ArrayList<>();

            if (commit != null) {
                for (TagType tag : commit.getTags())
                    this.tags.add(tag);
                for (ParentType parent : commit.getParents())
                    this.parents.add(parent.getId());
            }
        }

        private final ContentType defaultContentType() {
            switch (defaultIfNull(configuration.settings().getMigrationDefaultContentType(), MigrationDefaultContentType.INCREMENT)) {
                case INCREMENT:
                    return INCREMENT;
                case SCRIPT:
                    return SCRIPT;
                default:
                    throw new UnsupportedOperationException("Unsupported ContentType: " + configuration.settings().getMigrationDefaultContentType());
            }
        }

        @Override
        public String toString() {
            List<String> strings = new ArrayList<>();

            if (id != null)
                strings.add("id: " + id);
            if (message != null)
                strings.add("message: " + message);
            if (!tags.isEmpty())
                strings.add("tags: " + tags);
            if (!parents.isEmpty())
                strings.add("parents: " + parents);

            return "File: " + source.file() + " " + strings;
        }
    }

    static final ContentType contentType(String name) {
        switch (name.toLowerCase()) {
            case "schemas": return ContentType.SCHEMA;
            case "scripts": return ContentType.SCRIPT;
            case "increments": return ContentType.INCREMENT;
            case "snapshots": return ContentType.SNAPSHOT;
            default:
                throw new IllegalArgumentException("Unsupported content type: " + name);
        }
    }

    @Override
    public final Commits load(java.io.File directory) throws IOException {
        if (log.isDebugEnabled())
            log.debug("Reading directory", directory);

        FilePattern sqlPattern = new FilePattern().basedir(directory).pattern("**.sql");
        FilePattern xmlPattern = new FilePattern().basedir(directory).pattern("**.xml");
        List<Source> sql, xml;

        if (!isEmpty(sql = sqlPattern.collect()))
            return loadSQL(sqlPattern, sql);
        else if (!isEmpty(xml = xmlPattern.collect()))
            return loadXML(xmlPattern, xml);
        else
            return this;
    }

    private final Commits loadSQL(FilePattern pattern, List<Source> files) throws IOException {

        // [#9506] TODO: Turning a directory into a MigrationsType (and various other conversion)
        //               could be made reusable. This is certainly very useful for testing and interop,
        //               e.g. also to support other formats (Flyway, Liquibase) as source
        TreeMap<String, CommitType> idToCommit = new TreeMap<>(comparing(
            java.io.File::new,
            pattern.fileComparator()
        ));

        List<FileData> list = files.stream().map(s -> new FileData(pattern, s)).collect(toList());

        if (log.isDebugEnabled())
            list.forEach(f -> log.debug("Reading file", f));

        for (FileData f : list)
            idToCommit.putIfAbsent(f.id, new CommitType()
                .withId(f.id)
                .withAuthor(f.author)
                .withMessage(f.message)
                .withTags(f.tags)
            );

        for (FileData f : list) {
            CommitType commit = idToCommit.get(f.id);

            // Parents are implicit
            // [#9506] TODO: What cases of implicit parents are possible. What edge cases aren't?
            if (f.parents.isEmpty()) {
                Entry<String, CommitType> e = idToCommit.lowerEntry(f.id);

                if (e != null)
                    commit.setParents(asList(new ParentType().withId(e.getKey())));
            }

            // Parents are explicit
            else {
                for (String parent : f.parents)
                    if (idToCommit.containsKey(parent))
                        commit.getParents().add(new ParentType().withId(parent));
                    else
                        throw new DataMigrationVerificationException("Parent " + parent + " is not defined");
            }

            commit.getFiles().add(new FileType()
                .withPath(f.path)
                .withContentType(f.contentType)
                .withContent(f.source.readString())
                .withChange(ChangeType.MODIFY)
            );
        }

        if (log.isDebugEnabled())
            log.debug("Loading files into: " + new MigrationsType().withCommits(idToCommit.values()));

        return load(new MigrationsType().withCommits(idToCommit.values()));
    }

    private final Commits loadXML(FilePattern pattern, List<Source> files) throws IOException {
        MigrationsType m = new MigrationsType();

        for (Source s : files) {
            try (Reader reader = s.reader()) {
                MigrationsType u = MiniJAXB.unmarshal(s.reader(), MigrationsType.class);
                m = MiniJAXB.append(m, u);
            }
        }

        return load(m);
    }

    @Override
    public final Commits load(MigrationsType migrations) {
        Map<String, CommitType> map = new HashMap<>();

        for (CommitType commit : migrations.getCommits())
            map.put(commit.getId(), commit);

        map.putIfAbsent(Commit.ROOT, new CommitType().withId(root.id()).withMessage(root.message()));
        for (CommitType commit : migrations.getCommits())
            load(map, commit);

        return this;
    }

    private final Commit load(Map<String, CommitType> map, CommitType commit) {
        Commit result = commitsById.get(commit.getId());

        if (result != null) {
            if (result.equals(root)) {
                ((CommitImpl) result).delta.putAll(CommitImpl.map(files(commit), false));
                ((CommitImpl) result).files = null;
                ((CommitImpl) result).tags.addAll(map(commit.getTags(), t -> new TagImpl(t.getId(), t.getMessage())));
            }

            return result;
        }

        Commit p1 = root;
        Commit p2 = null;

        List<ParentType> parents = commit.getParents();
        int size = parents.size();
        if (size > 0) {
            CommitType c1 = map.get(parents.get(0).getId());

            if (c1 == null)
                throw new DataMigrationVerificationException("Parent not found: " + parents.get(0).getId());

            p1 = load(map, c1);
            if (size == 2) {
                CommitType c2 = map.get(parents.get(1).getId());

                if (c2 == null)
                    throw new DataMigrationVerificationException("Parent not found: " + parents.get(0).getId());

                p2 = load(map, c2);
            }
            else if (size > 2)
                throw new DataMigrationVerificationException("Merging more than two parents not yet supported");
        }

        result = p2 == null
            ? p1.commit(commit.getId(), commit.getMessage(), commit.getAuthor(), files(commit))
            : p1.merge(commit.getId(), commit.getMessage(), commit.getAuthor(), p2, files(commit));

        for (TagType tag : commit.getTags())
            result = result.tag(tag.getId(), tag.getMessage());

        add(result);
        return result;
    }

    private final List<File> files(CommitType commit) {
        return map(commit.getFiles(), f -> migrations.file(f.getPath(), f.getChange() == ChangeType.DELETE ? null : f.getContent(), f.getContentType()));
    }

    @Override
    public final MigrationsType export() {
        return new MigrationsType().withCommits(map(filter(this, c -> !Commit.ROOT.equals(c.id())), commit -> new CommitType()
            .withId(commit.id())
            .withMessage(commit.message())
            .withAuthor(commit.author())
            .withParents(map(commit.parents(), parent -> new ParentType().withId(parent.id())))
            .withTags(map(commit.tags(), tag -> new TagType().withId(tag.id()).withMessage(tag.message())))
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
        return "" + commitsById.values();
    }
}
