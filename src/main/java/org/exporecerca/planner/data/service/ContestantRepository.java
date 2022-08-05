package org.exporecerca.planner.data.service;

import java.util.UUID;

import org.exporecerca.planner.data.entity.Contestant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestantRepository extends JpaRepository<Contestant, Integer> {

}