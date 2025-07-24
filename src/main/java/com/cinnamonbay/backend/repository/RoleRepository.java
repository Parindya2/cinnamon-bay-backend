package com.cinnamonbay.backend.repository;

import com.cinnamonbay.backend.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.util.*;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    // Add this method to handle the duplicate issue
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    List<Role> findAllByName(@Param("name") String name);
}
