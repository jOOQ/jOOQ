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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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

import static org.jooq.ContentType.INCREMENT;
import static org.jooq.ContentType.SCHEMA;
import static org.jooq.impl.Tools.EMPTY_SOURCE;
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
import org.jooq.DSLContext;
import org.jooq.File;
import org.jooq.Files;
import org.jooq.Meta;
import org.jooq.Source;
import org.jooq.Tag;
import org.jooq.Version;
import org.jooq.exception.DataMigrationException;
import org.jooq.tools.StringUtils;

/**
 * @author Lukas Eder
 */
final class CommitImpl extends AbstractNode<Commit> implements Commit {

    final DSLContext        ctx;
    final List<Commit>      parents;
    final List<Tag>         tags;
    final Map<String, File> delta;
    final Map<String, File> files;

    CommitImpl(Configuration configuration, String id, String message, Commit root, List<Commit> parents, Collection<? extends File> delta) {
        super(configuration, id, message, root);

        this.ctx = configuration.dsl();
        this.parents = parents;
        this.tags = new ArrayList<>();
        this.delta = map(delta, false);
        this.files = initFiles();
    }

    private CommitImpl(CommitImpl copy) {
        super(copy.configuration(), copy.id(), copy.message(), copy.root);

        this.ctx = copy.ctx;
        this.parents = copy.parents;
        this.tags = new ArrayList<>(copy.tags);
        this.delta = copy.delta;
        this.files = copy.files;
    }

    // TODO extract this Map<String, File> type to new type
    private static final Map<String, File> map(Collection<? extends File> list, boolean applyDeletions) {
        return apply(new LinkedHashMap<>(), list, applyDeletions);
    }

    private static final Map<String, File> apply(Map<String, File> result, Collection<? extends File> list, boolean applyDeletions) {
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
    public final List<Commit> parents() {
        return Collections.unmodifiableList(parents);
    }

    @Override
    public final List<Tag> tags() {
        return Collections.unmodifiableList(tags);
    }

    @Override
    public final Commit tag(String id) {
        return tag(id, null);
    }

    @Override
    public final Commit tag(String id, String message) {
        CommitImpl result = new CommitImpl(this);
        result.tags.add(new TagImpl(id, message));
        return result;
    }

    @Override
    public final Collection<File> delta() {
        return delta.values();
    }

    @Override
    public final Collection<File> files() {
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
        return commit(newId, newMessage, Arrays.asList(newFiles));
    }

    @Override
    public final Commit commit(String newId, String newMessage, Collection<? extends File> newFiles) {
        return new CommitImpl(configuration(), newId, newMessage, root, Arrays.asList(this), newFiles);
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
        return merge(newId, newMessage, with, Arrays.asList(newFiles));
    }

    @Override
    public final Commit merge(String newId, String newMessage, Commit with, Collection<? extends File> newFiles) {
        return new CommitImpl(configuration(), newId, newMessage, root, Arrays.asList(this, with), newFiles);
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

        // TODO: Implement reverting a branch up to the common ancestor
        Commit ancestor = commonAncestor(resultCommit);
        return migrateTo0(resultCommit);
    }

    private final Files migrateTo0(Commit resultCommit) {

        // History are all the files that have been applied before this commit
        Map<String, File> history = new LinkedHashMap<>();
        Map<String, String> historyKeys = new HashMap<>();

        // Result are all the files that are applied starting from this commit
        Map<String, File> result = new LinkedHashMap<>();

        // Temporary FileType.SCHEMA changes that are collapsed until a FileType.INCREMENT is encountered
        Map<String, File> tempHistory = new LinkedHashMap<>();
        Map<String, String> tempHistoryKeys = new HashMap<>();

        Deque<Commit> commitHistory = new ArrayDeque<>();
        history(commitHistory, new HashSet<>(), Arrays.asList(resultCommit));

        boolean recordingResult = false;
        boolean hasDeletions = false;
        for (Commit commit : commitHistory) {
            List<File> commitFiles = new ArrayList<>(commit.delta());

            // Deletions
            Iterator<File> deletions = commitFiles.iterator();
            while (deletions.hasNext()) {
                File file = deletions.next();

                if (file.content() == null) {
                    hasDeletions |= true;
                    String path = file.path();
                    String tempKey = tempHistoryKeys.remove(path);
                    String tempRemove = tempKey != null ? tempKey : path;
                    String key = historyKeys.remove(path);
                    String remove = key != null ? key : path;

                    if (recordingResult && result.remove(tempRemove) == null && file.type() == INCREMENT && history.containsKey(tempRemove))
                        result.put(tempRemove, file);
                    else if (recordingResult && result.remove(remove) == null && file.type() == SCHEMA && history.containsKey(remove))
                        result.put(remove, file);
                    else
                        history.remove(tempRemove);

                    tempHistory.remove(path);
                    deletions.remove();
                }
            }

            // Increments
            Iterator<File> increments = commitFiles.iterator();
            while (increments.hasNext()) {
                File file = increments.next();

                if (file.type() == INCREMENT) {
                    String path = file.path();
                    File oldFile = recordingResult ? history.get(path) : history.put(path, file);

                    if (oldFile == null && !tempHistory.isEmpty() && !result.containsKey(path))
                        move(tempHistory, result, tempHistoryKeys);

                    if (recordingResult)
                        result.put(path, file);

                    increments.remove();
                }
            }

            // Schema files
            Iterator<File> schemas = commitFiles.iterator();
            while (schemas.hasNext()) {
                File file = schemas.next();

                if (file.type() == SCHEMA) {
                    String path = file.path();
                    String key = commit.id() + "-" + path;

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
            }

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
                        throw new DataMigrationException("Cannot edit increment file that has already been applied: " + file);

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
        Version from = version(ctx.migrations().version("init"), id(), versionFiles, history.values());
        Version to = version(from, resultCommit.id(), versionFiles, result.values());
        return new FilesImpl(from, to, result.values());
    }

    /**
     * Breadth first recursion over commit graph.
     */
    private static final void history(Deque<Commit> commitHistory, Set<Commit> set, List<Commit> commits) {
        for (Commit commit : commits)
            if (set.add(commit))
                commitHistory.push(commit);

        Collection<Commit> p = new LinkedHashSet<>();
        for (Commit commit : commits)
            p.addAll(commit.parents());

        if (!p.isEmpty()) {
            List<Commit> l = new ArrayList<>(p);
            Collections.reverse(l);
            history(commitHistory, set, l);
        }
    }

    private static final Version version(Version from, String newId, Map<String, File> files, Collection<File> result) {
        Version to = from;

        List<File> list = new ArrayList<>(result);

        for (int j = 0; j < list.size(); j++) {
            File file = list.get(j);
            String commitId = newId + "-" + file.path();

            if (file.type() == SCHEMA)
                to = to.commit(commitId, sources(apply(files, file, true).values()).toArray(EMPTY_SOURCE));
            else
                to = to.apply(commitId, file.content());
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

        if (!tags.isEmpty())
            sb.append(' ').append(tags);

        return sb.toString();
    }
}
