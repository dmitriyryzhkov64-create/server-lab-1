package com.example.demo.service;

import com.example.demo.model.Question;
import com.example.demo.model.Quiz;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.QuizRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Transactional
    public Quiz createQuizWithContent(Integer userId, Quiz quiz) {
        var author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        quiz.setAuthor(author);

        if (quiz.getQuestions() != null) {
            quiz.getQuestions().forEach(q -> {
                q.setQuiz(quiz);
                if (q.getOptions() != null) {
                    q.getOptions().forEach(opt -> opt.setQuestion(q));
                }
            });
        }

        return quizRepository.save(quiz);
    }

    @Transactional
    public Quiz updateQuiz(Integer quizId, Quiz updatedQuiz) {
        if (quizRepository.existsByAttempts(quizId)) {
            throw new RuntimeException("Нельзя изменить тест: по нему уже были попытки!");
        }

        Quiz existingQuiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Тест не найден"));

        existingQuiz.setTitle(updatedQuiz.getTitle());

        return quizRepository.save(existingQuiz);
    }

    @Transactional
    public void deleteQuizSafely(Integer quizId) {
        if (quizRepository.existsByAttempts(quizId)) {
            throw new RuntimeException("Нельзя удалить: по тесту уже есть попытки!");
        }
        quizRepository.deleteById(quizId);
    }

    @Transactional
    public Question updateQuestion(Integer questionId, String newText) {
        // Также добавим проверку на попытки здесь для безопасности
        Question question = questionRepository.findById(questionId).orElseThrow();
        if (quizRepository.existsByAttempts(question.getQuiz().getId())) {
            throw new RuntimeException("Нельзя редактировать вопрос: по тесту уже есть попытки!");
        }
        question.setText(newText);
        return questionRepository.save(question);
    }
}