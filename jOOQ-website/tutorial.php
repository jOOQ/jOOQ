<?php 
require 'frame.php';
function getH1() {
	echo 'Tutorial: Getting started quickly with jOOQ';
}
function getSlogan() {
	return "Get up and running quickly with jOOQ following these first simple steps.";
}
function getActiveMenu() {
	return "tutorial";
}
function printContent() {
?>
<h2>Introduction</h2>
<p>
Download and run jOOQ in 6 easy steps:
</p>

<ul>
	<li><a href="#step0">Preparation: Download jOOQ and your SQL driver</a></li>
	<li><a href="#step1">Step 1: Create a SQL database and a table</a></li>
	<li><a href="#step2">Step 2: Generate classes</a></li>
	<li><a href="#step3">Step 3: Write a main class and establish a MySQL connection</a></li>
	<li><a href="#step4">Step 4: Write a query using jOOQ's DSL</a></li>
	<li><a href="#step5">Step 5: Iterate over results</a></li>
	<li><a href="#step6">Step 6: Explore!</a></li>
	<li><a href="#FAQ">FAQ</a></li>
</ul>

<h2 id="step0">Preparation: Download jOOQ and your SQL driver</h2>
<p>
If you haven't already downloaded them, download jOOQ:<br/>
<a href="https://sourceforge.net/projects/jooq/files/Release/" target="_blank" title="jOOQ download">https://sourceforge.net/projects/jooq/files/Release/</a>
</p>
<p>
Alternatively, you can create a Maven dependency:
</p>
<pre class="prettyprint lang-xml">
&lt;dependency&gt;
  &lt;groupId&gt;org.jooq&lt;/groupId&gt;
  &lt;!-- artefacts are jooq, jooq-meta, jooq-codegen --&gt;
  &lt;artifactId&gt;jooq&lt;/artifactId&gt;
  &lt;version&gt;2.0.0&lt;/version&gt;
&lt;/dependency&gt;
</pre>
<p>
For this example, we'll be using MySQL. If you haven't already downloaded MySQL Connector/J, download it here:<br/>
<a href="http://dev.mysql.com/downloads/connector/j/" target="_blank" title="MySQL JDBC driver">http://dev.mysql.com/downloads/connector/j/</a>
</p>


<h2 id="step1">Step 1: Create a SQL database and a table</h2>
<p>
We're going to create a database called "guestbook" and a corresponding "posts" table. Connect to MySQL via your command line client and type the following:
</p>
<pre class="prettyprint lang-sql">
CREATE DATABASE guestbook;

CREATE TABLE `posts` (
  `id` bigint(20) NOT NULL,
  `body` varchar(255) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
</pre>

<h2 id="step2">Step 2: Generate classes</h2>
<p>
In this step, we're going to use jOOQ's command line tools to generate classes that 
map to the Posts table we just created. More detailed information about how to
set up the jOOQ code generator can be found here:<br/>
<a href="http://www.jooq.org/manual/META/Configuration/" target="_blank" title="jOOQ manual pages about setting up the code generator">http://www.jooq.org/manual/META/Configuration/</a> 
</p>
<p>
The easiest way to generate a schema is to copy the jOOQ jar files (there should be 3)
and the MySQL Connector jar file to a temporary directory. Then, create a guestbook.properties
that looks like this:
</p>
<pre class="prettyprint lang-properties">
#Configure the database connection here
jdbc.Driver=com.mysql.jdbc.Driver
jdbc.URL=jdbc:mysql://localhost:3306/guestbook
jdbc.User=root
jdbc.Password=

#The default code generator. You can override this one, to generate your own code style
#Defaults to org.jooq.util.DefaultGenerator
generator=org.jooq.util.DefaultGenerator

#The database type. The format here is:
#generator.database=org.util.[database].[database]Database
generator.database=org.jooq.util.mysql.MySQLDatabase

#The database schema (or owner, user, database name) to be generated
generator.database.input-schema=guestbook

#All elements that are generated from your schema (several Java regular expressions, separated by comma)
#Watch out for case-sensitivity. Depending on your database, this might be important!
generator.database.includes=.*

#All elements that are excluded from your schema (several Java regular expressions, separated by comma). Excludes match before includes
generator.database.excludes=

#Primary key / foreign key relations should be generated and used. 
#This will be a prerequisite for various advanced features
#Defaults to false
generator.generate.relations=true

#The destination package of your generated classes (within the destination directory)
generator.target.package=test.generated

#The destination directory of your generated classes
generator.target.directory=C:/workspace/MySQLTest/src
</pre>
<p>
Replace the username with whatever user has the appropriate privileges. 
You'll want to look at the other values and replace as necessary.
Here are the two interesting properties:
</p>
<p>
<code>generator.target.package</code> - set this to the parent package you want
to create for the generated classes. The setting of <code>test.generated</code>
will cause the <code>test.generated.Posts</code> and 
<code>test.generated.PostsRecord</code> to be created
</p>
<p>
<code>generator.target.directory</code> - the directory to output to.
</p>
<p>
Once you have the JAR files and guestbook.properties in your temp directory, type this
(use colons instead of semi-colons on UNIX/Linux systems): 
</p>
<pre>
java -classpath jooq-2.0.0.jar;jooq-meta-2.0.0.jar;jooq-codegen-2.0.0.jar;mysql-connector-java-5.1.18-bin.jar;. org.jooq.util.GenerationTool /guestbook.properties
</pre>
<p>
Note the prefix slash before guestbook.properies.
Even though it's in our working directory, we need to prepend a slash, as it is
loaded from the classpath.
Replace the filenames with your filenames. 
In this example, jOOQ 2.0.0 is being used.
If everything has worked, you should see this in your console output:
</p>
<pre>
Nov 1, 2011 7:25:06 PM org.jooq.impl.JooqLogger info
INFO: Initialising properties  : /guestbook.properties
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Database parameters      
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: ----------------------------------------------------------
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO:   dialect                : MYSQL
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO:   schema                 : guestbook
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO:   target dir             : /Users/jOOQ/Documents/workspace/MySQLTest/src
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO:   target package         : test.generated
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: ----------------------------------------------------------
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Emptying                 : /Users/jOOQ/workspace/MySQLTest/src/test/generated
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Generating classes in    : /Users/jOOQ/workspace/MySQLTest/src/test/generated
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Generating schema        : Guestbook.java
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Generating factory       : GuestbookFactory.java
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Schema generated         : Total: 122.18ms
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Sequences fetched        : 0 (0 included, 0 excluded)
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Masterdata tables fetched: 0 (0 included, 0 excluded)
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Tables fetched           : 5 (5 included, 0 excluded)
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Generating tables        : /Users/jOOQ/workspace/MySQLTest/src/test/generated/tables
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: ARRAYs fetched           : 0 (0 included, 0 excluded)
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Enums fetched            : 0 (0 included, 0 excluded)
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: UDTs fetched             : 0 (0 included, 0 excluded)
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Generating table         : Posts.java
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Tables generated         : Total: 680.464ms, +558.284ms
Nov 1, 2011 7:25:07 PM org.jooq.impl.JooqLogger info
INFO: Generating Keys          : /Users/jOOQ/workspace/MySQLTest/src/test/generated/tables
Nov 1, 2011 7:25:08 PM org.jooq.impl.JooqLogger info
INFO: Keys generated           : Total: 718.621ms, +38.157ms
Nov 1, 2011 7:25:08 PM org.jooq.impl.JooqLogger info
INFO: Generating records       : /Users/jOOQ/workspace/MySQLTest/src/test/generated/tables/records
Nov 1, 2011 7:25:08 PM org.jooq.impl.JooqLogger info
INFO: Generating record        : PostsRecord.java
Nov 1, 2011 7:25:08 PM org.jooq.impl.JooqLogger info
INFO: Table records generated  : Total: 782.545ms, +63.924ms
Nov 1, 2011 7:25:08 PM org.jooq.impl.JooqLogger info
INFO: Routines fetched         : 0 (0 included, 0 excluded)
Nov 1, 2011 7:25:08 PM org.jooq.impl.JooqLogger info
INFO: Packages fetched         : 0 (0 included, 0 excluded)
Nov 1, 2011 7:25:08 PM org.jooq.impl.JooqLogger info
INFO: GENERATION FINISHED!     : Total: 791.688ms, +9.143ms
</pre>



<h2 id="step3">Step 3: Write a main class and establish a MySQL connection</h2>
<p>
Let's just write a vanilla main class in the project containing the generated classes:
</p>
<pre class="prettyprint lang-java">
// For convenience, always static import your generated tables and 
// jOOQ functions to decrease verbosity:
import static test.generated.Tables.*;
import static org.jooq.impl.Factory.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        
        String userName = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/guestbook";
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
</pre>
<p>
This is pretty standard code for establishing a MySQL connection. 
</p>


<h2 id="step4">Step 4: Write a query using jOOQ's DSL</h2>
<p>
Let's add a simple query: 
</p>
<pre class="prettyprint lang-java">
GuestbookFactory create = new GuestbookFactory(conn);
Result&lt;?&gt; result = create.select().from(POSTS).fetch();
</pre>
<p>
First get an instance of <code>GuestbookFactory</code> so we can write a simple 
<code>SELECT</code> query. We pass an instance of the MySQL connection to 
<code>GuestbookFactory</code>. Note that the factory doesn't close the connection. 
We'll have to do that ourselves.
</p>
<p>
We then use jOOQ's DSL to return an instance of Result. We'll be using this result in 
the next step.
</p>


<h2 id="step5">Step 5: Iterate over results</h2>
<p>
After the line where we retrieve the results, let's iterate over the results and 
print out the data:
</p>
<pre class="prettyprint lang-java">
for (Record r : result) {
    Long id = r.getValue(POSTS.ID);
    String title = r.getValue(POSTS.TITLE);
    String description = r.getValue(POSTS.BODY);
   
    System.out.println("ID: " + id + " title: " + title + " desciption: " + description);
}
</pre>
<p>
The full program should now look like this: 
</p>
<pre class="prettyprint lang-java">
package test;

// For convenience, always static import your generated tables and 
// jOOQ functions to decrease verbosity:
import static test.generated.Tables.*;
import static org.jooq.impl.Factory.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jooq.Record;
import org.jooq.Result;

import test.generated.GuestbookFactory;
import test.generated.tables.Posts;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Connection conn = null;
        
        String userName = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/guestbook";
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, userName, password);

            GuestbookFactory create = new GuestbookFactory(conn);
            Result result = create.select().from(POSTS).fetch();
        
            for (Record r : result) {
                Long id = r.getValue(POSTS.ID);
                String title = r.getValue(POSTS.TITLE);
                String description = r.getValue(POSTS.BODY);
            
                System.out.println("ID: " + id + " title: " + title + " desciption: " + description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
</pre>

<h2 id="step6">Step 6: Explore!</h2>
<p>
jOOQ has grown to be a comprehensive SQL library. For more information, please consider the manual:<br/>
<a href="http://www.jooq.org/manual/" title="jOOQ Manual">http://www.jooq.org/manual/</a>
</p>
<p>
... explore the Javadoc:<br/>
<a href="http://www.jooq.org/javadoc/latest/" title="jOOQ Javadoc">http://www.jooq.org/javadoc/latest/</a>
</p>
<p>
... or join the news group:<br/>
<a href="https://groups.google.com/forum/#!forum/jooq-user" title="jOOQ news group">https://groups.google.com/forum/#!forum/jooq-user</a>
</p>
<p>
	This tutorial is the courtesy of Ikai Lan. See the original source here:<br/>
  	<a href="http://ikaisays.com/2011/11/01/getting-started-with-jooq-a-tutorial/" target="_blank" title="Ikai Lan's jOOQ tutorial">http://ikaisays.com/2011/11/01/getting-started-with-jooq-a-tutorial/</a>
</p>

<h2 id="FAQ">FAQ</h2>
<p>
Before you go on and read the whole manual, there are a couple of interesting questions that you might want to consider:
</p>

<p>
<b class="mono">Q:</b> When I generate source code from MySQL, can I also use it on another database?<br/>
<b class="mono">A:</b> Yes! The generated Java code will work for all supported databases, not only for the database driver that you used when you generated the code.
</p>
	
<p>    
<b class="mono">Q:</b> When I generate source code from my developer database, can I also use it on production?<br/>
<b class="mono">A:</b> Yes! You can map your schema at code generation time or at run time. <a href="http://www.jooq.org/manual/ADVANCED/SchemaMapping/" title="Schema mapping functionality in jOOQ">See the manual for details.</a>
</p>

<p>
<b class="mono">Q:</b> Can I use jOOQ without code generation?<br/>
<b class="mono">A:</b> Yes! You can define tables, fields, conditions using Strings. <a href="http://www.jooq.org/manual/DSL/SQL/" title="Plain SQL functionality in jOOQ">See the manual for details.</a>
</p>

<p>
<b class="mono">Q:</b> Can I use jOOQ as a query builder and execute queries with Spring?<br/>
<b class="mono">A:</b> Yes! This has been done by other users and will be documented soon. <a href="http://stackoverflow.com/questions/4474365/jooq-and-spring" title="Stack Overflow question about using jOOQ with Spring">See this Stack Overflow question for details.</a>
</p>

<?php 
}
?>