package com.mareussite.backend.service;

import com.mareussite.backend.dto.request.SignupRequest;
import com.mareussite.backend.dto.response.SuccessDto;

public interface IUserService {
    SuccessDto create(SignupRequest signUpRequest);
}
