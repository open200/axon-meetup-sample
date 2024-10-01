package com.open200.axon.demo.meetup.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record UnregisterAttendee(
        @TargetAggregateIdentifier
        String meetupId,
        String attendee
) {
}
