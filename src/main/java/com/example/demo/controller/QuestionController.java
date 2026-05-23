package com.example.demo.controller;

import com.example.demo.model.Question;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizService quizService;

    // CREATE: Добавить новый вопрос
    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        return questionRepository.save(question);
    }

    // READ: Получить все вопросы
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // DELETE: Удалить вопрос по ID
    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Integer id) {
        questionRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Question update(@PathVariable Integer id, @RequestParam String newText) {
        return quizService.updateQuestion(id, newText);
    }
}