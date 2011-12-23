
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
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/Configuration/">Configuration and setup of the generator</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Meta model code generation" href="<?=$root?>/manual/META/">previous</a> : <a title="Next section: The schema, top-level generated artefact" href="<?=$root?>/manual/META/SCHEMA/">next</a></td>
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
							
							
							<h2>Configure jOOQ</h2>
							<p>You need to tell jOOQ some things about your database connection.
								Here's an example of how to do it for a MySQL database </p>
<pre class="prettyprint">#Configure the database connection here
jdbc.Driver=com.mysql.jdbc.Driver
jdbc.URL=jdbc:mysql://[your jdbc URL]
jdbc.User=[your database user]
jdbc.Password=[your database password]

#The default code generator. You can override this one, to generate your own code style
#Defaults to org.jooq.util.DefaultGenerator
generator=org.jooq.util.DefaultGenerator

#The database type. The format here is:
#generator.database=org.util.[database].[database]Database
generator.database=org.jooq.util.mysql.MySQLDatabase

#The schema that is used locally as a source for meta information. This could be your
#development schema or the production schema, etc:
generator.database.input-schema=[your database schema / owner / name]

#All elements that are generated from your schema (several Java regular expressions, separated by comma)
#Watch out for case-sensitivity. Depending on your database, this might be important!
#You can create case-insensitive regular expressions using this syntax: (?i:expr)
generator.database.includes=.*

#All elements that are excluded from your schema (several Java regular expressions, separated by comma). Excludes match before includes
generator.database.excludes=

#Primary key / foreign key relations should be generated and used. 
#This will be a prerequisite for various advanced features
#Defaults to false
generator.generate.relations=true

#Generate deprecated code for backwards compatibility 
#Defaults to true
generator.generate.deprecated=false

#The destination package of your generated classes (within the destination directory)
generator.target.package=[org.jooq.your.package]

#The destination directory of your generated classes
generator.target.directory=[/path/to/your/dir]</pre>

							<p>And you can add some optional advanced configuration parameters: </p>
<pre class="prettyprint">#The schema that is used in generated source code. This will be the production schema
#Use this to override your local development schema name for source code generation
#If not specified, this will be the same as the input-schema.
generator.database.output-schema=[your database schema / owner / name]

#Generate instance fields in your tables, as opposed to static fields. This simplifies aliasing
#Defaults to true
generator.generate.instance-fields=true

#Generate jOOU data types for your unsigned data types, which are not natively supported in Java
#Defaults to true
generator.generate.unsigned-types=true

#Generate a master data table enum classes (several Java regular expressions, separated by comma)
generator.generate.master-data-tables=[a list of tables]

#For every master data table, specify two special columns
generator.generate.master-data-table-literal.[master data table]=[column used for enum literals]
generator.generate.master-data-table-description.[master data table]=[column used for documentation]</pre>
							<p>Check out the manual's section about 
								<a href="<?=$root?>/manual/ADVANCED/MasterData/" title="jOOQ Manual reference: Master data generation. Enumeration tables">master data</a>
								 to find out more
								about those advanced configuration parameters. </p>
								
							<h2>Run jOOQ code generation</h2>
							<p>Code generation works by calling this class with the above property file as argument.</p>
							<pre class="prettyprint">org.jooq.util.GenerationTool /jooq-config.properties</pre>
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
      &lt;masterDataTables&gt;
        &lt;masterDataTable&gt;
          &lt;name&gt;t_language&lt;/name&gt;
          &lt;literal&gt;cd&lt;/literal&gt;
          &lt;description&gt;description&lt;/description&gt;
        &lt;/masterDataTable&gt;
      &lt;/masterDataTables&gt;
    &lt;/generator&gt;
  &lt;/configuration&gt;
&lt;/plugin&gt;
</pre>
							<p>See the full example of a pom.xml including the jOOQ-codegen artefact here:
							<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ-codegen-maven-example/pom.xml" title="jOOQ-codegen-maven example pom.xml file">https://github.com/lukaseder/jOOQ/blob/master/jOOQ-codegen-maven-example/pom.xml</a>
</p>
									
							<h3>Use jOOQ generated classes in your application</h3>
							<p>Be sure, both jOOQ.jar and your generated package (see
								configuration) are located on your classpath. Once this is done, you
								can execute SQL statements with your generated classes.</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/Configuration/">Configuration and setup of the generator</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Meta model code generation" href="<?=$root?>/manual/META/">previous</a> : <a title="Next section: The schema, top-level generated artefact" href="<?=$root?>/manual/META/SCHEMA/">next</a></td>
</tr>
</table>
<?php 
}
?>

