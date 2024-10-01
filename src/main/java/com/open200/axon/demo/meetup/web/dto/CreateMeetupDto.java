package com.open200.axon.demo.meetup.web.dto;

public record CreateMeetupDto(
        String meetupId, String title, int capacity
) {
}
