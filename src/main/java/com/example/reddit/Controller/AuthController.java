package com.example.reddit.Controller;
import com.example.reddit.DTO.AuthenticationResponse;
import com.example.reddit.DTO.LoginRequest;
import com.example.reddit.Services.AuthService;
import com.example.reddit.DTO.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final AuthService authservice;

    @PostMapping(value = "/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {


        authservice.signup(registerRequest);
        return new ResponseEntity<>("user registration successful", OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authservice.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authservice.login(loginRequest);
    }



}




