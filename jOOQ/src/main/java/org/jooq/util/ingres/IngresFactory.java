/**
 * Copyright (c) 2009-2012, Lukas Eder, lukas.eder@gmail.com
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
package org.jooq.util.ingres;

import java.sql.Connection;

import org.jooq.SQLDialect;
import org.jooq.SchemaMapping;
import org.jooq.conf.Settings;
import org.jooq.impl.Factory;

/**
 * A {@link SQLDialect#INGRES} specific factory
 *
 * @author Lukas Eder
 */
@SuppressWarnings("deprecation")
public class IngresFactory extends Factory {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -2018237718414421677L;

    /**
     * Create a factory with connection and a schema mapping configured
     *
     * @param connection The connection to use with objects created from this
     *            factory
     * @param mapping The schema mapping to use with objects created from this
     *            factory
     * @deprecated - 2.0.5 - Use {@link #IngresFactory(Connection, Settings)}
     *             instead
     */
    @Deprecated
    public IngresFactory(Connection connection, SchemaMapping mapping) {
        super(connection, SQLDialect.INGRES, mapping);
    }

    /**
     * Create a factory with connection and a settings configured
     *
     * @param connection The connection to use with objects created from this
     *            factory
     * @param settings The runtime settings to apply to objects created from
     *            this factory
     */
    public IngresFactory(Connection connection, Settings settings) {
        super(connection, SQLDialect.INGRES, settings);
    }

    /**
     * Create a factory with connection
     *
     * @param connection The connection to use with objects created from this
     *            factory
     */
    public IngresFactory(Connection connection) {
        super(connection, SQLDialect.INGRES);
    }
}
