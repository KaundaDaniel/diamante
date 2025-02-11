package com.hotel.diamante.utils;

import com.hotel.diamante.dto.BookingDTO;
import com.hotel.diamante.dto.RoomDTO;
import com.hotel.diamante.dto.UserDTO;
import com.hotel.diamante.entity.Booking;
import com.hotel.diamante.entity.Room;
import com.hotel.diamante.entity.User;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private static  final String ALFPHANUMERIC="ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final SecureRandom rand = new SecureRandom();

    private String generateRandomAlphanumeric(int length){
        StringBuilder stringBuilder= new StringBuilder();
        for (int i=0; i<length; i++){
            int randomIndex=rand.nextInt(ALFPHANUMERIC.length());
            char randomChar=ALFPHANUMERIC.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
    public static UserDTO mapUserEntityToUserDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }

    public static User mapUserDTOToUserEntity(UserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .phoneNumber(userDTO.getPhoneNumber())
                .role(userDTO.getRole())
                .build();
    }
    public static RoomDTO mapRoomEntityToRoom(Room room){
        return RoomDTO.builder()
               .id(room.getId())
               .roomNumber(room.getRoomNumber())
               .roomType(room.getRoomType())
               .price(room.getPrice())
               .photoUrl(room.getPhotoUrl())
               .description(room.getDescription())
               .build();
    }
    public static Room mapRoomDTOToRoomEntity(RoomDTO roomDTO){
        return Room.builder()
               .id(roomDTO.getId())
               .roomNumber(roomDTO.getRoomNumber())
               .roomType(roomDTO.getRoomType())
               .price(roomDTO.getPrice())
               .photoUrl(roomDTO.getPhotoUrl())
               .description(roomDTO.getDescription())
               .build();
    }
    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room){
        RoomDTO roomDTO = RoomDTO.builder()
               .id(room.getId())
                .roomType(room.getRoomType())
                .price(room.getPrice())
                .photoUrl(room.getPhotoUrl())
                .build();
        if(room.getBookings()!=null){
            roomDTO.setBookingList(room.getBookings().stream().map(Utils::mapBookingEntityToBookingsDTO).collect(Collectors.toList()));
        }
        return roomDTO;

    }

    private static BookingDTO mapBookingEntityToBookingsDTO(Booking booking) {
        BookingDTO bookDTO = BookingDTO.builder()
                .id(booking.getId())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .numberOfAdults(booking.getNumberOfAdults())
                .numberOfChildren(booking.getNumberOfChildren())
                .bookingConfirmationCode(booking.getBookingConfirmationCode())
                .build();
        return bookDTO;

    }


    public static UserDTO mapUserEntityToUserPlusBookingsAndRoom(User user){
        UserDTO userDTO = new UserDTO();
         UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();

        if(!user.getBooking().isEmpty()){
           userDTO.setBookingList(user.getBooking().stream().map(booking->mapBookingEntityToBookingDTO(booking,false)).collect(Collectors.toList()));
        }

        return userDTO;
    }

    private static BookingDTO mapBookingEntityToBookingDTO(Booking booking, boolean mapUser){
        BookingDTO bookingDTO = BookingDTO.builder()
               .id(booking.getId())
               .checkIn(booking.getCheckIn())
               .checkOut(booking.getCheckOut())
               .numberOfAdults(booking.getNumberOfAdults())
               .numberOfChildren(booking.getNumberOfChildren())
                .totalNumberGuest(booking.getTotalNumberGuest())
               .bookingConfirmationCode(booking.getBookingConfirmationCode())
               .build();
        if(mapUser && booking.getUser()!=null){
            bookingDTO.setUser(mapUserEntityToUserDTO(booking.getUser()));
        }
        if(booking.getRoom()!=null){
            RoomDTO roomDTO= RoomDTO.builder()
                    .id(booking.getRoom().getId())
                    .roomNumber(booking.getRoom().getRoomNumber())
                    .roomType(booking.getRoom().getRoomType())
                    .price(booking.getRoom().getPrice())
                    .photoUrl(booking.getRoom().getPhotoUrl())
                    .description(booking.getRoom().getDescription())
                    .build();

        }
        return bookingDTO;

    }

    public static List<UserDTO>mapUserListEntityToUserListDTO(List<User> userList){
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }
    public static List<RoomDTO> mapRoomListEntityToRoomListDTO(List<Room> roomList){
        return roomList.stream().map(Utils::mapRoomEntityToRoom).collect(Collectors.toList());
    }
    public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList){
        return bookingList.stream().map(Utils::mapBookingEntityToBookingsDTO).collect(Collectors.toList());
    }



}
