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


/**
 * @author Lukas Eder
 */
public class BookWithoutAnnotations {

    public Integer        id;
    public int            id2;
    public int            ID;
    public String         title;
    public String         firstName;
    public String         firstName2;
    public String         lastName;
    public String         lastName2;
    public String         LAST_NAME;
    public java.util.Date DATE_OF_BIRTH;
    public java.sql.Date  dateOfBirth;

    public void setId(long id) {
        id2 = (int) id;
    }

    public void setFirstName(String f) {
        firstName2 = f;
    }

    public void setLAST_NAME(String l) {
        lastName = l;
    }

    public void LAST_NAME(String l) {
        lastName2 = l;
    }

    @SuppressWarnings("unused")
    public void setLAST_NAME(String l, String tooManyParameters) {
        throw new AssertionError();
    }

    public void setLAST_NAME() {
        throw new AssertionError();
    }

    @Override
    public String toString() {
        return "NonJPABook [id=" + id + ", title=" + title + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}
