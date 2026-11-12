package com.example.jobplatform.dto;
import jakarta.validation.constraints.*;
public class AuthDtos {
 public record RegisterRequest(@Email @NotBlank String email,@Size(min=8,max=72) String password) {}
 public record LoginRequest(@Email @NotBlank String email,@NotBlank String password) {}
 public record AuthResponse(String token,String tokenType,long expiresInSeconds) {}
}
