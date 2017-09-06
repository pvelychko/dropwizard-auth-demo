package com.pvelychko.views;

import com.pvelychko.model.LoginAttempt;
import com.pvelychko.model.User;
import io.dropwizard.views.View;

import java.util.List;

public class UserView extends View {
    private final User user;

    private final List<LoginAttempt> recentLoginAttempts;

    public UserView(User user, List<LoginAttempt> recentLoginAttempts) {
        super("/user.mustache");
        this.user = user;
        this.recentLoginAttempts = recentLoginAttempts;
    }

    public User getUser() {
        return user;
    }

    public List<LoginAttempt> getRecentLoginAttempts() {
        return recentLoginAttempts;
    }
}
