package com.example.demo.repository;

import com.example.demo.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    @Query("SELECT COUNT(a) > 0 FROM Attempt a WHERE a.quiz.id = :quizId")
    boolean existsByAttempts(@Param("quizId") Integer quizId);
}