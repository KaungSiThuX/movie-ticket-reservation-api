package com.kst.movie_ticket_reservation.feat.auth.user.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteRegisterDto
{
    private String name;

    private String phone;
}
