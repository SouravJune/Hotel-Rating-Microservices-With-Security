package com.ms.rating.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest implements Serializable {

	@JsonProperty(value = "user_id")
	private String userId;
	
	@JsonProperty(value = "hotel_id")
	private String hotelId;
	
	@JsonProperty(value = "rating")
	private int rating;
	
	@JsonProperty(value = "feedback")
	private String feedback;
}
