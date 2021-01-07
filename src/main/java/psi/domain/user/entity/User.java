package psi.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import psi.domain.auditedobject.entity.AuditedObject;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

import static psi.infrastructure.jpa.CacheRegions.USER_ENTITY_CACHE;
import static psi.infrastructure.jpa.CacheRegions.USER_NATURAL_ID_CACHE;
import static psi.infrastructure.jpa.PersistenceConstants.ID_GENERATOR;

@Entity
@Table(name = "USER")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
@Cacheable
@Audited
@Loader(namedQuery = "findUserById")
@NamedQuery(name = "findUserById", query = "SELECT u FROM User u WHERE u.id = ?1 AND u.objectState <> psi.domain.auditedobject.entity.ObjectState.REMOVED")
@Where(clause = AuditedObject.IS_NOT_REMOVED_OBJECT)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = USER_ENTITY_CACHE)
@NaturalIdCache(region = USER_NATURAL_ID_CACHE)
public class User extends AuditedObject {

    @Id
    @GeneratedValue(generator = ID_GENERATOR)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 40)
    private String surname;

    @NotBlank
    @Size(max = 40)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;

    @Email
    @NotBlank
    @Size(max = 40)
    @Column(unique = true)
    private String email;

    @NotBlank
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof User)) {
            return false;
        }

        User otherUser = (User) obj;
        return Objects.equals(otherUser.id, id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
