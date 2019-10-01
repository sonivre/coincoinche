# coincoinche
Website to play coinche online

For the moment, this repository only contains boilerplate from Spring Boot, as well as a `HelloController`.

`checkstyle` is used for linting. We adhere to the rules defined in the [Google style guide](https://google.github.io/styleguide/javaguide.html).

## Local setup
### Automatically format java files before commit.
To format java files, we use [google-java-format](https://github.com/google/google-java-format).
- Download google-java-format-1.7-all-deps.jar from https://github.com/google/google-java-format/releases
- Move google-java-format-1.7-all-deps.jar at the root of the repository
- Run `cp ./.git/hooks/pre-commit.sample ./.git/hooks/pre-commit`
- Add the following lines to the top of the pre-commit file:

`java -jar google-java-format-1.7-all-deps.jar -i $(git diff --name-only HEAD|grep \.java$)`

`git add $(git diff --name-only HEAD|grep \.java$)`

## Run the tests

### Unit tests

`./mvnw test`

### Integration tests

`./mvnw failsafe:integration-test`

## Build and run the application

### Linux

You can build and run the application by using `./mvnw spring-boot:run`.

Alternatively, you can:
- build the JAR file with `./mvnw clean package`
- then run the JAR file with `java -jar target/coincoinche-0.0.1-SNAPSHOT.jar`

**NB:** if you want to skip style check when building the application, add the `-Dcheckstyle.skip` option.