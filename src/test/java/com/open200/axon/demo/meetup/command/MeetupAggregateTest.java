package com.open200.axon.demo.meetup.command;


import com.open200.axon.demo.meetup.api.commands.ChangeMeetupCapacity;
import com.open200.axon.demo.meetup.api.commands.CreateMeetup;
import com.open200.axon.demo.meetup.api.events.AttendeeRegisteredEvent;
import com.open200.axon.demo.meetup.api.events.AttendeeUnregisteredEvent;
import com.open200.axon.demo.meetup.api.events.MeetupCapacityChangedEvent;
import com.open200.axon.demo.meetup.api.events.MeetupCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeetupAggregateTest {

    private AggregateTestFixture<MeetupAggregate> meetupAggregate;

    @BeforeEach
    void setUp() {
        meetupAggregate = new AggregateTestFixture<>(MeetupAggregate.class);
    }

    @Test
    void testCreateMeetup() {
        meetupAggregate.givenNoPriorActivity()
                .when(new CreateMeetup("1234", "Java Meetup", 100))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new MeetupCreatedEvent("1234", "Java Meetup", 100));
    }

    @Test
    void testChangeCapacity() {
        meetupAggregate.given(
                new MeetupCreatedEvent("1234", "Java Meetup", 100),
                new AttendeeRegisteredEvent("1234", "attendee1"),
                new AttendeeRegisteredEvent("1234", "attendee2"),
                new AttendeeRegisteredEvent("1234", "attendee3"),
                new AttendeeRegisteredEvent("1234", "attendee4"),
                new AttendeeRegisteredEvent("1234", "attendee5")
        ).when(new ChangeMeetupCapacity("1234", 4))
                .expectSuccessfulHandlerExecution()
                .expectEvents(
                        new MeetupCapacityChangedEvent("1234", 4),
                        new AttendeeUnregisteredEvent("1234", "attendee5")
                );
    }

}
