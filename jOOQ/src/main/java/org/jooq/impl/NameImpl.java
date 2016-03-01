/**
 * Copyright (c) 2009-2016, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
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
 * For more information, please visit: http://www.jooq.org/licenses
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
package org.jooq.impl;

import java.util.Arrays;

import org.jooq.Clause;
import org.jooq.CommonTableExpression;
import org.jooq.Context;
import org.jooq.DerivedColumnList;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Select;
import org.jooq.WindowDefinition;
import org.jooq.WindowSpecification;
import org.jooq.tools.StringUtils;

/**
 * The default implementation for a SQL identifier
 *
 * @author Lukas Eder
 */
class NameImpl extends AbstractQueryPart implements Name {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 8562325639223483938L;

    private String[]          qualifiedName;

    NameImpl(String[] qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    @Override
    public final void accept(Context<?> ctx) {

        // [#3437] Fully qualify this field only if allowed in the current context
        if (ctx.qualify()) {
            String separator = "";

            for (String name : qualifiedName) {
                if (!StringUtils.isEmpty(name)) {
                    ctx.sql(separator).literal(name);
                    separator = ".";
                }
            }
        }
        else {
            ctx.literal(qualifiedName[qualifiedName.length - 1]);
        }
    }

    @Override
    public final Clause[] clauses(Context<?> ctx) {
        return null;
    }

    @Override
    public final String[] getName() {
        return qualifiedName;
    }

    @Override
    public final WindowDefinition as(WindowSpecification window) {
        return new WindowDefinitionImpl(this, window);
    }

    @Override
    public final <R extends Record> CommonTableExpression<R> as(Select<R> select) {
        return fields(new String[0]).as(select);
    }

    @Override
    public final DerivedColumnList fields(String... fieldNames) {
        if (qualifiedName.length != 1)
            throw new IllegalStateException("Cannot create a DerivedColumnList from a qualified name : " + Arrays.asList(qualifiedName));

        return new DerivedColumnListImpl(qualifiedName[0], fieldNames);
    }

    // ------------------------------------------------------------------------
    // XXX: Object API
    // ------------------------------------------------------------------------

    @Override
    public int hashCode() {
        return Arrays.hashCode(getName());
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        // [#1626] NameImpl equality can be decided without executing the
        // rather expensive implementation of AbstractQueryPart.equals()
        if (that instanceof NameImpl) {
            return Arrays.equals(getName(), (((NameImpl) that).getName()));
        }

        return super.equals(that);
    }
}
