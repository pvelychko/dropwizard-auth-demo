package com.pvelychko.resources;

import com.pvelychko.model.LoginAttempt;
import com.pvelychko.model.User;
import com.pvelychko.store.LoginAttemptStore;
import com.pvelychko.store.UserStore;
import com.pvelychko.store.jdbc.LoginAttemptJdbcImpl;
import com.pvelychko.store.jdbc.UserStoreJdbcImpl;
import com.pvelychko.views.IndexView;
import com.pvelychko.views.LoginView;
import com.pvelychko.views.RegisterView;
import com.pvelychko.views.UserView;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.views.View;
import org.hibernate.SessionFactory;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.jax.rs.annotations.Pac4JCallback;
import org.pac4j.jax.rs.annotations.Pac4JLogout;
import org.pac4j.jax.rs.annotations.Pac4JProfileManager;
import org.pac4j.jax.rs.annotations.Pac4JSecurity;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Optional;

@Singleton
@Path("/")
public class ViewsResource {
    private final Config config;
    private final SessionFactory sessionFactory;

    @Inject
    public ViewsResource(Config config, SessionFactory sessionFactory) {
        this.config = config;
        this.sessionFactory = sessionFactory;
    }

    @GET
    public View index(@Pac4JProfileManager ProfileManager<CommonProfile> pm) {
        return new IndexView(pm);
    }

    @GET
    @Path("/profile")
    @Pac4JSecurity(clients = "FormClient", authorizers = "securityHeaders")
    @UnitOfWork
    public View form(@Pac4JProfileManager ProfileManager<CommonProfile> pm) {
        final UserStore userStore = new UserStoreJdbcImpl(sessionFactory);
        final LoginAttemptStore loginAttemptStore = new LoginAttemptJdbcImpl(sessionFactory);

        Optional<User> user = userStore.findById(Long.valueOf(pm.get(true).get().getId()));
        List<LoginAttempt> loginAttemptList = loginAttemptStore.findLoginAttempts(user.get(), 0, 5);

        return new UserView(user.get(), loginAttemptList);
    }

    @GET
    @Path("/login")
    @Pac4JSecurity(clients = "AnonymousClient", authorizers = "mustBeAnon")
    public View login() {
        return new LoginView(config);
    }

    @GET
    @Path("/register")
    @Pac4JSecurity(clients = "AnonymousClient", authorizers = "mustBeAnon")
    public View register() {
        return new RegisterView(config);
    }

    @POST
    @Path("/callback")
    @Pac4JCallback(multiProfile = true, renewSession = false, defaultUrl = "/")
    public void callbackPost() {
        // Nothing to do here, pac4j handles everything
        // Note that in jax-rs, you can't have two different http method on the
        // same resource method hence the duplication
    }

    @GET
    @Path("/callback")
    @Pac4JCallback(multiProfile = true, renewSession = false, defaultUrl = "/")
    public void callbackGet() {
        // Nothing to do here, pac4j handles everything
        // Note that in jax-rs, you can't have two different http method on the
        // same resource method hence the duplication
    }

    @GET
    @Path("/logout")
    @Pac4JSecurity(clients = "AnonymousClient", authorizers = "mustBeAuth")
    @Pac4JLogout(defaultUrl = "/")
    public void logout() {
        // Nothing to do here, pac4j handles everything
    }
}
