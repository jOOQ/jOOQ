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

import javax.annotation.Generated;

/**
 * The step in the {@link Constraint} construction DSL API that allows for
 * matching a <code>FOREIGN KEY</code> clause with a <code>REFERENCES</code>
 * clause.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface ConstraintForeignKeyReferencesStep5<T1, T2, T3, T4, T5> {

    /**
     * Add a <code>REFERENCES</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    ConstraintForeignKeyOnStep references(String table, String field1, String field2, String field3, String field4, String field5);

    /**
     * Add a <code>REFERENCES</code> clause to the <code>CONSTRAINT</code>.
     */
    @Support
    ConstraintForeignKeyOnStep references(Table<?> table, Field<T1> field1, Field<T2> field2, Field<T3> field3, Field<T4> field4, Field<T5> field5);
}
