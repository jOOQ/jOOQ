Thanks for downloading jOOQ.
Please visit http://www.jooq.org for more information.

To install and run this example, simply check it out and run the following Maven command

```
$ pwd
/path/to/checkout/dir
$ cd jOOQ-examples/jOOQ-checker-framework-example
...
$ mvn clean install
```

The above runs a build without any checkers or matchers.

In order to use the checker framework (supports only Java 8, currently), run

```
$ mvn clean install -P checker-framework
```

In order to use ErrorProne, run

```
$ mvn clean install -P error-prone
```