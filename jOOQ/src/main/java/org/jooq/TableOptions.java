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
package org.jooq;

// ...
// ...
import static org.jooq.SQLDialect.POSTGRES;

import java.io.Serializable;

/**
 * A description of various additional {@link Table} options to describe the
 * table runtime meta model.
 *
 * @author Lukas Eder
 */
public final class TableOptions implements Serializable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4840043541516260827L;

    private final TableType   type;
    private final OnCommit    onCommit;
    private final Select<?>   select;

    private TableOptions(TableType type) {
        this.type = type;
        this.onCommit = null;
        this.select = null;
    }

    private TableOptions(OnCommit onCommit) {
        this.type = TableType.TEMPORARY;
        this.onCommit = onCommit;
        this.select = null;
    }

    private TableOptions(TableType type, Select<?> select) {
        this.type = type;
        this.onCommit = null;
        this.select = select;
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#TABLE}.
     */
    public static final TableOptions table() {
        return new TableOptions(TableType.TABLE);
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#TEMPORARY}.
     */
    public static final TableOptions temporaryTable() {
        return new TableOptions(TableType.TEMPORARY);
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#TEMPORARY}.
     */
    public static final TableOptions temporaryTable(OnCommit onCommit) {
        return new TableOptions(onCommit);
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#VIEW} of
     * unknown content.
     */
    public static final TableOptions view() {
        return view(null);
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#VIEW}.
     */
    public static final TableOptions view(Select<?> select) {
        return new TableOptions(TableType.VIEW, select);
    }

    /**
     * Create a new {@link TableOptions} object for a
     * {@link TableType#MATERIALIZED_VIEW} of unknown content.
     */
    public static final TableOptions materializedView() {
        return materializedView(null);
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#MATERIALIZED_VIEW}.
     */
    public static final TableOptions materializedView(Select<?> select) {
        return new TableOptions(TableType.MATERIALIZED_VIEW, select);
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#EXPRESSION}.
     */
    public static final TableOptions expression() {
        return new TableOptions(TableType.EXPRESSION);
    }

    /**
     * Create a new {@link TableOptions} object for a {@link TableType#FUNCTION}.
     */
    public static final TableOptions function() {
        return new TableOptions(TableType.FUNCTION);
    }

    /**
     * The table type.
     * <p>
     * This is never <code>null</code>.
     */
    public final TableType type() {
        return type;
    }

    /**
     * The <code>ON COMMIT</code> flag for {@link TableType#TEMPORARY} tables.
     * <p>
     * This may be <code>null</code>, if it is undefined, or unknown, or if the
     * table is not a {@link TableType#TEMPORARY} table.
     */
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
    public final Select<?> select() {
        return select;
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
         */
        TEMPORARY,

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
         * Whether the type is a view.
         */
        public final boolean isView() {
            return this == VIEW || this == MATERIALIZED_VIEW;
        }

        /**
         * Whether the type is a view.
         */
        public final boolean isTable() {
            return this == TABLE || this == TEMPORARY;
        }
    }

    /**
     * The <code>ON COMMIT</code> flag for {@link TableType#TEMPORARY} tables.
     */
    public enum OnCommit {

        @Support({ POSTGRES })
        DELETE_ROWS,

        @Support({ POSTGRES })
        PRESERVE_ROWS,

        @Support({ POSTGRES })
        DROP;
    }
}
