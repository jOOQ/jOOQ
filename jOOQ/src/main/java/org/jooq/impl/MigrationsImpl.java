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

import static java.util.Collections.emptyList;

import org.jooq.Commit;
import org.jooq.Commits;
import org.jooq.DSLContext;
import org.jooq.Migration;
import org.jooq.Migrations;
import org.jooq.Version;
import org.jooq.Versions;

/**
 * @author Lukas Eder
 */
final class MigrationsImpl implements Migrations {

    final DSLContext ctx;

    MigrationsImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public final Version version(String id) {
        return new VersionImpl(ctx, id, null, new Version[0]);
    }

    @Override
    public final Versions versions() {
        return new VersionsImpl(version("init"));
    }

    @Override
    public final Commit commit(String id) {
        return new CommitImpl(ctx.configuration(), id, null, emptyList(), emptyList());
    }

    @Override
    public final Commits commits() {
        return new CommitsImpl(ctx.configuration(), commit("init"));
    }

    @Override
    public final Migration migrateTo(Commit to) {
        return new MigrationImpl(ctx.configuration(), to);
    }
}
