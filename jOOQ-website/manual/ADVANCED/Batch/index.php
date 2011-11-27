
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Using JDBC batch operations";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Some JDBC drivers have highly optimised means of executing batch
							operations. The JDBC interface for those operations is a bit verbose.
							jOOQ abstracts that by re-using the existing query API's
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/Batch/">Using JDBC batch operations</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Importing data from XML, CSV" href="<?=$root?>/manual/ADVANCED/Import/">previous</a></td>
</tr>
</table>
							<h2>JDBC batch operations</h2>
							<p>With JDBC, you can easily execute several statements at once using
								the addBatch() method. Essentially, there are two modes in JDBC</p>

							<ol>
								
<li>Execute several queries without bind values</li>
								
<li>Execute one query several times with bind values</li>
							
</ol>
	
							<p>In code, this looks like the following snippet:</p>
<pre class="prettyprint lang-java">// 1. several queries
// ------------------
Statement stmt = connection.createStatement();
stmt.addBatch("INSERT INTO author VALUES (1, 'Erich Gamma')");
stmt.addBatch("INSERT INTO author VALUES (2, 'Richard Helm')");
stmt.addBatch("INSERT INTO author VALUES (3, 'Ralph Johnson')");
stmt.addBatch("INSERT INTO author VALUES (4, 'John Vlissides')");
int[] result = stmt.executeBatch();

// 2. a single query
// -----------------
PreparedStatement stmt = connection.prepareStatement("INSERT INTO autho VALUES (?, ?)");
stmt.setInt(1, 1);
stmt.setString(2, "Erich Gamma");
stmt.addBatch();

stmt.setInt(1, 2);
stmt.setString(2, "Richard Helm");
stmt.addBatch();

stmt.setInt(1, 3);
stmt.setString(2, "Ralph Johnson");
stmt.addBatch();

stmt.setInt(1, 4);
stmt.setString(2, "John Vlissides");
stmt.addBatch();

int[] result = stmt.executeBatch();</pre>
								
								
							<h2>This will also be supported by jOOQ</h2>
							<p>Version 1.6.9 of jOOQ now supports executing queries in batch
								mode as follows:</p>
<pre class="prettyprint lang-java">// 1. several queries
// ------------------
create.batch(
	create.insertInto(AUTHOR, ID, NAME).values(1, "Erich Gamma"),
	create.insertInto(AUTHOR, ID, NAME).values(2, "Richard Helm"),
	create.insertInto(AUTHOR, ID, NAME).values(3, "Ralph Johnson"),
	create.insertInto(AUTHOR, ID, NAME).values(4, "John Vlissides"))
.execute();

// 2. a single query
// -----------------
create.batch(create.insertInto(AUTHOR, ID, NAME).values("?", "?"))
	  .bind(1, "Erich Gamma")
	  .bind(2, "Richard Helm")
	  .bind(3, "Ralph Johnson")
	  .bind(4, "John Vlissides")
	  .execute();</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/Batch/">Using JDBC batch operations</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Importing data from XML, CSV" href="<?=$root?>/manual/ADVANCED/Import/">previous</a></td>
</tr>
</table>
<?php 
}
?>

