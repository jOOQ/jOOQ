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

import static org.jooq.SQLDialect.CUBRID;
// ...
import static org.jooq.SQLDialect.FIREBIRD_3_0;
import static org.jooq.SQLDialect.H2;
// ...
import static org.jooq.SQLDialect.HSQLDB;
// ...
import static org.jooq.SQLDialect.POSTGRES_9_5;
// ...
// ...

import java.util.Collection;

import javax.annotation.Generated;

/**
 * This type is used for the H2-specific variant of the {@link Merge}'s DSL API.
 * <p>
 * Example: <code><pre>
 * DSLContext create = DSL.using(configuration);
 *
 * create.mergeInto(table, field1, field2, field3, .., field19, field20)
 *       .key(id)
 *       .values(value1, value2, value3, .., value19, value20)
 *       .execute();
 * </pre></code>
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface MergeKeyStep20<R extends Record, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> extends MergeValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> {

    /**
     * Specify an optional <code>KEY</code> clause.
     * <p>
     * Use this optional clause in order to override using the underlying
     * <code>PRIMARY KEY</code>.
     */
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, POSTGRES_9_5 })
    MergeValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> key(Field<?>... keys);

    /**
     * Specify an optional <code>KEY</code> clause.
     * <p>
     * Use this optional clause in order to override using the underlying
     * <code>PRIMARY KEY</code>.
     */
    @Support({ CUBRID, FIREBIRD_3_0, H2, HSQLDB, POSTGRES_9_5 })
    MergeValuesStep20<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> key(Collection<? extends Field<?>> keys);
}
