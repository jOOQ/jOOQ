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
 */
package org.jooq.impl;

import org.jooq.SQLDialect;
import org.jooq.UDT;
import org.jooq.UDTRecord;

/**
 * @author Lukas Eder
 */
final class UDTDataType<R extends UDTRecord<R>> extends DefaultDataType<R> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 3262508265391094581L;

    UDTDataType(UDT<R> udt) {
        super(SQLDialect.DEFAULT, udt.getRecordType(), getQualifiedName(udt));
    }

    private static String getQualifiedName(UDT<?> udt) {
        StringBuilder sb = new StringBuilder();

        if (udt.getSchema() != null) {
            sb.append(udt.getSchema().getName());
            sb.append(".");
        }

        sb.append(udt.getName());
        return sb.toString();
    }
}
