package org.example.email_entity.controller;

import lombok.AllArgsConstructor;
import org.example.email_entity.dto.AuthResponse;
import org.example.email_entity.dto.LoginDto;
import org.example.email_entity.dto.UserRequest;
import org.example.email_entity.service.AuthService;
import org.example.email_entity.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private final UserServiceImpl userService;

    @Autowired
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRequest user){
        return new ResponseEntity<>(userService.saveNewUser(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto request) {
        return authService.login(request);
    }


}
