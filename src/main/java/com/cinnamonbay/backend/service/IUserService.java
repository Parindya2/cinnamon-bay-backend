package com.cinnamonbay.backend.service;

import com.cinnamonbay.backend.model.*;

import java.util.*;

public interface IUserService {
    User registerUser(User user);

    List<User> getUsers();

    void deleteUser(String email);

    User getUser(String email);
}
