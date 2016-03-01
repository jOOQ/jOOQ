package org.jooq.groovy

import static org.jooq.impl.DSL.*
import static org.jooq.groovy.example.h2.Tables.*

import groovy.sql.Sql
import org.jooq.*
import org.jooq.impl.DSL

sql = Sql.newInstance(
    'jdbc:h2:~/scala-test', 'sa', '', 'org.h2.Driver')

a = T_AUTHOR.as("a")
b = T_BOOK.as("b")

DSL.using(sql.connection)
   .select(a.FIRST_NAME, a.LAST_NAME, b.TITLE)
   .from(a)
   .join(b).on(a.ID.eq(b.AUTHOR_ID))
   .fetchInto ({
       r -> println(
           "${r.getValue(a.FIRST_NAME)} " +
           "${r.getValue(a.LAST_NAME)} " +
           "has written ${r.getValue(b.TITLE)}"
       )
   } as RecordHandler)


   