package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSession {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at")
    private Instant expiresAt;

    public UserSession(User user, Instant expiresAt) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSession userSession = (UserSession) o;
        return Objects.equals(id, userSession.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", user=" + user +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
