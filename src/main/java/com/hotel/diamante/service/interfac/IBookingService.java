package com.hotel.diamante.service.interfac;

import com.hotel.diamante.dto.Response;
import com.hotel.diamante.entity.Booking;

public interface IBookingService {
    // TODO: Implement booking service methods

    Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
    Response getBookingById(Long bookingId);
    Response findBookingByConfirmationCode(String confirmationCode);
    Response getAllBookings();
    Response cancelBooking(Long bookingId);
}
