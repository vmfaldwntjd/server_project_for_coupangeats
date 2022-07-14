package com.example.demo.src.event.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetEventBannerRes {
    private int eventId;
    private String eventImageUrl;
}
