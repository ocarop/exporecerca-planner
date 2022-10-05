package org.exporecerca.planner.data.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.exporecerca.planner.data.entity.Jury;
import org.exporecerca.planner.data.entity.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.flow.data.provider.DataProvider;

public interface TimeslotRepository extends JpaRepository<Timeslot, Integer> {


	List<Timeslot> findAllByOrderByStartTime();

}