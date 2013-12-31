This example shows how to combine jOOQ, [Flyway](http://flywaydb.org) database migrations, and the
[H2](http://www.h2database.com) embedded database to code generate during the build. This approach
allows developers to avoid checking generated code into source control and supports Gradle's
incremental building. The process optionally supports schema per project for service isolation. At
application runtime a traditional database can be used, such as Postgres, using the generated jOOQ
models. An alternative mapping library, [ModelMapper](http://modelmapper.org), is used to
demonstrate automatically projecting the record into a complex model.
