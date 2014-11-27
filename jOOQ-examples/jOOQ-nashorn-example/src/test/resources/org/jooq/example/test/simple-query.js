var DSL = Java.type("org.jooq.impl.DSL");

var Assert = Java.type("org.junit.Assert");
var Arrays = Java.type("java.util.Arrays");

var Tables = Java.type("org.jooq.example.db.h2.Tables");
var b = Tables.BOOK;
var a = Tables.AUTHOR;

var books = DSL.using(connection)
   			   .select(b.ID)
   			   .from(b)
   			   .orderBy(b.ID)
   			   .fetch(b.ID);

Assert.assertEquals(Arrays.asList([1, 2, 3, 4]), books);
