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
import psi.infrastructure.exception.IllegalArgumentAppException;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.text.MessageFormat;
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

    public void setObjectState(ObjectState objectState) {
        validateStateTransition(objectState);
        this.objectState = objectState;
    }

    private void validateStateTransition(ObjectState newObjectState) {
        if (!isStateTransitionAllowed(newObjectState)) {
            throw new IllegalArgumentAppException(getValidationMessage(newObjectState));
        }
    }

    private boolean isStateTransitionAllowed(ObjectState newState) {
        return isStateTransitionAllowed(this.objectState, newState);
    }

    public static boolean isStateTransitionAllowed(ObjectState oldState, ObjectState newState) {
        if (oldState == null) {
            switch (newState) {
                case UNVERIFIED:
                case ACTIVE:
                    return true;
                default:
                    return false;
            }
        } else {
            switch (oldState) {
                case UNVERIFIED:
                    switch (newState) {
                        case VERIFIED:
                        case REMOVED:
                            return true;
                        default:
                            return false;
                    }
                case VERIFIED:
                    switch (newState) {
                        case INACTIVE:
                        case REMOVED:
                            return true;
                        default:
                            return false;
                    }
                case ACTIVE:
                    switch (newState) {
                        case INACTIVE:
                            return true;
                        default:
                            return false;
                    }
                case INACTIVE:
                    switch (newState) {
                        case ACTIVE:
                        case UNVERIFIED:
                        case REMOVED:
                            return true;
                        default:
                            return false;
                    }
                default:
                    return false;
            }
        }
    }

    private String getValidationMessage(ObjectState newObjectState) {
        if (this.objectState != null) {
            return MessageFormat.format("Cannot change state from {0} to {1}", this.objectState, newObjectState);
        }
        return MessageFormat.format("Cannot create object with state {0}", newObjectState);
    }

}
