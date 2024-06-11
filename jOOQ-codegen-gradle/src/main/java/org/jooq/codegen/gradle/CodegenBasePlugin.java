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

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.jetbrains.annotations.NotNull;
import org.jooq.tools.StringUtils;
import org.jooq.util.jaxb.tools.MiniJAXB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * The jOOQ Codegen Plugin
 *
 * @author Lukas Eder
 */
public class CodegenBasePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        CodegenPluginExtension jooq = project.getExtensions().create("jooq",
                CodegenPluginExtension.class
        );

        Configuration codegenClasspath = project.getConfigurations().create("jooqCodegen");
        codegenClasspath.setDescription("The classpath used for code generation, including JDBC drivers, code generation extensions, etc.");

        jooq.getExecutions().create("", configuration -> {
            configuration.unnamed = true;
        });

        List<NamedConfiguration> named = new ArrayList<>();

        jooq.getExecutions().configureEach(configuration -> {
            if (!configuration.unnamed)
                named.add(configuration);

            project.getTasks().register(
                    CodegenTask.taskName(configuration),
                    CodegenTask.class,
                    configuration,
                    codegenClasspath
            ).configure(configureTask(project, named, configuration));
        });
    }

    private static Action<CodegenTask> configureTask(
            Project project,
            List<NamedConfiguration> named,
            NamedConfiguration configuration
    ) {
        return task -> {
            if (configuration.unnamed) {
                task.named.addAll(named);

                for (NamedConfiguration other : named)
                    task.dependsOn(CodegenTask.taskName(other));
            }

            task.setDescription("jOOQ code generation" + (configuration.unnamed ? " for all executions" : " for the " + configuration.name + " execution"));
            task.setGroup("jOOQ");

            task.doFirst(t -> {
                SourceSetContainer source = project
                        .getExtensions()
                        .findByType(SourceSetContainer.class);

                if (source != null) {
                    source.configureEach(sourceSet -> {
                        if (sourceSet.getName().equals("main"))
                            sourceSet.getJava().srcDir(task.getOutputDirectory());
                    });
                }
            });
        };
    }
}
