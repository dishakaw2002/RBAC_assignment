package com.dishakaw.assignment.repository;

import com.dishakaw.assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentUserRepository extends JpaRepository<User, Long> {
    // Find a user by username
    User findByUsername(String username);
}
