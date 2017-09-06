package com.pvelychko.store.jdbc;

import com.pvelychko.model.User;
import com.pvelychko.store.UserStore;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

import static com.pvelychko.utils.Functions.optional;
import static org.hibernate.criterion.Restrictions.eq;

public class UserStoreJdbcImpl extends AbstractDAO<User> implements UserStore {

    public UserStoreJdbcImpl(SessionFactory factory) {
        super(factory);
    }

    @Override
    public Optional<User> findById(Long id) {
        return optional(uniqueResult(criteria().add(eq("id", id))));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return optional(uniqueResult(criteria().add(eq("email", email))));
    }

    @Override
    public List<User> findAll() {
        return criteria().list();
    }

    @Override
    public User create(User user) {
        return persist(user);
    }

    @Override
    public User update(User user) {
        return persist(user);
    }

    @Override
    public void delete(Long userId) {
        currentSession()
                .createSQLQuery("DELETE FROM users WHERE id = :id")
                .setLong("id", userId)
                .executeUpdate();
    }
}
