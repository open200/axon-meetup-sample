package com.open200.axon.demo.meetup.api.events;

public record MeetupCapacityChangedEvent(
        String meetupId,
        int capacity
) {
}
