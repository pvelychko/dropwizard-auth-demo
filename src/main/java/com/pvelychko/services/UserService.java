package com.pvelychko.services;

import com.pvelychko.model.User;
import com.pvelychko.store.UserStore;
import io.dropwizard.hibernate.UnitOfWork;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class UserService {
    private final UserStore store;

    @Inject
    public UserService(UserStore store) {
        this.store = store;
    }

    @UnitOfWork
    public User create(User user) {
        return store.create(user);
    }

    @UnitOfWork
    public Optional<User> findByEmail(final String email) {
        return store.findByEmail(email);
    }
}
