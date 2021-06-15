package com.example.savecontact.data_entry;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Contact")
public class UserContact {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "first_name")
    public String first_name;

    @ColumnInfo(name="last_name")
    public String last_name;

    @ColumnInfo(name="email")
    public String email;

    @ColumnInfo(name="compulsory_no")
    public String compulsory_no;

    @ColumnInfo(name="optional_no")
    public String optional_no;

    @ColumnInfo(name="address")
    public String address;

    public UserContact(int id, String first_name, String last_name, String email, String compulsory_no,
                       String optional_no, String address){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email =email;
        this.compulsory_no = compulsory_no;
        this.optional_no = optional_no;
        this.address = address;

    }

    @Ignore
    public UserContact(String first_name, String last_name, String email, String compulsory_no,
                       String optional_no, String address){

        this.first_name = first_name;
        this.last_name = last_name;
        this.email =email;
        this.compulsory_no = compulsory_no;
        this.optional_no = optional_no;
        this.address = address;

    }

}
