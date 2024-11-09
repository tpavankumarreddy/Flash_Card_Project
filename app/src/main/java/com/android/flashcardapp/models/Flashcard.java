package com.android.flashcardapp.models;

import java.io.Serializable;

public class Flashcard implements Serializable {
    private String question;
    private String answer;

    // Constructor, getters, and setters
    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
