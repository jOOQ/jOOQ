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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.impl.JooqLogger;

/**
 * @author Lukas Eder
 */
public abstract class AbstractPackageDefinition extends AbstractDefinition implements PackageDefinition {

    private static final JooqLogger log = JooqLogger.getLogger(AbstractPackageDefinition.class);

    private List<RoutineDefinition> procedures;
    private List<RoutineDefinition> functions;
    private List<RoutineDefinition> routines;

    public AbstractPackageDefinition(Database database, String name, String comment) {
        super(database, name, comment);
    }

    @Override
    @Deprecated
    public final List<RoutineDefinition> getProcedures() {
        if (procedures == null) {
            procedures = new ArrayList<RoutineDefinition>();

            for (RoutineDefinition routine : getRoutines()) {
                if (!routine.isSQLUsable()) {
                    procedures.add(routine);
                }
            }
        }

        return procedures;
    }

    @Override
    @Deprecated
    public final List<RoutineDefinition> getFunctions() {
        if (functions == null) {
            functions = new ArrayList<RoutineDefinition>();

            for (RoutineDefinition routine : getRoutines()) {
                if (routine.isSQLUsable()) {
                    functions.add(routine);
                }
            }
        }

        return functions;
    }

    @Override
    public final List<RoutineDefinition> getRoutines() {
        if (routines == null) {
            routines = new ArrayList<RoutineDefinition>();

            try {
                routines = getRoutines0();
            }
            catch (SQLException e) {
                log.error("Error while initialising package", e);
            }
        }

        return routines;
    }

    protected abstract List<RoutineDefinition> getRoutines0() throws SQLException;
}
