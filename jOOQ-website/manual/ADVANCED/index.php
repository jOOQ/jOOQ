
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../frame.php';
function printH1() {
    print "Advanced topics";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "In these sections you will learn about advanced concepts that
					you might not use every day";
}
function printContent() {
    global $root;
?>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/SQL/" title="Previous section: When it's just easier: Plain SQL">previous</a> : <a href="<?=$root?>/manual/ADVANCED/MasterData/" title="Next section: Master data generation. Enumeration tables">next</a></td>
</tr>
</table>
					<h2>Overview</h2>
					<p>This section covers some advanced topics and features that don't fit into any other section. </p>
				<h3>Table of contents</h3><ol>
<li>
<a href="<?=$root?>/manual/ADVANCED/MasterData/" title="Master data generation. Enumeration tables">Master data generation. Enumeration tables</a>
</li>
<li>
<a href="<?=$root?>/manual/ADVANCED/SchemaMapping/" title="Mapping generated schemata and tables">Mapping generated schemata and tables</a>
</li>
<li>
<a href="<?=$root?>/manual/ADVANCED/OracleHints/" title="Adding Oracle hints to queries">Adding Oracle hints to queries</a>
</li>
<li>
<a href="<?=$root?>/manual/ADVANCED/CONNECTBY/" title="The Oracle CONNECT BY clause">The Oracle CONNECT BY clause</a>
</li>
<li>
<a href="<?=$root?>/manual/ADVANCED/Export/" title="Exporting to XML, CSV, JSON, HTML, Text">Exporting to XML, CSV, JSON, HTML, Text</a>
</li>
<li>
<a href="<?=$root?>/manual/ADVANCED/Import/" title="Importing data from XML, CSV">Importing data from XML, CSV</a>
</li>
</ol><br><table cellpadding="0" cellspacing="0" border="0" width="100%">
<tr>
<td align="left" valign="top"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a></td><td align="right" valign="top" style="white-space: nowrap"><a href="<?=$root?>/manual/DSL/SQL/" title="Previous section: When it's just easier: Plain SQL">previous</a> : <a href="<?=$root?>/manual/ADVANCED/MasterData/" title="Next section: Master data generation. Enumeration tables">next</a></td>
</tr>
</table>
<?php 
}
?>

