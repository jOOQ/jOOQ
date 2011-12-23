<?php 
require 'frame.php';
function getH1() {
	return 'jOOQ : A peace treaty between SQL and Java';
}
function getSlogan() {
	return "SQL was never meant to be abstracted. To be confined in the narrow boundaries 
			of heavy mappers, hiding the beauty and simplicity of relational data.
			SQL was never meant to be object-oriented. SQL was never meant to be
			anything other than... SQL!";
}
function getActiveMenu() {
	return "home";
}
function printContent() {
?>
<h2>What does jOOQ code look like?</h2>
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
  SELECT FIRST_NAME, LAST_NAME, COUNT(*)
    FROM AUTHOR
    JOIN BOOK ON AUTHOR.ID = BOOK.AUTHOR_ID
   WHERE LANGUAGE = 'DE'
     AND PUBLISHED &gt; '2008-01-01'
GROUP BY FIRST_NAME, LAST_NAME
  HAVING COUNT(*) &gt; 5
ORDER BY LAST_NAME ASC NULLS FIRST
   LIMIT 2 
  OFFSET 1
     FOR UPDATE
      OF FIRST_NAME, LAST_NAME</pre></td>
		<td width="50%" class="right"><pre class="prettyprint lang-java">
create.select(FIRST_NAME, LAST_NAME, create.count())
      .from(AUTHOR)
      .join(BOOK).on(Author.ID.equal(Book.AUTHOR_ID))
      .where(LANGUAGE.equal("DE"))
      .and(PUBLISHED.greaterThan(parseDate('2008-01-01')))
      .groupBy(FIRST_NAME, LAST_NAME)
      .having(create.count().greaterThan(5))
      .orderBy(LAST_NAME.asc().nullsFirst())
      .limit(2)
      .offset(1)
      .forUpdate()
      .of(FIRST_NAME, LAST_NAME)</pre></td>
	</tr>
</table>
      
    <h2>What is jOOQ?</h2>
    <p>jOOQ stands for Java Object Oriented Querying. It combines these essential features:</p>
      
    <ul>
    <li>Code Generation: jOOQ generates a simple Java representation of your database schema. Every table, view, stored procedure, enum, UDT is a class.</li>
	<li>Active records: jOOQ implements an easy-to-use active record pattern. It is NOT an OR-mapper, but provides a 1:1 mapping between tables/views and classes. Between columns and members.</li>
    <li>Typesafe SQL: jOOQ allows for writing compile-time typesafe querying using its built-in fluent API.</li>
    <li>SQL standard: jOOQ supports all standard SQL language features including the more complex UNION's, nested SELECTs, joins, aliasing</li>
    <li>Vendor-specific feature support: jOOQ encourages the use of vendor-specific extensions such as stored procedures, UDT's and ARRAY's, recursive queries, and many more.</li>
    </ul>
      
    <h2>How does jOOQ help you?</h2>
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
    
    <h2>When to use jOOQ</h2>
    <ul>
    <li>When you love your RDBMS of choice, including all its vendor-specific features.</li>
    <li>When you love control over your code.</li>
    <li>When you love the relational data model (read this <a href="http://database-programmer.blogspot.com/2010/12/historical-perspective-of-orm-and.html" title="Ken Downs view on ORM's and how jOOQ fits into this world">interesting article</a>).</li>
    <li>When you love SQL.</li>
    <li>When you love stored procedures.</li>
    <li>When you love both <a href="http://en.wikipedia.org/wiki/OLAP" title="Online Analytical Processing, advanced SELECT statements, i.e. what none of the ORM's do, but jOOQ does best">OLAP</a> and <a href="http://en.wikipedia.org/wiki/OLTP" title="Online Transaction Processing, i.e. basic CRUD operations, what all ORM's do">OLTP</a></li>
    </ul>
    
    <h2>When not to use jOOQ</h2>
    <p>On the other hand, many people like the ease of use of Hibernate or other products, when it comes to simply persisting any domain model in any database. You should not use jOOQ...</p>
    <ul>
    <li>When you don't care about your database (or "persistence" as you would probably call it).</li>
    <li>When you don't really need SQL.</li>
    <li>When you want to map your object-oriented domain model to a database and not vice versa.</li>
    <li>When your schema changes more frequently than you can re-deploy jOOQ-generated source code.</li>
    <li>When you need to write DDL statements. jOOQ only supports DML statements.</li> 
    </ul>
    
    <h2>What databases are supported</h2>
    <p>Every RDMBS out there has its own little specialties. 
    jOOQ considers those specialties as much as possible, while trying to 
    standardise the behaviour in jOOQ. In order to increase the quality of jOOQ, 
    some 70 unit tests are run for syntax and variable binding verification, 
    as well as some 130 integration tests with an overall of around 900 queries for any 
    of these databases:</p>
<ul>
    <li>DB2 9.7</li>
    <li>Derby 10.8</li>
    <li>H2 1.3.161</li>
    <li>HSQLDB 2.2.5</li>
    <li>Ingres 10.1.0</li>
    <li>MySQL 5.1.41 and 5.5.8</li>
    <li>Oracle XE 10.2.0.1.0 and 11g</li>
    <li>PostgreSQL 9.0</li>
    <li>SQLite with inofficial JDBC driver v056</li>
    <li>SQL Server 2008 R8</li>
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
    <li>Firebird</li>
    <li>Informix</li>
    <li>Interbase</li>
    <li>SQL Azure</li>
    <li>Sybase SQL Anywhere OnDemand</li>
    <li>Teradata</li>
    </ul>
    
    <h2>Other requirements</h2>
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