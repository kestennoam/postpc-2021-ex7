package com.example.postpc_ex7;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.UUID;

public class ReadyOrderActivity extends AppCompatActivity {

    private Database db;
    private SharedPreferences sp;
    private LiveData<Sandwich> sandwichLiveData;
    private Sandwich sandwich;

    private Button gotItButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);

        // set logic components
        db = Database.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        // set sandwich
        LiveData<Sandwich> sandwichLiveData = db.getLiveDataSandwich(sp.getString("sandwichId", "didn't found"));
        sandwich = sandwichLiveData.getValue();

        // ensure not any sandwich on db
        sandwichLiveData.observe(this, sandwich -> {
            if (sandwich == null) {
                Log.d("NewOrderActivity", "sandwich is not in db");
            } else {
                Log.d("NewOrderActivity", "sandwich is in db");
                switch (sandwich.getStatus()) {
                    case "waiting":
                        Log.d("NewOrderActivity", "status is waiting");
                        break;
                    case "in-progress":
                        Log.d("NewOrderActivity", "status is in progress");
                        moveToInProgressOrderActivity();
                        break;
                    case "ready":
                        Log.d("NewOrderActivity", "status is ready");
                        break;
                    case "done":
                        Log.d("NewOrderActivity", "status is done");
                        moveToNewOrderActivity();
                        break;

                }
            }
        });

        gotItButton = findViewById(R.id.gotItButton);

        gotItButton.setOnClickListener(v -> {
            sandwich.setStatus("done");
            moveToNewOrderActivity();
        });

    }


    private void moveToNewOrderActivity() {
        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToEditOrderActivity() {
        Intent intent = new Intent(this, EditOrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToInProgressOrderActivity() {
        Intent intent = new Intent(this, InProgressActivity.class);
        startActivity(intent);
        finish();
    }

    private void moveToReadyOrderActivity() {
        Intent intent = new Intent(this, ReadyOrderActivity.class);
        startActivity(intent);
        finish();
    }

}


