package com.kst.movie_ticket_reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloController
{
    @GetMapping
    ResponseEntity<String> hello()
    {
        return ResponseEntity.ok("hello from movie ticket reservation");
    }
}
