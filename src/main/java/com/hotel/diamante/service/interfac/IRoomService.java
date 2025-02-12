package com.hotel.diamante.service.interfac;

import com.hotel.diamante.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRoomService {
    Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);
    Response updateRoom(Long roomId, MultipartFile photo, String roomType, BigDecimal roomPrice, String description);
    Response deleteRoom(Long roomId);
    Response getRoomById(Long roomId);
    Response getAvailableRoomByDataAndType(LocalDate checkIn, LocalDate checkOut, String roomType);
    Response getAllRooms();
    List<String>getAllRoomType();



}
