package com.hotel.diamante.dto;

import com.hotel.diamante.entity.Room;
import com.hotel.diamante.entity.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    @NotBlank(message = " data de entrada não pode nula")
    private LocalDate checkeIn;
    @NotBlank(message = "data de saída não pode nula")
    @Future(message = " data de saída não pode ser nopassado")
    private LocalDate checkOut;
    @Min(value = 1, message = "O numero de adultos não pode ser 0")
    private int numberOfAdults;
    @Min(value = 0, message = "O numero de crianças não pode ser menos que 0")
    private int numberOfChildren;
    private int totalNumberGuest;
    private String bookingConfirmationCOde;
    private UserDTO user;
    private RoomDTO room;
}
