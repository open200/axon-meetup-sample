# Build a simple event-driven system with the Axon Framework and Spring Boot

This is an example of an event-driven system built with the Axon Framework and Spring Boot.
The application is a simple Meetup application where you can create a meetup, register participants, and change the meetup capacity.
This application was used as a demonstration for talks and workshops about the Axon Framework.

Since this is an exemplary application, it does not cover all the best practices and patterns that you should follow when building an application for production.


## Requirements
- Java 21
- Docker (for running Axon Server)


## Running the application
1. Start Axon Server by running the following command:
    ```shell
    docker run --rm -p 8024:8024 -p 8124:8124 axoniq/axonserver:2024.1.2-jdk-17-nonroot
    ```
    This starts Axon Server on port 8024 for its administration web view and 8124 for the internal communication.
    When Axon Server is running, you can access the administration web view by navigating to `http://localhost:8024` in your browser.
    You need to initialize the Axon Server by selecting "Start standalone node" and complete the installation.

2. Run the application by executing the following command:
    ```shell
    ./gradlew bootRun
    ```

## Testing the application

There are some sample requests that you can use to test the application:


### Create a new meetup
```shell
curl -X POST --location "http://localhost:8080/api/v1/meetup" \
    -H "Content-Type: application/json" \
    -d '{ "meetupId": "1234", "title": "Java Meetup", "capacity": 100 }'
```

### Register participants to a meetup
```shell
curl -X POST --location "http://localhost:8080/api/v1/register" \
    -H "Content-Type: application/json" \
    -d '{ "meetupId": "1234", "attendee": "attendee1" }'
curl -X POST --location "http://localhost:8080/api/v1/register" \
    -H "Content-Type: application/json" \
    -d '{ "meetupId": "1234", "attendee": "attendee2" }'
curl -X POST --location "http://localhost:8080/api/v1/register" \
    -H "Content-Type: application/json" \
    -d '{ "meetupId": "1234", "attendee": "attendee3" }'
curl -X POST --location "http://localhost:8080/api/v1/register" \
    -H "Content-Type: application/json" \
    -d '{ "meetupId": "1234", "attendee": "attendee4" }'
curl -X POST --location "http://localhost:8080/api/v1/register" \
    -H "Content-Type: application/json" \
    -d '{ "meetupId": "1234", "attendee": "attendee5" }'
curl -X POST --location "http://localhost:8080/api/v1/register" \
    -H "Content-Type: application/json" \
    -d '{ "meetupId": "1234", "attendee": "attendee6" }'
```

### Fetch all meetups
```shell
curl -X GET --location "http://localhost:8080/api/v1/meetup" \
    -H "Content-Type: application/json"
```

### Change the meetup capacity
```shell
curl -X POST --location "http://localhost:8080/api/v1/meetup/change-capacity" \
    -H "Content-Type: application/json" \
    -d '{
          "meetupId": "1234",
          "capacity": 4
        }'
```
