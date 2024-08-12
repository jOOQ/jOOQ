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
package org.jooq.codegen.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Task;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.*;
import org.gradle.work.InputChanges;
import org.jooq.codegen.GenerationTool;
import org.jooq.tools.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Boolean.FALSE;

/**
 * The code generation task.
 */
@CacheableTask
public class CodegenTask extends DefaultTask {

    final NamedConfiguration       configuration;
    final FileCollection           codegenClasspath;
    final ProviderFactory          providers;
    final List<NamedConfiguration> named;
    final Property<Boolean>        caching;

    @Inject
    public CodegenTask(
        NamedConfiguration configuration,
        FileCollection codegenClasspath,
        ProviderFactory providers,
        ObjectFactory objects
    ) {
        this.configuration = configuration;
        this.providers = providers;
        this.codegenClasspath = codegenClasspath;
        this.named = new ArrayList<>();
        this.caching = objects.property(Boolean.class).convention(true);

        getOutputs().cacheIf("Caching is activated only in the presence of explicit inputs and when output isn't up to date", CodegenTask::upToDate);

        // [#16318] When the task is up-to-date, we still have to register our source set contributions, which
        //          apparently aren't being cached by gradle's build cache.
        getOutputs().upToDateWhen(task -> upToDate(task));
    }

    @SuppressWarnings("unchecked")
    static boolean upToDate(Task task) {

        // [#16318] Allow for turning off the feature
        if (task.hasProperty("caching") && FALSE.equals(((Property<Boolean>) task.property("caching")).get()))
            return false;

        TaskInputs inputs = task.getInputs();

        // [#16318] .jar files from the classpath don't count as inputs
        return !inputs.getFiles().filter(f -> !f.getName().endsWith(".jar")).isEmpty()
            || inputs.getHasSourceFiles()

            // There are input properties other than our own declared @Input
            || inputs.getProperties().size() > 2;
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
                GenerationTool.generate(configuration.getConfiguration());
            }
            finally {

                // [#2886] Restore old class loader
                Thread.currentThread().setContextClassLoader(oldCL);
            }
        }
    }

    @Input
    public Provider<String> getConfiguration() {
        return providers.provider(() ->
            configuration.getConfiguration().toString()
        );
    }

    @Input
    public Property<Boolean> getCaching() {
        return caching;
    }

    @Classpath
    public FileCollection getClasspath() {
        return codegenClasspath;
    }

    @OutputDirectories
    public List<DirectoryProperty> getOutputDirectory() {
        if (named.isEmpty() && configuration.getOutputDirectory() != null)
            return Arrays.asList(configuration.getOutputDirectory());
        else
            return Arrays.asList();
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

