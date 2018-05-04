Finch Template
====================

You can use this template to start your project. It contains the basic features to build a nice REST service.

### Requirements
* JDK8 [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html))
* sbt([http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html))

### Implemented Features

* Test coverage with *ScalaTest* and *scoverage* code coverage report
* Config file with optional runtime parameters
* In-Memory MongoDB database for tests
* Logging via *Slf4j* with a xml template

## Configuration
* Start a MongoDB via mlab or locally
* Configure your application.conf (`src/main/resources/`) (application.conf in test has to stay as it is, for running in a in-memory mongoDB instance)

## Run application
To run application, call:
```
sbt run
```
If you wanna restart your application without reloading of sbt, use (*revolver* sbt plugin):
```
sbt re-start
```

## Format
To format the code base
```
sbt fmt
```

## Validate
To validate the project by compiling, testing and generating coverage
```
sbt validate
```