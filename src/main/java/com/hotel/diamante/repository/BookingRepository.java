package com.hotel.diamante.repository;

import com.hotel.diamante.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Add additional methods here if needed

    List<Booking>findByRoomId(Long id);
    List<Booking>findByBookConfirmationCode(String confirmationCode);
    List<Booking> findByUserId(Long userId);

}
