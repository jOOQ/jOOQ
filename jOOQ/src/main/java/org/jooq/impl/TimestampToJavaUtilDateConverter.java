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

import java.sql.Timestamp;

/**
 * @author Lukas Eder
 */
final class TimestampToJavaUtilDateConverter extends AbstractConverter<Timestamp, java.util.Date> {

    /**
     * Generated UID
     */
    private static final long                     serialVersionUID = 1L;
    static final TimestampToJavaUtilDateConverter INSTANCE         = new TimestampToJavaUtilDateConverter();

    private TimestampToJavaUtilDateConverter () {
        super(Timestamp.class, java.util.Date.class);
    }

    @Override
    public final java.util.Date from(Timestamp t) {
        return t == null ? null : new java.util.Date(t.getTime());
    }

    @Override
    public final Timestamp to(java.util.Date u) {
        return u == null ? null : new Timestamp(u.getTime());
    }
}