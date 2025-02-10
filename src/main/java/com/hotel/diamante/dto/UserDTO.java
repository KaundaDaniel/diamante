package com.hotel.diamante.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotel.diamante.entity.Booking;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    @NotBlank(message="Digite a palavara Passe ")
    @NotNull(message = "Não pode estar vazia")
    private String password;
    private String phoneNumber;

    private String role;
    private List<BookingDTO> bookingList = new ArrayList<BookingDTO>();


}
