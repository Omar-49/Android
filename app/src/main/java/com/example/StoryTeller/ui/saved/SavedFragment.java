package com.example.StoryTeller.ui.saved;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.StoryTeller.Adapters.SavedStoryAdapter;
import com.example.StoryTeller.Database.Story.SavedStory;
import com.example.StoryTeller.R;

import java.util.ArrayList;

public class SavedFragment extends Fragment {

    private SavedViewModel mViewModel;

    public static SavedFragment newInstance() {
        return new SavedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(SavedViewModel.class);
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(SavedViewModel.class);


        RecyclerView recyclerView = view.findViewById(R.id.storyrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SavedStoryAdapter adapter = new SavedStoryAdapter(getContext(), new ArrayList<>()); // Initially empty list
        recyclerView.setAdapter(adapter);

        mViewModel.getAllSavedStories().observe(getViewLifecycleOwner(), savedStories -> {
            adapter.setSavedStories(savedStories);
        });

        adapter.setClickListener((v, position) -> {
            SavedStory story = adapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putInt("storyId", story.getId());
            Log.d("storyId",Integer.toString(story.getId()));
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_save_to_editSavedStoryFragment, bundle);
        });
    }


}