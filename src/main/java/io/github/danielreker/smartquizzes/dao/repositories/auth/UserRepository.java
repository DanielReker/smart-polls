package io.github.danielreker.smartquizzes.dao.repositories.auth;

import io.github.danielreker.smartquizzes.dao.entities.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository
        extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByLogin(String login);

}
