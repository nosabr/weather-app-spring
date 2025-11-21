package org.example.dao;

import org.example.model.Location;
import org.example.model.UserSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class LocationDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public LocationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Location location) {
        getCurrentSession().persist(location);
    }

    public Location findByLocation(Location location) {
        return getCurrentSession().get(Location.class, location);
    }

    public List<Location> findAllLocationsById(long userId){
        Query<Location> query = getCurrentSession().createQuery(
                "from Location where user.id=:userId",Location.class);
        query.setParameter("userId",userId);
        return query.list();
    }

    public boolean isLocationAlreadyAdded(Long userId, BigDecimal latitude, BigDecimal longitude) {
        Query<Long> query = getCurrentSession().createQuery(
                "select count(l) from Location l " +
                        "where l.user.id=:userId AND l.latitude = :latitude " +
                        "AND l.longitude = longitude" ,Long.class);
        return query.uniqueResult() > 1;
    }

    public void delete(Location location) {
        getCurrentSession().remove(location);
    }
}
