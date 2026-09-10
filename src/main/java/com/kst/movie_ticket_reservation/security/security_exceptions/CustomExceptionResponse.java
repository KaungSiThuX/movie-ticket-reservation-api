package com.kst.movie_ticket_reservation.security.security_exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomExceptionResponse
{
    private final ObjectMapper objectMapper;

    public void response(HttpServletRequest request, HttpServletResponse response,
                         CustomExceptionResponseData customExceptionResponseData) throws IOException
    {

//        String origin = request.getHeader("Origin");
//        if (origin == null || origin.isEmpty())
//        {
//            origin = "*";
//        }
//
//        // 3. Inject CORS headers before writing to the response body
//        response.setHeader("Access-Control-Allow-Origin", origin);
//        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.setHeader("Access-Control-Allow-Headers", "*");
//        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setStatus(customExceptionResponseData.status());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8);


        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", customExceptionResponseData.status());
        errorDetails.put("message", customExceptionResponseData.message());
        errorDetails.put("code", customExceptionResponseData.code());
        errorDetails.put("error", customExceptionResponseData.error());

        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
