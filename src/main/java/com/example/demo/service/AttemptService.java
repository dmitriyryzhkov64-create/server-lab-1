package com.example.demo.service;

import com.example.demo.model.Attempt;
import com.example.demo.model.UserAnswer;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttemptService {

    @Autowired
    private AttemptRepository attemptRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserAnswerRepository userAnswerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    public Attempt startAttempt(Integer userId, Integer quizId) {
        var user = userRepository.findById(userId).orElseThrow();
        var quiz = quizRepository.findById(quizId).orElseThrow();

        Attempt attempt = new Attempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setStatus("STARTED");
        attempt.setResult(0);
        return attemptRepository.save(attempt);
    }

    @Transactional
    public void submitAnswer(Integer attemptId, Integer questionId, Integer optionId) {
        var attempt = attemptRepository.findById(attemptId).orElseThrow();
        var question = questionRepository.findById(questionId).orElseThrow();
        var option = answerOptionRepository.findById(optionId).orElseThrow();

        UserAnswer answer = new UserAnswer();
        answer.setAttempt(attempt);
        answer.setQuestion(question);
        answer.setOption(option);
        userAnswerRepository.save(answer);
    }

    @Transactional
    public Attempt finishAttempt(Integer attemptId) {
        Attempt attempt = attemptRepository.findById(attemptId).orElseThrow();

        List<UserAnswer> answers = userAnswerRepository.findByAttempt(attempt);

        int score = 0;
        for (UserAnswer a : answers) {
            if (a.getOption() != null && Boolean.TRUE.equals(a.getOption().getIsCorrect())) {
                score++;
            }
        }

        attempt.setResult(score);
        attempt.setStatus("FINISHED");
        return attemptRepository.save(attempt);
    }

    @Transactional
    public void deleteAllAttempts() {
        userAnswerRepository.deleteAll();
        attemptRepository.deleteAll();
    }

    public List<Attempt> getAllAttempts() {
        return attemptRepository.findAll();
    }

    public Attempt getQuizResults(Integer id) {
        return attemptRepository.findById(id).orElseThrow();
    }

    public List<Attempt> getUserAttemptHistory(Integer userId) {
        return attemptRepository.findByUserId(userId);
    }
}