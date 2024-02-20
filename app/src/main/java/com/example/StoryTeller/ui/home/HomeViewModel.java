package com.example.StoryTeller.ui.home;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.StoryTeller.Database.Message.Message;
import com.example.StoryTeller.Database.Message.MessageRepository;
import com.example.StoryTeller.Database.Story.SavedStory;
import com.example.StoryTeller.Database.Story.StoryRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeViewModel extends AndroidViewModel {
    private MessageRepository mRepository;
    private StoryRepository sRepository;
    private LiveData<List<Message>> mAllMessages;
    private String currentStory = "";
    private int storyPart = 0;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    Application application;
    public HomeViewModel(Application application) {
        super(application);
        this.application = application;
        mRepository = new MessageRepository(application);
        sRepository = new StoryRepository(application);
        mAllMessages = mRepository.getAllMessages();

        LiveData<List<Message>> botMessagesLiveData = mRepository.getMessagesBySender(Message.SENT_BY_BOT);
        Observer<List<Message>> observer = new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                StringBuilder storyBuilder = new StringBuilder();
                for (Message message : messages) {
                    storyBuilder.append(message.getMessage()).append("\n\n");
                }
                currentStory = storyBuilder.toString().trim();
                storyPart = messages.size()+1;
                Log.d("Count",Integer.toString(storyPart));

            }
        };
        botMessagesLiveData.observeForever(observer);

    }


    LiveData<List<Message>> getAllMessages() { return mAllMessages; }

    public void insert(Message message) { mRepository.insert(message); }

    public void deleteAll() { mRepository.deleteAll(); }


    String createStoryPrompt(String userQuestion, int part, String previousStory) {
        String continuationPrompt = "";

        switch (part) {
            case 1:
                // Initial story setup
                continuationPrompt = "Write a story beginning with: " + userQuestion;
                break;
            case 2:
                // Middle of the story
                continuationPrompt = "Continue the story with a twist: " + userQuestion;
                break;
            case 10:
                continuationPrompt = "End the story: " + userQuestion;
                break;
            default:
                // Concluding the story or further parts
                continuationPrompt = "Continue the story: " + userQuestion;
                break;
        }

        // Combine previous story parts with the new prompt
        return previousStory + "\n\n" + continuationPrompt;
    }

    public void deleteAllMessages(){
        ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
        databaseExecutor.execute(() -> {
            mRepository.deleteAll();
        });
    }

    void handleUserChoice(String choice) {
        String modifiedStory = currentStory + "\n\n" + "The user chooses: " + choice;
        callAPI(modifiedStory);
    }

    public void callAPI(String userQuestion){



        String storyPrompt = createStoryPrompt(userQuestion, storyPart, currentStory);
        Log.d("Message",storyPrompt);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo-instruct");
            jsonBody.put("prompt", storyPrompt);
            jsonBody.put("max_tokens", 100); // Adjust as needed
            jsonBody.put("temperature", 0.7); // Adjust for creativity level
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer " +"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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("",e.getMessage());}

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject  jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        insert(new Message(result, Message.SENT_BY_BOT));

                    } catch (JSONException e) {
                        Toast.makeText(application.getApplicationContext(), "Unexpected error.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }


                }else{
                    Toast.makeText(application.getApplicationContext(), "Unexpected error.", Toast.LENGTH_SHORT).show();


            }
            }
        });

    }

    public void saveStory(String title) {
        if(currentStory == ""){
            Toast.makeText(application.getApplicationContext(), "Story cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else{
            SavedStory newSavedStory = new SavedStory(title, currentStory);
            sRepository.insert(newSavedStory);
        }

    }

}
