package com.example.StoryTeller.Database.Story;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "saved_stories")
public class SavedStory {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    public String title;
    public String story;

    @Ignore
    public SavedStory(int id, String title, String story) {
        this.id = id;
        this.title = title;
        this.story = story;

    }
    public SavedStory(String title, String story) {
        this.title = title;
        this.story = story;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
