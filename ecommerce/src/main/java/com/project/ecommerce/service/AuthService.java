package com.project.ecommerce.service;

import com.project.ecommerce.payload.AuthenticationResult;
import com.project.ecommerce.payload.UserResponse;
import com.project.ecommerce.security.request.LoginRequest;
import com.project.ecommerce.security.request.SignupRequest;
import com.project.ecommerce.security.response.MessageResponse;
import com.project.ecommerce.security.response.UserInfoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {
    AuthenticationResult login(LoginRequest loginRequest);
    ResponseEntity<MessageResponse> register(SignupRequest signupRequest);
    UserInfoResponse getCurrentUserDetails(Authentication authentication);
    ResponseCookie logoutUser();
    UserResponse getAllSellers(Pageable pageDetails);
}
