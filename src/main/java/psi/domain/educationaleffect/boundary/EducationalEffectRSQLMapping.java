package psi.domain.educationaleffect.boundary;

import lombok.experimental.UtilityClass;
import psi.domain.auditedobject.boundary.AuditedObjectRSQLMapping;
import psi.domain.educationaleffect.entity.EducationalEffect_;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class EducationalEffectRSQLMapping {

    public static final Map<String, String> RSQL_TO_JPA_ATTRIBUTE_MAPPING = getRsqlToJpaAttributeMapping();
    private static final String ID = "id";
    private static final String CODE = "code";
    private static final String TYPE = "type";
    private static final String PRK_LEVEL = "prk-level";
    private static final String IS_ENGINEER_EFFECT = "is-engineer-effect";
    private static final String IS_LINGUAL_EFFECT = "is-lingual-effect";
    private static final String CATEGORY = "category";
    private static final String DESCRIPTION = "description";

    private static Map<String, String> getRsqlToJpaAttributeMapping() {
        Map<String, String> educationalEffectsMapping = Map.of(
                ID, EducationalEffect_.ID,
                CODE, EducationalEffect_.CODE,
                TYPE, EducationalEffect_.TYPE,
                PRK_LEVEL, EducationalEffect_.PRK_LEVEL,
                IS_ENGINEER_EFFECT, EducationalEffect_.IS_ENGINEER_EFFECT,
                IS_LINGUAL_EFFECT, EducationalEffect_.IS_LINGUAL_EFFECT,
                CATEGORY, EducationalEffect_.CATEGORY,
                DESCRIPTION, EducationalEffect_.DESCRIPTION);
        Map<String, String> mapping = new HashMap<>();
        mapping.putAll(educationalEffectsMapping);
        mapping.putAll(AuditedObjectRSQLMapping.RSQL_TO_JPA_ATTRIBUTES_MAPPING);
        return Collections.unmodifiableMap(mapping);
    }

}
