package com.hotel.diamante.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private int  statusCode;
    private String  message;
    private String token;
    private String role;
    private String bookingConfirmationCOde;
    private UserDTO user;
    private RoomDTO room;
    private BookingDTO booking;
    private List<UserDTO>userDTOList;
    private List<RoomDTO>roomDTOList;
    private List<BookingDTO> bookingDTOList;
}
