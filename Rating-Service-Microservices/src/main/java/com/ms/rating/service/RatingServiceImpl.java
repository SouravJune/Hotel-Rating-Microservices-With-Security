package com.ms.rating.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ms.rating.entity.Rating;
import com.ms.rating.exception.CannotDeleteException;
import com.ms.rating.exception.CannotUpdateException;
import com.ms.rating.exception.ResourceNotFoundException;
import com.ms.rating.repository.RatingRepository;
import com.ms.rating.request.RatingRequest;
import com.ms.rating.response.RatingResponse;

@Service
public class RatingServiceImpl implements RatingService {
	
	private final RatingRepository ratingRepository;
	
	public RatingServiceImpl(RatingRepository ratingRepository) {
		super();
		this.ratingRepository = ratingRepository;
	}

	@Override
	public RatingResponse saveRating(RatingRequest ratingRequest) {
		
		Rating rating = new Rating();
		rating.setUserId(ratingRequest.getUserId());
		rating.setHotelId(ratingRequest.getHotelId());
		rating.setRating(ratingRequest.getRating());
		rating.setFeedback(ratingRequest.getFeedback());
		
		Rating savedRating = ratingRepository.save(rating);
		
		RatingResponse ratingResponse = new RatingResponse();
		ratingResponse.setRatingId(savedRating.getRatingId());
        ratingResponse.setUserId(savedRating.getUserId());
        ratingResponse.setHotelId(savedRating.getHotelId());
        ratingResponse.setRating(savedRating.getRating());
        ratingResponse.setFeedback(savedRating.getFeedback());
        
        return ratingResponse;
	}

	@Override
	public List<RatingResponse> getAllRatings() {
		
		List<Rating> findAllRatings = ratingRepository.findAll();

        return findAllRatings.stream().map(rate -> {

	        RatingResponse ratingResponse = new RatingResponse();
	        ratingResponse.setRatingId(rate.getRatingId());
	        ratingResponse.setUserId(rate.getUserId());
	        ratingResponse.setHotelId(rate.getHotelId());
	        ratingResponse.setRating(rate.getRating());
	        ratingResponse.setFeedback(rate.getFeedback());

	        return ratingResponse;
	    }).collect(Collectors.toList());
		
	}

	@Override
	public List<RatingResponse> getRatingByUserId(String userId) {
		
		List<Rating> rating = ratingRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found by given user id " + userId));

        return rating.stream().map(rate -> {

	        RatingResponse ratingResponse = new RatingResponse();
	        ratingResponse.setRatingId(rate.getRatingId());
	        ratingResponse.setUserId(rate.getUserId());
	        ratingResponse.setHotelId(rate.getHotelId());
	        ratingResponse.setRating(rate.getRating());
	        ratingResponse.setFeedback(rate.getFeedback());

	        return ratingResponse;
	    }).collect(Collectors.toList());
	}

	@Override
	public List<RatingResponse> getRatingByHotelId(String hotelId) {
		
		List<Rating> rating = ratingRepository.findByHotelId(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Hotel not found by given hotel id " + hotelId));

        return rating.stream().map(rate -> {

	        RatingResponse ratingResponse = new RatingResponse();
	        ratingResponse.setRatingId(rate.getRatingId());
	        ratingResponse.setUserId(rate.getUserId());
	        ratingResponse.setHotelId(rate.getHotelId());
	        ratingResponse.setRating(rate.getRating());
	        ratingResponse.setFeedback(rate.getFeedback());

	        return ratingResponse;
	    }).collect(Collectors.toList());
	}

	@Override
	public RatingResponse updateRating(String userId, String ratingId, RatingRequest ratingRequest) {
		
		Rating getRating = ratingRepository.findRatingByUserIdAndRatingId(userId, ratingId)
				.orElseThrow(() -> new CannotUpdateException("User ID " + userId + " or Rating ID " + ratingId + " mismatched or not found that's why cannot update the rating details"));
		
		if(ratingRequest.getRating() > 0) {
			getRating.setRating(ratingRequest.getRating());
		}

		if(!ratingRequest.getFeedback().isBlank()) {
			getRating.setFeedback(ratingRequest.getFeedback());
		}
		
		Rating savedRating = ratingRepository.save(getRating);
		
		RatingResponse ratingResponse = new RatingResponse();
		ratingResponse.setRatingId(savedRating.getRatingId());
        ratingResponse.setUserId(savedRating.getUserId());
        ratingResponse.setHotelId(savedRating.getHotelId());
        ratingResponse.setRating(savedRating.getRating());
        ratingResponse.setFeedback(savedRating.getFeedback());
        
        return ratingResponse;
	}

	@Override
	public Void deleteRating(String userId, String ratingId) {
		
		ratingRepository.findRatingByUserIdAndRatingId(userId, ratingId)
				.orElseThrow(() -> new CannotDeleteException("User ID " + userId + " or Rating ID " + ratingId + " mismatched or not found that's why cannot detele the rating"));
	
		ratingRepository.deleteById(ratingId);
        return null;
    }

	@Override
	public Void deleteRatingByHotel(String hotelId, String ratingId) {

		ratingRepository.findRatingByHotelIdAndRatingId(hotelId, ratingId)
				.orElseThrow(() -> new CannotDeleteException("User ID " + hotelId + " or Rating ID " + ratingId + " mismatched or not found that's why cannot detele the rating"));

		ratingRepository.deleteById(ratingId);
		return null;
	}

}
