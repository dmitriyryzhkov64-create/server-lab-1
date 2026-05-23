package com.example.demo.controller;

import com.example.demo.model.Attempt;
import com.example.demo.repository.AttemptRepository;
import com.example.demo.service.AttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    @Autowired
    private AttemptService attemptService;

    @Autowired
    private AttemptRepository attemptRepository;

    @PostMapping("/start")
    public Attempt start(@RequestParam Integer userId, @RequestParam Integer quizId) {
        return attemptService.startAttempt(userId, quizId);
    }

    @PostMapping("/{attemptId}/answer")
    public void submitAnswer(@PathVariable Integer attemptId,
                             @RequestParam Integer questionId,
                             @RequestParam Integer optionId) {
        attemptService.submitAnswer(attemptId, questionId, optionId);
    }

    @PostMapping("/{attemptId}/finish")
    public Attempt finish(@PathVariable Integer attemptId) {
        return attemptService.finishAttempt(attemptId);
    }

    @GetMapping("/{id}")
    public Attempt getResults(@PathVariable Integer id) {
        return attemptService.getQuizResults(id);
    }

    @GetMapping
    public List<Attempt> getAll() {
        return attemptService.getAllAttempts();
    }

    @GetMapping("/user/{userId}")
    public List<Attempt> getHistory(@PathVariable Integer userId) {
        return attemptService.getUserAttemptHistory(userId);
    }

    @GetMapping("/history/{userId}")
    public List<Attempt> getUserHistory(@PathVariable Integer userId) {
        return attemptRepository.findByUserId(userId);
    }

    @DeleteMapping("/clear-history")
    public void clearHistory() {
        attemptService.deleteAllAttempts();
    }
}