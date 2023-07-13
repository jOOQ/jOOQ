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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.jooq.tools.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The jOOQ Codegen Plugin
 *
 * @author Lukas Eder
 */
public class CodegenPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        CodegenPluginExtension jooq = project.getExtensions().create("jooq",
            CodegenPluginExtension.class
        );

        Configuration runtimeClasspath = project.getConfigurations().getByName("runtimeClasspath");
        Configuration codegenClasspath = project.getConfigurations().create("jooqCodegen");

        SourceSetContainer source = project
            .getExtensions()
            .getByType(SourceSetContainer.class);

        project.afterEvaluate(p -> {
            boolean unnamed = true;
            List<Task> tasks = new ArrayList<>();

            for (NamedConfiguration configuration : p.getExtensions().getByType(CodegenPluginExtension.class).configurations()) {
                unnamed &= configuration.unnamed;

                CodegenTask task = p.getTasks().create(
                    "jooqCodegen" + (configuration.unnamed ? "" : StringUtils.toUC(configuration.name)),
                    CodegenTask.class,
                    configuration,
                    runtimeClasspath,
                    codegenClasspath
                );

                task.setDescription("jOOQ code generation" + (configuration.unnamed ? "" : " for " + configuration.name + " execution"));
                task.setGroup("jOOQ");

                Task compileJava = p.getTasks().findByName("compileJava");
                if (compileJava != null)
                    compileJava.dependsOn(task);

                source.configureEach(sourceSet -> {
                    if (configuration.unnamed && sourceSet.getName().equals("main") ||
                            sourceSet.getName().equals(configuration.name)) {
                        sourceSet.getJava().srcDir(task.getOutputDirectory());
                    }
                });

                tasks.add(task);
            }

            if (!unnamed) {
                p.getTasks().create(
                    "jooqCodegen",
                    task -> {
                        task.setDescription("jOOQ code generation for all executions");
                        task.setGroup("jOOQ");
                        task.setDependsOn(tasks);
                    }
                );
            }
        });
    }
}
