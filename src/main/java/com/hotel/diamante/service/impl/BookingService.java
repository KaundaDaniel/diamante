package com.hotel.diamante.service.impl;

import com.hotel.diamante.dto.BookingDTO;
import com.hotel.diamante.dto.Response;
import com.hotel.diamante.entity.Booking;
import com.hotel.diamante.entity.Room;
import com.hotel.diamante.entity.User;
import com.hotel.diamante.exception.CustomerException;
import com.hotel.diamante.repository.BookingRepository;
import com.hotel.diamante.repository.RoomRepository;
import com.hotel.diamante.repository.UserRepository;
import com.hotel.diamante.service.interfac.IBookingService;
import com.hotel.diamante.service.interfac.IRoomService;
import com.hotel.diamante.utils.Utils;
import org.springframework.data.domain.Sort;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.List;
import java.util.Optional;

public class BookingService implements IBookingService {
    private  final BookingRepository bookingRepository;
    private final IRoomService service;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, IRoomService service, RoomRepository roomRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.service = service;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
        Response response= new Response();
        try {
            if (bookingRequest.getCheckOut().isAfter(bookingRequest.getCheckIn())){
                throw new IllegalStateException("A Data de saída deve ser depois da entrada");
            }
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomerException("Quarto não encontrado"));
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomerException("Usuário não encontrado"));
            List<Booking>existenceBooking= room.getBookings();

            if(!roomIsAvailable(bookingRequest, existenceBooking)){
                throw new CustomerException("Não existe quarto nesta data");
            }
            bookingRequest.setRoom(room);
            bookingRequest.setUser(user);
            String confirmationCode= Utils.generateRandomAlphanumeric(10);
            bookingRepository.save(bookingRequest);
            response.setStatusCode(200);
            response.setMessage("Reserva realizada com sucesso");
            response.setBookingConfirmationCode(confirmationCode);




        }catch (CustomerException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro interno no sistema");
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response getBookingById(Long bookingId) {
        return null;
    }

    @Override
    public Response findBookingByConfirmationCode(String confirmationCode) {
        Response response= new Response();
        try {
            Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode)
                    .orElseThrow(()->new CustomerException("Não encontrado nenhum quarto com esse código")
                    );
            BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTO(booking);
            response.setStatusCode(200);
            response.setMessage("Obtido com sucesso");
            response.setBookingConfirmationCode(confirmationCode);
        }catch (CustomerException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro interno no sistema");
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response getAllBookings() {
        Response response = new Response();
        try {
            List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<BookingDTO> bookingDTOList= Utils.mapBookingListEntityToBookingListDTO(bookings);

            response.setStatusCode(200);
            response.setMessage("Obtido com sucesso");
            response.setBookingDTOList(bookingDTOList);
        }catch (CustomerException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro interno no sistema");
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response cancelBooking(Long bookingId) {
        Response response = new Response();
        try {
            bookingRepository.findById(bookingId).orElseThrow(() -> new CustomerException("Reserva não encontrada"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("Reserva cancelada com sucesso");
        }catch (CustomerException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro interno no sistema");
            e.printStackTrace();
        }

        return response;
    }
    private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings){
        return existingBookings.stream()
               .noneMatch(b -> b.getCheckIn().isBefore(bookingRequest.getCheckOut()) && b.getCheckOut().isAfter(bookingRequest.getCheckIn()));
    }
}
