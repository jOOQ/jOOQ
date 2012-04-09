
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Functions and aggregate operators";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Highly effective SQL cannot do without functions. Operations on
							VARCHAR, DATE, and NUMERIC types in GROUP BY or ORDER BY clauses allow
							for very elegant queries.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/FUNCTIONS/">Functions and aggregate operators</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: UNION and other set operations" href="<?=$root?>/manual/DSL/UNION/">previous</a> : <a title="Next section: Stored procedures and functions" href="<?=$root?>/manual/DSL/PROCEDURES/">next</a></td>
</tr>
</table>
							<h2>jOOQ's strategy for supporting vendor-specific functions</h2>
							<p>jOOQ allows you to access native functions from your RDBMS. jOOQ
								follows two strategies: </p>
							<ul>
								
<li>Implement all SQL-92, SQL:1999, SQL:2003, and SQL:2008 standard
									functions, aggregate operators, and window functions. Standard
									functions could be
									<a href="http://oreilly.com/catalog/sqlnut/chapter/ch04.html" title="O'Reilly listing of SQL-92 standard functions and deviations thereof">these functions as listed by O'Reilly</a>. </li>
								
<li>Take the most useful of vendor-specific functions and simulate
									them for other RDBMS, where they may not be supported. An example for
									this are
									<a href="http://psoug.org/reference/analytic_functions.html" title="An example listing of Oracle Analytic Functions">Oracle Analytic Functions</a>
</li>
							
</ul>

							<h2>Functions </h2>
							<p>These are just a few functions in the Factory, so you get the idea: </p>

<pre class="prettyprint lang-java">Field&lt;String&gt; rpad(Field&lt;String&gt; field, Field&lt;? extends Number&gt; length);
Field&lt;String&gt; rpad(Field&lt;String&gt; field, int length);
Field&lt;String&gt; rpad(Field&lt;String&gt; field, Field&lt;? extends Number&gt; length, Field&lt;String&gt; c);
Field&lt;String&gt; rpad(Field&lt;String&gt; field, int length, char c);
Field&lt;String&gt; lpad(Field&lt;String&gt; field, Field&lt;? extends Number&gt; length);
Field&lt;String&gt; lpad(Field&lt;String&gt; field, int length);
Field&lt;String&gt; lpad(Field&lt;String&gt; field, Field&lt;? extends Number&gt; length, Field&lt;String&gt; c);
Field&lt;String&gt; lpad(Field&lt;String&gt; field, int length, char c);
Field&lt;String&gt; replace(Field&lt;String&gt; field, Field&lt;String&gt; search);
Field&lt;String&gt; replace(Field&lt;String&gt; field, String search);
Field&lt;String&gt; replace(Field&lt;String&gt; field, Field&lt;String&gt; search, Field&lt;String&gt; replace);
Field&lt;String&gt; replace(Field&lt;String&gt; field, String search, String replace);
Field&lt;Integer&gt; position(Field&lt;String&gt; field, String search);
Field&lt;Integer&gt; position(Field&lt;String&gt; field, Field&lt;String&gt; search);</pre>

							<h2>Aggregate operators</h2>
							<p>Aggregate operators work just like functions, even if they have a
								slightly different semantics. Here are some examples from
								Factory: </p>

<pre class="prettyprint lang-java">// Every-day, SQL standard aggregate functions
AggregateFunction&lt;Integer&gt; count(Field&lt;?&gt; field);
AggregateFunction&lt;T&gt; max(Field&lt;T&gt; field);
AggregateFunction&lt;T&gt; min(Field&lt;T&gt; field);
AggregateFunction&lt;BigDecimal&gt; sum(Field&lt;? extends Number&gt; field);
AggregateFunction&lt;BigDecimal&gt; avg(Field&lt;? extends Number&gt; field);


// DISTINCT keyword in aggregate functions
AggregateFunction&lt;Integer&gt; countDistinct(Field&lt;?&gt; field);
AggregateFunction&lt;T&gt; maxDistinct(Field&lt;T&gt; field);
AggregateFunction&lt;T&gt; minDistinct(Field&lt;T&gt; field);
AggregateFunction&lt;BigDecimal&gt; sumDistinct(Field&lt;? extends Number&gt; field);
AggregateFunction&lt;BigDecimal&gt; avgDistinct(Field&lt;? extends Number&gt; field);

// String aggregate functions
AggregateFunction&lt;String&gt; groupConcat(Field&lt;?&gt; field);
AggregateFunction&lt;String&gt; groupConcatDistinct(Field&lt;?&gt; field);
OrderedAggregateFunction&lt;String&gt; listAgg(Field&lt;?&gt; field);
OrderedAggregateFunction&lt;String&gt; listAgg(Field&lt;?&gt; field, String separator);

// Statistical functions
AggregateFunction&lt;BigDecimal&gt; median(Field&lt;? extends Number&gt; field);
AggregateFunction&lt;BigDecimal&gt; stddevPop(Field&lt;? extends Number&gt; field);
AggregateFunction&lt;BigDecimal&gt; stddevSamp(Field&lt;? extends Number&gt; field);
AggregateFunction&lt;BigDecimal&gt; varPop(Field&lt;? extends Number&gt; field);
AggregateFunction&lt;BigDecimal&gt; varSamp(Field&lt;? extends Number&gt; field);
</pre>

							<p>A typical example of how to use an aggregate operator is when
								generating the next key on insertion of an ID. When you want to
								achieve something like this </p>

							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">SELECT MAX(ID) + 1 AS next_id
  FROM T_AUTHOR</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">create.select(max(ID).add(1).as("next_id"))
      .from(T_AUTHOR);</pre>
</td>
</tr>
</table>

							<p>See also the section about
								<a href="<?=$root?>/manual/DSL/ARITHMETIC/" title="jOOQ Manual reference: Arithmetic operations and concatenation">Arithmetic operations</a>
</p>

							<h2>Window functions</h2>
							<p>Most major RDBMS support the concept of window functions. jOOQ knows
								of implementations in DB2, Oracle, Postgres, SQL Server, and Sybase
								SQL Anywhere,
								and supports most of their specific syntaxes. Window functions can be
								used for things like calculating a "running total". The following example
								fetches transactions and the running total for every transaction going
								back to the beginning of the transaction table (ordered by booked_at).

								They are accessible from the previously seen AggregateFunction type using
								the over() method:
							</p>

							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">SELECT booked_at, amount,
   SUM(amount) OVER (PARTITION BY 1
                     ORDER BY booked_at
                     ROWS BETWEEN UNBOUNDED PRECEDING
                     AND CURRENT ROW) AS total
  FROM transactions</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">create.select(t.BOOKED_AT, t.AMOUNT,
         sum(t.AMOUNT).over().partitionByOne()
                      .orderBy(t.BOOKED_AT)
                      .rowsBetweenUnboundedPreceding()
                      .andCurrentRow().as("total")
      .from(TRANSACTIONS.as("t"));</pre>
</td>
</tr>
</table>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/FUNCTIONS/">Functions and aggregate operators</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: UNION and other set operations" href="<?=$root?>/manual/DSL/UNION/">previous</a> : <a title="Next section: Stored procedures and functions" href="<?=$root?>/manual/DSL/PROCEDURES/">next</a></td>
</tr>
</table>
<?php 
}
?>

