package io.github.danielreker.smartquizzes.dao.repositories.auth;

import io.github.danielreker.smartquizzes.dao.entities.auth.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository
        extends JpaRepository<UserSessionEntity, UUID> {

    Optional<UserSessionEntity> findByTokenId(UUID tokenId);

    void deleteByTokenId(UUID tokenId);

    void deleteAllByUserId(UUID userId);

}
