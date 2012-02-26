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

import static org.jooq.impl.Factory.field;
import static org.jooq.impl.Factory.function;
import static org.jooq.impl.Factory.literal;
import static org.jooq.impl.Factory.val;

import java.math.BigDecimal;

import org.jooq.Configuration;
import org.jooq.Field;

/**
 * @author Lukas Eder
 */
class DateSub<T> extends AbstractFunction<T> {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = -4070594108194592245L;

    private final Field<T>    field;
    private final Number      value;

    DateSub(Field<T> field, Number value) {
        super("-", field.getDataType(), field);

        this.field = field;
        this.value = value;
    }

    @Override
    final Field<T> getFunction0(Configuration configuration) {
        switch (configuration.getDialect()) {
            case ASE:
                return function("dateadd", getDataType(), literal("day"), val(-value.intValue()), field);

            case DB2:
            case HSQLDB:
                return field.sub(field("? day", BigDecimal.class, value));

            case DERBY:
                return new FnPrefixFunction<T>("timestampadd", getDataType(),
                    field("SQL_TSI_DAY"),
                    val(-value.intValue()),
                    field);

            case INGRES:
                return field.sub(field("date('" + value + " days')", BigDecimal.class));

            case MYSQL:
                return function("timestampadd", getDataType(), field("day"), val(-value.intValue()), field);

            case POSTGRES:
                return field.sub(field("interval '" + value + " days'", BigDecimal.class));

            case SQLITE:
                return function("datetime", getDataType(), field, val("-" + value + " day"));

            default:
                return field.sub(val(value));
        }
    }
}
