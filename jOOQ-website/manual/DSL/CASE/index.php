
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "The CASE clause";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							The SQL standard supports a CASE clause, which works very similar to
							Java's if-else statement. In complex SQL, this is very useful for value
							mapping
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/CASE/">The CASE clause</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Arithmetic operations and concatenation" href="<?=$root?>/manual/DSL/ARITHMETIC/">previous</a> : <a title="Next section: Type casting" href="<?=$root?>/manual/DSL/CAST/">next</a></td>
</tr>
</table>
							<h2>The two flavours of CASE</h2>
							<p>The CASE clause is part of the standard SQL syntax. While some RDBMS
								also offer an IF clause, or a DECODE function, you can always rely on
								the two types of CASE syntax: </p>
								
							<table cellspacing="0" cellpadding="0" width="100%">
<tr>
<td class="left" width="50%">
<pre class="prettyprint lang-sql">CASE WHEN T_AUTHOR.FIRST_NAME = 'Paulo'  THEN 'brazilian'
     WHEN T_AUTHOR.FIRST_NAME = 'George' THEN 'english'
                                         ELSE 'unknown'
END

-- OR:

CASE T_AUTHOR.FIRST_NAME WHEN 'Paulo'  THEN 'brazilian'
                         WHEN 'George' THEN 'english'
                                       ELSE 'unknown'
END </pre>
</td><td class="right" width="50%">
<pre class="prettyprint lang-java">create.decode()
      .when(T_AUTHOR.FIRST_NAME.equal("Paulo"), "brazilian")
      .when(T_AUTHOR.FIRST_NAME.equal("George"), "english")
      .otherwise("unknown");

// OR:

create.decode().value(T_AUTHOR.FIRST_NAME)
               .when("Paulo", "brazilian")
               .when("George", "english")
               .otherwise("unknown");</pre>
</td>
</tr>
</table>

							<p>
								In jOOQ, both syntaxes are supported (although, Derby only knows the
								first one, which is more general). Unfortunately, both case and else
								are reserved words in Java. jOOQ chose to use decode() from the Oracle
								DECODE function, and otherwise(), which means the same as else. Please
								note that in the above examples, all values were always constants. You
								can of course also use Field instead of the various constants. 
							</p>
							<p>A CASE clause can be used anywhere where you can place a Field. For
								instance, you can SELECT the above expression, if you're selecting
								from T_AUTHOR: </p>
<pre class="prettyprint lang-sql">SELECT T_AUTHOR.FIRST_NAME, [... CASE EXPR ...] AS nationality
  FROM T_AUTHOR</pre>
  
  
  							<h2>CASE clauses in an ORDER BY clause</h2>
							<p>Sort indirection is often implemented with a CASE clause of a
								SELECT's ORDER BY clause. In SQL, this reads: </p>
								
<pre class="prettyprint lang-sql">SELECT *
FROM T_AUTHOR
ORDER BY CASE FIRST_NAME WHEN 'Paulo'  THEN 1
                         WHEN 'George' THEN 2
                                       ELSE null
         END</pre>
         
							<p>This will order your authors such that all 'Paulo' come first, then
								all 'George' and everyone else last (depending on your RDBMS' handling
								of NULL values in sorting). This is a very common task, such that jOOQ
								simplifies its use: </p>
<pre class="prettyprint lang-java">create.select()
      .from(T_AUTHOR)
      .orderBy(T_AUTHOR.FIRST_NAME.sortAsc("Paulo", "George"))
      .execute();</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/CASE/">The CASE clause</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Arithmetic operations and concatenation" href="<?=$root?>/manual/DSL/ARITHMETIC/">previous</a> : <a title="Next section: Type casting" href="<?=$root?>/manual/DSL/CAST/">next</a></td>
</tr>
</table>
<?php 
}
?>

