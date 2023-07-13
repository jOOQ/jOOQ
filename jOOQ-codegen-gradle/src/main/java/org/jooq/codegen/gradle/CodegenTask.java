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
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.work.InputChanges;
import org.jooq.codegen.GenerationTool;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * The code generation task.
 */
@CacheableTask
public class CodegenTask extends DefaultTask {

    private final NamedConfiguration configuration;
    private final ProviderFactory    providers;
    private final List<File>         classpath;
    private final Directory          outputDirectory;

    @Inject
    public CodegenTask(
        NamedConfiguration configuration,
        FileCollection runtimeClasspath,
        FileCollection codegenClasspath,
        ProviderFactory providers,
        ProjectLayout layout
    ) {
        this.configuration = configuration;
        this.providers = providers;
        this.classpath = new ArrayList<>();
        this.classpath.addAll(codegenClasspath.getFiles());

        // TODO: Support basedir configuration
        this.outputDirectory = layout.getProjectDirectory().dir(
            configuration.configuration.getGenerator().getTarget().getDirectory()
        );

        // TODO: Can we optimise this without using internals?
        getOutputs().upToDateWhen(task -> false);
    }

    @TaskAction
    public void execute(InputChanges changes) throws Exception {
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

    @Input
    public Provider<String> getInput() {
        return providers.provider(() ->
            configuration.configuration.toString()
        );
    }

    @Classpath
    public Iterable<File> getClasspath() {
        return classpath;
    }

    @OutputDirectory
    public Directory getOutputDirectory() {
        return outputDirectory;
    }

    private URLClassLoader getClassLoader() {
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
}

