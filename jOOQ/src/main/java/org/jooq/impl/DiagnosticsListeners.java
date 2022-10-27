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

import static java.lang.Boolean.FALSE;
import static org.jooq.impl.Tools.map;

import java.util.function.Predicate;

import org.jooq.Configuration;
import org.jooq.DiagnosticsContext;
import org.jooq.DiagnosticsListener;
import org.jooq.DiagnosticsListenerProvider;
import org.jooq.conf.Settings;

/**
 * @author Lukas Eder
 */
final class DiagnosticsListeners implements DiagnosticsListener {

    final DiagnosticsListener[] listeners;

    DiagnosticsListeners(DiagnosticsListenerProvider[] providers) {
        listeners = map(providers, p -> p.provide(), DiagnosticsListener[]::new);
    }

    static final DiagnosticsListeners get(Configuration configuration) {
        return new DiagnosticsListeners(configuration.diagnosticsListenerProviders());
    }

    private static final boolean check(DiagnosticsContext ctx, Predicate<? super Settings> test) {
        return check(ctx.settings(), test);
    }

    static final boolean check(Settings settings, Predicate<? super Settings> test) {
        return !FALSE.equals(test.test(settings));
    }

    private static final boolean checkPattern(DiagnosticsContext ctx, Predicate<? super Settings> test) {
        return checkPattern(ctx.settings(), test);
    }

    static final boolean checkPattern(Settings settings, Predicate<? super Settings> test) {
        return !FALSE.equals(settings.isDiagnosticsPatterns()) && check(settings, test);
    }

    @Override
    public final void tooManyRowsFetched(DiagnosticsContext ctx) {
        if (check(ctx, Settings::isDiagnosticsTooManyRowsFetched))
            for (DiagnosticsListener listener : listeners)
                listener.tooManyRowsFetched(ctx);
    }

    @Override
    public final void tooManyColumnsFetched(DiagnosticsContext ctx) {
        if (check(ctx, Settings::isDiagnosticsTooManyColumnsFetched))
            for (DiagnosticsListener listener : listeners)
                listener.tooManyColumnsFetched(ctx);
    }

    @Override
    public final void unnecessaryWasNullCall(DiagnosticsContext ctx) {
        if (check(ctx, Settings::isDiagnosticsUnnecessaryWasNullCall))
            for (DiagnosticsListener listener : listeners)
                listener.unnecessaryWasNullCall(ctx);
    }

    @Override
    public final void missingWasNullCall(DiagnosticsContext ctx) {
        if (check(ctx, Settings::isDiagnosticsMissingWasNullCall))
            for (DiagnosticsListener listener : listeners)
                listener.missingWasNullCall(ctx);
    }

    @Override
    public final void duplicateStatements(DiagnosticsContext ctx) {
        if (check(ctx, Settings::isDiagnosticsDuplicateStatements))
            for (DiagnosticsListener listener : listeners)
                listener.duplicateStatements(ctx);
    }

    @Override
    public final void repeatedStatements(DiagnosticsContext ctx) {
        if (check(ctx, Settings::isDiagnosticsRepeatedStatements))
            for (DiagnosticsListener listener : listeners)
                listener.repeatedStatements(ctx);
    }

































    @Override
    public final void exception(DiagnosticsContext ctx) {
        for (DiagnosticsListener listener : listeners)
            listener.exception(ctx);
    }
}
