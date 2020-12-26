package psi.domain.educationaleffect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import psi.domain.educationaleffect.entity.EducationalEffect;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
public class EducationalEffectMapping {

    @EmbeddedId
    private Id id;

    //HHH-8714
    @MapsId("mappedEducationalEffectId")
    @ManyToOne
    private EducationalEffect mappedEducationalEffect;

    @MapsId("mappingEducationalEffectId")
    @ManyToOne
    private EducationalEffect mappingEducationalEffect;

    public EducationalEffectMapping(EducationalEffect mappedEducationalEffect, EducationalEffect mappingEducationalEffect) {
        this.id = new Id(mappedEducationalEffect.getId(), mappingEducationalEffect.getId());
        this.mappedEducationalEffect = mappedEducationalEffect;
        this.mappingEducationalEffect = mappingEducationalEffect;
    }

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Id implements Serializable {

        @NotNull
        private Long mappedEducationalEffectId;

        @NotNull
        private Long mappingEducationalEffectId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Id)) return false;

            Id that = (Id) o;

            return Objects.equals(mappedEducationalEffectId, that.mappedEducationalEffectId) && Objects.equals(mappingEducationalEffectId, that.mappingEducationalEffectId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mappedEducationalEffectId, mappingEducationalEffectId);
        }

    }

}
