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
package org.jooq;

// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;
import static org.jooq.TableOptions.OnCommit.DELETE_ROWS;
import static org.jooq.TableOptions.OnCommit.DROP;
import static org.jooq.TableOptions.OnCommit.PRESERVE_ROWS;

import java.io.Serializable;

import org.jooq.impl.QOM.TableCommitAction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A description of various additional {@link Table} options to describe the
 * table runtime meta model.
 *
 * @author Lukas Eder
 */
public final class TableOptions implements Serializable {

    private static final TableOptions C_EXPRESSION        = new TableOptions(TableType.EXPRESSION);
    private static final TableOptions C_FUNCTION          = new TableOptions(TableType.FUNCTION);
    private static final TableOptions C_MATERIALIZED_VIEW = materializedView((String) null);
    private static final TableOptions C_TABLE             = new TableOptions(TableType.TABLE);
    private static final TableOptions C_GLOBAL_TEMPORARY  = new TableOptions(TableType.GLOBAL_TEMPORARY);
    private static final TableOptions C_LOCAL_TEMPORARY   = new TableOptions(TableType.LOCAL_TEMPORARY);
    private static final TableOptions C_VIEW              = view((String) null);

    private final TableType           type;
    private final OnCommit            onCommit;
    private final Select<?>           select;
    private final String              source;

    private TableOptions(TableType type) {
        this.type = type;
        this.onCommit = null;
        this.select = null;
        this.source = null;
    }

    private TableOptions(OnCommit onCommit) {
        this.type = TableType.GLOBAL_TEMPORARY;
        this.onCommit = onCommit;
        this.select = null;
        this.source = null;
    }

    private TableOptions(TableType type, Select<?> select) {
        this.type = type;
        this.onCommit = null;
        this.select = select;
        this.source = select == null ? null : select.toString();
    }

    private TableOptions(TableType type, String source) {
        this.type = type;
        this.onCommit = null;
        this.select = null;
        this.source = source;
    }

    /**
     * Get a new {@link TableOptions} object for a given table type.
     */
    @NotNull
    public static final TableOptions of(TableType tableType) {
        switch (tableType) {
            case EXPRESSION:
                return expression();
            case FUNCTION:
                return function();
            case MATERIALIZED_VIEW:
                return materializedView();
            case GLOBAL_TEMPORARY:
                return globalTemporaryTable();
            case LOCAL_TEMPORARY:
                return localTemporaryTable();
            case TEMPORARY:
                return temporaryTable();
            case VIEW:
                return view();
            case TABLE:
            case UNKNOWN:
            default:
                return table();
        }
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#TABLE}.
     */
    @NotNull
    public static final TableOptions table() {
        return C_TABLE;
    }

    /**
     * Create a new {@link TableOptions} object for a
     * {@link TableType#GLOBAL_TEMPORARY} table.
     *
     * @deprecated - 3.21.0 - [#18626] - Use {@link #globalTemporaryTable()} or
     *             {@link #localTemporaryTable()} instead.
     */
    @Deprecated(forRemoval = true)
    @NotNull
    public static final TableOptions temporaryTable() {
        return globalTemporaryTable();
    }

    /**
     * Create a new {@link TableOptions} object for a
     * {@link TableType#GLOBAL_TEMPORARY} table.
     *
     * @deprecated - 3.21.0 - [#18626] - Use
     *             {@link #globalTemporaryTable(OnCommit)}.
     */
    @Deprecated(forRemoval = true)
    @NotNull
    public static final TableOptions temporaryTable(OnCommit onCommit) {
        return globalTemporaryTable(onCommit);
    }

    /**
     * Create a new {@link TableOptions} object for a
     * {@link TableType#GLOBAL_TEMPORARY} table.
     *
     * @deprecated - 3.21.0 - [#18626] - Use
     *             {@link #globalTemporaryTable(TableCommitAction)}.
     */
    @Deprecated(forRemoval = true)
    @NotNull
    public static final TableOptions temporaryTable(TableCommitAction onCommit) {
        return globalTemporaryTable(onCommit);
    }

    /**
     * Create a new {@link TableOptions} object for a
     * {@link TableType#GLOBAL_TEMPORARY} table.
     */
    @NotNull
    public static final TableOptions globalTemporaryTable() {
        return C_GLOBAL_TEMPORARY;
    }

    /**
     * Create a new {@link TableOptions} object for a
     * {@link TableType#GLOBAL_TEMPORARY} table.
     */
    @NotNull
    public static final TableOptions globalTemporaryTable(OnCommit onCommit) {
        return new TableOptions(onCommit);
    }

    /**
     * Create a new {@link TableOptions} object for a
     * {@link TableType#GLOBAL_TEMPORARY} table.
     */
    @NotNull
    public static final TableOptions globalTemporaryTable(TableCommitAction onCommit) {
        if (onCommit == null)
            return new TableOptions((OnCommit) null);

        switch (onCommit) {
            case DELETE_ROWS: return globalTemporaryTable(DELETE_ROWS);
            case PRESERVE_ROWS: return globalTemporaryTable(PRESERVE_ROWS);
            case DROP: return globalTemporaryTable(DROP);
            default: throw new IllegalArgumentException("TableCommitAction not supported: " + onCommit);
        }
    }

    /**
     * Create a new {@link TableOptions} object for a
     * {@link TableType#LOCAL_TEMPORARY} table.
     */
    @NotNull
    public static final TableOptions localTemporaryTable() {
        return C_LOCAL_TEMPORARY;
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#VIEW} of
     * unknown content.
     */
    @NotNull
    public static final TableOptions view() {
        return C_VIEW;
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#VIEW}.
     */
    @NotNull
    public static final TableOptions view(Select<?> select) {
        return new TableOptions(TableType.VIEW, select);
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#VIEW}.
     */
    @NotNull
    public static final TableOptions view(String source) {
        return new TableOptions(TableType.VIEW, source);
    }

    /**
     * Create a new {@link TableOptions} object for a
     * {@link TableType#MATERIALIZED_VIEW} of unknown content.
     */
    @NotNull
    public static final TableOptions materializedView() {
        return C_MATERIALIZED_VIEW;
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#MATERIALIZED_VIEW}.
     */
    @NotNull
    public static final TableOptions materializedView(Select<?> select) {
        return new TableOptions(TableType.MATERIALIZED_VIEW, select);
    }

    /**
     * Create a new {@link TableOptions} object for a
     * {@link TableType#MATERIALIZED_VIEW} of unknown content.
     */
    @NotNull
    public static final TableOptions materializedView(String source) {
        return new TableOptions(TableType.MATERIALIZED_VIEW, source);
    }


    /**
     * Create a new {@link TableOptions} object for a {@link TableType#EXPRESSION}.
     */
    @NotNull
    public static final TableOptions expression() {
        return C_EXPRESSION;
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#FUNCTION}.
     */
    @NotNull
    public static final TableOptions function() {
        return C_FUNCTION;
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#FUNCTION}.
     */
    @NotNull
    public static final TableOptions function(String source) {
        return new TableOptions(TableType.FUNCTION, source);
    }

    /**
     * The table type.
     * <p>
     * This is never <code>null</code>.
     */
    @NotNull
    public final TableType type() {
        return type;
    }

    /**
     * The <code>ON COMMIT</code> flag for {@link TableType#GLOBAL_TEMPORARY}
     * tables.
     * <p>
     * This may be <code>null</code>, if it is undefined, or unknown, or if the
     * table is not a {@link TableType#GLOBAL_TEMPORARY} table.
     */
    @Nullable
    public final OnCommit onCommit() {
        return onCommit;
    }

    /**
     * The <code>SELECT</code> statement defining this {@link TableType#VIEW} or
     * {@link TableType#MATERIALIZED_VIEW}.
     * <p>
     * This may be <code>null</code>, if it is undefined, or unknown, or if the
     * table is not a view.
     */
    @Nullable
    public final Select<?> select() {
        return select;
    }

    /**
     * The <code>SELECT</code> statement defining this {@link TableType#VIEW} or
     * {@link TableType#MATERIALIZED_VIEW}, in source form.
     * <p>
     * This may be <code>null</code>, if it is undefined, or unknown, or if the
     * table is not a view.
     */
    @Nullable
    public final String source() {
        return source;
    }

    /**
     * A description of the type of a {@link Table}.
     */
    public enum TableType {

        /**
         * An ordinary table that is stored in the schema.
         */
        TABLE,

        /**
         * A global temporary table that is stored in the schema and visible to
         * everyone.
         *
         * @deprecated - 3.21.0 - [#18626] - Use {@link #GLOBAL_TEMPORARY} or
         *             {@link #LOCAL_TEMPORARY} instead.
         */
        @Deprecated(forRemoval = true)
        TEMPORARY,

        /**
         * A global temporary table that is stored in the schema and visible to
         * everyone.
         */
        GLOBAL_TEMPORARY,

        /**
         * A local temporary table that is stored in the schema and visible to
         * everyone.
         */
        LOCAL_TEMPORARY,

        /**
         * A view that is defined by a {@link Select} statement.
         */
        VIEW,

        /**
         * A materialised view that is defined by a {@link Select} statement, and
         * whose data is materialised in the schema.
         */
        MATERIALIZED_VIEW,

        /**
         * A table valued function that is defined by a {@link Routine}.
         */
        FUNCTION,

        /**
         * A table expression, such as a derived table, a joined table, a common
         * table expression, etc.
         */
        EXPRESSION,

        /**
         * A table type that is unknown to jOOQ.
         */
        UNKNOWN;

        /**
         * Whether the type is a view or a materialized view.
         */
        public final boolean isView() {
            return this == VIEW || this == MATERIALIZED_VIEW;
        }

        /**
         * Whether the type is a function.
         */
        public final boolean isFunction() {
            return this == FUNCTION;
        }

        /**
         * Whether the type is a table or a temporary table.
         */
        public final boolean isTable() {
            return this == TABLE
                || this == TEMPORARY
                || this == GLOBAL_TEMPORARY
                || this == LOCAL_TEMPORARY;
        }
    }

    /**
     * The <code>ON COMMIT</code> flag for {@link TableType#GLOBAL_TEMPORARY} tables.
     */
    public enum OnCommit {

        @Support({ POSTGRES })
        DELETE_ROWS,

        @Support({ POSTGRES })
        PRESERVE_ROWS,

        @Support({ POSTGRES })
        DROP;
    }

    // -------------------------------------------------------------------------
    // XXX: Object API
    // -------------------------------------------------------------------------

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((onCommit == null) ? 0 : onCommit.hashCode());
        result = prime * result + ((select == null) ? 0 : select.hashCode());
        result = prime * result + ((source == null) ? 0 : source.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TableOptions other = (TableOptions) obj;
        if (onCommit != other.onCommit)
            return false;
        if (select == null) {
            if (other.select != null)
                return false;
        }
        else if (!select.equals(other.select))
            return false;
        if (source == null) {
            if (other.source != null)
                return false;
        }
        else if (!source.equals(other.source))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TableOptions[" + type + "]";
    }
}
