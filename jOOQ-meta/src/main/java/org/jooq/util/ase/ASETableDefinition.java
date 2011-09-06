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
package org.jooq.util.ase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DataTypeDefinition;
import org.jooq.util.Database;
import org.jooq.util.DefaultColumnDefinition;
import org.jooq.util.DefaultDataTypeDefinition;


/**
 * Sybase Adaptive Server table definition
 *
 * @author Lukas Eder
 */
public class ASETableDefinition extends AbstractTableDefinition {

    public ASETableDefinition(Database database, String name, String comment) {
        super(database, name, comment);
    }

    @Override
    protected List<ColumnDefinition> getElements0() throws SQLException {
        List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

        PreparedStatement stmt = null;
        try {
            stmt = create().getConnection().prepareStatement("sp_help '" + getName() + "'");
            stmt.execute();
            stmt.getMoreResults();

            stmt.getResultSet().close();
            stmt.getMoreResults();

            ResultSet rs = stmt.getResultSet();

            int position = 1;
            while (rs.next()) {
                String p = rs.getString("Prec");
                String s = rs.getString("Scale");

                int precision = 0;
                int scale = 0;

                if (p != null && !"null".equalsIgnoreCase(p.trim())) precision = Integer.valueOf(p.trim());
                if (s != null && !"null".equalsIgnoreCase(s.trim())) scale = Integer.valueOf(s.trim());

                DataTypeDefinition type = new DefaultDataTypeDefinition(getDatabase(),
                    rs.getString("Type"),
                    precision, scale);

                result.add(new DefaultColumnDefinition(
                    getDatabase().getTable(getName()),
                    rs.getString("Column_name"),
                    position++,
                    type,
                    rs.getBoolean("Identity"),
                    null));
            }
        }
        finally {
            if (stmt != null) {
                stmt.getMoreResults(Statement.CLOSE_ALL_RESULTS);
                stmt.close();
            }
        }
        return result;
    }
}
