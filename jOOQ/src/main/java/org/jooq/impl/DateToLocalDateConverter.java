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
 *
 *
 *
 */
package org.jooq.impl;

import java.sql.Date;
import java.time.LocalDate;

import org.jooq.Converter;

/**
 * @author Lukas Eder
 * @deprecated - 3.15.0 - [#11505] - Use
 *             {@link Converter#ofNullable(Class, Class, java.util.function.Function, java.util.function.Function)}
 *             instead, e.g.
 *             <code>Converter.ofNullable(Date.class, LocalDate.class, Date::toLocalDate, Date::valueOf)</code>.
 */
@Deprecated
public final class DateToLocalDateConverter extends AbstractConverter<Date, LocalDate> {

    public DateToLocalDateConverter() {
        super(Date.class, LocalDate.class);
    }

    @Override
    public final LocalDate from(Date t) {
        return t == null ? null : t.toLocalDate();
    }

    @Override
    public final Date to(LocalDate u) {
        return u == null ? null : Date.valueOf(u);
    }
}
