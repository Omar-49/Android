package com.example.StoryTeller.ui.saved;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.StoryTeller.Database.Story.SavedStory;
import com.example.StoryTeller.R;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditSavedStoryFragment} factory method to
 * create an instance of this fragment.
 */
public class EditSavedStoryFragment extends Fragment {

    private EditText editTextTitle, editTextStory;
    private SavedViewModel savedViewModel;
    private int storyId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_edit_saved_story, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextStory = view.findViewById(R.id.editTextStory);

        savedViewModel = new ViewModelProvider(requireActivity()).get(SavedViewModel.class);

        if (getArguments() != null) {
            storyId = getArguments().getInt("storyId", -1);
            if (storyId != -1) {
                LiveData<SavedStory> storyLiveData = savedViewModel.getStoryById(storyId);
                Observer<SavedStory> storyObserver = new Observer<SavedStory>() {
                    @Override
                    public void onChanged(SavedStory savedStory) {
                        editTextTitle.setText(savedStory.getTitle());
                        editTextStory.setText(savedStory.getStory());
                        storyLiveData.removeObserver(this);
                    }
                };

                storyLiveData.observe(getViewLifecycleOwner(), storyObserver);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            savedViewModel.deleteById(storyId);
            Log.d("Delete","Deleted");
            NavHostFragment.findNavController(this).popBackStack();
            return true;
        } else if (item.getItemId() == R.id.action_confirm) {
            String title = editTextTitle.getText().toString();
            String story = editTextStory.getText().toString();
            Log.d("Confirm","Confirmed");

            savedViewModel.update(new SavedStory(storyId, title, story));
            NavHostFragment.findNavController(this).popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}