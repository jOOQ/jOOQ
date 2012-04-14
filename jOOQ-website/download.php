<?php
require 'frame.php';
function getH1() {
	return 'Get up and running with jOOQ';
}
function getActiveMenu() {
	return "download";
}
function printContent() {
	global $root;
?>

<h2>Download</h2>
<p>
Get the latest version from SourceForge<br/>
<a href="https://sourceforge.net/projects/jooq/files/Release/" title="Get the latest jOOQ version from SourceForge">https://sourceforge.net/projects/jooq/files/Release/</a>
</p>

<p>
jOOQ is also available as a Maven dependency from Maven central
</p>

<h2>License</h2>
<p>
jOOQ is distributed under the <a href="http://www.apache.org/licenses/LICENSE-2.0" title="Apache 2.0 License">Apache 2.0 licence</a>
</p>

<h2>Get the right version of jOOQ</h2>
<p>
For increased quality, jOOQ uses <a href="http://semver.org/" title="jOOQ uses semantic versioning">semantic versioning</a>.
The jOOQ roadmap plans for:
</p>

<ul>
  <li><code>jooq-X.0.0:</code> major releases about once a year</li>
  <li><code>jooq-X.Y.0:</code> minor releases about once a month</li>
  <li><code>jooq-X.Y.Z:</code>patch releases at will</li>
</ul>

<h3>Release notes</h3>
<p>
Find release notes for currently maintained branches here:
</p>
<ul>
  <li><a href="notes.php" title="Release notes for the latest jOOQ version">The latest version</a></li>
  <li><a href="notes.php?version=2.2" title="Release notes for the 2.2 jOOQ branch">The 2.2 branch</a></li>
  <li><a href="notes.php?version=2.1" title="Release notes for the 2.1 jOOQ branch">The 2.1 branch</a></li>
  <li><a href="notes.php?version=2.0" title="Release notes for the 2.0 jOOQ branch">The 2.0 branch</a></li>
</ul>

<p>
All branches are available here<br/>
<a href="https://sourceforge.net/projects/jooq/files/Release/" title="Get the latest jOOQ version from SourceForge">https://sourceforge.net/projects/jooq/files/Release/</a>
</p>

<h3>Future versions</h3>
<p>
The semi-formal roadmap is here:<br/>
<a href="https://sourceforge.net/apps/trac/jooq/report/6" title="The jOOQ Roadmap">https://sourceforge.net/apps/trac/jooq/report/6</a>
</p>

<h2>Download other products of the jOO* family</h2>
<p>
"jOO*" stands for Java Object Oriented ... It started with jOOQ, an internalised
domain specific language (aka SQL), written in Java. This DSL happens to be a
fluent API, a concept that is on the rise in software engineering. jOO* comes
into play whenever a pre-existing API is too clumsy and no fun to work with. Here
are some other products:

<h3>jOOQ's little sister jOOX</h3>
<div style="height: 100px;">
	<a href="http://code.google.com/p/joox/"
		title="jOOX, a product inspired by jOOQ">
		<img src="<?=$root?>/img/joox-small.png" alt="jOOX" style="float:left; margin-right: 4em; border: 0"/></a>
	<p><a href="http://code.google.com/p/joox/"
		title="jOOX, a product inspired by jOOQ">jOOX</a> stands for Java
	Object Oriented XML. It is a simple wrapper for the org.w3c.dom package,
	to allow for fluent XML document creation and manipulation where DOM is
	required but too verbose. jOOX only wraps the underlying document and
	can be used to enhance DOM, not as an alternative.</p>
</div>

<h3>jOOQ's infant twins jOOR and jOOU</h3>
<div style="height: 100px;">
	<div style="width: 120px; height: 100px; float: left; margin-right: 4em; border: 0">&nbsp;</div>
	<p><a href="http://code.google.com/p/joor/"
		title="jOOR, a product inspired by jOOQ">jOOR</a> stands for Java
	Object Oriented Reflection. It is a simple wrapper for the java.lang.reflect package,
	to allow for fluent reflective access of objects in Java.</p>
	<p><a href="http://code.google.com/p/joou/"
		title="jOOU, a product inspired by jOOQ">jOOU</a> stands for Java
	Object Oriented Unsigned. It is a simple implementation of unsigned integer numbers in Java,
	created out of necessity, as jOOQ supports MySQL's (and other databases') unsigned integers</p>
</div>

<?php
}
?>