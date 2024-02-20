package com.example.StoryTeller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.StoryTeller.Database.Story.SavedStory;
import com.example.StoryTeller.R;

import java.util.List;

public class SavedStoryAdapter extends RecyclerView.Adapter<SavedStoryAdapter.ViewHolder> {

    private List<SavedStory> savedStories;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public SavedStoryAdapter(Context context, List<SavedStory> data) {
        this.mInflater = LayoutInflater.from(context);
        this.savedStories = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_saved_story, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextViews in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SavedStory story = savedStories.get(position);
        holder.storyTitle.setText(story.getTitle());
        holder.storyPreview.setText(story.getStory()); // Consider trimming this if it's too long
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return savedStories.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView storyTitle, storyPreview;

        ViewHolder(View itemView) {
            super(itemView);
            storyTitle = itemView.findViewById(R.id.story_title);
            storyPreview = itemView.findViewById(R.id.story_preview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public SavedStory getItem(int id) {
        return savedStories.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setSavedStories(List<SavedStory> savedStories) {
        this.savedStories = savedStories;
        notifyDataSetChanged(); // Refresh the RecyclerView
    }

}
