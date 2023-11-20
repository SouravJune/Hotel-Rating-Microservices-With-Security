package com.ms.hotel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.ms.hotel.calling_service.RatingService;
import com.ms.hotel.response.HotelResponse;
import com.ms.hotel.response.RatingResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ms.hotel.entity.Hotel;
import com.ms.hotel.exception.CannotDeleteException;
import com.ms.hotel.exception.CannotUpdateException;
import com.ms.hotel.exception.ResourceNotFoundException;
import com.ms.hotel.repository.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService {
	
	private final HotelRepository hotelRepository;
	private final RatingService ratingService;

	private static final Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);

	public HotelServiceImpl(HotelRepository hotelRepository, RatingService ratingService) {
		super();
		this.hotelRepository = hotelRepository;
		this.ratingService = ratingService;
	}

	@Override
	public Hotel saveHotel(Hotel hotel) {
		
		return hotelRepository.save(hotel);
	}

	@Override
	@CircuitBreaker(name = "ratingHotelServiceBreaker", fallbackMethod = "ratingHotelServiceFallback")
	@RateLimiter(name = "hotelServiceLimiter", fallbackMethod = "ratingHotelServiceFallback")
	public List<HotelResponse> getAllHotels() {
		
		List<Hotel> findAllHotels = hotelRepository.findAll();
		
		if(findAllHotels.isEmpty()) {
			throw new ResourceNotFoundException("Database does not contain any hotel details");
		}

		return findAllHotels.stream()
				.map(hotel -> {

					List<RatingResponse> ratingResponseList = ratingService.getRatingByHotelId(hotel.getId());

					HotelResponse hotelResponse = new HotelResponse();
					hotelResponse.setId(hotel.getId());
					hotelResponse.setName(hotel.getName());
					hotelResponse.setLocation(hotel.getLocation());
					hotelResponse.setAbout(hotel.getAbout());
					hotelResponse.setRatingResponse(ratingResponseList);
					return hotelResponse;
				})
				.collect(Collectors.toList());
		
	}

	private List<HotelResponse> ratingHotelServiceFallback(Exception exception) {

		if(exception instanceof ResourceNotFoundException) {
			logger.info("Database does not contain any hotel details: " + exception.getMessage());
			throw new ResourceNotFoundException("Database does not contain any hotel details" );
		}

		logger.info("Fallback method is executed because service is down: " + exception.getMessage());

		List<HotelResponse> hotelResponseList = new ArrayList<>();
		HotelResponse hotel = new HotelResponse();
		hotel.setId("0000");
		hotel.setName("Name");
		hotel.setLocation("Location");
		hotel.setAbout("About");
		hotel.setRatingResponse(Lists.newArrayList());
		hotelResponseList.add(hotel);

		return hotelResponseList;

	}

	@Override
	@CircuitBreaker(name = "ratingHotelServiceBreaker", fallbackMethod = "ratingHotelServiceFallback")
	@RateLimiter(name = "hotelServiceLimiter", fallbackMethod = "ratingHotelServiceFallback")
	public HotelResponse getHotel(String hotelId) {
		
	Hotel getHotelById = hotelRepository.findById(hotelId)
				.orElseThrow(() -> new ResourceNotFoundException("Hotel not found by given id " + hotelId));

	List<RatingResponse> ratingResponseList = ratingService.getRatingByHotelId(hotelId);

	HotelResponse hotelResponse = new HotelResponse();
	hotelResponse.setId(getHotelById.getId());
	hotelResponse.setName(getHotelById.getName());
	hotelResponse.setLocation(getHotelById.getLocation());
	hotelResponse.setAbout(getHotelById.getAbout());
	hotelResponse.setRatingResponse(ratingResponseList);

	return hotelResponse;
	}

	private HotelResponse ratingHotelServiceFallback(String hotelId, Exception exception) {

		if(exception instanceof ResourceNotFoundException) {
			logger.info("Hotel not found by given id " + hotelId + " " + exception.getMessage());
			throw new ResourceNotFoundException("Hotel not found by given id " + hotelId);
		}

		logger.info("Fallback method is executed because service is down: " + exception.getMessage());

		HotelResponse hotel = new HotelResponse();
		hotel.setId("0000");
		hotel.setName("Name");
		hotel.setLocation("Location");
		hotel.setAbout("About");
		hotel.setRatingResponse(Lists.newArrayList());

		return hotel;

	}

	@Override
	public Hotel updateHotel(String hotelId, Hotel hotel) {
		
		Hotel getHotel = hotelRepository.findById(hotelId)
				.orElseThrow(() -> new CannotUpdateException("Hotel not found by given id " + hotelId + " That's why cannot update the hotel details"));
		
		if(!hotel.getName().isBlank()) {
			getHotel.setName(hotel.getName());
		}

		if(!hotel.getLocation().isBlank()) {
			getHotel.setLocation(hotel.getLocation());
		}
		
		if(!hotel.getAbout().isBlank()) {
			getHotel.setAbout(hotel.getAbout());
		}
		
		return hotelRepository.save(getHotel);
	}

	@Override
	@CircuitBreaker(name = "ratingHotelServiceBreaker", fallbackMethod = "hotelDeleteServiceFallback")
	public String deleteHotel(String hotelId) {

	    Hotel deletedHotel = hotelRepository.findById(hotelId)
	            .orElseThrow(() -> new CannotDeleteException("Hotel not found by given id " + hotelId + ". Cannot delete the hotel."));

		List<RatingResponse> ratingResponseList = ratingService.getRatingByHotelId(hotelId);

		ratingResponseList.forEach(rating -> {
			ratingService.deleteRating(hotelId, rating.getRatingId());
		});
	    hotelRepository.delete(deletedHotel);

	    return "Hotel along with ratings is deleted successfully by id: " + hotelId;
	}

	private String hotelDeleteServiceFallback(String hotelId, Exception exception) {

		if(exception instanceof CannotDeleteException) {
			logger.info("Hotel not found by given id: " + exception.getMessage());
			throw new CannotDeleteException("Hotel not found by given id " + hotelId + " That's why cannot delete the hotel");
		}

		logger.info("Fallback method is executed because service is down: " + exception.getMessage());
		return "Cannot perform this operation due to some technical problem. Try sometimes later!";

	}

}

