
<?php 
// The following content has been XSL transformed from manual.xml using html-pages.xsl
// Please do not edit this content manually
require '../../../frame.php';
function getH1() {
    return "Execute listeners and the jOOQ Console";
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
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/ExecuteListener/">Execute listeners and the jOOQ Console</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Mapping generated schemata and tables" href="<?=$root?>/manual/ADVANCED/SchemaMapping/">previous</a> : <a title="Next section: Adding Oracle hints to queries" href="<?=$root?>/manual/ADVANCED/OracleHints/">next</a></td>
</tr>
</table>
							<h2>ExecuteListener</h2>
							<p>
								The <a href="<?=$root?>/manual/JOOQ/Factory/" title="jOOQ Manual reference: The Factory class">jOOQ Factory Settings</a>
								let you specify a list of <a href="http://www.jooq.org/javadoc/latest/org/jooq/ExecuteListener.html" title="Internal API reference: org.jooq.ExecuteListener">org.jooq.ExecuteListener</a> classes.
								The ExecuteListener is essentially an event listener for
								Query, Routine, or ResultSet render, prepare, bind, execute, fetch steps. It is a
								base type for loggers, debuggers, profilers, data collectors. Advanced ExecuteListeners
								can also provide custom implementations of Connection, PreparedStatement and ResultSet
								to jOOQ in apropriate methods. For convenience, consider extending
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/impl/DefaultExecuteListener.html" title="Internal API reference: org.jooq.impl.DefaultExecuteListener">org.jooq.impl.DefaultExecuteListener</a>
								instead of implementing this interface.
							</p>
							<p>
								Here is a sample implementation of an ExecuteListener, that is simply counting
								the number of queries per type that are being executed using jOOQ:
							</p>

<pre class="prettyprint lang-java">package com.example;

public class StatisticsListener extends DefaultExecuteListener {
    public static Map&lt;ExecuteType, Integer&gt; STATISTICS = new HashMap&lt;ExecuteType, Integer&gt;();

    // Count "start" events for every type of query executed by jOOQ
    @Override
    public void start(ExecuteContext ctx) {
        Integer count = STATISTICS.get(ctx.type());

        if (count == null) {
            count = 0;
        }

        STATISTICS.put(ctx.type(), count + 1);
    }
}</pre>

							<p>
								Now, configure jOOQ's runtime to load your listener
							</p>

<pre class="prettyprint lang-xml">&lt;settings&gt;
  &lt;executeListeners&gt;
    &lt;executeListener&gt;com.example.StatisticsListener&lt;/executeListener&gt;
  &lt;/executeListeners&gt;
&lt;/settings&gt;</pre>

							<p>
								And log results any time with a snippet like this:
							</p>

<pre class="prettyprint lang-java">log.info("STATISTICS");
log.info("----------");

for (ExecuteType type : ExecuteType.values()) {
    log.info(type.name(), StatisticsListener.STATISTICS.get(type) + " executions");
}</pre>
                            <p>
                            	This may result in the following log output:
                            </p>

<pre class="prettyprint">15:16:52,982  INFO - TEST STATISTICS
15:16:52,982  INFO - ---------------
15:16:52,983  INFO - READ                     : 919 executions
15:16:52,983  INFO - WRITE                    : 117 executions
15:16:52,983  INFO - DDL                      : 2 executions
15:16:52,983  INFO - BATCH                    : 4 executions
15:16:52,983  INFO - ROUTINE                  : 21 executions
15:16:52,983  INFO - OTHER                    : 30 executions</pre>
							<p>
								Please read the
								<a href="http://www.jooq.org/javadoc/latest/org/jooq/ExecuteListener.html" title="ExecuteListener Javadoc">ExecuteListener Javadoc</a>
								for more details
							</p>

							<h2>jOOQ Console</h2>
							<p>
							    The ExecuteListener API was driven by a feature request by Christopher Deckers, who has
							    had the courtesy to contribute the jOOQ Console, a sample application interfacing
							    with jOOQ's ExecuteListeners. The jOOQ Console logs all queries executed by jOOQ and
							    displays them nicely in a Swing application. With the jOOQ Console's logger, you can:
						    </p>
						    <ul>
						    	
<li>Activate the console's DebugListener anytime (in-process or if the remote server is active).</li>
						    	
<li>View simple and batch queries and their parameters.</li>
						    	
<li>Reformat queries along with syntax highlighting for better readability.</li>
						    	
<li>View stack trace of originator of the call.</li>
						    	
<li>Dump the stack to stdout when in an IDE, to directly navigate to relevant classes.</li>
						    	
<li>Track execution time, binding time, parsing time, rows read, fields read.</li>
						    	
<li>Show/hide queries depending on their type (SELECT, UPDATE, etc.).</li>
						    	
<li>Sort any column (timing columns, queries, types, etc.)</li>
						    	
<li>Easy copy paste of rows/columns to Spreadsheet editors.</li>
						    
</ul>

						    <p>
						    	A short overview of such a debugging session can be seen here:
						    </p>
   							<div class="screenshot">
								
<img alt="jOOQ Console example" class="screenshot" src="<?=$root?>/img/jooq-console-01.png">
							</div>
						    <p>
							    Please note that the jOOQ Console is still experimental.
							    Any feedback is very welcome on
							    <a href="http://groups.google.com/group/jooq-user" title="the jooq-user group">the jooq-user group</a>
							
</p>
							<h2>jOOQ Console operation modes</h2>
							<p>
								The jOOQ Console can be run in two different modes:
							</p>
							<ul>
								
<li>In-process mode: running in the same process as the queries you're analysing</li>
								
<li>"headless" mode: running remotely</li>
							
</ul>

							<p>
								Both modes will require that you set the
								<a href="https://github.com/lukaseder/jOOQ/blob/master/jOOQ-console/src/org/jooq/debug/DebugListener.java" title="Internal API reference: org.jooq.debug.DebugListener">org.jooq.debug.DebugListener</a>
								in the Factory's settings. When using XML settings:
							</p>

<pre class="prettyprint lang-xml">&lt;settings&gt;
  &lt;executeListeners&gt;
    &lt;executeListener&gt;org.jooq.debug.DebugListener&lt;/executeListener&gt;
  &lt;/executeListeners&gt;
&lt;/settings&gt;</pre>

							<p>
								Or when using programmatic settings:
							</p>
<pre class="prettyprint lang-java">Settings settings = new Settings()
    .getExecuteListeners().add("org.jooq.debug.DebugListener");
Factory factory = new Factory(connection, dialect, settings);</pre>

							<h3>In-process mode</h3>
							<p>
								The in-process mode is useful for Swing applications or other,
								locally run Java programs accessing the database via jOOQ.
								In order to launch the jOOQ Console "in-process", specify the
								previously documented settings and launch the Console as follows:
							</p>

<pre class="prettyprint lang-java">// Define a DatabaseDescriptor for the "in-process" mode
// It is needed for the "Editor" tab
DatabaseDescriptor descriptor = new DatabaseDescriptor() {

    // Return your generated schema. This is used by the console
    // to introspect your schema data
    @Override
    public Schema getSchema() {
        return com.example.MySchema.MY_SCHEMA;
    }

    // Return the SQL dialect that you're using
    @Override
    public SQLDialect getSQLDialect() {
        return SQLDialect.ORACLE;
    }

    // Return a connection
    @Override
    public Connection createConnection() {
        try {
            return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "test", "test");
        }
        catch (Exception ignore) {}
    }
};

// Now pass this database descriptor to the Console and make it visible
try {

    // Use this for a nicer look-and-feel
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    // Create a new Console
    Console console = new Console(descriptor, true);
    console.setLoggingActive(true);
    console.setVisible(true);
}
catch (Exception ignore) {}
</pre>

							<p>
								Only in the in-process mode, you can execute ad-hoc queries directly
								from the console, if you provide it with proper DatabaseDescriptor.
								These queries are executed from the Editor pane which features:
							</p>
							<ul>
								
<li>SQL editing within the console.</li>
								
<li>Incremental search on tables.</li>
								
<li>Simple code completion with tables/columns/SQL keywords.</li>
								
<li>Syntax highlighting and formatting capabilities.</li>
								
<li>Results shown in one or several tabs.</li>
								
<li>Easy analysis of Logger output by copy/pasting/running queries in the Editor.</li>
							
</ul>
   							<div class="screenshot">
								
<img alt="jOOQ Console example" class="screenshot" src="<?=$root?>/img/jooq-console-02.png">
							</div>

                            <h3>"Headless" mode</h3>
							<p>
								In J2EE or other server/client environments, you may not be able
								to run the console in the same process as your application. You
								can then run the jOOQ Console in "headless" mode. In addition to
								the previously documented settings, you'll have to start a
								debugger server in your application process, that the console can
								connect to:
							</p>

<pre class="prettyprint lang-java">// Create a new RemoteDebuggerServer in your application that listens to
// incoming connections on a given port
SERVER = new RemoteDebuggerServer(DEBUGGER_PORT);</pre>

							<p>
								Now start your application along with the debugger server
								and launch the console with this command:
							</p>

<pre class="prettyprint">java -jar jooq-console-2.1.0.jar [host] [port]</pre>

							<p>
								Depending on your distribution, you may have to manually add
								rsyntaxtextarea-1.5.0.jar and jOOQ artefacts on your classpath.
							</p>
						<br><table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td valign="top" align="left"><a href="<?=$root?>/manual/">The jOOQ User Manual</a> : <a href="<?=$root?>/manual/ADVANCED/">Advanced topics</a> : <a href="<?=$root?>/manual/ADVANCED/ExecuteListener/">Execute listeners and the jOOQ Console</a></td><td style="white-space: nowrap" valign="top" align="right"><a title="Previous section: Mapping generated schemata and tables" href="<?=$root?>/manual/ADVANCED/SchemaMapping/">previous</a> : <a title="Next section: Adding Oracle hints to queries" href="<?=$root?>/manual/ADVANCED/OracleHints/">next</a></td>
</tr>
</table>
<?php 
}
?>

