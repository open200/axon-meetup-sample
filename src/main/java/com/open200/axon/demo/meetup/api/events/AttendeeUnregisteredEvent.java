package com.open200.axon.demo.meetup.api.events;

public record AttendeeUnregisteredEvent(
        String meetupId,
        String attendee
) {
}
