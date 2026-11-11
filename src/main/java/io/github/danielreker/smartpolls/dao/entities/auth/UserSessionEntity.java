package io.github.danielreker.smartpolls.dao.entities.auth;

import io.github.danielreker.smartpolls.dao.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_session")
@EntityListeners(AuditingEntityListener.class)
public class UserSessionEntity extends BaseEntity {

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(name = "token_id", columnDefinition = "uuid", nullable = false)
    private UUID tokenId;

    @Column(name = "token_hash", columnDefinition = "varchar(255)", nullable = false)
    private String tokenHash;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

}