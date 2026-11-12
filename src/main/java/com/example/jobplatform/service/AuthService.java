package com.example.jobplatform.service;
import com.example.jobplatform.dto.AuthDtos.*; import com.example.jobplatform.entity.AppUser; import com.example.jobplatform.repository.UserRepository; import com.example.jobplatform.security.JwtService; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service public class AuthService {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthService(UserRepository users,PasswordEncoder encoder,JwtService jwt){this.users=users;this.encoder=encoder;this.jwt=jwt;}
 @Transactional public AuthResponse register(RegisterRequest r){String email=r.email().toLowerCase().trim();if(users.findByEmail(email).isPresent())throw new IllegalArgumentException("Email already registered");AppUser u=new AppUser();u.setEmail(email);u.setPasswordHash(encoder.encode(r.password()));users.save(u);return response(email);}
 public AuthResponse login(LoginRequest r){AppUser u=users.findByEmail(r.email().toLowerCase().trim()).orElseThrow(()->new IllegalArgumentException("Invalid credentials"));if(!encoder.matches(r.password(),u.getPasswordHash()))throw new IllegalArgumentException("Invalid credentials");return response(u.getEmail());}
 private AuthResponse response(String email){return new AuthResponse(jwt.generate(email),"Bearer",jwt.expirationSeconds());}
}
