/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  https://www.apache.org/licenses/LICENSE-2.0
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
 * Apache-2.0 license and offer limited warranties, support, maintenance, and
 * commercial database integrations.
 *
 * For more information, please visit: https://www.jooq.org/legal/licensing
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
package org.jooq.impl;

import org.jooq.ContextConverter;
import org.jooq.ConverterContext;

/**
 * A marker for {@link ContextConverter} implementations whose
 * {@link ContextConverter#from(Object, ConverterContext)} and
 * {@link ContextConverter#to(Object, ConverterContext)} methods never access
 * the {@link ConverterContext} argument.
 * <p>
 * [#16243] Creating a {@link ConverterContext} is not free. Every instance is a
 * {@link org.jooq.Scope} with its own {@link org.jooq.Scope#creationTime()},
 * which requires a {@link java.time.Clock#instant()} call, which is
 * surprisingly costly for an object that is created so frequently. If the
 * converter cannot observe the context, then there is no need to create one in
 * the first place. Call sites can use
 * {@link Tools#convertFrom(ContextConverter, Object, org.jooq.Attachable)} and
 * {@link Tools#convertTo(ContextConverter, Object, org.jooq.Attachable)} to
 * profit from this.
 * <p>
 * Contexts that <em>are</em> passed to a converter are still created exactly
 * when they would have been created otherwise, with their own
 * {@link org.jooq.Scope#creationTime()}.
 * <p>
 * Only <code>final</code> classes may implement this interface, as subclasses
 * could otherwise override the methods and access the context.
 *
 * @author Lukas Eder
 */
interface ContextIndependentConverter {}
