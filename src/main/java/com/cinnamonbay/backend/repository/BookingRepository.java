package com.cinnamonbay.backend.repository;

import com.cinnamonbay.backend.model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface BookingRepository extends JpaRepository<BookedRoom, Long> {

    Optional<BookedRoom> findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> findByRoomId(Long roomId);

    List<BookedRoom> findByGuestEmail(String email);
}
