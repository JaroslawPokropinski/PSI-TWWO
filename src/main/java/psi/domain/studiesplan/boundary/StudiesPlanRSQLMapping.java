package psi.domain.studiesplan.boundary;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import psi.domain.auditedobject.boundary.AuditedObjectRSQLMapping;
import psi.domain.studiesplan.entity.StudiesPlan;
import psi.domain.studiesplan.entity.StudiesPlan_;
import psi.domain.studiesprogram.entity.StudiesProgram;
import psi.domain.studiesprogram.entity.StudiesProgram_;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class StudiesPlanRSQLMapping {

    public static final Map<String, String> RSQL_TO_JPA_ATTRIBUTE_MAPPING = getRsqlToJpaAttributeMapping();
    private static final String ID = "id";
    private static final String CODE = "code";
    private static final String STUDIES_PROGRAM_ID = "studies-program-id";
    private static final String STUDIES_PROGRAM_CODE = "studies-program-code";
    private static final String DECREE_DATE = "decree-date";
    private static final String IN_EFFECT_SINCE = "in-effect-since";


    private static Map<String, String> getRsqlToJpaAttributeMapping(){
        Map<String, String> studiesPlanPropertiesMapping = Map.ofEntries(
                Maps.immutableEntry(ID, StudiesPlan_.ID),
                Maps.immutableEntry(CODE, StudiesPlan_.CODE),
                Maps.immutableEntry(STUDIES_PROGRAM_ID, StudiesPlan_.STUDIES_PROGRAM + "." + StudiesProgram_.ID),
                Maps.immutableEntry(STUDIES_PROGRAM_CODE, StudiesPlan_.STUDIES_PROGRAM + "." + StudiesProgram_.CODE),
                Maps.immutableEntry(DECREE_DATE, StudiesPlan_.DECREE_DATE),
                Maps.immutableEntry(IN_EFFECT_SINCE, StudiesPlan_.IN_EFFECT_SINCE)
        );
        Map<String, String> mapping = new HashMap<>(AuditedObjectRSQLMapping.RSQL_TO_JPA_ATTRIBUTES_MAPPING);
        mapping.putAll(studiesPlanPropertiesMapping);
        return Collections.unmodifiableMap(mapping);
    }

}
