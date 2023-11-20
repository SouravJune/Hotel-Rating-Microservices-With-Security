package com.ms.user.entity;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ms.user.id_generator.CustomIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "microservice_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @GenericGenerator(
        name = "user_seq", 
        type = com.ms.user.id_generator.CustomIdGenerator.class, 
        parameters = {
        		@Parameter(name = CustomIdGenerator.INCREMENT_PARAM, value = "1")
        		})
	@Column(name = "user_id", updatable = false, nullable = false)
	@JsonProperty(value = "user_id")
    private String id;
	
	@Column(name = "user_name", length = 20)
	@JsonProperty(value = "user_name")
	private String name;
	
	@Column(name = "user_email")
	@JsonProperty(value = "user_email")
	private String email;
	
	@Column(name = "user_about", length = 200)
	@JsonProperty(value = "user_about")
	private String about;
	
}
