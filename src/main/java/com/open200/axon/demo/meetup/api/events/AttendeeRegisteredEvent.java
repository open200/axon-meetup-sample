package com.open200.axon.demo.meetup.api.events;

public record AttendeeRegisteredEvent(
        String meetupId,
        String attendee
) {
}
