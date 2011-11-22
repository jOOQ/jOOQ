/**
 * Copyright (c) 2009-2011, Lukas Eder, lukas.eder@gmail.com
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

package org.jooq.util;

import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Class generation facility that can be invoked from ant.
 *
 * @author Lukas Eder
 */
public class GenerationTask extends Task {

	private Properties properties = new Properties();

	{
		// Set default values
		properties.setProperty("jdbc.Driver", "com.mysql.jdbc.Driver");

		properties.setProperty("generator", "org.jooq.util.DefaultGenerator");
		properties.setProperty("generator.database", "org.jooq.util.mysql.MySQLDatabase");
		properties.setProperty("generator.database.includes", ".*");
		properties.setProperty("generator.database.excludes", "");
		properties.setProperty("generator.generate.records", "true");
		properties.setProperty("generator.generate.relations", "false");
		properties.setProperty("generator.generate.instance-fields", "true");
		properties.setProperty("generator.generate.unsigned-types", "true");

		properties.setProperty("generator.target.directory", ".");
	}

	public void setJdbcdriver(String value) {
		properties.setProperty("jdbc.Driver", value);
	}

	public void setJdbcurl(String value) {
		properties.setProperty("jdbc.URL", value);
	}

	public void setJdbcschema(String value) {
		properties.setProperty("jdbc.Schema", value);
	}

	public void setJdbcuser(String value) {
		properties.setProperty("jdbc.User", value);
	}

	public void setJdbcpassword(String value) {
		properties.setProperty("jdbc.Password", value);
	}

	public void setGenerator(String value) {
		properties.setProperty("generator", value);
	}

	public void setGeneratordatabase(String value) {
		properties.setProperty("generator.database", value);
	}

    public void setGeneratordatabaseincludes(String value) {
        properties.setProperty("generator.database.includes", value);
    }

    public void setGeneratordatabaseexcludes(String value) {
        properties.setProperty("generator.database.excludes", value);
    }

    public void setGeneratordatabaseinputschema(String value) {
        properties.setProperty("generator.database.input-schema", value);
    }

    public void setGeneratordatabaseoutputschema(String value) {
        properties.setProperty("generator.database.output-schema", value);
    }

	public void setGeneratorgeneraterecords(String value) {
		properties.setProperty("generator.generate.records", value);
	}

    public void setGeneratorgeneraterelations(String value) {
        properties.setProperty("generator.generate.relations", value);
    }

    public void setGeneratorgenerateinstancefields(String value) {
        properties.setProperty("generator.generate.instance-fields", value);
    }

    public void setGeneratorgenerateunsignedtypes(String value) {
        properties.setProperty("generator.generate.unsigned-types", value);
    }

	public void setGeneratortargetpackage(String value) {
		properties.setProperty("generator.target.package", value);
	}

	public void setGeneratortargetdirectory(String value) {
		properties.setProperty("generator.target.directory", value);
	}

	@Override
	public void execute() throws BuildException {
		try {
			GenerationTool.main(properties);
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}
}
