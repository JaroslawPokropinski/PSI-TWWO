package psi.api.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDTO {

    private Status status;

    public enum Status {
        UNVERIFIED,
        VERIFIED,
        ACTIVE,
        INACTIVE,
    }

}
