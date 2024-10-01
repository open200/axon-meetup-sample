package com.open200.axon.demo.meetup.api.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ChangeMeetupCapacity(
        @TargetAggregateIdentifier
        String meetupId,
        int newCapacity
) {
}
