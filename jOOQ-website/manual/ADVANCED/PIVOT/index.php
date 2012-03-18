
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "The Oracle 11g PIVOT clause";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
				    	    Oracle 11g has formally introduced the very powerful PIVOT clause, which
				    	    allows to specify a pivot column, expected grouping values for pivoting,
				    	    as well as a set of aggregate functions
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/PIVOT/">The Oracle 11g PIVOT clause</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The Oracle CONNECT BY clause" href="<?=$root?>/manual/ADVANCED/CONNECTBY/">previous</a> : <a title="Next section: jOOQ's relational division syntax" href="<?=$root?>/manual/ADVANCED/DIVISION/">next</a></td>
</tr>
</table>
							<h2>PIVOT (aggregate FOR column IN (columns))</h2>
							<p>If you are closely coupling your application to an Oracle database,
								you can take advantage of some Oracle-specific features, such as the
								PIVOT clause, used for statistical analyses. The formal syntax
								definition is as follows: </p>
<pre class="prettyprint lang-sql">-- SELECT ..
     FROM table PIVOT (aggregateFunction [, aggregateFunction] FOR column IN (expression [, expression]))
--  WHERE ..</pre>

							<p>
								The PIVOT clause is available from the
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/Table.html" title="Internal API reference: org.jooq.Table">org.jooq.Table</a>
								type, as pivoting is done directly on a table.
								Currently, only Oracle's PIVOT clause is supported. Support for SQL Server's
								PIVOT clause will be added later. Also, jOOQ may simulate PIVOT for other
								dialects in the future.
							</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/PIVOT/">The Oracle 11g PIVOT clause</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The Oracle CONNECT BY clause" href="<?=$root?>/manual/ADVANCED/CONNECTBY/">previous</a> : <a title="Next section: jOOQ's relational division syntax" href="<?=$root?>/manual/ADVANCED/DIVISION/">next</a></td>
</tr>
</table>
<?php 
}
?>

