
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Serializability of QueryParts and Results";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "Most of the jOOQ API implements the Serializable interface.
							This helps storing queries and partial queries in files, transferring
							queries or result data over TCP/IP, etc. ";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/Serializability/">Serializability of QueryParts and Results</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: QueryParts and the global architecture" href="<?=$root?>/manual/JOOQ/QueryPart/">previous</a> : <a title="Next section: Extend jOOQ with custom types" href="<?=$root?>/manual/JOOQ/Extend/">next</a></td>
</tr>
</table>
							<h2>Attaching QueryParts</h2>
							<p>
								The only transient element in any jOOQ object is the
								<a href="<?=$root?>/manual/JOOQ/Factory/" title="jOOQ Manual reference: The Factory class">The Factory class</a>'s
								underlying
								<a href="http://download.oracle.com/javase/6/docs/api/java/sql/Connection.html" title="External API reference: java.sql.Connection">java.sql.Connection</a>. When you want to execute queries after
								de-serialisation, or when you want to store/refresh/delete
								<a href="<?=$root?>/manual/JOOQ/UpdatableRecord/" title="jOOQ Manual reference: Updatable Records">Updatable Records</a>,
								you will have to "import" or "re-attach" them to a Factory
							</p>
<pre class="prettyprint lang-java">// Deserialise a SELECT statement
ObjectInputStream in = new ObjectInputStream(...);
Select&lt;?&gt; select = (Select&lt;?&gt;) in.readObject();

// This will throw a DetachedException:
select.execute();

// In order to execute the above select, attach it first
Factory create = new Factory(connection, SQLDialect.ORACLE);
create.attach(select);</pre>


							<h2>Automatically attaching QueryParts</h2>
							<p>
								Note, this functionality is deprecated with jOOQ 2.1.0.
								Please use the <a href="<?=$root?>/manual/ADVANCED/ExecuteListener/" title="jOOQ Manual reference: Execute listeners and the jOOQ Console">ExecuteListener</a> API
								instead, to provide jOOQ queries with a
								<a href="http://download.oracle.com/javase/6/docs/api/java/sql/Connection.html" title="External API reference: java.sql.Connection">java.sql.Connection</a> before
								execution.
							</p>
							<p>In simple cases, you can register a ConfigurationProvider in jOOQ's ConfigurationRegistry</p>
<pre class="prettyprint lang-java">// Create your own custom ConfigurationProvider that will make
// your default Factory available to jOOQ
ConfigurationProvider provider = new CustomConfigurationProvider();

// Statically register the provider to jOOQ's ConfigurationRegistry
ConfigurationRegistry.setProvider(provider);</pre>

							<p>Once you have executed these steps, all subsequent deserialisations
								will try to access a Configuration (containing a JDBC Connection) from
								your ConfigurationProvider. This may be useful when </p>
							<ul>
								
<li>transporting jOOQ QueryParts or Records via TCP/IP, RMI, etc (e.g.
									between client and server), before immediately executing queries,
									storing UpdatableRecords</li>
								
<li>
									Using automatic mechanisms as known in
									<a href="http://wicket.apache.org/">Wicket</a>
								
</li>
							
</ul>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/JOOQ/">jOOQ classes and their usage</a> : <a href="<?=$root?>/manual/JOOQ/Serializability/">Serializability of QueryParts and Results</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: QueryParts and the global architecture" href="<?=$root?>/manual/JOOQ/QueryPart/">previous</a> : <a title="Next section: Extend jOOQ with custom types" href="<?=$root?>/manual/JOOQ/Extend/">next</a></td>
</tr>
</table>
<?php 
}
?>

