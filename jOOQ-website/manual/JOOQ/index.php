
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../frame.php';
function getH1() {
    return "jOOQ classes and their usage";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
					In these sections, you will learn about how to use jOOQ object
					factories and the jOOQ query model, to express
					your SQL in jOOQ
				";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The jOOQ User Manual" href="<?=$root?>/manual/">previous</a> : <a title="Next section: The example database" href="<?=$root?>/manual/JOOQ/ExampleDatabase/">next</a></td>
</tr>
</table>
					<h2>Overview</h2>
					<p>jOOQ essentially has two packages:</p>
					<ul>
						
<li>org.jooq: the jOOQ API. Here you will find interfaces for all
							SQL concepts
						</li>
						
<li>org.jooq.impl: the jOOQ implementation and factories. Most
							implementation classes are package private, you can only access
							them using the <a href="<?=$root?>/manual/JOOQ/Factory/" title="jOOQ Manual reference: The Factory class">org.jooq.impl.Factory</a> 
						
</li>
					
</ul>
					<p>
						This section is about the main jOOQ classes and the global
						architecture. Most of the time, however, you will be using the
						<a href="<?=$root?>/manual/DSL/" title="jOOQ Manual reference: DSL or fluent API. Where SQL meets Java">DSL or fluent API. Where SQL meets Java</a>
						in order to create queries
						the way you're used to in SQL
					</p>
				<h3>Table of contents</h3><ol>
<li>
<a title="The example database" href="<?=$root?>/manual/JOOQ/ExampleDatabase/">The example database</a>
</li>
<li>
<a title="The Factory class" href="<?=$root?>/manual/JOOQ/Factory/">The Factory class</a>
</li>
<li>
<a title="Tables and Fields" href="<?=$root?>/manual/JOOQ/Table/">Tables and Fields</a>
</li>
<li>
<a title="Results and Records" href="<?=$root?>/manual/JOOQ/Result/">Results and Records</a>
</li>
<li>
<a title="Updatable Records" href="<?=$root?>/manual/JOOQ/UpdatableRecord/">Updatable Records</a>
</li>
<li>
<a title="The Query and its various subtypes" href="<?=$root?>/manual/JOOQ/Query/">The Query and its various subtypes</a>
</li>
<li>
<a title="ResultQuery and various ways of fetching data" href="<?=$root?>/manual/JOOQ/ResultQuery/">ResultQuery and various ways of fetching data</a>
</li>
<li>
<a title="QueryParts and the global architecture" href="<?=$root?>/manual/JOOQ/QueryPart/">QueryParts and the global architecture</a>
</li>
<li>
<a title="Serializability of QueryParts and Results" href="<?=$root?>/manual/JOOQ/Serializability/">Serializability of QueryParts and Results</a>
</li>
<li>
<a title="Extend jOOQ with custom types" href="<?=$root?>/manual/JOOQ/Extend/">Extend jOOQ with custom types</a>
</li>
</ol><br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: The jOOQ User Manual" href="<?=$root?>/manual/">previous</a> : <a title="Next section: The example database" href="<?=$root?>/manual/JOOQ/ExampleDatabase/">next</a></td>
</tr>
</table>
<?php 
}
?>

