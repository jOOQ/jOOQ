### What does the example do?

This example shows how to run jOOQ's code generation against a Testcontainers managed PostgreSQL database, which gets its schema migrated using Flyway.

The benefits of this approach are:

- You can integration test your Flyway migration along with your jOOQ code generation and your actual test code.
- You can run the code generation against your production database product, rather than simulating the database product with e.g. H2. This allows for using all vendor specific features available.
- All of these steps are automated and can be used on local dev machines as well as in CI environments.

If you're using Liquibase or something else for database change management, just adapt the plugin configuration.

For more information about this example, see https://blog.jooq.org/using-testcontainers-to-generate-jooq-code/

### How to run the example?

As all examples in this repository, this assumes you have either installed the latest snapshot version of the jOOQ Open Source Edition, or you patch the `pom.xml` files to use the jOOQ version that you're interested in.

To install and run this example, simply check it out and run the following Maven command

```
$ pwd
/path/to/checkout/dir
$ cd jOOQ-examples/jOOQ-testcontainers-flyway-example
$ mvn clean install
```

The example should run right away.