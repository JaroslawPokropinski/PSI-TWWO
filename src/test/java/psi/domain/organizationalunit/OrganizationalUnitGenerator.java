package psi.domain.organizationalunit;

import lombok.RequiredArgsConstructor;
import psi.domain.common.SequenceIdGenerator;
import psi.domain.common.UuidGenerator;
import psi.domain.organisationalunit.entity.OrganisationalUnit;
import psi.domain.organisationalunit.entity.OrganisationalUnitType;

@RequiredArgsConstructor
public class OrganizationalUnitGenerator {

    private final SequenceIdGenerator idGenerator;
    private final UuidGenerator uuidGenerator;

    public OrganisationalUnit generate(OrganisationalUnitType organisationalUnitType) {
        return generateDummyOrganizationalUnit(organisationalUnitType);
    }

    private OrganisationalUnit generateDummyOrganizationalUnit(OrganisationalUnitType organisationalUnitType) {
        return new OrganisationalUnit(idGenerator.generate(), uuidGenerator.generate(), organisationalUnitType);
    }

}
