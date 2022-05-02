### What does the example do?

This example shows how to integrate jOOQ in a classic Spring application, showing how to work with:

- Spring's transactions
- Spring's JdbcTemplate
- and more

If you're setting up Spring with Spring Boot, please also have a look at the [jOOQ-spring-boot-example](https://github.com/jOOQ/jOOQ/tree/main/jOOQ-examples/jOOQ-spring-boot-example)

This example was inspired by Petri Kainulainen's excellent blog post:
http://www.petrikainulainen.net/programming/jooq/using-jooq-with-spring-configuration/

### How to run the example?

As all examples in this repository, this assumes you have either installed the latest snapshot version of the jOOQ Open Source Edition, or you patch the `pom.xml` files to use the jOOQ version that you're interested in.

To install and run this example, simply check it out and run the following Maven command

```
$ pwd
/path/to/checkout/dir
$ cd jOOQ-examples/jOOQ-spring-example
...
$ mvn clean install
```
