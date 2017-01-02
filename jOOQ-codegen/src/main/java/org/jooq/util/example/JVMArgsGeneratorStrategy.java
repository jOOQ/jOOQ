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
 * A generator strategy that prefixes / suffixes class names and other artefacts
 * with values taken from JVM parameters.
 * <p>
 * An example variant of the default naming strategy
 * {@link DefaultGeneratorStrategy} adding the prefix and suffixes to generated
 * class names (e.g. to avoid name clashes with existing JPA entities and such).
 * <p>
 * The following JVM parameters are supported:
 * <ul>
 * <li><code>org.jooq.util.example.java-identifier-prefix</code></li>
 * <li><code>org.jooq.util.example.java-identifier-suffix</code></li>
 * <li><code>org.jooq.util.example.java-getter-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-getter-name-suffix</code></li>
 * <li><code>org.jooq.util.example.java-setter-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-setter-name-suffix</code></li>
 * <li><code>org.jooq.util.example.java-method-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-method-name-suffix</code></li>
 * <li><code>org.jooq.util.example.java-class-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-class-name-suffix</code></li>
 * <li><code>org.jooq.util.example.java-package-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-package-name-suffix</code></li>
 * <li><code>org.jooq.util.example.java-member-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-member-name-suffix</code></li>
 * <p>
 * This strategy is to be understood as a working example, not part of the code
 * generation library. It may be modified / adapted in the future. Use at your
 * own risk.
 *
 * @author Lukas Eder
 */
public class JVMArgsGeneratorStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaIdentifier(Definition definition) {
        return System.getProperty("org.jooq.util.example.java-identifier-prefix", "")
            + super.getJavaIdentifier(definition)
            + System.getProperty("org.jooq.util.example.java-identifier-suffix", "");
    }

    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-setter-name-prefix", "")
            + super.getJavaSetterName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-setter-name-suffix", "");
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-getter-name-prefix", "")
            + super.getJavaGetterName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-getter-name-suffix", "");
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-method-name-prefix", "")
            + super.getJavaMethodName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-method-name-suffix", "");
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-class-name-prefix", "")
            + super.getJavaClassName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-class-name-suffix", "");
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-package-name-prefix", "")
            + super.getJavaPackageName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-package-name-suffix", "");
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-member-name-prefix", "")
            + super.getJavaMemberName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-member-name-suffix", "");
    }
}
