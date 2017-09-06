package com.pvelychko.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;
import com.pvelychko.utils.Crypto;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@JsonInclude(Include.NON_NULL)
public class User implements Principal {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<LoginAttempt> loginAttemptList = new ArrayList<>();

    public User() {
        // Default constructor
    }

    public User(Long id, String firstName, String lastName, String email, String password, List<LoginAttempt> loginAttemptList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = Crypto.encrypt(password);
        this.loginAttemptList = loginAttemptList;
    }

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty
    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotEmpty
    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotEmpty
    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty
    @JsonIgnore
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(targetEntity = LoginAttempt.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    public List<LoginAttempt> getLoginAttemptList() {
        return loginAttemptList;
    }

    public void setLoginAttemptList(List<LoginAttempt> loginAttemptList) {
        this.loginAttemptList = loginAttemptList;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Override
    @JsonIgnore
    @Transient
    public String getName() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("email", email)
                .add("password", password)
                .toString();
    }

    public static class UserBuilder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private List<LoginAttempt> loginAttemptList;

        public UserBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setLoginAttemptList(List<LoginAttempt> loginAttemptList) {
            this.loginAttemptList = loginAttemptList;
            return this;
        }

        public User build() {
            return new User(id, firstName, lastName, email, password, loginAttemptList);
        }
    }
}
