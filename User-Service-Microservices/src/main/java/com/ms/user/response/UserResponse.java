package com.ms.user.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable{

	@JsonProperty(value = "user_id")
    private String id;
	
	@JsonProperty(value = "user_name")
	private String name;
	
	@JsonProperty(value = "user_email")
	private String email;
	
	@JsonProperty(value = "user_about")
	private String about;
	
	@JsonProperty(value = "user_ratings")
	private List<RatingResponse> ratings = new ArrayList<>();
}
