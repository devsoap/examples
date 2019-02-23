# Bookstore App Starter for Vaadin Flow using Gradle

This is the bookstore app starter https://vaadin.com/start/lts/java-ui which uses Gradle instead of Maven as a build tool.


## Prerequisites

The project can be imported into the IDE of your choice, with Java 8 installed, as a Gradle project.

## Project Structure

The project consists of the following three modules:

- parent project: common metadata and configuration
- bookstore-starter-flow-ui: main application module, development time
- bookstore-starter-flow-backend: POJO classes and mock services being used in ui

## Workflow

To compile the entire project, run "mvn install" in the parent project.

Other basic workflow steps:

- getting started
- compiling the whole project
  - run `./gradlew build` in parent project
- developing the application
  - edit code in the ui module
  - run `./gradlew jettyRun` in the ui module or in the parent module
  - open http://localhost:8080/my-starter-project-ui
- creating a production mode war
  - Set `vaadin.productionMode=true` in the ui module
  - run `./gradlew war` in the ui module or in the parent module
- running in production mode
  - Set `vaadin.productionMode=true` in the ui module
  - run `./gradlew jettyRun` in the ui module or in the parent module

