package com.ms.rating.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ms.rating.request.RatingRequest;
import com.ms.rating.response.RatingResponse;
import com.ms.rating.service.RatingService;

@RestController
@RequestMapping("/api/v1/rating")
public class RatingController {

	private final RatingService ratingService;

	public RatingController(RatingService ratingService) {
		super();
		this.ratingService = ratingService;
	}
	
	@PostMapping("/save")
	//@PreAuthorize("hasAuthority('Normal_Users')")
	public ResponseEntity<RatingResponse> saveRating(@RequestBody RatingRequest ratingRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.saveRating(ratingRequest));
	}
	
	@GetMapping("/all")
	//@PreAuthorize("hasAuthority('SCOPE_internal') && hasAuthority('Admin')")
	public ResponseEntity<List<RatingResponse>> getAllRatings() {
		return ResponseEntity.ok().body(ratingService.getAllRatings());
	}
	
	@GetMapping("/getrating-by-userid")
	//@PreAuthorize("hasAuthority('SCOPE_internal')")
	public ResponseEntity<List<RatingResponse>> getRatingByUserId(@RequestParam String userId) {
		return ResponseEntity.ok().body(ratingService.getRatingByUserId(userId));
	}
	
	@GetMapping("/getrating-by-hotelid")
	public ResponseEntity<List<RatingResponse>> getRatingByHotelId(@RequestParam String hotelId) {
		return ResponseEntity.ok().body(ratingService.getRatingByHotelId(hotelId));
	}
	
	@PutMapping("/update")
	public ResponseEntity<RatingResponse> updateRating(
			@RequestParam String userId, 
			@RequestParam String ratingId, 
			@RequestBody RatingRequest ratingRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.updateRating(userId, ratingId, ratingRequest));
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteRating(@RequestParam String userId, @RequestParam String ratingId) {
		return ResponseEntity.ok().body(ratingService.deleteRating(userId, ratingId));
	}

	@DeleteMapping("/delete-rating-by-hotel")
	public ResponseEntity<Void> deleteRatingByHotel(@RequestParam String hotelId, @RequestParam String ratingId) {
		return ResponseEntity.ok().body(ratingService.deleteRatingByHotel(hotelId, ratingId));
	}

}
