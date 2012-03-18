
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Configuration and setup of the generator";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "jOOQ uses a simple configuration file to configure source code generation.";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/Configuration/">Configuration and setup of the generator</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Meta model code generation" href="<?=$root?>/manual/META/">previous</a> : <a title="Next section: Advanced configuration of the generator" href="<?=$root?>/manual/META/AdvancedConfiguration/">next</a></td>
</tr>
</table>
							<h2>The deliverables</h2>
							<p>
								There are three binaries available with jOOQ, to be downloaded from
								<a href="https://sourceforge.net/projects/jooq/">SourceForge</a>
								or from Maven central:
							</p>
							<ul>
								
<li>
									
<strong>jOOQ.jar</strong>
									
<br>
									The main library that you will include in your application to run jOOQ
								</li>
								
<li>
									
<strong>jOOQ-meta.jar</strong>
									
<br>
									The utility that you will include in your build to navigate your
									database schema for code generation. This can be used as a schema
									crawler as well.
								</li>
								
<li>
									
<strong>jOOQ-codegen.jar</strong>
									
<br>
									The utility that you will include in your build to generate your
									database schema
								</li>
							
</ul>

							<h2>Dependencies</h2>
							<p>All of jOOQ's dependencies are "optional", i.e. you can run
								jOOQ without any of those libraries.
								For instance, jOOQ maintains an "optional" dependency on log4j and slf4j.
								This means, that jOOQ tries to find log4j (and /log4j.xml) or slf4j on the
								classpath. If they are not present, then java.util.logging.Logger is
								used instead.
							</p>
							<p>
								Other optional dependencies are the JPA API, and the Oracle JDBC driver,
								which is needed for Oracle's advanced data types, only
							</p>


							<h2>Configure jOOQ's code generator</h2>
							<p>You need to tell jOOQ some things about your database connection.
								Here's an example of how to do it for an Oracle database </p>
<pre class="prettyprint lang-xml">&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
&lt;configuration&gt;
  &lt;!-- Configure the database connection here --&gt;
  &lt;jdbc&gt;
    &lt;driver&gt;oracle.jdbc.OracleDriver&lt;/driver&gt;
    &lt;url&gt;jdbc:oracle:thin:@[your jdbc connection parameters]&lt;/url&gt;
    &lt;user&gt;[your database user]&lt;/user&gt;
    &lt;password&gt;[your database password]&lt;/password&gt;
    &lt;!-- You can also pass user/password and other JDBC properties in the optional properties tag: --&gt;
    &lt;properties&gt;
      &lt;property&gt;&lt;key&gt;user&lt;/key&gt;&lt;value&gt;[db-user]&lt;/value&gt;&lt;/property&gt;
      &lt;property&gt;&lt;key&gt;password&lt;/key&gt;&lt;value&gt;[db-password]&lt;/value&gt;&lt;/property&gt;
    &lt;/properties&gt;
  &lt;/jdbc&gt;

  &lt;generator&gt;
    &lt;database&gt;
      &lt;!-- The database dialect from jooq-meta. Available dialects are
           named org.util.[database].[database]Database. Known values are:

           org.jooq.util.ase.ASEDatabase
           org.jooq.util.db2.DB2Database
           org.jooq.util.derby.DerbyDatabase
           org.jooq.util.h2.H2Database
           org.jooq.util.hsqldb.HSQLDBDatabase
           org.jooq.util.ingres.IngresDatabase
           org.jooq.util.mysql.MySQLDatabase
           org.jooq.util.oracle.OracleDatabase
           org.jooq.util.postgres.PostgresDatabase
           org.jooq.util.sqlite.SQLiteDatabaes
           org.jooq.util.sqlserver.SQLServerDatabase
           org.jooq.util.sybase.SybaseDatabase

           You can also provide your own org.jooq.util.Database implementation
           here, if your database is currently not supported --&gt;
      &lt;name&gt;org.jooq.util.oracle.OracleDatabase&lt;/name&gt;

      &lt;!-- All elements that are generated from your schema (several Java
           regular expressions, separated by comma) Watch out for
           case-sensitivity. Depending on your database, this might be
           important! You can create case-insensitive regular expressions
           using this syntax: (?i:expr)A comma-separated list of regular
           expressions --&gt;
      &lt;includes&gt;.*&lt;/includes&gt;

      &lt;!-- All elements that are excluded from your schema (several Java
           regular expressions, separated by comma). Excludes match before
           includes --&gt;
      &lt;excludes&gt;&lt;/excludes&gt;

      &lt;!-- The schema that is used locally as a source for meta information.
           This could be your development schema or the production schema, etc
           This cannot be combined with the schemata element. --&gt;
      &lt;inputSchema&gt;[your database schema / owner / name]&lt;/inputSchema&gt;
    &lt;/database&gt;

    &lt;generate&gt;
      &lt;!-- See advanced configuration properties --&gt;
    &lt;/generate&gt;

    &lt;target&gt;
      &lt;!-- The destination package of your generated classes (within the
           destination directory) --&gt;
      &lt;packageName&gt;[org.jooq.your.packagename]&lt;/packageName&gt;

      &lt;!-- The destination directory of your generated classes --&gt;
      &lt;directory&gt;[/path/to/your/dir]&lt;/directory&gt;
    &lt;/target&gt;
  &lt;/generator&gt;
&lt;/configuration&gt;</pre>

							<p>
								There are also lots of advanced configuration parameters, which will be
								treated in the <a href="<?=$root?>/manual/META/AdvancedConfiguration/" title="jOOQ Manual reference: Advanced configuration of the generator">manual's next section</a>
								Note, you can find the official XSD file at
							   <a href="http://www.jooq.org/xsd/jooq-codegen-2.1.0.xsd" title="The jOOQ-codegen configuration XSD">http://www.jooq.org/xsd/jooq-codegen-2.1.0.xsd</a>
							   for a formal specification</p>

							<h2>Run jOOQ code generation</h2>
							<p>Code generation works by calling this class with the above property file as argument.</p>
							<pre class="prettyprint">org.jooq.util.GenerationTool /jooq-config.xml</pre>
							<p>Be sure that these elements are located on the classpath: </p>
							<ul>
								
<li>The property file</li>
								
<li>jooq.jar, jooq-meta.jar, jooq-codegen.jar</li>
								
<li>The JDBC driver you configured</li>
							
</ul>

							<h3>A command-line example (For Windows, unix/linux/etc will be similar)</h3>
							<ul>
								
<li>Put the property file, jooq*.jar and the JDBC driver into
									a directory, e.g. C:\temp\jooq</li>
							    
<li>Go to C:\temp\jooq</li>
							    
<li>Run java -cp jooq.jar;jooq-meta.jar;jooq-codegen.jar;[JDBC-driver].jar;. org.jooq.util.GenerationTool /[property file] </li>
							
</ul>
							<p>Note that the property file must be passed as a classpath resource</p>

							<h3>Run code generation from Eclipse</h3>
							<p>Of course, you can also run code generation from your IDE. In
								Eclipse, set up a project like this. Note that this example uses
								jOOQ's log4j support by adding log4j.xml and log4j.jar to the project
								classpath: </p>
							<div class="screenshot">
							
<img alt="Eclipse configuration" class="screenshot" src="<?=$root?>/img/eclipse-example-01.png">
							</div>

							<p>Once the project is set up correctly with all required artefacts on
								the classpath, you can configure an Eclipse Run Configuration for
								org.jooq.util.GenerationTool. </p>
							<div class="screenshot">
							
<img alt="Eclipse configuration" class="screenshot" src="<?=$root?>/img/eclipse-example-02.png">
							</div>

							<p>With the properties file as an argument </p>
							<div class="screenshot">
							
<img alt="Eclipse configuration" class="screenshot" src="<?=$root?>/img/eclipse-example-03.png">
							</div>

							<p>And the classpath set up correctly</p>
							<div class="screenshot">
							
<img alt="Eclipse configuration" class="screenshot" src="<?=$root?>/img/eclipse-example-04.png">
							</div>

							<p>Finally, run the code generation and see your generated artefacts</p>
							<div class="screenshot">
							
<img alt="Eclipse configuration" class="screenshot" src="<?=$root?>/img/eclipse-example-05.png">
							</div>

							<h3>Run generation with ant</h3>
							<p>
								You can also use an ant task to generate your classes. As a rule of thumb,
								remove the dots "." and dashes "-" from the .properties file's property names to get the
								ant task's arguments:
							</p>
<pre class="prettyprint lang-xml">&lt;!-- Task definition --&gt;
&lt;taskdef name="generate-classes" classname="org.jooq.util.GenerationTask"&gt;
  &lt;classpath&gt;
    &lt;fileset dir="${path.to.jooq.distribution}"&gt;
      &lt;include name="jOOQ.jar"/&gt;
      &lt;include name="jOOQ-meta.jar"/&gt;
      &lt;include name="jOOQ-codegen.jar"/&gt;
    &lt;/fileset&gt;
    &lt;fileset dir="${path.to.mysql.driver}"&gt;
      &lt;include name="${mysql.driver}.jar"/&gt;
    &lt;/fileset&gt;
  &lt;/classpath&gt;
&lt;/taskdef&gt;

&lt;!-- Run the code generation task --&gt;
&lt;target name="generate-test-classes"&gt;
  &lt;generate-classes
      jdbcurl="jdbc:mysql://localhost/test"
      jdbcuser="root"
      jdbcpassword=""
      generatordatabaseinputschema="test"
      generatortargetpackage="org.jooq.test.generatedclasses"
      generatortargetdirectory="${basedir}/src"/&gt;
&lt;/target&gt;</pre>

							<p>
								Note that when running code generation with ant's &lt;java/&gt; task,
								you may have to set fork="true":
							</p>

<pre class="prettyprint lang-xml">&lt;!-- Run the code generation task --&gt;
&lt;target name="generate-test-classes"&gt;
  &lt;java fork="true" classname="org.jooq.util.GenerationTool"&gt;
    [...]
  &lt;/java&gt;
&lt;/target&gt;
</pre>
							<h3>Integrate generation with Maven</h3>
							<p>Using the official jOOQ-codegen-maven plugin, you can integrate
								source code generation in your Maven build process: </p>

<pre class="prettyprint lang-xml">&lt;plugin&gt;

  &lt;!-- Specify the maven code generator plugin --&gt;
  &lt;groupId&gt;org.jooq&lt;/groupId&gt;
  &lt;artifactId&gt;jooq-codegen-maven&lt;/artifactId&gt;
  &lt;version&gt;1.6.7&lt;/version&gt;

  &lt;!-- The plugin should hook into the generate goal --&gt;
  &lt;executions&gt;
    &lt;execution&gt;
      &lt;goals&gt;
        &lt;goal&gt;generate&lt;/goal&gt;
      &lt;/goals&gt;
    &lt;/execution&gt;
  &lt;/executions&gt;

  &lt;!-- Manage the plugin's dependency. In this example, we'll use a Postgres database --&gt;
  &lt;dependencies&gt;
    &lt;dependency&gt;
      &lt;groupId&gt;postgresql&lt;/groupId&gt;
      &lt;artifactId&gt;postgresql&lt;/artifactId&gt;
      &lt;version&gt;8.4-702.jdbc4&lt;/version&gt;
    &lt;/dependency&gt;
  &lt;/dependencies&gt;

  &lt;!-- Specify the plugin configuration --&gt;
  &lt;configuration&gt;

    &lt;!-- JDBC connection parameters --&gt;
    &lt;jdbc&gt;
      &lt;driver&gt;org.postgresql.Driver&lt;/driver&gt;
      &lt;url&gt;jdbc:postgresql:postgres&lt;/url&gt;
      &lt;user&gt;postgres&lt;/user&gt;
      &lt;password&gt;test&lt;/password&gt;
    &lt;/jdbc&gt;

    &lt;!-- Generator parameters --&gt;
    &lt;generator&gt;
      &lt;name&gt;org.jooq.util.DefaultGenerator&lt;/name&gt;
      &lt;database&gt;
        &lt;name&gt;org.jooq.util.postgres.PostgresDatabase&lt;/name&gt;
        &lt;includes&gt;.*&lt;/includes&gt;
        &lt;excludes&gt;&lt;/excludes&gt;
        &lt;inputSchema&gt;public&lt;/inputSchema&gt;
      &lt;/database&gt;
      &lt;generate&gt;
        &lt;relations&gt;true&lt;/relations&gt;
        &lt;deprecated&gt;false&lt;/deprecated&gt;
      &lt;/generate&gt;
      &lt;target&gt;
        &lt;packageName&gt;org.jooq.util.maven.example&lt;/packageName&gt;
        &lt;directory&gt;target/generated-sources/jooq&lt;/directory&gt;
      &lt;/target&gt;
    &lt;/generator&gt;
  &lt;/configuration&gt;
&lt;/plugin&gt;
</pre>
							<p>See the full example of a pom.xml including the jOOQ-codegen artefact here:
							<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ-codegen-maven-example/pom.xml" title="jOOQ-codegen-maven example pom.xml file">https://github.com/lukaseder/jOOQ/blob/master/jOOQ-codegen-maven-example/pom.xml</a>
</p>

                            <h3>Migrate properties files from jOOQ 1.7, early versions of jOOQ 2.0.x:</h3>
                            <p>
                                Before jOOQ 2.0.4, the code generator was configured using properties files
                                These files are still supported for source code generation, but their syntax
                                won't be maintained any longer. If you wish to migrate to XML, you can
                                migrate the file using this command on the command line
                            </p>
							<pre class="prettyprint">org.jooq.util.GenerationTool /jooq-config.properties migrate</pre>
							<p>
								Using the migrate flag, jOOQ will read the properties file and output
								a corresponding XML file on system out
							</p>

							<h3>Use jOOQ generated classes in your application</h3>
							<p>Be sure, both jOOQ.jar and your generated package (see
								configuration) are located on your classpath. Once this is done, you
								can execute SQL statements with your generated classes.</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/Configuration/">Configuration and setup of the generator</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Meta model code generation" href="<?=$root?>/manual/META/">previous</a> : <a title="Next section: Advanced configuration of the generator" href="<?=$root?>/manual/META/AdvancedConfiguration/">next</a></td>
</tr>
</table>
<?php 
}
?>

