package com.open200.axon.demo.meetup.api.queries;

public record MeetupWithCapacity(
        String meetupId,
        String title,
        int capacity,
        int attendeesCount
) {
}
