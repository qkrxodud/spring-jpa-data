package study.datajpa.Entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createDateTime = now;
        updateDateTime = now;
    }

    @PreUpdate
    public void preUpdate() {
        updateDateTime = LocalDateTime.now();
    }
}
