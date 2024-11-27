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

import static org.jooq.ContentType.DECREMENT;
import static org.jooq.ContentType.INCREMENT;
import static org.jooq.ContentType.SCHEMA;
import static org.jooq.ContentType.SCRIPT;
import static org.jooq.ContentType.SNAPSHOT;
import static org.jooq.impl.Tools.EMPTY_SOURCE;
import static org.jooq.impl.Tools.anyMatch;
import static org.jooq.impl.Tools.filter;
import static org.jooq.impl.Tools.iterable;
import static org.jooq.tools.StringUtils.isBlank;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jooq.Commit;
import org.jooq.Configuration;
import org.jooq.ContentType;
import org.jooq.DSLContext;
import org.jooq.File;
import org.jooq.Files;
import org.jooq.Meta;
import org.jooq.Node;
// ...
import org.jooq.Source;
import org.jooq.Tag;
import org.jooq.Version;
import org.jooq.exception.DataMigrationVerificationException;
import org.jooq.impl.DefaultParseContext.IgnoreQuery;
import org.jooq.tools.StringUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Lukas Eder
 */
final class CommitImpl extends AbstractNode<Commit> implements Commit {

    final DSLContext            ctx;
    final List<Commit>          parents;
    final List<Tag>             tags;
    final Map<String, File>     delta;
    transient Map<String, File> files;
    final boolean               valid;

    CommitImpl(
        Configuration configuration,
        String id,
        String message,
        String author,
        Commit root,
        List<Commit> parents,
        Collection<? extends File> delta,
        boolean valid
    ) {
        super(configuration, id, message, author, root);

        if (Node.ROOT.equals(id) && root != null)
            throw new DataMigrationVerificationException("Cannot use reserved ID \"root\"");

        this.ctx = configuration.dsl();
        this.parents = parents;
        this.tags = new ArrayList<>();
        this.delta = map(delta, false);
        this.valid = valid;

        if (delta.size() > this.delta.size()) {
            throw new DataMigrationVerificationException("Path is ambiguous within commit: " + duplicatePath(delta));
        }
    }

    private static final String duplicatePath(Collection<? extends File> files) {
        Set<String> paths = new HashSet<>();

        for (File file : files)
            if (!paths.add(file.path()))
                return file.path();

        return null;
    }

    private CommitImpl(CommitImpl copy, boolean newValid) {
        super(copy.configuration(), copy.id(), copy.message(), copy.author(), copy.root);

        this.ctx = copy.ctx;
        this.parents = copy.parents;
        this.tags = new ArrayList<>(copy.tags);
        this.delta = copy.delta;
        this.files = copy.files;
        this.valid = newValid;
    }

    // TODO extract this Map<String, File> type to new type
    static final Map<String, File> map(Collection<? extends File> list, boolean applyDeletions) {
        return apply(new LinkedHashMap<>(), list, applyDeletions);
    }

    static final Map<String, File> apply(Map<String, File> result, Collection<? extends File> list, boolean applyDeletions) {
        for (File file : list)
            apply(result, file, applyDeletions);

        return result;
    }

    private static final Map<String, File> apply(Map<String, File> result, File file, boolean applyDeletions) {
        if (applyDeletions && file.content() == null)
            result.remove(file.path());
        else
            result.put(file.path(), file);

        return result;
    }

    private final Map<String, File> initFiles() {
        if (parents.isEmpty())
            return delta;

        // TODO: Support multiple parents
        Commit parent = parents.get(0);
        return apply(map(parent.files(), true), delta(), true);
    }

    @Override
    public final boolean valid() {
        return valid;
    }

    @Override
    public final Commit valid(boolean newValid) {
        return new CommitImpl(this, newValid);
    }

    @Override
    public final List<Commit> parents() {
        return Collections.unmodifiableList(parents);
    }

    @Override
    public final List<Tag> tags() {
        return Collections.unmodifiableList(tags);
    }

    @Override
    public final Commit tag(String tagId) {
        return tag(tagId, null);
    }

    @Override
    public final Commit tag(String tagId, String tagMessage) {
        CommitImpl result = new CommitImpl(this, valid);
        result.tags.add(new TagImpl(tagId, tagMessage));
        return result;
    }

    @Override
    public final Collection<File> delta() {
        return delta.values();
    }

    @Override
    public final Collection<File> files() {
        if (files == null)
            files = initFiles();

        return files.values();
    }

    private static final Collection<Source> sources(Collection<File> files) {
        return Tools.map(files, f -> Source.of(f.content()));
    }

    @Override
    public final Collection<Source> sources() {
        return sources(files());
    }

    @Override
    public final Commit commit(String newId, File... newFiles) {
        return commit(newId, "", newFiles);
    }

    @Override
    public final Commit commit(String newId, Collection<? extends File> newFiles) {
        return commit(newId, "", newFiles);
    }

    @Override
    public final Commit commit(String newId, String newMessage, File... newFiles) {
        return commit(newId, newMessage, null, Arrays.asList(newFiles));
    }

    @Override
    public final Commit commit(String newId, String newMessage, Collection<? extends File> newFiles) {
        return commit(newId, newMessage, null, newFiles);
    }

    @Override
    public final Commit commit(String newId, String newMessage, String newAuthor, File... newFiles) {
        return commit(newId, newMessage, newAuthor, Arrays.asList(newFiles));
    }

    @Override
    public final Commit commit(String newId, String newMessage, String newAuthor, Collection<? extends File> newFiles) {
        return new CommitImpl(configuration(), newId, newMessage, newAuthor, root, Arrays.asList(this), newFiles, valid);
    }

    @Override
    public final Commit merge(String newId, Commit with, File... newFiles) {
        return merge(newId, null, with, Arrays.asList(newFiles));
    }

    @Override
    public final Commit merge(String newId, Commit with, Collection<? extends File> newFiles) {
        return merge(newId, null, with, newFiles);
    }

    @Override
    public final Commit merge(String newId, String newMessage, Commit with, File... newFiles) {
        return merge(newId, newMessage, null, with, Arrays.asList(newFiles));
    }

    @Override
    public final Commit merge(String newId, String newMessage, Commit with, Collection<? extends File> newFiles) {
        return merge(newId, newMessage, null, with, newFiles);
    }

    @Override
    public final Commit merge(String newId, String newMessage, String newAuthor, Commit with, File... newFiles) {
        return merge(newId, newMessage, newAuthor,  with, Arrays.asList(newFiles));
    }

    @Override
    public final Commit merge(String newId, String newMessage, String newAuthor, Commit with, Collection<? extends File> newFiles) {
        return new CommitImpl(configuration(), newId, newMessage, newAuthor, root, Arrays.asList(this, with), newFiles, valid);
    }

    @Override
    public final Version version() {
        return root().migrateTo(this).to();
    }

    @Override
    public final Meta meta() {
        return version().meta();
    }

    @Override
    public final Files migrateTo(Commit resultCommit) {
        if (equals(resultCommit))
            if (equals(root()))
                return FilesImpl.empty(ctx.migrations().version(ROOT));
            else
                return FilesImpl.empty(version());

        // TODO: Implement reverting a branch up to the common ancestor
        Commit ancestor = commonAncestor(resultCommit);

        // TODO: The reverse check doesn't take into account branching
        if (ancestor.equals(resultCommit)) {
            configuration().requireCommercial(() -> "Reverse migrations are a commercial only feature. Please upgrade to the jOOQ Professional Edition or jOOQ Enterprise Edition.");




        }

        return migrateTo0(resultCommit);
    }




















































































































    private static final record MigrationHistory(
        Map<String, Map<String, File>> pathHistory,
        Files result
    ) {


























    }

    private final Files migrateTo0(Commit resultCommit) {
        return migrateTo1(resultCommit, false).result();
    }

    private final MigrationHistory migrateTo1(Commit resultCommit, boolean recordPathHistory) {
        Map<String, Map<String, File>> pathHistory = recordPathHistory ? new HashMap<>() : null;

        // History are all the files that have been applied before this commit
        Map<String, File> history = new LinkedHashMap<>();
        Map<String, String> historyKeys = new HashMap<>();

        // Result are all the files that are applied starting from this commit
        Map<String, File> result = new LinkedHashMap<>();

        // Temporary FileType.SCHEMA changes that are collapsed until a FileType.INCREMENT is encountered
        Map<String, File> tempHistory = new LinkedHashMap<>();
        Map<String, String> tempHistoryKeys = new HashMap<>();

        Deque<Commit> commitHistory = new ArrayDeque<>();
        boolean recordingResult = false;
        boolean hasDeletions = false;
        boolean isRoot = equals(root());
        Commit fromSnapshotCommit = null;

        history(commitHistory, new HashSet<>(), Arrays.asList(resultCommit));

        commitLoop:
        for (Commit commit : commitHistory) {
            List<File> commitFiles = new ArrayList<>(commit.delta());

            if (isRoot && anyMatch(commitFiles, f -> f.type() == SNAPSHOT)) {
                configuration().requireCommercial(() -> "Snapshots are a commercial only feature. Please upgrade to the jOOQ Professional Edition or jOOQ Enterprise Edition.");















            }

            // Deletions
            Iterator<File> deletions = filter(commitFiles.iterator(), f -> f.content() == null);
            for (File file : iterable(deletions)) {
                hasDeletions |= true;
                String path = file.path();
                String tempKey = tempHistoryKeys.remove(path);
                String tempRemove = tempKey != null ? tempKey : path;
                String key = historyKeys.remove(path);
                String remove = key != null ? key : path;

                if (recordingResult && result.remove(tempRemove) == null && file.type() == INCREMENT && history.containsKey(tempRemove))
                    result.put(tempRemove, file);

                // TODO: Support deletions of scripts
                else if (recordingResult && result.remove(remove) == null && file.type() == SCHEMA && history.containsKey(remove))
                    result.put(remove, file);
                else
                    history.remove(tempRemove);

                tempHistory.remove(path);
                deletions.remove();




            }

            // Increments
            Iterator<File> increments = filter(commitFiles.iterator(), f -> f.type() == INCREMENT);
            for (File file : iterable(increments)) {
                String path = file.path();
                File oldFile = recordingResult ? history.get(path) : history.put(path, file);

                if (oldFile == null && !tempHistory.isEmpty() && !result.containsKey(path))
                    move(tempHistory, result, tempHistoryKeys);

                if (recordingResult)
                    result.put(path, file);

                increments.remove();





            }













            // Script files
            Iterator<File> scripts = filter(commitFiles.iterator(), f -> f.type() == SCRIPT);
            boolean hasScripts = false;
            for (File file : iterable(scripts)) {
                hasScripts = true;
                String path = file.path();
                File oldFile = recordingResult ? history.get(path) : history.put(path, file);

                if (oldFile == null && !tempHistory.isEmpty() && !result.containsKey(path))
                    move(tempHistory, result, tempHistoryKeys);

                if (recordingResult)
                    result.put(path, file);

                scripts.remove();



            }

            // Schema files
            Iterator<File> schemas = filter(commitFiles.iterator(), f -> f.type() == SCHEMA);
            for (File file : iterable(schemas)) {
                String path = file.path();
                String key = commit.id() + "-" + path;

                if (hasScripts)
                    file = new IgnoreFile(file);

                if (recordingResult) {
                    tempHistory.put(path, file);
                    tempHistoryKeys.put(path, key);
                }
                else {
                    history.put(key, file);
                    historyKeys.put(path, key);
                }

                schemas.remove();





            }

            if (hasScripts)
                move(tempHistory, result, tempHistoryKeys);

            recordingResult |= id().equals(commit.id());
        }

        move(tempHistory, result, tempHistoryKeys);

        // See if resulting increments try to alter history
        for (Iterator<Entry<String, File>> it = result.entrySet().iterator(); it.hasNext();) {
            Entry<String, File> entry = it.next();
            String path = entry.getKey();
            File file = entry.getValue();

            if (file.type() == INCREMENT) {
                File historicFile = history.get(path);

                if (historicFile != null) {

                    // Altering history is not allowed
                    if (!StringUtils.equals(historicFile.content(), file.content()))
                        throw new DataMigrationVerificationException("Cannot edit increment file that has already been applied: " + file);

                    // History was altered, but the alteration was reverted
                    else
                        it.remove();
                }
            }
        }

        // Collapse consecutive FileType.SCHEMA files that were not consecutive
        // prior to the deletion.
        if (hasDeletions) {
            Map<String, List<String>> keys = new LinkedHashMap<>();
            Set<String> remove = new LinkedHashSet<>();

            result.forEach((key, file) -> {
                if (file.type() == SCHEMA)
                    keys.computeIfAbsent(file.path(), p -> new ArrayList<>()).add(key);
                else
                    moveAllButLast(keys, remove);
            });

            moveAllButLast(keys, remove);
            for (String r : remove)
                result.remove(r);
        }

        Map<String, File> versionFiles = new HashMap<>();
        Version from = version(ctx.migrations().version(ROOT), id(), versionFiles, history.values());
        Version fromSnapshot = null;






        Version to = version(from, resultCommit.id(), versionFiles, result.values());
        return new MigrationHistory(
            pathHistory,
            new FilesImpl(from, fromSnapshot, to, result.values())
        );
    }

    /**
     * Breadth first recursion over commit graph.
     */
    private static final void history(Deque<Commit> commitHistory, Set<Commit> set, List<Commit> commits) {

        // TODO: When encountering a snapshot on a single path (no branches), we can abort recursion
        for (Commit commit : commits)
            if (set.add(commit))
                commitHistory.push(commit);

        Collection<Commit> p = new LinkedHashSet<>();
        for (Commit commit : commits)
            p.addAll(commit.parents());

        if (!p.isEmpty()) {
            List<Commit> l = new ArrayList<>(p);
            Collections.reverse(l);

            // TODO: Use iteration instead of depending on tail recursion optimisation.
            history(commitHistory, set, l);
        }
    }

    private static final record IgnoreFile(File file) implements File {
        @Override
        public final String path() {
            return file.path();
        }

        @Override
        public final String name() {
            return file.name();
        }

        @Override
        public final String content() {
            return file.content();
        }

        @Override
        public final ContentType type() {
            return file.type();
        }
    }

    private final Version version(Version from, String newId, Map<String, File> files, Iterable<File> result) {
        Version to = from;

        for (File file : result) {

            // [#9506] TODO: This historic Version::id generation used to be necessary to create unique
            //         Version IDs per file path. It doesn't seem to be necessary anymore.
            // String commitId = newId + "-" + file.path();

            if (file.type() == SCHEMA) {
                Meta meta = ctx.meta(sources(apply(files, file, true).values()).toArray(EMPTY_SOURCE));

                if (file instanceof IgnoreFile)
                    to = ((VersionImpl) to).commit(newId, meta, ctx.queries());
                else
                    to = to.commit(newId, meta);
            }

            // [#9506] Scripts must be ignored by the interpreter
            else if (file.type() == SCRIPT)
                to = to.apply(newId, new IgnoreQuery(file.content(), ctx.configuration()));
            else
                to = to.apply(newId, file.content());
        }

        return to;
    }

    private static final void moveAllButLast(Map<String, List<String>> keys, Set<String> remove) {
        for (List<String> k : keys.values())
            if (k.size() > 1)
                remove.addAll(k.subList(0, k.size() - 1));

        keys.clear();
    }

    private static final void move(Map<String, File> files, Map<String, File> result, Map<String, String> keys) {
        for (File file : files.values())
            result.put(keys.get(file.path()), file);

        files.clear();
    }

    // -------------------------------------------------------------------------
    // The Object API
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return id().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Commit)
            return id().equals(((Commit) obj).id());

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id());

        if (!isBlank(message()))
            sb.append(" - ").append(message());

        if (!isBlank(author()))
            sb.append(", author: " + author());

        if (!tags.isEmpty())
            sb.append(", tags: ").append(tags);

        return sb.toString();
    }
}
