package com.pvelychko.store.jdbc;

import com.google.common.collect.ImmutableList;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.criterion.Criterion;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.pvelychko.utils.Functions.optional;

public abstract class DAO<T> extends AbstractDAO<T> {

    protected DAO(SessionFactory factory) {
        super(factory);
    }

    public T create(T entity) {
        return persist(entity);
    }

    public T edit(T entity) {
        return persist(entity);
    }

    public void remove(@CheckForNull T entity) {
        if (null != entity) {
            currentSession().delete(entity);
        }
    }

    public Optional<T> findById(@CheckForNull Serializable id) {
        if (null == id) {
            return Optional.empty();
        }

        return optional(get(id));
    }

    public List<T> findAll() {
        return findAll((Integer) null, null);
    }

    public List<T> findAll(Integer first, Integer max) {
        final Criteria c = criteria();

        if (null != first) {
            c.setFirstResult(first);
        }
        if (null != max) {
            c.setMaxResults(max);
        }

        return list(c);
    }

    public List<T> findAll(Criterion criterion, Criterion... criterions) {
        return findAll(ImmutableList.<Criterion>builder()
                .add(criterion)
                .add(criterions)
                .build());
    }

    public List<T> findAll(ImmutableList<Criterion> criterions) {
        final Criteria c = criteria();

        criterions.stream().forEach(c::add);

        return list(c);
    }

    protected Criteria criteria(String alias) {
        return currentSession().createCriteria(getEntityClass(), alias);
    }

    Optional<T> opt(Criteria c) {
        return optional(uniqueResult(checkNotNull(c)));
    }

    Optional<T> opt(Query q) {
        return optional(uniqueResult(checkNotNull(q)));
    }

    public void flush() {
        currentSession().flush();
    }
}
