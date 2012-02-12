
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Master data generation. Enumeration tables";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "Enumerations are a powerful concept. Unfortunately, almost no
							RDBMS supports them, leaving you to create numerous tables for your
							enumerated values. But these values are still enumerations!";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/MasterData/">Master data generation. Enumeration tables</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Advanced topics" href="<?=$root?>/manual/ADVANCED/">previous</a> : <a title="Next section: Mapping generated schemata and tables" href="<?=$root?>/manual/ADVANCED/SchemaMapping/">next</a></td>
</tr>
</table>
							<h2>Enumeration tables</h2>
							<p>Only MySQL and Postgres databases support true ENUM types natively.
								Some other RDBMS allow you to map the concept of an ENUM data type to
								a CHECK constraint, but those constraints can contain arbitrary SQL.
								 With jOOQ, you
								can "simulate" ENUM types by declaring a table as a "master data
								table" in the configuration. At code-generation time, this table will
								be treated specially, and a Java enum type is generated from its data.
							</p>

							<h2>Configure master data tables</h2>
							<p>As previously discussed in the
							  <a href="<?=$root?>/manual/META/Configuration/" title="jOOQ Manual reference: Configuration and setup of the generator">configuration and setup</a>
							   section, you can configure master data tables as follows: </p>
<pre class="prettyprint lang-xml">&lt;!-- These properties can be added to the database element: --&gt;
&lt;database&gt;
  &lt;masterDataTables&gt;
    &lt;masterDataTable&gt;
      &lt;!-- The name of a master data table --&gt;
      &lt;name&gt;[a table name]&lt;/name&gt;

      &lt;!-- The column used for enum literals --&gt;
      &lt;literal&gt;[a column name]&lt;/literal&gt;

      &lt;!-- The column used for documentation --&gt;
      &lt;description&gt;[a column name]&lt;/description&gt;
    &lt;/masterDataTable&gt;

    [ &lt;masterDataTable&gt;...&lt;/masterDataTable&gt; ... ]
  &lt;/masterDataTables&gt;
 &lt;/database&gt;</pre>

							<p>The results of this will be a Java enum that looks similar to this: </p>
<pre class="prettyprint lang-java">public enum TLanguage implements MasterDataType&lt;Integer&gt; {

  /**
   * English
   */
  en(1, "en", "English"),

  /**
   * Deutsch
   */
  de(2, "de", "Deutsch"),

  /**
   * Fran&ccedil;ais
   */
  fr(3, "fr", "Fran&ccedil;ais"),

  /**
   * null
   */
  pt(4, "pt", null),
  ;

  private final Integer id;
  private final String cd;
  private final String description;

  // [ ... constructor and getters for the above properties ]
}</pre>

							<p>In the above example, you can see how the configured primary key is
								mapped to the id member, the configured literal column is mapped to
								the cd member and the configured description member is mapped to the
								description member and output as Javadoc. In other words, T_LANGUAGE
								is a table with 4 rows and at least three columns. </p>
							<p>The general contract (with jOOQ 1.6.2+) is that there must be </p>
							<ul>
								
<li> A single-column primary key column of character or integer type
								</li>
								
<li>An optional unique literal column of character or integer type
									(otherwise, the primary key is used as enum literal) </li>
								
<li>An optional description column of any type </li>
							
</ul>

							<h2>Using MasterDataTypes</h2>
							<p>The point of MasterDataTypes in jOOQ is that they behave exactly
								like true ENUM types. When the above T_LANGUAGE table is referenced by
								T_BOOK, instead of generating foreign key navigation methods and a
								LANGUAGE_ID Field&lt;Integer&gt;, a Field&lt;TLanguage&gt; is
								generated: </p>

<pre class="prettyprint lang-java">public class TBook extends UpdatableTableImpl&lt;TBookRecord&gt; {

  // [...]
  public static final TableField&lt;TBookRecord, TLanguage&gt; LANGUAGE_ID =
                  new TableFieldImpl&lt;TBookRecord, TLanguage&gt;( /* ... */ );
}</pre>

							<p>Which can then be used in the TBookRecord directly: </p>
<pre class="prettyprint lang-java">public class TBookRecord extends UpdatableRecordImpl&lt;TBookRecord&gt; {

  // [...]
  public TLanguage getLanguageId() { // [...]
  public void setLanguageId(TLanguage value) { // [...]
}</pre>

							<h2>When to use MasterDataTypes</h2>
							<p>You can use master data types when you're actually mapping master
								data to a Java enum. When the underlying table changes frequently,
								those updates will not be reflected by the statically generated code.
								Also, be aware that it will be difficult to perform actual JOIN
								operations on the underlying table with jOOQ, once the master data
								type is generated. </p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/MasterData/">Master data generation. Enumeration tables</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Advanced topics" href="<?=$root?>/manual/ADVANCED/">previous</a> : <a title="Next section: Mapping generated schemata and tables" href="<?=$root?>/manual/ADVANCED/SchemaMapping/">next</a></td>
</tr>
</table>
<?php 
}
?>

