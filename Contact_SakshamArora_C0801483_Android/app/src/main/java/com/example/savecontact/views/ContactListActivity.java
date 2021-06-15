package com.example.savecontact.views;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.savecontact.data_entry.UserContact;
import com.example.savecontact.R;
import com.example.savecontact.data_base.ContactDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactListActivity extends AppCompatActivity {

    RecyclerView contactListRecyclerView;
    List<UserContact> userContactList;
    ImageView addContactBtn;
    ContactAdapter contactAdapter;
    ContactDatabase contactDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        contactListRecyclerView = findViewById(R.id.contactListRecyclerView);
        addContactBtn = findViewById(R.id.addContactBtn);
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactListRecyclerView.setHasFixedSize(true);

        addContactBtn.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(),AddUpdateContactActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
         super.onResume();
         userContactList = new ArrayList<>();
         contactDatabase = ContactDatabase.getInstance(this);
      //   new UserContactData().execute();

         userContactData();
    }

    private void userContactData() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            userContactList = contactDatabase.contactInterface().getUserContactList();
            handler.post(() -> {
                //UI Thread work here
                contactAdapter = new ContactAdapter(ContactListActivity.this, userContactList, contactListRecyclerView);
                contactListRecyclerView.setAdapter(contactAdapter);

            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<UserContact> filterList = new ArrayList<>();

        for (UserContact item : userContactList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.first_name.toLowerCase().contains(text.toLowerCase())
            ||item.first_name.toUpperCase().contains(text.toUpperCase())) {
                filterList.add(item);
            }
        }
        if (filterList.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        }
        else {
            contactAdapter.filterList(filterList);
        }
    }
}