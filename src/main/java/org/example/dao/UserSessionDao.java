package org.example.dao;

import org.example.model.UserSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional
public class UserSessionDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserSessionDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.openSession();
    }

    public void save(UserSession userSession) {
        getCurrentSession().persist(userSession);
    }

    public Optional<UserSession> findById(Long id) {
        UserSession userSession = getCurrentSession().get(UserSession.class, id);
        if(userSession != null
                && userSession.getExpiresAt().isBefore(Instant.now())) {
            delete(userSession);
            return Optional.empty();
        }
        return Optional.ofNullable(userSession);
    }

    public void delete(UserSession userSession) {
        getCurrentSession().remove(userSession);
    }
}
