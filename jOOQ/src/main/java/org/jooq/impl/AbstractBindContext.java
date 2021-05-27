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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jooq.BindContext;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.QueryPart;
import org.jooq.QueryPartInternal;
import org.jooq.exception.DataAccessException;

/**
 * A base class for {@link BindContext} implementations
 *
 * @author Lukas Eder
 */
abstract class AbstractBindContext extends AbstractContext<BindContext> implements BindContext {

    AbstractBindContext(Configuration configuration, PreparedStatement stmt) {
        super(configuration, stmt);
    }

    // ------------------------------------------------------------------------
    // BindContext API
    // ------------------------------------------------------------------------

    @Override
    protected void visit0(QueryPartInternal internal) {
        bindInternal(internal);
    }

    @Override
    public final BindContext bindValue(Object value, Field<?> field) throws DataAccessException {
        try {
            return bindValue0(value, field);
        }
        catch (SQLException e) {
            throw Tools.translate(null, e);
        }
    }

    // ------------------------------------------------------------------------
    // RenderContext API
    // ------------------------------------------------------------------------

    @Override
    public final String peekAlias() {
        return null;
    }

    @Override
    public final String nextAlias() {
        return null;
    }

    @Override
    public final String render() {
        return null;
    }

    @Override
    public final String render(QueryPart part) {
        return null;
    }

    @Override
    public final BindContext keyword(String keyword) {
        return this;
    }

    @Override
    public final BindContext sql(String sql) {
        return this;
    }

    @Override
    public final BindContext sql(String sql, boolean literal) {
        return this;
    }

    @Override
    public final BindContext sqlIndentStart(String sql) {
        return this;
    }

    @Override
    public final BindContext sqlIndentEnd(String sql) {
        return this;
    }

    @Override
    public final BindContext sqlIndentStart() {
        return this;
    }

    @Override
    public final BindContext sqlIndentEnd() {
        return this;
    }

    @Override
    public final BindContext sql(char sql) {
        return this;
    }

    @Override
    public final BindContext sqlIndentStart(char sql) {
        return this;
    }

    @Override
    public final BindContext sqlIndentEnd(char sql) {
        return this;
    }

    @Override
    public final BindContext sql(int sql) {
        return this;
    }

    @Override
    public final BindContext sql(long sql) {
        return this;
    }

    @Override
    public final BindContext sql(float sql) {
        return this;
    }

    @Override
    public final BindContext sql(double sql) {
        return this;
    }

    @Override
    public final BindContext format(boolean format) {
        return this;
    }

    @Override
    public final boolean format() {
        return false;
    }

    @Override
    public final BindContext formatNewLine() {
        return this;
    }

    @Override
    public final BindContext formatNewLineAfterPrintMargin() {
        return this;
    }

    @Override
    public final BindContext formatSeparator() {
        return this;
    }

    @Override
    public final BindContext separatorRequired(boolean separatorRequired) {
        return this;
    }

    @Override
    public final boolean separatorRequired() {
        return false;
    }

    @Override
    public final BindContext formatIndentStart() {
        return this;
    }

    @Override
    public final BindContext formatIndentStart(int indent) {
        return this;
    }

    @Override
    public final BindContext formatIndentLockStart() {
        return this;
    }

    @Override
    public final BindContext formatIndentEnd() {
        return this;
    }

    @Override
    public final BindContext formatIndentEnd(int indent) {
        return this;
    }

    @Override
    public final BindContext formatIndentLockEnd() {
        return this;
    }

    @Override
    public final BindContext formatPrintMargin(int margin) {
        return this;
    }

    @Override
    public final BindContext literal(String literal) {
        return this;
    }

    // ------------------------------------------------------------------------
    // AbstractBindContext template methods
    // ------------------------------------------------------------------------

    /**
     * Subclasses may override this method to achieve different behaviour
     */
    protected void bindInternal(QueryPartInternal internal) {
        internal.accept(this);
    }

    /**
     * Subclasses may override this method to achieve different behaviour
     */
    @SuppressWarnings("unused")
    protected BindContext bindValue0(Object value, Field<?> field) throws SQLException {
        return this;
    }

    // ------------------------------------------------------------------------
    // Object API
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }
}
