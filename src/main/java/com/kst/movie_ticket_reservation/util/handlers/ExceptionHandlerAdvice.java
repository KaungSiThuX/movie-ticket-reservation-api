package com.kst.movie_ticket_reservation.util.handlers;

import com.kst.movie_ticket_reservation.util.api_responses.ErrorApiResponse;
import com.kst.movie_ticket_reservation.util.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.MismatchedInputException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice
{
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorApiResponse> handleUnauthorizedException(UnauthorizedException exception)
    {
        String message = exception.getMessage();

        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(), "UNAUTHORIZED CODE", message);

        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED.value());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorApiResponse> handleNotFoundException(NotFoundException exception,
                                                                    HttpServletRequest request)
    {
        String message = exception.getMessage();


        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(), "NOT FOUND CODE", message);

        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.NOT_FOUND.value());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorApiResponse> handleConflictException(ConflictException exception)
    {
        String message = exception.getMessage();

        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), "CONFLICT CODE", message);

        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.CONFLICT.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception)
    {
        //Map<String, String> errors = new HashMap<>();
        List<String> errors = new ArrayList<>();

        exception.getBindingResult().getAllErrors().forEach(objectError ->
        {
            //  String field = ((FieldError) objectError).getField();
            // String error = objectError.getDefaultMessage();
            // errors.put(field, error);
            errors.add(objectError.getDefaultMessage());
        });

        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), "BAD_REQUEST_CODE", errors);

        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorApiResponse> handleBadRequestException(BadRequestException exception)
    {
        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), "BAD_REQUEST_CODE", exception.getLocalizedMessage());

        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }


    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorApiResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception)
    {
        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.METHOD_NOT_ALLOWED.value(),
                HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), "METHOD_NOT_ALLOWED_CODE",
                exception.getLocalizedMessage());

        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ErrorApiResponse> handleAccessDeniedAndAuthorizationDeniedException(Exception exception)
    {
        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(), "FORBIDDEN_CODE", exception.getLocalizedMessage());

        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.FORBIDDEN.value());
    }


    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(RequestTimeoutException.class)
    public ResponseEntity<ErrorApiResponse> handleRequestTimeoutException(RequestTimeoutException exception)
    {
        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.REQUEST_TIMEOUT.value(),
                HttpStatus.REQUEST_TIMEOUT.getReasonPhrase(), "REQUEST_TIMEOUT_CODE",
                exception.getLocalizedMessage());

        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<ErrorApiResponse> handleTooManyRequestException(TooManyRequestException exception)
    {
        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.TOO_MANY_REQUESTS.value(),
                HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(), "TOO_MANY_REQUEST_CODE",
                exception.getLocalizedMessage());

        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.TOO_MANY_REQUESTS.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorApiResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception)
    {
        List<String> errors = new ArrayList<>();
        log.info("get cause " + exception.getCause());

        if (exception.getCause() instanceof MismatchedInputException mismatchedInputException)
        {
            List<JacksonException.Reference> path = mismatchedInputException.getPath();


            String fieldName = path.stream()
                    .map(JacksonException.Reference::getPropertyName)
                    .filter(Objects::nonNull) // Ignores the null caused by the array element index
                    .collect(Collectors.joining(""));


            log.info("field name " + fieldName);

            boolean isCollectionElement = path.stream().anyMatch(ref -> ref.getIndex() >= 0);

            String typeSimpleName = mismatchedInputException.getTargetType().getSimpleName();

            log.info("org message " + mismatchedInputException.getOriginalMessage());
            log.info("target type " + mismatchedInputException.getClass().getSimpleName());
            log.info("is collection " + isCollectionElement);
            log.info("type simple name " + typeSimpleName);

            var targetType = mismatchedInputException.getTargetType();
            String type;

            if (isCollectionElement)
            {
                type = typeSimpleName + " Array";
            }
            else if (Set.of("java.util.List", "java.util.Set", "java.util.HashSet", "java.util.ArrayList").contains
                    (mismatchedInputException.getTargetType().getTypeName()))
            {
                type = "Array";
            }
//            else if (targetType != null && targetType() && targetType.getContentType() != null)
//            {
//
//            }
            else
            {
                type = typeSimpleName;
            }

            errors.add(String.format("%s must be %s", fieldName, type));
        }

        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "MEESAGE_NOT_READABLE_CODE", errors);


        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(AsyncRequestNotUsableException.class)
    public void handleAsyncRequestNotUsableException(AsyncRequestNotUsableException exception)
    {
        log.debug("Ignored client-side network drop: {}", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApiResponse> handleGenericException(Exception exception)
    {
        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "INTERNAL_SERVER_ERROR_CODE",
                exception.getLocalizedMessage());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(errorApiResponse, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(CustomS3Exception.class)
    public ResponseEntity<ErrorApiResponse> handleCustomS3Exception(CustomS3Exception exception)
    {
        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.BAD_GATEWAY.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase(), "S3_BAD_GATEWAY_ERROR_CODE", exception.getMessage());

        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.BAD_GATEWAY);
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(CustomStripeException.class)
    public ResponseEntity<ErrorApiResponse> handleCustomStripeException(CustomStripeException exception)
    {
        ErrorApiResponse errorApiResponse = new ErrorApiResponse(HttpStatus.BAD_GATEWAY.value(),
                HttpStatus.BAD_GATEWAY.getReasonPhrase(), "STRIPE_BAD_GATEWAY_ERROR_CODE",
                exception.getLocalizedMessage());


        return new ResponseEntity<>(errorApiResponse, new HttpHeaders(), HttpStatus.BAD_GATEWAY.value());
    }
}
