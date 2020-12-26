package psi.api.organisationalunit;

import lombok.AllArgsConstructor;
import lombok.Data;
import psi.domain.organisationalunit.entity.OrganisationalUnitType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class OrganisationalUnitDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private OrganisationalUnitType type;

}
