package psi.api.revision;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.history.RevisionMetadata;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class RevisionDTO<T> {

    private Integer revisionId;
    private Instant revisionInstant;
    private RevisionMetadata.RevisionType revisionType;
    private T entity;

}
