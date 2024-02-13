package ru.sinitsynme.logistapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sinitsynme.logistapi.entity.UserRefreshToken;

import java.util.UUID;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, UUID> {

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM user_refresh_token WHERE CURRENT_TIMESTAMP > expires_at",
            nativeQuery = true
    )
    void deleteAllBySchedule();
}
