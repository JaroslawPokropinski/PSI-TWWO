package psi.domain.organisationalunit.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import psi.domain.organisationalunit.entity.OrganisationalUnit;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrganisationalUnitRepository extends JpaRepository<OrganisationalUnit, Long> {

    List<OrganisationalUnit> findAllByNameIn(Collection<String> names);

    List<OrganisationalUnit> findAllByNameInAndIdNotIn(Collection<String> names, Collection<Long> ids);

}
