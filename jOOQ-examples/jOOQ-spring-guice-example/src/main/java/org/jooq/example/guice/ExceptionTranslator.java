/*
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
 */
package org.jooq.example.guice;

import javax.sql.DataSource;

import org.jooq.ExecuteContext;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DefaultExecuteListener;

import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

/**
 * This translator uses Spring to translate jOOQ {@link DataAccessException}
 * into "standard" Spring {@link org.springframework.dao.DataAccessException}.
 * <p>
 * The purpose of such a translator is to get a unified type hierarchy of SQL
 * exception semantics across various SQL vendors.
 *
 * @author Lukas Eder
 */
public class ExceptionTranslator extends DefaultExecuteListener {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 6914082794499325841L;
    private final DataSource  ds;

    public ExceptionTranslator(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void exception(ExecuteContext ctx) {
        ctx.exception(translator().translate("jOOQ", ctx.sql(), ctx.sqlException()));
    }

    private SQLExceptionTranslator translator() {
        return ds == null
            ? new SQLStateSQLExceptionTranslator()
            : new SQLErrorCodeSQLExceptionTranslator(ds);
    }
}
