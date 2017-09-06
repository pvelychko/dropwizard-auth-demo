package com.pvelychko.resources;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;

public abstract class AbstractResource<D> {

    private final D store;

    protected AbstractResource(D store) {
        this.store = store;
    }

    public D store() {
        return store;
    }

    protected NotFoundException notFound(String msg, Object... args) {
        return new NotFoundException(String.format(msg, args));
    }

    protected ForbiddenException forbidden(String msg, Object... data) {
        return new ForbiddenException(String.format(msg, data));
    }
}
