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

import java.beans.ConstructorProperties;
import java.util.Date;

import javax.persistence.Column;

/**
 * @author Lukas Eder
 */
public class ImmutableAuthorWithConstructorPropertiesAndJPAAnnotationsAndPublicFields {

    @Column(name = "FIRST_NAME")
    public final String f1;

    @Column(name = "LAST_NAME")
    public final String f2;

    @Column(name = "ID")
    public final int    f3;

    @Column(name = "DATE_OF_BIRTH")
    public final Date   f4;

    // Check if setAccessible is called correctly
    @ConstructorProperties({ "f1", "f2", "f3", "f4" })
    ImmutableAuthorWithConstructorPropertiesAndJPAAnnotationsAndPublicFields(String firstName, String lastName, int id,
        Date dateOfBirth) {

        this.f1 = firstName;
        this.f2 = lastName;
        this.f3 = id;
        this.f4 = dateOfBirth;
    }

    // This should never be called
    @SuppressWarnings("unused")
    ImmutableAuthorWithConstructorPropertiesAndJPAAnnotationsAndPublicFields(int ID, String firstName, String lastName) {
        throw new RuntimeException();
    }
}
