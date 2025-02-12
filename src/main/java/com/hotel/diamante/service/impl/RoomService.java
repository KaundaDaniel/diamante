package com.hotel.diamante.service.impl;

import com.hotel.diamante.dto.Response;
import com.hotel.diamante.dto.RoomDTO;
import com.hotel.diamante.entity.Room;
import com.hotel.diamante.exception.CustomerException;
import com.hotel.diamante.repository.BookingRepository;
import com.hotel.diamante.repository.RoomRepository;
import com.hotel.diamante.service.interfac.IRoomService;
import com.hotel.diamante.utils.Utils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    private static final String UPLOAD_DIRECTORY = "uploads/rooms";
    private static final String IMAGE_URL_PREFIX = "/images/rooms/";

    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }
    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
            Response response = new Response();
            try {
                String photoUrl = saveRoomImage(photo);
                Room room = Room.builder()
                        .photoUrl(photoUrl)
                        .roomType(roomType)
                        .price(roomPrice)
                        .description(description)
                        .build();
                roomRepository.save(room);
                RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(room);
                response.setStatusCode(200);
                response.setMessage("Quarto adicionada com sucesso");
                response.setRoom(roomDTO);

                response.setStatusCode(200);
                response.setMessage("Sala adicionada com sucesso");
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
    public Response updateRoom(Long roomId, MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomerException("Quarto não encontrado"));
            if (photo!= null) {
                String photoUrl = saveRoomImage(photo);
                room.setPhotoUrl(photoUrl);
            }

            room.setRoomType(roomType);
            room.setPrice(roomPrice);
            room.setDescription(description);
            roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(room);
            response.setStatusCode(200);
            response.setMessage("Quarto atualizado com sucesso");
            response.setRoom(roomDTO);
        } catch (CustomerException e) {
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
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomerException("Quarto não encontrado"));
            roomRepository.delete(room);
            deleteRoomImage(room.getPhotoUrl());
            response.setStatusCode(200);
            response.setMessage("Quarto apagado com sucesso");
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
    public Response getRoomById(Long roomId) {
        Response response = new Response();
        try {
            Room room = roomRepository.findById(roomId).orElseThrow(() -> new CustomerException("Quarto não encontrado"));
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTOPlusBookings(room);
            response.setStatusCode(200);
            response.setMessage("Quarto carregado com sucesso");
            response.setRoom(roomDTO);
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
    public Response getAvailableRoomByDataAndType(LocalDate checkIn, LocalDate checkOut, String roomType) {
        Response response = new Response();
        try {
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDateAndType(checkIn, checkOut, roomType);
            List<RoomDTO> roomDTOs = Utils.mapRoomListEntityToRoomListDTO(availableRooms);
            response.setStatusCode(200);
            response.setMessage("Quartos disponíveis carregados com sucesso");
            response.setRoomDTOList(roomDTOs);
        }catch (CustomerException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro interno no sistema");
        }
        return response;
    }

    @Override
    public Response getAllRooms() {
        Response response = new Response();
        try {
            List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<RoomDTO> roomDTOs = Utils.mapRoomListEntityToRoomListDTO(rooms);
            response.setStatusCode(200);
            response.setMessage("Quartos carregados com sucesso");
            response.setRoomDTOList(roomDTOs);
        }catch (CustomerException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Ocorreu um erro interno no sistema");
            e.printStackTrace();
        }
        return response ;
    }

    @Override
    public List<String> getAllRoomType() {
        return roomRepository.findDistinctRoomTypes();
    }


    private String saveRoomImage(MultipartFile photo) throws IOException {
        // Create directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = photo.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + fileExtension;

        // Save file
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return URL for database storage
        return IMAGE_URL_PREFIX + newFilename;
    }

    // Helper method to delete image if room creation fails
    private void deleteRoomImage(String photoUrl) {
        try {
            if (photoUrl != null && photoUrl.startsWith(IMAGE_URL_PREFIX)) {
                String filename = photoUrl.substring(IMAGE_URL_PREFIX.length());
                Path filePath = Paths.get(UPLOAD_DIRECTORY, filename);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            // Log error but don't throw - this is cleanup code
            e.printStackTrace();
        }
    }
}
