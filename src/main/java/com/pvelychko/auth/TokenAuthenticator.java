package com.pvelychko.auth;

import com.pvelychko.model.LoginAttempt;
import com.pvelychko.model.User;
import com.pvelychko.store.UserStore;
import com.pvelychko.store.jdbc.UserStoreJdbcImpl;
import com.pvelychko.utils.Crypto;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.SessionFactory;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;

import javax.annotation.Priority;
import javax.inject.Singleton;
import javax.ws.rs.Priorities;
import java.time.LocalDateTime;

@Priority(Priorities.AUTHENTICATION)
@Singleton
public class TokenAuthenticator implements Authenticator<UsernamePasswordCredentials> {
    private final SessionFactory sessionFactory;

    public TokenAuthenticator(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @UnitOfWork
    public void validate(final UsernamePasswordCredentials credentials, final WebContext context) throws HttpAction, CredentialsException {
        UserStore userStore = new UserStoreJdbcImpl(sessionFactory);

        if (null == credentials) {
            new CredentialsException("No credentials provided");
        }

        final User user = userStore.findByEmail(credentials.getUsername())
                .filter((p) -> Crypto.checkPassword(credentials.getPassword(), p.getPassword()))
                .orElseThrow(() -> new CredentialsException("The username or password you have entered is invalid"));

        // Store successful login attempt
        user.getLoginAttemptList().add(LoginAttempt.builder()
                        .setUser(user)
                        .setDate(LocalDateTime.now())
                        .setSuccess(true)
                        .build());
        userStore.update(user);

        final CommonProfile profile = new CommonProfile();
        profile.setId(user.getId());
        credentials.setUserProfile(profile);
    }
}
