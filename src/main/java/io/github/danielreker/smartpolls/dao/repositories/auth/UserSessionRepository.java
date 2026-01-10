package io.github.danielreker.smartpolls.dao.repositories.auth;

import io.github.danielreker.smartpolls.dao.entities.auth.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository
        extends JpaRepository<UserSessionEntity, Long> {

    Optional<UserSessionEntity> findByTokenId(UUID tokenId);

    void deleteByTokenId(UUID tokenId);

    void deleteAllByUserId(Long userId);

}
