<?php
require 'frame.php';
function getH1() {
	return 'Be a part of jOOQ! Become a jOOQer';
}
function getSlogan() {
	return "jOOQ can only be as great as its community. You can make the change happen! jOOQ needs
	        motivated staff, providing new database integrations, website translations, and shouting out to
	        the world about jOOQ. You have a blog? Write about jOOQ in your language!";
}
function getActiveMenu() {
	return "contribute";
}
function printContent() {
?>
<h2>The hall of fame</h2>
<p>jOOQ needs your help to implement all of this and more! Make it to the hall of fame of jOOQ contributors:</p>
<ul>
    <li>Lukas Eder: Project lead</li>
    <li>Espen Stromsnes: DB2, H2, Sybase, and partial HSQLDB support, lots of helpful support</li>
    <li>Sander Plas: jOOQ-codegen-maven, jOOQ-wicket contribution</li>
    <li>Christopher Klewes: Maven integration, good ideas to enhance the code generator</li>
    <li>Vladislav "FractalizeR" Rastrusny: Lots of constructive feedback, especially from the MySQL user perspective.</li>
    <li>Christopher Deckers: jOOQ-Console contribution</li>
    <li>You: ...</li>
</ul>
<p>Thanks to all of the above people!</p>

<h2>Contribution instructions</h2>
<p>You can contribute anything. Ideas, feature requests or bug reports (please use trac, and be explicit). You can also help enlarge the community, by shouting out to the world about jOOQ and how you use it. You have a blog? Write about jOOQ!</p>
<p>Or contribute code! If you want to share code, please follow these instructions</p>
<ul>
	<li>Check out the codebase from sourceforge: <a href="https://sourceforge.net/scm/?type=svn&group_id=283484" title="jOOQ SVN repository">SVN</a>. <a href="https://github.com/lukaseder/jOOQ" title="jOOQ GitHub repository">GitHub</a> is also available but may be incomplete</li>
    <li>Import the five jOOQ projects into <a href="http://www.eclipse.org/" title="jOOQ developers use Eclipse">Eclipse</a></li>
    <li>Use the Eclipse formatting settings that have been checked in with the projects. Keeps the code <a href="http://www.extremeprogramming.org/rules/standards.html">nice and clean</a>. Your code should not change (not much), if formatted with Ctrl-Shift-F. But don't format jOOQ DSL query code!</li>
    <li>Write code that does not need comments. Otherwise write comments where necessary. And NO Javadoc for self-explanatory things (e.g. getters/setters)</li>
    <li>Keep code <a href="http://en.wikipedia.org/wiki/DRY">DRY</a>!</li>
    <li>Add specification (interfaces, API)</li>
    <li>Add unit tests</li>
    <li>Add integration tests</li>
    <li>Add functionality (yes, it's called <a href="http://www.extremeprogramming.org/rules/testfirst.html">test-driven development</a>. You'll love it!)</li>
    <li>Run unit tests and database integration tests. Ideally, you will test your code against all supported databases</li>
    <li>Send me your changes directly to the <a href="http://groups.google.com/group/jooq-user" title="the jOOQ user group">user group</a> or in a <a href="https://sourceforge.net/apps/trac/jooq/newticket">trac ticket</a>.</li>
    <li>Send your changes as an Eclipse-compatible .patch file (or the sum of the modified Java classes). Ideally, the .patch file can be applied to the latest version of jOOQ on the SVN trunk.</li>
    <li>I'll review, audit your changes and it goes into the repository with the next release, if I'm happy :-) AND: you'll make it to the hall of fame. Provide me with links to your webpage / blog / etc, if you would like to see that mentioned.</li>
    <li>If you are a committer on sourceforge, please commit with the following comment scheme: <br/>[#Trac-ID] Trac-Ticket-Title - Your specific comment</li>
</ul>

<h2>Hints for new database integrations</h2>

<p>Should you wish to provide an extension/support for a new RDBMS (such as Informix, Firebird, etc) your work would consist of these steps:</p>
<ul>
    <li>Extend Eclipse .classpath files and .launch targets such that all developers are able to use your newly added database / JDBC drivers</li>
    <li>Implement code generation for the new RDBMS. For that, you'll need to provide an implementation for AbstractDatabase in jOOQ-meta</li>
    <li>Make the integration tests for the new RDBMS run.</li>
</ul>

<h3>Or in more detail:</h3>
<p>In detail, you have to do these steps:</p>
<ul>
    <li>Migrate create.sql and reset.sql scripts to your dialect</li>
    <li>Create the above schema (create.sql) and data (reset.sql) in your test database</li>
    <li>Extend org.jooq.util.AbstractDatabase. Ideally, you'll use your own generated meta-schema classes and the jOOQ API to navigate through your RDBMS's meta-schema in order to discover your test schema. Apart from the integration tests, this adds an additional layer of "proof of concept". See for instance org.jooq.util.oracle.OracleDatabase, where rather complex nested queries are used to navigate through Oracle foreign key relationships.</li>
    <li>For a minimal implementation, check out org.jooq.util.sqlite.SQLiteDatabase, which only implements loadPrimaryKeys0() and getTables0(). All other implementations are optional extensions. </li>
</ul>
<p>Once you've done these steps, in order to be sure that everything works fine you'll also have to do this:</p>
<ul>
    <li>Let jOOQ generate the integration test schema</li>
    <li>Extend the org.jooq.test.jOOQAbstractTest for your RDBMS</li>
    <li>Run the integration tests for your RDBMS</li>
    <li>Fix all issues. You should get syntax errors, SQLDialectNotSupportedExceptions, etc</li>
</ul>
<p>If you provide me with instructions how to set up your RDBMS instance and any partial implementation of the above, I'm going to take that into the jOOQ code base (giving you credit, of course) and guaranteeing implementation integrity in the future.</p>

<p>So let's get to work, then! :-)</p>

<p>Cheers, Lukas</p>
<?php
}
?>