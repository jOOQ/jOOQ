
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../frame.php';
function printH1() {
    print 'jOOQ classes and their use';
}
function printSlogan() {}
function printContent() {
    global $root;
?>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their use</a></td><td align="right"><a href="<?=$root?>/manual/">previous</a> : <a href="<?=$root?>/manual/JOOQ/ExampleDatabase/">next</a></td>
</tr>
</table>
					<p>
						In this section, you will learn about how to use jOOQ object
						factories and the jOOQ object oriented query model, to express
						your
						SQL in jOOQ
					</p>
					<h2>Overview</h2>
					<p>jOOQ essentially has two packages:</p>
					<ul>
						
<li>org.jooq: the jOOQ API. Here you will find interfaces for all
							SQL concepts
						</li>
						
<li>org.jooq.impl: the jOOQ implementation and factories. Most
							implementation classes are package private, you can only access
							them using the org.jooq.impl.Factory
						</li>
					
</ul>
					<p>
						This section is about the main jOOQ classes and the global
						architecture. Most of the time, however, you will be using the DSL
						API (DSL for Domain Specific Language) in order to create queries
						the way you're used to in SQL
					</p>
				<h3>Table of contents</h3><ol>
<li>
<a href="<?=$root?>/manual/JOOQ/ExampleDatabase/" title="The example database">The example database</a>
</li>
<li>
<a href="<?=$root?>/manual/JOOQ/Factory/" title="The factory class">The factory class</a>
</li>
<li>
<a href="<?=$root?>/manual/JOOQ/Table/" title="Tables and Fields">Tables and Fields</a>
</li>
<li>
<a href="<?=$root?>/manual/JOOQ/Result/" title="Results and Records">Results and Records</a>
</li>
<li>
<a href="<?=$root?>/manual/JOOQ/UpdatableRecord/" title="Updatable Records">Updatable Records</a>
</li>
<li>
<a href="<?=$root?>/manual/JOOQ/Query/" title="Query and its subtypes">Query and its subtypes</a>
</li>
<li>
<a href="<?=$root?>/manual/JOOQ/ResultQuery/" title="ResultQuery and various ways of fetching data">ResultQuery and various ways of fetching data</a>
</li>
<li>
<a href="<?=$root?>/manual/JOOQ/QueryPart/" title="QueryParts and the global architecture">QueryParts and the global architecture</a>
</li>
<li>
<a href="<?=$root?>/manual/JOOQ/Serializability/" title="Serializability of QueryParts and Results">Serializability of QueryParts and Results</a>
</li>
<li>
<a href="<?=$root?>/manual/JOOQ/Extend/" title="Extend jOOQ types with custom implementations">Extend jOOQ types with custom implementations</a>
</li>
</ol>
<?php 
}
?>

