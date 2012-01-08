
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Results and Records";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Results and their Records come into play, when SELECT statements are
							executed. There are various ways to fetch data from a jOOQ SELECT
							statement. Essentially, the query results are always provided in the
							Result API
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/Result/">Results and Records</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Tables and Fields" href="<?=$root?>/manual/JOOQ/Table/">previous</a> : <a title="Next section: Updatable Records" href="<?=$root?>/manual/JOOQ/UpdatableRecord/">next</a></td>
</tr>
</table>
							<h2>The Result</h2>
							<p>
								The
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Result.java" title="Internal API reference: org.jooq.Result">Result</a>&lt;R extends <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Record.java" title="Internal API reference: org.jooq.Record">Record</a>&gt;
								is essentially a wrapper for a List&lt;R extends Record&gt;
								providing
								many convenience methods for accessing single elements in
								the result
								set. Depending on the type of SELECT statement,
								&lt;R&gt; can be bound
								to a sub-type of Record, for instance to an
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/UpdatableRecord.java" title="Internal API reference: org.jooq.UpdatableRecord">org.jooq.UpdatableRecord</a>.
								See the section on
								<a href="<?=$root?>/manual/JOOQ/UpdatableRecord/" title="jOOQ Manual reference: Updatable Records">Updatable Records</a>
								for further details.
							</p>

							<h2>The Cursor</h2>
							<p>
								A similar object is the
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Cursor.java" title="Internal API reference: org.jooq.Cursor">Cursor</a>&lt;R extends Record&gt;.
								Unlike the Result, the cursor has not fetched all data from the database yet.
								This means, you save memory (and potentially speed), but you can only access
								data sequentially and you have to keep a JDBC ResultSet alive. Cursors behave
								very much like the <a href="http://download.oracle.com/javase/6/docs/api/java/util/Iterator.html" title="External API reference: java.util.Iterator">java.util.Iterator</a>,
								by providing a very simple API. Some sample methods are:
							</p>
<pre class="prettyprint lang-java">// Check whether there are any more records to be fetched
boolean hasNext() throws SQLException;

// Fetch the next record from the underlying JDBC ResultSet
R fetchOne() throws SQLException;

// Close the underlying JDBC ResultSet. Don't forget to call this, before disposing the Cursor.
void close() throws SQLException;</pre>

							<h2>The Record</h2>
							<p>
								The Record itself holds all the data from your selected tuple. If it is
								a <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/TableRecord.java" title="Internal API reference: org.jooq.TableRecord">org.jooq.TableRecord</a>, then it corresponds exactly to the type of one of your
								physical tables in your database. But any anonymous or ad-hoc tuple can
								be represented by the plain Record. A record mainly provides access to
								its data and adds convenience methods for data type conversion. These
								are the main access ways:
							</p>
<pre class="prettyprint lang-java">// If you can keep a reference of the selected field, then you can get the corresponding value type-safely
&lt;T&gt; T getValue(Field&lt;T&gt; field);

// If you know the name of the selected field within the tuple,
// then you can get its value without any type information
Object getValue(String fieldName);

// If you know the index of the selected field within the tuple,
// then you can get its value without any type information
Object getValue(int index);</pre>
							<p>
								In some cases, you will not be able to reference the selected Fields
								both when you create the SELECT statement and when you fetch data from
								Records. Then you might use field names or indexes, as with JDBC.
								However, of course, the type information will then be lost as well. If
								you know what type you want to get, you can always use the Record's
								convenience methods for type conversion, however. Some examples:
							</p>
<pre class="prettyprint lang-java">// These methods will try to convert a value to a BigDecimal.
// This will work for all numeric types and for CHAR/VARCHAR types, if they contain numeric values:
BigDecimal getValueAsBigDecimal(String fieldName);
BigDecimal getValueAsBigDecimal(int fieldIndex);

// This method can perform arbitrary conversions
&lt;T&gt; T getValue(String fieldName, Class&lt;? extends T&gt; type);
&lt;T&gt; T getValue(int fieldIndex, Class&lt;? extends T&gt; type);</pre>

							<p>
								For more information about the type conversions that are supported by
								jOOQ, read the Javadoc on
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/tools/Convert.java" title="Internal API reference: org.jooq.tools.Convert">org.jooq.tools.Convert</a>
							
</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/Result/">Results and Records</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Tables and Fields" href="<?=$root?>/manual/JOOQ/Table/">previous</a> : <a title="Next section: Updatable Records" href="<?=$root?>/manual/JOOQ/UpdatableRecord/">next</a></td>
</tr>
</table>
<?php 
}
?>

