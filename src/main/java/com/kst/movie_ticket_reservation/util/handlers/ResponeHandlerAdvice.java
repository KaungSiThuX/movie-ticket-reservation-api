//package com.kst.movie_ticket_reservation.util.handlers;
//
//import com.kst.movie_ticket_reservation.util.annotations.UseCursorPagination;
//import com.kst.movie_ticket_reservation.util.annotations.UseOffsetPagination;
//import com.kst.movie_ticket_reservation.util.api_responses.CursorPaginationResponse;
//import com.kst.movie_ticket_reservation.util.api_responses.OffsetPaginationResponse;
//import com.kst.movie_ticket_reservation.util.api_responses.SuccessApiResponse;
//import org.jspecify.annotations.Nullable;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//@RestControllerAdvice
//public class ResponeHandlerAdvice implements ResponseBodyAdvice<Object>
//{
//    @Override
//    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)
//    {
//        return true;
//    }
//
//    @Override
//    public @Nullable Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType,
//                                            MediaType selectedContentType,
//                                            Class<? extends HttpMessageConverter<?>> selectedConverterType,
//                                            ServerHttpRequest request, ServerHttpResponse response)
//    {
//
//        if (body == null || body instanceof SuccessApiResponse<?> || body instanceof OffsetPaginationResponse<?> ||
//        body instanceof CursorPaginationResponse<?>)
//        {
//            return body;
//        }
//
//        if (returnType.hasMethodAnnotation(UseCursorPagination.class))
//        {
//            return body;
//        }
//
//        if (returnType.hasMethodAnnotation(UseOffsetPagination.class))
//        {
//            return body;
//        }
//
//          return new SuccessApiResponse<>();
//    }
//}
