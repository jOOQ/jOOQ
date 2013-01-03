/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq;

/**
 * The final step in creating a case statement of the type <code><pre>
 * CASE WHEN x &lt; 1  THEN 'one'
 *      WHEN x &gt;= 2 THEN 'two'
 *      ELSE            'three'
 * END
 * </pre></code>
 *
 * @param <T> The type returned by this case statement
 * @author Lukas Eder
 * @see Case
 */
public interface CaseConditionStep<T> extends Field<T> {

    /**
     * Compare a condition to the already constructed case statement, return
     * result if the condition holds true
     *
     * @param condition The condition to add to the case statement
     * @param result The result value if the condition holds true
     * @return An intermediary step for case statement construction
     */
    @Support
    CaseConditionStep<T> when(Condition condition, T result);

    /**
     * Compare a condition to the already constructed case statement, return
     * result if the condition holds true
     *
     * @param condition The condition to add to the case statement
     * @param result The result value if the condition holds true
     * @return An intermediary step for case statement construction
     */
    @Support
    CaseConditionStep<T> when(Condition condition, Field<T> result);

    /**
     * Add an else clause to the already constructed case statement
     *
     * @param result The result value if no other value matches the case
     * @return The resulting field from case statement construction
     */
    @Support
    Field<T> otherwise(T result);

    /**
     * Add an else clause to the already constructed case statement
     *
     * @param result The result value if no other value matches the case
     * @return The resulting field from case statement construction
     */
    @Support
    Field<T> otherwise(Field<T> result);
}
