package psi.domain.subjectcard.boundary;

import lombok.experimental.UtilityClass;
import psi.domain.auditedobject.boundary.AuditedObjectRSQLMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class SubjectCardRSQLMapping {

    public static final Map<String, String> RSQL_TO_JPA_ATTRIBUTE_MAPPING = getRsqlToJpaAttributeMapping();
    private static final String ID = "id";
    private static final String SUBJECT_NAME = "subject-name";
    private static final String SUBJECT_NAME_ENG = "subject-name-eng";
    private static final String FIELD_OF_STUDY = "field-of-study";
    private static final String SPECIALIZATION = "specialization";
    private static final String STUDIES_LEVEL = "studies-level";
    private static final String STUDIES_FORM = "studies-form";
    private static final String SUBJECT_TYPE = "subject-type";
    private static final String SUBJECT_CODE = "subject-code";
    private static final String IS_GROUP_OF_COURSES = "is-group-of-courses";
    private static final String ORGANISATIONAL_UNIT = "organisational-unit";
    private static final String SUPERVISOR = "supervisor";
    private static final String EDUCATIONAL_EFFECTS = "educational-effects";

    private static Map<String, String> getRsqlToJpaAttributeMapping() {
//        Map<String, String> subjectCardMapping = Map.of(
//                ID, EducationalEffect_.ID,
//                SUBJECT_NAME, EducationalEffect_.CODE,
//                SUBJECT_NAME_ENG, EducationalEffect_.CODE,
//                FIELD_OF_STUDY, EducationalEffect_.TYPE,
//                SPECIALIZATION, EducationalEffect_.PRK_LEVEL,
//                STUDIES_LEVEL, EducationalEffect_.IS_ENGINEER_EFFECT,
//                STUDIES_FORM, EducationalEffect_.IS_LINGUAL_EFFECT,
//                SUBJECT_TYPE, EducationalEffect_.CATEGORY,
//                SUBJECT_CODE, EducationalEffect_.DESCRIPTION,
//                IS_GROUP_OF_COURSES, EducationalEffect_.DESCRIPTION,
//                ORGANISATIONAL_UNIT, EducationalEffect_.DESCRIPTION,
//                SUPERVISOR, EducationalEffect_.DESCRIPTION,
//                EDUCATIONAL_EFFECTS, EducationalEffect_.DESCRIPTION);
        Map<String, String> mapping = new HashMap<>(AuditedObjectRSQLMapping.RSQL_TO_JPA_ATTRIBUTES_MAPPING);
        return Collections.unmodifiableMap(mapping);
    }

}
