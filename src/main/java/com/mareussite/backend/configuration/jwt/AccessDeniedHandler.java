package com.mareussite.backend.configuration.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mareussite.backend.exception.ErrorDto;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.mareussite.backend.utils.ErrorMessage.FORBIDDEN_RESPONSE;

@Component
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    private final ObjectMapper om;

    public AccessDeniedHandler() {
        this.om = new ObjectMapper();
        this.om.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ServletOutputStream out = response.getOutputStream();
        om.writeValue(
                out,
                ErrorDto
                        .builder()
                        .httpCode(403)
                        .message(FORBIDDEN_RESPONSE)
                        .build()
        );
        out.flush();
    }
}
