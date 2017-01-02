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
package org.jooq;

/**
 * A field mapper that produces {@link Field} references for {@link Loader}
 * target tables.
 * <p>
 * Rather than index-based field mappings as in
 * {@link LoaderRowsStep#fields(Field...)} and other API methods, this mapper
 * can be used to translate source fields from the loader source (CSV, JSON,
 * arrays, records, etc.) to fields from the target table.
 *
 * @author Lukas Eder
 */

@FunctionalInterface

public interface LoaderFieldMapper {

    /**
     * Map a <code>Field</code> from the loader source onto a target table
     * <code>Field</code>.
     */
    Field<?> map(LoaderFieldContext ctx);

    /**
     * The argument object for {@link LoaderFieldMapper#map(LoaderFieldContext)}
     * .
     */
    interface LoaderFieldContext {

        /**
         * The {@link Field} of the source data to be mapped.
         * <p>
         * This returns the following, depending on the data source:
         * <ul>
         * <li>{@link LoaderSourceStep#loadArrays(Object[][])}: A generated,
         * unspecified field.</li>
         * <li>{@link LoaderSourceStep#loadCSV(String)}: If the first CSV row
         * specifies headers, those headers are used for field names. Otherwise,
         * a generated, unspecified field is provided.</li>
         * <li>{@link LoaderSourceStep#loadJSON(String)}: The field specified in
         * the JSON content is used.</li>
         * <li>{@link LoaderSourceStep#loadRecords(Record...)}: The field from
         * the {@link Record} is used.</li>
         * </ul>
         */
        Field<?> field();

        /**
         * The field index in order of specification in the source data.
         * <p>
         * This returns the following, depending on the data source:
         * <ul>
         * <li>{@link LoaderSourceStep#loadArrays(Object[][])}: The array index.
         * </li>
         * <li>{@link LoaderSourceStep#loadCSV(String)}: The CSV column index.
         * </li>
         * <li>{@link LoaderSourceStep#loadJSON(String)}: The JSON field
         * enumeration index (depending on your JSON serialisation, this might
         * not be reliable!)</li>
         * <li>{@link LoaderSourceStep#loadRecords(Record...)}: The record field
         * index.</li>
         * </ul>
         */
        int index();
    }
}
