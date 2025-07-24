package com.cinnamonbay.backend.service;

import com.cinnamonbay.backend.exception.ResourceNotFoundException;
import com.cinnamonbay.backend.model.Room;
import com.cinnamonbay.backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService{

    private final RoomRepository roomRepository;

    @Override
    public Room addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice) {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);

        try {
            room.setPhoto(new javax.sql.rowset.serial.SerialBlob(photo.getBytes()));
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error processing room photo", e);
        }

        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes(){
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }


    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) {
        Optional<Room> theRoom = roomRepository.findById(roomId);

        if (theRoom.isEmpty()) {
            System.err.println("Room not found for ID: " + roomId);
            return null;
        }

        Blob photoBlob = theRoom.get().getPhoto();

        if (photoBlob == null) {
            System.err.println("No photo available for room ID: " + roomId);
            return null;
        }

        try {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        } catch (SQLException e) {
            System.err.println("Error retrieving photo for room ID " + roomId + ": " + e.getMessage());
            return null; // don't crash the app – just return null
        }
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if(theRoom.isPresent()){
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        Room room =  roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (photoBytes != null && photoBytes.length > 0){
            try{
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException ex){
                throw new InternalServerException("Error updating room");

            }
        }

        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return Optional.of(roomRepository.findById(roomId).get());
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkoutDate, String roomType) {
        return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkoutDate, roomType);
    }


}
