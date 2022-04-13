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

import org.jooq.impl.QOM.GenerationOption;

/**
 * The statement type of a {@link GeneratorContext}.
 * <p>
 * {@link Generator} implementations may choose to execute different logic based
 * on the statement type. One example are {@link AuditProvider} fields, which
 * can distinguish between audit information on inserted or updated records.
 * <p>
 * This API is part of a commercial only feature. To use this feature, please
 * use the jOOQ Professional Edition or the jOOQ Enterprise Edition.
 *
 * @author Lukas Eder
 */
public enum GeneratorStatementType {

    /**
     * The {@link Generator} is being invoked in an {@link Insert} context, or
     * in a {@link Merge}'s <code>INSERT</code> clause context.
     * <p>
     * This applies to {@link GenerationOption#STORED} only.
     */
    INSERT,

    /**
     * The {@link Generator} is being invoked in an {@link Update} context, or
     * in an {@link Insert}'s <code>ON DUPLICATE KEY UPDATE</code> clause,
     * <code>ON CONFLICT DO UPDATE</code> clause context, or in a
     * {@link Merge}'s <code>UPDATE</code> clause context.
     * <p>
     * This applies to {@link GenerationOption#STORED} only.
     */
    UPDATE,

    /**
     * The {@link Generator} is being invoked in a {@link Select} context, or an
     * {@link Insert}'s, {@link Update}'s, or {@link Delete}'s
     * <code>RETURNING</code> clause context.
     * <p>
     * This applies to {@link GenerationOption#VIRTUAL} only.
     */
    SELECT
}
