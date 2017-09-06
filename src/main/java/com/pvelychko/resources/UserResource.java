package com.pvelychko.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.pvelychko.model.CreateUserRequest;
import com.pvelychko.model.User;
import com.pvelychko.store.UserStore;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Singleton
@Path("/users")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserResource extends AbstractResource<UserStore>  {

    public UserResource(UserStore store) {
        super(store);
    }

    @POST
    @Timed
    @UnitOfWork
    @ExceptionMetered
    public Response create(@Auth User requestor, @Context UriInfo uriInfo, CreateUserRequest cur) {
        User user = store().create(User.builder()
                .setFirstName(cur.getFirstName())
                .setLastName(cur.getLastName())
                .setEmail(cur.getEmail())
                .setPassword(cur.getPassword())
                .build());

        return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(user.getId())).build()).build();
    }

    @GET
    @Timed
    @UnitOfWork
    @ExceptionMetered
    public List<User> findAll(@Auth User requestor) {
        return store().findAll();
    }

    @GET
    @Timed
    @UnitOfWork
    @ExceptionMetered
    @Path("/{id}")
    public User findById(@Auth User requestor, @PathParam("id") LongParam id) {
        return store().findById(id.get())
                .orElseThrow(() -> new NotFoundException("Cannot find User with id " + id));
    }

    @GET
    @Timed
    @UnitOfWork
    @ExceptionMetered
    @Path("/find/{email}")
    public User findByEmail(@Auth User requestor, @PathParam("email") String email) {
        return store().findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Cannot find User with email " + email));
    }

    @DELETE
    @Timed
    @UnitOfWork
    @ExceptionMetered
    @Path("/{id}")
    public Response delete(@Auth User requestor, @PathParam("id") LongParam id) {
        User user = store().findById(id.get())
                .orElseThrow(() -> new NotFoundException("Cannot find User with id " + id));
        store().delete(user.getId());

        return Response.accepted().build();
    }
}
