package com.open200.axon.demo.meetup.web;

import com.open200.axon.demo.meetup.api.commands.ChangeMeetupCapacity;
import com.open200.axon.demo.meetup.api.commands.CreateMeetup;
import com.open200.axon.demo.meetup.api.commands.RegisterAttendee;
import com.open200.axon.demo.meetup.api.queries.FindMeetupsWithCapacity;
import com.open200.axon.demo.meetup.api.queries.MeetupWithCapacity;
import com.open200.axon.demo.meetup.web.dto.AttendeeDto;
import com.open200.axon.demo.meetup.web.dto.ChangeCapacityDto;
import com.open200.axon.demo.meetup.web.dto.CreateMeetupDto;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/api/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class MeetupController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public MeetupController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/meetup")
    public void createMeetup(@RequestBody CreateMeetupDto createMeetupDto) {
        commandGateway.sendAndWait(new CreateMeetup(
                createMeetupDto.meetupId(),
                createMeetupDto.title(),
                createMeetupDto.capacity()
        ));
    }

    @PostMapping("/meetup/change-capacity")
    public CompletableFuture<Void> createMeetup(@RequestBody ChangeCapacityDto changeCapacityDto) {
        return commandGateway.send(new ChangeMeetupCapacity(
                changeCapacityDto.meetupId(), changeCapacityDto.capacity()
        ));
    }

    @GetMapping("/meetup" )
    public Flux<MeetupWithCapacity> getMeetups() {
        return Flux.from(queryGateway.streamingQuery(new FindMeetupsWithCapacity(), MeetupWithCapacity.class));
    }

    @PostMapping("/register")
    public void registerAttendee(@RequestBody AttendeeDto attendeeDto) {
        commandGateway.sendAndWait(new RegisterAttendee(
                attendeeDto.meetupId(),
                attendeeDto.attendee()
        ));
    }

}

