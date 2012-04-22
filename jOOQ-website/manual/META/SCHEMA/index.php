
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "The schema, top-level generated artefact";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "The schema is the top-level generated object in jOOQ. In many
							RDBMS, the schema coincides with the owner of tables and other objects
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/SCHEMA/">The schema, top-level generated artefact</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Advanced configuration of the generator" href="<?=$root?>/manual/META/AdvancedConfiguration/">previous</a> : <a title="Next section: Tables, views and their corresponding records" href="<?=$root?>/manual/META/TABLE/">next</a></td>
</tr>
</table>
							<h2>The Schema</h2>
							<p>
								As of jOOQ 1.5, the top-level generated object is the
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/Schema.html" title="Internal API reference: org.jooq.Schema">org.jooq.Schema</a>.
								The Schema itself has no relevant functionality, except for holding
								the schema name for all dependent generated artefacts. jOOQ queries try
								to always fully qualify an entity within the database using that Schema
							</p>

							<p>
								Currently, it is not possible to link generated artefacts from various
								schemata. If you have a stored function from Schema A, which returns a
								UDT from Schema B, the types cannot be linked. This enhancement is on
								the roadmap, though: <a href="https://sourceforge.net/apps/trac/jooq/ticket/282" title="Trac ticket: #282">#282</a>.
							</p>

							<p>
								When you have several schemata that are logically equivalent (i.e. they
								contain identical entities, but the schemata stand for different
								users/customers/clients, etc), there is a solution for that. Check out
								the manual's section on support for
								<a href="<?=$root?>/manual/ADVANCED/SchemaMapping/" title="jOOQ Manual reference: Mapping generated schemata and tables">multiple equivalent schemata</a>
							
</p>

							<h3>Schema contents</h3>
							<p>The schema can be used to dynamically discover generate database
								artefacts. Tables, sequences, and other items are accessible from the
								schema. For example:</p>
<pre class="prettyprint lang-java">public final java.util.List&lt;org.jooq.Sequence&lt;?&gt;&gt; getSequences();
public final java.util.List&lt;org.jooq.Table&lt;?&gt;&gt; getTables();</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/SCHEMA/">The schema, top-level generated artefact</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Advanced configuration of the generator" href="<?=$root?>/manual/META/AdvancedConfiguration/">previous</a> : <a title="Next section: Tables, views and their corresponding records" href="<?=$root?>/manual/META/TABLE/">next</a></td>
</tr>
</table>
<?php 
}
?>

