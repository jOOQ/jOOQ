
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Exporting to XML, CSV, JSON, HTML, Text";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Get your data out of the Java world. Stream your data using any of the supported, wide-spread formats
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/Export/">Exporting to XML, CSV, JSON, HTML, Text</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: jOOQ's relational division syntax" href="<?=$root?>/manual/ADVANCED/DIVISION/">previous</a> : <a title="Next section: Importing data from XML, CSV" href="<?=$root?>/manual/ADVANCED/Import/">next</a></td>
</tr>
</table>
							<h2>Exporting with jOOQ</h2>
							<p>If you are using jOOQ for scripting purposes or in a slim, unlayered
								application server, you might be interested in using jOOQ's exporting
								functionality (see also importing functionality). You can export any
								Result&lt;Record&gt; into any of these formats: </p>

							<h3>XML</h3>
							<p>Export your results as XML: </p>
<pre class="prettyprint lang-java">// Fetch books and format them as XML
String xml = create.selectFrom(T_BOOK).fetch().formatXML();</pre>

							<p>The above query will result in an XML document looking like the following one: </p>
<pre class="prettyprint lang-xml">&lt;!-- Find the XSD definition on www.jooq.org: --&gt;
&lt;jooq-export:result xmlns:jooq-export="http://www.jooq.org/xsd/jooq-export-1.6.2.xsd"&gt;
  &lt;fields&gt;
    &lt;field name="ID"/&gt;
    &lt;field name="AUTHOR_ID"/&gt;
    &lt;field name="TITLE"/&gt;
  &lt;/fields&gt;
  &lt;records&gt;
    &lt;record&gt;
      &lt;value field="ID"&gt;1&lt;/value&gt;
      &lt;value field="AUTHOR_ID"&gt;1&lt;/value&gt;
      &lt;value field="TITLE"&gt;1984&lt;/value&gt;
    &lt;/record&gt;
    &lt;record&gt;
      &lt;value field="ID"&gt;2&lt;/value&gt;
      &lt;value field="AUTHOR_ID"&gt;1&lt;/value&gt;
      &lt;value field="TITLE"&gt;Animal Farm&lt;/value&gt;
    &lt;/record&gt;
  &lt;/records&gt;
&lt;/jooq-export:result&gt;</pre>

							<h3>CSV</h3>
							<p>Export your results as CSV: </p>
<pre class="prettyprint lang-java">// Fetch books and format them as CSV
String csv = create.selectFrom(T_BOOK).fetch().formatCSV();</pre>

							<p>The above query will result in a CSV document looking like the following one: </p>
<pre>ID;AUTHOR_ID;TITLE
1;1;1984
2;1;Animal Farm</pre>


							<h3>JSON</h3>
							<p>Export your results as JSON: </p>

<pre class="prettyprint lang-java">// Fetch books and format them as JSON
String json = create.selectFrom(T_BOOK).fetch().formatJSON();</pre>
							<p>The above query will result in a JSON document looking like the following one: </p>
<pre>{fields:["ID","AUTHOR_ID","TITLE"],
 records:[[1,1,"1984"],[2,1,"Animal Farm"]]}</pre>

 							<h3>HTML </h3>
 							<p>Export your results as HTML: </p>
<pre class="prettyprint lang-java">// Fetch books and format them as HTML
String html = create.selectFrom(T_BOOK).fetch().formatHTML();</pre>
							<p>The above query will result in an HTML document looking like the following one: </p>
<pre class="prettyprint lang-xml">&lt;table&gt;
  &lt;thead&gt;
    &lt;tr&gt;
      &lt;th&gt;ID&lt;/th&gt;
      &lt;th&gt;AUTHOR_ID&lt;/th&gt;
      &lt;th&gt;TITLE&lt;/th&gt;
    &lt;/tr&gt;
  &lt;/thead&gt;
  &lt;tbody&gt;
    &lt;tr&gt;
      &lt;td&gt;1&lt;/td&gt;
      &lt;td&gt;1&lt;/td&gt;
      &lt;td&gt;1984&lt;/td&gt;
    &lt;/tr&gt;
    &lt;tr&gt;
      &lt;td&gt;2&lt;/td&gt;
      &lt;td&gt;1&lt;/td&gt;
      &lt;td&gt;Animal Farm&lt;/td&gt;
    &lt;/tr&gt;
  &lt;/tbody&gt;
&lt;/table&gt;</pre>

							<h3>Text</h3>
							<p>Export your results as text: </p>
<pre class="prettyprint lang-java">// Fetch books and format them as text
String text = create.selectFrom(T_BOOK).fetch().format();</pre>

							<p>The above query will result in a text document looking like the following one: </p>
<pre>+---+---------+-----------+
| ID|AUTHOR_ID|TITLE      |
+---+---------+-----------+
|  1|        1|1984       |
|  2|        1|Animal Farm|
+---+---------+-----------+</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/Export/">Exporting to XML, CSV, JSON, HTML, Text</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: jOOQ's relational division syntax" href="<?=$root?>/manual/ADVANCED/DIVISION/">previous</a> : <a title="Next section: Importing data from XML, CSV" href="<?=$root?>/manual/ADVANCED/Import/">next</a></td>
</tr>
</table>
<?php 
}
?>

