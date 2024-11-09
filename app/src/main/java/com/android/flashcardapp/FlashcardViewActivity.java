package com.android.flashcardapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.flashcardapp.models.Flashcard;
import com.android.flashcardapp.models.FlashcardAdapter;

import java.util.List;

public class FlashcardViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FlashcardAdapter flashcardAdapter;
    private List<Flashcard> flashcardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_view);

        recyclerView = findViewById(R.id.recycler_view);

        // Receive the flashcards from MainActivity
        flashcardList = getIntent().getParcelableArrayListExtra("flashcards");

        flashcardAdapter = new FlashcardAdapter(flashcardList, new FlashcardAdapter.OnFlashcardClickListener() {
            @Override
            public void onEditClick(int position) {
                // Handle edit click
            }

            @Override
            public void onDeleteClick(int position) {
                // Handle delete click
            }

            @Override
            public void onFlashcardClick(int position) {
                // Handle flashcard click
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(flashcardAdapter);
    }
}
