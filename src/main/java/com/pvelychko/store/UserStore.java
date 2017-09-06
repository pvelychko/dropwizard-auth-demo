package com.pvelychko.store;

import com.pvelychko.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStore {

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User create(User user);
    User update(User user);
    void delete(Long id);
}
