
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function printH1() {
    print "When it's just easier: Plain SQL";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							jOOQ cannot foresee all possible vendor-specific SQL features for your
							database. And sometimes, even jOOQ code becomes too verbose. Then, you
							shouldn't hesitate to provide jOOQ with plain SQL, as you'd do with
							JDBC
						";
}
function printContent() {
    global $root;
?>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/SQL/">When it's just easier: Plain SQL</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/CAST/" title="Previous section: Type casting">previous</a> : <a href="<?=$root?>/manual/ADVANCED/" title="Next section: Advanced topics">next</a></td>
</tr>
</table>
							<h2>Plain SQL in jOOQ</h2>
							<p>A DSL is a nice thing to have, it feels "fluent" and "natural",
								especially if it models a well-known language, such as SQL. But a DSL
								is always expressed in another language (Java in this case), which was
								not made for exactly that DSL. If it were, then jOOQ would be
								implemented on a compiler-level, similar to Linq in .NET. But it's
								not, and so, the DSL is limited. We have seen many functionalities
								where the DSL becomes verbose. This can be especially true for: </p>
							<ul>
								
<li>
<a href="<?=$root?>/manual/DSL/ALIAS/" title="jOOQ Manual reference: Aliased tables and fields">aliasing</a>
</li>
								
<li>
<a href="<?=$root?>/manual/DSL/NESTED/" title="jOOQ Manual reference: Other types of nested SELECT">nested selects</a>
</li>
								
<li>
<a href="<?=$root?>/manual/DSL/ARITHMETIC/" title="jOOQ Manual reference: Arithmetic operations and concatenation">arithmetic expressions</a>
</li>
								
<li>
<a href="<?=$root?>/manual/DSL/CAST/" title="jOOQ Manual reference: Type casting">casting</a>
</li>
							
</ul>
							<p>You'll probably find other examples. If verbosity scares you off,
								don't worry. The verbose use-cases for jOOQ are rather rare, and when
								they come up, you do have an option. Just write SQL the way you're
								used to! </p>
							<p>jOOQ allows you to embed SQL as a String in these contexts: </p>
							<ul>
								
<li>Plain SQL as a condition </li>
								
<li>Plain SQL as a field </li>
								
<li>Plain SQL as a function </li>
								
<li>Plain SQL as a table </li>
								
<li>Plain SQL as a query </li>
							
</ul>

							<p>To construct artefacts wrapping plain SQL, you should use any of
								these methods from the Factory class: </p>
								
							<pre class="prettyprint lang-java">
// A condition
Condition condition(String sql);
Condition condition(String sql, Object... bindings);

// A field with an unknown data type
Field&lt;Object&gt; field(String sql);
Field&lt;Object&gt; field(String sql, Object... bindings);

// A field with a known data type
&lt;T&gt; Field&lt;T&gt; field(String sql, Class&lt;T&gt; type);
&lt;T&gt; Field&lt;T&gt; field(String sql, Class&lt;T&gt; type, Object... bindings);
&lt;T&gt; Field&lt;T&gt; field(String sql, DataType&lt;T&gt; type);
&lt;T&gt; Field&lt;T&gt; field(String sql, DataType&lt;T&gt; type, Object... bindings);

// A function
&lt;T&gt; Field&lt;T&gt; function(String name, Class&lt;T&gt; type, Field&lt;?&gt;... arguments);
&lt;T&gt; Field&lt;T&gt; function(String name, DataType&lt;T&gt; type, Field&lt;?&gt;... arguments);

// A table
Table&lt;?&gt; table(String sql);
Table&lt;?&gt; table(String sql, Object... bindings);

// A query without results (update, insert, etc)
Query query(String sql);
Query query(String sql, Object... bindings);

// A query with results
Result&lt;Record&gt; fetch(String sql);
Result&lt;Record&gt; fetch(String sql, Object... bindings);</pre>

							<p>Apart from the general factory methods, plain SQL is useful also in
								various other contexts. For instance, when adding a .where("a = b")
								clause to a query. Hence, there exist several convenience methods
								where plain SQL can be inserted usefully. This is an example
								displaying all various use-cases in one single query: </p>
							<pre class="prettyprint lang-java">
// You can use your table aliases in plain SQL fields
// As long as that will produce syntactically correct SQL
Field&lt;?&gt; LAST_NAME    = create.field("a.LAST_NAME");

// You can alias your plain SQL fields
Field&lt;?&gt; COUNT1       = create.field("count(*) x");

// If you know a reasonable Java type for your field, you
// can also provide jOOQ with that type
Field&lt;Integer&gt; COUNT2 = create.field("count(*) y", Integer.class);

       // Use plain SQL as select fields
create.select(LAST_NAME, COUNT1, COUNT2)

       // Use plain SQL as aliased tables (be aware of syntax!)
      .from("t_author a")
      .join("t_book b")

       // Use plain SQL for conditions both in JOIN and WHERE clauses
      .on("a.id = b.author_id")

       // Bind a variable in plain SQL
      .where("b.title != ?", "Brida")

       // Use plain SQL again as fields in GROUP BY and ORDER BY clauses
      .groupBy(LAST_NAME)
      .orderBy(LAST_NAME);</pre>

							<p>There are some important things to keep in mind when using plain
								SQL: </p>
							<ul>
								
<li>jOOQ doesn't know what you're doing. You're on your own again!
								</li>
								
<li>You have to provide something that will be syntactically correct.
									If it's not, then jOOQ won't know. Only your JDBC driver or your
									RDBMS will detect the syntax error. </li>
								
<li>You have to provide consistency when you use variable binding. The
									number of ? must match the number of variables </li>
								
<li>Your SQL is inserted into jOOQ queries without further checks.
									Hence, jOOQ can't prevent SQL injection. </li>
							
</ul>
						<br><table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/SQL/">When it's just easier: Plain SQL</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/CAST/" title="Previous section: Type casting">previous</a> : <a href="<?=$root?>/manual/ADVANCED/" title="Next section: Advanced topics">next</a></td>
</tr>
</table>
<?php 
}
?>

