package com.example.demo.controller;

import com.example.demo.model.Quiz;
import com.example.demo.service.QuizService;
import com.example.demo.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizRepository quizRepository;

    // Получение всех тестов
    @GetMapping("/all")
    public List<Quiz> getAll() {
        return quizRepository.findAll();
    }

    // Создание теста
    @PostMapping("/create/{userId}")
    public Quiz createFullQuiz(@PathVariable Integer userId, @RequestBody Quiz quiz) {
        return quizService.createQuizWithContent(userId, quiz);
    }

    // Редактирование теста
    @PutMapping("/{id}")
    public Quiz updateQuiz(@PathVariable Integer id, @RequestBody Quiz quiz) {
        return quizService.updateQuiz(id, quiz);
    }

    // Удаление теста
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        quizService.deleteQuizSafely(id);
    }
}