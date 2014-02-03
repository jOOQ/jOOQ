/**
 * Copyright (c) 2009-2014, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.test.h2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;

/**
 * A generator for large-schema.sql
 *
 * @author Lukas Eder
 */
public class GenerateLargeSchema {

    public static void main(String[] args) throws Exception {
        URL resource = GenerateLargeSchema.class.getResource("/org/jooq/test/h2/large-schema.sql");
        File file = new File(new URI(resource.toURI().toString().replace("/bin/", "/src/")));
        PrintWriter w = new PrintWriter(new FileOutputStream(file));

        try {
            w.println("DROP SCHEMA IF EXISTS large/");
            w.println("CREATE SCHEMA large/");

            w.println("CREATE TABLE large.t00000 (id INT,              CONSTRAINT pk_00000 PRIMARY KEY (id))/");

            for (int i = 1; i < 15000; i++) {
                w.println(String.format(
                    "CREATE TABLE large.t%1$05d (id INT, prev_id INT, CONSTRAINT pk_%1$05d PRIMARY KEY (id), CONSTRAINT fk_%1$05d FOREIGN KEY (prev_id) REFERENCES t%2$05d(id))/",
                    i, i - 1
                ));
            }
        }
        finally {
            w.flush();
            w.close();
        }
    }
}
