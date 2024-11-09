package com.android.flashcardapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.android.flashcardapp.models.Flashcard;
import java.util.Collections;
import java.util.List;

public class FlashcardViewActivity extends AppCompatActivity {

    private TextView questionTextView;
    private TextView answerTextView;
    private Button flipToQuestionButton;
    private Button nextButton;
    private Button shuffleButton;
    private boolean isFlipped = false;
    private Flashcard currentFlashcard;
    private List<Flashcard> flashcards; // List to hold flashcards
    private int currentFlashcardIndex = 0; // Track current flashcard index
    private CardView flashcardCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_view);

        // Initialize views
        questionTextView = findViewById(R.id.question_text);
        answerTextView = findViewById(R.id.answer_text);
        flipToQuestionButton = findViewById(R.id.flip_to_question_button);
        nextButton = findViewById(R.id.next_flashcard_button);
        shuffleButton = findViewById(R.id.shuffle_flashcards_button);
        flashcardCardView = findViewById(R.id.flashcard_card);

        // Get the flashcards list passed from the previous activity
        flashcards = (List<Flashcard>) getIntent().getSerializableExtra("flashcards");

        if (flashcards != null && !flashcards.isEmpty()) {
            // Display the first flashcard
            displayFlashcard(flashcards.get(currentFlashcardIndex));
        } else {
            Toast.makeText(this, "No flashcards available", Toast.LENGTH_SHORT).show();
        }

        // Set OnClickListener on CardView to flip the card
        flashcardCardView.setOnClickListener(v -> {
            if (isFlipped) {
                flipCardToQuestion();
            } else {
                flipCardToAnswer();
            }
        });

        // Set OnClickListener for the "Next" button
        nextButton.setOnClickListener(v -> {
            if (currentFlashcardIndex < flashcards.size() - 1) {
                currentFlashcardIndex++;
                displayFlashcard(flashcards.get(currentFlashcardIndex));
            }
        });

        // Set OnClickListener for the "Shuffle" button
        shuffleButton.setOnClickListener(v -> {
            Collections.shuffle(flashcards); // Shuffle the list
            currentFlashcardIndex = 0; // Reset to the first flashcard
            displayFlashcard(flashcards.get(currentFlashcardIndex)); // Show the first shuffled card
        });

        // Set OnClickListener to flip back to question (button)
        flipToQuestionButton.setOnClickListener(v -> flipCardToQuestion());
    }

    private void displayFlashcard(Flashcard flashcard) {
        currentFlashcard = flashcard;
        questionTextView.setText(flashcard.getQuestion());
        answerTextView.setText(flashcard.getAnswer());
        // Reset visibility when displaying a new flashcard
        questionTextView.setVisibility(View.VISIBLE);
        answerTextView.setVisibility(View.GONE);
        flipToQuestionButton.setVisibility(View.GONE);
        isFlipped = false;
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
