package com.hotel.diamante.repository;

import com.hotel.diamante.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Add additional methods here if needed

    List<Booking>findByRoomId(Long id);
    List<Booking>findByBookingConfirmationCode(String confirmationCode);
    List<Booking> findByUserId(Long userId);

}
