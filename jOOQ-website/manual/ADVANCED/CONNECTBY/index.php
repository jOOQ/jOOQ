
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "The Oracle CONNECT BY clause";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Hierarchical queries are supported by many RDBMS using the WITH clause.
							Oracle has a very neat and much less verbose syntax for hierarchical
							queries: CONNECT BY .. STARTS WITH
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/CONNECTBY/">The Oracle CONNECT BY clause</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Adding Oracle hints to queries" href="<?=$root?>/manual/ADVANCED/OracleHints/">previous</a> : <a title="Next section: Exporting to XML, CSV, JSON, HTML, Text" href="<?=$root?>/manual/ADVANCED/Export/">next</a></td>
</tr>
</table>
							<h2>CONNECT BY .. STARTS WITH</h2>
							<p>If you are closely coupling your application to an Oracle database,
								you can take advantage of some Oracle-specific features, such as the
								CONNECT BY clause, used for hierarchical queries. The formal syntax
								definition is as follows: </p>
								
<pre class="prettyprint lang-sql">--   SELECT ..
--     FROM ..
--    WHERE ..
 CONNECT BY [NOCYCLE] condition [AND condition, ...] [START WITH condition]
-- GROUP BY ..</pre>
							<p>This can be done in jOOQ using the .connectBy(Condition) clauses in your SELECT statement: </p>
<pre class="prettyprint lang-java">// Some Oracle-specific features are only available
// from the OracleFactory
OracleFactory create = new OracleFactory(connection);

// Get a table with elements 1, 2, 3, 4, 5
create.select(create.rownum())
      .connectBy(create.level().lessOrEqual(5))
      .fetch();</pre>

							<p>Here's a more complex example where you can recursively fetch
								directories in your database, and concatenate them to a path:</p>
<pre class="prettyprint lang-java"> OracleFactory ora = new OracleFactory(connection);

 List&lt;?&gt; paths =
 ora.select(ora.sysConnectByPath(DIRECTORY.NAME, "/").substring(2))
    .from(DIRECTORY)
    .connectBy(ora.prior(DIRECTORY.ID).equal(DIRECTORY.PARENT_ID))
    .startWith(DIRECTORY.PARENT_ID.isNull())
    .orderBy(ora.literal(1))
    .fetch(0);</pre>
    
    						<p>The output might then look like this</p>
<pre>+------------------------------------------------+
|substring                                       |
+------------------------------------------------+
|C:                                              |
|C:/eclipse                                      |
|C:/eclipse/configuration                        |
|C:/eclipse/dropins                              |
|C:/eclipse/eclipse.exe                          |
+------------------------------------------------+
|...21 record(s) truncated...
</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/CONNECTBY/">The Oracle CONNECT BY clause</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Adding Oracle hints to queries" href="<?=$root?>/manual/ADVANCED/OracleHints/">previous</a> : <a title="Next section: Exporting to XML, CSV, JSON, HTML, Text" href="<?=$root?>/manual/ADVANCED/Export/">next</a></td>
</tr>
</table>
<?php 
}
?>

