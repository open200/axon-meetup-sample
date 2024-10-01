package com.open200.axon.demo.meetup.command;

import com.open200.axon.demo.meetup.api.commands.ChangeMeetupCapacity;
import com.open200.axon.demo.meetup.api.commands.CreateMeetup;
import com.open200.axon.demo.meetup.api.commands.RegisterAttendee;
import com.open200.axon.demo.meetup.api.commands.UnregisterAttendee;
import com.open200.axon.demo.meetup.api.events.AttendeeRegisteredEvent;
import com.open200.axon.demo.meetup.api.events.AttendeeUnregisteredEvent;
import com.open200.axon.demo.meetup.api.events.MeetupCapacityChangedEvent;
import com.open200.axon.demo.meetup.api.events.MeetupCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class MeetupAggregate {
    @AggregateIdentifier
    private String meetupId;

    private int capacity;
    private List<String> attendees;

    public MeetupAggregate(){
        // needed for Axon Framework
    }


    @CreationPolicy(AggregateCreationPolicy.ALWAYS)
    @CommandHandler
    public void when(CreateMeetup cmd) {
        if (cmd.capacity() < 0) {
            throw new IllegalArgumentException("Negative capacity");
        }
        apply(new MeetupCreatedEvent(
                cmd.meetupId(),
                cmd.title(),
                cmd.capacity()
        ));
    }

    @EventSourcingHandler
    public void on(MeetupCreatedEvent evt) {
        this.meetupId = evt.meetupId();
        this.attendees = new ArrayList<>();
        this.capacity = evt.capacity();
    }

    @CommandHandler
    public void when(RegisterAttendee cmd) {
        if (attendees.size() >= capacity) {
            throw new IllegalStateException("Meetup already fully booked");
        }
        if (attendees.contains(cmd.attendee())) {
            throw new IllegalStateException("Attendee already registered!");
        }
        apply(new AttendeeRegisteredEvent(cmd.meetupId(), cmd.attendee()));
    }

    @EventSourcingHandler
    public void on(AttendeeRegisteredEvent evt) {
        attendees.add(evt.attendee());
    }

    @CommandHandler
    public void when(UnregisterAttendee cmd) {
        if (!attendees.contains(cmd.attendee())) {
            throw new IllegalStateException("Attendee not registered!");
        }
        apply(new AttendeeUnregisteredEvent(cmd.meetupId(), cmd.attendee()));
    }

    @EventSourcingHandler
    public void on(AttendeeUnregisteredEvent evt) {
        attendees.remove(evt.attendee());
    }

    @CommandHandler
    public void when(ChangeMeetupCapacity cmd) {
        if (cmd.newCapacity() < 0) {
            throw new IllegalArgumentException("Negative capacity not allowed");
        }
        apply(new MeetupCapacityChangedEvent(
                meetupId,
                cmd.newCapacity()
        ));
        if (attendees.size() > cmd.newCapacity()) {
            var overCapacity = attendees.size() - cmd.newCapacity();
            var attendeesToRemove = attendees.reversed()
                    .subList(0, overCapacity);
            for (String attendee : attendeesToRemove) {
                apply(new AttendeeUnregisteredEvent(meetupId, attendee));
            }
        }
    }

    @EventSourcingHandler
    public void on(MeetupCapacityChangedEvent evt) {
        capacity = evt.capacity();
    }
}
