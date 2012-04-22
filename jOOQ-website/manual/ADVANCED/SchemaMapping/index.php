
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Mapping generated schemata and tables";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Sometimes, you cannot control productive schema names, because your
							application is deployed on a shared host, and you only get one schema
							to work with.
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/SchemaMapping/">Mapping generated schemata and tables</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Custom data types and type conversion" href="<?=$root?>/manual/ADVANCED/CustomTypes/">previous</a> : <a title="Next section: Execute listeners and the jOOQ Console" href="<?=$root?>/manual/ADVANCED/ExecuteListener/">next</a></td>
</tr>
</table>
							<h2>Mapping your DEV schema to a productive environment</h2>
							<p>You may wish to design your database in a way that you have several
								instances of your schema. This is useful when you want to cleanly
								separate data belonging to several customers / organisation units /
								branches / users and put each of those entities' data in a separate
								database or schema. </p>
							<p>In our T_AUTHOR example this would mean that you provide a book
								reference database to several companies, such as My Book World and
								Books R Us. In that case, you'll probably have a schema setup like
								this: </p>
							<ul>
								
<li>DEV: Your development schema. This will be the schema that you
									base code generation upon, with jOOQ </li>
								
<li>MY_BOOK_WORLD: The schema instance for My Book World </li>
								
<li>BOOKS_R_US: The schema instance for Books R Us </li>
							
</ul>


							<h2>Mapping DEV to MY_BOOK_WORLD with jOOQ</h2>
							<p>When a user from My Book World logs in, you want them to access the
								MY_BOOK_WORLD schema using classes generated from DEV. This can be
								achieved with the
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/conf/RenderMapping.html" title="Internal API reference: org.jooq.conf.RenderMapping">org.jooq.conf.RenderMapping</a>
								class, that you can equip your Factory's settings
								with. Take the following example: </p>

<pre class="prettyprint lang-java">Settings settings = new Settings()
    .withRenderMapping(new RenderMapping()
    .withSchemata(
        new MappedSchema().withInput("DEV")
                          .withOutput("MY_BOOK_WORLD")));

// Add the settings to the factory
Factory create = new Factory(connection, SQLDialect.ORACLE, settings);

// Run queries with the "mapped" factory
create.selectFrom(T_AUTHOR).fetch();</pre>

							<p>The query executed with a Factory equipped with the above mapping
								will in fact produce this SQL statement: </p>
							<pre class="prettyprint lang-sql">SELECT * FROM MY_BOOK_WORLD.T_AUTHOR</pre>
							<p>Even if T_AUTHOR was generated from DEV. </p>

							<h2>Mapping several schemata</h2>
							<p>Your development database may not be restricted to hold only one DEV
								schema. You may also have a LOG schema and a MASTER schema. Let's say
								the MASTER schema is shared among all customers, but each customer has
								their own LOG schema instance. Then you can enhance your RenderMapping
								like this (e.g. using an XML configuration file): </p>

<pre class="prettyprint lang-xml">&lt;settings xmlns="http://www.jooq.org/xsd/jooq-runtime-2.1.0.xsd"&gt;
  &lt;renderMapping&gt;
    &lt;schemata&gt;
      &lt;schema&gt;
        &lt;input&gt;DEV&lt;/input&gt;
        &lt;output&gt;MY_BOOK_WORLD&lt;/output&gt;
      &lt;/schema&gt;
      &lt;schema&gt;
        &lt;input&gt;LOG&lt;/input&gt;
        &lt;output&gt;MY_BOOK_WORLD_LOG&lt;/output&gt;
      &lt;/schema&gt;
    &lt;/schemata&gt;
  &lt;/renderMapping&gt;
&lt;/settings&gt;</pre>

                            <p>Note, you can load the above XML file like this:</p>

<pre class="prettyprint lang-java">Settings settings = JAXB.unmarshal(new File("jooq-runtime.xml"), Settings.class);</pre>

							<p>This will map generated classes from DEV to MY_BOOK_WORLD, from LOG
								to MY_BOOK_WORLD_LOG, but leave the MASTER schema alone. Whenever you
								want to change your mapping configuration, you will have to create a
								new Factory</p>


							<h2>Using a default schema</h2>
							<p>Another option to switch schema names is to use a default schema for
								the Factory's underlying Connection. Many RDBMS support a USE or SET
								SCHEMA command, which you can call like this: </p>

<pre class="prettyprint lang-java">// Set the default schema
Schema MY_BOOK_WORLD = ...
create.use(MY_BOOK_WORLD);

// Run queries with factory having a default schema
create.selectFrom(T_AUTHOR).fetch();</pre>
							<p>Queries generated from the above Factory will produce this kind of SQL statement: </p>

<pre class="prettyprint lang-sql">-- the schema name is omitted from all SQL constructs.
SELECT * FROM T_AUTHOR</pre>


							<h2>Mapping of tables</h2>
							<p>Not only schemata can be mapped, but also tables. If you are not the
								owner of the database your application connects to, you might need to
								install your schema with some sort of prefix to every table. In our
								examples, this might mean that you will have to map DEV.T_AUTHOR to
								something MY_BOOK_WORLD.MY_APP__T_AUTHOR, where MY_APP__ is a prefix
								applied to all of your tables. This can be achieved by creating the
								following mapping: </p>

<pre class="prettyprint lang-java">Settings settings = new Settings()
    .withRenderMapping(new RenderMapping()
    .withSchemata(
        new MappedSchema().withInput("DEV")
                          .withOutput("MY_BOOK_WORLD")
                          .withTables(
         new MappedTable().withInput("T_AUTHOR")
                          .withOutput("MY_APP__T_AUTHOR"))));

// Add the settings to the factory
Factory create = new Factory(connection, SQLDialect.ORACLE, settings);

// Run queries with the "mapped" factory
create.selectFrom(T_AUTHOR).fetch();</pre>

							<p>The query executed with a Factory equipped with the above mapping will in fact produce this SQL statement: </p>
<pre class="prettyprint lang-sql">SELECT * FROM MY_BOOK_WORLD.MY_APP__T_AUTHOR</pre>

							<h2>Mapping at code generation time</h2>
							<p>
								Note that you can also hard-wire schema mapping in generated artefacts
								at code generation time, e.g. when you have 5 developers with their own
								dedicated developer databases, and a common integration database. In the
								code generation configuration, you would then write.
							</p>
<pre class="prettyprint lang-xml">&lt;schemata&gt;
  &lt;schema&gt;
    &lt;!-- Use this as the developer's schema: --&gt;
    &lt;inputSchema&gt;LUKAS_DEV_SCHEMA&lt;/inputSchema&gt;

    &lt;!-- Use this as the integration / production database: --&gt;
    &lt;outputSchema&gt;PROD&lt;/outputSchema&gt;
  &lt;/schema&gt;
&lt;/schemata&gt;</pre>
							<p>
								See the manual's section about
								<a href="<?=$root?>/manual/META/" title="jOOQ Manual reference: Meta model code generation">jooq-codegen configuration</a>
								for more details
							</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/SchemaMapping/">Mapping generated schemata and tables</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Custom data types and type conversion" href="<?=$root?>/manual/ADVANCED/CustomTypes/">previous</a> : <a title="Next section: Execute listeners and the jOOQ Console" href="<?=$root?>/manual/ADVANCED/ExecuteListener/">next</a></td>
</tr>
</table>
<?php 
}
?>

