package com.open200.axon.demo.meetup.api.events;

public record MeetupCreatedEvent(
        String meetupId,
        String title,
        int capacity
) {
}
