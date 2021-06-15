package com.example.savecontact.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savecontact.data_entry.UserContact;
import com.example.savecontact.R;
import com.example.savecontact.data_base.ContactDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    final Context applicationContext;
    List<UserContact> userContactList;
    final RecyclerView contactListRecyclerView;

    public ContactAdapter(Context applicationContext, List<UserContact> userContactList, RecyclerView contactListRecyclerView)
    {
        this.applicationContext = applicationContext;
        this.userContactList = userContactList;
        this.contactListRecyclerView = contactListRecyclerView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.contact_list, parent, false);
        return new MyViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserContact userContact = userContactList.get(position);
        holder.nameTextView.setText("Name:- "+userContactList.get(position).first_name +" "+userContactList.get(position).last_name);
        holder.emailTextView.setText("Email:- "+userContactList.get(position).email);
        holder.phoneTextView.setText("Phone No:- "+userContactList.get(position).compulsory_no +", "+userContactList.get(position).optional_no);
        holder.addressTextView.setText("Address:- "+userContactList.get(position).address);

        holder.deleteTextView.setOnClickListener(v -> deleteUserData(userContact));
        //new DeleteUser(userContact).execute());

        holder.editTextView.setOnClickListener(v -> openEditDetailsDialog(userContact));

        holder.itemView.setOnLongClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.open_dialog);
            ImageView callImageView =  dialog.findViewById(R.id.callImageView);
            ImageView messageImageView = dialog.findViewById(R.id.messageImageView);
            ImageView emailImageView =  dialog.findViewById(R.id.emailImageView);
            callImageView.setOnClickListener(v13 -> {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL); // Action for what intent called for
                intent.setData(Uri.parse("tel: " + userContactList.get(position).compulsory_no)); // Data with intent respective action on intent
                v13.getContext().startActivity(intent);
                dialog.dismiss();
            });

            messageImageView.setOnClickListener((View v1) -> {
                v1.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", userContactList.get(position).compulsory_no, null)));
                dialog.dismiss();
            });

            emailImageView.setOnClickListener(v12 -> {
                Intent intent = new Intent(Intent.ACTION_SEND);//common intent
                intent.setData(Uri.parse("mailto:"+userContactList.get(position).email));
                v12.getContext().startActivity(intent);
                dialog.dismiss();
            });

            dialog.show();

            return false;
        });
    }



    @Override
    public int getItemCount() {
        return userContactList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        final TextView nameTextView;
        final TextView emailTextView;
        final TextView phoneTextView;
        final TextView addressTextView;
        final TextView editTextView;
        final TextView deleteTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            editTextView = itemView.findViewById(R.id.editTextView);
            deleteTextView = itemView.findViewById(R.id.deleteTextView);
        }


    }

    private void deleteUserData(UserContact userContact) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            ContactDatabase contactDatabase = ContactDatabase.getInstance(applicationContext);
            contactDatabase.contactInterface().deleteUserContact(userContact);
            handler.post(() -> {
                //UI Thread work here
                Toast.makeText(applicationContext,"User Deleted Successfully.",Toast.LENGTH_LONG).show();
                userContactList.remove(userContact);
                ContactAdapter contactAdapter = new ContactAdapter(applicationContext,userContactList, contactListRecyclerView);
                contactListRecyclerView.setAdapter(contactAdapter);
            });
        });
    }


    // method for filtering our recyclerview items.
    public void filterList(ArrayList<UserContact> filterList) {
        userContactList = filterList;
        notifyDataSetChanged();
    }

    private void openEditDetailsDialog(UserContact userContact) {
        Dialog dialog = new Dialog(applicationContext, R.style.Theme_SaveContact);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.update_info_dialog);
        TextView cancelText =  dialog.findViewById(R.id.cancelText);
        TextView updateText =  dialog.findViewById(R.id.updateText);
        EditText firstNameEdit =  dialog.findViewById(R.id.firstNameEdit);
        EditText lastNameEdit =  dialog.findViewById(R.id.lastNameEdit);
        EditText emailNameEdit = dialog.findViewById(R.id.emailNameEdit);
        EditText phoneNameEdit =  dialog.findViewById(R.id.phoneNameEdit);
        EditText phone2NameEdit = dialog.findViewById(R.id.phone2NameEdit);
        EditText addressNameEdit =  dialog.findViewById(R.id.addressNameEdit);

        firstNameEdit.setText(userContact.first_name);
        lastNameEdit.setText(userContact.last_name);
        emailNameEdit.setText(userContact.email);
        phoneNameEdit.setText(userContact.compulsory_no);
        phone2NameEdit.setText(userContact.optional_no);
        addressNameEdit.setText(userContact.address);

        cancelText.setOnClickListener(v -> dialog.dismiss());

        updateText.setOnClickListener(v -> {
            userContact.first_name = firstNameEdit.getText().toString();
            userContact.last_name = lastNameEdit.getText().toString();
            userContact.email = emailNameEdit.getText().toString();
            userContact.compulsory_no = phoneNameEdit.getText().toString();
            userContact.optional_no = phone2NameEdit.getText().toString();
            userContact.address = addressNameEdit.getText().toString();
           // new UpdateUser(userContact).execute();
            userContactUpdate(userContact);
            dialog.dismiss();
        });

        dialog.show();

    }

    private void userContactUpdate(UserContact userContact) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            ContactDatabase contactDatabase = ContactDatabase.getInstance(applicationContext);
            contactDatabase.contactInterface().updateUserContact(userContact);
            handler.post(() -> {
                //UI Thread work here
                Toast.makeText(applicationContext,"User Data Update Successfully.",Toast.LENGTH_LONG).show();
                ContactAdapter contactAdapter = new ContactAdapter(applicationContext,userContactList, contactListRecyclerView);
                contactListRecyclerView.setAdapter(contactAdapter);

            });
        });
    }
}
