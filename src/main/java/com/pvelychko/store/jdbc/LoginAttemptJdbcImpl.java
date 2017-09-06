package com.pvelychko.store.jdbc;

import com.pvelychko.model.LoginAttempt;
import com.pvelychko.model.User;
import com.pvelychko.store.LoginAttemptStore;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import java.util.List;

import static org.hibernate.criterion.Restrictions.eq;

public class LoginAttemptJdbcImpl extends AbstractDAO<LoginAttempt> implements LoginAttemptStore {

    public LoginAttemptJdbcImpl(SessionFactory factory) {
        super(factory);
    }

    @Override
    public List<LoginAttempt> findLoginAttempts(User user, Integer first, Integer limit) {
        final Criteria c = criteria();
        c.addOrder(Order.desc("date"));
        if (null != user) {
            c.add(eq("user", user));
        }
        if (null != first) {
            c.setFirstResult(first);
        }
        if (null != limit) {
            c.setMaxResults(limit);
        }
        return list(c);
    }
}
