package org.exporecerca.planner.data.service;

import java.util.UUID;

import org.exporecerca.planner.data.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

}