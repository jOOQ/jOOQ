### What does the example do?

This example shows how to integrate jOOQ in a classic Spring application, showing how to work with:

- Spring's transactions
- Spring's JdbcTemplate
- and more

This example was inspired by Petri Kainulainen's excellent blog post:
http://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-configuration/

... and then modified by @thomasdarimont to show how Spring Boot could be used

If you're using a commercial distribution of jOOQ, please see this article on how to override Spring Boot's defaults:
https://blog.jooq.org/how-to-use-jooqs-commercial-distributions-with-spring-boot/

### How to run the example?

As all examples in this repository, this assumes you have either installed the latest snapshot version of the jOOQ Open Source Edition, or you patch the `pom.xml` files to use the jOOQ version that you're interested in.

To install and run this example, please check out the complete jOOQ repository first, and use Maven to install the latest SNAPSHOT version of jOOQ:

```
$ pwd
/path/to/checkout/dir
$ cd jOOQ-examples/jOOQ-spring-boot-example
...
$ mvn clean install
```
