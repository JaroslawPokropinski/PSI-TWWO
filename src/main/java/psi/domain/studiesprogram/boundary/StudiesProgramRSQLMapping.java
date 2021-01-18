package psi.domain.studiesprogram.boundary;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import psi.domain.auditedobject.boundary.AuditedObjectRSQLMapping;
import psi.domain.discipline.entity.Discipline_;
import psi.domain.fieldofstudy.entity.FieldOfStudy_;
import psi.domain.studiesprogram.entity.StudiesProgram;
import psi.domain.studiesprogram.entity.StudiesProgram_;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class StudiesProgramRSQLMapping {

    public static final Map<String, String> RSQL_TO_JPA_ATTRIBUTE_MAPPING = getRsqlToJpaAttributeMapping();
    private static final String ID = "id";
    private static final String CODE = "code";
    private static final String FIELD_OF_STUDY_ID = "field-of-study-id";
    private static final String FIELD_OF_STUDY_NAME = "field-of-study-name";
    private static final String STUDIES_LEVEL = "studies-level";
    private static final String STUDIES_FORM = "studies-form";
    private static final String STUDIES_PROFILE = "studies-profile";
    private static final String NUMBER_OF_SEMESTERS = "number-of-semesters";
    private static final String TOTAL_NUMBER_OF_ECTS_POINTS = "total-number-of-ects-points";
    private static final String TOTAL_NUMBER_OF_HOURS = "total-number-of-hours";
    private static final String DEGREE_TITLE = "degree-title";
    private static final String LANGUAGE_OF_STUDIES = "language-of-studies";
    private static final String GRADUATE_PROFILE = "graduate-profile";
    private static final String POSSIBILITY_OF_CONTINUING_STUDIES = "possibility-of-continuing-studies";
    private static final String CONNECTION_WITH_MISSION_AND_DEVELOPMENT_STRATEGY = "connection-with-mission-and-development-strategy";
    private static final String IN_EFFECT_SINCE = "in-effect-since";
    private static final String MAIN_DISCIPLINE_ID = "main-discipline-id";
    private static final String MAIN_DISCIPLINE_NAME = "main-discipline-name";
    private static final String DISCIPLINES_ID = "disciplines-id";
    private static final String DISCIPLINES_NAME = "disciplines-name";

    private static Map<String, String> getRsqlToJpaAttributeMapping(){

        Map<String, String> studiesPlanPropertiesMapping = Map.ofEntries(
                Maps.immutableEntry(ID, StudiesProgram_.ID),
                Maps.immutableEntry(CODE, StudiesProgram_.CODE),
                Maps.immutableEntry(FIELD_OF_STUDY_ID, StudiesProgram_.FIELD_OF_STUDY + "." + FieldOfStudy_.ID),
                Maps.immutableEntry(FIELD_OF_STUDY_NAME, StudiesProgram_.FIELD_OF_STUDY + "." + FieldOfStudy_.NAME),
                Maps.immutableEntry(STUDIES_LEVEL, StudiesProgram_.STUDIES_LEVEL),
                Maps.immutableEntry(STUDIES_FORM, StudiesProgram_.STUDIES_FORM),
                Maps.immutableEntry(STUDIES_PROFILE, StudiesProgram_.STUDIES_PROFILE),
                Maps.immutableEntry(NUMBER_OF_SEMESTERS, StudiesProgram_.NUMBER_OF_SEMESTERS),
                Maps.immutableEntry(TOTAL_NUMBER_OF_ECTS_POINTS, StudiesProgram_.TOTAL_NUMBER_OF_ECTS_POINTS),
                Maps.immutableEntry(TOTAL_NUMBER_OF_HOURS, StudiesProgram_.TOTAL_NUMBER_OF_HOURS),
                Maps.immutableEntry(DEGREE_TITLE, StudiesProgram_.DEGREE_TITLE),
                Maps.immutableEntry(LANGUAGE_OF_STUDIES, StudiesProgram_.LANGUAGE_OF_STUDIES),
                Maps.immutableEntry(GRADUATE_PROFILE, StudiesProgram_.GRADUATE_PROFILE),
                Maps.immutableEntry(POSSIBILITY_OF_CONTINUING_STUDIES, StudiesProgram_.POSSIBILITY_OF_CONTINUING_STUDIES),
                Maps.immutableEntry(CONNECTION_WITH_MISSION_AND_DEVELOPMENT_STRATEGY, StudiesProgram_.CONNECTION_WITH_MISSION_AND_DEVELOPMENT_STRATEGY),
                Maps.immutableEntry(IN_EFFECT_SINCE, StudiesProgram_.IN_EFFECT_SINCE),
                Maps.immutableEntry(MAIN_DISCIPLINE_ID, StudiesProgram_.MAIN_DISCIPLINE + "." + Discipline_.ID),
                Maps.immutableEntry(MAIN_DISCIPLINE_NAME, StudiesProgram_.MAIN_DISCIPLINE + "." + Discipline_.NAME),
                Maps.immutableEntry(DISCIPLINES_ID, StudiesProgram_.DISCIPLINES + "." + Discipline_.ID),
                Maps.immutableEntry(DISCIPLINES_NAME, StudiesProgram_.DISCIPLINES + "." + Discipline_.NAME)
        );

        Map<String, String> mapping = new HashMap<>(AuditedObjectRSQLMapping.RSQL_TO_JPA_ATTRIBUTES_MAPPING);
        mapping.putAll(studiesPlanPropertiesMapping);
        return Collections.unmodifiableMap(mapping);
    }

}
