
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Arithmetic operations and concatenation";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Your database can do the math for you. Most arithmetic operations are
							supported, but also string concatenation can be very efficient if done
							already in the database.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/ARITHMETIC/">Arithmetic operations and concatenation</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Stored procedures and functions" href="<?=$root?>/manual/DSL/PROCEDURES/">previous</a> : <a title="Next section: The CASE clause" href="<?=$root?>/manual/DSL/CASE/">next</a></td>
</tr>
</table>
							<h2>Mathematical operations</h2>
							<p>Arithmetic operations are implemented just like
								<a href="<?=$root?>/manual/DSL/FUNCTIONS/" title="jOOQ Manual reference: Functions and aggregate operators">functions</a>, with
								similar limitations as far as type restrictions are concerned. You can
								use any of these operators: </p>

							<pre class="prettyprint">  +  -  *  /  %</pre>

							<p>In order to express a SQL query like this one: </p>
							<pre class="prettyprint lang-sql">SELECT ((1 + 2) * (5 - 3) / 2) % 10 FROM DUAL</pre>
							<p>You can write something like this in jOOQ: </p>
							<pre class="prettyprint lang-java">create.select(create.val(1).add(2).mul(create.val(5).sub(3)).div(2).mod(10));	</pre>

							<h2>Datetime arithmetic</h2>
							<p>jOOQ also supports the Oracle-style syntax for adding days to a Field&lt;? extends java.util.Date&gt; </p>
							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">SELECT SYSDATE + 3 FROM DUAL;</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">create.select(create.currentTimestamp().add(3));</pre>
</td>
</tr>
</table>

							<h2>String concatenation</h2>
							<p>This is not really an arithmetic expression, but it's still an
								expression with operators: The string concatenation. jOOQ
								provides you with the Field's concat() method:</p>
							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">SELECT 'A' || 'B' || 'C' FROM DUAL

-- Or in MySQL:
SELECT concat('A', 'B', 'C')</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">&nbsp;
// For all RDBMS, including MySQL:
create.select(concat("A", "B", "C"));

</pre>
</td>
</tr>
</table>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/ARITHMETIC/">Arithmetic operations and concatenation</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Stored procedures and functions" href="<?=$root?>/manual/DSL/PROCEDURES/">previous</a> : <a title="Next section: The CASE clause" href="<?=$root?>/manual/DSL/CASE/">next</a></td>
</tr>
</table>
<?php 
}
?>

