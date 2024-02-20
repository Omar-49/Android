package com.example.StoryTeller.Database.Message;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    void insert(Message message);

    @Query("DELETE FROM message_table")
    void deleteAll();

    @Query("SELECT * FROM message_table ORDER BY id ASC")
    LiveData<List<Message>> getAllMessages();

    @Query("SELECT * FROM message_table WHERE sentBy = :sentBy")
    LiveData<List<Message>> getMessagesBySender(String sentBy);

}
