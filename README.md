# Kotlin Sudoku Server
- Imported from [this repo](https://github.com/hojongs/kotlin-spring-boot-grpc-example)
## Usage
### Run Local Server
```shell script
./gradlew bootRun
```
- HTTP API (port 8383)
  - `/hello`
- GRPC API (port 6565)
  - `grpcStub.SayHello(HelloReqeuest)`

## Sample Data

|   |   |   |   |   |   |   |   |   |
|---|---|---|---|---|---|---|---|---|
|   | 1 |   |   |   |   | 4 | 3 |   |
| 7 |   |   |   |   |   |   |   |   |
|   |   |   | 2 | 5 | 4 | 9 |   |   |
| 1 | 7 |   |   | 4 |   | 2 |   | 6 |
|   |   |   |   | 9 |   |   |   | 3 |
|   |   | 3 |   |   | 6 |   | 8 |   |
|   |   | 1 | 4 | 7 |   |   | 6 |   |
|   |   |   | 5 | 6 | 8 | 1 | 2 |   |
|   | 9 |   |   |   |   | 3 |   | 4 |

### Generate Proto
```shell script
./gradlew generateProto
```

## Used features

- Kotlin Language
- Gradle build tool
  - Kotlin DSL
- Spring Boot 2
  - Webflux
  - Reactor
- GRPC
  - Protocol Buffer (protobuf)
- JUnit 5
- Mockk
- Kotlintest-assertions
- Ktlint-gradle
