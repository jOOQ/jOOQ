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

import static org.jooq.conf.InvocationOrder.REVERSE;

import java.util.Arrays;

import org.jooq.Configuration;
import org.jooq.MigrationContext;
import org.jooq.MigrationListener;
import org.jooq.MigrationListenerProvider;

/**
 * @author Lukas Eder
 */
class MigrationListeners implements MigrationListener {

    private final MigrationListener[] listeners;

    MigrationListeners(Configuration configuration) {
        MigrationListenerProvider[] providers = configuration.migrationListenerProviders();
        listeners = new MigrationListener[providers.length];

        for (int i = 0; i < providers.length; i++)
            listeners[i] = providers[i].provide();
    }

    @Override
    public final void migrationStart(MigrationContext ctx) {
        for (MigrationListener listener : ctx.settings().getMigrationListenerStartInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.migrationStart(ctx);
    }

    @Override
    public final void migrationEnd(MigrationContext ctx) {
        for (MigrationListener listener : ctx.settings().getMigrationListenerEndInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.migrationEnd(ctx);
    }

    @Override
    public final void queriesStart(MigrationContext ctx) {
        for (MigrationListener listener : ctx.settings().getMigrationListenerStartInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.queriesStart(ctx);
    }

    @Override
    public final void queriesEnd(MigrationContext ctx) {
        for (MigrationListener listener : ctx.settings().getMigrationListenerEndInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.queriesEnd(ctx);
    }

    @Override
    public final void queryStart(MigrationContext ctx) {
        for (MigrationListener listener : ctx.settings().getMigrationListenerStartInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.queryStart(ctx);
    }

    @Override
    public final void queryEnd(MigrationContext ctx) {
        for (MigrationListener listener : ctx.settings().getMigrationListenerEndInvocationOrder() != REVERSE
            ? Arrays.asList(listeners)
            : Tools.reverseIterable(listeners))
            listener.queryEnd(ctx);
    }

}
