package com.example.StoryTeller.Database.Message;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MessageRepository {
    private MessageDao mMessageDao;
    private LiveData<List<Message>> mAllMessages;

    public MessageRepository(Application application) {
        MessageRoomDatabase db = MessageRoomDatabase.getDatabase(application);
        mMessageDao = db.messageDao();
        mAllMessages = mMessageDao.getAllMessages();
    }

    public LiveData<List<Message>> getAllMessages() {
        return mAllMessages;
    }

    public void insert(Message message) {
        MessageRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMessageDao.insert(message);
        });
    }

    public void deleteAll() {
        MessageRoomDatabase.databaseWriteExecutor.execute(() -> {
            mMessageDao.deleteAll();
        });
    }

    public LiveData<List<Message>> getMessagesBySender(String sentBy) {
        return mMessageDao.getMessagesBySender(sentBy);
    }
}
