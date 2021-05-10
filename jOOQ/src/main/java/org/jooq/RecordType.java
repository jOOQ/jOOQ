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

import org.jooq.impl.DefaultRecordMapper;

/**
 * A record type for {@link Table}, {@link Cursor}, {@link Result} and other
 * objects.
 * <p>
 * This type differs from {@link Row} in several ways:
 * <ul>
 * <li>It is generic using <code>&lt;R&gt;</code></li>
 * <li>It is not repeated for degrees 1 to 22, such as {@link Row1} ..
 * {@link RowN}</li>
 * <li>It is not part of the DSL</li>
 * </ul>
 *
 * @author Lukas Eder
 */
public interface RecordType<R extends Record> extends Fields {

    /**
     * Get the degree of this record type.
     */
    int size();

}
