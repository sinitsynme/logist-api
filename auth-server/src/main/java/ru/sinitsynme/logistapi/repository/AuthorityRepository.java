package ru.sinitsynme.logistapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
