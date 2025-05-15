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
package org.jooq.migrations.jgit;

import static java.util.Arrays.asList;
import static org.eclipse.jgit.diff.DiffEntry.ChangeType.ADD;
import static org.eclipse.jgit.diff.DiffEntry.ChangeType.DELETE;
import static org.eclipse.jgit.diff.DiffEntry.ChangeType.RENAME;
import static org.jooq.ContentType.INCREMENT;
import static org.jooq.ContentType.SCHEMA;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.Commit;
import org.jooq.CommitProvider;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.ContentType;
import org.jooq.DSLContext;
import org.jooq.File;
import org.jooq.FilePattern;
import org.jooq.Migrations;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link CommitProvider} that produces versions from a git repository.
 *
 * @author Lukas Eder
 */
public final class GitCommitProvider implements CommitProvider {

    private static final JooqLogger log = JooqLogger.getLogger(GitCommitProvider.class);

    private final DSLContext        dsl;
    private final Migrations        migrations;
    private final GitConfiguration  git;
    private final FilePattern       incrementFilePattern;
    private final FilePattern       schemaFilePattern;

    public GitCommitProvider(Configuration configuration) {
        this(configuration, new GitConfiguration());
    }

    public GitCommitProvider(Configuration configuration, GitConfiguration git) {
        this.dsl = configuration.dsl();
        this.migrations = dsl.migrations();
        this.git = git;
        this.incrementFilePattern = new FilePattern().pattern(combine(git.basedir(), git.incrementFilePattern()));
        this.schemaFilePattern = new FilePattern().pattern(combine(git.basedir(), git.schemaFilePattern()));
    }

    private static final String combine(String basedir, String pattern) {
        if (StringUtils.isEmpty(basedir))
            return pattern;
        else if (basedir.endsWith("/"))
            return basedir + pattern;
        else
            return basedir + "/" + pattern;
    }

    @Override
    public final Commits provide() {
        Commits commits = migrations.commits();

        try (
            Git g = Git.open(git.repository());
            Repository r = g.getRepository();
            ObjectReader reader = r.newObjectReader()
        ) {
            // Prevent a "close() called when useCnt is already zero" warning
            r.incrementOpen();

            Deque<RevCommit> revCommits = new ArrayDeque<>();
            Map<String, List<RevTag>> tags = new HashMap<>();
            RevCommit last = null;

            try {
                for (RevCommit commit : g.log().call()) {
                    if (last == null)
                        last = commit;

                    revCommits.add(commit);
                }
            }
            catch (NoHeadException e) {
                log.debug("No HEAD exists");
            }

            for (Ref ref : g.tagList().call()) {
                RevTag tag = RevTag.parse(reader.open(ref.getObjectId()).getBytes());
                tags.computeIfAbsent(tag.getObject().getName(), id -> new ArrayList<>()).add(tag);
            }

            Commit root = commits.root();

            // TODO: This algorithm is quadradic in the worst case. Can we find a better one?
            // TODO: We collect all the commits from git, when we could ignore the empty ones
            while (!revCommits.isEmpty()) {

                // The commits seem to come in reverse order from jgit.
                Iterator<RevCommit> it = revCommits.descendingIterator();

                commitLoop:
                while (it.hasNext()) {
                    RevCommit revCommit = it.next();

                    if (revCommit.getParents() == null || revCommit.getParents().length == 0) {
                        commits.add(tag(tags, root.commit(revCommit.getName(), revCommit.getFullMessage(), revCommit.getAuthorIdent().getName(), editFiles(r, revCommit))));
                        it.remove();
                    }
                    else {
                        Commit[] parents = new Commit[revCommit.getParentCount()];

                        // It seems the parents are not ordered deterministically in the order of which they were merged
                        List<RevCommit> l = new ArrayList<>(asList(revCommit.getParents()));
                        l.sort(COMMIT_COMPARATOR);

                        for (int i = 0; i < parents.length; i++)
                            if ((parents[i] = commits.get(revCommit.getParents()[i].getName())) == null)
                                continue commitLoop;

                        if (parents.length == 1)
                            commits.add(tag(tags, parents[0].commit(revCommit.getName(), revCommit.getFullMessage(), revCommit.getAuthorIdent().getName(), editFiles(r, revCommit))));
                        else if (parents.length == 2)
                            commits.add(tag(tags, parents[0].merge(revCommit.getName(), revCommit.getFullMessage(), revCommit.getAuthorIdent().getName(), parents[1], editFiles(r, revCommit))));
                        else
                            throw new UnsupportedOperationException("Merging more than two parents not yet supported");

                        it.remove();
                    }
                }
            }

            Status status = g.status().call();
            if (status.hasUncommittedChanges() || !status.getUntracked().isEmpty()) {
                Commit c1 = last != null ? commits.get(last.getName()) : root;
                Commit c2 = commit(c1, status);

                // If we have a diff in git, but the commit is empty.
                if (c2 != c1)
                    commits.add(c2);
            }
        }
        catch (Exception e) {
            throw new GitException("Error while providing git versions", e);
        }

        return commits;
    }

    private static final Commit tag(Map<String, List<RevTag>> tags, Commit commit) {
        Commit result = commit;
        List<RevTag> list = tags.get(commit.id());

        if (list != null)
            for (RevTag tag : list)
                result = result.tag(tag.getTagName(), tag.getFullMessage());

        return result;
    }

    private static final Comparator<RevCommit> COMMIT_COMPARATOR = (o1, o2) -> o1.getCommitTime() - o2.getCommitTime();

    private final Commit commit(Commit commit, Status status) {
        List<File> uncommitted = new ArrayList<>();
        List<File> untracked = new ArrayList<>();

        add(uncommitted, status.getAdded());
        add(uncommitted, status.getChanged());
        del(uncommitted, status.getRemoved());

        String message = null;

        // [#9506] TODO: It should be possible to migrate to uncommitted changes in dev mode.
        if (!uncommitted.isEmpty())
            commit = commit.commit(message = "uncommitted", message, uncommitted).valid(false);

        add(untracked, status.getModified());
        add(untracked, status.getUntracked());
        del(untracked, status.getMissing());

        if (!untracked.isEmpty())
            commit = commit.commit(message = message == null ? "untracked" : "uncommitted-and-untracked", message, untracked).valid(false);

        return commit;
    }

    private void add(List<File> files, Set<String> paths) {
        for (String path : paths) {
            ContentType contentType = contentType(path);

            if (contentType != null)
                files.add(read(path, contentType));
        }
    }

    private final void del(List<File> files, Set<String> paths) {
        for (String path : paths) {
            ContentType contentType = contentType(path);

            if (contentType != null)
                files.add(migrations.file(path, null, contentType));
        }
    }

    private final File read(String path, ContentType contentType) {
        try {
            return migrations.file(
                path,
                new String(java.nio.file.Files.readAllBytes(new java.io.File(git.repository(), path).toPath())),
                contentType
            );
        }
        catch (IOException e) {
            throw new GitException("Cannot read file", e);
        }
    }

    private final List<File> editFiles(Repository repository, RevCommit revCommit) throws Exception {
        if (revCommit.getParentCount() == 0)
            return allFiles(repository, revCommit);

        List<File> files = new ArrayList<>();
        try (DiffFormatter formatter = new DiffFormatter(DisabledOutputStream.INSTANCE)) {
            formatter.setRepository(repository);

            entryLoop:
            for (DiffEntry entry : formatter.scan(revCommit.getParent(0), revCommit)) {
                String oldPath = entry.getOldPath();
                String newPath = entry.getNewPath();

                ContentType oldType = contentType(oldPath);
                ContentType newType = contentType(newPath);
                ChangeType changeType = entry.getChangeType();

                if (newType == null && oldType == null)
                    continue entryLoop;

                if (changeType != DELETE)
                    if (newType == null)
                        changeType = DELETE;
                    else if (oldType == null)
                        changeType = ADD;
                    else if (oldType != newType)
                        changeType = RENAME;

                switch (changeType) {
                    case ADD:
                    case MODIFY:
                    case COPY:
                        files.add(migrations.file(newPath, read(repository, revCommit, newPath), newType));
                        break;

                    case RENAME:
                        files.add(migrations.file(oldPath, null, oldType));
                        files.add(migrations.file(newPath, read(repository, revCommit, newPath), newType));
                        break;

                    case DELETE:
                        files.add(migrations.file(oldPath, null, oldType));
                        break;

                    default:
                        throw new UnsupportedOperationException("" + changeType);
                }
            }
        }

        return files;
    }

    private final ContentType contentType(String path) {
        return incrementFilePattern.matches(path) ? INCREMENT :
               schemaFilePattern.matches(path) ? SCHEMA :
               null;
    }

    private final List<File> allFiles(Repository repository, RevCommit revCommit) throws Exception {
        List<File> files = new ArrayList<>();

        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(revCommit.getTree());
        treeWalk.setRecursive(false);

        while (treeWalk.next()) {
            String path = treeWalk.getPathString();

            if (treeWalk.isSubtree() && include(path)) {
                treeWalk.enterSubtree();
            }
            else {
                ContentType contentType = contentType(path);

                if (contentType != null) {
                    files.add(migrations.file(
                        path,
                        read(repository, revCommit, path),
                        contentType
                    ));
                }
            }
        }

        return files;
    }

    private final boolean include(String path) {

        // [#9506] TODO: resolve . and ..
        return git.basedir().startsWith(path)
            || path.startsWith(git.basedir());
    }

    private final String read(Repository repository, RevCommit commit, String path) throws IOException {
        try (TreeWalk treeWalk = TreeWalk.forPath(repository, path, commit.getTree())) {
            ObjectId blobId = treeWalk.getObjectId(0);
            try (ObjectReader objectReader = repository.newObjectReader()) {
                ObjectLoader objectLoader = objectReader.open(blobId);
                byte[] bytes = objectLoader.getBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            }
        }
    }
}
