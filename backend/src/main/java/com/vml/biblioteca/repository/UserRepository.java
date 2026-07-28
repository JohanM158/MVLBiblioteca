// genera consultas con leer el nombre de metodo (findByemail)
package com.vml.biblioteca.repository;

import com.vml.biblioteca.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByMembershipId(String membershipId);

    List<User> findByActive(Boolean active);

    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    long countByActive(Boolean active);
}
