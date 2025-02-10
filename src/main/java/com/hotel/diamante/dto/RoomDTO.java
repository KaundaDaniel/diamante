package com.hotel.diamante.dto;

import com.hotel.diamante.entity.Booking;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDTO {
    private Long id;
    private int roomNumber;
    private  String roomType;
    private BigDecimal price;
    private String Photo;
    private String description;
    private List<BookingDTO> bookingList= new ArrayList<BookingDTO>();

}
