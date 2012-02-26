
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Execute listeners and SQL tracing";
}
function getActiveMenu() {
	return "manual";
}
function getSlogan() {
	return "
							Feel the heart beat of your SQL statements at a very low level using listeners
						";
}
function printContent() {
    global $root;
?>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/ExecuteListener/">Execute listeners and SQL tracing</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Mapping generated schemata and tables" href="<?=$root?>/manual/ADVANCED/SchemaMapping/">previous</a> : <a title="Next section: Adding Oracle hints to queries" href="<?=$root?>/manual/ADVANCED/OracleHints/">next</a></td>
</tr>
</table>
							<h2>ExecuteListener</h2>
							<p>
								The <a href="<?=$root?>/manual/JOOQ/Factory/" title="jOOQ Manual reference: The Factory class">jOOQ Factory Settings</a>
								let you specify a list of <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/ExecuteListener.java" title="Internal API reference: org.jooq.ExecuteListener">org.jooq.ExecuteListener</a> classes.
								The ExecuteListener is essentially an event listener for
								Query, Routine, or ResultSet render, prepare, bind, execute, fetch steps. It is a
								base type for loggers, debuggers, profilers, data collectors. Advanced ExecuteListeners
								can also provide custom implementations of Connection, PreparedStatement and ResultSet
								to jOOQ in apropriate methods. For convenience, consider extending
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/impl/DefaultExecuteListener.java" title="Internal API reference: org.jooq.impl.DefaultExecuteListener">org.jooq.impl.DefaultExecuteListener</a>
								instead of implementing this interface. Please read the
								<a href="www.jooq.org/javadoc/latest/org/jooq/ExecuteListener.html" title="ExecuteListener Javadoc">ExecuteListener Javadoc</a>
								for more details
							</p>

							<h2>jOOQ Console</h2>
							<p>
							    The ExecuteListener API was driven by a feature request by Christopher Deckers, who has
							    had the courtesy to contribute the jOOQ Console, a sample application interfacing
							    with jOOQ's ExecuteListeners. Please note that the jOOQ Console is still experimental.
							    Any feedback is very welcome on
							    <a href="http://groups.google.com/group/jooq-user" title="the jooq-user group">the jooq-user group</a>
							
</p>
							<p>
								Here are the steps you need to do to run the console
							</p>
<pre class="prettyprint lang-java">// Create a new RemoteDebuggerServer in your application that listens to
// incoming connections on a given port
SERVER = new RemoteDebuggerServer(DEBUGGER_PORT);</pre>

							<p>
								And configure the <a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/debug/DebugListener.java" title="Internal API reference: org.jooq.debug.DebugListener">org.jooq.debug.DebugListener</a> in the
								Factory's settings:
							</p>

<pre class="prettyprint lang-xml">&lt;settings&gt;
  &lt;executeListeners&gt;
    &lt;executeListener&gt;org.jooq.debug.DebugListener&lt;/executeListener&gt;
  &lt;/executeListeners&gt;
&lt;/settings&gt;</pre>

							<p>
								Now start your application and the
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ/src/main/java/org/jooq/debug/console/Console.java" title="Internal API reference: org.jooq.debug.console.Console">org.jooq.debug.console.Console</a>, and start profiling!
								Use this command to start the console:
							</p>

<pre class="prettyprint">java -jar jooq-console-2.0.5.jar [host] [port]</pre>

							<div class="screenshot">
							
<img alt="jOOQ Console example" class="screenshot" src="<?=$root?>/img/jooq-console-01.png">
							</div>

							<p>
								The jOOQ Console also has other modes of execution, which will be documented here
								soon.
							</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/ExecuteListener/">Execute listeners and SQL tracing</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Mapping generated schemata and tables" href="<?=$root?>/manual/ADVANCED/SchemaMapping/">previous</a> : <a title="Next section: Adding Oracle hints to queries" href="<?=$root?>/manual/ADVANCED/OracleHints/">next</a></td>
</tr>
</table>
<?php 
}
?>

