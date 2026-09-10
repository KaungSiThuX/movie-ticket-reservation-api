package com.kst.movie_ticket_reservation.feat.order.mapper;

import com.kst.movie_ticket_reservation.feat.genre.entity.Genre;
import com.kst.movie_ticket_reservation.feat.order.dto.res.OrderDetailResDto;
import com.kst.movie_ticket_reservation.feat.order.entity.Order;
import com.kst.movie_ticket_reservation.feat.show_seat.entity.ShowSeat;
import com.kst.movie_ticket_reservation.feat.show_seat.mapper.ShowSeatMapper;

import java.util.Collections;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {ShowSeatMapper.class})
public interface OrderMapper
{
    @Mapping(target = "orderId", source = "order.publicId")
    @Mapping(target = "movieTitle", source = "order.showTime.showDate.movie.title")
    @Mapping(target = "movieGenres", source = "order.showTime.showDate.movie.genres")
    @Mapping(target = "showDisplayDate", source = "order.showTime.showDate.showDisplayDate")
    @Mapping(target = "showDisplayTime", source = "order.showTime.showDisplayTime")
    @Mapping(target = "theatreName", source = "order.showTime.showDate.theatre.name")
    @Mapping(target = "orderedShowSeats", source = "showSeatList")
    @Mapping(target = "orderTotal", source = "order.orderTotal")
    @Mapping(target = "subTotal", source = "order.subTotal")
    @Mapping(target = "promoCodeDiscount", source = "order.promoCodeDiscountAmount")
    @Mapping(target = "tax", source = "order.tax")
    OrderDetailResDto toOrderDetailResDto(Order order, List<ShowSeat> showSeatList);


    default List<String> mapGenres(Set<Genre> genres)
    {
        if (genres == null)
        {
            return Collections.emptyList();
        }
        return genres.stream()
                .map(Genre::getName)
                .toList();
    }
}
