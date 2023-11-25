package com.ms.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/error/fallback")
public class FallbackController {

    @RequestMapping("/contactSupport")
    public ResponseEntity<String> contactSupport() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred. Please try after some time or contact support team!!!");
    }

}
