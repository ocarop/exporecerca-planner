package org.exporecerca.planner.data.service;

import java.util.UUID;

import org.exporecerca.planner.data.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Integer> {

	Topic findByName(String topic);

}