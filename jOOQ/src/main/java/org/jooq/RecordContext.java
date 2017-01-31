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
 * A context object for {@link Record} manipulation passed to registered
 * {@link RecordListener}'s.
 *
 * @author Lukas Eder
 */
public interface RecordContext extends Scope {

    /**
     * The type of database interaction that is being executed.
     * <p>
     * Unlike {@link ExecuteContext#type()}, this can only result in any of
     * these:
     * <ul>
     * <li>{@link ExecuteType#BATCH} when calling
     * {@link DSLContext#batchStore(UpdatableRecord...) batchStore()},
     * {@link DSLContext#batchInsert(TableRecord...) batchInsert()},
     * {@link DSLContext#batchUpdate(UpdatableRecord...) batchUpdate()},
     * {@link DSLContext#batchDelete(UpdatableRecord...) batchDelete()}.</li>
     * <li>{@link ExecuteType#READ} when calling
     * {@link UpdatableRecord#refresh() refresh()}</li>
     * <li>{@link ExecuteType#WRITE} when calling
     * {@link UpdatableRecord#store() store()}, {@link UpdatableRecord#insert()
     * insert()}, {@link UpdatableRecord#update() update()},
     * {@link UpdatableRecord#delete() delete()}.</li>
     * </ul>
     *
     * @see ExecuteType
     */
    ExecuteType type();

    /**
     * The <code>Record</code> that is being manipulated.
     *
     * @return The <code>Record</code> being manipulated. This is never
     *         <code>null</code>
     */
    Record record();

    /**
     * The <code>RecordType</code> of the {@link #record()} that is being
     * manipulated.
     *
     * @return The <code>RecordType</code> being manipulated. This is never
     *         <code>null</code>.
     */
    RecordType<?> recordType();

    /**
     * The <code>Record</code>(s) that are being manipulated in batch mode.
     * <p>
     * If a single <code>Record</code> is being manipulated in non-batch mode,
     * this will return an array of length <code>1</code>, containing that
     * <code>Record</code>.
     *
     * @return The <code>Record</code>(s) being manipulated. This is never
     *         <code>null</code>
     */
    Record[] batchRecords();

    /**
     * The {@link Exception} being thrown or <code>null</code>.
     */
    Exception exception();

}
