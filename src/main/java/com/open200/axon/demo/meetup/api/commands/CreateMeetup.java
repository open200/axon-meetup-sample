package com.open200.axon.demo.meetup.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreateMeetup(
        @TargetAggregateIdentifier
        String meetupId,
        String title,
        int capacity
) {
}
