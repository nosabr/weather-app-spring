package org.example.dao;

import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public class UserDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public User save(User user) {
        getCurrentSession().persist(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        User user = getCurrentSession().get(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> findByLogin(String login) {
        Query<User> query = getCurrentSession().createQuery("from User where login = :login", User.class);
        query.setParameter("login", login);
        return query.uniqueResultOptional();
    }

    public boolean isUserExist(String login) {
        Query<Long> query = getCurrentSession()
                .createQuery("SELECT COUNT(u) FROM User u WHERE u.login = :login", Long.class);
        query.setParameter("login", login);
        return query.uniqueResult() > 0;
    }

    public void delete(User user) {
        getCurrentSession().remove(user);
    }

}
