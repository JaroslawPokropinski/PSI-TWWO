package psi.domain.auditedobject.boundary;

import lombok.experimental.UtilityClass;
import psi.domain.auditedobject.entity.AuditedObject_;
import psi.domain.user.entity.User_;

import java.util.Map;

@UtilityClass
public class AuditedObjectRSQLMapping {

    private static final String CREATED_BY = "created-by";
    private static final String CREATED_AT = "created-at";
    private static final String LAST_UPDATED_BY = "last-updated-by";
    private static final String LAST_UPDATED_AT = "last-updated-at";

    public static final Map<String, String> RSQL_TO_JPA_ATTRIBUTES_MAPPING = Map.of(
            CREATED_BY, AuditedObject_.CREATED_BY + "." + User_.USERNAME,
            CREATED_AT, AuditedObject_.CREATED_AT,
            LAST_UPDATED_BY, AuditedObject_.UPDATED_BY + "." + User_.USERNAME,
            LAST_UPDATED_AT, AuditedObject_.UPDATED_AT);

}
