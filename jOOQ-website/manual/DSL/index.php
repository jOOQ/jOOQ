
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../frame.php';
function getH1() {
    return "DSL or fluent API. Where SQL meets Java";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
					In these sections you will learn about how jOOQ makes SQL available to
					Java as if Java natively supported SQL
				";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Sequences" href="<?=$root?>/manual/META/SEQUENCE/">previous</a> : <a title="Next section: Complete SELECT syntax" href="<?=$root?>/manual/DSL/SELECT/">next</a></td>
</tr>
</table>
					<h2>Overview</h2>
					<p>jOOQ ships with its own DSL (or
						<a href="http://en.wikipedia.org/wiki/Domain-specific_language" title="Domain Specific Language">Domain Specific Language</a>) that
						simulates SQL as good as possible in Java. This means, that you can
						write SQL statements almost as if Java natively supported that syntax
						just like .NET's C# does with <a href="http://msdn.microsoft.com/en-us/library/bb425822.aspx">LINQ to SQL.</a>
</p>

					<p>Here is an example to show you what that means. When you want to write a query like this in SQL: </p>
					<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">-- Select all books by authors born after 1920,
-- named "Paulo" from a catalogue:
SELECT *
  FROM t_author a
  JOIN t_book b ON a.id = b.author_id
 WHERE a.year_of_birth &gt; 1920
   AND a.first_name = 'Paulo'
 ORDER BY b.title</pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">Result&lt;Record&gt; result =
create.select()
      .from(T_AUTHOR.as("a"))
      .join(T_BOOK.as("b")).on(a.ID.equal(b.AUTHOR_ID))
      .where(a.YEAR_OF_BIRTH.greaterThan(1920)
      .and(a.FIRST_NAME.equal("Paulo")))
      .orderBy(b.TITLE)
      .fetch();</pre>
</td>
</tr>
</table>

					<p>
						You couldn't come much closer to SQL itself in Java, without re-writing the compiler.
						We'll see how the aliasing works later in the section about
						<a href="<?=$root?>/manual/DSL/ALIAS/" title="jOOQ Manual reference: Aliased tables and fields">aliasing</a>
					
</p>
				<h3>Table of contents</h3><ol>
<li>
<a title="Complete SELECT syntax" href="<?=$root?>/manual/DSL/SELECT/">Complete SELECT syntax</a>
</li>
<li>
<a title="Table sources" href="<?=$root?>/manual/DSL/TABLESOURCE/">Table sources</a>
</li>
<li>
<a title="Conditions" href="<?=$root?>/manual/DSL/CONDITION/">Conditions</a>
</li>
<li>
<a title="Aliased tables and fields" href="<?=$root?>/manual/DSL/ALIAS/">Aliased tables and fields</a>
</li>
<li>
<a title="Nested SELECT using the IN operator" href="<?=$root?>/manual/DSL/IN/">Nested SELECT using the IN operator</a>
</li>
<li>
<a title="Nested SELECT using the EXISTS operator" href="<?=$root?>/manual/DSL/EXISTS/">Nested SELECT using the EXISTS operator</a>
</li>
<li>
<a title="Other types of nested SELECT" href="<?=$root?>/manual/DSL/NESTED/">Other types of nested SELECT</a>
</li>
<li>
<a title="UNION and other set operations" href="<?=$root?>/manual/DSL/UNION/">UNION and other set operations</a>
</li>
<li>
<a title="Functions and aggregate operators" href="<?=$root?>/manual/DSL/FUNCTIONS/">Functions and aggregate operators</a>
</li>
<li>
<a title="Stored procedures and functions" href="<?=$root?>/manual/DSL/PROCEDURES/">Stored procedures and functions</a>
</li>
<li>
<a title="Arithmetic operations and concatenation" href="<?=$root?>/manual/DSL/ARITHMETIC/">Arithmetic operations and concatenation</a>
</li>
<li>
<a title="The CASE clause" href="<?=$root?>/manual/DSL/CASE/">The CASE clause</a>
</li>
<li>
<a title="Type casting" href="<?=$root?>/manual/DSL/CAST/">Type casting</a>
</li>
<li>
<a title="When it's just easier: Plain SQL" href="<?=$root?>/manual/DSL/SQL/">When it's just easier: Plain SQL</a>
</li>
</ol><br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Sequences" href="<?=$root?>/manual/META/SEQUENCE/">previous</a> : <a title="Next section: Complete SELECT syntax" href="<?=$root?>/manual/DSL/SELECT/">next</a></td>
</tr>
</table>
<?php 
}
?>

