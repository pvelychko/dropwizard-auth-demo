package com.pvelychko.model;

import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Objects;

public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public CreateUserRequest() {
        // Default constructor
    }

    @NotEmpty
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotEmpty
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotEmpty
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateUserRequest)) return false;
        CreateUserRequest createUserRequest = (CreateUserRequest) o;
        return Objects.equals(firstName, createUserRequest.firstName) &&
                Objects.equals(lastName, createUserRequest.lastName) &&
                Objects.equals(email, createUserRequest.email) &&
                Objects.equals(password, createUserRequest.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("email", email)
                .add("password", "**********")
                .toString();
    }
}
