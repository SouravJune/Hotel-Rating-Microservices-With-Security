package com.ms.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse implements Serializable {

    @JsonProperty(value = "hotel_id")
    private String id;

    @JsonProperty(value = "hotel_name")
    private String name;

    @JsonProperty(value = "hotel_location")
    private String location;

    @JsonProperty(value = "hotel_about")
    private String about;
}
