package com.mastersessay.blockchain.accounting.controller;

import com.mastersessay.blockchain.accounting.dto.request.user.LoginRequest;
import com.mastersessay.blockchain.accounting.dto.request.user.SignupRequest;
import com.mastersessay.blockchain.accounting.dto.response.user.JwtResponse;
import com.mastersessay.blockchain.accounting.dto.response.user.MessageResponse;
import com.mastersessay.blockchain.accounting.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_END_MESSAGE;
import static com.mastersessay.blockchain.accounting.consts.BlockchainAccountingConstants.Http.REQUEST_PROCESSING_START_MESSAGE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("POST /signin. Body = {}", loginRequest);

        JwtResponse jwtResponse = userService.processUserSignIn(loginRequest);

        log.info("Response code = {}", HttpStatus.OK);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        log.info(REQUEST_PROCESSING_START_MESSAGE);
        log.info("POST /signup. Body = {}", signUpRequest);

        MessageResponse messageResponse = userService.registerUser(signUpRequest);

        log.info("Response code = {}", HttpStatus.CREATED);
        log.info(REQUEST_PROCESSING_END_MESSAGE);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(messageResponse);
    }

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }
}
