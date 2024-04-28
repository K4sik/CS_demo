package org.kasarab.cs_demo.repository;

import org.kasarab.cs_demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByFirstname(String firstname);

   Optional<User> findByLastname(String lastname);

   boolean existsByEmail(String email);

   List<User> findByBirthdateBetween(LocalDate dateFrom, LocalDate dateTo);

}
