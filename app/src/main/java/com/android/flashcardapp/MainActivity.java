package com.android.flashcardapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.flashcardapp.models.Flashcard;
import com.android.flashcardapp.models.FlashcardAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FlashcardAdapter flashcardAdapter;
    private List<Flashcard> flashcardList;
    private FloatingActionButton addButton;
    private Button viewAllButton;
    private FirebaseFirestore db;
    private CollectionReference flashcardCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view);
        addButton = findViewById(R.id.add_button);
        viewAllButton = findViewById(R.id.view_flashcard_button);
        flashcardList = new ArrayList<>();

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
        flashcardCollection = db.collection("flashcards");

        flashcardAdapter = new FlashcardAdapter(flashcardList, new FlashcardAdapter.OnFlashcardClickListener() {
            @Override
            public void onEditClick(int position) {
                showEditFlashcardDialog(position);
            }

            @Override
            public void onDeleteClick(int position) {
                flashcardList.remove(position);
                flashcardAdapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "Flashcard Deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFlashcardClick(int position) {
                showEditFlashcardDialog(position);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(flashcardAdapter);

        // Add new flashcard
        addButton.setOnClickListener(v -> showAddFlashcardDialog());

        // View all flashcards
        viewAllButton.setOnClickListener(v -> {
            if (!flashcardList.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, FlashcardViewActivity.class);
                intent.putExtra("flashcards", new ArrayList<>(flashcardList)); // Pass the list
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "No flashcards to view", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddFlashcardDialog() {
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_flashcard, null);
        EditText questionEditText = dialogView.findViewById(R.id.edit_question);
        EditText answerEditText = dialogView.findViewById(R.id.edit_answer);

        // Show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add Flashcard")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String question = questionEditText.getText().toString().trim();
                    String answer = answerEditText.getText().toString().trim();

                    if (!question.isEmpty() && !answer.isEmpty()) {
                        // Create a new flashcard
                        Flashcard newFlashcard = new Flashcard(question, answer);
                        flashcardList.add(newFlashcard);

                        // Save to Firebase Firestore
                        saveFlashcardToFirebase(newFlashcard);

                        // Update RecyclerView
                        flashcardAdapter.notifyItemInserted(flashcardList.size() - 1);

                        Toast.makeText(MainActivity.this, "Flashcard Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void saveFlashcardToFirebase(Flashcard flashcard) {
        // Add the flashcard to Firestore
        flashcardCollection.add(flashcard)
                .addOnSuccessListener(documentReference -> {
                    // Document was successfully added to Firestore
                    Toast.makeText(MainActivity.this, "Flashcard saved to Firebase", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error occurred while saving the flashcard
                    Toast.makeText(MainActivity.this, "Error saving flashcard: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void showEditFlashcardDialog(int position) {
        Flashcard flashcard = flashcardList.get(position);

        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_flashcard, null);
        EditText questionEditText = dialogView.findViewById(R.id.edit_question);
        EditText answerEditText = dialogView.findViewById(R.id.edit_answer);

        questionEditText.setText(flashcard.getQuestion());
        answerEditText.setText(flashcard.getAnswer());

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Edit Flashcard")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String question = questionEditText.getText().toString().trim();
                    String answer = answerEditText.getText().toString().trim();

                    if (!question.isEmpty() && !answer.isEmpty()) {
                        flashcard.setQuestion(question);
                        flashcard.setAnswer(answer);
                        flashcardAdapter.notifyItemChanged(position);
                        Toast.makeText(MainActivity.this, "Flashcard Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter both question and answer", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}
