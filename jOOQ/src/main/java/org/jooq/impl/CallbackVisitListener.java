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


import java.util.function.Consumer;

import org.jooq.VisitContext;
import org.jooq.VisitListener;

/**
 * A {@link VisitListener} that allows for functional composition.
 * <p>
 * For example: <code><pre>
 * VisitListener listener = VisitListener
 *   .onVisitStart(ctx -&gt; something())
 *   .onVisitEnd(ctx -&gt; something());
 * </pre></code>
 *
 * @author Lukas Eder
 */
public final class CallbackVisitListener implements VisitListener {

    private final Consumer<? super VisitContext> onClauseStart;
    private final Consumer<? super VisitContext> onClauseEnd;
    private final Consumer<? super VisitContext> onVisitStart;
    private final Consumer<? super VisitContext> onVisitEnd;

    public CallbackVisitListener() {
        this(null, null, null, null);
    }

    private CallbackVisitListener(
        Consumer<? super VisitContext> onClauseStart,
        Consumer<? super VisitContext> onClauseEnd,
        Consumer<? super VisitContext> onVisitStart,
        Consumer<? super VisitContext> onVisitEnd
    ) {
        this.onClauseStart = onClauseStart;
        this.onClauseEnd = onClauseEnd;
        this.onVisitStart = onVisitStart;
        this.onVisitEnd = onVisitEnd;
    }

    @Override
    public final void clauseStart(VisitContext context) {
        if (onClauseStart != null)
            onClauseStart.accept(context);
    }

    @Override
    public final void clauseEnd(VisitContext context) {
        if (onClauseEnd != null)
            onClauseEnd.accept(context);
    }

    @Override
    public final void visitStart(VisitContext context) {
        if (onVisitStart != null)
            onVisitStart.accept(context);
    }

    @Override
    public final void visitEnd(VisitContext context) {
        if (onVisitEnd != null)
            onVisitEnd.accept(context);
    }

    public final CallbackVisitListener onClauseStart(Consumer<? super VisitContext> newOnClauseStart) {
        return new CallbackVisitListener(
            newOnClauseStart,
            onClauseEnd,
            onVisitStart,
            onVisitEnd
        );
    }

    public final CallbackVisitListener onClauseEnd(Consumer<? super VisitContext> newOnClauseEnd) {
        return new CallbackVisitListener(
            onClauseStart,
            newOnClauseEnd,
            onVisitStart,
            onVisitEnd
        );
    }

    public final CallbackVisitListener onVisitStart(Consumer<? super VisitContext> newOnVisitStart) {
        return new CallbackVisitListener(
            onClauseStart,
            onClauseEnd,
            newOnVisitStart,
            onVisitEnd
        );
    }

    public final CallbackVisitListener onVisitEnd(Consumer<? super VisitContext> newOnVisitEnd) {
        return new CallbackVisitListener(
            onClauseStart,
            onClauseEnd,
            onVisitStart,
            newOnVisitEnd
        );
    }
}

