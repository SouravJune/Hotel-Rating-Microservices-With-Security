package com.ms.rating.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("microservice_ratings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating implements Serializable {

	@Id
	@JsonProperty(value = "rating_id")
	private String ratingId;
	
	@JsonProperty(value = "user_id")
	private String userId;
	
	@JsonProperty(value = "hotel_id")
	private String hotelId;
	
	@JsonProperty(value = "rating")
	private int rating;
	
	@JsonProperty(value = "feedback")
	private String feedback;
}
