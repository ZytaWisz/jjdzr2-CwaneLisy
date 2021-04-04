package com.infoshare.repository;

import com.infoshare.domain.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VolunteerRepository extends JpaRepository<Volunteer, UUID> {

    Optional<Volunteer> getVolunteerByEmail(String email);

}
