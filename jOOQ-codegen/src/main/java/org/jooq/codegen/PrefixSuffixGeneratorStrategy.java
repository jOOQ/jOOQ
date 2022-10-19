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
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
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
package org.jooq.codegen;

import java.util.function.Consumer;
import java.util.function.Function;

import org.jooq.meta.Definition;
import org.jooq.tools.StringUtils;

/**
 * A {@link GeneratorStrategy} that delegates to another, adding a prefix and/or
 * suffix to some identifiers.
 *
 * @author Lukas Eder
 */
public class PrefixSuffixGeneratorStrategy extends AbstractDelegatingGeneratorStrategy {

    private String javaIdentifierPrefix;
    private String javaIdentifierSuffix;
    private String javaSetterNamePrefix;
    private String javaSetterNameSuffix;
    private String javaGetterNamePrefix;
    private String javaGetterNameSuffix;
    private String javaMethodNamePrefix;
    private String javaMethodNameSuffix;
    private String javaClassNamePrefix;
    private String javaClassNameSuffix;
    private String javaPackageNamePrefix;
    private String javaPackageNameSuffix;
    private String javaMemberNamePrefix;
    private String javaMemberNameSuffix;

    static GeneratorStrategy wrap(
        GeneratorStrategy delegate,
        Function<? super PrefixSuffixGeneratorStrategy, ? extends PrefixSuffixGeneratorStrategy> configuration
    ) {
        return delegate instanceof GeneratorStrategyWrapper w
             ? new GeneratorStrategyWrapper(w.generator, configuration.apply(new PrefixSuffixGeneratorStrategy(w.delegate)))
             : configuration.apply(new PrefixSuffixGeneratorStrategy(delegate));
    }

    public PrefixSuffixGeneratorStrategy(GeneratorStrategy delegate) {
        super(delegate);
    }

    public PrefixSuffixGeneratorStrategy withJavaIdentifierPrefix(String prefix) {
        this.javaIdentifierPrefix = prefix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaIdentifierSuffix(String suffix) {
        this.javaIdentifierSuffix = suffix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaSetterNamePrefix(String prefix) {
        this.javaSetterNamePrefix = prefix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaSetterNameSuffix(String suffix) {
        this.javaSetterNameSuffix = suffix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaGetterNamePrefix(String prefix) {
        this.javaGetterNamePrefix = prefix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaGetterNameSuffix(String suffix) {
        this.javaGetterNameSuffix = suffix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaMethodNamePrefix(String prefix) {
        this.javaMethodNamePrefix = prefix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaMethodNameSuffix(String suffix) {
        this.javaMethodNameSuffix = suffix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaClassNamePrefix(String prefix) {
        this.javaClassNamePrefix = prefix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaClassNameSuffix(String suffix) {
        this.javaClassNameSuffix = suffix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaPackageNamePrefix(String prefix) {
        this.javaPackageNamePrefix = prefix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaPackageNameSuffix(String suffix) {
        this.javaPackageNameSuffix = suffix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaMemberNamePrefix(String prefix) {
        this.javaMemberNamePrefix = prefix;
        return this;
    }

    public PrefixSuffixGeneratorStrategy withJavaMemberNameSuffix(String suffix) {
        this.javaMemberNameSuffix = suffix;
        return this;
    }

    @Override
    public String getJavaIdentifier(Definition definition) {
        return StringUtils.defaultIfNull(javaIdentifierPrefix, "")
             + delegate.getJavaIdentifier(definition)
             + StringUtils.defaultIfNull(javaIdentifierSuffix, "");
    }

    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        return StringUtils.defaultIfNull(javaSetterNamePrefix, "")
             + delegate.getJavaSetterName(definition, mode)
             + StringUtils.defaultIfNull(javaSetterNameSuffix, "");
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        return StringUtils.defaultIfNull(javaGetterNamePrefix, "")
             + delegate.getJavaGetterName(definition, mode)
             + StringUtils.defaultIfNull(javaGetterNameSuffix, "");
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        return StringUtils.defaultIfNull(javaMethodNamePrefix, "")
             + delegate.getJavaMethodName(definition, mode)
             + StringUtils.defaultIfNull(javaMethodNameSuffix, "");
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        return StringUtils.defaultIfNull(javaClassNamePrefix, "")
             + delegate.getJavaClassName(definition, mode)
             + StringUtils.defaultIfNull(javaClassNameSuffix, "");
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        return StringUtils.defaultIfNull(javaPackageNamePrefix, "")
             + delegate.getJavaPackageName(definition, mode)
             + StringUtils.defaultIfNull(javaPackageNameSuffix, "");
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {
        return StringUtils.defaultIfNull(javaMemberNamePrefix, "")
             + delegate.getJavaMemberName(definition, mode)
             + StringUtils.defaultIfNull(javaMemberNameSuffix, "");
    }
}
