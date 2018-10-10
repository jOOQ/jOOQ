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

import static org.jooq.impl.Keywords.K_AS;
import static org.jooq.impl.SelectQueryImpl.SUPPORT_WINDOW_CLAUSE;
import static org.jooq.impl.Tools.DataKey.DATA_WINDOW_DEFINITIONS;

import java.util.Collection;

import org.jooq.Clause;
import org.jooq.Context;
import org.jooq.Name;
import org.jooq.OrderField;
import org.jooq.WindowDefinition;
import org.jooq.WindowSpecification;
import org.jooq.WindowSpecificationExcludeStep;
import org.jooq.WindowSpecificationRowsAndStep;
import org.jooq.WindowSpecificationRowsStep;

/**
 * @author Lukas Eder
 */
final class WindowDefinitionImpl extends AbstractQueryPart implements WindowDefinition {

    /**
     * Generated UID
     */
    private static final long         serialVersionUID = -7779419148766154430L;

    private final Name                name;
    private final WindowSpecification window;

    WindowDefinitionImpl(Name name, WindowSpecification window) {
        this.name = name;
        this.window = window;
    }

    final Name getName() {
        return name;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // In the WINDOW clause, always declare window definitions
        if (ctx.declareWindows()) {
            ctx.visit(name)
               .sql(' ')
               .visit(K_AS)
               .sql(" (");

            if (window != null)
                ctx.visit(window);

            ctx.sql(')');
        }

        // Outside the WINDOW clause, only few dialects actually support
        // referencing WINDOW definitions
        else if (SUPPORT_WINDOW_CLAUSE.contains(ctx.family())) {
            ctx.visit(name);
        }

        // When emulating, just repeat the window specification
        else if (window != null) {
            ctx.visit(window);
        }

        // Try looking up the window specification from the context
        else {

            @SuppressWarnings("unchecked")
            QueryPartList<WindowDefinition> windows = (QueryPartList<WindowDefinition>) ctx.data(DATA_WINDOW_DEFINITIONS);

            renderContextDefinitionOrName:
            if (windows != null) {

                windowLoop:
                for (WindowDefinition w : windows) {
                    if (((WindowDefinitionImpl) w).getName().equals(name)) {

                        // Prevent StackOverflowError
                        if (w == this)
                            break windowLoop;

                        ctx.visit(w);
                        break renderContextDefinitionOrName;
                    }
                }

                // [#7296] This is an empty window specification if we reach this far
            }
        }
    }

    @Override
    public final boolean declaresWindows() {
        return true;
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    // ------------------------------------------------------------------------
    // XXX: WindowSpecification API
    // ------------------------------------------------------------------------

    @Override
    public final WindowSpecificationRowsStep orderBy(OrderField<?>... fields) {
        return new WindowSpecificationImpl(this).orderBy(fields);
    }

    @Override
    public final WindowSpecificationRowsStep orderBy(Collection<? extends OrderField<?>> fields) {
        return new WindowSpecificationImpl(this).orderBy(fields);
    }

    @Override
    public final WindowSpecificationExcludeStep rowsUnboundedPreceding() {
        return new WindowSpecificationImpl(this).rowsUnboundedPreceding();
    }

    @Override
    public final WindowSpecificationExcludeStep rowsPreceding(int number) {
        return new WindowSpecificationImpl(this).rowsPreceding(number);
    }

    @Override
    public final WindowSpecificationExcludeStep rowsCurrentRow() {
        return new WindowSpecificationImpl(this).rowsCurrentRow();
    }

    @Override
    public final WindowSpecificationExcludeStep rowsUnboundedFollowing() {
        return new WindowSpecificationImpl(this).rowsUnboundedFollowing();
    }

    @Override
    public final WindowSpecificationExcludeStep rowsFollowing(int number) {
        return new WindowSpecificationImpl(this).rowsFollowing(number);
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenUnboundedPreceding() {
        return new WindowSpecificationImpl(this).rowsBetweenUnboundedPreceding();
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenPreceding(int number) {
        return new WindowSpecificationImpl(this).rowsBetweenPreceding(number);
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenCurrentRow() {
        return new WindowSpecificationImpl(this).rowsBetweenCurrentRow();
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenUnboundedFollowing() {
        return new WindowSpecificationImpl(this).rowsBetweenUnboundedFollowing();
    }

    @Override
    public final WindowSpecificationRowsAndStep rowsBetweenFollowing(int number) {
        return new WindowSpecificationImpl(this).rowsBetweenFollowing(number);
    }

    @Override
    public final WindowSpecificationExcludeStep rangeUnboundedPreceding() {
        return new WindowSpecificationImpl(this).rangeUnboundedPreceding();
    }

    @Override
    public final WindowSpecificationExcludeStep rangePreceding(int number) {
        return new WindowSpecificationImpl(this).rangePreceding(number);
    }

    @Override
    public final WindowSpecificationExcludeStep rangeCurrentRow() {
        return new WindowSpecificationImpl(this).rangeCurrentRow();
    }

    @Override
    public final WindowSpecificationExcludeStep rangeUnboundedFollowing() {
        return new WindowSpecificationImpl(this).rangeUnboundedFollowing();
    }

    @Override
    public final WindowSpecificationExcludeStep rangeFollowing(int number) {
        return new WindowSpecificationImpl(this).rangeFollowing(number);
    }

    @Override
    public final WindowSpecificationRowsAndStep rangeBetweenUnboundedPreceding() {
        return new WindowSpecificationImpl(this).rangeBetweenUnboundedPreceding();
    }

    @Override
    public final WindowSpecificationRowsAndStep rangeBetweenPreceding(int number) {
        return new WindowSpecificationImpl(this).rangeBetweenPreceding(number);
    }

    @Override
    public final WindowSpecificationRowsAndStep rangeBetweenCurrentRow() {
        return new WindowSpecificationImpl(this).rangeBetweenCurrentRow();
    }

    @Override
    public final WindowSpecificationRowsAndStep rangeBetweenUnboundedFollowing() {
        return new WindowSpecificationImpl(this).rangeBetweenUnboundedFollowing();
    }

    @Override
    public final WindowSpecificationRowsAndStep rangeBetweenFollowing(int number) {
        return new WindowSpecificationImpl(this).rangeBetweenFollowing(number);
    }

    @Override
    public final WindowSpecificationExcludeStep groupsUnboundedPreceding() {
        return new WindowSpecificationImpl(this).groupsUnboundedPreceding();
    }

    @Override
    public final WindowSpecificationExcludeStep groupsPreceding(int number) {
        return new WindowSpecificationImpl(this).groupsPreceding(number);
    }

    @Override
    public final WindowSpecificationExcludeStep groupsCurrentRow() {
        return new WindowSpecificationImpl(this).groupsCurrentRow();
    }

    @Override
    public final WindowSpecificationExcludeStep groupsUnboundedFollowing() {
        return new WindowSpecificationImpl(this).groupsUnboundedFollowing();
    }

    @Override
    public final WindowSpecificationExcludeStep groupsFollowing(int number) {
        return new WindowSpecificationImpl(this).groupsFollowing(number);
    }

    @Override
    public final WindowSpecificationRowsAndStep groupsBetweenUnboundedPreceding() {
        return new WindowSpecificationImpl(this).groupsBetweenUnboundedPreceding();
    }

    @Override
    public final WindowSpecificationRowsAndStep groupsBetweenPreceding(int number) {
        return new WindowSpecificationImpl(this).groupsBetweenPreceding(number);
    }

    @Override
    public final WindowSpecificationRowsAndStep groupsBetweenCurrentRow() {
        return new WindowSpecificationImpl(this).groupsBetweenCurrentRow();
    }

    @Override
    public final WindowSpecificationRowsAndStep groupsBetweenUnboundedFollowing() {
        return new WindowSpecificationImpl(this).groupsBetweenUnboundedFollowing();
    }

    @Override
    public final WindowSpecificationRowsAndStep groupsBetweenFollowing(int number) {
        return new WindowSpecificationImpl(this).groupsBetweenFollowing(number);
    }
}
