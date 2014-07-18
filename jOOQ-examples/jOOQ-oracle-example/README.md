Thanks for downloading jOOQ.
Please visit http://www.jooq.org for more information.

### This example doesn't run with the jOOQ Open Source Edition

Because this example is making use of the Oracle SQL dialect, it does not run with the jOOQ Open Source Edition, which does not support Oracle. In order to run this example, please get a commercial or trial distribution directly from: http://www.jooq.org/download.

Before you run this example, you will need to create the following user:

```sql
C:\> sqlplus "/ as sysdba"

Connected to:
Oracle Database 11g Express Edition Release 11.2.0.2.0 - Production

SQL> CREATE USER SP IDENTIFIED BY SP;

User created.

SQL> GRANT ALL PRIVILEGES TO SP;

Grant succeeded;

SQL> GRANT EXECUTE ON DBMS_AQ TO SP;

Grant succeeded;

SQL> GRANT EXECUTE ON DBMS_AQADM TO SP;

```

To install and run this example, please use Maven and Java 8 to install the latest distribution of jOOQ Professional Edition:

```
$ pwd
/path/to/checkout/dir
$ ls
jOOQ jOOQ-meta jOOQ-codegen ...
$ mvn clean install
...
$ cd jOOQ-examples/jOOQ-oracle-example
...
$ mvn clean install
```
