
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function printH1() {
    print "Functions and aggregate operators";
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
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/FUNCTIONS/">Functions and aggregate operators</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/UNION/" title="Previous section: UNION and other set operations">previous</a> : <a href="<?=$root?>/manual/DSL/PROCEDURES/" title="Next section: Stored procedures and functions">next</a></td>
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
							<p>These are just a few functions in the Field interface, so you get the idea: </p>
							
							<pre class="prettyprint lang-java">
Field&lt;String&gt; rpad(Field&lt;? extends Number&gt; length);
Field&lt;String&gt; rpad(int length);
Field&lt;String&gt; rpad(Field&lt;? extends Number&gt; length, Field&lt;String&gt; c);
Field&lt;String&gt; rpad(int length, char c);
Field&lt;String&gt; lpad(Field&lt;? extends Number&gt; length);
Field&lt;String&gt; lpad(int length);
Field&lt;String&gt; lpad(Field&lt;? extends Number&gt; length, Field&lt;String&gt; c);
Field&lt;String&gt; lpad(int length, char c);
Field&lt;String&gt; replace(Field&lt;String&gt; search);
Field&lt;String&gt; replace(String search);
Field&lt;String&gt; replace(Field&lt;String&gt; search, Field&lt;String&gt; replace);
Field&lt;String&gt; replace(String search, String replace);
Field&lt;Integer&gt; position(String search);
Field&lt;Integer&gt; position(Field&lt;String&gt; search);</pre>

							<h2>Aggregate operators</h2>
							<p>Aggregate operators work just like functions, even if they have a
								slightly different semantics. Some of them are also placed in the
								Field interface. Others in the Factory. Here are some examples from
								Field: </p>
								
							<pre class="prettyprint lang-java">
// Every-day functions
Field&lt;Integer&gt; count();
Field&lt;Integer&gt; countDistinct();
Field&lt;T&gt; max();
Field&lt;T&gt; min();
Field&lt;BigDecimal&gt; sum();
Field&lt;BigDecimal&gt; avg();

// Statistical functions
Field&lt;BigDecimal&gt; median();
Field&lt;BigDecimal&gt; stddevPop();
Field&lt;BigDecimal&gt; stddevSamp();
Field&lt;BigDecimal&gt; varPop();
Field&lt;BigDecimal&gt; varSamp();
</pre>

							<p>A typical example of how to use an aggregate operator is when
								generating the next key on insertion of an ID. When you want to
								achieve something like this </p>
								
							<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td width="50%" class="left">
<pre class="prettyprint lang-sql">
SELECT MAX(ID) + 1 AS next_id 
  FROM T_AUTHOR</pre>
</td><td width="50%" class="right">
<pre class="prettyprint lang-java">
create.select(ID.max().add(1).as("next_id"))
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
								back to the beginning of the transaction table (ordered by booked_at)
							</p>
							
							<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td width="50%" class="left">
<pre class="prettyprint lang-sql">
SELECT booked_at, amount,
   SUM(amount) OVER (PARTITION BY 1
                     ORDER BY booked_at
                     ROWS BETWEEN UNBOUNDED PRECEDING 
                     AND CURRENT ROW) AS total
  FROM transactions</pre>
</td><td width="50%" class="right">
<pre class="prettyprint lang-java">
create.select(BOOKED_AT, AMOUNT, 
              AMOUNT.sumOver().partitionByOne()
                    .orderBy(BOOKED_AT)
                    .rowsBetweenUnboundedPreceding()
                    .andCurrentRow().as("total")
      .from(TRANSACTIONS);</pre>
</td>
</tr>
</table>
						<br><table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/FUNCTIONS/">Functions and aggregate operators</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/UNION/" title="Previous section: UNION and other set operations">previous</a> : <a href="<?=$root?>/manual/DSL/PROCEDURES/" title="Next section: Stored procedures and functions">next</a></td>
</tr>
</table>
<?php 
}
?>

