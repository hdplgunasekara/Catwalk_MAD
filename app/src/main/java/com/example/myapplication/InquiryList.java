package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adapter.MyAdapter;
import com.example.myapplication.DAO.DAOInquiry;
import com.example.myapplication.Model.Inquiry;
import com.example.myapplication.Session.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class  InquiryList extends AppCompatActivity {

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<Inquiry> inquiryList;
    DatabaseReference database;
    Context context;

    SessionManager session;
    String userNumber;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_list);
        context = getApplicationContext();

        recyclerView = findViewById(R.id.inquiryViewList);
        database = FirebaseDatabase.getInstance().getReference(Inquiry.class.getSimpleName());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        userNumber = session.getUserDetails().get("mobile");
        userType = session.getUserDetails().get("type");

        inquiryList = new ArrayList<>();
        myAdapter = new MyAdapter(this, inquiryList);
        recyclerView.setAdapter(myAdapter);


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading Your inquiries...");
        progressDialog.show();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Inquiry inquiry = dataSnapshot.getValue(Inquiry.class);
                    if (inquiry.getUserNumber() != null && inquiry.getUserNumber().equals(userNumber)) {
                        inquiryList.add(inquiry);
                    }
                }
                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private boolean checkConnectivity() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
            alertBuilder("Please make sure that you have a active internet connection", "Check you internet connectivity");
        }
        return connected;
    }

    private void alertBuilder(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  ;
        builder.setMessage(message)
                .setCancelable(true)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }
}