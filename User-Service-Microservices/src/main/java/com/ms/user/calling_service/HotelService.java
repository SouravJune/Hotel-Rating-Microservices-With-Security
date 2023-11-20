package com.ms.user.calling_service;

import com.ms.user.response.HotelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "HOTEL-SERVICE-MS")
public interface HotelService {

    @GetMapping("/api/v1/hotel/gethotel")
    HotelResponse getHotelById(@RequestParam String hotelId);
}
