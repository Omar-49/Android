package com.example.StoryTeller.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.example.StoryTeller.R;
import com.example.StoryTeller.ui.home.HomeViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.StoryTeller.databinding.ActivityMainBinding;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        /*
        binding.appBarMain.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_Story, R.id.nav_save)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                invalidateOptionsMenu(); // Trigger the onCreateOptionsMenu to be called
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        int currentDestinationId = navController.getCurrentDestination().getId();

        MenuItem saveItem = menu.findItem(R.id.action_save);
        MenuItem restartItem = menu.findItem(R.id.action_restart);
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        MenuItem confirmItem = menu.findItem(R.id.action_confirm);

        boolean showItems = currentDestinationId == R.id.nav_Story;
        boolean showItems2 = currentDestinationId == R.id.editSavedStoryFragment;


        if(saveItem != null && restartItem != null) {
            saveItem.setVisible(showItems);
            restartItem.setVisible(showItems);
            deleteItem.setVisible(showItems2);
            confirmItem.setVisible(showItems2);
        }

        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_restart) {
            HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            new AlertDialog.Builder(this) // Use 'this' if you're in an Activity
                    .setTitle("Start Over")
                    .setMessage("Are you sure you want to start over? This will delete all current progress.")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // User confirmed to restart
                        Log.d("Delete", "delete");
                        homeViewModel.deleteAllMessages();
                    })
                    .setNegativeButton(android.R.string.no, null) // No action, just dismiss
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;

        } else if (item.getItemId() == R.id.action_save) {
            // Inflate the custom layout
            HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.title_input_dialog, null);
            EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);

            // Create and show the dialog
            new AlertDialog.Builder(this) // Use 'this' if you're in an Activity
                    .setTitle("Enter title for the story")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String title = editTextTitle.getText().toString();
                        if (!title.isEmpty()) {
                            homeViewModel.saveStory(title);
                        } else {
                            Toast.makeText(this, "Title cannot be empty.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}
