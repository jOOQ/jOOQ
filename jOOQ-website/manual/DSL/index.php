
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../frame.php';
function printH1() {
    print "DSL or fluent API. Where SQL meets Java";
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
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/META/SEQUENCE/" title="Previous section: Sequences">previous</a> : <a href="<?=$root?>/manual/DSL/SELECT/" title="Next section: Complete SELECT syntax">next</a></td>
</tr>
</table>
					<h2>Overview</h2>
					<p>jOOQ ships with its own DSL (or Domain Specific Language) that
						simulates SQL as good as possible in Java. This means, that you can
						write SQL statements almost as if Java natively supported that syntax
						just like .NET's C# does with <a href="http://msdn.microsoft.com/en-us/library/bb425822.aspx">LINQ to SQL.</a>
</p>
						
					<p>Here is an example to show you what that means. When you want to write a query like this in SQL: </p>
					<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td width="50%" class="left">
<pre class="prettyprint lang-sql">
-- Select all books by authors born after 1920, 
-- named "Paulo" from a catalogue:
SELECT * 
  FROM t_author a 
  JOIN t_book b ON a.id = b.author_id 
 WHERE a.year_of_birth &gt; 1920 
   AND a.first_name = 'Paulo'
 ORDER BY b.title</pre>
</td><td width="50%" class="right">
<pre class="prettyprint lang-java">
Result&lt;Record&gt; result = 
create.select()
      .from(T_AUTHOR)
      .join(T_BOOK).on(TAuthor.ID.equal(TBook.AUTHOR_ID))
      .where(TAuthor.YEAR_OF_BIRTH.greaterThan(1920)
      .and(TAuthor.FIRST_NAME.equal("Paulo")))
      .orderBy(TBook.TITLE)
      .fetch();</pre>
</td>
</tr>
</table>
					
					<p>You couldn't come much closer to SQL itself in Java, without re-writing the compiler. </p>
				<h3>Table of contents</h3><ol>
<li>
<a href="<?=$root?>/manual/DSL/SELECT/" title="Complete SELECT syntax">Complete SELECT syntax</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/CONDITION/" title="Conditions">Conditions</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/ALIAS/" title="Aliased tables and fields">Aliased tables and fields</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/IN/" title="Nested SELECT using the IN operator">Nested SELECT using the IN operator</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/EXISTS/" title="Nested SELECT using the EXISTS operator">Nested SELECT using the EXISTS operator</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/NESTED/" title="Other types of nested SELECT">Other types of nested SELECT</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/UNION/" title="UNION and other set operations">UNION and other set operations</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/FUNCTIONS/" title="Functions and aggregate operators">Functions and aggregate operators</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/PROCEDURES/" title="Stored procedures and functions">Stored procedures and functions</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/ARITHMETIC/" title="Arithmetic operations and concatenation">Arithmetic operations and concatenation</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/CASE/" title="The CASE clause">The CASE clause</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/CAST/" title="Type casting">Type casting</a>
</li>
<li>
<a href="<?=$root?>/manual/DSL/SQL/" title="When it's just easier: Plain SQL">When it's just easier: Plain SQL</a>
</li>
</ol><br><table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/META/SEQUENCE/" title="Previous section: Sequences">previous</a> : <a href="<?=$root?>/manual/DSL/SELECT/" title="Next section: Complete SELECT syntax">next</a></td>
</tr>
</table>
<?php 
}
?>

