cd jooq
call mvn clean %1 %2 %3 %4 %5 -P release
cd ..

cd jooq-meta
call mvn clean %1 %2 %3 %4 %5 -P release
cd ..

cd jooq-codegen
call mvn clean %1 %2 %3 %4 %5 -P release
cd ..

cd jooq-codegen-maven
call mvn clean %1 %2 %3 %4 %5 -P release
cd ..

cd jooq-codegen-maven-example
call mvn clean %1 %2 %3 %4 %5 -P release
cd ..

cd jooq-console
call mvn clean %1 %2 %3 %4 %5 -P release
cd ..
