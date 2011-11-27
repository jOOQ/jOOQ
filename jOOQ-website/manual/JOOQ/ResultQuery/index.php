
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "ResultQuery and various ways of fetching data";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Various jOOQ query type extend the ResultQuery which provides many means of
							fetching data. In general, fetching means executing and returning some
							sort of result.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/ResultQuery/">ResultQuery and various ways of fetching data</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The Query and its various subtypes" href="<?=$root?>/manual/JOOQ/Query/">previous</a> : <a title="Next section: QueryParts and the global architecture" href="<?=$root?>/manual/JOOQ/QueryPart/">next</a></td>
</tr>
</table>
							<h2>The ResultQuery provides many convenience methods</h2>
<pre class="prettyprint lang-java">public interface ResultQuery&lt;R extends Record&gt; {

  // These methods allow for fetching a jOOQ Result
  // or parts of it.
  // ----------------------------------------------

  // Fetch the whole result
  Result&lt;R&gt; fetch();

  // Fetch a single field from the result
  &lt;T&gt; List&lt;T&gt; fetch(Field&lt;T&gt; field);
      List&lt;?&gt; fetch(int fieldIndex);
      List&lt;?&gt; fetch(String fieldName);

  // Fetch the first Record
  R fetchAny();

  // Fetch exactly one Record
  R fetchOne();

  // Fetch a single field of exactly one Record
  &lt;T&gt; T  fetchOne(Field&lt;T&gt; field);
  Object fetchOne(int fieldIndex);
  Object fetchOne(String fieldName);

  // These methods transform the result into another
  // form, if org.jooq.Result is not optimal
  // -----------------------------------------------

  // Fetch the resulting records as Maps
  List&lt;Map&lt;String, Object&gt;&gt; fetchMaps();
  Map&lt;String, Object&gt; fetchOneMap();

  // Fetch the result as a Map
  &lt;K&gt;    Map&lt;K, R&gt; fetchMap(Field&lt;K&gt; key);
  &lt;K, V&gt; Map&lt;K, V&gt; fetchMap(Field&lt;K&gt; key, Field&lt;V&gt; value);

  // Fetch the resulting records as arrays
  Object[][] fetchArrays();
  Object[] fetchOneArray();

  // Fetch a single field as an array
  &lt;T&gt; T[] fetchArray(Field&lt;T&gt; field);
  Object[] fetchArray(int fieldIndex);
  Object[] fetchArray(String fieldName);

  // These methods transform the result into a user-
  // defined form, if org.jooq.Result is not optimal
  // -----------------------------------------------

  // Fetch the resulting records into a custom POJO
  // type, which may or may not be JPA-annotated
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

  // Fetch data asynchronously and let client code
  // decide, when the data must be available.
  // This makes use of the java.util.concurrent API,
  // Similar to how Avaj&eacute; Ebean works.
  FutureResult&lt;R&gt; fetchLater();
  FutureResult&lt;R&gt; fetchLater(ExecutorService executor);
}</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/ResultQuery/">ResultQuery and various ways of fetching data</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The Query and its various subtypes" href="<?=$root?>/manual/JOOQ/Query/">previous</a> : <a title="Next section: QueryParts and the global architecture" href="<?=$root?>/manual/JOOQ/QueryPart/">next</a></td>
</tr>
</table>
<?php 
}
?>

