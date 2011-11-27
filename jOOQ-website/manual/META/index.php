
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../frame.php';
function getH1() {
    return "Meta model code generation";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "In these sections you will learn about how to configure and use
					jOOQ's source code generator";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Extend jOOQ with custom types" href="<?=$root?>/manual/JOOQ/Extend/">previous</a> : <a title="Next section: Configuration and setup of the generator" href="<?=$root?>/manual/META/Configuration/">next</a></td>
</tr>
</table>
					<h2>Overview</h2>
					<p>
						In the previous chapter, we have seen how to use the 
						<a href="<?=$root?>/manual/JOOQ/Factory/" title="jOOQ Manual reference: The Factory class">Factory</a> in order to create 
						<a href="<?=$root?>/manual/JOOQ/Query/" title="jOOQ Manual reference: The Query and its various subtypes">Queries</a> and fetch data in 
						<a href="<?=$root?>/manual/JOOQ/Result/" title="jOOQ Manual reference: Results and Records">Results</a>. The strength of jOOQ not
						only lies in its object-oriented 
						<a href="<?=$root?>/manual/JOOQ/QueryPart/" title="jOOQ Manual reference: QueryParts and the global architecture">Query model</a>, 
						but also in the fact
						that Java source code is generated from your database schema into the
						META data model. jOOQ follows the paradigm, that your database comes
						first (see also <a href="<?=$root?>/">home page</a>). 
						This means that you should not modify
						generated source code, but only adapt entities in your database. Every
						change in your database is reflected in a corresponding change in your
						generated meta-model.
					</p>
					<p>
						Artefacts, such as tables, views, user defined types, sequences, stored
						procedures, packages have a corresponding artefact in Java.
					</p>
				<h3>Table of contents</h3><ol>
<li>
<a title="Configuration and setup of the generator" href="<?=$root?>/manual/META/Configuration/">Configuration and setup of the generator</a>
</li>
<li>
<a title="The schema, top-level generated artefact" href="<?=$root?>/manual/META/SCHEMA/">The schema, top-level generated artefact</a>
</li>
<li>
<a title="Tables, views and their corresponding records" href="<?=$root?>/manual/META/TABLE/">Tables, views and their corresponding records</a>
</li>
<li>
<a title="Procedures and packages" href="<?=$root?>/manual/META/PROCEDURE/">Procedures and packages</a>
</li>
<li>
<a title="UDT's including ARRAY and ENUM types" href="<?=$root?>/manual/META/UDT/">UDT's including ARRAY and ENUM types</a>
</li>
<li>
<a title="Sequences" href="<?=$root?>/manual/META/SEQUENCE/">Sequences</a>
</li>
</ol><br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Extend jOOQ with custom types" href="<?=$root?>/manual/JOOQ/Extend/">previous</a> : <a title="Next section: Configuration and setup of the generator" href="<?=$root?>/manual/META/Configuration/">next</a></td>
</tr>
</table>
<?php 
}
?>

