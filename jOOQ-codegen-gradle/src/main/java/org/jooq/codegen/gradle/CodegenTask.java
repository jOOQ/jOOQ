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
package org.jooq.codegen.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.Directory;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.*;
import org.gradle.work.InputChanges;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Target;
import org.jooq.tools.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The code generation task.
 */
@CacheableTask
public class CodegenTask extends DefaultTask {

    final NamedConfiguration       configuration;
    final FileCollection           codegenClasspath;
    final ProviderFactory          providers;
    final List<NamedConfiguration> named;

    @Inject
    public CodegenTask(
        NamedConfiguration configuration,
        FileCollection codegenClasspath,
        ProviderFactory providers
    ) {
        this.configuration = configuration;
        this.providers = providers;
        this.codegenClasspath = codegenClasspath;
        this.named = new ArrayList<>();

        // TODO: Can we optimise this without using internals?
        getOutputs().upToDateWhen(task -> false);
    }

    @TaskAction
    public void execute(InputChanges changes) throws Exception {
        if (named.isEmpty()) {
            ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
            URLClassLoader pluginClassLoader = getClassLoader();

            try {
                // [#2886] Add the surrounding project's dependencies to the current classloader
                //         This is the approach that worked well for the Maven plugin.
                //         There's probably a better way in Gradle.
                Thread.currentThread().setContextClassLoader(pluginClassLoader);
                GenerationTool.generate(configuration.configuration);
            }
            finally {

                // [#2886] Restore old class loader
                Thread.currentThread().setContextClassLoader(oldCL);
            }
        }
    }

    @Input
    public Provider<String> getInput() {
        return providers.provider(() ->
            configuration.configuration.toString()
        );
    }

    @Classpath
    public FileCollection getClasspath() {
        return codegenClasspath;
    }

    @OutputDirectory @Optional
    public Property<Directory> getOutputDirectory() {
        return configuration.outputDirectory;
    }

    private URLClassLoader getClassLoader() {
        List<File> classpath = new ArrayList<>(getClasspath().getFiles());

        try {
            URL urls[] = new URL[classpath.size()];

            for (int i = 0; i < urls.length; i++)
                urls[i] = classpath.get(i).toURI().toURL();

            return new URLClassLoader(urls, getClass().getClassLoader());
        }
        catch (Exception e) {
            throw new GradleException("Couldn't create a classloader.", e);
        }
    }

    static String taskName(NamedConfiguration configuration) {
        return "jooqCodegen" + (configuration.unnamed ? "" : StringUtils.toUC(configuration.name));
    }
}

