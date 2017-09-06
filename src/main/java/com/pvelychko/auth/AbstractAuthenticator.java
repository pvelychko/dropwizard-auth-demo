package com.pvelychko.auth;

import com.pvelychko.model.User;
import com.pvelychko.store.UserStore;
import com.pvelychko.store.jdbc.UserStoreJdbcImpl;
import com.pvelychko.utils.Crypto;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.Optional;

public class AbstractAuthenticator implements Authenticator<BasicCredentials, User> {
    @Inject
    private UserStore store;

    public AbstractAuthenticator(SessionFactory sessionFactory) {
        store = new UserStoreJdbcImpl(sessionFactory);
    }

    @Override
    @UnitOfWork
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        return Optional.ofNullable(store.findByEmail(credentials.getUsername())
                .filter((p) -> Crypto.checkPassword(credentials.getPassword(), p.getPassword()))
                .orElseThrow(() -> new AuthenticationException("The username or password you have entered is invalid")));
    }
}
