package com.pvelychko.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.pvelychko.model.User;
import com.pvelychko.store.UserStore;
import io.dropwizard.hibernate.UnitOfWork;

import javax.inject.Singleton;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;

@Singleton
@Path("/signup")
public class RegistrationResouce extends AbstractResource<UserStore>  {

    public RegistrationResouce(UserStore store) {
        super(store);
    }

    @POST
    @Timed
    @UnitOfWork
    @ExceptionMetered
    public Response register(@FormParam("firstName") String firstName,
                             @FormParam("lastName") String lastName,
                             @FormParam("email") String email,
                             @FormParam("password") String password) {
        if (store().findByEmail(email).isPresent()) {
            return Response.seeOther(URI.create("/register")).build();
        }

        store().create(User.builder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password)
                .build());

        return Response.seeOther(URI.create("/")).build();
    }
}
