# gRPC REST API Gateway Example

Current project showcases how to integrate [gRPC-Gateway](https://github.com/grpc-ecosystem/grpc-gateway) with Java
project.
The main difficult is that the gateway is written on Go, and it's not obvious to Java developers how to integrate it
with Java-based gRPC service.

## Important Notice

There are a couple of limitations for the current approach:

- It's a tricky to define REST annotations in the single proto file, as
  described [here](https://github.com/grpc-ecosystem/grpc-gateway#2-with-custom-annotations), because proto compiler for
  Java starts failing with unknown imports and statements. To do that you need to include dependency
  for [googleapis](https://github.com/googleapis/api-common-protos#packages) just for the `api-procotocol` being
  compilable for Java (**NOT TESTED!**):

```xml

<dependencies>
  <dependency>
    <groupId>com.google.api.grpc</groupId>
    <artifactId>proto-google-common-protos</artifactId>
  </dependency>
</dependencies>

```

- I haven't found "a good way" to define openapi (swagger) for the generated REST API.

## Overview

The project uses Maven build tool with the following modules:

- `api-protocol` – simple module with proto files describing the service gRPC API
- `grpc-service` – gRPC Service implementation on Spring Boot stack, uses `api-protocol` as a dependency and implements
  `ExampleService` defined there.
- `rest-gateway` - REST API Gateway module that uses `api-protocol` as a dependency and auto-generates the API using
  underlying gRPC Gateway plugin.

## Implementation Details

- The Service gRPC API is defined in proto files in the `api-protocol` module.
- The Service REST API rules is defined in yaml configuration files in the `rest-gateway` module separately from gRPC
  definitions.
- `api-protocol` is copied during the build into `grpc-service` **target** directory for further generation of Java
  Stubs, (thanks to `maven-dependency-plugin`) and the service implementation.
- `api-protocol` is copied during the build into `rest-service` **buf** directory for further generation of Go Stubs
  and REST API gateway implementation.
- `rest-gateway` Maven module consists of Go module definition, **buf** configuration and gateway server implementation;
  tied to Maven build lifecycle using `maven-exec-plugin`.

## Requirements

- Java 18
- Maven (or use `mvnw`)
- Golang

## Build And Run

To build the project simply run:

``` shell
mvn clean package
```

At the first terminal window start gRPC service (from the project root):

``` shell
java -jar grpc-service/target/grpc-service-0.0.1-SNAPSHOT.jar
```

At the second terminal window start REST gateway (from the project root):

``` shell
./rest-gateway/target/rest-gateway
```

At the third terminal window test REST API is ok:

``` shell
curl -X POST --location "http://localhost:8081/api/v1/example/hello" \
    -H "Accept: application/json" \
    -d "{ \"name\": \"Oleg\" }"
```

... and you should see:

```json
{
  "message": "Hello, Oleg"
}
```
