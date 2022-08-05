package org.exporecerca.planner.data.service;

import java.util.UUID;
import org.exporecerca.planner.data.entity.Jury;
import org.exporecerca.planner.data.entity.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeslotRepository extends JpaRepository<Timeslot, Integer> {

}