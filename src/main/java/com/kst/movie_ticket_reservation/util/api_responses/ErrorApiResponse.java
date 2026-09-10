package com.kst.movie_ticket_reservation.util.api_responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorApiResponse extends CommonApiResponse
{
    private List<String> errors;
    //  private String error;

    public ErrorApiResponse(int status, String message, String code, String error)
    {
        super(status, message, code);
        this.errors = Collections.singletonList(error);
        //  this.errors = null;
    }

    public ErrorApiResponse(int status, String message, String code, List<String> errors)
    {
        super(status, message, code);
        this.errors = errors;
        //  this.error = null;
    }
}
