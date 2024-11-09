package com.android.flashcardapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        addButton = findViewById(R.id.add_button);
        flashcardList = new ArrayList<>();

        flashcardAdapter = new FlashcardAdapter(flashcardList, new FlashcardAdapter.OnFlashcardClickListener() {
            @Override
            public void onEditClick(int position) {
                // Edit the flashcard (e.g., open an EditFlashcardActivity)
                Toast.makeText(MainActivity.this, "Edit Flashcard: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                // Delete the flashcard
                flashcardList.remove(position);
                flashcardAdapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "Flashcard Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(flashcardAdapter);

        // Show the dialog when the FAB is clicked
        addButton.setOnClickListener(v -> showAddFlashcardDialog());
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
}
