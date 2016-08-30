FROM maven:alpine

EXPOSE 8080

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

# Install the SQL Server JDBC driver
RUN curl -o /tmp/sqljdbc.tar.gz https://download.microsoft.com/download/0/2/A/02AAE597-3865-456C-AE7F-613F99F850A8/sqljdbc_4.2.6420.100_enu.tar.gz \
	&& tar -xvf /tmp/sqljdbc.tar.gz -C /tmp \
	&& mvn install:install-file -Dfile=/tmp/sqljdbc_4.2/enu/sqljdbc42.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.2 -Dpackaging=jar \
	&& rm -rf /tmp/sqljdbc_4.2 \
	&& rm /tmp/sqljdbc.tar.gz

ADD . /usr/src/app

RUN mvn install

CMD ["java", "-Xmx600m", "-jar", "/usr/src/app/target/sql-1.0-SNAPSHOT.jar"]
