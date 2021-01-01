package psi.domain.auditedobject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import psi.domain.user.entity.User;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@Data
@Audited
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuditedObject {

    public static final String IS_NOT_REMOVED_OBJECT = "object_state <> 'REMOVED'";

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    private User createdBy;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    private User updatedBy;

    @Enumerated(EnumType.STRING)
    private ObjectState objectState;

}
