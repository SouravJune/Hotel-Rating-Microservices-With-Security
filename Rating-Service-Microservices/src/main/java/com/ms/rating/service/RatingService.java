package com.ms.rating.service;

import java.util.List;

import com.ms.rating.request.RatingRequest;
import com.ms.rating.response.RatingResponse;

public interface RatingService {

	RatingResponse saveRating(RatingRequest ratingRequest);
	List<RatingResponse> getAllRatings();
	List<RatingResponse> getRatingByUserId(String userId);
	List<RatingResponse> getRatingByHotelId(String hotelId);
	RatingResponse updateRating(String userId, String ratingId, RatingRequest ratingRequest);
	Void deleteRating(String userId, String ratingId);
	Void deleteRatingByHotel(String hotelId, String ratingId);
}
