package com.example.StoryTeller.Database.Story;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StoryRepository {
    private SavedStoryDao mSavedStoryDao;
    private LiveData<List<SavedStory>> mAllSavedStories;

    public StoryRepository(Application application) {
        StoryDatabase db = StoryDatabase.getDatabase(application);
        mSavedStoryDao = db.savedStoryDao();
        mAllSavedStories = mSavedStoryDao.getAllStories();
    }

    public void insert(SavedStory story) {
        StoryDatabase.databaseWriteExecutor.execute(() -> mSavedStoryDao.insert(story));
    }

    public LiveData<List<SavedStory>> getAllSavedStories() {
        return mAllSavedStories;
    }


    public void deleteById(int storyId) {
        StoryDatabase.databaseWriteExecutor.execute(() -> mSavedStoryDao.deleteById(storyId));
    }

    public void update(SavedStory story) {
        StoryDatabase.databaseWriteExecutor.execute(() -> mSavedStoryDao.update(story));
    }
    public LiveData<SavedStory> getStoryById(int storyId) {
        return mSavedStoryDao.getStoryById(storyId);
    }
}