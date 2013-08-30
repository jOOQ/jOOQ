/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.util.oracle;

import static org.jooq.SQLDialect.ORACLE;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.Support;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

/**
 * The {@link SQLDialect#ORACLE} specific DSL.
 *
 * @author Lukas Eder
 */
public class OracleDSL extends DSL {

    /**
     * No instances
     */
    private OracleDSL() {
    }

    // -------------------------------------------------------------------------
    // General pseudo-columns
    // -------------------------------------------------------------------------

    /**
     * Retrieve the Oracle-specific <code>ROWNUM</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<Integer> rownum() {
        return field("rownum", Integer.class);
    }

    /**
     * Retrieve the Oracle-specific <code>ROWID</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<String> rowid() {
        return field("rowid", String.class);
    }

    // -------------------------------------------------------------------------
    // Oracle-specific functions
    // -------------------------------------------------------------------------

    /**
     * The Oracle-specific <code>SYS_CONTEXT</code> function.
     */
    @Support(ORACLE)
    public static Field<String> sysContext(String namespace, String parameter) {
        return function("sys_context", SQLDataType.VARCHAR, val(namespace), val(parameter));
    }

    /**
     * The Oracle-specific <code>SYS_CONTEXT</code> function.
     */
    @Support(ORACLE)
    public static Field<String> sysContext(String namespace, String parameter, int length) {
        return function("sys_context", SQLDataType.VARCHAR, val(namespace), val(parameter), val(length));
    }

    // -------------------------------------------------------------------------
    // Oracle Flashback Version Query pseudo-columns
    // -------------------------------------------------------------------------

    /**
     * The Oracle-specific <code>VERSIONS_STARTSCN</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<Long> versionsStartscn() {
        return field("{versions_startscn}", Long.class);
    }

    /**
     * The Oracle-specific <code>VERSIONS_STARTTIME</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<Timestamp> versionsStarttime() {
        return field("{versions_starttime}", Timestamp.class);
    }

    /**
     * The Oracle-specific <code>VERSIONS_ENDSCN</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<Long> versionsEndscn() {
        return field("{versions_endscn}", Long.class);
    }

    /**
     * The Oracle-specific <code>VERSIONS_ENDTIME</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<Timestamp> versionsEndtime() {
        return field("{versions_endtime}", Timestamp.class);
    }

    /**
     * The Oracle-specific <code>VERSIONS_XID</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<String> versionsXid() {
        return field("{versions_xid}", String.class);
    }

    /**
     * The Oracle-specific <code>VERSIONS_OPERATION</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<String> versionsOperation() {
        return field("{versions_operation}", String.class);
    }

    // -------------------------------------------------------------------------
    // Oracle Text functions
    // -------------------------------------------------------------------------

    /**
     * The Oracle-Text specific <code>CONTAINS</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> contains(Field<String> field, String query) {
        return field("{contains}({0}, {1})", SQLDataType.NUMERIC, nullSafe(field), val(query));
    }

    /**
     * The Oracle-Text specific <code>CONTAINS</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> contains(Field<String> field, String query, int label) {
        return field("{contains}({0}, {1}, {2})", SQLDataType.NUMERIC, nullSafe(field), val(query), inline(label));
    }

    /**
     * The Oracle-Text specific <code>MATCHES</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> matches(Field<String> field, String query) {
        return field("{matches}({0}, {1})", SQLDataType.NUMERIC, nullSafe(field), val(query));
    }

    /**
     * The Oracle-Text specific <code>CONTAINS</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> matches(Field<String> field, String query, int label) {
        return field("{matches}({0}, {1}, {2})", SQLDataType.NUMERIC, nullSafe(field), val(query), inline(label));
    }

    /**
     * The Oracle-Text specific <code>CATSEARCH</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> catsearch(Field<String> field, String textQuery, String structuredQuery) {
        return field("{catsearch}({0}, {1}, {2})", SQLDataType.NUMERIC, nullSafe(field), val(textQuery), val(structuredQuery));
    }

    /**
     * The Oracle-Text specific <code>SCORE</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> score(int label) {
        return field("{score}({0})", SQLDataType.NUMERIC, inline(label));
    }

    /**
     * The Oracle-Text specific <code>MATCH_SCORE</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> matchScore(int label) {
        return field("{match_score}({0})", SQLDataType.NUMERIC, inline(label));
    }

}
