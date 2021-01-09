package psi.domain.educationaleffect;

import lombok.RequiredArgsConstructor;
import psi.domain.educationaleffect.entity.EducationalEffect;
import psi.domain.educationaleffect.entity.EducationalEffectCategory;
import psi.domain.educationaleffect.entity.EducationalEffectType;
import psi.domain.common.SequenceIdGenerator;
import psi.domain.common.UuidGenerator;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class EducationalEffectGenerator {

    private final SequenceIdGenerator idGenerator;
    private final UuidGenerator uuidGenerator;

    public EducationalEffect generate(EducationalEffectCategory category) {
        return generateDummyEducationalEffect(category);
    }

    public List<EducationalEffect> generate(int count) {
        return generateDummyEducationalEffects(EnumSet.allOf(EducationalEffectCategory.class), count);
    }

    private List<EducationalEffect> generateDummyEducationalEffects(Set<EducationalEffectCategory> categories, int count) {
        return categories.stream()
                .map(category -> generateDummyEducationalEffects(category, count))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Set<EducationalEffect> generateDummyEducationalEffects(EducationalEffectCategory category, int count) {
        return Stream.generate(() -> generateDummyEducationalEffect(category))
                .limit(count)
                .collect(Collectors.toSet());
    }

    private EducationalEffect generateDummyEducationalEffect(EducationalEffectCategory category) {
        return EducationalEffect.builder()
                .id(idGenerator.generate())
                .code(uuidGenerator.generate())
                .type(EducationalEffectType.SUBJECT)
                .prkLevel(4)
                .isEngineerEffect(false)
                .isLingualEffect(false)
                .category(category)
                .description("Dummy description")
                .build();
    }

}
