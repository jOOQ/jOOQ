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

/**
 * The render context is used for rendering {@link QueryPart}'s to SQL.
 * <p>
 * A new render context is instantiated every time a {@link Query} is rendered.
 * <code>QueryPart</code>'s will then pass the same context to their components
 *
 * @author Lukas Eder
 * @see BindContext
 */
public interface RenderContext extends Context<RenderContext> {

    /**
     * The cast mode for bind values.
     *
     * @see RenderContext#castMode()
     */
    enum CastMode {

        /**
         * Cast all bind values to their respective type.
         */
        ALWAYS,

        /**
         * Cast no bind values to their respective type.
         */
        NEVER,

        /**
         * Cast when needed. This is the default mode if not specified
         * otherwise.
         */
        DEFAULT
    }
}
