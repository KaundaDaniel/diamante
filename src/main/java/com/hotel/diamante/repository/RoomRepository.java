package com.hotel.diamante.repository;

import com.hotel.diamante.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {


    @Query("SELECT DISTINCT r.roomType from room r")
    List<Room> findDististicRoomTypes();

    @Query("SELECT r FROM room r WHERE r.price <= :price AND r.roomType = :roomType")
    List<Room> findAvailableRoomsByPriceAndType(double price, String roomType);

    @Query("SELECT r FROM Room r WHERE r.roomType LIKE %roomType% r.id NOT IN (SELECT bk.room.id FROM Booking bk WHERE+" +
            "(bk.checkIn<= :checkOut) AND (bk.checkOut<= :checkIn))")
    List<Room> findAvailableRoomsByDateAndType(LocalDate checkIn, LocalDate checkOut, String roomType);

//    @Query("SELECT r FROM r WHERE r.id NOT IN (SELECT b.room.id from booking b) ORDER BY r.price   ")
//    List<Room> gettAllAvailableRooms();

}
