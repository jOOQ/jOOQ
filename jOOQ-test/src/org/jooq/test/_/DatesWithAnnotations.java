/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is triple-licensed under ASL 2.0, AGPL 3.0, and jOOQ EULA
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   ASL 2.0 or jOOQ EULA.
 * - If you're using this work with at least one commercial database, you may
 *   choose AGPL 3.0 or jOOQ EULA.
 *
 * For more information, please visit http://www.jooq.org/licenses
 *
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
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
 * AGPL 3.0
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 *
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details: http://www.jooq.org/eula
 */
package org.jooq.test._;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Lukas Eder
 */
@Entity
public class DatesWithAnnotations {

    // -------------------------------------------------------------------------
    // Mapping to java.util.Calendar
    // -------------------------------------------------------------------------

    @Column(name = "DATE_OF_BIRTH")
    public Calendar cal1;
    public Calendar cal2;
    public Calendar cal3;

    @Column(name = "DATE_OF_BIRTH")
    public void setCal(Calendar calendar) {
        cal2 = calendar;
    }

    public void setC(Calendar calendar) {
        cal3 = calendar;
    }

    @Column(name = "DATE_OF_BIRTH")
    public Calendar getC() {
        return cal3;
    }

    // -------------------------------------------------------------------------
    // Mapping to java.util.Date
    // -------------------------------------------------------------------------

    @Column(name = "DATE_OF_BIRTH")
    public Date date1;
    public Date date2;
    public Date date3;

    @Column(name = "DATE_OF_BIRTH")
    public void setDate(Date date) {
        date2 = date;
    }

    public void setD(Date date) {
        date3 = date;
    }

    @Column(name = "DATE_OF_BIRTH")
    public Date getD() {
        return date3;
    }

    // -------------------------------------------------------------------------
    // Mapping to java.lang.Long
    // -------------------------------------------------------------------------

    @Column(name = "DATE_OF_BIRTH")
    public Long long1;
    public Long long2;
    public Long long3;

    @Column(name = "DATE_OF_BIRTH")
    public void setLong(Long l) {
        long2 = l;
    }

    public void setL(Long l) {
        long3 = l;
    }

    @Column(name = "DATE_OF_BIRTH")
    public Long getL() {
        return long3;
    }

    // -------------------------------------------------------------------------
    // Mapping to long
    // -------------------------------------------------------------------------

    @Column(name = "DATE_OF_BIRTH")
    public long primitiveLong1;
    public long primitiveLong2;
    public long primitiveLong3;

    @Column(name = "DATE_OF_BIRTH")
    public void setPrimitiveLong(long l) {
        primitiveLong2 = l;
    }

    public void setPL(long l) {
        primitiveLong3 = l;
    }

    @Column(name = "DATE_OF_BIRTH")
    public long getPL() {
        return primitiveLong3;
    }
}
