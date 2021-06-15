package com.example.savecontact.dao_class;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.savecontact.data_entry.UserContact;

import java.util.List;

@Dao
public interface ContactInterface {

    @Query("Select * from contact")
    List<UserContact> getUserContactList();

    @Insert
    void insertUserContact(UserContact userContact);

    @Delete
    void  deleteUserContact(UserContact userContact);

    @Update
    void updateUserContact(UserContact userContact);
}
