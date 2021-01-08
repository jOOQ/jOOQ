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
package org.jooq.migrations.jgit;

import static java.util.Arrays.asList;
import static org.eclipse.jgit.diff.DiffEntry.ChangeType.ADD;
import static org.eclipse.jgit.diff.DiffEntry.ChangeType.DELETE;
import static org.eclipse.jgit.diff.DiffEntry.ChangeType.RENAME;
import static org.jooq.ContentType.INCREMENT;
import static org.jooq.ContentType.SCHEMA;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jooq.Commit;
import org.jooq.CommitProvider;
import org.jooq.Commits;
import org.jooq.Configuration;
import org.jooq.ContentType;
import org.jooq.DSLContext;
import org.jooq.File;
import org.jooq.FilePattern;
import org.jooq.impl.Migrations;
import org.jooq.tools.JooqLogger;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

/**
 * A {@link CommitProvider} that produces versions from a git repository.
 *
 * @author Lukas Eder
 */
public final class GitCommitProvider implements CommitProvider {

    private static final JooqLogger log = JooqLogger.getLogger(GitCommitProvider.class);

    private final DSLContext        dsl;
    private final GitConfiguration  git;
    private final FilePattern       incrementFilePattern;
    private final FilePattern       schemaFilePattern;

    public GitCommitProvider(Configuration configuration, GitConfiguration git) {
        this.dsl = configuration.dsl();
        this.git = git;
        this.incrementFilePattern = new FilePattern().pattern(git.incrementFilePattern());
        this.schemaFilePattern = new FilePattern().pattern(git.schemaFilePattern());
    }

    @Override
    public final Commits provide() {
        Commits commits = Migrations.commits(dsl.configuration());

        try (
            Git g = Git.open(git.repository());
            Repository r = g.getRepository()
        ) {
            List<RevCommit> revCommits = new ArrayList<>();
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

            // The commits seem to come in reverse order from jgit.
            Collections.reverse(revCommits);
            Commit init = commits.root();

            // TODO: This algorithm is quadradic in the worst case. Can we find a better one?
            // TODO: We collect all the commits from git, when we could ignore the empty ones
            while (!revCommits.isEmpty()) {
                Iterator<RevCommit> it = revCommits.iterator();

                commitLoop:
                while (it.hasNext()) {
                    RevCommit revCommit = it.next();

                    if (revCommit.getParents() == null || revCommit.getParents().length == 0) {
                        commits.add(init.commit(revCommit.getName(), revCommit.getFullMessage(), editFiles(r, revCommit)));
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
                            commits.add(parents[0].commit(revCommit.getName(), revCommit.getFullMessage(), editFiles(r, revCommit)));
                        else if (parents.length == 2)
                            commits.add(parents[0].merge(revCommit.getName(), revCommit.getFullMessage(), parents[1], editFiles(r, revCommit)));
                        else
                            throw new UnsupportedOperationException("Merging more than two parents not yet supported");

                        it.remove();
                    }
                }
            }

            Status status = g.status().call();
            if (status.hasUncommittedChanges() || !status.getUntracked().isEmpty())
                commits.add(commit(last != null ? commits.get(last.getName()) : init, status));
        }
        catch (Exception e) {
            throw new GitException("Error while providing git versions", e);
        }

        return commits;
    }

    private static final Comparator<RevCommit> COMMIT_COMPARATOR = (o1, o2) -> o1.getCommitTime() - o2.getCommitTime();

    private final Commit commit(Commit commit, Status status) {
        List<File> files = new ArrayList<>();

        add(files, status.getAdded());
        add(files, status.getChanged());
        add(files, status.getModified());
        add(files, status.getUntracked());
        del(files, status.getMissing());
        del(files, status.getRemoved());

        return commit.commit("uncommitted", "uncommitted", files);
    }

    private void add(List<File> files, Set<String> paths) {
        for (String path : paths) {
            ContentType contentType = contentType(path);

            if (contentType != null)
                files.add(read(path, contentType));
        }
    }

    private void del(List<File> files, Set<String> paths) {
        for (String path : paths) {
            ContentType contentType = contentType(path);

            if (contentType != null)
                files.add(Migrations.file(path, null, contentType));
        }
    }

    private File read(String path, ContentType contentType) {
        try {
            return Migrations.file(
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
                        files.add(Migrations.file(newPath, read(repository, revCommit, newPath), newType));
                        break;

                    case RENAME:
                        files.add(Migrations.file(oldPath, null, oldType));
                        files.add(Migrations.file(newPath, read(repository, revCommit, newPath), newType));
                        break;

                    case DELETE:
                        files.add(Migrations.file(oldPath, null, oldType));
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
            if (treeWalk.isSubtree()) {
                treeWalk.enterSubtree();
            }
            else {
                ContentType contentType = contentType(treeWalk.getPathString());

                if (contentType != null)
                    files.add(Migrations.file(
                        treeWalk.getPathString(),
                        read(repository, revCommit, treeWalk.getPathString()),
                        contentType
                    ));
            }
        }

        return files;
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
