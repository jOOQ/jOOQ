/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.util.oracle;

import static org.jooq.SQLDialect.ORACLE;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.Support;
import org.jooq.UDTRecord;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.tools.jdbc.JDBCUtils;

/**
 * The {@link SQLDialect#ORACLE} specific DSL.
 *
 * @author Lukas Eder
 */
public class OracleDSL extends DSL {

    /**
     * No instances
     */
    protected OracleDSL() {
    }

    // -------------------------------------------------------------------------
    // General pseudo-columns
    // -------------------------------------------------------------------------

    /**
     * The Oracle-specific <code>ROWNUM</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<Integer> rownum() {
        return DSL.rownum();
    }

    /**
     * The Oracle-specific <code>ROWID</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<String> rowid() {
        return field("rowid", String.class);
    }

    /**
     * The Oracle-specific <code>ORA_ROWSCN</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<Long> rowscn() {
        return oraRowscn();
    }

    /**
     * The Oracle-specific <code>ORA_ROWSCN</code> pseudo-field.
     */
    @Support(ORACLE)
    public static Field<Long> oraRowscn() {
        return field("ora_rowscn", Long.class);
    }

    // -------------------------------------------------------------------------
    // Oracle-specific functions
    // -------------------------------------------------------------------------

    /**
     * The Oracle-specific <code>SCN_TO_TIMESTAMP</code> function.
     */
    @Support(ORACLE)
    public static Field<Timestamp> scnToTimestamp(Number scn) {
        return scnToTimestamp(val(scn));
    }

    /**
     * The Oracle-specific <code>SCN_TO_TIMESTAMP</code> function.
     */
    @Support(ORACLE)
    public static Field<Timestamp> scnToTimestamp(Field<? extends Number> scn) {
        return function("scn_to_timestamp", SQLDataType.TIMESTAMP, nullSafe(scn));
    }

    /**
     * The Oracle-specific <code>TIMESTAMP_TO_SCN</code> function.
     */
    @Support(ORACLE)
    public static Field<Long> timestampToScn(java.util.Date timestamp) {
        return timestampToScn(val(timestamp));
    }

    /**
     * The Oracle-specific <code>TIMESTAMP_TO_SCN</code> function.
     */
    @Support(ORACLE)
    public static Field<Long> timestampToScn(Field<? extends java.util.Date> scn) {
        return function("timestamp_to_scn", SQLDataType.BIGINT, nullSafe(scn));
    }

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

    /**
     * The Oracle-specific <code>TO_CHAR</code> function.
     */
    @Support(ORACLE)
    public static Field<String> toChar(Object value) {
        return toChar(val(value));
    }

    /**
     * The Oracle-specific <code>TO_CHAR</code> function.
     */
    @Support(ORACLE)
    public static Field<String> toChar(Field<?> value) {
        return field("{to_char}({0})", String.class, nullSafe(value));
    }

    /**
     * The Oracle-specific <code>TO_CHAR</code> function.
     */
    @Support(ORACLE)
    public static Field<String> toChar(Object value, String formatMask) {
        return toChar(val(value), val(formatMask));
    }

    /**
     * The Oracle-specific <code>TO_CHAR</code> function.
     */
    @Support(ORACLE)
    public static Field<String> toChar(Object value, Field<String> formatMask) {
        return toChar(val(value), formatMask);
    }

    /**
     * The Oracle-specific <code>TO_CHAR</code> function.
     */
    @Support(ORACLE)
    public static Field<String> toChar(Field<?> value, String formatMask) {
        return toChar(value, val(formatMask));
    }

    /**
     * The Oracle-specific <code>TO_CHAR</code> function.
     */
    @Support(ORACLE)
    public static Field<String> toChar(Field<?> value, Field<String> formatMask) {
        return field("{to_char}({0}, {1})", String.class, nullSafe(value), nullSafe(formatMask));
    }

    /**
     * The Oracle-specific <code>TO_NUMBER</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> toNumber(String value) {
        return toNumber(val(value));
    }

    /**
     * The Oracle-specific <code>TO_NUMBER</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> toNumber(Field<String> value) {
        return field("{to_number}({0})", BigDecimal.class, nullSafe(value));
    }

    /**
     * The Oracle-specific <code>TO_NUMBER</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> toNumber(String value, String formatMask) {
        return toNumber(val(value), val(formatMask));
    }

    /**
     * The Oracle-specific <code>TO_NUMBER</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> toNumber(String value, Field<String> formatMask) {
        return toNumber(val(value), formatMask);
    }

    /**
     * The Oracle-specific <code>TO_NUMBER</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> toNumber(Field<String> value, String formatMask) {
        return toNumber(value, val(formatMask));
    }

    /**
     * The Oracle-specific <code>TO_NUMBER</code> function.
     */
    @Support(ORACLE)
    public static Field<BigDecimal> toNumber(Field<String> value, Field<String> formatMask) {
        return field("{to_number}({0}, {1})", BigDecimal.class, nullSafe(value), nullSafe(formatMask));
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

    /**
     * Oracle AQ related features are located here.
     *
     * @author Lukas Eder
     */
    public static final class DBMS_AQ {

        /**
         * A flag corresponding to
         * <code>DBMS_AQ.DEQUEUE_OPTIONS_T.DEQUEUE_MODE</code>.
         */
        public static enum DEQUEUE_MODE {
            BROWSE,
            LOCKED,
            REMOVE,
            REMOVE_NODATA
        }

        /**
         * A flag corresponding to
         * <code>DBMS_AQ.DEQUEUE_OPTIONS_T.NAVIGATION</code>.
         */
        public static enum NAVIGATION {
            NEXT_MESSAGE,
            NEXT_TRANSACTION,
            NEXT_MESSAGE_MULTI_GROUP,
            FIRST_MESSAGE,
            FIRST_MESSAGE_MULTI_GROUP
        }

        /**
         * A flag corresponding to
         * <code>DBMS_AQ.DEQUEUE_OPTIONS_T.VISIBILITY</code> and to
         * <code>DBMS_AQ.ENQUEUE_OPTIONS_T.VISIBILITY</code>.
         */
        public static enum VISIBILITY {
            ON_COMMIT,
            IMMEDIATE
        }

        /**
         * A flag corresponding to <code>DBMS_AQ.DEQUEUE_OPTIONS_T.WAIT</code>.
         */
        public static enum WAIT {
            FOREVER,
            NO_WAIT
        }

        /**
         * A flag corresponding to
         * <code>DBMS_AQ.DEQUEUE_OPTIONS_T.DELIVERY_MODE</code> and to
         * <code>DBMS_AQ.ENQUEUE_OPTIONS_T.DELIVERY_MODE</code>.
         */
        public static enum DELIVERY_MODE {
            BUFFERED,
            PERSISTENT,
            PERSISTENT_OR_BUFFERED
        }

        /**
         * A flag corresponding to <code>DBMS_AQ.ENQUEUE_OPTIONS_T.SEQUENCE_DEVIATION</code>.
         */
        public static enum SEQUENCE_DEVIATION {
            BEFORE,
            TOP
        }

        /**
         * A <code>RECORD</code> corresponding to <code>DBMS_AQ.DEQUEUE_OPTIONS_T</code>.
         */
        public static final class DEQUEUE_OPTIONS_T {

            public String        consumer_name;
            public DEQUEUE_MODE  dequeue_mode;
            public NAVIGATION    navigation;
            public VISIBILITY    visibility;
            public WAIT          wait;
            public String        correlation;
            public String        deq_condition;
            public String        transformation;
            public DELIVERY_MODE delivery_mode;

            public DEQUEUE_OPTIONS_T consumer_name(String newValue) {
                this.consumer_name = newValue;
                return this;
            }

            public DEQUEUE_OPTIONS_T dequeue_mode(DEQUEUE_MODE newValue) {
                this.dequeue_mode = newValue;
                return this;
            }

            public DEQUEUE_OPTIONS_T navigation(NAVIGATION newValue) {
                this.navigation = newValue;
                return this;
            }

            public DEQUEUE_OPTIONS_T visibility(VISIBILITY newValue) {
                this.visibility = newValue;
                return this;
            }

            public DEQUEUE_OPTIONS_T wait(WAIT newValue) {
                this.wait = newValue;
                return this;
            }

            public DEQUEUE_OPTIONS_T correlation(String newValue) {
                this.correlation = newValue;
                return this;
            }

            public DEQUEUE_OPTIONS_T deq_condition(String newValue) {
                this.deq_condition = newValue;
                return this;
            }

            public DEQUEUE_OPTIONS_T transformation(String newValue) {
                this.transformation = newValue;
                return this;
            }

            public DEQUEUE_OPTIONS_T delivery_mode(DELIVERY_MODE newValue) {
                this.delivery_mode = newValue;
                return this;
            }
        }

        /**
         * A <code>RECORD</code> corresponding to <code>DBMS_AQ.ENQUEUE_OPTIONS_T</code>.
         */
        public static final class ENQUEUE_OPTIONS_T {

            public VISIBILITY         visibility;
            public SEQUENCE_DEVIATION sequence_deviation;
            public String             transformation;
            public DELIVERY_MODE      delivery_mode;

            public ENQUEUE_OPTIONS_T visibility(VISIBILITY newValue) {
                this.visibility = newValue;
                return this;
            }

            public ENQUEUE_OPTIONS_T sequence_deviation(SEQUENCE_DEVIATION newValue) {
                this.sequence_deviation = newValue;
                return this;
            }

            public ENQUEUE_OPTIONS_T transformation(String newValue) {
                this.transformation = newValue;
                return this;
            }

            public ENQUEUE_OPTIONS_T delivery_mode(DELIVERY_MODE newValue) {
                this.delivery_mode = newValue;
                return this;
            }
        }

        /**
         * A <code>RECORD</code> corresponding to <code>DBMS_AQ.MESSAGE_PROPERTIES_T</code>.
         */
        public static final class MESSAGE_PROPERTIES_T {

            public Integer       priority;
            public BigDecimal    delay;
            public BigDecimal    expires;
            public String        correlation;
            public Integer       attempts;
            public String        exception_queue;
            public Timestamp     enqueue_time;
            public Integer       state;
            public String        transaction_group;
            public DELIVERY_MODE delivery_mode;

            public void priority(Integer newValue) {
                this.priority = newValue;
            }

            public void delay(BigDecimal newValue) {
                this.delay = newValue;
            }

            public void expires(BigDecimal newValue) {
                this.expires = newValue;
            }

            public void correlation(String newValue) {
                this.correlation = newValue;
            }

            public void attempts(Integer newValue) {
                this.attempts = newValue;
            }

            public void exception_queue(String newValue) {
                this.exception_queue = newValue;
            }

            public void enqueue_time(Timestamp newValue) {
                this.enqueue_time = newValue;
            }

            public void state(Integer newValue) {
                this.state = newValue;
            }

            public void transaction_group(String newValue) {
                this.transaction_group = newValue;
            }

            public void delivery_mode(DELIVERY_MODE newValue) {
                this.delivery_mode = newValue;
            }
        }

        /**
         * Enqueue a message in an Oracle AQ.
         *
         * @param configuration The configuration from which to get a connection.
         * @param queue The queue reference.
         * @param payload The message payload.
         */
        public static <R extends UDTRecord<R>> void enqueue(Configuration configuration, Queue<R> queue, R payload) {
            enqueue(configuration, queue, payload, null, null);
        }

        /**
         * Enqueue a message in an Oracle AQ.
         *
         * @param configuration The configuration from which to get a connection.
         * @param queue The queue reference.
         * @param payload The message payload.
         * @param options The enqueue options.
         * @param properties The message properties.
         */
        public static <R extends UDTRecord<R>> void enqueue(Configuration configuration, Queue<R> queue, R payload, ENQUEUE_OPTIONS_T options, MESSAGE_PROPERTIES_T properties) {
            if (options == null)
                options = new ENQUEUE_OPTIONS_T();
            if (properties == null)
                properties = new MESSAGE_PROPERTIES_T();

            // [#2626] TODO: Externalise this SQL string in a .properties file and use jOOQ's
            //               templating mechanism to load it

            List<Object> bindings = new ArrayList<Object>();

            if (options.transformation != null)
                bindings.add(options.transformation);

            if (properties.attempts != null)
                bindings.add(properties.attempts);
            if (properties.correlation != null)
                bindings.add(properties.correlation);
            if (properties.delay != null)
                bindings.add(properties.delay);
            if (properties.enqueue_time != null)
                bindings.add(properties.enqueue_time);
            if (properties.exception_queue != null)
                bindings.add(properties.exception_queue);
            if (properties.expires != null)
                bindings.add(properties.expires);
            if (properties.priority != null)
                bindings.add(properties.priority);
            if (properties.state != null)
                bindings.add(properties.state);
            if (properties.transaction_group != null)
                bindings.add(properties.transaction_group);

            bindings.add(queue.name());
            bindings.add(payload);


            DSL.using(configuration)
               .execute("DECLARE"
                    + "\n  v_msgid              RAW(16);"
                    + "\n  v_enqueue_options    DBMS_AQ.enqueue_options_t;"
                    + "\n  v_message_properties DBMS_AQ.message_properties_t;"
                    + "\nBEGIN"                                                                                           + (options.transformation       == null ? "" :
                      "\n  v_enqueue_options.transformation       := ?;")                                                 + (options.delivery_mode        == null ? "" :
                      "\n  v_enqueue_options.delivery_mode        := DBMS_AQ." + options.delivery_mode.name()      + ";") + (options.sequence_deviation   == null ? "" :
                      "\n  v_enqueue_options.sequence_deviation   := DBMS_AQ." + options.sequence_deviation.name() + ";") + (options.visibility           == null ? "" :
                      "\n  v_enqueue_options.visibility           := DBMS_AQ." + options.visibility.name()         + ";") + (properties.attempts          == null ? "" :
                      "\n  v_message_properties.attempts          := ?")                                                  + (properties.correlation       == null ? "" :
                      "\n  v_message_properties.correlation       := ?")                                                  + (properties.delay             == null ? "" :
                      "\n  v_message_properties.delay             := ?")                                                  + (properties.delivery_mode     == null ? "" :
                      "\n  v_message_properties.delivery_mode     := DBMS_AQ." + properties.delivery_mode.name()   + ";") + (properties.enqueue_time      == null ? "" :
                      "\n  v_message_properties.enqueue_time      := ?")                                                  + (properties.exception_queue   == null ? "" :
                      "\n  v_message_properties.exception_queue   := ?")                                                  + (properties.expires           == null ? "" :
                      "\n  v_message_properties.expires           := ?")                                                  + (properties.priority          == null ? "" :
                      "\n  v_message_properties.priority          := ?")                                                  + (properties.state             == null ? "" :
                      "\n  v_message_properties.state             := ?")                                                  + (properties.transaction_group == null ? "" :
                      "\n  v_message_properties.transaction_group := ?")
                    + "\n  DBMS_AQ.ENQUEUE("
                    + "\n    queue_name         => ?,"
                    + "\n    enqueue_options    => v_enqueue_options,"
                    + "\n    message_properties => v_message_properties,"
                    + "\n    payload            => ?,"
                    + "\n    msgid              => v_msgid"
                    + "\n  );"
                    + "\nEND;", bindings.toArray());

        }

        /**
         * Dequeue a message in an Oracle AQ.
         *
         * @param configuration The configuration from which to get a connection.
         * @param queue The queue reference.
         * @return The message payload.
         */
        public static <R extends UDTRecord<R>> R dequeue(Configuration configuration, Queue<R> queue) {
            return dequeue(configuration, queue, null, null);
        }

        /**
         * Dequeue a message in an Oracle AQ.
         *
         * @param configuration The configuration from which to get a connection.
         * @param queue The queue reference.
         * @return The message payload.
         * @param options The dequeue options.
         * @param properties The message properties.
         */
        public static <R extends UDTRecord<R>> R dequeue(Configuration configuration, Queue<R> queue, DEQUEUE_OPTIONS_T options, MESSAGE_PROPERTIES_T properties) {
            if (options == null)
                options = new DEQUEUE_OPTIONS_T();
            if (properties == null)
                properties = new MESSAGE_PROPERTIES_T();

            // [#2626] TODO: Externalise this SQL string in a .properties file and use jOOQ's
            //               templating mechanism to load it
            // [#3426] TODO: Replace this explicit call to Connection.prepareCall() by
            //               a call to the new DSLContext.callable() API in order to go through
            //               the complete jOOQ query execution lifecycle.

            String sql ="DECLARE"
                    + "\n  v_msgid              RAW(16);"
                    + "\n  v_dequeue_options    DBMS_AQ.dequeue_options_t;"
                    + "\n  v_message_properties DBMS_AQ.message_properties_t;"
                    + "\nBEGIN"                                                                                              + (options.consumer_name        == null ? "" :
                      "\n  v_dequeue_options.consumer_name        := ?;")                                                    + (options.correlation          == null ? "" :
                      "\n  v_dequeue_options.correlation          := ?;")                                                    + (options.deq_condition        == null ? "" :
                      "\n  v_dequeue_options.deq_condition        := ?;")                                                    + (options.transformation       == null ? "" :
                      "\n  v_dequeue_options.transformation       := ?;")                                                    + (options.delivery_mode        == null ? "" :
                      "\n  v_dequeue_options.delivery_mode        := DBMS_AQ." + options.delivery_mode.name()    + ";")      + (options.dequeue_mode         == null ? "" :
                      "\n  v_dequeue_options.dequeue_mode         := DBMS_AQ." + options.dequeue_mode.name()     + ";")      + (options.navigation           == null ? "" :
                      "\n  v_dequeue_options.navigation           := DBMS_AQ." + options.navigation.name()       + ";")      + (options.visibility           == null ? "" :
                      "\n  v_dequeue_options.visibility           := DBMS_AQ." + options.visibility.name()       + ";")      + (options.wait                 == null ? "" :
                      "\n  v_dequeue_options.wait                 := DBMS_AQ." + options.wait.name()             + ";")      + (properties.attempts          == null ? "" :
                      "\n  v_message_properties.attempts          := ?")                                                     + (properties.correlation       == null ? "" :
                      "\n  v_message_properties.correlation       := ?")                                                     + (properties.delay             == null ? "" :
                      "\n  v_message_properties.delay             := ?")                                                     + (properties.delivery_mode     == null ? "" :
                      "\n  v_message_properties.delivery_mode     := DBMS_AQ." + properties.delivery_mode.name() + ";")      + (properties.enqueue_time      == null ? "" :
                      "\n  v_message_properties.enqueue_time      := ?")                                                     + (properties.exception_queue   == null ? "" :
                      "\n  v_message_properties.exception_queue   := ?")                                                     + (properties.expires           == null ? "" :
                      "\n  v_message_properties.expires           := ?")                                                     + (properties.priority          == null ? "" :
                      "\n  v_message_properties.priority          := ?")                                                     + (properties.state             == null ? "" :
                      "\n  v_message_properties.state             := ?")                                                     + (properties.transaction_group == null ? "" :
                      "\n  v_message_properties.transaction_group := ?")
                    + "\n  DBMS_AQ.DEQUEUE("
                    + "\n    queue_name         => ?,"
                    + "\n    dequeue_options    => v_dequeue_options,"
                    + "\n    message_properties => v_message_properties,"
                    + "\n    payload            => ?,"
                    + "\n    msgid              => v_msgid"
                    + "\n  );"
                    + "\nEND;";

            ConnectionProvider cp = configuration.connectionProvider();
            Connection connection = cp.acquire();
            CallableStatement stmt = null;

            try {
                int i = 1;
                stmt = connection.prepareCall(sql);

                if (options.consumer_name != null)
                    stmt.setString(i++, options.consumer_name);
                if (options.correlation != null)
                    stmt.setString(i++, options.correlation);
                if (options.deq_condition != null)
                    stmt.setString(i++, options.deq_condition);
                if (options.transformation != null)
                    stmt.setString(i++, options.transformation);

                if (properties.attempts != null)
                    stmt.setInt(i++, properties.attempts);
                if (properties.correlation != null)
                    stmt.setString(i++, properties.correlation);
                if (properties.delay != null)
                    stmt.setBigDecimal(i++, properties.delay);
                if (properties.enqueue_time != null)
                    stmt.setTimestamp(i++, properties.enqueue_time);
                if (properties.exception_queue != null)
                    stmt.setString(i++, properties.exception_queue);
                if (properties.expires != null)
                    stmt.setBigDecimal(i++, properties.expires);
                if (properties.priority != null)
                    stmt.setInt(i++, properties.priority);
                if (properties.state != null)
                    stmt.setInt(i++, properties.state);
                if (properties.transaction_group != null)
                    stmt.setString(i++, properties.transaction_group);

                stmt.setString(i++, queue.name());
                stmt.registerOutParameter(i++, Types.STRUCT, queue.type().getName());
                stmt.execute();

                Map<String, Class<?>> map = new HashMap<String, Class<?>>();
                map.put(queue.type().getName(), queue.type().getRecordType());
                return (R) stmt.getObject(2, map);
            }
            catch (SQLException e) {
                throw new DataAccessException("Error while dequeuing message", e);
            }
            finally {
                JDBCUtils.safeClose(stmt);
                cp.release(connection);
            }
        }

        /**
         * No instances
         */
        private DBMS_AQ() {}
    }
}
