package org.exporecerca.planner.data.service;

import java.util.UUID;
import org.exporecerca.planner.data.entity.SamplePerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SamplePersonRepository extends JpaRepository<SamplePerson, UUID> {

}