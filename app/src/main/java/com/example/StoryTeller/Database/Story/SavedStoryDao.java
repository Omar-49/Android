package com.example.StoryTeller.Database.Story;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SavedStoryDao {
    @Insert
    void insert(SavedStory story);

    @Query("SELECT * FROM saved_stories")
    LiveData<List<SavedStory>> getAllStories();

    @Update
    void update(SavedStory story);

    @Query("DELETE FROM saved_stories WHERE id = :storyId")
    void deleteById(int storyId);

    @Query("SELECT * FROM saved_stories WHERE id = :storyId")
    LiveData<SavedStory> getStoryById(int storyId);
}
