
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Advanced configuration of the generator";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "jOOQ power users may want to fine-tune their source code generation settings. Here's how to do this";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/AdvancedConfiguration/">Advanced configuration of the generator</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Configuration and setup of the generator" href="<?=$root?>/manual/META/Configuration/">previous</a> : <a title="Next section: The schema, top-level generated artefact" href="<?=$root?>/manual/META/SCHEMA/">next</a></td>
</tr>
</table>
							<h2>Code generation</h2>
							<p>
								In the <a href="<?=$root?>/manual/META/Configuration/" title="jOOQ Manual reference: Configuration and setup of the generator">previous section</a>
								we have seen how jOOQ's source code generator is configured and
								run within a few steps. In this chapter we'll treat some advanced
								settings
							</p>

<pre class="prettyprint lang-xml">&lt;!-- These properties can be added directly to the generator element: --&gt;
&lt;generator&gt;
  &lt;!-- The default code generator. You can override this one, to generate your own code style
       Defaults to org.jooq.util.DefaultGenerator --&gt;
  &lt;name&gt;org.jooq.util.DefaultGenerator&lt;/name&gt;

  &lt;!-- The naming strategy used for class and field names.
       You may override this with your custom naming strategy. Some examples follow
       Defaults to org.jooq.util.DefaultGeneratorStrategy --&gt;
  &lt;strategy&gt;
    &lt;name&gt;org.jooq.util.DefaultGeneratorStrategy&lt;/name&gt;
  &lt;/strategy&gt;
&lt;/generator&gt;</pre>

							<p>
								The following example shows how you can override the
								DefaultGeneratorStrategy to render table and column names the way
								they are defined in the database, rather than switching them to
								camel case:
							</p>

<pre class="prettyprint lang-java">/**
 * It is recommended that you extend the DefaultGeneratorStrategy. Most of the
 * GeneratorStrategy API is already declared final. You only need to override any
 * of the following methods, for whatever generation behaviour you'd like to achieve
 *
 * Beware that most methods also receive a "Mode" object, to tell you whether a
 * TableDefinition is being rendered as a Table, Record, POJO, etc. Depending on
 * that information, you can add a suffix only for TableRecords, not for Tables
 */
public class AsInDatabaseStrategy extends DefaultGeneratorStrategy {

    /**
     * Override this to specifiy what identifiers in Java should look like.
     * This will just take the identifier as defined in the database.
     */
    @Override
    public String getJavaIdentifier(Definition definition) {
        return definition.getOutputName();
    }

    /**
     * Override these to specify what a setter in Java should look like. Setters
     * are used in TableRecords, UDTRecords, and POJOs. This example will name
     * setters "set[NAME_IN_DATABASE]"
     */
    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        return "set" + definition.getOutputName();
    }

    /**
     * Just like setters...
     */
    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        return "get" + definition.getOutputName();
    }

    /**
     * Override this method to define what a Java method generated from a database
     * Definition should look like. This is used mostly for convenience methods
     * when calling stored procedures and functions. This example shows how to
     * set a prefix to a CamelCase version of your procedure
     */
    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        return "call" + org.jooq.tools.StringUtils.toCamelCase(definition.getOutputName());
    }

    /**
     * Override this method to define how your Java classes and Java files should
     * be named. This example applies no custom setting and uses CamelCase versions
     * instead
     */
    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        return super.getJavaClassName(definition, mode);
    }

    /**
     * Override this method to re-define the package names of your generated
     * artefacts.
     */
    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        return super.getJavaPackageName(definition, mode);
    }

    /**
     * Override this method to define how Java members should be named. This is
     * used for POJOs and method arguments
     */
    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {
        return definition.getOutputName();
    }
}</pre>

                            <h3>jooq-meta configuration</h3>
							<p>
								Within the &lt;generator/&gt; element, there are other configuration elements:
							</p>

<pre class="prettyprint lang-xml">&lt;!-- These properties can be added to the database element: --&gt;
&lt;database&gt;
  &lt;!-- Generate java.sql.Timestamp fields for DATE columns. This is
       particularly useful for Oracle databases.
       Defaults to false --&gt;
  &lt;dateAsTimestamp&gt;false&lt;/dateAsTimestamp&gt;

  &lt;!-- Generate jOOU data types for your unsigned data types, which are
       not natively supported in Java.
       Defaults to true --&gt;
  &lt;unsignedTypes&gt;true&lt;/unsignedTypes&gt;

  &lt;!-- The schema that is used in generated source code. This will be the
       production schema. Use this to override your local development
       schema name for source code generation. If not specified, this
       will be the same as the input-schema. --&gt;
  &lt;outputSchema&gt;[your database schema / owner / name]&lt;/outputSchema&gt;

  &lt;!-- A configuration element to configure several input and/or output
       schemata for jooq-meta, in case you're using jooq-meta in a multi-
       schema environment.
       This cannot be combined with the above inputSchema / outputSchema --&gt;
  &lt;schemata&gt;
    &lt;schema&gt;
      &lt;inputSchema&gt;...&lt;/inputSchema&gt;
      &lt;outputSchema&gt;...&lt;/outputSchema&gt;
    &lt;/schema&gt;
    [ &lt;schema&gt;...&lt;/schema&gt; ... ]
  &lt;/schemata&gt;

  &lt;!-- A configuration element to configure master data table enum classes --&gt;
  &lt;masterDataTables&gt;...&lt;/masterDataTables&gt;

  &lt;!-- A configuration element to configure custom data types --&gt;
  &lt;customTypes&gt;...&lt;/customTypes&gt;

  &lt;!-- A configuration element to configure type overrides for generated
       artefacts (e.g. in combination with customTypes) --&gt;
  &lt;forcedTypes&gt;...&lt;/forcedTypes&gt;
&lt;/database&gt;</pre>

							<p>
								Check out the some of the manual's "advanced" sections
								to find out more about the advanced configuration parameters.
							</p>
							<ul>
								
<li>
<a href="<?=$root?>/manual/ADVANCED/SchemaMapping/" title="jOOQ Manual reference: Mapping generated schemata and tables">Schema mapping</a>
</li>
								
<li>
<a href="<?=$root?>/manual/ADVANCED/MasterData/" title="jOOQ Manual reference: Master data generation. Enumeration tables">Master data types</a>
</li>
								
<li>
<a href="<?=$root?>/manual/ADVANCED/CustomTypes/" title="jOOQ Manual reference: Custom data types and type conversion">Custom types</a>
</li>
							
</ul>

							<h3>jooq-codegen configuration</h3>
                            <p>Also, you can add some optional advanced configuration parameters for the generator: </p>

<pre class="prettyprint lang-xml">&lt;!-- These properties can be added to the generate element: --&gt;
&lt;generate&gt;
  &lt;!-- Primary key / foreign key relations should be generated and used.
       This is a prerequisite for various advanced features.
       Defaults to false --&gt;
  &lt;relations&gt;false&lt;/relations&gt;

  &lt;!-- Generate navigation methods to navigate foreign key relationships
       directly from Record classes. This is only relevant if relations
       is set to true, too.
       Defaults to true --&gt;
  &lt;navigationMethods&gt;true&lt;/navigationMethods&gt;

  &lt;!-- Generate deprecated code for backwards compatibility
       Defaults to true --&gt;
  &lt;deprecated&gt;true&lt;/deprecated&gt;

  &lt;!-- Generate instance fields in your tables, as opposed to static
       fields. This simplifies aliasing.
       Defaults to true --&gt;
  &lt;instanceFields&gt;true&lt;/instanceFields&gt;

  &lt;!-- Generate the javax.annotation.Generated annotation to indicate
       jOOQ version used for source code.
       Defaults to true --&gt;
  &lt;generatedAnnotation&gt;true&lt;/generatedAnnotation&gt;

  &lt;!-- Generate jOOQ Record classes for type-safe querying. You can
       turn this off, if you don't need "active records" for CRUD
       Defaults to true --&gt;
  &lt;records&gt;true&lt;/records&gt;

  &lt;!-- Generate POJOs in addition to Record classes for usage of the
       ResultQuery.fetchInto(Class) API
       Defaults to false --&gt;
  &lt;pojos&gt;false&lt;/pojos&gt;

  &lt;!-- Annotate POJOs and Records with JPA annotations for increased
       compatibility and better integration with JPA/Hibernate, etc
       Defaults to false --&gt;
  &lt;jpaAnnotations&gt;false&lt;/jpaAnnotations&gt;
&lt;/generate&gt;</pre>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/META/">Meta model code generation</a> : <a href="<?=$root?>/manual/META/AdvancedConfiguration/">Advanced configuration of the generator</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Configuration and setup of the generator" href="<?=$root?>/manual/META/Configuration/">previous</a> : <a title="Next section: The schema, top-level generated artefact" href="<?=$root?>/manual/META/SCHEMA/">next</a></td>
</tr>
</table>
<?php 
}
?>

