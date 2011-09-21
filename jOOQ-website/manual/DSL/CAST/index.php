
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function printH1() {
    print "Type casting";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Many RDBMS allow for implicit or explicit conversion between types.
							Apart from true type conversion, this is most often done with casting.
						";
}
function printContent() {
    global $root;
?>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/CAST/">Type casting</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/CASE/" title="Previous section: The CASE clause">previous</a> : <a href="<?=$root?>/manual/DSL/SQL/" title="Next section: When it's just easier: Plain SQL">next</a></td>
</tr>
</table>
							<h2>Enforcing a specific type when you need it</h2>
							<p>jOOQ's source code generator tries to find the most accurate type
								mapping between your vendor-specific data types and a matching Java
								type. For instance, most VARCHAR, CHAR, CLOB types will map to String.
								Most BINARY, BYTEA, BLOB types will map to byte[]. NUMERIC types will
								default to java.math.BigDecimal, but can also be any of
								java.math.BigInteger, Long, Integer, Short, Byte, Double, Float. </p>
							<p>Sometimes, this automatic mapping might not be what you needed, or
								jOOQ cannot know the type of a field (because you created it from a
								<a href="<?=$root?>/manual/DSL/NESTED/" title="jOOQ Manual reference: Other types of nested SELECT">nested select</a>). 
								In those cases you would write SQL type CASTs like
								this: </p>
							<pre class="prettyprint lang-sql">
-- Let's say, your Postgres column LAST_NAME was VARCHAR(30)
-- Then you could do this:
SELECT CAST(T_AUTHOR.LAST_NAME AS TEXT) FROM DUAL</pre>
							<p>in jOOQ, you can write something like that: </p>
							<pre class="prettyprint lang-java">create.select(TAuthor.LAST_NAME.cast(PostgresDataType.TEXT));</pre>
							<p>The same thing can be achieved by casting a Field directly to
								String.class, as TEXT is the default data type in Postgres to map to
								Java's String</p>
							<pre class="prettyprint lang-java">create.select(TAuthor.LAST_NAME.cast(String.class));</pre>
							<p>The complete CAST API in Field consists of these three methods: </p>
							<pre class="prettyprint lang-java">
public interface Field&lt;T&gt; {
    &lt;Z&gt; Field&lt;Z&gt; cast(Field&lt;Z&gt; field);
    &lt;Z&gt; Field&lt;Z&gt; cast(DataType&lt;Z&gt; type);
    &lt;Z&gt; Field&lt;Z&gt; cast(Class&lt;? extends Z&gt; type);
}

// And additional convenience methods in the Factory:
public class Factory {
    &lt;T&gt; Field&lt;T&gt; cast(Object object, Field&lt;T&gt; field);
    &lt;T&gt; Field&lt;T&gt; cast(Object object, DataType&lt;T&gt; type);
    &lt;T&gt; Field&lt;T&gt; cast(Object object, Class&lt;? extends T&gt; type);
    &lt;T&gt; Field&lt;T&gt; castNull(Field&lt;T&gt; field);
    &lt;T&gt; Field&lt;T&gt; castNull(DataType&lt;T&gt; type);
    &lt;T&gt; Field&lt;T&gt; castNull(Class&lt;? extends T&gt; type);
}</pre>
						<br><table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/DSL/">DSL or fluent API. Where SQL meets Java</a> : <a href="<?=$root?>/manual/DSL/CAST/">Type casting</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/CASE/" title="Previous section: The CASE clause">previous</a> : <a href="<?=$root?>/manual/DSL/SQL/" title="Next section: When it's just easier: Plain SQL">next</a></td>
</tr>
</table>
<?php 
}
?>

