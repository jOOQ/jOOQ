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

package org.jooq.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.jooq.Attachable;
import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.JoinType;
import org.jooq.Table;
import org.jooq.TableLike;

/**
 * @author Lukas Eder
 */
class Join extends AbstractQueryPart {

    private static final long           serialVersionUID = 2275930365728978050L;

    private final TableLike<?>          table;
    private final JoinType              type;
    private final ConditionProviderImpl condition;
    private final FieldList             using;
    private final boolean               usingSyntax;

    Join(TableLike<?> table, JoinType type, Condition... conditions) {
        this.condition = new ConditionProviderImpl();
        this.condition.addConditions(conditions);
        this.using = new FieldList();
        this.usingSyntax = false;

        this.table = table;
        this.type = type;
    }

    Join(TableLike<?> table, JoinType type, Collection<? extends Field<?>> using) {
        this.condition = new ConditionProviderImpl();
        this.using = new FieldList(using);
        this.usingSyntax = true;

        this.table = table;
        this.type = type;
    }

    @Override
    public final List<Attachable> getAttachables() {
        return getAttachables(table, condition, using);
    }

    @Override
    public final int bindReference(Configuration configuration, PreparedStatement stmt, int initialIndex) throws SQLException {
        return bind0(configuration, stmt, initialIndex, false);
    }

    @Override
    public final int bindDeclaration(Configuration configuration, PreparedStatement stmt, int initialIndex)
        throws SQLException {
        return bind0(configuration, stmt, initialIndex, true);
    }

    private final int bind0(Configuration configuration, PreparedStatement stmt, int initialIndex, boolean declaration)
        throws SQLException {

        int result = initialIndex;
        if (declaration) {
            result = internal(getTable()).bindDeclaration(configuration, stmt, result);
        }
        else {
            result = internal(getTable()).bindReference(configuration, stmt, result);
        }

        if (usingSyntax) {
            result = internal(using).bindReference(configuration, stmt, result);
        }
        else {
            result = internal(getCondition()).bindReference(configuration, stmt, result);
        }

        return result;
    }

    public final Condition getCondition() {
        return condition.getWhere();
    }

    public final Table<?> getTable() {
        return table.asTable();
    }

    public final JoinType getType() {
        return type;
    }

    @Override
    public final String toSQLDeclaration(Configuration configuration, boolean inlineParameters) {
        return toSQL(configuration, inlineParameters, true);
    }

    @Override
    public final String toSQLReference(Configuration configuration, boolean inlineParameters) {
        return toSQL(configuration, inlineParameters, false);
    }

    private final String toSQL(Configuration configuration, boolean inlineParameters, boolean toSQLDeclaration) {
        StringBuilder sb = new StringBuilder();

        sb.append(getType().toSQL());
        sb.append(" ");

        if (toSQLDeclaration) {
            sb.append(internal(getTable()).toSQLDeclaration(configuration, inlineParameters));
        }
        else {
            sb.append(internal(getTable()).toSQLReference(configuration, inlineParameters));
        }

        switch (getType()) {

            // CROSS JOIN does not have any additional clauses
            case CROSS_JOIN:

            // NATURAL JOIN does not have any additional clauses
            case NATURAL_JOIN:
            case NATURAL_LEFT_OUTER_JOIN:
            case NATURAL_RIGHT_OUTER_JOIN:
                break;

            // Regular JOINs
            default: {
                if (usingSyntax) {
                    sb.append(" using (");
                    sb.append(using.toSQLNames(configuration));
                    sb.append(")");
                }
                else {
                    sb.append(" on ");
                    sb.append(internal(getCondition()).toSQLReference(configuration, inlineParameters));
                }

                break;
            }
        }


        return sb.toString();
    }
}
