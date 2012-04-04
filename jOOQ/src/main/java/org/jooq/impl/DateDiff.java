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
package org.jooq.impl;

import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.function;
import static org.jooq.impl.Factory.literal;

import java.sql.Date;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class DateDiff extends AbstractFunction<Integer> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4813228000332771961L;

    private final Field<Date> date1;
    private final Field<Date> date2;

    DateDiff(Field<Date> date1, Field<Date> date2) {
        super("datediff", SQLDataType.INTEGER, date1, date2);

        this.date1 = date1;
        this.date2 = date2;
    }

    @Override
    final Field<Integer> getFunction0(Configuration configuration) {
        switch (configuration.getDialect()) {
            case ASE:
                return function("datediff", getDataType(), literal("day"), date2, date1);

            case MYSQL:
                return function("datediff", getDataType(), date1, date2);

            case DB2:
                return function("days", getDataType(), date1).sub(
                       function("days", getDataType(), date2));

            case DERBY:
                return new FnPrefixFunction<Integer>("timestampdiff",
                    getDataType(), field("SQL_TSI_DAY"), date2, date1);

            case H2:
                return function("datediff", getDataType(), literal("'day'"), date2, date1);

            case CUBRID:
            case ORACLE:

            // TODO [#585] This cast shouldn't be necessary
            return date1.sub(date2).cast(Integer.class);
        }

        return null;
    }
}
