package com.example.demo.repository;

import com.example.demo.model.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Integer> {
    // Метод для поиска всех вариантов ответов по ID вопроса
    List<AnswerOption> findByQuestionId(Integer questionId);
}