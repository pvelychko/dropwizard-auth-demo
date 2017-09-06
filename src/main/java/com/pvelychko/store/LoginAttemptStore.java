package com.pvelychko.store;

import com.pvelychko.model.LoginAttempt;
import com.pvelychko.model.User;

import java.util.List;

public interface LoginAttemptStore {

    List<LoginAttempt> findLoginAttempts(User user, Integer first, Integer limit);
}
