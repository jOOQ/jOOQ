
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
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/Table.html" title="Internal API reference: org.jooq.Table">Tables</a> and
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/TableRecord.html" title="Internal API reference: org.jooq.TableRecord">TableRecords</a>. As
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


							<h3>Generated or custom POJO's instead of jOOQ's Records</h3>
							<p>
								If you're using jOOQ along with Hibernate / JPA, or if you
								want to use your own, custom domain-model instead of jOOQ's
								Record type-hierarchy, you can choose to select values into
								POJOs. Let's say you defined a POJO for authors:
							</p>

<pre class="prettyprint lang-java">package com.example;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MyAuthor {
    // Some fields may be public
    @Column(name = "ID")
    public int id;

    // Others are private and have associated getters / setters:
    private String firstName;
    private String lastName;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }
}</pre>

                            <p>
                            	The above could be your custom POJO or a POJO generated
                            	by jooq-codegen (see
                            	<a href="<?=$root?>/manual/META/AdvancedConfiguration/" title="jOOQ Manual reference: Advanced configuration of the generator">the manual's section about advanced codegen configuration</a>
                            	for more details). Also, JPA-annotations are not necessary
                            	if you wish to let jOOQ map record columns onto your POJO
                            	attributes by convention. Instead of fetching records normally,
                            	you can now let jOOQ fetch records "into" your custom type:
                            </p>

<pre class="prettyprint lang-java">List&lt;MyAuthor&gt; results = create.select().from(TAuthor.T_AUTHOR).fetchInto(MyAuthor.class);</pre>

							<p>
								Read the javadoc for
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/Record.html#into%28java.lang.Class%29" title="Record.into() javadoc, explaining about how to map jOOQ Records onto custom types">Record.into()</a>
								for more details.
							</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/TABLE/">Tables, views and their corresponding records</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The schema, top-level generated artefact" href="<?=$root?>/manual/META/SCHEMA/">previous</a> : <a title="Next section: Procedures and packages" href="<?=$root?>/manual/META/PROCEDURE/">next</a></td>
</tr>
</table>
<?php 
}
?>

