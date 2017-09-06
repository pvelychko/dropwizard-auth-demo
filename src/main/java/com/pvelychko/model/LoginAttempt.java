package com.pvelychko.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;
import com.pvelychko.converter.LocalDateTimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "login_attempt")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginAttempt {
    private Long id;
    private User user;
    private LocalDateTime date;
    private boolean success;

    public LoginAttempt() {
        // Default constructor
    }

    public LoginAttempt(Long id, User user, LocalDateTime date, boolean success) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.success = success;
    }

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "date")
    @Convert(converter = LocalDateTimeConverter.class)
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Column(name = "success")
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static LoginAttempt.LoginAttemptBuilder builder() {
        return new LoginAttempt.LoginAttemptBuilder();
    }

    public static class LoginAttemptBuilder {
        private Long id;
        private User user;
        private LocalDateTime date;
        private boolean success;

        public LoginAttempt.LoginAttemptBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public LoginAttempt.LoginAttemptBuilder setUser(User user) {
            this.user = user;
            return this;
        }

        public LoginAttempt.LoginAttemptBuilder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public LoginAttempt.LoginAttemptBuilder setSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public LoginAttempt build() {
            return new LoginAttempt(id, user, date, success);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginAttempt)) return false;
        LoginAttempt loginAttempt = (LoginAttempt) o;
        return Objects.equals(id, loginAttempt.id) &&
                Objects.equals(user, loginAttempt.user) &&
                Objects.equals(date, loginAttempt.date) &&
                Objects.equals(success, loginAttempt.success);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, date, success);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("user", user)
                .add("date", date)
                .add("success", success)
                .toString();
    }
}
