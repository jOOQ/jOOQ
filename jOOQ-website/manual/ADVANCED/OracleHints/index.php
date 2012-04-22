
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Adding Oracle hints to queries";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Oracle has a powerful syntax to add hints as comments directly in your SQL
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/OracleHints/">Adding Oracle hints to queries</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Execute listeners and the jOOQ Console" href="<?=$root?>/manual/ADVANCED/ExecuteListener/">previous</a> : <a title="Next section: The Oracle CONNECT BY clause" href="<?=$root?>/manual/ADVANCED/CONNECTBY/">next</a></td>
</tr>
</table>
							<h2>How to embed Oracle hints in SELECT</h2>
							<p>If you are closely coupling your application to an Oracle database,
								you might need to be able to pass hints of the form /*+HINT*/ with
								your SQL statements to the Oracle database. For example: </p>
<pre class="prettyprint lang-sql">SELECT /*+ALL_ROWS*/ FIRST_NAME, LAST_NAME
  FROM T_AUTHOR</pre>

  							<p>This can be done in jOOQ using the .hint() clause in your SELECT statement: </p>
<pre class="prettyprint lang-java">create.select(FIRST_NAME, LAST_NAME)
      .hint("/*+ALL_ROWS*/")
      .from(T_AUTHOR);</pre>

							<p>Note that you can pass any string in the .hint() clause. If you use
								that clause, the passed string will always be put in between the
								SELECT [DISTINCT] keywords and the actual projection list </p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/OracleHints/">Adding Oracle hints to queries</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Execute listeners and the jOOQ Console" href="<?=$root?>/manual/ADVANCED/ExecuteListener/">previous</a> : <a title="Next section: The Oracle CONNECT BY clause" href="<?=$root?>/manual/ADVANCED/CONNECTBY/">next</a></td>
</tr>
</table>
<?php 
}
?>

