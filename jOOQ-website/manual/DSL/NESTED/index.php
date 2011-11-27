
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Other types of nested SELECT";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "Apart from the most common IN and EXISTS clauses that encourage
							the use of nested selects, SQL knows a few more syntaxes to make use
							of such constructs. ";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/NESTED/">Other types of nested SELECT</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Nested SELECT using the EXISTS operator" href="<?=$root?>/manual/DSL/EXISTS/">previous</a> : <a title="Next section: UNION and other set operations" href="<?=$root?>/manual/DSL/UNION/">next</a></td>
</tr>
</table>
							<h2>Comparison with single-field SELECT clause</h2>
							<p>If you can ensure that a nested SELECT will only return one Record
								with one Field, then you can test for equality. This is how it is done
								in SQL: </p>
								
							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">SELECT * 
  FROM T_BOOK
 WHERE T_BOOK.AUTHOR_ID = (
 		SELECT ID 
          FROM T_AUTHOR 
         WHERE LAST_NAME = 'Orwell')</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">create.select()
      .from(T_BOOK)
      .where(T_BOOK.AUTHOR_ID.equal(create
             .select(T_AUTHOR.ID)
             .from(T_AUTHOR)
             .where(T_AUTHOR.LAST_NAME.equal("Orwell"))));</pre>
</td>
</tr>
</table>
                            
                            <p>More examples like the above can be guessed from the 
                            <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Field.java" title="Internal API reference: org.jooq.Field">org.jooq.Field</a> API, as documented in the manual's section about 
                            <a href="<?=$root?>/manual/DSL/CONDITION/" title="jOOQ Manual reference: Conditions">Conditions</a>. For the = operator, the available comparisons are these:</p>
                            
<pre class="prettyprint lang-java">Condition equal(Select&lt;?&gt; query);
Condition equalAny(Select&lt;?&gt; query);
Condition equalSome(Select&lt;?&gt; query);
Condition equalAll(Select&lt;?&gt; query);</pre>
                            
                            
                            <h2>Selecting from a SELECT - SELECT acts as a Table</h2>
							<p>Often, you need to nest a SELECT statement simply because SQL is
								limited in power. For instance, if you want to find out which author
								has written the most books, then you cannot do this: </p>
								
<pre class="prettyprint lang-sql">  SELECT AUTHOR_ID, count(*) books
    FROM T_BOOK
GROUP BY AUTHOR_ID
ORDER BY books DESC</pre>

							<p>Instead, you have to do this (or something similar). For jOOQ, this
								is an excellent example, combining various SQL features into a single
								statement. Here's how to do it: </p>
								
							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">SELECT nested.* FROM (
      SELECT AUTHOR_ID, count(*) books
        FROM T_BOOK
    GROUP BY AUTHOR_ID
) nested
ORDER BY nested.books DESC


</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">Table&lt;Record&gt; nested = 
    create.select(T_BOOK.AUTHOR_ID, count().as("books"))
          .from(T_BOOK)
          .groupBy(T_BOOK.AUTHOR_ID).asTable("nested");

create.select(nested.getFields())
      .from(nested)
      .orderBy(nested.getField("books"));</pre>
</td>
</tr>
</table>
							
							<p>You'll notice how some verbosity seems inevitable when you combine nested SELECT statements with aliasing. </p>
                    	
	                    	<h2>Selecting a SELECT - SELECT acts as a Field</h2>
							<p>Now SQL is even more powerful than that. You can also have SELECT
								statements, wherever you can have Fields. It get's harder and harder
								to find good examples, because there is always an easier way to
								express the same thing. But why not just count the number of books the
								really hard way? :-) But then again, maybe you want to take advantage
								of <a href="http://lukaseder.wordpress.com/2011/09/02/oracle-scalar-subquery-caching/" title="Oracle Scalar Subquery Caching with jOOQ">Oracle Scalar Subquery Caching</a>
</p>
							
							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">  SELECT LAST_NAME, (
      SELECT COUNT(*) 
       FROM T_BOOK 
      WHERE T_BOOK.AUTHOR_ID = T_AUTHOR.ID) books
    FROM T_AUTHOR
ORDER BY books DESC



</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">// The type of books cannot be inferred from the Select&lt;?&gt;
Field&lt;Object&gt; books = 
    create.selectCount()
          .from(T_BOOK)
          .where(T_BOOK.AUTHOR_ID.equal(T_AUTHOR.ID))
          .asField("books");
create.select(T_AUTHOR.ID, books)
      .from(T_AUTHOR)
      .orderBy(books, T_AUTHOR.ID));</pre>
</td>
</tr>
</table>
                    	<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/NESTED/">Other types of nested SELECT</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Nested SELECT using the EXISTS operator" href="<?=$root?>/manual/DSL/EXISTS/">previous</a> : <a title="Next section: UNION and other set operations" href="<?=$root?>/manual/DSL/UNION/">next</a></td>
</tr>
</table>
<?php 
}
?>

