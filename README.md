ManyWho Sql Service
===================

This service allows you to read and write from a database.
Supported databases MySQL 5.1, PostgreSQL 8.4. and Sql Server 2008


This service is under active development and is not stable.

## Usage

If you need to run your own instance of the service (e.g. for compliance reasons), it's easy to spin up following these
instructions:

#### Testing

You will need a database for the functional test of this service
The string connection by default is "jdbc:postgresql://localhost:5432/service-sql", (host: localhost, port: 5432 and database name service-sql)
the username is "postgres" and the password is "admin"
If you need to test with a different database you can change these parameters at
src\test\java\com\manywho\services\sql\BaseFunctionalTest.java

#### Building

To build the service, you will need to have Maven 3 and a Java 8 implementation installed.

Now you can build the runnable shaded JAR:

```bash
$ mvn clean package
```

#### Running

The service is a Jersey JAX-RS application, that by default is run under the Grizzly2 server on port 8080 (if you use 
the packaged JAR).

##### Defaults

Running the following command will start the service listening on `0.0.0.0:8080/api/sql/1`:

```bash
$ java -jar target/sql-1.0-SNAPSHOT.jar
```

##### Custom Port

You can specify a custom port to run the service on by passing the `server.port` property when running the JAR. The
following command will start the service listening on port 9090 (`0.0.0.0:9090/api/sql/1`):

```bash
$ java -Dserver.port=9090 -jar target/sql-2.0-SNAPSHOT.jar
```

## Contributing

Contribution are welcome to the project - whether they are feature requests, improvements or bug fixes! Refer to 
[CONTRIBUTING.md](CONTRIBUTING.md) for our contribution requirements.

## License

This service is released under the [MIT License](http://opensource.org/licenses/mit-license.php).