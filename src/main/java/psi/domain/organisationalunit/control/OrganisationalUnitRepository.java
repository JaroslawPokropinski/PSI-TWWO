package psi.domain.organisationalunit.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psi.domain.organisationalunit.entity.OrganisationalUnit;

@Repository
public interface OrganisationalUnitRepository extends JpaRepository<OrganisationalUnit, Long> {
}
