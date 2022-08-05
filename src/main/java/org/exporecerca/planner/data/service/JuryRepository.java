package org.exporecerca.planner.data.service;

import java.util.UUID;
import org.exporecerca.planner.data.entity.Jury;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JuryRepository extends JpaRepository<Jury, Integer> {

}