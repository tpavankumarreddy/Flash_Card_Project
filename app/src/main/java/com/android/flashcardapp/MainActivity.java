package com.android.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.flashcardapp.models.Flashcard;
import com.android.flashcardapp.models.FlashcardAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FlashcardAdapter flashcardAdapter;
    private List<Flashcard> flashcardList;
    private FloatingActionButton addButton;
    private Button viewAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        addButton = findViewById(R.id.add_button);
        viewAllButton = findViewById(R.id.view_flashcard_button);  // Add a button to view all flashcards
        flashcardList = new ArrayList<>();

        flashcardAdapter = new FlashcardAdapter(flashcardList, new FlashcardAdapter.OnFlashcardClickListener() {
            @Override
            public void onEditClick(int position) {
                // Handle edit click: Show an edit dialog or activity
                showEditFlashcardDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                // Handle delete click: Remove from the list and notify adapter
                flashcardList.remove(position);
                flashcardAdapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "Flashcard Deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFlashcardClick(int position) {
                // Handle flashcard click: Show an edit dialog
                showEditFlashcardDialog(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(flashcardAdapter);

        // Show the dialog when the FAB is clicked
        addButton.setOnClickListener(v -> showAddFlashcardDialog());

        // Inside MainActivity.java

        viewAllButton.setOnClickListener(v -> {
            // Get the first flashcard from the list (or you can choose another way to select the flashcard)
            if (!flashcardList.isEmpty()) {
                Flashcard flashcard = flashcardList.get(0);  // You can modify this to get any flashcard

                Intent intent = new Intent(MainActivity.this, FlashcardViewActivity.class);
                // Pass the flashcard to the next activity
                intent.putExtra("flashcard", flashcard);
                startActivity(intent);
            }
        });

    }

    private void showAddFlashcardDialog() {
        // Create a dialog layout for adding a flashcard
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_flashcard, null);
        EditText questionEditText = dialogView.findViewById(R.id.edit_question);
        EditText answerEditText = dialogView.findViewById(R.id.edit_answer);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add Flashcard")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    // Get the input question and answer
                    String question = questionEditText.getText().toString().trim();
                    String answer = answerEditText.getText().toString().trim();

                    // Check if the question and answer are not empty
                    if (!question.isEmpty() && !answer.isEmpty()) {
                        // Add the new flashcard to the list
                        flashcardList.add(new Flashcard(question, answer));
                        flashcardAdapter.notifyItemInserted(flashcardList.size() - 1);
                        Toast.makeText(MainActivity.this, "Flashcard Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }

    private void showEditFlashcardDialog(int position) {
        // Get the current flashcard details
        Flashcard flashcard = flashcardList.get(position);

        // Create a dialog layout for editing the flashcard
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_flashcard, null);
        EditText questionEditText = dialogView.findViewById(R.id.edit_question);
        EditText answerEditText = dialogView.findViewById(R.id.edit_answer);

        // Pre-fill the EditText fields with the current flashcard details
        questionEditText.setText(flashcard.getQuestion());
        answerEditText.setText(flashcard.getAnswer());

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Edit Flashcard")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    // Get the updated question and answer
                    String question = questionEditText.getText().toString().trim();
                    String answer = answerEditText.getText().toString().trim();

                    // Check if the question and answer are not empty
                    if (!question.isEmpty() && !answer.isEmpty()) {
                        // Update the flashcard in the list
                        flashcard.setQuestion(question);
                        flashcard.setAnswer(answer);
                        flashcardAdapter.notifyItemChanged(position);
                        Toast.makeText(MainActivity.this, "Flashcard Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }
}
