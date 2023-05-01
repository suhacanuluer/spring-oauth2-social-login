package com.suhacan.springoauth2sociallogin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class DemoController {

    @GetMapping("/auth")
    public ResponseEntity<String> getRequest(Principal principal) {
        return ResponseEntity.ok("secured Hello with OAuth2. Welcome " + principal + "!");
    }
}
