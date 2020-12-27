package psi.domain.subjectcard.boundary;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import psi.domain.auditedobject.boundary.AuditedObjectRSQLMapping;
import psi.domain.educationaleffect.entity.EducationalEffect_;
import psi.domain.fieldofstudy.entity.FieldOfStudy_;
import psi.domain.organisationalunit.entity.OrganisationalUnit_;
import psi.domain.subjectcard.entity.SubjectCard_;
import psi.domain.user.entity.User_;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class SubjectCardRSQLMapping {

    public static final Map<String, String> RSQL_TO_JPA_ATTRIBUTE_MAPPING = getRsqlToJpaAttributeMapping();
    private static final String ID = "id";
    private static final String SUBJECT_NAME = "subject-name";
    private static final String SUBJECT_NAME_ENG = "subject-name-eng";
    private static final String FIELD_OF_STUDY_ID = "field-of-study-id";
    private static final String FIELD_OF_STUDY_NAME = "field-of-study-name";
    private static final String SPECIALIZATION = "specialization";
    private static final String STUDIES_LEVEL = "studies-level";
    private static final String STUDIES_FORM = "studies-form";
    private static final String SUBJECT_TYPE = "subject-type";
    private static final String SUBJECT_CODE = "subject-code";
    private static final String IS_GROUP_OF_COURSES = "is-group-of-courses";
    private static final String ORGANISATIONAL_UNIT_ID = "organisational-unit-id";
    private static final String ORGANISATIONAL_UNIT_NAME = "organisational-unit-name";
    private static final String SUPERVISOR_ID = "supervisor-id";
    private static final String SUPERVISOR_NAME = "supervisor-name";
    private static final String SUPERVISOR_SURNAME = "supervisor-surname";
    private static final String EDUCATIONAL_EFFECTS_ID = "educational-effects-id";
    private static final String EDUCATIONAL_EFFECTS_CODE = "educational-effects-code";

    private static Map<String, String> getRsqlToJpaAttributeMapping() {
        Map<String, String> subjectCardPropertiesMapping = Map.ofEntries(
                Maps.immutableEntry(ID, SubjectCard_.ID),
                Maps.immutableEntry(SUBJECT_NAME, SubjectCard_.SUBJECT_NAME),
                Maps.immutableEntry(SUBJECT_NAME_ENG, SubjectCard_.SUBJECT_NAME_IN_ENGLISH),
                Maps.immutableEntry(FIELD_OF_STUDY_ID, SubjectCard_.MAIN_FIELD_OF_STUDY + "." + FieldOfStudy_.ID),
                Maps.immutableEntry(FIELD_OF_STUDY_NAME, SubjectCard_.MAIN_FIELD_OF_STUDY + "." + FieldOfStudy_.NAME),
                Maps.immutableEntry(SPECIALIZATION, SubjectCard_.SPECIALIZATION),
                Maps.immutableEntry(STUDIES_LEVEL, SubjectCard_.STUDIES_LEVEL),
                Maps.immutableEntry(STUDIES_FORM, SubjectCard_.STUDIES_FORM),
                Maps.immutableEntry(SUBJECT_TYPE, SubjectCard_.SUBJECT_TYPE),
                Maps.immutableEntry(SUBJECT_CODE, SubjectCard_.SUBJECT_CODE),
                Maps.immutableEntry(IS_GROUP_OF_COURSES, SubjectCard_.IS_GROUP_OF_COURSES),
                Maps.immutableEntry(ORGANISATIONAL_UNIT_ID, SubjectCard_.ORGANISATIONAL_UNIT + "." + OrganisationalUnit_.ID),
                Maps.immutableEntry(ORGANISATIONAL_UNIT_NAME, SubjectCard_.ORGANISATIONAL_UNIT + "." + OrganisationalUnit_.NAME),
                Maps.immutableEntry(SUPERVISOR_ID, SubjectCard_.SUPERVISOR + "." + User_.ID),
                Maps.immutableEntry(SUPERVISOR_NAME, SubjectCard_.SUPERVISOR + "." + User_.NAME),
                Maps.immutableEntry(SUPERVISOR_SURNAME, SubjectCard_.SUPERVISOR + "." + User_.SURNAME),
                Maps.immutableEntry(EDUCATIONAL_EFFECTS_ID, SubjectCard_.EDUCATIONAL_EFFECTS + "." + EducationalEffect_.ID),
                Maps.immutableEntry(EDUCATIONAL_EFFECTS_CODE, SubjectCard_.EDUCATIONAL_EFFECTS + "." + EducationalEffect_.CODE));
        Map<String, String> mapping = new HashMap<>(AuditedObjectRSQLMapping.RSQL_TO_JPA_ATTRIBUTES_MAPPING);
        mapping.putAll(subjectCardPropertiesMapping);
        return Collections.unmodifiableMap(mapping);
    }

}
