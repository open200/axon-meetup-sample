package com.open200.axon.demo.meetup.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record RegisterAttendee(
        @TargetAggregateIdentifier
        String meetupId,
        String attendee
) {
}
