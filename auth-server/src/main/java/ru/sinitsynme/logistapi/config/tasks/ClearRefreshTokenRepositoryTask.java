package ru.sinitsynme.logistapi.config.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.sinitsynme.logistapi.repository.UserRefreshTokenRepository;

@Component
public class ClearRefreshTokenRepositoryTask {

    private final UserRefreshTokenRepository repository;
    private static final Logger log = LoggerFactory.getLogger(ClearRefreshTokenRepositoryTask.class);

    @Autowired
    public ClearRefreshTokenRepositoryTask(UserRefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void clearRefreshTokenRepository() {
        repository.deleteAllBySchedule();
        log.info("Daily task of clearing tokens completed");
    }
}
