<?php 
require 'frame.php';
function printH1() {
	print 'Useful links for jOOQ users and developers';
}
function getSlogan() {
	return "You've come far in your search of more intuitive integration of SQL and Java. 
			Here you will find further resources related to jOOQ, to similar products and
			other inspiration related to jOOQ";
}
function getActiveMenu() {
	return "links";
}
function printContent() {
	global $root;
?>
<h2>jOOQ news groups</h2>
<ul>
	<li>Subscribe to the <a href="http://groups.google.com/group/jooq-user">jOOQ User Group</a></li>
	<li>Subscribe to the <a href="http://groups.google.com/group/jooq-developer">jOOQ Developer Group</a></li>
</ul>

<h2>jOOQ in the WWW</h2>
<ul>
	<li>On Wordpress: <a href="http://lukaseder.wordpress.com/" title="Lukas Eder's blog about Java, SQL and jOOQ">http://lukaseder.wordpress.com/</a></li>
    <li>On SourceForge: <a href="https://sourceforge.net/projects/jooq/" title="jOOQ download and hosting on source forge">https://sourceforge.net/projects/jooq/</a></li>
    <li>On GitHub: <a href="https://github.com/lukaseder/jOOQ" title="fork jOOQ on GitHub">https://github.com/lukaseder/jOOQ</a></li>
    <li>On ohloh: <a href="https://www.ohloh.net/p/jooq" title="see some statistics about jOOQ on ohloh">https://www.ohloh.net/p/jooq</a></li>
    <li>On freshmeat: <a href="http://freshmeat.net/projects/jooq" title="subscribe to some jOOQ announcements on freshmeat">http://freshmeat.net/projects/jooq</a></li>
    <li>On Stack Overflow: <a href="http://stackoverflow.com/questions/tagged/jooq" title="ask questions about jOOQ on Stack Overflow">http://stackoverflow.com/questions/tagged/jooq</a></li>
    <li>On Facebook: <a href="http://www.facebook.com/pages/jOOQ/197164647019108" title="the jOOQ page on Facebook">http://www.facebook.com/pages/jOOQ/197164647019108</a></li> 
</ul>

<h2>jOOQ's source of inspiration, similar products</h2>
<p>When you consider jOOQ for your project, you might also have considered any of these similar products:</p>

<ul>
	<li><a href="http://source.mysema.com/display/querydsl/Querydsl" title="QueryDSL, a product similar to jOOQ">Querydsl</a>: Focusing on the DSL and abstracting "backend integrations", such as SQL, JPA, collections, etc.</li>
    <li><a href="http://www.h2database.com/html/jaqu.html" title="JaQu, a product similar to jOOQ">JaQu</a>: Writing SQL statements using actual Java expressions</li>
    <li><a href="http://iciql.com/" title="iciql, a fork of JaQu, a product similar to jOOQ">iciql</a>: A fork of JaQu</li>
</ul>

<p>And database tools, such as</p>
<ul>
	<li><a href="http://code.google.com/p/activejdbc/" title="QueryDSL, a product similar to jOOQ">ActiveJDBC</a>: A simple mapping tool implementing ActiveRecords in a Ruby-like way</li>
</ul>

<p>And OR-mapping tools, such as</p>
<ul>
	<li><a href="http://www.hibernate.org/" title="QueryDSL, a product similar to jOOQ">Hibernate</a>: The mother of all inspiration to Java persistence</li>
	<li><a href="http://www.oracle.com/technetwork/java/javaee/tech/persistence-jsp-140049.html" title="QueryDSL, a product similar to jOOQ">JPA</a>: The J2EE standard</li>
</ul>

<h2>jOOQ's little sister</h2>
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
<?php 
}
?>