Thanks for downloading jOOQ.
Please visit http://www.jooq.org for more information.

To install and run this example, please check out the complete jOOQ repository first, and use Maven to install the latest SNAPSHOT version of jOOQ:

```
$ pwd
/path/to/checkout/dir
$ ls
jOOQ jOOQ-meta jOOQ-codegen ...
$ mvn clean install
...
$ cd jOOQ-examples/jOOQ-javaee-example
...
$ mvn clean install
```

After the above, you should find a `jooq-javaee-example.war` file in

```
$ pwd
/path/to/checkout/dir
$ cd jOOQ-examples/jOOQ-javaee-example/target
...
```

You can deploy this war file in your WildFly AS or any other application server. The example will use an embedded H2 database, which should be pre-filled with the library example H2 database. It uses a non-managed `DataSource`, which is configured and consumed directly by the application itself.

For more information about how to setup a WildFly project using EJB, please visit the WildFly Quickstart projects, e.g.:
https://github.com/wildfly/quickstart/tree/master/ejb-in-war