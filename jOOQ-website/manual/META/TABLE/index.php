
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Tables, views and their corresponding records";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							The most important generated artefacts are Tables and TableRecords.
							Every Table has a Record type associated with it that models a single tuple
							of that entity: Table&lt;R extends Record&gt;.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/TABLE/">Tables, views and their corresponding records</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The schema, top-level generated artefact" href="<?=$root?>/manual/META/SCHEMA/">previous</a> : <a title="Next section: Procedures and packages" href="<?=$root?>/manual/META/PROCEDURE/">next</a></td>
</tr>
</table>
							<h2>Tables and TableRecords</h2>
							<p>
								The most important generated artefacts are
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/Table.java" title="Internal API reference: org.jooq.Table">Tables</a> and
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/TableRecord.java" title="Internal API reference: org.jooq.TableRecord">TableRecords</a>. As
								discussed in previous chapters about
								<a href="<?=$root?>/manual/JOOQ/Table/" title="jOOQ Manual reference: Tables and Fields">Tables</a> and
								<a href="<?=$root?>/manual/JOOQ/Result/" title="jOOQ Manual reference: Results and Records">Results</a>, jOOQ uses the
								Table class to model entities (both tables and views) in your database
								Schema. Every Table has a Record type associated with it that models a
								single tuple of that entity: Table&lt;R extends Record&gt;. This
								couple of Table&lt;R&gt; and R are generated as such:
							</p>
							<p>
								Suppose we have the tables as defined in the
								<a href="<?=$root?>/manual/JOOQ/ExampleDatabase/" title="jOOQ Manual reference: The example database">example database</a>.
								Then, using a
								default configuration, these (simplified for the example) classes will
								be generated:
							</p>

							<h3>The Table as an entity meta model</h3>
<pre class="prettyprint lang-java">public class TAuthor extends UpdatableTableImpl&lt;TAuthorRecord&gt; {

    // The singleton instance of the Table
    public static final TAuthor T_AUTHOR = new TAuthor();

    // The Table's fields.
    // Depending on your jooq-codegen configuraiton, they can also be static
    public final TableField&lt;TAuthorRecord, Integer&gt; ID =            // [...]
    public final TableField&lt;TAuthorRecord, String&gt; FIRST_NAME =     // [...]
    public final TableField&lt;TAuthorRecord, String&gt; LAST_NAME =      // [...]
    public final TableField&lt;TAuthorRecord, Date&gt; DATE_OF_BIRTH =    // [...]
    public final TableField&lt;TAuthorRecord, Integer&gt; YEAR_OF_BIRTH = // [...]

    // When you don't choose the static meta model, you can typesafely alias your tables.
    // Aliased tables will then hold references to the above final fields, too
    public TAuthor as(String alias) {
      // [...]
    }
}</pre>

							<h3>The Table's associated TableRecord</h3>
							<p>If you use the
								<a href="<?=$root?>/manual/JOOQ/Query/" title="jOOQ Manual reference: The Query and its various subtypes">SimpleSelectQuery</a>
								 syntax (both in standard and DSL
								mode), then your SELECT statement will return the single Table&lt;R
								extends Record&gt;'s associated Record type &lt;R&gt;. In the case of
								the above TAuthor Table, this will be a TAuthorRecord. </p>

<pre class="prettyprint lang-java">public class TAuthorRecord extends UpdatableRecordImpl&lt;TAuthorRecord&gt; {

    // Getters and setters for the various fields
    public void setId(Integer value) {       // [...]
    public Integer getId() {                 // [...]
    public void setFirstName(String value) { // [...]
    public String getFirstName() {           // [...]
    public void setLastName(String value) {  // [...]
    public String getLastName() {            // [...]
    public void setDateOfBirth(Date value) { // [...]
    public Date getDateOfBirth() {           // [...]

    // Navigation methods for foreign keys
    public List&lt;TBookRecord&gt; fetchTBooks() { // [...]
}</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/TABLE/">Tables, views and their corresponding records</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The schema, top-level generated artefact" href="<?=$root?>/manual/META/SCHEMA/">previous</a> : <a title="Next section: Procedures and packages" href="<?=$root?>/manual/META/PROCEDURE/">next</a></td>
</tr>
</table>
<?php 
}
?>

