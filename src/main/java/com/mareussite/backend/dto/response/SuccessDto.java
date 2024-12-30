package com.mareussite.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessDto {
    private String message;
}