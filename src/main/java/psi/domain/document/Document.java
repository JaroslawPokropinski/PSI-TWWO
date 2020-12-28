package psi.domain.document;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class Document {

    private final String name;
    private final Resource resource;

}
