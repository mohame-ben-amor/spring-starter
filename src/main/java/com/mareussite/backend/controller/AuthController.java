package com.mareussite.backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.mareussite.backend.configuration.jwt.JwtUtils;
import com.mareussite.backend.configuration.service.UserDetailsImpl;
import com.mareussite.backend.dto.request.LoginRequest;
import com.mareussite.backend.dto.request.SignupRequest;
import com.mareussite.backend.dto.response.JwtResponse;
import com.mareussite.backend.dto.response.MessageResponse;
import com.mareussite.backend.dto.response.SuccessDto;
import com.mareussite.backend.entity.Enum.ERole;
import com.mareussite.backend.entity.Role;
import com.mareussite.backend.entity.User;
import com.mareussite.backend.exception.EntityNotFoundException;
import com.mareussite.backend.exception.InvalidOperationException;
import com.mareussite.backend.repository.IRoleRepository;
import com.mareussite.backend.repository.IUserRepository;
import com.mareussite.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserService iUserService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/signup")
    public ResponseEntity<SuccessDto> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(iUserService.create(signUpRequest));
    }
}