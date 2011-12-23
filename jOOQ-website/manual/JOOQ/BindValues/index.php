
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Bind values";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Variable binding has a great impact on how you design your SQL queries.
							It will influence your SQL queries' security aspect as well as execution speed.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/BindValues/">Bind values</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: ResultQuery and various ways of fetching data" href="<?=$root?>/manual/JOOQ/ResultQuery/">previous</a> : <a title="Next section: QueryParts and the global architecture" href="<?=$root?>/manual/JOOQ/QueryPart/">next</a></td>
</tr>
</table>
							<h2>Bind values</h2>
							<p>
								Bind values are used in SQL / JDBC for various reasons. Among the most
								obvious ones are:
							</p>
							<ul>
								
<li>
									Protection against SQL injection. Instead of inlining values
									possibly originating from user input, you bind those values to
									your prepared statement and let the JDBC driver / database take
									care of handling security aspects.
								</li>
								
<li>
									Increased speed. Advanced databases such as Oracle can keep
									execution plans of similar queries in a dedicated cache to prevent
									hard-parsing your query again and again. In many cases, the actual
									value of a bind variable does not influence the execution plan, hence
									it can be reused. Preparing a statement will thus be faster
								</li>
								
<li>
									On a JDBC level, you can also reuse the SQL string and prepared statement
									object instead of constructing it again, as you can bind new values to
									the prepared statement. This is currently not supported by jOOQ, though
								</li>
							
</ul>
							
							<h3>Ways to introduce bind values with jOOQ</h3>
							<p>
								Bind values are omni-present in jOOQ. Whenever you create a condition,
								you're actually also adding a bind value:
							</p>
<pre class="prettyprint lang-java">// In jOOQ, "Poe" will be the bind value bound to the condition
LAST_NAME.equal("Poe");</pre>

							
							<p>
								The above notation is actually convenient way to explicitly create
								a bind value for "Poe". You could also write this, instead:
							</p>
<pre class="prettyprint lang-java">// The Factory allows for explicitly creating bind values
LAST_NAME.equal(Factory.val("Poe"));

// Or, when static importing Factory.val:
LAST_NAME.equal(val("Poe"))</pre>							
							
							<p>
								Once created, bind values are part of the query's syntax tree (see
								<a href="<?=$root?>/manual/JOOQ/QueryPart/" title="jOOQ Manual reference: QueryParts and the global architecture">the manual's section about jOOQ's architecture</a>
							    for more information about jOOQ's internals),
								and cannot be modified directly anymore. If you wish to reuse a query and
								modify bind values between subsequent query executions, you can access them again
								through the <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Query.java" title="Internal API reference: org.jooq.Query">org.jooq.Query</a> interface:
							</p>
							
<pre class="prettyprint lang-java">// Access the first bind value from a query. Indexes are counted from 1, just as with JDBC
Query query = create.select().from(T_AUTHOR).where(LAST_NAME.equal("Poe"));
Param&lt;?&gt; param = query.getParam("1");

// You could now modify the Query's underlying bind value:
if ("Poe".equal(param.getValue())) {
    param.setConverted("Orwell");
}</pre>

							<p>
								The <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Param.java" title="Internal API reference: org.jooq.Param">org.jooq.Param</a> type can also be named explicitly
								using the Factory's param() methods:
							</p>
<pre class="prettyprint lang-java">// Create a query with a named parameter. You can then use that name for accessing the parameter again
Query query1 = create.select().from(T_AUTHOR).where(LAST_NAME.equal(param("lastName", "Poe")));
Param&lt;?&gt; param1 = query.getParam("lastName");

// Or, keep a reference to the typed parameter in order not to lose the &lt;T&gt; type information:
Param&lt;String&gt; param2 = param("lastName", "Poe");
Query query2 = create.select().from(T_AUTHOR).where(LAST_NAME.equal(param2));

// You can now change the bind value directly on the Param reference:
param2.setValue("Orwell");
</pre>

							<p>
								The <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Query.java" title="Internal API reference: org.jooq.Query">org.jooq.Query</a> interface also allows for
								setting new bind values directly, without accessing the Param type:
							</p>
							
<pre class="prettyprint lang-java">Query query1 = create.select().from(T_AUTHOR).where(LAST_NAME.equal("Poe"));
query1.bind(1, "Orwell");

// Or, with named parameters
Query query2 = create.select().from(T_AUTHOR).where(LAST_NAME.equal(param("lastName", "Poe")));
query2.bind("lastName", "Orwell");</pre>

							<p>
								NOTE: Should you wish to use jOOQ only as a query builder and execute
								queries with another tool, such as Spring Data instead, you can also
								use the Factory's renderNamedParams() method, to actually render named
								parameter names in generated SQL:
							</p>
							
							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">-- The named bind variable can be rendered

SELECT *
FROM T_AUTHOR
WHERE LAST_NAME = :lastName
</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">create.renderNamedParams(
    create.select()
          .from(T_AUTHOR)
          .where(LAST_NAME.equal(
                 param("lastName", "Poe"))));</pre>
</td>
</tr>
</table>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/BindValues/">Bind values</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: ResultQuery and various ways of fetching data" href="<?=$root?>/manual/JOOQ/ResultQuery/">previous</a> : <a title="Next section: QueryParts and the global architecture" href="<?=$root?>/manual/JOOQ/QueryPart/">next</a></td>
</tr>
</table>
<?php 
}
?>

