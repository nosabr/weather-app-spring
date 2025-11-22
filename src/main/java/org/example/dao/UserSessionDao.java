package org.example.dao;

import org.example.model.UserSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<UserSession> findById(UUID id) {
        UserSession userSession = getCurrentSession().get(UserSession.class, id);
        if(userSession != null
                && userSession.getExpiresAt().isBefore(Instant.now())) {
            delete(userSession);
            return Optional.empty();
        }
        return Optional.ofNullable(userSession);
    }

    public List<UserSession> findByUserId(Long userId) {
        Query<UserSession> query = getCurrentSession().createQuery(
                "from UserSession where user.id = :userId", UserSession.class);
        query.setParameter("userId", userId);
        return query.list();
    }

    public int deleteAllSessionsByUserId(long userId) {
        MutationQuery query = getCurrentSession().createMutationQuery(
                "delete from UserSession where user.id = :userId");
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }

    public int deleteAllExpiredSessions(){
        MutationQuery query = getCurrentSession().createMutationQuery(
                "delete from UserSession where expiresAt < :now");
        query.setParameter("now", Instant.now());
        return query.executeUpdate();
    }

    public void delete(UserSession userSession) {
        getCurrentSession().remove(userSession);
    }
}
