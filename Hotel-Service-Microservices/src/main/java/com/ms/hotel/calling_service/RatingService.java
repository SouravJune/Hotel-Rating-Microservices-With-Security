package com.ms.hotel.calling_service;

import com.ms.hotel.response.RatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "RATING-SERVICE-MS")
public interface RatingService {

    @GetMapping("/api/v1/rating/getrating-by-hotelid")
    List<RatingResponse> getRatingByHotelId(@RequestParam String hotelId);

    @DeleteMapping("/api/v1/rating/delete-rating-by-hotel")
    void deleteRating(@RequestParam String hotelId, @RequestParam String ratingId);
}
