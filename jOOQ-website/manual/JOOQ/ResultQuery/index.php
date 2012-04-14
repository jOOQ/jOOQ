
<?php
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "ResultQuery and fetch() methods";
}
function getActiveMenu() {
	return "learn";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual. Multiple Pages</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/ResultQuery/">ResultQuery and fetch() methods</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The Query and its various subtypes" href="<?=$root?>/manual/JOOQ/Query/">previous</a> : <a title="Next section: Bind values and parameters" href="<?=$root?>/manual/JOOQ/BindValues/">next</a></td>
</tr>
</table>
							<h2>The ResultQuery and its convenience methods</h2>
							<p>
							    Data fetching is one of the great hassles in JDBC and JPA.
							    With jOOQ, you will be able to specify exactly, what kind of
							    data you want to fetch from any given query, as well as how
							    you want to fetch that data. This doesn't just mean distinguishing
							    between fetching one record at a time, or the whole resultset,
							    between fetching one column at a time, or the whole resultset.
							    This also means transforming your result (a list) into a map,
							    into arrays, into custom types, into JPA-annotated types, into
							    a call-back, or simply fetching it asynchronously
							</p>
							<p>These methods allow for fetching a jOOQ Result or parts of it.</p>

<pre class="prettyprint lang-java">// Fetch the whole result
Result&lt;R&gt; fetch();

// Fetch a single field from the result
&lt;T&gt; List&lt;T&gt; fetch(Field&lt;T&gt; field);
    List&lt;?&gt; fetch(int fieldIndex);
&lt;T&gt; List&lt;T&gt; fetch(int fieldIndex, Class&lt;? extends T&gt; type);
    List&lt;?&gt; fetch(String fieldName);
&lt;T&gt; List&lt;T&gt; fetch(String fieldName, Class&lt;? extends T&gt; type);

// Fetch the first Record
R fetchAny();

// Fetch exactly one Record
R fetchOne();

// Fetch a single field of exactly one Record
&lt;T&gt; T  fetchOne(Field&lt;T&gt; field);
Object fetchOne(int fieldIndex);
&lt;T&gt; T  fetchOne(int fieldIndex, Class&lt;? extends T&gt; type);
Object fetchOne(String fieldName);
&lt;T&gt; T  fetchOne(String fieldName, Class&lt;? extends T&gt; type);</pre>

							<p>These methods transform the result into another form, if org.jooq.Result is not optimal</p>

<pre class="prettyprint lang-java">// Fetch the resulting records as Maps
List&lt;Map&lt;String, Object&gt;&gt; fetchMaps();
     Map&lt;String, Object&gt;  fetchOneMap();

// Fetch the result as a Map
&lt;K&gt;    Map&lt;K, R&gt; fetchMap(Field&lt;K&gt; key);
&lt;K, V&gt; Map&lt;K, V&gt; fetchMap(Field&lt;K&gt; key, Field&lt;V&gt; value);

// Fetch the resulting records as arrays
Object[][] fetchArrays();
Object[]   fetchOneArray();

// Fetch a single field as an array
&lt;T&gt;  T[] fetchArray(Field&lt;T&gt; field);
Object[] fetchArray(int fieldIndex);
&lt;T&gt;  T[] fetchArray(int fieldIndex, Class&lt;? extends T&gt; type);
Object[] fetchArray(String fieldName);
&lt;T&gt;  T[] fetchArray(String fieldName, Class&lt;? extends T&gt; type);</pre>

							<p>These methods transform the result into a user-defined form, if org.jooq.Result is not optimal</p>

<pre class="prettyprint lang-java">// Fetch the resulting records into a custom POJO
// type, which may or may not be JPA-annotated
// Use the generator's &lt;pojos&gt;true&lt;/pojos&gt; and &lt;jpaAnnotation&gt;true&lt;/jpaAnnotation&gt;
// configurations to generate such POJOs with jOOQ
&lt;E&gt; List&lt;E&gt; fetchInto(Class&lt;? extends E&gt; type);

// Fetch the resulting records into a custom
// record handler, similar to how Spring JdbcTemplate's
// RowMapper or the Ollin Framework works.
&lt;H extends RecordHandler&lt;R&gt;&gt; H fetchInto(H handler);

// These change the behaviour of fetching itself,
// especially, when not all data should be
// fetched at once
// ----------------------------------------------

// Fetch a Cursor for lazy iteration
Cursor&lt;R&gt; fetchLazy();

// Or a JDBC ResultSet, if you prefer that
ResultSet fetchResultSet();

// Fetch data asynchronously and let client code
// decide, when the data must be available.
// This makes use of the java.util.concurrent API,
// Similar to how Avaj&eacute; Ebean works.
FutureResult&lt;R&gt; fetchLater();
FutureResult&lt;R&gt; fetchLater(ExecutorService executor);</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual. Multiple Pages</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/ResultQuery/">ResultQuery and fetch() methods</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The Query and its various subtypes" href="<?=$root?>/manual/JOOQ/Query/">previous</a> : <a title="Next section: Bind values and parameters" href="<?=$root?>/manual/JOOQ/BindValues/">next</a></td>
</tr>
</table>
<?php
}
?>

