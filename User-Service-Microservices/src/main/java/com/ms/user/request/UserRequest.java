package com.ms.user.request;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest implements Serializable {
	
	@JsonProperty(value = "user_name")
	private String name;
	
	@JsonProperty(value = "user_email")
	private String email;
	
	@JsonProperty(value = "user_about")
	private String about;
	
}
	
