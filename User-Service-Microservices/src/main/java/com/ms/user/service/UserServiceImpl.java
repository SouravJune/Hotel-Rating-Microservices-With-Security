package com.ms.user.service;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ms.user.calling_service.HotelService;
import com.ms.user.calling_service.RatingService;
import com.ms.user.exception.CustomizedException;
import com.ms.user.response.HotelResponse;
import com.ms.user.response.RatingResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ms.user.entity.User;
import com.ms.user.exception.CannotDeleteException;
import com.ms.user.exception.CannotUpdateException;
import com.ms.user.exception.ResourceNotFoundException;
import com.ms.user.repository.UserRepository;
import com.ms.user.request.UserRequest;
import com.ms.user.response.UserResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final RestTemplate restTemplate;
	private final RatingService ratingService;
	private final HotelService hotelService;

	public UserServiceImpl(UserRepository userRepository, RestTemplate restTemplate,
						   RatingService ratingService, HotelService hotelService) {
		super();
		this.userRepository = userRepository;
		this.restTemplate = restTemplate;
		this.ratingService = ratingService;
		this.hotelService = hotelService;
	}

	private final String RATING_SERVICE_BASE_URL = "http://RATING-SERVICE-MS/api/v1/rating"; //"http://localhost:8084/api/v1/rating";
	private final String HOTEL_SERVICE_BASE_URL = "http://HOTEL-SERVICE-MS/api/v1/hotel"; //"http://localhost:8082/api/v1/hotel";
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserResponse saveUser(UserRequest userRequest) {
		
		User user = new User();
		user.setName(userRequest.getName());
		user.setEmail(userRequest.getEmail());
		user.setAbout(userRequest.getAbout());
		
		User savedUser = userRepository.save(user);
		UserResponse userResponse = new UserResponse();
		
		userResponse.setId(savedUser.getId());
		userResponse.setName(savedUser.getName());
		userResponse.setEmail(savedUser.getEmail());
		userResponse.setAbout(savedUser.getAbout());
		
		return userResponse;
	}

	@Override
	public List<UserResponse> getAllUsers() {

		List<User> findAllUsers = userRepository.findAll();
		
		if(findAllUsers.isEmpty()) {
			throw new ResourceNotFoundException("Database does not contain any user details");
		}

        return findAllUsers.stream()
				.map(user -> {
					UserResponse userResponse = new UserResponse();
					userResponse.setId(user.getId());
					userResponse.setName(user.getName());
					userResponse.setEmail(user.getEmail());
					userResponse.setAbout(user.getAbout());
						return userResponse;
				})
				.collect(Collectors.toList());
	}

	@Override
	@CircuitBreaker(name = "allRatingHotelServiceBreaker", fallbackMethod = "allRatingHotelServiceFallback")
	//@Retry(name = "allRatingHotelServiceBreaker", fallbackMethod = "allRatingHotelServiceFallback")
	@RateLimiter(name = "allUserServiceLimiter", fallbackMethod = "ratingHotelServiceFallback")
	public List<UserResponse> getAllUsersWithRatings() {

		List<User> findAllUsers = userRepository.findAll();

		if(findAllUsers.isEmpty()) {
			throw new ResourceNotFoundException("Database does not contain any user details");
		}

		return findAllUsers.stream()
				.map(user -> {

					List<RatingResponse> ratingResponseList = ratingService.getRatingByUserId(user.getId());
					List<RatingResponse> ratingDetailsList = ratingResponseList.stream().peek(rating -> {
						HotelResponse hotelResponse = hotelService.getHotelById(rating.getHotelId());
						rating.setHotelResponse(hotelResponse);
					}).collect(Collectors.toList());

					UserResponse userResponse = new UserResponse();
					userResponse.setId(user.getId());
					userResponse.setName(user.getName());
					userResponse.setEmail(user.getEmail());
					userResponse.setAbout(user.getAbout());
					userResponse.setRatings(ratingDetailsList);
					return userResponse;
				})
				.collect(Collectors.toList());
	}

	public List<UserResponse> allRatingHotelServiceFallback(Exception exception) {

		if(exception instanceof ResourceNotFoundException) {
			logger.info("Database does not contain any user details: " + exception.getMessage());
			throw new ResourceNotFoundException("Database does not contain any user details" );
		}

		logger.info("Fallback method is executed because service is down: " + exception.getMessage());

		List<UserResponse> userResponseList = new ArrayList<>();
		UserResponse user = new UserResponse();
		user.setId("0000");
		user.setName("Dummy");
		user.setEmail("dummy@gmail.com");
		user.setAbout("This user is a dummy user and created because service is down");
		user.setRatings(Lists.newArrayList());
		userResponseList.add(user);

		return userResponseList;

	}

	@Override
	public UserResponse getUser(String userId) {

	User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found by given id " + userId));

	UserResponse userResponse = new UserResponse();
	
	userResponse.setId(user.getId());
	userResponse.setName(user.getName());
	userResponse.setEmail(user.getEmail());
	userResponse.setAbout(user.getAbout());
	
	return userResponse;
	
	}

	@Override
	@CircuitBreaker(name = "ratingHotelServiceBreaker", fallbackMethod = "ratingHotelServiceFallback")
	//@Retry(name = "ratingHotelServiceBreaker", fallbackMethod = "ratingHotelServiceFallback")
	@RateLimiter(name = "userServiceLimiter", fallbackMethod = "ratingHotelServiceFallback")
	public UserResponse getUserByIdWithRatings(String userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found by given id " + userId));

		/* To get the rating details for a particular user */
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(RATING_SERVICE_BASE_URL)
				.path("/getrating-by-userid")
				.queryParam("userId", userId);

		List<RatingResponse> ratingResponseList;

		try {
			ratingResponseList = Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(
					builder.toUriString(),
					RatingResponse[].class
			)));
		} catch (Exception e) {
			logger.error("ERROR: " + e.getMessage());
			throw new CustomizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Getting error in rating service");
		}

		/* To get hotel details using a hotel_id using the rating details */
		List<RatingResponse> ratingDetailsList = ratingResponseList.stream().map(rating -> {
			UriComponentsBuilder path = UriComponentsBuilder.fromHttpUrl(HOTEL_SERVICE_BASE_URL)
					.path("/gethotel")
					.queryParam("hotelId", rating.getHotelId());

			try {
				ResponseEntity<HotelResponse> hotelResponseEntity = restTemplate.getForEntity(
						path.toUriString(),
						HotelResponse.class
				);

				HotelResponse hotel = hotelResponseEntity.getBody();
				rating.setHotelResponse(hotel);
				return rating;
			} catch (Exception e) {
				logger.error("ERROR: " + e.getMessage());
				throw new CustomizedException(HttpStatus.INTERNAL_SERVER_ERROR, "Getting error in hotel service");
			}
		}).collect(Collectors.toList());

		UserResponse userResponse = new UserResponse();

		userResponse.setId(user.getId());
		userResponse.setName(user.getName());
		userResponse.setEmail(user.getEmail());
		userResponse.setAbout(user.getAbout());
		userResponse.setRatings(ratingDetailsList);

		return userResponse;
	}

	private UserResponse ratingHotelServiceFallback(String userId, Exception exception) {

		if(exception instanceof ResourceNotFoundException) {
			logger.info("User not found by given id: " + exception.getMessage());
			throw new ResourceNotFoundException("User not found by given id: " + userId);
		}

		logger.info("Fallback method is executed because service is down: " + exception.getMessage());

		UserResponse user = new UserResponse();
		user.setId("0000");
		user.setName("Dummy");
		user.setEmail("dummy@gmail.com");
		user.setAbout("This user is a dummy user and created because service is down");
		user.setRatings(Lists.newArrayList());

		return user;

	}

	@Override
	public UserResponse updateUser(String userId, UserRequest userRequest) {
		
		User getUser = userRepository.findById(userId)
				.orElseThrow(() -> new CannotUpdateException("User not found by given id " + userId + " That's why cannot update the user details"));
		
		if(!userRequest.getName().isBlank()) {
			getUser.setName(userRequest.getName());
		}

		if(!userRequest.getEmail().isBlank()) {
			getUser.setEmail(userRequest.getEmail());
		}
		
		if(!userRequest.getAbout().isBlank()) {
			getUser.setAbout(userRequest.getAbout());
		}
		
		User updatedUser =  userRepository.save(getUser);
		
		UserResponse userResponse = new UserResponse();
		
		userResponse.setId(updatedUser.getId());
		userResponse.setName(updatedUser.getName());
		userResponse.setEmail(updatedUser.getEmail());
		userResponse.setAbout(updatedUser.getAbout());
		
		return userResponse;
	}

	@Override
	@CircuitBreaker(name = "ratingHotelServiceBreaker", fallbackMethod = "ratingDeleteServiceFallback")
	public String deleteUserWithRatings(String userId) {

		userRepository.findById(userId)
				.orElseThrow(() -> new CannotDeleteException("User not found by given id " + userId + " That's why cannot detele the user"));


		List<RatingResponse> ratingResponseList = ratingService.getRatingByUserId(userId);

		ratingResponseList.forEach(rating -> {
			ratingService.deleteRating(userId, rating.getRatingId());
		});

		userRepository.deleteById(userId);

		return "User along with ratings is deleted successfully by id: " + userId;
	}

	private String ratingDeleteServiceFallback(String userId, Exception exception) {

		if(exception instanceof CannotDeleteException) {
			logger.info("User not found by given id: " + exception.getMessage());
			throw new CannotDeleteException("User not found by given id " + userId + " That's why cannot delete the user");
		}

		logger.info("Fallback method is executed because service is down: " + exception.getMessage());
		return "Cannot perform this operation due to some technical problem. Try sometimes later!";

	}
}
