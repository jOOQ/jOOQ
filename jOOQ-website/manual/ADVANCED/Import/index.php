
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Importing data from XML, CSV";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Use jOOQ to easily merge imported data into your database.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/Import/">Importing data from XML, CSV</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Exporting to XML, CSV, JSON, HTML, Text" href="<?=$root?>/manual/ADVANCED/Export/">previous</a> : <a title="Next section: Using JDBC batch operations" href="<?=$root?>/manual/ADVANCED/Batch/">next</a></td>
</tr>
</table>
							<h2>Importing with jOOQ</h2>
							<p>If you are using jOOQ for scripting purposes or in a slim, unlayered
								application server, you might be interested in using jOOQ's importing
								functionality (see also exporting functionality). You can import data
								directly into a table from any of these formats: </p>
								
							<h3>CSV</h3>
							<p>The below CSV data represents two author records that may have been
								exported previously, by jOOQ's exporting functionality, and then
								modified in Microsoft Excel or any other spreadsheet tool: </p>
								
<pre>ID;AUTHOR_ID;TITLE
1;1;1984
2;1;Animal Farm</pre>

							<p>With jOOQ, you can load this data using various parameters from the
								loader API. A simple load may look like this: </p>
								
<pre class="prettyprint lang-java">Factory create = new Factory(connection, SQLDialect.ORACLE);

// Load data into the T_AUTHOR table from an input stream
// holding the CSV data.
create.loadInto(T_AUTHOR)
      .loadCSV(inputstream)
      .fields(ID, AUTHOR_ID, TITLE)
      .execute();</pre>
      
      						<p>Here are various other examples: </p>
<pre class="prettyprint lang-java">// Ignore the AUTHOR_ID column from the CSV file when inserting
create.loadInto(T_AUTHOR)
      .loadCSV(inputstream)
      .fields(ID, null, TITLE)
      .execute();

// Specify behaviour for duplicate records.
create.loadInto(T_AUTHOR)

      // choose any of these methods
      .onDuplicateKeyUpdate()
      .onDuplicateKeyIgnore()
      .onDuplicateKeyError() // the default

      .loadCSV(inputstream)
      .fields(ID, null, TITLE)
      .execute();

// Specify behaviour when errors occur.
create.loadInto(T_AUTHOR)

      // choose any of these methods
      .onErrorIgnore()
      .onErrorAbort() // the default

      .loadCSV(inputstream)
      .fields(ID, null, TITLE)
      .execute();

// Specify transactional behaviour where this is possible
// (e.g. not in container-managed transactions)
create.loadInto(T_AUTHOR)

      // choose any of these methods
      .commitEach()
      .commitAfter(10)
      .commitAll()
      .commitNone() // the default

      .loadCSV(inputstream)
      .fields(ID, null, TITLE)
      .execute();</pre>
      
							<p>Any of the above configuration methods can be combined to achieve
								the type of load you need. Please refer to the API's Javadoc to learn
								about more details. Errors that occur during the load are reported by
								the execute method's result: </p>
								
<pre class="prettyprint lang-java">Loader&lt;TAuthor&gt; loader = /* .. */ .execute();

// The number of processed rows
int processed = loader.processed();
 
// The number of stored rows (INSERT or UPDATE)
int stored = loader.stored();
 
// The number of ignored rows (due to errors, or duplicate rule)
int ignored = loader.ignored();
 
// The errors that may have occurred during loading
List&lt;LoaderError&gt; errors = loader.errors();
LoaderError error = errors.get(0);
 
// The exception that caused the error
SQLException exception = error.exception();
 
// The row that caused the error
int rowIndex = error.rowIndex();
String[] row = error.row();
 
// The query that caused the error
Query query = error.query();</pre>

							<h3>XML </h3>
							<p>This will be implemented soon... </p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/Import/">Importing data from XML, CSV</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Exporting to XML, CSV, JSON, HTML, Text" href="<?=$root?>/manual/ADVANCED/Export/">previous</a> : <a title="Next section: Using JDBC batch operations" href="<?=$root?>/manual/ADVANCED/Batch/">next</a></td>
</tr>
</table>
<?php 
}
?>

