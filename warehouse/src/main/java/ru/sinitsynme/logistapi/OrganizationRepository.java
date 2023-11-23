package ru.sinitsynme.logistapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.Organization;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends PagingAndSortingRepository<Organization, Long>,
        JpaRepository<Organization, Long> {

    Optional<Organization> findById(Long id);
}
