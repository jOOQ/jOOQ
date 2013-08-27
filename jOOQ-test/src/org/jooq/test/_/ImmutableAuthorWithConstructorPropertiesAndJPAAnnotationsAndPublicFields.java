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
