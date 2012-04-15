<?php
require 'frame.php';
function getH1() {
	return 'Contribute to the jOOQ community';
}
function getActiveMenu() {
	return "community";
}
function printContent() {
	global $root;
?>

<h2>jOOQ lives from your feedback and support</h2>
<p>
jOOQ is a very interactive project. It lives from your feedback and support.
There are many ways how you can contribute to the jOOQ community.
</p>

<table width="100%">
<tr>
<td valign="top" width="50%" style="padding-right: 10px">
<h2>The technical community</h2>
<h3>Hall of Fame</h3>
<p>
Passionate contributors have made jOOQ what it is. Here's a list of contributors
worth mentioning (in alphabetical order)
</p>
<ul>
    <li>Christopher Deckers: jOOQ-Console contribution</li>
    <li>Christopher Klewes: Maven integration</li>
    <li>Espen Stromsnes: DB2, H2, Sybase, and HSQLDB integrations</li>
    <li>Florian Adler: jOOQ-minuteproject contribution</li>
    <li>Lukas Eder: Project lead</li>
    <li>Rakesh Waghela: Spreading the news</li>
    <li>Sander Plas: jOOQ-codegen-maven, jOOQ-wicket contribution</li>
    <li>Sergey Epik: jOOQ-spring contribution</li>
    <li>Vladislav "FractalizeR" Rastrusny: MySQL integration feedback</li>
</ul>

<h3>You can contribute too!</h3>
<p>
The easiest way to contribute is by providing feedback. Tell me, tell us, tell the world!
How to do it? Telling us:<br/>
<a href="http://groups.google.com/group/jooq-user">The jOOQ User Group</a><br/><br/>
Telling the world:<br/>
<a href="http://stackoverflow.com/questions/tagged/jooq" title="ask questions about jOOQ on Stack Overflow">http://stackoverflow.com/questions/tagged/jooq</a><br/><br/>
... or blog about jOOQ. Write articles! Advertise jOOQ!
</p>


<h3>Contributing code</h3>
<p>... or contribute code! If you want to share code, please follow these instructions</p>
<ul>
	<li>Check out the codebase from sourceforge: <a href="https://sourceforge.net/scm/?type=svn&group_id=283484" title="jOOQ SVN repository">SVN</a>. <a href="https://github.com/lukaseder/jOOQ" title="jOOQ GitHub repository">GitHub</a> is also available but may be incomplete and not up to date!</li>
    <li>Import the jOOQ projects into <a href="http://www.eclipse.org/" title="jOOQ developers use Eclipse">Eclipse</a></li>
    <li>If you haven't already, get <a href="http://www.eclipse.org/m2e/">M2E</a>, the Eclipse Maven plugin to build jOOQ with Maven in Eclipse</li>
    <li>Use the Eclipse formatting settings that have been checked in with the projects. Keeps the code <a href="http://www.extremeprogramming.org/rules/standards.html">nice and clean</a>. Your code should not change (not much), if formatted with Ctrl-Shift-F. But don't format jOOQ DSL query code!</li>
    <li>Send me your changes directly to the <a href="http://groups.google.com/group/jooq-user" title="the jOOQ user group">user group</a> or in a <a href="https://sourceforge.net/apps/trac/jooq/newticket">trac ticket</a>.</li>
    <li>Send your changes as an Eclipse-compatible .patch file (or the sum of the modified Java classes). Ideally, the .patch file can be applied to the latest version of jOOQ on the SVN trunk.</li>
    <li>I'll review, audit your changes and it goes into the repository with the next release, if I'm happy :-) AND: you'll make it to the hall of fame. Provide me with links to your webpage / blog / etc, if you would like to see that mentioned.</li>
    <li>If you are a committer on sourceforge, please commit with the following comment scheme: <br/>[#Trac-ID] Trac-Ticket-Title - Your specific comment</li>
</ul>

<h3>Hints for new database integrations</h3>

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
</td>

<td valign="top" width="50%" style="padding-left: 10px">
<h2>The business community</h2>
<h3>Invest in jOOQ</h3>
<p>
jOOQ is currently looking for partnerships. If you are a database vendor
or a software vendor and you wish to get basic or advanced support for your
specific database product integrated in jOOQ, or if you have any other specific
feature request, please contact me directly at
<a href="mailto:lukas.eder@gmail.com">lukas.eder@gmail.com</a>.
</p>

<h3>Advertise on www.jooq.org</h3>
<p>
jOOQ has a growing community in dire need for good database products. Make
yourself heard to many database users!
</p>
<pre>
<div id="downloads" style="width: 100%; height: 300px;"></div>
</pre>

<script>
    // See the Google chart API documentation for details:
    // https://google-developers.appspot.com/chart/interactive/docs/quick_start
    // https://google-developers.appspot.com/chart/interactive/docs/gallery/linechart#Configuration_Options
    google.load("visualization", "1", {packages:["corechart"]});
	$.getJSON('<?=$root?>/json/stats.php', function(data) {
	    var rows = new Array();
	    var total = 0;

	    $.each(data.sourceforge.downloads, function() {
	      var date = this[0].substring(0, 7);
	      var maven = Number(data.maven[date]);

	      rows.push([
	        date,
	        this[1],
	        maven
	      ]);

	      if (isNaN(maven)) {
            maven = 0;
          }

	      total += (this[1] + maven);
	    });

        var table = new google.visualization.DataTable();
        table.addColumn('string', 'Date');
        table.addColumn('number', 'SourceForge');
        table.addColumn('number', 'Maven');
        table.addRows(rows);

        var chart = new google.visualization.LineChart(document.getElementById('downloads'));
        chart.draw(table, {
          title: 'Total downloads last year: ' + total,
          titleTextStyle: {
            color: '#ffffff',
            fontSize: 15
          },

          fontSize: 12,
          fontName: 'Georgia',

          backgroundColor: '#333333',
          lineWidth: 4,
          pointSize: 8,
          chartArea: {
            width: '85%',
            height: '80%'
          },
          legend: {
            position: 'bottom',
            textStyle: { color: '#ffffff' }
          },
          colors: ['#678CB1', '#93C763'],
          hAxis: {
            textPosition: 'none'
          },
          vAxis: {
            textStyle: { color: '#ffffff' }
          }
        });
	});
</script>

<h3>Donations</h3>
<p>
If you simply wish to
<a href="https://sourceforge.net/project/project_donations.php?group_id=283484" title="Donate to jOOQ, if you like it!">donate</a>,
that is fine too.
</p>

<h3>The latest news, links</h3>
<p>
Follow jOOQ on any of these channels to get the latest news:
</p>
<ul>
	<li>On Twitter: <a href="https://twitter.com/#!/JavaOOQ" title="jOOQ on Twitter">https://twitter.com/#!/JavaOOQ</a>
	<li>On Wordpress: <a href="http://blog.jooq.org" title="Lukas Eder's blog about Java, SQL and jOOQ">http://blog.jooq.org</a></li>
    <li>On SourceForge: <a href="https://sourceforge.net/projects/jooq/" title="jOOQ download and hosting on source forge">https://sourceforge.net/projects/jooq/</a></li>
    <li>On GitHub: <a href="https://github.com/lukaseder/jOOQ" title="fork jOOQ on GitHub">https://github.com/lukaseder/jOOQ</a></li>
    <li>On ohloh: <a href="https://www.ohloh.net/p/jooq" title="see some statistics about jOOQ on ohloh">https://www.ohloh.net/p/jooq</a></li>
    <li>On freecode: <a href="http://freecode.com/projects/jooq" title="subscribe to some jOOQ announcements on freshmeat">http://freecode.com/projects/jooq</a></li>
</ul>

<h3>The jOOQ Brand</h3>
<p>
Want to publish something about jOOQ? Use the official brand! Right-click on any image and
choose "Save As..." to store the image on your computer.
<a href="mailto:lukas.eder@gmail.com">contact me</a> for more specific formats:
</p>
<h3>Logo (colour and b/w, 250x180):</h3>
<table width="100%">
<tr>
<td valign="top" width="50%">
  <a href="<?=$root?>/img/logo.png">
	<img style="width: 100%; border: 0" src="<?=$root?>/img/logo.png" title="The jOOQ Logo 250x180" alt="The jOOQ Logo 250x180"/>
  </a>
</td>
<td valign="top" width="50%">
  <a href="<?=$root?>/img/logo-bw.png">
	<img style="width: 100%; border: 0" src="<?=$root?>/img/logo-bw.png" title="The jOOQ Logo 250x180" alt="The jOOQ Logo 250x180"/>
  </a>
</td>
</tr>
</table>

<h3>Logo (colour and b/w, 1024x768):</h3>
<table width="100%">
<tr>
<td valign="top" width="50%">
  <a href="<?=$root?>/img/logo-big.png">
	<img style="width: 100%; border: 0" src="<?=$root?>/img/logo-big.png" title="The jOOQ Logo 1024x768" alt="The jOOQ Logo 1024x768"/>
  </a>
</td>
<td valign="top" width="50%">
  <a href="<?=$root?>/img/logo-big-bw.png">
	<img style="width: 100%; border: 0" src="<?=$root?>/img/logo-big-bw.png" title="The jOOQ Logo 1024x768" alt="The jOOQ Logo 1024x768"/>
  </a>
</td>
</tr>
</table>

<h3>Banner (colour and b/w, 1024x186):</h3>
  <a href="<?=$root?>/img/banner-medium.png">
	<img style="width: 100%; border: 0; box-shadow: 5px 5px 20px #AAAAAA;" src="<?=$root?>/img/banner-medium.png" title="The jOOQ Logo 1024x186" alt="The jOOQ Logo 1024x186"/>
  </a>


<h3>Logo (colour and b/w, 220x220):</h3>
<table width="100%">
<tr>
<td valign="top" width="50%">
  <a href="<?=$root?>/img/joox.png">
	<img style="width: 100%; border: 0" src="<?=$root?>/img/joox.png" title="The jOOQ Logo 220x220" alt="The jOOQ Logo 250x180"/>
  </a>
</td>
<td valign="top" width="50%">
  <a href="<?=$root?>/img/joox-bw.png">
	<img style="width: 100%; border: 0" src="<?=$root?>/img/joox-bw.png" title="The jOOQ Logo 220x220" alt="The jOOQ Logo 250x180"/>
  </a>
</td>
</tr>
</table>

<h3>jOOX Logo (colour, 1800x1800):</h3>
<table width="100%">
<tr>
<td valign="top" width="50%">
  <a href="<?=$root?>/img/joox-big.png">
	<img style="width: 100%; border: 0" src="<?=$root?>/img/joox-big.png" title="The jOOQ Logo 1800x1800" alt="The jOOQ Logo 1024x768"/>
  </a>
</td>
<td valign="top" width="50%">
  <a href="<?=$root?>/img/joox-big-bw.png">
	<img style="width: 100%; border: 0" src="<?=$root?>/img/joox-big-bw.png" title="The jOOQ Logo 1800x1800" alt="The jOOQ Logo 1024x768"/>
  </a>
</td>
</tr>
</table>

</td>
</tr>
</table>

<?php
}
?>