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
