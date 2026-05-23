package com.example.demo.repository;

import com.example.demo.model.Attempt;
import com.example.demo.model.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
    List<UserAnswer> findByAttempt(Attempt attempt);
}