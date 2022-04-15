# dna-analyzer

> DNA Analyzer Project

## Requirements
```sh
Java 12
Docker Compose
Plugin Lombok
```

## Install Docker Compose

https://docs.docker.com/compose/install/

## Install lombok for Eclipse

https://projectlombok.org/setup/eclipse

## Install lombok for IntelliJ

https://projectlombok.org/setup/intellij

## Create database container with docker

Development:

```sh
docker-compose -p dnaanalyzer -f docker/docker-compose-development.yml up -d
```

Test:

```sh
docker-compose -p dnaanalyzer_test -f docker/docker-compose-test.yml up -d
```

## Run tests and coverage

To run the tests, you need to create the test container with docker before

```sh
./mvnw clean test jacoco:report
```

## Environment variables

| Name | Description | Default Value | Required |
| -- | -- | -- | -- |
| JWT_SECRET_KEY | Secret for validate signature of JWT to access API | | :white_check_mark: |
| SECURITY_PASSWORD | Password for Basic Authentication to access Actuator Endpoints | | :white_check_mark: |
| DATABASE_URL | URL JDBC to connect to the database | | :white_check_mark: |
| DATABASE_USERNAME | Username to connect to the database | | :white_check_mark: |
| DATABASE_PASSWORD | Password to connect to the database | | :white_check_mark: |

## Running application

When the application executes for the first time, the migrations will be executed through the flyway

```sh
java -jar target/dna-analyzer-X.X.X.jar
```

## Run / clean migrations manually

```sh
./mvnw -Dflyway.url=<url> -Dflyway.user=<user> -Dflyway.password=<password> flyway:migrate

./mvnw -Dflyway.url=<url> -Dflyway.user=<user> -Dflyway.password=<password> flyway:clean
```

## Access Swagger

```sh
http://<host>:<port>/swagger-ui.html
```

## Endpoints to access application

```sh
curl -X GET \
  https://dna-analyzer.herokuapp.com/actuator/health
```

To access protected actuator endpoints, you must enter the Basic Authentication token. Example: 

```sh
curl -X GET \
  https://dna-analyzer.herokuapp.com/actuator/beans \
  -H 'Authorization: Basic ZG5hYW5hbHl6ZXI6c0hZOWc2d3pYZTIyVVR5REhGSTA='
```

To access API endpoints, you must enter the authentication token. Example:

```sh
curl -X POST \
  https://dna-analyzer.herokuapp.com/simian \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.PF45UbS5BOdQf-2ecsgs-eT6EWXwkakQGpeu3xf-QDA' \
  -H 'Content-Type: application/json' \
  -d '{
    "dna": ["AAAA", "CACC", "CCCC", "CTCC"]
}'
```

```sh
curl -X GET \
  https://dna-analyzer.herokuapp.com/stats \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.e30.PF45UbS5BOdQf-2ecsgs-eT6EWXwkakQGpeu3xf-QDA'
```
