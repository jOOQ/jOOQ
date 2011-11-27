import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.impl.Factory;
import org.jooq.util.oracle.OracleFactory;

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

public class Run {

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "TEST", "TEST");
        Factory create = new OracleFactory(connection);

        System.out.println(create.fetch("with data as ("+
            " select 'Lukas'      as employee,"+
            "       'SoftSkills' as company, "+
            "       80000        as salary, "+
            "       2007         as year "+
            " from dual"+
            " union all select 'Lukas', 'SoftSkills', 80000,  2008 from dual"+
            " union all select 'Lukas', 'SmartSoft',  90000,  2009 from dual"+
            " union all select 'Lukas', 'SmartSoft',  95000,  2010 from dual"+
            " union all select 'Lukas', 'jOOQ',       200000, 2011 from dual"+
            " union all select 'Lukas', 'jOOQ',       250000, 2012 from dual"+
            " union all select 'Tom',   'SoftSkills', 89000,  2007 from dual"+
            " union all select 'Tom',   'SoftSkills', 90000,  2008 from dual"+
            " union all select 'Tom',   'SoftSkills', 91000,  2009 from dual"+
            " union all select 'Tom',   'SmartSoft',  92000,  2010 from dual"+
            " union all select 'Tom',   'SmartSoft',  93000,  2011 from dual"+
            " union all select 'Tom',   'SmartSoft',  94000,  2012 from dual"+
        " )"+
        " select grouping_id(employee, company) id, company, employee, avg(salary)"+
        " from data"+
        " group by cube(employee, company)"));
    }

}
