package com.mareussite.backend.service.implementation;

import com.mareussite.backend.dto.request.SignupRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static com.mareussite.backend.utils.ErrorMessage.*;
import static com.mareussite.backend.utils.SuccessMessage.USER_SUCCESSFULLY_CREATED;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public SuccessDto create(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new InvalidOperationException(USERNAME_TAKEN);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new InvalidOperationException(EMAIL_TAKEN);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<ERole> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            throw new EntityNotFoundException(ROLE_NOT_FOUND);
        } else {
            strRoles.forEach(roleItem -> {
                Role role = roleRepository.findByName(roleItem)
                        .orElseThrow(() -> new EntityNotFoundException(ROLE_NOT_FOUND));
                roles.add(role);
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return SuccessDto.builder()
                .message(USER_SUCCESSFULLY_CREATED)
                .build();
    }
}
