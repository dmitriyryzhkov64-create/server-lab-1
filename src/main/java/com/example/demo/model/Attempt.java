package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "attempts")
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // поле нужно для setUser()

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz; // поле нужно для setQuiz()

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAnswer> userAnswers;

    private Integer result;
    private String status; // поле для статуса "STARTED"
    private Date date;

    // Пустой конструктор (нужен для Spring/Hibernate)
    public Attempt() {}

    // Геттеры и Сеттеры
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Quiz getQuiz() { return quiz; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getResult() { return result; }
    public void setResult(Integer result) { this.result = result; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}