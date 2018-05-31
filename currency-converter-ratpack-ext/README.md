# Currency converter using Ratpack

This project is an example application written for http://devsoap.com/making-reactive-micro-services-in-groovy to 
provide an end-point for doing currency conversion. 

In this example the handlers and services are extracted into their own classes to be more testable.

This project uses the following technologies:

* Groovy
* Ratpack
* Flyway
* H2
* Gradle
* Docker

The project can be run by running ``./gradlew -t run `` in the root directory of the project. The project will expose
one endpoint http://localhost:5050/convert/EUR/USD/1.0 where EUR,USD,1.0 are parameters.

The project can be deployed to docker by running ``./gradlew distDocker``. The docker image can then be run by 
running ``docker -p 5050:5050 run``

