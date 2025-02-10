package com.hotel.diamante.repository;

import com.hotel.diamante.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository

public interface RoomRepository extends JpaRepository<Room, Long> {


    @Query("SELECT DISTINCT r.roomType from Room r")
    List<String>findDistinctRoomTypes();

    @Query("SELECT r FROM Room r WHERE r.price <= :price AND r.roomType = :roomType")
    List<Room> findAvailableRoomsByPriceAndType(double price, String roomType);
    @Query("SELECT r FROM Room r WHERE r.roomType LIKE %:roomType% AND r.id NOT IN " +
            "(SELECT bk.room.id FROM Booking bk WHERE" +
            "(bk.checkIn <= :checkOut) AND (bk.checkOut >= :checkIn))")
    List<Room> findAvailableRoomsByDateAndType(LocalDate checkIn, LocalDate checkOut, String roomType);
//    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id from booking b) ORDER BY r.price   ")
//    List<Room> gettAllAvailableRooms();

}
