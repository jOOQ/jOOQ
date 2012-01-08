
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Stored procedures and functions";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							The full power of your database's vendor-specific extensions can hardly
							be obtained outside of the
							database itself. Most modern RDBMS support
							their own procedural language. With jOOQ, stored procedures are
							integrated easily
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/PROCEDURES/">Stored procedures and functions</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Functions and aggregate operators" href="<?=$root?>/manual/DSL/FUNCTIONS/">previous</a> : <a title="Next section: Arithmetic operations and concatenation" href="<?=$root?>/manual/DSL/ARITHMETIC/">next</a></td>
</tr>
</table>
							<h2>Interaction with stored procedures</h2>
							<p>The main way to interact with your RDBMS's stored procedures and
								functions is by using the generated artefacts. See the manual's
								section about
								<a href="<?=$root?>/manual/META/PROCEDURE/" title="jOOQ Manual reference: Procedures and packages">generating procedures and packages</a>
							    for more details
								about the source code generation for stored procedures and functions.
							</p>

							<h2>Stored functions</h2>
							<p>When it comes to DSL, stored functions can be very handy in SQL
								statements as well. Every stored function (this also applies to
								FUNCTIONS in Oracle PACKAGES) can generate a Field representing a call
								to that function. Typically, if you have this type of function in your
								database: </p>

<pre class="prettyprint lang-sql">CREATE OR REPLACE FUNCTION f_author_exists (author_name VARCHAR2)
RETURN NUMBER;</pre>

							<p>Then convenience methods like these are generated: </p>
<pre class="prettyprint lang-java">// Create a field representing a function with another field as parameter
public static Field&lt;BigDecimal&gt; fAuthorExists(Field&lt;String&gt; authorName) { // [...]

// Create a field representing a function with a constant parameter
public static Field&lt;BigDecimal&gt; fAuthorExists(String authorName) { // [...]</pre>

							<p>Let's say, you have a T_PERSON table with persons' names in it, and
								you want to know whether there exists an author with precisely that
								name, you can reuse the above stored function in a SQL query: </p>

							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">SELECT T_PERSON.NAME, F_AUTHOR_EXISTS(T_PERSON.NAME)
  FROM T_PERSON

-- OR:

SELECT T_PERSON.NAME
  FROM T_PERSON
 WHERE F_AUTHOR_EXISTS(T_PERSON.NAME) = 1</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">create.select(T_PERSON.NAME, Functions.fAuthorExists(T_PERSON.NAME))
      .from(T_PERSON);

// OR: Note, the static import of Functions.*
create.select(T_PERSON.NAME)
      .from(T_PERSON)
      .where(fAuthorExists(T_PERSON.NAME));</pre>
</td>
</tr>
</table>

							<h2>Stored procedures</h2>
							<p>The notion of a stored procedure is implemented in most RDBMS by the
								fact, that the procedure has no RETURN VALUE (like void in Java), but
								it may well have OUT parameters. Since there is not a standard way how
								to embed stored procedures in SQL, they cannot be integrated in jOOQ's
								DSL either. </p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/PROCEDURES/">Stored procedures and functions</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Functions and aggregate operators" href="<?=$root?>/manual/DSL/FUNCTIONS/">previous</a> : <a title="Next section: Arithmetic operations and concatenation" href="<?=$root?>/manual/DSL/ARITHMETIC/">next</a></td>
</tr>
</table>
<?php 
}
?>

