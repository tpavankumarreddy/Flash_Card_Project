package com.android.flashcardapp.models;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.flashcardapp.R;

import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {

    private List<Flashcard> flashcardList;
    private OnFlashcardClickListener listener;

    public interface OnFlashcardClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
        void onFlashcardClick(int position); // New listener for flashcard view
    }

    public FlashcardAdapter(List<Flashcard> flashcardList, OnFlashcardClickListener listener) {
        this.flashcardList = flashcardList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_item, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        Flashcard flashcard = flashcardList.get(position);
        holder.questionTextView.setText(flashcard.getQuestion());
        holder.itemView.setOnClickListener(v -> listener.onFlashcardClick(position)); // Handle flashcard click
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return flashcardList.size();
    }

    public class FlashcardViewHolder extends RecyclerView.ViewHolder {

        TextView questionTextView;
        TextView deleteButton;

        public FlashcardViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.flashcard_question);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
