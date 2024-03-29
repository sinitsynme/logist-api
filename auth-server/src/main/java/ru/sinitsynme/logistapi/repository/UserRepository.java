package ru.sinitsynme.logistapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.Authority;
import ru.sinitsynme.logistapi.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Page<User> findByAuthoritiesIn(Collection<Authority> authority, Pageable pageable);
}
