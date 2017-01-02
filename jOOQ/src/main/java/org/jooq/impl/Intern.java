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

import java.io.Serializable;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record;

/**
 * @author Lukas Eder
 */
final class Intern implements Serializable {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 6455756912567274014L;

    // Some temp variables for String interning
    int[]      internIndexes;
    Field<?>[] internFields;
    String[]   internNameStrings;
    Name[]     internNames;

    final int[] internIndexes(Field<?>[] fields) {
        if (internIndexes != null) {
            return internIndexes;
        }
        else if (internFields != null) {
            return new Fields<Record>(fields).indexesOf(internFields);
        }
        else if (internNameStrings != null) {
            return new Fields<Record>(fields).indexesOf(internNameStrings);
        }
        else if (internNames != null) {
            return new Fields<Record>(fields).indexesOf(internNames);
        }

        return null;
    }
}
