package com.cinnamonbay.backend.service;

import com.cinnamonbay.backend.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

public interface IRoomService {
    Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException;

    List<String> getAllRoomTypes();

    List<Room> getAllRooms();

    byte[] getRoomPhotoByRoomId(Long roomId);

    void deleteRoom(Long roomId);

    Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes);

    Optional<Room> getRoomById(Long roomId);

    List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkoutDate, String roomType);
}
