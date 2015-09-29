var DSL = Java.type("org.jooq.impl.DSL");
var Settings = Java.type("org.jooq.conf.Settings");
var RenderNameStyle = Java.type("org.jooq.conf.RenderNameStyle");

var Assert = Java.type("org.junit.Assert");
var Arrays = Java.type("java.util.Arrays");

var Tables = Java.type("org.jooq.example.db.h2.Tables");
var b = Tables.BOOK;
var a = Tables.AUTHOR;


// Unfortunately, there is a Nashorn / Java interoperablility issue documented here:
// http://stackoverflow.com/q/25603191/521799
// 
// To work around this issue, tables should probably be supplied in JavaScript arrays,
// in order to explicitly invoke the method accepting varargs, instead of the overloaded method

var authors = DSL.using(connection, new Settings().withRenderNameStyle(RenderNameStyle.AS_IS))
                 .select(a.ID)
                 .from([a])
                 .orderBy(a.ID)
                 .fetch(a.ID);

Assert.assertEquals(Arrays.asList([1, 2]), authors);

var authors = DSL.using(connection, new Settings().withRenderNameStyle(RenderNameStyle.AS_IS))
                 .select(a.ID)
                 .from([DSL.tableByName("author")])
                 .orderBy(a.ID)
                 .fetch(a.ID);

Assert.assertEquals(Arrays.asList([1, 2]), authors);