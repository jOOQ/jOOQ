<?php
require 'frame.php';
function getH1() {
	return 'The manual, tutorials, FAQ, references';
}
function getActiveMenu() {
	return "learn";
}
function printContent() {
	global $root;
    global $minorVersion;
	global $version;
?>

<table width="100%">
<tr>
<td valign="top" width="50%">
<h2>Getting started</h2>
<p>
Your simplest entry point is probably to get <a href="tutorial.php">the tutorial</a>
running. It shows how to use jOOQ and its code generator with a simple MySQL database
</p>
<ul>
    <li><a href="tutorial.php#step0">Preparation: Download jOOQ and your SQL driver</a></li>
    <li><a href="tutorial.php#step1">Step 1: Create a SQL database and a table</a></li>
    <li><a href="tutorial.php#step2">Step 2: Generate classes</a></li>
    <li><a href="tutorial.php#step3">Step 3: Write a main class and establish a MySQL connection</a></li>
    <li><a href="tutorial.php#step4">Step 4: Write a query using jOOQ's DSL</a></li>
    <li><a href="tutorial.php#step5">Step 5: Iterate over results</a></li>
    <li><a href="tutorial.php#step6">Step 6: Explore!</a></li>
</ul>
</td>

<td valign="top" width="50%">
<h2>Documentation</h2>
<table width="100%">
<tr>
<th width="80" class="right">Version</th>
<th class="right">Javadoc</th>
<th class="right">Manual</th>
</tr>
<tr>
<td class="right">2.6.0</td>
<td class="right">
	<a style="padding-right: 20px" href="http://www.jooq.org/javadoc/latest/" title="The jOOQ Javadoc">HTML</a>
</td>
<td class="right">
	<a style="padding-right: 20px" href="<?=$root?>/doc/2.6/manual-single-page" title="The jOOQ Manual on one single page">HTML</a>
	<a style="padding-right: 20px" href="<?=$root?>/doc/2.6/manual" title="The jOOQ Manual on multiple pages">HTML (multi-page)</a>
	<a style="padding-right: 20px" href="<?=$root?>/doc/2.6/manual-pdf/jOOQ-manual-2.6.pdf" title="The jOOQ Manual as a PDF">PDF</a>
</td>
</tr>

<tr>
<td class="right">2.5.0</td>
<td class="right">
	<a style="padding-right: 20px" href="http://www.jooq.org/javadoc/2.5.x/" title="The jOOQ Javadoc">HTML</a>
</td>
<td class="right">
	<a style="padding-right: 20px" href="<?=$root?>/doc/2.5/manual-single-page" title="The jOOQ Manual on one single page">HTML</a>
	<a style="padding-right: 20px" href="<?=$root?>/doc/2.5/manual" title="The jOOQ Manual on multiple pages">HTML (multi-page)</a>
	<a style="padding-right: 20px" href="<?=$root?>/doc/2.5/manual-pdf/jOOQ-manual-2.5.pdf" title="The jOOQ Manual as a PDF">PDF</a>
</td>
</tr>

<tr>
<td class="right">2.4.0</td>
<td class="right">
	<a style="padding-right: 20px" href="http://www.jooq.org/javadoc/2.4.x/" title="The jOOQ Javadoc">HTML</a>
</td>
<td class="right">
	<a style="padding-right: 20px" href="<?=$root?>/manual-single-page" title="The jOOQ Manual on one single page">HTML</a>
	<a style="padding-right: 20px" href="<?=$root?>/manual" title="The jOOQ Manual on multiple pages">HTML (multi-page)</a>
	<a style="padding-right: 20px" href="<?=$root?>/manual-pdf/jOOQ-manual.pdf" title="The jOOQ Manual as a PDF">PDF</a>
</ul>
</td>
</tr>

<tr>
<td class="right">2.3.0</td>
<td class="right">
	<a style="padding-right: 20px" href="http://www.jooq.org/javadoc/2.3.x/" title="The jOOQ Javadoc">HTML</a>
</td>
<td class="right">
</ul>
</td>
</tr>

<tr>
<td class="right">2.2.0</td>
<td class="right">
	<a style="padding-right: 20px" href="http://www.jooq.org/javadoc/2.2.x/" title="The jOOQ Javadoc">HTML</a>
</td>
<td class="right">
</ul>
</td>
</tr>

<tr>
<td class="right">2.1.0</td>
<td class="right">
	<a style="padding-right: 20px" href="http://www.jooq.org/javadoc/2.1.x/" title="The jOOQ Javadoc">HTML</a>
</td>
<td class="right">
</ul>
</td>
</tr>

<tr>
<td class="right">2.0.0</td>
<td class="right">
	<a style="padding-right: 20px" href="http://www.jooq.org/javadoc/2.0.x/" title="The jOOQ Javadoc">HTML</a>
</td>
<td class="right">
</ul>
</td>
</tr>
</table>

</td>
</tr>
</table>


<h2 id="FAQ">FAQ</h2>
<p>
Before you go on and read the whole manual, there are a couple of interesting questions that you might want to consider:
</p>

<p>
<b class="mono">Q:</b> When I generate source code from MySQL, can I also use it on another database?<br/>
<b class="mono">A:</b> Yes! The generated Java code will work for all supported databases, not only for the database driver that you used when you generated the code.
</p>

<p>
<b class="mono">Q:</b> When I generate source code from my developer database, can I also use it on production?<br/>
<b class="mono">A:</b> Yes! You can map your schema at code generation time or at run time. <a href="http://www.jooq.org/doc/<?=$minorVersion?>/manual/code-generation/codegen-advanced" title="Schema mapping functionality in jOOQ">See the manual for details.</a>
</p>

<p>
<b class="mono">Q:</b> Can I use jOOQ without code generation?<br/>
<b class="mono">A:</b> Yes! You can define tables, fields, conditions using Strings. <a href="http://www.jooq.org/doc/<?=$minorVersion?>/manual/getting-started/use-cases" title="Plain SQL functionality in jOOQ">See the manual for details.</a>
</p>

<p>
<b class="mono">Q:</b> Can I use jOOQ as a query builder and execute queries with Spring?<br/>
<b class="mono">A:</b> Yes! This has been done by other users and will be documented soon. <a href="http://stackoverflow.com/questions/4474365/jooq-and-spring" title="Stack Overflow question about using jOOQ with Spring">See this Stack Overflow question for details.</a>
</p>

<?php
}
?>