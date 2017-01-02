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
package org.jooq.util.example;

import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;

/**
 * A generator strategy that prefixes class names with a letter <code>J</code>.
 * <p>
 * An example variant of the default naming strategy
 * {@link DefaultGeneratorStrategy} adding the prefix 'J' to generated class
 * names (e.g. to avoid name clashes with existing JPA entities and such).
 * <p>
 * This strategy is to be understood as a working example, not part of the code
 * generation library. It may be modified / adapted in the future. Use at your
 * own risk.
 *
 * @author Peter Ertl
 * @author Lukas Eder
 */
public class JPrefixGeneratorStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaClassName(final Definition definition, final Mode mode) {
        return 'J' + super.getJavaClassName(definition, mode);
    }
}
