package io.github.danielreker.smartpolls.dao.repositories.auth;

import io.github.danielreker.smartpolls.dao.entities.auth.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

}
