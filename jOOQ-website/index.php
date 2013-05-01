<?php
$home = true;
require 'frame.php';
function getH1() {
	return 'jOOQ : A peace treaty between SQL and Java';
}
function getActiveMenu() {
	return "home";
}
function printContent() {
?>
<h2 id="News"><a href="#News" name="News">#</a> News</h2>
<p>Join the <a href="http://www.jug.ch/html/events/2013/jooq_lu.html">JUGS in Lucerne, Switzerland</a> on June 5, 2013 for an introductory session on jOOQ!</p>
<h2 id="Philosophy"><a href="#Philosophy" name="Philosophy">#</a> Philosophy</h2>
<p>SQL was never meant to be abstracted. To be confined in the narrow boundaries
			of heavy mappers, hiding the beauty and simplicity of relational data.
			SQL was never meant to be object-oriented. SQL was never meant to be
			anything other than... SQL!</p>
<h2 id="jOOQ-code"><a href="#jOOQ-code" name="jOOQ-code">#</a> What does jOOQ code look like?</h2>
<p>It's simple. With the jOOQ DSL, SQL looks almost as if it were
natively supported by Java. For instance, get all books published in 2011, ordered by title</p>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td width="50%" class="left"><pre class="prettyprint lang-sql">
  SELECT * FROM BOOK
   WHERE PUBLISHED_IN = 2011
ORDER BY TITLE</pre></td>
		<td width="50%" class="right"><pre class="prettyprint lang-java">
create.selectFrom(BOOK)
      .where(PUBLISHED_IN.equal(2011))
      .orderBy(TITLE)</pre></td>
	</tr>
</table>

<p>jOOQ also supports more complex SQL statements. get all authors'
	first and last names, and the number of books they've written in
	German, if they have written more than five books in German in the last
	three years (from 2011), and sort those authors by last names limiting
	results to the second and third row, then lock first and last names
	columns for update</p>
<table width="100%" cellpadding="0" cellspacing="0">
<tr>
		<td width="50%" class="left"><pre class="prettyprint lang-sql">
  SELECT AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME, COUNT(*)
    FROM AUTHOR
    JOIN BOOK ON AUTHOR.ID = BOOK.AUTHOR_ID
   WHERE BOOK.LANGUAGE = 'DE'
     AND BOOK.PUBLISHED &gt; DATE '2008-01-01'
GROUP BY AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME
  HAVING COUNT(*) &gt; 5
ORDER BY AUTHOR.LAST_NAME ASC NULLS FIRST
   LIMIT 2
  OFFSET 1
     FOR UPDATE
      OF AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME</pre></td>
		<td width="50%" class="right"><pre class="prettyprint lang-java">
create.select(AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME, count())
      .from(AUTHOR)
      .join(BOOK).on(AUTHOR.ID.equal(BOOK.AUTHOR_ID))
      .where(BOOK.LANGUAGE.eq("DE"))
      .and(BOOK.PUBLISHED.gt(date("2008-01-01")))
      .groupBy(AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
      .having(count().gt(5))
      .orderBy(AUTHOR.LAST_NAME.asc().nullsFirst())
      .limit(2)
      .offset(1)
      .forUpdate()
      .of(AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)</pre></td>
	</tr>
</table>

<h2 id="jOOQ-typesafety"><a href="#jOOQ-typesafety" name="jOOQ-typesafety">#</a> What does type safety mean to jOOQ?</h2>
    <p>SQL is a very type safe language. So is jOOQ. jOOQ uniquely respects SQL's row value expression typesafety. jOOQ will use your Java compiler to type-check the following:</p>

<pre class="prettyprint lang-java">
select().from(t).where(t.a.eq(select(t2.x).from(t2));
// Type-check here: ---------------> ^^^^
select().from(t).where(t.a.eq(any(select(t2.x).from(t2)));
// Type-check here: -------------------> ^^^^
select().from(t).where(t.a.in(select(t2.x).from(t2));
// Type-check here: ---------------> ^^^^
</pre>

    <p>And also set operations:</p>

<pre class="prettyprint lang-java">
select(t1.a, t1.b).from(t1).union(select(t2.a, t2.b).from(t2));
// Type-check here: -------------------> ^^^^^^^^^^
</pre>

    <p>And even row value expressions:</p>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td width="50%" class="left"><pre class="prettyprint lang-sql">
SELECT * FROM t WHERE (t.a, t.b) = (1, 2)

SELECT * FROM t WHERE (t.a, t.b) OVERLAPS (date1, date2)

SELECT * FROM t WHERE (t.a, t.b) IN (SELECT x, y FROM t2)

UPDATE t SET (a, b) = (SELECT x, y FROM t2 WHERE ...)

INSERT INTO t (a, b) VALUES (1, 2)
&nbsp;
</pre></td>
		<td width="50%" class="right"><pre class="prettyprint lang-java">
select().from(t).where(row(t.a, t.b).eq(1, 2));
// Type-check here: ----------------->  ^^^^
select().from(t).where(row(t.a, t.b).overlaps(date1, date2));
// Type-check here: ------------------------> ^^^^^^^^^^^^
select().from(t).where(row(t.a, t.b).in(select(t2.x, t2.y).from(t2)));
// Type-check here: -------------------------> ^^^^^^^^^^
update(t).set(row(t.a, t.b), select(t2.x, t2.y).where(...));
// Type-check here: --------------> ^^^^^^^^^^
insertInto(t, t.a, t.b).values(1, 2);
// Type-check here: ---------> ^^^^
</pre></td>
	</tr>
</table>

<h2 id="What-is-jOOQ"><a href="#What-is-jOOQ" name="What-is-jOOQ">#</a> What is jOOQ?</h2>
    <p>jOOQ stands for Java Object Oriented Querying. It combines these essential features:</p>

    <ul>
    <li>Code Generation: jOOQ generates a simple Java representation of your database schema. Every table, view, stored procedure, enum, UDT is a class.</li>
	<li>Active records: jOOQ implements an easy-to-use active record pattern. It is NOT an OR-mapper, but provides a 1:1 mapping between tables/views and classes. Between columns and members.</li>
    <li>Typesafe SQL: jOOQ allows for writing compile-time typesafe querying using its built-in fluent API.</li>
    <li>SQL standard: jOOQ supports all standard SQL language features including the more complex UNION's, nested SELECTs, joins, aliasing</li>
    <li>Vendor-specific feature support: jOOQ encourages the use of vendor-specific extensions such as stored procedures, UDT's and ARRAY's, recursive queries, and many more.</li>
    </ul>

<h2 id="How-does-jOOQ-help-you"><a href="#How-does-jOOQ-help-you" name="How-does-jOOQ-help-you">#</a> How does jOOQ help you?</h2>
    <ul>
    <li>Your database always comes FIRST! That's where the schema is, not in your Java code or some XML mapping file.</li>
    <li>Your schema is generated in Java. You can use auto-completion in your IDE!</li>
    <li>Your "value objects" or "data transfer objects" are generated too. This keeps things DRY</li>
    <li>Your Java code won't compile anymore when you modify your schema. That means less runtime errors.</li>
    <li>You and your DBA can be friends again because you have full control over your SQL.</li>
    <li>You can port your SQL to a new database. jOOQ will generate SQL that works on any database.</li>
    <li>You won't have syntax errors in your query.</li>
    <li>You won't forget to bind variables correctly. No SQL injection, either.</li>
    <li>You can forget about JDBC's verbosity (especially useful when dealing with UDTs, ARRAYs and stored procedures). </li>
    </ul>
    <h3>Or in short:</h3>
    <ul>
    <li>You can be productive again!</li>
    </ul>

<h2 id="When-to-use-jOOQ"><a href="#When-to-use-jOOQ" name="When-to-use-jOOQ">#</a> When to use jOOQ</h2>
    <ul>
    <li>When you love your RDBMS of choice, including all its vendor-specific features.</li>
    <li>When you love control over your code.</li>
    <li>When you love the relational data model.</li>
    <li>When you love SQL.</li>
    <li>When you love stored procedures.</li>
    <li>When you love both <a href="http://en.wikipedia.org/wiki/OLAP" title="Online Analytical Processing, advanced SELECT statements, i.e. what none of the ORMs do, but jOOQ does best">OLAP</a> and <a href="http://en.wikipedia.org/wiki/OLTP" title="Online Transaction Processing, i.e. basic CRUD operations, what all ORM's do">OLTP</a></li>
    </ul>

<h2 id="When-not-to-use-jOOQ"><a href="#When-not-to-use-jOOQ" name="When-not-to-use-jOOQ">#</a> When not to use jOOQ</h2>
    <p>On the other hand, many people like the ease of use of Hibernate or other products, when it comes to simply persisting any domain model in any database. You should not use jOOQ...</p>
    <ul>
    <li>When you want to map your object-oriented domain model to a database and not vice versa.</li>
    <li>When your schema changes more frequently than you can re-deploy jOOQ-generated source code.</li>
    <li>When you need to write DDL statements. jOOQ only supports DML statements.</li>
    <li>When you don't really need SQL, only "persistence".</li>
    </ul>

<h2 id="What-databases-are-supported"><a href="#What-databases-are-supported" name="What-databases-are-supported">#</a> What databases are supported</h2>
    <p>Every RDMBS out there has its own little specialties.
    jOOQ considers those specialties as much as possible, while trying to
    standardise the behaviour in jOOQ. In order to increase the quality of jOOQ,
    some 70 unit tests are run for syntax and variable binding verification,
    as well as some 180 integration tests with an overall of around 1200 queries for any
    of these databases:</p>
<ul>
    <li>CUBRID 8.4.1 and 9.0.0</li>
    <li>DB2 9.7</li>
    <li>Derby 10.8</li>
    <li>Firebird 2.5.1</li>
    <li>H2 1.3.161</li>
    <li>HSQLDB 2.2.5</li>
    <li>Ingres 10.1.0</li>
    <li>MySQL 5.1.41 and 5.5.8</li>
    <li>Oracle XE 10.2.0.1.0 and 11g</li>
    <li>PostgreSQL 9.0</li>
    <li>SQLite (using <a href="https://bitbucket.org/xerial/sqlite-jdbc">xerial sqlite-jdbc</a>)</li>
    <li>SQL Server 2008 R8 and 2012</li>
    <li>Sybase Adaptive Server Enterprise 15.5</li>
    <li>Sybase SQL Anywhere 12</li>
</ul>
	<p>These platforms have been observed to work as well, but are not integration-tested</p>
<ul>
	<li>Google Cloud SQL (MySQL)</li>
</ul>

<h3>Planned (Contributions and suggestions are very welcome!)</h3>
    <ul>
    <li>Access</li>
    <li>EXASOL</li>
    <li>Informix</li>
    <li>Interbase</li>
    <li>Mondrian</li>
    <li>Netezza</li>
    <li>SQL Azure</li>
    <li>SQLFire</li>
    <li>Sybase SQL Anywhere OnDemand</li>
    <li>Teradata</li>
    <li>Vectorwise</li>
    <li>Vertica</li>
    <li>VoltDB</li>
    </ul>

<h2 id="Other-requirements"><a href="#Other-requirements" name="Other-requirements">#</a> Other requirements</h2>
<p>jOOQ runs with Java 1.6+</p>

<h3>License</h3>
<p>jOOQ is licensed under the <a href="http://www.apache.org/licenses/LICENSE-2.0" title="Apache Software License 2.0">Apache Software License 2.0</a></p>


<h3>Thanks</h3>
<p>YourKit is kindly supporting open source projects with its
full-featured Java Profiler. YourKit, LLC is the creator of innovative
and intelligent tools for profiling Java and .NET applications. Take a
look at YourKit's leading software products: <a
	href="http://www.yourkit.com/java/profiler/index.jsp">YourKit Java
Profiler</a> and <a
	href="http://www.yourkit.com/.net/profiler/index.jsp">YourKit .NET
Profiler.</a></p>
<?php
}
?>