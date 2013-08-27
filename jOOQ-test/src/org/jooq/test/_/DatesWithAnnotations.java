/**
 * Copyright (c) 2009-2013, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed Open Source, under LGPL and jOOQ EULA
 * =============================================================================
 * You may freely choose which license applies to you. For more information 
 * about licensing, please visit http://www.jooq.org/licenses
 * 
 * LGPL:  
 * -----------------------------------------------------------------------------
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.
 * If not, see http://www.gnu.org/licenses.
 * 
 * jOOQ End User License Agreement:
 * -----------------------------------------------------------------------------
 * This library is commercial software; you may not redistribute it nor
 * modify it.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ End User
 * License Agreement for more details.
 * 
 * You should have received a copy of the jOOQ End User License Agreement
 * along with this library.
 * If not, see http://www.jooq.org/eula
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
