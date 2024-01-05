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
import org.jooq.util.jaxb.tools.MiniJAXB;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
        codegenClasspath.setDescription("The classpath used for code generation, including JDBC drivers, code generation extensions, etc.");

        SourceSetContainer source = project
            .getExtensions()
            .getByType(SourceSetContainer.class);

        jooq.getExecutions().create("", configuration -> {
            configuration.unnamed = true;
            configuration.configuration = CodegenPluginExtension.copy(jooq.configuration);
        });

        AtomicReference<Task> all = new AtomicReference<>();

        jooq.getExecutions().configureEach(configuration -> {
            configuration.configuration = MiniJAXB.append(
                MiniJAXB.append(new org.jooq.meta.jaxb.Configuration(), CodegenPluginExtension.copy(jooq.configuration)),
                CodegenPluginExtension.copy(configuration.configuration)
            );

            // [#15966] [#15974] TODO: The default, unnamed execution only makes sense in the absence of executions, but how to add it conditionally?
            CodegenTask task = project.getTasks().create(
                "jooqCodegen" + (configuration.unnamed ? "" : StringUtils.toUC(configuration.name)),
                CodegenTask.class,
                configuration,
                runtimeClasspath,
                codegenClasspath
            );

            task.setDescription("jOOQ code generation" + (configuration.unnamed ? "" : " for the " + configuration.name + " execution"));
            task.setGroup("jOOQ");

            source.configureEach(sourceSet -> {
                if (configuration.unnamed && sourceSet.getName().equals("main") ||
                        sourceSet.getName().equals(configuration.name)) {
                    sourceSet.getJava().srcDir(task.getOutputDirectory());
                }
            });

            if (!configuration.unnamed) {
                if (all.get() != null) {
                    all.get().dependsOn(task);
                }
                else {
                    all.set(project.getTasks().create("jooqCodegenAll",
                        t -> {
                            t.setDescription("jOOQ code generation for all executions");
                            t.setGroup("jOOQ");
                            t.dependsOn(task);
                        }
                    ));
                }
            }
        });
    }
}
