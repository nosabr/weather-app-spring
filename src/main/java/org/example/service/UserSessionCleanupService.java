package org.example.service;

import org.example.dao.UserSessionDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSessionCleanupService {
    private final UserSessionDao userSessionDao;

    public UserSessionCleanupService(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanup() {
        int deleted = userSessionDao.deleteAllExpiredSessions();
        System.out.println("Deleted "+deleted+" expired sessions");
    }
}
