package com.example.savecontact.views;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import com.example.savecontact.data_entry.UserContact;
import com.example.savecontact.R;
import com.example.savecontact.data_base.ContactDatabase;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AddUpdateContactActivity extends AppCompatActivity {

    TextInputEditText first_name_edit_text, last_name_edit_text,
    email_edit_text, phone_edit_text, phone2_edit_text, address_edit_text;
    Button submitButton;
    ContactDatabase contactDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactDatabase = ContactDatabase.getInstance(this);
        submitButton = findViewById(R.id.submitButton);
        first_name_edit_text = findViewById(R.id.first_name_edit_text);
        last_name_edit_text = findViewById(R.id.last_name_edit_text);
        email_edit_text = findViewById(R.id.email_edit_text);
        phone_edit_text = findViewById(R.id.phone_edit_text);
        phone2_edit_text = findViewById(R.id.phone2_edit_text);
        address_edit_text = findViewById(R.id.address_edit_text);

        submitButton.setOnClickListener(v -> {
            if(Objects.requireNonNull(first_name_edit_text.getText()).toString().equalsIgnoreCase("")||
                    Objects.requireNonNull(last_name_edit_text.getText()).toString().equals("")||
                    Objects.requireNonNull(email_edit_text.getText()).toString().equalsIgnoreCase("")||
                    Objects.requireNonNull(phone_edit_text.getText()).toString().equalsIgnoreCase("")||
                    Objects.requireNonNull(address_edit_text.getText()).toString().equalsIgnoreCase(""))
            {
                Toast.makeText(getApplicationContext(),"Please check your fields.",Toast.LENGTH_LONG).show();
            }

            else
            {
                userContactSave();
            }
        });
    }

    private void userContactSave() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            UserContact userContact = new UserContact(Objects.requireNonNull(first_name_edit_text.getText()).toString(),
                    Objects.requireNonNull(last_name_edit_text.getText()).toString(),
                    Objects.requireNonNull(email_edit_text.getText()).toString(),
                    Objects.requireNonNull(phone_edit_text.getText()).toString(),
                    Objects.requireNonNull(phone2_edit_text.getText()).toString(),
                    Objects.requireNonNull(address_edit_text.getText()).toString());
            contactDatabase.contactInterface().insertUserContact(userContact);
            handler.post(() -> {
                //UI Thread work here
                Toast.makeText(getApplicationContext(),"Your data successfully save.",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), ContactListActivity.class));

            });
        });
    }


}