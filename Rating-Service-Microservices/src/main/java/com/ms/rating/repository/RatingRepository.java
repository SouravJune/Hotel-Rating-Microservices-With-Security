package com.ms.rating.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.ms.rating.entity.Rating;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String>{

	Optional<List<Rating>> findByUserId(String userId);
	Optional<List<Rating>> findByHotelId(String hotelId);
	
	@Query("{userId: ?0, ratingId: ?1}")    
	Optional<Rating> findRatingByUserIdAndRatingId(String userId, String ratingId);

	@Query("{hotelId: ?0, ratingId: ?1}")
	Optional<Rating> findRatingByHotelIdAndRatingId(String hotelId, String ratingId);
}
