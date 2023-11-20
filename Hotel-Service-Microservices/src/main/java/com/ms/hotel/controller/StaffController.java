package com.ms.hotel.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * This class is mainly to see how we can configure multiple API endpoints of a microservice in API Gateway.
 */
@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    @GetMapping("/")
    public ResponseEntity<List<String>> allStaticStaffs() {
        List<String> allStaffs = Arrays.asList("Ram", "Sita", "Krishna", "Radha");
        return new ResponseEntity<>(allStaffs, HttpStatus.OK);
    }
}
