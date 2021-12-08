package org.jooq;

import static org.jooq.impl.DSL.table;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.not;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;
//import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.jooq.impl.DSL.*;
import java.util.Arrays;

import org.jooq.*;
import org.jooq.impl.*;
import org.jooq.conf.*;

import generated.routines.DonothingArray;

public class JohnTests {
    @Test
    public void TestScratch() {
        DSLContext db = DSL.using(SQLDialect.POSTGRES);

        ResultQuery<?> query = db.parser().parseResultQuery(
                "select * from a,b,c,d where c.i = d.i and (c.c1  = b.c or c.c2 = b.c) and b.a = a.a"
        );

        System.out.println(query.getSQL());
    }

    @Test
    public void TestInlinedNumberArray() {
        DSLContext db = DSL.using(SQLDialect.POSTGRES);
        Integer[] values = { 1, 2, 3 };

        // works but doesn't exhibit the string[] inlining
//        Select<?> s = db
//                .select()
//                .from(unnest(Arrays.asList(values));
//
//        System.out.println(s.getSQL());

        // doesn't work currently, doesn't allow inlining the asList() result b/c lost type
        Select<?> s = db
                .select()
                .from(unnest(inline(Arrays.asList(values))).as("x"));

        System.out.println(s.getSQL());
    }

    @Test
    @Ignore  // this is the currently working 3rd position extension, emulated in MySQL
    public void TestMySQLInstr3rdPosEmulation() {
        // Oracle dialect also not supported for DSL Context
        // MySQL has INSTR() AND emulates the 3rd-position behavior. Maybe we can make an emulated feature instead of extending as Oracle-specific
        DSLContext db = DSL.using(SQLDialect.MYSQL);

        ResultQuery<?> query = db.parser().parseResultQuery(
                "select instr('abcdabcd','a',2) from dual"
        );

        assertEquals("select case position('a' in substring('abcdabcd', 2)) when 0 then 0 else (position('a' in substring('abcdabcd', 2)) + (2 - 1)) end", query.getSQL());
    }

    @Test
    @Ignore  // the ANSI transform parameter is not available in the open source edition
    public void TestANSIJoinTransform12338() {
        DSLContext db = DSL.using(SQLDialect.POSTGRES);
        Settings set = new Settings().withTransformTableListsToAnsiJoin(true);
        ResultQuery<?> query = db.parser().parseResultQuery(
                "select * from a,b,c,d where c.i = d.i and (c.c1  = b.c or c.c2 = b.c) and b.a = a.a"
        );

        System.out.println(query.getSQL());
    }

    @Test
    public void TestPGArrayParamInlining6359() {
        int i0 = 1234567890;
        int i1 = 1234567891;
        Long[] bigint_ary = {Long.valueOf(i0),Long.valueOf(i1)};
        DonothingArray func = new DonothingArray();
        Configuration conf = new DefaultConfiguration().set(SQLDialect.POSTGRES);

        func.setPBigints(bigint_ary);

        func.attach(conf);

        DSLContext db = DSL.using(SQLDialect.POSTGRES);

        String inlined_call = db.renderInlined(func);

        assertThat(inlined_call, allOf(
                containsString(Integer.toString(i0)),
                containsString(Integer.toString(i1))
                )
        );
        assertThat(inlined_call, not(anyOf(
                containsString("\"" + Integer.toString(i0) + "\""),
                containsString("\"" + Integer.toString(i1) + "\"")
                ))
        );
    }

    @Test
    public void Test12350CTEDeclarationMismatch() {
        DSLContext db = DSL.using(SQLDialect.POSTGRES);
        ResultQuery<?> query = db.parser().parseResultQuery(
//                "with t(a) as (select a, b from t) select * from t"
                "select 1,2,3 from table_name " +
                        "union " +
                        "select 1,2,3,4"
        );

        // error happens around CommonTableExpressionImpl.java:166
//        throw exception("Select list must contain " + degree + " columns. Got: " + select.size());
        // ParserException overridden by exception on ParserImpl:13767
    }
    
}
