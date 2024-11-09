package com.android.flashcardapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.flashcardapp.models.Flashcard;

public class FlashcardViewActivity extends AppCompatActivity {

    private TextView questionTextView;
    private TextView answerTextView;
    private Button flipToQuestionButton;
    private boolean isFlipped = false;
    private Flashcard currentFlashcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_view);

        questionTextView = findViewById(R.id.question_text);
        answerTextView = findViewById(R.id.answer_text);
        flipToQuestionButton = findViewById(R.id.flip_to_question_button);

        // Get the flashcard passed from MainActivity
        currentFlashcard = (Flashcard) getIntent().getSerializableExtra("flashcard");

        // Display the question initially
        questionTextView.setText(currentFlashcard.getQuestion());
        answerTextView.setText(currentFlashcard.getAnswer());

        // Set an OnClickListener to flip the card
        questionTextView.setOnClickListener(v -> flipCardToAnswer());
        flipToQuestionButton.setOnClickListener(v -> flipCardToQuestion());
    }

    private void flipCardToAnswer() {
        if (!isFlipped) {
            questionTextView.setVisibility(View.GONE);
            answerTextView.setVisibility(View.VISIBLE);
            flipToQuestionButton.setVisibility(View.VISIBLE);
            isFlipped = true;
        }
    }

    private void flipCardToQuestion() {
        if (isFlipped) {
            questionTextView.setVisibility(View.VISIBLE);
            answerTextView.setVisibility(View.GONE);
            flipToQuestionButton.setVisibility(View.GONE);
            isFlipped = false;
        }
    }
}
