package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Attempt attempt;

    @ManyToOne
    private Question question;

    @ManyToOne
    private AnswerOption option;

    public UserAnswer() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Attempt getAttempt() { return attempt; }
    public void setAttempt(Attempt attempt) { this.attempt = attempt; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public AnswerOption getOption() { return option; } // Тот самый геттер
    public void setOption(AnswerOption option) { this.option = option; }
}