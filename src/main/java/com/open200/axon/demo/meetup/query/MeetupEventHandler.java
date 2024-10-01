package com.open200.axon.demo.meetup.query;

import com.open200.axon.demo.meetup.api.events.AttendeeRegisteredEvent;
import com.open200.axon.demo.meetup.api.events.AttendeeUnregisteredEvent;
import com.open200.axon.demo.meetup.api.events.MeetupCapacityChangedEvent;
import com.open200.axon.demo.meetup.api.events.MeetupCreatedEvent;
import com.open200.axon.demo.meetup.api.queries.FindMeetupsWithCapacity;
import com.open200.axon.demo.meetup.api.queries.MeetupWithCapacity;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@ProcessingGroup("meetup")
public class MeetupEventHandler {

    private static final Logger log = LoggerFactory.getLogger(MeetupEventHandler.class);
    private final Map<String, Meetup> meetups = new HashMap<>();


    @EventHandler
    public void on(MeetupCreatedEvent event) {
        meetups.put(event.meetupId(), new Meetup(
                event.title(),
                event.capacity(),
                new ArrayList<>()
        ));
        log.debug("Meetup created: {}", event);
    }

    @EventHandler
    public void on(AttendeeRegisteredEvent event) {
        var meetup = meetups.get(event.meetupId());
        meetup.attendees().add(event.attendee());
        log.debug("Attendee {} added to meetup {}", event.attendee(), event.meetupId());
    }

    @EventHandler
    public void on(AttendeeUnregisteredEvent event) {
        var meetup = meetups.get(event.meetupId());
        meetup.attendees().remove(event.attendee());
        log.debug("Attendee {} removed from meetup {}", event.attendee(), event.meetupId());
    }

    @EventHandler
    public void on(MeetupCapacityChangedEvent event) {
        var meetup = meetups.get(event.meetupId());
        if (meetup == null) {
            throw new RuntimeException("Meetup not found");
        }
        meetups.put(event.meetupId(), new Meetup(
                meetup.title(),
                event.capacity(),
                meetup.attendees()
        ));
        log.debug("Capacity changed for meetup {} to {}", event.meetupId(), event.capacity());
    }

    @QueryHandler
    public Set<MeetupWithCapacity> handle(FindMeetupsWithCapacity query) {
        return meetups.entrySet().stream().map(entry -> {
            var meetupId = entry.getKey();
            var meetup = entry.getValue();

            return new MeetupWithCapacity(
                    meetupId, meetup.title(), meetup.capacity(), meetup.attendees().size()
            );
        }).collect(Collectors.toSet());
    }

}

record Meetup(
        String title,
        int capacity,
        List<String> attendees
) {}
