package com.example.StoryTeller.ui.saved;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;


import com.example.StoryTeller.Database.Story.SavedStory;
import com.example.StoryTeller.Database.Story.StoryRepository;

import java.util.List;

public class SavedViewModel extends AndroidViewModel {
    private StoryRepository mRepository;
    private LiveData<List<SavedStory>> mAllSavedStories;

    public SavedViewModel(Application application) {
        super(application);

        mRepository = new StoryRepository(application);
        mAllSavedStories = mRepository.getAllSavedStories();
    }

    LiveData<List<SavedStory>> getAllSavedStories() {
        return mAllSavedStories;
    }

    public void insert(SavedStory story) {
        mRepository.insert(story);
    }

    public void update(SavedStory story) {
        mRepository.update(story);
    }


    public void deleteById(int storyId) {
        mRepository.deleteById(storyId);
    }

    public LiveData<SavedStory> getStoryById(int storyId) {
        return mRepository.getStoryById(storyId);
    }

}
