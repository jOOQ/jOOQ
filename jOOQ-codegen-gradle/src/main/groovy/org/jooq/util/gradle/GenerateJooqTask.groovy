/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq.util.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.jooq.util.GenerationTool
import org.jooq.util.jaxb.Generator
import org.jooq.util.jaxb.Target

/**
 * A task that performs jOOQ code generation.
 *
 * @author Ben Manes (ben.manes@gmail.com)
 */
class GenerateJooqTask extends DefaultTask {
  def configuration

  GenerateJooqTask() {
    description = 'Generates jOOQ Java classes.'
    group = 'Build'

    project.afterEvaluate {
      parseConfiguration()
      outputs.dir configuration.generator.target.directory
      project.sourceSets.main.java.srcDirs += [ configuration.generator.target.directory ]
    }
  }

  @TaskAction
  def generateJooq() {
    logger.info 'Using this configuration:\n{}', project.jooq.xml
    GenerationTool.main(configuration);
  }

  def parseConfiguration() {
    configuration = GenerationTool.load(new ByteArrayInputStream(project.jooq.xml.getBytes('utf8')))
    if (useDefaultTargetDirectory()) {
      gradleTargertDir()
    }
  }

  def useDefaultTargetDirectory() {
    def parsed = new XmlParser().parseText(project.jooq.xml)
    parsed.generator.target.directory.text().isEmpty()
  }

  def gradleTargertDir() {
    configuration.generator = configuration.generator ?: new Generator()
    configuration.generator.target = configuration.generator.target ?: new Target()
    configuration.generator.target.directory = "${project.buildDir}/generated-sources/jooq"
  }
}
