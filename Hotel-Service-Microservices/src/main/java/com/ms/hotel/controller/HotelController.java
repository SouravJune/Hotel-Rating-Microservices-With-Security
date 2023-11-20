package com.ms.hotel.controller;

import java.util.List;

import com.ms.hotel.response.HotelResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ms.hotel.entity.Hotel;
import com.ms.hotel.service.HotelService;


@RestController
@RequestMapping("/api/v1/hotel")
public class HotelController {

	private final HotelService hotelService;

	public HotelController(HotelService hotelService) {
		super();
		this.hotelService = hotelService;
	}
	
	@PostMapping("/save")
	//@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<Hotel> saveHotel(@RequestBody Hotel hotel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.saveHotel(hotel));
	}
	
	@GetMapping("/all")
	//@PreAuthorize("hasAuthority('SCOPE_internal') && hasAuthority('Admin')")
	public ResponseEntity<List<HotelResponse>> getAllHotels() {
		return ResponseEntity.ok().body(hotelService.getAllHotels());
	}

	@GetMapping("/gethotel")
	//@PreAuthorize("hasAuthority('SCOPE_internal')")
	public ResponseEntity<HotelResponse> getHotelById(@RequestParam String hotelId) {
		return ResponseEntity.ok().body(hotelService.getHotel(hotelId));
	}
	
	@PutMapping("/update")
	public ResponseEntity<Hotel> updateHotel(@RequestParam String hotelId, @RequestBody Hotel hotel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.updateHotel(hotelId, hotel));
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteHotel(@RequestParam String hotelId) {
		return ResponseEntity.ok().body(hotelService.deleteHotel(hotelId));
	}
}

