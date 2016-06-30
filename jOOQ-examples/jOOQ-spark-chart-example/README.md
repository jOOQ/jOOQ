Thanks for downloading jOOQ.
Please visit http://www.jooq.org for more information.

### Description

This example uses

1. [Flyway](https://flywaydb.org/) for database installation
2. [Spark Java](http://sparkjava.com/) as a web server
3. [chart.js](http://www.chartjs.org/) as an HTML5 charting library
4. [jOOQ](http://www.jooq.org) for reporting

The whole thing then looks like this:

![animation](http://i.imgur.com/W8u29Zn.gif)

### Installation

To install and run this example, simply check it out and follow these steps

1. Create a "Sakila" database on your PostgreSQL instance
2. Edit src/main/resources/config.properties and configure your database instance
2. Run the following commands

```
$ pwd
/path/to/checkout/dir
$ cd jOOQ-examples/jOOQ-spark-chart-example
...
$ mvn clean install
...
```
