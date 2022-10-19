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

import java.util.List;
import java.util.Locale;

import org.jooq.meta.Definition;
import org.jooq.meta.EnumDefinition;

/**
 * A {@link GeneratorStrategy} that delegates to another one.
 *
 * @author Lukas Eder
 */
public abstract class AbstractDelegatingGeneratorStrategy extends AbstractGeneratorStrategy {

    protected final GeneratorStrategy delegate;

    public AbstractDelegatingGeneratorStrategy() {
        this(new DefaultGeneratorStrategy());
    }

    public AbstractDelegatingGeneratorStrategy(GeneratorStrategy delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getTargetDirectory() {
        return delegate.getTargetDirectory();
    }

    @Override
    public void setTargetDirectory(String directory) {
        delegate.setTargetDirectory(directory);
    }

    @Override
    public String getTargetPackage() {
        return delegate.getTargetPackage();
    }

    @Override
    public void setTargetPackage(String packageName) {
        delegate.setTargetPackage(packageName);
    }

    @Override
    public Locale getTargetLocale() {
        return delegate.getTargetLocale();
    }

    @Override
    public void setTargetLocale(Locale targetLocale) {
        delegate.setTargetLocale(targetLocale);
    }

    @Override
    public Language getTargetLanguage() {
        return delegate.getTargetLanguage();
    }

    @Override
    public void setTargetLanguage(Language targetLanguage) {
        delegate.setTargetLanguage(targetLanguage);
    }

    @Override
    public void setInstanceFields(boolean instanceFields) {
        delegate.setInstanceFields(instanceFields);
    }

    @Override
    public boolean getInstanceFields() {
        return delegate.getInstanceFields();
    }

    @Override
    public void setJavaBeansGettersAndSetters(boolean javaBeansGettersAndSetters) {
        delegate.setJavaBeansGettersAndSetters(javaBeansGettersAndSetters);
    }

    @Override
    public boolean getJavaBeansGettersAndSetters() {
        return delegate.getJavaBeansGettersAndSetters();
    }

    @Override
    public void setUseTableNameForUnambiguousFKs(boolean useTableNameForUnambiguousFKs) {
        delegate.setUseTableNameForUnambiguousFKs(useTableNameForUnambiguousFKs);
    }

    @Override
    public boolean getUseTableNameForUnambiguousFKs() {
        return delegate.getUseTableNameForUnambiguousFKs();
    }

    @Override
    public String getJavaEnumLiteral(EnumDefinition definition, String literal) {
        return delegate.getJavaEnumLiteral(definition, literal);
    }

    @Override
    public String getGlobalReferencesJavaClassExtends(Definition container, Class<? extends Definition> objectType) {
        return delegate.getGlobalReferencesJavaClassExtends(container, objectType);
    }

    @Override
    public String getJavaClassExtends(Definition definition, Mode mode) {
        return delegate.getJavaClassExtends(definition, mode);
    }

    @Override
    public List<String> getGlobalReferencesJavaClassImplements(Definition container, Class<? extends Definition> objectType) {
        return delegate.getGlobalReferencesJavaClassImplements(container, objectType);
    }

    @Override
    public List<String> getJavaClassImplements(Definition definition, Mode mode) {
        return delegate.getJavaClassImplements(definition, mode);
    }

    @Override
    public String getGlobalReferencesJavaClassName(Definition container, Class<? extends Definition> objectType) {
        return delegate.getGlobalReferencesJavaClassName(container, objectType);
    }

    @Override
    public String getGlobalReferencesJavaPackageName(Definition container, Class<? extends Definition> objectType) {
        return delegate.getGlobalReferencesJavaPackageName(container, objectType);
    }

    @Override
    public String getGlobalReferencesFileHeader(Definition container, Class<? extends Definition> objectType) {
        return delegate.getGlobalReferencesFileHeader(container, objectType);
    }

    @Override
    public String getFileHeader(Definition definition, Mode mode) {
        return delegate.getFileHeader(definition, mode);
    }

    @Override
    public String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex) {
        return delegate.getOverloadSuffix(definition, mode, overloadIndex);
    }
}
