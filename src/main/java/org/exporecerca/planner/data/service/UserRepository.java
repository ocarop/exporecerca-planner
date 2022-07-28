package org.exporecerca.planner.data.service;

import java.util.UUID;
import org.exporecerca.planner.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);
}