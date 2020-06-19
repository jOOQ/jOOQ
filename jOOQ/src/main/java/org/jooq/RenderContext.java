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

import org.jooq.conf.ParamType;

import org.jetbrains.annotations.NotNull;

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
     * Recurse rendering.
     *
     * @deprecated - 3.2.0 - [#2666] - Use {@link #visit(QueryPart)} instead
     */
    @NotNull
    @Deprecated
    RenderContext sql(QueryPart part);

    /**
     * Whether bind variables should be inlined, rather than rendered as
     * <code>'?'</code>.
     *
     * @deprecated - 3.1.0 - [#2414] - This method should no longer be used. Use
     *             {@link #paramType()} instead.
     */
    @Deprecated
    boolean inline();

    /**
     * Set the new context value for {@link #inline()}.
     *
     * @deprecated - 3.1.0 - [#2414] - This method should no longer be used. Use
     *             {@link #paramType(ParamType)} instead.
     */
    @NotNull
    @Deprecated
    RenderContext inline(boolean inline);

    /**
     * Whether bind variables should be rendered as named parameters:<br/>
     * <code>&#160; :1, :2, :custom_name</code>
     * <p>
     * or as JDBC bind variables <br/>
     * <code>&#160; ?</code>
     *
     * @deprecated - 3.1.0 - [#2414] - This method should no longer be used. Use
     *             {@link #paramType()} instead.
     */
    @Deprecated
    boolean namedParams();

    /**
     * Set the new context value for {@link #namedParams()}.
     *
     * @deprecated - 3.1.0 - [#2414] - This method should no longer be used. Use
     *             {@link #paramType(ParamType)} instead.
     */
    @NotNull
    @Deprecated
    RenderContext namedParams(boolean renderNamedParams);

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
         * Cast bind values only in some dialects. The specified dialects assume
         * {@link #ALWAYS} behaviour, all the other dialects assume
         * {@link #NEVER}.
         *
         * @deprecated - [#3703] - 3.5.0 - Do not use this any longer
         */
        @Deprecated
        SOME,

        /**
         * Cast when needed. This is the default mode if not specified
         * otherwise.
         */
        DEFAULT
    }
}
