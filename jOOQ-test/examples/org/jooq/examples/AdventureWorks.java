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
package org.jooq.examples;


import static org.jooq.examples.sqlserver.adventureworks.humanresources.Tables.Department;
import static org.jooq.examples.sqlserver.adventureworks.humanresources.Tables.Employee;
import static org.jooq.examples.sqlserver.adventureworks.humanresources.Tables.EmployeeAddress;
import static org.jooq.examples.sqlserver.adventureworks.humanresources.Tables.EmployeeDepartmentHistory;
import static org.jooq.examples.sqlserver.adventureworks.person.Tables.Contact;
import static org.jooq.impl.DSL.val;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class AdventureWorks {

    public static void main(String[] args) throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=AdventureWorks;integratedSecurity=true");

        DSLContext create = DSL.using(connection, SQLDialect.SQLSERVER);

        System.out.println(create
              .select(Employee.fields())
              .select(val("###"))
              .select(Department.fields())
              .select(val("###"))
              .select(Contact.fields())
              .from(Employee)
              .join(EmployeeAddress).using(Employee.EmployeeID)
              .join(EmployeeDepartmentHistory).using(Employee.EmployeeID)
              .join(Department).using(Department.DepartmentID)
              .join(Contact).using(Contact.ContactID)
              .fetch());
    }
}
