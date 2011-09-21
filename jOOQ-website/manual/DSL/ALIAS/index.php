
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function printH1() {
    print "Aliased tables and fields";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Aliasing is at the core of SQL and relational algebra. When you join
							the same entity multiple times, you can rename it to distinguish the
							various meanings of the same entity
						";
}
function printContent() {
    global $root;
?>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/ALIAS/">Aliased tables and fields</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/CONDITION/" title="Previous section: Conditions">previous</a> : <a href="<?=$root?>/manual/DSL/IN/" title="Next section: Nested SELECT using the IN operator">next</a></td>
</tr>
</table>
							<h2>Aliasing Tables</h2>
							<p>A typical example of what you might want to do in SQL is this: </p>
							<pre class="prettyprint lang-sql">
SELECT a.ID, b.ID
  FROM T_AUTHOR a
  JOIN T_BOOK b on a.ID = b.AUTHOR_ID</pre>
  
  							<p>In this example, we are aliasing Tables, calling them a and b. Here is how you can create Table aliases in jOOQ: </p>
  							<pre class="prettyprint lang-java">
Table&lt;TBookRecord&gt; book = T_BOOK.as("b");
Table&lt;TAuthorRecord&gt; author = T_AUTHOR.as("a");</pre>

							<p>Now, if you want to reference any fields from those Tables, you may
								not use the original T_BOOK or T_AUTHOR meta-model objects anymore.
								Instead, you have to get the fields from the new book and author Table
								aliases: </p>
								
							<pre class="prettyprint lang-java">
Field&lt;Integer&gt; bookID = book.getField(TBook.ID);
Field&lt;Integer&gt; authorID = author.getField(TAuthor.ID);</pre>

							<p>
								Unfortunately, this tends to be a bit verbose. The static meta-model
								is not very suitable for accessing fields dynamically from Table
								aliases. This will be resolved in a future version as of ticket
								<a href="https://sourceforge.net/apps/trac/jooq/ticket/117" title="Trac ticket: #117">#117</a>.
								For now, this is how the above SQL statement would read in jOOQ:
							</p>
							<pre class="prettyprint lang-java">
create.select(authorID, bookID)
      .from(author)
      .join(book).on(authorID.equal(book.getField(TBook.AUTHOR_ID)));</pre>
      
      
      						<h3>Aliasing nested selects as tables</h3>
							<p>There is an interesting, more advanced example of how you can select
								from an aliased nested select in the manual's section about 
								<a href="<?=$root?>/manual/DSL/NESTED/" title="jOOQ Manual reference: Other types of nested SELECT">nested selects</a>
</p>
								
								
							<h2>Aliasing fields</h2>
							<p>Fields can also be aliased independently from Tables. Most often,
								this is done when using functions or aggregate operators. Here is an
								example: </p>
							<pre class="prettyprint lang-sql">
  SELECT FIRST_NAME || ' ' || LAST_NAME author, COUNT(*) books
    FROM T_AUTHOR
    JOIN T_BOOK ON T_AUTHOR.ID = AUTHOR_ID
GROUP BY FIRST_NAME, LAST_NAME;</pre>
							<p>Here is how it's done with jOOQ: </p>
							<pre class="prettyprint lang-java">
Record record = create.select(
         TAuthor.FIRST_NAME.concat(" ", TAuthor.LAST_NAME).as("author"),
         create.count().as("books"))
      .from(T_AUTHOR)
      .join(T_BOOK).on(TAuthor.ID.equal(TBook.AUTHOR_ID))
      .groupBy(TAuthor.FIRST_NAME, TAuthor.LAST_NAME).fetchAny();</pre>
      						<p>When you alias Fields like above, you can access those Fields' values using the alias name: </p>
      						<pre class="prettyprint lang-java">
System.out.println("Author : " + record.getValue("author"));
System.out.println("Books  : " + record.getValue("books"));</pre>
						<br><table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/ALIAS/">Aliased tables and fields</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/CONDITION/" title="Previous section: Conditions">previous</a> : <a href="<?=$root?>/manual/DSL/IN/" title="Next section: Nested SELECT using the IN operator">next</a></td>
</tr>
</table>
<?php 
}
?>

