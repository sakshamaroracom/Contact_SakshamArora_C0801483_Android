package com.example.savecontact.data_base;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.savecontact.dao_class.ContactInterface;
import com.example.savecontact.data_entry.UserContact;

@Database(entities = UserContact.class, exportSchema = false, version = 1)
public abstract class ContactDatabase extends RoomDatabase {

    private  static  final  String db_name = "contact_db";
    private static ContactDatabase instant;

    public  static synchronized ContactDatabase getInstance(Context mContext)
    {
         if(instant == null)
         {
             instant = Room.databaseBuilder(mContext.getApplicationContext(), ContactDatabase.class, db_name).fallbackToDestructiveMigration().build();
         }

         return instant;
    }

   public abstract ContactInterface contactInterface();
}
