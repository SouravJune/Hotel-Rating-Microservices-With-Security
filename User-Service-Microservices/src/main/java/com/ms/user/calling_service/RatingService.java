package com.ms.user.calling_service;

import com.ms.user.response.RatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "RATING-SERVICE-MS")
public interface RatingService {

    @GetMapping("/api/v1/rating/getrating-by-userid")
    List<RatingResponse> getRatingByUserId(@RequestParam String userId);

    @DeleteMapping("/api/v1/rating/delete")
    void deleteRating(@RequestParam String userId, @RequestParam String ratingId);
}
