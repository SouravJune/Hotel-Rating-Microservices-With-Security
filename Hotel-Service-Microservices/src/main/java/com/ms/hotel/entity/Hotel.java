package com.ms.hotel.entity;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ms.hotel.id_generator.CustomIdGenerator;

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
@Table(name = "microservice_hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hotel_seq")
    @GenericGenerator(
        name = "hotel_seq", 
        type = com.ms.hotel.id_generator.CustomIdGenerator.class, 
        		parameters = {
        	            @Parameter(name = CustomIdGenerator.INITIAL_PARAM, value = "100"),
        	            @Parameter(name = CustomIdGenerator.INCREMENT_PARAM, value = "1"),
        	            @Parameter(name = CustomIdGenerator.VALUE_PREFIX_PARAMETER, value = "Microservices_Hotel_"),
        	            @Parameter(name = CustomIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
        		})
	@Column(name = "hotel_id")
	@JsonProperty(value = "hotel_id")
    private String id;
	
	@Column(name = "hotel_name", length = 30)
	@JsonProperty(value = "hotel_name")
	private String name;
	
	@Column(name = "hotel_location")
	@JsonProperty(value = "hotel_location")
	private String location;
	
	@Column(name = "hotel_about", length = 200)
	@JsonProperty(value = "hotel_about")
	private String about;
	
}
