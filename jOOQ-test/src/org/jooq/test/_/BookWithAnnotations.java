/**
 * Copyright (c) 2009-2014, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 *
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 *
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
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
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 *
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.test._;

import javax.persistence.Column;

/**
 * @author Lukas Eder
 */
public class BookWithAnnotations {

    // JPA-annotated members
    // ---------------------
    @Column(name = "ID")
    public Integer id;

    @Column(name = "ID")
    public Long id4;

    @Column(name = "ID")
    public int     id2;

    @Column(name = "TITLE")
    public String  title;

    @Column(name = "FIRST_NAME")
    public String  firstName;

    @Column(name = "DATE_OF_BIRTH")
    public java.util.Date dateOfBirth;

    // Members without annotations
    // ---------------------------
    public int     id3;
    public long    id5;
    public String  firstName2;
    public String  lastName;
    public String  lastName2;

    // Methods with annotations
    // ------------------------
    @Column(name = "ID")
    public void setId(long id) {
        id3 = (int) id;
    }

    @Column(name = "ID")
    public void setId(Long id) {
        id5 = (int) (long) id;
    }

    @Column(name = "FIRST_NAME")
    public void setFirstName(String f) {
        firstName2 = f;
    }

    @Column(name = "LAST_NAME")
    public void setLastName(String l) {
        lastName = l;
    }

    public void setLAST_NAME(String l) {
        lastName2 = l;
    }

    @Column(name = "LAST_NAME")
    public String getLAST_NAME() {
        return lastName2;
    }

    @Column(name = "LAST_NAME")
    @SuppressWarnings("unused")
    public void tooManyParameters(String l, String tooMany) {
        throw new AssertionError();
    }

    @Override
    public String toString() {
        return "JPABook [id=" + id + ", title=" + title + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }
}
