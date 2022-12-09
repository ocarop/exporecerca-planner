package org.exporecerca.planner.data.service;

import java.util.List;
import java.util.UUID;

import org.exporecerca.planner.data.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

	@Query("SELECT e FROM Evaluation e WHERE e.jury is not null")
	List<Evaluation> findAllAssignedEvaluations();

}