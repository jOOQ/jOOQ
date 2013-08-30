/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
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
