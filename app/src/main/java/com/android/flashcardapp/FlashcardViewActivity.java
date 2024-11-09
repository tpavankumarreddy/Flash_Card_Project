package com.android.flashcardapp;// Inside FlashcardViewActivity.java

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.appcompat.app.AppCompatActivity;

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
            // Flip animation
            rotateCard(questionTextView, 0f, 180f);
            rotateCard(answerTextView, 180f, 360f);

            // Set visibility after the flip
            questionTextView.setVisibility(View.GONE);
            answerTextView.setVisibility(View.VISIBLE);
            flipToQuestionButton.setVisibility(View.VISIBLE);
            isFlipped = true;
        }
    }

    private void flipCardToQuestion() {
        if (isFlipped) {
            // Flip animation
            rotateCard(answerTextView, 180f, 360f);
            rotateCard(questionTextView, 0f, 180f);

            // Set visibility after the flip
            questionTextView.setVisibility(View.VISIBLE);
            answerTextView.setVisibility(View.GONE);
            flipToQuestionButton.setVisibility(View.GONE);
            isFlipped = false;
        }
    }

    // Helper method for applying rotation animation
    private void rotateCard(View view, float fromDegrees, float toDegrees) {
        RotateAnimation rotate = new RotateAnimation(
                fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(500); // Duration of the flip animation
        rotate.setFillAfter(true); // Keep the view at the end position of the animation
        view.startAnimation(rotate);
    }
}
